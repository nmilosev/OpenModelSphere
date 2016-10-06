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
package org.modelsphere.sms.or;

import java.awt.Image;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.graphic.tool.SMSGraphicalLinkTool;
import org.modelsphere.sms.graphic.tool.SMSNoticeTool;
import org.modelsphere.sms.or.db.DbORDomain;
import org.modelsphere.sms.or.db.DbORDomainModel;
import org.modelsphere.sms.or.graphic.tool.ORDomainTool;

public final class ORDomainModelToolkit extends SMSToolkit {
    private static final String kGraphicalLink = SMSGraphicalLinkTool.GRAPHICAL_LINKS;
    private static final Image kImageAngularGraphicalLinkTool = GraphicUtil.loadImage(Tool.class,
            "resources/angulardependency.gif"); // NOT
    // LOCALIZABLE
    // -
    // tool
    // image
    private static final Image kImageRightAngleGraphicalLinkTool = GraphicUtil.loadImage(
            Tool.class, "resources/rightangledependency.gif"); // NOT

    // LOCALIZABLE
    // -
    // tool
    // image

    public GraphicComponentFactory createGraphicalComponentFactory() {
        return new ORGraphicComponentFactory();
    }

    protected MetaClass[] getSupportedPackage() {
        return new MetaClass[] { DbORDomainModel.metaClass };
    }

    public DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException {
        if (composite != null) {
            if (abstractMetaClass.equals(DbORDomain.metaClass))
                return composite.createComponent(DbORDomain.metaClass);
        }
        return null;
    }

    public DbObject createDbObject(DbObject baseobject, DbObject composite, Object[] parameters)
            throws DbException {
        return null;
    }

    public boolean isMetaClassSupported(MetaClass metaClass) {
        return false;
    }

    public Tool[] createTools() {
        return new Tool[] {
                new ORDomainTool(ORDomainTool.kDomainCreationTool,
                        ORDomainTool.kImageDomainCreationTool),
                new SMSNoticeTool(),
                new SMSGraphicalLinkTool(kGraphicalLink, new String[] { kGraphicalLink,
                        kGraphicalLink }, kImageAngularGraphicalLinkTool, new Image[] {
                        kImageRightAngleGraphicalLinkTool, kImageAngularGraphicalLinkTool }), };
    }
}
