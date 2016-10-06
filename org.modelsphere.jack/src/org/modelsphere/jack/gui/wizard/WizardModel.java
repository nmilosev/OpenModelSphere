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

/**
 * 
 * Generic wizard panel
 * 
 */
public interface WizardModel {

    // Get a page sequence id. Given ids must be recognasible by this model.
    // This method is called before changing each page using NEXT in the wizard
    // so it is possible to change the pages sequence.
    public int[] getPagesSequence();

    // Return a WizardPage object represented in this model by <pageid>.
    public WizardPage getPage(int pageid);

    // Return the Wizard title to display for the page represented in this model
    // by <pageid>.
    public String getTitle(int pageid);

    // Return the configuration object for this model. This object is passed
    // through each pages for initialisation and
    // termination.
    public Object getConfiguration();

    // Called from the wizard to notify that the wizard will remove the page
    // represented in this model by <pageid>.
    public boolean beforePageChange(int pageid);

    // Called from the wizard to notify that the wizard just added the page
    // represented in this model by <pageid>.
    public boolean afterPageChange(int pageid);

    // Called from the wizard to notify that the wizard has been disposed and
    // that the user requested a cancel or that
    // an error occurs.
    public void cancel();

    // Called from the wizard to notify that the wizard has been disposed and
    // that the task may start.
    public void finish();

}
