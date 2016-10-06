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

import java.util.List;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbUDF;
import org.modelsphere.plugins.export.links.wrappers.Matrix.Cell;
import org.modelsphere.plugins.export.links.wrappers.Matrix.Column;

public class DbUdfWrapper {
    private DbUDF m_dbUdf;

    public DbUdfWrapper(DbUDF dbUdf) {
        m_dbUdf = dbUdf;
    }

    public String getName() {
        String name;

        try {
            name = m_dbUdf.getName();
        } catch (DbException ex) {
            name = null;
        } //end try 

        return name;
    }

    public String getNamesAndValues(Matrix.Row row) {
        String namesAndValues = "";

        for (Column column : row.getColumns()) {
            DbDataModelWrapper dataModel = column.getDataModel();
            Cell cell = column.isSummation() ? null : row.m_cells.get(dataModel);

            if (column.isSummation()) {
                List<Cell> summatedCells = Cell.summateCells(row.m_cells, dataModel);
                namesAndValues += getSummatedNamesAndValues(summatedCells);
            } else if (cell == null) {
                namesAndValues += ";;;" + Matrix.V_SEPARATOR + ";";
            } else {
                String nameAndValue = cell.getNameAndValue(this);
                namesAndValues += ";" + nameAndValue + ";" + Matrix.V_SEPARATOR + ";";
            } //end if
        } //end for

        return namesAndValues;
    } //get getNamesAndValues()

    private String getSummatedNamesAndValues(List<Cell> summatedCells) {
        String summatedNamesAndValues;

        int nb = summatedCells.size();
        if (nb == 0) {
            summatedNamesAndValues = ";;;" + Matrix.V_SEPARATOR + ";";
        } else if (nb == 1) {
            String nameAndValue = summatedCells.get(0).getNameAndValue(this);
            summatedNamesAndValues = ";" + nameAndValue + ";" + Matrix.V_SEPARATOR + ";";
        } else {
            summatedNamesAndValues = ";" + summateNamesAndValues(summatedCells) + ";"
                    + Matrix.V_SEPARATOR + ";";
        } //end if

        return summatedNamesAndValues;
    }

    private String summateNamesAndValues(List<Cell> summatedCells) {
        String guiName = getName();
        String guiValue = "";
        String nameAndValue = guiName + ";" + guiValue;;
        return nameAndValue;
    }
} //end DbUdfWrapper
