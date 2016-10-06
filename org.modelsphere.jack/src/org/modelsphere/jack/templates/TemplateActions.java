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
import org.modelsphere.jack.srtool.forward.IntegerModifier;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.exceptions.ConceptNameNotFoundException;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.templates.parsing.ParseException;

public abstract class TemplateActions {

    // Called at the beginning of the .tpl file
    public abstract void beginningOfFile();

    // Called at each declaration of a new rule
    // For example, in : ''table_name TEMPL "$subrule;$n;"'' //NOT LOCALIZABLE,
    // comment
    // it will be called after 'table_name', with 'table_name' as parameter
    // Called for all kinds of declaration (TEMPL, CONN, etc.)
    public abstract void processDeclName(String name);

    // Called at each template declaration, with the pattern string as parameter
    // For example, in : ''table_name TEMPL "$subrule;$n;"'' //NOT LOCALIZABLE,
    // comment
    // it will be called after "$subrule;$n;", and before modifiers, if any,
    // //NOT LOCALIZABLE, comment
    // with "$subrule;$n;" as parameter. //NOT LOCALIZABLE, comment
    // Note: template can contain two consecutive double-quotes, in this case,
    // these double-quotes must be skipped
    // ex: "$subrule1;""$subrule2;" must be read as "$subrule1;$subrule2;" //NOT
    // LOCALIZABLE, comment
    public abstract void processTemplate(String template);

    public abstract void processAttribute(String attribute);

    public abstract void processConnector(String connector);

    public abstract void processUserFn(String function);

    public abstract void processUserConnector(String connector);

    public abstract void processGroup(String group);

    public abstract void processCondition(BooleanModifier condition);

    public abstract void processVariableDeclaration(String id, String type, Object value);

    public abstract void processSetStatement(String variable, Object value);

    public abstract void processGetStatement(String variable);

    public BooleanModifier processInstanceOf(String concept) throws ConceptNameNotFoundException {
        return null;
    }

    public void processAddToList(String id1, Object id2) {
    }

    public void processClearList(String id) {
    }

    public void processForEach(String iter, IntegerModifier start, IntegerModifier end) {
    }

    public void processForEach(String iter, String listname, String duplication) {
    }

    public void processImportClause(String classfile) {
    }

    // These methods are called after each modifier, in any kind of declaration
    // (TEMPL, CONN, etc.)

    // child = one of: {CHILD, CHILD1}
    public abstract void processAttrQual(String qual);

    public abstract void processChildQual(String child, String qual);

    public abstract void processOneChildQual(String qual);

    public abstract void processDomQual(String qual);

    public abstract void processExternQual(String qual);

    public abstract void processFalseQual(String qual);

    public abstract void processFileQual(String qual);

    public abstract void processIfQual(String qual);

    public abstract void processIfNotQual(String qual);

    public abstract void processNullQual(String qual);

    public abstract void processParmQual(String qual);

    public abstract void processSepQual(String qual);

    public abstract void processWhenCondition(BooleanModifier qual);

    public void processLowerQual() throws ParseException {
    };

    public void processUpperQual() throws ParseException {
    };

    // pref = one of: {PREF, PREF1}
    public abstract void processPrefQual(String pref, String qual);

    // suf = one of: {SUF, SUF1}
    public abstract void processSufQual(String suf, String qual);

    public abstract void processTrueQual(String qual);

    // CDOM methods
    public abstract void processCharacterDomain();

    public abstract void processCharacterDomainClause(String domain, String value);

    public abstract void processNoCaseQual(String caseQual); // can be null

    // IDOM methods
    public abstract void processIntegerDomain();

    public abstract void processIntegerDomainClause(String domain, String value);

    // USERFN method
    public abstract void processUserFunction(String functionName);

    // Boolean expression methods
    public abstract BooleanModifier processNotExpression(BooleanModifier bm1);

    public abstract BooleanModifier processBooleanExpression(int operation, Modifier m1, Modifier m2);

    public abstract BooleanModifier processBooleanVariable(String varname)
            throws VariableNotDefinedRuleException;

    public abstract IntegerModifier processIntegerVariable(String varname)
            throws VariableNotDefinedRuleException;

    public abstract IntegerModifier processIntegerLength(String varname)
            throws VariableNotDefinedRuleException;

    public BooleanModifier processBooleanExpression(int operation, String pred, String operand,
            String varname) throws VariableNotDefinedRuleException {
        return null;
    }

    // Called when the semi-colon ';' (indicating the end of the declaration)
    // is read.
    public abstract void endOfDecl() throws ParseException;

    // Called at the end of the .tpl file, if no syntax errors
    public abstract void endOfFile();

}
