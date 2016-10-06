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

package org.modelsphere.plugins.diagram.tree;

class TreeDiagramOptions {
    private boolean m_honrizontal = true;
    private boolean m_center = true;
    private boolean m_fitProcesses = true;
    private boolean m_onlyExploded = false;
    private boolean m_doUpdate = false;
    private boolean m_span = true;

    TreeDiagramOptions() {
    }

    //accessors
    boolean isHorizontal() {
        return m_honrizontal;
    }

    boolean isCenter() {
        return m_center;
    }

    boolean fitProcesses() {
        return m_fitProcesses;
    }

    boolean onlyExploded() {
        return m_onlyExploded;
    }

    boolean doUpdate() {
        return m_doUpdate;
    }

    boolean isSpan() {
        return m_span;
    }

    void setHorizontal(boolean value) {
        m_honrizontal = value;
    }

    void setCenter(boolean value) {
        m_center = value;
    }

    void setfitProcesses(boolean value) {
        m_fitProcesses = value;
    }

    void setOnlyExploded(boolean value) {
        m_onlyExploded = value;
    }

    void setDoUpdate(boolean value) {
        m_doUpdate = value;
    }

    void setSpan(boolean value) {
        m_span = value;
    }

} //end ProcessTreeOptions
