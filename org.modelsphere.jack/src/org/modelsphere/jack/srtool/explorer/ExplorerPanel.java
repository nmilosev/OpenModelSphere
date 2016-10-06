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

package org.modelsphere.jack.srtool.explorer;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.Header;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.preference.context.ContextManager;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.FocusManager;
import org.modelsphere.jack.srtool.international.LocaleMgr;

public class ExplorerPanel extends JPanel {
    private static final String kTitle = LocaleMgr.screen.getString("Explorer");

    // BEWARE: These constants represent directly the indices in the <Explorer>
    // sub-menu;
    // if it has to be changed, reflect the changes in ShowExplorerAction.
    public final static int EXPLORER_HIDE = 0;
    public final static int EXPLORER_SINGLE = 1;
    public final static int EXPLORER_TOP_BOTTOM = 2;
    public final static int EXPLORER_LEFT_RIGHT = 3;

    private Explorer explorer;
    private ExplorerView mainView;
    private ExplorerView secondView;
    private int visibility;

    private ExplorerContext context;

    private JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new EmptyPanel(),
            new EmptyPanel());
    private Header title = new Header(kTitle);

    private static class EmptyPanel extends JPanel {
        EmptyPanel() {
        }

        public final Color getBackground() {
            return UIManager.getColor("Tree.background"); // NOT LOCALIZABLE, property key
        }
    }

    public ExplorerPanel(Explorer explorer) {
        super(new BorderLayout());

        FocusManager.getSingleton().addCurrentFocusListener(new CurrentFocusListener() {

            @Override
            public void currentFocusChanged(Object oldFocusObject, Object focusObject)
                    throws DbException {
                title.setSelected(focusObject != null
                        && (focusObject == mainView || focusObject == secondView));
            }
        });

        this.setName("ExplorerPanel"); //NOT LOCALIZABLE, for QA

        add(title, BorderLayout.NORTH);
        add(splitPanel, BorderLayout.CENTER);
        this.explorer = explorer;
        splitPanel.setName("ExplorerSplitPanel"); //NOT LOCALIZABLE, for QA
        mainView = new ExplorerView(explorer);
        mainView.setName("ExplorerMainView"); //NOT LOCALIZABLE, for QA
        secondView = new ExplorerView(explorer);
        secondView.setName("ExplorerSecondView"); //NOT LOCALIZABLE, for QA
        visibility = EXPLORER_HIDE;
        splitPanel.setLeftComponent(mainView.getViewPanel());

        splitPanel.setBorder(null);
        setVisible(false);

        context = new ExplorerContext(this);
        ContextManager.getInstance().registerComponent(context);
    } //end ExplorerPanel

    public final Explorer getExplorer() {
        return explorer;
    }

    public final ExplorerView getMainView() {
        return mainView;
    }

    public final ExplorerView getSecondView() {
        return secondView;
    }

    public final boolean hasFocusState() {
        return mainView.hasFocusState() || secondView.hasFocusState();
    }

    public final int getVisibility() {
        return visibility;
    }

    public final void setVisibility(int newVisibility) {
        if (visibility == newVisibility)
            return;
        if (newVisibility == EXPLORER_HIDE) {
            if (hasFocusState())
                ApplicationContext.getFocusManager().setFocusToExplorer(null);
            setVisible(false);
            explorer.hideContent();
        } else {
            if (newVisibility == EXPLORER_SINGLE) {
                if (secondView.hasFocusState())
                    ApplicationContext.getFocusManager().setFocusToExplorer(null);
                splitPanel.setRightComponent(null);
                splitPanel.setOneTouchExpandable(false);
                splitPanel.setDividerSize(0);
                splitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
            } else {
                splitPanel
                        .setOrientation(newVisibility == EXPLORER_TOP_BOTTOM ? JSplitPane.VERTICAL_SPLIT
                                : JSplitPane.HORIZONTAL_SPLIT);
                splitPanel.setRightComponent(secondView.getViewPanel());
                splitPanel.setOneTouchExpandable(true);
                splitPanel.setDividerSize(5);
                splitPanel.setDividerLocation(0.50);
            }
            if (visibility == EXPLORER_HIDE) {
                explorer.showContent();
                mainView.expandDbNodes();
                secondView.expandDbNodes();
                setVisible(true);
            }
        }
        visibility = newVisibility;
        splitPanel.invalidate();
        synchronized(getTreeLock()){
        	validateTree();
        }
    }

    public void updateUI() {
        super.updateUI();
        if (splitPanel == null)
            return;
        if (splitPanel.getLeftComponent() == null && mainView != null)
            mainView.updateUI();
        if (splitPanel.getRightComponent() == null && secondView != null)
            secondView.updateUI();
    }

}
