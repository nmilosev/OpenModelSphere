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

package org.modelsphere.sms.templates;

import java.awt.Frame;
import java.io.File;

import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.gui.task.DefaultController;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.ForwardOptions;
import org.modelsphere.jack.srtool.forward.ForwardToolkitInterface;
import org.modelsphere.jack.srtool.forward.ForwardWorker;
import org.modelsphere.sms.international.LocaleMgr;

public abstract class BasicForwardToolkit implements ForwardToolkitInterface {
    protected static final String NO_RULE_FOR_NAME = LocaleMgr.misc.getString("NO_RULE_FOR_NAME");
    protected static final String GENERATING = LocaleMgr.misc.getString("GENERATING");
    protected static final String CANNOT_GENERATE = LocaleMgr.misc.getString("CANNOT_GENERATE");
    protected static final String SEPARATOR = System.getProperty("file.separator"); //NOT LOCALIZABLE
    private static final String ForwardEngineering = LocaleMgr.screen
            .getString("ForwardEngineering");

    public static final String DEFAULT_LOG_FILENAME = ApplicationContext.getLogPath()
            + System.getProperty("file.separator") + "forward.log"; //NOT LOCALIZABLE

    protected abstract File selectActualDirectory(String defaultDirectory);

    public abstract File getSelectedDirectory();

    protected abstract ForwardWorker createForwardWorker(ForwardOptions options);

    protected String getRootDirFromUserProp() {
        String defaultDirectory = ApplicationContext.getDefaultWorkingDirectory();
        return defaultDirectory;
    }

    public void generateFile(ForwardOptions options) {
        GenericForwardEngineeringPlugin forward = (GenericForwardEngineeringPlugin) options
                .getForward();
        String defaultDirectory = (forward == null) ? getRootDirFromUserProp() : forward
                .getRootDirFromUserProp();
        File actualDirectory = selectActualDirectory(defaultDirectory);

        if (actualDirectory != null) {
            options.setActualDirectory(actualDirectory);
            Controller controller = new DefaultController(getTitle(), false, DEFAULT_LOG_FILENAME);
            ForwardWorker worker = createForwardWorker(options);
            controller.start(worker);
        } //end if
    }

    public String getTitle() {
        return ForwardEngineering;
    }

    public static Frame getMainFrame() {

        Frame frame = null;
        try {
            Class claz = Class.forName("org.modelsphere.sms.MainFrame"); //NOT LOCALIZABLE, unit test
            java.lang.reflect.Method method = claz
                    .getDeclaredMethod("getSingleton", new Class[] {}); //NOT LOCALIZABLE, unit test
            frame = (Frame) method.invoke(null, new Object[] {});
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
        return frame;
    }

    public abstract Class getForwardClass();

}
