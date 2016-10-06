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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.modelsphere.jack.awt.FontChooserDialog;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.baseDb.db.srtypes.PageNoPosition;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;

public class SetPageNumberPositionDialog extends JDialog implements TestableWindow {
    /* Use here appropriate LocalMgr calls */
    private boolean accepted = false;
    private boolean showPageNum = false;
    private int pageNumPosition;
    private ApplicationDiagram diag;
    private Font newFont = null;

    private JPanel mainPane = new JPanel();
    public static final String kSetPageNumberPosition = LocaleMgr.screen
            .getString("SetPageNumberPosition");
    private JCheckBox checkBox = new JCheckBox(LocaleMgr.screen.getString("ShowPageNumber"));
    public static final char kShowPageNumberMnc = LocaleMgr.screen.getMnemonic("ShowPageNumber");

    /* Position panel */
    private JPanel positionPane = new JPanel();
    private String positionTitle = LocaleMgr.screen.getString("positionLabel");
    private JPanel radioPanel = new JPanel();
    private ButtonGroup radioGroup = new ButtonGroup();
    private JRadioButton radioTopLeft = new JRadioButton();
    private JRadioButton radioTopCenter = new JRadioButton();
    private JRadioButton radioTopRight = new JRadioButton();
    private JRadioButton radioBottomLeft = new JRadioButton();
    private JRadioButton radioBottomCenter = new JRadioButton();
    private JRadioButton radioBottomRight = new JRadioButton();

    /* Font panel */
    private JPanel fontPane = new JPanel();
    private String fontTitle = LocaleMgr.screen.getString("FontPanelLabel");
    private JLabel fontLabel = new JLabel();
    private JButton lookupBtn = new JButton("...");

    /* Button panel */
    private JPanel buttonPanel = new JPanel();
    private JButton okButton = new JButton(LocaleMgr.screen.getString("OK"));
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
    private GridLayout gridLayout1 = new GridLayout();

    public SetPageNumberPositionDialog(Component comp, ApplicationDiagram diagram) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), kSetPageNumberPosition, true);
        jbInit();
        diag = diagram;
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        populateContents();
        this.pack();
    }

    void jbInit() {
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * okButton.setText("OK"); //NOT LOCALIZABLE cancelButton.setText("Cancel"); //NOT
         * LOCALIZABLE radioTopLeft.setText(""); //NOT LOCALIZABLE radioTopCenter.setText(""); //NOT
         * LOCALIZABLE radioTopRight.setText(""); //NOT LOCALIZABLE radioBottomLeft.setText("");
         * //NOT LOCALIZABLE radioBottomCenter.setText(""); //NOT LOCALIZABLE
         * radioBottomRight.setText(""); //NOT LOCALIZABLE fontLabel.setText("Fonte xyz Bold 14");
         * //NOT LOCALIZABLE checkBox.setText("Show Page Number"); //NOT LOCALIZABLE
         * checkBox.setMnemonic('S'); //NOT LOCALIZABLE fontTitle = "Font"; //NOT LOCALIZABLE
         * positionTitle = "Position"; //NOT LOCALIZABLE
         */

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        /* Main Panel */
        mainPane.setLayout(new GridBagLayout());
        getContentPane().add(mainPane);
        mainPane.add(checkBox, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 0, 12),
                0, 0));
        checkBox.setMnemonic(kShowPageNumberMnc);
        checkBox.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (((JCheckBox) e.getSource()).isSelected())
                    showPageNum = true;
                else
                    showPageNum = false;
                setPanelsEnabled(showPageNum);
            }
        });

        /* Position Panel */
        positionPane.setLayout(new GridBagLayout());
        positionPane.setBorder(BorderFactory.createTitledBorder(positionTitle));
        radioPanel.setLayout(new GridBagLayout());
        radioPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        initRadioButton();
        mainPane.add(positionPane, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(11, 12, 0, 5), 0, 0));
        positionPane.add(radioPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 15, 15, 15), 0,
                0));
        radioPanel.add(radioTopLeft, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 6, 25, 3), 0, 0));
        radioPanel.add(radioTopCenter, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(6, 3, 25, 3), 0, 0));
        radioPanel.add(radioTopRight, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(6, 3, 25, 0), 0, 0));
        radioPanel.add(radioBottomLeft, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(25, 6, 6, 3), 0, 0));
        radioPanel.add(radioBottomCenter, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(25, 3, 6, 3), 0, 0));
        radioPanel.add(radioBottomRight, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(25, 3, 6, 0), 0, 0));

        /* Font Panel */
        fontPane.setLayout(new GridBagLayout());
        fontPane.setBorder(BorderFactory.createTitledBorder(fontTitle));
        mainPane.add(fontPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(11, 6, 0, 12), 0, 0));
        fontPane
                .add(fontLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(12, 12, 12, 12), 0, 0));
        fontPane
                .add(lookupBtn, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.BOTH,
                        new Insets(12, 0, 12, 12), 0, -5));

        lookupBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                Object o = this;
                FontChooserDialog fc = new FontChooserDialog(
                        org.modelsphere.jack.srtool.ApplicationContext.getDefaultMainFrame(),
                        LocaleMgr.screen.getString("ChooseFont"), Diagram.defaultFont);
                fc.setVisible(true);
                if (fc.getSelectedFont() != null) {
                    newFont = fc.getSelectedFont();
                }
                if (newFont != null) {
                    setPageNoFont(newFont);
                }
            }
        });

        /* Button Panel */
        gridLayout1.setHgap(5);
        buttonPanel.setLayout(gridLayout1);
        mainPane.add(buttonPanel, new GridBagConstraints(0, 2, 2, 1, 0.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(17, 12, 12, 12),
                0, 0));
        buttonPanel.add(okButton, null);
        buttonPanel.add(cancelButton, null);
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                accepted = true;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        getRootPane().setDefaultButton(okButton);
        new HotKeysSupport(this, cancelButton, null);
    }

    public final boolean isAccepted() {
        return accepted;
    }

    private final void initRadioButton() {
        radioGroup = new ButtonGroup();

        radioGroup.add(radioTopLeft);
        radioGroup.add(radioTopCenter);
        radioGroup.add(radioTopRight);
        radioGroup.add(radioBottomLeft);
        radioGroup.add(radioBottomCenter);
        radioGroup.add(radioBottomRight);

        radioTopLeft.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_TOP_LEFT));
        radioTopCenter.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_TOP_CENTER));
        radioTopRight.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_TOP_RIGHT));
        radioBottomLeft.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_BOTTOM_LEFT));
        radioBottomCenter.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_BOTTOM_CENTER));
        radioBottomRight.setActionCommand(Integer.toString(PageNoPosition.PAGE_NO_BOTTOM_RIGHT));

        radioTopLeft.addChangeListener(new RadioListener());
        radioTopCenter.addChangeListener(new RadioListener());
        radioTopRight.addChangeListener(new RadioListener());
        radioBottomLeft.addChangeListener(new RadioListener());
        radioBottomCenter.addChangeListener(new RadioListener());
        radioBottomRight.addChangeListener(new RadioListener());

    }

    private class RadioListener implements ChangeListener {
        RadioListener() {
        }

        public void stateChanged(ChangeEvent e) {
            if (((JRadioButton) e.getSource()).isSelected()) {
                pageNumPosition = Integer
                        .valueOf(((JRadioButton) e.getSource()).getActionCommand()).intValue();
            }
        }

    }

    private final void setPanelsEnabled(boolean b) {
        if (b)
            radioPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        else
            radioPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        for (Enumeration e = radioGroup.getElements(); e.hasMoreElements();) {
            ((JRadioButton) e.nextElement()).setEnabled(b);
        }
        fontLabel.setEnabled(b);
        lookupBtn.setEnabled(b);
    }

    /**
     * return one of the radiobutton value or the checkbox value see PageNoPosition domain.
     */
    public final int getPageNoPos() {
        if (!showPageNum)
            return PageNoPosition.PAGE_NO_NONE;
        else
            return pageNumPosition;
    }

    /**
     * use only to set the radiobutton
     */
    private final void setPosition(int value) {
        String sValue = Integer.toString(value);
        for (Enumeration e = radioGroup.getElements(); e.hasMoreElements();) {
            JRadioButton rB = (JRadioButton) e.nextElement();
            if (rB.getActionCommand().equals(sValue))
                rB.setSelected(true);
            else
                rB.setSelected(false);
        }
    }

    private final void setPageNoFont(Font f) {
        newFont = f;
        fontLabel.setText(fontToString(f));
        this.pack();
    }

    public final Font getPageNoFont() {
        return newFont;
    }

    private final void populateContents() {
        Font currentFont = null;
        if (diag != null) {
            currentFont = diag.getPageNoFont();
        }

        if (currentFont == null)
            currentFont = Diagram.defaultFont;
        setPageNoFont(currentFont);

        if (diag != null) {
            if (diag.getPageNoPos() == PageNoPosition.PAGE_NO_NONE) {
                checkBox.setSelected(false);
                setPosition(PageNoPosition.PAGE_NO_BOTTOM_RIGHT);
            } // end if
            else {
                checkBox.setSelected(true);
                if (diag != null) {
                    setPosition(diag.getPageNoPos());
                }
            }
        }

        setPanelsEnabled(checkBox.isSelected());
    }

    private String fontToString(Font f) {
        if (f == null)
            return "";
        String strStyle;

        if (f.isBold()) {
            strStyle = f.isItalic() ? "BoldItalic" : "Bold"; // NOT LOCALIZABLE
        } else {
            strStyle = f.isItalic() ? "Italic" : "Plain"; // NOT LOCALIZABLE
        }
        return f.getName() + ", " + LocaleMgr.screen.getString(strStyle) + ", " + f.getSize();
    }

    // *************
    // DEMO FUNCTION
    // *************

    public SetPageNumberPositionDialog() {
        this(null, null);
        try {
            jbInit();
            this.pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Window createTestWindow(Container owner) {
        SetPageNumberPositionDialog dialog = new SetPageNumberPositionDialog();
        return dialog;
    }

    private static void runDemo() {
        SetPageNumberPositionDialog dialogue = new SetPageNumberPositionDialog();
        dialogue.setVisible(true);
        boolean done = false;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (!dialogue.isShowing()) {
                dialogue.dispose();
                dialogue = null;
                done = true;
            }
        } while (!done);
        System.exit(0);
    } // end main()

    // Run the demo
    public static void main(String[] args) {
        runDemo();
    }

} // end SetPageNumberPositionDialog

