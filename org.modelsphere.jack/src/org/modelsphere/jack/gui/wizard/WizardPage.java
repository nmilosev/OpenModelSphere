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

package org.modelsphere.jack.gui.wizard;

import java.awt.*;
import javax.swing.*;

/**
 * 
 * Generic wizard panel
 * 
 */
public abstract class WizardPage extends JPanel {

    protected WizardPage() {
        super();
    }

    protected WizardPage(LayoutManager layout) {
        super(layout);
    }

    /**
     * This method is called by the wizard after adding the page.
     * 
     * This method should be used to init the GUI with the options.
     */
    public abstract boolean initialize(Object configuration);

    /**
     * This method is called by the wizard before removing the page.
     * 
     * This method should be used to save GUI setting in the options and to remove any references
     * that may prevent garbage collection.
     * 
     * Use the saveData parameter to determine if data should be processed and updated. (Usually,
     * this value will be true if the wizard is going forward and false if going backward.
     */
    public abstract boolean terminate(Object configuration, boolean saveData);

    public Wizard getWizard() {
        Component parent = getParent();
        while ((parent != null) && !(parent instanceof Wizard))
            parent = parent.getParent();
        return (Wizard) parent;
    }

    /**
     * This method enable or disabled the next(finish) button. When a new page is displayed, the
     * next button is disabled by the wizard
     */
    protected final void setNextEnabled(boolean enable) {
        Wizard wizard = getWizard();
        if (wizard != null) {
            wizard.setNextEnabled(enable);
        }
    }

}
