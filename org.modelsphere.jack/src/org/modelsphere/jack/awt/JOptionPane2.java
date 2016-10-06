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

/*
 * Created on Feb 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.modelsphere.jack.awt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JOptionPane2 extends JOptionPane {
    static int g_selection = -1;

    public static int showOptionDialog(Component parentComponent, Object message, String title,
            int optionType, int messageType, Icon icon, Object[] options, Object initialValue) {
        JDialog dialog = null;
        if (parentComponent instanceof Frame) {
            Frame owner = (Frame) parentComponent;
            dialog = new JDialog(owner, title, true);
        } else if (parentComponent instanceof Dialog) {
            Dialog owner = (Dialog) parentComponent;
            dialog = new JDialog(owner, title, true);
        } // end if

        if (dialog == null) {
            return CLOSED_OPTION;
        }

        if (icon == null) {
            icon = getDefaultIcon(messageType);
        }

        // create content
        createContent(dialog, message, icon, options, initialValue);
        AwtUtil.centerWindow(dialog);

        // show
        dialog.setVisible(true);
        dialog.dispose();
        return g_selection;

    } // end showInputDialog()

    //
    // PRIVATE METHODS
    //

    // get icon
    private static Icon getDefaultIcon(int messageType) {
        JOptionPane pane = new JOptionPane(null, messageType);
        Icon icon = pane.getIcon();
        return icon;
    } // end getIcon()

    private static void createContent(final JDialog dialog, Object message, Icon icon,
            Object[] options, Object initialValue) {
        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        panel.setLayout(gridBagLayout);

        // add icon and text
        // JLabel label = new JLabel(icon);
        JTextPane jTextPane = new JTextPane();
        jTextPane.setBackground(panel.getBackground());
        jTextPane.setEditable(false);
        jTextPane.setText(message.toString());
        // panel.add(label, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        // ,GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new
        // Insets(12, 12, 12, 12), 0, 0));
        panel.add(jTextPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(12, 12, 12, 12), 0, 0));

        // add radio buttons
        final JRadioButton[] buttons = new JRadioButton[options.length];
        addRadioButtons(panel, buttons, options, initialValue);

        // add OK button
        JButton jButton = new JButton();
        jButton.setText("OK");
        panel.add(jButton, new GridBagConstraints(0, 2 + options.length, 1, 1, 0.0, 1.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0,
                0));

        // add panel in dialog
        BorderLayout borderLayout1 = new BorderLayout();
        // panel1.setBackground(SystemColor.activeCaptionBorder);
        dialog.getContentPane().setLayout(borderLayout1);
        dialog.getContentPane().add(panel, BorderLayout.CENTER);

        // add listener
        jButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                g_selection = getSelection(buttons);
                dialog.setVisible(false);
            } // end actionPerformed()
        });

        dialog.pack();
    } // end createContent()

    private static int getSelection(JRadioButton[] buttons) {
        int selection = -1;

        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].isSelected()) {
                selection = i;
                break;
            }
        } // end for

        return selection;
    } // end getSelection()

    // create buttons
    private static void addRadioButtons(JPanel panel, JRadioButton[] buttons, Object[] options,
            Object initialValue) {
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < options.length; i++) {
            buttons[i] = new JRadioButton(options[i].toString());
            GridBagConstraints constr = new GridBagConstraints(0, 2 + i, 1, 1, 0.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 6, 6), 0, 0);

            group.add(buttons[i]);
            panel.add(buttons[i], constr);
            if ((initialValue != null) && initialValue.equals(options[i])) {
                buttons[i].setSelected(true);
                g_selection = i;
            } // end if
        } // end for
    } // end createButtons()

    //
    // MAIN
    //
    public static void main(String[] args) {
        JFrame frame = new JFrame("test");
        frame.setVisible(true);

        Object[] options = new Object[] { "op1", "op2", "op3", "op4" };
        Object initialValue = "op4";
        JOptionPane2.showOptionDialog(frame, "message", "title", 0, JOptionPane2.WARNING_MESSAGE,
                null, options, initialValue);
    } // end main()
} // end JOptionPane
