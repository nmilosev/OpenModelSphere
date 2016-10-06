/*************************************************************************

Copyright (C) 2012 Grandite

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

package org.modelsphere.jack.srtool.features.layout;

import java.util.HashMap;

/**
 * The Class LayoutAttribute.
 * 
 * @param <T>
 *            the generic type
 */
public class LayoutAttribute<T> {

    /** The key. */
    private String key;

    /** The default value. */
    private T defaultValue;

    /**
     * Instantiates a new layout attribute.
     * 
     * @param key
     *            the key
     * @param defaultValue
     *            the default value
     */
    public LayoutAttribute(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     * 
     * @return the default value
     */
    public T getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the key.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value.
     * 
     * @param attributes
     *            the attributes
     * @return the value
     */
    @SuppressWarnings("unchecked")
    public T getValue(HashMap<LayoutAttribute<?>, Object> attributes) {
        T value = null;
        try {
            value = (T) attributes.get(this);
        } catch (Exception e) {
        }
        return value == null ? defaultValue : value;
    }

    /**
     * Sets the value.
     * 
     * @param attributes
     *            the attributes
     * @param value
     *            the value
     */
    public void setValue(HashMap<LayoutAttribute<?>, Object> attributes, T value) {
        attributes.put(this, value);
    }
}
