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

package org.modelsphere.jack.srtool.plugins.xml;

//This class is responsible of the mapping between XMI tags and
//the concepts defined in our .srx stream format.
public final class XmiMapping {
    // SRX ID NUMBERS
    static final int DIAGRAM_ID = 0;
    static final int DATA_TYPE_ID = 1;
    static final int CLASS_ID = 2;
    static final int ATTRIBUTE_ID = 3;
    static final int GRAPH_TABLE_ID = 4;
    static final int PK_ID = 5;
    static final int UK_ID = 6;
    static final int INDEX_ID = 7;
    static final int ASSOCIATION_ID = 8;
    static final int CONNECTOR_ID = 9;
    static final int UNKNOWN = -1;

    static final String CHARACTERS = "characters"; // NOT LOCALIZABLE, xmi keyword
    static final String REFERENCE = "reference"; // NOT LOCALIZABLE, xmi keyword

    // THE ORDER OF THE MAPPING MUST RESPECT THE ORDER OF THE SRX ID NUMBERS
    private static String[][][] mapping = new String[][][] {
    // Map XMI diagrams to SRX diagrams
            new String[][] { new String[] { "Diagram", "DIAGRAM" }, // NOT LOCALIZABLE, keyword
                    new String[] { "", null }, // no identifier
                    new String[] { "", null }, // no composite identifier
                    new String[] { "nb_pages_x", "NB_PAGE_X" }, // NOT LOCALIZABLE, keyword
                    new String[] { "nb_pages_y", "NB_PAGE_Y" }, // NOT LOCALIZABLE, keyword
                    new String[] { "page_height", "PAGE_HEIGHT" },// NOT LOCALIZABLE, keyword
                    new String[] { "page_width", "PAGE_WIDTH" } // NOT LOCALIZABLE, keyword
            },
            // Map XMI data types to SRX domains
            new String[][] { new String[] { "DataType", "DOMAIN" }, // NOT LOCALIZABLE, keyword
                    new String[] { "name", "NAME" } // NOT LOCALIZABLE, identifier keyword
            },
            // Map XMI classes to SRX tables
            new String[][] { new String[] { "Class", "TABLE" }, // NOT LOCALIZABLE, keyword
                    new String[] { "name", "NAME" } // NOT LOCALIZABLE, identifier keyword
            },
            // Map XMI attributes to SRX columns
            new String[][] { new String[] { "Attribute", "COLUMN" }, // NOT LOCALIZABLE
                    new String[] { "name", "NAME" }, // NOT LOCALIZABLE, identifier keyword
                    new String[] { "", "TABLE_NAME" }, // NOT LOCALIZABLE, composite identifier
                    new String[] { "type", "DOMAIN_NAME", REFERENCE } // NOT LOCALIZABLE, keyword
            },
            // Map XMI graphical tables to SRX ones
            new String[][] { new String[] { "GraphicalTable", "GRAPHICAL_TABLE" }, // NOT LOCALIZABLE
                    new String[] { "", null }, // NOT LOCALIZABLE, no identifier
                    new String[] { "", "TABLE_NAME" }, // NOT LOCALIZABLE composite identifier
                    new String[] { "duplicate", "DUPLICATE" }, // NOT LOCALIZABLE
                    new String[] { "position", "POSITION" } // NOT LOCALIZABLE
            },
            // Map XMI pks to SRX pk_uk_cst
            new String[][] { new String[] { "PrimaryKey", "PK_UK_CST" }, // NOT LOCALIZABLE
                    new String[] { "CstName", "cst_name" }, // NOT LOCALIZABLE identifier
                    new String[] { "", "TABLE_NAME" }, // NOT LOCALIZABLE composite identifier
                    new String[] { "Column", "COLUMN", REFERENCE }, // NOT LOCALIZABLE
            },
            // Map XMI uks to SRX pk_uk_cst
            new String[][] { new String[] { "UniqueKey", "PK_UK_CST" }, // NOT LOCALIZABLE
                    new String[] { "CstName", "cst_name" }, // NOT LOCALIZABLE identifier
                    new String[] { "", "TABLE_NAME" }, // NOT LOCALIZABLE composite identifier
                    new String[] { "Column", "COLUMN", REFERENCE }, // NOT LOCALIZABLE
            },
            /*
             * //PATCH: IGNORE FOREIGN KEYS //Map XMI fks to SRX ones new String[][] { new String[]
             * {"ForeignKey", "FK_CST"}, //NOT LOCALIZABLE new String[] {"", null}, //NOT
             * LOCALIZABLE no identifier new String[] {"", "TABLE_NAME"}, //NOT LOCALIZABLE
             * composite identifier new String[] {"Column", "COLUMN", REFERENCE}, //NOT LOCALIZABLE
             * new String[] {"PrimaryKey", "PRIMARY_KEY", REFERENCE}, //NOT LOCALIZABLE },
             */
            /*
             * //Map XMI fk cols to SRX ones new String[][] { new String[] {"ForeignKeyColumn",
             * "CST_COL"}, //NOT LOCALIZABLE //new String[] {"", null}, //NOT LOCALIZABLE no
             * identifier //new String[] {"", "TABLE_NAME"}, //NOT LOCALIZABLE composite identifier
             * //new String[] {"Column", "COLUMN", REFERENCE}, //NOT LOCALIZABLE //new String[]
             * {"PrimaryKey", "PRIMARY_KEY", REFERENCE}, //NOT LOCALIZABLE },
             */
            // /* //PATCH: IGNORE FOREIGN KEYS
            // Map XMI indexes to SRX ones
            new String[][] { new String[] { "Index", "INDEX" }, // NOT LOCALIZABLE
                    new String[] { "", null }, // NOT LOCALIZABLE no identifier
                    new String[] { "", "TABLE_NAME" }, // NOT LOCALIZABLE composite identifier
                    new String[] { "Column", "COLUMN", REFERENCE }, // NOT LOCALIZABLE
            },
            // Map XMI associations to SRX connectors
            new String[][] { new String[] { "Association", "CONNECTOR" }, // NOT LOCALIZABLE
                    new String[] { "name", "NAME" }, // NOT LOCALIZABLE identifier
                    new String[] { "", null }, // NOT LOCALIZABLE no composite identifier
            },
            // Map XMI connections to SRX directions
            new String[][] { new String[] { "AssociationEnd", "DIRECTION" }, // NOT LOCALIZABLE
                    new String[] { "name", "NAME" }, // NOT LOCALIZABLE identifier
                    new String[] { "", "CONN_NAME" }, // NOT LOCALIZABLE composite identifier
                    new String[] { "multiplicity", "MULTIPLICITY" }, // NOT LOCALIZABLE requires a special processing
                    new String[] { "origin", "ORITAB_NAME", REFERENCE }, // NOT LOCALIZABLE requires a special processing
                    new String[] { "type", "DESTTAB_NAME", REFERENCE }, // NOT LOCALIZABLE requires a special processing
            }
    // */
    };

    // get srx id, if any
    static int getSrxId(String tag) {
        int srxId = UNKNOWN;

        // for each concept
        for (int i = 0; i < mapping.length; i++) {
            String[][] concept = mapping[i];

            String[] nameMapping = concept[0];
            if (tag.equals(nameMapping[0])) {
                srxId = i;
                break;
            }
        } // end for

        return srxId;
    }

    static String getMappingName(int srxId) {
        String mappedName;

        String[][] concept = mapping[srxId];
        String[] occurrenceNameMapping = concept[0];
        mappedName = occurrenceNameMapping[1];

        return mappedName;
    }

    static String getXmiName(int srxId) {
        String xmiName;

        String[][] concept = mapping[srxId];
        String[] nameMapping = concept[1];
        xmiName = nameMapping[0];

        return xmiName;
    }

    static String getSrxName(int srxId) {
        String srxName;

        String[][] concept = mapping[srxId];
        String[] nameMapping = concept[1];
        srxName = nameMapping[1];

        return srxName;
    }

    // may be null
    static String getComposite(int srxId) {
        String composite = null;

        String[][] concept = mapping[srxId];
        if (concept.length >= 3) {
            String[] compositeMapping = concept[2];
            composite = compositeMapping[1];
        }

        return composite;
    }

    static int getAttrId(int srxId, String tag) {
        int attrId = UNKNOWN;

        String[][] concept = mapping[srxId];
        for (int i = 2; i < concept.length; i++) {
            String[] compositeMapping = concept[i];
            if (compositeMapping[0].equals(tag)) {
                attrId = i;
                break;
            } // end if
        } // end for

        return attrId;
    }

    static String getMappedAttr(int srxId, int attrId) {
        String[][] concept = mapping[srxId];
        String[] compositeMapping = concept[attrId];
        return compositeMapping[1];
    }

} // end XmiMapping
