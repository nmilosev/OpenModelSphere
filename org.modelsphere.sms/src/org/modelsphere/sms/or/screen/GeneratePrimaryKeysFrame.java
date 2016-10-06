package org.modelsphere.sms.or.screen;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.sms.db.DbSMSTargetSystem;
import org.modelsphere.sms.or.db.DbORBuiltInType;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.util.PrimaryKey;
import org.modelsphere.sms.or.international.LocaleMgr;

public class GeneratePrimaryKeysFrame extends JDialog implements ActionListener {
    private DbORDataModel m_dataModel;

    JLabel instructionLabel = new JLabel(LocaleMgr.screen.getString("GeneratePKForEachTableMsg"));
    JLabel noticeLabel = new JLabel();

    JRadioButton generatePKeyBtn;
    JRadioButton generateSKeyBtn;

    JLabel identifierLabel = new JLabel(LocaleMgr.screen.getString("NameOfSurrogateKey"));
    JTextField identifierTxFld = new JTextField();
    JLabel identifierTypeLabel = new JLabel(LocaleMgr.screen.getString("TypeOfSurrogateKey"));
    JComboBox identifierTypeCombo = new JComboBox();

    JButton okButton;
    JButton cancelButton;

    private static PrimaryKey.Options options = new PrimaryKey.Options();

    public GeneratePrimaryKeysFrame(JFrame frame, String title, DbORDataModel dataModel)
            throws DbException {
        super(frame, title, true);
        m_dataModel = dataModel;

        initContents();
        initValues();
        addListeners();

        this.pack();
        this.setLocationRelativeTo(frame);
    }

    // has cancelled
    private boolean cancelled = true;

    public boolean hasCancelled() {
        return cancelled;
    }

    private void initContents() {
        setLayout(new GridBagLayout());

        int row = 0;
        generatePKeyBtn = new JRadioButton(LocaleMgr.screen
                .getString("GenerateEmptyPrimaryKeysNow"));
        generateSKeyBtn = new JRadioButton(LocaleMgr.screen.getString("GenerateSurrogateKeys"));

        // Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(generatePKeyBtn);
        group.add(generateSKeyBtn);

        // instruction label
        Font font = instructionLabel.getFont();
        Font bold = new Font(font.getName(), Font.BOLD, font.getSize());
        instructionLabel.setFont(bold);
        add(instructionLabel,
                new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        // PK w/o columns
        add(generatePKeyBtn, new GridBagConstraints(0, row, 3, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 6, 0, 6), 0,
                0));
        generatePKeyBtn.setSelected(true);
        row++;

        // Surrogate keys
        add(generateSKeyBtn,
                new GridBagConstraints(0, row, 3, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                        GridBagConstraints.NONE, new Insets(6, 6, 0, 6), 0, 0));
        row++;

        // Name of surrogate key
        add(identifierLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 30, 0, 6), 0,
                0));
        add(identifierTxFld, new GridBagConstraints(1, row, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 0, 0, 12), 0, 0));
        identifierTxFld.setText(options.surrogateName);
        row++;

        // Type of surrogate key
        add(identifierTypeLabel, new GridBagConstraints(0, row, 1, 1, 0.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(6, 30, 0, 6), 0,
                0));
        add(identifierTypeCombo, new GridBagConstraints(1, row, 2, 1, 1.0, 0.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(6, 0, 0, 12), 0, 0));
        row++;

        add(noticeLabel, new GridBagConstraints(0, row, 3, 1, 0.0, 1.0,
                GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(12, 12, 12, 12),
                0, 0));
        row++;

        // OK and Cancel
        okButton = new JButton(LocaleMgr.screen.getString("OK"));
        cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
        JPanel controlBtnPanel = new JPanel();
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.setHgap(5);
        controlBtnPanel.setLayout(gridLayout1);
        controlBtnPanel.add(okButton, null);
        controlBtnPanel.add(cancelButton, null);

        add(controlBtnPanel, new GridBagConstraints(0, row, GridBagConstraints.REMAINDER, 1, 0.0,
                0.0, GridBagConstraints.SOUTHEAST, GridBagConstraints.NONE,
                new Insets(30, 30, 6, 6), 0, 0));
        row++;

        try {
            resetIdentifierTypeCombo(identifierTypeCombo);
        } catch (DbException ex) {
        }
    }

    private void addListeners() {
        generatePKeyBtn.addActionListener(this);
        generateSKeyBtn.addActionListener(this);

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {

        Object src = ev.getSource();
        if (src.equals(generatePKeyBtn)) {
            initValues();
        } else if (src.equals(generateSKeyBtn)) {
            initValues();
        } else if (src.equals(okButton)) {
            DefaultComparableElement elem = (DefaultComparableElement) identifierTypeCombo
                    .getSelectedItem();
            PrimaryKey.TypeInfo info = (PrimaryKey.TypeInfo) elem.object;

            cancelled = false;
            options.generateSurrogate = generateSKeyBtn.isSelected();
            options.surrogateName = identifierTxFld.getText();
            options.surrogateType = info.m_builtInType;
            dispose();
        } else if (src.equals(cancelButton)) {
            cancelled = true;
            dispose();
        } // end if

    } // end actionPerformed()

    private void initValues() {
        boolean surrogate = (generateSKeyBtn.isSelected());
        identifierLabel.setEnabled(surrogate);
        identifierTxFld.setEnabled(surrogate);
        identifierTypeLabel.setEnabled(surrogate);
        identifierTypeCombo.setEnabled(surrogate);

        String msg1 = LocaleMgr.screen.getString("NoticeOnPrimaryKeys");
        String msg2 = LocaleMgr.screen.getString("NoticeOnSurrogateKeys");
        String notice = surrogate ? msg2 : msg1;

        noticeLabel.setText(notice);
    }

    private void resetIdentifierTypeCombo(JComboBox typeCombo) throws DbException {

        if (m_dataModel != null) {
            m_dataModel.getDb().beginReadTrans();
            DbSMSTargetSystem target = m_dataModel.getTargetSystem();
            PrimaryKey surrogate = PrimaryKey.getInstance();

            List<PrimaryKey.TypeInfo> types = surrogate.getTypes(target);
            DbORBuiltInType defaultLongType = surrogate.getLongType(target);
            typeCombo.removeAllItems();
            int index = 0;
            int selectedIndex = 0;

            DbORBuiltInType identifierType = defaultLongType;

            if (types != null) {
                for (PrimaryKey.TypeInfo type : types) {
                    if (type.m_builtInType.equals(identifierType)) {
                        selectedIndex = index;
                    }

                    String text = type.m_typename;
                    typeCombo.addItem(new DefaultComparableElement(type, text));
                    index++;
                }
            }

            if (typeCombo.getItemCount() > selectedIndex) {
                typeCombo.setSelectedIndex(selectedIndex);
            }

            m_dataModel.getDb().commitTrans();
        }

    }

    // *************
    // DEMO FUNCTION
    // *************
    private static void createAndShowGUI() throws DbException {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
        }

        // Create and set up the window.
        JFrame frame = new JFrame("GeneratePrimaryKeysDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        DbORDataModel model = null;
        JDialog dialog = new GeneratePrimaryKeysFrame(frame, "Demo", model);
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

    public PrimaryKey.Options getOptions() {
        return options;
    }

}
