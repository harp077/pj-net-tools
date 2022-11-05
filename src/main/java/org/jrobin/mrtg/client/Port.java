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

import org.jrobin.core.Util;
import org.jrobin.mrtg.MrtgException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Date;
import java.util.Hashtable;

public class Port {
	static final int DEFAULT_SAMPLING_INTERVAL = 300;

	private int ifIndex = -1;

	private String ifDescr = "";

	private String ifAlias = "";

	private String descr = "";

	private int samplingInterval = DEFAULT_SAMPLING_INTERVAL;

	private boolean active = true;

	private int sampleCount;

	private long lastSampleTime;

	private RawSample lastSample;

	private boolean sampling;

	Port() {
	}

	Port(Node linkNode) {
		NodeList nodes = linkNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			Node firstChild = node.getFirstChild();
			String value = (firstChild != null) ? firstChild.getNodeValue()
					.trim() : null;
			if (name.equals("ifIndex")) {
				setIfIndex(Integer.parseInt(value));
			} else if (name.equals("ifDescr")) {
				setIfDescr(value);
			} else if (name.equals("ifAlias")) {
				setIfAlias(value);
			} else if (name.equals("description")) {
				setDescr(value);
			} else if (name.equals("samplingInterval")) {
				setSamplingInterval(Integer.parseInt(value));
			} else if (name.equals("active")) {
				setActive(new Boolean(value).booleanValue());
			}
		}
	}

	int getIfIndex() {
		return ifIndex;
	}

	void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
	}

	String getIfDescr() {
		return ifDescr;
	}

	String getIfDescrCore() {
		int index = ifDescr.indexOf("#");
		if (index != -1) {
			return ifDescr.substring(0, index);
		} else {
			return ifDescr;
		}
	}

	void setIfDescr(String ifDescr) {
		if (ifDescr != null) {
			this.ifDescr = ifDescr;
		}
	}

	String getIfAlias() {
		return ifAlias;
	}

	void setIfAlias(String ifAlias) {
		if (ifAlias != null) {
			this.ifAlias = ifAlias;
		}
	}

	int getSamplingInterval() {
		return samplingInterval;
	}

	void setSamplingInterval(int samplingInterval) {
		this.samplingInterval = samplingInterval;
	}

	boolean isActive() {
		return active;
	}

	boolean getActive() {
		return active;
	}

	void setActive(boolean active) {
		this.active = active;
	}

	String getDescr() {
		return descr;
	}

	void setDescr(String descr) {
		if (descr != null) {
			this.descr = descr;
		}
	}

	public String toString() {
		return ifDescr + " [" + ifIndex + "] " + descr + ", " + ifAlias
				+ " (each " + samplingInterval + "sec, active=" + active + ")";
	}

	long getLastSampleTime() {
		return lastSampleTime;
	}

	void setLastSampleTime(long lastSampleTime) {
		this.lastSampleTime = lastSampleTime;
	}

	boolean isSampling() {
		return sampling;
	}

	void setSampling(boolean sampling) {
		this.sampling = sampling;
	}

	boolean isDue() {
		if (lastSampleTime == 0) {
			return true;
		}
		long elapsedTime = Util.getTime() - lastSampleTime;
		return elapsedTime >= samplingInterval;
	}

	void switchToIfIndex(int ifIndex) throws MrtgException {
		this.ifIndex = ifIndex;
		Server.getInstance().saveHardware();
	}

	void deactivate() throws MrtgException {
		this.active = false;
		Server.getInstance().saveHardware();
	}

	void processSample(RawSample sample) throws MrtgException {
		// check if ifDescr match
		if (!getIfDescrCore().equals(sample.getIfDescr())) {
			// something changed on the router
			switchToIfIndex(-1);
			return;
		}
		sample.setIfDescr(ifDescr);
		if (lastSample != null
				&& lastSample.getSysUpTime() >= sample.getSysUpTime()) {
			// sysUpTime decreased
			sample.setValid(false);
		}
		// Debug.print("Saving sample: " + sample);
		Server.getInstance().getRrdWriter().store(sample);
		sampleCount++;
		lastSample = sample;
		lastSampleTime = sample.getTimestamp();
	}

	Hashtable getLinkInfo() {
		Hashtable link = new Hashtable();
		link.put("ifIndex", new Integer(ifIndex));
		link.put("ifDescr", ifDescr);
		link.put("ifAlias", ifAlias);
		link.put("descr", descr);
		link.put("samplingInterval", new Integer(samplingInterval));
		link.put("active", new Boolean(active));
		link.put("sampleCount", new Integer(sampleCount));
		if (lastSample != null) {
			link.put("lastSampleTime", new Date(lastSampleTime * 1000L));
			link.put("ifInOctets", String.valueOf(lastSample.getIfInOctets()));
			link
					.put("ifOutOctets", String.valueOf(lastSample
							.getIfOutOctets()));
			link.put("ifOperStatus", String.valueOf(lastSample
					.getIfOperStatus()));
		}
		return link;
	}

	void appendXml(Element routerElem) {
		Document doc = routerElem.getOwnerDocument();
		Element linkElem = doc.createElement("interface");
		routerElem.appendChild(linkElem);
		Element ifIndexElem = doc.createElement("ifIndex");
		ifIndexElem.appendChild(doc.createTextNode("" + ifIndex));
		linkElem.appendChild(ifIndexElem);
		Element ifDescrElem = doc.createElement("ifDescr");
		ifDescrElem.appendChild(doc.createTextNode(ifDescr));
		linkElem.appendChild(ifDescrElem);
		Element ifAliasElem = doc.createElement("ifAlias");
		ifAliasElem.appendChild(doc.createTextNode(ifAlias));
		linkElem.appendChild(ifAliasElem);
		Element descrElem = doc.createElement("description");
		descrElem.appendChild(doc.createTextNode(descr));
		linkElem.appendChild(descrElem);
		Element samplElem = doc.createElement("samplingInterval");
		samplElem.appendChild(doc.createTextNode("" + samplingInterval));
		linkElem.appendChild(samplElem);
		Element activeElem = doc.createElement("active");
		activeElem.appendChild(doc.createTextNode("" + active));
		linkElem.appendChild(activeElem);
	}
}
