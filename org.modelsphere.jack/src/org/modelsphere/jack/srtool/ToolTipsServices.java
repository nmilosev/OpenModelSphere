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

package org.modelsphere.jack.srtool;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.preference.DisplayToolTipsOptionGroup;
import org.modelsphere.jack.util.TargetRuntimeException;

/**
 * The work around Tooltips is not completed. Only the explorer take into consideration this
 * service. We should also be able to specify more complex cases (using a new class able to contain
 * the field and value or a custom way. See ListElement or AddElement.) This service should alse be
 * able to use the semantical model in order to obtain the display value.
 * 
 * 
 */
public final class ToolTipsServices {
    private static final String AMP = "&amp;"; //NOT LOCALIZABLE - HTML tag
    private static final String LT = "&lt;"; //NOT LOCALIZABLE - HTML tag
    private static final String GT = "&gt;"; //NOT LOCALIZABLE - HTML tag
    private static final String QUOT = "&quot;"; //NOT LOCALIZABLE - HTML tag
    private static final String COPY = "&copy;"; //NOT LOCALIZABLE - HTML tag
    private static final String REG = "&reg;"; //NOT LOCALIZABLE - HTML tag
    private static final String NBSP = "&nbsp;"; //NOT LOCALIZABLE - HTML tag

    private static final int MAX_LINE_COUNT_IN_TOOLTIPS = 12;
    private static final int MAX_CHAR_COUNT_IN_TOOLTIPS = 800;
    private static final int MAX_CHAR_PER_LINE = 70;
    private static final int LONG_TEXT_TOOLTIPS_DELAY = 25000;

    private static final int DEFAULT_TOOLTIPS_DISMISS_DELAY;

    private static final ToolTipsServicesListener mouseListener = new ToolTipsServicesListener();

    static {
        DEFAULT_TOOLTIPS_DISMISS_DELAY = ToolTipManager.sharedInstance().getDismissDelay();
    }

    private ToolTipsServices() {
    }

    private static class ToolTipsServicesListener implements MouseListener, MouseMotionListener {
        ToolTipsServicesListener() {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
            //ToolTipManager.sharedInstance().setDismissDelay(DEFAULT_TOOLTIPS_DISMISS_DELAY);
        }

        public final void mouseDragged(MouseEvent e) {
        }

        public final void mouseMoved(MouseEvent e) {
        }
    }

    public static final void register(JComponent component) {
        component.addMouseListener(mouseListener);
        component.addMouseMotionListener(mouseListener);
    }

    public static final void unregister(JComponent component) {
        component.removeMouseListener(mouseListener);
        component.removeMouseMotionListener(mouseListener);
    }

    /*
     * public static final String getToolTips(Object value, String header){ String text = ""; text
     * += "<html>"; // NOT LOCALIZABLE
     * 
     * text += getToolTipsBody(value, header);
     * 
     * text += "</html>"; // NOT LOCALIZABLE return text; }
     */

    public static final String getToolTips(DbObject dbo, Class context) throws DbException {
        if (dbo == null)
            return null;
        String text = "";
        text += "<html>"; // NOT LOCALIZABLE

        String sValue = ApplicationContext.getSemanticalModel().getDisplayText(dbo, context);
        text += sValue;

        // add options fields if applicable
        MetaField fields[] = DisplayToolTipsOptionGroup.getToolTipsMetaFields();
        String fieldInfo = null;
        Object value = null;
        String header = null;
        for (int i = 0; fields != null && i < fields.length; i++) {
            if (!dbo.hasField(fields[i])
                    || !ApplicationContext.getSemanticalModel().isVisibleOnScreen(
                            dbo.getMetaClass(), fields[i], dbo, context))
                continue;
            value = dbo.get(fields[i]);
            header = ApplicationContext.getSemanticalModel().getDisplayText(dbo.getMetaClass(),
                    fields[i], context);
            try {
                fieldInfo = getToolTipsBody(value, header, context);
                text += "<br>";
                text += fieldInfo;
            } catch (Exception e) {
                Debug.trace(e);
                if (e instanceof DbException) {
                    throw new TargetRuntimeException(e);
                }
                return "";
            }
        }

        text += "</html>"; // NOT LOCALIZABLE    
        return text;
    }

    private static final String getToolTipsBody(Object value, String header, Class context)
            throws DbException {
        String text = "";
        if (header != null && header.length() > 0)
            text += "<b>" + header + ":</b>" + NBSP + NBSP; // NOT LOCALIZABLE

        String sValue = "";
        if (value instanceof DbObject) {
            sValue = ApplicationContext.getSemanticalModel().getDisplayText((DbObject) value,
                    context);
        } else if (value != null) {
            sValue = value.toString();
        }

        if (sValue.indexOf("\n") < 0) { // NOT LOCALIZABLE
            text += sValue;
            //ToolTipManager.sharedInstance().setDismissDelay(DEFAULT_TOOLTIPS_DISMISS_DELAY);
        } else {
            StringTokenizer st = new StringTokenizer(sValue, ",.©®(;:?!<>& \n\"", true); // NOT LOCALIZABLE
            int count = 0;
            int linecount = 0;
            int charinline = 0;
            while (st.hasMoreTokens() && count < MAX_CHAR_COUNT_IN_TOOLTIPS
                    && linecount < MAX_LINE_COUNT_IN_TOOLTIPS) {
                String token = st.nextToken();
                if (token.equals("\n")) { // NOT LOCALIZABLE
                    text += "<br>"; // NOT LOCALIZABLE
                    linecount++;
                    charinline = 0;
                } else if (token.equals("<")) {
                    text += LT;
                    charinline += 1;
                } else if (token.equals(">")) {
                    text += GT;
                    charinline += 1;
                } else if (token.equals("&")) {
                    text += AMP;
                    charinline += 1;
                } else if (token.equals("\"")) {
                    text += QUOT;
                    charinline += 1;
                } else if (token.equals("©")) {
                    text += COPY;
                    charinline += 1;
                } else if (token.equals("®")) {
                    text += REG;
                    charinline += 1;
                } else if (charinline >= MAX_CHAR_PER_LINE) { // NOT LOCALIZABLE
                    text += "<br>"; // NOT LOCALIZABLE
                    linecount++;
                    charinline = 0;
                    text += (token.equals(" ") ? NBSP : token);
                    charinline += token.length();
                } else {
                    text += (token.equals(" ") ? NBSP : token);
                    charinline += token.length();
                }
                count += token.length();
            }
            if (count >= MAX_CHAR_COUNT_IN_TOOLTIPS || linecount >= MAX_LINE_COUNT_IN_TOOLTIPS)
                text += "<br><b>...</b>"; // NOT LOCALIZABLE
            // ToolTipManager.sharedInstance().setDismissDelay(LONG_TEXT_TOOLTIPS_DELAY);
        }
        return text;
    }

}
