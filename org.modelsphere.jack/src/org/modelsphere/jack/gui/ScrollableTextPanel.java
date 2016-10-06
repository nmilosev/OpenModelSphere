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

import java.awt.CardLayout;
import java.awt.Rectangle;
import java.awt.ScrollPane;

import javax.swing.*;

import org.modelsphere.jack.international.LocaleMgr;

//TODO: This class is designed as an alternative to jack.awt.TextViewPanel
//(and will probably replace it)
public class ScrollableTextPanel extends JPanel {
    // flags
    public static final int USE_HTML = 1;
    public static final int EDITABLE = 2;
    // NEXT = 4,8,16..

    private int m_flags;
    CardLayout cardLayout1 = new CardLayout();
    String type = "text/html"; // NOT LOCALIZABLE
    String text = LocaleMgr.screen.getString("NotRefreshed"); // "(not refreshed)";

    JScrollPane m_jscrollPane = new JScrollPane();
    EditScrollPane m_scrollPane = new EditScrollPane();

    {
        m_jscrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    public ScrollableTextPanel(int flags) {
        m_flags = flags;
        JEditorPane pane1 = new JEditorPane(type, text);
        JEditorPane pane2 = new JEditorPane(type, text);
        m_scrollPane.setContent(pane1);
        m_jscrollPane.setViewportView(pane2);
        jbInit();
    }

    void jbInit() {
        // components adding
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(cardLayout1);
    }

    private boolean initialized = false;

    private void init(JEditorPane pane) {
        if (!initialized) {
            boolean editable = ((m_flags & EDITABLE) == 0) ? false : true;

            add(m_scrollPane, "awt"); // NOT LOCALIZABLE
            add(m_jscrollPane, "swing"); // NOT LOCALIZABLE
            setEditable(editable, false);

            initialized = true;
        }
    }

    public void setText(String text) {
        JEditorPane pane;

        if (useSwingScroll) {
            pane = (JEditorPane) m_jscrollPane.getViewport().getView();
        } else {
            pane = m_scrollPane.getContent();
        }

        init(pane);
        pane.setText(text);

        if (useSwingScroll) {
            JViewport viewport = m_jscrollPane.getViewport();
            Rectangle rect = new Rectangle(0, 0, 10, 10);
            viewport.scrollRectToVisible(rect);
        } // end if
    } // end setText()

    private boolean useSwingScroll = true;

    public void setEditable(boolean editable, boolean cancel) {
        boolean useSwing = editable;

        JEditorPane pane = null;
        if (useSwing) {
            // get the content of the previous pane
            JEditorPane prevPane = m_scrollPane.getContent();
            String text = prevPane.getText();
            cardLayout1.show(this, "swing"); // NOT LOCALIZABLE
            pane = (JEditorPane) m_jscrollPane.getViewport().getView();
            pane.setText(text);
            useSwingScroll = true;
        } else {
            cardLayout1.show(this, "awt"); // NOT LOCALIZABLE
            pane = m_scrollPane.getContent();

            // if user has not cancelled, get the content of the previous pane
            if (!cancel) {
                JEditorPane prevPane = (JEditorPane) m_jscrollPane.getViewport().getView();
                String text = prevPane.getText();
                pane.setText(text);
            }

            useSwingScroll = false;
        }

        pane.setEditable(editable);
    }

    // INNER CLASS
    private class EditScrollPane extends ScrollPane {
        private JEditorPane m_pane;

        EditScrollPane() {
            super();
        }

        void setContent(JEditorPane pane) {
            m_pane = pane;
            add(pane);
        }

        JEditorPane getContent() {
            return m_pane;
        }
    }
}
