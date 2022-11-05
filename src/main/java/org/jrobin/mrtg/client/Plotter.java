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

import org.jrobin.core.RrdException;
import org.jrobin.graph.RrdGraph;
import org.jrobin.graph.RrdGraphDefTemplate;
import org.jrobin.graph.RrdGraphDef;
import org.jrobin.mrtg.MrtgConstants;
import org.jrobin.mrtg.MrtgException;

import java.io.IOException;
import java.io.File;
import java.util.Date;

public class Plotter implements MrtgConstants {

	private String ifDescr, host, alias;

	private static RrdGraphDefTemplate rrdGraphDefTemplate = null;

	static {
		try {
			rrdGraphDefTemplate = new RrdGraphDefTemplate(new File(Config
					.getGraphTemplateFile()));
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (RrdException e) {
			// e.printStackTrace();
		}
	}

	Plotter(String host, String ifDescr) throws MrtgException {
		if (rrdGraphDefTemplate == null) {
			throw new MrtgException("Could not load graph XML template");
		}
		this.host = host;
		this.ifDescr = ifDescr;
		this.alias = Server.getInstance().getDeviceList().getRouterByHost(host)
				.getLinkByIfDescr(ifDescr).getIfAlias();
	}

	byte[] getPngGraphBytes(long start, long stop) throws MrtgException {
		RrdGraph graph = getRrdGraph(start, stop);
		try {
			return graph.getPNGBytes(GRAPH_WIDTH, GRAPH_HEIGHT);
		} catch (RrdException e) {
			throw new MrtgException(e);
		} catch (IOException e) {
			throw new MrtgException(e);
		}
	}

	RrdGraph getRrdGraph(long start, long end) throws MrtgException {
		RrdGraphDef rrdGraphDef;
		// only one template parsed, many threads plotting
		synchronized (rrdGraphDefTemplate) {
			rrdGraphDefTemplate.setVariable("start", start);
			rrdGraphDefTemplate.setVariable("end", end);
			rrdGraphDefTemplate.setVariable("interface", ifDescr);
			rrdGraphDefTemplate.setVariable("host", host);
			rrdGraphDefTemplate.setVariable("rrd", RrdWriter.getRrdFilename(
					host, ifDescr));
			rrdGraphDefTemplate.setVariable("alias", alias);
			rrdGraphDefTemplate.setVariable("date_start", new Date(
					start * 1000L).toString());
			rrdGraphDefTemplate.setVariable("date_end", new Date(end * 1000L)
					.toString());
			try {
				rrdGraphDef = rrdGraphDefTemplate.getRrdGraphDef();
			} catch (RrdException e) {
				throw new MrtgException(e);
			}
		}
		RrdGraph graph = new RrdGraph(rrdGraphDef, true); // use pool
		return graph;
	}
}
