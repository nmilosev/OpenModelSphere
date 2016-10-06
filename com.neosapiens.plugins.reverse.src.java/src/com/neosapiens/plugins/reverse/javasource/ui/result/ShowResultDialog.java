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
package com.neosapiens.plugins.reverse.javasource.ui.result;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.sms.db.DbSMSProject;

import com.neosapiens.plugins.reverse.javasource.international.JavaSourceReverseLocaleMgr;
import com.neosapiens.plugins.reverse.javasource.utils.SearchFiltering;

@SuppressWarnings("serial")
public class ShowResultDialog extends JPanel implements ActionListener {
    private JTabbedPane _tabbedPane;
    private JTextField _textField;
    private JButton _showResultsBtn = new JButton(JavaSourceReverseLocaleMgr.misc.getString("ShowResults")); 
    private JButton _closeBtn = new JButton(JavaSourceReverseLocaleMgr.misc.getString("Close"));    
    private JCheckBox name;
    private JCheckBox alias;
    private JCheckBox desc;
    private JCheckBox text;
    private JCheckBox option2;
    private JComboBox objList;
    
    private Window parent;
    private String modelName;
    private List<DbObject> objects;
    private DbSMSProject project;
    
    //private List<DbJVPackage> _packages;
    //private DbJVClassModel _model;
    
    /** Creates the GUI shown inside the frame's content pane. */
    public ShowResultDialog(JDialog parent, String modelName, List<DbObject> objects,
            DbSMSProject project) {
        super(new GridLayout(1, 1));
        this.modelName = modelName;
        setOpaque(true);
        parent.setContentPane(this);
        this.parent = parent;
        this.objects = objects;
        this.project = project;
        //_model = model;
        //_packages = packages;
        
        _tabbedPane = new JTabbedPane();
        JPanel panel = createOptionPage();
        _tabbedPane.addTab(JavaSourceReverseLocaleMgr.misc.getString("SearchOptions"), panel);
        add(_tabbedPane);
        addListeners();
        
    }

    private JPanel createOptionPage() {
        JPanel page = new JPanel(new GridBagLayout());
        
        int y=0;
        int w = 1, h = 1;
        double wx = 0.0, wy = 0.0;
        int nofill = GridBagConstraints.NONE;
        
        JLabel label2 = new JLabel(JavaSourceReverseLocaleMgr.misc.getString("SearchUnder")); 
        page.add(label2, new GridBagConstraints(0, y, 1, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 6, 6, 3), 0, 0));
        
        JTextField textField1 = new JTextField();
        textField1.setColumns(10);
        textField1.setText(modelName);
        textField1.setEditable(false);
        textField1.setEnabled(false);
        page.add(textField1, new GridBagConstraints(1, y, 1, h, 0.5, wy,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(12, 6, 6, 3), 0, 0));
        
        JLabel label1 = new JLabel("Recursively");
        page.add(label1, new GridBagConstraints(2, y, 2, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 6, 6, 3), 0, 0));
        y++;
        
        JLabel label3 = new JLabel(JavaSourceReverseLocaleMgr.misc.getString("ForInstancesOf")); 
        page.add(label3, new GridBagConstraints(0, y, 2, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 6, 6, 3), 0, 0));
        
        String[] objects = {
                JavaSourceReverseLocaleMgr.misc.getString("AnyObject"),
                JavaSourceReverseLocaleMgr.misc.getString("Class"), 
                JavaSourceReverseLocaleMgr.misc.getString("Method"),
                JavaSourceReverseLocaleMgr.misc.getString("Field"),
                JavaSourceReverseLocaleMgr.misc.getString("Parameter")
        };
        
        objList = new JComboBox(objects);
        page.add(objList, new GridBagConstraints(1, y, 2, h, 0.5, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 6, 6, 3), 30, 0));
        y++;
         
        JLabel field = new JLabel(JavaSourceReverseLocaleMgr.misc.getString("WhoseFields")); 
        page.add(field, new GridBagConstraints(0, y, 1, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 6, 6, 3), 0, 0));
        
        JPanel optionPanel = createFieldPanel();
        page.add(optionPanel, new GridBagConstraints(1, y, 3, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(12, 0, 6, 3), 0, 0));
        y++;
        
        JLabel text = new JLabel(JavaSourceReverseLocaleMgr.misc.getString("ContainingText")); 
        page.add(text, new GridBagConstraints(0, y, 1, 3, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(12, 6, 36, 3), 0, 0));
        
        _textField = new JTextField(20);
        page.add(_textField, new GridBagConstraints(1, y, 2, 3, 1.0, wy,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(9, 6, 0, 3), 0, 0));
        
        option2 = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("CaseSensitive"));
        page.add(option2, new GridBagConstraints(3, y, 1, h, wx, wy,
                GridBagConstraints.NORTHWEST, nofill, new Insets(3, 6, 0, 3), 0, 0));
        y++;
        
        JPanel buttonPanel = createButtonPanel(); 
        page.add(buttonPanel, new GridBagConstraints(3, y, 1, h, 0.0, 1.0,
                GridBagConstraints.SOUTHEAST, nofill, new Insets(0, 0, 0, 3), 0, 0));
      

        return page;
    }
    
    private void addListeners() {
        _showResultsBtn.addActionListener(this);
        _closeBtn.addActionListener(this);
    }
    
    private JPanel createFieldPanel() {
        JPanel optionPanel = new JPanel(new FlowLayout());
        int nofill = GridBagConstraints.NONE;
        double wx = 0.0, wy = 0.0;
        
        name = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("Name"));
        alias = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("Alias"));
        desc = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("Description"));
        text = new JCheckBox(JavaSourceReverseLocaleMgr.misc.getString("OtherTextFields"));
        
        optionPanel.add(name, new GridBagConstraints(0, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 0, 0, 3), 0, 0));
        optionPanel.add(alias, new GridBagConstraints(1, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
        optionPanel.add(desc, new GridBagConstraints(2, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
        optionPanel.add(text, new GridBagConstraints(3, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
        
        return optionPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel optionPanel = new JPanel();
        int nofill = GridBagConstraints.NONE;
        double wx = 0.0, wy = 0.0;
        
        _showResultsBtn = new JButton(JavaSourceReverseLocaleMgr.misc.getString("ShowResults")); 
        _closeBtn = new JButton(JavaSourceReverseLocaleMgr.misc.getString("Close")); 
        
        optionPanel.add(_showResultsBtn, new GridBagConstraints(0, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
        optionPanel.add(_closeBtn, new GridBagConstraints(1, 0, 1, 1, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
       
        return optionPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Object src = ev.getSource();
        
        if (_showResultsBtn.equals(src)) {
            addResultTab();
        } else if (_closeBtn.equals(src)) {            
            parent.setVisible(false);
        }
    }
    

    private void addResultTab() {
        String matchingText = _textField.getText();
        boolean caseSensitive = option2.isSelected();
        boolean searchInName = name.isSelected();
        boolean searchInDescription = desc.isSelected();
        boolean searchInBody = text.isSelected();        
        List<DbObject> result;
        int index = objList.getSelectedIndex();
        
        boolean showAll = false;
        // if no option is selected, we show all results
        if(matchingText.equals("") && !caseSensitive && !searchInName && 
                !searchInDescription && !searchInBody)
            showAll = true;
        
        List<ResultObject> matchingObjects = new ArrayList<ResultObject>();
        Db db = project.getDb();
        try {
            db.beginReadTrans();
            SearchFiltering filter = new SearchFiltering(objects);
            if(showAll) {
                result = filter.search(matchingText, false, true, true, true);
            }
            else result = filter.search(matchingText, caseSensitive, searchInName,
                    searchInDescription, searchInBody);
        
            for(DbObject obj : result) {
                String type = obj.getMetaClass().getGUIName();
                String name = obj.getName();
                String alias = obj.getName();
                String description = null;
                
                // Class
                if(index == 1) {
                    if(type.equals("Class"))
                        matchingObjects.add(new ResultObject(type, name, alias, description));
                }
                
                // Method
                else if(index == 2) {
                    if(type.equals("Method") || type.equals("Constructor"))
                        matchingObjects.add(new ResultObject(type, name, alias, description));
                }
                
                // Field
                else if(index == 3) {
                    if(type.equals("Field"))
                        matchingObjects.add(new ResultObject(type, name, alias, description));
                }
                
                // Parameter
                else if(index == 4) {
                    if(type.equals("Parameter"))
                        matchingObjects.add(new ResultObject(type, name, alias, description));
                }
                
                else matchingObjects.add(new ResultObject(type, name, alias, description));
            }
            
            db.commitTrans();
        } catch (DbException e) {
            db.abortTrans();
        } 

        JPanel resultPanel = new ShowResultPage(matchingText, matchingObjects); 
        _tabbedPane.addTab(JavaSourceReverseLocaleMgr.misc.getString("Results"), resultPanel);
        int idx = _tabbedPane.getTabCount() - 1;
        _tabbedPane.setTabComponentAt(idx, new ButtonTabComponent(_tabbedPane)); 
        _tabbedPane.setSelectedComponent(resultPanel);
    }
}
