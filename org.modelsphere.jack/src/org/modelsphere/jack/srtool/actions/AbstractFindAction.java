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

package org.modelsphere.jack.srtool.actions;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.features.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public abstract class AbstractFindAction extends AbstractApplicationAction {

    public AbstractFindAction(String sAction, ImageIcon iIAction) {
        super(sAction, iIAction);
    }

    public AbstractFindAction(String sAction) {
        this(sAction, null);
    }

    protected static final boolean isValidForFindNext() {
        DbObject[] roots = ApplicationContext.getDefaultMainFrame().getFindSession()
                .getRootObjects();
        if (roots == null) // FindSession not initialized
            return false;
        else {
            if (!roots[0].getDb().isValid())
                return false;
            else
                return true;
        }
    }

    protected final void findNext(int direction) {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        DbFindSession fs = mf.getFindSession();
        DbObject[] roots = fs.getRootObjects();
        if (roots == null) // FindSession not initialized
            return;
        Db db = roots[0].getDb();
        if (!db.isValid())
            return;

        try {
            db.beginTrans(Db.READ_TRANS);
            DbFindSession.FoundObject fo = fs.findNext(direction);
            processFoundObject(fs, db, fo, direction);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(mf, ex);
        }
    }

    // This method takes care of closing the transaction.
    protected final void processFoundObject(DbFindSession fs, Db db, DbFindSession.FoundObject fo,
            int direction) throws DbException {
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        while (fo != null) {
            if (mf.findInExplorer(fo.dbo))
                break;
            fo = fs.findNext(direction);
        }

        if (fo != null) {
            String pattern = LocaleMgr.message.getString("FoundInName0OfObject1");
            String mess = MessageFormat.format(pattern, new Object[] { fo.mf.getGUIName(),
                    fo.dbo.getSemanticalName(DbObject.LONG_FORM) });
            mf.setStatusMessage(mess);
            db.commitTrans();
        } else {
            db.commitTrans();
            String pattern = LocaleMgr.message.getString("StringToto0NotFound");
            String mess = MessageFormat.format(pattern, new Object[] { fs.getFindOption()
                    .getTextToFind() });
            JOptionPane.showMessageDialog(mf, mess);
        }
    }
}
