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

import org.jrobin.core.*;
import org.jrobin.mrtg.MrtgConstants;
import org.jrobin.mrtg.MrtgException;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RrdWriter extends Thread implements MrtgConstants {
	private RrdDefTemplate rrdDefTemplate;

	private int sampleCount, badSavesCount, goodSavesCount;

	private List queue = Collections.synchronizedList(new LinkedList());

	private static final RrdDbPool pool = RrdDbPool.getInstance();

	private volatile boolean active = true;

	RrdWriter() throws MrtgException {
		// get definition from template
		try {
			rrdDefTemplate = new RrdDefTemplate(new File(Config
					.getRrdTemplateFile()));
		} catch (IOException e) {
			throw new MrtgException(e);
		} catch (RrdException e) {
			throw new MrtgException(e);
		}
		start();
	}

	public void run() {
		// Debug.print("Archiver started");
		// the code is plain ugly but it should work
		while (active) {
			while (active && queue.size() == 0) {
				synchronized (this) {
					try {
						wait();
					} catch (InterruptedException e) {
						// Debug.print(e.toString());
					}
				}
			}
			if (active && queue.size() > 0) {
				RawSample rawSample = (RawSample) queue.remove(0);
				process(rawSample);
			}
		}
		// Debug.print("Archiver ended");
	}

	void terminate() {
		active = false;
		synchronized (this) {
			notify();
		}
	}

	private void process(RawSample rawSample) {
		RrdDb rrdDb = null;
		try {
			rrdDb = openRrdFileFor(rawSample);
			Sample sample = rrdDb.createSample();
			sample.setTime(rawSample.getTimestamp());
			if (rawSample.isValid()) {
				sample.setValue("in", rawSample.getIfInOctets());
				sample.setValue("out", rawSample.getIfOutOctets());
			}
			sample.update();
			goodSavesCount++;
		} catch (IOException e) {
			// Debug.print(e.toString());
			badSavesCount++;
		} catch (RrdException e) {
			// Debug.print(e.toString());
			badSavesCount++;
		} finally {
			try {
				pool.release(rrdDb);
			} catch (IOException e) {
				// Debug.print(e.toString());
			} catch (RrdException e) {
				// Debug.print(e.toString());
			}
		}
	}

	private String getRrdFilenameFor(RawSample rawSample) {
		return getRrdFilename(rawSample.getHost(), rawSample.getIfDescr());
	}

	static String getRrdFilename(String host, String ifDescr) {
		String filename = ifDescr.replaceAll("[^0-9a-zA-Z]", "_") + "@"
				+ host.replaceFirst(":", "_") + ".rrd";
		return Config.getRrdDir() + filename;
	}

	synchronized void store(RawSample sample) {
		queue.add(sample);
		sampleCount++;
		notify();
	}

	private RrdDb openRrdFileFor(RawSample rawSample) throws IOException,
			RrdException {
		String rrdFile = getRrdFilenameFor(rawSample);
		if (new File(rrdFile).exists()) {
			return pool.requestRrdDb(rrdFile);
		} else {
			// create RRD file first
			rrdDefTemplate.setVariable("path", rrdFile);
			RrdDef rrdDef = rrdDefTemplate.getRrdDef();
			// Debug.print("Creating: " + rrdFile);
			return pool.requestRrdDb(rrdDef);
		}
	}

	int getSampleCount() {
		return sampleCount;
	}

	int getBadSavesCount() {
		return badSavesCount;
	}

	int getGoodSavesCount() {
		return goodSavesCount;
	}

	int getSavesCount() {
		return getGoodSavesCount() + getBadSavesCount();
	}

}
