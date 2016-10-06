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
// Possible Values :
// VALUE
// REFERENCE
// NOT NAVIGABLE
package org.modelsphere.sms.or.db.srtypes;

import java.awt.Image;

import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.or.international.LocaleMgr;

public class ORNotationSymbol extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int NONE = 0;
    public static final int SINGLE_LINE = 1;
    public static final int CARRET = 2;
    public static final int EMPTY_SINGLE_ARROW = 3;
    public static final int EMPTY_DOUBLE_ARROW = 4;
    public static final int FILLED_SINGLE_ARROW = 5;
    public static final int FILLED_DOUBLE_ARROW = 6;
    public static final int EMPTY_HALF_LARGE_DIAMOND = 7;
    public static final int EMPTY_HALF_LARGE_SQUARE = 8;
    public static final int EMPTY_HALF_LARGE_CIRCLE = 9;
    public static final int EMPTY_SMALL_CIRCLE = 10;
    public static final int FILLED_SMALL_CIRCLE = 11;
    public static final int EMPTY_SMALL_SQUARE = 12;
    public static final int FILLED_SMALL_SQUARE = 13;
    public static final int EMPTY_SMALL_DIAMOND = 14;
    public static final int FILLED_SMALL_DIAMOND = 15;

    public static final ORNotationSymbol[] objectPossibleValues = new ORNotationSymbol[] {
            new ORNotationSymbol(NONE), new ORNotationSymbol(SINGLE_LINE),
            new ORNotationSymbol(CARRET), new ORNotationSymbol(EMPTY_SINGLE_ARROW),
            new ORNotationSymbol(EMPTY_DOUBLE_ARROW), new ORNotationSymbol(FILLED_SINGLE_ARROW),
            new ORNotationSymbol(FILLED_DOUBLE_ARROW),
            new ORNotationSymbol(EMPTY_HALF_LARGE_DIAMOND),
            new ORNotationSymbol(EMPTY_HALF_LARGE_SQUARE),
            new ORNotationSymbol(EMPTY_HALF_LARGE_CIRCLE),
            new ORNotationSymbol(EMPTY_SMALL_CIRCLE), new ORNotationSymbol(FILLED_SMALL_CIRCLE),
            new ORNotationSymbol(EMPTY_SMALL_SQUARE), new ORNotationSymbol(FILLED_SMALL_SQUARE),
            new ORNotationSymbol(EMPTY_SMALL_DIAMOND), new ORNotationSymbol(FILLED_SMALL_DIAMOND) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("nosymbol"), LocaleMgr.misc.getString("singleline"),
            LocaleMgr.misc.getString("carret"), LocaleMgr.misc.getString("emptysinglearrow"),
            LocaleMgr.misc.getString("emptydoublearrow"),
            LocaleMgr.misc.getString("filledsinglearrow"),
            LocaleMgr.misc.getString("filleddoublearrow"),
            LocaleMgr.misc.getString("emptyhalfldiamond"),
            LocaleMgr.misc.getString("emptyhalflsquare"),
            LocaleMgr.misc.getString("emptyhalflcircle"),
            LocaleMgr.misc.getString("emptysmallcircle"),
            LocaleMgr.misc.getString("filledsmallcircle"),
            LocaleMgr.misc.getString("emptysmallsquare"),
            LocaleMgr.misc.getString("filledsmallsquare"),
            LocaleMgr.misc.getString("emptysmalldiamond"),
            LocaleMgr.misc.getString("filledsmalldiamond") };

    public static final Image[] imagePossibleValues = new Image[] {
            LocaleMgr.misc.getImage("nosymbol"), LocaleMgr.misc.getImage("singleline"),
            LocaleMgr.misc.getImage("carret"), LocaleMgr.misc.getImage("emptysinglearrow"),
            LocaleMgr.misc.getImage("emptydoublearrow"),
            LocaleMgr.misc.getImage("filledsinglearrow"),
            LocaleMgr.misc.getImage("filleddoublearrow"),
            LocaleMgr.misc.getImage("emptyhalfldiamond"),
            LocaleMgr.misc.getImage("emptyhalflsquare"),
            LocaleMgr.misc.getImage("emptyhalflcircle"),
            LocaleMgr.misc.getImage("emptysmallcircle"),
            LocaleMgr.misc.getImage("filledsmallcircle"),
            LocaleMgr.misc.getImage("emptysmallsquare"),
            LocaleMgr.misc.getImage("filledsmallsquare"),
            LocaleMgr.misc.getImage("emptysmalldiamond"),
            LocaleMgr.misc.getImage("filledsmalldiamond") };

    public static ORNotationSymbol getInstance(int value) {
        return objectPossibleValues[objectPossibleValues[0].indexOf(value)];
    }

    protected ORNotationSymbol(int value) {
        super(value);
    }

    //Parameterless constructor
    public ORNotationSymbol() {
    }

    public final DbtAbstract duplicate() {
        return new ORNotationSymbol(value);
    }

    public final Domain[] getObjectPossibleValues() {
        return objectPossibleValues;
    }

    public final String[] getStringPossibleValues() {
        return stringPossibleValues;
    }

    public final Image[] getImagePossibleValues() {
        return imagePossibleValues;
    }
}
