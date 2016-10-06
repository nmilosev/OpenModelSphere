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

package org.modelsphere.jack.gui.wizard2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultWizardModel implements WizardModel {
    private ArrayList<WizardModelListener> wizardListeners = new ArrayList<WizardModelListener>();

    private ArrayList<Page> pages = new ArrayList<Page>();

    private int pageID = -1;

    public void setPages(Page[] pages) {
        Page activePage = null;
        int oldID = pageID;

        List<Page> newPages = Arrays.asList(pages);

        if (newPages.equals(pages)) {
            return;
        }

        if (pageID > -1) {
            activePage = this.pages.get(pageID);
        }

        this.pages.clear();
        this.pages.addAll(newPages);

        if (activePage != null) {
            pageID = this.pages.indexOf(activePage);
        } else if (pageID > -1 && this.pages.size() == 0) {
            pageID = -1;
        }

        fireStructureChanged();
        if (oldID != pageID) {
            fireActivePageChanged();
            fireStateChanged();
        }
    }

    @Override
    public int getPageCount() {
        return pages.size();
    }

    @Override
    public Page getPage(int index) {
        return pages.get(index);
    }

    @Override
    public void addWizardModelListener(WizardModelListener listener) {
        if (wizardListeners.indexOf(listener) == -1)
            wizardListeners.add(listener);
    }

    @Override
    public void removeWizardModelListener(WizardModelListener listener) {
        wizardListeners.remove(listener);
    }

    protected void fireStructureChanged() {
        for (int i = wizardListeners.size(); --i >= 0;) {
            WizardModelListener listener = wizardListeners.get(i);
            listener.structureChanged();
        }
    }

    protected void fireActivePageChanged() {
        for (int i = wizardListeners.size(); --i >= 0;) {
            WizardModelListener listener = wizardListeners.get(i);
            listener.activePageChanged();
        }
    }

    protected void fireStateChanged() {
        for (int i = wizardListeners.size(); --i >= 0;) {
            WizardModelListener listener = wizardListeners.get(i);
            listener.stateChanged();
        }
    }

    @Override
    public boolean nextEnabled() {
        return pages.size() > 0 && pageID < pages.size() - 1
                && !(pages.get(pageID + 1) instanceof UndefinedPage);
    }

    @Override
    public boolean previousEnabled() {
        return pageID > 0;
    }

    public int getPageID() {
        return pageID;
    }

    public void setPageID(int pageID) {
        this.pageID = pageID;
        fireActivePageChanged();
    }

    @Override
    public int getNextPage() {
        if (pageID >= pages.size() - 1)
            return -1;
        return pageID + 1;
    }

    @Override
    public int getPreviousPage() {
        if (pageID > 0)
            return pageID - 1;
        return -1;
    }

    @Override
    public boolean finishEnabled() {
        return pages.size() > 0 && pageID == pages.size() - 1;
    }

    @Override
    public String getWarning() {
        return null;
    }

    @Override
    public Page getPage() {
        if (pageID < 0)
            return null;
        return pages.get(pageID);
    }

}
