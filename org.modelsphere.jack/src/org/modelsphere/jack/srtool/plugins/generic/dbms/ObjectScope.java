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

package org.modelsphere.jack.srtool.plugins.generic.dbms;

import java.io.Serializable;
import java.util.ArrayList;

import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;

/**
 * 
 * Contain all the information needed for all objects to be reversed.
 * 
 */
public class ObjectScope implements Serializable {
    /**
     * This class must be initialize in the toolkit for the reverse amd Forward engineering.
     * 
     * mappingName : (Reverse)The name corresponding to the 'sr_occurrence_type' in the extract
     * file. Each 'Select' statement in the extract file must be registered with ObjectScope in the
     * toolkit.
     * 
     * 
     * 
     * conceptName : The name of the concept, this name will be used in all JTree.
     * 
     * conceptID : Give a unique ID for all concepts, start with 0.
     * 
     * parentID : (Reverse) Create a hierarchy in the JTree in the Option Page. Use the conceptID.
     * (Forward) If !null the concept is send to template only if the parentID is not send to
     * template! If the parentID is send to the template, then the concept is not send because the
     * concept is include in the parent
     * 
     * showConcept : If the concept must be shown in the Option Page.
     * 
     * showOccurences : If the concept must be shown with his occurences in the Object Page.
     * 
     * isOwnedObject : If the concept must be defined with the owner.
     * 
     * SQLGetID : Give a unique number corresponding to the order in the GetFile.sql
     * 
     * metaClass : Used by synchro for convertin a concept scope usable by the reverse eng from an
     * integration scope.
     * 
     */
    public String mappingName;
    public String conceptName;
    public int conceptID;
    public Object parentID = null;
    public boolean showConcept;
    public boolean showOccurences;
    public boolean isOwnedObject;
    public Object SQLGetID = null;
    public boolean isSelected = true;
    public MetaClass metaClass = null;
    public Integer[] childrenID = null;
    public boolean isInitiallyChecked = true;

    public ArrayList occurences = new ArrayList();

    public ObjectScope(String aMappingName, String name, int conceptID, Object parentID,
            boolean showConcept, boolean showOccurences, boolean isOwnedObject, Object SQLGetID) {
        this.mappingName = aMappingName;
        this.conceptName = name;
        this.conceptID = conceptID;
        this.parentID = parentID;
        this.showConcept = showConcept;
        this.showOccurences = showOccurences;
        this.isOwnedObject = isOwnedObject;
        this.SQLGetID = SQLGetID;
    }

    public ObjectScope(String aMappingName, String name, int conceptID, Object parentID,
            boolean showConcept, boolean showOccurences, boolean isOwnedObject, Object SQLGetID,
            boolean isInitiallyChecked) {
        this.mappingName = aMappingName;
        this.conceptName = name;
        this.conceptID = conceptID;
        this.parentID = parentID;
        this.showConcept = showConcept;
        this.showOccurences = showOccurences;
        this.isOwnedObject = isOwnedObject;
        this.SQLGetID = SQLGetID;
        this.isInitiallyChecked = isInitiallyChecked;
    }

    public ObjectScope(String aMappingName, String name, int conceptID, Object parentID,
            boolean showConcept, boolean showOccurences, boolean isOwnedObject, Object SQLGetID,
            MetaClass metaClass, boolean isInitiallyChecked) {
        this.mappingName = aMappingName;
        this.conceptName = name;
        this.conceptID = conceptID;
        this.parentID = parentID;
        this.showConcept = showConcept;
        this.showOccurences = showOccurences;
        this.isOwnedObject = isOwnedObject;
        this.SQLGetID = SQLGetID;
        this.metaClass = metaClass;
        this.isInitiallyChecked = isInitiallyChecked;
    }

    public ObjectScope(String aMappingName, String name, int conceptID, Object parentID,
            boolean showConcept, boolean showOccurences, boolean isOwnedObject, Object SQLGetID,
            MetaClass metaClass) {
        this.mappingName = aMappingName;
        this.conceptName = name;
        this.conceptID = conceptID;
        this.parentID = parentID;
        this.showConcept = showConcept;
        this.showOccurences = showOccurences;
        this.isOwnedObject = isOwnedObject;
        this.SQLGetID = SQLGetID;
        this.metaClass = metaClass;
    }

    public ObjectScope(String name, int conceptID, Object parentID, boolean showConcept,
            boolean showOccurences, boolean isOwnedObject, Object SQLGetID) {
        this("", name, conceptID, parentID, showConcept, showOccurences, isOwnedObject, SQLGetID);
    }

    public ObjectScope(String name, int conceptID, Object parentID, boolean showConcept,
            boolean showOccurences, boolean isOwnedObject, Object SQLGetID,
            boolean isInitiallyChecked) {
        this("", name, conceptID, parentID, showConcept, showOccurences, isOwnedObject, SQLGetID,
                isInitiallyChecked);
    }

    // use for the Forward
    public ObjectScope(String name, Integer conceptID, Object parentID, Integer[] childrenID,
            boolean showConcept, boolean showOccurences, MetaClass metaClass, boolean isOwnedObject) {
        this(null, name, conceptID.intValue(), parentID, showConcept, showOccurences,
                isOwnedObject, null);
        this.metaClass = metaClass;
        this.childrenID = childrenID;
    }

    // use for the Forward
    public ObjectScope(String name, Integer conceptID, Object parentID, boolean showConcept,
            boolean showOccurences, MetaClass metaClass, boolean isOwnedObject) {
        this(name, conceptID, parentID, null, showConcept, showOccurences, metaClass, isOwnedObject);
    }

    // use for the Forward
    public ObjectScope(String condition, String name, Integer conceptID, Object parentID,
            boolean showConcept, boolean showOccurences, MetaClass metaClass, boolean isOwnedObject) {
        this(name, conceptID, parentID, null, showConcept, showOccurences, metaClass, isOwnedObject);
        this.mappingName = condition;
    }

    public ObjectScope(String mappingName, int conceptID, Object parentID) {
        this(mappingName, "", conceptID, parentID, false, false, false, null);
    }

    // use for the Forward & Synchro
    public ObjectScope(String mappingName, int conceptID, Object parentID, MetaClass metaClass) {
        this(mappingName, "", conceptID, parentID, false, false, false, null);
        this.metaClass = metaClass;
    }

    public ObjectScope(int conceptID, Object parentID) {
        this("", conceptID, parentID);
    }

    // Find a specific concept in an ObjectScope array with the conceptName
    public static ObjectScope findConceptInObjectScopeWithConceptName(ObjectScope[] scope,
            String conceptName) {
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.conceptName.equalsIgnoreCase(conceptName))
                return concept;
        }
        return null;
    }

    // Find a specific concept in an ObjectScope array with the mappingName
    public static ObjectScope findConceptInObjectScopeWithMappingName(ObjectScope[] scope,
            String mappingName) {
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.mappingName.equalsIgnoreCase(mappingName))
                return concept;
        }
        // GP: TEMPORARY - Should return null ... workaround to fix a bug with
        // international releases
        return findConceptInObjectScopeWithConceptName(scope, mappingName);
    }

    // Find a specific concept in an ObjectScope array with the metaClass
    public static ObjectScope findConceptInObjectScopeWithMetaClass(ObjectScope[] scope,
            MetaClass metaClass) {
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.metaClass == metaClass)
                return concept;
        }
        return null;
    }

    // Find a specific concept in an ObjectScope array with the metaClassName
    public static ObjectScope findConceptInObjectScopeWithMetaClassName(ObjectScope[] scope,
            String metaClassName) {
        MetaClass metaclass = MetaClass.find(metaClassName);
        if (metaclass == null) // obsolete metaClass
            return null;
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.metaClass == metaclass)
                return concept;
        }
        return null;
    }

    // Find a specific concept in the ObjectScope array with the conceptID
    public static ObjectScope findConceptInObjectScopeWithConceptID(ObjectScope[] scope,
            Object conceptID) {
        for (int i = 0; i < scope.length; i++) {
            ObjectScope concept = scope[i];
            if (concept.conceptID == ((Integer) conceptID).intValue())
                return concept;
        }
        return null;
    }

    // Find a specific occurence in an ObjectScope.occurence,
    // must be an arraylit of ObjectSelection
    public static boolean findOccurenceByName(ObjectScope scope, String occurenceName) {
        ObjectSelection objSelection = findObjectOccurenceByName(scope, occurenceName);
        if (objSelection != null)
            return true;

        return false;
    }

    public static ObjectSelection findObjectOccurenceByName(ObjectScope scope, String occurenceName) {
        for (int i = 0; i < scope.occurences.size(); i++) {
            ObjectSelection objSelection = (ObjectSelection) scope.occurences.get(i);
            if (objSelection != null && objSelection.name.equals(occurenceName))
                return objSelection;
        }
        return null;
    }

    // Find a specific occurence in an ObjectScope.occurence,
    // must be an arraylit of ObjectSelection
    public static boolean findOccurenceByObject(ObjectScope scope, DbObject occurence) {
        for (int i = 0; i < scope.occurences.size(); i++) {
            ObjectSelection foundOccurence = (ObjectSelection) scope.occurences.get(i);
            if (foundOccurence != null && foundOccurence.occurence == occurence)
                return true;
        }
        return false;
    }

    // Find a specific occurence in an ObjectScope and return if the occurence
    // is selected
    public static boolean isOccurenceIsSelectedByObject(ObjectScope scope, DbObject occurence) {
        for (int i = 0; i < scope.occurences.size(); i++) {
            if (scope.isOwnedObject) {
                UserInfo userInfo = (UserInfo) scope.occurences.get(i);
                for (int j = 0; j < userInfo.occurences.size(); j++) {
                    ObjectSelection foundOccurence = (ObjectSelection) userInfo.occurences.get(j);
                    if (foundOccurence != null && foundOccurence.occurence == occurence)
                        return foundOccurence.getIsSelected();
                }
            } else {
                ObjectSelection foundOccurence = (ObjectSelection) scope.occurences.get(i);
                if (foundOccurence != null && foundOccurence.occurence == occurence)
                    return foundOccurence.getIsSelected();
            }
        }
        return false;
    }

    // Verify if the concept is selected, if his parent his selected and also
    // if there is at least one occurence selected.
    public static boolean verifyConceptSelection(ObjectScope[] scope, ObjectScope concept) {
        return verifyConceptSelection(scope, concept, true);
    }

    // Verify if the concept is selected, if his parent his selected and also
    // if there is at least one occurence selected.
    public static boolean verifyConceptSelection(ObjectScope[] scope, ObjectScope concept,
            boolean verifyOccurenceSelection) {
        if (!concept.isSelected)
            return false;
        if (verifyOccurenceSelection && !verifyOccurenceSelection(concept))
            return false;

        if (concept.parentID != null) {
            ObjectScope parent = findConceptInObjectScopeWithConceptID(scope, concept.parentID);
            if (parent != null)
                return verifyConceptSelection(scope, parent);
        }
        return true;
    }

    // verify if there is at least one occurence selected.
    private static boolean verifyOccurenceSelection(ObjectScope concept) {
        boolean select = false;

        if (concept.showOccurences) {
            if (concept.isOwnedObject) {
                for (int i = 0; i < concept.occurences.size(); i++) {
                    UserInfo userInfo = (UserInfo) concept.occurences.get(i);
                    for (int j = 0; j < userInfo.occurences.size(); j++) {
                        ObjectSelection occurence = (ObjectSelection) userInfo.occurences.get(j);
                        if (occurence.getIsSelected())
                            select = true;
                    }
                }
            } else {
                for (int i = 0; i < concept.occurences.size(); i++) {
                    ObjectSelection occurence = (ObjectSelection) concept.occurences.get(i);
                    if (occurence.getIsSelected())
                        select = true;
                }
            }
        } else
            select = true;

        return select;
    }

    public String toString() {
        return (conceptName != null ? conceptName : ""); // NOT LOCALIZABLE
    }
}
