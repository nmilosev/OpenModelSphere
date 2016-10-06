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

package org.modelsphere.sms.features;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.HotKeysSupport;
import org.modelsphere.jack.awt.JackOptionPane;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.SMSToolkit;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.international.LocaleMgr;

@SuppressWarnings("serial")
public final class SaveDiagramDialog extends JDialog {
	private static final String JPEG = "JPEG";
	private static final String PNG = "PNG";
	
    private JPanel containerPanel = new JPanel(new GridBagLayout());
    private JPanel pageOptionPanel = new JPanel(new GridBagLayout());
    private JPanel controlBtnPanel = new JPanel();
    private JPanel sliderPanel = new JPanel(new GridBagLayout());
    private JPanel filePanel = new JPanel(new GridBagLayout());
    private GridLayout gridLayout1 = new GridLayout();
    private TitledBorder titledBorder1 = new TitledBorder(LocaleMgr.screen.getString("Options")); // NOT LOCALIZABLE, key
    private TitledBorder titledBorder2 = new TitledBorder(LocaleMgr.screen.getString("File")); // NOT LOCALIZABLE, key

    // buttons
    private JButton browseButton = new JButton("..."); // NOT LOCALIZABLE, key
    private JButton saveButton = new JButton(LocaleMgr.screen.getString("Save")); // NOT
    // LOCALIZABLE,
    // key
    private JButton cancelButton = new JButton(LocaleMgr.screen.getString("Close")); // NOT LOCALIZABLE, key
    private JButton helpButton = new JButton(LocaleMgr.screen.getString("Help")); // NOT
    // LOCALIZABLE,
    // key

    // radio buttons
    private JRadioButton singleFileRadioButton = new JRadioButton(LocaleMgr.screen
            .getString("OneFileOnly")); // NOT LOCALIZABLE, key
    private JRadioButton multipleFilesRadioButton = new JRadioButton(LocaleMgr.screen
            .getString("MultipleFiles")); // NOT LOCALIZABLE,
    // key
    private JRadioButton allRadioButton = new JRadioButton(LocaleMgr.screen.getString("All")); // NOT LOCALIZABLE, key
    private JRadioButton pageRadioButton = new JRadioButton(LocaleMgr.screen.getString("Pages")); // NOT LOCALIZABLE, key

    // text fields
    private JTextField fromTextField = new JTextField(3);
    private JTextField toTextField = new JTextField(3);
    private JTextField browseTextField = new JTextField();
    
    // labels
    private JLabel toLabel = new JLabel(LocaleMgr.screen.getString("To")); // NOT
    // LOCALIZABLE,
    // key
    // private JLabel fileNameLabel = new
    // JLabel(LocaleMgr.screen.getString("File"/*"FileName"*/)); // NOT
    // LOCALIZABLE, key
    private JLabel fromLabel = new JLabel(LocaleMgr.screen.getString("From")); // NOT
    // LOCALIZABLE,
    // key
    private JLabel fileNote = new JLabel(LocaleMgr.screen.getString("FileNote"));

    private ButtonGroup groupA = new ButtonGroup();
    private ButtonGroup groupB = new ButtonGroup();

    private JLabel scaleComboBoxLabel = new JLabel(LocaleMgr.screen.getString("ImageScale")); // NOT LOCALIZABLE, key

    private JComboBox scaleComboBox = new JComboBox(new String[] { "100", "75", "50", "25" }); // NOT LOCALIZABLE, numbers
    private JComboBox typeComboBox;

    private JLabel jpegQualityLabel = new JLabel(LocaleMgr.screen.getString("JPEGQuality"));
    private JLabel jpegQualityNote = new JLabel(LocaleMgr.screen.getString("JPEGNote"));
    private JSlider jpegqualitySlider = new JSlider(0, 100);

    private ApplicationDiagram appDiagram = null;
    private DbSMSDiagram diagram = null;

    // resources
    private static final String kDialogTitle = LocaleMgr.screen.getString("SaveDiagram"); // NOT LOCALIZABLE, key
    private static final String kExportToJPEG = LocaleMgr.screen.getString("ExportToJPEG"); // NOT LOCALIZABLE, key
    private static final String kExportToPNG = LocaleMgr.screen.getString("ExportToPNG"); // NOT LOCALIZABLE, key
    private static final String kOK = LocaleMgr.screen.getString("Ok"); // NOT LOCALIZABLE key

    private static final String SAVE_DIAG_DIR = "SaveDiagDir"; // NOT
    // LOCALIZABLE,
    // property key
    private static final String SAVE_DIAG_SCALE = "SaveDiagScale"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final String SAVE_DIAG_QUALITY = "SaveDiagQuality"; // NOT
    // LOCALIZABLE,
    // property
    // key
    private static final int DEFAULT_SCALE = 100;
    private static final int DEFAULT_QUALITY = 60;

    private static String diagramFolder;
    private boolean overwriteAll = false;

    // private SaveDiagramThread thread = null;
    public final static int CHECKTIME = 200; // 1sec == 1000
    // private SaveDiagramTask task = null;
    private boolean diagramHasToBeDeleted = false;

    private int nbOfPages = 0;
    private String diagramName;

    // for option dialog
    /*
     * private String yesButtonLabel = LocaleMgr.screen.getString("Yes"); private String
     * yesToAllButtonLabel = LocaleMgr.screen.getString("YesToAll"); private String noButtonLabel =
     * LocaleMgr.screen.getString("No"); private String cancelButtonLabel =
     * LocaleMgr.screen.getString("Cancel2"); private Object[] options = new
     * Object[]{yesButtonLabel, yesToAllButtonLabel, noButtonLabel, cancelButtonLabel};
     */

    public SaveDiagramDialog(DbSMSDiagram aDiagram, Frame frame, boolean modal) throws DbException {
        this(aDiagram, frame, kDialogTitle, modal);
    }

    public SaveDiagramDialog(DbSMSDiagram aDiagram, Frame frame, String title, boolean modal)
            throws DbException {
        super(frame, title, modal);
        diagram = aDiagram;
        diagramFolder = getSaveDiagDir();

        diagram.getDb().beginTrans(Db.READ_TRANS);
        diagramName = diagram.getName();
        DbSemanticalObject so = (DbSemanticalObject) diagram.getComposite();
        SMSToolkit kit = SMSToolkit.getToolkit(so);
        appDiagram = new ApplicationDiagram(so, diagram, kit.createGraphicalComponentFactory(),
                ApplicationContext.getDefaultMainFrame().getDiagramsToolGroup());
        nbOfPages = appDiagram.getNumberOfPages();
        appDiagram.delete();
        appDiagram = null;
        diagram.getDb().commitTrans();

        jbInit();
        this.pack();
        this.setLocationRelativeTo(frame);
        

        //browseTextField.setText(new File(jpegDiagDir, diagram.getName() + "."
        //        + ExtensionFileFilter.jpgFileFilter.getExtension()).getAbsolutePath());
        //diagram.getDb().commitTrans();
        // browseTextField.setText(jpegDiagDir+"diagram0"+"."+ExtensionFileFilter.jpgFileFilter.getExtension());

    }

    public SaveDiagramDialog(ApplicationDiagram aDiagram, Frame frame, String title, boolean modal,
            boolean hasToBeDeleted) throws DbException {
        super(frame, title, modal);
        appDiagram = aDiagram;
        
        appDiagram.getDiagramGO().getDb().beginTrans(Db.READ_TRANS);
        diagramName = appDiagram.getDiagramGO().getName();
        appDiagram.getDiagramGO().getDb().commitTrans();

        nbOfPages = appDiagram.getNumberOfPages();

        diagramHasToBeDeleted = hasToBeDeleted;
        diagramFolder = getSaveDiagDir();

        jbInit();
        this.pack();
        this.setLocationRelativeTo(frame);

        /*
        appDiagram.getDiagramGO().getDb().beginTrans(Db.READ_TRANS);
        diagramName = appDiagram.getDiagramGO().getName();
        
        //browseTextField.setText(new File(jpegDiagDir, appDiagram.getDiagramGO().getName() + "."
        //        + ExtensionFileFilter.jpgFileFilter.getExtension()).getAbsolutePath());
        appDiagram.getDiagramGO().getDb().commitTrans();
        // browseTextField.setText(jpegDiagDir+"diagram0"+"."+ExtensionFileFilter.jpgFileFilter.getExtension());
         */

    }

    public SaveDiagramDialog(ApplicationDiagram aDiagram, Frame frame, boolean modal,
            boolean hasToBeDeleted) throws DbException {
        this(aDiagram, frame, kDialogTitle, modal, hasToBeDeleted);
    }

    public SaveDiagramDialog(ApplicationDiagram aDiagram, Frame frame, boolean hasToBeDeleted)
            throws DbException {
        this(aDiagram, frame, "Titre du dialogue en ressource", false, hasToBeDeleted); // NOT LOCALIZABLE, default title
    }

    void jbInit() throws DbException {
        containerPanel.setBorder(BorderFactory.createEtchedBorder());
        pageOptionPanel.setBorder(titledBorder1);
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        getContentPane().setLayout(new GridBagLayout());

        filePanel.setBorder(titledBorder2);

        // label text color
        // toLabel.setForeground(SystemColor.infoText);
        // fileNameLabel.setForeground(SystemColor.infoText);
        // fromLabel.setForeground(SystemColor.infoText);

        // radio button groups
        groupA.add(this.singleFileRadioButton);
        groupA.add(this.multipleFilesRadioButton);
        groupB.add(this.allRadioButton);
        groupB.add(this.pageRadioButton);

        // scaleComboBoxLabel.setForeground(Color.black);
        scaleComboBox.setEditable(true);
        scaleComboBox.setSelectedItem(Integer.toString(getScaleFromPreference()));
        jpegqualitySlider.setValue(getQualityFromPreference());
        jpegqualitySlider.setPaintLabels(true);
        jpegqualitySlider.setPaintTicks(true);
        jpegqualitySlider.setPaintTrack(true);
        jpegqualitySlider.setMajorTickSpacing(10);
        jpegqualitySlider.setMinorTickSpacing(5);
        jpegqualitySlider.setSnapToTicks(true);
        jpegQualityNote.setForeground(Color.GRAY.darker());
        fileNote.setForeground(Color.GRAY.darker());
        AwtUtil.normalizeComponentDimension(new JButton[] { saveButton, cancelButton, helpButton });

        // Option Panel

        getContentPane().add(
                containerPanel,
                new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(12, 12, 0, 11), 0, 0));

        containerPanel.add(pageOptionPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 12, 0, 11),
                150, 0));
        pageOptionPanel.add(singleFileRadioButton, new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 9, 0, 0), 0,
                0));
        pageOptionPanel.add(multipleFilesRadioButton, new GridBagConstraints(0, 1, 5, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 9, 0, 0), 0, 0));

        pageOptionPanel.add(allRadioButton, new GridBagConstraints(0, 2, 5, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 36, 0, 0), 0, 0));
        
        // Pages from x to y
        
        JPanel fromToPagesPanel = new JPanel(new GridBagLayout());
        pageOptionPanel.add(fromToPagesPanel, new GridBagConstraints(0, 3, 5, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 36, 12, 0), 0, 0));
        
        fromToPagesPanel.add(pageRadioButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 3), 0, 0));
        fromToPagesPanel.add(fromLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
        fromToPagesPanel.add(fromTextField, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
        fromToPagesPanel.add(toLabel, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
        fromToPagesPanel.add(toTextField, new GridBagConstraints(4, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 3, 0, 3), 0, 0));
        fromToPagesPanel.add(new JPanel(), new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
     
        
        pageOptionPanel.add(scaleComboBoxLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 3, 11), 0, 0));
        
        pageOptionPanel.add(scaleComboBox, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 0, 3, 12), -50, 0));

        // Image Type
        JLabel imageTypeComboBoxLabel = new JLabel(LocaleMgr.screen.getString("ImageType")); 
        pageOptionPanel.add(imageTypeComboBoxLabel, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 3, 11), 0, 0));
        
        typeComboBox = new JComboBox(new String[] { "JPEG", "PNG" }); // NOT LOCALIZABLE, image type
        pageOptionPanel.add(typeComboBox, new GridBagConstraints(3, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 0, 3, 0), 0, 0));
  
        
        // Slider Panel
        sliderPanel.add(jpegQualityLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 5, 0), 0, 0));
        sliderPanel.add(jpegqualitySlider, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 3, 5, 0), 0,
                0));
        sliderPanel.add(jpegQualityNote, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 9, 5, 0), 0,
                0));
        pageOptionPanel.add(sliderPanel, new GridBagConstraints(0, 5, 5, 1, 1.0, 0.5,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(9, 12, 3, 12),
                0, 0));

        // File Panel
        containerPanel.add(filePanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 12, 11, 11),
                0, 0));

        filePanel.add(browseTextField, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 5, 0), 0,
                0));
        filePanel.add(browseButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 5, 11), 0, -5));

        filePanel.add(fileNote, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 12, 11, 11), 0, 0));

        // Control Button Panel
        getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 1.0, 1.0,
                        GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE, new Insets(17, 12,
                                11, 11), 0, 0));

        controlBtnPanel.add(saveButton, null);
        controlBtnPanel.add(cancelButton, null);
        // HIDEHELPforV1//controlBtnPanel.add(helpButton, null);

        /** Listener */
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                save();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browse();
            }
        });

        singleFileRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageOptions();
            }
        });
        multipleFilesRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageOptions();
            }
        });

        allRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageOptions();
            }
        });
        pageRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageOptions();
            }
        });
        
        typeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshImageType();
            }
        });

        browseTextField.setEditable(false);
        browseTextField.getDocument().addDocumentListener(new DocumentListener() {

            public final void insertUpdate(DocumentEvent e) {
                saveButton.setEnabled(browseTextField.getText().length() != 0);
            }

            public final void removeUpdate(DocumentEvent e) {
                saveButton.setEnabled(browseTextField.getText().length() != 0);
            }

            public final void changedUpdate(DocumentEvent e) {
            }
        });

        KeyAdapter keyAdapter = new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char car = e.getKeyChar();
                if (!Character.isDigit(car) && "-+\b".indexOf(car) == -1) { // NOT
                    // LOCALIZABLE
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
            }
        };

        fromTextField.addKeyListener(keyAdapter);
        toTextField.addKeyListener(keyAdapter);
        scaleComboBox.addKeyListener(keyAdapter);

        initializeComponents();
        getRootPane().setDefaultButton(saveButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        new HotKeysSupport(this, cancelButton, null);
        refreshImageType();
    }

    private void initializeComponents() throws DbException {

        saveButton.setEnabled(false);
        singleFileRadioButton.setSelected(true);
        allRadioButton.setSelected(true);
        fromTextField.setText("1");// NOT LOCALIZABLE,
        toTextField.setText(String.valueOf(nbOfPages));
        fromTextField.setColumns(3);
        toTextField.setColumns(3);
        // fromTextField.setHorizontalAlignment(JTextField.RIGHT);
        // toTextField.setHorizontalAlignment(JTextField.RIGHT);

        if (nbOfPages == 1)
            multipleFilesRadioButton.setEnabled(false);

        manageOptions();
    }

    private void manageOptions() {
        allRadioButton.setEnabled(multipleFilesRadioButton.isSelected());
        pageRadioButton.setEnabled(multipleFilesRadioButton.isSelected());
        fromLabel.setEnabled(multipleFilesRadioButton.isSelected() && pageRadioButton.isSelected());
        fromTextField.setEnabled(multipleFilesRadioButton.isSelected()
                && pageRadioButton.isSelected());
        toLabel.setEnabled(multipleFilesRadioButton.isSelected() && pageRadioButton.isSelected());
        toTextField.setEnabled(multipleFilesRadioButton.isSelected()
                && pageRadioButton.isSelected());
    }
    
	private void refreshImageType() {
		Object item = typeComboBox.getSelectedItem();
		boolean isJpeg = JPEG.equals(item);
		jpegQualityLabel.setEnabled(isJpeg);
		jpegqualitySlider.setEnabled(isJpeg); 
		
		//build file name
		ExtensionFileFilter filter = isJpeg ? ExtensionFileFilter.jpgFileFilter : ExtensionFileFilter.pngFileFilter;
		String sep = System.getProperty("file.separator");
		String filename = diagramFolder + sep + diagramName + "." + filter.getExtension();
		browseTextField.setText(filename);
	}

    // ************************************************************************
    // //
    // Show a file chooser dialog
    // If the chosen file doesnt have an extension, .jpg is added to it
    // the directory of the chosen file is stored for next browse
    // ************************************************************************
    // //
    private void browse() {
        File file = null;
        DefaultMainFrame mainFrame = ApplicationContext.getDefaultMainFrame();
        JFileChooser chooser = new JFileChooser(diagramFolder);
        
        Object item = typeComboBox.getSelectedItem();
		boolean isJpeg = JPEG.equals(item);
		ExtensionFileFilter filter = isJpeg ? ExtensionFileFilter.jpgFileFilter : ExtensionFileFilter.pngFileFilter;
        chooser.addChoosableFileFilter(filter);
        chooser.setFileFilter(filter);
        
        String title = isJpeg ? kExportToJPEG : kExportToPNG;
        chooser.setDialogTitle(title);
        chooser.setApproveButtonText(kOK);

        if (chooser.showDialog(mainFrame, null) != JFileChooser.APPROVE_OPTION)
            return;
        file = chooser.getSelectedFile();
        if (file == null)
            return;
        
        if (file.getName().lastIndexOf('.') == file.getName().lastIndexOf(".\\")) { // or "./"
            String filename = file.getPath() + "." + filter.getExtension();
        	browseTextField.setText(filename);
        } else {
            browseTextField.setText(file.getPath());
        }
        
        diagramFolder = chooser.getCurrentDirectory().toString();
        setSaveDiagDir(diagramFolder);

    }

    // ************************************************************************
    // //
    // validate dialog fields (only for "pages from ... to ...")
    //
    // a thread is launched for the saving, it can be cancelled
    // ************************************************************************
    // //
    private void save() {

        try {
            validateFields();
            setScaleInPreference();
            setQualityInPreference();
            launchTask();
        } catch (SaveDiagramException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), LocaleMgr.message
                    .getString("Warning"), JOptionPane.WARNING_MESSAGE); // NOT LOCALIZABLE, key
            if (ex.getComponent() != null) {
                // put focus on the component (source of the error)
                ex.getComponent().requestFocus();
                if (ex.getComponent() instanceof JTextField) {
                    // select the text
                    JTextField textField = (JTextField) ex.getComponent();
                    Caret caret = textField.getCaret();
                    caret.setDot(0);
                    caret.moveDot(textField.getText().length());
                }
            }
        }
    }

    // ************************************************************************
    // //
    // validation for "from-to" option only
    // ************************************************************************
    // //
    private void validateFields() throws SaveDiagramException {
        String illegalCharactersErrorMessage, numericValueErrorMessage;
        String invalidValueErrorMessage, pageNumberMissingErrorMessage, fromGreaterThanToErrorMessage;

        int firstPage = 0;
        int lastPage = nbOfPages - 1;

        /*
         * if(browseTextField.getText().length()==0) throw new
         * SaveDiagramException(LocaleMgr.message.getString("FileNameMissing"), browseTextField);
         */

        try {
            Integer.parseInt((String) scaleComboBox.getSelectedItem());
        } catch (NumberFormatException e) {
            illegalCharactersErrorMessage = MessageFormat.format(LocaleMgr.message
                    .getString("ScaleContainsIllegalCharacters"), null); // NOT
            // LOCALIZABLE,
            // key
            throw new SaveDiagramException(illegalCharactersErrorMessage, scaleComboBox);
        }

        if (Integer.parseInt((String) scaleComboBox.getSelectedItem()) <= 0) {
            numericValueErrorMessage = MessageFormat.format(LocaleMgr.message
                    .getString("ScaleMustBeNumeric"), null); // NOT LOCALIZABLE,
            // key
            throw new SaveDiagramException(numericValueErrorMessage, scaleComboBox);
        }

        if (pageRadioButton.isSelected()) {
            // check if fields are empty
            if (fromTextField.getText().length() == 0) {
                pageNumberMissingErrorMessage = MessageFormat.format(LocaleMgr.message
                        .getString("PageNumberMissing0"), new Object[] { LocaleMgr.screen
                        .getString("From") }); // NOT
                // LOCALIZABLE,
                // key
                throw new SaveDiagramException(pageNumberMissingErrorMessage, fromTextField);
            }
            if (toTextField.getText().length() == 0) {
                pageNumberMissingErrorMessage = MessageFormat.format(LocaleMgr.message
                        .getString("PageNumberMissing0"), new Object[] { LocaleMgr.screen
                        .getString("To") }); // NOT
                // LOCALIZABLE,
                // key
                throw new SaveDiagramException(pageNumberMissingErrorMessage, toTextField);
            }

            // they are not empty empty so get the value (page number) of each
            firstPage = Integer.parseInt(fromTextField.getText()) - 1;
            lastPage = Integer.parseInt(toTextField.getText()) - 1;

            // check if values are valid [1, nbOfPages]
            invalidValueErrorMessage = MessageFormat.format(LocaleMgr.message
                    .getString("InvalidValue01"), new Object[] { new String("1"),
                    String.valueOf(nbOfPages) }); // NOT LOCALIZABLE, key
            if ((firstPage < 0) || (firstPage >= nbOfPages))
                throw new SaveDiagramException(invalidValueErrorMessage, fromTextField);
            if ((lastPage < 0) || (lastPage >= nbOfPages))
                throw new SaveDiagramException(invalidValueErrorMessage, toTextField);

            if (firstPage > lastPage) {
                fromGreaterThanToErrorMessage = MessageFormat.format(LocaleMgr.message
                        .getString("AGreaterThanB01"), new Object[] {
                        LocaleMgr.screen.getString("From"), LocaleMgr.screen.getString("To") }); // NOT
                // LOCALIZABLE,
                // key
                throw new SaveDiagramException(fromGreaterThanToErrorMessage, fromTextField);
            }
        }
    }

    private void launchTask() {
        int firstPage = 1;
        int lastPage = 1;
        String browseText = browseTextField.getText();

        if (multipleFilesRadioButton.isSelected()) {
            lastPage = nbOfPages;

            if (pageRadioButton.isSelected()) {
                firstPage = Integer.parseInt(fromTextField.getText());
                lastPage = Integer.parseInt(toTextField.getText());
            }
        }

        Object[] objects;

        if (diagram != null) {
            objects = new Object[] { diagram };
        } else if (appDiagram != null) {
            objects = new Object[] { appDiagram };
        } else {
            objects = new Object[0];
        }

        String DEFAULT_LOG_FILENAME = ApplicationContext.getLogPath()
                + System.getProperty("file.separator") + "SaveDiagram.log";
        Controller c = new DefaultController(LocaleMgr.screen.getString("SaveDiagramTitle"), false,
                DEFAULT_LOG_FILENAME);
        
        //set options
        Object item = typeComboBox.getSelectedItem();
		boolean isJpeg = JPEG.equals(item);
		ExtensionFileFilter filter = isJpeg ? ExtensionFileFilter.jpgFileFilter : ExtensionFileFilter.pngFileFilter;
        SaveDiagramOptions options = new SaveDiagramOptions(this, objects, filter);
        
        c.start(new SaveDiagramWorker(options));
        setVisible(false);
    }

    public boolean isMultiple() {
        return multipleFilesRadioButton.isSelected();
    }

    public int getFirstPage() {
        return Integer.parseInt(fromTextField.getText());
    }

    public int getLastPage() {
        return Integer.parseInt(toTextField.getText());
    }

    public int getScale() {
        return Integer.parseInt((String) scaleComboBox.getSelectedItem());
    }

    public float getQuality() {
        return jpegqualitySlider.getValue();
    }

    public void dispose() {
        if (diagramHasToBeDeleted) {
            appDiagram.delete();
        }

        super.dispose();
    }

    // ************************************************************************
    // //
    // 1- if file to validate doesnt have any extension, .jpg is added to it
    // 2- if a relative path is specified, it will be with the working directory
    // 3- if the file already exists and is read-only, ...
    // 4- if the file already exists, is writable, ask user input :
    // - the current file can be overwritten (yes),
    // - the current and remaining files can overwritten (yes to all), (multiple
    // files option only)
    // - the current file cannot be overwritten (no),
    // - cancel the whole saving operation
    // 5 - if the file already exists and is read-only, cancel saving and return
    // to dialog
    // ************************************************************************
    // //
    private File validateFile(String fileName) throws CancelledOperationException {
        File file = null;
        String readOnlyMessage;// =
        // LocaleMgr.message.getString("FileReadOnly0");
        // // NOT LOCALIZABLE, key
        String wanOverwriteMessage;// =LocaleMgr.message.getString("FileExistsWanOverwriteIt0");//
        // NOT LOCALIZABLE, key

        file = new File(fileName);
        // if absolute is not specified
        if (!file.isAbsolute())
            file = new File(diagramFolder, fileName);

        if (file.exists()) {
            if (file.canWrite()) {
                if (!overwriteAll) {
                    int returnValue;
                    wanOverwriteMessage = MessageFormat.format(LocaleMgr.message
                            .getString("FileExistsWanOverwriteIt0"), // NOT
                            // LOCALIZABLE,
                            // key
                            new Object[] { file.getPath(), "\'" });
                    // int nbChoices;
                    if (singleFileRadioButton.isSelected()) {
                        // it's impossible to put the focus on a particular
                        // button (last parameter), it's always the first button
                        // that has the focus
                        // returnValue =
                        // JOptionPane.showOptionDialog(MainFrame.getSingleton(),
                        // wanOverwriteMessage, "",
                        // javax.swing.JOptionPane.DEFAULT_OPTION,
                        // javax.swing.JOptionPane.QUESTION_MESSAGE, null, new
                        // Object[]{options[0],options[2],options[3]},
                        // options[0]); // NOT LOCALIZABLE, key
                        // nbChoices = 3;
                        returnValue = JackOptionPane.showConfirmDialog(MainFrame.getSingleton(),
                                wanOverwriteMessage, "", JackOptionPane.YES_NO_CANCEL_OPTION,
                                JackOptionPane.QUESTION_MESSAGE); // NOT
                        // LOCALIZABLE
                        switch (returnValue) {
                        case JackOptionPane.YES_OPTION:
                            break; // nothing
                        case JackOptionPane.NO_OPTION:
                            file = null;
                            break;
                        case JackOptionPane.CANCEL_OPTION:
                            throw new CancelledOperationException();
                        }
                    } else {
                        // returnValue =
                        // JOptionPane.showOptionDialog(MainFrame.getSingleton(),
                        // wanOverwriteMessage, "",
                        // javax.swing.JOptionPane.DEFAULT_OPTION,
                        // javax.swing.JOptionPane.QUESTION_MESSAGE, null,
                        // options, options[0]); // NOT LOCALIZABLE, key
                        // nbChoices = 4;
                        returnValue = JackOptionPane.showConfirmDialog(MainFrame.getSingleton(),
                                wanOverwriteMessage, "",
                                JackOptionPane.YES_YESTOALL_NO_CANCEL_OPTION,
                                JackOptionPane.QUESTION_MESSAGE); // NOT
                        // LOCALIZABLE
                        switch (returnValue) {
                        case 0 /* YES */:
                            break; // nothing
                        case 1 /* YES TO ALL */:
                            overwriteAll = true;
                            break;
                        case 2 /* NO */:
                            file = null;
                            break;
                        case 3 /* CANCEL */:
                            throw new CancelledOperationException();
                        }
                    }
                    /*
                     * switch(returnValue){ case 1: overwriteAll = true; break; // 3rd button is No
                     * 3rd button is Cancel case 2: if(nbChoices==4) {file = null;break;} else throw
                     * new CancelledOperationException(); case 3: throw new
                     * CancelledOperationException(); }
                     */
                }
            } else {
                readOnlyMessage = MessageFormat.format(
                        LocaleMgr.message.getString("FileReadOnly0"),
                        new Object[] { file.getPath() }); // NOT LOCALIZABLE, key
                JOptionPane.showMessageDialog(SaveDiagramDialog.this, readOnlyMessage, "", // NOT LOCALIZABLE, key
                        JOptionPane.ERROR_MESSAGE);
                // file = null;
                throw new CancelledOperationException();
            }
        }

        return file;
    }

    public String getSaveDiagDir() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        return appSet.getPropertyString(SaveDiagramDialog.class, SAVE_DIAG_DIR, ApplicationContext
                .getDefaultWorkingDirectory());
    }

    public void setSaveDiagDir(String dir) {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appSet.setProperty(SaveDiagramDialog.class, SAVE_DIAG_DIR, dir);
    }

    public int getScaleFromPreference() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        return appSet.getPropertyInteger(SaveDiagramDialog.class, SAVE_DIAG_SCALE,
                new Integer(DEFAULT_SCALE)).intValue();
    }

    public void setScaleInPreference() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appSet.setProperty(SaveDiagramDialog.class, SAVE_DIAG_SCALE, getScale());
    }

    public int getQualityFromPreference() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        return appSet.getPropertyInteger(SaveDiagramDialog.class, SAVE_DIAG_QUALITY,
                new Integer(DEFAULT_QUALITY)).intValue();
    }

    public void setQualityInPreference() {
        PropertiesSet appSet = PropertiesManager.APPLICATION_PROPERTIES_SET;
        appSet.setProperty(SaveDiagramDialog.class, SAVE_DIAG_QUALITY, (int) getQuality());
    }

    public String getFile() {
        return browseTextField.getText();
    }

    // INNER CLASS
    private class SaveDiagramException extends Exception {
        private JComponent component = null;

        SaveDiagramException(String errorMessage) {
            super(errorMessage);
        }

        SaveDiagramException(String errorMessage, JComponent aComponent) {
            super(errorMessage);
            component = aComponent;
        }

        public JComponent getComponent() {
            return component;
        }
    }

    // INNER CLASS
    private class CancelledOperationException extends Exception {

        CancelledOperationException() {
            super();
        }

        CancelledOperationException(String errorMessage) {
            super(errorMessage);
        }
    }
}
