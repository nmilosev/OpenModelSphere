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

package org.modelsphere.sms.or.features.extract;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.sms.db.DbSMSSemanticalObject;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORIndex;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORTypeClassifier;
import org.modelsphere.sms.or.oracle.db.srtypes.ORASizeUnit;

abstract class ExtractFileWriter {
    protected static final String MSG_PATTERN = org.modelsphere.jack.srtool.international.LocaleMgr.message
            .getString("successfullyCreatedPattern");
    protected static final String RDM_VERSION = "2.8.0.0";
    protected Controller m_controller;
    private static final String TEXT_EXT = ".txt"; // NOT LOCALIZABLE, file

    // extension

    protected ExtractFileWriter(Controller controller) {
        m_controller = controller;
    }

    protected File createFile(DbORDataModel dataModel, File directory, ArrayList fileList)
            throws IOException, DbException {
        // create file and write file header
        String filename = dataModel.getName() + TEXT_EXT;
        int discriminant = 2;
        while (fileList.contains(filename)) {
            filename = dataModel.getName() + discriminant + TEXT_EXT;
            discriminant++;
        } // end while

        fileList.add(filename);
        File file = new File(directory, filename);
        return file;
    } // end createPrintWriter()

    protected int getTypeLength(DbORTypeClassifier type, int recursivityLevel) throws DbException {
        int len = 0;

        if (recursivityLevel > 16) // avoid endlessloop
            return len;

        if (type instanceof DbORDomain) {
            DbORDomain domain = (DbORDomain) type;
            Integer iLen = domain.getLength();
            if (iLen != null) {
                len = iLen.intValue();
            } else {
                DbORTypeClassifier sourceType = domain.getSourceType();
                len = getTypeLength(sourceType, ++recursivityLevel);
            }
        } // end if

        return len;
    } // end getTypeLength()

    protected int getTypeScale(DbORTypeClassifier type, int recursivityLevel) throws DbException {
        int scale = 0;

        if (recursivityLevel > 16) // avoid endlessloop
            return scale;

        if (type instanceof DbORDomain) {
            DbORDomain domain = (DbORDomain) type;
            Integer iScale = domain.getNbDecimal();
            if (iScale != null) {
                scale = iScale.intValue();
            } else {
                DbORTypeClassifier sourceType = domain.getSourceType();
                scale = getTypeScale(sourceType, ++recursivityLevel);
            }
        } // end if

        return scale;
    } // end getTypeScale()

    protected void writeForeignKeysOfTable(String owner, DbORTable table, PrintWriter writer)
            throws DbException {
        // For each FK
        String tableName = getPhysicalOrLogicalName(table);
        DbRelationN relN = table.getComponents();
        DbEnumeration dbEnum = relN.elements(DbORForeign.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbORForeign fKey = (DbORForeign) dbEnum.nextElement();
            writeForeign(fKey, writer, owner, tableName);
        } // end while
        dbEnum.close();
    } // end writeTable()

    protected String getTypeName(DbORTypeClassifier type, int recursivityLevel) throws DbException {
        String typename = "CHAR"; // by default

        if (recursivityLevel > 16) // avoid endlessloop
            return typename;

        if (type instanceof DbORBuiltInType) {
            typename = type.getPhysicalName();
        } else if (type instanceof DbORDomain) {
            DbORDomain domain = (DbORDomain) type;
            DbORTypeClassifier sourceType = domain.getSourceType();
            typename = getTypeName(sourceType, ++recursivityLevel);
        } // end if

        return typename;
    } // end getTypeName()

    protected String getPhysicalOrLogicalName(DbSMSSemanticalObject obj) throws DbException {
        return getPhysicalOrLogicalName(obj, "null");
    }

    protected String getPhysicalOrLogicalName(DbSMSSemanticalObject obj, String nullString)
            throws DbException {
        if (obj == null) {
            return nullString;
        } // end if

        String name = obj.getPhysicalName();
        if ((name == null) || (name.equals("")))
            name = obj.getName();

        return name;
    } // end getPhysicalOrLogicalName()

    protected int computeActualValue(Integer rawValue) {
        return computeActualValue(rawValue, null);
    }

    protected int computeActualValue(Integer rawValue, ORASizeUnit unit) {
        int actualValue = 0;

        if (rawValue == null) {
            return actualValue;
        }

        actualValue = rawValue.intValue();
        if (unit != null) {
            switch (unit.getValue()) {
            case ORASizeUnit.KB:
                actualValue *= 1024;
                break;
            case ORASizeUnit.MB:
                actualValue *= 1024 * 1024;
                break;
            }
        } // end if

        return actualValue;
    } // end computeActualValue()

    //
    // abstract methods
    //
    abstract void writeHeader(PrintWriter writer, DbORDataModel dataModel) throws DbException;

    abstract void writeDataModel(DbORDataModel dataModel, File directory, ArrayList fileList)
            throws IOException, DbException;

    abstract void writeTable(DbORTable table, PrintWriter writer) throws DbException;

    abstract void writeColumn(DbORColumn column, PrintWriter writer, String tableName, int colID)
            throws DbException;

    abstract void writePuKey(DbORPrimaryUnique key, PrintWriter writer, String owner,
            String tableName) throws DbException;

    abstract void writeIndex(DbORIndex index, PrintWriter writer, String owner, String tableName)
            throws DbException;

    abstract void writeForeign(DbORForeign fKey, PrintWriter writer, String owner, String tableName)
            throws DbException;

} // end ExtractFileWriter
