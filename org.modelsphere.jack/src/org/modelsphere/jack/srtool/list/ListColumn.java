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

package org.modelsphere.jack.srtool.list;

import org.modelsphere.jack.baseDb.db.*;

/**
 * 
 * This class allow creation of custom column in the ListTableModel
 * 
 * @see ListDescriptor
 */

public interface ListColumn {
    /**
     * 
     * @return this column title
     */
    public String getTitle();

    /**
     * dbo is the root list's root DbObject neighbor is the row's DbObject
     * 
     * @return the value for the neighbor object for this column
     */
    public Object getValue(DbObject dbo, DbObject neighbor) throws DbException;

    /**
     * 
     * @return the String name of the renderer to display this column. If null, the default renderer
     *         (toString()) will be used.
     */
    public String getRenderer();

    /**
     * 
     * @return the default column width in the table. If < 5, the default width for String type
     *         Columns will be used.
     */
    public int getDefaultWidth();

    /**
     * 
     * @return a unique String ID for this column. This ID is used for column preferences
     *         persistence For example, it may be the JField name for a MetaField. This ID must be
     *         unique within all the list columns.
     */
    public String getID();

}
