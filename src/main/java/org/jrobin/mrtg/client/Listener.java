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

import org.jrobin.mrtg.Debug;
import org.jrobin.mrtg.MrtgConstants;
import org.jrobin.mrtg.MrtgException;

import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class Listener implements MrtgConstants {

	Listener() {
	}

	void terminate() {

	}

	protected void finalize() {
		terminate();
	}

	public static int addRouter(String host, String community, String descr,
			boolean active) {
		try {
			int status = Server.getInstance().addRouter(host, community, descr,
					active);
			// Debug.print("Router " + host + " added [" + status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static int updateRouter(String host, String community, String descr,
			boolean active) {
		try {
			int status = Server.getInstance().updateRouter(host, community,
					descr, active);
			// Debug.print("Router " + host + " updated [" + status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static int removeRouter(String host) {
		try {
			int status = Server.getInstance().removeRouter(host);
			// Debug.print("Router " + host + " removed [" + status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static int addLink(String host, String ifDescr, String descr,
			int samplingInterval, boolean active) {
		try {
			int status = Server.getInstance().addLink(host, ifDescr, descr,
					samplingInterval, active);
			// Debug.print("Interface " + ifDescr + "@" + host + " added [" +
			// status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static int updateLink(String host, String ifDescr, String descr,
			int samplingInterval, boolean active) {
		try {
			int status = Server.getInstance().updateLink(host, ifDescr, descr,
					samplingInterval, active);
			// Debug.print("Interface " + ifDescr + "@" + host + " updated [" +
			// status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static int removeLink(String host, String ifDescr) {
		try {
			int status = Server.getInstance().removeLink(host, ifDescr);
			// Debug.print("Interface " + ifDescr + "@" + host + " removed [" +
			// status + "]");
			return status;
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
			return -10;
		}
	}

	public static byte[] getPngGraph(String host, String ifDescr,
			Date startDate, Date stopDate) {
		byte[] graph = new byte[0];
		long start = startDate.getTime() / 1000L;
		long stop = stopDate.getTime() / 1000L;
		try {
			graph = Server.getInstance()
					.getPngGraph(host, ifDescr, start, stop);
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
		}
		// Debug.print("Graph for interface " + ifDescr + "@" + host + "
		// generated [" + graph.length + " bytes]");
		return graph;
	}

	public static Vector getAvailableLinks(String host) {
		Vector result = new Vector();
		try {
			String[] links = Server.getInstance().getAvailableLinks(host);
			for (int i = 0; i < links.length; i++) {
				result.add(links[i]);
			}
		} catch (MrtgException e) {
			// Debug.print("Event handler error: " + e);
		}
		// Debug.print(result.size() + " interfaces found on " + host);
		return result;
	}

	public static Vector getRouters() {
		Vector result = new Vector();
		Device[] routers = Server.getInstance().getRouters();
		for (int i = 0; i < routers.length; i++) {
			result.add(routers[i].getRouterInfo());
		}
		// Debug.print("Sending router data [" + result.size() + " routers
		// found]");
		return result;
	}

	public static Hashtable getServerInfo() {
		Hashtable hash = new Hashtable();
		Server server = Server.getInstance();
		hash = server.getServerInfo();
		// Debug.print("Sending MRTG server info");
		return hash;
	}

	public static Hashtable getMrtgInfo() {
		Hashtable mrtgInfo = new Hashtable();
		mrtgInfo.put("serverInfo", getServerInfo());
		mrtgInfo.put("routers", getRouters());
		return mrtgInfo;
	}
}
