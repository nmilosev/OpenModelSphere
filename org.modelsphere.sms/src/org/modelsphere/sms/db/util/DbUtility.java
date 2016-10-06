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
package org.modelsphere.sms.db.util;

import java.awt.Rectangle;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBENotation;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.be.db.util.BEUtility;
import org.modelsphere.sms.be.features.UpdateEnvironment;
import org.modelsphere.sms.db.DbSMSClassifier;
import org.modelsphere.sms.db.DbSMSClassifierGo;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORNotation;

/**
 * @author Grandite
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DbUtility {

    //Called by sms.actions.AddAction.doActionPerformed()
    public static DbSMSDiagram selectOneDiagram(DbSMSClassifier classifier) throws DbException {
        ArrayList list = new ArrayList();

        DbRelationN relN = classifier.getClassifierGos();
        DbEnumeration dbEnum = relN.elements(DbSMSClassifierGo.metaClass);
        while (dbEnum.hasMoreElements()) {
            DbSMSClassifierGo go = (DbSMSClassifierGo) dbEnum.nextElement();
            DbSMSDiagram diag = (DbSMSDiagram) go.getCompositeOfType(DbSMSDiagram.metaClass);
            if (!list.contains(diag)) {
                list.add(diag);
            }

        } //end while
        dbEnum.close();

        DbSMSDiagram diagram = (list.size() == 0) ? null : (DbSMSDiagram) list.get(0);
        return diagram;
    } //end selectOneDiagram()

    //Called by sms.actions.AddAction.doActionPerformed()
    public static DbBEDiagram addDiagramElement(DbBEUseCase usecase, DbBEDiagram parentDiagram)
            throws DbException {
        DbObject obj = null;
        DbBENotation notation = null;
        DbSMSProject project = (DbSMSProject) usecase.getProject();
        project.getDb().beginReadTrans();
        String term = usecase.getTerminologyName();
        if (term == null)
            obj = null;
        else if (term.equals(""))
            obj = null;

        if (obj != null)
            obj = project.findComponentByName(DbBENotation.metaClass, term);
        if (obj == null) {
            DbObject dbComp = usecase.getComposite();
            if (dbComp instanceof DbBEModel)
                notation = (DbBENotation) project.findComponentByName(DbBENotation.metaClass,
                        ((DbBEModel) dbComp).getTerminologyName());
            else if (dbComp instanceof DbBEUseCase)
                notation = (DbBENotation) project.findComponentByName(DbBENotation.metaClass,
                        ((DbBEUseCase) dbComp).getTerminologyName());
        } else
            notation = (DbBENotation) obj;

        project.getDb().commitTrans();

        BEUtility util = BEUtility.getSingleInstance();
        DbBENotation childNotation = util.chooseChildNotation(usecase, notation);
        if (childNotation == null)
            return null;

        DbBEDiagram newDiagram = util.createBEDiagram(usecase, childNotation);
        UpdateEnvironment updateEnv = new UpdateEnvironment();
        updateEnv.execute(usecase, parentDiagram, null);

        return newDiagram;
    } //end addDiagramElement()

    //TODO : replace name check by an additional meta field
    private static final String ENTITY_RELATIONSHIP_TXT = org.modelsphere.sms.or.international.LocaleMgr.misc
            .getString("EntityRelationship");

    public static boolean notationIsEntityRelationship(DbORNotation notation) throws DbException {
        String name = notation.getName();
        boolean isER = name.equals(ENTITY_RELATIONSHIP_TXT);
        return isER;
    } //end notationIsEntityRelationship

    public static void onSetRectangle(Rectangle value) {
        // TODO Auto-generated method stub

    }

} //end DbUtility
