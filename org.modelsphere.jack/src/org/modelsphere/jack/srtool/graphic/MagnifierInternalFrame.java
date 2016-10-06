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

package org.modelsphere.jack.srtool.graphic;

import javax.swing.*;

import org.modelsphere.jack.graphic.*;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class MagnifierInternalFrame extends DiagramViewInternalFrame {
    private float zoomFactor = 1.0f;

    public MagnifierInternalFrame() {
        setTitle(LocaleMgr.screen.getString("magnifierTitle"));
        // take the same icon as the action
        setFrameIcon(LocaleMgr.action.getImageIcon("showMagnifierWindow"));
        setClosable(true);
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        setZoomFactor(preferences.getPropertyFloat(MagnifierView.class,
                MagnifierView.ZOOM_FACTOR_PROPERTY,
                new Float(MagnifierView.ZOOM_FACTOR_PROPERTY_DEFAULT)).floatValue());
    }

    protected void createViewComponent(DiagramView diagView) {
        view = new MagnifierView(diagView);
        viewComponent = new JScrollPane(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ((JViewport) view.getParent()).setScrollMode(JViewport.SIMPLE_SCROLL_MODE); // force complete repaint when scrolling
        ((JComponent) view.getParent()).putClientProperty("EnableWindowBlit", null); // NOT LOCALIZABLE property
        view.setZoomFactor(zoomFactor);
    }

    public final void setZoomFactor(float zoomfactor) {
        zoomFactor = zoomfactor;
        if (view == null)
            return;
        view.setZoomFactor(zoomfactor);
    }

}
