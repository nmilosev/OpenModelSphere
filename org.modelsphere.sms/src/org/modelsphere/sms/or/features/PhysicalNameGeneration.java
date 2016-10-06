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

package org.modelsphere.sms.or.features;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

import org.modelsphere.jack.actions.util.DbMultiTrans;
import org.modelsphere.jack.awt.TextViewerDialog;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.jack.baseDb.meta.MetaClass;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.StringUtil;
import org.modelsphere.sms.be.db.DbBEDiagram;
import org.modelsphere.sms.be.db.DbBEModel;
import org.modelsphere.sms.be.db.DbBEUseCase;
import org.modelsphere.sms.db.DbSMSDiagram;
import org.modelsphere.sms.db.DbSMSPackage;
import org.modelsphere.sms.or.db.DbORAssociationEnd;
import org.modelsphere.sms.or.db.DbORCommonItemModel;
import org.modelsphere.sms.or.db.DbORDiagram;
import org.modelsphere.sms.or.db.DbORForeign;
import org.modelsphere.sms.or.db.DbORModel;
import org.modelsphere.sms.or.db.DbORPrimaryUnique;
import org.modelsphere.sms.or.db.DbORUserNode;
import org.modelsphere.sms.or.international.LocaleMgr;
import org.modelsphere.sms.or.oracle.db.DbORAAbsPartition;
import org.modelsphere.sms.or.screen.GeneratePhysicalNameFrame;
import org.modelsphere.sms.or.screen.model.GeneratePhysicalNameModel;

public final class PhysicalNameGeneration {
    private static final String EOL = System.getProperty("line.separator"); //NOT LOCALIZABLE, property key
    private static final String TAB = "    "; //4 spaces use as TAB   NOT LOCALIZABLE, property key
    private static final String COMMENT_SEPARATOR = "***********************************************************"
            + EOL; //NOT LOCALIZABLE, property key
    private static final String SMS_FILE = LocaleMgr.screen.getString("sourceFileForwardHeader"); //NOT LOCALIZABLE, property key
    private static final String GENERATE_ON = LocaleMgr.screen.getString("generateOnForwardHeader"); //NOT LOCALIZABLE, property key
    private static final String EMPTY_NAME = LocaleMgr.screen.getString("emptyToken"); //NOT LOCALIZABLE, property key
    private static final String PhysicalNameTrans = LocaleMgr.action.getString("PhysicalNameTrans");//NOT LOCALIZABLE, property key
    private static final String NO_CHANGE = LocaleMgr.screen.getString("PhysicalNameGenNoChanges"); //NOT LOCALIZABLE
    private static final String ARROW = " --> "; //NOT LOCALIZABLE

    private String wordDelimiters = null;
    private boolean useDictionary = false;
    private File dictionaryFile = null;
    private PhysicalNameDictionary dictionary = new PhysicalNameDictionary();
    private StringBuffer report = new StringBuffer(250);
    private boolean somePhysicalNameGen = false;
    private HashMap accentedCharactersHashMap = null;

    public PhysicalNameGeneration() {
    }

    public final void generate(Frame frame, DbObject[] semObjs) throws DbException {
        GeneratePhysicalNameFrame genPNames = new GeneratePhysicalNameFrame(frame, dictionary);
        genPNames.setVisible(true);
        if (genPNames.doGeneration()) {
            wordDelimiters = genPNames.getWordDelimiters();
            useDictionary = genPNames.useDictionary();
            dictionaryFile = genPNames.getDictionaryFile();
            accentedCharactersHashMap = genPNames.getAccentedCharactersToReplace();
            writeHeader(semObjs);
            generateForConcepts(((GeneratePhysicalNameModel) genPNames.getConceptsTable()
                    .getModel()).getParametersToGenerate(), semObjs);
            writeEnd();
            if (somePhysicalNameGen)
                TextViewerDialog.showTextDialog(frame, PhysicalNameTrans, report.toString());
            else
                JOptionPane.showMessageDialog(frame, NO_CHANGE);
            // javax.swing.JOptionPane.showMessageDialog(frame, LocaleMgr.message.getString("PNGCompleted"));
        }
    }

    private final void generateForConcepts(ArrayList concepts, DbObject[] semObjs)
            throws DbException {
        DbMultiTrans.beginTrans(Db.WRITE_TRANS, semObjs, PhysicalNameTrans);
        for (int i = 0; i < semObjs.length; i++) {
            DbObject semObj = semObjs[i];
            if (semObj instanceof DbORDiagram) {
                semObj = (DbORModel) semObj.getCompositeOfType(DbORModel.metaClass);
            }

            if (semObj instanceof DbBEDiagram) {
                semObj = (DbBEUseCase) semObj.getCompositeOfType(DbBEUseCase.metaClass);
            }

            if (semObj != null) {
                writePackageHeader(semObj);
                boolean innerPackageNameGen = false;
                for (int j = 0; j < concepts.size(); j++) {
                    ArrayList conceptsChanged = new ArrayList();
                    boolean addSemObjToGeneration = false;
                    ORPNGParameters param = (ORPNGParameters) concepts.get(j);
                    if (param.getConceptMetaClass().isAssignableFrom(semObj.getMetaClass()))
                        addSemObjToGeneration = true;
                    ArrayList uniqueName = (param.isUnique().booleanValue() ? new ArrayList()
                            : null);
                    if (param.getStatus().getValue() == ORPNGStatus.PARTIAL) {
                        ArrayList listConcepts = new ArrayList();
                        if (addSemObjToGeneration)
                            listConcepts.add(semObj);
                        DbEnumeration dbEnum = semObj.componentTree(param.getConceptMetaClass(),
                                new MetaClass[] { DbSMSPackage.metaClass, DbSMSDiagram.metaClass });
                        while (dbEnum.hasMoreElements()) {
                            DbSemanticalObject concept = (DbSemanticalObject) dbEnum.nextElement();
                            listConcepts.add(concept);
                            if (!StringUtil.isEmptyString(concept.getPhysicalName())
                                    && param.isUnique().booleanValue())
                                uniqueName.add(concept.getPhysicalName());
                        } //end while
                        dbEnum.close();
                        for (int ii = 0; ii < listConcepts.size(); ii++) {
                            DbSemanticalObject concept = (DbSemanticalObject) listConcepts.get(ii);
                            if (StringUtil.isEmptyString(concept.getPhysicalName())) {
                                String physicalName = generatePhysicalNameFor(concept, uniqueName,
                                        param);
                                if (physicalName == null)
                                    continue;
                                concept.setPhysicalName(physicalName);
                                conceptsChanged.add(getConceptComment(concept));
                                if (param.isUnique().booleanValue())
                                    uniqueName.add(concept.getPhysicalName());
                            } //end if
                        } //end for
                    } else if (param.getStatus().getValue() == ORPNGStatus.COMPLETE) {
                        if (addSemObjToGeneration) {
                            String physicalName = generatePhysicalNameFor(
                                    (DbSemanticalObject) semObj, uniqueName, param);
                            if (physicalName != null) {
                                ((DbSemanticalObject) semObj).setPhysicalName(physicalName);
                                if (param.isUnique().booleanValue())
                                    uniqueName.add(((DbSemanticalObject) semObj).getPhysicalName());
                            } //end if
                        } //end if
                        DbEnumeration dbEnum = semObj.componentTree(param.getConceptMetaClass(),
                                new MetaClass[] { DbSMSPackage.metaClass, DbSMSDiagram.metaClass });
                        while (dbEnum.hasMoreElements()) {
                            DbSemanticalObject concept = (DbSemanticalObject) dbEnum.nextElement();
                            String genName = generatePhysicalNameFor(concept, uniqueName, param);
                            if (genName == null)
                                continue;
                            concept.setPhysicalName(genName);
                            conceptsChanged.add(getConceptComment(concept));
                            if (param.isUnique().booleanValue())
                                uniqueName.add(genName);

                        } //end while
                    } //end if 

                    if (conceptsChanged.size() > 0) {
                        writeInnerPackageComment(param.getConceptName());
                        writeInnerConceptComment(conceptsChanged);
                        innerPackageNameGen = true;
                    } //end if 
                } //end for 

                if (innerPackageNameGen)
                    somePhysicalNameGen = true;
                else
                    writeInnerPackageComment(NO_CHANGE);
            } //end if
        } //end for

        DbMultiTrans.commitTrans(semObjs);
    } //end generateForConcepts()

    private final String generatePhysicalNameFor(DbSemanticalObject concept, ArrayList uniqueName,
            ORPNGParameters param) throws DbException {
        String physicalName = null;

        physicalName = getSourceNameFor(concept);
        if (physicalName == null)
            return null;
        if (useDictionary && dictionaryFile != null)
            physicalName = getNameWithAbbreviationFor(physicalName);
        physicalName = getNameWithDelimitersReplacedBySpace(physicalName, param);
        physicalName = getNameWithTruncatedWords(physicalName, param);
        physicalName = getNameWithSpaceReplaced(physicalName, param);
        physicalName = getNameWithParamApplied(physicalName, param);
        physicalName = getNameWithAccentedCharactersReplaced(physicalName);
        if (param.isUnique().booleanValue()) {
            physicalName = getUniqueName(physicalName, uniqueName, param);
        }
        return physicalName;
    }

    //get the source name use to generate the physical name, by default, the name is use.
    private final String getSourceNameFor(DbSemanticalObject concept) throws DbException {
        String sourceName = null;

        if (concept instanceof DbORAssociationEnd) {
            sourceName = concept.getComposite().getName() + " " + concept.getName(); //NOT LOCALIZABLE
        } else if (concept instanceof DbORForeign) {
            sourceName = LocaleMgr.misc.getString("FK") + " " + concept.getName(); //NOT LOCALIZABLE
        } else if (concept instanceof DbORAAbsPartition) {
            sourceName = concept.getComposite().getName();
            if (((DbORAAbsPartition) concept).getTablespace() != null) {
                sourceName = sourceName + " "
                        + ((DbORAAbsPartition) concept).getTablespace().getName(); //NOT LOCALIZABLE
            }
            sourceName = sourceName + " " + concept.getName(); //NOT LOCALIZABLE
        } else if (concept instanceof DbORPrimaryUnique) {
            if (((DbORPrimaryUnique) concept).isPrimary())
                sourceName = LocaleMgr.misc.getString("PK") + " "
                        + concept.getComposite().getName() + " " + concept.getName(); //NOT LOCALIZABLE
            else
                sourceName = LocaleMgr.misc.getString("UK") + " "
                        + concept.getComposite().getName() + " " + concept.getName(); //NOT LOCALIZABLE
        } else {
            sourceName = concept.getName();
        }

        return sourceName;
    }

    // replace all spaces and other word delimiters by the replacement character in the generation parameter
    private final String getNameWithSpaceReplaced(String name, ORPNGParameters param) {
        String newName;
        String replacementString = param.getReplacementChar();

        newName = StringUtil.replaceWords(name, " ", replacementString);
        return newName;
    }

    // replace all accented characters ex: é = e à = a
    private final String getNameWithAccentedCharactersReplaced(String name) {
        String newName = name;
        Set keySet = accentedCharactersHashMap.keySet();
        Object[] keyArray = keySet.toArray();
        for (int i = 0; i < keyArray.length; i++) {
            String key = (String) keyArray[i];
            if (newName.indexOf(key) != -1) {
                String value = (String) accentedCharactersHashMap.get(key);
                if (value != null)
                    newName = StringUtil.replaceWords(newName, key, value);
            }
        }
        return newName;
    }

    // replace all delimiters by space
    private final String getNameWithDelimitersReplacedBySpace(String name, ORPNGParameters param) {
        String newName = name;
        String space = " ";
        char[] wordDelimitersList = wordDelimiters.toCharArray();

        for (int i = 0; i < wordDelimitersList.length; i++) {
            newName = StringUtil
                    .replaceWords(newName, String.valueOf(wordDelimitersList[i]), space);
        }
        return newName;
    }

    private final String getNameWithTruncatedWords(String name, ORPNGParameters param) {
        String space = " ";
        int nbrCharByWord = param.getNbrCharByWord().intValue();
        String newName = null;

        //Apply the nbr of char by word
        while (name.indexOf(space) != -1) {
            String word = name.substring(0, name.indexOf(space));
            if (word.length() > nbrCharByWord)
                word = word.substring(0, nbrCharByWord);
            if (word.length() > 0)
                newName = newName == null ? word : newName + space + word;
            if ((name.indexOf(space) + space.length()) < name.length())
                name = name.substring(name.indexOf(space) + space.length(), name.length());
            else {
                name = "";
                break;
            }
        }
        if (newName == null) {
            newName = name;
            if (newName.length() > nbrCharByWord)
                newName = newName.substring(0, nbrCharByWord);
        } else {
            if (name.length() > nbrCharByWord)
                newName = newName + space + name.substring(0, nbrCharByWord);
            else
                newName = newName + space + name;
        }

        return newName;
    }

    private final String getNameWithParamApplied(String name, ORPNGParameters param) {
        String replacementString = param.getReplacementChar();
        int length = param.getLength().intValue();
        String newName = name;

        //Apply the physical name length
        if (newName.length() > length)
            newName = newName.substring(0, length);

        //Apply Case change;
        switch (param.getCase().getValue()) {
        case ORPNGCase.LOWER:
            newName = newName.toLowerCase();
            break;
        case ORPNGCase.UPPER:
            newName = newName.toUpperCase();
            break;
        default:
            break;
        }
        return newName;
    }

    private final String getUniqueName(String name, ArrayList uniqueName, ORPNGParameters param) {
        int length = param.getLength().intValue();
        int nbrCharByWord = param.getNbrCharByWord().intValue();
        String newName = name;

        if (!uniqueNameContains(uniqueName, name))
            return name;
        int i = 1;

        while (uniqueNameContains(uniqueName, newName)) {
            String seq = String.valueOf(i);

            if (name.length() + seq.length() > length) {
                newName = name.substring(0, length - seq.length()) + seq;
            } else {
                newName = name + seq;
            }
            i++;
        }
        return newName;
    }

    private final boolean uniqueNameContains(ArrayList uniqueName, String name) {
        for (int j = 0; j < uniqueName.size(); j++) {
            if (((String) uniqueName.get(j)).equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    private final String getNameWithAbbreviationFor(String name) {
        String newName = null;

        dictionary.load(dictionaryFile);
        newName = dictionary.getAbbreviation(name);
        if (newName == null) {
            //try each word in dictionary
            String tempName = name;
            while (tempName.indexOf(' ') != -1) { //NOT LOCALIZABLE
                String word = tempName.substring(0, tempName.indexOf(' '));
                String abb = dictionary.getAbbreviation(word);
                if (abb != null) {
                    newName = newName == null ? abb : newName + " " + abb; //NOT LOCALIZABLE
                } else
                    newName = newName == null ? word : newName + " " + word; //NOT LOCALIZABLE

                if ((tempName.indexOf(' ') + 1) < tempName.length())
                    tempName = tempName.substring(tempName.indexOf(' ') + 1, tempName.length());
                else {
                    tempName = null;
                    break;
                }
            }
            if (!StringUtil.isEmptyString(tempName)) {
                String abb = dictionary.getAbbreviation(tempName);
                if (abb != null)
                    newName = newName == null ? abb : newName + " " + abb;
                else
                    newName = name;
            }
        }
        return newName;
    }

    //Report Section
    private final void writeHeader(DbObject[] semObjs) throws DbException {
        report.append(COMMENT_SEPARATOR);
        report.append(TAB + PhysicalNameTrans + EOL);
        String fileName = semObjs[0].getProject().getRamFileName() == null ? EMPTY_NAME
                : semObjs[0].getProject().getRamFileName();
        report.append(TAB + SMS_FILE + "  \"" + fileName + "\"" + EOL); //NOT LOCALIZABLE
        report.append(TAB + GENERATE_ON + "  " + new java.util.Date(System.currentTimeMillis())
                + EOL);
        report.append(COMMENT_SEPARATOR);
    }

    private final void writePackageHeader(DbObject sem) throws DbException {
        report.append(EOL + (sem.getName() == null ? "" : sem.getName()) + EOL + EOL);
    }

    private final void writeInnerPackageComment(String s) throws DbException {
        report.append(TAB + s + EOL);
    }

    private final void writeInnerConceptComment(String s) throws DbException {
        report.append(TAB + TAB + s + EOL);
    }

    private final void writeInnerConceptComment(ArrayList innerComments) throws DbException {
        Collections.sort(innerComments, new CollationComparator());
        for (int i = 0; i < innerComments.size(); i++) {
            writeInnerConceptComment((String) innerComments.get(i));
        }
    }

    private final String getConceptComment(DbSemanticalObject sem) throws DbException {
        String comment;
        if (sem.getComposite() instanceof DbORModel
                || sem.getComposite() instanceof DbORCommonItemModel
                || sem.getComposite() instanceof DbORUserNode
                || sem.getComposite() instanceof DbBEModel
                || sem.getComposite() instanceof DbBEUseCase)
            comment = sem.getName() + ARROW + sem.getPhysicalName();
        else
            comment = sem.getComposite().getName() + "." + sem.getName() + ARROW
                    + sem.getPhysicalName(); //NOT LOCALIZABLE
        return comment;
    }

    private final void writeEnd() {
        report.append(EOL + COMMENT_SEPARATOR);
        report.append(LocaleMgr.message.getString("PNGCompleted") + EOL);
        report.append(COMMENT_SEPARATOR + EOL);
    }
}
