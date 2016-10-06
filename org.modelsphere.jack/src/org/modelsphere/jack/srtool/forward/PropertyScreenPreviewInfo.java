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

package org.modelsphere.jack.srtool.forward;

public abstract class PropertyScreenPreviewInfo {
    //
    // String returned by getDisplayName() will be displayed to the user
    // in the Generate submenu.
    //
    public String getTabName() {
        return "???";
    }

    //
    // The default behavior is to generate plain text in display window. A
    // subclass can change the default behavior by returning a String which
    // specifies the kind a text to be displayed, for example a subclass can
    // override this method and returns 'text/html' to display text in a html
    // format. String must be a valid format of function setContentType() of
    // the class javax.swing.JEditorPane.
    //
    public String getContentType() {
        return "text/plain";
    } // NOT LOCALIZABLE, content type

    //
    // Returns the list of supported classes for the preview window
    public Class[] getSupportedClasses() {
        return new Class[] {};
    };
}
