/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.preference;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.preference.OptionDialog;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayGUIOptionGroup extends OptionGroup {
    public static final String PROPERTY_LAYOUT = DefaultMainFrame.PROPERTY_LAYOUT;
    public static final Integer PROPERTY_LAYOUT_DEFAULT = DefaultMainFrame.PROPERTY_LAYOUT_DEFAULT;
    public static final Image LEFT_RIGHT_NOTITLED_IMAGE = GraphicUtil.loadImage(
            DefaultMainFrame.class, "international/resources/layout3a.jpg");
    public static final Image LEFT_BOTTOM_NOTITLED_IMAGE = GraphicUtil.loadImage(
            DefaultMainFrame.class, "international/resources/layout2a.jpg");
    public static final Image LEFT_LEFT_NOTITLED_IMAGE = GraphicUtil.loadImage(
            DefaultMainFrame.class, "international/resources/layout1a.jpg");

    private static class DisplayGUIOptionPanel extends OptionPanel implements ActionListener {
        private JLabel layoutLabel = new JLabel();
        private JLabel noticeLabel = new JLabel();
        private JRadioButton leftrightLayout = new JRadioButton();
        private JRadioButton leftbottomLayout = new JRadioButton();
        private JRadioButton leftleftLayout = new JRadioButton();
        private int selected = -1;
        private ButtonGroup layoutGroup = new ButtonGroup();
        private Image previewImage;
        private JLabel previewLabel = new JLabel();
        private JPanel previewPanel = new JPanel() {
            protected void paintChildren(Graphics g) {
                if (previewImage == null)
                    return;
                Insets insets = getInsets();
                g.drawImage(previewImage, insets.left, insets.top, null);
            }

            public Dimension getPreferredSize() {
                Insets insets = getInsets();
                Dimension dim = new Dimension(insets.left + insets.right
                        + LEFT_LEFT_NOTITLED_IMAGE.getWidth(null), insets.top + insets.bottom
                        + LEFT_LEFT_NOTITLED_IMAGE.getHeight(null));
                return dim;
            }

            public Dimension getMinimumSize() {
                Insets insets = getInsets();
                Dimension dim = new Dimension(insets.left + insets.right
                        + LEFT_LEFT_NOTITLED_IMAGE.getWidth(null), insets.top + insets.bottom
                        + LEFT_LEFT_NOTITLED_IMAGE.getHeight(null));
                return dim;
            }
        };

        DisplayGUIOptionPanel() {
            setLayout(new GridBagLayout());

            // Ensure all images are loaded completely
            GraphicUtil.waitForImage(LEFT_RIGHT_NOTITLED_IMAGE);
            GraphicUtil.waitForImage(LEFT_BOTTOM_NOTITLED_IMAGE);
            GraphicUtil.waitForImage(LEFT_LEFT_NOTITLED_IMAGE);

            layoutLabel.setText(LocaleMgr.screen.getString("componentsLayout"));
            noticeLabel.setText(LocaleMgr.misc.getString("needRestart"));
            previewLabel.setText(LocaleMgr.screen.getString("Preview"));
            leftrightLayout.setText(LocaleMgr.screen.getString("leftrightLayout"));
            leftbottomLayout.setText(LocaleMgr.screen.getString("leftbottomLayout"));
            leftleftLayout.setText(LocaleMgr.screen.getString("leftleftLayout"));

            layoutGroup.add(leftrightLayout);
            layoutGroup.add(leftbottomLayout);
            layoutGroup.add(leftleftLayout);

            previewPanel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

            add(layoutLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(18, 9, 0, 5), 0, 0));
            add(noticeLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(0, 9, 0, 5), 0, 0));
            add(leftrightLayout,
                    new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.BOTH, new Insets(6, 27, 0, 5), 0, 0));
            add(leftbottomLayout,
                    new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.BOTH, new Insets(0, 27, 0, 5), 0, 0));
            add(leftleftLayout,
                    new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.BOTH, new Insets(0, 27, 0, 5), 0, 0));
            add(previewLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.BOTH, new Insets(12, 9, 0, 5), 0, 0));
            add(previewPanel, new GridBagConstraints(0, 7, 2, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(6, 32, 0, 5), 0, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 8, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            leftrightLayout.addActionListener(this);
            leftbottomLayout.addActionListener(this);
            leftleftLayout.addActionListener(this);

        }

        public void init() {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            int layout = prefs.getPropertyInteger(DefaultMainFrame.class, PROPERTY_LAYOUT,
                    PROPERTY_LAYOUT_DEFAULT).intValue();
            switch (layout) {
            case (DefaultMainFrame.LAYOUT_LEFT_RIGHT):
                leftrightLayout.setSelected(true);
                break;
            case (DefaultMainFrame.LAYOUT_LEFT_LEFT):
                leftleftLayout.setSelected(true);
                break;
            case (DefaultMainFrame.LAYOUT_LEFT_BOTTOM):
                leftbottomLayout.setSelected(true);
                break;
            }
            selected = layout;
            updatePreview();
            revalidate();
        }

        private void updatePreview() {
            switch (selected) {
            case (DefaultMainFrame.LAYOUT_LEFT_RIGHT):
                previewImage = LEFT_RIGHT_NOTITLED_IMAGE;
                break;
            case (DefaultMainFrame.LAYOUT_LEFT_LEFT):
                previewImage = LEFT_LEFT_NOTITLED_IMAGE;
                break;
            case (DefaultMainFrame.LAYOUT_LEFT_BOTTOM):
                previewImage = LEFT_BOTTOM_NOTITLED_IMAGE;
                break;
            default:
                previewImage = null;
            }
            previewPanel.repaint();
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();

            if (source == leftrightLayout) {
                fireOptionChanged(prefs, DefaultMainFrame.class, PROPERTY_LAYOUT, new Integer(
                        DefaultMainFrame.LAYOUT_LEFT_RIGHT));
                if (leftrightLayout.isSelected())
                    selected = DefaultMainFrame.LAYOUT_LEFT_RIGHT;
            } else if (source == leftbottomLayout) {
                fireOptionChanged(prefs, DefaultMainFrame.class, PROPERTY_LAYOUT, new Integer(
                        DefaultMainFrame.LAYOUT_LEFT_BOTTOM));
                if (leftbottomLayout.isSelected())
                    selected = DefaultMainFrame.LAYOUT_LEFT_BOTTOM;
            } else if (source == leftleftLayout) {
                fireOptionChanged(prefs, DefaultMainFrame.class, PROPERTY_LAYOUT, new Integer(
                        DefaultMainFrame.LAYOUT_LEFT_LEFT));
                if (leftleftLayout.isSelected())
                    selected = DefaultMainFrame.LAYOUT_LEFT_LEFT;
            }
            updatePreview();

            setRequireRestart();
        }

    };

    public DisplayGUIOptionGroup() {
        super(LocaleMgr.misc.getString("GUI"));
    }

    protected OptionPanel createOptionPanel() {
        return new DisplayGUIOptionPanel();
    }

}
