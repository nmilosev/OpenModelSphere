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

package org.modelsphere.jack.baseDb.db.xml.imports;

/**
 * Title:        Sms
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      Grandite
 * @author
 * @version 1.0
 */

/**
 * This class allows to open a XML-formatted output file
 * This is the conly class in this package that have a public visibility, and
 * onlt its doOpenFile() method is accessible to the outside.
 *
 * When a XML node is encountered, searches for its id attribute. If such an
 * attribute exists, it's a concept node; if not, it's a value node.
 * Fills the element map while the XML file is parsed.
 *
 */

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.srtool.DefaultMainFrame;

public class XmlFileOpener {
    private static final String TRANSACTION_NAME = "XML Import"; // (not

    // localizable)
    // debug
    // only

    // //////////////////////////////////////////
    // Implements the 'singleton' design pattern
    //
    public static XmlFileOpener getSingleton() {
        if (g_singleInstance == null)
            g_singleInstance = new XmlFileOpener();
        return g_singleInstance;
    }

    private static XmlFileOpener g_singleInstance = null;

    private XmlFileOpener() {
    }

    //
    // /////////////////////////////////////////

    /**
     * Opens the XML-formatted file named 'filename', parses it and fills the element map. Then
     * creates all the objects in the database provided in parameter, if not null.
     */
    public void doOpenFile(Db db, /* Project project, */String filename, DefaultMainFrame mainFrame) {
        XmlImportWorker worker = new XmlImportWorker(db, filename, mainFrame, TRANSACTION_NAME);
        DefaultController controller = new DefaultController(TRANSACTION_NAME, true, null);
        controller.start(worker);
    } // end doOpenFile

} // end XmlOpenFile
