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

package org.modelsphere.jack.text;

/**
 * This class just fixes some bugs of the java.text.Messageformat class, specifically related to its
 * processing of single quotes. It has the same name that its java.text conterpart, and only defines
 * the format() method. So, users that want to use the class instead just have to remove the import
 * java.text.MessageFormat clause and replace it by the import
 * org.modelsphere.jack.text.MessageFormat.
 * 
 * bug #1 (fixed): The single quote (') are replaced by double single quotes (''). This is
 * necessary, because all the localized strings are in .properties files, and at this point, a
 * translator or a technical writer cannot figure out if the string will be used as a
 * MessageFormat.format()'s argument. bug #2 (fixed): As '{' is used for text replacement, we will
 * use "\{" to output an actual opening curly brace. The actual closing curly braces '}' are not
 * affected by text replacement. bug #3 (to do): An error (java.lang.IllegalArgumentException)
 * occurs when we have more than 10 text replacements. A further nice feature would be to detect
 * these cases before they procure errors, split the long string into smaller substrings, perform
 * the text replacement, and then concatenate the resulting formatted substrings. Not supported for
 * now because no such case has been detected yet.
 * 
 * known bug : the following pattern ''\\{\\{'' proceduces ''{'''. This is because this pattern is
 * converted in "'{''{'", which is not properly processed by java.text.MessageFormat, because the
 * two consecutive single quotes are replaced by an actual single quote in a first pass, giving
 * "'{'{'", and then the three enclosing characters are considered like an actual string, giving
 * "{'{" as the final result. Indeed, it is not a bug introduced by
 * org.modelsphere.jack.text.MessageFormat, by rather by its java.text conterpart.
 */

public class MessageFormat {

    private MessageFormat() {
    }

    public final static String format(String pattern, Object[] args) {
        String newPattern = new String(pattern);

        // replace single quotes \' by double single quotes \'\'
        int idx = 0;
        do {
            idx = newPattern.indexOf("\'", idx);
            if (idx != -1) {
                newPattern = newPattern.substring(0, idx) + "\'\'" + newPattern.substring(idx + 1);
                idx += 2; // for the next iteration, search after the two new
                // single quotes
            }
        } while ((idx != -1) && (idx < newPattern.length()));

        // replace special espace codes for opening curly brace '\{' by an
        // actual opening curly brace '{'
        idx = 0;
        do {
            idx = newPattern.indexOf("\\{", idx);
            if (idx != -1) {
                String pref = newPattern.substring(0, idx);
                String suff = newPattern.substring(idx + 2);
                newPattern = pref + "'{'" + suff;
                idx += 3; // for the next iteration, search after the new
                // opening curly brace
            }
        } while ((idx != -1) && (idx < newPattern.length()));

        String formatted = java.text.MessageFormat.format(newPattern, args);
        return formatted;
    } // end format()

    //
    // Unit test
    //
    public static void main(String args[]) {
        String pattern, msg;

        pattern = "";
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

        pattern = "'";
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

        pattern = "\\{";
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

        pattern = "''''";
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

        pattern = "\\{\\{";
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

        pattern = "\\{''It's true, I can't \\{use} the '{0}' method in '{1}', it doesn't work.''\\{"; // NOT
        // LOCALIZABLE,
        // unit
        // test
        msg = MessageFormat.format(pattern, new Object[] {});
        System.out.println(msg);

    } // end main()
} // MessageFormat
