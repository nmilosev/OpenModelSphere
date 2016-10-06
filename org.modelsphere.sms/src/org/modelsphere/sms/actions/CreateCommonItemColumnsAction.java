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

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.tree.TreePath;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.actions.SelectionActionListener;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.Diagram;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.graphic.ZoneBox;
import org.modelsphere.jack.graphic.zone.CellID;
import org.modelsphere.jack.graphic.zone.MatrixCellID;
import org.modelsphere.jack.graphic.zone.MatrixZone;
import org.modelsphere.jack.graphic.zone.Zone;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.SrSort;
import org.modelsphere.sms.oo.db.DbOOClass;
import org.modelsphere.sms.oo.db.DbOODataMember;
import org.modelsphere.sms.oo.db.srtypes.OOVisibility;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.db.srtypes.JVVisibility;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORCommonItem;
import org.modelsphere.sms.or.db.DbORTable;
import org.modelsphere.sms.or.db.DbORView;
import org.modelsphere.sms.or.generic.db.DbGEColumn;
import org.modelsphere.sms.or.generic.db.DbGETable;
import org.modelsphere.sms.or.generic.db.DbGEView;
import org.modelsphere.sms.or.ibm.db.DbIBMColumn;
import org.modelsphere.sms.or.ibm.db.DbIBMTable;
import org.modelsphere.sms.or.ibm.db.DbIBMView;
import org.modelsphere.sms.or.informix.db.DbINFColumn;
import org.modelsphere.sms.or.informix.db.DbINFTable;
import org.modelsphere.sms.or.informix.db.DbINFView;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.oracle.db.DbORAColumn;
import org.modelsphere.sms.or.oracle.db.DbORATable;
import org.modelsphere.sms.or.oracle.db.DbORAView;
import org.modelsphere.sms.plugins.TargetSystemInfo;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class CreateCommonItemColumnsAction extends AbstractApplicationAction implements
        SelectionActionListener {

    private static final String kCreateCommonItemColumns = LocaleMgr.action
            .getString("CreateCommonItemColumns");

    /**
     * @param name
     */
    public CreateCommonItemColumnsAction(String name) {
        super(name);
    }

    /**
     * @param name
     * @param icon
     */
    public CreateCommonItemColumnsAction() {
        super(kCreateCommonItemColumns);
        setMnemonic(LocaleMgr.action.getMnemonic("GenerateCommonItems"));
        setVisibilityMode(VISIBILITY_DEFAULT);
        setVisible(false);
    }

    public CreateCommonItemColumnsAction(String name, Icon icon) {
        super(name, icon);
    }

    public void updateSelectionAction() throws DbException {
    }

    public void performAction(DbObject[] commonItems, DbObject tableOrClassorColumnOrField,
            Point location) {
        try {
            boolean bIsNameZone = false;

            if (tableOrClassorColumnOrField instanceof DbORTable
                    || tableOrClassorColumnOrField instanceof DbOOClass)
                bIsNameZone = true;

            DbObject composite = null;
            boolean bIsObjectRelationalTarget = true;
            if (tableOrClassorColumnOrField instanceof DbORTable
                    || tableOrClassorColumnOrField instanceof DbORView)
                composite = tableOrClassorColumnOrField;
            else if (tableOrClassorColumnOrField instanceof DbORColumn) {
                tableOrClassorColumnOrField.getDb().beginReadTrans();
                composite = tableOrClassorColumnOrField.getComposite();
                tableOrClassorColumnOrField.getDb().commitTrans();
            } else if (tableOrClassorColumnOrField instanceof DbOOClass) {
                composite = tableOrClassorColumnOrField;
                bIsObjectRelationalTarget = false;
            } else if (tableOrClassorColumnOrField instanceof DbOODataMember) {
                tableOrClassorColumnOrField.getDb().beginReadTrans();
                composite = tableOrClassorColumnOrField.getComposite();
                tableOrClassorColumnOrField.getDb().commitTrans();
                bIsObjectRelationalTarget = false;
            }
            if (composite != null) {
                composite.getDb().beginWriteTrans(kCreateCommonItemColumns);
                ArrayList compareList = new ArrayList();
                for (int i = 0; i < commonItems.length; i++) {
                    commonItems[i].getDb().beginReadTrans();
                    compareList.add(new DefaultComparableElement(commonItems[i], commonItems[i]
                            .getName()));
                    commonItems[i].getDb().commitTrans();
                }
                DefaultComparableElement[] ts = new DefaultComparableElement[compareList.size()];
                compareList.toArray(ts);
                CollationComparator comparator = new CollationComparator();
                SrSort.sortArray(ts, ts.length, comparator);

                for (int i = 0; i < commonItems.length; i++)
                    commonItems[i] = (DbObject) ts[i].object;

                if (bIsObjectRelationalTarget)
                    performORAction(commonItems, composite, tableOrClassorColumnOrField,
                            bIsNameZone);
                else
                    performOOAction(commonItems, composite, tableOrClassorColumnOrField,
                            bIsNameZone);

                composite.getDb().commitTrans();
            }
        } catch (DbException dbe) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), dbe);
        }
    }

    private void performOOAction(DbObject[] sortedCommonItems, DbObject composite,
            DbObject classOrField, boolean bIsNameZone) throws DbException {
        DbJVDataMember column = null;
        DbRelationN relation = composite.getComponents();
        int insertPosition = 0;
        DbObject lastColumn = null;
        if (bIsNameZone) {
            DbEnumeration allColumns = composite.getComponents().elements(DbJVDataMember.metaClass);
            while (allColumns.hasMoreElements())
                lastColumn = allColumns.nextElement();
            allColumns.close();
            if (lastColumn != null)
                insertPosition = relation.indexOf(lastColumn) + 1;
        } else
            insertPosition = relation.indexOf(classOrField);

        for (int i = 0; i < sortedCommonItems.length; i++) {
            column = new DbJVDataMember(composite);

            DbORCommonItem commonItem = (DbORCommonItem) sortedCommonItems[i];

            // update the column values from the common item values
            column.setName(commonItem.getName());
            column.setPhysicalName(commonItem.getPhysicalName());
            column.setAlias(commonItem.getAlias());
            column.setDescription(commonItem.getDescription());
            column.setUmlStereotype(commonItem.getUmlStereotype());
            column.setCommonItem(commonItem);

            // //
            // set OOProperties

            JVVisibility visibility = (JVVisibility) commonItem.getVisibility();
            if (visibility != null)
                column.setVisibility(visibility);
            else
                column.setVisibility(JVVisibility.getInstance(OOVisibility.PRIVATE));
            column.setTypeUse(commonItem.getTypeUse());
            column.setTypeUseStyle(commonItem.getTypeUseStyle());
            column.setStatic(commonItem.getStatic());
            column.setFinal(commonItem.getFinal());
            column.setTransient(commonItem.getTransient());
            column.setVolatile(commonItem.getVolatile());
            column.setType(commonItem.getOoType());

            int oldIndex = composite.getComponents().indexOf(column);
            composite.reinsert(composite.getComponents().getMetaRelation(), oldIndex,
                    insertPosition++);
        }
    }

    private void performORAction(DbObject[] sortedCommonItems, DbObject composite,
            DbObject tableOrColumn, boolean bIsNameZone) throws DbException {
        DbORColumn column = null;
        DbRelationN relation = composite.getComponents();
        int insertPosition = 0;
        DbObject lastColumn = null;
        if (bIsNameZone) {
            DbEnumeration allColumns = composite.getComponents().elements(DbORColumn.metaClass);
            while (allColumns.hasMoreElements())
                lastColumn = allColumns.nextElement();
            allColumns.close();
            if (lastColumn != null)
                insertPosition = relation.indexOf(lastColumn) + 1;
        } else
            insertPosition = relation.indexOf(tableOrColumn);

        for (int i = 0; i < sortedCommonItems.length; i++) {
            if (composite instanceof DbGETable || composite instanceof DbGEView)
                column = new DbGEColumn(composite);
            else if (composite instanceof DbORATable || composite instanceof DbORAView)
                column = new DbORAColumn(composite);
            else if (composite instanceof DbIBMTable || composite instanceof DbIBMView)
                column = new DbIBMColumn(composite);
            else if (composite instanceof DbINFTable || composite instanceof DbINFView)
                column = new DbINFColumn(composite);

            DbORCommonItem commonItem = (DbORCommonItem) sortedCommonItems[i];

            // update the column values from the common item values
            column.setName(commonItem.getName());
            column.setPhysicalName(commonItem.getPhysicalName());
            column.setAlias(commonItem.getAlias());
            column.setDescription(commonItem.getDescription());
            column.setUmlStereotype(commonItem.getUmlStereotype());
            column.setLength(commonItem.getLength());
            column.setType(commonItem.getType());
            column.setNbDecimal(commonItem.getNbDecimal());
            column.setNull(commonItem.getNull());
            column.setCommonItem(commonItem);

            int oldIndex = composite.getComponents().indexOf(column);
            composite.reinsert(composite.getComponents().getMetaRelation(), oldIndex,
                    insertPosition++);
        }
    }
}
