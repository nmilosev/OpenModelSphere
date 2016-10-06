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

import java.awt.*;
import java.io.*;
import javax.swing.*;

public final class SourceChooser extends JFileChooser {
    // to know how the approve have be execute
    public static final int DOUBLECLICKAPPROVE = 1;
    public static final int BUTTONAPPROVE = 2;

    private int approveMethod = DOUBLECLICKAPPROVE;

    /*
     * CONSTRUCTORS
     */

    public SourceChooser() {
        super();
    }

    public SourceChooser(String path) {
        super(path);
    }

    public SourceChooser(File directory) {
        super(directory);
    }

    /*
     * METHODS
     */

    public String getFileName() {
        String filename = null;

        File file = getSelectedFile();
        if (file != null) {
            String s = file.toString();
            filename = s.substring(0, s.indexOf(' '));
        }

        return filename;
    }

    /**
     * Pops up a "select source File" file chooser dialog.
     * 
     * @return the return state of the filechooser on popdown: JFileChooser.CANCEL_OPTION,
     *         JFileChooser.APPROVE_OPTION
     */
    public int show(Component parent) {
        return showOpenDialog(parent);
    }

    // BUG Swing du JFileChooser qui calcule mal la dimension des composantes.
    // Ça semble être le popup des FileFilters
    // I've increased the width from 600 to 700 because buttons were troncated
    // in French [MS]
    public Dimension getPreferredSize() {
        return new Dimension(700, 300);
    }

    public void setApproveMethod(int type) {
        approveMethod = type;
    }

    public int getApproveMethod() {
        return approveMethod;
    }
}
