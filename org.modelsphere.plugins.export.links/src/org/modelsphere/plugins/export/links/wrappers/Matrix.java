/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links.wrappers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORColumn;

public class Matrix {
    private static final String EMPTY = LocaleMgr.misc.getString("Empty");
    private static final String NA = LocaleMgr.misc.getString("NA");
    private static final String H_SEPARATOR = "\"- - - - - -\"";
    private static final String CROSSING = "\"| | - - - -\"";
    static final String V_SEPARATOR = "| |";

    private List<Row> m_rows = new ArrayList<Row>();
    private List<Column> m_columns = new ArrayList<Column>();
    private List<DbDataModelWrapper> m_dockedModels = new ArrayList<DbDataModelWrapper>();

    private Map<DbDataModelWrapper, Column> m_regularColumns = new HashMap<DbDataModelWrapper, Column>();

    public Matrix(DbProjectWrapper project) {
    }

    public Column findColumn(DbDataModelWrapper dataModel) {

        Column column = m_regularColumns.get(dataModel);
        if (column == null) {
            column = new Column(dataModel);
            m_columns.add(column);
            m_dockedModels.add(dataModel);
            m_regularColumns.put(dataModel, column);
        }

        return column;
    }

    public String getHeader() {
        String header = "";

        for (Column column : m_columns) {
            String name = column.toString();
            header += name + ";;;" + V_SEPARATOR + ";";
        }

        return header;
    }

    public String getDockingUnderline() {
        String underline = "";

        int nb = m_columns.size();
        for (int i = 0; i < nb; i++) {
            underline += H_SEPARATOR + ";" + H_SEPARATOR + ";" + H_SEPARATOR + ";" + CROSSING + ";";
        }

        return underline;
    }

    public void arrangeMatrix() {

        //add summation columns
        addSummationColumns();

        //sort columns and rows
        Column longerColumn = findLongerColumn();

        DbDataModelWrapper largestModel = findLargestModel();
        Comparator<Row> comparator = new RowComparator(largestModel);
        Collections.sort(m_columns);
        Collections.sort(m_rows, comparator);

        //merge rows
        //mergeRows(largestModel, comparator);
    }

    private Column findLongerColumn() {
        Column longerColumn = null;
        int longerSize = -1;

        for (Column c : m_columns) {
            int size = c.getSize();
            if (size > longerSize) {
                longerSize = size;
                longerColumn = c;
            }
        }

        return longerColumn;
    }

    private void addSummationColumns() {

        //find summable data models
        List<DbDataModelWrapper> summableDataModels = new ArrayList<DbDataModelWrapper>();

        //for each column
        for (Column c : m_columns) {
            DbDataModelWrapper dm = c.getDataModel();
            boolean projectChild = dm.isProjectChild();
            if (projectChild) {
                List<DbDataModelWrapper> subModels = dm.getSubModels();
                if (!subModels.isEmpty()) {
                    summableDataModels.add(dm);
                }
            }
        } //end for

        for (DbDataModelWrapper summableDataModel : summableDataModels) {
            Column column = createSummationColumn(summableDataModel);
            m_columns.add(column);
        } //end for
    } //end addSummationColumns()

    private DbDataModelWrapper m_largestModel = null;

    private DbDataModelWrapper findLargestModel() {
        if (m_largestModel == null) {
            //find the main column
            int longerSize = 0;
            for (DbDataModelWrapper dataModel : m_dockedModels) {
                Column col = m_regularColumns.get(dataModel);
                int size = col.getSize();
                if (size > longerSize) {
                    longerSize = size;
                    m_largestModel = dataModel;
                }
            } //end for
        } //end if 

        return m_largestModel;
    }

    //merge rows based on the same DB column
    private void mergeRows(DbDataModelWrapper largestModel, Comparator<Row> comparator) {
        Map<DbORColumn, List<Row>> map = new HashMap<DbORColumn, List<Row>>();

        //find mergeable rows
        for (Row row : m_rows) {
            Cell cell = row.m_cells.get(largestModel);
            DbColumnWrapper column = (cell == null) ? null : cell.getColumn();
            if (column != null) {
                if (!column.isRubrique()) {
                    DbORColumn c = column.getDbColumn();
                    List<Row> mergeableRows = map.get(c);
                    if (mergeableRows == null) {
                        mergeableRows = new ArrayList<Row>();
                        map.put(c, mergeableRows);
                    }

                    mergeableRows.add(row);
                }
            } //end if
        } //end for 

        //merge rows (and keep traces of rows to delete)
        List<Row> rowsToDelete = new ArrayList<Row>();
        for (DbORColumn col : map.keySet()) {
            List<Row> mergeableRows = map.get(col);
            Row firstRow = mergeableRows.get(0);
            int nb = mergeableRows.size();
            for (int i = 1; i < nb; i++) {
                Row row = mergeableRows.get(i);
                firstRow.merge(row);
                rowsToDelete.add(row);
            }
        } //end for

        //delete duplicated rows
        m_rows.removeAll(rowsToDelete);

    } //end mergeRows()

    public List<Row> getRows() {
        return m_rows;
    }

    //
    // inner classes
    //
    class Column implements Comparable<Column> {
        private boolean m_isSummation = false;

        public boolean isSummation() {
            return m_isSummation;
        }

        private DbDataModelWrapper m_dataModel;

        public DbDataModelWrapper getDataModel() {
            return m_dataModel;
        }

        Column(DbDataModelWrapper dataModel) {
            m_dataModel = dataModel;
        }

        List<Cell> cells = new ArrayList<Cell>();

        public void addCell(DbColumnWrapper col, int rowIdx) {
            Cell cell = new Cell(col);
            cells.add(cell);

            while (rowIdx >= m_rows.size()) {
                Row row = new Row();
                m_rows.add(row);
            }

            Row row = m_rows.get(rowIdx);
            row.addCell(cell);
        }

        @Override
        public String toString() {
            String text;

            if (m_isSummation) {
                List<DbDataModelWrapper> subModels = m_dataModel.getSubModels();
                int nb = subModels.size();
                String first = subModels.get(0).getName();
                String last = subModels.get(nb - 1).getName();
                String pattern = LocaleMgr.misc.getString("SumOfDataModels01");
                text = MessageFormat.format(pattern, first, last);
            } else {
                text = m_dataModel.toString();
            }

            return text;
        }

        @Override
        public int compareTo(Column that) {
            String thisName = this.m_dataModel.toString();
            String thatName = that.m_dataModel.toString();
            int comparison = thisName.compareTo(thatName);

            return comparison;
        }

        public int getSize() {
            return cells.size();
        }
    } //end Column

    public Column createSummationColumn(DbDataModelWrapper dataModel) {
        Column c = new Column(dataModel);
        c.m_isSummation = true;
        return c;
    }

    public static class Cell {
        private DbColumnWrapper m_col;

        public DbColumnWrapper getColumn() {
            return m_col;
        }

        public Cell(DbColumnWrapper col) {
            m_col = col;
        }

        public DbDataModelWrapper getDataModel() {
            DbDataModelWrapper dataModel = m_col.getParent().getParent();
            return dataModel;
        }

        @Override
        public String toString() {
            return m_col.toString();
        }

        public String getName() {
            String name = m_col.getName();
            return name;
        }

        public String getDescription() {
            String guiName = DbSemanticalObject.fDescription.getGUIName();
            String s = m_col.getDescription();
            String guiValue = (s == null) ? EMPTY : s;
            String desc = guiName + ";\"" + guiValue + "\"";

            return desc;
        }

        public String getFormat() {
            String guiName = "Format";
            String guiValue = m_col.getFormat();
            String format = guiName + ";" + guiValue;
            return format;
        }

        public String getNullable() {
            String guiName = DbORColumn.fNull.getGUIName();
            String guiValue = m_col.getNullable();
            String nullable = guiName + ";" + guiValue;
            return nullable;
        }

        public String getNameAndValue(DbUdfWrapper udf) {
            String guiName = udf.getName();
            String guiValue = m_col.getUdfValue(guiName);
            String nameAndValue = guiName + ";" + guiValue;
            return nameAndValue;
        }

        public static List<Cell> summateCells(Map<DbDataModelWrapper, Cell> cells,
                DbDataModelWrapper dataModel) {
            List<DbDataModelWrapper> subModels = dataModel.getSubModels();
            List<Cell> summatedCells = new ArrayList<Cell>();
            for (DbDataModelWrapper subModel : subModels) {
                Cell cell = cells.get(subModel);
                if (cell != null) {
                    summatedCells.add(cell);
                }
            } //end for  

            return summatedCells;
        }
    } //end Cell

    public class Row implements Comparable<Row> {
        Map<DbDataModelWrapper, Cell> m_cells = new HashMap<DbDataModelWrapper, Cell>();

        public void addCell(Cell cell) {
            DbDataModelWrapper dataModel = cell.getDataModel();
            m_cells.put(dataModel, cell);
        }

        public void merge(Row that) {
            for (DbDataModelWrapper model : m_dockedModels) {
                Cell cell1 = this.m_cells.get(model);
                Cell cell2 = that.m_cells.get(model);
                if (cell1 == null) {
                    this.m_cells.put(model, cell2);
                }
            } //end for
        } //end merge()

        public String getNamesAndDescriptions() {
            String namesAndDescriptions = "";

            for (Column column : m_columns) {
                DbDataModelWrapper dataModel = column.m_dataModel;
                Cell cell = column.m_isSummation ? null : m_cells.get(dataModel);

                if (column.m_isSummation) {
                    List<Cell> summatedCells = Cell.summateCells(this.m_cells, dataModel);
                    namesAndDescriptions += getSummatedNameAndDescriptions(summatedCells);
                } else if (cell == null) {
                    namesAndDescriptions += NA + ";;;" + V_SEPARATOR + ";";
                } else {
                    String name = cell.getName();
                    String desc = cell.getDescription();
                    namesAndDescriptions += name + ";" + desc + ";" + V_SEPARATOR + ";";
                } //end if
            } //end for

            return namesAndDescriptions;
        } //end getNamesAndDescriptions()

        public String getFormats() {
            String formats = "";

            for (Column column : m_columns) {
                DbDataModelWrapper dataModel = column.m_dataModel;
                Cell cell = column.m_isSummation ? null : m_cells.get(dataModel);

                if (column.m_isSummation) {
                    List<Cell> summatedCells = Cell.summateCells(this.m_cells, dataModel);
                    formats += getSummatedFormats(summatedCells);
                } else if (cell == null) {
                    formats += ";;;" + V_SEPARATOR + ";";
                } else {
                    String format = cell.getFormat();
                    formats += ";" + format + ";" + V_SEPARATOR + ";";
                } //end if
            } //end for

            return formats;
        }

        public String getNullables() {
            String nullables = "";

            for (Column column : m_columns) {
                DbDataModelWrapper dataModel = column.m_dataModel;
                Cell cell = column.m_isSummation ? null : m_cells.get(dataModel);

                if (column.m_isSummation) {
                    List<Cell> summatedCells = Cell.summateCells(this.m_cells, dataModel);
                    nullables += getSummatedNullables(summatedCells);
                } else if (cell == null) {
                    nullables += ";;;" + V_SEPARATOR + ";";
                } else {
                    String nullable = cell.getNullable();
                    nullables += ";" + nullable + ";" + V_SEPARATOR + ";";
                } //end if
            } //end for

            return nullables;
        } //end getNullables()

        private String getSummatedNameAndDescriptions(List<Cell> summatedCells) {
            String nameAndDescription;

            int nb = summatedCells.size();
            if (nb == 0) {
                nameAndDescription = NA + ";;;" + V_SEPARATOR + ";";
            } else if (nb == 1) {
                String name = summatedCells.get(0).getName();
                String desc = summatedCells.get(0).getDescription();
                nameAndDescription = name + ";" + desc + ";" + V_SEPARATOR + ";";
            } else {
                String name = summateNames(summatedCells);
                String desc = summateDescriptions(summatedCells);
                nameAndDescription = name + ";" + desc + ";" + V_SEPARATOR + ";";
            } //end if

            return nameAndDescription;
        } //end getSummatedNameAndDescriptions()

        private String getSummatedFormats(List<Cell> summatedCells) {
            String formats;

            int nb = summatedCells.size();
            if (nb == 0) {
                formats = NA + ";;;" + V_SEPARATOR + ";";
            } else if (nb == 1) {
                formats = ";" + summatedCells.get(0).getFormat() + ";" + V_SEPARATOR + ";";
            } else {
                formats = ";" + summateFormats(summatedCells) + ";" + V_SEPARATOR + ";";
            } //end if

            return formats;
        }

        private String getSummatedNullables(List<Cell> summatedCells) {
            String nullables;

            int nb = summatedCells.size();
            if (nb == 0) {
                nullables = NA + ";;;" + V_SEPARATOR + ";";
            } else if (nb == 1) {
                nullables = ";" + summatedCells.get(0).getNullable() + ";" + V_SEPARATOR + ";";
            } else {
                nullables = ";" + summateNullables(summatedCells) + ";" + V_SEPARATOR + ";";
            } //end if

            return nullables;
        }

        private String summateNames(List<Cell> summatedCells) {
            String names = "(" + summatedCells.size() + " items)";
            return names;
        }

        private String summateDescriptions(List<Cell> summatedCells) {
            String guiName = DbSemanticalObject.fDescription.getGUIName();
            String descriptions = "(" + summatedCells.size() + " items)";
            return guiName + ";" + descriptions;
        }

        private String summateFormats(List<Cell> summatedCells) {
            String guiName = "Format";
            String formats = guiName + ";";
            return formats;
        }

        private String summateNullables(List<Cell> summatedCells) {
            String guiName = DbORColumn.fNull.getGUIName();
            String nullable = guiName + ";";
            return nullable;
        }

        @Override
        public String toString() {
            return getNamesAndDescriptions();
        }

        @Override
        public int compareTo(Row that) {
            DbDataModelWrapper dataModel = findLargestModel();
            Cell cell1 = this.m_cells.get(dataModel);
            Cell cell2 = that.m_cells.get(dataModel);

            String s1 = cell1.toString();
            String s2 = cell2.toString();
            int comparaison = s1.compareTo(s2);
            return comparaison;
        }

        public List<DbDataModelWrapper> getDockedModels() {
            return m_dockedModels;
        }

        public List<Column> getColumns() {
            return m_columns;
        }
    }

    public static class RowComparator implements Comparator<Row> {
        private DbDataModelWrapper m_largestModel;

        public RowComparator(DbDataModelWrapper largestModel) {
            m_largestModel = largestModel;
        }

        @Override
        public int compare(Row r1, Row r2) {
            Cell cell1 = r1.m_cells.get(m_largestModel);
            Cell cell2 = r2.m_cells.get(m_largestModel);
            String s1 = (cell1 == null) ? "" : cell1.toString();
            String s2 = (cell2 == null) ? "" : cell2.toString();

            int comparison = (s1 == null) ? -1 : s1.compareTo(s2);
            return comparison;
        }

    }

}
