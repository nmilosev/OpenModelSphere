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

package org.modelsphere.sms.plugins.java.genmeta;

import java.io.File;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.baseDb.db.DbObject;

final class DbForwardOptions implements ForwardOptions {
    private JackForwardEngineeringPlugin m_forward = null;
    private DbObject[] m_objects = null;
    private File m_actualDirectory = null;

    DbForwardOptions(JackForwardEngineeringPlugin forward, DbObject[] objects) {
        m_forward = forward;
        m_objects = objects;
    }

    public void setObjects(DbObject[] objects) {
        m_objects = objects;
    }

    public DbObject[] getObjects() {
        return m_objects;
    }

    public JackForwardEngineeringPlugin getForward() {
        return m_forward;
    }

    public void setActualDirectory(File actualDirectory) {
        m_actualDirectory = actualDirectory;
    }

    File getActualDirectory() {
        return m_actualDirectory;
    }
}
