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

package org.modelsphere.jack.baseDb.screen;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.SRSystemClipboard;
import org.modelsphere.jack.baseDb.screen.spellchecking.SpellCheckerTextArea;
import org.modelsphere.jack.international.LocaleMgr;

// Only used by LookupDescriptionEditor.
// For a text viewer, use instead jack.awt.TextViewerFrame or jack.awt.TextViewerDialog
public class TextEditorDialog extends JDialog implements ActionListener, DocumentListener {

    private boolean textModified = false;
    private boolean checkSpellVisible = false;
    private SpellCheckerTextArea textArea;
    private JButton okBtn = new JButton(LocaleMgr.screen.getString("OK"));
    private JButton cancelBtn = new JButton(LocaleMgr.screen.getString("Cancel"));
    private JToggleButton spellCheckBtn;
    private JPanel containerPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel();
    private GridLayout gridLayout = new GridLayout();
    private JToolBar toolbar = new JToolBar();

    private Action copyAction = new AbstractAction(
            org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("copy"),
            org.modelsphere.jack.srtool.international.LocaleMgr.action.getImageIcon("copy")) {

        {
            putValue(AbstractAction.SHORT_DESCRIPTION,
                    org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("copy"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String selection = textArea.getSelectedText();
            if (selection != null && selection.length() > 0) {
                SRSystemClipboard.setClipboardText(selection);
            }
        }
    };

    private Action pasteAction = new AbstractAction(
            org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("Paste"),
            org.modelsphere.jack.srtool.international.LocaleMgr.action.getImageIcon("Paste")) {
        {
            putValue(AbstractAction.SHORT_DESCRIPTION,
                    org.modelsphere.jack.srtool.international.LocaleMgr.action.getString("Paste"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (SRSystemClipboard.containsText()) {
                String text = SRSystemClipboard.getClipboardText();
                if (text != null && text.length() > 0) {
                    textArea.replaceSelection(text);
                }
            }
        }
    };

    public TextEditorDialog(Component comp, String text, String title, boolean checkSpellVisible) {
        super((comp instanceof JFrame ? (JFrame) comp : (JFrame) SwingUtilities.getAncestorOfClass(
                JFrame.class, comp)), title, true);

        this.checkSpellVisible = checkSpellVisible;
        textArea = new SpellCheckerTextArea(text, title);

        init();
        setLocationRelativeTo((comp instanceof JFrame ? (JFrame) comp : (JFrame) SwingUtilities
                .getAncestorOfClass(JFrame.class, comp)));

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                textArea.requestFocus();
                textArea.checkSpelling();
            }

        });
    }

    private void init() {
        containerPanel.setLayout(new GridBagLayout());
        gridLayout.setHgap(6);
        controlBtnPanel.setLayout(gridLayout);
        getContentPane().add(containerPanel);
        textArea.getDocument().addDocumentListener(this);

        //spell check button and control button panel
        Icon spellCheckedIcon = LocaleMgr.action.getImageIcon("SpellCheckedImage");
        Icon spellUncheckedIcon = LocaleMgr.action.getImageIcon("SpellUnheckedImage");
        String toolTip = LocaleMgr.action.getString("ToggleSpellCheck");

        spellCheckBtn = new JToggleButton(spellUncheckedIcon);
        toolbar.setFloatable(false);
        JButton button = toolbar.add(copyAction);
        button.setFocusable(false);
        button = toolbar.add(pasteAction);
        button.setFocusable(false);
        toolbar.addSeparator();
        toolbar.add(spellCheckBtn);
        spellCheckBtn.setFocusable(false);

        containerPanel.add(toolbar, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(3, 6, 3, 6), 0,
                0));
        containerPanel.add(new JScrollPane(textArea), new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 6, 0, 6), 0, 0));
        containerPanel.add(controlBtnPanel, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));

        boolean checkSpelling = textArea.isSpellCheckingEnabled();
        spellCheckBtn.setSelectedIcon(spellCheckedIcon);
        spellCheckBtn.addActionListener(this);
        spellCheckBtn.setVisible(this.checkSpellVisible);
        spellCheckBtn.setSelected(checkSpelling);
        spellCheckBtn.setToolTipText(toolTip);

        controlBtnPanel.add(okBtn, null);
        controlBtnPanel.add(cancelBtn, null);
        okBtn.setEnabled(false);
        okBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        setSize(AwtUtil.getBestDialogSize());

        getRootPane().setDefaultButton(okBtn);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        new HotKeysSupport(this, cancelBtn, null);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();

        if (spellCheckBtn.equals(src)) {
            boolean selected = spellCheckBtn.isSelected();
            textArea.setSpellCheckingEnabled(selected);
        } else if (okBtn.equals(src)) {
            textModified = true;
            dispose();
        } else if (cancelBtn.equals(src)) {
            dispose();
        }
    }

    public void setSpellCheckingEnabled(boolean enabled) {
        textArea.setSpellCheckingEnabled(enabled);
    }

    public final String getText() {
        String text = textArea.getText();
        return text;
    }

    public final void setText(String text) {
        textArea.setText(text);
    }

    public final boolean isTextModified() {
        return textModified;
    }

    ///////////////////////////////////
    // DocumentListener SUPPORT
    //
    public final void insertUpdate(DocumentEvent e) {
        okBtn.setEnabled(true);
    }

    public final void removeUpdate(DocumentEvent e) {
        okBtn.setEnabled(true);
    }

    public final void changedUpdate(DocumentEvent e) {
    }
    //
    // End of DocumentListener SUPPORT
    ///////////////////////////////////

}
