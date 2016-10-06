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

package org.modelsphere.jack.preference.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.FocusManager;

/**
 * Singleton class for managing the contextual data in the application.
 * <p>
 * Components interested in persisting contextual data related to a specific db object can
 * implements the {@link ContextComponent} interface. The manager invoke the load and methods by
 * providing the configuration node during start up and shut down.
 * <p>
 * Configuration nodes are ContextComponent specific. Components are free to define their own
 * internal structures to store/read their data.
 * <p>
 * To register a component, use the method {@link #registerComponent(ContextComponent)}.
 * <p>
 * The load / save process depends of the options selected by the user. If the user uncheck the
 * option to restore projects, the load/save methods on ContextComponents are not invoked.
 * 
 * @author gpelletier
 * 
 */
public class ContextManager {

    private static ContextManager instance;

    private Map<String, ContextComponent> components = new HashMap<String, ContextComponent>();

    private ContextIO contextIO = new ContextIO();

    // Finalizer runnable.  This runnable is registered on the main frame 
    // and is invoked during the shut down process
    private Runnable finalizer = new Runnable() {

        @Override
        public void run() {
            PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
            Map<String, ContextComponent> activeComponents = null;
            if (preferences.getPropertyBoolean(ContextOptionGroup.class,
                    ContextOptionGroup.LOAD_LAST_PROJECTS,
                    ContextOptionGroup.LOAD_LAST_PROJECTS_DEFAULT)) {

                if (preferences.getPropertyBoolean(ContextOptionGroup.class,
                        ContextOptionGroup.RESTORE_WORKSPACE,
                        ContextOptionGroup.RESTORE_WORKSPACE_DEFAULT)) {
                    activeComponents = Collections.unmodifiableMap(components);
                }

            }
            contextIO.save(activeComponents);
        }
    };

    private ContextManager() {
    }

    public void init() {
        ApplicationContext.getDefaultMainFrame().registerPostFinalizer(finalizer);

        PropertiesSet preferences = PropertiesManager.getPreferencePropertiesSet();
        if (preferences.getPropertyBoolean(ContextOptionGroup.class,
                ContextOptionGroup.LOAD_LAST_PROJECTS,
                ContextOptionGroup.LOAD_LAST_PROJECTS_DEFAULT)) {
            Map<String, ContextComponent> activeComponents = null;

            if (preferences.getPropertyBoolean(ContextOptionGroup.class,
                    ContextOptionGroup.RESTORE_WORKSPACE,
                    ContextOptionGroup.RESTORE_WORKSPACE_DEFAULT)) {
                activeComponents = Collections.unmodifiableMap(components);
            }

            try {
                FocusManager.getSingleton().setGuiLocked(true);
                contextIO.load(activeComponents);
            } finally {
                FocusManager.getSingleton().setGuiLocked(false);
            }
        }
    }

    /**
     * @return The singleton instance manager.
     */
    public static ContextManager getInstance() {
        if (instance == null)
            instance = new ContextManager();
        return instance;
    }

    /**
     * Register the component as contextual data provider.
     * 
     * @param component
     *            The component to register.
     */
    public void registerComponent(ContextComponent component) {
        if (component == null)
            return;
        String id = component.getID();
        if (id == null || id.trim().length() == 0 || components.containsKey(id)) {
            throw new IllegalArgumentException("ContextManager:  Invalid ID for component "
                    + component);
        }
        components.put(id, component);
    }
}
