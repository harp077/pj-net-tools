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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class MrtgData {
    
	private static MrtgData instance;

	private Vector hardwareInfo = new Vector();

	private Hashtable serverInfo = new Hashtable();

	private String mrtgHost;

	private RpcClient client;

	static synchronized MrtgData getInstance() {
		if (instance == null) {
			instance = new MrtgData();
		}
		return instance;
	}

	private MrtgData() {
	}

	void setHost(String mrtgHost) throws IOException {
		this.mrtgHost = mrtgHost;
		reload();
	}

	void reload() throws IOException {
		client = new RpcClient(mrtgHost);
		Hashtable mrtgInfo = client.getMrtgInfo();
		serverInfo = (Hashtable) mrtgInfo.get("serverInfo");
		hardwareInfo = (Vector) mrtgInfo.get("routers");
	}

	String getMrtgHost() {
		return mrtgHost;
	}

	int getRouterCount() {
		return hardwareInfo.size();
	}

	String getRouterHost(int routerIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		return (String) routerInfo.get("host");
	}

	String getRouterDescr(int routerIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		return (String) routerInfo.get("descr");
	}

	String getRouterCommunity(int routerIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		return (String) routerInfo.get("community");
	}

	boolean getRouterActive(int routerIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Boolean active = (Boolean) routerInfo.get("active");
		return active.booleanValue();
	}

	int getLinkCount(int routerIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		return linksInfo.size();
	}

	String getLinkIfDescr(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return (String) linkInfo.get("ifDescr");
	}

	String getLinkIfAlias(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return (String) linkInfo.get("ifAlias");
	}

	String getLinkDescr(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return (String) linkInfo.get("descr");
	}

	int getLinkIfIndex(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return ((Integer) linkInfo.get("ifIndex")).intValue();
	}

	int getLinkSamplingInterval(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return ((Integer) linkInfo.get("samplingInterval")).intValue();
	}

	boolean getLinkActive(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return ((Boolean) linkInfo.get("active")).booleanValue();
	}

	Date getLinkLastSampleTime(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		return (Date) linkInfo.get("lastSampleTime");
	}

	long getLinkIfInOctets(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		String inStr = (String) linkInfo.get("ifInOctets");
		return inStr != null ? Long.parseLong(inStr) : 0;
	}

	long getLinkIfOutOctets(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		String outStr = (String) linkInfo.get("ifOutOctets");
		return outStr != null ? Long.parseLong(outStr) : 0;
	}

	int getLinkSampleCount(int routerIndex, int linkIndex) {
		Hashtable routerInfo = (Hashtable) hardwareInfo.get(routerIndex);
		Vector linksInfo = (Vector) routerInfo.get("links");
		Hashtable linkInfo = (Hashtable) linksInfo.get(linkIndex);
		Integer countStr = (Integer) linkInfo.get("sampleCount");
		return countStr.intValue();
	}

	RouterInfo getRouterInfo(int routerIndex) {
		RouterInfo rInfo = new RouterInfo();
		rInfo.setActive(getRouterActive(routerIndex));
		rInfo.setCommunity(getRouterCommunity(routerIndex));
		rInfo.setDescr(getRouterDescr(routerIndex));
		rInfo.setHost(getRouterHost(routerIndex));
		Vector linkInfo = new Vector();
		for (int linkIndex = 0; linkIndex < getLinkCount(routerIndex); linkIndex++) {
			linkInfo.add(getLinkInfo(routerIndex, linkIndex));
		}
		Collections.sort(linkInfo, LinkInfo.getComparator());
		rInfo.setLinkInfo((LinkInfo[]) linkInfo.toArray(new LinkInfo[0]));
		return rInfo;
	}

	LinkInfo getLinkInfo(int routerIndex, int linkIndex) {
		LinkInfo lInfo = new LinkInfo();
		lInfo.setActive(getLinkActive(routerIndex, linkIndex));
		lInfo.setIfDescr(getLinkIfDescr(routerIndex, linkIndex));
		lInfo.setDescr(getLinkDescr(routerIndex, linkIndex));
		lInfo.setIfAlias(getLinkIfAlias(routerIndex, linkIndex));
		lInfo.setIfIndex(getLinkIfIndex(routerIndex, linkIndex));
		lInfo.setIfInOctets(getLinkIfInOctets(routerIndex, linkIndex));
		lInfo.setIfOutOctets(getLinkIfOutOctets(routerIndex, linkIndex));
		lInfo.setLastSampleTime(getLinkLastSampleTime(routerIndex, linkIndex));
		lInfo.setSamplingInterval(getLinkSamplingInterval(routerIndex,
				linkIndex));
		lInfo.setSampleCount(getLinkSampleCount(routerIndex, linkIndex));
		return lInfo;
	}

	RouterInfo[] getInfo() {
		Vector rInfo = new Vector();
		for (int routerIndex = 0; routerIndex < getRouterCount(); routerIndex++) {
			rInfo.add(getRouterInfo(routerIndex));
		}
		Collections.sort(rInfo, RouterInfo.getComparator());
		return (RouterInfo[]) rInfo.toArray(new RouterInfo[0]);
	}

	ServerInfo getServerInfo() {
		ServerInfo info = new ServerInfo();
		info.setServerHost(mrtgHost);
		info.setBadSavesCount(Integer.parseInt(serverInfo.get("badSavesCount")
				.toString()));
		info.setGoodSavesCount(Integer.parseInt(serverInfo
				.get("goodSavesCount").toString()));
		info.setSavesCount(Integer.parseInt(serverInfo.get("savesCount")
				.toString()));
		info.setSampleCount(Integer.parseInt(serverInfo.get("sampleCount")
				.toString()));
		info.setPoolEfficency(Double.parseDouble(serverInfo
				.get("poolEfficency").toString()));
		info.setStartDate((Date) serverInfo.get("startDate"));
		return info;
	}

	int addRouter(RouterInfo routerInfo) throws IOException {
		int status = client.addRouter(routerInfo);
		reload();
		return status;
	}

	int updateRouter(RouterInfo routerInfo) throws IOException {
		int status = client.updateRouter(routerInfo);
		reload();
		return status;
	}

	int deleteRouter(RouterInfo routerInfo) throws IOException {
		int status = client.deleteRouter(routerInfo);
		reload();
		return status;
	}

	String[] getAvailableLinks(RouterInfo routerInfo) throws IOException {
		Vector links = client.getAvailableLinks(routerInfo);
		Collections.sort(links);
		return (String[]) links.toArray(new String[0]);
	}

	int addLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		int status = client.addLink(routerInfo, linkInfo);
		reload();
		return status;
	}

	int updateLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		int status = client.updateLink(routerInfo, linkInfo);
		reload();
		return status;
	}

	int removeLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		int status = client.removeLink(routerInfo, linkInfo);
		reload();
		return status;
	}

	byte[] getPngGraph(RouterInfo routerInfo, LinkInfo linkInfo, Date start,
			Date stop) throws IOException {
		return client.getPngGraph(routerInfo, linkInfo, start, stop);
	}

	TreeModel getTreeModel() {
		ServerInfo serverInfo = getServerInfo();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(serverInfo);
		RouterInfo[] routerInfo = getInfo();
		for (int routerIndex = 0; routerIndex < routerInfo.length; routerIndex++) {
			DefaultMutableTreeNode routerNode = new DefaultMutableTreeNode(
					routerInfo[routerIndex]);
			root.add(routerNode);
			LinkInfo[] linkInfo = routerInfo[routerIndex].getLinkInfo();
			for (int linkIndex = 0; linkIndex < linkInfo.length; linkIndex++) {
				DefaultMutableTreeNode linkNode = new DefaultMutableTreeNode(
						linkInfo[linkIndex]);
				routerNode.add(linkNode);
			}
		}
		return new DefaultTreeModel(root);
	}

}
