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
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

import org.modelsphere.jack.international.LocaleMgr;

import com.swabunga.spell.event.DocumentWordTokenizer;
import com.swabunga.spell.event.Word;
import com.swabunga.spell.event.WordTokenizer;

public class SpellCheckerTextArea extends JTextArea implements DocumentListener, MouseListener {
    private static final long serialVersionUID = 1L;
    private ISpellChecker fSpellChecking = new SpellChecker();
    private List<com.swabunga.spell.event.Word> words = new ArrayList<Word>();
    private JMenuItem noSuggestionItem = new JMenuItem(LocaleMgr.screen.getString("NoSuggestion"));
    private Color errorColor = new Color(255, 0, 0, 160);

    private boolean pendingUpdate = false;
    private Runnable pendingUpdateRunnable = new Runnable() {

        @Override
        public void run() {
            if (!pendingUpdate)
                return;
            pendingUpdate = false;
            List<com.swabunga.spell.event.Word> temps = null;
            try {
                //if spell check is turned off, exit
                if (fSpellChecking.isSpellCheckerEnabled()) {
                    //get document text
                    Document doc = getDocument();
                    WordTokenizer wordTokenizer = new DocumentWordTokenizer(doc);
                    fSpellChecking.checkSpell(wordTokenizer);
                    temps = fSpellChecking.getMisspelledWords();
                } else {
                    temps = null;
                    fSpellChecking.clear();
                } //end if
            } catch (Exception ex) {
                ex.printStackTrace();
                temps = null;
                fSpellChecking.clear();
            }

            words.clear();
            if (temps != null)
                words.addAll(temps);
            repaint();
        }
    };

    public SpellCheckerTextArea(String text, String title) {
        super(text);
        setEditable(true);
        setLineWrap(true);
        setWrapStyleWord(true);

        noSuggestionItem.setEnabled(false);

        this.addMouseListener(this);
        this.getDocument().addDocumentListener(this);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        checkSpelling();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        checkSpelling();
    }

    private boolean lock = false;

    @Override
    public void removeUpdate(DocumentEvent e) {
        // Jazzy has a bug when manipulating a document (DocumentWordTokenizer's constructor generates an exception) when
        // a delete occurs and offset == 0. 
        // To avoid this issue, we avoid checkSpelling and re-init the document with the same text instead.
        // (document's structure is different after this and jazzy works correctly with the new structure)
        if (e.getOffset() == 0) {
            if (lock)
                return;
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // make sure to keep a lock or the text replacement will generate another removeUpdate and so on...
                    lock = true;
                    String text = getText();
                    int pos = getCaretPosition();
                    setText(text);
                    if (pos > -1) {
                        setCaretPosition(pos);
                    }
                    lock = false;
                }
            });
        } else
            checkSpelling();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        boolean popupTrigger = e.isPopupTrigger();

        if (popupTrigger && fSpellChecking.isSpellCheckerEnabled()) {
            popupSuggestions(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        boolean popupTrigger = e.isPopupTrigger();

        if (popupTrigger && fSpellChecking.isSpellCheckerEnabled()) {
            popupSuggestions(e.getX(), e.getY());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            underlineMisspelledWord(g);
        } catch (Exception ex) {
            // ignore and continue painting
        }
    }

    public void setSpellCheckingEnabled(boolean enabled) {
        fSpellChecking.setSpellCheckerEnabled(enabled);
        checkSpelling();
    }

    public boolean isSpellCheckingEnabled() {
        return fSpellChecking.isSpellCheckerEnabled();
    }

    //
    // private methods
    //
    public final void checkSpelling() {
        if (pendingUpdate)
            return;
        pendingUpdate = true;
        SwingUtilities.invokeLater(pendingUpdateRunnable);
    } //end checkSpelling()

    private void underlineMisspelledWord(Graphics g) throws BadLocationException {
        // for each mispelled word..
        //        List<com.swabunga.spell.event.Word> misspelledWords = fSpellChecking.getMisspelledWords();
        if (words == null)
            return;
        for (com.swabunga.spell.event.Word word : words) {
            int start = word.getStart();
            int end = word.getEnd();

            // convert to XY coordinates
            Rectangle startRect = this.modelToView(start);
            Rectangle endRect = this.modelToView(end);
            int x1 = startRect.x;
            int y1 = startRect.y + startRect.height - 4;
            int x2 = endRect.x;

            //            List<com.swabunga.spell.engine.Word> suggestions = fSpellChecking.getSuggestions(word);
            //            Color lineColor = suggestions.isEmpty() ? Color.ORANGE : Color.RED;
            Color originalColor = g.getColor();
            g.setColor(errorColor);
            drawBrokenLine(g, x1, y1, x2);
            g.setColor(originalColor);
        }
    }

    /*
     * Draw a horizontal broken line between (x1,y1) and (x1,y1). The broken line looks like:
     * \/\/\/\/\/\/\
     */
    private static final int SEGMENT_LENGTH = 3;

    private void drawBrokenLine(Graphics g, int x1, int y1, int x2) {
        // for each segment to be drawn
        for (int x = x1; x < (x2 - SEGMENT_LENGTH * 2); x += SEGMENT_LENGTH * 2) {
            g.drawLine(x, y1, x + SEGMENT_LENGTH, y1 + SEGMENT_LENGTH); // descendant
            // segment
            g.drawLine(x + 5, y1 + SEGMENT_LENGTH, x + SEGMENT_LENGTH * 2, y1); // ascendant
            // segment
        }
    }

    private void popupSuggestions(int x, int y) {

        if (isEditable()) {
            Document doc = getDocument();
            int length = doc.getLength();
            try {
                Point pt = new Point(x, y);
                int offset = viewToModel(pt);
                String text = doc.getText(0, length);
                WordPosition pos = getWordPosition(text, offset, length);
                String word = text.substring(pos.start, pos.end);
                boolean correct = word.isEmpty() ? true : fSpellChecking.isCorrect(word);

                if (!correct) {
                    com.swabunga.spell.event.Word w = new com.swabunga.spell.event.Word(word, 0);
                    List<com.swabunga.spell.engine.Word> suggestions = fSpellChecking
                            .getSuggestions(w);
                    displayPopup(suggestions, pt, pos.start, pos.end - pos.start, doc);
                } //end if

                // 
            } catch (BadLocationException ex) {
                //do not popup
            }
        }
    } //end popupSuggestions()

    private void displayPopup(List<com.swabunga.spell.engine.Word> suggestions, Point pt,
            int start, int length, Document doc) {
        JPopupMenu popup = new JPopupMenu();
        if (suggestions.isEmpty()) {
            popup.add(noSuggestionItem);
        } else {
            ReplaceListener listener = new ReplaceListener(start, length, doc);

            for (com.swabunga.spell.engine.Word o : suggestions) {
                com.swabunga.spell.engine.Word suggestion = (com.swabunga.spell.engine.Word) o;
                JMenuItem item = new JMenuItem(suggestion.getWord());
                item.addActionListener(listener);
                item.setActionCommand(suggestion.getWord());
                popup.add(item);
            } //end for

        } //end if
        popup.show(this, pt.x, pt.y);
    } //end displayPopup()

    private WordPosition getWordPosition(String text, int offset, int length) {
        int start = offset;
        int end = offset;

        char c = 0;

        //find start of word
        while (start > 0) {
            start--;
            c = text.charAt(start);
            if (!Character.isLetter(c) && c != '\'') {
                start++;
                break;
            }
        }

        //find end of word
        while (end < length - 1) {
            end++;
            c = text.charAt(end);
            if (!Character.isLetter(c) && c != '\'') {
                end--;
                break;
            }
        }

        if (end == length) {
            end--;
        }

        WordPosition pos = new WordPosition(start, end + 1);
        return pos;
    }

    private static class WordPosition {
        public WordPosition(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int start;
        int end;
    }

    private static class ReplaceListener implements ActionListener {
        int offset;
        int length;
        Document doc;

        public ReplaceListener(int offset, int length, Document doc) {
            this.offset = offset;
            this.length = length;
            this.doc = doc;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                String cmd = e.getActionCommand();
                doc.remove(offset, length);
                doc.insertString(offset, cmd, null);
            } catch (BadLocationException f) {
            }
        }
    }
}
