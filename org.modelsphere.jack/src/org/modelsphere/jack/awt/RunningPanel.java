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

package org.modelsphere.jack.awt;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import org.modelsphere.jack.graphic.GraphicUtil;

public class RunningPanel extends JPanel implements MouseListener {
    // direction constants
    private static final int MOVE_RIGHT = 1;
    private static final int MOVE_LEFT = -1;

    // parameters
    private int minTrailWidthPercentage = 0; // %
    private int maxTrailWidthPercentage = 60; // %
    private int colorZoneWidth = 4;
    private Insets margins = new Insets(4, 8, 4, 8);
    private Color barForegroundColor;
    private Color barBackgroundColor;
    private Color messageColor;

    // The message to display
    private String runningMessage = new String(" ");
    private String runningFinishedMessage = new String(" ");
    private String notRunningMessage = new String(" ");
    private String messageDisplayed = new String(notRunningMessage);
    private int messagewidth = 0;

    // Set between start and stop
    private int runX = 0;
    private int trailWidth = 0;
    private int stepSize = 0;
    private int direction = MOVE_RIGHT;
    private boolean running = false;

    // The Thread
    private WaitingProcessThread waitingProcess;
    private int waitingProcessPriority = 6; // normal process are set to 5. This
    // one should be higher to remove
    // flicking effect.

    private JFrame frame;

    // Foreground and background colors informations
    private int fr;
    private int fg;
    private int fb;
    private int br;
    private int bg;
    private int bb;

    public RunningPanel(int steplenght, int maxtraillenght, int mintraillenght,
            int colorzonelenght, Insets insets) {
        super(null);
        minTrailWidthPercentage = mintraillenght;
        maxTrailWidthPercentage = maxtraillenght;
        colorZoneWidth = colorzonelenght;
        stepSize = steplenght;
        if (insets != null)
            margins = insets;
        setMinimumSize(new Dimension(margins.right + margins.left, margins.top + margins.bottom));
        barForegroundColor = new Color(getForeground().getRed(), getForeground().getGreen(),
                getForeground().getBlue());
        barBackgroundColor = new Color(getBackground().getRed(), getBackground().getGreen(),
                getBackground().getBlue());
        messageColor = new Color(getForeground().getRed(), getForeground().getGreen(),
                getForeground().getBlue());
        setRequestFocusEnabled(false);
    }

    synchronized private final void setVisibleMessage(String message) {
        // Must check for non null graphics (occurs if component is hidden)
        if (this.getGraphics() != null) {
            messageDisplayed = new String(message);
            FontMetrics fm = getGraphics().getFontMetrics();
            messagewidth = fm.stringWidth(messageDisplayed);
            // init next running start location
            runX = (margins.left * 2) + messagewidth;
            trailWidth = (minTrailWidthPercentage * (Math.max(getWidth() - runX - margins.right, 1))) / 100;
            // TODO: paintImmediately cause problems : uncomment if Thread
            // reactivated
            /*
             * if (!running) // if running, we must let the process waits the initial waiting time.
             * paintImmediately(0,0,getWidth(),getHeight());
             */
            repaint();
        }
    }

    synchronized public final void setPriority(int priority) {
        if ((priority <= 10) && (priority >= 1))
            waitingProcessPriority = priority;
    }

    synchronized public final void setMessageColor(Color c) {
        if (c != null)
            messageColor = c;
    }

    synchronized public final void setBarForegroundColor(Color c) {
        if (c != null)
            barForegroundColor = c;
    }

    synchronized public final void setBarBackgroundColor(Color c) {
        if (c != null)
            barBackgroundColor = c;
    }

    synchronized public final boolean isRunning() {
        return running;
    }

    synchronized protected final void paintComponent(Graphics g) {
        GraphicUtil.configureGraphics((Graphics2D) g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(messageColor);
        g.drawString(messageDisplayed, margins.left, getHeight() - margins.bottom);

        if (running) {
            // get Size of the string to display

            int w = Math.max(getWidth() - (margins.left * 2) - margins.right - messagewidth, 1);
            int h = Math.max(getHeight() - margins.top - margins.bottom, 1);

            int maxTrailWidth = (maxTrailWidthPercentage * w) / 100;
            int minTrailWidth = (minTrailWidthPercentage * w) / 100;

            int absolutezero = (margins.left * 2) + messagewidth;

            // clean all the area of the component

            // move the trail
            if (direction == MOVE_RIGHT)
                runX = Math.min(runX + stepSize, w + absolutezero);
            else
                runX = Math.max(runX - stepSize, absolutezero);

            // change direction?
            if ((runX == (w + absolutezero)) && (trailWidth <= minTrailWidth)) {
                direction = MOVE_LEFT;
                trailWidth = minTrailWidth;
            } else if ((runX == absolutezero) && (trailWidth <= minTrailWidth)) {
                direction = MOVE_RIGHT;
                trailWidth = minTrailWidth;
            }

            // adjust trail size
            if (direction == MOVE_RIGHT) {
                if ((runX + trailWidth + stepSize) > (w + absolutezero))
                    trailWidth = Math.max(trailWidth - stepSize, minTrailWidth);
                else
                    trailWidth = Math.min(trailWidth + stepSize, maxTrailWidth);
            } else {
                if (runX == absolutezero)
                    trailWidth = Math.max(trailWidth - stepSize, minTrailWidth);
                else
                    trailWidth = Math.min(trailWidth + stepSize, maxTrailWidth);
            }

            float zoneCount = Math.max(trailWidth / colorZoneWidth, 0) + 2; // 2
            // zones
            // added
            // to
            // eliminate
            // flicking
            float rdelta = (fr - br) / zoneCount;
            float gdelta = (fg - bg) / zoneCount;
            float bdelta = (fb - bb) / zoneCount;

            if (direction == MOVE_RIGHT) {
                for (int i = 0; i < zoneCount; i++) {
                    Color c = new Color(fr - (int) (rdelta * i), fg - (int) (gdelta * i), fb
                            - (int) (bdelta * i));
                    g.setColor(c);
                    int x = runX + (int) (colorZoneWidth * (zoneCount - i));
                    g.fillRect(x, margins.top, Math.min(colorZoneWidth, Math.max((w + absolutezero)
                            - x, 0)), h);
                }
            } else {
                for (int i = 0; i < zoneCount; i++) {
                    Color c = new Color(fr - (int) (rdelta * i), fg - (int) (gdelta * i), fb
                            - (int) (bdelta * i));
                    g.setColor(c);
                    int x = runX + (int) (colorZoneWidth * i);
                    g.fillRect(x, margins.top, Math.min(colorZoneWidth, Math.max((w + absolutezero)
                            - x, 0)), h);
                }
            }
        }
    }

    protected final void paintChildren(Graphics g) {
    }

    synchronized public final void start(String message, long timeBeforeStarting) {
        if (waitingProcess == null) {
            direction = MOVE_RIGHT;
            // running = true; // TODO: remove if thread reactivated
            if (message != null)
                runningMessage = new String(message);
            // get color informations
            fr = barForegroundColor.getRed();
            fg = barForegroundColor.getGreen();
            fb = barForegroundColor.getBlue();
            br = barBackgroundColor.getRed();
            bg = barBackgroundColor.getGreen();
            bb = barBackgroundColor.getBlue();
            setVisibleMessage(runningMessage);
            // TODO: check if paintImmediately work properly before activating
            // the following lines.
            // waitingProcess = new WaitingProcessThread(this,
            // timeBeforeStarting);
            // waitingProcess.setPriority(waitingProcessPriority);
            // waitingProcess.start();
        }
    }

    synchronized public final void start(String message) {
        start(message, 1);
    }

    private int stepCount = 0;

    synchronized final void step() {
        stepCount++;
        if (stepCount > 50) {
            JFrame frame = (JFrame) SwingUtilities.getAncestorOfClass(JFrame.class, this);
            if (frame == null) {
                paintImmediately(0, 0, getWidth(), getHeight());
            } else {
                // System.out.println(frame.getRootPane().getBounds());
                frame.validate();
                // frame.getRootPane().invalidate();
                // frame.getRootPane().validate();
                // frame.getLayeredPane()

                RepaintManager.currentManager((JPanel) frame.getContentPane()).addDirtyRegion(
                        (JPanel) frame.getContentPane(), 0, 0, frame.getContentPane().getWidth(),
                        frame.getContentPane().getHeight());
                RepaintManager.currentManager(frame.getJMenuBar()).addDirtyRegion(
                        frame.getJMenuBar(), 0, 0, frame.getJMenuBar().getWidth(),
                        frame.getJMenuBar().getHeight());
                RepaintManager.currentManager((JPanel) frame.getContentPane()).paintDirtyRegions();
                // ((JPanel)frame.getContentPane()).paintImmediately(frame.getContentPane().getBounds());
            }
            stepCount = 0;
        } else {
            RepaintManager.currentManager(this).addDirtyRegion(this, 0, 0, getWidth(), getHeight());
            RepaintManager.currentManager(this).paintDirtyRegions();
            // paintImmediately(0,0,getWidth(),getHeight());
        }

    }

    synchronized public final void stop(String message) {
        running = false;
        if (waitingProcess != null) {
            waitingProcess.stopRunning();
            waitingProcess = null;
        }
        if (message != null)
            runningFinishedMessage = new String(message);
        else
            runningFinishedMessage = new String(" ");

        if (frame == null) {
            // search parent frame
            boolean stop = false;
            Component comp = this;
            while (!stop) {
                if (comp.getParent() != null) {
                    comp = comp.getParent();
                    if (comp instanceof JFrame) {
                        frame = (JFrame) comp;
                        stop = true;
                    }
                } else {
                    stop = true;
                }
            }
        }

        if (frame != null) {
            frame.addMouseListener(this);
        }
        setVisibleMessage(runningFinishedMessage);
    }

    public final void setMessage(String message) {
        if (frame != null)
            frame.removeMouseListener(this);
        if (message != null)
            notRunningMessage = new String(message);
        else
            notRunningMessage = new String(" ");
        setVisibleMessage(notRunningMessage);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        frame.removeMouseListener(this);
        setVisibleMessage(notRunningMessage);
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}

class WaitingProcessThread extends Thread {
    private RunningPanel rp;
    private long timeBeforeStarting;
    private boolean started = false;
    private boolean processStoped = false;

    WaitingProcessThread(RunningPanel rp, long timeBeforeStarting) {
        super();
        this.rp = rp;
        this.timeBeforeStarting = timeBeforeStarting;
    }

    public void run() {
        if (!started) {
            try {
                sleep(timeBeforeStarting);
                started = true;
            } catch (java.lang.InterruptedException ex) {
                rp.stop(" ");
            }
        }
        while (!processStoped) {
            rp.step();
            try {
                sleep(25);
            } catch (java.lang.InterruptedException ex) {
                rp.stop(" ");
            }
        }
    }

    public void stopRunning() {
        processStoped = true;
    }
}
