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

import java.awt.event.ActionEvent;
import java.io.*;
import java.util.*;
import java.text.MessageFormat;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.plugins.*;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;
import org.modelsphere.jack.srtool.forward.exceptions.*;
import org.modelsphere.jack.srtool.international.LocaleMgr;

/**
 * A Rule variable is a variable on which the operation expand() can be performed. Rule is an
 * abstract class, i.e. a Rule variable is not directly an instance of Rule, but rather an instance
 * of one of subclasses of Rule. The subclasses of Rule are: CharacterDomain, Property, Connector,
 * Group and Template.
 * 
 * IMPORTANT TODO : expand(writer, object, options) { this(writer, object, null, null, options); }
 * 
 * expand(writer, object1, object2, metafields[], options) { if (object1 == null) { throw
 * IllegalArguemntException() }
 * 
 * depending of metafields, get value from object1 or object2 }
 */
public abstract class Rule implements Plugin, Serializable {
    //global variables
    private static final String PROCESS_TIME0 = LocaleMgr.message.getString("processTime0");

    //Further extension: replace array by array of Stack for restoring
    //previous value
    public String m_ruleName;
    public boolean isError = false;

    //modifiers (are public because can be used in UserDefinedField)
    public DomainModifier domainModifier = null;
    public ExternModifier externModifier = null;
    public FileModifier fileModifier = null;
    public NullModifier nullModifier = null;
    public PrefixModifier prefixModifier = null;
    public SeparatorModifier separatorModifier = null;
    public SuffixModifier suffixModifier = null;
    public Vector variableVector = null;
    protected BooleanModifier m_booleanModifier = null;
    protected CaseModifier m_caseModifier = null;

    //keep which variable(s) has changed
    int variableModification = 0;

    //
    //  CONSTRUCTORS
    //

    public Rule() {
    } //Parameter-less constructor required by jack.io.Plugins

    public Rule(String name) {
        m_ruleName = name;
    }

    //TODO: check for multiple modifiers (several Ifs, several Prefix, ect.)
    Rule(String name, Modifier[] someModifiers) {
        this(name);
        for (int i = 0; i < someModifiers.length; i++) {
            Modifier modifier = someModifiers[i];

            if (modifier instanceof PrefixModifier) {
                prefixModifier = (PrefixModifier) modifier;
            } else if (modifier instanceof SuffixModifier) {
                suffixModifier = (SuffixModifier) modifier;
            } else if (modifier instanceof SeparatorModifier) {
                separatorModifier = (SeparatorModifier) modifier;
            } else if (modifier instanceof NullModifier) {
                nullModifier = (NullModifier) modifier;
            } else if (modifier instanceof DomainModifier) {
                domainModifier = (DomainModifier) modifier;
            } else if (modifier instanceof FileModifier) {
                fileModifier = (FileModifier) modifier;
            } else if (modifier instanceof ExternModifier) {
                externModifier = (ExternModifier) modifier;
            } else if (modifier instanceof BooleanModifier) {
                m_booleanModifier = (BooleanModifier) modifier;
            } else if (modifier instanceof CaseModifier) {
                m_caseModifier = (CaseModifier) modifier;
            } else if (modifier instanceof VariableModifier) {
                if (variableVector == null) {
                    variableVector = new Vector();
                }

                variableVector.addElement(modifier);
            }
        }
    }

    //////////////////
    //OVERRIDES Object
    public String toString() {
        return m_ruleName;
    }

    //
    //////////////////

    //////////////////
    //OVERRIDES Plugin
    private static final PluginSignature g_signature = new PluginSignature("Rule",
            "$Revision: 5 $", "$Date: 2009/04/14 14:00p $", 100); //NOT LOCALIZABLE

    public PluginSignature getSignature() {
        return g_signature;
    }

    public final boolean isSupportedObject(Object obj) {
        return true;
    }

    //
    //////////////////

    private static final boolean EDITION_CODES_NOT_ALLOWED = false;

    protected void setActualModifiers(Hashtable table) throws RuleException {
        try {
            if (prefixModifier != null) {
                prefixModifier.setActualSubRules(table, EDITION_CODES_NOT_ALLOWED);
            }

            if (suffixModifier != null) {
                suffixModifier.setActualSubRules(table, EDITION_CODES_NOT_ALLOWED);
            }

            if (separatorModifier != null) {
                separatorModifier.setActualSubRules(table, EDITION_CODES_NOT_ALLOWED);
            }

            if (nullModifier != null) {
                nullModifier.setActualSubRules(table, EDITION_CODES_NOT_ALLOWED);
            }

            if (fileModifier != null) {
                fileModifier.setActualSubRules(table, EDITION_CODES_NOT_ALLOWED);
            }
        } catch (RuleException ex) {
            String old_msg = ex.getMessage();
            String msg = ErrorInRuleException.buildMessage(m_ruleName, old_msg);
            throw new ErrorInRuleException(msg);
        }
    } //end setActualModifiers()

    private void setVariables(VariableScope varList, String[] parameters) throws IOException,
            RuleException, VariableNotDefinedRuleException {
        int nb = parameters.length;

        for (int i = 0; i < nb; i++) {
            String parameter = parameters[i];
            int index = parameter.indexOf('=');
            if (index == -1) {
                String msg = NoEqualCharacterRuleException.buildMessage(i, parameter);
                throw new NoEqualCharacterRuleException(msg);
            } //end if

            //get varname and value
            String varname = parameter.substring(0, index);
            String value = parameter.substring(index + 1);

            //set varname to value
            boolean isExtern = false;
            VariableDecl.declare(varList, varname, VariableDecl.STRING, "\"" + value + "\"",
                    isExtern); //NOT LOCALIZABLE
            Template templ = new Template(null, value, new Rule[] {}, null, new Modifier[] {});
            SetVariable variable = new SetVariable(SetVariable.SET_VARIABLE, varList, null,
                    varname, templ);
            Writer writer = null;
            Serializable object = null;
            RuleOptions options = null;
            variable.expand(writer, object, options);
        } //end for
    } //end setVariables

    public boolean expand(Writer output, Serializable object) throws IOException, RuleException {
        Rule.RuleOptions options = null;
        boolean expanded = expand(output, object, options);
        return expanded;
    }

    /*
     * Expand in an outout stream. Output stream can be a FileWriter, a StringWriter, a PrintWriter,
     * etc. Return false if no characters is expanded
     */
    public abstract boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException;

    /*
     * @param ouput : output stream
     * 
     * @param object : object to process
     * 
     * @param parameters : array of String, whose format is 'var0=hello', where var0 is a variable
     * to set and hello is the init value of the variable.
     */
    public boolean expand(Writer output, Serializable object, VariableScope varList,
            RuleOptions options, String[] parameters) throws IOException, RuleException {
        boolean expanded;

        try {
            setVariables(varList, parameters);
            expanded = expand(output, object, options);
        } catch (VariableNotDefinedRuleException ex) {
            throw new RuleException(ex.getMessage());
        }

        return expanded;
    } //end expand()

    /*
     * This function must be called at the end of each expand() function in all subclasses
     */
    public static long cumulative = 0;

    public void terminate(Writer writer, RuleOptions options) throws RuleException {
        //Clear all variables set by the current rule
        variableVector = null;

        //Cancel everything if the user has decided to stop the operation
        if (options != null) {
            Controller controller = options.m_controller;
            if (controller != null) {

                feedback(writer, controller); //gives a feedback to the user

                boolean can_continue = controller.checkPoint();
                if (!can_continue) {
                    throw new AbortRuleException(controller.getAbortedString());
                }
            }
        } //end if
    } //end terminate()

    //Gives a feedback to the user at each two seconds
    private static long g_currentTime = 0;
    private static long g_lastOutputTime = 0;

    public static void resetTime() {
        g_currentTime = 0;
        g_lastOutputTime = 0;
    }

    private void feedback(Writer writer, Controller controller) {
        Long counter = controller.getCounter();
        if (counter == null)
            return;

        g_currentTime = System.currentTimeMillis();
        long delay = g_currentTime - g_lastOutputTime;

        if (delay > 2000) {
            if (g_lastOutputTime != 0) {
                counter = new Long(counter.longValue() + delay);
                controller.setCounter(counter);
                String message = MessageFormat.format(PROCESS_TIME0, new Object[] { new Long(
                        counter.longValue() / 1000) });
                controller.println(message);
            }

            g_lastOutputTime = g_currentTime;
        }
    } //end feedback()

    public static MetaField getMetaField(DbObject dbo, String name) {
        MetaField[] allMetaFields = dbo.getMetaClass().getAllMetaFields();
        for (int i = 0; i < allMetaFields.length; i++) {
            MetaField metaField = allMetaFields[i];
            if (name.equals(metaField.getJField().getName()))
                return metaField;
        }
        return null;
    }

    //if a kind of rule (template, connector, etc.) supports string subrules,
    //then set actual actual subrules from its string rules; (this method is
    //then overriden in the given kind of rule).
    //otherwise, do nothing.
    public void setActualSubRules(Hashtable table) throws RuleException {
        setActualModifiers(table);
    }

    //if propertyName is in excludeList, or if className is in excludeList
    //className : like 'Table', 'Column', 'Class', ..
    //propertyName : like 'Table.Name', 'Column.Alias', ..
    protected boolean verifyExclusion(String className, String propertyName, RuleOptions options) {
        boolean excluded = false;

        if (options == null) {
            return false;
        }

        ArrayList excludeList = options.getExcludeList();

        if (excludeList != null) {
            if (excludeList.contains(className)) {
                excluded = true;
            } else if (excludeList.contains(propertyName)) {
                excluded = true;
            }
        } //end if

        return excluded;
    }

    protected boolean verifyExclusion(Serializable obj, RuleOptions options) {
        boolean excluded = false;

        if (options == null) {
            return false;
        }

        ArrayList excludeList = options.getExcludeList();

        if (obj != null) {
            if (obj instanceof DbObject) {
                DbObject dbObj = (DbObject) obj;
                MetaClass metaClass = dbObj.getMetaClass();
                String className = metaClass.getGUIName();
                if (excludeList != null) {
                    if (excludeList.contains(className)) {
                        excluded = true;
                    }
                }
            }
        } //end if

        return excluded;
    }

    // plugin implementation
    public final String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    public final Class[] getSupportedClasses() {
        return null;
    }

    public final void execute(ActionEvent actEvent) throws Exception {
    }

    //
    // INNER CLASS
    //
    public static class RuleOptions {

        //Note : package visibility to be accessed by other kinds of rule
        DbObject m_refObject; //A reference object
        MetaField[] m_metafields; //list of metafields
        Controller m_controller; //controller to stop the processing

        //Used by Property rules, ignored by others
        //If a property's name is contained in the excludeList, then doesn't generate the property
        private ArrayList m_propertyExcludeList;

        public RuleOptions(DbObject refObject, MetaField[] metafields, Controller controller,
                ArrayList excludeList) {
            m_refObject = refObject;
            m_metafields = metafields;
            m_controller = controller;
            m_propertyExcludeList = excludeList;
        }

        ArrayList getExcludeList() {
            return m_propertyExcludeList;
        }

        boolean isExcluded(String name) {
            if (m_propertyExcludeList != null) {
                if (m_propertyExcludeList.contains(name)) {
                    return true;
                }
            } //end if
            return false;
        } //end isExcluded
    } //end RuleOptions
    
    public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }
} //end Rule

class InexistantFieldException extends Exception {
    public InexistantFieldException() {
        super();
    }

    public InexistantFieldException(String s) {
        super(s);
    }
}
