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

package org.modelsphere.jack.graphic.tool;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;

import org.modelsphere.jack.awt.*;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.graphic.DiagramView;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.CurrentFocusListener;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;

public class ZoomCombo extends JackComboBox implements CurrentFocusListener {

    private static final String OVERVIEW = LocaleMgr.screen.getString("overview");
    private Object[] values = { OVERVIEW, "25%", "50%", "80%", "100%", "125%", "150%", "200%" };// NOT LOCALIZABLE

    private static final String USER_DEFINED_ZOOM_VALUE = "UserDefinedZoomValue"; // NOT LOCALIZABLE, property key

    private DiagramView diagView = null;
    private boolean userDefinedZoomEnabled = false;
    private boolean externalUpdate = false;

    public ZoomCombo() {
        // add the user defined value if one is specified in the properties file
        PropertiesSet pref = ApplicationContext.getDefaultMainFrame().getApplicationPreferences();
        if (pref != null) {
            String userDefinedZoomValue = pref.getPropertyString(ZoomCombo.class,
                    USER_DEFINED_ZOOM_VALUE, "-1");
            if (!userDefinedZoomValue.equals("-1")) { // NOT LOCALIZABLE
                Object[] newvalues = new String[values.length + 2];
                newvalues[0] = userDefinedZoomValue;
                newvalues[1] = null; // separator
                for (int i = 0; i < values.length; i++) {
                    newvalues[i + 2] = values[i];
                }
                values = newvalues;
                userDefinedZoomEnabled = true;
            }
        } // end if

        for (int i = 0; i < values.length; i++) {
            Object item = values[i];
            if (item == null)
                item = new SeparatorIcon();
            addItem(item);
        }

        setMaximumRowCount(values.length);
        setSelectedItem("100%"); // NOT LOCALIZABLE
        setEditable(true);
        setToolTipText(LocaleMgr.message.getString("zoomLevel"));
        setEnabled(false);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (externalUpdate)
                    return;
                setViewZoomFactor();
            }
        });
        ApplicationContext.getFocusManager().addCurrentFocusListener(this);

    }

    private void updatePreferredSize() {
        int largestitemwidth = SwingUtilities.computeStringWidth(getFontMetrics(getFont()),
                OVERVIEW);
        int preferredwdith = Math.max(largestitemwidth + 10, 60);
        setPreferredSize(new Dimension(preferredwdith, getPreferredSize().height));
    }

    public void updateUI() {
        setPreferredSize(null); // will force evalutation of the new sizes
        super.updateUI();
        updatePreferredSize();
    }

    public final void setDiagramView(DiagramView diagView) {
        this.diagView = diagView;
        if (diagView != null) {
            setSelectedItem(Integer.toString((int) (diagView.getZoomFactor() * 100)) + "%"); // NOT LOCALIZABLE
            setEnabled(true);
        } else
            setEnabled(false);
    }

    private void setViewZoomFactor() {
        String str = (String) getSelectedItem();
        int percent;
        boolean keepvalue = true; // used to ensure that the user def zoom is
        // valid before updating possible values
        if (str.equals(OVERVIEW))
            percent = (int) (diagView.getPanoramaZoomFactor() * 100);
        else {
            if (str.endsWith("%")) // NOT LOCALIZABLE
                str = str.substring(0, str.indexOf("%")); // NOT LOCALIZABLE
            try {
                percent = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                percent = (int) (diagView.getZoomFactor() * 100);
                keepvalue = false;
            }
        }
        percent = Math.max(1, percent);
        diagView.setZoomFactor((float) percent / 100);
        String sPercent = Integer.toString((int) (diagView.getZoomFactor() * 100)) + "%"; // NOT LOCALIZABLE

        if ((getSelectedIndex() == -1) && keepvalue) { // If value is valid and not in the current
            // list, add it and and save the value
            if (userDefinedZoomEnabled) { // replace the user value
                removeItemAt(0);
                insertItemAt(sPercent, 0);
            } else { // add the user value
                insertItemAt(sPercent, 0);
                insertItemAt(new SeparatorIcon(), 1);
                setMaximumRowCount(getMaximumRowCount() + 2);
                userDefinedZoomEnabled = true;
            }
            PropertiesSet pref = ApplicationContext.getDefaultMainFrame()
                    .getApplicationPreferences();
            pref.setProperty(ZoomCombo.class, USER_DEFINED_ZOOM_VALUE, sPercent);
        }

        if (!sPercent.equals((String) getSelectedItem())) { // avoid an infinite recursion
            setSelectedItem(sPercent);
        }

    }

    public final void update() {
        externalUpdate = true;
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        DiagramView diagView = (focusObject instanceof ApplicationDiagram ? ((ApplicationDiagram) focusObject)
                .getMainView()
                : null);
        setDiagramView(diagView);
        externalUpdate = false;
    }

    // /////////////////////////////////////
    // CurrentFocusListener support
    //
    public final void currentFocusChanged(Object oldFocusObject, final Object focusObject)
            throws DbException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                DiagramView diagView = (focusObject instanceof ApplicationDiagram ? ((ApplicationDiagram) focusObject)
                        .getMainView()
                        : null);
                setDiagramView(diagView);
            }
        });
    }
    //
    // End of CurrentFocusListener support
    // /////////////////////////////////////
}
