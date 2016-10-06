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

import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.Connector;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORIndex;

/**
 * For an index, lists all the columns defined on it. <br>
 * Target System : <b>All Except Java</b><br>
 * Type : <b>Connector</b><br>
 */
public final class IndexColumns extends Connector {

    /*
     * In a .tpl file: list_PK_fromTable CONN TablePrimaryCombination, CHILD = pkTag;
     * 
     * "pkTag" is the childRule
     */
    public IndexColumns() {
    } //Parameter-less constructor required by jack.io.Plugins

    public IndexColumns(String childRule) {
        super(null, DbORIndex.fComponents, childRule, new String[] { null }, DbORColumn.metaClass,
                new Modifier[] {});
    }

    //////////////////
    //OVERRIDES Plugin
    private static final PluginSignature g_signature = new IndexColumnsSignature();

    public PluginSignature getSignature() {
        return g_signature;
    }

    //
    //////////////////

    private static class IndexColumnsSignature extends PluginSignature {
        private static final String NAME = "IndexColumns"; // NOT LOCALIZABLE
        private static final String REVISION_NUMBER_STR = "$Revision: 4 $"; // NOT LOCALIZABLE
        private static final String AUTHOR = ApplicationContext.APPLICATION_AUTHOR;
        private static final String DATE_STR = "$Date: 2009/04/14 14:00p $"; // NOT LOCALIZABLE
        private static final int BUILD_REQUIRED = 212;

        public IndexColumnsSignature() {
            super(NAME, REVISION_NUMBER_STR, AUTHOR, DATE_STR, BUILD_REQUIRED);
        }
    }
} //end of IndexColumns
