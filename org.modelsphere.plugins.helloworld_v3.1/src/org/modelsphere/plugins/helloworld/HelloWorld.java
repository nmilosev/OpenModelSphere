/*************************************************************************

Copyright (C) 2009 by neosapiens inc.

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

You can reach neosapiens inc. at: 

neosapiens inc.
1236 Gaudias-Petitclerc
Qu&eacute;bec, Qc, G1Y 3G2
CANADA
Telephone: 418-561-8403
Fax: 418-650-2375
http://www.neosapiens.com/
Email: marco.savard@neosapiens.com
       gino.pelletier@neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.helloworld;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.plugins.Plugin2;
import org.modelsphere.jack.plugins.PluginAction;
import org.modelsphere.jack.plugins.PluginSignature;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.MainFrameMenu;

public class HelloWorld implements Plugin2 {

    @Override
    public OptionGroup getOptionGroup() {
        return null;
    }

    @Override
    public PluginAction getPluginAction() {
        return null;
    }

    @Override
    public void execute(ActionEvent ev) throws Exception {
        JOptionPane.showMessageDialog(ApplicationContext.getDefaultMainFrame(), "Hello World!");
    }

    @Override
    public PluginSignature getSignature() {
        return null;
    }

    @Override
    public Class<? extends Object>[] getSupportedClasses() {
        return new Class[] { DbProject.class };
    }

    @Override
    public String installAction(DefaultMainFrame frame, MainFrameMenu menuManager) {
        return null;
    }

    @Override
    public boolean doListenSelection() {
        return false;
    }
}