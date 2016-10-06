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

package org.modelsphere.jack.awt;

import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.text.*;

public class NumericTextField extends JTextField {

    public static final int INTEGER = 0;
    public static final int LONG = 1;
    public static final int DOUBLE = 2;

    private int type = 0;
    private boolean signed = true;

    // Default INTEGER
    public NumericTextField() {
        this(INTEGER);
    }

    // type: INTEGER, DOUBLE or LONG
    public NumericTextField(int type) {
        this.type = type;
    }

    // type: INTEGER, DOUBLE or LONG
    // signed: Allow negative numbers (default is true)
    // NOTE: Even if signed==false, the positive boundary of the int value will
    // not be higher than singned==true.
    // signed is just an added validation on field's edition
    public NumericTextField(int type, boolean signed) {
        this.type = type;
        this.signed = signed;
    }

    private class NumericDocument extends PlainDocument {
        NumericDocument() {
        }

        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null || str.length() == 0) {
                super.insertString(offs, str, a);
                return;
            }
            str = str.replace(',', '.');
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                // \b == backspace
                if (!Character.isDigit(c) && ("-+\b".indexOf(c) == -1)
                        && !(type == DOUBLE && c == '.')) { // NOT LOCALIZABLE,
                    // escape code \b
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                if (!signed && ("-+".indexOf(c) > -1)) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
            }
            boolean valid = false;
            try {
                String newString = getText(0, getLength());
                if (newString == null)
                    newString = "";
                newString = newString.substring(0, offs) + str
                        + newString.substring(offs, newString.length());
                if (newString.equals("-") && signed) {
                    valid = true;
                } else {
                    switch (type) {
                    case INTEGER:
                        int testint = Integer.parseInt(newString);
                        valid = true;
                        break;
                    case LONG:
                        long testlong = Long.parseLong(newString);
                        valid = true;
                        break;
                    case DOUBLE:
                        double testdouble = Double.parseDouble(newString);
                        valid = true;
                        break;
                    }
                }
            } catch (Exception e) {
            }
            if (!valid) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            super.insertString(offs, str, a);
        }
    }

    protected Document createDefaultModel() {
        return new NumericDocument();
    }

}
