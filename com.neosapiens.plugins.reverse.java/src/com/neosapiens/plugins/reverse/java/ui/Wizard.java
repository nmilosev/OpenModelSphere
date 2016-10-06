/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package com.neosapiens.plugins.reverse.java.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;

import com.neosapiens.plugins.reverse.java.international.LocaleMgr;

public class Wizard<P extends WizardParameters> {
    private JPanel cards;
    private WizardNavigationPanel navigation;
    private CardLayout layout;
    private int nbPages = 0;
    private int currentPage = 0;
    private int MIN_HEIGHT = 300;
    private int FIX_WIDTH = 500;
    Map<String, WizardPage> pages = new HashMap<String, WizardPage>();

    private JDialog dialog;

    public JDialog getDialog() {
        return dialog;
    }

    private P m_parameters;

    public P getParameters() {
        return m_parameters;
    }

    public Wizard(P parameters) {
        layout = new CardLayout();
        cards = new JPanel(layout);
        pages.clear();
        m_parameters = parameters;
    }

    void addPage(WizardPage page) {
        String id = Integer.toString(nbPages);
        cards.add(page, id);
        pages.put(id, page);
        nbPages++;
    }

    public void showOpenDialog(JFrame frame, String title) {
        boolean modal = true;
        dialog = new JDialog(frame, title, modal);
        dialog.setLayout(new BorderLayout());
        navigation = new WizardNavigationPanel(this, dialog);

        dialog.add(cards, BorderLayout.CENTER);
        dialog.add(navigation, BorderLayout.SOUTH);

        showPage(0);
        dialog.pack();
        int height = Math.max(MIN_HEIGHT, dialog.getSize().height);
        dialog.setSize(FIX_WIDTH, height);

        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
    }

    public void showPage(int newPage) {
        //terminate current page
        String id = Integer.toString(currentPage);
        WizardPage page = pages.get(id);
        page.afterShowing();

        //set new page 
        currentPage = newPage;

        //show new page
        id = Integer.toString(newPage);
        page = pages.get(id);
        page.beforeShowing();
        layout.show(cards, id);
        updateNavigationButton();
        page.onShowing();
    }

    public void previousPage() {
        showPage(currentPage - 1);
    }

    public void nextPage() {
        showPage(currentPage + 1);
    }

    public void updateNavigationButton() {
        navigation.updateButtons(currentPage, nbPages);
    }

    public void canGoNextPage(boolean b) {
        navigation.canGoNextPage(b);
    }

    //
    // inner class
    //
    private static class WizardNavigationPanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 1L;
        private Wizard<? extends WizardParameters> m_wizard;
        private JButton previousButton, nextButton, proceedButton, cancelButton;
        private JDialog m_parent;

        public WizardNavigationPanel(Wizard<? extends WizardParameters> wizard, JDialog parent) {
            m_wizard = wizard;
            m_parent = parent;

            int y = 0;
            int h = 1;
            double wx = 0.0, wy = 1.0;
            int nofill = GridBagConstraints.NONE;
            Insets insets = new Insets(3, 3, 3, 3);

            this.setLayout(new GridBagLayout());

            String back = "< " + LocaleMgr.misc.getString("Back");
            String next = LocaleMgr.misc.getString("Next") + " >";
            String proceed = LocaleMgr.misc.getString("Proceed");
            String cancel = LocaleMgr.misc.getString("Cancel");

            previousButton = new JButton(back);
            this.add(previousButton, new GridBagConstraints(0, y, 1, h, 1.0, wy,
                    GridBagConstraints.SOUTHEAST, nofill, insets, 0, 0));

            nextButton = new JButton(next);
            this.add(nextButton, new GridBagConstraints(1, y, 1, h, wx, wy,
                    GridBagConstraints.SOUTHEAST, nofill, insets, 0, 0));

            proceedButton = new JButton(proceed);
            this.add(proceedButton, new GridBagConstraints(2, y, 1, h, wx, wy,
                    GridBagConstraints.SOUTHEAST, nofill, new Insets(3, 24, 3, 3), 0, 0));

            cancelButton = new JButton(cancel);
            this.add(cancelButton, new GridBagConstraints(3, y, 2, h, wx, wy,
                    GridBagConstraints.SOUTHEAST, nofill, insets, 0, 0));

            AwtUtil.normalizeComponentDimension(new JComponent[] { previousButton, nextButton,
                    cancelButton });

            previousButton.addActionListener(this);
            nextButton.addActionListener(this);
            proceedButton.addActionListener(this);
            cancelButton.addActionListener(this);
        }

        public void canGoNextPage(boolean b) {
            nextButton.setEnabled(b);
        }

        public void updateButtons(int currentPage, int nbPages) {

            String id = Integer.toString(currentPage);
            Map<String, WizardPage> pages = m_wizard.pages;
            WizardPage page = pages.get(id);
            boolean canContinue = page.canContinue();

            previousButton.setEnabled(currentPage > 0);
            nextButton.setEnabled(canContinue && (currentPage < (nbPages - 1)));
            proceedButton.setEnabled(canContinue && (currentPage == (nbPages - 1)));
        }

        public void actionPerformed(ActionEvent ev) {
            Object src = ev.getSource();

            if (src.equals(previousButton)) {
                m_wizard.previousPage();
            } else if (src.equals(nextButton)) {
                m_wizard.nextPage();
            } else if (src.equals(proceedButton)) {
                //terminate current page
                String id = Integer.toString(m_wizard.currentPage);
                m_wizard.pages.get(id);
                WizardPage page = (WizardPage) m_wizard.pages.get(id);
                page.afterShowing();

                m_wizard.m_parameters.status = WizardParameters.Status.FINISHED;
                m_parent.setVisible(false);
            } else if (src.equals(cancelButton)) {
                m_wizard.m_parameters.status = WizardParameters.Status.CANCELED;
                m_parent.setVisible(false);
            } //end if

        } //actionPerformed()
    }
} //end Wizard
