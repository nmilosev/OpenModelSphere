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

package org.modelsphere.sms.plugins.report.model;

// Jack

// Sms

// JDK
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.baseDb.util.TerminologyUtil;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.or.db.DbORAssociation;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORField;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.plugins.report.LocaleMgr;

public class Concept {

    private DbObject[] m_entryPoints;
    private MetaClass m_metaClass;
    private Terminology terminology = null;

    private MetaField[] metaFields;
    private MetaClass[] components;

    private int nMode = 0;

    public Concept(DbObject[] entryPoints, MetaClass metaClass) {
        m_entryPoints = new DbObject[] { entryPoints[0] };

        m_metaClass = metaClass;

        initFields();
        initComponents();

        ////
        // try to get the terminology

        TerminologyUtil terminologyUtil = TerminologyUtil.getInstance();
        DbObject model = m_entryPoints[0];
        try {
            model.getDb().beginReadTrans();
            terminology = terminologyUtil.findModelTerminology(model);
            if (terminologyUtil.isDataModel(model))
                nMode = terminologyUtil.getModelLogicalMode(model);
            model.getDb().commitTrans();
        } catch (Exception e) {
            terminology = null;
        }

    }

    private void initFields() {
        ArrayList screenMetaFields = m_metaClass.getScreenMetaFields();
        metaFields = new MetaField[screenMetaFields.size() + 1];//new MetaField[screenMetaFields.size()+2];

        // the composite and components metafields are added to the screen metafields
        metaFields[0] = DbObject.fComposite;
        System.arraycopy(screenMetaFields.toArray(), 0, metaFields, 1, screenMetaFields.size());
        //metaFields[metaFields.length-1] = DbObject.fComponents;
    }

    private void initComponents() {
        components = getComponentMetaClasses(m_metaClass);
    }

    private MetaClass[] getComponentMetaClasses(MetaClass metaClass) {
        MetaClass[] components;
        MetaClass[] superMetaClassComponents;

        if (metaClass.getSuperMetaClass() != null)
            superMetaClassComponents = getComponentMetaClasses(metaClass.getSuperMetaClass());
        else
            superMetaClassComponents = new MetaClass[0];

        components = new MetaClass[superMetaClassComponents.length
                + metaClass.getComponentMetaClasses().length];
        System.arraycopy(superMetaClassComponents, 0, components, 0,
                superMetaClassComponents.length);
        System.arraycopy(metaClass.getComponentMetaClasses(), 0, components,
                superMetaClassComponents.length, metaClass.getComponentMetaClasses().length);

        return components;
    }

    public MetaClass getMetaClass() {
        return m_metaClass;
    }

    public MetaField[] getFields() {
        return metaFields;
    }

    public MetaClass[] getComponents() {
        return components;
    }

    public ArrayList getOccurences() throws DbException {
        ArrayList occurences = new ArrayList();

        for (int i = 0; i < m_entryPoints.length; i++)
            occurences.addAll(getOccurences(m_entryPoints[i]));

        return occurences;
    }

    public ArrayList getOccurences(DbObject parent) throws DbException {
        ArrayList occurences = new ArrayList();

        if (parent == null)
            return occurences;

        if (m_metaClass.isAssignableFrom(parent.getMetaClass())) {
            occurences.add(parent);
        }

        DbEnumeration dbEnum = parent.getComponents().elements();
        while (dbEnum.hasMoreElements()) {
            occurences.addAll(getOccurences(dbEnum.nextElement()));
        }
        dbEnum.close();

        return occurences;
    }

    public String toString() {

        String str = null;
        if (terminology != null) {
            if (nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                if (m_metaClass == DbGETable.metaClass)
                    str = terminology.getTerm(m_metaClass) + " | "
                            + terminology.getTerm(DbOOClass.metaClass);
                else if (m_metaClass == DbORAssociation.metaClass)
                    str = terminology.getTerm(m_metaClass);
                else if (m_metaClass == DbGEColumn.metaClass)
                    str = terminology.getTerm(m_metaClass);
        }

        if (str == null)
            str = m_metaClass.getGUIName(false, true);

        if (m_metaClass == DbOODiagram.metaClass || m_metaClass == DbJVAssociation.metaClass
                || m_metaClass == DbJVDataMember.metaClass)
            str = str + " " + LocaleMgr.misc.getString("javaSuffix"); //NOT LOCALIZABLE
        else if (m_metaClass == DbORDiagram.metaClass || m_metaClass == DbORAssociation.metaClass
                || m_metaClass == DbORField.metaClass) {
            if (!(m_metaClass == DbORAssociation.metaClass && nMode == TerminologyUtil.LOGICAL_MODE_ENTITY_RELATIONSHIP))
                str = str + " " + LocaleMgr.misc.getString("relationalSuffix"); //NOT LOCALIZABLE
        } else if (m_metaClass == DbBEDiagram.metaClass)
            str = str + " " + LocaleMgr.misc.getString("bpmSuffix"); //NOT LOCALIZABLE

        return str;
    }
}
