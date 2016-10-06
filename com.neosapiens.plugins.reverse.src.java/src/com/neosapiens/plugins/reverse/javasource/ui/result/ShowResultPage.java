package com.neosapiens.plugins.reverse.javasource.ui.result;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

//import org.modelsphere.jack.baseDb.db.DbObject;

@SuppressWarnings("serial")
public class ShowResultPage extends JPanel {
    private JButton _showButton; 

    public ShowResultPage(String matchingText, List<ResultObject> matchingObjects) {
        super(new GridBagLayout());
        int y=0;
        int w = 1, h = 1;
        double wx = 0.0, wy = 0.0;
        int nofill = GridBagConstraints.NONE;
       
        String pattern = "{0} results matching ''{1}''"; 
        int nbMatches = matchingObjects.size();
        String msg = MessageFormat.format(pattern, nbMatches, matchingText); 
        JLabel itemsFound = new JLabel(msg); 
        add(itemsFound, new GridBagConstraints(1, y, 1, h, wx, wy,
                GridBagConstraints.WEST, nofill, new Insets(6, 6, 0, 3), 0, 0));
        y++;
        
        JTable table = createTable(matchingObjects);
        JScrollPane scrolledTable = new JScrollPane(table);  
        table.setFillsViewportHeight(true);
        add(scrolledTable, new GridBagConstraints(0, y, 5, h, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(6, 6, 0, 3), 0, 0));
        y++;
        
        _showButton = new JButton("Show in Explorer");
        _showButton.setEnabled(false);
        add(_showButton, new GridBagConstraints(2, y, 2, h, 1.0, wy,
                GridBagConstraints.EAST, nofill, new Insets(6, 6, 0, 3), 0, 0));
    } //end ShowResultPage
    
    private JTable createTable(List<ResultObject> matchingObjects) {

        TableModel model = new ResultTableModel(matchingObjects);
        JTable table = new JTable(model);
        
        JTableHeader header = table.getTableHeader(); 
        header.setVisible(true);
        header.setPreferredSize(null); 
        
        //add selection listener
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ListSelectionListener listener = new TableSelectionListener(table);
        table.getSelectionModel().addListSelectionListener(listener); 
                     
        return table;   
    }
    
    //
    // inner classes
    //
    private class TableSelectionListener implements ListSelectionListener {
        JTable _table; 
        
        TableSelectionListener(JTable table) {
            _table = table;
        }

        @Override
        public void valueChanged(ListSelectionEvent arg0) {
            //row == -1 if no row is selected
            int row = _table.getSelectedRow();
            _showButton.setEnabled(row != -1); 
        } //end valueChanged()
    }
      
    //
    // dummy DbObject 
    //
    /*
    static class ResultDbObject {
        String _objectType; 
        String _name;
        String _alias; 
        String _description; 
        
        ResultDbObject(String objectType, String name, String alias, String description) {
            _objectType = objectType;
            _name = name;
            _alias = alias;
            _description = description;
        }
    
    }
    */
}
