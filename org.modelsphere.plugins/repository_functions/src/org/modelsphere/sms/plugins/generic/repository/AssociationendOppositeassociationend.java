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
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.oo.java.db.DbJVAssociationEnd;
import org.modelsphere.sms.or.db.DbORAssociationEnd;

/**
 * For an association end, gets its opposite association end. <br>
 * Target System : <b>Java and Relational</b><br>
 * Type : <b>Connector</b><br>
 */

public final class AssociationendOppositeassociationend extends UserDefinedField {

    public AssociationendOppositeassociationend() {
    } //Parameter-less constructor required by jack.io.Plugins

    public AssociationendOppositeassociationend(String ruleName, String subRule,
            Modifier[] modifiers) throws RuleException {
        super(ruleName, subRule, modifiers);
    }

    public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;
        if (object instanceof DbJVAssociationEnd) {
            DbJVAssociationEnd assocEnd = (DbJVAssociationEnd) object;
            try {
                DbJVAssociationEnd opposEnd = (DbJVAssociationEnd) assocEnd.getOppositeEnd();
                Rule subRule = getSubRule();
                expanded = subRule.expand(output, opposEnd, options);
            } catch (DbException ex) {
                //let expanded at false
            } //end try
        } else if (object instanceof DbORAssociationEnd) {
            DbORAssociationEnd assocEnd = (DbORAssociationEnd) object;
            try {
                DbORAssociationEnd opposEnd = assocEnd.getOppositeEnd();
                Rule subRule = getSubRule();
                expanded = subRule.expand(output, opposEnd, options);
            } catch (DbException ex) {
                //let expanded at false
            } //end try

        } //end if

        return expanded;
    } //end expand()

    //////////////////
    //OVERRIDES Plugin
    private static final PluginSignature g_signature = new AssociationendOppositeassociationendSignature();

    public PluginSignature getSignature() {
        return g_signature;
    }

    //
    //////////////////

    private static class AssociationendOppositeassociationendSignature extends PluginSignature {
        private static final String NAME = "AssociationendOppositeassociationend"; // NOT LOCALIZABLE
        private static final String REVISION_NUMBER_STR = "$Revision: 4 $"; // NOT LOCALIZABLE
        private static final String AUTHOR = ApplicationContext.APPLICATION_AUTHOR;
        private static final String DATE_STR = "$Date: 2009/04/14 14:00p $"; // NOT LOCALIZABLE
        private static final int BUILD_REQUIRED = 212;

        public AssociationendOppositeassociationendSignature() {
            super(NAME, REVISION_NUMBER_STR, AUTHOR, DATE_STR, BUILD_REQUIRED);
        }
    }
} //end of CompilationunitPackage
