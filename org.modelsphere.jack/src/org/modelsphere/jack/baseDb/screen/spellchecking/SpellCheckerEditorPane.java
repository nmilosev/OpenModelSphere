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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.modelsphere.jack.baseDb.meta.MetaField;

import com.swabunga.spell.event.DocumentWordTokenizer;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordTokenizer;

public class SpellCheckerEditorPane extends JEditorPane implements DocumentListener {

    private static final long serialVersionUID = 1L;

    public SpellCheckerEditorPane(String text, MetaField mf) {
        this.setText(text);
        this.getDocument().addDocumentListener(this);

        String mfName = (mf == null) ? null : mf.getJName();
        boolean enabled = "m_description".equals(mfName);
        setSpellCheckingEnabled(enabled);
        checkSpelling();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkSpelling();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkSpelling();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        checkSpelling();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            identifyMisspelledWord(g);
        } catch (BadLocationException ex) {
            // ignore and continue painting
        }
    }

    //
    // private methods
    //
    private ISpellChecker fSpellChecking = new SpellChecker();

    private void checkSpelling() {
        //if spell check is turned off, exit
        if (!isSpellCheckingEnabled())
            return;

        //get document text
        Document doc = this.getDocument();

        WordTokenizer wordTokenizer = new DocumentWordTokenizer(doc);
        synchronized (this) {
            fSpellChecking.checkSpell(wordTokenizer);
            List<Word> words = fSpellChecking.getMisspelledWords();
            words.addAll(words);
            this.repaint();
        }
    } //end checkSpelling()

    private boolean fSpellCheckingEnabled;

    public boolean isSpellCheckingEnabled() {
        return fSpellCheckingEnabled;
    }

    public void setSpellCheckingEnabled(boolean enabled) {
        fSpellCheckingEnabled = enabled;

        if (enabled) {
            checkSpelling();
        }
    }

    private void identifyMisspelledWord(Graphics g) throws BadLocationException {
        // for each misspelled word..
        List<Word> misspelledWords = fSpellChecking.getMisspelledWords();
        for (Word word : misspelledWords) {
            int start = word.getStart();
            int end = word.getEnd();

            // convert to XY coordinates
            Rectangle startRect = this.modelToView(start);
            Rectangle endRect = this.modelToView(end);
            int x1 = startRect.x;
            int y1 = startRect.y + startRect.height - 4;
            int x2 = endRect.x;

            Color originalColor = g.getColor();
            g.setColor(Color.RED);
            drawBrokenLine(g, x1, y1, x2);
            g.setColor(originalColor);
        }
    }

    /*
     * Draw a horizontal broken line between (x1,y1) and (x1,y1). The broken line looks like:
     * \/\/\/\/\/\/\
     */
    private void drawBrokenLine(Graphics g, int x1, int y1, int x2) {
        final int SEGMENT_LENGTH = 2;
        // for each segment to be drawn
        for (int x = x1; x < (x2 - SEGMENT_LENGTH * 2); x += SEGMENT_LENGTH * 2) {
            g.drawLine(x, y1, x + SEGMENT_LENGTH, y1 + SEGMENT_LENGTH); // descendant
            // segment
            g.drawLine(x + 5, y1 + SEGMENT_LENGTH, x + SEGMENT_LENGTH * 2, y1); // ascendant
            // segment
        }
    }

}
