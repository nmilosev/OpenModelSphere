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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.features;

import java.awt.Color;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.jack.graphic.zone.CellRenderingOption;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.sms.db.DbSMSAbstractPackage;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSFeature;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.oo.db.DbOOAbsPackage;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORDiagram;

public class DisplayRecentModifications {

    private static DisplayRecentModifications g_instance = null;

    private DisplayRecentModifications() {
    }

    public static DisplayRecentModifications getInstance() {
        if (g_instance == null) {
            g_instance = new DisplayRecentModifications();
        }

        return g_instance;
    }

    public static final String DISPLAY_RECENT_MODIFS_OPTION_PROPERTY = "DisplayRecentModifsOption";
    public static final int DO_NOT_DISPLAY_RECENT_MODIFS = 0;
    public static final int DISPLAY_SESSION_MODIFS = 1;
    public static final int DISPLAY_UNSAVED_MODIFS = 2;
    public static final int DISPLAY_MODIFS_SINCE = 3;

    public static final String RECENT_MODIFS_COLOR_PROPERTY = "RecentModifsColor";
    public static final String DISP_RECENTLY_MODIFIED_COLUMNS_PROPERTY = "RecentlyModifiedColumns";
    public static final String DISP_RECENTLY_MODIFIED_FIELDS_PROPERTY = "RecentlyModifiedFields";
    public static final String DISP_RECENTLY_MODIFIED_METHODS_PROPERTY = "RecentlyModifiedMethods";

    private static long g_currentSessionStartTime = System.currentTimeMillis();
    private static long g_lastSaveTime = System.currentTimeMillis();

    // private SMSRecentModificationOption m_recentOption;
    private long m_date;

    // private Color m_recentColor;

    /*
     * public DisplayRecentModifications(SMSRecentModificationOption recentOption, long date, Color
     * recentColor) { m_recentOption = recentOption; m_date = date; m_recentColor = recentColor; }
     */

    public CellRenderingOption getRenderingOptionForRecent(DbSMSFeature feature,
            CellRenderingOption option) throws DbException {

        // get property
        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
        int recentDisplay = applOptions.getPropertyInteger(DisplayRecentModifications.class,
                DISPLAY_RECENT_MODIFS_OPTION_PROPERTY, DO_NOT_DISPLAY_RECENT_MODIFS);

        // if no display, exit
        if (recentDisplay == DO_NOT_DISPLAY_RECENT_MODIFS) {
            return option;
        }

        boolean recent = isRecent(feature, recentDisplay);
        if (recent) {
            int recentColor = applOptions.getPropertyInteger(DisplayRecentModifications.class,
                    RECENT_MODIFS_COLOR_PROPERTY, Color.BLUE.getRGB());

            option.setColor(new Color(recentColor));
        }

        /*
         * //do not show recent modifications DbSMSProject project =
         * (DbSMSProject)feature.getCompositeOfType(DbSMSProject.metaClass);
         * SMSRecentModificationOption disp = project.getRecentModificationDisplay(); int
         * displayOption = (disp == null) ? 0 : disp.getValue(); if (displayOption ==
         * SMSRecentModificationOption.DO_NOT_SHOW_RECENT_MODIFS) { return option; }
         */
        return option;
    }

    public static void notifySaveProject(DbSMSProject proj) {

        // get current time
        g_lastSaveTime = System.currentTimeMillis();

        // for each model
        try {
            proj.getDb().beginReadTrans();

            DbRelationN relN = proj.getComponents();
            DbEnumeration enu = relN.elements(DbSMSAbstractPackage.metaClass);
            while (enu.hasMoreElements()) {
                DbSMSAbstractPackage pack = (DbSMSAbstractPackage) enu.nextElement();

                if (pack instanceof DbJVClassModel) {
                    setModelSaveTime((DbJVClassModel) pack);
                } else if (pack instanceof DbORDataModel) {
                    setModelSaveTime((DbORDataModel) pack);
                }
            } // end while
            enu.close();

            proj.getDb().commitTrans();
        } catch (DbException ex) {
        } // end try
    }

    //
    // private methods
    //

    private static void setModelSaveTime(DbOOAbsPackage pack) throws DbException {
        DbRelationN relN = pack.getComponents();

        // for each diagram
        DbEnumeration enu = relN.elements(DbOODiagram.metaClass);
        while (enu.hasMoreElements()) {
            DbOODiagram diagram = (DbOODiagram) enu.nextElement();
            refreshDiagram(diagram);
        } // end while
        enu.close();

        // for each sub package
        enu = relN.elements(DbJVPackage.metaClass);
        while (enu.hasMoreElements()) {
            DbJVPackage subPack = (DbJVPackage) enu.nextElement();
            setModelSaveTime(subPack);
        } // end while
        enu.close();
    } // end setModelSaveTime()

    private static void setModelSaveTime(DbORDataModel dataModel) throws DbException {
        DbRelationN relN = dataModel.getComponents();
        DbEnumeration enu = relN.elements(DbORDiagram.metaClass);

        // for each diagram
        while (enu.hasMoreElements()) {
            DbORDiagram diagram = (DbORDiagram) enu.nextElement();
            refreshDiagram(diagram);

        } // end while
        enu.close();

    } // end setModelSaveTime()

    private static void refreshDiagram(DbSMSDiagram diagram) throws DbException {
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        if (focusObject instanceof ApplicationDiagram) {
            ApplicationDiagram diag = (ApplicationDiagram) focusObject;
            diag.getDiagramInternalFrame().refresh();
        }
    }

    private static boolean isRecent(DbSMSFeature feature, int displayOption) throws DbException {
        boolean recent = false;
        Object o = feature.getModificationTime();

        if (o instanceof Long) {
            long modificationTime = (Long) o;

            switch (displayOption) {
            case DISPLAY_SESSION_MODIFS:
                recent = modificationTime > g_currentSessionStartTime;
                break;
            case DISPLAY_UNSAVED_MODIFS:
                recent = modificationTime > g_lastSaveTime;
                break;
            case DISPLAY_MODIFS_SINCE:
                recent = false; // TODO modificationTime > g_lastSaveTime;
                break;
            }
        } // end if

        return recent;
    } // end isRecent()

}
