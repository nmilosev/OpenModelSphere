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

package org.modelsphere.sms.preference;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.international.LocaleMgr;

public class DiagramStampOptionGroup extends OptionGroup {
    private static final String USE_STAMP_IMAGE = "UseStampImage"; // NOT LOCALIZABLE
    private static final Boolean USE_STAMP_IMAGE_DEFAULT = Boolean.TRUE;
    public static final String STAMP_IMAGE_PATH = "StampImagePath"; // NOT LOCALIZABLE
    public static final String STAMP_IMAGE_PATH_DEFAULT = SMSFilter.class.getResource(
            "international/resources/sms_stamp.jpg").toString();
    private static final int PREVIEW_MAX_HEIGHT = 200;
    private static final int PREVIEW_MAX_WIDTH = 300;

    // private static final String  BROWSE_STAMP_IMAGE          = LocaleMgr.screen.getString("browseStampImage"); 

    @SuppressWarnings("serial")
    private static class DiagramStampOptionPanel extends OptionPanel implements ActionListener {
        private JButton defButton = new JButton(LocaleMgr.misc.getString("Default"));
        private JTextField stampImageFileTextField = new JTextField();
        private JCheckBox useStampImageCBox = new JCheckBox(LocaleMgr.screen
                .getString("UseStampImage"));
        private JButton browseStampImageButton = new JButton("...");
        private JButton defaultStampImageButton = new JButton(LocaleMgr.screen
                .getString("defaultStampImgBtn"));
        private JPanel defaultStampImagePanel;
        private Image previewImage;
        private JLabel previewLabel = new JLabel();
        private JPanel previewPanel = new JPanel() {
            protected void paintChildren(Graphics g) {
                if (previewImage == null)
                    return;
                Insets insets = getInsets();
                Shape oldClip = g.getClip();
                g.setClip(insets.left, insets.top, getWidth() - insets.left - insets.right,
                        getHeight() - insets.top - insets.bottom);
                g.drawImage(previewImage, insets.left, insets.top, null);
                g.setClip(oldClip);
                if (!useStampImageCBox.isSelected())
                    previewImage.flush();
            }

            public Dimension getPreferredSize() {
                if (previewImage == null)
                    return new Dimension(200, 200);
                Insets insets = getInsets();
                Dimension dim = new Dimension(insets.left + insets.right
                        + previewImage.getWidth(null), insets.top + insets.bottom
                        + previewImage.getHeight(null));
                if (dim.width > PREVIEW_MAX_WIDTH)
                    dim.width = PREVIEW_MAX_WIDTH;
                if (dim.height > PREVIEW_MAX_HEIGHT)
                    dim.height = PREVIEW_MAX_HEIGHT;
                return dim;
            }

            public Dimension getMinimumSize() {
                if (previewImage == null)
                    return new Dimension(100, 100);
                Insets insets = getInsets();
                Dimension dim = new Dimension(insets.left + insets.right
                        + previewImage.getWidth(null), insets.top + insets.bottom
                        + previewImage.getHeight(null));
                if (dim.width > PREVIEW_MAX_WIDTH)
                    dim.width = PREVIEW_MAX_WIDTH;
                if (dim.height > PREVIEW_MAX_HEIGHT)
                    dim.height = PREVIEW_MAX_HEIGHT;
                if (dim.width < 50)
                    dim.width = 50;
                if (dim.height < 50)
                    dim.height = 50;
                return dim;
            }

            public Dimension getMaximumSize() {
                return new Dimension(PREVIEW_MAX_WIDTH, PREVIEW_MAX_HEIGHT);
            }
        };

        DiagramStampOptionPanel() {
            setLayout(new GridBagLayout());
            defaultStampImagePanel = new JPanel(new GridBagLayout());
            JLabel defaultStampLabel = new JLabel(LocaleMgr.screen.getString("DefaultStampImage")
                    + ":");

            previewLabel.setText(LocaleMgr.screen.getString("Preview"));
            previewPanel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));

            defaultStampImagePanel.add(defaultStampLabel, new GridBagConstraints(0, 0, 1, 1, 1.0,
                    0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6, 6,
                            6, 5), 0, 0));
            defaultStampImagePanel.add(useStampImageCBox, new GridBagConstraints(0, 1, 1, 1, 1.0,
                    0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(6,
                            24, 6, 5), 0, 0));
            defaultStampImagePanel.add(stampImageFileTextField, new GridBagConstraints(0, 2, 1, 1,
                    1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(
                            0, 48, 6, 5), 0, 0));
            defaultStampImagePanel.add(browseStampImageButton, new GridBagConstraints(1, 2, 1, 1,
                    0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 3, 6,
                            5), 0, 0));
            defaultStampImagePanel.add(previewLabel, new GridBagConstraints(0, 3, 2, 1, 0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 27, 0, 5), 0,
                    0));
            defaultStampImagePanel.add(previewPanel, new GridBagConstraints(0, 4, 2, 1, 0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 48, 11, 5), 0,
                    0));

            add(defaultStampImagePanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18, 6,
                            0, 5), 0, 0));
            add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0,
                            0, 0), 0, 0));
            add(defButton, new GridBagConstraints(0, 3, 1, 0, 1.0, 0.0,
                    GridBagConstraints.SOUTHEAST, GridBagConstraints.VERTICAL, new Insets(6, 0, 6,
                            5), 0, 0));

            defButton.addActionListener(this);
            browseStampImageButton.addActionListener(this);
            stampImageFileTextField.setEditable(false);
            useStampImageCBox.addActionListener(this);
        }

        public void init() {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
            stampImageFileTextField.setText(prefs.getPropertyString(DiagramStampOptionGroup.class,
                    STAMP_IMAGE_PATH, STAMP_IMAGE_PATH_DEFAULT));
            useStampImageCBox.setSelected((prefs.getPropertyBoolean(DiagramStampOptionGroup.class,
                    USE_STAMP_IMAGE, USE_STAMP_IMAGE_DEFAULT).booleanValue()));
            stampImageFileTextField.setEnabled(useStampImageCBox.isSelected());
            browseStampImageButton.setEnabled(useStampImageCBox.isSelected());
            updatePreview(stampImageFileTextField.getText());
        }

        private void updatePreview(String imageName) {
            if (imageName == null || imageName.length() == 0)
                previewImage = null;
            else {
                try {
                    File file = new File(imageName);
                    URL url = ((file != null && file.exists()) ? file.toURL() : new URL(imageName));
                    previewImage = Toolkit.getDefaultToolkit().createImage(url);
                    if (previewImage != null)
                        GraphicUtil.waitForImage(previewImage);
                } catch (Exception e) {
                    previewImage = null;
                }
            }
            if (!useStampImageCBox.isSelected())
                previewImage.flush();
            previewPanel.invalidate();
            previewPanel.revalidate();
            invalidate();
            repaint();
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();

            if (source == browseStampImageButton) {
                JFileChooser chooser = new JFileChooser(DirectoryOptionGroup
                        .getDefaultWorkingDirectory());
                chooser.setAccessory(new ImagePreviewer(chooser));
                chooser.addChoosableFileFilter(ExtensionFileFilter.allImagesFilter);
                int retval = chooser.showDialog(this, null);

                if (retval == JFileChooser.APPROVE_OPTION) {
                    File theFile = chooser.getSelectedFile();

                    if (theFile != null && theFile.exists() && theFile.canRead()) {
                        stampImageFileTextField.setText(theFile.getAbsolutePath());
                        fireOptionChanged(prefs, DiagramStampOptionGroup.class, STAMP_IMAGE_PATH,
                                theFile.getAbsolutePath());
                        updatePreview(stampImageFileTextField.getText());
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(this, LocaleMgr.message
                                .getString("FileNotExistOrCantbeRead"));
                    }
                }
            } else if (source == useStampImageCBox) {
                fireOptionChanged(prefs, DiagramStampOptionGroup.class, USE_STAMP_IMAGE,
                        new Boolean(useStampImageCBox.isSelected()));
                stampImageFileTextField.setEnabled(useStampImageCBox.isSelected());
                defaultStampImageButton.setEnabled(useStampImageCBox.isSelected());
                browseStampImageButton.setEnabled(useStampImageCBox.isSelected());
                updatePreview(stampImageFileTextField.getText());
                previewPanel.invalidate();
                previewPanel.revalidate();
                invalidate();
                repaint();
            } else if (source == defButton) {
                stampImageFileTextField.setText(STAMP_IMAGE_PATH_DEFAULT);
                useStampImageCBox.setSelected(USE_STAMP_IMAGE_DEFAULT.booleanValue());
                stampImageFileTextField.setEnabled(useStampImageCBox.isSelected());
                defaultStampImageButton.setEnabled(useStampImageCBox.isSelected());
                browseStampImageButton.setEnabled(useStampImageCBox.isSelected());
                fireOptionChanged(prefs, DiagramStampOptionGroup.class, STAMP_IMAGE_PATH,
                        STAMP_IMAGE_PATH_DEFAULT);
                fireOptionChanged(prefs, DiagramStampOptionGroup.class, USE_STAMP_IMAGE,
                        USE_STAMP_IMAGE_DEFAULT);
                updatePreview(stampImageFileTextField.getText());
            }
        }

    };

    public DiagramStampOptionGroup() {
        super(LocaleMgr.screen.getString("Stamp"));
    }

    protected OptionPanel createOptionPanel() {
        return new DiagramStampOptionPanel();
    }

    // Service Methods to access this options group values

    public static boolean isUseStampImage() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyBoolean(DiagramStampOptionGroup.class, USE_STAMP_IMAGE,
                USE_STAMP_IMAGE_DEFAULT).booleanValue();
    }

    public static String getStampImagePath() {
        PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();
        return prefs.getPropertyString(DiagramStampOptionGroup.class, STAMP_IMAGE_PATH,
                STAMP_IMAGE_PATH_DEFAULT);
    }

}
