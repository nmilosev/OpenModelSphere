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

package org.modelsphere.sms.features;

import org.modelsphere.jack.awt.ExtensionFileFilter;

public class SaveDiagramOptions {

    private Object[] m_semObjs;
    private SaveDiagramDialog m_dialog;
    private ExtensionFileFilter m_filter;

    public SaveDiagramOptions(SaveDiagramDialog dialog, Object[] semObjs, ExtensionFileFilter filter) {
        m_semObjs = semObjs;
        m_dialog = dialog;
        m_filter = filter;
    }

    public SaveDiagramDialog getDialog() {
        return m_dialog;
    }

    public Object[] getObjects() {
        return m_semObjs;
    }

    public void setObjects(Object[] objects) {
        m_semObjs = objects;
    };
    
    public ExtensionFileFilter getFilter() {
    	return m_filter;
    }
}
