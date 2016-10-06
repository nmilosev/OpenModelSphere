package org.modelsphere.jack.plugins;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.international.LocaleMgr;

/**
 * Defines the dialog for plugin license. To display the dialog, use the utility method
 * {@link #showLicenseAgreement(Component, PluginDescriptor, boolean)}
 * 
 * @author gpelletier
 * 
 */
@SuppressWarnings("serial")
public class LicenseAgreementDialog extends JDialog {
    private static final String CLOSE = LocaleMgr.screen.getString("Close");
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String OK = LocaleMgr.screen.getString("OK");
    private static final String LICENSE0 = LocaleMgr.screen.getString("LicenseAgreement0");
    private static final String LICENSE_ = LocaleMgr.screen.getString("LicenseAgreement_");
    private static final String LICENSEYES = LocaleMgr.screen.getString("LicenseYes");
    private static final String LICENSENO = LocaleMgr.screen.getString("LicenseNo");
    private static final String LOADING_LICENSE_ = LocaleMgr.screen.getString("LoadingLicense_");
    private static final String LOADING_FAILED = LocaleMgr.screen.getString("FailedLoading");

    static final int ACCEPT_OPTION = 1;
    static final int REFUSE_OPTION = 2;
    static final int CANCEL_OPTION = 3;

    private JRadioButton acceptButton;
    private JRadioButton refuseButton;
    private JButton okButton;

    private int result = CANCEL_OPTION;

    private JEditorPane licensePane;

    private URL licenseURL;
    private boolean showOnly = false;

    private Runnable loadLicenseRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                licensePane.setPage(licenseURL);
            } catch (IOException e) {
                licensePane.setText(LOADING_FAILED);
            }
        }
    };

    private LicenseAgreementDialog(Window parent, String pluginName, URL licenseURL,
            boolean showOnly) {
        super(parent);
        if (parent instanceof Dialog || parent instanceof Frame) {
            setModal(true);
        }
        setTitle(MessageFormat.format(LICENSE0, new Object[] { pluginName }));
        this.licenseURL = licenseURL;
        this.showOnly = showOnly;
        init();
        setSize(AwtUtil.getBestDialogSize());
        AwtUtil.centerWindow(this);
    }

    private void init() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        getContentPane().setLayout(new GridBagLayout());

        acceptButton = new JRadioButton(LICENSEYES);
        refuseButton = new JRadioButton(LICENSENO);
        acceptButton.setSelected(false);
        refuseButton.setSelected(false);

        acceptButton.setVisible(!showOnly);
        refuseButton.setVisible(!showOnly);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(acceptButton);
        buttonGroup.add(refuseButton);

        licensePane = new JEditorPane();
        licensePane.setEditable(false);
        licensePane.setText(LOADING_LICENSE_);

        JLabel licenseLabel = new JLabel(LICENSE_);

        okButton = new JButton(OK);
        JButton cancelButton = new JButton(showOnly ? CLOSE : CANCEL);

        acceptButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = ACCEPT_OPTION;
                okButton.setEnabled(showOnly || acceptButton.isSelected()
                        || refuseButton.isSelected());
            }
        });

        refuseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = REFUSE_OPTION;
                okButton.setEnabled(!showOnly || acceptButton.isSelected()
                        || refuseButton.isSelected());
            }
        });

        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                result = CANCEL_OPTION;
                dispose();
            }
        });

        okButton.setVisible(!showOnly);
        okButton.setEnabled(false);

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        buttonPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
        buttonPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0, 0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));

        getContentPane().add(
                licenseLabel,
                new GridBagConstraints(0, 0, 2, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(12, 6, 6, 6), 0, 0));
        getContentPane().add(
                new JScrollPane(licensePane),
                new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 6, 6, 6), 0, 0));
        getContentPane().add(
                Box.createHorizontalGlue(),
                new GridBagConstraints(0, 2, 1, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(12, 0, 3, 0), 0, 0));
        getContentPane().add(
                acceptButton,
                new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(12, 6, 3, 24), 0, 0));
        getContentPane().add(
                refuseButton,
                new GridBagConstraints(1, 3, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 6, 6, 24), 0, 0));
        getContentPane().add(
                buttonPanel,
                new GridBagConstraints(0, 4, 2, 1, 1, 0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(12, 6, 6, 6), 0, 0));

        new Thread(loadLicenseRunnable).start();
    }

    /**
     * Display a modal license dialog for the specified plugin. The user is ask to accept/reject if
     * showOnly == false.
     * 
     * @param parent
     *            The parent component for the dialog.
     * @param pluginDescriptor
     *            The plugin.
     * @param showOnly
     *            If true, the user won't be ask to accept or reject the license. If false, the user
     *            will be required to select Accept or Reject from a Radio buttons group.
     * @return {@link #ACCEPT_OPTION}, {@link #REFUSE_OPTION} or {@link #CANCEL_OPTION}
     */
    public static int showLicenseAgreement(Component parent, PluginDescriptor pluginDescriptor,
            boolean showOnly) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        LicenseAgreementDialog dialog = new LicenseAgreementDialog(window, pluginDescriptor
                .getName(), pluginDescriptor.getLicenseURL(), showOnly);
        dialog.setVisible(true);
        return dialog.result;
    }
}
