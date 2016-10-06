package com.neosapiens.plugins.reverse.javasource;

import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.modelsphere.jack.awt.JackMenu;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.Worker;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSelectionListener;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;
import com.neosapiens.plugins.reverse.javasource.ui.ReverseJavaSourcecodeWizard;
import com.neosapiens.plugins.reverse.javasource.ui.WizardParameters;

@SuppressWarnings("deprecation")
public class ReverseJavaSourcecodePlugin2 implements Plugin2 {

    @Override
    public PluginSignature getSignature() {
        return null;
    }

    @Override
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        JMenu importFromMenu = getJavaToolMenu(menuManager);
        PluginAction action = getPluginAction();
        importFromMenu.add(action);
        return null;
    }

    @Override
    public Class<? extends Object>[] getSupportedClasses() {
        return null;
    }
    

    @Override
    public void execute(ActionEvent ev) throws Exception {
        ReverseJavaSourcecodeParameters params = new ReverseJavaSourcecodeParameters();
        JFrame frame = ApplicationContext.getDefaultMainFrame();
        ReverseJavaSourcecodeWizard wizard = new ReverseJavaSourcecodeWizard(params);
        String title = JavaSourceReverseLocaleMgr.misc.getString("ImportJavaSourcecode");
        wizard.showOpenDialog(frame, title);
        
        params = (ReverseJavaSourcecodeParameters) wizard.getParameters();
        WizardParameters.Status status = params.getStatus();
        if (status == WizardParameters.Status.FINISHED) {
            params.saveProperties();
            title = JavaSourceReverseLocaleMgr.misc.getString("ImportingJavaSourceFiles");
            boolean useProgressBar = true;
            Controller controller = new DefaultController1(title, useProgressBar,
                    "sourcecodeImportLogFile.txt");
            Worker worker = new ReverseJavaSourcecodeWorker(params);
            controller.start(worker);
        }
    }
    
    
    private JackMenu getJavaToolMenu(MainFrameMenu menuManager) {
        JMenu toolMenu = menuManager.getMenuForKey(MainFrameMenu.MENU_TOOLS);
        JackMenu javaToolMenu = null;

        int nbItems = toolMenu.getItemCount();
        for (int i = 0; i < nbItems; i++) {
            JMenuItem item = toolMenu.getItem(i);
            if (item instanceof JackMenu) {
                JackMenu menu = (JackMenu) item;
                String text = menu.getText();
                if ("Java".equals(text)) {
                    javaToolMenu = menu;
                    break;
                }
            }
        }

        return javaToolMenu;
    }

    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }
    

    @Override
    public PluginAction getPluginAction() {
        String name = JavaSourceReverseLocaleMgr.misc.getString("ReverseEngineerJavaSourcecodeAction");
        ImageIcon icon = JavaSourceReverseLocaleMgr.misc.getImageIcon("ReverseEngineerJavaSourcecodeAction");
        PluginAction action = new PluginAction(this, name);
        action.setIcon(icon);
        return action;
    }

    public boolean doListenSelection() {
        return (this instanceof PluginSelectionListener);
    }
}
