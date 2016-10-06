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

package org.modelsphere.jack.srtool;

import java.awt.Image;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.screen.DbListView;
import org.modelsphere.jack.baseDb.screen.plugins.MultiTimestampRenderer;
import org.modelsphere.jack.baseDb.util.Terminology;
import org.modelsphere.jack.srtool.list.ListDescriptor;
import org.modelsphere.jack.srtool.list.ListTableModel;

public abstract class SemanticalModel {
    public static final int NAME_SHORT_FORM = DbObject.SHORT_FORM;
    public static final int NAME_LONG_FORM = DbObject.LONG_FORM;
    public static final int NAME_CUSTOM_FORM = 100;

    protected Terminology terminology = null;

    public abstract Terminology getTerminology();

    public boolean isVisibleOnScreen(DbObject composite, DbObject dbo, Class context)
            throws DbException {
        return true;
    }

    // Block metafields to be displayed for the specified dbo
    // return true to allow the specified metafield to be displayed
    // Invoked within a read transaction
    // multiSelection indicates if the current DbObject selection has more than
    // one object
    public boolean isVisibleOnScreen(MetaClass metaclass, MetaField metafield, DbObject dbo,
            boolean multiSelection, Class context) throws DbException {
        return metafield == null ? false : metafield.isVisibleInScreen();
    }

    public final boolean isVisibleOnScreen(MetaClass metaclass, MetaField metafield, DbObject dbo,
            Class context) throws DbException {
        return isVisibleOnScreen(metaclass, metafield, dbo, false, context);
    }

    // Return true if the specified dbo's name or physical name can be edited
    // (for the specified metafield)
    public boolean isEditable(MetaField metafield, DbObject dbo, Class context) throws DbException {
        return true;
    }

    public String getDisplayText(DbObject dbo, Class context) throws DbException {
        return getDisplayText(dbo, NAME_CUSTOM_FORM, null, context);
    }

    // form: NAME_SHORT_FORM, NAME_LONG_FORM or NAME_CUSTOM_FORM
    public String getDisplayText(DbObject dbo, int form, DbObject root, Class context)
            throws DbException {
        if (dbo == null)
            return "";
        if ((context != null) && (DbListView.class.isAssignableFrom(context))) {
            String name = dbo.getSemanticalName(DbObject.LONG_FORM);
            int i = name.lastIndexOf('.');
            if (i != -1)
                name = name.substring(i + 1) + "  (" + name.substring(0, i) + ")"; // NOT LOCALIZABLE
            return name;
        }

        String name = (form == DbObject.LONG_FORM) ? dbo.buildFullNameString() : dbo.getName();
        return name;
    }

    // Display Text For a MetaField
    public String getDisplayText(MetaClass metaclass, MetaField metafield, Class context,
            boolean isGroup) {
        if (metafield == null && metaclass == null)
            return null;
        if (metafield == null)
            return getName(metaclass, isGroup);
        return getName(metaclass, metafield, isGroup);
    }

    public String getDisplayText(MetaClass metaclass, MetaField metafield, Class context) {
        return getDisplayText(metaclass, metafield, context, false);
    }

    protected String getName(MetaClass metaClass) {
        return getName(metaClass, false);
    }

    protected String getName(MetaClass metaClass, boolean isGroup) {
        String name = null;
        if (terminology != null)
            name = terminology.getTerm(metaClass, isGroup);
        else
            name = metaClass.getGUIName(isGroup, false);
        return name;
    }

    protected String getName(MetaClass metaClass, MetaField metaField) {
        return getName(metaClass, metaField, false);
    }

    protected String getName(MetaClass metaClass, MetaField metaField, boolean isGroup) {
        String name = null;
        if (terminology != null)
            name = terminology.getTerm(metaClass, metaField, isGroup);
        else
            name = metaField.getGUIName();
        return name;
    }

    // Display Text For a MetaField depending on a specific dbo context
    public String getDisplayText(MetaClass metaclass, MetaField metafield, DbObject dbo,
            Class context) throws DbException {
        return getDisplayText(metaclass, metafield, context);
    }

    public abstract String getDisplayText(MetaClass metaclass, MetaField metafield, DbObject dbo,
            Class context, boolean bIsGroup) throws DbException;

    public String getClassDisplayText(DbObject dbo, Class context) throws DbException {
        return getName(dbo.getMetaClass());
    }

    // BEWARE: compare <metaClass> with final classes.
    // If this list becomes long, it is better to add a new UDF in the meta <GUI
    // name for UDF>
    public String getDisplayTextForUDF(MetaClass metaClass) {
        return metaClass.getGUIName(false, true);
    }

    // Special case workaround for stereotype metaclass (used by design panel
    // only)
    public Object getDisplayValue(Object value, MetaField field, Class context) {
        if (field == DbObject.fCreationTime || field == DbObject.fModificationTime) {
            if (value instanceof Long) {
                DateFormat df = new SimpleDateFormat(MultiTimestampRenderer.DATETIME_FORMAT);
                df.setTimeZone(TimeZone.getDefault());
                return df.format((Long) value);
            }
        }
        return value;
    }

    public Image getImage(DbObject dbo, Class context) throws DbException {
        return null;
    }

    public boolean isNameEditable(DbObject dbo, Class context) throws DbException {
        return true;
    }

    public abstract ListDescriptor[] getListDescriptors(DbObject composite) throws DbException;

    public abstract boolean isValidForCopy(DbObject[] dbos) throws DbException;

    public abstract boolean isValidForPaste(DbObject[] dbos, DbObject[] composites)
            throws DbException;

    public abstract boolean isValidForDrag(DbObject[] dbos) throws DbException;

    public abstract boolean isValidForDrop(DbObject[] dbos, DbObject[] composites)
            throws DbException;

    public abstract boolean isValidForPasteOffline(DbObject[] dbos, DbObject[] composites);

    public abstract void paste(DbObject[] dbos, DbObject[] composites, String actionName,
            boolean prefixAllowed) throws DbException;

    public abstract void paste(DbObject[] dbos, DbObject[] composites, String actionName,
            boolean prefixAllowed, Point location) throws DbException;

    public abstract boolean isValidCopyOperation(DbObject[] dragObjs, DbObject dropObj)
            throws DbException;

    public abstract boolean isCreateCommonItems(DbObject[] dragObjs, DbObject dropObj);

    public abstract int getDefaultDropAction(DbObject[] dragObjs, DbObject dropObj)
            throws DbException;

    public abstract boolean isLinkQualifierOrResource(DbObject[] dragObjs, DbObject dropObj);

    public abstract DbRelationN getGos(DbObject semObj) throws DbException;

    public abstract ArrayList getLineLabels(DbObject semObj) throws DbException;

    // Order is important (will be displayed according to the order in the
    // returned array)
    public abstract AddElement[] getAddElements();

    public ListTableModel createListTableModel(DbObject root, ListDescriptor descriptor)
            throws DbException {
        return new ListTableModel(root, descriptor);
    }

}
