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

package org.modelsphere.jack.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

@SuppressWarnings("serial")
public final class InfoDialog extends JDialog {
    protected JButton button; // The okay button of the dialog
    protected JTextArea textArea; // The message displayed by the dialog

    public InfoDialog(JFrame parent, String title, String message) {
        // Create a non-modal dialog with the specified title and parent
        super(parent, title, true);

        // Create the message component and add it to the window
        // MultiLineLabel is a custom component defined later in this chapter
        textArea = new JTextArea(message);
        textArea.setEditable(false);
        button = new JButton("OK"); // NOT LOCALIZABLE - This is used during a failed application startup, localemgr may be broken

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        getContentPane().setLayout(new GridBagLayout());

        getContentPane().add(
                new JLabel("Details:"),
                new GridBagConstraints(0, 0, 2, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(12, 6, 6, 6), 0, 0));
        getContentPane().add(
                new JScrollPane(textArea),
                new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 6, 12, 6), 0, 0));

        getContentPane().add(

                Box.createHorizontalGlue(),
                new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        getContentPane().add(
                button,
                new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));

        setSize(new Dimension(640, 400));
    }

}
