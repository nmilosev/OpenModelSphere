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

package org.modelsphere.jack.graphic.tool;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.db.event.DbRefreshPassListener;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;

/**
 * A ToolButtonGroup is Tool container. It is also an AWT Component that can be added to any AWT
 * container to display a tool button group. Every tool in the ToolButtonGroup can be selected by
 * the user. When a tool become selected, it became the current tool of the tool diagram.
 * 
 * @author Christian Gauthier
 * @version 1.0
 */
public class ToolButtonGroup implements ActionListener, CurrentFocusListener {

    private static final String MASTER_TOOL_TIP = " " + LocaleMgr.message.getString("MasterTool");

    private DiagramView diagView = null;
    private DbSemanticalObject diagPackage = null;
    private ArrayList origTools = new ArrayList();
    private ArrayList toolComponents = new ArrayList();
    private int origMaster = 0;

    public ToolButtonGroup() {
        super();
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);

        Db.addDbRefreshPassListener(new DbRefreshPassListener() {

            @Override
            public void afterRefreshPass(Db db) throws DbException {
                if (diagView != null) {
                    Tool tool = diagView.getCurrentTool();
                    try {
                        tool.updateHelp();
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void beforeRefreshPass(Db db) throws DbException {
            }
        });
    }

    public final JComponent addTool(Tool tool, boolean master) {
        JComponent comp = tool.createUIComponent();
        if (!(comp instanceof ToolComponent)) {
            org.modelsphere.jack.debug.Debug.assert2(false,
                    "ToolButtonGroup:  Tool component must be instanceof ToolComponent."); // NOT LOCALIZABLE - debug only
            return null;
        } else
            ((ToolComponent) comp).addActionListener(this);

        tool.addMasterActivationListener(new MasterActivationListener() {

            @Override
            public void masterActivated(Tool tool) {
                setMasterTool(origTools.indexOf(tool));
            }
        });
        // Set a windows id for QA
        if (comp.getName() == null)
            comp.setName(tool.getClass().getName());

        if (tool.supportedDiagramMetaClasses != null && !tool.isAlwaysVisible())
            comp.setVisible(false);
        else
            comp.setVisible(true);
        //add(comp);
        origTools.add(tool);
        toolComponents.add(comp);
        if (master)
            origMaster = origTools.size() - 1;
        comp.setEnabled(false);
        ((ToolComponent) comp).setState(ToolComponent.STATE_NORMAL);
        return comp;
    }

    public final JComponent getComponentFor(Tool tool) {
        int idx = origTools.indexOf(tool);
        if (idx == -1)
            return null;
        return (JComponent) toolComponents.get(idx);
    }

    public final Tool[] getOrigTools(DiagramView diagView) {
        Tool[] tools = new Tool[origTools.size()];
        origTools.toArray(tools);
        for (int i = 0; i < tools.length; i++) {
            tools[i] = (Tool) tools[i].clone();
            tools[i].setDiagramView(diagView);
        }
        return tools;
    }

    public final int getOrigMaster() {
        return origMaster;
    }

    private boolean init = false;
    private HashMap diagramPackagesCompVisibility = new HashMap();
    private HashMap diagramPackagesCompEnabled = new HashMap();

    public void setVisible(int index, boolean visible) {
        MetaClass diagPackageMetaClass = diagPackage.getMetaClass();
        boolean[] componentsVisible = (boolean[]) this.diagramPackagesCompVisibility
                .get(diagPackageMetaClass);
        componentsVisible[index] = visible;
        diagramPackagesCompVisibility.put(diagPackageMetaClass, componentsVisible);
    }

    public void setEnabled(int index, boolean visible) {
        MetaClass diagPackageMetaClass = diagPackage.getMetaClass();
        boolean[] componentsEnabled = (boolean[]) diagramPackagesCompEnabled
                .get(diagPackageMetaClass);
        componentsEnabled[index] = visible;
        diagramPackagesCompEnabled.put(diagPackageMetaClass, componentsEnabled);
    }

    private void updateVisibility() {
        if (diagPackage == null)
            return;

        MetaClass diagPackageMetaClass = diagPackage.getMetaClass();
        boolean[] componentsVisibility = (boolean[]) diagramPackagesCompVisibility
                .get(diagPackageMetaClass);
        boolean[] componentsEnabled = (boolean[]) diagramPackagesCompEnabled
                .get(diagPackageMetaClass);
        if (componentsVisibility == null) {
            componentsVisibility = new boolean[toolComponents.size()];
            componentsEnabled = new boolean[toolComponents.size()];
            for (int i = 0; i < toolComponents.size(); i++) {
                Component comp = (Component) toolComponents.get(i);
                Tool tool = (Tool) origTools.get(i);
                MetaClass[] supported = tool.supportedDiagramMetaClasses;
                if (supported == null) {
                    componentsVisibility[i] = true;
                    componentsEnabled[i] = true;
                    continue;
                }
                boolean visible = false;
                for (int j = 0; j < supported.length && !visible; j++) {
                    if (!supported[j].isAssignableFrom(diagPackageMetaClass))
                        continue;
                    visible = true;
                }
                componentsVisibility[i] = tool.isAlwaysVisible() || visible;
                componentsEnabled[i] = visible;
            }
            diagramPackagesCompVisibility.put(diagPackageMetaClass, componentsVisibility);
            diagramPackagesCompEnabled.put(diagPackageMetaClass, componentsEnabled);
        }
        Tool[] tools = diagView.getTools();
        for (int i = 0; i < toolComponents.size(); i++) {
            Component comp = (Component) toolComponents.get(i);
            comp.setEnabled(componentsEnabled[i]);
            comp.setVisible(componentsVisibility[i]);

            Tool oriTool = tools[i];
            String sToolTip = oriTool.getText();
            ((JComponent) comp).setToolTipText(sToolTip);
            if (comp instanceof DefaultToolComponent)
                ((DefaultToolComponent) comp).setIcon(new ImageIcon(oriTool.getIcon()));
        }
        diagView.setTools(tools);
    }

    public final void setDiagramView(DiagramView newDiagView) {
        if (diagView == newDiagView)
            return;

        if (diagView != null) {
            setSelectDrawing(diagView.getCurrentToolInd(), false);
            setMasterDrawing(diagView.getMasterToolInd(), false);
        }

        if (diagView != null) {
            Tool tool = diagView.getCurrentTool();
            if (tool != null)
                tool.toolDesactivated();
        }

        diagView = newDiagView;
        if (diagView != null) {
            setAllEnabled(true);
            setMasterDrawing(diagView.getMasterToolInd(), true);
            setSelectDrawing(diagView.getCurrentToolInd(), true);
        } else
            setAllEnabled(false);

        // Restore auxiliary selection value for the components
        if (diagView != null) {
            Tool[] tools = diagView.getTools();
            if (tools != null) {
                for (int i = 0; i < tools.length; i++) {
                    ToolComponent comp = (ToolComponent) toolComponents.get(i);
                    comp.setAuxiliaryIndex(tools[i].getAuxiliaryIndex());
                }
            }
        }

        if (diagView != null) {
            Tool tool = diagView.getCurrentTool();
            if (tool != null)
                tool.toolActivated();
        }

    }

    public final void setSelectedTool(int ind) {
        setSelectDrawing(diagView.getCurrentToolInd(), false);
        setSelectDrawing(ind, true);
        diagView.setCurrentTool(ind);
    }

    public final void setSelectedTool(DiagramView view, int ind) {
        setSelectDrawing(view.getCurrentToolInd(), false);
        if (view == diagView)
            setSelectDrawing(ind, true);
        view.setCurrentTool(ind);
    }

    public final void setMasterTool(int ind) {
        setMasterDrawing(diagView.getMasterToolInd(), false);
        setMasterDrawing(ind, true);
        diagView.setMasterTool(ind);
    }

    public final void setMasterTool(DiagramView view, int ind) {
        setMasterDrawing(view.getMasterToolInd(), false);
        if (view == diagView)
            setMasterDrawing(ind, true);
        view.setMasterTool(ind);
    }

    private void setSelectDrawing(int ind, boolean selected) {
        ToolComponent comp = (ToolComponent) toolComponents.get(ind);
        int newstate = comp.getState();
        if (selected) {
            newstate = ToolComponent.STATE_SELECTED
                    | (comp.getState() & ToolComponent.STATE_MASTER);
        } else {
            newstate = comp.getState() & ToolComponent.STATE_MASTER;
        }
        comp.setState(newstate);
        updateToolTipText(comp);
    }

    private void setMasterDrawing(int ind, boolean master) {
        ToolComponent comp = (ToolComponent) toolComponents.get(ind);
        int newstate = comp.getState();
        if (master) {
            newstate = ToolComponent.STATE_MASTER
                    | (comp.getState() & ToolComponent.STATE_SELECTED);
        } else {
            newstate = comp.getState() & ToolComponent.STATE_SELECTED;
        }
        comp.setState(newstate);
        updateToolTipText(comp);
    }

    private void setAllEnabled(boolean value) {
        int nb = toolComponents.size();
        for (int i = 0; i < nb; i++) {
            JComponent comp = (JComponent) toolComponents.get(i);
            comp.setEnabled(value);
        }
    }

    private void updateToolTipText(ToolComponent comp) {
        JComponent c = (JComponent) comp;
        int state = comp.getState();
        String text = c.getToolTipText();
        if (text != null) {
            int idx = text.indexOf(ToolButtonGroup.MASTER_TOOL_TIP);
            if ((state == ToolComponent.STATE_MASTER) && (idx == -1))
                c.setToolTipText(c.getToolTipText() + ToolButtonGroup.MASTER_TOOL_TIP);
            else if ((state != ToolComponent.STATE_MASTER) && (idx != -1))
                c.setToolTipText(text.substring(0, text.indexOf(ToolButtonGroup.MASTER_TOOL_TIP)));
        }
    }

    // /////////////////////////////////////
    // CurrentFocusListener support
    //
    public void currentFocusChanged(Object oldFocusObject, Object focusObject) throws DbException {

        diagPackage = (focusObject instanceof ApplicationDiagram ? ((ApplicationDiagram) focusObject)
                .getSemanticalObject()
                : null);
        DiagramView diagView = (focusObject instanceof ApplicationDiagram ? ((ApplicationDiagram) focusObject)
                .getMainView()
                : null);
        setDiagramView(diagView);
        updateVisibility();

        if (!(focusObject instanceof ApplicationDiagram)) {
            // clear potential messages set by tools
            ApplicationContext.getDefaultMainFrame().getStatusBar().setMessage(null);
        }
    }

    //
    // End of CurrentFocusListener support
    // /////////////////////////////////////

    // ///////////////////////////////
    // ActionListener SUPPORT
    //
    public final void actionPerformed(ActionEvent e) {
        int idx = toolComponents.indexOf(e.getSource());

        // update auxiliary selection for the tool
        // I make this before setSelectedTool(idx) to ensure the diagView will
        // get the correct cursor from the tool
        Tool nextselectedtool = diagView.getTools()[idx];
        if (nextselectedtool != null) {
            ToolComponent comp = (ToolComponent) toolComponents.get(idx);
            nextselectedtool.setAuxiliaryIndex(comp.getAuxiliaryIndex());
        }

        setSelectedTool(idx);
    }
    //
    // End of ActionListener SUPPORT
    // ///////////////////////////////
}
