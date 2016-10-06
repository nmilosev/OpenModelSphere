package com.neosapiens.plugins.reverse.javasource.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.modelsphere.jack.awt.AwtUtil;

import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;


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
    private P parameters;
    
    
    public Wizard(P _parameters) {
        layout = new CardLayout();
        cards = new JPanel(layout);
        pages.clear();
        parameters = _parameters;
    }
    
    
    public JDialog getDialog() {
        return dialog;
    }
    
    
    public P getParameters() {
        return parameters;
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
        String id = Integer.toString(currentPage);
        WizardPage page = pages.get(id);
        page.afterShowing();
        currentPage = newPage;
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
    
    
    private static class WizardNavigationPanel extends JPanel implements ActionListener {
        private static final long serialVersionUID = 2L;
        private JButton previousButton, nextButton, proceedButton, cancelButton;
        private Wizard<? extends WizardParameters> wizard;
        private JDialog parent;
        
        
        public WizardNavigationPanel(Wizard<? extends WizardParameters> _wizard, JDialog _parent) {
            wizard = _wizard;
            parent = _parent;
            
            int y = 0;
            int h = 1;
            double wx = 0.0, wy = 1.0;
            int nofill = GridBagConstraints.NONE;
            Insets insets = new Insets(3, 3, 3, 3);
            
            this.setLayout(new GridBagLayout());
            
            String back = "< " + JavaSourceReverseLocaleMgr.misc.getString("Back");
            String next = JavaSourceReverseLocaleMgr.misc.getString("Next") + " >";
            String proceed = JavaSourceReverseLocaleMgr.misc.getString("Proceed");
            String cancel = JavaSourceReverseLocaleMgr.misc.getString("Cancel");
            
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
            Map<String, WizardPage> pages = wizard.pages;
            WizardPage page = pages.get(id);
            boolean canContinue = page.canContinue();

            previousButton.setEnabled(currentPage > 0);
            nextButton.setEnabled(canContinue && (currentPage < (nbPages - 1)));
            proceedButton.setEnabled(canContinue && (currentPage == (nbPages - 1)));
        }
        
        
        public void actionPerformed(ActionEvent ev) {
            Object src = ev.getSource();

            if (src.equals(previousButton)) {
                wizard.previousPage();
            } else if (src.equals(nextButton)) {
                wizard.nextPage();
            } else if (src.equals(proceedButton)) {
                String id = Integer.toString(wizard.currentPage);
                wizard.pages.get(id);
                WizardPage page = (WizardPage) wizard.pages.get(id);
                page.afterShowing();

                wizard.parameters.status = WizardParameters.Status.FINISHED;
                parent.setVisible(false);
            } else if (src.equals(cancelButton)) {
                wizard.parameters.status = WizardParameters.Status.CANCELED;
                parent.setVisible(false);
            }
        }
    }
}
