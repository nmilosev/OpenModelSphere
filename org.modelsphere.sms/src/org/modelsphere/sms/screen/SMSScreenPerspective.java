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
package org.modelsphere.sms.screen;

import java.awt.Toolkit;
import java.util.Locale;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.srtool.AddElement;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.Application;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSUmlExtensibility;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.sms.or.db.DbORUserNode;

//Customization of an application
//Application name and icone: SMSScreenPerspective.setApplicationNameAndSymbols()
//ShowLicence: sms.action.ShowLicenseAction
public class SMSScreenPerspective extends ScreenPerspective {
    //An image used for splash screen located in sms.international.resource package
    private static final String SPLASH_SCREEN_IMAGE = "international/resources/about_en.png";
    
    //Set application name and icon here
    public static void setApplicationNameAndSymbols() {
              
        //Product and suite names are now language-independant (they stay the same for all languages).
        //This is necessary because the application folder is based on these names,
        //and this folder must stay the same notwithstanding the current locale. [MS]
        ApplicationContext.setApplicationName(LocaleMgr.misc.getString("ApplicationName"));
        ApplicationContext.setApplicationVersion(LocaleMgr.misc.getString("ApplicationVersion"));
        ApplicationContext.APPLICATION_BUILD_ID_EXTENSION = ""; //LocaleMgr.misc.getString("ApplicationBuildExt");
        ApplicationContext.APPLICATION_IMAGE_ICON = Toolkit.getDefaultToolkit().getImage(
                LocaleMgr.misc.getUrl("ApplicationName"));
        
        //Application-independant
        ApplicationContext.APPLICATION_BUILD_ID = Application.getBuildId();
        ApplicationContext.REPOSITORY_ROOT_NAME = Application.getRepositoryName();
    }

    //locale is ignored
    //String language = locale.getLanguage();
    //boolean isFrench = language.compareTo("fr") == 0;
    public static String getSplashScreenImagePath(Locale locale) {
        return SPLASH_SCREEN_IMAGE;
    }
    
    public static boolean isVisible(DbObject dbo) {
        boolean visible = true; 
        
        if (_modelerPerspective == ModelerPerspective.DATA_MODEL_VIEWER) {
            if (dbo instanceof DbORUserNode) {
                visible = false;
            } else if (dbo instanceof DbSMSUmlExtensibility) {
                visible = false;
            }
        }
        return visible;
    }

    public static boolean isVisibleElement(AddElement element) {
        boolean visible = true;
        /*if (_perspective == Perspective.DATA_MODEL_VIEWER) {
            MetaClass mc = element.getMetaClass();
            MetaClass diagramMC = DbSMSDiagram.metaClass; 
            visible = diagramMC.isAssignableFrom(mc); 
        }*/
        
        return visible;
    }

}
