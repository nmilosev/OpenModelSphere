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

package org.modelsphere.jack.actions;

import javax.swing.Icon;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.ApplicationContext;

public abstract class AbstractTriStatesAction extends AbstractTwoStatesAction {

    // Possible states:
    // - STATE_ON (inherited from AbstractTwoStatesAction)
    // - STATE_OFF (inherited from AbstractTwoStatesAction)
    // - STATE_NOT_APPLICABLE (inherited from AbstractTwoStatesAction)
    // - STATE_ON_OFF (specific to AbstractTriStatesAction)
    // Note: state is the current state of objects on which action could be
    // performed.
    // for example, if state = STATE_ON, all objects are currently at ON. If
    // action
    // is performed, all objects will be setted OFF.
    public static final int STATE_ON_OFF = 2;

    public AbstractTriStatesAction(String name) {
        this(name, null);
    }

    public AbstractTriStatesAction(String name, Icon icon) {
        super(name, icon);
    }

    protected final void validateState(int stateValue) {
        Debug.assert2((stateValue == STATE_ON) || (stateValue == STATE_OFF)
                || (stateValue == STATE_ON_OFF) || (stateValue == STATE_NOT_APPLICABLE),
                "Invalid State for an AbstractTriStatesAction"); // NOT
        // LOCALIZABLE
    }

    protected final void performTriState(Object[] objs, String transName) {
        Boolean value = getStateToPerform();
        try {
            DbMultiTrans.beginTrans(Db.WRITE_TRANS, objs, transName);
            for (int i = 0; i < objs.length; i++)
                setObjectValue(objs[i], value);
            DbMultiTrans.commitTrans(objs);
            // would be better with a trigger, but probably too costly to do it
            if (getUpdateSelectionMode() == UPDATE_SELECTION_ONLINE)
                setState(value.booleanValue() ? STATE_ON : STATE_OFF);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

    protected final void updateTriState(Object[] objs) throws DbException {
        int state = STATE_NOT_APPLICABLE;
        for (int i = 0; i < objs.length; i++) {
            Boolean value = getObjectValue(objs[i]);
            if (value == null)
                continue;
            state = updateTriState(state, value.booleanValue());
            if (state == STATE_ON_OFF)
                break;
        }
        setState(state);
    }

    protected final int updateTriState(int state, boolean value) {
        if (state == STATE_ON_OFF)
            return state;
        if (value)
            return (state == STATE_OFF ? STATE_ON_OFF : STATE_ON);
        else
            return (state == STATE_ON ? STATE_ON_OFF : STATE_OFF);
    }

    protected final Boolean getStateToPerform() {
        return (getState() == STATE_ON ? Boolean.FALSE : Boolean.TRUE);
    }

    // Returns null if object incompatible with the action
    // Overridden
    protected Boolean getObjectValue(Object obj) throws DbException {
        return null;
    }

    // Does nothing if the object is incompatible with the action
    // Overridden
    protected void setObjectValue(Object obj, Boolean value) throws DbException {
    }
}
