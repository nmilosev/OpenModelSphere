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
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Locale;

import org.modelsphere.jack.srtool.forward.exceptions.AlreadyDefinedVariableRuleException;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public final class VariableDecl implements Serializable {
    public static final int BOOLEAN = 1;
    public static final int FLOAT = 2;
    public static final int INTEGER = 3;
    public static final int STRING = 4;
    public static final int LIST = 5;
    public static final String NOT_INITIALIZED_PATTERN = LocaleMgr.message
            .getString("VariableNotInitialized2"); // "({0}\'s value not initialized)";

    // public static void cleanUp(HashMap varList) {
    // varList.clear();
    // }
    public static void declare(VariableScope varList, String varname, int type, Object value,
            boolean isExtern) throws RuleException {
        declare(varList, varname, type, value, isExtern, null);
    }

    public static void declare(VariableScope varList, String varname, int type, Object value,
            boolean isExtern, String externValue) throws RuleException {

        VariableStructure struct = (VariableStructure) varList.getVariable(varname);
        if (struct != null) {
            String msg = AlreadyDefinedVariableRuleException.buildMessage(varname);
            throw new AlreadyDefinedVariableRuleException(msg);
        }

        String defValue = MessageFormat.format(NOT_INITIALIZED_PATTERN, new Object[] { varname });
        struct = new VariableStructure(varname, type, defValue);

        // set value according its type
        String strValue;
        switch (type) {
        case BOOLEAN:
            if (value instanceof String) {
                strValue = (String) value;
                struct.m_value = (strValue.equalsIgnoreCase("TRUE")) ? Boolean.TRUE : Boolean.FALSE; // NOT LOCALIZABLE, keyword
            } else if (value instanceof BooleanModifier) {
                BooleanModifier bm = (BooleanModifier) value;
                boolean result = bm.evaluate(null);
                struct.m_value = result ? Boolean.TRUE : Boolean.FALSE;
            }
            break;
        case INTEGER:
            IntegerModifier im = (IntegerModifier) value;
            int result = im.evaluate(null);
            struct.m_value = new Integer(result);
            break;
        case LIST:
            struct.m_value = new ArrayList();
            break;
        case STRING:
        default:
            // remove quotes
            strValue = (String) value;
            struct.m_value = strValue.substring(1, strValue.length() - 1);
            break;
        } // end switch

        struct.m_isExtern = isExtern;
        if (externValue != null) {
            processExternClause(externValue, struct);
        }

        varList.declare(varname, struct);
    } // end declare()

    private static void processExternClause(String externClause, VariableStructure struct) {
        StringTokenizer st = new StringTokenizer(externClause, ",");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            int idx = token.indexOf('=');
            String field = token.substring(0, idx);
            String value = token.substring(idx + 1);
            value = unquote(value);

            if (field.equals("DISPLAY")) { // NOT LOCALIZABLE, template keyword
                struct.setENDisplayName(value);
            } else if (field.equals("EN_DISPLAY")) { // NOT LOCALIZABLE,
                // template keyword
                struct.setENDisplayName(value);
            } else if (field.equals("FR_DISPLAY")) { // NOT LOCALIZABLE,
                // template keyword
                struct.setFRDisplayName(value);
            } else if (field.equals("ORDER")) { // NOT LOCALIZABLE, template
                // keyword
                int order = Integer.parseInt(value);
                struct.setOrder(order);
            }

        } // end while
    } // end processExternClause()

    private static String unquote(String value) {
        if (value.charAt(value.length() - 1) == '\"') {
            value = value.substring(0, value.length() - 1);
        }

        if (value.charAt(0) == '\"') {
            value = value.substring(1);
        }

        return value;
    } // end unquote()

    public static void undeclare(VariableScope varList, String varname) {
        varList.undeclare(varname);
    }

    public static class VariableStructure implements Serializable {
        private String m_name;
        private int m_type;
        private Serializable m_value;
        private boolean m_isExtern = false;
        private String m_enDisplayName;
        private String m_frDisplayName;
        private int m_order = 100; // default order value

        public VariableStructure(String name, int type, Serializable value) {
            m_name = name;
            m_type = type;
            m_value = value;
        }

        public boolean isExtern() {
            return m_isExtern;
        }

        public int getType() {
            return m_type;
        }

        public String getName() {
            return m_name;
        }

        void setName(String name) {
            m_name = name;
        }

        // get display name, according current locale
        public String getDisplayName() {
            String displayName = null;

            if (Locale.getDefault().equals(Locale.FRENCH)) {
                displayName = m_frDisplayName;
            } else {
                displayName = m_enDisplayName;
            }

            if (displayName == null)
                displayName = m_name;

            return displayName;
        }

        void setENDisplayName(String value) {
            m_enDisplayName = value;
        }

        void setFRDisplayName(String value) {
            m_frDisplayName = value;
        }

        void setOrder(int order) {
            m_order = order;
        }

        public int getOrder() {
            return m_order;
        }

        public Serializable getValue() {
            return m_value;
        }

        public void setValue(Serializable value) {
            // check type
            if ((m_type == BOOLEAN) && !(value instanceof Boolean)) {
                throw new IllegalArgumentException();
            } else if ((m_type == STRING) && !(value instanceof String)) {
                throw new IllegalArgumentException();
            } // end if

            m_value = value;
        }
    }
} // VariableDecl
