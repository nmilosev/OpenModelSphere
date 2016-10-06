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
package org.modelsphere.sms.oo.java.graphic.tool;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.oo.java.JavaModule;
import org.modelsphere.sms.oo.java.db.srtypes.JVClassCategory;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class JVInterfaceTool extends JVBoxTool {

    public JVInterfaceTool(String text, Image image) {
        super(text, image);
    }

    protected Cursor loadDefaultCursor() {
        // return AwtUtil.loadCursor(OOModule.class,
        // "international/resources/interfaceCursor.gif", new Point(8,8),
        // "box");//NOT LOCALIZABLE, not yet
        return AwtUtil.createCursor(GraphicUtil.loadImage(JavaModule.class,
                "db/resources/dbjvinterface.gif"), new Point(8, 8), "box", true); // NOT LOCALIZABLE, not yet
    }

    protected final void createBox(int x, int y) {
        try {
            createClass(JVClassCategory.INTERFACE, LocaleMgr.action.getString("InterfaceCreation"),
                    x, y);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

}
