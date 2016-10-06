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

package org.modelsphere.sms.or.screen;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.WholeNumberField;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.or.features.ORPNGCase;
import org.modelsphere.sms.or.features.ORPNGStatus;
import org.modelsphere.sms.or.features.PhysicalNameDictionary;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.screen.model.GeneratePhysicalNameModel;

public class GeneratePhysicalNameFrame extends JDialog {
    private static final String PROPERTIES_DIRECTORY = ApplicationContext.getPropertiesFolderPath();
    private static final String PHYSICAL_NAME_GENERATION_PROPERTIES = "physicalnamegeneration properties"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String PHYSICAL_NAME_GENERATION_PROPERTIES_FILE_NAME = "physicalnamegeneration.properties"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String WORD_DELIMITERS = "WORD_DELIMITERS"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String USE_DICTIONARY = "USE_DICTIONARY"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String DICTIONARY_FILE_NAME = "DICTIONARY_FILE_NAME"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String ACCENTED_CHARACTERS_TO_REPLACE = "ACCENTED_CHARACTERS_TO_REPLACE"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private PropertiesSet physicalNameGenerationPropertiesSet = null;
    private static final String DEFAULT_ACCENTED_CHARACTERS_TO_REPLACE = "å=a;ã=a;á=a;à=a;â=a;ä=a;ç=c;é=e;è=e;ê=e;ë=e;í=i;î=i;î=i;ï=i;ñ=n;ó=o;õ=o;ò=o;ô=o;ö=o;ú=u;ù=u;û=u;ü=u;ý=y;ÿ=y;Å=A;Ã=A;Á=A;À=A;Â=A;Ä=A;Ç=C;É=E;È=E;Ê=E;Ë=E;Í=I;Î=I;Î=I;Ï=I;Ñ=N;Ó=O;Õ=O;Ò=O;Ô=O;Ö=O;Ú=U;Ù=U;Û=U;Ü=U;Ý=Y;Ÿ=Y;"; // NOT
    // LOCALIZABLE

    private static final String DICTIONARY_DIR = "dictionaryDir"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private String dictionaryDir;

    private boolean useDictionary = false;
    private boolean generate = false;
    private String wordDelimiters = null;
    private String dictionaryFileName = null;
    private String accentedCharactersToReplace = null;
    private HashMap accentedCharactersToReplaceHashMap = null;
    private PhysicalNameDictionary dictionary = null;
    private static final String EMPTY_COMBOBOX_VALUE = new String(""); // NOT
    // LOCALIZABLE
    private static final String kSrDictionnary = LocaleMgr.screen.getString("SRD");

    JPanel containerPanel = new JPanel();
    JPanel controlBtnPanel = new JPanel();
    JButton generateButton = new JButton();
    JButton closeButton = new JButton();
    JButton helpButton = new JButton();
    GridLayout gridLayout1 = new GridLayout();
    JTabbedPane tabbedPane = new JTabbedPane();
    JPanel conceptPanel = new JPanel();
    JPanel preferencesPanel = new JPanel();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JScrollPane ConceptsScrollPane = new JScrollPane();
    JButton defaultButton = new JButton();
    JPanel wordDelimitersPanel = new JPanel();
    TitledBorder wordDelimitersTitledBorder;
    JLabel wordDelimitersLabel = new JLabel();
    GridBagLayout WordDelimitersGridBagLayout = new GridBagLayout();
    JTextField wordDelimitersTextField = new JTextField();
    JLabel wordDelimitersNoteLabel = new JLabel();
    JPanel DictionaryPanel = new JPanel();
    TitledBorder DictionaryTitledBorder;
    GridBagLayout DicPanelGridBagLayout = new GridBagLayout();
    JButton open = new JButton();
    GridBagLayout preferencesGridBagLayout = new GridBagLayout();
    JComboBox caseCombo = new JComboBox(ORPNGCase.objectPossibleValues);
    WholeNumberField nbCharByWordTextField = new WholeNumberField(
            GeneratePhysicalNameModel.DEFAULT_NCBW);
    WholeNumberField lengthTextField = new WholeNumberField(
            GeneratePhysicalNameModel.DEFAULT_LENGTH);
    JTextField replacementCharTextField = new JTextField(
            GeneratePhysicalNameModel.DEFAULT_REPLACEMENT_STRING);
    JComboBox statusCombo = new JComboBox(ORPNGStatus.objectPossibleValues);
    GridBagLayout conceptsGridBagLayout = new GridBagLayout();
    JTable conceptsTable;
    JCheckBox uniqueCheckBox = new JCheckBox();
    JPanel parametersPanel = new JPanel(new CustomLayout());
    GridBagLayout parametersGridBagLayout = new GridBagLayout();
    JCheckBox useDictionaryCheckBox = new JCheckBox();
    JTextField dictionaryFileTextField = new JTextField();
    TableModelListener tableModelListener = new TableModelListener() {
        public void tableChanged(TableModelEvent e) {
            updateParametersPanel();
        }
    };

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == caseCombo) {
                if (caseCombo.getSelectedItem() != EMPTY_COMBOBOX_VALUE)
                    updateTableColumn(6, caseCombo.getSelectedItem());
            } else if (e.getSource() == statusCombo) {
                if (statusCombo.getSelectedItem() != EMPTY_COMBOBOX_VALUE) {
                    updateTableColumn(1, statusCombo.getSelectedItem());
                    generateButton.setEnabled(isSomethingToGenerate());
                }
            } else if (e.getSource() == defaultButton) {
                defaultButton_actionPerformed(e);
            } else if (e.getSource() == open) {
                open_actionPerformed(e);
            } else if (e.getSource() == useDictionaryCheckBox) {
                useDictionaryCheckBox_actionPerformed(e);
            } else if (e.getSource() == generateButton) {
                generatePhysicalNames(e);
            } else if (e.getSource() == closeButton) {
                dispose();
            }

        }
    };

    DocumentListener documentListener = new DocumentListener() {
        public void insertUpdate(DocumentEvent e) {
            if (e.getDocument() == replacementCharTextField.getDocument()) {
                fireUpdateReplacementChar();
            } else if (e.getDocument() == lengthTextField.getDocument()) {
                fireUpdateLengthTextField();
            } else if (e.getDocument() == nbCharByWordTextField.getDocument()) {
                fireUpdateNbCharByWordTextField();
            }
        }

        public void removeUpdate(DocumentEvent e) {
            if (e.getDocument() == replacementCharTextField.getDocument()) {
                fireUpdateReplacementChar();
            } else if (e.getDocument() == lengthTextField.getDocument()) {
                fireUpdateLengthTextField();
            } else if (e.getDocument() == nbCharByWordTextField.getDocument()) {
                fireUpdateNbCharByWordTextField();
            }
        }

        public void changedUpdate(DocumentEvent e) {
        }

        // we won't ever get this with PlainDocument

        private void fireUpdateReplacementChar() {
            updateTableColumn(2, replacementCharTextField.getText());
        }

        private void fireUpdateLengthTextField() {
            if (!StringUtil.isEmptyString(lengthTextField.getText())
                    && lengthTextField.getValue() <= Integer.MAX_VALUE
                    && lengthTextField.getValue() >= 4
                    && lengthTextField.getValue() >= nbCharByWordTextField.getValue())
                updateTableColumn(3, lengthTextField.getText());
        }

        private void fireUpdateNbCharByWordTextField() {
            if (!StringUtil.isEmptyString(nbCharByWordTextField.getText())
                    && nbCharByWordTextField.getValue() <= Integer.MAX_VALUE
                    && nbCharByWordTextField.getValue() > 0
                    && nbCharByWordTextField.getValue() <= lengthTextField.getValue())
                updateTableColumn(5, nbCharByWordTextField.getText());
        }
    };

    ItemListener uniqueCheckBoxListener = new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
            updateTableColumn(4, new Boolean(uniqueCheckBox.isSelected()));
        }
    };

    public GeneratePhysicalNameFrame(Frame frame, String title, boolean modal,
            PhysicalNameDictionary dictionary) {
        super(frame, title, modal);
        this.dictionary = dictionary;
        PropertiesManager.installPropertiesSet(PHYSICAL_NAME_GENERATION_PROPERTIES, new File(
                PROPERTIES_DIRECTORY + System.getProperty("file.separator")
                        + PHYSICAL_NAME_GENERATION_PROPERTIES_FILE_NAME)); // NOT
        // LOCALIZABLE,
        // property
        // key
        physicalNameGenerationPropertiesSet = PropertiesManager
                .getPropertiesSet(PHYSICAL_NAME_GENERATION_PROPERTIES);
        // Init value from properties
        GeneratePhysicalNameModel nameModel = new GeneratePhysicalNameModel(
                physicalNameGenerationPropertiesSet);
        conceptsTable = new JTable(nameModel) {
            public void valueChanged(ListSelectionEvent e) {
                super.valueChanged(e);
                if (!e.getValueIsAdjusting())
                    if (GeneratePhysicalNameFrame.this != null)
                        updateParametersPanel();
            }
        };
        wordDelimiters = physicalNameGenerationPropertiesSet.getPropertyString(
                GeneratePhysicalNameFrame.class, WORD_DELIMITERS, ""); // NOT
        // LOCALIZABLE
        useDictionary = physicalNameGenerationPropertiesSet.getPropertyBoolean(
                GeneratePhysicalNameFrame.class, USE_DICTIONARY, new Boolean(useDictionary))
                .booleanValue();
        accentedCharactersToReplace = physicalNameGenerationPropertiesSet.getPropertyString(
                GeneratePhysicalNameFrame.class, ACCENTED_CHARACTERS_TO_REPLACE,
                DEFAULT_ACCENTED_CHARACTERS_TO_REPLACE); // NOT
        // LOCALIZABLE
        dictionaryFileName = physicalNameGenerationPropertiesSet.getPropertyString(
                GeneratePhysicalNameFrame.class, DICTIONARY_FILE_NAME, ""); // NOT LOCALIZABLE
        if (dictionaryFileName != null) {
            File theFile = new File(dictionaryFileName);
            if (theFile != null && theFile.exists() && theFile.canRead()) {
                if (dictionary.validate(theFile) >= 0)
                    dictionaryFileTextField.setText(theFile.getAbsolutePath());
                else {
                    dictionaryFileTextField.setText(null);
                    dictionaryFileName = ""; // NOT LOCALIZABLE
                }
            }
        } else
            dictionaryFileTextField.setText(null);

        jbInit();
        setUpStatusColumn(conceptsTable.getColumnModel().getColumn(1));
        setUpCaseColumn(conceptsTable.getColumnModel().getColumn(6));
        conceptsTable.selectAll();
        dictionaryDir = getDictionaryDir();
        this.pack();
        this.setLocationRelativeTo(frame);
    }

    public GeneratePhysicalNameFrame(Frame frame, PhysicalNameDictionary dictionary) {
        this(frame, LocaleMgr.screen.getString("PhysicalNamesGeneration"), true, dictionary);
    }

    void jbInit() {
        wordDelimitersTitledBorder = new TitledBorder("");
        DictionaryTitledBorder = new TitledBorder("");
        generateButton.setText(LocaleMgr.screen.getString("GeneratePNG"));
        closeButton.setText(LocaleMgr.screen.getString("Close"));
        helpButton.setText(LocaleMgr.screen.getString("Help"));
        conceptsTable.getTableHeader().setReorderingAllowed(false);
        for (int i = 0; i < conceptsTable.getColumnModel().getColumnCount(); i++) {
            conceptsTable.getColumnModel().getColumn(i).addPropertyChangeListener(
                    new java.beans.PropertyChangeListener() {
                        public void propertyChange(java.beans.PropertyChangeEvent evt) {
                            if (evt.getPropertyName() == TableColumn.COLUMN_WIDTH_PROPERTY
                                    || evt.getPropertyName() == "columnWidth"
                                    || evt.getPropertyName() == "width")
                                updateParametersPanelPosition();
                        }
                    });
        }
        addComponentListener(new ComponentListener() {
            public void componentResized(ComponentEvent e) {
                updateParametersPanelPosition();
            };

            public void componentMoved(ComponentEvent e) {
            };

            public void componentShown(ComponentEvent e) {
            };

            public void componentHidden(ComponentEvent e) {
            };
        });
        conceptsTable.getModel().addTableModelListener(tableModelListener);
        final WholeNumberField integerField = new WholeNumberField(0);
        integerField.setHorizontalAlignment(WholeNumberField.RIGHT);
        DefaultCellEditor integerEditor = new DefaultCellEditor(integerField) {
            public Object getCellEditorValue() {
                return new Integer(integerField.getValue());
            }
        };
        conceptsTable.setDefaultEditor(Integer.class, integerEditor);
        caseCombo.addItem(EMPTY_COMBOBOX_VALUE);
        statusCombo.addItem(EMPTY_COMBOBOX_VALUE);
        containerPanel.setLayout(gridBagLayout1);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        conceptPanel.setLayout(conceptsGridBagLayout);
        preferencesPanel.setLayout(preferencesGridBagLayout);
        defaultButton.setText(LocaleMgr.screen.getString("Default"));
        defaultButton.addActionListener(actionListener);

        wordDelimitersPanel.setBorder(wordDelimitersTitledBorder);
        wordDelimitersPanel.setLayout(WordDelimitersGridBagLayout);
        wordDelimitersTitledBorder.setTitle(LocaleMgr.screen.getString("WordDelimiters"));
        wordDelimitersLabel.setText(LocaleMgr.screen
                .getString("EachCharacterSpecifiedIsAWordDelimiter"));
        wordDelimitersNoteLabel.setText(LocaleMgr.screen
                .getString("NoteSpaceIsTheDefaultWordDelimiter"));
        wordDelimitersTextField.setMinimumSize(new Dimension(4, 210));
        wordDelimitersTextField.setText(wordDelimiters);
        DictionaryPanel.setBorder(DictionaryTitledBorder);
        DictionaryPanel.setLayout(DicPanelGridBagLayout);
        DictionaryTitledBorder.setTitle(LocaleMgr.screen.getString("AbbreviationDictionary"));
        open.setText(LocaleMgr.screen.getString("Open"));
        open.addActionListener(actionListener);
        conceptPanel.setName(LocaleMgr.screen.getString("Concepts"));
        preferencesPanel.setName(LocaleMgr.screen.getString("Preferences"));
        containerPanel.setPreferredSize(new Dimension(660, 510));
        useDictionaryCheckBox.setSelected(useDictionary);
        if (useDictionary) {
            open.setEnabled(true);
            dictionaryFileTextField.setEnabled(true);
        } else {
            open.setEnabled(false);
            dictionaryFileTextField.setEnabled(false);
        }
        useDictionaryCheckBox.setText(LocaleMgr.screen.getString("UseDictionary"));
        useDictionaryCheckBox.addActionListener(actionListener);

        dictionaryFileTextField.setEditable(false);
        getContentPane().add(containerPanel);

        containerPanel.add(controlBtnPanel, new GridBagConstraints(0, 1,
                GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.SOUTHEAST,
                GridBagConstraints.NONE, new Insets(17, 12, 12, 12), 0, 0));

        controlBtnPanel.add(generateButton, null);
        controlBtnPanel.add(closeButton, null);
        // HIDEHELPforV1//controlBtnPanel.add(helpButton, null);
        containerPanel.add(tabbedPane,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        tabbedPane.add(conceptPanel, LocaleMgr.screen.getString("Concepts"));
        conceptPanel.add(ConceptsScrollPane, new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 6, 6), 0, 0));
        ConceptsScrollPane.getViewport().add(conceptsTable, null);

        conceptPanel.add(parametersPanel, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 0, 6), 0, 0));
        parametersPanel.add(statusCombo);
        parametersPanel.add(replacementCharTextField);
        parametersPanel.add(lengthTextField);
        parametersPanel.add(uniqueCheckBox);
        parametersPanel.add(nbCharByWordTextField);
        parametersPanel.add(caseCombo);

        conceptPanel.add(defaultButton, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(12, 6, 6, 12), 0,
                0));
        tabbedPane.add(preferencesPanel, LocaleMgr.screen.getString("Preferences"));
        preferencesPanel.add(wordDelimitersPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 6, 6),
                0, 0));
        wordDelimitersPanel.add(wordDelimitersLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(12, 6, 6, 6), 0,
                0));
        wordDelimitersPanel.add(wordDelimitersTextField, new GridBagConstraints(0, 1, 1, 1, 0.0,
                1.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                new Insets(0, 6, 6, 6), 0, 0));
        wordDelimitersPanel.add(wordDelimitersNoteLabel, new GridBagConstraints(0, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE, new Insets(6, 6, 6, 6),
                0, 0));
        preferencesPanel.add(DictionaryPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0,
                0));
        DictionaryPanel.add(open, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        DictionaryPanel.add(useDictionaryCheckBox, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 6, 6, 6), 0, 0));
        DictionaryPanel.add(dictionaryFileTextField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 6),
                0, 0));

        generateButton.addActionListener(actionListener);

        closeButton.addActionListener(actionListener);
        /*
         * help todo: helpButton.addActionListener(new ActionListener(){ public void
         * actionPerformed(ActionEvent e){
         * 
         * }});
         */
        uniqueCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

        getRootPane().setDefaultButton(generateButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, closeButton, helpButton);
    }

    private final void setUpStatusColumn(TableColumn statusColumn) {
        JComboBox comboBox = new JComboBox(ORPNGStatus.objectPossibleValues);

        statusColumn.setCellEditor(new DefaultCellEditor(comboBox));
        statusColumn.setCellRenderer(new DefaultTableCellRenderer());
    }

    private final void setUpCaseColumn(TableColumn caseColumn) {
        JComboBox comboBox = new JComboBox(ORPNGCase.objectPossibleValues);

        caseColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    public final JTable getConceptsTable() {
        return conceptsTable;
    }

    void open_actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(dictionaryDir);
        ExtensionFileFilter filter = new ExtensionFileFilter(new String[] { "srd" }, kSrDictionnary); // NOT LOCALIZABLE
        chooser.addChoosableFileFilter(filter);
        int retval = chooser.showDialog(this, null);

        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            setDictionaryDir(theFile.getParent());

            if (theFile != null && theFile.exists() && theFile.canRead()) {
                if (dictionary.validate(this, theFile) >= 0) {
                    dictionaryFileTextField.setText(theFile.getAbsolutePath());
                    dictionaryFileName = theFile.getAbsolutePath();
                } else {
                    dictionaryFileTextField.setText(null);
                    dictionaryFileName = null;
                }
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, LocaleMgr.message
                        .getString("FileNotExist"));
                dictionaryFileTextField.setText(null);
            }
        }
    }

    void useDictionaryCheckBox_actionPerformed(ActionEvent e) {
        open.setEnabled(useDictionaryCheckBox.isSelected());
        dictionaryFileTextField.setEnabled(useDictionaryCheckBox.isSelected());
        useDictionary = useDictionaryCheckBox.isSelected();
    }

    public final boolean useDictionary() {
        return useDictionary;
    }

    public final File getDictionaryFile() {
        return new File(dictionaryFileName);
    }

    void generatePhysicalNames(ActionEvent e) {
        generate = true;
        wordDelimiters = wordDelimitersTextField.getText();
        // Save in properties
        ((GeneratePhysicalNameModel) conceptsTable.getModel())
                .saveParametersInProperties(physicalNameGenerationPropertiesSet);
        physicalNameGenerationPropertiesSet.setProperty(GeneratePhysicalNameFrame.class,
                WORD_DELIMITERS, wordDelimiters);
        physicalNameGenerationPropertiesSet.setProperty(GeneratePhysicalNameFrame.class,
                USE_DICTIONARY, useDictionary);
        physicalNameGenerationPropertiesSet.setProperty(GeneratePhysicalNameFrame.class,
                DICTIONARY_FILE_NAME, dictionaryFileName);
        accentedCharactersToReplaceHashMap = convertAccentedCharactersToReplaceStringToHashMap();
        physicalNameGenerationPropertiesSet.setProperty(GeneratePhysicalNameFrame.class,
                ACCENTED_CHARACTERS_TO_REPLACE, accentedCharactersToReplace);

        this.dispose();
    }

    public final boolean doGeneration() {
        return generate;
    }

    public final String getWordDelimiters() {
        return wordDelimiters;
    }

    public final HashMap getAccentedCharactersToReplace() {
        return accentedCharactersToReplaceHashMap;
    }

    private final HashMap convertAccentedCharactersToReplaceStringToHashMap() {
        HashMap hm = new HashMap();
        StringTokenizer st = new StringTokenizer(accentedCharactersToReplace, ";"); // NOT LOCALIZABLE
        try {
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.indexOf("=") != -1) { // NOT LOCALIZABLE
                    String key = token.substring(0, token.indexOf("=")); // NOT
                    // LOCALIZABLE
                    String value = token.substring(token.indexOf("=") + 1, token.length()); // NOT LOCALIZABLE
                    hm.put(key, value);
                }
            }
        } catch (java.lang.StringIndexOutOfBoundsException e) {
            accentedCharactersToReplace = DEFAULT_ACCENTED_CHARACTERS_TO_REPLACE;
            hm = convertAccentedCharactersToReplaceStringToHashMap();
        }
        return hm;
    }

    private final void addParametersListener() {
        statusCombo.addActionListener(actionListener);
        replacementCharTextField.getDocument().addDocumentListener(documentListener);
        lengthTextField.getDocument().addDocumentListener(documentListener);
        uniqueCheckBox.addItemListener(uniqueCheckBoxListener);
        nbCharByWordTextField.getDocument().addDocumentListener(documentListener);
        caseCombo.addActionListener(actionListener);
    }

    private final void removeParametersListener() {
        statusCombo.removeActionListener(actionListener);
        replacementCharTextField.getDocument().removeDocumentListener(documentListener);
        lengthTextField.getDocument().removeDocumentListener(documentListener);
        uniqueCheckBox.removeItemListener(uniqueCheckBoxListener);
        nbCharByWordTextField.getDocument().removeDocumentListener(documentListener);
        caseCombo.removeActionListener(actionListener);

    }

    private final void updateTableColumn(int col, Object value) {
        TableModel tableModel = conceptsTable.getModel();
        tableModel.removeTableModelListener(tableModelListener);
        int[] selectedRows = conceptsTable.getSelectedRows();
        for (int i = 0; i < selectedRows.length; i++) {
            tableModel.setValueAt(value, selectedRows[i], col);
        }
        tableModel.addTableModelListener(tableModelListener);
    }

    private final void updateParametersPanel() {
        Object status = null, caseChange = null;
        String replacementChar = null, length = null, nbCharByWord = null;
        Boolean Unique = null;

        if (conceptsTable == null)
            return;

        TableModel tableModel = conceptsTable.getModel();

        int[] selectedRows = conceptsTable.getSelectedRows();
        if (selectedRows.length == 0) {
            status = EMPTY_COMBOBOX_VALUE;
            replacementChar = GeneratePhysicalNameModel.DEFAULT_REPLACEMENT_STRING;
            length = String.valueOf(GeneratePhysicalNameModel.DEFAULT_LENGTH);
            Unique = new Boolean(GeneratePhysicalNameModel.DEFAULT_UNIQUE);
            nbCharByWord = String.valueOf(GeneratePhysicalNameModel.DEFAULT_NCBW);
            caseChange = EMPTY_COMBOBOX_VALUE;
        }

        for (int i = 0; i < selectedRows.length; i++) {
            if (status == null)
                status = tableModel.getValueAt(selectedRows[i], 1);
            else if (status != tableModel.getValueAt(selectedRows[i], 1))
                status = EMPTY_COMBOBOX_VALUE;
            replacementChar = (String) tableModel.getValueAt(selectedRows[i], 2);
            length = String.valueOf(tableModel.getValueAt(selectedRows[i], 3));
            Unique = (Boolean) tableModel.getValueAt(selectedRows[i], 4);
            nbCharByWord = String.valueOf(tableModel.getValueAt(selectedRows[i], 5));
            if (caseChange == null)
                caseChange = tableModel.getValueAt(selectedRows[i], 6);
            else if (caseChange != tableModel.getValueAt(selectedRows[i], 6))
                caseChange = EMPTY_COMBOBOX_VALUE;

        }
        removeParametersListener();
        statusCombo.setSelectedItem(status);
        replacementCharTextField.setText(replacementChar);
        lengthTextField.setText(String.valueOf(length));
        uniqueCheckBox.setSelected(Unique.booleanValue());
        nbCharByWordTextField.setText(String.valueOf(nbCharByWord));
        caseCombo.setSelectedItem(caseChange);
        addParametersListener();
        generateButton.setEnabled(isSomethingToGenerate());
    }

    private final boolean isSomethingToGenerate() {
        boolean activeGenerateButton = false;

        for (int i = 0; i < conceptsTable.getRowCount(); i++) {
            if (((ORPNGStatus) conceptsTable.getModel().getValueAt(i, 1)) != ORPNGStatus
                    .getInstance(ORPNGStatus.DISABLED))
                activeGenerateButton = true;
        }
        return activeGenerateButton;

    }

    void defaultButton_actionPerformed(ActionEvent e) {
        conceptsTable.clearSelection();
        ((GeneratePhysicalNameModel) conceptsTable.getModel()).setDefault();
        conceptsTable.selectAll();
    }

    private final void updateParametersPanelPosition() {
        parametersPanel.revalidate();
    }

    public String getDictionaryDir() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        return appSet.getPropertyString(GeneratePhysicalNameFrame.class, DICTIONARY_DIR,
                ApplicationContext.getDefaultWorkingDirectory());
    }

    public void setDictionaryDir(String dir) {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appSet.setProperty(GeneratePhysicalNameFrame.class, DICTIONARY_DIR, dir);
    }

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Layout the parameters components to fit the table columns position and
    // width
    private class CustomLayout implements LayoutManager {
        CustomLayout() {
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container target) {
            Dimension dim = null;
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                dim = new Dimension(100, new Double(caseCombo.getPreferredSize().getHeight())
                        .intValue()
                        + insets.top + insets.bottom);
            }
            return dim;
        }

        public Dimension minimumLayoutSize(Container target) {
            Dimension dim = null;
            synchronized (target.getTreeLock()) {
                Insets insets = target.getInsets();
                dim = new Dimension(100, new Double(caseCombo.getPreferredSize().getHeight())
                        .intValue()
                        + insets.top + insets.bottom);
            }
            return dim;
        }

        public void layoutContainer(Container target) {
            synchronized (target.getTreeLock()) {
                int h = target.getHeight();
                Insets insets = target.getInsets();
                Rectangle r = conceptsTable.getCellRect(0, 1, false);
                statusCombo.setBounds(r.x, 0 + insets.top, r.width, h - insets.top - insets.bottom);
                r = conceptsTable.getCellRect(0, 2, false);
                replacementCharTextField.setBounds(r.x, 0 + insets.top, r.width, h - insets.top
                        - insets.bottom);
                r = conceptsTable.getCellRect(0, 3, false);
                lengthTextField.setBounds(r.x, 0 + insets.top, r.width, h - insets.top
                        - insets.bottom);
                r = conceptsTable.getCellRect(0, 4, false);
                uniqueCheckBox.setBounds(r.x, 0 + insets.top, r.width, h - insets.top
                        - insets.bottom);
                r = conceptsTable.getCellRect(0, 5, false);
                nbCharByWordTextField.setBounds(r.x, 0 + insets.top, r.width, h - insets.top
                        - insets.bottom);
                r = conceptsTable.getCellRect(0, 6, false);
                caseCombo.setBounds(r.x, 0 + insets.top, r.width, h - insets.top - insets.bottom);
            }
        }
    }

    // *************
    // DEMO FUNCTION
    // *************

    private static void runDemo() {
        /*
         * GeneratePhysicalNameFrame dialogue = new GeneratePhysicalNameFrame(null);
         * dialogue.setVisible(true); boolean done = false; do { try { Thread.sleep(200); } catch
         * (InterruptedException ex) {}
         * 
         * if (!dialogue.isShowing()) { dialogue.dispose(); dialogue = null; done = true; } } while
         * (! done); System.exit(0);
         */
    } // end runDemo()

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); } //end main()
     */

} // end GeneratePhysicalNameFrame
