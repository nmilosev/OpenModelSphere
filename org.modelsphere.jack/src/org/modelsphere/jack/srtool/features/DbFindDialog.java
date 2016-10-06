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

package org.modelsphere.jack.srtool.features;

import org.apache.regexp.RESyntaxException;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;

import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class DbFindDialog extends JDialog implements ActionListener, TestableWindow {

    private static final int MAX_RECENT_SEARCHES = 10;
    private static Vector recentSearches = null;
    private static boolean caseSensitiveValue = false;
    private static boolean wholeWordsOnlyValue = false;
    private static boolean regularExpressionsValue = false;
    private static boolean recursiveValue = true;
    private static boolean nameScopeValue = true;
    private static boolean physicalNameScopeValue = false;
    private static boolean aliasScopeValue = false;
    private static boolean descriptionScopeValue = false;
    private static boolean allOtherTextFieldsScopeValue = false;
    private FindOption fo = null;

    JPanel innerContentPane = new JPanel();
    JPanel optionPanel = new JPanel();
    JPanel scopePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    TitledBorder titledBorder1 = new TitledBorder(LocaleMgr.screen.getString("options"));
    TitledBorder titledBorder2 = new TitledBorder(LocaleMgr.screen.getString("scope"));
    JLabel objectToFindL = new JLabel(LocaleMgr.screen.getString("objectToFind"));
    JComboBox findCombo;
    JCheckBox caseSensitiveCB = new JCheckBox(LocaleMgr.screen.getString("caseSensitive"),
            caseSensitiveValue);
    JCheckBox wholeWorldsCB = new JCheckBox(LocaleMgr.screen.getString("wholeWordsOnly"),
            wholeWordsOnlyValue);
    JCheckBox grepCB = new JCheckBox(LocaleMgr.screen.getString("regularExpressions"),
            regularExpressionsValue);
    JCheckBox recursiveCB = new JCheckBox(LocaleMgr.screen.getString("recursive"), recursiveValue);
    JCheckBox nameCB = new JCheckBox(LocaleMgr.screen.getString("name"), nameScopeValue);
    JCheckBox codedNameCB = new JCheckBox(LocaleMgr.screen.getString("codedName"),
            physicalNameScopeValue);
    JCheckBox aliasCB = new JCheckBox(LocaleMgr.screen.getString("alias"), aliasScopeValue);
    JCheckBox descriptionCB = new JCheckBox(LocaleMgr.screen.getString("description"),
            descriptionScopeValue);
    JCheckBox allOtherTextCB = new JCheckBox(LocaleMgr.screen.getString("allOtherTextFields"),
            allOtherTextFieldsScopeValue);
    JButton findButton = new JButton(LocaleMgr.screen.getString("find"));
    JButton cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
    GridLayout gridLayout1 = new GridLayout();

    public DbFindDialog(Frame frame) {
        super(frame, LocaleMgr.screen.getString("find"), true);
        jbInit();
        this.pack();
        this.setLocationRelativeTo(frame);
    }

    void jbInit() {
        initComboBox();
        // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        // Graphic Design Utility. Keep it into comments when modification are
        // done.

        /*
         * label.setText("Object to find:"); //NOT LOCALIZABLE
         * objectToFindL.setText("Object to find:"); //NOT LOCALIZABLE
         * caseSensitiveCB.setText("Case Sensitive"); //NOT LOCALIZABLE
         * wholeWorldsCB.setText("Whole Word Only"); //NOT LOCALIZABLE
         * grepCB.setText("Regular Expressions (grep)"); //NOT LOCALIZABLE
         * findButton.setText("Find"); //NOT LOCALIZABLE cancelButton.setText("Cancel"); //NOT
         * LOCALIZABLE objectToFindL.setDisplayedMnemonic('O'); //NOT LOCALIZABLE
         * caseSensitiveCB.setMnemonic('C'); //NOT LOCALIZABLE wholeWorldsCB.setMnemonic('W'); //NOT
         * LOCALIZABLE grepCB.setMnemonic('R'); //NOT LOCALIZABLE findButton.setMnemonic('F'); //NOT
         * LOCALIZABLE nameCB.setText("Name"); //NOT LOCALIZABLE codedNameCB.setText("Coded name");
         * //NOT LOCALIZABLE aliasCB.setText("Alias"); //NOT LOCALIZABLE
         * descriptionCB.setText("Description"); //NOT LOCALIZABLE
         * allOtherTextCB.setText("All other text fields"); //NOT LOCALIZABLE
         * recursiveCB.setText("Recursive"); //NOT LOCALIZABLE
         */
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        innerContentPane.setLayout(new GridBagLayout());
        optionPanel.setBorder(titledBorder1);
        optionPanel.setLayout(new GridBagLayout());
        scopePanel.setLayout(new GridBagLayout());
        scopePanel.setBorder(titledBorder2);
        buttonPanel.setLayout(gridLayout1);
        gridLayout1.setHgap(5);
        getContentPane().add(innerContentPane);

        innerContentPane.add(objectToFindL, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 12, 12, 0), 0, 0));
        innerContentPane.add(findCombo, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                new Insets(12, 12, 12, 12), 160, 0));
        /** Options Panel */
        innerContentPane.add(optionPanel, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 12, 12, 12),
                0, 0));
        optionPanel.add(caseSensitiveCB, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        optionPanel.add(wholeWorldsCB, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        optionPanel.add(grepCB, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        optionPanel.add(recursiveCB, new GridBagConstraints(0, 3, 2, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        /** Scope Panel */
        innerContentPane.add(scopePanel, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 12, 0, 12), 0,
                0));
        scopePanel.add(nameCB, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        scopePanel.add(codedNameCB, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        scopePanel.add(aliasCB, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        scopePanel.add(descriptionCB, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        scopePanel.add(allOtherTextCB, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 12, 0, 12), 0,
                0));
        nameCB.addActionListener(this);
        codedNameCB.addActionListener(this);
        aliasCB.addActionListener(this);
        descriptionCB.addActionListener(this);
        allOtherTextCB.addActionListener(this);

        /** Button Panel */
        innerContentPane.add(buttonPanel, new GridBagConstraints(0, 3,
                GridBagConstraints.REMAINDER, 1, 1.0, 1.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(17, 12, 12, 12), 0, 0));
        buttonPanel.add(findButton, null);
        buttonPanel.add(cancelButton, null);

        cancelButton.addActionListener(this);
        findButton.addActionListener(this);

        getRootPane().setDefaultButton(findButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                findCombo.getEditor().getEditorComponent().requestFocus();
            }
        });

        new HotKeysSupport(this, cancelButton, null);
    }

    // if return null, the user as cancel the dialog
    public FindOption getFindOption() {
        return fo;
    }

    private void initComboBox() {
        if (recentSearches != null)
            findCombo = new JComboBox(recentSearches);
        else
            findCombo = new JComboBox();

        findCombo.setEditable(true);
        findCombo.setMaximumRowCount(MAX_RECENT_SEARCHES);
        findCombo.getEditor().selectAll();
        // Je mets ce bloc en commentaire, il courcircuite le comportement
        // standard
        // Sun ont un bug sur ce probleme (sun id: 4256046)... en fait, le
        // VK_ENTER sert a
        // faire disparaitre le popup du combo s'il est visible. Le combo ne
        // devrait pas
        // consommer l'événement du VK_ENTER si son popup n'est pas visible. On
        // attend pour
        // régler ca ... Sun ont le temps de corriger ce pb d'ici la livraison
        // de l'outil
        // mrd. Si non corrigé, faudra vérifier via combo.isPopupVisible() avant
        // de déclancher
        // l'action du VK_ENTER que l'on veut avoir pour ne pas courcircuiter la
        // sélection
        // standard dans le combo.
        /*
         * combo.getEditor().getEditorComponent().addKeyListener(new KeyAdapter(){ public void
         * keyPressed(KeyEvent e){ if (e.getKeyCode() == KeyEvent.VK_ENTER) doFind(true); } });
         */
    }

    private void saveSearchToRecentSearch(String newSearch) {
        if (recentSearches == null)
            recentSearches = new Vector();
        int index = searchStringInRecents(newSearch);
        if (index != -1)
            recentSearches.removeElementAt(index);
        recentSearches.insertElementAt(newSearch, 0);
        if (recentSearches.size() > MAX_RECENT_SEARCHES)
            recentSearches.setSize(MAX_RECENT_SEARCHES);
    }

    private String getComboBoxText(JComboBox comboBox) {
        String text = null;
        if (findCombo.getEditor() != null) {
            Component comp = findCombo.getEditor().getEditorComponent();
            if (comp instanceof javax.swing.text.JTextComponent)
                text = ((javax.swing.text.JTextComponent) comp).getText();
        } else
            text = (String) findCombo.getSelectedItem();
        return text;
    }

    private void doFind(boolean withDispose) {
        String textToFind = getComboBoxText(findCombo);
        ArrayList scopeOptions = getScopeValue();
        if (textToFind != null && textToFind.length() != 0) {
            try {
                saveSearchToRecentSearch(textToFind);
                // Save CheckBox Value for next search
                caseSensitiveValue = caseSensitiveCB.isSelected();
                wholeWordsOnlyValue = wholeWorldsCB.isSelected();
                regularExpressionsValue = grepCB.isSelected();
                recursiveValue = recursiveCB.isSelected();
                nameScopeValue = nameCB.isSelected();
                physicalNameScopeValue = codedNameCB.isSelected();
                aliasScopeValue = aliasCB.isSelected();
                descriptionScopeValue = descriptionCB.isSelected();
                allOtherTextFieldsScopeValue = allOtherTextCB.isSelected();
                fo = new FindOption(textToFind, caseSensitiveCB.isSelected(), wholeWorldsCB
                        .isSelected(), grepCB.isSelected(), recursiveCB.isSelected(), scopeOptions);
                if (withDispose)
                    dispose();
            } catch (RESyntaxException ex) {
                javax.swing.JOptionPane.showMessageDialog(getParent(), LocaleMgr.screen
                        .getString("regularExpressionInvalid")
                        + ex.getMessage());
            }
        }
    }

    private int searchStringInRecents(String str) {
        if (recentSearches != null) {
            for (int i = 0; i < recentSearches.size(); i++) {
                if (recentSearches.elementAt(i).equals(str))
                    return i;
            }
        }
        return -1;
    }

    private ArrayList getScopeValue() {
        ArrayList scopeOptions = new ArrayList();

        scopeOptions.add(new Boolean(nameCB.isSelected()));
        scopeOptions.add(new Boolean(codedNameCB.isSelected()));
        scopeOptions.add(new Boolean(aliasCB.isSelected()));
        scopeOptions.add(new Boolean(descriptionCB.isSelected()));
        scopeOptions.add(new Boolean(allOtherTextCB.isSelected()));
        return scopeOptions;
    }

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == nameCB || source == codedNameCB || source == aliasCB
                || source == descriptionCB || source == allOtherTextCB)
            findButton.setEnabled(validForFind());
        else if (source == findButton) {
            doFind(true);
        } else if (source == cancelButton) {
            dispose();
        }
    }

    private boolean validForFind() {
        if (nameCB.isSelected() || codedNameCB.isSelected() || aliasCB.isSelected()
                || descriptionCB.isSelected() || allOtherTextCB.isSelected())
            return true;
        else
            return false;
    }

    // *************
    // DEMO FUNCTION
    //
    // *************

    public DbFindDialog() {
        this(null);
        try {
            jbInit();
            this.pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end DbFindDialog()

    private static void runDemo() {
        DbFindDialog dialog = new DbFindDialog();
        dialog.setVisible(true);
        boolean done = false;
        do {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (!dialog.isShowing()) {
                dialog.dispose();
                dialog = null;
                done = true;
            }
        } while (!done);
        System.exit(0);
    } // end runDemo()

    public Window createTestWindow(Container owner) {
        DbFindDialog dialog = new DbFindDialog();
        return dialog;
    }

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); } //end main()
     */

} // end DbFindDialog

