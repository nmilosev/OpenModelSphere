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
package org.modelsphere.jack.srtool.reverse.file;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Vector;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.gui.task.Controller;

public abstract class AbstractReverser {
    protected Controller m_controller;
    protected long m_fileLength;
    protected long m_totalSize;

    public AbstractReverser() {
    }

    // When a file is large (ex: a .jar file), update the progresion while the
    // big file is being reversed
    public void setPartialProgression(Controller controller, long fileLength, long totalSize) {
        m_controller = controller;
        m_fileLength = fileLength;
        m_totalSize = totalSize;
    } // end setPartialProgression()

    public abstract String reverseCurrentFile(DbObject reverseEngineeredPackage,
            JackReverseEngineeringPlugin reverse, Vector obsoleteObjectVector, HashMap diagMap,
            File file, DbProject project, StringWriter logBuffer) throws Exception;

    public abstract void preparePackageForDisplay(DbObject pack) throws Exception;

    public abstract void clearObjects(Vector obsoleteObjectVector, DbObject dboDestination)
            throws DbException;
}
