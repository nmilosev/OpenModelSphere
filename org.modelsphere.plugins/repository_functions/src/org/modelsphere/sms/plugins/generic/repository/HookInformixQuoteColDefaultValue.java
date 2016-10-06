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

package org.modelsphere.sms.plugins.generic.repository;

import java.io.*;
import java.lang.reflect.Field;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.features.dbms.DBMSUtil;
import org.modelsphere.sms.or.informix.db.DbINFColumn;

/**
 * Quotes the column's default value if the column's type requires it. For instance : NUMBER 55 =>
 * 55 CHAR 55 => '55' CHAR it's => 'it''s' //inner apostrophe are duplicated
 */
public final class HookInformixQuoteColDefaultValue extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature(
            "HookInformixQuoteColDefaultValue", "$Revision: 4 $",
            ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 212); // NOT LOCALIZABLE
    private static final Rule initialValueProperty = new Property(null, DbINFColumn.fInitialValue);

    public HookInformixQuoteColDefaultValue() {
    } //Parameter-less constructor required by jack.io.Plugins

    public HookInformixQuoteColDefaultValue(String rulename, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(rulename, subrule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        if (!(object instanceof DbINFColumn))
            return expanded;

        DbINFColumn column = (DbINFColumn) object;

        String classPathName = getPluginClassPath();
        Class forwardClass = PluginMgr.getSingleInstance().getPluginClass(classPathName);
        if (forwardClass == null)
            return false;

        String[] quotedTypes = null;
        String[] systemFunctions = null;

        try {
            Field quotedTypesField = forwardClass.getDeclaredField("INFORMIX_QUOTED_TYPES");
            Field systemFunctionsField = forwardClass.getDeclaredField("INFORMIX_SYSTEM_FUNCTIONS");
            quotedTypes = quotedTypesField == null ? null : (String[]) quotedTypesField.get(null);
            systemFunctions = systemFunctionsField == null ? null : (String[]) systemFunctionsField
                    .get(null);
        } catch (Exception e) {
            return false;
        }
        if (quotedTypes == null || systemFunctions == null)
            return false;

        // Gets the default value string
        StringWriter writer = new StringWriter();
        if (initialValueProperty != null) {
            initialValueProperty.expand(writer, object);
            String defaultValue = writer.toString();
            if ((defaultValue != null) && (defaultValue.length() > 0)) {
                String typeName = null;
                try {
                    DbORTypeClassifier type = AnyORObject.getTargetSystemType(column);
                    if (type != null)
                        typeName = type.getPhysicalName();
                    String defValueText = DBMSUtil.enquoteDBMSValue(defaultValue, "'", typeName,
                            quotedTypes, systemFunctions);

                    output.write(defValueText);
                    expanded = true;

                } catch (DbException ex) {
                    throw new RuleException(ex.getMessage());
                }

            }
        }
        writer.close();
        return expanded;
    } //end expand()

    private String getPluginClassPath() {
        String folderPath = System.getProperty("user.dir"); //NOT LOCALIZABLE, property
        String filename = "InformixPlugin.properties"; //NOT LOCALIZABLE, independant from current language
        String classPath = null;
        File file = new File(folderPath, filename);
        try {
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();
                while (line != null) {
                    if (line.startsWith("informixPluginPath=")) {
                        int i = line.indexOf("=");
                        classPath = line.substring(i + 1, line.length());
                        return classPath;
                    }
                    line = reader.readLine();
                }
                reader.close();
            }
        } catch (Exception e) {
        }

        return classPath;
    }
}
