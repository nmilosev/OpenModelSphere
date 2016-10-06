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

import java.awt.Font;

import javax.swing.SwingConstants;

import org.modelsphere.jack.baseDb.db.srtypes.SrFont;
import org.modelsphere.jack.baseDb.screen.DefaultRenderer;
import org.modelsphere.jack.baseDb.screen.ScreenView;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SrFontRenderer extends DefaultRenderer {
    public static final SrFontRenderer singleton = new SrFontRenderer();
    private SrFont m_srFont;

    protected final void setValue(ScreenView screenView, int row, int column, Object value) {
        if (value != null) { // if null value, render as blank instead of
            // 'unspecified'.
            Font font = (Font) value;
            m_srFont = new SrFont(font);
        }

        if (m_srFont != null) {
            String text = m_srFont.toString();
            setText(text);
            setToolTipText(text);
            this.setHorizontalAlignment(SwingConstants.CENTER);
        }
    } // end setValue()

    protected final SrFont getValue() {
        return m_srFont;
    } // end getFont()
} // end SrFontRenderer
