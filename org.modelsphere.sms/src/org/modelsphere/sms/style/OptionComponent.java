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

package org.modelsphere.sms.style;

import javax.swing.JComponent;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaField;

public abstract class OptionComponent {

    protected JComponent optionPanel;
    protected StyleComponent StyleComponent;
    protected MetaField[] metaFields;
    protected Object[] values;
    protected boolean[] changes;
    protected DbObject style;

    public OptionComponent(StyleComponent StyleComponent, MetaField[] metaFields) {
        this.StyleComponent = StyleComponent;
        this.metaFields = metaFields;
        values = new Object[metaFields.length];
        changes = new boolean[metaFields.length];
    }

    public final JComponent getOptionPanel() {
        return optionPanel;
    }

    public void setStyle(DbObject style, boolean refresh) throws DbException {
        this.style = style;
        for (int i = 0; i < values.length; i++) {
            if (refresh && changes[i])
                continue;
            values[i] = style.get(metaFields[i]);
            changes[i] = false;
        }
    }

    final void setValue(Object value, int i) {
        if (DbObject.valuesAreEqual(value, values[i]))
            return;
        values[i] = value;
        changes[i] = true;
        if (StyleComponent != null) {
            StyleComponent.setApply(true);
        }
    }

    public final void applyChanges() throws DbException {
        for (int i = 0; i < values.length; i++) {
            if (changes[i])
                style.set(metaFields[i], values[i]);
        }
    }

    public final void resetChanges() {
        for (int i = 0; i < changes.length; i++)
            changes[i] = false;
    }
}
