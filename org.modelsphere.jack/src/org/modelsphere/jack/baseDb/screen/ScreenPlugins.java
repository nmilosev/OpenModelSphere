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

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.util.StringUtil;

public abstract class ScreenPlugins {

    private static final String PACKAGE_NAME = "org.modelsphere.jack.baseDb.screen.plugins"; //NOT LOCALIZABLE
    private static final String EDITOR_SUFFIX_NAME = "Editor"; //NOT LOCALIZABLE
    private static final String RENDERER_SUFFIX_NAME = "Renderer"; //NOT LOCALIZABLE
    private static final String MULTI_PREFIX_NAME = "Multi"; //NOT LOCALIZABLE

    private static HashMap<String, Renderer> rendererMap = new HashMap<String, Renderer>();
    private static HashMap<String, TableCellRenderer> multiRendererMap = new HashMap<String, TableCellRenderer>();

    private static HashMap<MetaField, String> externalPlugins = new HashMap<MetaField, String>();
    private static HashMap<String, String> externalPluginPackages = new HashMap<String, String>();

    private static String[] auxiliaryPackages = null;

    //private static String s6 = ""; // NOT LOCALIZABLE

    /* For editors, we allocate a new object for each edition. */
    public static Editor getEditor(String pluginName) {
        Editor editor = null;
        try {
            editor = (Editor) findPlugin(pluginName, pluginName, EDITOR_SUFFIX_NAME).newInstance();
        } catch (Exception ex) {
        }
        return editor;
    }

    /*
     * For renderers, we always reuse the same singleton. Use a HashMap to call ClassLoader only
     * once for each renderer.
     */
    public static Renderer getRenderer(String pluginName) {
        Object renderer = rendererMap.get(pluginName);
        if (renderer == null) {
            try {
                renderer = findPlugin(pluginName, pluginName, RENDERER_SUFFIX_NAME).getField(
                        "singleton").get(null);//NOT LOCALIZABLE
            } catch (Exception ex) {
                renderer = "notFound"; //NOT LOCALIZABLE
            }
            if (renderer instanceof Renderer)
                rendererMap.put(pluginName, (Renderer) renderer);
        }
        return (renderer instanceof Renderer ? (Renderer) renderer : null);
    }

    /* For editors, we allocate a new object for each edition. */
    public static TableCellEditor getMultiEditor(String pluginName) {
        TableCellEditor editor = null;
        try {
            editor = (TableCellEditor) findPlugin(pluginName, MULTI_PREFIX_NAME + pluginName,
                    EDITOR_SUFFIX_NAME).newInstance();
        } catch (Exception ex) {
        }
        return editor;
    }

    /*
     * For renderers, we always reuse the same singleton. Use a HashMap to call ClassLoader only
     * once for each renderer.
     */
    public static TableCellRenderer getMultiRenderer(String pluginName) {
        Object renderer = multiRendererMap.get(pluginName);
        if (renderer == null) {
            try {
                renderer = findPlugin(pluginName, MULTI_PREFIX_NAME + pluginName,
                        RENDERER_SUFFIX_NAME).getField("singleton").get(null);//NOT LOCALIZABLE
            } catch (Exception ex) {
                renderer = "notFound"; //NOT LOCALIZABLE
            }
            if (renderer instanceof TableCellRenderer)
                multiRendererMap.put(pluginName, (TableCellRenderer) renderer);
        }
        return (renderer instanceof TableCellRenderer ? (TableCellRenderer) renderer : null);
    }

    public static String getPluginName(MetaField field, DbObject parentObj) throws DbException {
        String pluginName = externalPlugins.get(field);
        if (pluginName == null)
            pluginName = field.getRendererPluginName();
        if (pluginName == null || pluginName.length() == 0) {
            // Take the renderer appropriate for the type of the field.
            Class<?> type = ((field == DbUDFValue.fValue && parentObj != null) ? ((DbUDF) parentObj)
                    .getValueClass()
                    : field.getJField().getType());
            pluginName = type.getName();
            pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1);
            if (type.isPrimitive()) {
                if (pluginName.equals("int")) //NOT LOCALIZABLE
                    pluginName = "Integer"; //NOT LOCALIZABLE
                else
                    pluginName = StringUtil.titleString(pluginName);
            } else if (Domain.class.isAssignableFrom(type))
                pluginName = "Domain"; //NOT LOCALIZABLE
            else if (pluginName.startsWith("Sr")) { //NOT LOCALIZABLE
                if (pluginName.equals("SrInteger") || pluginName.equals("SrLong")
                        || pluginName.equals("SrDouble")) //NOT LOCALIZABLE
                    pluginName = pluginName.substring(2);
            }
        }
        return pluginName;
    }

    public static String getPluginName(MetaField field) {
        String pluginName = externalPlugins.get(field);
        if (pluginName == null)
            pluginName = field.getRendererPluginName();
        if (pluginName == null || pluginName.length() == 0) {
            // Take the renderer appropriate for the type of the field.
            Class<?> type = field.getJField().getType();
            pluginName = type.getName();
            pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1);
            if (type.isPrimitive()) {
                if (pluginName.equals("int")) //NOT LOCALIZABLE
                    pluginName = "Integer"; //NOT LOCALIZABLE
                else
                    pluginName = StringUtil.titleString(pluginName);
            } else if (Domain.class.isAssignableFrom(type))
                pluginName = "Domain"; //NOT LOCALIZABLE
            else if (pluginName.startsWith("Sr")) { //NOT LOCALIZABLE
                if (pluginName.equals("SrInteger") || pluginName.equals("SrLong")
                        || pluginName.equals("SrDouble")) //NOT LOCALIZABLE
                    pluginName = pluginName.substring(2);
            }
        }
        return pluginName;
    }

    private static Class<?> findPlugin(String pluginName, String pluginFileName, String suffix) {
        Class<?> c = null;
        String className = null;
        if (externalPlugins.containsValue(pluginName)) {
            String packageName = externalPluginPackages.get(pluginName);
            className = (packageName == null ? "" : packageName + ".") + pluginFileName + suffix; //NOT LOCALIZABLE
        } else {
            className = PACKAGE_NAME + "." + pluginFileName + suffix; //NOT LOCALIZABLE
        }
        try {
            c = Class.forName(className);
        } catch (Exception ex) {
        }
        if (c != null || auxiliaryPackages == null)
            return c;

        for (int i = 0; i < auxiliaryPackages.length && c == null; i++) {
            className = auxiliaryPackages[i] + "." + pluginFileName + suffix; //NOT LOCALIZABLE
            try {
                c = Class.forName(className);
            } catch (Exception ex) {
            }
        }
        return c;
    }

    public static void setAuxiliaryPackages(String[] packages) throws Exception {
        initPackages_(packages);
    }

    private static void initPackages_(String[] packages) throws Exception {
        // Init packages
        auxiliaryPackages = packages;
    }

    public static void updateRenderersUI() {
        Iterator<Renderer> iter = rendererMap.values().iterator();
        while (iter.hasNext()) {
            Object renderer = iter.next();
            if (renderer instanceof JComponent) {
                ((JComponent) renderer).updateUI();
            }
        }
        Iterator<TableCellRenderer> iter2 = multiRendererMap.values().iterator();
        while (iter2.hasNext()) {
            Object renderer = iter2.next();
            if (renderer instanceof JComponent) {
                ((JComponent) renderer).updateUI();
            }
        }
    }

    public static void installPlugin(MetaField metafield, String packageName, String pluginName) {
        externalPlugins.put(metafield, pluginName);
        externalPluginPackages.put(pluginName, packageName);
    }

}
