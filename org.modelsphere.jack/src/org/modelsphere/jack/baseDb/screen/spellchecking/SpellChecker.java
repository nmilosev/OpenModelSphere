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

 ***********************************************************************/
package org.modelsphere.jack.baseDb.screen.spellchecking;

import java.util.*;

import org.modelsphere.jack.properties.PropertySet;

import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordTokenizer;

/**
 * Implementation of ISpellChecking
 * 
 * @author Marco Savard, neosapiens
 * 
 *         Features: 1) Correct words come from various dictionary files (UK English, etc.) 2)
 *         Ignore acronyms 3) Support English and French languages (Locale.getLocale())
 * 
 *         TODO: 1) Add to dictionary
 */
public class SpellChecker implements ISpellChecker {
    private static final String SPELL_CHECKING_ENABLED = "SpellCheckingEnabled"; //NOT LOCALIZABLE, properties

    //detected words whose spelling is wrong
    private List<Word> fMisspelledWords = new ArrayList<Word>();

    public SpellChecker() {
        PropertySet properties = PropertySet.getInstance(SpellChecker.class);
        String value = properties.getProperty(SPELL_CHECKING_ENABLED, Boolean.toString(true));
        boolean enabled = Boolean.parseBoolean(value);
        setSpellCheckerEnabled(enabled);
    }

    //enabled?
    private boolean fSpellCheckingEnabled;

    @Override
    public boolean isSpellCheckerEnabled() {
        return fSpellCheckingEnabled;
    }

    @Override
    public void setSpellCheckerEnabled(boolean enabled) {
        fSpellCheckingEnabled = enabled;
        PropertySet properties = PropertySet.getInstance(SpellChecker.class);
        properties.setProperty(SPELL_CHECKING_ENABLED, Boolean.toString(enabled));
    }

    @Override
    public void checkSpell(WordTokenizer wordTokenizer) {
        fMisspelledWords.clear();

        //for each word.. 
        try {
            while (wordTokenizer.hasMoreWords()) {
                String token = wordTokenizer.nextWord();
                int pos = wordTokenizer.getCurrentWordPosition();
                final Word word = new Word(token, pos);
                int len = word.length();

                //..that has more than one character and not already identified as wrong
                if ((len > 1) && (!fMisspelledWords.contains(word))) {
                    //..check its spelling; if misspelled, add to the list of misspelled word
                    boolean correct = isCompoundWordCorrect(word.toString());
                    if (!correct) {
                        fMisspelledWords.add(word);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } //end try
    } //end checkSpell()

    @Override
    public List<Word> getMisspelledWords() {
        return fMisspelledWords;
    }

    @Override
    public List<com.swabunga.spell.engine.Word> getSuggestions(Word word) {
        final int THRESHOLD = 100;
        List<com.swabunga.spell.engine.Word> suggestions = new ArrayList<com.swabunga.spell.engine.Word>();

        //for each loaded dictionary..
        List<SpellDictionary> dictionaries = Dictionary.instance.getDictionaries();
        for (SpellDictionary dic : dictionaries) {
            //..add words suggested in the current dictionary
            String text = word.getText();
            List<com.swabunga.spell.engine.Word> sugs = dic.getSuggestions(text, THRESHOLD);
            suggestions.addAll(sugs);
        } //end for

        return suggestions;
    } //end getSuggestions()  

    @Override
    public Word findWordAtOffset(int offset) {
        Word wordAtOffset = null;

        //this will work efficiently if misspelledWords.length < 100 elements
        List<Word> misspelledWords = getMisspelledWords();
        for (Word word : misspelledWords) {
            if (offset >= word.getStart() && (offset <= word.getEnd())) {
                wordAtOffset = word;
                break;
            } //end if
        } //end for

        return wordAtOffset;
    } //end findWordAtOffset()

    //
    // private methods
    //
    private static final String WORD_SEPARATORS = "-_";

    private boolean isCompoundWordCorrect(String compound) {
        boolean correct = true;

        StringTokenizer st = new StringTokenizer(compound, WORD_SEPARATORS);
        while (st.hasMoreTokens()) {
            String simpleWord = st.nextToken();
            correct &= isCorrect(simpleWord);
            if (!correct) {
                break;
            }
        }

        return correct;
    }

    @Override
    public boolean isCorrect(String word) {
        //Proper nouns are never misspelled
        if (Character.isUpperCase(word.charAt(0))) {
            return true;
        }

        boolean correct = false; //false until found in dictionary 

        List<SpellDictionary> dictionaries = Dictionary.instance.getDictionaries();
        for (Iterator<SpellDictionary> i = dictionaries.iterator(); i.hasNext();) {
            SpellDictionary dic = i.next();
            boolean c = dic.isCorrect(word);
            if (c == true) {
                correct = c;
                break;
            } //end
        } //end for

        //if still not correct
        if (!correct) {
            correct = true;

            //the word is correct, except if a lowercase character 
            //is found anywhere in the word.
            for (int i = 0; i < word.length(); i++) {
                char ch = word.charAt(i);
                boolean c = Character.isLowerCase(ch);
                if (c == true) {
                    correct = false;
                    break;
                } //end if
            } //end if 
        } //end if 

        return correct;
    } //end if 

    @Override
    public void clear() {
        fMisspelledWords.clear();
    }

} //end SpellChecking