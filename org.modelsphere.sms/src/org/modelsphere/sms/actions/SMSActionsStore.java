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

// See comments in class AbstractActionsStore
package org.modelsphere.sms.actions;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.srtool.actions.ExportToXMLAction;
import org.modelsphere.jack.srtool.actions.ImportFromXMLAction;

public final class SMSActionsStore extends AbstractActionsStore {

    // //////////////////////////////////////////////////////////////////////////
    // Keys

    // Application Keys
    public static final String APPLICATION_ABOUT = "ApplicationShowAbout"; // NOT LOCALIZABLE, internal code
    public static final String APPLICATION_HELP = "ApplicationOpenHelp"; // NOT LOCALIZABLE, internal code
    public static final String APPLICATION_LICENSE = "ApplicationShowLicense"; // NOT LOCALIZABLE, internal code
    public static final String APPLICATION_PREFERENCES = "Application Preferences"; // NOT LOCALIZABLE, internal code
    public static final String COLOR_BORDER = "Graphical Object Border Color"; // NOT LOCALIZABLE, internal code
    public static final String COLOR_FILL = "Graphical Object Fill Color"; // NOT LOCALIZABLE, internal code
    public static final String COLOR_TEXT = "Graphical Object Text Color"; // NOT LOCALIZABLE, internal code
    public static final String CREATE_LINK = "CreateLink"; // NOT LOCALIZABLE, internal code
    public static final String CREATE_MISSING_GRAPHICS = "CreateMissingGraphics"; // NOT LOCALIZABLE, internal code
    public static final String CONSOLIDATE_DIAGRAMS = "ConsolidateDiagram"; // NOT LOCALIZABLE, internal code
    public static final String DASHLINE = "Graphical Object DashLine"; // NOT LOCALIZABLE, internal code
    public static final String EXPORT_TO_XML = "Export to XML"; // NOT LOCALIZABLE, internal code
    public static final String EDIT_NOTATIONS = "Edit Notations"; // NOT LOCALIZABLE, property key
    public static final String FORMAT = "Edit Format"; // NOT LOCALIZABLE, internal code
    public static final String GENERATE_FROM_TEMPLATES = "Generate from Templates"; // NOT LOCALIZABLE, internal code
    public static final String HIGHLIGHT = "Graphical Object Highlight"; // NOT LOCALIZABLE, internal code
    public static final String IMPORT_FROM_XML = "Import from XML"; // NOT LOCALIZABLE, internal code
    public static final String INTEGRATE = "Integrate"; // NOT LOCALIZABLE, internal code
    public static final String LAYOUT_SELECTION = "LayoutSelection"; // NOT LOCALIZABLE, internal code
    public static final String LAYOUT_DIAGRAM = "LayoutDiagram"; // NOT LOCALIZABLE, internal code
    public static final String MOVE = "Move"; // NOT LOCALIZABLE
    public static final String PLUGIN_MGR = "Plugin Manager"; // NOT LOCALIZABLE
    public static final String PROJECT_STYLE = "Edit Project Style"; // NOT LOCALIZABLE, internal code
    public static final String REMOVE_GRAPHIC = "Remove Graphic"; // NOT LOCALIZABLE, internal code
    public static final String REPOSITION_LABELS = "Reposition Labels"; // NOT LOCALIZABLE
    public static final String SAFE_MODE = "Safe Mode"; // NOT LOCALIZABLE
    public static final String SELECT_CLASSES = "Select Classes"; // NOT LOCALIZABLE
    public static final String SET_DIAGRAM_DEFAULT_STYLE = "Set Diagram Default Style"; // NOT LOCALIZABLE, internal code
    public static final String SET_GO_STYLE = "Set Go Style"; // NOT LOCALIZABLE, internal code
    public static final String SET_NOTATION = "Set Notation"; // NOT LOCALIZABLE, property key
    public static final String SEND_TO_DIAGRAM = "Send To Diagram"; // NOT LOCALIZABLE, internal code
    public static final String SAVE_DIAGRAM = "SaveDiagram"; // NOT LOCALIZABLE, property key
    public static final String STARTING_WIZARD = "Starting Wizard"; // NOT LOCALIZABLE
    public static final String APPLICATION_CONFIGURE_GRID = "Configure Grid"; // NOT LOCALIZABLE, internal code

    // SMS Object Keys
    public static final String ADD = "Add"; // NOT LOCALIZABLE, property key
    public static final String SMS_SET_PROJECT_DEFAULT_BE_NOTATION = "sms Set Project Default BE Notation"; // NOT LOCALIZABLE, internal code
    public static final String SMS_SET_PROJECT_DEFAULT_OR_NOTATION = "sms Set Project Default Or Notation"; // NOT LOCALIZABLE, internal code
    public static final String SMS_SET_PROJECT_DEFAULT_ER_NOTATION = "sms Set Project Default Er Notation"; // NOT LOCALIZABLE, internal code
    // End Keys
    // /////////////////////////////////////////////////////////////////////////

    private static SMSActionsStore singleton;

    private SMSActionsStore() {
        super();
        put(APPLICATION_ABOUT, new ShowAboutAction());
        put(APPLICATION_HELP, new OpenHelpAction());
        put(APPLICATION_LICENSE, new ShowLicenseAction());
        put(APPLICATION_PREFERENCES, new ApplicationPreferencesAction());
        put(ADD, new AddAction());
        put(COLOR_BORDER, new BorderColorAction());
        put(COLOR_FILL, new FillColorAction());
        put(COLOR_TEXT, new TextColorAction());
        put(CREATE_LINK, new CreateLinkAction());
        put(CREATE_MISSING_GRAPHICS, new CreateMissingGraphicsAction());
        put(CONSOLIDATE_DIAGRAMS, new ConsolidateDiagramsAction());
        put(DASHLINE, new DashLineAction());
        put(EXPORT_TO_XML, new ExportToXMLAction());
        put(FORMAT, new FormatAction());
        put(GENERATE_FROM_TEMPLATES, new GenerateFromTemplatesAction());
        put(HIGHLIGHT, new HighlightAction());
        put(IMPORT_FROM_XML, new ImportFromXMLAction());
        put(INTEGRATE, new IntegrateAction());
        put(LAYOUT_DIAGRAM, new LayoutDiagramAction());
        put(LAYOUT_SELECTION, new LayoutSelectionAction());
        put(MOVE, new MoveAction());
        put(EDIT_NOTATIONS, new EditNotationAction());
        put(PLUGIN_MGR, new PluginMgrAction());
        put(PROJECT_STYLE, new StyleAction());
        put(REMOVE_GRAPHIC, new RemoveGraphicAction());
        put(REPOSITION_LABELS, new RepositionLabelsAction());
        put(SELECT_CLASSES, new SelectAllClassesAction());
        put(SELECT_LINES, new SelectAllLinesAction());
        put(SET_DIAGRAM_DEFAULT_STYLE, new SetDiagramDefaultStyleAction());
        put(SET_GO_STYLE, new SetGoStyleAction());
        put(SET_NOTATION, new SetNotationAction());
        put(SMS_SET_PROJECT_DEFAULT_BE_NOTATION, new SetProjectDefaultBENotationAction());
        put(SMS_SET_PROJECT_DEFAULT_OR_NOTATION, new SetProjectDefaultORNotationAction());
        put(SMS_SET_PROJECT_DEFAULT_ER_NOTATION, new SetProjectDefaultERNotationAction());
        put(SEND_TO_DIAGRAM, new SendToDiagramAction());
        put(SAVE_DIAGRAM, new SaveDiagramAction());
        put(STARTING_WIZARD, new StartingWizardAction());
        put(APPLICATION_CONFIGURE_GRID, new ConfigureGridAction());

    }

    public static final SMSActionsStore getSingleton() {
        if (singleton == null) {
            singleton = new SMSActionsStore();
        }
        return singleton;
    }

}
