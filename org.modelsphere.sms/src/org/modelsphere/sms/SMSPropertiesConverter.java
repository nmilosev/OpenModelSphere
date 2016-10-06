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

package org.modelsphere.sms;

import org.modelsphere.jack.preference.PropertiesConverter;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.preference.RecentFiles;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.preference.DiagramAutomaticCreationOptionGroup;
import org.modelsphere.sms.preference.DiagramStampOptionGroup;
import org.modelsphere.sms.preference.DirectoryOptionGroup;
import org.modelsphere.sms.preference.DisplayGUIOptionGroup;
import org.modelsphere.sms.preference.DisplayWindowsOptionGroup;

public final class SMSPropertiesConverter implements PropertiesConverter {

    public void convert(PropertiesSet set, int oldversion) {
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (set == preferences) {
            if (oldversion == 0) {
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.ComponentsHeaderVisible",
                                DisplayGUIOptionGroup.class, "ComponentsHeaderVisible"); // NOT LOCALIZABLE
                set.renameKey(null,
                        "org.modelsphere.jack.srtool.preference.Preferences.UseStampImage",
                        DiagramStampOptionGroup.class, "UseStampImage"); // NOT
                // LOCALIZABLE
                set.renameKey(null,
                        "org.modelsphere.jack.srtool.preference.Preferences.StampImagePath",
                        DiagramStampOptionGroup.class, "StampImagePath"); // NOT
                // LOCALIZABLE
                set.renameKey(null,
                        "org.modelsphere.jack.srtool.preference.Preferences.ApplicationLocale",
                        ApplicationContext.class, "ApplicationLocale"); // NOT
                // LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.CreateCommonItemModelDiagrams",
                                DiagramAutomaticCreationOptionGroup.class,
                                "CreateCommonItemModelDiagrams"); // NOT
                // LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.CreateDataModelDiagrams",
                                DiagramAutomaticCreationOptionGroup.class,
                                "CreateDataModelDiagrams"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.CreateDomainModelDiagrams",
                                DiagramAutomaticCreationOptionGroup.class,
                                "CreateDomainModelDiagrams"); // NOT LOCALIZABLE
                set.renameKey(null,
                        "org.modelsphere.jack.srtool.preference.Preferences.CreateJavaDiagrams",
                        DiagramAutomaticCreationOptionGroup.class, "CreateJavaDiagrams"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.DDLGenerationDirectory",
                                DirectoryOptionGroup.class, "DDLGenerationDirectory"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.DefaultWorkingDirectory",
                                ApplicationContext.class, "DefaultWorkingDirectory"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.DiagramFrameDefaultSize",
                                DisplayWindowsOptionGroup.class, "DiagramFrameDefaultSize"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.HtmlGenerationDirectory",
                                DirectoryOptionGroup.class, "HtmlGenerationDirectory"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.JavaGenerationDirectory",
                                DirectoryOptionGroup.class, "JavaGenerationDirectory"); // NOT LOCALIZABLE
                set
                        .renameKey(
                                null,
                                "org.modelsphere.jack.srtool.preference.Preferences.PropertyFrameDefaultSize",
                                DisplayWindowsOptionGroup.class, "PropertyFrameDefaultSize"); // NOT LOCALIZABLE
            }
            if (oldversion < 303) {
                // update stamp default image
                String stamp = set
                        .getPropertyString(
                                org.modelsphere.sms.preference.DiagramStampOptionGroup.class,
                                org.modelsphere.sms.preference.DiagramStampOptionGroup.STAMP_IMAGE_PATH,
                                org.modelsphere.sms.preference.DiagramStampOptionGroup.STAMP_IMAGE_PATH_DEFAULT);
                if (stamp != null
                        && (stamp.indexOf("sms_stamp_2_0.jpg") > -1
                                || stamp.indexOf("sms_stamp_1_0.jpg") > -1 || stamp
                                .indexOf("sr_stamp.gif") > -1)) {
                    set
                            .setProperty(
                                    org.modelsphere.sms.preference.DiagramStampOptionGroup.class,
                                    org.modelsphere.sms.preference.DiagramStampOptionGroup.STAMP_IMAGE_PATH,
                                    org.modelsphere.sms.preference.DiagramStampOptionGroup.STAMP_IMAGE_PATH_DEFAULT);
                }
            }
            if (oldversion < 307) {
                // update stamp default image
                Boolean zoom = set
                        .getPropertyBoolean(
                                "org.modelsphere.jack.srtool.actions.ZoomAction",
                                org.modelsphere.jack.actions.AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION,
                                Boolean.TRUE);

                set
                        .setProperty(
                                "org.modelsphere.jack.srtool.actions.ZoomAction.in",
                                org.modelsphere.jack.actions.AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION,
                                zoom.booleanValue());
                set
                        .setProperty(
                                "org.modelsphere.jack.srtool.actions.ZoomAction.out",
                                org.modelsphere.jack.actions.AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION,
                                zoom.booleanValue());
                set
                        .removeProperty(
                                "org.modelsphere.jack.srtool.actions.ZoomAction",
                                org.modelsphere.jack.actions.AbstractApplicationAction.TOOLBAR_VISIBLE_OPTION);
            }
        } else if (set == PropertiesManager.APPLICATION_PROPERTIES_SET) {
            switch (oldversion) {
            case 0: // version 1.0
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.0",
                        RecentFiles.class, "0"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.1",
                        RecentFiles.class, "1"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.2",
                        RecentFiles.class, "2"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.3",
                        RecentFiles.class, "3"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.4",
                        RecentFiles.class, "4"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.5",
                        RecentFiles.class, "5"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.6",
                        RecentFiles.class, "6"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.7",
                        RecentFiles.class, "7"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.8",
                        RecentFiles.class, "8"); // NOT LOCALIZABLE
                set.renameKey(null, "org.modelsphere.jack.srtool.preference.RecentFiles.9",
                        RecentFiles.class, "9"); // NOT LOCALIZABLE
                set.removeProperty("org.modelsphere.jack.srtool.screen.DesignPanel",
                        "verticalLocation");
                // no break, all update must be chained
                // case xxx:
            }
        }
    }

}
