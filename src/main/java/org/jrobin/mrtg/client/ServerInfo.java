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

import java.util.Date;

public class ServerInfo implements TreeElementInfo {
	private String serverHost;

	private int sampleCount, savesCount, goodSavesCount, badSavesCount;

	private double poolEfficency;

	private Date startDate;

	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	int getSampleCount() {
		return sampleCount;
	}

	void setSampleCount(int sampleCount) {
		this.sampleCount = sampleCount;
	}

	int getSavesCount() {
		return savesCount;
	}

	void setSavesCount(int savesCount) {
		this.savesCount = savesCount;
	}

	int getGoodSavesCount() {
		return goodSavesCount;
	}

	void setGoodSavesCount(int goodSavesCount) {
		this.goodSavesCount = goodSavesCount;
	}

	int getBadSavesCount() {
		return badSavesCount;
	}

	void setBadSavesCount(int badSavesCount) {
		this.badSavesCount = badSavesCount;
	}

	Date getStartDate() {
		return startDate;
	}

	void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String toString() {
		return getServerHost() + " [right click to reload]";
	}

	public String getInfo() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Server host: " + getServerHost() + "\n");
		buffer.append("Server started on: " + getStartDate() + "\n");
		buffer.append("Samples created: " + getSampleCount() + "\n");
		buffer.append("Total samples processed: " + getSavesCount() + "\n");
		buffer.append("Samples stored OK: " + getGoodSavesCount() + "\n");
		buffer.append("Samples not stored: " + getBadSavesCount() + "\n");
		buffer.append("Pool efficency: " + getPoolEfficency() + "\n");
		return buffer.toString();
	}

	public boolean equals(Object obj) {
		return obj instanceof ServerInfo;
	}

	double getPoolEfficency() {
		return poolEfficency;
	}

	void setPoolEfficency(double poolEfficency) {
		this.poolEfficency = poolEfficency;
	}

}
