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
package org.modelsphere.sms.templates;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelationship;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.srtool.forward.BooleanModifier;
import org.modelsphere.jack.srtool.forward.CaseModifier;
import org.modelsphere.jack.srtool.forward.CharacterDomain;
import org.modelsphere.jack.srtool.forward.Choice;
import org.modelsphere.jack.srtool.forward.Connector;
import org.modelsphere.jack.srtool.forward.DomainModifier;
import org.modelsphere.jack.srtool.forward.ExternModifier;
import org.modelsphere.jack.srtool.forward.FileModifier;
import org.modelsphere.jack.srtool.forward.ForEach;
import org.modelsphere.jack.srtool.forward.GetVariable;
import org.modelsphere.jack.srtool.forward.Group;
import org.modelsphere.jack.srtool.forward.ImportClause;
import org.modelsphere.jack.srtool.forward.IntegerModifier;
import org.modelsphere.jack.srtool.forward.JackForwardEngineeringPlugin;
import org.modelsphere.jack.srtool.forward.Modifier;
import org.modelsphere.jack.srtool.forward.NullModifier;
import org.modelsphere.jack.srtool.forward.PrefixModifier;
import org.modelsphere.jack.srtool.forward.Property;
import org.modelsphere.jack.srtool.forward.Rule;
import org.modelsphere.jack.srtool.forward.RuleException;
import org.modelsphere.jack.srtool.forward.SeparatorModifier;
import org.modelsphere.jack.srtool.forward.SetVariable;
import org.modelsphere.jack.srtool.forward.StringStructure;
import org.modelsphere.jack.srtool.forward.SuffixModifier;
import org.modelsphere.jack.srtool.forward.Template;
import org.modelsphere.jack.srtool.forward.TemplateEnumeration;
import org.modelsphere.jack.srtool.forward.UserDefinedField;
import org.modelsphere.jack.srtool.forward.VariableScope;
import org.modelsphere.jack.srtool.forward.exceptions.InvalidMetafieldRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.VariableNotDefinedRuleException;
import org.modelsphere.jack.templates.GenericModifierStructure;
import org.modelsphere.jack.templates.GenericRuleStructure;
import org.modelsphere.jack.templates.TemplateDbActions;
import org.modelsphere.jack.templates.parsing.JavaCharStream;
import org.modelsphere.jack.templates.parsing.ParseException;
import org.modelsphere.jack.templates.parsing.TemplateParser;
import org.modelsphere.jack.templates.parsing.TokenMgrError;
import org.modelsphere.jack.util.JarUtil;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * This class provides common services for code generation plug-ins.
 * 
 */
public abstract class GenericForwardEngineeringPlugin extends JackForwardEngineeringPlugin {
    protected static final boolean ALWAYS_PARSE = true;
    protected static final boolean PARSE_IF_NECESSARY = false;
    //private static final String GENERIC_FORWARD = "Generic Forward";  //NOT LOCALIZABLE, debug info only

    private static final String CANNOT_FIND_CONSTR_PATTRN = LocaleMgr.message
            .getString("CANNOT_FIND_CONSTR_PATTRN");
    private static final String SYN_ERR_DETECTED_PATTRN = LocaleMgr.message
            .getString("SYN_ERR_DETECTED_PATTRN");
    private static final String LEX_ERR_PATTRN = LocaleMgr.message.getString("LEX_ERR_PATTRN");
    private static final String ENDLESS_LOOP_PATTRN = LocaleMgr.message
            .getString("ENDLESS_LOOP_PATTRN");
    private static final String RULE_NOT_FOUND_MSG = LocaleMgr.message
            .getString("RULE_NOT_FOUND_MSG");
    private static final String RULE_NOT_FOUND_PATTRN = LocaleMgr.message
            .getString("RULE_NOT_FOUND_PATTRN");
    private static final String CONN_ERR_PATTRN = LocaleMgr.message.getString("CONN_ERR_PATTRN");
    private static final String NO_TRUE_RULE_PATTERN = LocaleMgr.message
            .getString("NO_TRUE_RULE_PATTERN");
    private static final String NO_FALSE_RULE_PATTERN = LocaleMgr.message
            .getString("NO_FALSE_RULE_PATTERN");
    private static final String NO_DOMAIN_ALLOWED_PATTERN = LocaleMgr.message
            .getString("NO_DOMAIN_ALLOWED_PATTERN");

    protected URL getTemplateURL() {
        return null;
    }

    //one variable list per forward module
    //protected VariableScope m_varList = new VariableScope(GENERIC_FORWARD);
    public abstract VariableScope getVarScope(); // {return m_varList;}

    public abstract void setVarScope(VariableScope varScope);

    //Required by the unit test. Do not remove!
    static {
        try {
            Class<?> claz = Class.forName("org.modelsphere.sms.Application"); //NOT LOCALIZABLE, unit test
            java.lang.reflect.Method method = claz.getDeclaredMethod("initMeta", new Class[] {}); //NOT LOCALIZABLE, unit test
            method.invoke(null, new Object[] {});
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    protected Hashtable m_ruletable = new Hashtable();

    //DUMMY METHOD, its only purpose is to avoid the declarations of forwardTo(DbObject, int)
    //in subclasses.  DO NOT REMOVE final modifier [MS]
    protected int forwardTo(DbObject semObj, int nbFiles) throws DbException, IOException,
            RuleException {
        return 0;
    }

    protected void forwardTo(DbObject semObj, ArrayList generatedFiles) throws DbException,
            IOException, RuleException {
        // has to be coded
    }

    public void setOutputToASCIIFormat() {
    }

    //This function sets rule constructors (template constructor m_tempConstr,
    //connector constructor, and so on).
    Constructor m_tempConstr, m_connConstr, m_propConstr, m_cdomConstr; //set here, but used elsewhere
    Constructor m_userFnConstr, m_groupConstr, m_condConstr;

    private void getRuleConstructors() throws InitializationException {

        Class currentClass = Template.class;

        try {
            currentClass = UserDefinedField.class;
            m_userFnConstr = currentClass.getConstructor(new Class[] { String.class, String.class,
                    Modifier[].class });

            //TODO: add other required constructors HERE
        } catch (NoSuchMethodException ex) {
            String msg = MessageFormat.format(CANNOT_FIND_CONSTR_PATTRN,
                    new Object[] { currentClass.getName() });
            throw new InitializationException(msg);
        }

    } //end getRuleConstructors()

    //this method creates rules extracted from a .tpl file
    private static TemplateParser g_parser = null; //just one occurrence of parser

    private GenericRuleStructure[] createRules(URL templateURL) throws FileNotFoundException,
            IOException {

        GenericRuleStructure[] rules = null;

        InputStream stream = null;
        try {
            g_syntaxErrorRule = null;
            TemplateDbActions actions = new TemplateDbActions(getVarScope());
            stream = createInputStream(templateURL); 
            //stream = new FileInputStream(tplURL);
            if (g_parser == null) {
                g_parser = new TemplateParser(stream);
            } else {
                TemplateParser.ReInit(stream);
            }

            TemplateParser.setActions(actions);
            TemplateParser.Input(); //call the entry point of the parser

            rules = actions.getReadRules();
        } catch (ParseException ex) {
            String msg = ex.getMessage();
            msg = removeLastDotIfAny(msg);
            String syn_msg = MessageFormat.format(SYN_ERR_DETECTED_PATTRN, new Object[] {
                    getTemplateURL() == null ? templateURL.getFile() : getTemplateURL(), msg });
            g_syntaxErrorRule = new Template(null, syn_msg);
        } catch (TokenMgrError ex) {
            String msg = ex.getMessage();
            msg = removeLastDotIfAny(msg);
            String lex_msg = MessageFormat.format(LEX_ERR_PATTRN, new Object[] {
                    getTemplateURL() == null ? templateURL.getFile() : getTemplateURL(), msg });
            g_syntaxErrorRule = new Template(null, lex_msg);
        } finally {
            JavaCharStream.Done();
            if (stream != null) {
                try {
                    stream.close(); //close the file, even if errors occur
                } catch (IOException ex) {
                    //ignore
                }
            }
        }

        return rules;
    }

    private InputStream createInputStream(URL templateURL) {
    	InputStream input;
    	
    	//try to open as a URL
    	try {
    		input = templateURL.openStream();
    	} catch (IOException e) {
			input = null;
		} //end try
		
		//2nd try : open as a file
		if (input == null) {
			try {
				File file = new File (templateURL.getFile());
				input = new FileInputStream(file);
			} catch (IOException e) {
				input = null;
			} //end try
		}

		return input;
	}

	//If msg ends with a '.', remove it
    private String removeLastDotIfAny(String msg) {
        int len = msg.length();
        if (msg.charAt(len - 1) == '.') {
            msg = msg.substring(0, len - 1);
        }

        return msg;
    }

    private Rule createTemplateRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            VariableNotDefinedRuleException {

        Rule rule = null;
        String rulename = currentRS.getRuleName();
        String text = currentRS.getRuleText();
        String[] subr = currentRS.m_stringRules;
        String dom = currentRS.getSubRule();
        Modifier[] modifiers = createModifiers(currentRS);

        if (subr.length == 0) {
            //if there are no subrules
            rule = new Template(rulename, text, new String[] {}, dom, modifiers);
        } else {
            rule = new Template(rulename, text, subr, dom, modifiers);
        }

        return rule;
    }

    private Modifier[] createModifiers(GenericRuleStructure currentRS)
            throws VariableNotDefinedRuleException {
        return createModifiers(currentRS, null);
    }

    private Modifier[] createModifiers(GenericRuleStructure currentRS, Modifier optionalModifier)
            throws VariableNotDefinedRuleException {
        Modifier[] modifiers;
        ArrayList modifierList = new ArrayList();
        if (optionalModifier != null) {
            modifierList.add(optionalModifier);
        }

        //create modifiers in an array list
        int nb = currentRS.m_modifiers.length;
        for (int i = 0; i < nb; i++) {
            GenericModifierStructure ms = currentRS.m_modifiers[i];
            //Note: DomainModifier is a special case, not processed here
            switch (ms.m_modifierCategory) {
            case GenericModifierStructure.EXTERN_MODIFIER:
                ExternModifier ex = new ExternModifier(ms.m_text);
                modifierList.add(ex);
                break;
            case GenericModifierStructure.FILE_MODIFIER:
                FileModifier fm = new FileModifier(ms.m_text);
                modifierList.add(fm);
                break;
            case GenericModifierStructure.IF_MODIFIER:
                BooleanModifier bm = new BooleanModifier(getVarScope(), ms.m_text);
                modifierList.add(bm);
                break;
            case GenericModifierStructure.IFNOT_MODIFIER:
                BooleanModifier bm1 = new BooleanModifier(getVarScope(), ms.m_text);
                BooleanModifier bm2 = new BooleanModifier(BooleanModifier.NOT_EXPRESSION, bm1);
                modifierList.add(bm2);
                break;
            case GenericModifierStructure.NULL_MODIFIER:
                NullModifier nm = new NullModifier(ms.m_text);
                modifierList.add(nm);
                break;
            case GenericModifierStructure.PREF_MODIFIER:
                PrefixModifier pm = new PrefixModifier(ms.m_text);
                modifierList.add(pm);
                break;
            case GenericModifierStructure.SEP_MODIFIER:
                SeparatorModifier sp = new SeparatorModifier(ms.m_text);
                modifierList.add(sp);
                break;
            case GenericModifierStructure.SUF_MODIFIER:
                SuffixModifier sm = new SuffixModifier(ms.m_text);
                modifierList.add(sm);
                break;
            case GenericModifierStructure.START_MODIFIER:
                IntegerModifier start = ms.m_im;
                modifierList.add(start);
                break;
            case GenericModifierStructure.END_MODIFIER:
                IntegerModifier end = ms.m_im;
                modifierList.add(end);
                break;
            case GenericModifierStructure.WHEN_MODIFIER:
                modifierList.add(ms.m_whenCond);
                break;
            case GenericModifierStructure.CASE_POLICY_MODIFIER:
                CaseModifier mod = new CaseModifier(ms.getCasePolicy());
                modifierList.add(mod);
                //TODO: add other modifiers HERE
            }
        } //end for

        //return array of modifiers
        nb = modifierList.size();
        modifiers = new Modifier[nb];
        for (int i = 0; i < nb; i++) {
            modifiers[i] = (Modifier) modifierList.get(i);
        }

        return modifiers;
    } //end createModifiers()

    private Rule createConnectorRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            IllegalAccessException, InstantiationException, RuleException, ClassNotFoundException,
            VariableNotDefinedRuleException {

        String rulename = currentRS.getRuleName();
        String text = currentRS.getRuleText();
        String child = currentRS.getSubRule();
        String[] strRules = currentRS.m_stringRules; //ONECHILD stored here, if any
        Rule rule = null;

        Modifier[] modifiers = createModifiers(currentRS);
        try {
            MetaRelationship metaRelation = (MetaRelationship) GenericMapping.getMetaRelation(text);
            MetaClass childrenMetaClass = GenericMapping.getChildrenMetaClass(text);

            if (metaRelation != null) {
                rule = new Connector(rulename, metaRelation, child, strRules, childrenMetaClass,
                        modifiers);
            } else {
                String ruleText = currentRS.getRuleText();
                String repositoryFunctionName = GenericMapping.getRepositoryFunction(ruleText);
                rule = createUserFunctionRule(GenericRuleStructure.CONNECTOR, currentRS,
                        repositoryFunctionName, modifiers);
            } //end if
        } catch (NullPointerException ex) {
            String msg = MessageFormat.format(CONN_ERR_PATTRN, new Object[] { text });
            throw new RuleException(msg);
        } catch (InvocationTargetException ex) {
            String msg = MessageFormat.format(CONN_ERR_PATTRN, new Object[] { text });
            throw new RuleException(msg);
        }

        return rule;
    }

    private Rule createConditionRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            RuleException, ClassNotFoundException, VariableNotDefinedRuleException {

        Rule rule = null;
        Debug.trace("enterring createConditionRule()");

        String rulename = currentRS.getRuleName();
        BooleanModifier booleanModifier = currentRS.m_booleanModifier;
        Modifier[] modifiers = createModifiers(currentRS, booleanModifier);
        String trueCondition = currentRS.m_stringRules[0];
        String falseCondition = currentRS.m_stringRules[1];

        if ((trueCondition == null) && (falseCondition == null)) {
            String msg = MessageFormat.format(NO_TRUE_RULE_PATTERN, new Object[] { rulename });
            throw new RuleException(msg);
        } //end if

        rule = new Choice(rulename, trueCondition, falseCondition, modifiers);
        return rule;
    }

    private Rule createGroupRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            VariableNotDefinedRuleException {

        String rulename = currentRS.getRuleName();
        String text = currentRS.getRuleText();
        Modifier[] modifiers = createModifiers(currentRS);
        Rule rule = new Group(rulename, text, modifiers);
        return rule;
    }

    private Rule createPropertyRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException,
            ClassNotFoundException, RuleException, VariableNotDefinedRuleException {

        Rule rule = null;
        String rulename = currentRS.getRuleName();
        String text = currentRS.getRuleText();
        String dom = currentRS.getSubRule();

        Modifier[] modifiers = createModifiers(currentRS);
        MetaField metaField = GenericMapping.getMetaField(currentRS.getRuleText());
        if (metaField != null) {
            rule = new Property(rulename, metaField, dom, modifiers);
        } else {
            String repositoryFunctionName = GenericMapping.getRepositoryFunction(currentRS
                    .getRuleText());
            String msg = InvalidMetafieldRuleException.buildMessage(rulename,
                    repositoryFunctionName);
            throw new RuleException(msg);
        }

        return rule;
    }

    private Rule createUserFunctionRule(int kindOfRuleInTemplate, //how it has been defined in the template
            GenericRuleStructure currentRS, String repositoryFunctionName, Modifier[] modifiers)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException,
            InstantiationException, ClassNotFoundException, RuleException {
    	
        String actualName = "org.modelsphere.sms.plugins.generic.repository."; //NOT LOCALIZABLE
        actualName += repositoryFunctionName;
        String rulename = currentRS.getRuleName();

        Class claz = null;
        PluginMgr pluginMgr = PluginMgr.getSingleInstance();
        claz = pluginMgr.getPluginClass(actualName);
        if (claz == null)
            claz = Class.forName(actualName);

        if (claz == null) {
            throw new ClassNotFoundException();
        }

        Constructor constr = null;
        try {
            //plug-in function constructor
            constr = claz
                    .getConstructor(new Class[] { String.class, String.class, Modifier[].class });
        } catch (NoSuchMethodException ex) {
            //Do nothing, will try another constructor later
        }

        Rule rule;
        if (constr != null) {
            String subRule = currentRS.getSubRule();
            rule = (Rule) constr.newInstance(new Object[] { rulename, subRule, modifiers });
        } else {
            Connector.UserConnector userConn = (Connector.UserConnector) claz.newInstance();
            String childRule = currentRS.getSubRule();
            String oneChildRule = (currentRS.m_stringRules == null) ? null
                    : currentRS.m_stringRules[0];
            rule = userConn.createInstance(childRule, oneChildRule);

            //add modifiers
            for (int i = 0; i < modifiers.length; i++) {
                Modifier mod = modifiers[i];
                if (mod instanceof PrefixModifier) {
                    rule.prefixModifier = (PrefixModifier) mod;
                } else if (mod instanceof SeparatorModifier) {
                    rule.separatorModifier = (SeparatorModifier) mod;
                } else if (mod instanceof SuffixModifier) {
                    rule.suffixModifier = (SuffixModifier) mod;
                } else if (mod instanceof NullModifier) {
                    rule.nullModifier = (NullModifier) mod;
                } else if (mod instanceof DomainModifier) {
                    rule.domainModifier = (DomainModifier) mod;
                } //end if
            } //end for
        } //end if

        return rule;
    }

    private Rule createCDomRule(GenericRuleStructure currentRS) throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException, InstantiationException {

        Rule rule = null;
        String rulename = currentRS.getRuleName();
        String text = currentRS.getRuleText();
        String[] subr = currentRS.m_stringRules;
        String[] dom = currentRS.m_stringDomains;
        String[] tmpRules = null;
        ArrayList rulesList = new ArrayList();

        for (int a = 0; a < subr.length; a++) {

            if (currentRS.m_ruleCategory == GenericRuleStructure.CDOM) {
                TemplateEnumeration enumeration = new TemplateEnumeration(subr[a]);
                ArrayList ruleList = new ArrayList();
                while (enumeration.hasMoreElements()) {
                    StringStructure ss = (StringStructure) enumeration.nextElement();
                    if (ss.stringRule != null) {
                        String s = ss.stringRule;
                        s = s.substring(1, s.length() - 1);
                        ruleList.add(s);
                    }
                } //end while

                tmpRules = new String[ruleList.size()];

                for (int i = 0; i < ruleList.size(); i++) {
                    String s = (String) ruleList.get(i);
                    tmpRules[i] = s;
                } //end for i
            } // end if

            if (tmpRules.length == 0) {
                //if there are no subrules
                rule = new Template(rulename, subr[a], new String[] {}, null, new Modifier[] {});
            } else {
                rule = new Template(rulename, subr[a], tmpRules, null, new Modifier[] {});
            }
            rulesList.add(rule);
        }
        Rule[] rules = new Rule[rulesList.size()];

        for (int b = 0; b < rulesList.size(); b++) {
            rules[b] = (Rule) rulesList.get(b);
        }
        rule = new CharacterDomain(rulename, dom, subr);
        return rule;
    }

    /*
     * VARIABLE MANAGEMENT
     */

    private Rule createSetVarRule(GenericRuleStructure currentRS) throws RuleException,
            VariableNotDefinedRuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;
        Object value = currentRS.m_value;

        rule = new SetVariable(SetVariable.SET_VARIABLE, getVarScope(), rulename, varname, value);
        return rule;
    }

    private Rule createAddToListRule(GenericRuleStructure currentRS) throws RuleException,
            VariableNotDefinedRuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;
        Object value = currentRS.m_value;

        rule = new SetVariable(SetVariable.ADD_TO_LIST, getVarScope(), rulename, varname, value);
        return rule;
    }

    private Rule createClearListRule(GenericRuleStructure currentRS) throws RuleException,
            VariableNotDefinedRuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;

        rule = new SetVariable(SetVariable.CLEAR_LIST, getVarScope(), rulename, varname, null);
        return rule;
    }

    private Rule createGetVarRule(GenericRuleStructure currentRS) throws RuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;
        rule = new GetVariable(getVarScope(), rulename, varname);
        return rule;
    }

    private Rule createForeachIterRule(GenericRuleStructure currentRS) throws RuleException,
            VariableNotDefinedRuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;
        String child = currentRS.getSubRule();

        IntegerModifier start = null, end = null;
        int nb = currentRS.m_modifiers.length;
        for (int i = 0; i < nb; i++) {
            GenericModifierStructure ms = currentRS.m_modifiers[i];
            if (ms.m_modifierCategory == GenericModifierStructure.START_MODIFIER) {
                start = ms.m_im;
            } else if (ms.m_modifierCategory == GenericModifierStructure.END_MODIFIER) {
                end = ms.m_im;
            }

            if ((start != null) && (end != null))
                break;
        } //end for

        rule = new ForEach(getVarScope(), rulename, varname, child, start, end);
        return rule;
    }

    private Rule createForeachEnumRule(GenericRuleStructure currentRS) throws RuleException,
            VariableNotDefinedRuleException {
        Rule rule = null;

        String rulename = currentRS.getRuleName();
        String varname = currentRS.m_varname;
        String child = currentRS.getSubRule();

        int duplication = GenericModifierStructure.NONE;
        GenericModifierStructure[] modifs = currentRS.m_modifiers;
        int nb = modifs.length;
        for (int i = 0; i < nb; i++) {
            GenericModifierStructure ms = modifs[i];
            if (ms.m_modifierCategory == GenericModifierStructure.UNIQUE_MODIFIER) {
                duplication = GenericModifierStructure.UNIQUE_MODIFIER;
                break;
            } else if (ms.m_modifierCategory == GenericModifierStructure.DUPLICATED_MODIFIER) {
                duplication = GenericModifierStructure.DUPLICATED_MODIFIER;
                break;
            }
        }

        rule = new ForEach(getVarScope(), rulename, varname, child, currentRS.m_listname,
                duplication);
        return rule;
    }

    private Rule createImportRule(GenericRuleStructure currentRS, File tplFile) {
        String beanClassFile = currentRS.getBeanClassFile();
        Rule rule = new ImportClause(beanClassFile, tplFile);
        return rule;
    }

    //restore m_ruletable from serialized file
    private void readTplObjFile(File tplObjFile) throws FileNotFoundException, IOException,
            RuleException, StreamCorruptedException, ClassNotFoundException {
        //open .tpl.o file
        ObjectInputStream input = new ObjectInputStream(new FileInputStream(tplObjFile));

        try {
            //read the variable list
            VariableScope varList = (VariableScope) input.readObject();
            setVarScope(varList);

            do {
                //read all the rules
                Rule rule = (Rule) input.readObject();
                String name = rule.m_ruleName;
                m_ruletable.put(name, rule);
            } while (true);
        } catch (EOFException ex) {
            input.close();
        }

        //set actual sub rules
        findActualSubRules();
    }

    //serialize m_ruletable into .tpl.o file
    private void writeSerialized(File tplObjFile) throws IOException {
        // check dir exist
        if (!tplObjFile.exists()) {
            tplObjFile = PathFile.createFile(tplObjFile.getAbsolutePath());
        }

        //create .tpl.o file
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(tplObjFile));

        try {
            //write the list of variables
            output.writeObject(getVarScope());

            //write all the rules
            Enumeration enumeration = m_ruletable.elements();
            while (enumeration.hasMoreElements()) {
                Rule rule = (Rule) enumeration.nextElement();

                if (rule instanceof Connector) {
                    Connector conn = (Connector) rule;
                    int t = 0;
                }

                output.writeObject(rule);
            } //end while

            output.close();
        } catch (IOException ex) {
            //delete file if not able to generate it completely
            output.close();
            tplObjFile.delete();
            throw ex; //propagate to the caller
        }
    }

    private void parseTplFile(URL templateURL, File tplObjFile) throws FileNotFoundException,
            InstantiationException, ClassNotFoundException, IllegalAccessException, RuleException,
            InvocationTargetException, NoSuchMethodException, IOException,
            VariableNotDefinedRuleException {

        Debug.trace("Enterring parseTplFile()");

        //create rules from a .tpl file
        GenericRuleStructure[] rules = createRules(templateURL);

        //if a syntax error is found in the .tpl file
        if (rules == null) {
            if (g_syntaxErrorRule != null) {
                Writer writer = new StringWriter();
                g_syntaxErrorRule.expand(writer, null);
                g_syntaxErrorRule = null; //re-init for a future use
                String msg = writer.toString();
                throw new InstantiationException(msg);
            }
            return;
        } //end if

        // Extract the file to the temp dir if jar
        File tplFile = null;
        
        String entryName = templateURL.getFile(); //JarUtil.getEntryName(tplURL);
        if (entryName != null) {
            tplFile = JarUtil.createTemporaryFile(entryName);
        } else {
            tplFile = new File(entryName);
        }

        //Create instances and keep them in the hash table
        for (int i = 0; i < rules.length; i++) {
            GenericRuleStructure currentRS = rules[i];
            Rule rule = null;
            String metaFieldString = null;
            switch (currentRS.m_ruleCategory) {
            case GenericRuleStructure.TEMPLATE:
                rule = createTemplateRule(currentRS);
                break;
            case GenericRuleStructure.CONNECTOR:
                rule = createConnectorRule(currentRS);
                break;
            case GenericRuleStructure.PROPERTY:
                rule = createPropertyRule(currentRS);
                break;
            case GenericRuleStructure.CDOM:
                rule = createCDomRule(currentRS);
                break;
            case GenericRuleStructure.USERFN:
                String repositoryFunctionName = currentRS.getRuleText();
                rule = createUserFunctionRule(GenericRuleStructure.USERFN, currentRS,
                        repositoryFunctionName, new Modifier[] {});
                break;
            case GenericRuleStructure.USERCONN:
                String repositoryFunctionName2 = currentRS.getRuleText();
                rule = createUserFunctionRule(GenericRuleStructure.USERCONN, currentRS,
                        repositoryFunctionName2, new Modifier[] {});
                break;
            case GenericRuleStructure.GROUP:
                rule = createGroupRule(currentRS);
                break;
            case GenericRuleStructure.CONDITION:
                rule = createConditionRule(currentRS);
                break;
            case GenericRuleStructure.SET_VAR:
                rule = createSetVarRule(currentRS);
                break;
            case GenericRuleStructure.GET_VAR:
                rule = createGetVarRule(currentRS);
                break;
            case GenericRuleStructure.FOREACH_ITER:
                rule = createForeachIterRule(currentRS);
                break;
            case GenericRuleStructure.FOREACH_ENUM:
                rule = createForeachEnumRule(currentRS);
                break;
            case GenericRuleStructure.ADD_TO_LIST:
                rule = createAddToListRule(currentRS);
                break;
            case GenericRuleStructure.CLEAR_LIST:
                rule = createClearListRule(currentRS);
                break;
            case GenericRuleStructure.IMPORT:
                rule = createImportRule(currentRS, tplFile);
                break;
            //TODO: add other kinds of rule HERE
            } //end switch

            if (rule != null) {
                String rulename = currentRS.getRuleName();
                if (rulename == null) {
                    rulename = "";
                } //null not a valid key
                m_ruletable.put(rulename, rule);
            }
        } //end for

        //find actual sub rules
        findActualSubRules();

        //try to serialize m_ruletable into .tpl.o file
        if (tplObjFile != null) {
            try {
                writeSerialized(tplObjFile);
            } catch (IOException ex) {
                //ignore, will read from the .tpl file at the next call
            }
        } //end if
    }

    private void fillRuletable(URL templateURL, File tplObjFile) throws InitializationException,
            InstantiationException, IllegalAccessException, InvocationTargetException,
            FileNotFoundException, ClassNotFoundException, NoSuchMethodException, RuleException,
            IOException, VariableNotDefinedRuleException {

        //clean-up previous elements in m_ruletable
        m_ruletable.clear();

        //get the Rules' constructors
        getRuleConstructors();

        //read from serialized .tpl.o? (otherwise parse .tpl)
        boolean readFromSerialized = true;
        String filename = templateURL.getFile();

        //if .tpl.o not found, then cannot read from it
        if (tplObjFile == null) {
            readFromSerialized = false;
        } else if (!tplObjFile.exists()) {
            readFromSerialized = false;
        } else { //if .tpl.o exists, but older than the .tpl file
            long objTime = tplObjFile.lastModified();
            String jarName = JarUtil.getJarAbsolutePath(filename);
            long tplTime = 0;
            
            if (jarName != null) {
                tplTime = new File(jarName).lastModified();
            } else {
            	File file = new File(filename);
                tplTime = file.lastModified();
            }

            if (objTime < tplTime) {
                readFromSerialized = false;
            }
        } //end if

        boolean readFromSerializedIsSuccessful = false;

        if (readFromSerialized) {
            try {
                readTplObjFile(tplObjFile);
                readFromSerializedIsSuccessful = true;
            } catch (Exception ex) {
                readFromSerializedIsSuccessful = false;
            }
        }

        if (!readFromSerializedIsSuccessful) {
            parseTplFile(templateURL, tplObjFile);
        }
    }

    private void findActualSubRules() throws RuleException {
        //for each rule of the Hashtable
        Enumeration enumeration = m_ruletable.elements();
        String currentRule = null;

        try {
            while (enumeration.hasMoreElements()) {
                Rule rule = (Rule) enumeration.nextElement();
                currentRule = rule.m_ruleName; //keep the name, if case of failure

                try {
                    rule.setActualSubRules(m_ruletable); //can throw StackOverflowError
                } catch (NullPointerException ex) {
                    RuleException ruleException = new RuleException(currentRule);
                    throw ruleException;
                }
            } //end while
        } catch (StackOverflowError err) {
            String msg = MessageFormat.format(ENDLESS_LOOP_PATTRN, new Object[] { currentRule });
            throw new RuleException(msg);
        }
    }

    protected static Template notFoundRule = new Template(null, RULE_NOT_FOUND_MSG);

    protected abstract Template getFileNotFoundRule();

    private static Template classNotFoundRule = null;
    private static Template g_syntaxErrorRule = null;

    //get the GENERIC_TEMPLATE_FILE's last modification date
    //if it's the first time that we execute this method
    //  then the rules' table must be initialized
    //if GENERIC_TEMPLATE_FILE has changed since the last time
    //  then the rules' table must be reinitialized
    //@return true if rules' table must initialized or reinitialized
    private long lastModified = 0; // last Modification of tpl-file
    private boolean initialized = false;

    protected boolean mustBeInitialized(URL templateURL) {
    	String template = templateURL.getFile(); 
        String jarFileName = JarUtil.getJarAbsolutePath(template);
        File myFile = null;
        if (jarFileName == null)
            myFile = new File(template);
        else
            myFile = new File(jarFileName);

        long myFileLastModified = myFile.lastModified();
        boolean mustBeInitialized = initialized;
        if ((lastModified == 0) | (lastModified != myFile.lastModified())) {
            lastModified = myFile.lastModified();
            Debug.trace("file changed: " + lastModified);
            mustBeInitialized = true;
        } else if (lastModified == myFile.lastModified()) {
            Debug.trace("file did not change: " + lastModified);
            mustBeInitialized = false;
        }
        //TODO: add the code HERE
        return mustBeInitialized;
    }

    //get the rule named 'rulename' in the template file named 'filename'
    protected boolean m_successfullyInitialized = false; //only used in getTableRule()

    protected void lazyInit(VariableScope varList, URL templateURL, boolean parseAlways)
            throws RuleException, NoSuchMethodException, InvocationTargetException,
            ClassNotFoundException, FileNotFoundException, InstantiationException,
            IllegalAccessException, GenericForwardEngineeringPlugin.InitializationException,
            IOException, VariableNotDefinedRuleException {

        Debug.trace("Enterring lazyInit()");
        String template = templateURL.getFile(); 

        if ((!m_successfullyInitialized) || (mustBeInitialized(templateURL))) {
            m_successfullyInitialized = false;
            File tplObjFile = null;
            String jarFileEntry = JarUtil.getEntryName(template);
            if (!parseAlways && jarFileEntry == null) {
                String templateURLFile = template.replaceAll("%20", " ");
                tplObjFile = new File(templateURLFile + ".o");
            } else if (!parseAlways) {
                String tplDir = getTemplateDirectory().replaceAll("%20", " ");
                tplObjFile = new File(tplDir + System.getProperty("file.separator") + jarFileEntry
                        + ".o");
            }

            fillRuletable(templateURL, tplObjFile);
            String jarName = JarUtil.getJarAbsolutePath(template);
            if (jarName == null)
                lastModified = new File(template).lastModified();
            else
                lastModified = new File(jarName).lastModified();
            m_successfullyInitialized = true;
        } //end if
    } //end lazyInit()

    //if rulename == null, return null (just to initialize)
    protected Rule getRule(String rulename, URL templateURL) {
        Rule rule = null;
        Template.resetMargin();

        try {
            g_syntaxErrorRule = null; //re-init to erase previous errors
            lazyInit(getVarScope(), templateURL, GenericForwardEngineeringPlugin.PARSE_IF_NECESSARY); //Parse the .tpl here

            if (g_syntaxErrorRule != null) {
                rule = g_syntaxErrorRule;
                rule.isError = true;
            } else {
                if (rulename == null) {
                    return null;
                }

                rule = (Rule) m_ruletable.get(rulename);
                if (rule == null) {
                    String msg = MessageFormat.format(RULE_NOT_FOUND_PATTRN,
                            new Object[] { rulename });
                    rule = new Template(null, msg);
                    rule.isError = true;
                }
            } //end if

            //ignore those exceptions (if they occur, rule will be 'null')
        } catch (InvocationTargetException ex) {
            Throwable th = ex.getTargetException();
            String msg = th.getMessage();
            if (msg == null) {
                msg = th.toString();
            }
            classNotFoundRule = new Template(null, msg);
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (InitializationException ex) {
            classNotFoundRule = new Template(null, ex.getMessage());
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (InstantiationException ex) {
            classNotFoundRule = new Template(null, ex.toString());
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (IllegalAccessException ex) {
            classNotFoundRule = new Template(null, ex.toString());
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (NoSuchMethodException ex) {
            classNotFoundRule = new Template(null, ex.toString());
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (ClassNotFoundException ex) {
            classNotFoundRule = new Template(null, ex.toString());
            classNotFoundRule.isError = true;
            rule = classNotFoundRule;
            g_syntaxErrorRule = classNotFoundRule;
        } catch (IOException ex) {
            rule = getFileNotFoundRule();
            if (rule != null) {
                rule.isError = true;
            }
        } catch (RuleException ex) {
            String msg = ex.getMessage();
            if (msg == null) {
                msg = ex.toString();
            }
            rule = new Template(null, msg);
            rule.isError = true;
        } catch (VariableNotDefinedRuleException ex) {
            String msg = ex.getMessage();
            if (msg == null) {
                msg = ex.toString();
            }
            rule = new Template(null, msg);
            rule.isError = true;
        } catch (RuntimeException ex) {
            //catch Runtime exceptions such as NullPointerException, IllegalArgumentException, ..
            String msg = ex.toString();

            rule = new Template(null, msg);
            rule.isError = true;
        }

        //if rule not found
        if (rule == null) {
            rule = notFoundRule;
            rule.isError = true;
        }

        return rule;
    }

    protected Rule getProjectRule() {
        Rule rule = getRule("projectEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getDataModelRule() {
        Rule rule = getRule("datamodelEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getTableRule() {
        Rule rule = getRule("tableEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getViewRule() {
        Rule rule = getRule("viewEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getColumnRule() {
        Rule rule = getRule("columnEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getIndexRule() {
        Rule rule = getRule("indexEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getPrimaryUniqueRule() {
        Rule rule = getRule("primaryuniqueEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getCheckRule() {
        Rule rule = getRule("checkEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    protected Rule getTriggerRule() {
        Rule rule = getRule("triggerEntryPoint", getTemplateURL()); //NOT LOCALIZABLE
        return rule;
    }

    //INNER CLASSES
    public static class InitializationException extends Exception {
        InitializationException(String msg) {
            super(msg);
        }
    } //end of InitializationException
}
