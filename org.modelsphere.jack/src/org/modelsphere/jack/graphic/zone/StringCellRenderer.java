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

package org.modelsphere.jack.graphic.zone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.List;

import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicUtil;

public class StringCellRenderer extends AbstractSelectionCellRenderer {

    private boolean underline;
    private boolean wordWrap;

    private Font font = null;

    private int refScreenFontResolution = -1;
    private Font refFont = null;

    public StringCellRenderer() {
        this(false, false);
    }

    // wordWrap is ignored if not in a SingletonZone
    public StringCellRenderer(boolean underline, boolean wordWrap) {
        this.underline = underline;
        this.wordWrap = wordWrap;
    }

    private void initFont(Graphics g, Font afont) {
        if (afont == null)
            afont = g.getFont();
        int screenFontResolution = Toolkit.getDefaultToolkit().getScreenResolution();

        if (refScreenFontResolution == screenFontResolution
                && (refFont == afont || (refFont != null && refFont.equals(afont)))) {
            return;
        }

        // store the reference infos used for creating the actual font. This is
        // used
        // to avoid creating a new font for each call to paint and
        // getDimension()
        refFont = afont;
        refScreenFontResolution = screenFontResolution;

        double ratio = (double) screenFontResolution / (double) 72;
        double fsize = afont.getSize();
        fsize = (fsize > 0 ? fsize : 1);

        this.font = new Font(afont.getFontName(), afont.getStyle(), (int) (fsize * ratio) + 1);
    }

    // BEWARE: diagView is null for printing
    public final void paint(Graphics g, DiagramView diagView, Rectangle rect, Font font,
            Color color, int margin, int alignment, Cell cell) {
        if (cell.getPaintData() == null)
            return;
        float zoomFactor = (diagView != null ? diagView.getZoomFactor() : 1.0f);
        if (zoomFactor != 1.0f) {
            font = diagView.zoom(g, font);
            if (font == null)
                return; // font too small
        }

        g.setColor(color);
        g.setFont(font);

        initFont(g, font);

        Font oldFont = g.getFont();
        g.setFont(this.font);
        FontMetrics fm = g.getFontMetrics();

        margin += GraphicUtil.GAP_FROM_BORDER;

        int x = (int) ((rect.x + margin) * zoomFactor);
        int width = (int) ((rect.width - 2 * margin) * zoomFactor);
        int y = (int) (rect.y * zoomFactor) + fm.getAscent();
        Object paintData = cell.getPaintData();
        if (paintData instanceof String) {
            drawString(g, (String) cell.getPaintData(), x, width, y, alignment, fm);
        } else {
            Object[] lines = (Object[]) paintData;
            for (int i = 0; i < lines.length; i++) {
                drawString(g, (String) lines[i], x, width, y, alignment, fm);
                y += fm.getHeight();
            }
        }

        g.setFont(oldFont);
    }

    private void drawString(Graphics g, String str, int x, int width, int y, int alignment,
            FontMetrics fm) {
        if (str.length() == 0)
            return;
        int offset = 0;

        if (alignment != GraphicUtil.LEFT_ALIGNMENT)
            offset = GraphicUtil.getAlignmentOffset(alignment, width, fm.stringWidth(str));

        if (underline) {
            AttributedString as = new AttributedString(str);
            as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            as.addAttribute(TextAttribute.FONT, font);
            try {
                g.drawString(as.getIterator(), x + offset, y);
            } catch (Exception ex) {
                g.drawString(str, x + offset, y);
            }
        } else {
            g.drawString(str, x + offset, y);
        }
    }

    // If (wordWrap && fixedWidth != 0), perform word wrapping
    public final Dimension getDimension(Graphics g, Font font, int margin, Cell cell, int fixedWidth) {
        Object data = cell.getCellData();
        Object paintData = null;
        Dimension dim = new Dimension(0, 0);

        // TODO:  This needs to be changed.  The pref size is currently computed ignoring the zoomfactor on the view
        // (as opposed to the paint method).  This cause the component to show a different size than the actual size 
        // required.  This can be verified by dragging free text components (outline usually too big).
        Toolkit tk = Toolkit.getDefaultToolkit();
        int nScreenFontResolution = tk.getScreenResolution();
        double ratio = (double) nScreenFontResolution / (double) 72;
        double fsize = font.getSize();
        fsize = (fsize > 0 ? fsize : 1);
        Font nf = new Font(font.getFontName(), font.getStyle(), (int) (fsize * ratio) + 1);

        if (data != null) {
            FontMetrics fm = g.getFontMetrics(nf);
            margin += GraphicUtil.GAP_FROM_BORDER;
            int maxWidth = (wordWrap && fixedWidth != 0 ? Math.max(1, fixedWidth - 2 * margin) : 0);
            List<String> strings = GraphicUtil.getWrappedStrings(fm, data.toString(), maxWidth);
            for (int i = 0; i < strings.size(); i++) {
                dim.width = Math.max(dim.width, fm.stringWidth((String) strings.get(i)));
            }

            dim.width += 2 * margin;
            dim.height = (int) ((double) strings.size() * (double) fm.getHeight());
            paintData = (strings.size() == 1 ? strings.get(0) : strings.toArray()); // minimize the no of objects kept in each cell.
        }
        cell.setPaintData(paintData);
        cell.setDataWidth(dim.width);
        return dim;
    }
}
