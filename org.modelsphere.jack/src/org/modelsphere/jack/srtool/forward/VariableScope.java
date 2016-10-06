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

package org.modelsphere.jack.srtool.forward;

import java.io.Serializable;
import java.util.*;

//
// Encapsulates the hashMap used to store variables
// One variable scope per plug-in
//
public class VariableScope implements Serializable {
    HashMap m_hashmap = new HashMap(); // map variable names and variable structs
    String m_scopename;

    public VariableScope(String scopename) {
        m_scopename = scopename;
    }

    public void clear() {
        m_hashmap.clear();
    }

    public Set getKeySet() {
        return m_hashmap.keySet();
    }

    public VariableDecl.VariableStructure getVariable(String varname) {
        return (VariableDecl.VariableStructure) m_hashmap.get(varname);
    }

    public void setVariable(String varname, Serializable value) {
        VariableDecl.VariableStructure variable = (VariableDecl.VariableStructure) m_hashmap
                .remove(varname);
        variable.setValue(value);
        m_hashmap.put(varname, variable);
    }

    public Iterator getIterator() {
        Collection coll = m_hashmap.entrySet();
        ArrayList list = new ArrayList(coll);
        Comparator comparator = new VariableComparator();
        Collections.sort(list, comparator);
        Iterator iter = list.iterator();
        return iter;
    }

    public boolean isDefined(String varname) {
        return m_hashmap.containsKey(varname);
    } // end isDefined

    // use VariableDecl.declare() to declare outside this package
    void declare(String varname, VariableDecl.VariableStructure struct) {
        m_hashmap.put(varname, struct);
    }

    void undeclare(String varname) {
        m_hashmap.remove(varname);
    }

    //
    // INNER CLASS
    //
    private static class VariableComparator implements Comparator {
        VariableComparator() {
        }

        public int compare(Object o1, Object o2) {
            Map.Entry entry1 = (Map.Entry) o1;
            Map.Entry entry2 = (Map.Entry) o2;

            VariableDecl.VariableStructure var1 = (VariableDecl.VariableStructure) entry1
                    .getValue();
            VariableDecl.VariableStructure var2 = (VariableDecl.VariableStructure) entry2
                    .getValue();

            int diff = (var1.getOrder() - var2.getOrder()); // order 10 before
            // order 20
            if (diff == 0) {
                diff = var1.getDisplayName().compareTo(var2.getDisplayName());
            }

            return diff;
        }
    } // end VariableComparator
} // end VariableScope
