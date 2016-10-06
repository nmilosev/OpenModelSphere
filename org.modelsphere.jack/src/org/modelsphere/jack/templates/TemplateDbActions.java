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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.forward.*;
import org.modelsphere.jack.srtool.forward.exceptions.*;
import org.modelsphere.jack.templates.GenericRuleStructure.*;
import org.modelsphere.jack.templates.parsing.ParseException;

public final class TemplateDbActions extends TemplateActions {
    private static final int UNKNOWN = -1;

    private String m_previousDeclName = null;
    private int m_previousKindOfRule = UNKNOWN;
    private Hashtable m_ruleList = new Hashtable();
    private VariableScope m_varList;

    public TemplateDbActions(VariableScope varList) {
        m_varList = varList;
    }

    // ///////////////////////////////////
    // SUPPORTS TemplateActions
    // Called at the beginning of the .tpl file
    public void beginningOfFile() {
        // clear previously-defined variables
        // VariableDecl.cleanUp(m_varList);
        m_varList.clear();
    }

    public void processDeclName(String name) {
        m_previousDeclName = name;
    }

    private String m_group = null;

    // @param group: list of rules separated by commas
    public void processGroup(String group) {
        m_group = group;
        m_previousKindOfRule = GenericRuleStructure.GROUP;
    }

    private String m_template = null;

    public void processTemplate(String template) {
        // remove starting and ending doubles quotes
        template = template.substring(1, template.length() - 1);
        template = removeDoubleQuotes(template);

        m_template = template;
        m_previousKindOfRule = GenericRuleStructure.TEMPLATE;
    }

    private String m_attribute = null;

    public void processAttribute(String attribute) {
        m_attribute = attribute;
        m_previousKindOfRule = GenericRuleStructure.PROPERTY;
    }

    public void processUserFn(String functionName) {
        m_userFunctionName = functionName;
        m_previousKindOfRule = GenericRuleStructure.USERFN;
    }

    private String m_connector = null;

    public void processConnector(String connector) {
        m_connector = connector;
        m_previousKindOfRule = GenericRuleStructure.CONNECTOR;
    }

    public void processUserConnector(String connector) {
        processConnector(connector);
    }

    // COND method
    private BooleanModifier m_condition = null;

    public void processCondition(BooleanModifier condition) {
        m_condition = condition;
        m_previousKindOfRule = GenericRuleStructure.CONDITION;
    }

    // CDOM methods
    public void processCharacterDomain() {
        m_previousKindOfRule = GenericRuleStructure.CDOM;
    }

    private ArrayList m_DList = new ArrayList();
    private ArrayList m_RList = new ArrayList();

    public void processCharacterDomainClause(String domain, String value) {
        // remove starting and ending doubles quotes
        domain = domain.substring(1, domain.length() - 1);
        value = value.substring(1, value.length() - 1);

        m_DList.add(domain);
        m_RList.add(value);
    }

    public void processNoCaseQual(String caseQual) {
    } // param caseQual can be null

    // IDOM methods
    public void processIntegerDomain() {
    }

    public void processIntegerDomainClause(String domain, String value) {
    }

    // USERFN method
    private String m_userFunctionName = null;

    public void processUserFunction(String functionName) {
        m_userFunctionName = functionName;
        m_previousKindOfRule = GenericRuleStructure.USERFN;
    }

    // LIST-RELATED methods
    public void processAddToList(String listName, Object element) {
        m_variable = listName;
        m_value = element;
        m_previousKindOfRule = GenericRuleStructure.ADD_TO_LIST;
    } // processAddToList()

    public void processClearList(String listName) {
        m_variable = listName;
        m_previousKindOfRule = GenericRuleStructure.CLEAR_LIST;
        // VariableDecl.VariableStructure listVar =
        // m_varList.getVariable(listName);
        // ArrayList list = (ArrayList)listVar.getValue();
        // list.clear();
    } // end processClearList()

    private String m_iter;
    private IntegerModifier m_start;
    private IntegerModifier m_end;

    public void processForEach(String iter, IntegerModifier start, IntegerModifier end) {
        m_iter = iter;
        m_start = start;
        m_end = end;
        m_previousKindOfRule = GenericRuleStructure.FOREACH_ITER;
    }

    private String m_duplication = null;

    public void processForEach(String iter, String listname, String duplication) {
        m_iter = iter;
        m_variable = listname;
        m_duplication = duplication;
        m_previousKindOfRule = GenericRuleStructure.FOREACH_ENUM;
    }

    public void processVariableDeclaration(String varname, String type, Object value)
            throws RuntimeException {

        try {
            int typeVal;
            if (type.equals("BOOLEAN")) { // NOT LOCALIZABLE, keyword
                typeVal = VariableDecl.BOOLEAN;
            } else if (type.equals("FLOAT")) { // NOT LOCALIZABLE, keyword
                typeVal = VariableDecl.FLOAT;
            } else if (type.equals("INTEGER")) { // NOT LOCALIZABLE, keyword
                typeVal = VariableDecl.INTEGER;
            } else if (type.equals("LIST")) { // NOT LOCALIZABLE, keyword
                typeVal = VariableDecl.LIST;
            } else {
                typeVal = VariableDecl.STRING;
            }

            boolean isExtern = m_externQual;
            VariableDecl.declare(m_varList, varname, typeVal, value, isExtern, m_externQualValue);
        } catch (RuleException ex) {
            throw new RuntimeException(ex.toString());
        }
    } // end processVariableDeclaration()

    private String m_variable = null;
    private Object m_value = null;

    public void processSetStatement(String variable, Object value) {
        m_variable = variable;
        m_value = value;
        m_previousKindOfRule = GenericRuleStructure.SET_VAR;
    } // end processSetStatement()

    public void processGetStatement(String variable) {
        m_variable = variable;
        m_previousKindOfRule = GenericRuleStructure.GET_VAR;
    } // end processGetStatement()

    private GenericRuleStructure createSetStatement() {

        GenericRuleStructure rs = new GenericSetVariableStructure(m_previousDeclName, m_variable,
                m_value);

        return rs;
    } // end processSetStatement()

    private GenericRuleStructure createAddToListStructure() {
        GenericRuleStructure rs = new GenericAddToListStructure(m_previousDeclName, m_variable,
                m_value);

        return rs;
    } // end createAddToListStructure()

    private GenericRuleStructure createClearListStructure() {
        GenericRuleStructure rs = new GenericClearListStructure(m_previousDeclName, m_variable);

        return rs;
    } // end createClearListStructure()

    private GenericRuleStructure createGetStatement() {

        GenericRuleStructure rs = new GenericGetVariableStructure(m_previousDeclName, m_variable);

        return rs;
    } // end processGetStatement()

    private GenericRuleStructure createImportStruct() {
        GenericRuleStructure rs = new GenericImportStructure(m_beanClassfile);
        return rs;
    } // end createImportStruct()

    private String m_beanClassfile = null;

    public void processImportClause(String classfile) {
        m_beanClassfile = classfile;
        m_previousKindOfRule = GenericRuleStructure.IMPORT;
    }

    /**
     ** MODIFIERS PROCESSING
     **/
    private String m_attrQual = null;

    public void processAttrQual(String qual) {
        m_attrQual = qual;
    }

    private String m_childQual = null;
    private String m_oneChildQual = null;

    public void processChildQual(String child, String qual) {
        // child is ignored (was used to store either CHILD or CHILD1)
        m_childQual = qual;
    }

    public void processOneChildQual(String qual) {
        m_oneChildQual = qual;
    }

    private String m_domQual = null;

    public void processDomQual(String qual) {
        qual = removeDoubleQuotes(qual);
        m_domQual = qual;
    }

    private boolean m_externQual = false;
    private String m_externQualValue = null;

    public void processExternQual(String qual) {
        Debug.trace("EXTERN = " + (qual == null ? "null" : qual));
        m_externQual = true;
        m_externQualValue = qual;
    }

    private String m_falseQual = null;

    public void processFalseQual(String qual) {
        m_falseQual = qual;
    }

    private String m_fileQual = null;

    public void processFileQual(String qual) {
        qual = removeDoubleQuotes(qual);
        m_fileQual = qual;
    }

    private String m_ifQual = null;

    public void processIfQual(String qual) {
        m_ifQual = qual;
    }

    private String m_ifNotQual = null;

    public void processIfNotQual(String qual) {
        m_ifNotQual = qual;
    }

    private String m_nullQual = null;

    public void processNullQual(String qual) {
        // qual = qual.substring(1, qual.length() - 1);
        qual = removeDoubleQuotes(qual);
        m_nullQual = qual;
    }

    private String m_parmQual = null;

    public void processParmQual(String qual) {
        m_parmQual = qual;
    }

    private static final String PREF = "PREF"; // NOT LOCALIZABLE, keyword
    private String m_pref1Qual = null;
    private String m_prefQual = null;

    public void processPrefQual(String pref, String qual) {
        qual = removeDoubleQuotes(qual);
        if (pref.equalsIgnoreCase(PREF)) {
            m_prefQual = qual;
        } else {
            m_pref1Qual = qual;
        }
    }

    private static final String SUF = "SUF"; // NOT LOCALIZABLE, keyword
    private String m_suf1Qual = null;
    private String m_sufQual = null;

    public void processSufQual(String suf, String qual) {
        qual = removeDoubleQuotes(qual);
        if (suf.equalsIgnoreCase(SUF)) {
            m_sufQual = qual;
        } else {
            m_suf1Qual = qual;
        }
    }

    private String m_sepQual = null;

    public void processSepQual(String qual) {
        qual = removeDoubleQuotes(qual);
        m_sepQual = qual;
    }

    private String m_trueQual = null;

    public void processTrueQual(String qual) {
        m_trueQual = qual;
    }

    private BooleanModifier m_whenCond = null;

    public void processWhenCondition(BooleanModifier bm) {
        m_whenCond = bm;
    }

    //
    // UPPER and LOWER keywords
    //
    private int m_casePolicy = CaseModifier.UNCHANGE;

    public void processUpperQual() throws ParseException {
        if (m_casePolicy == CaseModifier.LOWER) {
            throw new ParseException("LOWER and UPPER modifiers are mutually exclusive."); // NOT
            // LOCALIZABLE,
            // template
            // message
        }

        m_casePolicy = CaseModifier.UPPER;
    }

    public void processLowerQual() throws ParseException {
        if (m_casePolicy == CaseModifier.UPPER) {
            throw new ParseException("LOWER and UPPER modifiers are mutually exclusive."); // NOT
            // LOCALIZABLE,
            // template
            // message
        }

        m_casePolicy = CaseModifier.LOWER;
    }

    //
    // BOOLEAN
    //
    public BooleanModifier processNotExpression(BooleanModifier bm1) {
        BooleanModifier bm = new BooleanModifier(BooleanModifier.NOT_EXPRESSION, bm1);
        return bm;
    }

    public BooleanModifier processBooleanExpression(int operation, Modifier m1, Modifier m2) {
        BooleanModifier bm = new BooleanModifier(operation, m1, m2);
        return bm;
    }

    public BooleanModifier processBooleanVariable(String varname)
            throws VariableNotDefinedRuleException {
        BooleanModifier bm = new BooleanModifier(m_varList, varname);
        return bm;
    }

    public BooleanModifier processBooleanExpression(int operation, String pred, String operand,
            String varname) throws VariableNotDefinedRuleException {
        BooleanModifier bm = new BooleanModifier(operation, pred, operand, m_varList, varname);
        return bm;
    }

    public BooleanModifier processInstanceOf(String conceptname)
            throws ConceptNameNotFoundException {
        MetaClass mc = MetaClass.find(conceptname);

        if (mc == null) { // try reflexion
            try {
                String unquoted = conceptname.substring(1, conceptname.length() - 1);
                Class claz = Class.forName(unquoted);
                Field f = claz.getField("metaClass"); // NOT LOCALIZABLE
                mc = (MetaClass) f.get(null);
                int i = 0;
            } catch (ClassNotFoundException ex) {
                mc = null;
            } catch (NoSuchFieldException ex) {
                mc = null;
            } catch (IllegalAccessException ex) {
                mc = null;
            } // end try
        } // end if

        if (mc == null) {
            throw new ConceptNameNotFoundException(conceptname);
        } // end if

        BooleanModifier bm = new BooleanModifier(BooleanModifier.INSTANCEOF_EXPRESSION, mc);
        return bm;
    }

    //
    // INTEGER
    //

    public IntegerModifier processIntegerVariable(String varname)
            throws VariableNotDefinedRuleException {
        IntegerModifier im = new IntegerModifier(m_varList, varname);
        return im;
    }

    public IntegerModifier processIntegerLength(String varname)
            throws VariableNotDefinedRuleException {
        IntegerModifier im = new IntegerModifier(IntegerModifier.INTEGER_LENGTH, m_varList, varname);
        return im;
    }

    /*
     * NOT READY YET public BooleanModifier processInstanceOf(String conceptName) { conceptName
     * 
     * //BooleanModifier bm = new BooleanModifier(m_varList, varname); return bm; }
     */

    //
    // RESTORE CONTEXT after each statement
    //
    private void restoreContext() {
        // erase rules;
        m_previousDeclName = null;
        m_previousKindOfRule = UNKNOWN;
        m_template = null;
        m_connector = null;
        m_attribute = null;
        m_variable = null;
        m_value = null;
        m_condition = null;
        m_DList = new ArrayList();
        m_RList = new ArrayList();
        m_beanClassfile = null;

        // erase qualifiers
        m_attrQual = null;
        m_childQual = null;
        m_oneChildQual = null;
        m_domQual = null;
        m_falseQual = null;
        m_fileQual = null;
        m_ifQual = null;
        m_ifNotQual = null;
        m_nullQual = null;
        m_parmQual = null;
        m_prefQual = null;
        m_pref1Qual = null;
        m_sepQual = null;
        m_sufQual = null;
        m_suf1Qual = null;
        m_trueQual = null;
        m_externQual = false;
        m_externQualValue = null;
        m_whenCond = null;
        m_casePolicy = CaseModifier.UNCHANGE;

        m_iter = null;
        m_start = null;
        m_end = null;
        m_duplication = null;
    } // end restoreContext()

    private GenericRuleStructure createTemplateStruct() {
        GenericModifierStructure[] modifiers = createModifierStruct();

        GenericRuleStructure rs = new GenericTemplateStructure(m_previousDeclName, m_template,
                m_domQual, modifiers);

        return rs;
    }

    private GenericModifierStructure[] createModifierStruct() {
        GenericModifierStructure[] modifiers;
        ArrayList list = new ArrayList();

        GenericModifierStructure mod = null;

        if (m_externQual) {
            mod = new GenericModifierStructure(GenericModifierStructure.EXTERN_MODIFIER,
                    m_externQualValue);
            list.add(mod);
        }

        if (m_fileQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.FILE_MODIFIER, m_fileQual);
            list.add(mod);
        }

        if (m_ifQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.IF_MODIFIER, m_ifQual);
            list.add(mod);
        }

        if (m_ifNotQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.IFNOT_MODIFIER, m_ifNotQual);
            list.add(mod);
        }

        if (m_nullQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.NULL_MODIFIER, m_nullQual);
            list.add(mod);
        }

        if (m_prefQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.PREF_MODIFIER, m_prefQual);
            list.add(mod);
        }

        if (m_pref1Qual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.PREF_MODIFIER, m_pref1Qual);
            list.add(mod);
        }

        if (m_sepQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.SEP_MODIFIER, m_sepQual);
            list.add(mod);
        }

        if (m_sufQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.SUF_MODIFIER, m_sufQual);
            list.add(mod);
        }

        if (m_suf1Qual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.SUF_MODIFIER, m_suf1Qual);
            list.add(mod);
        }

        if (m_parmQual != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.PARM_MODIFIER, m_parmQual);
            list.add(mod);
        }

        if (m_whenCond != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.WHEN_MODIFIER, m_whenCond);
            list.add(mod);
        }

        if (m_start != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.START_MODIFIER, m_start);
            list.add(mod);
        }

        if (m_end != null) {
            mod = new GenericModifierStructure(GenericModifierStructure.END_MODIFIER, m_end);
            list.add(mod);
        }

        if (m_casePolicy != CaseModifier.UNCHANGE) {
            mod = new GenericModifierStructure(GenericModifierStructure.CASE_POLICY_MODIFIER,
                    m_casePolicy);
            list.add(mod);
        }

        if (m_duplication != null) {
            if (m_duplication.equals("UNIQUE")) { // NOT LOCALIZABLE, keyword
                mod = new GenericModifierStructure(GenericModifierStructure.UNIQUE_MODIFIER);
            } else {
                mod = new GenericModifierStructure(GenericModifierStructure.DUPLICATED_MODIFIER);
            }

            list.add(mod);
        }

        // TODO: add other modifiers HERE
        /*
         * m_falseQual = null; COND m_trueQual = null; COND
         */

        // m_attrQual only used in USERFN
        // m_childQual & m_domQual only used in CONN and ATTR constructor,
        // respectively
        // no need to log them in GenericModifierStructure.
        // return array of modifiers
        int nb = list.size();
        modifiers = new GenericModifierStructure[nb];
        for (int i = 0; i < nb; i++) {
            modifiers[i] = (GenericModifierStructure) list.get(i);
        } // end if

        return modifiers;
    }

    private GenericRuleStructure createConnectorStruct() {

        GenericModifierStructure[] modifiers = createModifierStruct();

        GenericRuleStructure rs = new GenericConnectorStructure(m_previousDeclName, m_connector,
                m_childQual, m_oneChildQual, // may
                // be
                // null
                modifiers);

        return rs;
    }

    private GenericRuleStructure createConditionStruct() {
        GenericModifierStructure[] modifiers = createModifierStruct();
        String[] ruleList = new String[] { m_trueQual, m_falseQual };

        GenericRuleStructure rs = new GenericConditionStructure(m_previousDeclName, m_condition,
                ruleList, modifiers);

        return rs;
    }

    private GenericRuleStructure createGroupStruct() {

        GenericModifierStructure[] modifiers = createModifierStruct();

        GenericRuleStructure rs = new GenericGroupStructure(m_previousDeclName, m_group, modifiers);

        return rs;
    }

    private GenericRuleStructure createPropertyStruct() {

        GenericModifierStructure[] modifiers = createModifierStruct();

        GenericRuleStructure rs = new GenericPropertyStructure(m_previousDeclName, m_attribute,
                m_domQual, modifiers);

        return rs;
    }

    private GenericRuleStructure createCDomStruct() {

        GenericModifierStructure[] modifiers = createModifierStruct();
        String[] dom = new String[m_DList.size()];
        String[] rlz = new String[m_RList.size()];
        for (int i = 0; i < m_DList.size(); i++) {
            dom[i] = removeDoubleQuotes((String) m_DList.get(i));
            rlz[i] = removeDoubleQuotes((String) m_RList.get(i));
        }

        GenericRuleStructure rs = new GenericCDomStructure(m_previousDeclName, dom, rlz, modifiers);

        return rs;
    }

    private GenericRuleStructure createUserFunction() {

        GenericModifierStructure[] modifiers = createModifierStruct();
        GenericRuleStructure rs = new GenericUserFnStructure(m_previousDeclName,
                m_userFunctionName, m_childQual, modifiers);

        return rs;
    }

    private GenericRuleStructure createUserConnFunction() {

        GenericModifierStructure[] modifiers = createModifierStruct();
        GenericRuleStructure rs = new GenericUserConnStructure(m_previousDeclName,
                m_userFunctionName, m_childQual, modifiers);

        return rs;
    }

    private GenericRuleStructure createForEachIterStructure() {
        GenericModifierStructure[] modifiers = createModifierStruct();
        GenericRuleStructure rs = new GenericForEachIterStructure(m_previousDeclName, m_iter, null,
                m_childQual, modifiers);

        return rs;
    }

    private GenericRuleStructure createForEachEnumStructure() {
        GenericModifierStructure[] modifiers = createModifierStruct();
        GenericRuleStructure rs = new GenericForEachEnumStructure(m_previousDeclName, m_iter,
                m_variable, m_childQual, modifiers);

        return rs;
    }

    // meet of ';' in .tpl file
    public void endOfDecl() throws ParseException {
        GenericRuleStructure rs = null;

        switch (m_previousKindOfRule) {
        case GenericRuleStructure.IMPORT:
            rs = createImportStruct();
            break;
        case GenericRuleStructure.TEMPLATE:
            try {
                rs = createTemplateStruct();
            } catch (NumberFormatException ex) {
                String msg = IllformedNumberException.buildMessage(m_previousDeclName, ex
                        .getMessage(), m_template);
                throw new IllformedNumberException(msg);
            } catch (IllegalArgumentException ex) {
                String msg = ex.getMessage();
                throw new TemplateFormatException(msg);
            }
            break;
        case GenericRuleStructure.CONNECTOR:
            rs = createConnectorStruct();
            break;
        case GenericRuleStructure.PROPERTY:
            rs = createPropertyStruct();
            break;
        case GenericRuleStructure.USERFN:
            rs = createUserFunction();
            break;
        case GenericRuleStructure.USERCONN:
            rs = createUserConnFunction();
            break;
        case GenericRuleStructure.CDOM:
            rs = createCDomStruct();
            break;
        case GenericRuleStructure.GROUP:
            rs = createGroupStruct();
            break;
        case GenericRuleStructure.CONDITION:
            rs = createConditionStruct();
            break;
        case GenericRuleStructure.SET_VAR:
            rs = createSetStatement();
            break;
        case GenericRuleStructure.GET_VAR:
            rs = createGetStatement();
            break;
        case GenericRuleStructure.FOREACH_ITER:
            rs = createForEachIterStructure();
            break;
        case GenericRuleStructure.FOREACH_ENUM:
            rs = createForEachEnumStructure();
            break;
        case GenericRuleStructure.ADD_TO_LIST:
            rs = createAddToListStructure();
            break;
        case GenericRuleStructure.CLEAR_LIST:
            rs = createClearListStructure();
            break;
        }

        try {
            if (rs != null) {
                // TODO : raise an exception if a rule with the same name exists
                String name = rs.getRuleName();
                if ((name != null) && (m_ruleList.containsKey(name))) {
                    String msg = DuplicatedRulenamesException.buildMessage(name);
                    throw new DuplicatedRulenamesException(msg);
                }
                if (name == null) {
                    name = "";
                } // null not a valid key
                m_ruleList.put(name, rs);
            } // end if
        } finally {
            restoreContext();
        } // end try
    } // end endOfDecl

    public void endOfFile() {
        // Debug.trace("EndOfFile");
    }

    // //
    // //////////////////////////////

    // Called by GenericForward.createRules()
    public GenericRuleStructure[] getReadRules() {

        // get rules contained in the hashtable
        int nb = m_ruleList.size();
        GenericRuleStructure[] rules = new GenericRuleStructure[nb];

        int i = 0;
        Enumeration enumeration = m_ruleList.elements();
        while (enumeration.hasMoreElements()) {
            rules[i++] = (GenericRuleStructure) enumeration.nextElement();
        } // end while

        return rules;
    }

    private String removeDoubleQuotes(String aString) {
        // remove starting double quotes after line break in a template like
        // test TEMPL "aaaa" //NOT LOCALIZABLE, comment
        // "ewww"; //NOT LOCALIZABLE, comment
        int i = 0;
        while (i < aString.length()) {
            if (aString.charAt(i) == '\"') {
                aString = aString.substring(0, i) + aString.substring(i + 1, aString.length());
                i = 0;
            }
            i++;
        } // end while
        return aString;
    }

    /*
     * public static void main(String[] args) throws java.io.IOException { TemplateDbActions actions
     * = new TemplateDbActions(); String str = actions.removeDoubleQuotes("\"DEFA\"ULT\""); //NOT
     * LOCALIZABLE, unit test Debug.trace("str = " + str);
     * 
     * Debug.trace("Press ENTER to quit."); byte[] buffer = new byte[256]; Debug.trace(buffer, 0,
     * 256); }
     */
}
