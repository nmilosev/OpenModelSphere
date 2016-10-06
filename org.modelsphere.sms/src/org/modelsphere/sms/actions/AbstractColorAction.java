/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.actions;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.Icon;

import org.modelsphere.jack.actions.AbstractColorDomainAction;
import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.JackColorChooser;
import org.modelsphere.jack.awt.PopupColorPanel;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.DiagramImage;
import org.modelsphere.sms.db.DbSMSGraphicalObject;
import org.modelsphere.sms.international.LocaleMgr;

public abstract class AbstractColorAction extends AbstractColorDomainAction /*
                                                                             * implements
                                                                             * SelectionActionListener
                                                                             */{
    private static final String kSelectCustom = LocaleMgr.action.getString("SelectCustom_");
    private static final String kTransparent = LocaleMgr.action.getString("Transparent");

    private static final String CUSTOM_COLOR_PROPERTY = "Color";

    private static final Color TRANSPARENT_COLOR = new Color(255, 255, 255, 0);

    private static Color[] defaultColors = new Color[40];

    private ArrayList values = new ArrayList();
    private Color[] customColors = new Color[PopupColorPanel.COL_COUNT];

    private static AbstractColorAction[] colorActions = new AbstractColorAction[] {};

    private String fieldName;
    private String transName;
    private String customSelectTitle;

    protected static final int BLACK_INDEX = 39;
    protected static final int WHITE_INDEX = 0;

    private boolean alpha = false;

    protected int defaultColor = WHITE_INDEX;

    static {
        ArrayList def = new ArrayList();
        defaultColors[0] = new Color(255, 255, 255);
        defaultColors[1] = new Color(204, 153, 255);
        defaultColors[2] = new Color(153, 204, 255);
        defaultColors[3] = new Color(204, 255, 255);
        defaultColors[4] = new Color(204, 255, 204);
        defaultColors[5] = new Color(255, 255, 153);
        defaultColors[6] = new Color(255, 204, 153);
        defaultColors[7] = new Color(255, 153, 204);

        defaultColors[8] = new Color(192, 192, 192);
        defaultColors[9] = new Color(153, 51, 102);
        defaultColors[10] = new Color(0, 204, 255);
        defaultColors[11] = new Color(0, 255, 255);
        defaultColors[12] = new Color(0, 255, 0);
        defaultColors[13] = new Color(255, 255, 0);
        defaultColors[14] = new Color(255, 204, 0);
        defaultColors[15] = new Color(255, 0, 255);

        defaultColors[16] = new Color(153, 153, 153);
        defaultColors[17] = new Color(128, 0, 128);
        defaultColors[18] = new Color(51, 102, 255);
        defaultColors[19] = new Color(51, 204, 204);
        defaultColors[20] = new Color(51, 153, 102);
        defaultColors[21] = new Color(153, 204, 0);
        defaultColors[22] = new Color(255, 153, 0);
        defaultColors[23] = new Color(255, 0, 0);

        defaultColors[24] = new Color(128, 128, 128);
        defaultColors[25] = new Color(102, 102, 153);
        defaultColors[26] = new Color(0, 0, 255);
        defaultColors[27] = new Color(0, 128, 128);
        defaultColors[28] = new Color(0, 128, 0);
        defaultColors[29] = new Color(128, 128, 0);
        defaultColors[30] = new Color(255, 102, 0);
        defaultColors[31] = new Color(128, 0, 0);

        defaultColors[32] = new Color(51, 51, 51);
        defaultColors[33] = new Color(51, 51, 153);
        defaultColors[34] = new Color(0, 0, 128);
        defaultColors[35] = new Color(0, 51, 102);
        defaultColors[36] = new Color(0, 51, 0);
        defaultColors[37] = new Color(51, 51, 0);
        defaultColors[38] = new Color(153, 51, 0);
        defaultColors[39] = new Color(0, 0, 0);
    }

    {
        AbstractColorAction[] tempColorActions = new AbstractColorAction[colorActions.length + 1];
        System.arraycopy(colorActions, 0, tempColorActions, 0, colorActions.length);
        tempColorActions[colorActions.length] = this;
        colorActions = tempColorActions;
    }

    public AbstractColorAction(String name, Icon icon, String fieldName, String transName,
            String customSelectTitle, boolean alpha) {
        super(name, icon, defaultColors);
        this.alpha = alpha;
        setEnabled(true);
        this.fieldName = fieldName;
        this.transName = transName;
        this.customSelectTitle = customSelectTitle;
        if (fieldName == null)
            throw new IllegalArgumentException("Null Field Name"); // NOT
        // LOCALIZABLE
        initColors();
        setSelectedIndex(0);
    }

    // will be applyed as selected color if selection's color differ or null
    // default is opaque white
    protected void setDefaultColor(int index) {

    }

    protected final void doActionPerformed() {
        Object[] selObjs = ApplicationContext.getFocusManager().getSelectedObjects();
        Object value = this.getSelectedObject();
        Color selColor = null;
        if (value == null)
            return;
        if (value instanceof Color)
            selColor = (Color) value;
        else if (value instanceof String) {
            if (value.equals(kTransparent)) {
                selColor = TRANSPARENT_COLOR;
            } else {
                Color c1 = null;
                boolean differ = false;
                try {
                    DbMultiTrans.beginTrans(Db.READ_TRANS, selObjs, null);
                    for (int i = 0; i < selObjs.length; i++) {
                        if ((selObjs[i] instanceof DiagramImage)
                                || !(selObjs[i] instanceof ActionInformation)
                                || !(((ActionInformation) selObjs[i]).getGraphicalObject() instanceof DbSMSGraphicalObject)) {
                            continue;
                        }
                        DbSMSGraphicalObject go = (DbSMSGraphicalObject) ((ActionInformation) selObjs[i])
                                .getGraphicalObject();
                        if (go == null) {
                            continue;
                        }
                        MetaField metaField = go.getMetaField(fieldName); // NOT
                        // LOCALIZABLE
                        if (metaField != null && !differ) {
                            Color c = (Color) go.find(metaField);
                            if (c != null) {
                                if (c1 != null)
                                    differ = !c1.equals(c);
                                else
                                    c1 = c;
                            } else if (c1 != null)
                                differ = true;
                        }
                    }
                    DbMultiTrans.commitTrans(selObjs);
                } catch (Exception ex) {
                    org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                            ApplicationContext.getDefaultMainFrame(), ex);
                }
                if (differ || c1 == null)
                    c1 = defaultColors[defaultColor];
                selColor = JackColorChooser.showDialog(ApplicationContext.getDefaultMainFrame(),
                        customSelectTitle, c1, alpha);
            }
        }
        if (selColor == null)
            return;
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, selObjs, transName);
            for (int i = 0; i < selObjs.length; i++) {
                if ((selObjs[i] instanceof DiagramImage)
                        || !(selObjs[i] instanceof ActionInformation)
                        || !(((ActionInformation) selObjs[i]).getGraphicalObject() instanceof DbSMSGraphicalObject)) {
                    continue;
                }
                DbSMSGraphicalObject go = (DbSMSGraphicalObject) ((ActionInformation) selObjs[i])
                        .getGraphicalObject();
                MetaField metaField = go.getMetaField(fieldName); // NOT
                // LOCALIZABLE
                if (metaField != null)
                    go.set(metaField, selColor);
            }
            DbMultiTrans.commitTrans(selObjs);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
        if (selColor != null && !values.contains(selColor) && !selColor.equals(TRANSPARENT_COLOR)) {
            // Update all color actions with the newly selected color
            for (int i = 0; i < colorActions.length; i++) {
                colorActions[i].addCustomColor(selColor);
            }
            saveCustomColors(customColors);
        }
        if (selColor != null && !selColor.equals(TRANSPARENT_COLOR))
            setSelectedObject(selColor);
    }

    private static void saveCustomColors(Color[] customColors) {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();

        for (int i = 0; i < customColors.length; i++) {
            if (customColors[i] == null)
                preferences.setProperty(AbstractColorAction.class, CUSTOM_COLOR_PROPERTY + i
                        + ".rgba", -1);
            else
                preferences.setProperty(AbstractColorAction.class, CUSTOM_COLOR_PROPERTY + i
                        + ".rgba", customColors[i].getRGB());
        }
    }

    private void loadCustomColors() {
        ArrayList tempColors = new ArrayList();
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            for (int i = 0; i < PopupColorPanel.COL_COUNT; i++) {
                int rgba = preferences.getPropertyInteger(AbstractColorAction.class,
                        CUSTOM_COLOR_PROPERTY + i + ".rgba", new Integer(-1)).intValue();
                if (rgba == -1)
                    continue;
                tempColors.add(new Color(rgba, true));
            }
            customColors = new Color[PopupColorPanel.COL_COUNT];
            for (int i = 0; i < customColors.length && i < tempColors.size(); i++) {
                customColors[PopupColorPanel.COL_COUNT - tempColors.size() + i] = (Color) tempColors
                        .get(i);
            }
        }
    }

    private void initColors() {
        values.clear();
        loadCustomColors();
        for (int i = 0; i < defaultColors.length; i++) {
            values.add(defaultColors[i]);
        }
        for (int i = 0; i < customColors.length; i++) {
            if (customColors[i] != null && !values.contains(customColors[i]))
                values.add(customColors[i]);
        }
        if (alpha)
            values.add(kTransparent);
        values.add(kSelectCustom);
        Object[] arrayvalues = new Object[values.size()];
        for (int i = 0; i < arrayvalues.length; i++) {
            arrayvalues[i] = values.get(i);
        }
        this.setDomainValues(arrayvalues);
    }

    private void addCustomColor(Color value) {
        if (value != null && !values.contains(value)) {
            int old = this.getSelectedIndex();
            Object oldvalue = this.getSelectedObject();
            for (int i = 0; i < customColors.length - 1; i++) {
                customColors[i] = customColors[i + 1];
            }
            customColors[customColors.length - 1] = value;
            values.clear();
            for (int i = 0; i < defaultColors.length; i++) {
                values.add(defaultColors[i]);
            }
            for (int i = 0; i < customColors.length; i++) {
                if (customColors[i] != null && !values.contains(customColors[i]))
                    values.add(customColors[i]);
            }
            if (alpha)
                values.add(kTransparent);
            values.add(kSelectCustom);
            Object[] arrayvalues = new Object[values.size()];
            for (int i = 0; i < arrayvalues.length; i++) {
                arrayvalues[i] = values.get(i);
            }
            this.setDomainValues(arrayvalues);
            if (oldvalue != null && values.contains(oldvalue))
                this.setSelectedIndex(values.indexOf(oldvalue));
            else
                this.setSelectedIndex(old);
        }
    }

    public final boolean hasAlpha() {
        return alpha;
    }

}
