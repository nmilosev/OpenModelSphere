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

package org.modelsphere.sms.or.features.extract;

import org.modelsphere.sms.plugins.TargetSystem;

/**
 * RDM and SMS define IDs for supported DBMS. These IDs are different between RDM and SMS. This
 * class allows to find the RDM ID of a given DBMS from its SMS ID.
 */
class RdmDbmsIdMapping {
    //
    // Follows singleton design pattern
    //
    private RdmDbmsIdMapping() {
    }

    private static RdmDbmsIdMapping g_singleton = null;

    public static RdmDbmsIdMapping getSingleton() {
        if (g_singleton == null) {
            g_singleton = new RdmDbmsIdMapping(); // create single instance
        }

        return g_singleton;
    } // end getSingleton()

    //
    // RDMS IDs as defined by RDM (defined in epi_obj.h)
    //
    static final int RDM_ANSI_SQL = 0;
    static final int RDM_ORACLE6 = 4;
    static final int RDM_INFORMIX = 6;
    static final int RDM_ORACLE7 = 23;
    static final int RDM_INFORMIX_SE = 32;
    static final int RDM_INFORMIX_OL = 33;
    static final int RDM_INFORMIX_US = 51;
    static final int RDM_ORACLE8 = 52;
    static final int RDM_INFORMIX_IDS92 = 68;
    static final int RDM_ORACLE8i = 70;
    static final int RDM_ORACLE9i = 71;
    static final int RDM_INFORMIX_IDS93 = 72;

    //
    // Mapping method
    //
    int getRDMCounterPart(int smsID) {
        int rdmID = RDM_ANSI_SQL;

        switch (smsID) {
        //
        // Oracle mapping
        //
        case TargetSystem.SGBD_ORACLE6:
            rdmID = RDM_ORACLE6;
            break;
        case TargetSystem.SGBD_ORACLE7:
            rdmID = RDM_ORACLE7;
            break;
        case TargetSystem.SGBD_ORACLE8:
            rdmID = RDM_ORACLE8;
            break;
        case TargetSystem.SGBD_ORACLE81:
            rdmID = RDM_ORACLE8i;
            break;
        case TargetSystem.SGBD_ORACLE90:
            rdmID = RDM_ORACLE9i;
            break;
        //
        // Informix mapping
        //
        case TargetSystem.SGBD_INFORMIX_SE:
            rdmID = RDM_INFORMIX_SE;
            break;
        case TargetSystem.SGBD_INFORMIX_OL:
            rdmID = RDM_INFORMIX_OL;
            break;
        case TargetSystem.SGBD_INFORMIX_IUS9:
            rdmID = RDM_INFORMIX_US;
            break;
        case TargetSystem.SGBD_INFORMIX_IDS_2000:
            rdmID = RDM_INFORMIX_IDS93;
            break;
        } // end switch

        return rdmID;
    } // end getRDMCounterPart()
} // end RdmDbmsIdMapping
