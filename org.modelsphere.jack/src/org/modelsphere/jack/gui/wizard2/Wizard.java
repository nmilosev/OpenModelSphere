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

package org.modelsphere.jack.gui.wizard2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.Border;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;

@SuppressWarnings("serial")
public abstract class Wizard extends JDialog {
    private static final String CANCEL = LocaleMgr.screen.getString("Cancel");
    private static final String NEXT = LocaleMgr.screen.getString("Next");
    private static final String PREVIOUS = LocaleMgr.screen.getString("Previous");
    private static final String kFinish = LocaleMgr.screen.getString("Finish");
    private static final String kCancelOperation = LocaleMgr.message.getString("CancelOperation");

    private JPanel mainPanel = new JPanel();
    private JPanel controlBtnPanel = new JPanel(new GridBagLayout());

    private class IndexPanel extends JPanel {
        private Image image;
        private Color color1;
        private Color color2 = Color.WHITE;

        IndexPanel() {
            setLayout(new GridBagLayout());
        }

        private Image getImage() {
            return image;
        }

        private void setImage(Image image) {
            this.image = image;
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension dim = super.getPreferredSize();
            Insets insets = getInsets();
            dim.width = Math.max(dim.width + 12, 160);
            if (image != null) {
                dim.width = Math.max(image.getWidth(null) + insets.left + insets.right, dim.width);
                dim.height = Math.max(image.getHeight(null) + insets.top + insets.bottom,
                        dim.height);
            }
            return dim;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Insets insets = getInsets();

            Paint paint = ((Graphics2D) g).getPaint();
            GradientPaint gradient = new GradientPaint((float) (getWidth() * -0.5),
                    (float) (getHeight() * 0.4), color2, (float) (getWidth() * 0.8),
                    (float) (getHeight() * 1), color1);
            ((Graphics2D) g).setPaint(gradient);
            g.fillRect(insets.left, insets.top, getWidth() - insets.left - insets.right,
                    getHeight() - insets.top - insets.bottom);
            ((Graphics2D) g).setPaint(paint);
            if (image != null) {
                g.drawImage(image, insets.left, insets.top, null);
            }
        }

        @Override
        public void updateUI() {
            super.updateUI();
            color1 = UIManager.getColor("Panel.background"); // NOT LOCALIZABLE
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(color1.darker(), 1));
        }

        public void updateIndexes() {
            removeAll();
            if (model == null) {
                return;
            }

            int count = model.getPageCount();

            for (int i = 0; i < count; i++) {
                Page page = model.getPage(i);
                if (page != null) {
                    JLabel label = new JLabel(page.getTitle());
                    label.setOpaque(false);
                    if (i == model.getPageID()) {
                        Font font = label.getFont();
                        label.setFont(new Font(font.getFontName(), font.getStyle() | Font.BOLD,
                                font.getSize()));
                    }
                    add(label, new GridBagConstraints(0, i, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL, new Insets(6, 6, 0, 6), 0, 0));
                }
            }
            add(Box.createVerticalGlue(), new GridBagConstraints(0, count, 1, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            invalidate();
            revalidate();
            repaint();
        }
    }

    private IndexPanel indexPanel = new IndexPanel();

    private JPanel optionsPanel;

    private Action nextAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            next();
        }
    };

    private Action previousAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            previous();
        }
    };

    private Action cancelAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (confirmCancel()) {
                cancel();
                dispose();
            }
        }
    };

    private Action finishAction = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent e) {
            finishAction.setEnabled(false);
            cancelAction.setEnabled(false);
            previousAction.setEnabled(false);
            nextAction.setEnabled(false);
            SwingUtilities.invokeLater(finishRunnable);
        }
    };

    private Runnable finishRunnable = new Runnable() {

        @Override
        public void run() {
            try {
                getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                finish();
            } catch (Exception ex) {
                ExceptionHandler.processUncatchedException(Wizard.this, ex);
            } finally {
                getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                dispose();
            }
        }

    };

    private volatile boolean pendingUdates = false;

    private Runnable updateRunnable = new Runnable() {

        @Override
        public void run() {
            pendingUdates = false;
            finishAction.setEnabled(true);
            cancelAction.setEnabled(true);
            if (model == null || model.getPageCount() == 0) {
                previousAction.setEnabled(false);
                finishButton.setVisible(false);
                nextButton.setVisible(true);
                nextAction.setEnabled(false);
            } else {
                previousAction.setEnabled(model.previousEnabled());
                finishButton.setVisible(model.getPageID() == (model.getPageCount() - 1));
                nextButton.setVisible(!finishButton.isVisible());
                finishButton.setEnabled(model.finishEnabled());
                nextAction.setEnabled(model.nextEnabled());
            }

            String message = model == null ? null : model.getWarning();
            warningLabel.setVisible(message != null && message.trim().length() > 0);
            warningLabel.setText(message);
        }
    };

    private JButton previousButton = new JButton(previousAction);
    private JButton nextButton = new JButton(nextAction);
    private JButton cancelButton = new JButton(cancelAction);
    private JButton finishButton = new JButton(finishAction);
    private JButton[] buttons = new JButton[] { previousButton, nextButton, finishButton,
            cancelButton };
    private JButton[] possibleButtons = new JButton[] { previousButton, nextButton, cancelButton,
            finishButton };

    private WizardModel model = null;

    private boolean changed = false;

    private JLabel warningLabel = new JLabel() {

        @Override
        public void updateUI() {
            super.updateUI();

            Icon icon = UIManager.getIcon("OptionPane.warningIcon"); // NOT LOCALIZABLE
            if (icon != null && (icon.getIconWidth() != 16 || icon.getIconHeight() != 16)) {
                BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = image.createGraphics();
                icon.paintIcon(this, g, 0, 0);
                g.dispose();
                Image scaledImage = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaledImage));
            } else
                setIcon(icon);

            //            Font font = getFont();
            //            setFont(new Font(font.getName(), font.getStyle() | Font.BOLD, font.getSize()));

            setOpaque(true);

            Color color = UIManager.getColor("Panel.background"); // NOT LOCALIZABLE

            Border border = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color
                    .darker(), 1), BorderFactory.createEmptyBorder(2, 3, 2, 2));
            setBorder(border);

            setBackground(UIManager.getColor("ToolTip.background")); // NOT LOCALIZABLE
            setForeground(UIManager.getColor("ToolTip.foreground")); // NOT LOCALIZABLE
        }

        @Override
        public void setText(String text) {
            if (text != null && !text.trim().startsWith("<html>")) {
                text = "<html><body>" + text + "</body></html>";
            }
            super.setText(text);
        }

    };

    private WizardModelListener modelListener = new WizardModelListener() {

        @Override
        public void activePageChanged() {
            setActivePage(model.getPageID());
            updateStates();
            indexPanel.updateIndexes();
        }

        @Override
        public void structureChanged() {
            setActivePage(model.getPageID());
            updateStates();
            indexPanel.updateIndexes();
        }

        @Override
        public void stateChanged() {
            updateStates();
        }

    };

    public Wizard() {
        init();
        initControlComponents();
    }

    public Wizard(Frame owner) {
        super(owner, true);
        init();
        initControlComponents();
    }

    public Wizard(Dialog owner) {
        super(owner, true);
        init();
        initControlComponents();
    }

    public Wizard(Window owner) {
        super(owner);
        if (owner instanceof Frame || owner instanceof Dialog)
            setModal(true);
        init();
        initControlComponents();
    }

    private void initControlComponents() {
        previousButton.setEnabled(false);
        nextButton.setEnabled(true);
        finishButton.setVisible(false);
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        getContentPane().setLayout(new GridBagLayout());

        nextAction.putValue(AbstractAction.NAME, NEXT + " >"); // NOT LOCALIZABLE
        previousAction.putValue(AbstractAction.NAME, "< " + PREVIOUS); // NOT LOCALIZABLE
        cancelAction.putValue(AbstractAction.NAME, CANCEL);
        finishAction.putValue(AbstractAction.NAME, kFinish);

        mainPanel.setLayout(new BorderLayout());

        AwtUtil.normalizeComponentDimension(buttons, possibleButtons);

        controlBtnPanel.add(Box.createHorizontalGlue(),
                new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        controlBtnPanel.add(previousButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
        controlBtnPanel.add(nextButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
        controlBtnPanel.add(finishButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 6), 0, 0));
        controlBtnPanel.add(Box.createHorizontalGlue(),
                new GridBagConstraints(4, 0, 1, 1, 0.2, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        controlBtnPanel.add(cancelButton, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        JPanel panelLeftRight = new JPanel(new BorderLayout());
        JPanel panelTopBottom = new JPanel(new BorderLayout());

        panelLeftRight.add(indexPanel, BorderLayout.WEST);
        panelLeftRight.add(panelTopBottom, BorderLayout.CENTER);

        panelTopBottom.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));

        panelTopBottom.add(mainPanel, BorderLayout.CENTER);
        panelTopBottom.add(warningLabel, BorderLayout.SOUTH);

        getContentPane().add(
                panelLeftRight,
                new GridBagConstraints(0, 0, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(6, 6, 6, 6), 0, 0));
        getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));

        setSize(AwtUtil.getBestDialogSize());

    }

    private void setActivePage(int page) {
        Page newpage = null;
        Page currentpage = (Page) (mainPanel.getComponentCount() == 0 ? null : mainPanel
                .getComponent(0));
        if (page > -1) {
            newpage = model.getPage(page);
        }
        if (newpage != currentpage) {
            mainPanel.removeAll();
            mainPanel.add((Component) newpage, BorderLayout.CENTER);
            synchronized(getTreeLock()){
            	validateTree();
            }
            mainPanel.revalidate();
            mainPanel.repaint();
        }
    }

    public void setModel(WizardModel model) {
        if (this.model == model)
            return;
        if (this.model != null)
            this.model.removeWizardModelListener(modelListener);
        this.model = model;
        if (this.model != null)
            this.model.addWizardModelListener(modelListener);

        if (model != null) {
            if (model.getPageCount() > 0) {
                Page page = model.getPage(0);
                model.setPageID(0);
                try {
                    page.load();
                } catch (DbException e) {
                    dispose();
                    ExceptionHandler.processUncatchedException(this, e);
                }
            }
        }

        indexPanel.updateIndexes();
        updateStates();
    }

    public WizardModel getModel() {
        return model;
    }

    private void updateStates() {
        // We post to ensure we only evaluate the states once after a change occurs.
        // In many cases, multiple calls to updateStates() are done resulting from one change
        // in a page or in the model.
        if (pendingUdates)
            return;
        pendingUdates = true;
        SwingUtilities.invokeLater(updateRunnable);
    }

    private void changePage(final boolean forward) throws DbException {
        if (model == null)
            return;
        nextAction.setEnabled(false);
        previousAction.setEnabled(false);
        cancelAction.setEnabled(false);
        finishAction.setEnabled(false);

        // Post to ensure a repaint is performed on buttons' state before changing the page.
        // TODO We should perform the save and load on another thread instead.

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Component focusComponent = getFocusOwner();
                try {
                    getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    int pageID = model.getPageID();
                    if (pageID > -1 && forward) {
                        Page page = model.getPage(pageID);
                        if (page != null) {
                            page.save();
                        }
                    }
                    int newPageID = -1;
                    if (forward) { //Next
                        newPageID = model.getNextPage();
                    } else { // Previous
                        newPageID = model.getPreviousPage();
                    }

                    model.setPageID(newPageID);
                    if (newPageID > -1) {
                        Page page = model.getPage(newPageID);
                        if (page != null) {
                            if (forward)
                                page.load();
                            else
                                page.rollback();
                        }
                    }
                    setActivePage(newPageID);
                } catch (DbException e) {
                    ExceptionHandler.processUncatchedException(Wizard.this, e);
                    dispose();
                } finally {
                    updateStates();
                    getRootPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    if (forward && nextAction.isEnabled() && nextButton.isVisible()) {
                        nextButton.requestFocus();
                    } else if (!forward && previousAction.isEnabled() && previousButton.isVisible()) {
                        previousButton.requestFocus();
                    } else if (forward && finishAction.isEnabled() && finishButton.isVisible()) {
                        finishButton.requestFocus();
                    } else {
                        if (focusComponent != null
                                && SwingUtilities.getAncestorOfClass(Wizard.class, focusComponent) == Wizard.this) {
                            focusComponent.requestFocus();
                        } else if (model.getPageID() > -1) {
                            ((JComponent) model.getPage(model.getPageID())).transferFocus();
                        }
                    }
                }
            }
        });
    }

    private boolean confirmCancel() {
        if (!changed)
            return true;
        boolean cancel = false;
        int answer = JOptionPane.showConfirmDialog(this, kCancelOperation,
                ApplicationContext.getApplicationName(), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE); //NOT LOCALIZABLE
        if (answer == JOptionPane.YES_OPTION) {
            cancel = true;
        }
        return cancel;
    }

    protected void next() {
        try {
            changePage(true);
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
        }
    }

    protected void previous() {
        try {
            changePage(false);
        } catch (DbException e) {
            ExceptionHandler.processUncatchedException(this, e);
        }
    }

    protected abstract void cancel();

    protected abstract void finish();

    public JPanel getOptionsPanel() {
        return optionsPanel;
    }

    public void setOptionsPanel(JPanel optionsPanel) {
        if (this.optionsPanel != null) {
            getContentPane().remove(this.optionsPanel);
        }
        this.optionsPanel = optionsPanel;
        if (this.optionsPanel != null) {
            this.getContentPane().add(
                    optionsPanel,
                    new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                            GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0, 0));

        }
    }

    public Image getImage() {
        return indexPanel.getImage();
    }

    public void setImage(Image image) {
        indexPanel.setImage(image);
    }

}
