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

import java.awt.Image;
import java.awt.Polygon;

import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public class JVAssociationTool extends JVLineTool {
    public static final String kAssociationCreationTool = LocaleMgr.screen
            .getString("AssociationCreationTool");
    public static final Image kImageAssociationCreationTool = GraphicUtil.loadImage(
            LocaleMgr.class, "resources/angularline.gif"); // NOT LOCALIZABLE - tool image
    public static final Image kImageRightAngleAssociationCreationTool = GraphicUtil.loadImage(
            LocaleMgr.class, "resources/rightangleline.gif"); // NOT LOCALIZABLE - tool image

    public JVAssociationTool(String text, String[] tooltips, Image image, Image[] secondaryImages) {
        super(text, tooltips, image, secondaryImages, 0);
    }

    protected final void createLine(GraphicNode source, GraphicNode dest, Polygon poly) {
        try {
            createAssociation((AdtBox) source, (AdtBox) dest, poly, OOAggregation
                    .getInstance(OOAggregation.NONE));
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(ApplicationContext
                    .getDefaultMainFrame(), ex);
        }
    }

}
