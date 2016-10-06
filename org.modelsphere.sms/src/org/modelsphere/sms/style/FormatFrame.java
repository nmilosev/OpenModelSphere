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

package org.modelsphere.sms.style;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.ColorIcon;
import org.modelsphere.jack.awt.JackColorChooser;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.util.ExceptionHandler;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.international.LocaleMgr;

public class FormatFrame extends JDialog {
    private DbSMSProject project;

    // String section
    private static final String APPLY = LocaleMgr.screen.getString("Apply");
    private static final String BACKGROUND = LocaleMgr.screen.getString("Background");
    private static final String BORDER = LocaleMgr.screen.getString("Border");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String CHOOSE_COLOR = LocaleMgr.screen.getString("ChooseColor");
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String DASH_LINE = LocaleMgr.screen.getString("DashLine");
    private static final String FORMAT_TITLE = LocaleMgr.screen.getString("Format");
    private static final String FORMAT_MODIFICATION = LocaleMgr.action
            .getString("formatModification");
    private static final String HIGHLIGHT = LocaleMgr.screen.getString("Highlight");
    private static final String TEXT = LocaleMgr.screen.getString("Text");
    private static final String THREE_DOT = LocaleMgr.screen.getString("ThreeDot");
    private static final String TAB_FONT = LocaleMgr.screen.getString("Font");
    private static final String TAB_COLOR = LocaleMgr.screen.getString("Color");
    private static final String TAB_LINE_STYLE = LocaleMgr.screen.getString("LineStyle");

    private static final Dimension DIMENSION_COLOR = new Dimension(20, 10);
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel colorPanel = new JPanel();
    private JPanel lineStylePanel = new JPanel();
    private JCheckBox cbHighlight = new JCheckBox(HIGHLIGHT);
    private JCheckBox cbDashLine = new JCheckBox(DASH_LINE);
    private JLabel textLabel = new JLabel(TEXT);
    private JLabel backgrdLabel = new JLabel(BACKGROUND);
    private JLabel borderLabel = new JLabel(BORDER);
    private JButton btnText = new JButton(THREE_DOT);
    private JButton btnBackground = new JButton(THREE_DOT);
    private JButton btnBorder = new JButton(THREE_DOT);
    private TitledBorder titledBorder1 = new TitledBorder(FORMAT_TITLE);
    private JPanel controlBtnPanel = new JPanel();
    private JButton btnClose = new JButton(CLOSE);
    private JButton btnApply = new JButton(APPLY);
    private GridLayout gridLayout1 = new GridLayout();
    private Color TextColor;
    private Color BackgroundColor;
    private Color BorderColor;
    private Color newBackgroundColor = null;
    private Color newBorderColor = null;
    private Color newTextColor = null;
    private Boolean newHighlight = null;
    private Boolean newDashLine = null;

    private FormatFrame(Frame frame, DbSMSProject project) throws DbException {
        super(frame, FORMAT_TITLE, true);
        this.project = project;
        jbInit();
        this.pack();
        Dimension dim = getSize();
        setSize(Math.max(dim.width, 500), Math.max(dim.height, 300));
        this.setLocationRelativeTo(frame);
    }

    public static void showFormatFrame(DbSMSProject project) {
        FormatFrame formatFrame = null;
        try {
            project.getDb().beginReadTrans();
            formatFrame = new FormatFrame(ApplicationContext.getDefaultMainFrame(), project);
            project.getDb().commitTrans();
            formatFrame.setVisible(true);
        } catch (DbException e) {
            if (formatFrame != null)
                formatFrame.dispose();
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), e);
        }
    }

    private void jbInit() throws DbException {
        final Object[] objects = org.modelsphere.jack.srtool.ApplicationContext.getFocusManager()
                .getSelectedObjects();

        // Color Tab
        colorPanel.setLayout(new GridBagLayout());
        // Background
        BackgroundColor = (Color) getObjectValue(objects, "m_backgroundColor");// NOT
        // LOCALIZABLE
        if (BackgroundColor == null) {
            btnBackground.setIcon(new ColorIcon(btnBackground.getBackground(), 24, 12));
            btnBackground.setDisabledIcon(new ColorIcon(btnBackground.getBackground(), 24, 12));
            btnBackground.setEnabled(false);
            backgrdLabel.setEnabled(false);
        } else {
            btnBackground.setIcon(new ColorIcon(BackgroundColor == null ? null : new Color(
                    BackgroundColor.getRGB(), false), 24, 12));
            btnBackground.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    newBackgroundColor = selectColor(btnBackground, BackgroundColor, true);
                    if (newBackgroundColor != null
                            && (BackgroundColor == null || !BackgroundColor
                                    .equals(newBackgroundColor))) {
                        BackgroundColor = newBackgroundColor;
                        btnBackground.setIcon(new ColorIcon(BackgroundColor == null ? null
                                : new Color(BackgroundColor.getRGB(), false), 24, 12));
                        setApply(true);
                    } else
                        newBackgroundColor = BackgroundColor;
                }
            });
        }
        // Border
        BorderColor = (Color) getObjectValue(objects, "m_lineColor"); // NOT
        // LOCALIZABLE
        if (BorderColor == null) {
            btnBorder.setIcon(new ColorIcon(btnBorder.getBackground(), 24, 12));
            btnBorder.setDisabledIcon(new ColorIcon(btnBorder.getBackground(), 24, 12));
            btnBorder.setEnabled(false);
            borderLabel.setEnabled(false);
        } else {
            btnBorder.setIcon(new ColorIcon(BorderColor == null ? null : new Color(BorderColor
                    .getRGB(), false), 24, 12));
            btnBorder.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    newBorderColor = selectColor(btnBorder, BorderColor, true);
                    if (newBorderColor != null
                            && (BorderColor == null || !BorderColor.equals(newBorderColor))) {
                        BorderColor = newBorderColor;
                        btnBorder.setIcon(new ColorIcon(BorderColor == null ? null : new Color(
                                BorderColor.getRGB(), false), 24, 12));
                        setApply(true);
                    } else
                        newBorderColor = BorderColor;
                }
            });
        }
        // Text
        TextColor = (Color) getObjectValue(objects, "m_textColor"); // NOT
        // LOCALIZABLE
        if (TextColor == null) {
            btnText.setIcon(new ColorIcon(btnText.getBackground(), 24, 12));
            btnText.setDisabledIcon(new ColorIcon(btnText.getBackground(), 24, 12));
            btnText.setEnabled(false);
            textLabel.setEnabled(false);
        } else {
            btnText.setIcon(new ColorIcon(TextColor == null ? null : new Color(TextColor.getRGB(),
                    false), 24, 12));
            btnText.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    newTextColor = selectColor(btnText, TextColor, true);
                    if (newTextColor != null
                            && (TextColor == null || !TextColor.equals(newTextColor))) {
                        TextColor = newTextColor;
                        // btnText.setBackground(TextColor);
                        // btnText.setForeground(AwtUtil.getContrastBlackOrWhite(TextColor));
                        btnText.setIcon(new ColorIcon(TextColor == null ? null : new Color(
                                TextColor.getRGB(), false), 24, 12));
                        setApply(true);
                    } else
                        newTextColor = TextColor;
                }
            });
        }

        // LineStyle Tab
        lineStylePanel.setLayout(new GridBagLayout());
        // Dash Line
        Boolean dashLineValue = (Boolean) getObjectValue(objects, "m_dashStyle"); // NOT
        // LOCALIZABLE
        if (dashLineValue != null) {
            cbDashLine.setSelected(dashLineValue.booleanValue());
            cbDashLine.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (cbDashLine.isSelected())
                        newDashLine = Boolean.TRUE;
                    else
                        newDashLine = Boolean.FALSE;
                    setApply(true);
                }
            });
        } else
            cbDashLine.setEnabled(false);
        // Highlight
        Boolean highlightValue = (Boolean) getObjectValue(objects, "m_highlight");// NOT LOCALIZABLE
        if (highlightValue != null) {
            cbHighlight.setSelected(highlightValue.booleanValue());
            cbHighlight.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    if (cbHighlight.isSelected())
                        newHighlight = Boolean.TRUE;
                    else
                        newHighlight = Boolean.FALSE;
                    setApply(true);
                }
            });
        } else
            cbHighlight.setEnabled(false);

        this.getContentPane().setLayout(new GridBagLayout());

        // Button Panel
        btnClose.setHorizontalAlignment(SwingConstants.RIGHT);
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        setApply(false);
        btnApply.setEnabled(false);
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyChanges(objects);
            }
        });
        controlBtnPanel.setLayout(gridLayout1);
        gridLayout1.setHgap(6);
        controlBtnPanel.add(btnApply, null);
        controlBtnPanel.add(btnClose, null);

        // Color Tab Constraints
        tabbedPane.add(colorPanel, TAB_COLOR);
        colorPanel.add(backgrdLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        colorPanel.add(borderLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));
        colorPanel.add(textLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));
        colorPanel.add(btnBackground, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 0, 6, 6), 0, 0));
        colorPanel.add(btnBorder, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        colorPanel.add(btnText, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        colorPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 3, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        colorPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 3, 3, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        // Line Style Tab Constraints
        tabbedPane.add(lineStylePanel, TAB_LINE_STYLE);
        lineStylePanel.add(cbDashLine, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        lineStylePanel.add(cbHighlight, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));
        lineStylePanel.add(Box.createHorizontalGlue(), new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        lineStylePanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        tabbedPane.setEnabled(true);
        this.getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        this.getContentPane().add(
                tabbedPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
    }

    private Object getObjectValue(Object[] objects, String fieldName) throws DbException {
        for (int i = 0; i < objects.length; i++) {
            DbObject object = ((ActionInformation) objects[i]).getGraphicalObject();
            if (object instanceof DbSMSGraphicalObject) {
                MetaField metaFieldName = (((DbSMSGraphicalObject) object).getMetaField(fieldName));
                if (metaFieldName != null) {
                    object.getDb().beginTrans(Db.READ_TRANS);
                    Object value = ((DbSMSGraphicalObject) object).find(metaFieldName);
                    object.getDb().commitTrans();
                    return value;
                }
            }
        }
        return null;
    }

    private Color selectColor(JButton btn, Color color, boolean transparency) {
        return JackColorChooser.showDialog(btn, CHOOSE_COLOR, color, transparency);
    }

    private final void setApply(boolean state) {
        btnApply.setEnabled(state);
        btnClose.setText(state ? CANCEL : CLOSE);
    }

    private void applyChanges(Object[] objects) {
        DbObject object;
        MetaField metaFieldName;

        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, FORMAT_MODIFICATION);
            for (int i = 0; i < objects.length; i++) {
                object = ((ActionInformation) objects[i]).getGraphicalObject();
                if (newBackgroundColor != null) {
                    metaFieldName = (((DbSMSGraphicalObject) object)
                            .getMetaField("m_backgroundColor")); // NOT
                    // LOCALIZABLE
                    if (metaFieldName != null)
                        object.set(metaFieldName, newBackgroundColor);
                }
                if (newBorderColor != null) {
                    metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_lineColor")); // NOT LOCALIZABLE
                    if (metaFieldName != null)
                        object.set(metaFieldName, newBorderColor);
                }
                if (newTextColor != null) {
                    metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_textColor")); // NOT LOCALIZABLE
                    if (metaFieldName != null)
                        object.set(metaFieldName, newTextColor);
                }
                if (newHighlight != null) {
                    metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_highlight")); // NOT LOCALIZABLE
                    if (metaFieldName != null)
                        object.set(metaFieldName, newHighlight);
                }
                if (newDashLine != null) {
                    metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_dashStyle")); // NOT LOCALIZABLE
                    if (metaFieldName != null)
                        object.set(metaFieldName, newDashLine);
                }
            }
            DbMultiTrans.commitTrans(objects);

            ApplicationDiagram ad = ApplicationContext.getFocusManager().getActiveDiagram();
            if (ad != null) {
                Db db = ad.getDiagramGO().getDb();
                db.beginTrans(Db.READ_TRANS);
                ad.getDiagramInternalFrame().refresh();
                db.commitTrans();
            }

        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(this, ex);
        }
        setApply(false);
    }

    private final void close() {
        this.dispose();
    }

    // reset the GO to the default style
    public static void resetToDefaultStyle(Object[] objects) throws DbException {
        DbObject object;
        MetaField metaFieldName;

        DbMultiTrans.beginTrans(Db.WRITE_TRANS, objects, FORMAT_MODIFICATION);
        for (int i = 0; i < objects.length; i++) {
            object = ((ActionInformation) objects[i]).getGraphicalObject();
            metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_backgroundColor")); // NOT LOCALIZABLE
            if (metaFieldName != null)
                object.set(metaFieldName, null);
            metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_lineColor")); // NOT LOCALIZABLE
            if (metaFieldName != null)
                object.set(metaFieldName, null);
            metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_textColor")); // NOT LOCALIZABLE
            if (metaFieldName != null)
                object.set(metaFieldName, null);
            metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_highlight")); // NOT LOCALIZABLE
            if (metaFieldName != null)
                object.set(metaFieldName, null);
            metaFieldName = (((DbSMSGraphicalObject) object).getMetaField("m_dashStyle")); // NOT LOCALIZABLE
            if (metaFieldName != null)
                object.set(metaFieldName, null);
        }
        DbMultiTrans.commitTrans(objects);

    }

    // *************
    // DEMO FUNCTION
    // *************

    private FormatFrame() throws DbException {
        this(null, null);
        try {
            this.pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end FormatFrame()

    private static void runDemo() throws DbException {
        FormatFrame dialog = new FormatFrame();
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
    } // end demo()

    /*
     * //Run the demo static public void main(String[] args) { runDemo(); } //end main()
     */
} // end FormatFrame

