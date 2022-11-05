// Copyright (C) 2005 Mila NetWhistler.  All rights reserved.
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
//
// For more information contact:
//      Mila NetWhistler        <netwhistler@gmail.com>
//      http://www.netwhistler.spb.ru/
//
// This file from an org.jdesktop.jdnc.incubator.dleuck.icon package @author Daniel Leuck.

package nnm.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;


public class ColorIcon {

        private ColorIcon() {}
        public static Image colorize(Image icon, Color color) {
                Icon imageIcon = new ImageIcon(icon);
                BufferedImage bi=new BufferedImage(imageIcon.getIconWidth(),
                                imageIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
                imageIcon.paintIcon(null, bi.createGraphics(),0,0);
                int iw=bi.getWidth();
                int ih=bi.getHeight();
                float[] hsb=new float[3];
                Color.RGBtoHSB(color.getRed(),color.getGreen(),color.getBlue(),hsb);
                float chue = hsb[0];
                for(int y=0; y<ih; y++) {
                        for(int x=0; x<iw; x++) {
                                int rgb=bi.getRGB(x,y);
                                int red=(rgb >> 16) & 0xFF;
                                int green=(rgb >> 8) & 0xFF;
                                int blue=(rgb >> 0) & 0xFF;
                                int alpha=(rgb >> 24) & 0xFF;
                                Color.RGBtoHSB(red,green,blue,hsb);
                                rgb = HSBtoRGB(chue, hsb[1], hsb[2],alpha);
                                bi.setRGB(x,y,rgb);
                        }
                }
                return bi;
        }
        public static ImageIcon makeIconFromComponent(Component component,
                        int width, int height, boolean forceAntialiasing) {

                CellRendererPane renderer= new CellRendererPane();
                renderer.add(component);

                Dimension d = component.getPreferredSize();
                int iconWidth=(width==-1)? d.width : width;
                int iconHeight=(height==-1)? d.height : height;

                BufferedImage bi=new BufferedImage(iconWidth, iconHeight,
                (component.isOpaque()) ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB);

                Graphics2D g = (Graphics2D)bi.createGraphics();

                if(forceAntialiasing) {
                        Object oldAA = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
                        Object oldFM = g.getRenderingHint(
                                        RenderingHints.KEY_FRACTIONALMETRICS);
                        Object oldRQ = g.getRenderingHint(RenderingHints.KEY_RENDERING);

                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                                        RenderingHints.VALUE_RENDER_QUALITY);

                        renderer.paintComponent(g,(Component)component, null, 0, 0,
                                        iconWidth, iconHeight, true);

                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAA);
                        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oldFM);
                        g.setRenderingHint(RenderingHints.KEY_RENDERING, oldRQ);
                } else {
                        renderer.paintComponent(g,(Component)component, null, 0, 0,
                                        iconWidth, iconHeight, true);
                }

                return new ImageIcon(bi);
        }
    private static int HSBtoRGB(float hue, float saturation, float brightness,
            int alpha) {

                int r = 0, g = 0, b = 0;
                        if (saturation == 0) {
                        r = g = b = (int) (brightness * 255.0f + 0.5f);
                } else {
                        float h = (hue - (float)Math.floor(hue)) * 6.0f;
                        float f = h - (float)java.lang.Math.floor(h);
                        float p = brightness * (1.0f - saturation);
                        float q = brightness * (1.0f - saturation * f);
                        float t = brightness * (1.0f - (saturation * (1.0f - f)));
                        switch ((int) h) {
                                case 0:
                                        r = (int) (brightness * 255.0f + 0.5f);
                                        g = (int) (t * 255.0f + 0.5f);
                                        b = (int) (p * 255.0f + 0.5f);
                                        break;
                                case 1:
                                        r = (int) (q * 255.0f + 0.5f);
                                        g = (int) (brightness * 255.0f + 0.5f);
                                        b = (int) (p * 255.0f + 0.5f);
                                        break;
                                case 2:
                                        r = (int) (p * 255.0f + 0.5f);
                                        g = (int) (brightness * 255.0f + 0.5f);
                                        b = (int) (t * 255.0f + 0.5f);
                                        break;
                                case 3:
                                        r = (int) (p * 255.0f + 0.5f);
                                        g = (int) (q * 255.0f + 0.5f);
                                        b = (int) (brightness * 255.0f + 0.5f);
                                        break;
                                case 4:
                                        r = (int) (t * 255.0f + 0.5f);
                                        g = (int) (p * 255.0f + 0.5f);
                                        b = (int) (brightness * 255.0f + 0.5f);
                                        break;
                                case 5:
                                        r = (int) (brightness * 255.0f + 0.5f);
                                        g = (int) (p * 255.0f + 0.5f);
                                        b = (int) (q * 255.0f + 0.5f);
                                        break;
                        }
                }

                return ((alpha & 0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                                        ((g & 0xFF) << 8)  |
                                        ((b & 0xFF) << 0);
    }

}
