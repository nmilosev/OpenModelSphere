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

package org.modelsphere.sms.db.srtypes;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.or.db.DbORDiagram;

public class SMSMultiplicity extends IntDomain {

    static final long serialVersionUID = -6189338175335611972L;

    public static final int UNDEFINED = 0;
    public static final int MANY = 1;
    public static final int ONE_OR_MORE = 2;
    public static final int EXACTLY_ONE = 3;
    public static final int OPTIONAL = 4;
    public static final int SPECIFIC = 5;

    public static final SMSMultiplicity[] objectPossibleValues = new SMSMultiplicity[] {
            new SMSMultiplicity(MANY), new SMSMultiplicity(ONE_OR_MORE),
            new SMSMultiplicity(EXACTLY_ONE), new SMSMultiplicity(OPTIONAL),
            new SMSMultiplicity(UNDEFINED), new SMSMultiplicity(SPECIFIC) };

    public static String[] stringPossibleValues = new String[] { LocaleMgr.misc.getString("Many"),
            LocaleMgr.misc.getString("OneOrMore"), LocaleMgr.misc.getString("ExactlyOne"),
            LocaleMgr.misc.getString("Optional"), LocaleMgr.misc.getString("Undefined"),
            LocaleMgr.misc.getString("Specific"), };

    public static SMSMultiplicity getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    //Parameterless constructor
    public SMSMultiplicity() {
        stringPossibleValues = new String[] { Many, OneOrMore, ExactlyOne, Optional, Undefined,
                Specific };
    }

    protected SMSMultiplicity(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSMultiplicity(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public static String Many = LocaleMgr.misc.getString("Many");
    public static String OneOrMore = LocaleMgr.misc.getString("OneOrMore");
    public static String ExactlyOne = LocaleMgr.misc.getString("ExactlyOne");
    public static String Optional = LocaleMgr.misc.getString("Optional");
    public static String Undefined = LocaleMgr.misc.getString("Undefined");
    public static String Specific = LocaleMgr.misc.getString("Specific");

    public static String ManyOR = LocaleMgr.misc.getString("ManyOR");
    public static String OneOrMoreOR = LocaleMgr.misc.getString("OneOrMoreOR");
    public static String ExactlyOneOR = LocaleMgr.misc.getString("ExactlyOneOR");
    public static String OptionalOR = LocaleMgr.misc.getString("OptionalOR");
    public static String UndefinedOR = LocaleMgr.misc.getString("UndefinedOR");

    public static String ManyUML = LocaleMgr.misc.getString("ManyUML");
    public static String OneOrMoreUML = LocaleMgr.misc.getString("OneOrMoreUML");
    public static String ExactlyOneUML = LocaleMgr.misc.getString("ExactlyOneUML");
    public static String OptionalUML = LocaleMgr.misc.getString("OptionalUML");
    public static String UndefinedUML = LocaleMgr.misc.getString("UndefinedUML");

    public final String[] getStringPossibleValues() {
        return new String[] { Many, OneOrMore, ExactlyOne, Optional, Undefined, Specific };
    }

    public final String[] getUIStringPossibleValues() {

        ApplicationDiagram ad = ApplicationContext.getFocusManager().getActiveDiagram();
        if (ad != null) {
            DbObject semObj = ad.getDiagramGO();
            if (semObj != null) {
                if (semObj instanceof DbORDiagram)
                    return new String[] { ManyOR, OneOrMoreOR, ExactlyOneOR, OptionalOR,
                            UndefinedOR, Specific };
                else if (semObj instanceof DbOODiagram)
                    return new String[] { ManyUML, OneOrMoreUML, ExactlyOneUML, OptionalUML,
                            UndefinedUML, Specific };
                else
                    return new String[] { Many, OneOrMore, ExactlyOne, Optional, Undefined,
                            Specific };
            } else
                return new String[] { Many, OneOrMore, ExactlyOne, Optional, Undefined, Specific };
        } else
            return new String[] { Many, OneOrMore, ExactlyOne, Optional, Undefined, Specific };
    }

    public final boolean isMin1() {
        return (value == EXACTLY_ONE || value == ONE_OR_MORE);
    }

    public final boolean isMaxN() {
        return (value == MANY || value == ONE_OR_MORE);
    }

    public final String getUMLMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "0..*";
            break;
        case ONE_OR_MORE:
            label = "1..*";
            break;
        case EXACTLY_ONE:
            label = "1";
            break;
        case OPTIONAL:
            label = "0..1";
            break;
        case SPECIFIC:
            label = "0..2";
            break;
        }
        return label;
    }

    public final String getUMLMinimumMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "0";
            break;
        case ONE_OR_MORE:
            label = "1";
            break;
        case EXACTLY_ONE:
            label = "1";
            break;
        case OPTIONAL:
            label = "0";
            break;
        }
        return label;
    }

    public final String getUMLMaximumMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "*";
            break;
        case ONE_OR_MORE:
            label = "*";
            break;
        case EXACTLY_ONE:
            label = "1";
            break;
        case OPTIONAL:
            label = "1";
            break;
        }
        return label;
    }

    public final String getDatarunMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "0,N";
            break;
        case ONE_OR_MORE:
            label = "1,N";
            break;
        case EXACTLY_ONE:
            label = "1,1";
            break;
        case OPTIONAL:
            label = "0,1";
            break;
        case SPECIFIC:
            label = "0,2";
            break;
        }
        return label;
    }

    public final String getDatarunMinimumMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "0";
            break;
        case ONE_OR_MORE:
            label = "1";
            break;
        case EXACTLY_ONE:
            label = "1";
            break;
        case OPTIONAL:
            label = "0";
            break;
        }
        return label;
    }

    public final String getDatarunMaximumMultiplicityLabel() {
        String label = "";
        switch (getValue()) {
        case MANY:
            label = "N";
            break;
        case ONE_OR_MORE:
            label = "N";
            break;
        case EXACTLY_ONE:
            label = "1";
            break;
        case OPTIONAL:
            label = "1";
            break;
        }
        return label;
    }

}
