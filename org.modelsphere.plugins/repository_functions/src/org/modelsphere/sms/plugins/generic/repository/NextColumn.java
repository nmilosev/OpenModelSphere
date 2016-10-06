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

//Java
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.sms.or.db.DbORColumn;
import org.modelsphere.sms.or.db.DbORTable;

public class NextColumn extends Connector.UserConnector {

    private PluginSignature m_signature = null;

    public PluginSignature getSignature() {
        if (m_signature == null) {
            m_signature = new PluginSignature("INFProcedureReturntypes", "$Revision: 4 $",
                    ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 212); // NOT LOCALIZABLE
        }

        return m_signature;
    }

    //Navigate within all its components
    private static final String COMPONENTS = DbORTable.fComponents.getJName();

    private static final String PROJECT_NOT_FOUND_PATTRN = "Object {0} not under any project."; // NOT LOCALIZABLE

    // we use fComponents to force the call of expandMetaRelationN()
    // but we do not really use the components of a column (there isnt anyway)
    public Connector createInstance(String childRule, String oneChildRule) {
        Connector conn = new AdaptedConnector(DbORColumn.fComponents, childRule,
                new String[] { oneChildRule }, DbORColumn.metaClass, new Modifier[] {});
        return conn;
    }

    public NextColumn() {
    } //Parameter-less constructor required by jack.io.Plugins

    //
    // INNER CLASS
    //
    private static final class AdaptedConnector extends Connector {

        //constructor
        public AdaptedConnector(MetaRelationship aConnector, String childRule,
                String[] optionalRules, MetaClass childrenMetaClass, Modifier[] someModifiers) {
            super(null, aConnector, childRule, optionalRules, childrenMetaClass, someModifiers);
        } //end AdaptedConnector

        //overrides Connector.expandMetaRelationN
        protected boolean expandMetaRelationN(Writer output, Serializable object,
                MetaRelationship metaRelation, boolean state[], MetaClass childrenMetaClass)
                throws IOException, RuleException {

            boolean expanded = false;
            DbEnumeration dbEnumChildren = null;
            DbORColumn column = (DbORColumn) object;

            try {
                DbObject table = column.getComposite();
                DbRelationN relationN = (DbRelationN) table.getComponents();
                if (childrenMetaClass == null) {
                    dbEnumChildren = relationN.elements();
                } else {
                    dbEnumChildren = relationN.elements(childrenMetaClass);
                } //end if

                int nbChildren = table.getNbNeighbors(DbObject.fComponents);
                while (dbEnumChildren.hasMoreElements()) {
                    DbObject child = (DbObject) dbEnumChildren.nextElement();

                    if ((child == column) && (dbEnumChildren.hasMoreElements())) {
                        DbObject enumChild = (DbObject) dbEnumChildren.nextElement();
                        Rule.RuleOptions options = null;
                        expanded |= expandChild(output, column, enumChild, nbChildren, state,
                                options);
                    }
                } // end while

                boolean atLeastOneChildPrinted = state[1];

                Rule.RuleOptions options = null;
                if ((atLeastOneChildPrinted) && (suffixModifier != null)) {
                    expanded |= suffixModifier.expand(output, object, options);
                } else if ((!atLeastOneChildPrinted) && (nullModifier != null)) {
                    expanded |= nullModifier.expand(output, object, options);
                }

            } catch (DbException ex) {
                throw new RuleException(ex.getMessage());
            } //end try

            return expanded;
        } //end expandMetaRelationN
    } //end AdaptedConnector
} //end of NextColumn
