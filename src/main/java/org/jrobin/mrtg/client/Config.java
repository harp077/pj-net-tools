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
import java.io.File;

public class Config implements MrtgConstants {
	// various paths
	private static final String DELIM = System.getProperty("file.separator");

	private static final String CONF_DIR = "mrtg/" + "conf" + DELIM;

	private static final String RRD_DIR = "mrtg/" + "rrd" + DELIM;

	private static final String HARDWARE_FILE = CONF_DIR + "mrtg.dat";

	private static final String RRD_DEF_TEMPLATE_FILE = CONF_DIR
			+ "rrd_template.xml";

	private static final String RRD_GRAPH_DEF_TEMPLATE_FILE = CONF_DIR
			+ "graph_template.xml";

	static {
		// create directories if not found
		new File(CONF_DIR).mkdirs();
		new File(RRD_DIR).mkdirs();
	}

	static String getConfDir() {
		return CONF_DIR;
	}

	static String getRrdDir() {
		return RRD_DIR;
	}

	static String getHardwareFile() {
		return HARDWARE_FILE;
	}

	static String getRrdTemplateFile() {
		return RRD_DEF_TEMPLATE_FILE;
	}

	static String getGraphTemplateFile() {
		return RRD_GRAPH_DEF_TEMPLATE_FILE;
	}
}
