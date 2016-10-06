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

package org.modelsphere.jack.awt.beans;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class is responsible to create a modal dialog and to show it; when the dialog is closed, the
 * bean passed as parameter is updated with the new values changed within the dialog. These
 * constraints must be met be the class implementing this interface:
 * 
 * - the dialog's title is beaninfo.getBeanDescriptor().getDisplayName(); - the dialog's icon is the
 * owner's icon, unless beaninfo.getImage() returns a not null value - the dialog display jTable
 * whose rows are the bean's properties, and whose columns are their name and value (such as
 * jack.srtool.screen.DesignPanel), unless beaninfo.getBeanDescriptor().getCustomizerClass() returns
 * a not null value - for each property, display getShortDescription() as tooltip - if at least one
 * propety isHidden(), implements a Shows/Hides details button.
 */
public interface BeanDialog {

    /*
     * @param bean the bean whose properties are set by the dialog
     * 
     * @param beaninfo info about the bean
     */

    /**
     * Creates a modal dialog and shows it.
     * 
     * @param owner
     *            the container who owns the modal dialog
     */
    public void showPropertyDialog(JFrame owner);

    /**
     * Creates a modal dialog and shows it.
     * 
     * @param owner
     *            the container who owns the modal dialog
     */
    public void showPropertyDialog(JDialog owner);

    // For implementation

    // create a JPanel from the bean's properties
    // normally called by showPropertyDialog(), but you can call it directly
    public JPanel createPanel();

    // force the end of all cells' edition
    public void stopCellEditing();
}
