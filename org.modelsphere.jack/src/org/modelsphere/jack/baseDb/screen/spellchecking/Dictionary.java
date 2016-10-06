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
package org.modelsphere.jack.baseDb.screen.spellchecking;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.modelsphere.jack.baseDb.screen.spellchecking.resources.en.EnglishResources;
import org.modelsphere.jack.baseDb.screen.spellchecking.resources.fr.FrenchResources;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;

import com.swabunga.spell.engine.SpellDictionary;

public class Dictionary {

    public static final Dictionary instance = new Dictionary();
    private List<SpellDictionary> fDictionaries = new ArrayList<SpellDictionary>();

    /**
     * 
     */
    private Dictionary() {
        initDictionaries();
    }

    private void initDictionaries() {
        Locale locale = Locale.getDefault();

        boolean french = Locale.FRENCH.getDisplayLanguage().equals(locale.getDisplayLanguage());
        Class<?> resource = french ? FrenchResources.class : EnglishResources.class;
        String[] dictionaryNames = getDictionaryNames(french);

        try {
            //for each dictionary in DICTIONARIES..
            for (String name : dictionaryNames) {

                //..load it and store in loadedDictionaries
                URL url = resource.getResource(name);

                if (url != null) {
                    SpellDictionary dic = new GenericSpellDictionary2(url);
                    fDictionaries.add(dic);
                } else {
                    //System.out.println("Missing dictionary: " + DICTIONARIES[i]);
                }
            } //end for
        } catch (Exception ex) {
            ExceptionHandler
                    .processUncatchedException(ApplicationContext.getDefaultMainFrame(), ex);
        }
    }

    private String[] dictionaryNames = null;

    private String[] getDictionaryNames(boolean french) {

        if (dictionaryNames == null) {
            if (french) {
                dictionaryNames = new String[] { "fr.dic" };
            } else {
                dictionaryNames = new String[] {
                        "eng_com.dic", //the common English (should be the 1st for performance) 
                        // US          // UK
                        "center.dic", "center.dic", "color.dic", "colour.dic", "ize.dic",
                        "ise.dic", "labeled.dic", "labelled.dic", "yze.dic", "ise.dic"
                //TODO: add domain-specific dictionaries  
                };
            }
        }

        return dictionaryNames;
    }

    /**
     * @return the dictionaries
     */
    public List<SpellDictionary> getDictionaries() {
        return fDictionaries;
    }
}
