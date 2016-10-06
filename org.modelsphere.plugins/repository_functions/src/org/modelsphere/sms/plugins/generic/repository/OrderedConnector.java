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
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.DefaultComparableElement;

/**
 * For any object, lists by alphabetical order. <br>
 * Target System : <b>All</b><br>
 * Type : <b>Connector</b><br>
 * Note : <i>For internal uses only.</i>
 */
public abstract class OrderedConnector extends Connector.UserConnector {

    public OrderedConnector() {
    } //Parameter-less constructor required by jack.io.Plugins

    protected Connector createInstance(MetaRelationship aConnector, String childRule,
            String oneChildRule, MetaClass childrenMetaClass) {
        Connector conn = new AdaptedConnector(aConnector, childRule, new String[] { oneChildRule },
                childrenMetaClass, new Modifier[] {});
        return conn;
    }

    private PluginSignature m_signature = null;

    public PluginSignature getSignature() {
        if (m_signature == null) {
            m_signature = new PluginSignature("OrderedConnector", "$Revision: 4 $",
                    ApplicationContext.APPLICATION_AUTHOR, "$Date: 2009/04/14 14:00p $", 212); // NOT LOCALIZABLE
        }

        return m_signature;
    }

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
        //add childrenMetaClass to filter only children of that class
        //  if null, scan each child
        protected boolean expandMetaRelationN(Writer output, DbObject object,
                MetaRelationship metaRelation, boolean state[], MetaClass childrenMetaClass)
                throws DbException, IOException, RuleException {

            boolean expanded = false;
            DbEnumeration dbEnumChildren = null;
            DbRelationN relationN = (DbRelationN) object.get(metaRelation);
            if (childrenMetaClass == null) {
                dbEnumChildren = relationN.elements();
            } else {
                dbEnumChildren = relationN.elements(childrenMetaClass);
            }

            int size = 0;
            ArrayList elems = new ArrayList();

            int i = 0;
            try {
                while (dbEnumChildren.hasMoreElements()) {
                    DbObject child = (DbObject) dbEnumChildren.nextElement();
                    //elems[i++]     = new DefaultComparableElement(child, child.getName());
                    elems.add(new DefaultComparableElement(child, child.getName()));
                } // end while
            } finally {
                dbEnumChildren.close();
            }

            DefaultComparableElement[] sortedArray = new DefaultComparableElement[elems.size()];
            System.arraycopy(elems.toArray(), 0, sortedArray, 0, sortedArray.length);
            Arrays.sort(sortedArray, new CollationComparator());
            int nb = sortedArray.length;
            Rule.RuleOptions options = null;
            for (i = 0; i < nb; i++) {
                expanded |= expandChild(output, object, (DbObject) sortedArray[i].object, nb,
                        state, options);
            } //end for

            boolean atLeastOneChildPrinted = state[1];

            if ((atLeastOneChildPrinted) && (suffixModifier != null)) {
                expanded |= suffixModifier.expand(output, object, options);
            } else if ((!atLeastOneChildPrinted) && (nullModifier != null)) {
                expanded |= nullModifier.expand(output, object, options);
            }

            return expanded;
        } //end expandMetaRelationN
    } //end AdaptedConnector
} //end of OrderedConnector
