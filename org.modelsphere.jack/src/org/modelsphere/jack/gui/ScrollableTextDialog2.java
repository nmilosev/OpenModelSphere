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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;

import org.modelsphere.jack.international.LocaleMgr;

/**
 * Shows a dialog that contains a scrollable text area, and a close button.
 * 
 */
public class ScrollableTextDialog2 extends JDialog {
    private static final long serialVersionUID = -1L;
    private JButton m_closeBtn;
    private JTabbedPane m_tabbedPane;

    /**
     * Construct the dialog; may accept a JFrame as parent.
     */
    public ScrollableTextDialog2() {
        super();
        init();
    }

    public ScrollableTextDialog2(JFrame frame) {
        super(frame, true);
        init();
    } // end ScrollableTextDialog2

    private void init() {
        this.setLayout(new GridBagLayout());

        // add tab
        m_tabbedPane = new JTabbedPane();

        this.add(m_tabbedPane,
                new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 3, 6), 0, 0));

        // add close button
        m_closeBtn = new JButton(LocaleMgr.screen.getString("Close"));
        this.add(m_closeBtn,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.NONE, new Insets(3, 6, 6, 6), 0, 0));

        // add listener
        final ScrollableTextDialog2 thisDialog = this;
        m_closeBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                thisDialog.dispose();
            }
        });
    }

    public void addPanel(String string, URL licenseURL) {
        JEditorPane editorPane = new JEditorPane();
        JScrollPane scrollPane = new JScrollPane(editorPane);
        m_tabbedPane.addTab(string, null, scrollPane);

        try {
            setURL(editorPane, licenseURL, false); // editable = false
        } catch (IOException ex) {
            String msg = ex.toString();
            setText(editorPane, msg, false); // editable = false
        }
    } // end addPanel()

    private void setURL(JEditorPane editorPane, URL url, boolean editable) throws IOException {
        editorPane.setEditable(false);
        editorPane.setPage(url);
    }

    private void setText(JEditorPane editorPane, String text, boolean editable) {
        editorPane.setEditable(false);
        editorPane.setText(text);
    }

    //
    // unit test
    //
    public static void main(String[] args) {
        runDemo();
    } // end main()

    private static void runDemo() {
        ScrollableTextDialog2 dialog = new ScrollableTextDialog2();
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
            } // end if
        } while (!done);
    } // end runDemo()

} // end ScrollableTextDialog2
