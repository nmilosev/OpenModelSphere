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

import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.UIManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.screen.TextEditorDialog;

public class SpellCheckerTest {
    // *************
    // DEMO FUNCTION
    // *************
    private static void createAndShowGUI() throws DbException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }

        // Create and set up the window.
        JFrame frame = new JFrame("TextEditorDialog Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Locale.setDefault(Locale.FRENCH);
        // String text = "this sentense containes some speling misstakes";
        String text = "Ce text contien des fauts d'orthographe; il est mal-corrigé.";

        boolean doCheckSpell = true;
        String title = "Test";
        TextEditorDialog dialog = new TextEditorDialog(frame, text, title, doCheckSpell);
        dialog.setSpellCheckingEnabled(true);
        dialog.setVisible(true);
    }

    // Run the demo
    //
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (DbException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}
