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
import java.util.StringTokenizer;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.baseDb.meta.MetaRelation;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.srtool.forward.exceptions.DomainNotFoundRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.InvalidMetafieldRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.NotADomainRuleException;

/**
 * Optional Keywords: PrefixModifier, SuffixModifier, NullModifier, DomainModifier
 * 
 */
public final class Property extends Rule {
    private String propertyName;
    private String domain = null;
    private static String kDot = ".";
    private static String kEmpty = "";

    // CONSTRUCTORS
    public Property(String ruleName, String aPropertyName) {
        super(ruleName);
        propertyName = aPropertyName;
    }

    public Property(String ruleName, String aPropertyName, Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        propertyName = aPropertyName;
    }

    // these constructors allow compiler to validate correctness of the
    // parameters
    public Property(String ruleName, MetaField metaField) {
        super(ruleName);
        propertyName = metaField.getJName();
    }

    public Property(String ruleName, MetaField metaField, Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        propertyName = metaField.getJName();
    }

    public Property(String ruleName, MetaField metaField, String aDomain, Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        domain = aDomain; // can be null
        propertyName = metaField.getJName();
    }

    public void setActualSubRules(Hashtable table) throws RuleException {
        try {
            // change dom with its actual rule
            if (domain != null) {
                CharacterDomain domainRule = (CharacterDomain) table.get(domain);
                domainModifier = new DomainModifier(domainRule);
            }

            // change modifiers with their actual rules
            super.setActualModifiers(table);
        } catch (ClassCastException ex) {
            String msg = NotADomainRuleException.buildMessage(m_ruleName);
            throw new NotADomainRuleException(msg);
        }
    }

    // METHODS
    public String getPropertyName() {
        return propertyName;
    }

    public String getDomain() {
        return domain;
    }

    public Object getPropertyValue(DbObject dbo) throws DbException {
        Object value = dbo;
        StringTokenizer st = new StringTokenizer(propertyName, kDot);
        while (st.hasMoreTokens()) {
            dbo = (DbObject) value;
            value = dbo.get(getMetaField(dbo, st.nextToken()));
            if (value == null)
                break;
        }
        return value;
    }

    // replace '\n' by '$n;' to be sensitive to margins.
    private static final String EOL = "\n"; // NOT LOCALIZABLE

    private void expandWithinMargins(Writer output, String strValue) throws IOException {

        StringTokenizer st = new StringTokenizer(strValue, EOL);
        while (st.hasMoreTokens()) {
            String line = st.nextToken();
            output.write(line);

            if (st.hasMoreElements()) {
                output.write("$n;"); // NOT LOCALIZABLE
            }
        } // end while
    } // end expandWithinMargins

    // get actual string : replace $ by $$
    private String getActualString(Object value) {
        String actualString = "";
        String s = value.toString();

        StringTokenizer st = new StringTokenizer(s, "$", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            if (token.equals("$")) {
                actualString += "$$";
            } else {
                actualString += token;
            } // end if
        } // end while

        return actualString;
    }

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws /* DbException, */IOException, RuleException {
        boolean expanded = false;

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

        DbObject currentObject = (DbObject) object;

        // special case if UPPER or LOWER
        if (m_caseModifier != null) {
            if (m_caseModifier.getCasePolicy() != CaseModifier.UNCHANGE) {
                int casePolicy = m_caseModifier.getCasePolicy(); // keep case
                // policy
                m_caseModifier.resetCasePolicy(); // to avoid endless-looping in
                // recursive call
                StringWriter swriter = new StringWriter();
                expanded = expand(swriter, object, options); // recursive call
                String result = swriter.toString();
                result = result.toUpperCase();
                if (casePolicy == CaseModifier.UPPER) {
                    result = result.toUpperCase();
                } else if (casePolicy == CaseModifier.LOWER) {
                    result = result.toLowerCase();
                } // end if

                output.write(result);
                super.terminate(output, options);
                return expanded;
            } // end if
        } // end if

        StringTokenizer st = new StringTokenizer(propertyName, kDot);

        try {
            while (st.hasMoreTokens()) {
                String property = st.nextToken();

                if (st.hasMoreTokens()) {
                    MetaRelation metaRelation = (MetaRelation) getMetaField(currentObject, property);
                    currentObject = (DbObject) currentObject.get(metaRelation);

                    if (currentObject == null) {
                        if (nullModifier != null) {
                            expanded |= nullModifier.expand(output, object, options);
                        }
                        break;
                    }
                } else { // last token
                    String strValue = null;
                    MetaField metaField = getMetaField(currentObject, property);
                    MetaClass metaClass = currentObject.getMetaClass();
                    String classGuiName = metaClass.getGUIName();

                    // build propert name : guiName + metafield.guiname;
                    String propertyName = classGuiName + "." + metaField.getGUIName();
                    boolean excluded = verifyExclusion(classGuiName, propertyName, options);

                    Object value = null;
                    if (!excluded) {
                        value = currentObject.get(metaField);
                    }

                    if (value == null) {
                        strValue = kEmpty;
                    } else {
                        if (value instanceof IntDomain) {
                            IntDomain dom = (IntDomain) value;
                            strValue = "" + dom.getValue(); // get numeric value
                            // ('0', '1', etc.)
                        } else {
                            strValue = getActualString(value);
                        } // end if
                    } // end if

                    if ((nullModifier != null) && (strValue.equals(kEmpty))) {
                        expanded |= nullModifier.expand(output, object, options);
                    }

                    if ((prefixModifier != null) && (!strValue.equals(kEmpty))) {
                        expanded |= prefixModifier.expand(output, object, options);
                    }

                    if (domainModifier != null) {
                        CharacterDomain cdom = domainModifier.getDomain();
                        if (cdom == null) {
                            String msg = DomainNotFoundRuleException.buildMessage(m_ruleName);
                            throw new DomainNotFoundRuleException(msg);
                        }
                        expanded |= cdom.expand(output, object, options, strValue);
                    } else {
                        if (!strValue.equals(kEmpty)) {
                            expandWithinMargins(output, strValue);
                            expanded = true;
                        }
                    } // end if domain == null

                    if ((suffixModifier != null) && (!strValue.equals(kEmpty))) {
                        expanded |= suffixModifier.expand(output, object, options);
                    }
                } // end if last token
            } // end while
        } catch (DbException ex) {
            String msg = ex.toString();
            throw new RuleException(msg);
        } catch (RuntimeException ex) {
            DbObject dbObject = (DbObject) object;
            String objName = (object == null ? null : dbObject.getMetaClass().getGUIName());
            String msg = InvalidMetafieldRuleException.buildMessage(propertyName, objName);
            throw new InvalidMetafieldRuleException(msg);
        }

        super.terminate(output, options);

        return expanded;
    }
}
