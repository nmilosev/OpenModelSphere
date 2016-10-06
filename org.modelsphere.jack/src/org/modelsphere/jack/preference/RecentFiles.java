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

package org.modelsphere.jack.preference;

import java.io.File;
import java.util.ArrayList;

public class RecentFiles implements PropertiesProvider {
    public static final String PROPERTY_NB_RECENT_FILE = "RecentFilesDisplayed"; // NOT LOCALIZABLE
    public static final Integer PROPERTY_NB_RECENT_FILE_DEFAULT = new Integer(9);
    public static final int PROPERTY_NB_RECENT_FILE_MIN = 1;
    public static final int PROPERTY_NB_RECENT_FILE_MAX = 30;

    private static int DEFAULT_NB_OF_STORED_RF = 9; // maximun stored recent files
    private static int DEFAULT_NB_OF_VISIBLE_RF = 9; // maximun visible recent files in gui
    private static final boolean DEFAULT_VIEW_RF = true; // if the recent files are visible in gui

    private ArrayList files;
    private int nbOfVisibleRF;
    private boolean viewRF;
    private PropertiesSet appPref;

    public RecentFiles() {
        appPref = PropertiesManager.APPLICATION_PROPERTIES_SET;
        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences != null) {
            DEFAULT_NB_OF_STORED_RF = preferences.getPropertyInteger(RecentFiles.class,
                    PROPERTY_NB_RECENT_FILE, PROPERTY_NB_RECENT_FILE_DEFAULT).intValue();
            DEFAULT_NB_OF_VISIBLE_RF = preferences.getPropertyInteger(RecentFiles.class,
                    PROPERTY_NB_RECENT_FILE, PROPERTY_NB_RECENT_FILE_DEFAULT).intValue();
            nbOfVisibleRF = DEFAULT_NB_OF_VISIBLE_RF;
            files = new ArrayList(DEFAULT_NB_OF_STORED_RF);
            viewRF = DEFAULT_VIEW_RF;
            PropertiesManager.registerPropertiesProvider(this);
            for (int i = DEFAULT_NB_OF_STORED_RF - 1; i >= 0; i--) {
                String fileName = appPref.getPropertyString(RecentFiles.class, "" + i, null);
                if ((fileName != null) && (!fileName.equals("")))
                    add(fileName);
            } // end for
        } // end if
    } // end RecentFiles()

    public final void add(String obj) {
        files.remove(obj);
        if (files.size() == DEFAULT_NB_OF_STORED_RF)
            files.remove(DEFAULT_NB_OF_STORED_RF - 1);
        files.add(0, obj);
    }

    public final void remove(String obj) {
        files.remove(obj);
    }

    public final void clean() {
        int i = 0;
        while (i < files.size()) {
            File file = new File((String) files.get(i));
            if (!file.exists())
                files.remove(i);
            else
                i++;
        }
    }

    public final void updateProperties() {
        for (int i = 0; i < PROPERTY_NB_RECENT_FILE_MAX; i++) {
            appPref.removeProperty(RecentFiles.class, "" + i);
        }
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i) != null)
                appPref.setProperty(RecentFiles.class, "" + i, (String) files.get(i));
        }
    }

    public final int size() {
        return files.size();
    }

    public final String elementAt(int index) {
        return (String) files.get(index);
    }

    public final int getNbVisibleRF() {
        return nbOfVisibleRF;
    }

    public final void setNbVisibleRF(int nb) {
        if (nb > 0 && nb < 10)
            nbOfVisibleRF = nb;
    }

    public final boolean isVisible() {
        return viewRF;
    }

    public final void setVisible(boolean v) {
        viewRF = v;
    }

    public final void registerDefaultPreferences() {
        files.ensureCapacity(DEFAULT_NB_OF_STORED_RF);
        nbOfVisibleRF = DEFAULT_NB_OF_VISIBLE_RF;
        viewRF = DEFAULT_VIEW_RF;
    }

    public final void uninstall() {
        updateProperties();
        PropertiesManager.unregisterPropertiesProvider(this);
    }
}
