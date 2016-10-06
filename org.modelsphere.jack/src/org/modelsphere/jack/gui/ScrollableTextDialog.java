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

import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.international.LocaleMgr;

public class ScrollableTextDialog extends JDialog implements TestableWindow {
    private JPanel intermediatePanel = new JPanel();
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    JButton jButton1 = new JButton();
    ScrollableTextPanel jPanel1;
    GridBagLayout gridBagLayout1 = new GridBagLayout();

    public ScrollableTextDialog(JFrame parent) {
        super(parent);
        jbInit(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }

    public ScrollableTextDialog() {
        super();
        jbInit(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
    }

    void jbInit(boolean editable) {
        jPanel1 = new ScrollableTextPanel(ScrollableTextPanel.EDITABLE);

        // panels creation
        jButton1.setText(CLOSE);
        intermediatePanel.setLayout(gridBagLayout1);

        // components adding
        intermediatePanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 100,
                50));
        intermediatePanel.add(jButton1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(6, 12, 12, 12),
                12, 0));
        getContentPane().add(intermediatePanel);

        addListener();
    }

    private void addListener() {
        final ScrollableTextDialog thisDialog = this;
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                thisDialog.dispose();
            }
        });
    }

    public void setText(String text) {
        setText(text, true); // editable = true
    }

    public void setText(String text, boolean editable) {
        jPanel1.setText(text);
        // jPanel1.setEditable(editable, true); //cancel = false
    }

    public void scrollToTop() {
        // TODO

    }

    // *************
    // DEMO FUNCTION
    // *************
    private static final String MESSAGE = "<FONT COLOR=BLUE>/*a*a*<B>*a*a*a*****<FONT COLOR=RED>****************<br>*********************************\n"
            + // NOT LOCALIZABLE, unit test
            "/****                                                         ****\n" + // NOT LOCALIZABLE, unit test
            "/****                    Error Message                        ****\n" + // NOT LOCALIZABLE, unit test
            "/****                                                         ****\n" + // NOT LOCALIZABLE, unit test
            "/**********************</FONT>*******</B>************<br>************************\n"; // NOT

    // LOCALIZABLE,
    // unit
    // test

    public Window createTestWindow(Container owner) {
        ScrollableTextDialog dialog = new ScrollableTextDialog();
        dialog.setText(MESSAGE);
        return dialog;
    } // end createTestWindow

    private static void runDemo() {
        ScrollableTextDialog dialog = new ScrollableTextDialog();
        dialog.setText(MESSAGE);
        dialog.pack();
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

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); } //end main()
     */
}
