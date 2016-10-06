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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.File;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.modelsphere.jack.srtool.ApplicationContext;

public class TextViewerFrame extends JInternalFrame implements DocumentListener {

    private TextViewerController controller;

    public TextViewerFrame(String title, String str) {
        this(title, str, false, null, false);
    }

    public TextViewerFrame(String title, String str, boolean useHTML) {
        this(title, str, useHTML, null, false);
    }

    public TextViewerFrame(String title, File file) {
        this(title, "", false, file, false);
    }

    private TextViewerFrame(String title, String str, boolean useHTML, File file) {
        this(title, str, useHTML, null, false);
    }

    private TextViewerFrame(String title, String str, boolean useHTML, File file,
            boolean mailToEnabled) {
        super(title);
        controller = new TextViewerController(this, str, useHTML, file, mailToEnabled);
        getTextPanel().getDocument().addDocumentListener(this);
        getTextPanel().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    controller.closeViewer();
            }
        });
        getTextPanel().setBackground(Color.white);
        setSize(AwtUtil.getBestDialogSize());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        getContentPane().setBackground(UIManager.getColor("Panel.background")); // NOT LOCALIZABLE : ui property
        // set default icon
        ImageIcon icon = new ImageIcon(ApplicationContext.APPLICATION_IMAGE_ICON);
        setFrameIcon(icon);
    }

    public final void showTextViewerFrame(JDesktopPane desktop, Integer layer) {
        desktop.add(this, layer);
        setVisible(true);
        try {
            setSelected(true);
        } catch (PropertyVetoException e) {
        }
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

    public void updateUI() {
        super.updateUI();
        if (controller == null || !controller.isHtml || getTextPanel() == null)
            return;
        getTextPanel().setBackground(Color.white);
    }

}
