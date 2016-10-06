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

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.GraphicNode;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.oo.db.DbOOAssociationGo;
import org.modelsphere.sms.oo.db.srtypes.OOAggregation;
import org.modelsphere.sms.oo.java.db.DbJVAssociation;
import org.modelsphere.sms.oo.java.db.DbJVClass;
import org.modelsphere.sms.oo.java.db.DbJVDataMember;
import org.modelsphere.sms.oo.java.graphic.AdtBox;
import org.modelsphere.sms.oo.java.international.LocaleMgr;

public abstract class JVLineTool extends LineTool {

    public JVLineTool(String text, String[] tooltips, Image image, Image[] secondaryImages) {
        super(0, text, tooltips, image, secondaryImages);
    }

    public JVLineTool(String text, String[] tooltips, Image image, Image[] secondaryImages,
            int defaultIndex) {
        super(0, text, tooltips, image, secondaryImages, defaultIndex);
    }

    protected final boolean isSourceAcceptable(GraphicNode source) {
        boolean acceptable = (source != null) && (source instanceof AdtBox);
        return acceptable;
    }

    protected boolean isDestAcceptable(GraphicNode source, GraphicNode dest) {
        boolean acceptable = (dest != null) && (dest instanceof AdtBox);
        return acceptable;
    }

    protected final void createAssociation(AdtBox box1, AdtBox box2, Polygon poly,
            OOAggregation aggreg) throws DbException {
        DbSMSDiagram diagGO = (DbSMSDiagram) ((ApplicationDiagram) model).getDiagramGO();
        diagGO.getDb()
                .beginTrans(Db.WRITE_TRANS, LocaleMgr.action.getString("AssociationCreation"));
        DbJVClass frontAdt = (DbJVClass) box1.getAdtSO();
        DbJVClass backAdt = (DbJVClass) box2.getAdtSO();
        DbJVDataMember frontMember = new DbJVDataMember(frontAdt);
        DbJVDataMember backMember = new DbJVDataMember(backAdt);
        SMSMultiplicity frontMult = SMSMultiplicity.getInstance(SMSMultiplicity.MANY);
        SMSMultiplicity backMult = SMSMultiplicity.getInstance(SMSMultiplicity.OPTIONAL);
        frontMember.setRoleDefaultInitialValues(backMember, frontMult);
        backMember.setRoleDefaultInitialValues(frontMember, backMult);
        DbJVAssociation assSO = new DbJVAssociation(frontMember, frontMult, backMember, backMult);
        frontMember.getAssociationEnd().setAggregation(aggreg);
        assSO.setName("");

        DbOOAssociationGo assGO = new DbOOAssociationGo(diagGO, box1.getAdtGO(), box2.getAdtGO(),
                assSO);
        assGO.setPolyline(poly);
        assGO.setRightAngle(rightAngle ? Boolean.TRUE : Boolean.FALSE);
        diagGO.getDb().commitTrans();
    }

}
