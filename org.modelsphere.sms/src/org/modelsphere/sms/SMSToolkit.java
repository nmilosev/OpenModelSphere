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

package org.modelsphere.sms;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbProject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.graphic.tool.LineTool;
import org.modelsphere.jack.graphic.tool.Tool;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.graphic.ApplicationDiagram;
import org.modelsphere.jack.srtool.graphic.GraphicComponentFactory;
import org.modelsphere.jack.srtool.screen.ScreenPerspective;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.graphic.tool.SMSFreeFormTool;
import org.modelsphere.sms.graphic.tool.SMSFreeLineTool;
import org.modelsphere.sms.graphic.tool.SMSSelectNeighborhoodTool;
import org.modelsphere.sms.graphic.tool.SMSSelectTool;
import org.modelsphere.sms.graphic.tool.SMSStampTool;
import org.modelsphere.sms.graphic.tool.SMSTextTool;
import org.modelsphere.sms.international.LocaleMgr;

/**
 * This class provide a way for actions and other fontionnalities to make abstraction of the active
 * diagram or dbobject. For example, instead of creating an action for adding a table for informix,
 * one for oracle table, ... we can make one single action and make a call to the active toolkit to
 * create the table for the current dbms. In this example, the statement in the add table action
 * would be: SMSToolkit.getToolkit().createDbObject(DbORTable.getMetaClass(), composite, null);
 * 
 * This class is also responsible for creating other king of objects related to the diagram (tools,
 * graphic component, ...)
 * 
 * There is two way to get a specific toolkit: 1) SMSToolkit.getToolkit() This method will search
 * for the toolkit managed by the active diagram (if one). This method is equivalent to
 * getToolkit(APPLICATION_DIAGRAM_MASK) except that it doesn't throws DbException. 2)
 * SMSToolkit.getToolkit(DbObject dbo) This method will search for the toolkit that support the
 * specified dbo (ignoring the context) 3) SMSToolkit.getToolkit(int scope) This method will search
 * in the specified scope for the active toolkit with consideration of the context. This method is
 * the most complete of the 3 getToolkit. If the context is the explorer and EXPLORER_MASK is in the
 * scope, this method will search the for a toolkit valid for all selected objects. If no toolkit
 * found or differents toolkit found, the default toolkit will be return (see note below). Possible
 * values for scope are combination of APPLICATION_DIAGRAM_MASK and EXPLORER_MASK.
 * 
 * 
 * Note: In both cases, you can't obtain a null object. You will receive the DefaultSMSToolkit
 * instead. This means that getting the toolkit doesn't need a (toolkit != null) verification.
 * 
 * See method documentation for more details.
 */

public abstract class SMSToolkit {
    public static final String kSelectTool = LocaleMgr.screen.getString("SelectTool");
    public static final Image kImageSelectTool = GraphicUtil.loadImage(Tool.class,
            "resources/selectiontool2.gif"); // NOT LOCALIZABLE - tool image

    private static final String kStampTool = LocaleMgr.screen.getString("StampTool");
    private static final Image kImageStampTool = GraphicUtil.loadImage(SMSModule.class,
            "international/resources/stamp.gif"); // NOT
    // LOCALIZABLE
    // - tool
    // image

    private static Hashtable toolkits = new Hashtable(20);

    // The default toolkit is considered as a Null Object.
    // Getting the toolkit doesn't need a (toolkit != null) verification.
    private static SMSToolkit defaultToolkit = new DefaultSMSToolkit();

    private static SMSToolkit lastKit = null;
    private static ApplicationDiagram lastDiagram = null;

    /**
     * Available mask for method getToolkit(int scope)
     */
    public static final int APPLICATION_DIAGRAM_MASK = 0x0001; // Allow search
    // in active
    // diagram
    public static final int EXPLORER_MASK = 0x0002; // Allow search in active
    // explorer

    {
        if (!(this instanceof DefaultSMSToolkit)) {
            MetaClass[] supportedPackages = getSupportedPackage();
            if (supportedPackages != null) {
                // register package mapping
                for (int i = 0; i < supportedPackages.length; i++) {
                    if (toolkits.get(supportedPackages[i]) == null)
                        toolkits.put(supportedPackages[i], this);
                    else
                        org.modelsphere.jack.debug.Debug
                                .assert2(false,
                                        "Toolkit init error:  More than 1 toolkit support the same package.");
                }
            }
        }
    }

    /**
     * Return all intalled toolkits
     * 
     * Note: The 'null toolkit' instance of DefaultSMSToolkit will not be returned.
     */
    static final SMSToolkit[] getToolkits() {
        ArrayList list = new ArrayList(toolkits.values());
        ArrayList noduplicatelist = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            if (!noduplicatelist.contains(o) && (o != null))
                noduplicatelist.add(o);
        }
        noduplicatelist.trimToSize();
        SMSToolkit[] objs = new SMSToolkit[noduplicatelist.size()];
        for (int i = 0; i < noduplicatelist.size(); i++) {
            objs[i] = (SMSToolkit) noduplicatelist.get(i);
        }
        return objs;
    }

    /**
     * Get the toolkit for the current context If no specific toolkit for the context, return the
     * defaultToolkit The returned toolkit will search only in the focus object types represented by
     * the 'scope' parameter. Possible values for scope are: Any bits combinations of
     * APPLICATION_DIAGRAM_MASK and EXPLORER_MASK
     */
    public static final SMSToolkit getToolkit(int scope) throws DbException {
        if (scope != 0) {
            Object focusobject = ApplicationContext.getFocusManager().getFocusObject();
            if (focusobject == null)
                return defaultToolkit;
            if ((focusobject instanceof ExplorerView) && ((scope & EXPLORER_MASK) != 0)) {
                DbObject[] focusobjects = ApplicationContext.getFocusManager()
                        .getSelectedSemanticalObjects();
                if ((focusobjects == null) || (focusobjects.length == 0))
                    return defaultToolkit;
                SMSToolkit candidatekit = null;
                for (int i = 0; i < focusobjects.length; i++) {
                    SMSToolkit objectkit = getToolkit(focusobjects[i]);
                    if ((objectkit != candidatekit) && (candidatekit != null))
                        return defaultToolkit;
                    candidatekit = objectkit;
                }
                if (candidatekit != null)
                    return candidatekit;
                return defaultToolkit;
            }
            if ((focusobject instanceof ApplicationDiagram)
                    && ((scope & APPLICATION_DIAGRAM_MASK) != 0)) {
                DbObject diag = ((ApplicationDiagram) focusobject).getSemanticalObject();
                return (diag == null) ? defaultToolkit : (SMSToolkit) toolkits.get(diag
                        .getMetaClass());
            }
        }
        return defaultToolkit;
    }

    /**
     * See getToolkit(int scope) This method will look for active toolkit only in ApplicationDiagram
     * (no 'throws DbException')
     */
    public static final SMSToolkit getToolkit() {
        Object focusobject = ApplicationContext.getFocusManager().getFocusObject();
        if (focusobject == lastDiagram) // prevent reevaluation of the toolkit
            // if the diag is the same as the diag
            // of the last time this method has been
            // called
            return lastKit;
        if (focusobject == null) {
            lastKit = defaultToolkit;
            lastDiagram = null;
            return lastKit;
        }
        if (focusobject instanceof ApplicationDiagram) {
            DbObject diag = ((ApplicationDiagram) focusobject).getSemanticalObject();
            lastDiagram = (ApplicationDiagram) focusobject;
            lastKit = (diag == null) ? defaultToolkit : (SMSToolkit) toolkits.get(diag
                    .getMetaClass());
            return lastKit;
        }
        return defaultToolkit;
    }

    /**
     * Get the toolkit for the specified DbObject If no specific toolkit for the dbo, return the
     * defaultToolkit
     */
    public static final SMSToolkit getToolkit(DbObject dbo) throws DbException {
        if ((dbo == null) || (dbo instanceof DbProject))
            return defaultToolkit;
        else {
            SMSToolkit toolkit = null;
            DbObject compositePackage = null;
            if (dbo instanceof DbSMSPackage)
                compositePackage = dbo;
            else {
                dbo.getDb().beginTrans(Db.READ_TRANS);
                compositePackage = dbo.getCompositeOfType(DbSMSPackage.metaClass);
                dbo.getDb().commitTrans();
            }
            toolkit = (compositePackage == null) ? defaultToolkit : (SMSToolkit) toolkits
                    .get(compositePackage.getMetaClass());
            return (toolkit != null) ? toolkit : defaultToolkit;
        }
    }

    public static final SMSToolkit getToolkit(MetaClass mc) throws DbException {
        SMSToolkit toolkit = (mc == null) ? defaultToolkit : (SMSToolkit) toolkits.get(mc);
        return toolkit;
    } // end getToolkit()

    /**
     * Return the toolkit that is used when no other toolkit can be applied This toolkit is a Null
     * Object pattern.
     */
    public static final SMSToolkit getDefaultToolkit() {
        return defaultToolkit;
    }

    /**
     * Create a new GraphicComponentFactory
     */
    public abstract GraphicComponentFactory createGraphicalComponentFactory();

    /**
     * Return the MetaClass[] supported by this toolkit (the jclass of those MetaClass must be
     * instanceof DbSMSPackage) Example: return new MetaClass[]{DbOROracleDataModel.getMetaClass()};
     * Note: If more than one supported package, they must use the same tools and graphical obj
     * factory
     */
    protected abstract MetaClass[] getSupportedPackage();

    /**
     * MUST be called within a write transaction. This method provide a way to make global actions
     * or features without considering the target system. Use this method to create a new object of
     * type abstractMetaClass. Parameter 'parameters' can be used to pass other parameter that may
     * be needed to create the new object.
     */
    public abstract DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException;

    /**
     * MUST be called within a write transaction. Use this method to create a copy object of the
     * baseobject parameter but specific to the toolkit. (exemple: if baseobject is instance of
     * DbOracleTable, the informix toolkit could create an equivalent object of class
     * DbInformixTable). The rules for converting and the deepnest of the copy is toolkit specific.
     * Parameter 'parameters' can be used to pass other parameter that may be needed to create the
     * new object.
     */
    public abstract DbObject createDbObject(DbObject baseobject, DbObject composite,
            Object[] parameters) throws DbException;

    /**
     * Return true if an instance of the specified metaClass can be created or duplicated (bridge)
     */
    public abstract boolean isMetaClassSupported(MetaClass metaClass);

    /**
     * Create and return the tools supported by this module An SMSToolButtonGroup will be created to
     * support the supported packages Global tools will be added if the return Tool[] value is not
     * null. Otherwise, the global tools won't be added.
     */
    protected abstract Tool[] createTools();

    // Return true if the specified submetaclass is a meta sub class of the
    // specified supermetaclass or if
    // both meta class are the same.
    protected final boolean isAssignableFrom(MetaClass supermetaclass, MetaClass submetaclass) {
        if ((supermetaclass == null) || (submetaclass == null))
            return false;
        return supermetaclass.isAssignableFrom(submetaclass);
    }

    public static Tool[] getTools() {
        List<Tool> allTools = new ArrayList<Tool>();

        // global tools - Added to all-tools group
        // select tool must be the first one
        Tool selectTool = new SMSSelectTool(kSelectTool, kImageSelectTool);
        allTools.add(selectTool);

        Tool selectNeighborhoodTool = new SMSSelectNeighborhoodTool();
        allTools.add(selectNeighborhoodTool);

        // add specific toolkit tools
        SMSToolkit[] kits = getToolkits();
        for (int i = 0; i < kits.length; i++) {
            SMSToolkit toolkit = kits[i];
            Tool[] kittools = toolkit.createTools();
            MetaClass[] mcs = toolkit.getSupportedPackage();

            if (kittools == null)
                continue;
            for (int j = 0; j < kittools.length; j++) {
                kittools[j].setSupportedDiagramMetaClasses(mcs);
                allTools.add(kittools[j]);
            } //end for
        } //end for

        // global tools - Added to all-tools group
        Tool temptool = new SMSStampTool(kStampTool, kImageStampTool);
        temptool.setToolBar(MainFrame.DRAWING_TOOLBAR);
        allTools.add(temptool);
        
        if (ScreenPerspective.isFullVersion()) {
        	String textTool = LocaleMgr.screen.getString("TextTool"); 
        	Image textToolImage = GraphicUtil.loadImage(SMSModule.class,
            	"international/resources/note.gif");
        	temptool = new SMSTextTool(textTool, textToolImage);
        	temptool.setToolBar(MainFrame.DRAWING_TOOLBAR);
            allTools.add(temptool);
        }
        
        temptool = new SMSFreeLineTool(SMSFreeLineTool.kFreeLineTool, new String[] {
                LineTool.kRightAngleTooltips, LineTool.kAngularLineTooltips },
                SMSFreeLineTool.kImageFreeRightAngleLineTool, new Image[] {
                        SMSFreeLineTool.kImageFreeRightAngleLineTool,
                        SMSFreeLineTool.kImageFreeLineTool });
        temptool.setToolBar(MainFrame.DRAWING_TOOLBAR);
        allTools.add(temptool);
        temptool = new SMSFreeFormTool();
        temptool.setToolBar(MainFrame.DRAWING_TOOLBAR);
        allTools.add(temptool);

        Tool[] arrayTools = new Tool[allTools.size()];
        for (int i = 0; i < arrayTools.length; i++) {
            arrayTools[i] = (Tool) allTools.get(i);
        }

        return arrayTools;
    } //end getTools()
} // end SMSToolkit

/**
 * A Default Class for Toolkit. The instance of this class represent a null toolkit that will be
 * returned from a call to getToolkit when no toolkit is applicable.
 */
class DefaultSMSToolkit extends SMSToolkit {
    public GraphicComponentFactory createGraphicalComponentFactory() {
        return null;
    }

    protected MetaClass[] getSupportedPackage() {
        return new MetaClass[] {};
    }

    public DbObject createDbObject(MetaClass abstractMetaClass, DbObject composite,
            Object[] parameters) throws DbException {
        return null;
    }

    public DbObject createDbObject(DbObject baseobject, DbObject composite, Object[] parameters)
            throws DbException {
        return null;
    }

    public boolean isMetaClassSupported(MetaClass metaClass) {
        return false;
    }

    public Tool[] createTools() {
        return null;
    };
}
