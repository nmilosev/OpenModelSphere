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
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.HashMap;

import org.modelsphere.jack.gui.task.Controller;

/*
 * Options to parameterize the reverse engineering
 * a parameter of Reverse.reverseFile()
 */
public final class ReverseFileOptions {

    private InputStream m_input = null;
    private String m_fileName = null;
    private long m_fileLength = 0;
    private Vector m_objectsToDeleteVector;
    private HashMap m_diagMap;
    private Controller m_controller;
    private long m_partialSize;
    private long m_totalSize;

    public ReverseFileOptions(Vector objectsToDeleteVector, HashMap diagMap, Controller controller,
            long partialSize, long totalSize) {
        m_objectsToDeleteVector = objectsToDeleteVector;
        m_diagMap = diagMap;
        m_controller = controller;
        m_partialSize = partialSize;
        m_totalSize = totalSize;
    } // ReverseFileOptions()

    public void setInput(InputStream input, String fileName, long fileLength) {
        m_input = input;
        m_fileName = fileName;
        m_fileLength = fileLength;
    }

    public void setInput(File file) throws FileNotFoundException {
        m_input = new FileInputStream(file);
        m_fileName = file.toString();
        m_fileLength = file.length();
    }

    public void closeInput() throws IOException {
        if (m_input != null) {
            m_input.close();
            m_input = null;
        }
    }

    public InputStream getInput() {
        return m_input;
    }

    public long getFileLength() {
        return m_fileLength;
    }

    public String getFileName() {
        return m_fileName;
    }

    public Vector getObjectsToDeleteVector() {
        return m_objectsToDeleteVector;
    }

    public HashMap getDiagMap() {
        return m_diagMap;
    }

    public Controller getController() {
        return m_controller;
    }

    public long getPartialSize() {
        return m_partialSize;
    }

    public long getTotalSize() {
        return m_totalSize;
    }

    // for reversing a node-structured file, such as XML, keep the total number
    // of nodes (computed just before to read the whole file) and the current
    // number of nodes (to know which percent of the file has been reverse
    // engineered).

    private long m_totalNodes = 0;

    public void setTotalNbNodes(long totalNodes) {
        m_totalNodes = totalNodes;
    } // caller : XmlReader.startXmlReader()

    public long getTotalNbNodes() {
        return m_totalNodes;
    } // caller :

    private long m_currentNodes = 0;

    public void incrementCurrentNbNodes() {
        m_currentNodes++;
    } // caller : NodeReader.printNodeList()

    public long getCurrentNbNodes() {
        return m_currentNodes;
    } // caller :
} // end ReverseFileOptions

