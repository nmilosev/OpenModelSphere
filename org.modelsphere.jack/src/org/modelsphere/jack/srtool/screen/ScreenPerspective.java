/*************************************************************************

Copyright (C) 2010 Grandite

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
package org.modelsphere.jack.srtool.screen;

import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;

/*
 * A perspective is a configuration of the screen content.
 * 
 * Full Version: Everything is available
 * Data Model Viewer: Allows graphical modifications, but prevents semantic changes 
 */
public class ScreenPerspective {
	//full or data model viewer?
    public enum ModelerPerspective { FULL_VERSION, DATA_MODEL_VIEWER }
    protected static ModelerPerspective _modelerPerspective = ModelerPerspective.FULL_VERSION; 
    
    //which terminology?
    public enum TerminologyPerspective { OMS_TERMINOLOGY, RDM_TERMINOLOGY }
    protected static TerminologyPerspective _terminologyPerspective = TerminologyPerspective.OMS_TERMINOLOGY; 
    
    public static boolean isFullVersion() {
        return _modelerPerspective == ModelerPerspective.FULL_VERSION;
    }
    
    public static boolean isOMSTerminology() {
        return _terminologyPerspective == TerminologyPerspective.OMS_TERMINOLOGY;
    }

	public static void handleApplicationHasNoName() {
		DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
		String msg = "Application Name is Null"; 
		//ExceptionHandler.showErrorMessage(mf, msg);
	}

    
}

