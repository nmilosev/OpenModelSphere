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

package org.modelsphere.jack.baseDb.meta;

import java.lang.reflect.Field;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.db.event.*;
import org.modelsphere.jack.util.SrVector;

/**
 * A property defined on a meta-class. Meta-relationshio is a subclass of meta-field. <br>
 * <br>
 * MetaField provides the following functionality: <li>getGUIName(): returns the display name of the
 * property. The string is language-dependent.
 * 
 */
public class MetaField {

    /* Flags in MetaField.flags */
    public final static int COPY_REFS = 0x0001;
    public final static int HUGE_RELN = 0x0002;
    public final static int INTEGRABLE = 0x0004; // only for relN
    public final static int WRITE_CHECK = 0x0008; // only for relN
    public final static int NO_WRITE_CHECK = 0x0010;
    public final static int INTEGRABLE_BY_NAME = 0x0020; // only for rel1: when
    // outside of integration scope (not matchable), allow matching by name

    private String GUIName;
    private MetaClass metaClass;
    private Field jField;
    private int flags;

    // //////////////
    // screen properties
    private boolean visibleInScreen = true;
    private boolean editable = true;
    private String screenOrder = null;// "<fieldName" = before field, ">fieldName" = after field, "<" = at beginning, ">" = at end. // NOT LOCALIZABLE
    private String rendererPluginName = null;// when null a default renderer is used based on field type end of screen properties
    // ///////////////

    private SrVector updateListeners;
    private SrVector refreshListeners;

    public static void addDbUpdateListener(DbUpdateListener listener, int prio,
            MetaField[] metaFields) {
        for (int i = 0; i < metaFields.length; i++)
            metaFields[i].addDbUpdateListener(listener, prio);
    }

    public static void removeDbUpdateListener(DbUpdateListener listener, MetaField[] metaFields) {
        for (int i = 0; i < metaFields.length; i++)
            metaFields[i].removeDbUpdateListener(listener);
    }

    public static void addDbRefreshListener(DbRefreshListener listener, DbProject project,
            MetaField[] metaFields) {
        for (int i = 0; i < metaFields.length; i++)
            metaFields[i].addDbRefreshListener(listener, project);
    }

    public static void removeDbRefreshListener(DbRefreshListener listener, MetaField[] metaFields) {
        for (int i = 0; i < metaFields.length; i++)
            metaFields[i].removeDbRefreshListener(listener);
    }

    public MetaField(String GUIName) {
        this(GUIName, 0);
    }

    public MetaField(String GUIName, int flags) {
        this.GUIName = GUIName;
        this.flags = flags;
    }

    /**
     * Returns the display name of the property. The string is language-dependent.
     */
    public final String getGUIName() {
        return GUIName;
    }

    public final String getJName() {
        return jField.getName();
    }

    public final Field getJField() {
        return jField;
    }

    public final void setJField(Field jField) {
        this.jField = jField;
        jField.setAccessible(true);
    }

    public final int getFlags() {
        return flags;
    }

    public final void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Returns the meta-class to which this property belongs.
     */
    public final MetaClass getMetaClass() {
        return metaClass;
    }

    public final String toString() {
        return metaClass.toString() + "." + GUIName;
    }

    public final void addDbUpdateListener(DbUpdateListener listener, int prio) {
        if (updateListeners == null)
            updateListeners = new SrVector();
        DbUpdateListenerElement elem = new DbUpdateListenerElement(listener, prio);
        int index = updateListeners.indexOfUsingEquals(elem, 0);
        if (index == -1)
            updateListeners.addElement(elem);
        else
            updateListeners.setElementAt(elem, index);
    }

    public final void removeDbUpdateListener(DbUpdateListener listener) {
        if (updateListeners == null)
            return;
        DbUpdateListenerElement elem = new DbUpdateListenerElement(listener, 0);
        int index = updateListeners.indexOfUsingEquals(elem, 0);
        if (index != -1)
            updateListeners.removeElementAt(index);
    }

    public final boolean hasDbUpdateListeners() {
        return (updateListeners != null && updateListeners.size() != 0);
    }

    public final void addDbUpdateListenerCalls(SrVector listenerCalls, DbUpdateEvent event) {
        if (updateListeners == null)
            return;
        int nb = updateListeners.size();
        for (int i = 0; i < nb; i++) {
            DbUpdateListenerElement elem = (DbUpdateListenerElement) updateListeners.elementAt(i);
            listenerCalls.addElement(new DbUpdateListenerCall(elem.listener, event, elem.prio));
        }
    }

    /* refreshListeners vector currently in enumeration, and its current index. */
    private static SrVector refreshInEnum;
    private static int refreshIndex;

    public final void addDbRefreshListener(DbRefreshListener listener) {
        addDbRefreshListener(listener, null);
    }

    public final void addDbRefreshListener(DbRefreshListener listener, DbProject project) {
        if (refreshListeners == null)
            refreshListeners = new SrVector();
        DbRefreshListenerElement elem = new DbRefreshListenerElement(listener, project);
        int index = refreshListeners.indexOfUsingEquals(elem, 0);
        if (index == -1)
            refreshListeners.addElement(elem);
        else
            refreshListeners.setElementAt(elem, index);
    }

    public final void removeDbRefreshListener(DbRefreshListener listener) {
        if (refreshListeners == null)
            return;
        DbRefreshListenerElement elem = new DbRefreshListenerElement(listener, null);
        int index = refreshListeners.indexOfUsingEquals(elem, 0);
        if (index != -1) {
            refreshListeners.removeElementAt(index);
            /*
             * If remove from currently enumerated vector, correct the enumeration index
             */
            if (refreshInEnum == refreshListeners && refreshIndex > index)
                refreshIndex--;
        }
    }

    public final boolean hasDbRefreshListeners() {
        return (refreshListeners != null && refreshListeners.size() != 0);
    }

    public final void fireDbRefreshListeners(DbUpdateEvent event) throws DbException {
        if (refreshListeners == null)
            return;
        refreshInEnum = refreshListeners;
        refreshIndex = refreshInEnum.size();
        DbProject project = event.dbo.getProject();
        while (--refreshIndex >= 0) {
            DbRefreshListenerElement elem = (DbRefreshListenerElement) refreshInEnum
                    .elementAt(refreshIndex);
            if (elem.project == null || elem.project == project) {
                if (elem.listener != null) {
                    elem.listener.refreshAfterDbUpdate(event);
                }
            }
        }
        refreshInEnum = null;
    }

    final void setMetaClass(MetaClass metaClass) {
        this.metaClass = metaClass;
    }

    private static class DbUpdateListenerElement {

        final DbUpdateListener listener;
        final int prio;

        DbUpdateListenerElement(DbUpdateListener listener, int prio) {
            this.listener = listener;
            this.prio = prio;
        }

        public boolean equals(Object elem) {
            if (elem == null)
                return false;
            if (!(elem instanceof DbUpdateListenerElement))
                return false;
            return (listener == ((DbUpdateListenerElement) elem).listener);
        }
    }

    private static class DbRefreshListenerElement {

        final DbRefreshListener listener;
        final DbProject project;

        DbRefreshListenerElement(DbRefreshListener listener, DbProject project) {
            this.listener = listener;
            this.project = project;
        }

        public boolean equals(Object elem) {
            if (elem == null)
                return false;
            if (!(elem instanceof DbRefreshListenerElement))
                return false;
            return (listener == ((DbRefreshListenerElement) elem).listener);
        }
    }

    public final boolean isVisibleInScreen() {
        return visibleInScreen;
    }

    public final void setVisibleInScreen(boolean val) {
        visibleInScreen = val;
    }

    public final boolean isEditable() {
        return editable;
    }

    public final void setEditable(boolean val) {
        editable = val;
    }

    public final String getScreenOrder() {
        return screenOrder;
    }

    public final void setScreenOrder(String val) {
        screenOrder = val;
    }

    public final String getRendererPluginName() {
        return rendererPluginName;
    }

    public final void setRendererPluginName(String val) {
        rendererPluginName = val;
    }
}
