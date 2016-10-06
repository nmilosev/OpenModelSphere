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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;
import javax.swing.border.Border;

public class StatusBar extends JPanel implements PropertyChangeListener {
    public static final int RELATIVE_WIDTH = -1;
    // Return MESSAGE_PANEL from getComponentAt(int) inside your model if you
    // want to use this feature.
    public static RunningPanel MESSAGE_PANEL = new RunningPanel(2, 25, 0, 4, new Insets(6, 7, 6, 7));

    // ALL BORDER SHOULD HAVE THE SAME INSETS
    private static final Border DEFAULT_BORDER = new ThinBevelBorder(ThinBevelBorder.LOWERED);
    private static final Border NO_BORDER = BorderFactory.createEmptyBorder(3, 3, 3, 3);

    private StatusBarModel model;
    private JPanel panel;

    static {
        MESSAGE_PANEL.setMessageColor(Color.black);
        MESSAGE_PANEL.setBarForegroundColor(new Color(10, 50, 100));
    }

    public StatusBar(StatusBarModel aModel) {
        super();
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 0));

        panel = new JPanel(new GridBagLayout());
        add(panel, BorderLayout.CENTER);
        setModel(aModel);
    }

    public final StatusBarModel getModel() {
        return model;
    }

    public final void setModel(StatusBarModel aModel) {
        model = aModel;

        panel.removeAll();
        int gridXCount = 0;

        int colCount = model.getComponentCount();
        for (int j = 0; j < colCount; j++) {

            // Title manipulations
            if (model.getTitleForComponentAt(j) != null) {
                JComponent title = (JComponent) model.getTitleForComponentAt(j);
                title.setBorder(BorderFactory.createEmptyBorder(2, 3, 2, 2));
                title.setFont(new Font(title.getFont().getName(), Font.PLAIN, 11));
                title.setForeground(Color.black);

                if (title instanceof JLabel) {
                    JLabel label = (JLabel) title;
                    label.setHorizontalAlignment(SwingConstants.RIGHT);
                }

                GridBagConstraints c1 = new GridBagConstraints();
                c1.gridx = gridXCount;
                c1.gridy = 0;
                c1.weighty = 0;
                c1.weightx = 0;
                c1.insets = new Insets(0, 9, 0, 2);
                if (gridXCount == 0)
                    c1.insets.left = 0;
                if (gridXCount == model.getComponentCount())
                    c1.insets.right = 0;
                c1.fill = GridBagConstraints.BOTH;
                c1.ipadx = 2;
                c1.ipady = 0;
                c1.anchor = GridBagConstraints.EAST;
                gridXCount++;
                panel.add(title, c1);
            }

            JComponent comp = model.getComponentAt(j);
            comp.setBorder(DEFAULT_BORDER);
            if (comp != MESSAGE_PANEL)
                comp.setForeground(Color.black);
            comp.setFont(new Font(comp.getFont().getName(), Font.PLAIN, 11));
            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = gridXCount;
            c2.gridy = 0;
            c2.weighty = 0;
            c2.insets = new Insets(0, 5, 0, 0);
            if (gridXCount == 0)
                c2.insets.left = 0;
            c2.fill = GridBagConstraints.BOTH;
            c2.ipadx = 2;
            c2.ipady = 0;
            c2.anchor = GridBagConstraints.WEST;
            gridXCount++;

            if (model.getWidthAt(j) == RELATIVE_WIDTH) {
                c2.weightx = 1;
            } else {
                int w = model.getWidthAt(j);
                c2.weightx = 0;
                comp.setMinimumSize(new Dimension(w, this.getMinimumSize().height));
                comp.setMaximumSize(new Dimension(w, this.getMaximumSize().height));
                comp.setPreferredSize(new Dimension(w, this.getPreferredSize().height));
            }

            /*
             * if (comp instanceof JLabel) { JLabel label = (JLabel)comp;
             * label.setHorizontalAlignment(SwingConstants.LEFT); }
             */

            panel.add(comp, c2);
            // TODO: Try to remove this line (and the property change listener
            // support)
            // .... if there is no refresh problem in further AWT release.
            comp.addPropertyChangeListener(this);
        }

        // This is a patch. I should not have to do this. JDK1.3RC1
        Component parent = this.getParent();
        if (parent != null)
            parent.doLayout();
    }

    public final void propertyChange(PropertyChangeEvent evt) {
        this.validate();
    }

    public final void startWaitingBar(String message) {
        MESSAGE_PANEL.start(message);
    }

    public final void startWaitingBar(String message, long timeBeforeStarting) {
        MESSAGE_PANEL.start(message, timeBeforeStarting);
    }

    public final void stopWaitingBar(String message) {
        MESSAGE_PANEL.stop(message);
    }

    public final void setMessage(String message) {
        MESSAGE_PANEL.setMessage(message);
        if (message == null || message.length() == 0 || MESSAGE_PANEL.isRunning())
            MESSAGE_PANEL.setBorder(DEFAULT_BORDER);
        else
            MESSAGE_PANEL.setBorder(NO_BORDER);
        MESSAGE_PANEL.setToolTipText(message);
    }

}
