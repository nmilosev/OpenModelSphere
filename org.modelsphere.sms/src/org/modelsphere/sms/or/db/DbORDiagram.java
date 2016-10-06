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
package org.modelsphere.sms.or.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.db.*;
import org.modelsphere.sms.db.srtypes.*;
import javax.swing.Icon;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b>none.<br>
 * 
 * <b>Composites : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDataModel.html">DbORDataModel</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORDomainModel.html">DbORDomainModel</A>, <A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORCommonItemModel.html">DbORCommonItemModel</A>.<br>
 * <b>Components : </b><A
 * HREF="../../../../../org/modelsphere/sms/or/db/DbORTableGo.html">DbORTableGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSCommonItemGo.html">DbSMSCommonItemGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSUserTextGo.html">DbSMSUserTextGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSStampGo.html">DbSMSStampGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSImageGo.html">DbSMSImageGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSPackageGo.html">DbSMSPackageGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSLineGo.html">DbSMSLineGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSFreeGraphicGo.html">DbSMSFreeGraphicGo</A>, <A
 * HREF="../../../../../org/modelsphere/sms/db/DbSMSNoticeGo.html">DbSMSNoticeGo</A>.<br>
 **/
public final class DbORDiagram extends DbSMSDiagram {

    //Meta

    public static final MetaRelation1 fNotation = new MetaRelation1(LocaleMgr.db
            .getString("notation"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbORDiagram"),
            DbORDiagram.class, new MetaField[] { fNotation }, MetaClass.CLUSTER_ROOT
                    | MetaClass.NO_UDF);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbSMSDiagram.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbORTableGo.metaClass,
                    DbSMSCommonItemGo.metaClass });
            metaClass.setIcon("userordiagram.gif");

            fNotation.setJField(DbORDiagram.class.getDeclaredField("m_notation"));
            fNotation.setFlags(MetaField.COPY_REFS);

            fNotation.setOppositeRel(DbORNotation.fDiagrams);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;
    private static Icon dataModelDiagramIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORDiagram.class, "resources/dbordatadiagram.gif");
    private static Icon domainModelDiagramIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORDiagram.class, "resources/dbordomaindiagram.gif");
    private static Icon conceptualDiagramIcon = org.modelsphere.jack.graphic.GraphicUtil.loadIcon(
            DbORDiagram.class, "resources/dborconceptualdiagram.gif");;
    private static Icon commonItemModelDiagramIcon = org.modelsphere.jack.graphic.GraphicUtil
            .loadIcon(DbORDiagram.class, "resources/dborcommonitemdiagram.gif");

    //Instance variables
    DbORNotation m_notation;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbORDiagram() {
    }

    /**
     * Creates an instance of DbORDiagram.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbORDiagram(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
    }

    /**
     * @return style
     **/
    public final DbSMSStyle findStyle() throws DbException {
        DbObject semObj = this.getComposite();
        DbSMSStyle style = null;
        style = getStyle();
        if (style == null) {
            if (semObj instanceof DbORDomainModel)
                style = ((DbSMSProject) getProject()).getOrDefaultDomainStyle();
            else if (semObj instanceof DbORCommonItemModel)
                style = ((DbSMSProject) getProject()).getOrDefaultCommonItemStyle();
            else if (semObj instanceof DbORDataModel) {

                DbORDataModel dataModel = (DbORDataModel) semObj;
                if (dataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
                    style = ((DbSMSProject) getProject()).getErDefaultStyle();
                else
                    style = ((DbSMSProject) getProject()).getOrDefaultStyle();
            } else
                style = ((DbSMSProject) getProject()).getOrDefaultStyle();

        }
        return style;
    }

    /**
     * @return notation
     **/
    public final DbORNotation findNotation() throws DbException {

        DbORNotation notation = getNotation();
        if (notation == null) {

            DbORModel dataModel = (DbORModel) getCompositeOfType(DbORDataModel.metaClass);
            if (dataModel instanceof DbORDataModel) {
                DbORDataModel orDataModel = (DbORDataModel) dataModel;
                if (orDataModel.getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP) {
                    notation = ((DbSMSProject) getProject()).getErDefaultNotation();
                } else {
                    notation = ((DbSMSProject) getProject()).getOrDefaultNotation();
                }
            } else {
                notation = ((DbSMSProject) getProject()).getOrDefaultNotation();
            }
        }
        return notation;
    }

    /**
     * @param form
     *            int
     * @return icon
     **/
    public final Icon getSemanticalIcon(int form) throws DbException {
        if (getComposite() instanceof DbORCommonItemModel)
            return commonItemModelDiagramIcon;
        else if (getComposite() instanceof DbORDomainModel)
            return domainModelDiagramIcon;
        else if (((DbORDataModel) getComposite()).getOperationalMode() == DbORDataModel.LOGICAL_MODE_ENTITY_RELATIONSHIP)
            return conceptualDiagramIcon;
        else
            return dataModelDiagramIcon;
    }

    //Setters

    /**
     * Sets the notation object associated to a DbORDiagram's instance.
     * 
     * @param value
     *            the notation object to be associated
     **/
    public final void setNotation(DbORNotation value) throws DbException {
        basicSet(fNotation, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the notation object associated to a DbORDiagram's instance.
     * 
     * @return the notation object
     **/
    public final DbORNotation getNotation() throws DbException {
        return (DbORNotation) get(fNotation);
    }

    public MetaClass getMetaClass() {
        return metaClass;
    }

}
