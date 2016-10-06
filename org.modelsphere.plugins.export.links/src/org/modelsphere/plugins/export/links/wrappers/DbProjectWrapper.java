/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links.wrappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.sms.db.DbSMSLinkModel;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDataModel;

public class DbProjectWrapper {
    private Matrix m_dockingMatrix = null;
    private List<DbSMSProject> m_projects;
    private List<DbLinkModelWrapper> m_linkModels = null;

    private DbProjectWrapper() {
        m_projects = new ArrayList<DbSMSProject>();
        m_linkModels = new ArrayList<DbLinkModelWrapper>();
    }

    private static DbProjectWrapper g_instance = null;

    public static DbProjectWrapper getInstance() {
        if (g_instance == null) {
            g_instance = new DbProjectWrapper();
        }

        return g_instance;
    }

    public void clear() {
        m_dockingMatrix = null;
        m_projects.clear();
        m_linkModels.clear();
    }

    public void addProject(DbSMSProject project) throws DbException {
        m_projects.add(project);

        //for each project component
        DbRelationN relN = project.getComponents();
        DbEnumeration enu = relN.elements();
        while (enu.hasMoreElements()) {
            DbObject o = enu.nextElement();
            if (o instanceof DbSMSLinkModel) {
                DbSMSLinkModel lm = (DbSMSLinkModel) o;
                DbLinkModelWrapper linkModel = new DbLinkModelWrapper(this, lm);
                m_linkModels.add(linkModel);
            } else if (o instanceof DbORDataModel) {
                DbORDataModel dataModel = (DbORDataModel) o;
                addDataModel(dataModel);
            } //end if
        } //end while
        enu.close();
    }

    public void addDataModel(DbORDataModel dataModel) throws DbException {
        DbSMSProject project = (DbSMSProject) dataModel.getCompositeOfType(DbSMSProject.metaClass);
        if (!m_projects.contains(project)) {
            m_projects.add(project);
        }

        //for each project component
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration enu = relN.elements();
        while (enu.hasMoreElements()) {
            DbObject o = enu.nextElement();
            if (o instanceof DbSMSLinkModel) {
                DbSMSLinkModel lm = (DbSMSLinkModel) o;
                addLinkModel(lm);
            } else if (o instanceof DbORDataModel) {
                DbORDataModel subDataModel = (DbORDataModel) o;
                addDataModel(subDataModel);
            } //end if
        } //end while
        enu.close();
    }

    public void addLinkModel(DbSMSLinkModel lm) throws DbException {
        DbSMSProject project = (DbSMSProject) lm.getCompositeOfType(DbSMSProject.metaClass);
        if (!m_projects.contains(project)) {
            m_projects.add(project);
        }

        DbLinkModelWrapper linkModel = new DbLinkModelWrapper(this, lm);
        m_linkModels.add(linkModel);
    }

    public String getDockingHeader() throws DbException {
        //create matrix, if not done
        if (m_dockingMatrix == null) {
            buildDockingMatrix();
        }

        String header = m_dockingMatrix.getHeader();
        return header;
    }

    public String getDockingUnderline() throws DbException {
        //create matrix, if not done
        if (m_dockingMatrix == null) {
            buildDockingMatrix();
        }

        String underline = m_dockingMatrix.getDockingUnderline();
        return underline;
    }

    public List<Matrix.Row> getDockingRows() throws DbException {
        //create matrix, if not done
        if (m_dockingMatrix == null) {
            buildDockingMatrix();
        }

        //return rows of the matrix
        List<Matrix.Row> rows = m_dockingMatrix.getRows();
        return rows;
    } //end getDockedColumns

    private void buildDockingMatrix() throws DbException {
        int rowIdx = 0;
        m_dockingMatrix = new Matrix(this);
        List<DbLinkModelWrapper> linkModels = getLinkModels();

        //scan all links
        for (DbLinkModelWrapper linkModel : linkModels) {
            List<DbLinkWrapper> links = linkModel.getLinks();
            for (DbLinkWrapper link : links) {
                addRow(link, rowIdx);
                rowIdx++;
            } //end for
        } //end for

        //sort models
        m_dockingMatrix.arrangeMatrix();
    } //end buildDockingMatrix()

    private void addRow(DbLinkWrapper link, int rowIdx) throws DbException {
        List<DbColumnWrapper> srcColumns = link.getSourceColumns();
        List<DbColumnWrapper> destColumns = link.getDestinationColumns();
        List<DbColumnWrapper> linkedColumns = new ArrayList<DbColumnWrapper>();

        //add source columns
        for (DbColumnWrapper col : srcColumns) {
            linkedColumns.add(col);
            DbTableWrapper table = col.getParent();
            DbDataModelWrapper dataModel = table.getDataModel(this);
            Matrix.Column matrixColumn = m_dockingMatrix.findColumn(dataModel);
            matrixColumn.addCell(col, rowIdx);
        } //end for

        //add destination columns
        for (DbColumnWrapper col : destColumns) {
            linkedColumns.add(col);
            DbTableWrapper table = col.getParent();
            DbDataModelWrapper dataModel = table.getDataModel(this);
            Matrix.Column matrixColumn = m_dockingMatrix.findColumn(dataModel);
            matrixColumn.addCell(col, rowIdx);
        } //end for

        //add super copy
        for (DbColumnWrapper col : linkedColumns) {
            DbColumnWrapper sc = col.getSuperColumn(this);
            if (sc != null) {
                DbDataModelWrapper dataModel = sc.getParent().getParent();
                Matrix.Column matrixColumn = m_dockingMatrix.findColumn(dataModel);
                matrixColumn.addCell(sc, rowIdx);
            } //end if
        } //end for
    } //end addRow()

    public List<DbLinkModelWrapper> getLinkModels() {
        return m_linkModels;
    } //end getLinkModels()

    private Map<DbORDataModel, DbDataModelWrapper> dataModels = new HashMap<DbORDataModel, DbDataModelWrapper>();

    public DbDataModelWrapper getDataModel(DbORDataModel dm) {
        DbDataModelWrapper dataModel = dataModels.get(dm);
        if (dataModel == null) {
            dataModel = DbDataModelWrapper.getInstance(dm);
            dataModels.put(dm, dataModel);
        }

        return dataModel;
    }

    private List<DbUdfWrapper> m_udfs = null;

    public List<DbUdfWrapper> getUdfs() {
        if (m_udfs == null) {
            m_udfs = new ArrayList<DbUdfWrapper>();
            List<DbUDF> udfs = getUDFs();
            for (DbUDF udf : udfs) {
                DbUdfWrapper udf1 = new DbUdfWrapper(udf);
                m_udfs.add(udf1);
            } //end for
        } //end if

        return m_udfs;
    }

    public List<DbUDF> getUDFs() {
        List<DbUDF> udfs = new ArrayList<DbUDF>();

        try {
            for (DbSMSProject project : m_projects) {
                DbRelationN relN = project.getComponents();
                DbEnumeration enu = relN.elements(DbUDF.metaClass);
                while (enu.hasMoreElements()) {
                    DbUDF udf = (DbUDF) enu.nextElement();
                    MetaClass mc = udf.getUDFMetaClass();

                    if (DbORColumn.metaClass.isAssignableFrom(mc)) {
                        udfs.add(udf);
                    }
                } //end while
                enu.close();
            } //end for

        } catch (DbException ex) {

        }

        return udfs;
    }

    public int getNbLinks() {
        int nbLinks = 0;

        try {
            for (DbLinkModelWrapper linkModel : m_linkModels) {
                nbLinks += linkModel.getLinks().size();
            }
        } catch (DbException ex) {
            nbLinks = -1;
        }

        return nbLinks;
    }

} //end ProjectWrapper
