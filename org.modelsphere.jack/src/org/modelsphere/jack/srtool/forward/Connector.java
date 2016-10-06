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

import java.io.*;
import java.util.Hashtable;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.srtool.forward.exceptions.InvalidConnectorRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.NoChildRuleException;

/**
 * Optional Keywords: WhenModifier, PrefixModifier, SeparatorModifier, SuffixModifier, NullModifier
 * 
 */
public class Connector extends Rule {
    transient MetaRelationship m_connector = null;
    String sConnector = null; // use it if m_connector == null
    Rule childRule = null;
    Rule oneChildRule = null;
    String m_childStrRule = null; // use it if childRule == null
    String m_oneChildStrRule = null; // may be null
    String m_whenCond = null; // a condition to interprete
    transient MetaClass m_childrenMetaClass = null;
    String sChildrenMetaClass = null; // use it if m_childrenMetaClass == null

    public Connector() {
    } // parameter-less constructor required by jack.io.Plugins

    protected Connector(String ruleName, String aSConnector, String aStringRule,
            Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        sConnector = aSConnector;
        m_childStrRule = aStringRule;
    } // end Connector()

    public Connector(String ruleName, MetaRelationship aConnector, Rule aChildRule,
            MetaClass childrenMetaClass, Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        m_connector = aConnector;
        m_childrenMetaClass = childrenMetaClass; // doesn't work
        childRule = aChildRule;

        // store m_connector because it is transient
        sConnector = m_connector.getJName();

        // store m_childrenMetaClass because it is transient
        if (m_childrenMetaClass != null) {
            sChildrenMetaClass = m_childrenMetaClass.getJClass().getName();
        }
    } // end Connector()

    // 'official' constructor
    // new param: childrenMetaClass
    public Connector(String ruleName, MetaRelationship aConnector, String childRule,
            String[] optionalRules, MetaClass childrenMetaClass, Modifier[] someModifiers) {
        this(ruleName, aConnector, (Rule) null, childrenMetaClass, someModifiers);
        int nbRules = optionalRules.length;
        m_childStrRule = childRule;
        m_oneChildStrRule = optionalRules[0];
        m_whenCond = (nbRules > 1) ? optionalRules[1] : null;
        m_childrenMetaClass = childrenMetaClass;

        // store m_childrenMetaClass because it is transient
        if (m_childrenMetaClass != null) {
            sChildrenMetaClass = m_childrenMetaClass.getJClass().getName();
        }
    } // end Connector()

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {
        // find childRule from stringRule
        childRule = (Rule) table.get(m_childStrRule);
        if (m_oneChildStrRule != null) {
            oneChildRule = (Rule) table.get(m_oneChildStrRule);
        }

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    }

    public boolean expandChild(Writer output, DbObject parent, DbObject child, int nbChildren,
            boolean state[], // for {prefixPrinted,
            // atLeastOneChildPrinted}
            RuleOptions options) throws DbException, IOException, RuleException {
        boolean expanded = false;
        boolean prefixPrinted = state[0];
        boolean atLeastOneChildPrinted = state[1];

        // If no 'WHEN MODIFIER' defined, or one defined that returns true
        // if ((m_whenClause == null) ||
        boolean condition = true;
        if (m_booleanModifier != null) {
            condition = m_booleanModifier.evaluate(child);
        } // end if

        if (condition) {
            // Make sure a child has been defined
            if (childRule == null) {
                String msg = NoChildRuleException.buildMessage(m_ruleName, m_childStrRule);
                throw new NoChildRuleException(msg);
            }

            // create a string buffer
            StringWriter sw = new StringWriter();

            // take CHILD rule, except if there is one element and ONECHILD rule
            // is defined
            Rule ruleToUse = childRule;
            if ((nbChildren == 1) && (oneChildRule != null)) {
                ruleToUse = oneChildRule;
            }

            // expand the child in the string buffer
            boolean childExpanded = ruleToUse.expand(sw, child, options);

            if (childExpanded) {
                // if there is a prefix to print
                if ((prefixModifier != null) && (!prefixPrinted)) {
                    expanded |= prefixModifier.expand(output, parent, options);
                    prefixPrinted = true;
                } else { // prefix already printed, or no prefix
                    if (atLeastOneChildPrinted) {
                        if (separatorModifier != null) {
                            // expand the SEParator right before to print the
                            // child
                            expanded |= separatorModifier.expand(output, parent, options);
                        } // end if
                    } // end if
                } // end if

                // copy the string buffer into the output stream
                output.write(sw.toString());
            } // end if

            expanded |= childExpanded;
            if (childExpanded) {
                atLeastOneChildPrinted = true;
            }
        } // end if

        state[0] = prefixPrinted;
        state[1] = atLeastOneChildPrinted;
        return expanded;
    }

    private boolean expandMetaRelation1(Writer output, DbObject object,
            MetaRelation1 metaRelation1, boolean state[], MetaClass childrenMetaClass,
            RuleOptions options) throws DbException, IOException, RuleException {

        boolean expanded = false;
        Object child;
        if (childrenMetaClass != null) {
            if (metaRelation1 == DbObject.fComposite) {
                child = object.getCompositeOfType(childrenMetaClass);
            } else {
                child = object.get(metaRelation1);
                if (child instanceof DbObject) {
                    DbObject dbChild = (DbObject) child;
                    MetaClass mc = dbChild.getMetaClass();
                    if (mc != childrenMetaClass) {
                        child = null;
                    }
                }
            }
        } else {
            child = object.get(metaRelation1);
        } // end if

        if (child != null) {
            if (child instanceof DbObject) {
                DbObject dbChild = (DbObject) child;
                MetaClass metaClass = dbChild.getMetaClass();
                String className = metaClass.getGUIName();
                boolean excluded = false;

                // check if child's class is excluded
                if (options != null) {
                    excluded = options.isExcluded(className);

                    // if not, check if parent's connection is excluded
                    if (!excluded) {
                        metaClass = object.getMetaClass();
                        String parentClassName = metaClass.getGUIName();
                        String connectionName = parentClassName + "." + metaRelation1.getGUIName();
                        excluded = options.isExcluded(connectionName);
                    } // end if
                } // end if

                if (!excluded) {
                    expanded |= expandChild(output, object, dbChild, 1, state, options);
                } // end if
            } // end if
        } // end if

        // if a SUF modifier has to be expanded
        if ((expanded) && (suffixModifier != null)) {
            suffixModifier.expand(output, object, options);
        }

        // if a NULL modifier has to be expanded
        if ((!expanded) && (nullModifier != null)) {
            expanded |= nullModifier.expand(output, object, options);
        }

        return expanded;
    } // end expandMetaRelation1

    // add childrenMetaClass to filter only children of that class
    // if null, scan each child
    protected boolean expandMetaRelationN(Writer output, DbObject object,
            MetaRelationship metaRelation, boolean state[], MetaClass childrenMetaClass,
            RuleOptions options) throws DbException, IOException, RuleException {

        boolean expanded = false;
        DbEnumeration dbEnumChildren = null;
        DbRelationN relationN = (DbRelationN) object.get(metaRelation);
        if (childrenMetaClass == null) {
            dbEnumChildren = relationN.elements();
        } else {
            dbEnumChildren = relationN.elements(childrenMetaClass);
        }

        // a first pass to find out the number of children
        int nbChildren = 0;
        try {
            while (dbEnumChildren.hasMoreElements()) {
                dbEnumChildren.nextElement();
                nbChildren++;

                // Cancel everything if the user has decided to stop the
                // operation
                if (options != null) {
                    Controller controller = options.m_controller;
                    if (controller != null) {
                        boolean can_continue = controller.checkPoint();
                        if (!can_continue) {
                            throw new RuleException(controller.getAbortedString());
                        }
                    }
                } // end if
            } // end while
        } finally {
            dbEnumChildren.close();
        }

        // cannot rollback to the first element, so recreate the enumeration
        if (childrenMetaClass == null) {
            dbEnumChildren = relationN.elements();
        } else {
            dbEnumChildren = relationN.elements(childrenMetaClass);
        }

        try {
            while (dbEnumChildren.hasMoreElements()) {
                DbObject child = (DbObject) dbEnumChildren.nextElement();
                MetaClass metaClass = child.getMetaClass();
                String className = metaClass.getGUIName();
                boolean excluded = false;

                // check if child's class is excluded
                if (options != null) {
                    excluded = options.isExcluded(className);

                    // if not, check if parent's connection is excluded
                    if (!excluded) {
                        metaClass = object.getMetaClass();
                        String parentClassName = metaClass.getGUIName();
                        String connectionName = parentClassName + "." + metaRelation.getGUIName();
                        excluded = options.isExcluded(connectionName);
                    } // end if
                } // end if

                if (!excluded) {
                    expanded |= expandChild(output, object, child, nbChildren, state, options);
                } // end if
            } // end while
        } finally {
            dbEnumChildren.close();
        }

        boolean atLeastOneChildPrinted = state[1];

        if ((atLeastOneChildPrinted) && (suffixModifier != null)) {
            expanded |= suffixModifier.expand(output, object, options);
        } else if ((!atLeastOneChildPrinted) && (nullModifier != null)) {
            expanded |= nullModifier.expand(output, object, options);
        }

        return expanded;
    } // end expandMetaRelationN

    private boolean expandMetaChoice(Writer output, DbObject object, MetaChoice choice,
            boolean state[], MetaClass childrenMetaClass, RuleOptions options) throws DbException,
            IOException, RuleException {

        boolean expanded = false;
        Object child = object.get(choice);

        if (child instanceof DbObject) {
            DbObject dbChild = (DbObject) child;
            MetaClass metaClass = dbChild.getMetaClass();
            String className = metaClass.getGUIName();
            boolean excluded = false;

            // check if child's class is excluded
            if (options != null) {
                excluded = options.isExcluded(className);

                // if not, check if parent's connection is excluded
                if (!excluded) {
                    MetaClass metaClass2 = object.getMetaClass();
                    String parentClassName = metaClass2.getGUIName();
                    String connectionName = parentClassName + "." + choice.getGUIName();
                    excluded = options.isExcluded(connectionName);
                } // end if
            } // end if

            if (!excluded) {
                if (childrenMetaClass.isAssignableFrom(metaClass)) {
                    expanded |= expandChild(output, object, dbChild, 1, state, options);
                } // end if
            } // end if
        } // end if

        if ((!expanded) && (nullModifier != null)) {
            expanded |= nullModifier.expand(output, object, options);
        } // end if

        return expanded;
    } // end expandMetaChoice

    private MetaRelationship getMetaRelation(DbObject object) {
        MetaRelationship metaRelation = m_connector;

        // get metaRelation from its string representation
        if (metaRelation == null) {
            m_connector = (MetaRelationship) getMetaField(object, sConnector);
            metaRelation = m_connector;
        }

        return metaRelation;
    }

    private MetaClass getChildrenMetaClass() {
        MetaClass childrenMetaClass = m_childrenMetaClass;

        if (childrenMetaClass == null) {
            m_childrenMetaClass = MetaClass.find(sChildrenMetaClass);
            childrenMetaClass = m_childrenMetaClass;
        }

        return childrenMetaClass;
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;
        boolean prefixPrinted = false;
        boolean atLeastOneChildPrinted = false;

        if (object == null) {
            if (Debug.isDebug()) {
                throw new NullPointerException(); // disclose programming error
                // in debug..
            } else {
                return false;
            }
        } // end if

        if (!(object instanceof DbObject)) {
            return false;
        } // end if

        DbObject dbObject = (DbObject) object;
        MetaRelationship metaRelation = getMetaRelation(dbObject);
        MetaClass childrenMetaClass = getChildrenMetaClass();

        try {
            // get metaRelation from its string representation
            if (metaRelation == null)
                metaRelation = (MetaRelationship) getMetaField(dbObject, sConnector);

            boolean state[] = { prefixPrinted, atLeastOneChildPrinted };

            if (metaRelation instanceof MetaRelation1) {
                MetaRelation1 metaRelation1 = (MetaRelation1) metaRelation;
                expanded |= expandMetaRelation1(output, dbObject, metaRelation1, state,
                        childrenMetaClass, options);
            } else if (metaRelation instanceof MetaRelationN) {
                expanded |= expandMetaRelationN(output, dbObject, metaRelation, state,
                        childrenMetaClass, options);
            } else if (metaRelation instanceof MetaChoice) {
                MetaChoice choice = (MetaChoice) metaRelation;
                expanded |= expandMetaChoice(output, dbObject, choice, state, childrenMetaClass,
                        options);
            } else {
                // TODO: throw 'meta-relationship not supported'
            }

            super.terminate(output, options);
        } catch (DbException ex) {
            String msg = ex.getMessage();
            throw new RuleException(msg);
        } catch (RuntimeException ex) {
            String conn = m_connector.getGUIName();
            String objectKind = dbObject.getMetaClass().getGUIName();
            String msg = InvalidConnectorRuleException.buildMessage(m_ruleName, conn, objectKind);
            throw new InvalidConnectorRuleException(msg);
        }

        return expanded;
    }

    // INNER CLASS
    public static abstract class UserConnector extends Rule {

        // oneChildRule can be null
        public abstract Connector createInstance(String childRule, String oneChildRule);

        public boolean expand(Writer output, Serializable object, Rule.RuleOptions options)
                throws IOException, RuleException {
            boolean expanded = false;
            Connector conn = getConnector();
            expanded = conn.expand(output, object);
            return expanded;
        } // end expand()

        // //////////////////////////////
        // internal connector (property)
        private Connector m_conn = null;

        protected void setConnector(Connector conn) {
            m_conn = conn;
        } // end setConnector()

        protected Connector getConnector() {
            return m_conn;
        }
        //
        // ////////////////////////////////

    } // end UserConnector

    // UNIT TESTING
    /*
     * //TODO make it independant from sms private static void doTest() { String ruleName =
     * "ruleName"; //NOT LOCALIZABLE, unit test MetaRelationship rel =
     * org.modelsphere.sms.oo.java.db.DbJVParameter.fType; String aStringRule = "";
     * 
     * Connector conn = new Connector(ruleName, rel, aStringRule, new String[] {}, null, new
     * Modifier[] {}); } //end doTest()
     * 
     * 
     * private static final void main(String[] args) throws IOException { doTest();
     * org.modelsphere.jack.debug.Debug.trace("PRESS ENTER TO QUIT."); byte[] buffer = new
     * byte[256]; System.in.read(buffer, 0, 255); }
     */
} // end Connector()
