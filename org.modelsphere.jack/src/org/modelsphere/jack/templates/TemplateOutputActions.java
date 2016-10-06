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

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.forward.BooleanModifier;
import org.modelsphere.jack.srtool.forward.IntegerModifier;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;

public final class TemplateOutputActions extends TemplateActions {

    // Called at the beginning of the .tpl file
    public void beginningOfFile() {
    }

    public void processDeclName(String name) {
        Debug.trace("Decl name = " + name);
    }

    public void processTemplate(String template) {
        Debug.trace("TEMPL = " + template);
    }

    public void processAttribute(String attribute) {
        Debug.trace("ATTR = " + attribute);
    }

    public void processConnector(String connector) {
        Debug.trace("CONN = " + connector);
    }

    public void processUserFn(String attribute) {
        Debug.trace("USERFN = " + attribute);
    }

    public void processUserConnector(String connector) {
        Debug.trace("USERCONN = " + connector);
    }

    public void processCondition(BooleanModifier condition) {
    }

    public void processVariableDeclaration(String id, String type, Object value) {
    }

    public void processSetStatement(String variable, Object value) {
    }

    public void processGetStatement(String variable) {
    }

    public void processInstanceof(String concept, String trueCondition, String falseCondition) {
    }

    // CDOM methods
    public void processCharacterDomain() {
        Debug.trace("CDOM");
    }

    public void processCharacterDomainClause(String domain, String value) {
        Debug.trace("CharDOM = (" + domain + ", " + value + ")");
    }

    public void processNoCaseQual(String caseQual) {
        Debug.trace("NOCASE = " + ((caseQual == null) ? "null" : caseQual));
    }

    // IDOM methods
    public void processIntegerDomain() {
        Debug.trace("IDOM");
    }

    public void processIntegerDomainClause(String domain, String value) {
        Debug.trace("IntegerDOM = (" + domain + ", " + value + ")");
    }

    // USERFN method
    public void processUserFunction(String functionName) {
        Debug.trace("USERFN = " + functionName);
    }

    public void processGroup(String group) {
        Debug.trace("GROUP = " + group);
    }

    /* MODIFIERS PROCESSING */
    public void processAttrQual(String qual) {
        Debug.trace("ATTR = " + qual);
    }

    public void processChildQual(String child, String qual) {
        Debug.trace("CHILD = " + child + "," + qual);
    }

    public void processDomQual(String qual) {
        Debug.trace("DOM = " + qual);
    }

    public void processExternQual(String qual) {
        Debug.trace("EXTERN");
    }

    public void processFalseQual(String qual) {
        Debug.trace("FALSE = " + qual);
    }

    public void processFileQual(String qual) {
        Debug.trace("FILE = " + qual);
    }

    public void processIfQual(String qual) {
        Debug.trace("IF = " + qual);
    }

    public void processIfNotQual(String qual) {
        Debug.trace("IFNOT = " + qual);
    }

    public void processNullQual(String qual) {
        Debug.trace("NULL = " + qual);
    }

    public void processParmQual(String qual) {
        Debug.trace("PARM = " + qual);
    }

    public void processPrefQual(String pref, String qual) {
        Debug.trace("PREF = " + pref + "," + qual);
    }

    public void processSepQual(String qual) {
        Debug.trace("SEP = " + qual);
    }

    public void processSufQual(String suf, String qual) {
        Debug.trace("SUF = " + suf + "," + qual);
    }

    public void processTrueQual(String qual) {
        Debug.trace("TRUE = " + qual);
    }

    public void processOneChildQual(String qual) {
    }

    public void processWhenCondition(BooleanModifier qual) {
    }

    public BooleanModifier processNotExpression(BooleanModifier bm1) {
        return null;
    }

    public BooleanModifier processBooleanExpression(int operation, Modifier m1, Modifier m2) {
        return null;
    }

    public BooleanModifier processBooleanVariable(String varname)
            throws VariableNotDefinedRuleException {
        return null;
    }

    public IntegerModifier processIntegerVariable(String varname)
            throws VariableNotDefinedRuleException {
        return null;
    }

    public IntegerModifier processIntegerLength(String varname)
            throws VariableNotDefinedRuleException {
        return null;
    }

    public void endOfDecl() {
        Debug.trace("end of decl");
    }

    public void endOfFile() {
        Debug.trace("Success: end of file has been reached");
    }
}
