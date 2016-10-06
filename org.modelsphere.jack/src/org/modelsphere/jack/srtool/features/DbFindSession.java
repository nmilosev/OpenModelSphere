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

package org.modelsphere.jack.srtool.features;

import javax.swing.tree.TreePath;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.UDFValueType;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.explorer.DynamicNode;
import org.modelsphere.jack.srtool.explorer.ExplorerView;
import org.modelsphere.jack.srtool.explorer.GroupParams;
import org.modelsphere.jack.srtool.graphic.DbGraphic;
import org.modelsphere.jack.util.CollationComparator;
import org.modelsphere.jack.util.SrVector;

public final class DbFindSession {
    // direction values
    public static final int FORWARD = 1;
    public static final int BACKWARD = -1;

    // fields valid during a find session
    private FindOption findOption;
    private DbObject[] rootObjects;
    private SrVector foundObjects = new SrVector();
    private int index;
    private SrVector findListeners = new SrVector();

    // Caller must start a read transaction on all Db's encompassed by the root
    // objects.
    public final FoundObject find(FindOption findOption, DbObject[] rootObjects, int direction)
            throws DbException {
        this.findOption = findOption;
        this.rootObjects = rootObjects;
        _find();
        return findNextFromStart(direction);
    }

    // Caller must start a read transaction on all Db's encompassed by the root
    // objects.
    public final FoundObject findNext(int direction) throws DbException {
        FoundObject fo = null;
        while (true) {
            if (direction == FORWARD) {
                if (++index >= foundObjects.size()) {
                    index = foundObjects.size();
                    break;
                }
            } else {
                if (--index < 0) {
                    index = -1;
                    break;
                }
            }
            fo = (FoundObject) foundObjects.get(index);
            if (fo.dbo.isAlive())
                break;
            fo = null;
        }
        fireFindSessionListeners();
        return fo;
    }

    // Caller must start a read transaction on all Db's encompassed by the root
    // objects.
    public final FoundObject findNextFromStart(int direction) throws DbException {
        index = (direction == FORWARD ? -1 : foundObjects.size());
        return findNext(direction);
    }

    public final boolean hasNext() {
        return index + 1 < foundObjects.size();
    }

    public final boolean hasPrevious() {
        return index > 0;
    }

    public final FindOption getFindOption() {
        return findOption;
    }

    public final DbObject[] getRootObjects() {
        return rootObjects;
    }

    private void _find() throws DbException {
        foundObjects = new SrVector();
        DbEnumeration dbEnum;
    	boolean isNode = false;
        Object focusObject = ApplicationContext.getFocusManager().getFocusObject();
        if(focusObject instanceof ExplorerView){
        	TreePath path = ((ExplorerView) focusObject).getAnchorSelectionPath();
            DynamicNode node = (DynamicNode) path.getLastPathComponent();
            Object userObject = node.getUserObject();
        	if (userObject instanceof GroupParams){
        		isNode = true;
        	}        	
        }       
        for (int i = 0; i < rootObjects.length; i++) {
            if (findOption.getRecursive()){
            	dbEnum = rootObjects[i].componentTree(DbSemanticalObject.metaClass);
            }  
            else {
            	if (isNode)
            		break;
            	else
            		dbEnum = rootObjects[i].getComponents().elements(DbSemanticalObject.metaClass);	            	
            }
            while (dbEnum.hasMoreElements()) {           	           	
                DbSemanticalObject dbo = (DbSemanticalObject) dbEnum.nextElement();
                MetaField[] fields = dbo.getMetaClass().getAllMetaFields();
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].getJField().getType() == String.class) {
                        if (!(findOption.selectedInScope(fields[j])))
                            continue;
                    } else
                        continue;
                    String text = (String) dbo.get(fields[j]);
                    if (findOption.match(text)) {
                        foundObjects.add(new FoundObject(dbo, fields[j], dbo.getSemanticalName(DbObject.LONG_FORM)));
                        break;
                    }
                }
                // search in UDF
                if (findOption.browseUDF()) {
                    DbUDF[] udfs = DbUDF.getUDF(dbo.getProject(), dbo.getMetaClass());
                    for (int k = 0; k < udfs.length; k++) {
                        if (udfs[k].getValueType().equals(
                                UDFValueType.getInstance(UDFValueType.STRING))) {
                            if (findOption.match((String) dbo.get(udfs[k]))) {
                                foundObjects.add(new FoundObject(dbo, DbObject.fUdfValues, dbo.getSemanticalName(DbObject.LONG_FORM)));
                                break;
                            }
                        }
                    }
                }
            }
            dbEnum.close();
            // search in diagram
            if (findOption.selectedInScope(DbGraphic.fDiagramName)) {
                if (findOption.getRecursive())
                    dbEnum = rootObjects[i].componentTree(DbGraphic.fDiagramName.getMetaClass());
                else
                    dbEnum = rootObjects[i].getComponents().elements(
                            DbGraphic.fDiagramName.getMetaClass());
                while (dbEnum.hasMoreElements()) {
                    DbObject dbo = dbEnum.nextElement();
                    String text = (String) dbo.get(DbGraphic.fDiagramName);
                    if (findOption.match(text)) {
                        foundObjects.add(new FoundObject(dbo, DbGraphic.fDiagramName, text));
                        break;
                    }
                }
                dbEnum.close();
            }
        }
        if (isNode){
        	for (int i = 0; i < rootObjects.length; i++) {
        		DbSemanticalObject dbo = (DbSemanticalObject) rootObjects[i];
                MetaField[] fields = dbo.getMetaClass().getAllMetaFields();
                for (int j = 0; j < fields.length; j++) {
                    if (fields[j].getJField().getType() == String.class) {
                        if (!(findOption.selectedInScope(fields[j])))
                            continue;
                    } else
                        continue;
                    String text = (String) dbo.get(fields[j]);
                    if (findOption.match(text)) {
                        foundObjects.add(new FoundObject(dbo, fields[j], dbo.getSemanticalName(DbObject.LONG_FORM)));
                        break;
                    }
                } 
        	}      	
        }       
        foundObjects.sort();
    }
    public class FoundObject implements Comparable {
        public DbObject dbo;
        public MetaField mf;
        public String name;

        public FoundObject(DbObject dbo, MetaField mf, String name) {
            this.dbo = dbo;
            this.mf = mf;
            this.name = (name == null ? "" : name); // never null
        }

        public final int compareTo(Object obj) {
            return CollationComparator.getDefaultCollator().compare(name, ((FoundObject) obj).name);
        }
    }

    public final void addFindSessionListener(FindSessionListener listener) {
        if (!findListeners.contains(listener))
            findListeners.add(listener);
    }

    public final void removeFindSessionListener(FindSessionListener listener) {
        findListeners.remove(listener);
    }

    private void fireFindSessionListeners() {
        for (int i = findListeners.size(); --i >= 0;)
            ((FindSessionListener) findListeners.get(i)).indexChanged(this);
    }
}
