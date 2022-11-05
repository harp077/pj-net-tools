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

import org.jrobin.mrtg.MrtgConstants;

import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.jrobin.mrtg.*;

public class RpcClient implements MrtgConstants {

	private Server s;

	RpcClient(String host) throws IOException {
		s = Server.getInstance();

		try {
			s.start();
		} catch (MrtgException ex) {
		}

	}

	Hashtable getMrtgInfo() throws IOException {
		return (Hashtable) Listener.getMrtgInfo();
	}

	byte[] getPngGraph(RouterInfo routerInfo, LinkInfo linkInfo, Date start,
			Date stop) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(linkInfo.getIfDescr());
		params.add(start);
		params.add(stop);
		return (byte[]) Listener.getPngGraph(routerInfo.getHost(), linkInfo
				.getIfDescr(), start, stop);
	}

	int addRouter(RouterInfo routerInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(routerInfo.getCommunity());
		params.add(routerInfo.getDescr());
		params.add(new Boolean(routerInfo.isActive()));
		return Listener.addRouter(routerInfo.getHost(), routerInfo
				.getCommunity(), routerInfo.getDescr(), new Boolean(routerInfo
				.isActive()).booleanValue());
	}

	int updateRouter(RouterInfo routerInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(routerInfo.getCommunity());
		params.add(routerInfo.getDescr());
		params.add(new Boolean(routerInfo.isActive()));
		return Listener.updateRouter(routerInfo.getHost(), routerInfo
				.getCommunity(), routerInfo.getDescr(), new Boolean(routerInfo
				.isActive()).booleanValue());
	}

	int deleteRouter(RouterInfo routerInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		return Listener.removeRouter(routerInfo.getHost());
	}

	Vector getAvailableLinks(RouterInfo routerInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		Vector result = (Vector) Listener.getAvailableLinks(routerInfo
				.getHost());
		return result;
	}

	int addLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(linkInfo.getIfDescr());
		params.add(linkInfo.getDescr());
		params.add(new Integer(linkInfo.getSamplingInterval()));
		params.add(new Boolean(linkInfo.isActive()));
		return Listener.addLink(routerInfo.getHost(), linkInfo.getIfDescr(),
				linkInfo.getDescr(),
				new Integer(linkInfo.getSamplingInterval()).intValue(),
				new Boolean(linkInfo.isActive()).booleanValue());
	}

	int updateLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(linkInfo.getIfDescr());
		params.add(linkInfo.getDescr());
		params.add(new Integer(linkInfo.getSamplingInterval()));
		params.add(new Boolean(linkInfo.isActive()));
		return Listener.updateLink(routerInfo.getHost(), linkInfo.getIfDescr(),
				linkInfo.getDescr(),
				new Integer(linkInfo.getSamplingInterval()).intValue(),
				new Boolean(linkInfo.isActive()).booleanValue());

	}

	int removeLink(RouterInfo routerInfo, LinkInfo linkInfo) throws IOException {
		Vector params = new Vector();
		params.add(routerInfo.getHost());
		params.add(linkInfo.getIfDescr());
		return Listener.removeLink(routerInfo.getHost(), linkInfo.getIfDescr());
	}
}
