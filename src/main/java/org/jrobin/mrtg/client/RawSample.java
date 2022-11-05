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

public class RawSample {
	private String host;

	private String ifDescr = "";

	private boolean valid = true;

	private long timestamp = Util.getTime();

	private long ifInOctets;

	private long ifOutOctets;

	private long sysUpTime;

	private int ifOperStatus;

	String getHost() {
		return host;
	}

	void setHost(String host) {
		this.host = host;
	}

	String getIfDescr() {
		return ifDescr;
	}

	void setIfDescr(String ifDescr) {
		this.ifDescr = ifDescr;
	}

	boolean isValid() {
		return valid;
	}

	void setValid(boolean valid) {
		this.valid = valid;
	}

	long getTimestamp() {
		return timestamp;
	}

	void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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

	long getSysUpTime() {
		return sysUpTime;
	}

	void setSysUpTime(long sysUpTime) {
		this.sysUpTime = sysUpTime;
	}

	int getIfOperStatus() {
		return ifOperStatus;
	}

	void setIfOperStatus(int ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}

	public String toString() {
		return ifDescr + "@" + host + ": timestamp=" + timestamp + " valid="
				+ valid + " ifInOctets=" + ifInOctets + " ifOutOctets="
				+ ifOutOctets + " sysUpTime=" + sysUpTime;
	}
}
