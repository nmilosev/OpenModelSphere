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

import java.awt.Image;

import org.modelsphere.jack.baseDb.db.srtypes.DbtAbstract;
import org.modelsphere.jack.baseDb.db.srtypes.Domain;
import org.modelsphere.jack.baseDb.db.srtypes.IntDomain;
import org.modelsphere.jack.graphic.shape.ActivityPillShape;
import org.modelsphere.jack.graphic.shape.DiamondShape;
import org.modelsphere.jack.graphic.shape.GraphicShape;
import org.modelsphere.jack.graphic.shape.MeriseActorShape;
import org.modelsphere.jack.graphic.shape.MeriseProcessShape;
import org.modelsphere.jack.graphic.shape.OvalShape;
import org.modelsphere.jack.graphic.shape.OvalUnderlinedShape;
import org.modelsphere.jack.graphic.shape.RectangleShape;
import org.modelsphere.jack.graphic.shape.RoundRectShape;
import org.modelsphere.jack.graphic.shape.ShadowRectShape;
import org.modelsphere.jack.graphic.shape.TriangleShape;
import org.modelsphere.jack.graphic.shape.UmlActorShape;
import org.modelsphere.jack.graphic.shape.UmlComponentShape;
import org.modelsphere.jack.graphic.shape.UmlNodeShape;
import org.modelsphere.jack.graphic.shape.UmlUseCaseShape;
import org.modelsphere.sms.international.LocaleMgr;

public class SMSNotationShape extends IntDomain {

    static final long serialVersionUID = 0;

    public static final int RECTANGLE = 1;
    public static final int ROUND_RECTANGLE = 2;
    public static final int SHADOW_RECTANGLE = 3;
    public static final int OVALE = 4;
    public static final int RECTANGLE_WITH_ACTOR = 5;
    public static final int OPENED_RECTANGLE_RIGHT = 6;
    public static final int OPENED_RECTANGLE_LEFT_RIGHT = 7;
    public static final int RECTANGLE_WITH_REVERSED_TRIANGLE = 8;
    public static final int RECTANGLE_WITH_UML_ACTOR = 9;
    public static final int OVAL_FOR_UML_USECASE = 10;
    public static final int UML_COMPONENT = 11;
    public static final int UML_NODE = 12;
    public static final int UML_ACTIVITY_PILL = 13;
    public static final int TRIANGLE = 14;
    public static final int DIAMOND = 15;
    public static final int OVAL_UNDERLINED = 16;

    public static final SMSNotationShape[] objectPossibleValues = new SMSNotationShape[] {
            new SMSNotationShape(RECTANGLE), new SMSNotationShape(ROUND_RECTANGLE),
            new SMSNotationShape(SHADOW_RECTANGLE), new SMSNotationShape(OVALE),
            new SMSNotationShape(RECTANGLE_WITH_ACTOR),
            new SMSNotationShape(RECTANGLE_WITH_UML_ACTOR),
            new SMSNotationShape(OVAL_FOR_UML_USECASE),
            new SMSNotationShape(OPENED_RECTANGLE_RIGHT),
            new SMSNotationShape(OPENED_RECTANGLE_LEFT_RIGHT),
            new SMSNotationShape(RECTANGLE_WITH_REVERSED_TRIANGLE),
            new SMSNotationShape(UML_COMPONENT), new SMSNotationShape(UML_NODE),
            new SMSNotationShape(UML_ACTIVITY_PILL), new SMSNotationShape(TRIANGLE),
            new SMSNotationShape(DIAMOND), new SMSNotationShape(OVAL_UNDERLINED) };

    public static final String[] stringPossibleValues = new String[] {
            LocaleMgr.misc.getString("shaperectangle"),
            LocaleMgr.misc.getString("shaperoundrectangle"),
            LocaleMgr.misc.getString("shapeshadowrectangle"),
            LocaleMgr.misc.getString("shapeovale"), LocaleMgr.misc.getString("shaperectactor"),
            LocaleMgr.misc.getString("shaperectumlactor"),
            LocaleMgr.misc.getString("shapeumlusecase"),
            LocaleMgr.misc.getString("shapeopenedrectright"),
            LocaleMgr.misc.getString("shapeopenedrectleftright"),
            LocaleMgr.misc.getString("shaperectwithreversedtriangle"),
            LocaleMgr.misc.getString("shapecomponent"), LocaleMgr.misc.getString("shapenode"),
            LocaleMgr.misc.getString("shapepill"), LocaleMgr.misc.getString("shapetriangle"),
            LocaleMgr.misc.getString("shapediamond"),
            LocaleMgr.misc.getString("shapeovaleaboveline") };

    public static final Image[] imagePossibleValues = new Image[] {
            LocaleMgr.misc.getImage("shaperectangle"),
            LocaleMgr.misc.getImage("shaperoundrectangle"),
            LocaleMgr.misc.getImage("shapeshadowrectangle"), LocaleMgr.misc.getImage("shapeovale"),
            LocaleMgr.misc.getImage("shaperectactor"),
            LocaleMgr.misc.getImage("shaperectumlactor"),
            LocaleMgr.misc.getImage("shapeumlusecase"),
            LocaleMgr.misc.getImage("shapeopenedrectright"),
            LocaleMgr.misc.getImage("shapeopenedrectleftright"),
            LocaleMgr.misc.getImage("shaperectwithreversedtriangle"),
            LocaleMgr.misc.getImage("shapecomponent"), LocaleMgr.misc.getImage("shapenode"),
            LocaleMgr.misc.getImage("shapepill"), LocaleMgr.misc.getImage("shapetriangle"),
            LocaleMgr.misc.getImage("shapediamond"), LocaleMgr.misc.getImage("shapeovaleaboveline") };

    public static SMSNotationShape getInstance(int value) {
        int idx = objectPossibleValues[0].indexOf(value);
        return objectPossibleValues[idx];
    }

    //Parameterless constructor
    public SMSNotationShape() {
    }

    protected SMSNotationShape(int value) {
        super(value);
    }

    public final DbtAbstract duplicate() {
        return new SMSNotationShape(value);
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

    public static final GraphicShape getShape(int value) {
        switch (value) {
        case RECTANGLE:
            return RectangleShape.singleton;
        case ROUND_RECTANGLE:
            return RoundRectShape.singleton;
        case SHADOW_RECTANGLE:
            return ShadowRectShape.singleton;
        case OVALE:
            return OvalShape.singleton;
        case RECTANGLE_WITH_ACTOR:
            return MeriseActorShape.singleton;
        case RECTANGLE_WITH_UML_ACTOR:
            return UmlActorShape.singleton;
        case OVAL_FOR_UML_USECASE:
            return UmlUseCaseShape.singleton;
        case OPENED_RECTANGLE_RIGHT:
            return RectangleShape.tlb_singleton;
        case OPENED_RECTANGLE_LEFT_RIGHT:
            return RectangleShape.tb_singleton;
        case RECTANGLE_WITH_REVERSED_TRIANGLE:
            return MeriseProcessShape.singleton;
        case UML_COMPONENT:
            return UmlComponentShape.singleton;
        case UML_NODE:
            return UmlNodeShape.singleton;
        case UML_ACTIVITY_PILL:
            return ActivityPillShape.singleton;
        case TRIANGLE:
            return TriangleShape.singleton;
        case DIAMOND:
            return DiamondShape.singleton;
        case OVAL_UNDERLINED:
            return OvalUnderlinedShape.singleton;
        default:
            return RectangleShape.singleton;
        }
    }
}
