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
import java.util.Date;

public class LinkInfo implements TreeElementInfo {
	private String ifDescr, ifAlias, descr;

	private int ifIndex, samplingInterval;

	private boolean active;

	private Date lastSampleTime;

	private long ifInOctets, ifOutOctets;

	private int sampleCount;

	String getIfDescr() {
		return ifDescr;
	}

	void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	String getIfAlias() {
		return ifAlias;
	}

	void setIfAlias(String ifAlias) {
		this.ifAlias = ifAlias;
	}

	String getDescr() {
		return descr;
	}

	void setDescr(String descr) {
		this.descr = descr;
	}

	int getIfIndex() {
		return ifIndex;
	}

	void setIfIndex(int ifIndex) {
		this.ifIndex = ifIndex;
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

	void setActive(boolean active) {
		this.active = active;
	}

	Date getLastSampleTime() {
		return lastSampleTime;
	}

	void setLastSampleTime(Date lastSampleTime) {
		this.lastSampleTime = lastSampleTime;
	}

	long getIfInOctets() {
		return ifInOctets;
	}

	void setIfInOctets(long ifInOctets) {
		this.ifInOctets = ifInOctets;
	}

	long getIfOutOctets() {
		return ifOutOctets;
	}

	void setIfOutOctets(long ifOutOctets) {
		this.ifOutOctets = ifOutOctets;
	}

	int getSampleCount() {
		return sampleCount;
	}

	void setSampleCount(int sampleCount) {
		this.sampleCount = sampleCount;
	}

	public String toString() {
		return ifDescr + " [" + ifIndex + "]";
	}

	public String getInfo() {
		StringBuffer b = new StringBuffer();
		b.append("Interface description: " + getIfDescr() + "\n");
		//b.append("MRTG description: " + getDescr() + "\n");
		b.append("Interface alias: " + getIfAlias() + "\n");
		b.append("Interface index: " + getIfIndex() + "\n");
		b.append("Sampling interval: " + getSamplingInterval() + " sec\n");
		b.append("Active: " + isActive() + "\n\n");
		b.append("Last sample time: " + getLastSampleTime() + "\n");
		b.append("Last sample input octets:  " + getIfInOctets() + "\n");
		b.append("Last sample output octets: " + getIfOutOctets() + "\n");
		b.append("Samples collected: " + getSampleCount() + "\n");
		return b.toString();
	}

	static Comparator getComparator() {
		return new Comparator() {
			public int compare(Object o1, Object o2) {
				LinkInfo link1 = (LinkInfo) o1;
				LinkInfo link2 = (LinkInfo) o2;
				return link1.getIfDescr().compareToIgnoreCase(
						link2.getIfDescr());
			}
		};
	}

	public boolean equals(Object obj) {
		if (obj instanceof LinkInfo) {
			Comparator c = getComparator();
			return c.compare(this, obj) == 0;
		}
		return false;
	}
}
