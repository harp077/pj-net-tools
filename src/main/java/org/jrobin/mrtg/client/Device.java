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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

public class Device {
	private String host = "";

	private String community = "";

	private String descr = "";

	private boolean active = true;

	private Vector links = new Vector();

	Device() {
	}

	Device(Node routerNode) {
		NodeList nodes = routerNode.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			Node firstChild = node.getFirstChild();
			String value = (firstChild != null) ? firstChild.getNodeValue()
					.trim() : null;
			if (name.equals("host")) {
				setHost(value);
			} else if (name.equals("community")) {
				setCommunity(value);
			} else if (name.equals("description")) {
				setDescr(value);
			} else if (name.equals("active")) {
				setActive(new Boolean(node.getFirstChild().getNodeValue()
						.trim()).booleanValue());
			} else if (name.equals("interface")) {
				links.add(new Port(node));
			}
		}
	}

	String getHost() {
		return host;
	}

	void setHost(String host) {
		if (host != null) {
			this.host = host;
		}
	}

	String getCommunity() {
		return community;
	}

	void setCommunity(String community) {
		if (community != null) {
			this.community = community;
		}
	}

	String getDescr() {
		return descr;
	}

	void setDescr(String descr) {
		if (descr != null) {
			this.descr = descr;
		}
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

	Vector getLinks() {
		return links;
	}

	void setLinks(Vector links) {
		this.links = links;
	}

	public String toString() {
		String buff = new String();
		buff += "Node: " + host + " -- " + "community=" + community + ", ";
		buff += "descr=" + descr + ", ";
		buff += "active=" + active + "\n";
		// dump links
		for (int i = 0; i < links.size(); i++) {
			Port link = (Port) links.get(i);
			buff += "  Link: " + link + "\n";
		}
		return buff;
	}

	Port getLinkByIfDescr(String ifDescr) {
		for (int i = 0; i < links.size(); i++) {
			Port link = (Port) links.get(i);
			if (link.getIfDescr().equalsIgnoreCase(ifDescr)) {
				return link;
			}
		}
		return null;
	}

	void addLink(Port link) {
		links.add(link);
	}

	void removeLink(Port link) {
		links.remove(link);
	}

	int getLinkCount() {
		return links.size();
	}

	String[] getAvailableLinks() throws IOException {
		Poller comm = null;
		try {
			comm = new Poller(host, community);
			Map links = comm.walkIfDescr();
			return (String[]) links.values().toArray(new String[0]);
		} finally {
			if (comm != null) {
				comm.close();
			}
		}
	}

	Hashtable getRouterInfo() {
		Hashtable table = new Hashtable();
		table.put("host", host);
		table.put("community", community);
		table.put("descr", descr);
		table.put("active", new Boolean(active));
		// add link info
		Vector linkData = new Vector();
		for (int i = 0; i < links.size(); i++) {
			Port link = (Port) links.get(i);
			linkData.add(link.getLinkInfo());
		}
		table.put("links", linkData);
		return table;
	}

	void appendXml(Element root) {
		Document doc = root.getOwnerDocument();
		Element routerElem = doc.createElement("router");
		root.appendChild(routerElem);
		Element hostElem = doc.createElement("host");
		hostElem.appendChild(doc.createTextNode(host));
		routerElem.appendChild(hostElem);
		Element commElem = doc.createElement("community");
		commElem.appendChild(doc.createTextNode(community));
		routerElem.appendChild(commElem);
		Element descrElem = doc.createElement("description");
		descrElem.appendChild(doc.createTextNode(descr));
		routerElem.appendChild(descrElem);
		Element activeElem = doc.createElement("active");
		activeElem.appendChild(doc.createTextNode("" + active));
		routerElem.appendChild(activeElem);
		for (int i = 0; i < links.size(); i++) {
			Port link = (Port) links.elementAt(i);
			link.appendXml(routerElem);
		}
	}
}