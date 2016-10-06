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

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

final class CellViewerMapping {
    private HashMap m_mapping = new HashMap();

    // not directly instantiable
    private CellViewerMapping() {
        m_mapping.put(Boolean.class, BooleanViewer.class);
        m_mapping.put(File.class, FileViewer.class);
        m_mapping.put(Integer.class, IntegerViewer.class);
        m_mapping.put(String.class, StringViewer.class);
        m_mapping.put(Image.class, ImageViewer.class);
    }

    private static CellViewerMapping g_singleInstance = null;

    static CellViewerMapping getSingleton() {
        if (g_singleInstance == null) {
            g_singleInstance = new CellViewerMapping();
        }
        return g_singleInstance;
    }

    // called by BeanDialogImpl
    TableCellViewer getCellViewer(Class type) {
        TableCellViewer cellViewer;

        Class claz = (Class) m_mapping.get(type);
        if (claz == null) {
            claz = (Class) m_mapping.get(String.class); // default viewer
        }

        try {
            AbstractViewer viewer = (AbstractViewer) claz.newInstance();
            TableCellRenderer renderer = viewer.getTableCellRenderer();
            TableCellEditor editor = viewer.getTableCellEditor();
            cellViewer = new TableCellViewer(renderer, editor);
        } catch (IllegalAccessException ex) {
            cellViewer = null;
        } catch (InstantiationException ex) {
            cellViewer = null;
        }

        return cellViewer;
    } // get getCellViewer()
} // endCellViewerMapping
