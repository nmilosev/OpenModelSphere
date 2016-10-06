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
package org.modelsphere.jack.baseDb.screen.plugins;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.modelsphere.jack.awt.AbstractTableCellEditor;
import org.modelsphere.jack.awt.FontChooserDialog;
import org.modelsphere.jack.baseDb.db.srtypes.SrFont;
import org.modelsphere.jack.baseDb.screen.Editor;
import org.modelsphere.jack.baseDb.screen.ScreenView;
import org.modelsphere.jack.baseDb.screen.model.ScreenModel;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.international.LocaleMgr;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SrFontEditor implements Editor, ActionListener {
    private AbstractTableCellEditor editor;
    private ScreenView screenView;
    private JButton actionBtn = new JButton("");
    private Font m_font;
    protected static final String CHOOSE_A_FONT = LocaleMgr.screen.getString("chooseFont");
    private int row;

    // implements Editor
    public final boolean stopCellEditing() {
        return true;
    }

    // implements Editor
    public final Object getCellEditorValue() {
        return m_font;
    }

    public SrFontEditor() {
        int i = 0;
    }

    // implements Editor
    public final Component getTableCellEditorComponent(ScreenView screenView,
            AbstractTableCellEditor editor, Object value, boolean isSelected, int row, int column) {
        this.editor = editor;
        this.screenView = screenView;
        this.row = row;

        if (value instanceof Font) {
            m_font = (Font) value;
            if (m_font != null) {
                SrFont srfont = new SrFont(m_font);
                String text = srfont.toString();
                actionBtn.setText(text);
            }
        } // end if
        actionBtn.addActionListener(this);
        actionBtn.setBackground(screenView.getSelectionBackground());
        return actionBtn;
    } // end getTableCellEditorComponent()

    // implements ActionListener
    public final void actionPerformed(ActionEvent ev) {
        ScreenModel model = screenView.getModel();
        Object obj = model.getRenderer(row, 1);
        SrFont srfont = null;
        if (obj instanceof SrFontRenderer) {
            SrFontRenderer renderer = (SrFontRenderer) obj;
            srfont = renderer.getValue();
        }

        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        Font font = (m_font == null) ? (Font) srfont.toApplType() : m_font;
        m_font = doActionPerformed(mf, row, font, actionBtn);
    } // end actionPerformed()

    protected static Font doActionPerformed(Container parent, int row, Font font, JButton actionBtn) {
        if (font != null) {
            SrFont srfont = new SrFont(font);
            String text = srfont.toString();
            actionBtn.setText(text);
            actionBtn.setToolTipText(text);
        } // end if

        FontChooserDialog dialog = new FontChooserDialog(parent, CHOOSE_A_FONT, font);
        dialog.setVisible(true);
        font = dialog.getSelectedFont();
        if (font != null) {
            SrFont srfont = new SrFont(font);
            String text = srfont.toString();
            actionBtn.setText(text);
            actionBtn.setToolTipText(text);
        }

        return font;
    } // end doActionPerformed()

    @Override
    public int getClickCountForEditing() {
        return 2;
    }
} // end SrFontEditor
