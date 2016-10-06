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

package org.modelsphere.sms.plugins.generic.repository;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.DiagramInternalFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORTableGo;

/**
 * For a graphical table, gets its XY coordonates. <br>
 * Target System : <b>All Except Java</b><br>
 * Type : <b>User Function</b><br>
 * Returns : a string of the form ''x1,y1,x2,y2'', where :<br>
 * - x1 and y1 represent the graphical table's left upper corner.<br>
 * - x2 and y2 represent the graphical table's right lower corner.<br>
 */
public final class TableCoordinates extends UserDefinedField {
    private static final PluginSignature signature = new PluginSignature("TableCoordinates",
            "$Revision: 4 $", ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $",
            212); // NOT LOCALIZABLE

    public TableCoordinates() {
    } //Parameter-less constructor required by jack.io.Plugins

    public TableCoordinates(String ruleName, String subrule, Modifier[] modifiers)
            throws RuleException {
        super(ruleName, subrule, modifiers);
    }

    public PluginSignature getSignature() {
        return signature;
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        try {
            DbORTableGo tableGo = (DbORTableGo) object;
            DbORDiagram diagram = (DbORDiagram) tableGo.getComposite();;
            GraphicComponent gc;
            Rectangle rect;
            Point origin;

            DefaultMainFrame frame = null;
            try {
                Class claz = Class.forName("org.modelsphere.sms.MainFrame"); //NOT LOCALIZABLE
                java.lang.reflect.Method method = claz.getDeclaredMethod("getSingleton",
                        new Class[] {}); //NOT LOCALIZABLE
                frame = (DefaultMainFrame) method.invoke(null, new Object[] {});
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
            }
            DiagramInternalFrame diagramInternalFrame = frame.getDiagramInternalFrame(diagram);
            ApplicationDiagram appDiagram;
            boolean deleteApplicationDiagram = false;

            if (diagramInternalFrame == null) {
                DbSemanticalObject so = (DbSemanticalObject) diagram.getComposite();
                SMSToolkit kit = SMSToolkit.getToolkit(so);
                appDiagram = new ApplicationDiagram(so, diagram, kit
                        .createGraphicalComponentFactory(), frame.getDiagramsToolGroup());
                deleteApplicationDiagram = true;
            } else {
                appDiagram = diagramInternalFrame.getDiagram();
            }

            gc = (GraphicComponent) tableGo.getGraphicPeer();
            rect = (Rectangle) gc.getRectangle().clone();
            origin = new Point(GraphicComponent.LINE_BOLD_WIDTH - appDiagram.getContentRect().x,
                    GraphicComponent.LINE_BOLD_WIDTH - appDiagram.getContentRect().y);

            if (deleteApplicationDiagram) {
                appDiagram.delete();
            }

            rect.translate(origin.x, origin.y);

            if (prefixModifier != null) {
                prefixModifier.expand(output, object, options);
            }

            //write the converted text
            output.write(rect.x + "," + rect.y + "," + (rect.x + rect.width) + ","
                    + (rect.y + rect.height));
            expanded = true;

            if (suffixModifier != null) {
                suffixModifier.expand(output, object, options);
            }

        } catch (DbException ex) {
            throw new RuleException(ex.getMessage());
        }

        return expanded;
    }
} //end of ObjectID
