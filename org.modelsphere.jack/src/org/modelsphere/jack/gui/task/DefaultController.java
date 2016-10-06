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

package org.modelsphere.jack.gui.task;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.JTextAreaFix;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;

/**
 * This class provide a default implementation for Controller.
 * 
 * It provide logging support, optionnal progressBar,
 */

public final class DefaultController extends GuiController {
    private static final String kStatus = LocaleMgr.screen.getString("Status_");
    private static final String kOk = LocaleMgr.screen.getString("OK");
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    private static final String kDetail = LocaleMgr.screen.getString("Details");
    private static final String kShowDetails = LocaleMgr.screen.getString("ShowDetails");
    private static final String kHideDetails = LocaleMgr.screen.getString("HideDetails");
    private static final String kSaveAs = LocaleMgr.screen.getString("SaveAs");

    private JDialog dialog;
    private JTextArea outarea; // output area
    private JButton cancelButton;
    private JButton okButton;
    private JButton saveAsButton;
    private JLabel reverseLabel;
    private JLabel jobDescription;
    private JLabel timeLabel = new JLabel(" ");
    private JProgressBar progressBar;
    private boolean useProgressBar;
    private String logFileName;

    private boolean detailVisible = false;
    private JPanel detailPanel = new JPanel(new GridBagLayout());
    private JButton detailButton = new JButton(kShowDetails);

    private JLabel iconLabel = new JLabel() {
        public Dimension getPreferredSize() {
            Icon icon = UIManager.getIcon("OptionPane.errorIcon");
            Icon currentIcon = getIcon();
            Dimension dim = new Dimension(0, 0);
            if (icon != null) {
                // dim.width = 1; // icon.getIconWidth();
                dim.height = icon.getIconHeight();
            }
            if (defaultIcon != null) {
                dim.width = Math.max(dim.width, defaultIcon.getIconWidth());
                dim.height = Math.max(dim.height, defaultIcon.getIconHeight());
            }
            if (currentIcon != null) {
                dim.width = Math.max(dim.width, currentIcon.getIconWidth());
                dim.height = Math.max(dim.height, currentIcon.getIconHeight());
            }
            return dim;
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    };
    private Icon defaultIcon;

    private int dialogSizeHeight = -1;
    private int detailsSizeHeight = -1;

    public JDialog getDialog() {
        return dialog;
    }

    public DefaultController(String title, boolean useProgressBar, String logFileName) {
        this(title, useProgressBar, logFileName, null, null);
    }

    public DefaultController(String title, boolean useProgressBar, String logFileName,
            String jobDescription) {
        this(title, useProgressBar, logFileName, jobDescription, null);
    }

    public DefaultController(String title, boolean useProgressBar, String logFileName,
            String jobDescription, Icon jobIcon) {
        dialog = new JDialog(ApplicationContext.getDefaultMainFrame(), title, true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        this.logFileName = logFileName;
        this.defaultIcon = jobIcon;

        setDetailVisible(detailVisible);
        detailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setDetailVisible(!detailVisible);
            }
        });

        if (jobDescription != null)
            this.jobDescription = new JLabel(jobDescription);

        JPanel paneltop = new JPanel(new GridBagLayout());
        JPanel panelbot = new JPanel(new GridBagLayout());
        JLabel statusLabel = new JLabel(kStatus);

        reverseLabel = new JLabel(" ");

        outarea = new JTextAreaFix(11, 40);
        outarea.setEditable(false);
        outarea.setText("");
        JScrollPane jsp = new JScrollPane(outarea,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.useProgressBar = useProgressBar;
        if (useProgressBar)
            progressBar = new JProgressBar(JProgressBar.HORIZONTAL);

        int y = 0; // y location for next added component in paneltop
        int x1 = 0; // x1 location for next added component in paneltop
        if (iconLabel != null) {
            paneltop.add(iconLabel, new GridBagConstraints(x1, y, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets((y == 0 ? 12 : 6),
                            (x1 == 0 ? 12 : 6), 6, 0), 0, 0));
            x1++;
        }
        if (this.jobDescription != null) {
            paneltop.add(this.jobDescription, new GridBagConstraints(x1, y, 3, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets((y == 0 ? 12 : 6),
                            (x1 == 0 ? 12 : 6), 6, 12), 0, 0));
            y++;
        }
        if (progressBar != null) {
            paneltop.add(progressBar, new GridBagConstraints(x1, y, 3, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets((y == 0 ? 12
                            : 6), (x1 == 0 ? 12 : 6), 6, 12), 0, 0));
            y++;
        } else {
            paneltop.add(statusLabel, new GridBagConstraints(x1, y, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets((y == 0 ? 12 : 6),
                            (x1 == 0 ? 12 : 6), 12, 6), 0, 0));
            paneltop.add(reverseLabel, new GridBagConstraints(x1 + 1, y, 2, 1, 0.5, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets((y == 0 ? 12 : 6),
                            6, 12, 12), 0, 0));
            y++;
            // paneltop.add(statusLabel, new GridBagConstraints(0, 0, 1, 1, 0.0,
            // 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new
            // Insets(12,12,6,6), 0, 0));
            // paneltop.add(reverseLabel, new GridBagConstraints(1, 0, 1, 1,
            // 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new
            // Insets(12,12,6,12), 0, 0));
            // paneltop.add(timeLabel, new GridBagConstraints(0, 1, 2, 1, 0.5,
            // 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new
            // Insets(6,12,12,12), 0, 0));
        }
        // paneltop.add(Box.createVerticalGlue(), new GridBagConstraints(0, y,
        // 4, 1, 0.5, 0.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new
        // Insets(0,12,0,12), 0, 0));
        // y++;

        saveAsButton = new JButton(kSaveAs);
        detailPanel.add(new JLabel(kDetail), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 12, 6, 12), 0, 0));
        detailPanel.add(saveAsButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 12, 6, 12), 6, 0));
        detailPanel.add(jsp, new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.EAST,
                GridBagConstraints.BOTH, new Insets(0, 12, 12, 12), 100, 80));

        cancelButton = new JButton(kCancel);
        okButton = new JButton(kOk);
        AwtUtil.normalizeComponentDimension(new JButton[] { okButton, cancelButton });
        okButton.setEnabled(false);
        panelbot
                .add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.BOTH,
                        new Insets(12, 12, 12, 12), 0, 0));
        panelbot.add(detailButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(12, 12, 12, 0),
                0, 0));
        panelbot.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 12, 0),
                0, 0));
        panelbot.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 12, 12),
                0, 0));

        dialog.getContentPane().setLayout(new GridBagLayout());
        dialog.getContentPane().add(
                paneltop,
                new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 24));
        dialog.getContentPane().add(
                panelbot,
                new GridBagConstraints(0, 2, 1, 1, 0.5, 0.0, GridBagConstraints.EAST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        dialog.getContentPane().add(
                detailPanel,
                new GridBagConstraints(0, 3, 1, 1, 0.5, 0.5, GridBagConstraints.EAST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        dialog.pack();
        dialog.setSize(Math.max(dialog.getSize().width, Math.max(
                detailPanel.getPreferredSize().width, 560)), dialog.getSize().height);
        // backup height - better result after show details - resize then hide
        // details
        dialogSizeHeight = dialog.getSize().height;
    }

    // invoked only within events queue thread
    private void setDetailVisible(boolean b) {
        detailVisible = b;
        if (detailVisible)
            detailButton.setText(kHideDetails);
        else
            detailButton.setText(kShowDetails);
        detailPanel.setVisible(detailVisible);
        if (!detailVisible && dialog.isVisible()) {
            // restore height
            dialog.setSize(dialog.getSize().width, dialogSizeHeight);
            detailsSizeHeight = detailPanel.getSize().height;
        } else if (dialog.isVisible()) {
            Dimension dim = detailPanel.getPreferredSize();
            if (dim != null) {
                if (detailsSizeHeight != -1)
                    dialog
                            .setSize(
                                    dialog.getSize().width,
                                    (int) (dialog.getSize().height + (detailsSizeHeight * (detailVisible ? 1
                                            : -1))));
                else
                    dialog.setSize(dialog.getSize().width, (int) (dialog.getSize().height + (dim
                            .getHeight() * (detailVisible ? 1 : -1))));
            }
        }
        dialog.validate();
    }

    protected JTextArea getTextArea() {
        return outarea;
    }

    protected JButton getSaveAsButton() {
        return saveAsButton;
    }

    protected JLabel getStatusLabel() {
        return useProgressBar ? null : reverseLabel;
    }

    protected FileWriter getLogFileWriter() {
        if (logFileName == null)
            return null;
        FileWriter logWriter = null;
        try {
            logWriter = new FileWriter(logFileName, false);
        } catch (IOException e) {
            logWriter = null;
        }
        return logWriter;
    }

    protected JButton getOKButton() {
        return okButton;
    }

    protected JLabel getTimeElapsedLabel() {
        return /* timeLabel */null;
    }

    protected JButton getCancelButton() {
        return cancelButton;
    }

    protected Window getWindow() {
        return dialog;
    }

    protected JProgressBar getProgressBar() {
        return useProgressBar ? progressBar : null;
    }

    protected JLabel getIconLabel() {
        return iconLabel;
    }

    protected Icon getDefaultIcon() {
        return defaultIcon;
    }
}
