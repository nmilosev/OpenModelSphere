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
import java.awt.event.*;
import java.util.Locale;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.modelsphere.jack.international.LocaleChangeManager;
import org.modelsphere.jack.international.LocaleMgr;

//import org.modelsphere.jack.debug.TestableWindow;

public class FontChooserDialog extends JDialog implements ComponentTestable {
    private static String kFontSample = " " + LocaleMgr.screen.getString("FontSample");
    private JPanel intermediatePanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel samplePanel = new JPanel();
    private Font selectedFont = null;
    private JList list;
    private JComboBox sizeCombo;
    private JCheckBox boldCB = new JCheckBox(LocaleMgr.screen.getString("Bold"));
    private JCheckBox italicCB = new JCheckBox(LocaleMgr.screen.getString("Italic"));
    private JLabel sample = new JLabel(kFontSample);
    private JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JButton selectBtn = new JButton(LocaleMgr.screen.getString("Select"));;
    private int initialFontSize = 12;
    private String[] fontNames = getInstallFont();
    private String kSampleTitle = LocaleMgr.screen.getString("Sample");
    private GridLayout gridLayout1 = new GridLayout();

    // for unit testing
    public FontChooserDialog() {
        this(null, null, null);
    }

    public FontChooserDialog(Component comp, String title, Font preselectFont) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), title, true);
        jbInit();
        getRootPane().setDefaultButton(cancelBtn);
        /**
         * last initialization
         */
        if (preselectFont != null) {
            int index = getFontIndex(fontNames, preselectFont.getName());
            if (index != -1) {
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
            boldCB.setSelected(preselectFont.isBold());
            italicCB.setSelected(preselectFont.isItalic());
            initialFontSize = preselectFont.getSize();
            String size = Integer.toString(initialFontSize);
            sizeCombo.setSelectedItem(size);
        } else {
            list.setSelectedIndex(0);
            list.ensureIndexIsVisible(0);
            sizeCombo.setSelectedItem("12");
        }
        manageSelectButton();
        updateSample();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        setSize(getWidth() + 80, getHeight() + 60);

        setLocationRelativeTo(comp);
    }

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * selectBtn.setText("Select"); //NOT LOCALIZABLE cancelBtn.setText("Cancel"); //NOT
         * LOCALIZABLE boldCB.setText("Bold"); //NOT LOCALIZABLE italicCB.setText("Italic"); //NOT
         * LOCALIZABLE sample.setText("FontSample"); //NOT LOCALIZABLE kSampleTitle = "Sample";
         * //NOT LOCALIZABLE
         */

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        /* main components creation */
        list = new JList(fontNames);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane treePanel = new JScrollPane(list);
        sizeCombo = new JComboBox();
        sizeCombo.setEditable(true);
        sizeCombo.addItem("8"); // NOT LOCALIZABLE
        sizeCombo.addItem("9"); // NOT LOCALIZABLE
        sizeCombo.addItem("10"); // NOT LOCALIZABLE
        sizeCombo.addItem("11"); // NOT LOCALIZABLE
        sizeCombo.addItem("12"); // NOT LOCALIZABLE
        sizeCombo.addItem("14"); // NOT LOCALIZABLE
        sizeCombo.addItem("16"); // NOT LOCALIZABLE
        sizeCombo.addItem("18"); // NOT LOCALIZABLE
        sizeCombo.addItem("20"); // NOT LOCALIZABLE
        sizeCombo.addItem("22"); // NOT LOCALIZABLE
        sizeCombo.addItem("24"); // NOT LOCALIZABLE
        sizeCombo.addItem("26"); // NOT LOCALIZABLE
        sizeCombo.addItem("28"); // NOT LOCALIZABLE
        sizeCombo.addItem("36"); // NOT LOCALIZABLE
        sizeCombo.addItem("48"); // NOT LOCALIZABLE
        sizeCombo.addItem("72"); // NOT LOCALIZABLE
        sizeCombo.setMaximumRowCount(10);

        /* panels creation */
        intermediatePanel.setLayout(new GridBagLayout());
        southPanel.setLayout(gridLayout1);
        gridLayout1.setHgap(5);
        getContentPane().add(intermediatePanel);
        samplePanel.setLayout(new BorderLayout());
        samplePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createLoweredBevelBorder(), kSampleTitle));

        /* components adding */
        samplePanel.add(sample);
        southPanel.add(selectBtn);
        southPanel.add(cancelBtn);

        intermediatePanel.add(treePanel, new GridBagConstraints(0, 0, 3, 3, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 12, 0), 10,
                0));

        intermediatePanel.add(sizeCombo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 6, 12),
                0, 0));

        intermediatePanel.add(italicCB, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 12, 0, 12), 0,
                0));

        intermediatePanel.add(boldCB, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 12, 12),
                0, 0));

        intermediatePanel
                .add(samplePanel, new GridBagConstraints(0, 3, 4, 1, 1.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 12, 0, 12), 0, 0));

        intermediatePanel
                .add(southPanel, new GridBagConstraints(0, 4, 4, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(17, 12, 12, 12), 0, 0));

        /* main components actionListner Installation */
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                manageSelectButton();
                if (!e.getValueIsAdjusting())
                    updateSample();
            }
        });
        selectBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedFont = createFontFromSetting();
                dispose();
            }
        });
        boldCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSample();
            }
        });
        italicCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSample();
            }
        });
        sizeCombo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                updateSample();
            }
        });
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void updateSample() {
        final Font font = createFontFromSetting();
        Font sampleFont = new Font(font.getName(), font.getStyle(), font.getSize() + 2);
        Toolkit tk = Toolkit.getDefaultToolkit();
        int nScreenFontResolution = tk.getScreenResolution();
        double ratio = (double) nScreenFontResolution / (double) 72;
        double fsize = sampleFont.getSize();
        fsize = (fsize > 0 ? fsize : 1);
        final Font nf = new Font(sampleFont.getFontName(), sampleFont.getStyle(),
                (int) (fsize * ratio) + 1);
        sample.setFont(nf);

        if (nf != null) {
            // This is a workaround .... bad refresh on some font when updating
            // Bold or Italic

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    sample.setFont(nf);
                }
            });
        }
    }

    private Font createFontFromSetting() {
        String fontName = (String) list.getSelectedValue();
        String sizeStr = (String) sizeCombo.getSelectedItem();
        if (fontName != null && sizeStr != null && sizeStr.length() > 0) {
            int style = Font.PLAIN;
            if (boldCB.isSelected())
                style |= Font.BOLD;
            if (italicCB.isSelected())
                style |= Font.ITALIC;
            int size;
            try {
                size = Integer.parseInt(sizeStr);
            } catch (NumberFormatException ex) {
                size = initialFontSize;
            }
            return new Font(fontName, style, size);
        }
        return null;
    }

    private String[] getInstallFont() {
        Locale locale = LocaleChangeManager.getLocale();
        java.awt.GraphicsEnvironment ge = java.awt.GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        return fontFilter(ge.getAvailableFontFamilyNames(locale));
    }

    private String[] fontFilter(String[] fontFamilyNames) {
        int i, j;

        for (i = 0, j = 0; i < fontFamilyNames.length; i++) {
            if (!Character.isLowerCase(fontFamilyNames[i].charAt(0))) {
                Font font = new Font(fontFamilyNames[i], Font.PLAIN, 12);
                if (font.canDisplay('a')) {
                    j++;
                }
            }
        }

        String[] fontNames = new String[j];

        for (i = 0, j = 0; i < fontFamilyNames.length; i++) {
            if (!Character.isLowerCase(fontFamilyNames[i].charAt(0))) {
                Font font = new Font(fontFamilyNames[i], Font.PLAIN, 12);
                if (font.canDisplay('a')) {
                    fontNames[j] = fontFamilyNames[i];
                    j++;
                }
            }
        }

        return fontNames;
    }

    private int getFontIndex(String[] fontNames, String fontName) {
        int index = -1;
        for (int i = 0; i < fontNames.length; i++) {
            if (fontNames[i].equalsIgnoreCase(fontName)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void manageSelectButton() {
        selectBtn.setEnabled(!list.isSelectionEmpty());
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    // *************
    // DEMO FUNCTION
    // *************

    // IMPLEMENTS ComponentTestable
    public JComponent createOccurrence() {
        JComponent comp = new JPanel();
        JButton button = new JButton("Test");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                FontChooserDialog dialog = new FontChooserDialog(null, null, null);
                dialog.setVisible(true);
            }
        });

        comp.add(button);
        return comp;
    }

    // IMPLEMENTS TestableWindow
    public Window createTestWindow(Container owner) {
        FontChooserDialog dialog = new FontChooserDialog(null, null, null);
        return dialog;
    }

    private static void runDemo() {
        FontChooserDialog dialog = new FontChooserDialog(null, null, null);
        dialog.setVisible(true);
        boolean done = false;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (!dialog.isShowing()) {
                dialog.dispose();
                dialog = null;
                done = true;
            }
        } while (!done);
        System.exit(0);
    } // end runDemo()

    // Run the demo
    /*
     * public static void main(String[] args) { runDemo(); }
     */

}
