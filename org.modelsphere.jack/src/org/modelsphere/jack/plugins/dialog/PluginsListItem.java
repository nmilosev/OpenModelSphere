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

package org.modelsphere.jack.plugins.dialog;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.net.URL;
import java.text.MessageFormat;
import java.util.StringTokenizer;

import javax.swing.*;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.plugins.LicenseAgreementDialog;
import org.modelsphere.jack.plugins.PluginConfigurationHandler;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.io.ConfigurationCommandFactory;
import org.modelsphere.jack.plugins.io.DeleteCommand;
import org.modelsphere.jack.plugins.io.PluginLoader;
import org.modelsphere.jack.srtool.ApplicationContext;

@SuppressWarnings("serial")
class PluginsListItem extends JPanel implements PropertyChangeListener {
    private static final String ENABLE = LocaleMgr.screen.getString("Enable");
    private static final String DELETE = LocaleMgr.screen.getString("Delete");
    private static final String DISABLE = LocaleMgr.screen.getString("Disable");
    private static final String LAST_UPDATED0 = LocaleMgr.screen.getString("LastUpdated0");
    private static final String UNKNOWN = LocaleMgr.screen.getString("Unknown");
    private static final String LICENSE = LocaleMgr.screen.getString("License");
    private static final String STATE_ADDED0 = LocaleMgr.screen.getString("PluginAdded0");
    private static final String STATE_REMOVED0 = LocaleMgr.screen.getString("PluginDeleted0");
    private static final String STATE_UPDATED0 = LocaleMgr.screen.getString("PluginUpdated0");
    private static final String DELETE_CONFIRM0 = LocaleMgr.screen.getString("DeleteConfirm0");

    static enum STATE {
        NORMAL, ADDED, REMOVED, UPDATED
    };

    private class EnableDisableLabel extends JLabel {

        EnableDisableLabel(String text) {
            super(text);
        }

        @Override
        public void paint(Graphics g) {
            if (((!pluginEnabled || state == STATE.ADDED || state == STATE.REMOVED) && !selected)) {
                Composite composite = ((Graphics2D) g).getComposite();
                ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                        0.4f));
                super.paint(g);
                ((Graphics2D) g).setComposite(composite);
            } else {
                super.paint(g);
            }
        }

    }

    private Color selectionBackground;
    private Color selectionForeground;
    private Color background;
    private Color foreground;

    //    private Gradient activeGradient;
    //    private Gradient inactiveGradient;

    private JLabel nameLabel;
    private JLabel versionLabel;
    private JLabel authorLabel;
    private JLabel authorContactLabel;
    private JLabel descriptionLabel;
    private JLabel dateLabel;
    private JLabel imageLabel;
    private JLabel stateLabel;

    private String descriptionShort;
    private String descriptionLong;
    private String author;
    private URI authorURI;
    private URI authorContactURI;

    private boolean selected = true;
    private boolean pluginEnabled = true;
    private Boolean pluginEnabledInitialState;
    private String initialVersion;
    private STATE state = STATE.NORMAL;

    private JButton enableDisableButton;
    private JButton deleteButton;
    private JButton licenseButton;

    private JLabel oneRowLabel = new JLabel("Lazy");

    private PluginDescriptor pluginDescriptor;

    private PluginConfigurationHandler configurationHandler;
    private URL licenseURL;

    PluginsListItem(PluginConfigurationHandler configurationHandler) {
        this.configurationHandler = configurationHandler;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIManager.getColor("controlShadow")));

        nameLabel = new EnableDisableLabel("");
        Font font = nameLabel.getFont();
        nameLabel.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));

        versionLabel = new EnableDisableLabel("");
        descriptionLabel = new EnableDisableLabel("");
        stateLabel = new EnableDisableLabel("");
        font = stateLabel.getFont();
        stateLabel.setFont(new Font(font.getFontName(), Font.ITALIC, font.getSize()));
        dateLabel = new EnableDisableLabel("");
        font = dateLabel.getFont();
        dateLabel.setFont(new Font(font.getFontName(), Font.ITALIC, font.getSize()));

        authorLabel = new JLabel("");
        authorLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && authorURI != null)
                    try {
                        Desktop.getDesktop().browse(authorURI);
                    } catch (Exception ex) {
                    }
            }
        });

        authorContactLabel = new JLabel("");
        authorContactLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && authorContactURI != null)
                    try {
                        Desktop.getDesktop().mail(authorContactURI);
                    } catch (Exception ex) {
                    }
            }
        });

        imageLabel = new EnableDisableLabel("");

        nameLabel.setOpaque(false);
        versionLabel.setOpaque(false);
        authorLabel.setOpaque(false);
        authorContactLabel.setOpaque(false);
        descriptionLabel.setOpaque(false);
        stateLabel.setOpaque(false);
        imageLabel.setOpaque(false);
        dateLabel.setOpaque(false);

        enableDisableButton = new JButton(DISABLE);
        enableDisableButton.setOpaque(false);

        enableDisableButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pluginDescriptor != null) {
                    pluginDescriptor.setEnabled(!pluginDescriptor.isEnabled());
                }
            }
        });

        enableDisableButton.setVisible(false);

        deleteButton = new JButton(DELETE);
        deleteButton.setOpaque(false);

        deleteButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (PluginsListItem.this.configurationHandler != null) {
                    int result = JOptionPane.showConfirmDialog(PluginsListItem.this, MessageFormat
                            .format(DELETE_CONFIRM0, new Object[] { pluginDescriptor.getName() }),
                            ((JDialog) SwingUtilities.getAncestorOfClass(JDialog.class,
                                    PluginsListItem.this)).getTitle(),
                            JOptionPane.YES_NO_CANCEL_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        PluginsListItem.this.configurationHandler.delete(pluginDescriptor);
                        PluginLoader.registerCommand(pluginDescriptor, new DeleteCommand());
                    }
                }
            }
        });

        deleteButton.setVisible(false);

        licenseButton = new JButton(LICENSE);
        licenseButton.setOpaque(false);

        licenseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LicenseAgreementDialog.showLicenseAgreement(PluginsListItem.this, pluginDescriptor,
                        true);
            }
        });

        licenseButton.setVisible(false);

        stateLabel.setVisible(false);

        descriptionLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);

        add(imageLabel, new GridBagConstraints(0, 0, 1, 5, 0, 0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 0), 0, 0));

        add(nameLabel, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 6, 6, 6), 0, 0));
        add(versionLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(6, 0, 6, 6), 0, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(3, 0, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        add(authorLabel, new GridBagConstraints(4, 0, 4, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(6, 0, 6, 6), 0, 0));

        add(stateLabel, new GridBagConstraints(1, 1, 6, 1, 1, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(0, 6, 6, 6), 0, 0));

        add(authorContactLabel, new GridBagConstraints(4, 2, 4, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));

        add(descriptionLabel, new GridBagConstraints(1, 3, 6, 1, 1, 1, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 6, 6, 6), 0, 0));

        add(dateLabel, new GridBagConstraints(1, 4, 2, 1, 0, 0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(0, 6, 6, 6), 0, 0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(3, 4, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        add(Box.createHorizontalGlue(), new GridBagConstraints(4, 4, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        add(licenseButton, new GridBagConstraints(5, 4, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        add(deleteButton, new GridBagConstraints(6, 4, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        add(enableDisableButton, new GridBagConstraints(7, 4, 1, 1, 0, 0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));

        setSelected(false);
    }

    private void setDescription(String text) {
        descriptionShort = formatText(text, true);
        descriptionLong = formatText(text, false);
        if (selected) {
            descriptionLabel.setText(descriptionLong);
        } else {
            descriptionLabel.setText(descriptionShort);
        }
    }

    private void setAuthor(String author) {
        this.author = author;
        updateAuthor();
    }

    private void setVersion(String version) {
        if (initialVersion == null) {
            initialVersion = version;
        }
        versionLabel.setText(version);
        updateState();
    }

    private void setImage(Image image) {
        if (image != null)
            imageLabel.setIcon(new ImageIcon(image));
    }

    private void setDate(String date) {
        String temp = date;
        if (temp == null || temp.length() == 0) {
            temp = UNKNOWN;
        }
        dateLabel.setText(MessageFormat.format(LAST_UPDATED0, new Object[] { temp }));
    }

    private void setPluginName(String name) {
        nameLabel.setText(name);
    }

    private void setAuthorURI(String url) {
        try {
            this.authorURI = new URI(url);
        } catch (Exception e) {
        }
        updateAuthor();
    }

    private void setLicenseURL(URL url) {
        this.licenseURL = url;
        licenseButton.setVisible(this.licenseURL != null && isSelected());
    }

    private URI getAuthorContactURI() {
        return authorContactURI;
    }

    private void setAuthorContactURI(String authorContactURI) {
        try {
            if (authorContactURI != null)
                this.authorContactURI = new URI("mailto:" + authorContactURI);
        } catch (Exception e) {
        }

        if (this.authorContactURI == null) {
            authorContactLabel.setText("");
            authorContactLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            authorContactLabel.setText("<html><body><a href=\"" + this.authorContactURI + "\">"
                    + authorContactURI + "</a></body></html>");
            authorContactLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    private void updateAuthor() {
        if (authorURI == null) {
            authorLabel.setText(author);
            authorLabel.setToolTipText(null);
            authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } else {
            authorLabel.setText("<html><body><a href=\"\">" + author + "</a></body></html>");
            authorLabel.setToolTipText(authorURI.toString());
            authorLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    private String formatText(String text, boolean shortForm) {
        if (text == null)
            return null;
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        StringTokenizer st = new StringTokenizer(text, "\n", false);
        boolean firstLine = true;
        while (st.hasMoreTokens()) {
            String s = (String) st.nextToken();
            if (!firstLine) {
                sb.append("<br>");
            }
            sb.append(s);
            firstLine = false;
            if (shortForm)
                break;
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setOpaque(true);
        background = UIManager.getColor("List.background");
        foreground = UIManager.getColor("List.foreground");
        selectionBackground = UIManager.getColor("List.selectionBackground");
        selectionForeground = UIManager.getColor("List.selectionForeground");

        //        activeGradient = new Gradient(selectionBackground, AwtUtil.brighter(selectionBackground, 0.90f)); 
        //        int grayRGB = (selectionBackground.getRed() + selectionBackground.getGreen() + selectionBackground.getBlue()) / 3;
        //        Color gray = new Color(grayRGB, grayRGB, grayRGB);
        //        inactiveGradient = new Gradient(gray, AwtUtil.brighter(gray, 0.80f)); 

        setBackground(background);
        setForeground(foreground);
    }

    //    protected void paintComponent(Graphics g) {
    //        super.paintComponent(g);
    //        Insets insets = getInsets();
    //        int width = getWidth();
    //        int height = getHeight();
    //        Gradient gradient = null;
    //        if (selected){
    //            if (pluginEnabled)
    //                gradient = activeGradient;
    //            else
    //                gradient = inactiveGradient;
    //        } 
    //
    //        if (gradient != null && g instanceof Graphics2D) {
    //            gradient.paint(this, g, insets.left, insets.top, width - insets.left - insets.right,
    //                    height - insets.top - insets.bottom);
    //        } else {
    //            Color old = g.getColor();
    //            g.setColor(getBackground());
    //            g.fillRect(insets.left, insets.top, width - insets.left - insets.right, height
    //                    - insets.top - insets.bottom);
    //            g.setColor(old);
    //        }
    //
    //    }
    //
    boolean isSelected() {
        return selected;
    }

    void setSelected(boolean selected) {
        if (this.selected == selected)
            return;
        this.selected = selected;

        enableDisableButton.setVisible(selected);
        licenseButton.setVisible(this.licenseURL != null && selected);

        if (selected) {
            setBackground(selectionBackground);
            setForeground(selectionForeground);
            nameLabel.setForeground(selectionForeground);
            versionLabel.setForeground(selectionForeground);
            authorLabel.setForeground(selectionForeground);
            descriptionLabel.setForeground(selectionForeground);
            dateLabel.setForeground(selectionForeground);
            authorContactLabel.setForeground(selectionForeground);
        } else {
            setBackground(background);
            setForeground(foreground);
            nameLabel.setForeground(foreground);
            versionLabel.setForeground(foreground);
            authorLabel.setForeground(foreground);
            descriptionLabel.setForeground(foreground);
            dateLabel.setForeground(foreground);
            authorContactLabel.setForeground(foreground);
        }
        if (selected) {
            descriptionLabel.setText(descriptionLong);
            descriptionLabel.setMaximumSize(null);
            descriptionLabel.setPreferredSize(null);
        } else {
            descriptionLabel.setText(descriptionShort);
            descriptionLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, oneRowLabel
                    .getPreferredSize().height));
            descriptionLabel.setPreferredSize(new Dimension(Integer.MAX_VALUE, oneRowLabel
                    .getPreferredSize().height));
        }

        dateLabel.setVisible(selected);
        authorContactLabel.setVisible(selected);

        updateState();

        invalidate();
    }

    private boolean isPluginEnabled() {
        return pluginEnabled;
    }

    private void setPluginEnabled(boolean pluginEnabled) {
        if (pluginEnabledInitialState == null) {
            pluginEnabledInitialState = pluginEnabled;
        }
        if (this.pluginEnabled == pluginEnabled) {
            return;
        }
        this.pluginEnabled = pluginEnabled;
        if (pluginEnabled)
            enableDisableButton.setText(DISABLE);
        else
            enableDisableButton.setText(ENABLE);

        repaint();
    }

    boolean changed() {
        if (pluginEnabledInitialState == null)
            return false;
        return !pluginEnabledInitialState.equals(pluginDescriptor.isEnabled())
                || state != STATE.NORMAL;
    }

    PluginDescriptor getPluginDescriptor() {
        return pluginDescriptor;
    }

    void setPluginDescriptor(PluginDescriptor pluginDescriptor) {
        if (this.pluginDescriptor == pluginDescriptor)
            return;
        if (this.pluginDescriptor != null) {
            this.pluginDescriptor.removePropertyChangeListener(this);
        }
        this.pluginDescriptor = pluginDescriptor;

        if (this.pluginDescriptor != null) {
            setAuthor(pluginDescriptor.getAuthor());
            setAuthorURI(pluginDescriptor.getAuthorURL());
            setPluginName(pluginDescriptor.getName());
            setDescription(pluginDescriptor.getDescription());
            setVersion(pluginDescriptor.getVersion());
            setAuthorContactURI(pluginDescriptor.getAuthorEmail());
            setDate(pluginDescriptor.getDate());
            setImage(pluginDescriptor.getImage());
            setPluginEnabled(pluginDescriptor.isEnabled());
            setLicenseURL(pluginDescriptor.getLicenseURL());
            pluginDescriptor.addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("enabled")) {
            setPluginEnabled((Boolean) e.getNewValue());
        } else if (e.getPropertyName().equals("version")) {
            Object newvalue = e.getNewValue();
            setVersion(newvalue == null ? null : newvalue.toString());
            if ((newvalue != null && initialVersion != null && !initialVersion.equals(newvalue))
                    || (newvalue == null ^ initialVersion == null)) {
                setPluginState(STATE.UPDATED);
            }
        }
    }

    void setPluginState(STATE state) {
        if (state == null || this.state == state) {
            return;
        }
        if (state != STATE.UPDATED && this.state == STATE.ADDED && state == STATE.REMOVED) {
            state = STATE.NORMAL;
        } else if (state != STATE.UPDATED && this.state == STATE.REMOVED && state == STATE.ADDED) {
            state = STATE.NORMAL;
        } else if (state == STATE.UPDATED && this.state == STATE.ADDED) {
            return;
        }
        this.state = state;
        updateState();
    }

    private void updateState() {
        deleteButton.setEnabled(false);
        if (!isSelected()) {
            deleteButton.setVisible(false);
        } else if (pluginDescriptor.getContext().getLoader().isConfigurationCommandSupported(
                ConfigurationCommandFactory.ACTION_DELETE)) {
            deleteButton.setVisible(true);
        } else {
            deleteButton.setVisible(false);
        }
        deleteButton.setEnabled(state == STATE.NORMAL);
        enableDisableButton.setEnabled(state == STATE.NORMAL);

        if (state == STATE.NORMAL) {
            stateLabel.setVisible(false);
        } else {
            if (state == STATE.ADDED) {
                stateLabel.setText(MessageFormat.format(STATE_ADDED0,
                        new Object[] { ApplicationContext.getApplicationName() }));
            } else if (state == STATE.REMOVED) {
                stateLabel.setText(MessageFormat.format(STATE_REMOVED0,
                        new Object[] { ApplicationContext.getApplicationName() }));
            } else {
                stateLabel.setText(MessageFormat.format(STATE_UPDATED0,
                        new Object[] { ApplicationContext.getApplicationName() }));
            }
            stateLabel.setVisible(true);
        }
    }

}