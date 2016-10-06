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

package org.modelsphere.sms.screen.plugins;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingConstants;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.baseDb.screen.plugins.MultiDbSemanticalObjectShortEditor;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.java.db.DbJVClass;

/**
 * 
 * Editor for fields: DbORIndex.fConstraint, DbORPrimaryUnique.fIndex, DbORForeign.fIndex
 * 
 */
public final class MultiDbSMSStereotypeEditor extends MultiDbSemanticalObjectShortEditor {

    protected final Collection getSelectionSet(DbObject parentDbo) throws DbException {
        ArrayList dbos = new ArrayList();

        // Check for a special case
        if (parentDbo instanceof DbJVClass) {
            DbJVClass claz = (DbJVClass) parentDbo;
            if (claz.isInterface() || claz.isException()) {
                DbSMSStereotype stereotype = claz.getUmlStereotype();
                if (stereotype != null) {
                    dbos.add(stereotype);
                }
                return dbos; // interfaces and exceptions have already their
                // stereotypes..
            }
        } // end if

        DbEnumeration dbEnum = parentDbo.getProject().getComponents().elements(
                DbSMSUmlExtensibility.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbObject dbo = dbEnum.nextElement();
            DbEnumeration enum2 = dbo.getComponents().elements(DbSMSStereotype.metaClass);
            while (enum2.hasMoreElements()) {
                DbSMSStereotype stereotype = (DbSMSStereotype) enum2.nextElement();
                if (stereotype.getStereotypeMetaClass().isAssignableFrom(parentDbo.getMetaClass()))
                    dbos.add(stereotype);
            }
            enum2.close();
        }
        dbEnum.close();
        return dbos;
    }

    protected final String getStringForNullValue() {
        return MultiDefaultRenderer.kNone;
    }

    protected Object getUserObject(DbObject dbo) throws DbException {
        if (dbo == null || !(dbo instanceof DbSMSStereotype))
            return null;
        return ((DbSMSStereotype) dbo).getIcon();
    }

    protected void configureJComboBox(JComboBox combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (!(value instanceof DefaultComparableElement))
                    return this;
                DefaultComparableElement element = (DefaultComparableElement) value;
                if (element.object2 != null) {
                    setIcon(new ImageIcon((Image) element.object2) {
                        public int getIconHeight() {
                            int height = super.getIconHeight();
                            return height > 30 ? 30 : height;
                        }

                    });
                    setHorizontalTextPosition(SwingConstants.LEFT);
                }
                return this;
            }
        });
    }

}
