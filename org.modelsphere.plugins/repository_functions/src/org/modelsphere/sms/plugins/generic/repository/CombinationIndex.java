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

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.Connector;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.sms.or.db.*;

public final class CombinationIndex extends Connector.UserConnector {

    /**
     * For a primary, a unique or a foreign key, lists all the indexes defined on it. <br>
     * Target System : <b>All Except Java</b><br>
     * Type : <b>Connector</b><br>
     */
    public CombinationIndex() {
    } //Parameter-less constructor required by jack.io.Plugins

    public Connector createInstance(String childRule, String oneChildRule) {
        Connector conn = new Connector(null, DbORConstraint.fComponents, childRule,
                new String[] { oneChildRule }, DbORIndex.metaClass, new Modifier[] {});
        return conn;
    }

    public boolean expand(Writer output, Serializable object) throws IOException, RuleException {
        boolean expanded = false;

        try {
            DbObject index = null;
            if (object instanceof DbORPrimaryUnique) {
                DbORPrimaryUnique pu = (DbORPrimaryUnique) object;
                index = pu.getIndex();
                //expand it
                expanded = super.expand(output, index);
            } else if (object instanceof DbORForeign) {
                DbORForeign foreign = (DbORForeign) object;
                index = foreign.getIndex();
                //expand it
                expanded = super.expand(output, index);
            } //end if
        } catch (DbException ex) {
            throw new RuleException(ex.toString());
        }

        return expanded;
    }

    //////////////////
    //OVERRIDES Plugin
    private static final PluginSignature g_signature = new CombinationIndexSignature();

    public PluginSignature getSignature() {
        return g_signature;
    }

    //
    //////////////////

    private static class CombinationIndexSignature extends PluginSignature {
        private static final String NAME = "CombinationIndex"; // NOT LOCALIZABLE
        private static final String REVISION_NUMBER_STR = "$Revision: 4 $"; // NOT LOCALIZABLE
        private static final String AUTHOR = ApplicationContext.APPLICATION_AUTHOR;
        private static final String DATE_STR = "$Date: 2009/04/14 14:00p $"; // NOT LOCALIZABLE
        private static final int BUILD_REQUIRED = 212;

        public CombinationIndexSignature() {
            super(NAME, REVISION_NUMBER_STR, AUTHOR, DATE_STR, BUILD_REQUIRED);
        }
    }
} //end of CombinationIndex
