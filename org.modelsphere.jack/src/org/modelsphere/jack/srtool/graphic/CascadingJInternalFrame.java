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

package org.modelsphere.jack.srtool.graphic;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.ImageIcon;

import org.modelsphere.jack.srtool.ApplicationContext;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;

/**
 * 
 * A CascadingJInternalFrame implements an initially autopositioning JFrame using a named
 * PointCascade to assign the instance's location. A HashMap of PointCascades, keyed by name, is
 * maintained in the class. It is accessed when the CascadingJInternalFrame is constructed to get
 * the initial location. Afterwards, on the first call to setBounds, the JFrame's position is
 * adjusted to the cascaded location. The autopositioning may be turned off or adjusted on an
 * instance prior to the setBounds call.
 * 
 */
public class CascadingJInternalFrame extends JInternalFrame {
    private String pointCascadeName = null;
    private static HashMap pointCascadeHashMap = new HashMap();
    /** The name of the default PointCascade **/
    public static final String defaultCascadeName = "_default_"; // NOT LOCALIZABLE, property key

    private Point location;
    private boolean autoposition;
    private int initialOptionSize = 0;

    /**
     * @return Returns the initialOptionSize.
     */
    public int getInitialOptionSize() {
        return initialOptionSize;
    }

    /**
     * @param initialOptionSize
     *            The initialOptionSize to set.
     */
    public void setInitialOptionSize(int initialOptionSize) {
        this.initialOptionSize = initialOptionSize;
    }

    /**
     * Creates a new CascadingJInternalFrame with no title using the default PointCascade.
     **/
    public CascadingJInternalFrame() {
        this("", defaultCascadeName, true);
    }

    /**
     * Creates a new CascadingJInternalFrame with the given title using the default PointCascade.
     * 
     * @param title
     *            the text to be placed on the title bar
     **/
    public CascadingJInternalFrame(String title) {
        this(title, defaultCascadeName, true);
    }

    /**
     * Creates a new CascadingJInternalFrame with the given title using the named PointCascade.
     * 
     * @param title
     *            the text to be placed on the title bar
     * @param cascadeName
     *            the name of the PointCascade to retrieve the location from
     **/
    public CascadingJInternalFrame(String title, String cascadeName) {
        this(title, cascadeName, true);
    }

    /**
     * Creates a new CascadingJInternalFrame with the given title using the named PointCascade.
     * 
     * @param title
     *            the text to be placed on the title bar
     * @param cascadeName
     *            the name of the PointCascade to retrieve the location from
     * @param autoposition
     *            if false, then no autopositioning will be done for the instance
     **/
    public CascadingJInternalFrame(String title, String cascadeName, boolean autoposition) {
        super(title);
        pointCascadeName = cascadeName;
        if (autoposition)
            setLocation(cascadeName);
        setAutoposition(autoposition);
        // set the default icon
        ImageIcon icon = new ImageIcon(ApplicationContext.APPLICATION_IMAGE_ICON);
        setFrameIcon(icon);
        setBounds_(0, 0, 600, 400);
    }

    /**
     * Get the CascadeName
     * 
     * @return String the cascadeName
     **/
    public final String getCascadeName() {
        return pointCascadeName;
    }

    /**
     * Sets the default PointCascade to the given PointCascade.
     * 
     * @param pointCascade
     *            the new default PointCascade
     **/

    public static void setPointCascade(PointCascade pointCascade) {
        setPointCascade(defaultCascadeName, pointCascade);
    }

    /**
     * Sets the named PointCascade to the given PointCascade.
     * 
     * @param cascadeName
     *            the name of the PointCascade
     * @param pointCascade
     *            the new PointCascade
     **/
    public static void setPointCascade(String cascadeName, PointCascade pointCascade) {
        pointCascadeHashMap.put(cascadeName, pointCascade);
    }

    /**
     * Gets the default PointCascade.
     * 
     * @return the default PointCascade
     **/
    public static PointCascade getPointCascade() {
        return CascadingJInternalFrame.getPointCascade(defaultCascadeName);
    }

    /**
     * Gets the named PointCascade. If a point cascade by that name does not exist, a new one is
     * created.
     * 
     * @param cascadeName
     *            the name of the PointCascade
     * @return the named PointCascade
     **/
    public static PointCascade getPointCascade(String cascadeName) {
        boolean firstFrame = true;

        if (cascadeName == null)
            cascadeName = defaultCascadeName;
        else {
            JDesktopPane desktop = org.modelsphere.jack.srtool.ApplicationContext
                    .getDefaultMainFrame().getJDesktopPane();
            if (desktop != null) {
                JInternalFrame[] frames = desktop.getAllFrames();
                for (int i = 0; i < frames.length; i++) {
                    if (frames[i] instanceof CascadingJInternalFrame) {
                        if (((CascadingJInternalFrame) frames[i]).getCascadeName().equals(
                                cascadeName)) {
                            firstFrame = false;
                            break;
                        }
                    }
                }// end for
            }
        }// end else

        PointCascade pointCascade = null;
        if (!firstFrame)
            pointCascade = (PointCascade) (pointCascadeHashMap.get(cascadeName));
        if (pointCascade == null) {
            pointCascade = new PointCascade();
            CascadingJInternalFrame.setPointCascade(cascadeName, pointCascade);
        }
        return pointCascade;
    }

    /**
     * Sets whether the instance will be autopositioned.
     * 
     * @param autoposition
     *            true if autopositioning is desired
     **/
    public void setAutoposition(boolean autoposition) {
        this.autoposition = autoposition;
    }

    /**
     * Gets whether the instance will be autopositioned.
     * 
     * @return true if autopositioning will occur
     **/
    public boolean getAutoposition() {
        return autoposition;
    }

    /**
     * Sets the instance's location by retrieving it from the named PointCascade.
     * 
     * @param cascadeName
     *            the name of the PointCascade
     **/
    public void setLocation(String cascadeName) {
        PointCascade pointCascade = getPointCascade(cascadeName);
        setLocation(pointCascade.location());
        pointCascade.increment();
    }

    /**
     * Sets the instance's location to the specified Point.
     * 
     * @param location
     *            the new location
     **/
    public void setLocation(Point location) {
        this.location = location;
    }

    /**
     * Gets the instance's location.
     * 
     * @return the location
     **/
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the instance's bounds, autopositioning if intended.
     * 
     * @param x
     *            the new x coordinate of the upper left position
     * @param y
     *            the new y coordinate of the upper left position
     * @param width
     *            the new width of the instance
     * @param height
     *            the new height of the instance
     **/
    private void setBounds_(int x, int y, int width, int height) {
        if (getAutoposition() == true) {
            Point frameLocation = getLocation();
            x = frameLocation.x;
            y = frameLocation.y;
            setAutoposition(false);
        }
        super.setBounds(x, y, width, height);
    }

    /**
     * Sets the instance's bounds, autopositioning if intended.
     * 
     * @param r
     *            the new bounds of the instance
     **/
    public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }
}
