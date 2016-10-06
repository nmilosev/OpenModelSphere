/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.or.screen.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import javax.swing.table.AbstractTableModel;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.sms.db.util.AnySemObject;
import org.modelsphere.sms.or.db.util.AnyORObject;
import org.modelsphere.sms.or.features.ORPNGCase;
import org.modelsphere.sms.or.features.ORPNGParameters;
import org.modelsphere.sms.or.features.ORPNGStatus;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.oracle.db.DbORADataFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogFile;
import org.modelsphere.sms.or.oracle.db.DbORARedoLogGroup;

public class GeneratePhysicalNameModel extends AbstractTableModel {
    private static final String CONCEPTS_PARAMETERS = "CONCEPTS_PARAMETERS"; // NOT LOCALIZABLE
    private static final String CONCEPT_PARAM_SEPARATOR = "////"; // NOT LOCALIZABLE
    public static final ORPNGStatus DEFAULT_STATUS = ORPNGStatus.getInstance(ORPNGStatus.PARTIAL);
    public static final boolean DEFAULT_UNIQUE = false;
    public static final int DEFAULT_LENGTH = 18;
    public static final int DEFAULT_NCBW = 6; //Nb Char per Word
    public static final String DEFAULT_REPLACEMENT_STRING = "_"; //NOT LOCALIZABLE
    public static final ORPNGCase DEFAULT_CASE = ORPNGCase.getInstance(ORPNGCase.UPPER);

    Object[] listConcepts = null;
    ORPNGParameters[] parameters = null;

    final String[] columnNames = { LocaleMgr.screen.getString("Concept"),
            LocaleMgr.screen.getString("Status"), LocaleMgr.screen.getString("ReplacementString"),
            LocaleMgr.screen.getString("Length"), LocaleMgr.screen.getString("UniquePNG"),
            LocaleMgr.screen.getString("NbrCharbyWord"), LocaleMgr.screen.getString("Case") };

    public GeneratePhysicalNameModel(PropertiesSet physicalNameGenerationPropertiesSet) {
        listConcepts = getMetaClassesWithPhysicalName();
        parameters = new ORPNGParameters[listConcepts.length];
        populateParametersFromString(physicalNameGenerationPropertiesSet.getPropertyString(
                GeneratePhysicalNameModel.class, CONCEPTS_PARAMETERS,
                getDefaultParametersToString()));
    }

    private final Object[] getMetaClassesWithPhysicalName() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < AnyORObject.ORMetaClasses.length; i++) {
            list.add(AnyORObject.ORMetaClasses[i]);
        }

        Enumeration enumMetaClasses = DbObject.metaClass.enumMetaClassHierarchy(true);
        while (enumMetaClasses.hasMoreElements()) {
            MetaClass metaClass = (MetaClass) enumMetaClasses.nextElement();
            if (AnyORObject.getMetaClassIndex(metaClass) == -1
                    && AnySemObject.supportsPhysicalName(metaClass)
                    && metaClass != DbORARedoLogGroup.metaClass
                    && metaClass != DbORADataFile.metaClass
                    && metaClass != DbORARedoLogFile.metaClass)
                list.add(metaClass);
        }
        Collections.sort(list, new CollationComparator());
        return list.toArray();

    }

    private final void populateParameters() {
        for (int i = 0; i < listConcepts.length; i++) {
            parameters[i] = new ORPNGParameters((MetaClass) listConcepts[i], DEFAULT_STATUS,
                    DEFAULT_REPLACEMENT_STRING, new Integer(DEFAULT_LENGTH), new Boolean(
                            DEFAULT_UNIQUE), new Integer(DEFAULT_NCBW), DEFAULT_CASE);
        }
    }

    private final String getDefaultParametersToString() {
        String defaultParametersString = ""; //NOT LOCALIZABLE
        for (int i = 0; i < listConcepts.length; i++) {
            ORPNGParameters param = new ORPNGParameters((MetaClass) listConcepts[i],
                    DEFAULT_STATUS, DEFAULT_REPLACEMENT_STRING, new Integer(DEFAULT_LENGTH),
                    new Boolean(DEFAULT_UNIQUE), new Integer(DEFAULT_NCBW), DEFAULT_CASE);
            defaultParametersString = defaultParametersString + param.toString()
                    + CONCEPT_PARAM_SEPARATOR;
        }
        return defaultParametersString;
    }

    public final String getParametersToString() {
        String parametersString = ""; //NOT LOCALIZABLE
        for (int i = 0; i < parameters.length; i++) {
            parametersString = parametersString + parameters[i].toString()
                    + CONCEPT_PARAM_SEPARATOR;
        }
        return parametersString;
    }

    public final void setDefault() {
        populateParameters();
    }

    public final ArrayList getParametersToGenerate() {
        ArrayList toGenerate = new ArrayList();
        for (int i = 0; i < parameters.length; i++) {
            ORPNGParameters param = parameters[i];
            if ((param.getStatus() == ORPNGStatus.getInstance(ORPNGStatus.PARTIAL))
                    || (param.getStatus() == ORPNGStatus.getInstance(ORPNGStatus.COMPLETE)))
                toGenerate.add(param);
        }
        return toGenerate;
    }

    public String getColumnName(int column) {
        return columnNames[column];
    }

    public int getRowCount() {
        return parameters.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public Object getValueAt(int row, int column) {
        return parameters[row].get(column);

    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col) {
        if (col < 1) {
            return false;
        } else {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col) {
        if (parameters[0].get(col) instanceof Integer && !(value instanceof Integer)) {
            try {
                Integer iValue = new Integer(value.toString());
                if (col == 3) {
                    Integer length = iValue;
                    Integer wordLength = (Integer) getValueAt(row, 5);
                    if (length.intValue() < 4 || length.intValue() < wordLength.intValue())
                        return;
                } else if (col == 5) {
                    Integer wordLength = iValue;
                    Integer length = (Integer) getValueAt(row, 3);
                    if (wordLength.intValue() <= 0 || wordLength.intValue() > length.intValue())
                        return;
                }

                parameters[row].set(col, iValue);
                fireTableCellUpdated(row, col);
            } catch (NumberFormatException e) {
                //Invalide Integer because length to long! do nothing
            }
        } else if (parameters[0].get(col) instanceof Integer && (value instanceof Integer)) {
            if (col == 3) {
                Integer length = (Integer) value;
                Integer wordLength = (Integer) getValueAt(row, 5);
                if (length.intValue() < 4 || length.intValue() < wordLength.intValue())
                    return;
            } else if (col == 5) {
                Integer wordLength = (Integer) value;
                Integer length = (Integer) getValueAt(row, 3);
                if (wordLength.intValue() <= 0 || wordLength.intValue() > length.intValue())
                    return;
            }
            parameters[row].set(col, value);
            fireTableCellUpdated(row, col);
        } else {
            parameters[row].set(col, value);
            fireTableCellUpdated(row, col);
        }

    }

    private final void populateParametersFromString(String parameterListString) {
        try {
            for (int i = 0; i < parameters.length; i++) {
                String conceptParam = parameterListString.substring(0, parameterListString
                        .indexOf(CONCEPT_PARAM_SEPARATOR));
                ORPNGParameters param = getPNGParametersFromString(conceptParam);
                if (param == null) {
                    populateParameters();
                    return;
                }

                parameters[i] = param;
                parameterListString = parameterListString.substring(parameterListString
                        .indexOf(CONCEPT_PARAM_SEPARATOR)
                        + CONCEPT_PARAM_SEPARATOR.length(), parameterListString.length());
            }
        } catch (Exception e) {
            populateParameters();
        }
    }

    private final ORPNGParameters getPNGParametersFromString(String PNGParamString) {
        ORPNGParameters orPNGParameters = null;
        //MetaClass , ORPNGStatus , String , Integer , Boolean , Integer , ORPNGCase
        MetaClass metaClass = null;
        ORPNGStatus status = null;
        Integer length = null;
        Boolean unique = null;
        Integer nbrCharByWord = null;
        ORPNGCase pnCase = null;
        Integer integer = null;

        String metaClassString, statusString, replacementChar, lengthString, uniqueString, nbrCharByWordString;

        metaClassString = PNGParamString.substring(0, PNGParamString
                .indexOf(ORPNGParameters.SEPARATOR));
        metaClass = getMetaClassFromString(metaClassString);
        if (metaClass == null)
            return null;
        PNGParamString = PNGParamString.substring(PNGParamString.indexOf(ORPNGParameters.SEPARATOR)
                + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
        statusString = PNGParamString.substring(0, PNGParamString
                .indexOf(ORPNGParameters.SEPARATOR));
        try {
            integer = Integer.valueOf(statusString);
            status = ORPNGStatus.getInstance(integer.intValue());
            if (status == null)
                return null;
            PNGParamString = PNGParamString.substring(PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR)
                    + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
            replacementChar = PNGParamString.substring(0, PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR));
            PNGParamString = PNGParamString.substring(PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR)
                    + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
            lengthString = PNGParamString.substring(0, PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR));
            length = Integer.valueOf(lengthString);
            PNGParamString = PNGParamString.substring(PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR)
                    + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
            uniqueString = PNGParamString.substring(0, PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR));
            unique = Boolean.valueOf(uniqueString);
            PNGParamString = PNGParamString.substring(PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR)
                    + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
            nbrCharByWordString = PNGParamString.substring(0, PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR));
            nbrCharByWord = Integer.valueOf(nbrCharByWordString);
            PNGParamString = PNGParamString.substring(PNGParamString
                    .indexOf(ORPNGParameters.SEPARATOR)
                    + ORPNGParameters.SEPARATOR.length(), PNGParamString.length());
            integer = Integer.valueOf(PNGParamString);
            pnCase = ORPNGCase.getInstance(integer.intValue());
            if (pnCase == null)
                return null;
        } catch (Exception e) {
            return null;
        }

        return new ORPNGParameters(metaClass, status, replacementChar, length, unique,
                nbrCharByWord, pnCase);
    };

    private final MetaClass getMetaClassFromString(String metaClassString) {
        MetaClass metaClass = null;
        for (int i = 0; i < listConcepts.length; i++) {
            if (((MetaClass) listConcepts[i]).getGUIName().equals(metaClassString)) {
                metaClass = (MetaClass) listConcepts[i];
                break;
            }
        }
        return metaClass;
    }

    public final void saveParametersInProperties(PropertiesSet physicalNameGenerationPropertiesSet) {
        physicalNameGenerationPropertiesSet.setProperty(GeneratePhysicalNameModel.class,
                CONCEPTS_PARAMETERS, getParametersToString());

    }
}
