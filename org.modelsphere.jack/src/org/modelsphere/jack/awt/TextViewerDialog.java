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

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class TextViewerDialog extends JDialog implements DocumentListener {

    private TextViewerController controller;

    public static void showTextDialog(Component comp, String title, String text) {
        TextViewerDialog dialog = new TextViewerDialog(comp, title, text);
        dialog.setVisible(true);
    }

    public static void showTextDialog(Component comp, String title, String text, boolean useHTML) {
        showTextDialog(comp, title, text, useHTML, false);
    }

    public static void showTextDialog(Component comp, String title, String text, boolean useHTML,
            boolean mailToEnable) {
        TextViewerDialog dialog = new TextViewerDialog(comp, title, text, useHTML, mailToEnable);
        dialog.setVisible(true);
    }

    public TextViewerDialog(Component comp, String title, String text) {
        this(comp, title, text, false, false, false);
    }

    public TextViewerDialog(Component comp, String title, String text, boolean useHTML) {
        this(comp, title, text, false, false, false);
    }

    public TextViewerDialog(Component comp, String title, String text, boolean useHTML,
            boolean mailToEnable) {
        this(comp, title, text, useHTML, mailToEnable, false);
    }

    public TextViewerDialog(Component comp, String title, String text, boolean useHTML,
            boolean mailToEnable, boolean enableVMMenu) {
        super((comp instanceof Frame ? (Frame) comp : (Frame) SwingUtilities.getAncestorOfClass(
                Frame.class, comp)), title, true);
        controller = new TextViewerController(this, text, useHTML, null, mailToEnable, enableVMMenu);
        getTextPanel().getDocument().addDocumentListener(this);
        getTextPanel().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    controller.closeViewer();
            }
        });
        if (useHTML)
            getTextPanel().setBackground(Color.white);
        setSize(AwtUtil.getBestDialogSize());
        setLocationRelativeTo(comp);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public final JTextComponent getTextPanel() {
        return controller.getTextPanel();
    }

    public final void setStatusText(String text) {
        controller.setStatusText(text);
    }

    ///////////////////////////////////
    // DocumentListener SUPPORT
    //
    public final void insertUpdate(DocumentEvent e) {
        controller.setDocumentState(true);
    }

    public final void removeUpdate(DocumentEvent e) {
        controller.setDocumentState(true);
    }

    public final void changedUpdate(DocumentEvent e) {
    }
    //
    // End of DocumentListener SUPPORT
    ///////////////////////////////////

}
