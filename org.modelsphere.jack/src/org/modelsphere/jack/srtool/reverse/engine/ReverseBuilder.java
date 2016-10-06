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

package org.modelsphere.jack.srtool.reverse.engine;

import java.util.*;
import java.lang.reflect.*;

import org.modelsphere.jack.actions.AbstractActionsStore;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.actions.ShowDiagramAction;
import org.modelsphere.jack.srtool.international.LocaleMgr;
import org.modelsphere.jack.srtool.reverse.Actions;
import org.modelsphere.jack.srtool.reverse.SRXConstants;
import org.modelsphere.jack.text.MessageFormat;

public abstract class ReverseBuilder implements Actions, SRXConstants {
    public static final String kReverse = LocaleMgr.screen.getString("ReverseEngineering");
    private static final String kSummary_ = LocaleMgr.misc.getString("Summary_");
    private static final String kRepportSummaryCount01 = LocaleMgr.misc.getString("repportCount01");

    protected static final String EOL = System.getProperty("line.separator");

    private static final int NEW_MAP_OBJECT_SIZE = 8;

    // This object represent an invalid object. Returning this object from a
    // hook will cancel the reverse.
    public static final Object INVALID_OBJECT = new Object();

    // Options possible values:

    // Apply to composition only.
    // Make sure the srx tag corresponding to the sequence id equals
    // <multiPartTag>
    protected static final int MULTI_PART_OBJECT = 0x01;

    // Can be used for fields - Will append the value to the dbo actual value
    // (APPEND_NEW_VALUE_TO_OLD_VALUE)
    // or the dbo actual value to the current
    // value(APPEND_OLD_VALUE_TO_NEW_VALUE).
    // Can be used with sequenced object text fields or modif on existing
    // dbobject.
    protected static final int APPEND_NEW_VALUE_TO_OLD_VALUE = 0x02;
    protected static final int APPEND_OLD_VALUE_TO_NEW_VALUE = 0x04;

    // Apply for MULTI_PART_OBJECT, this will append an End of Line at the end
    // of each part
    protected static final int APPEND_EOL_TO_PART = 0x08;

    // This will id concept not specified in class Mapping
    // TODO: Should be useless (all concepts are now user defined)
    protected static final int USER_DEFINED_CONCEPT_ID = -2;

    protected static final String PATTERN_DBO_CREATED = LocaleMgr.message.getString("DboCreated01");
    protected static final String PATTERN_DBO_MODIFIED = LocaleMgr.message
            .getString("DboModified01");
    protected static final String PATTERN_DBO_REVERSED = LocaleMgr.message
            .getString("DboReversed01");

    // PREDEFINED KEYS FOR currentObject
    // The created db object
    protected static final String CURRENT_OBJECT = "___ db object ___"; // NOT
    // LOCALIZABLE
    // -
    // property
    // The concept id (from class srtool.reverse.Mapping)
    // Concept not defined in class Mapping will get an id of
    // 'USER_DEFINED_CONCEPT_ID'
    protected static final String CURRENT_OBJECT_CONCEPT_ID = "___ concept id ___"; // NOT
    // LOCALIZABLE
    // -
    // property
    // Name of the concept received from sql extraction result (sr_occurrence
    // tag)
    protected static final String CURRENT_OBJECT_CONCEPT_NAME = "___ concept name ___"; // NOT
    // LOCALIZABLE
    // -
    // property
    // The composite of the created db object
    protected static final String CURRENT_OBJECT_COMPOSITE = "___ db object composite ___";// NOT
    // LOCALIZABLE
    // -
    // property

    // Defined the key (srx tag) to search for identifying MULTI_PART_OBJECT
    protected String multiPartTag = "seq"; // NOT LOCALIZABLE - sequence tag
    // from sql result

    protected Object configuration;
    protected DbObject root;
    protected DbProject project;

    protected static final String ADJUST_VALUE_FOR_FIX_TAG_LENGTH = "fixTagLength"; // Integer
    // (fix
    // size)
    // //
    // NOT
    // LOCALIZABLE
    // Use db.commitAndChain() instead of db.commit() -- Default is db.commit()
    // - FALSE
    protected static final String CHAIN_COMMIT = "chainCommit"; // Boolean //
    // NOT
    // LOCALIZABLE
    private HashMap globalOptions = new HashMap(2);

    // This field contains all the properties of the created object. There is
    // one entry for each sql extraction result tag.
    // Also contains predefined tag (see CURRENT_OBJECT,
    // CURRENT_OBJECT_CONCEPT_ID, CURRENT_OBJECT_CONCEPT_NAME,
    // and CURRENT_OBJECT_COMPOSITE).
    protected HashMap currentObject;

    // Stock all objects for the current concept
    private ArrayList tempObjects = new ArrayList(100);

    // map an HashSet object for each supported concept - this HashSet object
    // contains all the field level mappings
    private HashMap conceptMapping = new HashMap(15);

    // map a concept with its composition definition (dbobject,
    // compositeMapping, SrxDbObjectElement or hook)
    // Used to get the dbobject
    private HashMap compositionMapping = new HashMap(15);

    // map concept-hook (to be executed after all occurrences of this concept
    // have been created)
    private HashMap postProcessConceptMapping = new HashMap(2);

    // map concept-hook (to be executed after each occurrence creation for this
    // concept)
    private HashMap postProcessOccurrenceMapping = new HashMap(2);

    // contains the concept to skip
    private ArrayList ignoredConcept = new ArrayList(5);

    // contains the concept that used the MULTI_PART_OBJECT options.
    private ArrayList multiPartConcept = new ArrayList(5);

    // Will be set to true if a called to removeCurrentObject() occurs
    private boolean removeObject = false;

    // Used to track concept change (concept hook support)
    private String lastConcept = null;

    // The task controler - this one manage the output to user
    private Controller controller = null;

    // Reports field - Concepts contains in this array will generate an output
    // feedback to the user
    private ArrayList conceptInOutputRepport = new ArrayList(10);
    private HashMap conceptGroupInOutputRepport = new HashMap(5);
    private String repportSummary = new String();
    private HashMap occurrencesCounters = new HashMap(10);
    private HashMap guiNamesSingular = new HashMap(10);
    private HashMap guiNamesPlural = new HashMap(10);
    private ArrayList repportSummarySequence = new ArrayList(10);

    public final void init(DbProject project, Object configuration) throws Exception {
        Debug.assert2(project != null, "ReverseBuilder:  Null project");
        Debug.assert2(configuration != null, "ReverseBuilder:  Null configuration");

        globalOptions = new HashMap(2);
        this.configuration = configuration;
        root = getRoot();
        this.project = project;
        Db db = project.getDb();
        db.beginTrans(Db.WRITE_TRANS, kReverse);

        repportSummary = createRepportSummaryHeader();
        initSpecific();

        if (this.root == null) {
            throw new RuntimeException("Null Root. "); // NOT LOCALIZABLE
        }
    }

    protected String createRepportSummaryHeader() {
        return new String(EOL + EOL + kSummary_);
    }

    protected String createRepportSummaryFooter() {
        return null;
    }

    abstract public void buildORDiagram();

    protected abstract void initSpecific() throws Exception;

    // Possible options:
    // ADJUST_VALUE_FOR_FIX_TAG_LENGTH - Value must be an Integer object (will
    // remove extra spaces in values of type text)
    // CHAIN_COMMIT
    // You may add any custom options
    protected final void addGlobalOption(String option, Object value) {
        globalOptions.put(option, value);
    }

    protected final Object getGlobalOption(String option) {
        return globalOptions.get(option);
    }

    // Return the root object for creating components
    protected abstract DbObject getRoot() throws Exception;

    // This method should not be call by subclasses. It is declared public for
    // implementation purpose
    public final void setController(Controller controller) {
        if (this.controller == null)
            this.controller = controller;
    }

    // Use this method to send feedback to user and to the log file
    public final void appendOutputText(String text) {
        if (controller != null)
            controller.println(text);
    }

    // Use this method to send feedback to user in the controler status field
    public final void setStatusText(String text) {
        if (controller != null)
            controller.setStatusText(text);
    }

    public final void incrementErrorsCounter() {
        if (controller != null)
            controller.incrementErrorsCounter();
    }

    public final void incrementWarningsCounter() {
        if (controller != null)
            controller.incrementWarningsCounter();
    }

    // This method will cancel the reverse
    /*
     * public final void cancel(){ //controler.cancel(); throw new RuntimeException(); }
     */

    protected final void add(String concept, DbElement dbElement, SrxElement srxElement,
            boolean active) throws DbException {
        add(concept, dbElement, srxElement, active, 0);
    }

    // map srx value with a Db MetaField.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String concept, DbElement dbElement, SrxElement srxElement,
            boolean active, int options) throws DbException {
        if (concept == null || dbElement == null || srxElement == null)
            throw new NullPointerException("Null parameters not allowed in add(). "); // NOT
        // LOCALIZABLE
        if (active) {
            HashSet conceptSet = (HashSet) conceptMapping.get(concept);
            if (conceptSet == null) {
                conceptSet = new HashSet(6);
                conceptMapping.put(concept, conceptSet);
            }
            conceptSet.add(new FieldMapping(dbElement, srxElement, options));
        }
    }

    // Define object <composite> as the composite for creating object of type
    // <componentMetaClass> for concept tag <concept>
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String concept, MetaClass componentMetaClass, SrxElement composite,
            boolean active) throws DbException {
        add(concept, componentMetaClass, composite, active, 0);
    }

    // Define object <dboElement> as the object to use. Will not create a
    // dbobject. Instead, it will resolve the SrxDbObjectElement
    // to get the dbobject to use.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String concept, SrxDbObjectElement dboElement, boolean active)
            throws DbException {
        add(concept, dboElement, active, 0);
    }

    // Define object <dboElement> as the object to use. Will not create a
    // dbobject. Instead, it will resolve the SrxDbObjectElement
    // to get the dbobject to use.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    // Options: MULTI_PART_OBJECT
    protected final void add(String concept, SrxDbObjectElement dboElement, boolean active,
            int options) throws DbException {
        if (concept == null || dboElement == null)
            throw new NullPointerException("Null parameters not allowed in add(). "); // NOT
        // LOCALIZABLE
        if (active) {
            compositionMapping.put(concept, dboElement);
            if ((options & MULTI_PART_OBJECT) != 0)
                multiPartConcept.add(concept);
        } else
            ignoredConcept.add(concept);
    }

    // Define object <composite> as the composite for creating object of type
    // <componentMetaClass> for concept tag <concept>
    // options: MULTI_PART_OBJECT or 0 for none.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String concept, MetaClass componentMetaClass, SrxElement composite,
            boolean active, int options) throws DbException {
        if (concept == null || componentMetaClass == null || composite == null)
            throw new NullPointerException("Null parameters not allowed in add(). "); // NOT
        // LOCALIZABLE
        if (active) {
            compositionMapping.put(concept, new CompositeMapping(componentMetaClass, composite));
            if ((options & MULTI_PART_OBJECT) != 0)
                multiPartConcept.add(concept);
        } else
            ignoredConcept.add(concept);
    }

    // This one will take the specified dbo as the only object for this
    // conceptTag and process the tag values on it.
    // (No DbObject will be created)
    // An example can be this.database or this.dataModel.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String conceptTag, DbObject dbo, boolean active) {
        if (conceptTag == null || (dbo == null && active))
            throw new NullPointerException("Null parameters not allowed in add(). "); // NOT
        // LOCALIZABLE
        if (active)
            compositionMapping.put(conceptTag, dbo);
        else
            ignoredConcept.add(conceptTag);
    }

    // This one will delegate the creation of the object to the specified method
    // represented by the hook parameter
    // Usually used when the composite is unknowned or
    // if the constructor uses more than one parameter (composite). If the tag
    // representing the composite
    // identifier is known and if the composite is the only parameter, hook
    // should not be needed.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void add(String conceptTag, String dboCreationHook, boolean active) {
        if (conceptTag == null || dboCreationHook == null)
            throw new NullPointerException("Null parameters not allowed in add(). "); // NOT
        // LOCALIZABLE
        if (active)
            compositionMapping.put(conceptTag, dboCreationHook);
        else
            ignoredConcept.add(conceptTag);
    }

    // This specified hook will be invoked after all objects of concept
    // <conceptTag> have been created.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void addPostProcessConceptHook(String conceptTag, String hook, boolean active) {
        if (conceptTag == null || hook == null)
            throw new NullPointerException(
                    "Null parameters not allowed in addPostProcessConceptHook(). "); // NOT
        // LOCALIZABLE
        if (active)
            postProcessConceptMapping.put(conceptTag, hook);
    }

    // This specified hook will be invoked after creation of each objects of
    // concept <conceptTag>.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void addPostProcessOccurrenceHook(String conceptTag, String hook, boolean active) {
        if (conceptTag == null || hook == null)
            throw new NullPointerException(
                    "Null parameters not allowed in addPostProcessOccurrenceHook(). "); // NOT
        // LOCALIZABLE
        if (active)
            postProcessOccurrenceMapping.put(conceptTag, hook);
    }

    // Register this concept as part of the repport
    // Order of appearance in the summary depends on the registering order.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void registerConceptForOutputReport(String conceptTag, boolean active) {
        if (conceptTag == null)
            throw new NullPointerException(
                    "Null parameters not allowed in registerConceptForOutputReport(). "); // NOT
        // LOCALIZABLE
        if (!active)
            return;
        if (conceptInOutputRepport.contains(conceptTag))
            return;
        conceptInOutputRepport.add(conceptTag);
        repportSummarySequence.add(conceptTag);
    }

    // Register these concepts as part of the repport.
    // If a group is specified, each conceptTag for the group will be considered
    // as the same concept.
    // Note: Group names can be any value but must be unique.
    // Order of appearance in the summary depends on the registering order.
    // <active> parameter will decide at runtime if the mapping should be
    // enabled or not.
    protected final void registerConceptForOutputReport(String[] conceptTags, String group,
            boolean active) {
        if (conceptTags == null || group == null)
            throw new NullPointerException(
                    "Null parameters not allowed in registerConceptForOutputReport(). "); // NOT
        // LOCALIZABLE
        if (!active)
            return;
        for (int i = 0; i < conceptTags.length; i++) {
            if (!conceptInOutputRepport.contains(conceptTags[i])) {
                conceptInOutputRepport.add(conceptTags[i]);
                conceptGroupInOutputRepport.put(conceptTags[i], group);
            }
        }
        if (!repportSummarySequence.contains(group))
            repportSummarySequence.add(group);
    }

    public final void processOccurrenceId(int occurrenceId, String conceptName) throws Exception {
        if (currentObject != null
                && !multiPartConcept.contains((String) currentObject
                        .get(CURRENT_OBJECT_CONCEPT_NAME))) {
            processCurrentObject();
        }
        processConceptChanged(conceptName);

        currentObject = null;
        if (occurrenceId != -1) { // Concept defined in class
            // 'org.modelsphere.jack.srtool.reverse.Mapping'
            currentObject = new HashMap(NEW_MAP_OBJECT_SIZE);
            currentObject.put(CURRENT_OBJECT_CONCEPT_ID, new Integer(occurrenceId));
            currentObject.put(CURRENT_OBJECT_CONCEPT_NAME, conceptName);
        } else if ((conceptName != null) && (compositionMapping.get(conceptName) != null)) { // user
            // defined
            // concept
            currentObject = new HashMap(NEW_MAP_OBJECT_SIZE);
            currentObject.put(CURRENT_OBJECT_CONCEPT_ID, new Integer(USER_DEFINED_CONCEPT_ID));
            currentObject.put(CURRENT_OBJECT_CONCEPT_NAME, conceptName);
        } else if (!ignoredConcept.contains(conceptName)) {
            Debug.trace("Unsupported concept:  " + conceptName);
            currentObject = null;
        } else
            currentObject = null;

        if (currentObject != null) {
            if (multiPartConcept.contains(conceptName))
                tempObjects.add(currentObject); // Add the object currentObject
            // to the temp
        }
    }

    public final void setOccIdentifier(String val) {
    }

    public final void setCompositeIdentifier(String val) {
    }

    public final void hasCompositeIdentifier(boolean b) {
    }

    public final void setAttribute(String attr, String val) throws Exception {
        if ((currentObject == null) || (attr == null) || (val == null))
            return;

        // check for value adjustment (ADJUST_VALUE_FOR_FIX_TAG_LENGTH option)
        if (isTextAttr(attr)) {
            Integer adjust = (Integer) globalOptions.get(ADJUST_VALUE_FOR_FIX_TAG_LENGTH);
            if (adjust != null) {
                int attrfixsize = adjust.intValue();
                int attrsize = attr.length();
                if (attrfixsize > attrsize) {
                    int diff = attrfixsize - attrsize;
                    if (val.length() == diff) {
                        val = "";
                    } else if (val.length() > diff) {
                        val = val.substring(diff, val.length());
                    }
                }
            }
        }
        // if a value with the same tag exists, append val to the old value
        String previousValue = (String) currentObject.get(attr.toLowerCase());
        val = (previousValue == null) ? val : previousValue.concat(val);

        currentObject.put(attr.toLowerCase(), val);
    }

    private boolean isTextAttr(String word) {
        boolean found = false;
        for (int i = 0; i < kTextKeywords.length; i++) {
            String lowercase = word.toLowerCase();
            if (lowercase.equals(kTextKeywords[i])) {
                found = true;
                break;
            }
        }
        return found;
    }

    // Use this method to terminate processing the currentObject and to remove
    // it
    // Can be call at any time during the reverse process
    protected final void removeCurrentObject() throws DbException {
        removeObject = true;
    }

    private boolean checkStatus() throws DbException {
        if (removeObject) {
            if (currentObject != null) {
                DbObject dbo = (DbObject) currentObject.get(CURRENT_OBJECT);
                if (dbo != null)
                    dbo.remove();
                currentObject = null;
            }
            removeObject = false;
        }
        return currentObject != null;
    }

    private void processConceptChanged(String conceptName) throws Exception {
        if (conceptName == null)
            return;
        if (lastConcept == null) {
            lastConcept = conceptName;
            return;
        }
        if (!lastConcept.equals(conceptName)) {
            if (!ignoredConcept.contains(lastConcept)) {
                // process lastConcept objects
                mergeMultiPartObjects();

                Iterator objects = tempObjects.iterator();
                while (objects.hasNext() && (controller == null ? true : controller.checkPoint())) {
                    currentObject = (HashMap) objects.next();
                    processCurrentObject();
                }

                String hook = (String) postProcessConceptMapping.get(lastConcept);
                invokeHook(this, hook);
            }
            currentObject = null;
            tempObjects = new ArrayList(100);
            lastConcept = conceptName;
        }
    }

    protected final void incrementRepportCounter(String concept) {
        if (concept == null)
            return;
        String group = (String) conceptGroupInOutputRepport.get(concept);
        Integer count = (Integer) occurrencesCounters.get(group == null ? concept : group);
        if (count == null)
            count = new Integer(1);
        else {
            int oldcount = count.intValue();
            oldcount++;
            count = new Integer(oldcount);
        }
        occurrencesCounters.put((group == null ? concept : group), count);
    }

    final Object invokeHook(String hook) throws Exception {
        return invokeHook(this, hook);
    }

    final Object invokeHook(Object hookContainer, String hook) throws Exception {
        Object result = null;
        if (hook != null && hookContainer != null) {
            Method hookmethod = hookContainer.getClass().getMethod(hook, null);
            if (hookmethod == null)
                return null;
            result = hookmethod.invoke(this, null);
        }
        return result;
    }

    // If MULTI_PART_OBJECT, merge parts
    private void mergeMultiPartObjects() {
        if (tempObjects.size() == 0)
            return;
        String concept = (String) ((HashMap) tempObjects.get(0)).get(CURRENT_OBJECT_CONCEPT_NAME);
        if (!multiPartConcept.contains(concept))
            return;

        ArrayList mergedObjects = new ArrayList(50);
        int lastPart = Integer.MAX_VALUE;
        boolean firstPart = true;

        // Check tag to merge
        HashSet fields = (HashSet) conceptMapping.get(concept);
        if (fields == null)
            fields = new HashSet();

        ArrayList toMergeFieldMaps = new ArrayList(); // String tag to merge
        ArrayList toMergeKeys = new ArrayList(); // String tag to merge
        Iterator iter = fields.iterator();
        while (iter.hasNext()) {
            FieldMapping fieldmap = (FieldMapping) iter.next();
            if ((fieldmap.options > 0) && (fieldmap.srxElement instanceof SrxPrimitiveElement)) {
                toMergeFieldMaps.add(fieldmap);
                toMergeKeys.add(((SrxPrimitiveElement) fieldmap.srxElement).fieldTag);
            }
        }

        HashMap mergedObject = null;
        Iterator objects = tempObjects.iterator();
        while (objects.hasNext()) {
            HashMap objmap = (HashMap) objects.next();
            int part = Integer.MAX_VALUE;
            try {
                part = Integer.parseInt((String) objmap.get(multiPartTag));
            } catch (Exception e) { // If error, each part will be kept as a
                // unique object (part = Integer.MAX_VALUE)
                Debug.trace("Error processing multipart object.");
            }
            if (part > lastPart)
                firstPart = false;
            else
                firstPart = true;
            lastPart = part;

            if (firstPart) {
                mergedObject = new HashMap();
                mergedObjects.add(mergedObject);
            }

            Iterator iter1 = objmap.keySet().iterator();
            while (iter1.hasNext()) {
                String key = (String) iter1.next();
                if (firstPart) {
                    mergedObject.put(key, objmap.get(key));
                    continue;
                }
                int idx = toMergeKeys.indexOf(key);
                if (idx == -1)
                    continue;

                String oldValue = (String) mergedObject.get(key);
                String newValue = (String) objmap.get(key);
                String resultValue = null;

                int options = ((FieldMapping) toMergeFieldMaps.get(idx)).options;
                if ((options & APPEND_EOL_TO_PART) != 0)
                    newValue = (newValue == null) ? null : newValue.concat(EOL);
                if ((options & APPEND_OLD_VALUE_TO_NEW_VALUE) != 0)
                    resultValue = (newValue == null) ? oldValue : newValue
                            .concat((oldValue == null) ? "" : oldValue);
                else if ((options & APPEND_NEW_VALUE_TO_OLD_VALUE) != 0)
                    resultValue = (oldValue == null) ? newValue : oldValue
                            .concat((newValue == null) ? "" : newValue);

                mergedObject.put(key, resultValue);
            }
        }
        tempObjects = mergedObjects;
    }

    private void processOccurrenceHook(String conceptName) throws Exception {
        if (conceptName == null)
            return;
        String hook = (String) postProcessOccurrenceMapping.get(conceptName);
        invokeHook(hook);
    }

    private void processCurrentObject() throws Exception {
        String concept = (String) currentObject.get(CURRENT_OBJECT_CONCEPT_NAME);
        DbObject dbo = null;
        dbo = getCurrentDbObject();
        if (dbo == null) {
            if (Debug.isDebug()) {
                int category = getCompositionCategory(concept);
                if (category == COMPOSITION_CUSTOM)
                    Debug.trace("Null Component.  Concept=" + concept
                            + ".  This may not be an error (component created by hook).");
                else
                    Debug.trace("Null Component.  Concept=" + concept);
            }
            return;
        }

        if (!checkStatus())
            return;

        currentObject.put(CURRENT_OBJECT, dbo);

        currentObject.put(CURRENT_OBJECT_COMPOSITE, dbo.getComposite());
        processFields();

        if (!checkStatus()) // Check if object must be removed or has been
            // removed
            return;

        processOccurrenceHook(concept); // check occurence hook

        if (!checkStatus()) // Check if object must be removed or has been
            // removed
            return;

        if (conceptInOutputRepport.contains(concept)) {
            String guiName = dbo.getMetaClass().getGUIName(false, false);
            String dboName = dbo.getSemanticalName(DbObject.SHORT_FORM);
            if (guiName != null && dboName != null) {
                String conceptGroup = (String) conceptGroupInOutputRepport.get(concept);
                switch (getCompositionCategory(concept)) {
                case COMPOSITION_FIND_EXISTING:
                case COMPOSITION_USE_EXISTING:
                    appendOutputText(MessageFormat.format(PATTERN_DBO_MODIFIED, new Object[] {
                            guiName, dboName }));
                    break;
                case COMPOSITION_CREATE:
                    appendOutputText(MessageFormat.format(PATTERN_DBO_CREATED, new Object[] {
                            guiName, dboName }));
                    incrementRepportCounter(concept);
                    if (guiNamesSingular.get(conceptGroup == null ? concept : conceptGroup) == null) {
                        guiNamesSingular.put((conceptGroup == null ? concept : conceptGroup), dbo
                                .getMetaClass().getGUIName(false, false));
                        guiNamesPlural.put((conceptGroup == null ? concept : conceptGroup), dbo
                                .getMetaClass().getGUIName(true, false));
                    }
                    break;
                case COMPOSITION_CUSTOM:
                    appendOutputText(MessageFormat.format(PATTERN_DBO_REVERSED, new Object[] {
                            guiName, dboName }));
                    incrementRepportCounter(concept);
                    if (guiNamesSingular.get(conceptGroup == null ? concept : conceptGroup) == null) {
                        guiNamesSingular.put((conceptGroup == null ? concept : conceptGroup), dbo
                                .getMetaClass().getGUIName(false, false));
                        guiNamesPlural.put((conceptGroup == null ? concept : conceptGroup), dbo
                                .getMetaClass().getGUIName(true, false));
                    }
                    break;
                default:
                    break;
                }
            }
        }
    }

    private static final int COMPOSITION_UNDEFINED = -1;
    private static final int COMPOSITION_FIND_EXISTING = 0;
    private static final int COMPOSITION_CREATE = 1;
    private static final int COMPOSITION_USE_EXISTING = 2;
    private static final int COMPOSITION_CUSTOM = 3;

    private int getCompositionCategory(String concept) {
        if (concept == null)
            return COMPOSITION_UNDEFINED;
        Object composition = compositionMapping.get(concept);
        if (composition == null)
            return COMPOSITION_UNDEFINED;
        if (composition instanceof SrxDbObjectElement)
            return COMPOSITION_FIND_EXISTING;
        if (composition instanceof CompositeMapping)
            return COMPOSITION_CREATE;
        if (composition instanceof DbObject)
            return COMPOSITION_USE_EXISTING;
        if (composition instanceof String)
            return COMPOSITION_CUSTOM;
        return COMPOSITION_UNDEFINED;
    }

    private DbObject getCurrentDbObject() throws Exception {
        DbObject dbo = null;
        String concept = (String) currentObject.get(CURRENT_OBJECT_CONCEPT_NAME);
        Object composition = compositionMapping.get(concept);
        switch (getCompositionCategory(concept)) {
        case COMPOSITION_FIND_EXISTING:
            SrxDbObjectElement dbodef = (SrxDbObjectElement) composition;
            Object result1 = ((SrxDbObjectElement) composition).getValue(this, currentObject);
            if (result1 instanceof DbObject)
                dbo = (DbObject) result1;
            else if (result1 == INVALID_OBJECT)
                throw new RuntimeException();
            // if name is specified, ensure it is duplicated in the physical
            // name and alias
            if (dbo != null && (dbo instanceof DbSemanticalObject)) {
                String name = dbo.getName();
                if (name != null && name.length() > 0) {
                    if (((DbSemanticalObject) dbo).getPhysicalName() == null
                            || ((DbSemanticalObject) dbo).getPhysicalName().length() == 0) {
                        ((DbSemanticalObject) dbo).setPhysicalName(name);
                    }
                    if (duplicateNameInAlias()
                            && (((DbSemanticalObject) dbo).getAlias() == null || ((DbSemanticalObject) dbo)
                                    .getAlias().length() == 0)) {
                        ((DbSemanticalObject) dbo).setAlias(name);
                    }
                }
            }
            break;
        case COMPOSITION_CREATE:
            CompositeMapping compositemapping = (CompositeMapping) composition;
            // Find the composite of this component
            Object result2 = (DbObject) compositemapping.srxElement.getValue(this, currentObject);
            DbObject composite = null;
            if (result2 instanceof DbObject)
                composite = (DbObject) result2;
            else if (result2 == INVALID_OBJECT)
                throw new RuntimeException();
            if (composite == null) {
                if (Debug.isDebug())
                    Debug.trace("Null Composite.  Concept=" + concept + ".  srxElement=\n"
                            + compositemapping.srxElement.getDebugInfo(currentObject));
                return null;
            }
            // Create the object
            if (compositemapping.componentMetaClass != null) {
                if (Debug.isDebug())
                    Debug.assert2(compositemapping.componentMetaClass.compositeIsAllowed(composite
                            .getMetaClass()), "Cannot create an object of type "
                            + compositemapping.componentMetaClass + " for composite of type "
                            + composite.getMetaClass() + "."); // NOT
                // LOCALIZABLE
                // -
                // Debug
                dbo = composite.createComponent(compositemapping.componentMetaClass);
            }
            break;
        case COMPOSITION_USE_EXISTING:
            dbo = (DbObject) composition;
            break;
        case COMPOSITION_CUSTOM:
            String hook = (String) composition;
            Object obj = invokeHook(hook);
            if (obj instanceof DbObject)
                dbo = (DbObject) obj;
            else if (obj == INVALID_OBJECT)
                throw new RuntimeException();
            else if (obj != null) {
                Debug.trace("Invalid Hook for creating <" + concept
                        + "> :  Returned value must be a DbObject.");
                throw new RuntimeException();
            }
            break;
        default:
            Debug.trace("No db object composition mapping for concept: " + concept
                    + ".  currentObject ignored. ");
            return null;
        }
        return dbo;
    }

    // Set the metafields on the dbo
    private void processFields() throws Exception {
        String concept = (String) currentObject.get(CURRENT_OBJECT_CONCEPT_NAME);
        DbObject dbo = (DbObject) currentObject.get(CURRENT_OBJECT);
        HashSet conceptSet = (HashSet) conceptMapping.get(concept);
        if (conceptSet != null) {
            Iterator mappings = conceptSet.iterator();
            while (mappings.hasNext() && checkStatus()) {
                FieldMapping fieldmapping = (FieldMapping) mappings.next();
                if (fieldmapping != null) {
                    Object value = fieldmapping.srxElement.getValue(this, currentObject);
                    if (value == INVALID_OBJECT)
                        throw new RuntimeException();
                    else
                        fieldmapping.dbElement.setValue(this, currentObject, value);
                }
            }
        }

        if (currentObject == null) // object removed
            return;

        // set the alias if name exist for that object and option is selected
        if ((dbo instanceof DbSemanticalObject) && checkStatus()) {
            String name = ((DbSemanticalObject) dbo).getName();
            ((DbSemanticalObject) dbo).setPhysicalName(name);
            if (duplicateNameInAlias())
                ((DbSemanticalObject) dbo).setAlias(name);
        }
    }

    // Override to allow automatic duplication of name in alias (apply to all
    // concept)
    protected boolean duplicateNameInAlias() {
        return false;
    }

    // For debug purpose only
    protected final void printMapObject(HashMap map) throws DbException {
        Iterator keys = map.keySet().iterator();
        Debug.trace("MAP OBJECT --------------------------------------------");
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Debug.trace("   Key <" + key + ">.  Value <" + map.get(key) + ">.");
        }
        Debug.trace("END OF MAP OBJECT -------------------------------------");
    }

    private void dumpRepportSummary() {
        String dump = "";
        Iterator iter1 = repportSummarySequence.iterator();
        while (iter1.hasNext()) {
            String conceptOrGroup = (String) iter1.next();
            Integer count = (Integer) occurrencesCounters.get(conceptOrGroup);
            if (count == null)
                continue;
            String guiname = (count.intValue() < 2) ? (String) guiNamesSingular.get(conceptOrGroup)
                    : (String) guiNamesPlural.get(conceptOrGroup);
            if (guiname == null)
                continue;
            String conceptsummary = EOL
                    + MessageFormat.format(kRepportSummaryCount01, new Object[] { count, guiname });
            dump = dump.concat(conceptsummary);
        }
        String reportFooter = createRepportSummaryFooter();
        if (reportFooter != null)
            dump += reportFooter;
        appendOutputText(dump);
    }

    public final void abort() throws Exception {
        abortSpecific();
        currentObject = null;
        tempObjects = null;
        root.getDb().abortTrans();
        controller = null;
        conceptMapping = null;
        compositionMapping = null;
        postProcessConceptMapping = null;
        ignoredConcept = null;
        postProcessOccurrenceMapping = null;
        multiPartConcept = null;
        conceptGroupInOutputRepport = null;
    }

    public final void exit() throws Exception {
        if (currentObject != null
                && !multiPartConcept.contains((String) currentObject
                        .get(CURRENT_OBJECT_CONCEPT_NAME))) {
            processCurrentObject();
        }
        processConceptChanged(""); // process last concept

        currentObject = null;
        tempObjects = null;
        exitSpecific();
        dumpRepportSummary();
        Boolean chaincommit = (Boolean) globalOptions.get(CHAIN_COMMIT);
        if (chaincommit == null || !chaincommit.booleanValue())
            project.getDb().commitTrans();
        else
            project.getDb().commitTrans(Db.CHAIN_HIST);
        controller = null;
        conceptMapping = null;
        compositionMapping = null;
        postProcessConceptMapping = null;
        ignoredConcept = null;
        postProcessOccurrenceMapping = null;
        multiPartConcept = null;
        conceptGroupInOutputRepport = null;
    }

    // Override to remove all references that may prevent garbage collection
    protected abstract void exitSpecific() throws Exception;

    // Override to remove all references that may prevent garbage collection
    protected abstract void abortSpecific() throws Exception;
}
