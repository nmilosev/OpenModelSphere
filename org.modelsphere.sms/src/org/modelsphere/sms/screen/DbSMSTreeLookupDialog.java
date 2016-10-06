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

/*
 * Created on Jan 16, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.sms.screen;

import java.awt.Component;

import javax.swing.tree.DefaultTreeModel;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.screen.DbTreeLookupDialog;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModel;
import org.modelsphere.jack.baseDb.screen.model.DbTreeModelListener;

/**
 * @author Nicolask
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class DbSMSTreeLookupDialog extends DbTreeLookupDialog {

    public DbSMSTreeLookupDialog(Component comp, String title, DbTreeModel model,
            boolean multipleSelection, boolean isUML) {
        super(comp, title, model, multipleSelection);
    }

    public DbSMSTreeLookupDialog(Component comp, String title, DbUMLTreeModel model,
            boolean multipleSelection, boolean isUML) {
        super(comp, title, model, multipleSelection);
    }

    // If nullStr specified, the method returns nullStr if the user selects the
    // nullNode.

    public Object selectOneUML(Component comp, String title, DbObject[] roots,
            MetaClass[] metaClasses, DbTreeModelListener listener, String nullStr, DbObject selDbo,
            boolean isUML) {
        try {
            DbUMLTreeModel model = new DbUMLTreeModel(roots, metaClasses, listener, nullStr, isUML);
            DbSMSTreeLookupDialog ld = new DbSMSTreeLookupDialog(comp, title, model, false, isUML);
            ld.find(selDbo);
            ld.setVisible(true);
            if (ld.getUserAction() == DbTreeLookupDialog.SELECT) {
                DbObject[] selObjs = ld.getSelectedObjects();
                if (selObjs.length > 0)
                    return (selObjs[0] == null ? (Object) nullStr : (Object) selObjs[0]);
            }
            return null;
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(comp, ex);
            return null;
        }
    }
}
