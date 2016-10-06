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

package org.modelsphere.sms.or.features.dbms;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;

import org.modelsphere.jack.awt.tree.CheckTreeNode;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.plugins.generic.dbms.ObjectScope;
import org.modelsphere.sms.db.DbSMSAbstractPackage;

public class DBMSForwardOptions implements ForwardOptions {
    // Statements To Generate
    public final static int CREATE_STATEMENTS = 0;
    public final static int DROP_STATEMENTS = 1;
    public final static int DROP_CREATE_STATEMENTS = 2;

    // Actions
    public static final int CREATE_ADD_FORWARD = 0;
    public static final int CREATE_ADD_SYNCHRO = 1;
    public static final int DROP_DELETE = 2;
    public static final int ALTER_MODIFY = 3;
    public static final int RENAME = 4;

    // Default Values
    private final static int DEFAULT_STATEMENTS = CREATE_STATEMENTS;
    public String databaseName = null;
    public int statementToGenerate = DEFAULT_STATEMENTS;

    public DBMSForwardOptions() {
    }

    // ////////////////////////
    // OVERRIDES ForwardOptions
    private DbObject[] m_objects;

    public void setObjects(DbObject[] objects) {
        m_objects = objects;
    }

    public DbObject[] getObjects() {
        return m_objects;
    }

    public void setActualDirectory(File actualDirectory) {
    }

    public JackForwardEngineeringPlugin getForward() {
        return null;
    }

    //
    // /////////////////////////

    // Target System ID
    private int m_targetSystemId = -1;

    public int getTargetSystemId() {
        return m_targetSystemId;
    }

    public void setTargetSystemId(int targetSystemId) {
        m_targetSystemId = targetSystemId;
    }

    // Abstract package
    private DbSMSAbstractPackage m_package = null; // either datamodel,

    // procedure library or
    // database
    public DbSMSAbstractPackage getAbstractPackage() {
        return m_package;
    }

    public void setAbstractPackage(DbSMSAbstractPackage pack) {
        m_package = pack;
    }

    // nameList
    private ArrayList m_nameList = new ArrayList();

    public ArrayList getNameList() {
        return m_nameList;
    }

    public void setNameList(ArrayList list) {
        m_nameList = list;
    }

    // output file
    private String m_outputFile;

    public String getOutputFile() {
        return m_outputFile;
    }

    public void setOutputFile(String outputFile) {
        m_outputFile = outputFile;
    }

    // Specific options
    // Contain the options specific to the DBMS
    // the initialisation is made in the toolkit
    private Object m_specificDBMSOptions;

    public final void setSpecificDBMSOptions(Object specificOptions) {
        m_specificDBMSOptions = specificOptions;
    }

    public final Object getSpecificDBMSOptions() {
        return m_specificDBMSOptions;
    }

    // Object scope
    // objectsScope contains all objects supported by the SGBD,
    // the initialisation is made in the toolkit
    private ObjectScope[] m_objectsScope;

    public final void setObjectsScope(ObjectScope[] scope) {
        m_objectsScope = scope;
    }

    public final ObjectScope[] getObjectsScope() {
        return m_objectsScope;
    }

    // is output in Html?
    private boolean m_inHtml;

    public final void setOutputInHtml(boolean inHtml) {
        m_inHtml = inHtml;
    }

    public final boolean getOutputInHtml() {
        return m_inHtml;
    }

    private CheckTreeNode m_fieldTree = null;

    public final void setCheckTreeNode(CheckTreeNode fieldTree) {
        m_fieldTree = fieldTree;
    }

    public final CheckTreeNode getCheckTreeNode() {
        return m_fieldTree;
    }

    public static ArrayList getExcludeList(CheckTreeNode fieldTree) {
        ArrayList excludeList = new ArrayList();

        if (fieldTree != null) {
            Enumeration enumClasses = fieldTree.children();
            while (enumClasses.hasMoreElements()) {
                CheckTreeNode node = (CheckTreeNode) enumClasses.nextElement();
                String className = node.toString();
                if (!node.isSelected()) { // if a node is disable, exclude all
                    // its items
                    excludeList.add(className);
                } else {
                    Enumeration enumFields = node.children();
                    while (enumFields.hasMoreElements()) {
                        CheckTreeNode subnode = (CheckTreeNode) enumFields.nextElement();
                        if (!subnode.isSelected()) {
                            String fieldName = subnode.toString();
                            excludeList.add(className + "." + fieldName);
                        } // end if
                    } // end while
                } // end if
            } // end while
        } // end if

        return excludeList;
    } // end getExcludeList()
}
