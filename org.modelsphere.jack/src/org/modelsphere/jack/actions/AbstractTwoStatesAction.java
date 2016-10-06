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

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.util.ExceptionHandler;

public abstract class AbstractTwoStatesAction extends AbstractApplicationAction {

    public static final String STATE = "state"; // NOT LOCALIZABLE, property key

    // Possible states
    public static final int STATE_NOT_APPLICABLE = -1;
    public static final int STATE_OFF = 0;
    public static final int STATE_ON = 1;

    public static final int DEFAULT_STATE = STATE_NOT_APPLICABLE;

    public AbstractTwoStatesAction(String name) {
        this(name, null);
    }

    public AbstractTwoStatesAction(String name, Icon icon) {
        super(name, icon);
        super.setEnabled(false);
    }

    public int getState() {
        Integer state = (Integer) getValue(STATE);
        if (state != null)
            return state.intValue();
        else {
            this.putValue(STATE, new Integer(DEFAULT_STATE));
            return DEFAULT_STATE;
        }
    }

    // I override this one because this method is public and i want to be sure
    // that
    // no one make a setEnabled(true) when the state is STATE_NOT_APPLICABLE
    // and setEnabled(true) when the state is different from
    // STATE_NOT_APPLICABLE
    // the enabled property is manage by the setState method. The isEnabled()
    // method
    // still work correctly.
    // If we need a way to setEnabled(false) all actions without making
    // particular code
    // depending of the class of the action, we can add this code in the body:
    // if (b == false)
    // setState(STATE_NOT_APPLICABLE);
    // But we will not be able to do a setEnabled(true) because we can't
    // determine which
    // other state to set.
    //
    /*
     * public final void setEnabled(boolean b){ Debug.assert(true,
     * "Must use setState(STATE_NOT_APPLICABLE) instead of setEnabled() for AbstractTwoStatesAction"
     * ); }
     */

    // must be overridden by subclasses if other possible values for state are
    // possible
    protected void validateState(int stateValue) {
        Debug.assert2((stateValue == STATE_ON) || (stateValue == STATE_OFF)
                || (stateValue == STATE_NOT_APPLICABLE),
                "Invalid State for an AbstractTwoStatesAction"); // NOT
        // LOCALIZABLE,
        // property
        // key
    }

    public synchronized void setState(int newValue) {
        try {
            validateState(newValue);
            int oldValue = getState();
            if (newValue != oldValue) {
                this.putValue(STATE, new Integer(newValue));
                if (newValue == STATE_NOT_APPLICABLE) {
                    super.setEnabled(false);
                } else {
                    super.setEnabled(true);
                }
            }
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(null, e);
        }
    }

}
