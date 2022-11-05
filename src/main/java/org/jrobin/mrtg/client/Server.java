/* ============================================================
 * JRobin : Pure java implementation of RRDTool's functionality
 * ============================================================
 *
 * Project Info:  http://www.jrobin.org
 * Project Lead:  Sasa Markovic (saxon@jrobin.org);
 *
 * (C) Copyright 2003, by Sasa Markovic.
 *
 * Developers:    Sasa Markovic (saxon@jrobin.org)
 *                Arne Vandamme (cobralord@jrobin.org)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 */
package org.jrobin.mrtg.client;

import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdException;
import org.jrobin.mrtg.MrtgException;
import org.jrobin.mrtg.MrtgConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class Server implements MrtgConstants {
	private static Server instance;

	private DeviceList deviceList;

	private Date startDate;

	private Timer timer;

	private RrdWriter rrdWriter;

	public static Listener listener;

	private boolean active = false;

	public synchronized static Server getInstance() {
		if (instance == null) {
			instance = new Server();
		}
		return instance;
	}

	private Server() {
		RrdDb.setLockMode(RrdDb.NO_LOCKS);
		RrdDbPool.getInstance().setCapacity(POOL_CAPACITY);
	}

	public synchronized void start() throws MrtgException {
		if (active) {
			throw new MrtgException("Cannot start Server, already started");
		}
		// set default backend factory
		try {
			RrdDb.setDefaultFactory(BACKEND_FACTORY_NAME);
		} catch (RrdException e) {
			throw new MrtgException("Inavlide backend factory ("
					+ BACKEND_FACTORY_NAME + ")");
		}
		// create template files
		try {
			createXmlTemplateIfNecessary(Config.getRrdTemplateFile(),
					RRD_TEMPLATE_STR);
			createXmlTemplateIfNecessary(Config.getGraphTemplateFile(),
					GRAPH_TEMPLATE_STR);
		} catch (IOException ioe) {
			throw new MrtgException(ioe);
		}
		// load configuration
		String hwFile = Config.getHardwareFile();
		if (new File(hwFile).exists()) {
			loadHardware();
		} else {
			saveHardware();
		}
		// create threads
		rrdWriter = new RrdWriter();
		timer = new Timer();
		listener = new Listener();
		startDate = new Date();
		active = true;
	}

	private void createXmlTemplateIfNecessary(String filePath,
			String fileContent) throws IOException {
		File file = new File(filePath);
		if (!file.exists()) {
			FileWriter writer = new FileWriter(filePath, false);
			writer.write(fileContent);
			writer.flush();
			writer.close();
		}
	}

	public synchronized void stop() throws MrtgException {
		if (!active) {
			throw new MrtgException("Cannot stop Server, not started");
		}
		rrdWriter.terminate();
		timer.terminate();
		listener.terminate();
		active = false;
		try {
			RrdDbPool.getInstance().reset();
		} catch (IOException e) {
			throw new MrtgException(e);
		}
	}

	void saveHardware() throws MrtgException {
		if (deviceList == null) {
			deviceList = new DeviceList();
		}
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = doc.createElement("mrtg");
			doc.appendChild(root);
			Vector routers = deviceList.getRouters();
			for (int i = 0; i < routers.size(); i++) {
				Device router = (Device) routers.get(i);
				router.appendXml(root);
			}
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer
					.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			DOMSource source = new DOMSource(root);
			FileOutputStream destination = new FileOutputStream(Config
					.getHardwareFile());
			StreamResult result = new StreamResult(destination);
			transformer.transform(source, result);
			destination.close();
		} catch (Exception e) {
			throw new MrtgException(e);
		}
	}

	private void loadHardware() throws MrtgException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(false);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(Config.getHardwareFile()));
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getElementsByTagName("router");
			deviceList = new DeviceList();
			Vector routers = deviceList.getRouters();
			for (int i = 0; i < nodes.getLength(); i++) {
				routers.add(new Device(nodes.item(i)));
			}
		} catch (Exception e) {
			throw new MrtgException(e);
		}
	}

	public String toString() {
		return deviceList.toString();
	}

	synchronized int addRouter(String host, String community, String descr,
			boolean active) throws MrtgException {
		int retCode = deviceList.addRouter(host, community, descr, active);
		if (retCode == 0) {
			saveHardware();
		}
		return retCode;
	}

	synchronized int updateRouter(String host, String community, String descr,
			boolean active) throws MrtgException {
		int retCode = deviceList.updateRouter(host, community, descr, active);
		if (retCode == 0) {
			saveHardware();
		}
		return retCode;
	}

	synchronized int removeRouter(String host) throws MrtgException {
		int retCode = deviceList.removeRouter(host);
		if (retCode == 0) {
			saveHardware();
		}
		return retCode;
	}

	synchronized int addLink(String host, String ifDescr, String descr,
			int samplingInterval, boolean active) throws MrtgException {
		int retCode = deviceList.addLink(host, ifDescr, descr,
				samplingInterval, active);
		if (retCode == 0) {
			saveHardware();
		}
		return retCode;
	}

	synchronized int updateLink(String host, String ifDescr, String descr,
			int samplingInterval, boolean active) throws MrtgException {
		int retCode = deviceList.updateLink(host, ifDescr, descr,
				samplingInterval, active);
		if (retCode == 0) {
			saveHardware();
		}
		return retCode;
	}

	synchronized int removeLink(String host, String ifDescr)
			throws MrtgException {
		int retCode = deviceList.removeLink(host, ifDescr);
		if (retCode == 0) {
			saveHardware();
			if (REMOVE_RRD_FOR_DEACTIVATED_LINK) {
				// remove the underlying RRD file
				String rrdFile = RrdWriter.getRrdFilename(host, ifDescr);
				new File(rrdFile).delete();
			}
		}
		return retCode;
	}

	synchronized byte[] getPngGraph(String host, String ifDescr, long start,
			long stop) throws MrtgException {
		Plotter grapher = new Plotter(host, ifDescr);
		return grapher.getPngGraphBytes(start, stop);
	}

	synchronized Device[] getRouters() {
		return (Device[]) deviceList.getRouters().toArray(new Device[0]);
	}

	String[] getAvailableLinks(String host) throws MrtgException {
		Device router = deviceList.getRouterByHost(host);
		try {
			if (router != null) {
				return router.getAvailableLinks();
			} else {
				return null;
			}
		} catch (IOException e) {
			throw new MrtgException(e);
		}
	}

	DeviceList getDeviceList() {
		return deviceList;
	}

	RrdWriter getRrdWriter() {
		return rrdWriter;
	}

	Date getStartDate() {
		return startDate;
	}

	Hashtable getServerInfo() {
		Hashtable hash = new Hashtable();
		hash.put("sampleCount", new Integer(rrdWriter.getSampleCount()));
		hash.put("savesCount", new Integer(rrdWriter.getSavesCount()));
		hash.put("goodSavesCount", new Integer(rrdWriter.getGoodSavesCount()));
		hash.put("badSavesCount", new Integer(rrdWriter.getBadSavesCount()));
		hash.put("startDate", startDate);
		hash.put("poolEfficency", new Double(RrdDbPool.getInstance()
				.getPoolEfficency()));
		return hash;
	}

	public static void main(String[] args) throws Exception {
		Server s = Server.getInstance();
		s.start();
	}

}
