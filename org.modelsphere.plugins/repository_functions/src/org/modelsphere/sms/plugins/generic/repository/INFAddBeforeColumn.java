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

//Java
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.UserDefinedField;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFTable;

public class INFAddBeforeColumn extends UserDefinedField {

    private PluginSignature m_signature = null;

    public PluginSignature getSignature() {
        if (m_signature == null) {
            m_signature = new PluginSignature("INFAddBeforeColumn", "$Revision: 4 $",
                    ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 605); // NOT LOCALIZABLE
        }

        return m_signature;
    }

    public INFAddBeforeColumn() {
    } //Parameter-less constructor required by jack.io.Plugins

    public INFAddBeforeColumn(String ruleName, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName, subrule, modifiers);
    }

    //Navigate within all its components
    private static final String COMPONENTS = DbINFTable.fComponents.getJName();

    private static final String PROJECT_NOT_FOUND_PATTRN = "Object {0} not under any project."; // NOT LOCALIZABLE

    // we use fComponents to force the call of expandMetaRelationN()
    // but we do not really use the components of a column (there isnt anyway)
    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {

        boolean expanded = false;

        String value;
        DbINFColumn column = (DbINFColumn) object, nextColumn = null;

        try {
            DbObject compositeObject = column.getComposite();
            DbEnumeration enumeration = compositeObject.getComponents().elements(
                    DbINFColumn.metaClass);
            while (enumeration.hasMoreElements()) {
                DbObject currentDbo = enumeration.nextElement();
                if (currentDbo.equals(column)) {
                    if (enumeration.hasMoreElements())
                        nextColumn = (DbINFColumn) enumeration.nextElement();

                    break;
                }
            }
            enumeration.close();
            if (nextColumn != null)
                output.write(nextColumn.getPhysicalName());
            else
                output.write("");

            expanded = true;

        } catch (DbException ex) {
            throw new RuleException(ex.getMessage());
        }

        return expanded;
    }

} //end of NextColumn
