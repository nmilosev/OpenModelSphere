package com.neosapiens.plugins.helloworld;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

public class HelloWorldPlugin implements Plugin2 {

	@Override
	public OptionGroup getOptionGroup() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PluginAction getPluginAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute(ActionEvent ev) throws Exception {
		JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(),
		"Hello World!");
	}

	@Override
	public PluginSignature getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends Object>[] getSupportedClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String installAction(DefaultMainFrame frame,
			MainFrameMenu menuManager) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean doListenSelection() { return (this instanceof PluginSelectionListener); }

}
