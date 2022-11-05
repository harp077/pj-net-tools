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

import java.util.Comparator;

public class RouterInfo implements TreeElementInfo {
	private String host, descr, community;

	private boolean active;

	private LinkInfo[] linksInfo;

	LinkInfo[] getLinkInfo() {
		return linksInfo;
	}

	void setLinkInfo(LinkInfo[] linksInfo) {
		this.linksInfo = linksInfo;
	}

	String getHost() {
		return host;
	}

	void setHost(String host) {
		this.host = host;
	}

	String getDescr() {
		return descr;
	}

	void setDescr(String descr) {
		this.descr = descr;
	}

	String getCommunity() {
		return community;
	}

	void setCommunity(String community) {
		this.community = community;
	}

	boolean isActive() {
		return active;
	}

	void setActive(boolean active) {
		this.active = active;
	}

	public String toString() {
		return host;
	}

	public String getInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Node address: " + getHost() + "\n");
		buffer.append("SNMP-read Community: " + getCommunity() + "\n");
		//buffer.append("Description: " + getDescr() + "\n");
		buffer.append("Active: " + isActive() + "\n");
		buffer.append("Sampled links: " + getLinkInfo().length + "\n");
		return buffer.toString();
	}

	static Comparator getComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				RouterInfo router1 = (RouterInfo) o1;
				RouterInfo router2 = (RouterInfo) o2;
				return router1.getHost().compareToIgnoreCase(router2.getHost());
			}
		};
	}

	public boolean equals(Object obj) {
		if (obj instanceof RouterInfo) {
			Comparator c = getComparator();
			return c.compare(this, obj) == 0;
		}
		return false;
	}

	String[] getInterfaces() {
		String[] interfaces = new String[linksInfo.length];
		for (int i = 0; i < linksInfo.length; i++) {
			interfaces[i] = linksInfo[i].getIfDescr();
		}
		return interfaces;
	}

}
