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

package org.modelsphere.jack.awt.beans.impl;

import java.awt.*;
import java.awt.event.*;
import java.beans.BeanInfo;
import javax.swing.*;

import org.modelsphere.jack.awt.beans.BeanDialog;

import java.io.Serializable;
import java.util.ArrayList;

// TODO : Support two modes
//  (1) immediate change mode; close button to close dialog (Already supported)
//  (2) differed mode; Apply/Cancel buttons when changes; Apply disabled and Cancel renamed Close when no changes.
//
public class PropertyDialog extends JDialog {
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JButton jButton1 = new JButton();
    JButton jButton2 = new JButton();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    BorderLayout borderLayout1 = new BorderLayout();

    //
    // PUBLIC CONSTRUCTORS
    //
    public PropertyDialog(JFrame owner, String title, String buttonText, ArrayList propertyList) {
        this(owner, title, buttonText, new BeanDialogImpl(propertyList));
    }

    public PropertyDialog(JDialog owner, String title, String buttonText, ArrayList propertyList) {
        this(owner, title, buttonText, new BeanDialogImpl(propertyList));
    }

    public PropertyDialog(JFrame owner, String title, String buttonText, Serializable bean,
            BeanInfo info) {
        this(owner, title, buttonText, new BeanDialogImpl(bean, info));
    }

    public PropertyDialog(JDialog owner, String title, String buttonText, Serializable bean,
            BeanInfo info) {
        this(owner, title, buttonText, new BeanDialogImpl(bean, info));
    }

    //
    // PRIVATE CONSTRUCTORS
    //
    private PropertyDialog(JFrame owner, String title, String buttonText, BeanDialog dialog) {
        super(owner, title, true);
        try {
            jbInit();
            init(dialog, buttonText);
        } catch (Exception ex) {
            ex.printStackTrace();
        } // end try
    } // end PropertyPanel()

    private PropertyDialog(JDialog owner, String title, String buttonText, BeanDialog dialog) {
        super(owner, title, true);
        try {
            jbInit();
            init(dialog, buttonText);
        } catch (Exception ex) {
            ex.printStackTrace();
        } // end try
    } // end PropertyPanel()

    //
    // INIT METHODS
    //
    void jbInit() throws Exception {
        this.getContentPane().setLayout(gridBagLayout1);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        jPanel1.setLayout(borderLayout1);
        jButton1.setText("Apply"); // NOT LOCALIZABLE, for IDE preview, will be
        // localized in init()
        jButton2.setText("Close"); // NOT LOCALIZABLE, for IDE preview, will be
        // localized in init()
        this.getContentPane().add(
                jPanel1,
                new GridBagConstraints(0, 0, 3, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 0, 0));
        this.getContentPane().add(
                jPanel2,
                new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.HORIZONTAL, new Insets(6, 12, 12, 6), 0, 0));
        this.getContentPane().add(
                jButton1,
                new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(6, 6, 12, 6), 0, 0));
        this.getContentPane().add(
                jButton2,
                new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                        GridBagConstraints.NONE, new Insets(6, 6, 12, 12), 0, 0));
    }

    private BeanDialog m_dialog;

    private void init(final BeanDialog dialog, String buttonText) {
        m_dialog = dialog;
        JPanel beanPanel = dialog.createPanel();
        jButton1.setVisible(false); // furthur extension
        JScrollPane scrollpane = new JScrollPane(beanPanel);
        jPanel1.add(scrollpane, BorderLayout.CENTER);
        // jButton1.setText(APPLY); //localize button, furthur extension
        jButton2.setText(buttonText); // localize button here

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                dialog.stopCellEditing();
            }
        });

        jButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                dispose();
            }
        });
    } // end init()

    // OVERRIDES java.awt.Dialog
    public void dispose() {
        m_dialog.stopCellEditing();
        super.dispose();
    }

    //
    // UNIT TEST
    //
    public static void main(String[] args) {
        // Create a property dialog
        JFrame mainframe = new JFrame("BeanInfo"); // NOT LOCALIZABLE, unit test
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create propertyList
        ArrayList propertyList = new ArrayList();
        propertyList.add(new AbstractProperty.BooleanProperty("generate user", true)); // NOT LOCALIZABLE, unit test
        propertyList.add(new AbstractProperty.BooleanProperty("physical specs", false)); // NOT LOCALIZABLE, unit test

        JDialog dialog = new PropertyDialog(mainframe, "Title", "Close", propertyList); // NOT LOCALIZABLE, unit test
        dialog.pack();
        dialog.setSize(2 * dialog.getWidth(), dialog.getHeight());
        org.modelsphere.jack.awt.AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);

        // display the values
        java.util.Iterator iter = propertyList.iterator();
        while (iter.hasNext()) {
            AbstractProperty.BooleanProperty property = (AbstractProperty.BooleanProperty) iter
                    .next();
            Serializable value = property.getValue();
            System.out.println(value.toString());
        }// end while

        System.exit(0);
    } // end main()
}
