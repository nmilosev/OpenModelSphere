/*************************************************************************

Copyright (C) 2012 Grandite

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
package org.modelsphere.jack.srtool.features.layout.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import org.modelsphere.jack.actions.AbstractApplicationAction;
import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginDescriptor;
import org.modelsphere.jack.plugins.PluginMgr;
import org.modelsphere.jack.plugins.PluginsRegistry;
import org.modelsphere.jack.plugins.xml.XmlPlugin;
import org.modelsphere.jack.plugins.xml.XmlPluginDescriptor;
import org.modelsphere.jack.srtool.features.layout.*;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.util.DefaultComparableElement;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 * The Class LayoutDialog.
 */
@SuppressWarnings("serial")
public class LayoutDialog extends JDialog implements ActionListener {

    /** The ok button. */
    private JButton okButton;

    /** The cancel button. */
    private JButton cancelButton;

    /** The diagram. */
    private ApplicationDiagram diagram;

    /** The nodes combo. */
    private JComboBox nodesCombo;

    /** The edges combo. */
    private JComboBox edgesCombo;

    /** The clusters combo. */
    private JComboBox clustersCombo;

    /** The expand combo. */
    private JComboBox expandCombo;

    private JComboBox scopeCombo;

    /** The randomize check box. */
    private JCheckBox randomizeCheckBox;

    /** The default algorithms. */
    private List<DefaultComparableElement> defaultAlgorithms;
    
    private AbstractApplicationAction starLayoutAction;

    /** The buttons listener. */
    private ActionListener buttonsListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == okButton) {
                performLayout();
            }
            dispose();
        }
    };

    /**
     * Instantiates a new layout dialog.
     * 
     * @param parent
     *            the parent
     * @param diagram
     *            the diagram
     */
    public LayoutDialog(Window parent, ApplicationDiagram diagram, boolean selection, 
    		AbstractApplicationAction starLayoutAction, NodesLayoutAlgorithm starLayoutAlgorithm) {
        super(parent, LocaleMgr.screen.getString("diagramLayout"));
        if (parent instanceof Frame) {
            setModal(true);
        }

        this.diagram = diagram;
        this.starLayoutAction = starLayoutAction;

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        init(selection, starLayoutAlgorithm);

        setSize(600, 400);
        AwtUtil.centerWindow(this);
    }

    /**
     * Inits the.
     */
    private void init(boolean selection, NodesLayoutAlgorithm starLayoutAlgorithm) {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        JPanel optionsPanel = new JPanel(new GridBagLayout());

        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        okButton = new JButton(LocaleMgr.screen.getString("OK"));
        cancelButton = new JButton(LocaleMgr.screen.getString("cancel"));
        AwtUtil.normalizeComponentDimension(new JButton[] { okButton, cancelButton });

        okButton.addActionListener(buttonsListener);
        cancelButton.addActionListener(buttonsListener);

        DefaultComboBoxModel model = new DefaultComboBoxModel(new Object[] {
                new DefaultComparableElement(0, LocaleMgr.screen.getString("none")),
                new DefaultComparableElement(1, LocaleMgr.screen.getString("horizontally")),
                new DefaultComparableElement(2, LocaleMgr.screen.getString("vertically")),
                new DefaultComparableElement(3, LocaleMgr.screen.getString("both")) });
        expandCombo = new JComboBox(model);
        expandCombo.setEditable(false);
        expandCombo.setSelectedIndex(3);
        JLabel expandLabel = new JLabel(LocaleMgr.screen.getString("allowDiagramExpansion") + ":");

        model = new DefaultComboBoxModel(new Object[] {
                new DefaultComparableElement(0, LocaleMgr.screen.getString("selection")),
                new DefaultComparableElement(1, LocaleMgr.screen.getString("entireDiagram")) });
        scopeCombo = new JComboBox(model);
        scopeCombo.setEditable(false);
        scopeCombo.setSelectedIndex(selection ? 0 : 1);
        JLabel scopeLabel = new JLabel(LocaleMgr.screen.getString("scope") + ":");

        randomizeCheckBox = new JCheckBox(LocaleMgr.screen.getString("randomize"));
        randomizeCheckBox.setSelected(true);

        ArrayList<DefaultComparableElement> nodeAlgos = new ArrayList<DefaultComparableElement>();
        ArrayList<DefaultComparableElement> edgeAlgos = new ArrayList<DefaultComparableElement>();
        ArrayList<DefaultComparableElement> clusterAlgos = new ArrayList<DefaultComparableElement>();
        
        //add default
        DefaultComparableElement element = 
        	new DefaultComparableElement(starLayoutAlgorithm, starLayoutAlgorithm.getText());
        nodeAlgos.add(element);

        defaultAlgorithms = new ArrayList<DefaultComparableElement>();
        PluginMgr mgr = PluginMgr.getSingleInstance();
        PluginsRegistry registry = mgr.getPluginsRegistry(); 
        List<LayoutPlugin> plugins = getEnabledPluginsInstancesOf(registry, LayoutPlugin.class);
        //List<LayoutPlugin> plugins = registry.getActivePluginInstances(LayoutPlugin.class);
       
        for (LayoutPlugin layoutPlugin : plugins) {
        	
            List<LayoutAlgorithm> algos = layoutPlugin.getLayoutAlgorithms();
            List<LayoutAlgorithm> defaults = layoutPlugin.getDefaultLayoutAlgorithms();

            for (LayoutAlgorithm layoutAlgorithm : algos) {

                String text = layoutAlgorithm.getText();
                if (text == null || text.trim().length() == 0) {
                    text = layoutAlgorithm.getClass().getSimpleName();
                }

                // TODO : this will not work if there are many layout plugins
                element = new DefaultComparableElement(layoutAlgorithm, text);
                if (defaults.contains(layoutAlgorithm)) {
                    defaultAlgorithms.add(element);
                }

                if (layoutAlgorithm instanceof NodesLayoutAlgorithm) {
                    nodeAlgos.add(element);
                } else if (layoutAlgorithm instanceof EdgesLayoutAlgorithm) {
                    edgeAlgos.add(element);
                } else if (layoutAlgorithm instanceof ClustersLayoutAlgorithm) {
                    clusterAlgos.add(element);
                }
            }
        }
        Collections.sort(nodeAlgos);
        Collections.sort(edgeAlgos);
        Collections.sort(clusterAlgos);

        model = new DefaultComboBoxModel(nodeAlgos.toArray());
        nodesCombo = new JComboBox(model);
        nodesCombo.setEditable(false);

        model = new DefaultComboBoxModel(edgeAlgos.toArray());
        edgesCombo = new JComboBox(model);
        edgesCombo.setEditable(false);

        model = new DefaultComboBoxModel(clusterAlgos.toArray());
        clustersCombo = new JComboBox(model);
        clustersCombo.setEditable(false);

        selectDefaultAlgorithms();

        optionsPanel.add(new JLabel(LocaleMgr.screen.getString("algorithms")),
                new GridBagConstraints(0, 1, 2, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        optionsPanel.add(new JLabel(LocaleMgr.screen.getString("classifiersDisposition") + ":"),
                new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(nodesCombo, new GridBagConstraints(1, 2, 1, 1, 0.7, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6), 0,
                0));

        if (edgeAlgos.size() > 0) {
            // TODO determine if edge algo are relevant or if they should be merged with the nodes algos.
            // For now we hide these algo since we don't have any.
            optionsPanel.add(new JLabel(LocaleMgr.screen.getString("linesDisposition") + ":"),
                    new GridBagConstraints(0, 3, 1, 1, 0, 0, GridBagConstraints.WEST,
                            GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
            optionsPanel.add(edgesCombo, new GridBagConstraints(1, 3, 1, 1, 0.7, 0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6),
                    0, 0));
        }

        optionsPanel.add(new JLabel(LocaleMgr.screen.getString("clustersDisposition") + ":"),
                new GridBagConstraints(0, 4, 1, 1, 0, 0, GridBagConstraints.WEST,
                        GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(clustersCombo, new GridBagConstraints(1, 4, 1, 1, 0.7, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6), 0,
                0));

        optionsPanel.add(new JLabel(LocaleMgr.screen.getString("options")), new GridBagConstraints(
                0, 8, 2, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12,
                        0, 6, 6), 0, 0));

        optionsPanel.add(scopeLabel, new GridBagConstraints(0, 9, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(scopeCombo, new GridBagConstraints(1, 9, 1, 1, 0.2, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        optionsPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 9, 1, 1, 0.8, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6), 0,
                0));

        optionsPanel.add(expandLabel, new GridBagConstraints(0, 10, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(expandCombo, new GridBagConstraints(1, 10, 1, 1, 0.2, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 6), 0, 0));
        optionsPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 10, 1, 1, 0.8, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 6, 6), 0,
                0));

        optionsPanel.add(randomizeCheckBox, new GridBagConstraints(0, 11, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 24, 6, 6), 0, 0));
        optionsPanel.add(Box.createVerticalGlue(), new GridBagConstraints(0, 15, 4, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        buttonsPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(0, 0, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0,
                0));
        buttonsPanel.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));
        buttonsPanel.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0, 0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 6, 0, 0), 0, 0));

        contentPane.add(optionsPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(12, 6, 6, 6), 0, 0));
        contentPane.add(buttonsPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(6, 6, 6, 6), 0,
                0));

        getRootPane().setDefaultButton(cancelButton);
        
        nodesCombo.addActionListener(this);
        refreshWidgets();
    }

    //adaptation for OMS 3.2
    private List<LayoutPlugin> getEnabledPluginsInstancesOf(PluginsRegistry registry, Class<LayoutPlugin> filterClass) {
		
    	List<LayoutPlugin> instances = new ArrayList<LayoutPlugin>();
    	List<PluginDescriptor> descriptors = registry.getValidPlugins();
    	
    	for (PluginDescriptor descriptor : descriptors) {
    		if (descriptor instanceof XmlPluginDescriptor) {
    			XmlPluginDescriptor xmlDescriptor = (XmlPluginDescriptor)descriptor;
    			Class<?> pluginClass = xmlDescriptor.getPluginClass(); 
    			boolean enabled = xmlDescriptor.isEnabled();
    			
    			if (enabled && filterClass.isAssignableFrom(pluginClass)) {
    				XmlPlugin plugin = xmlDescriptor.getPluginInstance();
    				Plugin2 nested = plugin.getNestedPlugin();
    				if (nested instanceof LayoutPlugin) {
    					instances.add((LayoutPlugin)nested);
    				}
    			}
    		} //end if
    	} //end for
    	
		return instances;
	}

	/**
     * Perform layout.
     */
    
    private void performLayout() {
    	 NodesLayoutAlgorithm nodesAlgo = getNodesLayoutAlgorithm();
    	 
    	 if (nodesAlgo instanceof BuiltinLayoutAlgorithm) {
    		 starLayoutAction.performAction();
    	 } else {
    		 performPluginLayout();
    	 }
    } //end performLayout()
    
    private void performPluginLayout() {
        int expand = (Integer) ((DefaultComparableElement) expandCombo.getSelectedItem()).object;

        boolean growWidth = expand == 1 || expand == 3;
        boolean growHeight = expand == 2 || expand == 3;

        ClustersLayoutAlgorithm clustersAlgo = getClustersLayoutAlgorithm();

        NodesLayoutAlgorithm nodesAlgo = getNodesLayoutAlgorithm();

        EdgesLayoutAlgorithm edgesAlgo = getEdgesLayoutAlgorithm();

        boolean randomize = randomizeCheckBox.isSelected();

        boolean scopeSelection = ((DefaultComparableElement) scopeCombo.getSelectedItem()).object
                .equals(0);

        try {
            LayoutDialogDataOperator.layoutExecutor(growWidth, growHeight, randomize,
                    scopeSelection, clustersAlgo, nodesAlgo, edgesAlgo, diagram);
        } catch (Exception e) {
            ExceptionHandler.processUncatchedException(this, e);
        }

    }

    /**
     * Select default algorithms.
     */
    private void selectDefaultAlgorithms() {
        for (DefaultComparableElement element : defaultAlgorithms) {
            if (element.object instanceof LayoutAlgorithm) {
                LayoutAlgorithm algo = (LayoutAlgorithm) element.object;

                if (algo instanceof NodesLayoutAlgorithm) {
                    nodesCombo.setSelectedItem(element);
                } else if (algo instanceof EdgesLayoutAlgorithm) {
                    edgesCombo.setSelectedItem(element);
                } else if (algo instanceof ClustersLayoutAlgorithm) {
                    clustersCombo.setSelectedItem(element);
                }
            }
        }
    }

    /**
     * Gets the clusters layout algorithm.
     * 
     * @return the clusters layout algorithm
     */
    private ClustersLayoutAlgorithm getClustersLayoutAlgorithm() {
        ClustersLayoutAlgorithm clustersAlgo = null;
        if (clustersCombo.getSelectedItem() != null) {
            clustersAlgo = (ClustersLayoutAlgorithm) ((DefaultComparableElement) clustersCombo
                    .getSelectedItem()).object;
        }
        return clustersAlgo;
    }

    /**
     * Gets the nodes layout algorithm.
     * 
     * @return the nodes layout algorithm
     */
    private NodesLayoutAlgorithm getNodesLayoutAlgorithm() {
        NodesLayoutAlgorithm nodesAlgo = null;
        if (nodesCombo.getSelectedItem() != null) {
            nodesAlgo = (NodesLayoutAlgorithm) ((DefaultComparableElement) nodesCombo
                    .getSelectedItem()).object;
        }
        return nodesAlgo;
    }

    /**
     * Gets the edges layout algorithm.
     * 
     * @return the edges layout algorithm
     */
    private EdgesLayoutAlgorithm getEdgesLayoutAlgorithm() {
        EdgesLayoutAlgorithm edgesAlgo = null;
        if (edgesCombo.getSelectedItem() != null) {
            edgesAlgo = (EdgesLayoutAlgorithm) ((DefaultComparableElement) edgesCombo
                    .getSelectedItem()).object;
        }
        return edgesAlgo;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource(); 
		
		if (src.equals(nodesCombo)) {
			refreshWidgets();
		}
	}

	private void refreshWidgets() {
		DefaultComparableElement elem = (DefaultComparableElement)nodesCombo.getSelectedItem();
		NodesLayoutAlgorithm nodesAlgo = (NodesLayoutAlgorithm)elem.object; 
		boolean builtin = (nodesAlgo instanceof BuiltinLayoutAlgorithm); 
		
		edgesCombo.setEnabled(! builtin);
		clustersCombo.setEnabled(! builtin);
		scopeCombo.setEnabled(! builtin);
		expandCombo.setEnabled(! builtin);
		randomizeCheckBox.setEnabled(! builtin);
	}

}
