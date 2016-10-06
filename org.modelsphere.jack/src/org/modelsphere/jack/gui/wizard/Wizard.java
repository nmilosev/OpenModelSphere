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

package org.modelsphere.jack.gui.wizard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.util.ExceptionHandler;

public final class Wizard extends JDialog {
    // Constants used to identify the panel
    public static final int PAGE_NONE = -1;

    // Constants used to identify navigation directions
    private static final int NEXT = 1;
    private static final int PREVIOUS = -1;
    private static final int FIRST = 0;

    private static final String kPrevious = "< " + LocaleMgr.screen.getString("Previous");
    private static final String kNext = LocaleMgr.screen.getString("Next") + " >";
    private static final String kFinish = LocaleMgr.screen.getString("Proceed");
    private static final String kCancel = LocaleMgr.screen.getString("Cancel");
    // private static final String kMisc = LocaleMgr.screen.getString("Misc");

    // GUI Components
    private BorderLayout contentPaneLayout = new BorderLayout();
    private BorderLayout mainPanelLayout = new BorderLayout();
    private GridBagLayout navigationPanelLayout = new GridBagLayout();
    private JPanel mainPanel = new JPanel(mainPanelLayout);
    private JPanel navigationPanel = new JPanel(navigationPanelLayout);
    // private JButton miscButton = new JButton(kMisc);
    private JButton previousButton = new JButton(kPrevious);
    private JButton nextButton = new JButton(kNext);
    private JButton cancelButton = new JButton(kCancel);

    private WizardPage activePage;
    private int[] pages_sequence = new int[] {};
    private int activePageIndex = PAGE_NONE;
    private String sFinish = kFinish;

    private WizardModel model;

    public Wizard(WizardModel wizmodel) {
        this(wizmodel, null);
    }

    public Wizard(WizardModel wizmodel, String finishText) {
        super(ApplicationContext.getDefaultMainFrame(), true);
        if (wizmodel == null)
            throw new NullPointerException("Null WizardModel"); // NOT
        // LOCALIZABLE
        // Exception
        if (finishText != null && finishText.length() > 0)
            sFinish = finishText;
        initGUI();
        try {
            setWizardModel(wizmodel);
            AwtUtil.centerWindow(this);
        } catch (Exception e) {
            processException(e);
        }
    }

    public WizardModel getWizardModel() {
        return model;
    }

    public void setWizardModel(WizardModel model) throws Exception {
        if (this.model != model && model != null) {
            this.model = model;
            pages_sequence = model.getPagesSequence();
            changeActivePage(FIRST);
        }
    }

    private void initGUI() {
        setSize(new Dimension(640, 480));
        // miscButton.setEnabled(false);

        // Normalize the button dimension.
        JButton[] jButtonList = new JButton[] { previousButton, nextButton, cancelButton /*
                                                                                          * ,
                                                                                          * miscButton
                                                                                          */};
        AwtUtil.normalizeComponentDimension(jButtonList);

        Container contentPanel = getContentPane();
        contentPanel.setLayout(contentPaneLayout);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(navigationPanel, BorderLayout.SOUTH);

        /*
         * navigationPanel.add(miscButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
         * ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 12, 12, 6), 0, 0));
         */
        navigationPanel.add(Box.createHorizontalGlue(),
                new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        navigationPanel.add(previousButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 12, 0), 0, 0));
        navigationPanel.add(nextButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 12, 0), 0, 0));
        navigationPanel.add(cancelButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0, 0));

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelPerformed();
            }
        });
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    changeActivePage(NEXT);
                } catch (Exception ex) {
                    processException(ex);
                }
            }
        });
        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    changeActivePage(PREVIOUS);
                } catch (Exception ex) {
                    processException(ex);
                }
            }
        });

        getRootPane().setDefaultButton(nextButton);
    }

    private void processException(Exception e) {
        dispose();
        model = null;
        ExceptionHandler.processUncatchedException(null, e);
    }

    private void cancelPerformed() {
        dispose();
        model.cancel();
        model = null;
    }

    private void changeActivePage(int direction) throws Exception {
        if (direction == FIRST) { // TODO switch on the first page
            removeActivePage(false);
            activePageIndex = 0;
            activePage = model.getPage(pages_sequence[activePageIndex]);
            setTitle(model.getTitle(pages_sequence[activePageIndex]));
            nextButton.setEnabled(false);
            mainPanel.add(activePage, BorderLayout.CENTER);
            activePage.initialize(model.getConfiguration());
            model.afterPageChange(pages_sequence[activePageIndex]);
            updateNavigationButtons();
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if (activePageIndex + direction == pages_sequence.length) { // finish
            if (!removeActivePage(direction == NEXT)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                return;
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            finishPerformed();
            return;
        }

        int page = pages_sequence[activePageIndex];
        if ((activePageIndex > -1) && !model.beforePageChange(page)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        if (!removeActivePage(direction == NEXT)) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        pages_sequence = model.getPagesSequence();
        activePageIndex = activePageIndex + direction;
        activePage = model.getPage(pages_sequence[activePageIndex]);
        if (activePage != null) {
            nextButton.setEnabled(false);
            mainPanel.add(activePage, BorderLayout.CENTER);
            activePage.initialize(model.getConfiguration());
        }
        setTitle(model.getTitle(pages_sequence[activePageIndex]));
        mainPanel.revalidate();

        if (!model.afterPageChange(pages_sequence[activePageIndex])) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            return;
        }

        updateNavigationButtons();
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }

    final void setNextEnabled(boolean b) {
        nextButton.setEnabled(b);
    }

    public void updateNavigationButtons() {
        if (activePageIndex == 0) { // FIRST PAGE
            previousButton.setEnabled(false);
            nextButton.setText(kNext);
            return;
        }
        if (activePageIndex == (pages_sequence.length - 1)) { // LAST PAGE
            previousButton.setEnabled(true);
            nextButton.setText(sFinish);
            return;
        }

        // MIDDLE PAGE
        previousButton.setEnabled(true);
        nextButton.setText(kNext);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private boolean removeActivePage(boolean saveData) {
        if (activePage != null) {
            if (activePage.terminate(model.getConfiguration(), saveData)) {
                mainPanel.remove(activePage);
                activePage = null;
            } else
                return false;
        }
        return true;
    }

    private void finishPerformed() {
        dispose();
        model.finish();
        model = null;
    }

}
