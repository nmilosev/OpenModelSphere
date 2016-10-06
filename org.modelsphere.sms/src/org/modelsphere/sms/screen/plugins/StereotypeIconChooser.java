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

package org.modelsphere.sms.screen.plugins;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.ImagePreviewer;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.util.Extensibility;
import org.modelsphere.sms.international.LocaleMgr;

public class StereotypeIconChooser extends JPanel {
    JRadioButton defaultOption = new JRadioButton();
    JRadioButton customOption = new JRadioButton();
    JButton browseButton = new JButton();
    JPanel imagePreviewContainer = new JPanel();
    ImagePreview imagePreviewPanel = new ImagePreview();
    TitledBorder titledBorder1;
    //JLabel stereotypeLabel = new JLabel();
    //JLabel stereotypeName = new JLabel();
    JButton cancelButton = new JButton();
    JButton okButton = new JButton();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    GridBagLayout gridBagLayout2 = new GridBagLayout();
    JLabel imageLabel = new JLabel();
    JTextField imageName = new JTextField();
    JPanel okCancelPanel = new JPanel();

    private static HashMap g_stereotypeMap = new HashMap(); //keep previous values
    private StereotypeStructure m_stereotypeStruct;
    private DbSMSStereotype m_stereotype;
    private boolean isCancelled = true; //until OK is pressed
    private JDialog m_dialog = null;

    //TO LOCALIZED
    //  private static final String TITLE = LocaleMgr.screen.getString("StereotypeIconSelection");
    private static final String IMAGE_PREVIEW = LocaleMgr.screen.getString("ImagePreview");
    private static final String USE_THE_DEFAULT_ICON = LocaleMgr.screen
            .getString("UseTheDefaultIcon");
    private static final String SELECT_CUSTOM_ICON = LocaleMgr.screen
            .getString("SelectACustomizedIcon");
    private static final String SELECT = LocaleMgr.screen.getString("Select") + "...";
    private static final String SELECT_IMAGE_FOR = LocaleMgr.screen.getString("SelectAnIconFor");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String OK = LocaleMgr.screen.getString("Ok");
    private static final String IMAGE_NAME = LocaleMgr.screen.getString("FileName");
    private static final String TRANSACTION_NAME = LocaleMgr.action
            .getString("UMLStereotypeIconUpdate");

    String getTransName() {
        return TRANSACTION_NAME;
    }

    private StereotypeIconChooser() throws DbException {
        m_stereotype = null;
        m_stereotypeStruct = new StereotypeStructure("actor", null); //NOT LOCALIZABLE, for unit testing
        try {
            jbInit();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StereotypeIconChooser(DbSMSStereotype stereotype) throws DbException {
        try {
            m_stereotype = stereotype;
            m_stereotypeStruct = (StereotypeStructure) g_stereotypeMap.get(stereotype);
            if (m_stereotypeStruct == null) {
                stereotype.getDb().beginReadTrans();
                String name = stereotype.getName();
                Image icon = stereotype.getIcon();
                stereotype.getDb().commitTrans();
                m_stereotypeStruct = new StereotypeStructure(name, icon);
            } //end if

            if (m_stereotypeStruct.m_isDefault) {
                imagePreviewPanel.setImage(m_stereotypeStruct.m_defaultImage);
            } else {
                imagePreviewPanel.setImage(m_stereotypeStruct.m_customImage);
            }

            jbInit();
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //To view with JBuilder
    private void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        defaultOption.setText("Use the Default Icon"); //NOT LOCALIZABLE, JBuilder code
        this.setLayout(gridBagLayout1);

        customOption.setText("Select a Custom Icon"); //NOT LOCALIZABLE, JBuilder code
        browseButton.setText("Select..."); //NOT LOCALIZABLE, JBuilder code
        imagePreviewContainer.setBorder(titledBorder1);
        //    stereotypeLabel.setText("Select image for:"); //NOT LOCALIZABLE, JBuilder code
        //    stereotypeName.setText("boundary"); //NOT LOCALIZABLE, JBuilder code
        cancelButton.setText("Cancel"); //NOT LOCALIZABLE, JBuilder code
        okButton.setText("OK"); //NOT LOCALIZABLE, JBuilder code
        imageLabel.setText("image name:"); //NOT LOCALIZABLE, JBuilder code
        imageName.setText("image.gif"); //NOT LOCALIZABLE, JBuilder code
        //    this.add(stereotypeLabel,   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        //            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 24, 12), 0, 0));
        //    this.add(stereotypeName,      new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
        //            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 6, 24, 12), 20, 0));

        this.add(defaultOption, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 6), 0, 0));
        this.add(customOption, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 6), 0, 0));

        this.add(imageLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(6, 24, 12, 6), 0,
                0));
        this.add(imageName, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 6, 6), 80, 0));
        this.add(browseButton,
                new GridBagConstraints(2, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 0, 0));

        //Image preview
        this.add(imagePreviewContainer, new GridBagConstraints(3, 1, 2, 3, 1.0, 1.0,
                GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(12, 12, 12, 12),
                80, 80));
        imagePreviewContainer.setLayout(new GridBagLayout());
        imagePreviewContainer.add(imagePreviewPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
        imagePreviewPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        //Ok, Cancel buttons
        this.add(okCancelPanel, new GridBagConstraints(0, 4, 5, 1, 1.0, 0.0,
                GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 12, 6),
                0, 0));
        okCancelPanel.setLayout(gridBagLayout2);
        okCancelPanel.add(cancelButton,
                new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
        okCancelPanel.add(okButton,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));

    } //end jbInit()

    private void init() throws DbException {
        //Set localized test
        titledBorder1.setTitle(IMAGE_PREVIEW);
        defaultOption.setText(USE_THE_DEFAULT_ICON);
        customOption.setText(SELECT_CUSTOM_ICON);
        browseButton.setText(SELECT);
        //    stereotypeLabel.setText(SELECT_IMAGE_FOR);
        //    stereotypeName.setText("");
        cancelButton.setText(CANCEL);
        okButton.setText(OK);
        imageLabel.setText(IMAGE_NAME);
        imageName.setText("");

        //set other setting
        //    stereotypeName.setText(m_stereotypeStruct.m_name);
        //stereotypeName.setEditable(false);
        //Color fg = stereotypeName.getForeground();
        //    stereotypeName.setBorder(new BevelBorder(BevelBorder.LOWERED));
        imageName.setText(m_stereotypeStruct.m_imageName);
        imageName.setEditable(false);
        imagePreviewPanel.repaint();

        //create button group
        ButtonGroup group = new ButtonGroup();
        group.add(defaultOption);
        group.add(customOption);
        if (m_stereotype != null) {
            Db db = m_stereotype.getDb();
            db.beginReadTrans();
            if (!m_stereotype.isBuiltIn()) {
                m_stereotypeStruct.m_isDefault = false;
                defaultOption.setEnabled(false);
            }
            db.commitTrans();
        }

        if (m_stereotypeStruct.m_isDefault) {
            defaultOption.setSelected(true);
        } else {
            customOption.setSelected(true);
        }
        imageName.setEnabled(!m_stereotypeStruct.m_isDefault);
        browseButton.setEnabled(!m_stereotypeStruct.m_isDefault);

        //Add listeners
        addListeners();

        //set buttons
        //Color fg = imageLabel.getForeground();
        //Border border = new LineBorder(fg);
        //okCancelPanel.setBorder(border);

        Dimension dim = browseButton.getPreferredSize();
        cancelButton.setPreferredSize(dim);
        okButton.setPreferredSize(dim);
        //int width = (int)imageName.getPreferredSize().getWidth(); //store width
        //imageName.setPreferredSize(new Dimension(width, (int)dim.getHeight())); //align to browse button
    }

    //Add listeners
    private StereotypeIconChooser m_chooser;

    private void addListeners() {
        m_chooser = this;
        defaultOption.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                imageName.setEnabled(false);
                browseButton.setEnabled(false);
                okButton.setEnabled(true);
                m_stereotypeStruct.m_isDefault = true;
                imagePreviewPanel.setImage(m_stereotypeStruct.m_defaultImage);
                imagePreviewPanel.repaint();
            }
        });

        customOption.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ev) {
                imageName.setEnabled(true);
                browseButton.setEnabled(true);
                okButton.setEnabled(m_stereotypeStruct.m_imageName != null);
                m_stereotypeStruct.m_isDefault = false;
                imagePreviewPanel.setImage(m_stereotypeStruct.m_customImage);
                imagePreviewPanel.repaint();
            }
        });

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                browseImage();
            }
        });

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (g_stereotypeMap.containsKey(m_stereotype)) {
                    g_stereotypeMap.remove(m_stereotype);
                }

                g_stereotypeMap.put(m_stereotype, m_stereotypeStruct);
                isCancelled = false;
                m_chooser.m_dialog.dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                isCancelled = true;
                m_chooser.m_dialog.dispose();
            }
        });
    } //end addListeners()

    private void browseImage() {
        JFileChooser chooser;
        if (m_stereotypeStruct.m_customImageFolder == null) {
            chooser = new JFileChooser();
        } else {
            chooser = new JFileChooser(m_stereotypeStruct.m_customImageFolder);
        }

        chooser.setAccessory(new ImagePreviewer(chooser));
        chooser.addChoosableFileFilter(ExtensionFileFilter.allImagesFilter);
        int retval = chooser.showDialog(this, null);

        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            if (theFile != null && theFile.exists() && theFile.canRead()) {
                try {
                    URL url = theFile.toURL();
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        m_stereotypeStruct.m_imageName = theFile.getName();
                        m_stereotypeStruct.m_customImageFolder = theFile.getParentFile();
                        m_stereotypeStruct.m_customImage = icon.getImage();
                        m_stereotypeStruct.m_isDefault = false;
                        imageName.setText(m_stereotypeStruct.m_imageName);
                        imagePreviewPanel.setImage(m_stereotypeStruct.m_customImage);
                        imagePreviewPanel.repaint();
                        okButton.setEnabled(true);
                    }
                } catch (MalformedURLException ex) {
                    //ignore
                }
            }
        } //end if
    } //end browseImage

    boolean isCancelled() {
        return isCancelled;
    }

    Image getImage() {
        if (m_stereotypeStruct.m_isDefault) {
            return m_stereotypeStruct.m_defaultImage;
        } else {
            return m_stereotypeStruct.m_customImage;
        }
    } //end getImage()

    public JDialog getDialog(JFrame owner) {
        //create dialog
        String title = MessageFormat.format(SELECT_IMAGE_FOR,
                new Object[] { m_stereotypeStruct.m_name });
        m_dialog = new JDialog(owner, title, true);
        m_dialog.getContentPane().add(this);
        m_dialog.pack();
        AwtUtil.centerWindow(m_dialog);
        return m_dialog;
    }

    private static class ImagePreview extends JPanel {
        private Image m_image = null;

        ImagePreview() {
            this.setBackground(Color.white);
        }

        protected void paintChildren(Graphics g) {
            int x = super.getWidth() / 2;
            int y = super.getHeight() / 2;

            if (m_image != null) {
                int width = m_image.getHeight((ImageObserver) null);
                int height = m_image.getHeight((ImageObserver) null);
                /*
                 * Squeeze the image by 80x80 if (width > 80) width = 80; if (height > 80) height =
                 * 80;
                 */
                int dx = width / 2;
                int dy = height / 2;
                g.drawImage(m_image, x - dx, y - dy, width, height, (ImageObserver) null);
            }
        } //end printChildren()

        void setImage(Image image) {
            m_image = image;
        }
    } //end ImagePreview

    private static class StereotypeStructure {
        private String m_name;
        private String m_imageName = null;
        private Image m_defaultImage;
        private Image m_customImage = null;
        private boolean m_isDefault = true;
        private File m_customImageFolder = null;

        StereotypeStructure(String name, Image defaultImage) {
            m_name = name;
            //m_imageName = imageName;
            m_defaultImage = defaultImage;
        }
    } //end StereotypeStructure

    public static void main(String[] args) throws DbException {
        JFrame frame = new JFrame("Select Stereotype Icon"); //NOT LOCALIZABLE, unit test

        //create a stereotype for testing
        Image image = Extensibility.getImage("boundary.gif"); //NOT LOCALIZABLE, unit test
        StereotypeStructure stereotype = new StereotypeStructure("boundary", image); //NOT LOCALIZABLE, unit test

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(1, 1));
        contentPane.add(new StereotypeIconChooser());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.pack();
        frame.setVisible(true);
    } //end main()
} //end StereotypeIconViewer
