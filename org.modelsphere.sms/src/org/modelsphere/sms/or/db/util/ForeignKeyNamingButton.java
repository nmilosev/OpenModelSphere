/*************************************************************************

Copyright (C) 2011 Grandite

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

package org.modelsphere.sms.or.db.util;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.db.DbSMSProject;
import org.modelsphere.sms.or.db.DbORDataModel;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORStyle;
import org.modelsphere.sms.or.international.LocaleMgr;

@SuppressWarnings("serial")
public class ForeignKeyNamingButton extends JButton implements ActionListener {
	private GenForeignKeyDialog _owner;
	private Color _bgColor;
	
	public ForeignKeyNamingButton(GenForeignKeyDialog owner, DbORDataModel dataModel) {
		super(LocaleMgr.screen.getString("NamingOptions") + "..");
		setVisible(true); //false until functional
		_owner = owner;
		
		_bgColor = getDefaultTableBgColor(dataModel);
		addActionListener(this);
	}

	private Color getDefaultTableBgColor(DbORDataModel dataModel) {
		Color bgColor = null;
		
		try {
			dataModel.getDb().beginReadTrans();
			DbSMSProject project = (DbSMSProject)dataModel.getCompositeOfType(DbSMSProject.metaClass);
			DbORStyle style = project.getOrDefaultStyle();
			
			bgColor = style.getBackgroundColorDbORTable();
			dataModel.getDb().commitTrans();
		} catch (DbException ex) {
			bgColor = new Color(255, 255, 255);
		}
		
		return bgColor;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		//Create and set up the window.
        JDialog dialog = new JDialog(_owner, LocaleMgr.screen.getString("NamingOptions"), true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        String foreignKeyName = DbORForeign.metaClass.getGUIName();
        
        PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
      	String fcPattern = applOptions.getPropertyString(ForeignKeyNamingPanel.class, 
      			ForeignKeyNamingPanel.FOREIGN_COLUMN_NAME_PATTERN, 
      			ForeignKeyNamingPanel.TABLE_COLUMN); 
      	
      	String fkPattern = applOptions.getPropertyString(ForeignKeyNamingPanel.class, 
      			ForeignKeyNamingPanel.FOREIGN_KEY_NAME_PATTERN, 
      			foreignKeyName); 

        //Create and set up the content pane.
        ForeignKeyNamingPanel newContentPane = new ForeignKeyNamingPanel(dialog);
        newContentPane.createContent(_bgColor, foreignKeyName, fcPattern, fkPattern);

        //Display the window.
        dialog.pack();
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
	}
}
