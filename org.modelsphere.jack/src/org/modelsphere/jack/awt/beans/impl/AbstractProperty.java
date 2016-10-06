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

package org.modelsphere.jack.awt.beans.impl;

import java.io.Serializable;

public abstract class AbstractProperty implements Serializable {

    public abstract String getName();

    public abstract Serializable getValue();

    abstract void setValue(Object val);

    abstract Class getType();

    public static class BooleanProperty extends AbstractProperty {
        private boolean m_value;
        private final String m_name;

        public BooleanProperty(String name, boolean value) {
            m_name = name;
            m_value = value;
        }

        public String getName() {
            return m_name;
        }

        public Serializable getValue() {
            return new Boolean(m_value);
        }

        void setValue(Object val) {
            m_value = ((Boolean) val).booleanValue();
        }

        Class getType() {
            return Boolean.class;
        }
    } // end BooleanProperty

} // end AbstractProperty

