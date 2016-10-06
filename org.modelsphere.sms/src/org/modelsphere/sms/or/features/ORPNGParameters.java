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

package org.modelsphere.sms.or.features;

import org.modelsphere.jack.baseDb.meta.MetaClass;

public final class ORPNGParameters {
    public static final String SEPARATOR = "%$$%";

    private MetaClass conceptMC;
    private int status;
    private String replacementChar;
    private int length;
    private boolean unique;
    private int nbrCharByWord;
    private int casePNG;

    public ORPNGParameters(MetaClass metaclass, ORPNGStatus status, String replacementChar,
            Integer length, Boolean unique, Integer nbrCharByWord, ORPNGCase c) {
        conceptMC = metaclass;
        this.status = status.getValue();
        this.replacementChar = replacementChar.toString();
        this.length = length.intValue();
        this.unique = unique.booleanValue();
        this.nbrCharByWord = nbrCharByWord.intValue();
        casePNG = c.getValue();
    }

    public final MetaClass getConceptMetaClass() {
        return conceptMC;
    }

    public final String getConceptName() {
        return conceptMC.getGUIName();
    }

    public final void setConceptMetaClass(MetaClass mc) {
        conceptMC = mc;
    }

    public final ORPNGStatus getStatus() {
        return ORPNGStatus.getInstance(status);
    }

    public final void setStatus(ORPNGStatus s) {
        status = s.getValue();
    }

    public final String getReplacementChar() {
        return replacementChar;
    }

    public final void setReplacementChar(String s) {
        replacementChar = s;
    }

    public final Boolean isUnique() {
        return new Boolean(unique);
    }

    public final void setUnique(Boolean b) {
        unique = b.booleanValue();
    }

    public final Integer getLength() {
        return new Integer(length);
    }

    public final void setLength(Integer i) {
        length = i.intValue();
    }

    public final Integer getNbrCharByWord() {
        return new Integer(nbrCharByWord);
    }

    public final void setNbrCharByWord(Integer i) {
        nbrCharByWord = i.intValue();
    }

    public final ORPNGCase getCase() {
        return ORPNGCase.getInstance(casePNG);
    }

    public final void setCase(ORPNGCase c) {
        casePNG = c.getValue();
    }

    public final Object get(int position) {
        switch (position) {
        case 0:
            return getConceptName();
        case 1:
            return getStatus();
        case 2:
            return getReplacementChar();
        case 3:
            return getLength();
        case 4:
            return isUnique();
        case 5:
            return getNbrCharByWord();
        case 6:
            return getCase();
        default:
            return null;
        }
    }

    public final void set(int position, Object value) {
        switch (position) {
        case 0:
            setConceptMetaClass((MetaClass) value);
            break;
        case 1:
            setStatus((ORPNGStatus) value);
            break;
        case 2:
            setReplacementChar((String) value);
            break;
        case 3:
            setLength((Integer) value);
            break;
        case 4:
            setUnique((Boolean) value);
            break;
        case 5:
            setNbrCharByWord((Integer) value);
            break;
        case 6:
            setCase((ORPNGCase) value);
            break;
        }
    }

    public final boolean toGenerate() {
        if ((status == ORPNGStatus.getInstance(ORPNGStatus.PARTIAL).getValue())
                || (status == ORPNGStatus.getInstance(ORPNGStatus.COMPLETE).getValue()))
            return true;
        else
            return false;
    }

    public final String toString() {
        String stringValue = null;
        stringValue = conceptMC.toString() + SEPARATOR + String.valueOf(status) + SEPARATOR
                + replacementChar + SEPARATOR + String.valueOf(length) + SEPARATOR
                + String.valueOf(unique) + SEPARATOR + String.valueOf(nbrCharByWord) + SEPARATOR
                + String.valueOf(casePNG);

        return stringValue;
    }

}
