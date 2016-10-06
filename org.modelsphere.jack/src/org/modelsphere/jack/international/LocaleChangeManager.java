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

package org.modelsphere.jack.international;

import java.util.Locale;
import java.util.Vector;

/**
 * LocaleChangeManager is a non-visual java bean which notifies registered observers when a locale
 * change occurs. LocaleChangeManager itself must be registered with a LocaleChangeEvent source, in
 * this case LocaleChooser. Using a LocaleChangeManager rather than having locale-aware components
 * register themselves directly with the user-interface for selecting locales has two advantages:
 *<ol>
 *<li>Switching to a different locale chooser only requires changing one hookup, the
 * LocaleChangeManager to the locale chooser component.
 *<li>Locale-aware components don't need to have (global) access to the locale chooser component.
 *</ol>
 * 
 */
public class LocaleChangeManager implements LocaleChangeListener {
    // holds the single instance of the LocaleChangeManager
    private static LocaleChangeManager localeChangeManager = null;
    // holds registered event listeners
    Vector localeChangeListeners = new Vector();
    private static Locale locale = Locale.getDefault();

    protected LocaleChangeManager() {
    }

    /**
     * Returns the single instance of the locale change manager.
     * 
     * @return instance of locale change manager
     */
    public static LocaleChangeManager getLocaleChangeManager() {
        if (localeChangeManager == null) {
            localeChangeManager = new LocaleChangeManager();
        }
        return localeChangeManager;
    }

    /**
     * Returns the current locale according to the locale change manager
     * 
     * @return locale change manager's current locale
     */
    public static Locale getLocale() {
        return new Locale(locale.getLanguage(), locale.getCountry(), locale.getVariant());
    }

    /**
     * Registers a LocaleChangeListener to be notified on a locale change event.
     * 
     * @param listener
     *            LocaleChangeListener to be added to notification list
     */
    public synchronized void addLocaleChangeListener(LocaleChangeListener listener) {
        localeChangeListeners.addElement(listener);
    }

    /**
     * Removes a LocaleChangeListener to be notified on a locale change event.
     * 
     * @param listener
     *            LocaleChangeListener to be removed
     */
    public synchronized void removeLocaleChangeListener(LocaleChangeListener listener) {
        localeChangeListeners.removeElement(listener);
    }

    /**
     * Required for implementation of the LocaleChangeListener interface. Notifies all registered
     * listeners of a locale change
     * 
     * @param e
     *            LocaleChangeEvent describing new locale
     */
    public void localeChanged(LocaleChangeEvent e) {
        Vector listeners;

        locale = e.getLocale();

        synchronized (this) {
            listeners = (Vector) localeChangeListeners.clone();
        }
        for (int i = 0; i < listeners.size(); i++) {
            ((LocaleChangeListener) listeners.elementAt(i)).localeChanged(e);
        }
    }
}
