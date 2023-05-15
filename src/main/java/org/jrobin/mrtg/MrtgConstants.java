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

package org.jrobin.mrtg;

public interface MrtgConstants {
	// backend factory to be used
	// WARNING: Change this to "FILE" if you run the Server app
	// on a machine with less RAM. "NIO" consumes a lot of memory
	// but it's fast, very fast.
	String BACKEND_FACTORY_NAME = "NIO";

	// turn debugging on/off
	boolean DEBUG = false;

	// run Scheduler each 5 seconds
	int SCHEDULER_RESOLUTION = 5;

	// pause between poller threads in milliseconds
	int SCHEDULER_DELAY = 20;

	// number of open RRD files held in the pool
	int POOL_CAPACITY = 100;

	// graph dimensions
	int GRAPH_WIDTH = 600, GRAPH_HEIGHT = 224;

	// should we remove a RRD file if link is removed from the client
	boolean REMOVE_RRD_FOR_DEACTIVATED_LINK = true;

	// initial template for RrdDef
	String RRD_TEMPLATE_STR = "<rrd_def>                                 \n"
			+ "    <path>${path}</path>                  \n"
			+ "    <step>300</step>                      \n"
			+ "    <datasource>                          \n"
			+ "        <name>in</name>                   \n"
			+ "        <type>COUNTER</type>              \n"
			+ "        <heartbeat>6000</heartbeat>       \n"
			+ "        <min>U</min>                      \n"
			+ "        <max>U</max>                      \n"
			+ "    </datasource>                         \n"
			+ "    <datasource>                          \n"
			+ "        <name>out</name>                  \n"
			+ "        <type>COUNTER</type>              \n"
			+ "        <heartbeat>6000</heartbeat>       \n"
			+ "        <min>U</min>                      \n"
			+ "        <max>U</max>                      \n"
			+ "    </datasource>                         \n"
			+ "    <archive>                             \n"
			+ "        <cf>AVERAGE</cf>                  \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>1</steps>                  \n"
			+ "        <rows>600</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>AVERAGE</cf>                  \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>6</steps>                  \n"
			+ "        <rows>700</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>AVERAGE</cf>                  \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>24</steps>                 \n"
			+ "        <rows>775</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>AVERAGE</cf>                  \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>288</steps>                \n"
			+ "        <rows>797</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MAX</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>1</steps>                  \n"
			+ "        <rows>600</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MAX</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>6</steps>                  \n"
			+ "        <rows>700</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MAX</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>24</steps>                 \n"
			+ "        <rows>775</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MAX</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>288</steps>                \n"
			+ "        <rows>797</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MIN</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>1</steps>                  \n"
			+ "        <rows>600</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MIN</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>6</steps>                  \n"
			+ "        <rows>700</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MIN</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>24</steps>                 \n"
			+ "        <rows>775</rows>                  \n"
			+ "    </archive>                            \n"
			+ "    <archive>                             \n"
			+ "        <cf>MIN</cf>                      \n"
			+ "        <xff>0.5</xff>                    \n"
			+ "        <steps>288</steps>                \n"
			+ "        <rows>797</rows>                  \n"
			+ "    </archive>                            \n"
			+ "</rrd_def>                                ";

	// initial RrdGraphDef template
	String GRAPH_TEMPLATE_STR = "<rrd_graph_def>                                                          \n"
			+ "    <span>                                                               \n"
			+ "        <start>${start}</start>                                          \n"
			+ "        <end>${end}</end>                                                \n"
			+ "    </span>                                                              \n"
			+ "    <options>                                                            \n"
			+ "        <anti_aliasing>off</anti_aliasing>                               \n"
			+ "        <border>                                                         \n"
			+ "            <color>#FFFFFF</color>                                       \n"
			+ "            <width>0</width>                                             \n"
			+ "        </border>                                                        \n"
			+ "        <title>${interface} at ${host}</title>                           \n"
			+ "        <vertical_label>transfer speed [bits/sec]</vertical_label>       \n"
			+ "    </options>                                                           \n"
			+ "    <datasources>                                                        \n"
			+ "        <def>                                                            \n"
			+ "            <name>in</name>                                              \n"
			+ "            <rrd>${rrd}</rrd>                                            \n"
			+ "            <source>in</source>                                          \n"
			+ "            <cf>AVERAGE</cf>                                             \n"
			+ "        </def>                                                           \n"
			+ "        <def>                                                            \n"
			+ "            <name>out</name>                                             \n"
			+ "            <rrd>${rrd}</rrd>                                            \n"
			+ "            <source>out</source>                                         \n"
			+ "            <cf>AVERAGE</cf>                                             \n"
			+ "        </def>                                                           \n"
			+ "        <def>                                                            \n"
			+ "            <name>in8</name>                                             \n"
			+ "            <rpn>in,8,*</rpn>                                            \n"
			+ "        </def>                                                           \n"
			+ "        <def>                                                            \n"
			+ "            <name>out8</name>                                            \n"
			+ "            <rpn>out,8,*</rpn>                                           \n"
			+ "        </def>                                                           \n"
			+ "    </datasources>                                                       \n"
			+ "    <graph>                                                              \n"
			+ "        <area>                                                           \n"
			+ "            <datasource>out8</datasource>                                \n"
			+ "            <color>#00FF00</color>                                       \n"
			+ "            <legend>: traffic output@l</legend>                          \n"
			+ "        </area>                                                          \n"
			+ "        <line>                                                           \n"
			+ "            <datasource>in8</datasource>                                 \n"
			+ "            <color>#0000FF</color>                                       \n"
			+ "            <legend>: traffic input@l</legend>                           \n"
			+ "        </line>                                                          \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>out8</datasource>                                \n"
			+ "            <cf>AVERAGE</cf>                                             \n"
			+ "            <format>Average output:@7.2 @sbits/s</format>                \n"
			+ "        </gprint>                                                        \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>out8</datasource>                                \n"
			+ "            <cf>MAX</cf>                                                 \n"
			+ "            <format>Maximum output:@7.2 @Sbits/s</format>                \n"
			+ "        </gprint>                                                        \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>out</datasource>                                 \n"
			+ "            <cf>TOTAL</cf>                                               \n"
			+ "            <format>Total output:@7.2 @sbytes@l</format>                 \n"
			+ "            <base>1024</base>                                            \n"
			+ "        </gprint>                                                        \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>in8</datasource>                                 \n"
			+ "            <cf>AVERAGE</cf>                                             \n"
			+ "            <format>Average input: @7.2 @sbits/s</format>                \n"
			+ "        </gprint>                                                        \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>in8</datasource>                                 \n"
			+ "            <cf>MAX</cf>                                                 \n"
			+ "            <format>Maximum input: @7.2 @Sbits/s</format>                \n"
			+ "        </gprint>                                                        \n"
			+ "        <gprint>                                                         \n"
			+ "            <datasource>in</datasource>                                  \n"
			+ "            <cf>TOTAL</cf>                                               \n"
			+ "            <format>Total input :@7.2 @sbytes@l</format>                 \n"
			+ "            <base>1024</base>                                            \n"
			+ "        </gprint>                                                        \n"
			+ "        <comment>@l</comment>                                            \n"
			+ "        <comment>Description on device: ${alias}@l</comment>             \n"
			+ "        <comment>[${date_start}] -- [${date_end}]</comment>              \n"
			+ "    </graph>                                                             \n"
			+ "</rrd_graph_def>                                                         ";
}
