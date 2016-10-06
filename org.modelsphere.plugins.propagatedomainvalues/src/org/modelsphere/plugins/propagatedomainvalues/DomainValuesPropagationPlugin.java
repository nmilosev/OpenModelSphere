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

package org.modelsphere.plugins.propagatedomainvalues;

import java.awt.event.ActionEvent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginServices;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.actions.PluginDefaultAction;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORDomain;

public final class DomainValuesPropagationPlugin implements Plugin2 {
    public PluginSignature getSignature() {
        return null;
    }

    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public Class[] getSupportedClasses() {
        return new Class[] { DbORDomain.class };
    }

    public void execute(ActionEvent actEvent) throws Exception {
        PluginServices.multiDbBeginTrans(Db.WRITE_TRANS, LocaleMgr.misc
                .getString("UpdateFromDomainValue"));
        DbObject[] domains = PluginServices.getSelectedSemanticalObjects();
        for (int i = 0; i < domains.length; i++) {
            if (domains[i] instanceof DbORDomain) {
                DbORDomain domain = (DbORDomain) domains[i];
                DbEnumeration columns = domain.getTypedAttributes().elements(DbORColumn.metaClass);
                while (columns.hasMoreElements()) {
                    DbORColumn column = (DbORColumn) columns.nextElement();
                    column.setLength(domain.getLength());
                    column.setNbDecimal(domain.getNbDecimal());
                    column.setNull(Boolean.valueOf(domain.isNull()));
                    column.setReference(Boolean.valueOf(domain.isReference()));
                }
                columns.close();
            }
        }
        PluginServices.multiDbCommitTrans();
    }
    
    public boolean doListenSelection() { return false; }

	@Override
	public OptionGroup getOptionGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		return new PluginDefaultAction(this);
	}
}
