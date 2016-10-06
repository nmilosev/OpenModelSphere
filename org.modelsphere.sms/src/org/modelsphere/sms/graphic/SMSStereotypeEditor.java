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

/*
 * Created on Aug 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.graphic;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.SwingConstants;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.MultiDefaultRenderer;
import org.modelsphere.jack.graphic.zone.StereotypeCellEditor;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSStereotype;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.oo.java.db.DbJVClass;

/**
 * @author nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class SMSStereotypeEditor extends StereotypeCellEditor {

    public SMSStereotypeEditor(MetaField mf) {
        super(mf);
    }

    public void populateCombo() {

        domValues = new ArrayList<DbObject>();

        DbObject parentDbo = (DbObject) value.getObject();
        try {
            parentDbo.getDb().beginTrans(Db.READ_TRANS);

            //Check for a special case
            boolean bIsException = false;
            if (parentDbo instanceof DbJVClass) {
                DbJVClass claz = (DbJVClass) parentDbo;
                if (claz.isInterface() || claz.isException()) {
                    bIsException = true;
                    DbSMSStereotype stereotype = claz.getUmlStereotype();
                    if (stereotype != null)
                        domValues.add(stereotype);
                }
            }
            if (bIsException == false) {
                DbEnumeration dbEnum = parentDbo.getProject().getComponents().elements(
                        DbSMSUmlExtensibility.metaClass);
                while (dbEnum.hasMoreElements()) {
                    DbObject dbo = dbEnum.nextElement();
                    DbEnumeration enum2 = dbo.getComponents().elements(DbSMSStereotype.metaClass);
                    while (enum2.hasMoreElements()) {
                        DbSMSStereotype stereotype = (DbSMSStereotype) enum2.nextElement();
                        if (stereotype.getStereotypeMetaClass().isAssignableFrom(
                                parentDbo.getMetaClass()))
                            domValues.add(stereotype);
                    }
                    enum2.close();
                }
                dbEnum.close();
            }

            DbObject setObject = (DbObject) parentDbo.get(metaField);
            for (int i = 0; i < domValues.size(); i++)
                combo.addItem(domValues.get(i));
            if (setObject != null)
                combo.setSelectedItem(setObject);

            parentDbo.getDb().commitTrans();
            configureJComboBox(combo);

            if (combo.getItemCount() > 0)
                combo.addItem(null);

        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(
                    ((ApplicationDiagram) diagram).getMainView(), ex);
        }
    }

    protected void configureJComboBox(JComboBox combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                try {
                    if (value != null) {
                        DbObject dbo = (DbObject) value;
                        dbo.getDb().beginReadTrans();
                        setText(dbo.getName());
                        Image icon = ((DbSMSStereotype) dbo).getIcon();
                        dbo.getDb().commitTrans();
                        if (icon != null) {
                            setIcon(new ImageIcon(icon) {
                                public int getIconHeight() {
                                    int height = super.getIconHeight();
                                    return height > 30 ? 30 : height;
                                }
                            });
                        }
                    } else
                        setText(MultiDefaultRenderer.kNone);
                    setHorizontalTextPosition(SwingConstants.LEFT);
                } catch (DbException dbe) {
                }
                return this;
            }
        });
    }
}
