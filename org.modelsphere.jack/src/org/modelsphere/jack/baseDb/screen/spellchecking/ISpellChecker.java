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

import java.util.List;

import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordTokenizer;

/**
 * @author Marco Savard, neosapiens
 * 
 */
public interface ISpellChecker {

    /**
     * Tells whether the spell checking is enabled
     * 
     * @return true if enabled
     */
    public boolean isSpellCheckerEnabled();

    /**
     * Enable/Disable spell checking
     * 
     * @param enabled
     */
    public void setSpellCheckerEnabled(boolean enabled);

    /**
     * 
     * Check spell of each word returned by wordTokenizer.
     * 
     * @param wordTokenizer
     *            A class that reads the underlying document and returns a list of words.
     */
    public void checkSpell(WordTokenizer wordTokenizer);

    /**
     * Get the list of misspelled words of the underlying document. checkSpell() must be called
     * before using this method.
     * 
     * @return list of misspelled words
     */
    public List<Word> getMisspelledWords();

    /**
     * Tells if a given word is correct (included in the dictionary)
     * 
     * @param word
     * @return true is the word is correct
     */
    public boolean isCorrect(String word);

    /**
     * Returns a list of correct words that look like the given misspelled word.
     * 
     * Ex: getSuggestions("uzed") returns ["used", "zed", "fuzed"]
     */
    public List<com.swabunga.spell.engine.Word> getSuggestions(Word misspelled);

    /**
     * Return the word located at offset. The offset is the position from start (where offset = 0)
     * in the underlying document. Resizing the window may move a given word to a different line or
     * column, but has no effect on its offset.
     * 
     * @return Word, or null if offset is less than 0 or greater than the end of the document
     */
    public Word findWordAtOffset(int offset);

    /**
     * Clear the list of misspelled words
     */
    public void clear();

} //end ISpellChecking