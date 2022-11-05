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

import java.util.Vector;

public class Timer extends Thread implements MrtgConstants {
	private volatile boolean active = true;

	Timer() {
		start();
	}

	public void run() {
		DeviceList deviceList;
		deviceList = Server.getInstance().getDeviceList();
		// Debug.print("Scheduler started");
		while (active) {
			Vector routers = deviceList.getRouters();
			for (int i = 0; i < routers.size(); i++) {
				Device router = (Device) routers.get(i);
				Vector links = router.getLinks();
				for (int j = 0; j < links.size(); j++) {
					Port link = (Port) links.get(j);
					if (router.isActive() && link.isActive() && link.isDue()
							&& !link.isSampling()) {
						new SnmpReader(router, link).start();
						try {
							sleep((long) (1 + Math.random() * SCHEDULER_DELAY));
						} catch (InterruptedException e) {
							// e.printStackTrace();
						}
					}
				}
			}
			// sleep for a while
			synchronized (this) {
				try {
					wait(SCHEDULER_RESOLUTION * 1000L);
				} catch (InterruptedException e) {
				}
			}
		}
		// Debug.print("Scheduler ended");
	}

	void terminate() {
		active = false;
		synchronized (this) {
			notify();
		}
	}
}
