/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.or.features;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.or.international.LocaleMgr;

public class PhysicalNameDictionary {
    public static final int VALID_DICTIONARY = 0;
    public static final int NONVALID_DICTIONARY = -1;
    public static final int FILE_NOT_EXIST = -2;

    private static final String DIC_TOKEN = "DIC="; // NOT LOCALIZABLE
    private static final String SEPARATOR_TOKEN = "="; // NOT LOCALIZABLE
    private static final String DIC_COMMENT = "//"; // NOT LOCALIZABLE

    private KeyAbbreviationList keyAbbList = null;

    public PhysicalNameDictionary() {
    }

    private final int load(JDialog dialog, File file, boolean validate) {

        try {
            if (file.exists()) {
                FileInputStream inFile = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inFile));
                String line = reader.readLine();
                int lineNumber = 2;
                if (validate)
                    keyAbbList = null;
                else
                    keyAbbList = new KeyAbbreviationList();
                if (line != null && line.indexOf(DIC_TOKEN) != -1) {
                    line = reader.readLine();
                    while (line != null) {
                        // read and validate dictionary
                        if (!StringUtil.isEmptyString(line) && !isComment(line)) {
                            if (line.indexOf(SEPARATOR_TOKEN) != -1) {
                                String key, abbreviation;
                                key = line.substring(0, line.indexOf(SEPARATOR_TOKEN));
                                if (line.indexOf(SEPARATOR_TOKEN) + 1 > line.length()) {
                                    showError(dialog, LocaleMgr.message
                                            .getString("SyntaxErrorLine")
                                            + ": " + String.valueOf(lineNumber));
                                    return NONVALID_DICTIONARY;
                                }
                                abbreviation = line.substring(line.indexOf(SEPARATOR_TOKEN) + 1,
                                        line.length());
                                if (StringUtil.isEmptyString(key)
                                        || StringUtil.isEmptyString(abbreviation)) {
                                    showError(dialog, LocaleMgr.message
                                            .getString("SyntaxErrorLine")
                                            + ": " + String.valueOf(lineNumber));
                                    return NONVALID_DICTIONARY;
                                }
                                if (!validate)
                                    keyAbbList.add(key, abbreviation);
                            } else {
                                showError(dialog, LocaleMgr.message.getString("SyntaxErrorLine")
                                        + ": " + String.valueOf(lineNumber));
                                return NONVALID_DICTIONARY;
                            }
                        }
                        line = reader.readLine();
                        lineNumber++;
                    }
                    reader.close();
                    inFile.close();
                } else {
                    showError(dialog, LocaleMgr.message.getString("NonValidDicFirstLineNotDIC")
                            + " \"" + DIC_TOKEN + "\".");
                    return NONVALID_DICTIONARY;
                }
            } else {
                showError(dialog, LocaleMgr.message.getString("FileNotExist"));
                return FILE_NOT_EXIST;
            }
        } catch (Exception e) {
            org.modelsphere.jack.debug.Debug.handleException(e);
        }
        return VALID_DICTIONARY;
    }

    public final int load(File file) {
        return load(null, file, false);
    }

    public final int validate(File file) {
        return load(null, file, true);
    }

    public final int validate(JDialog dialog, File file) {
        return load(dialog, file, true);
    }

    public final String getAbbreviation(String wordToFind) {
        return keyAbbList == null ? null : keyAbbList.get(wordToFind);
    }

    // Key and abbreviation stockage class
    private final class KeyAbbreviationList {
        ArrayList keys, values;

        public KeyAbbreviationList() {
            keys = new ArrayList();
            values = new ArrayList();
        }

        public String get(String key) {
            for (int i = 0; i < keys.size(); i++) {
                String s = (String) keys.get(i);
                if (s.compareToIgnoreCase(key) == 0)
                    return (String) values.get(i);
            }
            return null;
        }

        public void add(String key, String value) {
            keys.add(key);
            values.add(value);
        }
    }

    private final boolean isComment(String line) {
        line = StringUtil.replaceWords(line, " ", "");
        if (line.startsWith(DIC_COMMENT))
            return true;
        return false;
    }

    private final void showError(JDialog dialog, String error) {
        if (dialog != null) {
            javax.swing.JOptionPane.showMessageDialog(dialog, error);
        }
    }
}
