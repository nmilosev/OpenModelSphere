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

public class FilePositions {
    private int m_linePos;
    private int m_colnPos;

    public int getLine() {
        return m_linePos;
    }

    public int getColumn() {
        return m_colnPos;
    }

    public FilePositions(int line, int coln) {
        m_linePos = line;
        m_colnPos = coln;
    } // end FilePositions()

    public boolean isBefore(FilePositions anotherPositions) {
        boolean isBefore;

        if (m_linePos < anotherPositions.m_linePos) {
            isBefore = true;
        } else if (m_linePos > anotherPositions.m_linePos) {
            isBefore = false;
        } else {
            isBefore = (m_colnPos < anotherPositions.m_colnPos) ? true : false;
        } // end if

        return isBefore;
    } // end isBefore()

} // end FilePositions
