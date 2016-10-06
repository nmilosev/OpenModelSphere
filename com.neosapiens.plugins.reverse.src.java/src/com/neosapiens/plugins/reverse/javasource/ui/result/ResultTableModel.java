package com.neosapiens.plugins.reverse.javasource.ui.result;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

//import org.modelsphere.jack.baseDb.db.DbObject;

public class ResultTableModel implements TableModel {
    private List<ResultObject> _matchingObjects; 
    private String[] _colummnNames = new String[] {"Object Type", "Name", "Alias", "Description"};
    
    public ResultTableModel(List<ResultObject> matchingObjects) {
        _matchingObjects = matchingObjects;
    }

    @Override
    public int getColumnCount() {
        return _colummnNames.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return _colummnNames[column];
    }
    
    @Override
    public int getRowCount() {
        int nbObjects = _matchingObjects.size();
        return nbObjects;
    }

    @Override
    public Class<?> getColumnClass(int col) {
        Object o = getValueAt(0, col);
        Class<?> claz = o.getClass();
        return claz;
    }

    @Override
    public Object getValueAt(int row, int col) {
        ResultObject dbo = _matchingObjects.get(row);
        String value = null; 
        
        switch (col) {
        case 0:
            value = dbo.getType(); 
            break;
        case 1:
            value = dbo.getName(); 
            break;
        case 2:
            value = dbo.getAlias(); 
            break;
        case 3:
        default:
            value = dbo.getDescription(); 
            break;
        }
        
        if (value == null) {
            value = "";
        }

        return value;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    @Override
    public void addTableModelListener(TableModelListener listener) {
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
    }
}
