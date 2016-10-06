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

package org.modelsphere.jack.srtool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.international.LocaleMgr;

@SuppressWarnings("serial")
public abstract class Splash extends JDialog implements ActionListener {
    static final Font VERSION_FONT = new Font("SansSerif", Font.BOLD, 16); //NOT LOCALIZABLE
    static final Font BUILD_FONT = new Font("SansSerif", Font.BOLD, 11); //NOT LOCALIZABLE
    static final Font STATUS_FONT = new Font("SansSerif", Font.PLAIN, 10); //NOT LOCALIZABLE
    static String buildString = null;
    static String copyrightString = null;
    private static final String kBetaVersion= LocaleMgr.screen.getString("betaVersion");

    static {
        buildString = "("
                + MessageFormat.format(LocaleMgr.screen.getString("Build0"),
                        new Object[] { new Integer(ApplicationContext.APPLICATION_BUILD_ID) })
                + ")";
        copyrightString = LocaleMgr.screen.getString("Copyright");

        if (ApplicationContext.APPLICATION_BUILD_ID_EXTENSION != null
                && ApplicationContext.APPLICATION_BUILD_ID_EXTENSION.length() > 0) {
            buildString += " " + ApplicationContext.APPLICATION_BUILD_ID_EXTENSION;
        }
    }

    //These variables are set in the constructor
    //Don't take for granted that ApplicationContext is already loaded when Splash is [MS]
    final String version;

    private boolean init = false;

    private SplashImage imagePanel;

    private boolean messageEnable = false;
    private Image image;
    private FontMetrics fm;

    static final int X_LOC = 6;

    // if externalHandler == true, window will not dispose by itself and will allow message display
    public Splash(Frame owner, boolean externalHandler, boolean messageEnable) {
        super(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        if (owner != null) {
            setModal(true);
        }

        //		AWTUtilities.setWindowOpaque(this, false);
        //		AWTUtilities.setWindowOpacity(this, 1f);

        imagePanel = new SplashImage(this, !externalHandler);

        version = new String(ApplicationContext.getApplicationVersion()); // + ' ' + kBetaVersion;

        this.messageEnable = messageEnable;
        image = getImage();
        imagePanel.setImage(image);
        setContentPane(imagePanel);

        setSize(imagePanel.getPreferredSize());
        validate();
        AwtUtil.centerWindow(this);
        if (!externalHandler) {
            if ((owner != null) && (owner instanceof JFrame)) {
                JFrame frame = (JFrame) owner;
                JLayeredPane pane = frame.getLayeredPane();
                pane.paintImmediately(pane.getBounds());
            }
            setVisible(true);
        }
    }

    // we must init during repaint.  Linux bug where graphics will be null during initialization.
    void initGraphics() {
        if (init)
            return;
        Graphics g = imagePanel.getGraphics();

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GraphicUtil.configureGraphics(g2d);

        int versionHeight = 0;

        //version number
        fm = g.getFontMetrics(VERSION_FONT);
        int buildHeight = fm.getHeight();
        int buildwidth = fm.stringWidth(version);
        int width = getWidth();
        int x = width - buildwidth - 25;
        int y = buildHeight - fm.getDescent() + versionHeight + 144;
        Point point = new Point(x, y);
        imagePanel.setVersionPosition(point);

        //build id
        fm = g.getFontMetrics(BUILD_FONT);
        buildHeight = fm.getHeight();
        buildwidth = fm.stringWidth(buildString);
        x = width - buildwidth - 25;
        y = buildHeight - fm.getDescent() + versionHeight + 162;
        point = new Point(x, y);
        imagePanel.setBuildPosition(point);

        if (messageEnable) {
            fm = g.getFontMetrics(STATUS_FONT);
            imagePanel.setTextPosition(new Point(X_LOC, getHeight() - 6 - fm.getHeight()));
        }
        imagePanel.validate();
        init = true;
    }

    public abstract Image getImage();

    public final void actionPerformed(ActionEvent e) {
    };

    public final void setGUIText(final String text) {
        if (!messageEnable)
            return;
        if (SwingUtilities.isEventDispatchThread()) {
            imagePanel.setStatusText(text);
        } else {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        imagePanel.setStatusText(text);
                    }
                });
            } catch (InvocationTargetException e) {
            } catch (InterruptedException e) {
            }
        }
    }

    public final void appendGUIText(String text) {
        if (!messageEnable)
            return;
        setGUIText(imagePanel.getStatusText().concat(text));
    }

}
