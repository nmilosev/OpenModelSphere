/*************************************************************************

Copyright (C) 2012 Grandite

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

package org.modelsphere.jack.srtool.features.layout;

import java.awt.event.ActionEvent;
import java.util.List;

import org.modelsphere.jack.plugins.Plugin;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

/**
 * The Class LayoutPlugin.
 */
public abstract class LayoutPlugin implements Plugin {

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#doListenSelection()
     */
    @Override
    public final boolean doListenSelection() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#execute(java.awt.event.ActionEvent)
     */
    @Override
    public final void execute(ActionEvent ev) throws Exception {
    }

    /**
     * Gets the layout algorithms.
     * 
     * @return the layout algorithms
     */
    public abstract List<LayoutAlgorithm> getLayoutAlgorithms();

    /**
     * Gets the default layout algorithms.
     * 
     * @return the default layout algorithms
     */
    public abstract List<LayoutAlgorithm> getDefaultLayoutAlgorithms();

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#getSignature()
     */
    @Override
    public final PluginSignature getSignature() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#getSupportedClasses()
     */
    @Override
    public final Class<? extends Object>[] getSupportedClasses() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.modelsphere.jack.plugins.Plugin#installAction(org.modelsphere.jack
     * .srtool.DefaultMainFrame , org.modelsphere.jack.srtool.MainFrameMenu)
     */
    @Override
    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

}
