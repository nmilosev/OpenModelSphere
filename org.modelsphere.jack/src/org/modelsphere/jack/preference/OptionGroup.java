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

package org.modelsphere.jack.preference;

import java.util.ArrayList;

public class OptionGroup {
    private ArrayList optionGroups = new ArrayList(5);

    private String title = "";

    public OptionGroup(String title) {
        if (title == null)
            throw new NullPointerException("Null title in OptionGroup not allowed"); // NOT LOCALIZABLE
        this.title = title;
    }

    public final void addOptionGroup(OptionGroup group) {
        if (group != null && optionGroups.indexOf(group) == -1)
            optionGroups.add(group);
    }

    protected final String getTitle() {
        return title;
    }

    protected OptionPanel createOptionPanel() {
        return null;
    }

    public final OptionGroup[] getOptionGroups() {
        Object[] temp = optionGroups.toArray();
        OptionGroup[] groups = new OptionGroup[temp.length];
        for (int i = 0; i < temp.length; i++) {
            groups[i] = (OptionGroup) temp[i];
        }
        return groups;
    }

    public final String toString() {
        return title;
    }

}
