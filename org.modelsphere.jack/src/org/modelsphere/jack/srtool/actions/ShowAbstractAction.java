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

package org.modelsphere.jack.srtool.actions;

import javax.swing.Icon;
import javax.swing.Action;

import org.modelsphere.jack.actions.AbstractApplicationAction;

public abstract class ShowAbstractAction extends AbstractApplicationAction {

    public static final String COMPONENT_VISIBLE = "Component Visible"; // NOT LOCALIZABLE - PROPERTY KEY

    private String pattern;
    private boolean componentVisible;
    private String hideText;
    private String showText;

    public ShowAbstractAction(String showText, String hideText, boolean initialState) {
        super(hideText);
        this.hideText = hideText;
        this.showText = showText;
        this.setEnabled(true);
        componentVisible = initialState;
        putValue(COMPONENT_VISIBLE, componentVisible ? Boolean.TRUE : Boolean.FALSE);
        refreshName();
    }

    public ShowAbstractAction(String showText, String hideText, boolean initialState, Icon icon) {
        super(hideText, icon);
        this.hideText = hideText;
        this.showText = showText;
        this.setEnabled(true);
        componentVisible = initialState;
        putValue(COMPONENT_VISIBLE, componentVisible ? Boolean.TRUE : Boolean.FALSE);
        refreshName();
    }

    protected void doActionPerformed() {
        if (componentVisible)
            hideComponent();
        else
            showComponent();
        componentVisible = !componentVisible;
        putValue(COMPONENT_VISIBLE, componentVisible ? Boolean.TRUE : Boolean.FALSE);
        refreshName();
    }

    private final void refreshName() {
        if (componentVisible)
            putValue(Action.NAME, hideText);
        else
            putValue(Action.NAME, showText);
    }

    public final boolean isComponentVisible() {
        return componentVisible;
    }

    abstract protected void showComponent();

    abstract protected void hideComponent();

}
