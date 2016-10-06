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

/*
 * Created on Nov 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.modelsphere.jack.debug;

/**
 * @author Grandite
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences -
 *         Java - Code Style - Code Templates
 */
public class ConceptPair implements Comparable {

    private String conceptName;
    private int occurrencesCount;
    private boolean isSpecialized;

    public ConceptPair(String conceptName, int occurrencesCount, boolean isSpecialized) {
        setConceptName(conceptName);
        setOccurrencesCount(occurrencesCount);
        this.isSpecialized = isSpecialized;
    }

    public int compareTo(Object o) {
        return this.getConceptName().compareTo((String) o);
    }

    public void setConceptName(String sName) {
        conceptName = sName;
    }

    public String getConceptName() {
        return conceptName;
    }

    public void setOccurrencesCount(int nCount) {
        occurrencesCount = nCount;
    }

    public int getOccurrencesCount() {
        return occurrencesCount;
    }

    /**
     * @return Returns the m_bIsSpecialized.
     */
    public boolean IsSpecialized() {
        return isSpecialized;
    }
    /**
     * @param isSpecialized
     *            The m_bIsSpecialized to set.
     */
}
