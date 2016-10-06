/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.graphic;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.modelsphere.jack.debug.Debug;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public abstract class GraphicUtil {

    public static final int LEFT_ALIGNMENT = 1;
    public static final int RIGHT_ALIGNMENT = 2;
    public static final int CENTER_ALIGNMENT = 3;
    public static final int JUSTIFY = 4;
    public static final int GAP_FROM_BORDER = 2;

    private static Map desktopHints;

    private static MediaTracker mediaTracker;
    private static int mediaTrackerID;

    public static int getAlignmentOffset(int alignment, int cellWidth, int dataWidth) {
        switch (alignment) {
        case GraphicUtil.CENTER_ALIGNMENT:
            return (cellWidth - dataWidth) / 2;
        case GraphicUtil.RIGHT_ALIGNMENT:
            return (cellWidth - dataWidth);
        default: // LEFT_ALIGNMENT or JUSTIFY
            return 0;
        }
    }

    // Wrap the string at word boundaries so that it can be drawn in <maxWidth>.
    // Return the list of strings to be drawn one per line.
    // If (maxWidth == 0), means infinite width
    public static ArrayList<String> getWrappedStrings(FontMetrics fm, String str, int maxWidth) {
        ArrayList<String> strings = new ArrayList<String>();
        while (true) {
            int ind = str.indexOf('\n');
            String substr = (ind == -1 ? str : str.substring(0, ind));
            boolean isEmpty = true;
            while (true) {
                int i;
                for (i = 0; i < substr.length(); i++) {
                    if (substr.charAt(i) != ' ')
                        break;
                }
                if (i == substr.length())
                    break;
                substr = substr.substring(i);
                i = (maxWidth == 0 ? substr.length() : getFittingLength(fm, substr, maxWidth));
                if (i < substr.length() && substr.charAt(i) != ' ') {
                    for (int i2 = i; --i2 > 0;) {
                        if (substr.charAt(i2) == ' ') {
                            i = i2;
                            break;
                        }
                    }
                }
                while (substr.charAt(i - 1) == ' ')
                    i--;
                strings.add(substr.substring(0, i));
                isEmpty = false;
                substr = substr.substring(i);
            }
            if (isEmpty)
                strings.add(""); // insure to draw at least one line.
            if (ind == -1)
                break;
            str = str.substring(ind + 1);
        }
        return strings;
    }

    // Return the number of chars that can be drawn in <maxWidth>.
    public static int getFittingLength(FontMetrics fm, String str, int maxWidth) {
        int width = fm.stringWidth(str);
        if (width <= maxWidth)
            return str.length();

        int low = 1; // min 1 char.
        int current = str.length();
        int high = current;
        while (low + 1 < high) {
            current = (current * maxWidth) / width; // fast convergence.
            if (current <= low)
                current = low + 1;
            if (current >= high)
                current = high - 1;
            width = fm.stringWidth(str.substring(0, current));
            if (width <= maxWidth)
                low = current;
            else
                high = current;
        }
        return low;
    }

    /**
     * returns the center of a rectangle
     */
    public static Point rectangleGetCenter(Rectangle r) {
        return new Point(r.x + r.width / 2, r.y + r.height / 2);
    }

    // Resize a rectangle keeping the same center.
    public static Rectangle rectangleResize(Rectangle rect, int width, int height) {
        return new Rectangle(rect.x + (rect.width - width) / 2,
                rect.y + (rect.height - height) / 2, width, height);
    }

    public static boolean samePoint(int x1, int y1, int x2, int y2, int delta) {
        return (Math.abs(x1 - x2) <= delta && Math.abs(y1 - y2) <= delta);
    }

    public static void waitForImage(Image image) {
        new ImageIcon(image).getImage();
    }

    public static void waitForImage(Component comp, Image image) {
        MediaTracker tracker = new MediaTracker(comp);
        try {
            tracker.addImage(image, 0);
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wallPaper(Component comp, Graphics g, Image image) {
        Dimension compSize = comp.getSize();

        waitForImage(comp, image);
        int patchW = image.getWidth(comp);
        int patchH = image.getHeight(comp);
        //Assert.notFalse(patchW != -1 && patchH != -1);
        for (int i = 0; i < compSize.width; i += patchW) {
            for (int j = 0; j < compSize.height; j += patchH) {
                g.drawImage(image, i, j, comp);
            }
        }
    }

    /**
     * load an image from a class relative path.
     * 
     * @param relToThisClass
     *            the class location is prepend to the relImageName to an absolute path.
     * @param relImageName
     *            the relative image file path.
     * @return the image if found null otherwise.
     */
    public static Image loadImage(Class relToThisClass, String relImageName) {
        ClassLoader cl = relToThisClass.getClassLoader();

        Image image = null;
        URL url = relToThisClass.getResource(relImageName);
        if (url != null) {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            image = toolkit.createImage(url);
            MediaTracker mediatracker = getMediaTracker();
            synchronized (mediatracker) {
                int id = getMediaTrackerNextID();
                mediatracker.addImage(image, id);
                try {
                    mediatracker.waitForID(id, 0);
                } catch (InterruptedException e) {
                    Debug.trace("Image failed to load:  " + relImageName);
                }
                mediatracker.removeImage(image, id);
            }
        } else {
            image = findImagefromClasspath(relToThisClass, relImageName);
        } // end if

        if (image == null) {
            Exception ex = new FileNotFoundException(relImageName);
            throw new RuntimeException(ex);
        } // end if

        return image;
    }// end loadImage()

    private static MediaTracker getMediaTracker() {
        if (mediaTracker == null)
            mediaTracker = new MediaTracker(new JLabel());
        return mediaTracker;
    }

    private static int getMediaTrackerNextID() {
        synchronized (getMediaTracker()) {
            return ++mediaTrackerID;
        }
    }

    private static final String CLASSPATH = "java.class.path";

    private static Image findImagefromClasspath(Class relToThisClass, String relImageName) {
        Image image = null;

        String name = relToThisClass.getName();
        String path = name.substring(0, name.lastIndexOf('.'));
        path = path.replace('.', '/');
        path = path + relImageName.substring(1); //remove the '.' of the relative path 

        String classpath = System.getProperty(CLASSPATH);
        StringTokenizer st = new StringTokenizer(classpath, ";:");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            try {
                ZipFile file = new ZipFile(token);
                ZipEntry entry = file.getEntry(path);
                if (entry != null) {
                    InputStream input = file.getInputStream(entry);
                    int size = (int) entry.getSize();
                    byte[] data = getImageFromStream(input, size);
                    ImageIcon icon = new ImageIcon(data);
                    image = icon.getImage();
                    break;
                } //end if

            } catch (IOException ex) {
                //ignore, and continue the iterations
            } //end try
        } //end while
        return image;
    } //end findImagefromClasspath()

    private static byte[] getImageFromStream(InputStream input, int size) {
        byte[] image = new byte[size];

        try {
            int read = input.read(image, 0, size);
        } catch (IOException ex) {
            image = null;
        }

        return image;

    } //end getImageFromStream()

    public static Icon loadIcon(Class relToThisClass, String relImageName) {
        Image image = loadImage(relToThisClass, relImageName);
        return (image == null ? null : new ImageIcon(image));
    }

    public static Point[] polygonToPointArray(Polygon poly) {
        Point[] points = new Point[poly.npoints];
        for (int i = 0; i < poly.npoints; i++)
            points[i] = new Point(poly.xpoints[i], poly.ypoints[i]);
        return points;
    }

    public static Polygon pointArrayToPolygon(Point[] points) {
        Polygon poly = new Polygon();
        for (int i = 0; i < points.length; i++)
            poly.addPoint(points[i].x, points[i].y);
        return poly;
    }

    /**
     * find center in a multi-segment line
     */
    public static final Point getLineCenter(Polygon poly) {
        int i;
        int len = 0;
        for (i = 1; i < poly.npoints; i++) {
            len += getSegmentLengthApprox(poly.xpoints[i - 1], poly.ypoints[i - 1],
                    poly.xpoints[i], poly.ypoints[i]);
        }
        int halfLen = len / 2;
        int prevLen = 0;
        len = 0;
        for (i = 1; i < poly.npoints; i++) {
            prevLen = len;
            len += getSegmentLengthApprox(poly.xpoints[i - 1], poly.ypoints[i - 1],
                    poly.xpoints[i], poly.ypoints[i]);
            if (len >= halfLen)
                break;
        }
        float f = (float) (halfLen - prevLen) / (len - prevLen);
        int x = poly.xpoints[i - 1] + (int) (f * (poly.xpoints[i] - poly.xpoints[i - 1]));
        int y = poly.ypoints[i - 1] + (int) (f * (poly.ypoints[i] - poly.ypoints[i - 1]));
        return new Point(x, y);
    }

    /**
     * Return the approx. length of a line segment (max error = 9%). Fast algorithm.
     */
    private static final int getSegmentLengthApprox(int x1, int y1, int x2, int y2) {
        int dmax = Math.abs(x1 - x2);
        int dmin = Math.abs(y1 - y2);
        if (dmax < dmin) {
            int d = dmax;
            dmax = dmin;
            dmin = d;
        }
        return dmax + (dmin * 7) / 16;
    }

    public static void rotate(Polygon poly, double theta) {
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        for (int i = 0; i < poly.npoints; i++) {
            double x = poly.xpoints[i];
            double y = poly.ypoints[i];
            poly.xpoints[i] = (int) (x * cos - y * sin);
            poly.ypoints[i] = (int) (x * sin + y * cos);
        }
    }

    // BEWARE: do not use poly.getBounds() which is not maintained when we modify directly the polygon points.
    public static Rectangle getBounds(Polygon poly) {
        if (poly.npoints == 0)
            return null;
        int minX, minY, maxX, maxY;
        minX = maxX = poly.xpoints[0];
        minY = maxY = poly.ypoints[0];
        for (int i = 1; i < poly.npoints; i++) {
            int x = poly.xpoints[i];
            if (minX > x)
                minX = x;
            if (maxX < x)
                maxX = x;
            int y = poly.ypoints[i];
            if (minY > y)
                minY = y;
            if (maxY < y)
                maxY = y;
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    // Return the index of the first point of the line that is outside the rectangle
    public static int getFirstOutPointIndex(Polygon poly, Rectangle rect, boolean reverse) {
        if (reverse) {
            for (int i = poly.npoints - 2; i >= 0; i--) {
                if (!rect.contains(poly.xpoints[i], poly.ypoints[i]))
                    return i;
            }
        } else {
            for (int i = 1; i < poly.npoints; i++) {
                if (!rect.contains(poly.xpoints[i], poly.ypoints[i]))
                    return i;
            }
        }
        return -1;
    }

    /*
     * Return the intersection point of the segment <x1,y1>, <x2,y2> with the rectangle; <x1,y1> is
     * inside the rectangle, <x2,y2> may be inside or outside. Fast algorithm.
     */
    public static Point getIntersectionPoint(int x1, int y1, int x2, int y2, Rectangle rect) {
        if (x2 == x1) /* vertical line */
            return new Point(x2, (y2 < y1 ? rect.y : rect.y + rect.height));
        if (y2 == y1) /* horizontal line */
            return new Point((x2 < x1 ? rect.x : rect.x + rect.width), y2);

        double m = (double) (y2 - y1) / (x2 - x1);
        int x = (x2 < x1 ? rect.x : rect.x + rect.width);
        double fy = m * (x - x2) + y2;
        int y;
        /* float comparison, because fy may be bigger than the biggest integer */
        if (fy >= rect.y && fy <= rect.y + rect.height)
            y = (int) fy;
        else {
            y = (y2 < y1 ? rect.y : rect.y + rect.height);
            x = (int) ((y - y2) / m) + x2;
        }
        return new Point(x, y);
    }

    /*
     * The <-1> correction is necessary because <drawRect> draws the right and bottom lines at
     * <x+width> and <y+height>, while drawing is clipped outside the area <x, x+width-1> , <y,
     * y+height-1>.
     */
    public static boolean confineToRect(Rectangle rect, Rectangle container) {
        boolean changed = false;
        if (rect.x < container.x) {
            rect.x = container.x;
            changed = true;
        }
        if (rect.y < container.y) {
            rect.y = container.y;
            changed = true;
        }
        int d = (rect.x + rect.width) - (container.x + container.width - 1);
        if (d > 0) {
            rect.x -= d;
            d = rect.x - container.x;
            if (d < 0) {
                rect.x -= d;
                rect.width += d;
            }
            changed = true;
        }
        d = (rect.y + rect.height) - (container.y + container.height - 1);
        if (d > 0) {
            rect.y -= d;
            d = rect.y - container.y;
            if (d < 0) {
                rect.y -= d;
                rect.height += d;
            }
            changed = true;
        }
        return changed;
    }

    public static boolean confineCenterToRect(Rectangle rect, Rectangle container) {
        int x = confineXToRect(rect.x + rect.width / 2, container) - rect.width / 2;
        int y = confineYToRect(rect.y + rect.height / 2, container) - rect.height / 2;
        if (x == rect.x && y == rect.y)
            return false;
        rect.x = x;
        rect.y = y;
        return true;
    }

    public static boolean confineToRect(Point point, Rectangle container) {
        boolean changed = false;
        if (point.x < container.x) {
            point.x = container.x;
            changed = true;
        }
        if (point.y < container.y) {
            point.y = container.y;
            changed = true;
        }
        int hi = container.x + container.width - 1;
        if (point.x > hi) {
            point.x = hi;
            changed = true;
        }
        hi = container.y + container.height - 1;
        if (point.y > hi) {
            point.y = hi;
            changed = true;
        }
        return changed;
    }

    public static boolean confineToRect(Polygon poly, Rectangle container) {
        boolean changed = false;
        int maxX = container.x + container.width - 1;
        int maxY = container.y + container.height - 1;
        for (int i = 0; i < poly.npoints; i++) {
            if (poly.xpoints[i] < container.x) {
                poly.xpoints[i] = container.x;
                changed = true;
            }
            if (poly.ypoints[i] < container.y) {
                poly.ypoints[i] = container.y;
                changed = true;
            }
            if (poly.xpoints[i] > maxX) {
                poly.xpoints[i] = maxX;
                changed = true;
            }
            if (poly.ypoints[i] > maxY) {
                poly.ypoints[i] = maxY;
                changed = true;
            }
        }
        return changed;
    }

    public static int confineXToRect(int x, Rectangle container) {
        if (x < container.x)
            x = container.x;
        else if (x >= container.x + container.width)
            x = container.x + container.width - 1;
        return x;
    }

    public static int confineYToRect(int y, Rectangle container) {
        if (y < container.y)
            y = container.y;
        else if (y >= container.y + container.height)
            y = container.y + container.height - 1;
        return y;
    }

    public static void saveImageToJPEGFile(BufferedImage bi, File file, float quality)
            throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        JPEGEncodeParam param = JPEGCodec.getDefaultJPEGEncodeParam(bi);
        param.setQuality(quality, false);
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos, param);
        encoder.encode(bi);
        fos.close();
    }
    
    public static void saveImageToPNGFile(BufferedImage bi, File file)
            throws IOException {
    	ImageIO.write(bi, "png", file); 
    }

    public static BufferedImage toBufferedImage(Image image) {
        // This code ensures that all the pixels in the image are loaded.
        image = new ImageIcon(image).getImage();
        // Create the buffered image.
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        // Copy image to buffered image.
        Graphics g = bufferedImage.createGraphics();
        // Clear background and paint the image.
        g.setColor(Color.white);
        g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bufferedImage;
    }

    /**
     * @param opacity
     *            must a number between 0 and 1
     */
    public static Image createDissolveImage(Image image, float opacacity) {
        DissolveFilter filter = new DissolveFilter((int) (255 * opacacity));
        FilteredImageSource fis = new FilteredImageSource(image.getSource(), filter);
        Image dissolveImage = Toolkit.getDefaultToolkit().createImage(fis);
        waitForImage(dissolveImage);
        return dissolveImage;
    }

    public static void configureGraphics(Graphics2D g2d) {
        if (g2d == null)
            return;
        if (desktopHints == null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            desktopHints = (Map) (tk.getDesktopProperty("awt.font.desktophints"));
        }
        if (desktopHints != null) {
            g2d.addRenderingHints(desktopHints);
        }
    }

    public static void configureGraphicsForPrinting(Graphics2D g2d) {
        if (g2d == null)
            return;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //	   g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //	   g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        //	   g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    public static class DissolveFilter extends RGBImageFilter {
        private int opacity;

        public DissolveFilter() {
            this(0);
        }

        public DissolveFilter(int opacity) {
            canFilterIndexColorModel = true;
            setOpacity(opacity);
        }

        public void setOpacity(int opacity) {
            // Assert.notFalse(opacity >= 0 && opacity <= 255);
            this.opacity = opacity;
        }

        public int filterRGB(int x, int y, int rgb) {
            DirectColorModel cm = (DirectColorModel) ColorModel.getRGBdefault();
            int alpha = cm.getAlpha(rgb);
            int red = cm.getRed(rgb);
            int green = cm.getGreen(rgb);
            int blue = cm.getBlue(rgb);

            alpha = opacity;

            return alpha << 24 | red << 16 | green << 8 | blue;
        }
    }
}
