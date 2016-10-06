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

package org.modelsphere.sms.plugins.report.model;

public class Property {

    //private ? m_composite;
    private String m_label;
    private String m_key;
    private Object m_value;
    private Object m_mapping;

    public Property(/* ? composite, */String label, String key, Object initialValue) {
        this(label, key, initialValue, null);
    }

    public Property(/* ? composite, */String label, String key, Object initialValue, Object mapping) {
        //m_composite = composite;
        m_label = label;
        m_key = key;
        m_value = initialValue;
        m_mapping = mapping;
    }

    public String getKey() {
        return m_key;
    }

    public Object getValue() {
        return m_value;
    }

    public Object getMappingObject() {
        return m_mapping;
    }

    public void setValue(Object newValue) {
        m_value = newValue;
    }

    public String toString() {
        return m_label;
    }
}
