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

package org.modelsphere.jack.templates;

import org.modelsphere.jack.srtool.forward.BooleanModifier;
import org.modelsphere.jack.srtool.forward.CaseModifier;
import org.modelsphere.jack.srtool.forward.IntegerModifier;

//A .tpl file is a list of several RuleStructures
public final class GenericModifierStructure {
    // Modifier Categories
    public static final int PREF_MODIFIER = 0;
    public static final int SUF_MODIFIER = 1;
    public static final int NULL_MODIFIER = 2;
    public static final int SEP_MODIFIER = 3;
    public static final int IF_MODIFIER = 4;
    public static final int IFNOT_MODIFIER = 5;
    public static final int FILE_MODIFIER = 6;
    public static final int EXTERN_MODIFIER = 7;
    public static final int PARM_MODIFIER = 8;
    public static final int WHEN_MODIFIER = 9;
    public static final int START_MODIFIER = 10;
    public static final int END_MODIFIER = 11;
    public static final int CASE_POLICY_MODIFIER = 12;

    // duplication
    public static final int NONE = 13;
    public static final int UNIQUE_MODIFIER = 14;
    public static final int DUPLICATED_MODIFIER = 15;

    // Modifier fields
    public int m_modifierCategory;
    public String m_text;
    public BooleanModifier m_whenCond;
    public IntegerModifier m_im;

    public GenericModifierStructure(int aCategory, String aText) {
        m_modifierCategory = aCategory;
        m_text = aText;
    }

    public GenericModifierStructure(int aCategory, BooleanModifier whenCond) {
        m_modifierCategory = aCategory; // assert WHEN_MODIFIER
        m_whenCond = whenCond;
    }

    public GenericModifierStructure(int aCategory, IntegerModifier im) {
        m_modifierCategory = aCategory; // assert START OR END
        m_im = im;
    }

    private int m_casePolicy = CaseModifier.UNCHANGE;

    public int getCasePolicy() {
        return m_casePolicy;
    }

    public GenericModifierStructure(int aCategory, int casePolicy) {
        m_modifierCategory = aCategory; // assert CASE_POLICY_MODIFIER
        m_casePolicy = casePolicy;
    }

    public GenericModifierStructure(int aCategory) {
        this(aCategory, (String) null);
    }
} // end of GenericModifierStructure
