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

/*
 * Created on Oct 6, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.jack.srtool.actions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.modelsphere.jack.actions.AbstractTriStatesAction;
import org.modelsphere.jack.graphic.Grid;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.international.LocaleMgr;

/**
 * @author nicolask
 * 
 */
@SuppressWarnings("serial")
public class ActivateGridAction extends AbstractTriStatesAction implements PropertyChangeListener {

    private static final String kGrid = LocaleMgr.action.getString("ActivateGrid");

    public ActivateGridAction() {
        super(kGrid);
        this.setMnemonic(LocaleMgr.action.getMnemonic("ActivateGrid"));
        setDefaultToolBarVisibility(false);
        init();
        update();
    }

    public void init() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean value = false;
        if (options != null)
            value = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE,
                    Grid.PROPERTY_GRID_ACTIVE_DEFAULT);
        setState(value ? STATE_ON : STATE_OFF);

        options.addPropertyChangeListener(Grid.class, Grid.PROPERTY_HIDE_GRID, this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        update();
    }

    protected final void doActionPerformed() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean value = false;
        if (options != null)
            value = options.getPropertyBoolean(Grid.class, Grid.PROPERTY_GRID_ACTIVE,
                    Grid.PROPERTY_GRID_ACTIVE_DEFAULT);
        options.setProperty(Grid.class, Grid.PROPERTY_GRID_ACTIVE, !value);
        setState(!value ? STATE_ON : STATE_OFF);
    }

    private void update() {
        PropertiesSet options = PropertiesManager.APPLICATION_PROPERTIES_SET;
        boolean gridvisible = false;
        if (options != null)
            gridvisible = !options.getPropertyBoolean(Grid.class, Grid.PROPERTY_HIDE_GRID,
                    Grid.PROPERTY_HIDE_GRID_DEFAULT);
        setEnabled(gridvisible);
    }

}
