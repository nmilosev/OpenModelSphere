/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.or.actions;

import java.io.File;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.sms.SMSFilter;
import org.modelsphere.sms.actions.GenerateFromTemplatesAction;
import org.modelsphere.sms.or.generic.db.DbGEDataModel;
import org.modelsphere.sms.preference.DirectoryOptionGroup;

public class GenerateHTMLAction extends GenerateFromTemplatesAction /*
                                                                     * extends
                                                                     * AbstractApplicationAction
                                                                     * implements
                                                                     * SelectionActionListener
                                                                     */{

    private static final String sep = new String(System.getProperties().getProperty(
            "file.separator"));
    private static final String installDir = ApplicationContext.getDefaultMainFrame()
            .getApplicationDirectory();
    private static final String templateFile = new String("GEhtml.tpl");

    public GenerateHTMLAction() {
        super("Generate HTML");
        // setEnabled(true);
        g_outputDir = DirectoryOptionGroup.getHTMLGenerationDirectory();
    }

    protected final File selectTemplateFile() {
        String templateDir = new String(installDir + sep + "plugins" + sep + "html");
        return new File(templateDir, templateFile);
    }

    public final void updateSelectionAction() {// throws DbException
        DbObject[] semObjs = ApplicationContext.getFocusManager().getSelectedSemanticalObjects();
        boolean enable = (semObjs.length > 0);
        for (int i = 0; i < semObjs.length; i++) {
            DbObject semObj = semObjs[i];
            if (!(semObj instanceof DbGEDataModel)) {
                enable = false;
                break;
            } // end if
        } // end for

        setEnabled(enable);
    }

    protected int getFeatureSet() {
        return SMSFilter.RELATIONAL;
    }
    /*
     * //+ protected static String TPL_EXTENSION = "tpl"; //NOT LOCALIZABLE protected String
     * templateDir = SMSPreferences.getSingleton().getPreferenceAsString
     * (SMSPreferences.DEFAULT_WORKING_DIRECTORY); protected String outputDir =
     * SMSPreferences.getSingleton().getPreferenceAsString(SMSPreferences.
     * DEFAULT_WORKING_DIRECTORY);
     * 
     * private String sep = new String(System.getProperties().getProperty("file.separator"));
     * private String installDir =
     * ApplicationContext.getDefaultMainFrame().getApplicationDirectory();
     * 
     * // for Generic Data Model only private String templateFile = new
     * String("GEhtml."+TPL_EXTENSION); //private String templateDir = SMSPreferences
     * .getSingleton().getPreferenceAsString(org.modelsphere.sms.preference
     * .SMSPreferences.HTML_GENERATION_DIRECTORY);
     * 
     * private TemplateGenericForward m_fwd = new TemplateGenericForward();
     * 
     * public GenerateHTMLAction() { super("Generate HTML"); //setEnabled(true); templateDir = new
     * String(installDir+sep+"plugins"+sep+"html"); outputDir
     * =SMSPreferences.getSingleton().getPreferenceAsString(SMSPreferences.
     * HTML_GENERATION_DIRECTORY); }
     * 
     * protected final File selectTemplateFile() { return new File(templateDir, templateFile); }
     * 
     * //ApplicationContext.getFocusManager() protected final void doActionPerformed(ActionEvent e){
     * doActionPerformed(); }
     * 
     * protected final void doActionPerformed(){ DbObject[] objects =
     * ApplicationContext.getFocusManager().getSelectedSemanticalObjects(); for(int i=0 ;
     * i<objects.length ; i++) System.out.println(objects[i]); }
     * 
     * 
     * private ArrayList displayExternalRules(Rule[] rules) { ArrayList externalRules = new
     * ArrayList(); ArrayList conditions = new ArrayList();
     * 
     * //get EXTERNal rules for (int i=0; i<rules.length; i++) { Rule rule = rules[i]; if
     * (rule.externModifier != null) { externalRules.add(rule); } } //end for
     * 
     * //get conditions HashMap map = ConcreteIfModifier.getHashMap(); Set set = map.keySet();
     * Iterator iter = set.iterator(); while (iter.hasNext()) { String key = (String)iter.next();
     * IfModifier mod = (IfModifier)map.get(key); mod.modifierName = key; conditions.add(mod); }
     * //end while
     * 
     * TemplateDialog diag = new TemplateDialog(MainFrame.getSingleton(), externalRules,
     * conditions); diag.setSize(TEMPLATE_DIAG_WIDTH, TEMPLATE_DIAG_HEIGHT); diag.center(); //center
     * the dialog ONCE its size is set diag.setModal(true); diag.setVisible(true); //the user enters
     * the values HERE
     * 
     * ArrayList selectedRules = diag.getSelectedRules(); ArrayList setConditions =
     * diag.getSetConditions(); //get IF and IFNOT conditions
     * 
     * return selectedRules; }
     * 
     * 
     * 
     * 
     * 
     * public final void updateSelectionAction() {//throws DbException DbObject[] semObjs =
     * ApplicationContext.getFocusManager().getSelectedSemanticalObjects(); boolean enable =
     * (semObjs.length > 0); for (int i=0 ; i < semObjs.length ; i++) { DbObject semObj =
     * semObjs[i]; if (!(semObj instanceof DbGEDataModel)) { enable = false; break; } //end if }
     * //end for
     * 
     * setEnabled(enable); }
     */
}
