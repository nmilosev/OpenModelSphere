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

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.SMSSynchroModel.SynchroObject;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.features.dbms.DefaultSynchroModel;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;

/**
 * Finds out if the modified properties of a column justify a MODIFY COLUMN SQL clause <br>
 * Target System : <b>Oracle</b><br>
 * Type : <b>User Function</b><br>
 * Parameter : a string to be converted in uppercase.<br>
 * Returns : integer from 0 to 9 where : "0" just datatype,length, or decimals changed "1" default
 * value changed "2" null/not null changed "3" default + null/null (1+2) "4" comments changed and
 * "0" "5" comments changed and "1" "6" comments changed and "2" "7" comments changed and "3" "8"
 * only comments changed "9" no changes justify SQL code generation
 */
public final class HookOracleModifyColumn extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("HookOracleModifyColumn",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE
    private static final String DEFAULT = "DEFAULT"; //NOT LOCALIZABLE, keyword
    private static final String NULL = "NULL"; //NOT LOCALIZABLE, keyword
    private static final String NOT_NULL = "NOT NULL"; //NOT LOCALIZABLE, keyword

    public HookOracleModifyColumn() {
    } //Parameter-less constructor required by jack.io.Plugins

    public HookOracleModifyColumn(String rulename, String subRule, Modifier[] modifiers)
            throws RuleException {
        super(rulename, subRule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        if (!(object instanceof DbORAColumn))
            return expanded;

        DbORAColumn column = (DbORAColumn) object;

        // The synchro object representing the column
        SynchroObject synchroObject = DefaultSynchroModel.getSynchroObject();
        // Make sure we are in a modification state
        if (synchroObject.action != SynchroObject.ACT_MODIFY)
            return expanded;

        //for each modified field, find out if it justify a MODIFY COLUMN clause
        MetaField[] modifiedFields = synchroObject.modifiedMetaFields;
        boolean doAlterTable = false;

        //boolean modType = false;
        //boolean modLenDec = false;
        boolean modDefault = false;
        boolean modNull = false;
        boolean modDesc = false;
        int returnCode = 0;

        for (int i = 0; i < modifiedFields.length; i++) {
            MetaField metaField = modifiedFields[i];
            if (metaField == DbORColumn.fType)
                doAlterTable = true;
            else if ((metaField == DbORColumn.fLength) || (metaField == DbORColumn.fNbDecimal))
                doAlterTable = true;
            else if (metaField == DbORColumn.fInitialValue)
                modDefault = true;
            else if (metaField == DbORColumn.fNull)
                modNull = true;
            else if (metaField == DbORColumn.fDescription)
                modDesc = true;
        } //end for

        if (modDefault) {
            returnCode += 1;
        } //end if

        if (modNull) {
            returnCode += 2;
        } //end if

        if (modDesc) {
            returnCode += 4;

            if (!(doAlterTable || modDefault || modNull)) {
                returnCode = 8;
            } //end if
        } //end if

        if (!(doAlterTable || modDefault || modNull || modDesc)) {
            returnCode = 9;
        } //end if

        output.write("" + returnCode);
        expanded = true;

        /*
         * if (modDefault == true && modNull == true){ output.write(buildDefaultClause(column));
         * output.write(buildNullClause(column)); } else if (modDefault == true){
         * output.write(buildDefaultClause(column)); } else if (modNull == true){
         * output.write(buildNullClause(column)); } else{ // This will trigger the template
         * executing which deals with the datatype,length,decimals boolean alterTable = (modType ||
         * modLenDec); if (alterTable && (!modDesc)) output.write("1"); else if (modDesc &&
         * (!alterTable)) output.write("2"); else if (alterTable && modDesc) output.write("3"); else
         * output.write("0"); } //end if expanded = true;
         * 
         * } catch (DbException ex) { throw new RuleException(ex.getMessage()); }
         */

        return expanded;
    } //end expand()

    private String buildDefaultClause(DbORAColumn column) throws DbException {
        String defaultValue = column.getInitialValue();
        if (defaultValue == null) {
            defaultValue = "";
        }

        if (!defaultValue.equals("")) {
            defaultValue = " " + DEFAULT + " " + defaultValue; //NOT LOCALIZABLE, two strings
        }

        return defaultValue;
    }

    private String buildNullClause(DbORAColumn column) throws DbException {
        boolean isNull = column.isNull();
        if (isNull == true)
            return " " + NULL;
        else
            return " " + NOT_NULL;
    }

} //end HookOracleModifyColumn
