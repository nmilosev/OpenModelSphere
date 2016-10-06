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
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreviewer extends JComponent implements PropertyChangeListener {
    ImageIcon thumbnail = null;
    File f = null;
    private static final int PREVIEW_WIDTH = 180;
    private static final int MARGIN = 3;

    JComponent content = new JComponent() {
        public void paint(java.awt.Graphics g) {
            paintBorder(g);
            if (thumbnail == null) {
                loadImage();
            }

            if (thumbnail != null) {
                int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
                int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;
                if (y < 0) {
                    y = 0;
                }

                if (x < MARGIN) {
                    x = MARGIN;
                }
                thumbnail.paintIcon(this, g, x, y);
            }
        }

    };

    public ImagePreviewer(JFileChooser fc) {
        setLayout(new GridBagLayout());
        add(content, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 6, 0, 0), PREVIEW_WIDTH, 0));
        fc.addPropertyChangeListener(this);
        content.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
    }

    public void loadImage() {
        if (f != null) {
            ImageIcon tmpIcon = new ImageIcon(f.getPath());
            if (tmpIcon.getIconWidth() > (PREVIEW_WIDTH - MARGIN * 2)) {
                thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(
                        (PREVIEW_WIDTH - MARGIN * 2), -1, Image.SCALE_DEFAULT));
            } else {
                thumbnail = tmpIcon;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
            f = (File) e.getNewValue();
            if (isShowing()) {
                loadImage();
                repaint();
            }
        }
    }

    // protected void paintComponent(Graphics g) {
}
