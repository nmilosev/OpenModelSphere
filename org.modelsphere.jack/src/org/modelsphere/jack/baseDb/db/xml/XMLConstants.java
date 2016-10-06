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

package org.modelsphere.jack.baseDb.db.xml;

public interface XMLConstants {
    public static final String EOL = System.getProperty("line.separator"); // NOT LOCALIZABLE, property name
    public static final String INDENT = "    "; // NOT LOCALIZABLE
    public static final String QUOTE = "\""; // text quote

    public static final String TAG_BEGIN_PREFIX = "<";
    public static final String TAG_BEGIN_SUFFIX = ">";
    public static final String TAG_END_PREFIX = "</";
    public static final String TAG_END_SUFFIX = ">";
    public static final String TAG_EMPTY_PREFIX = "<"; // flag begin
    public static final String TAG_EMPTY_SUFFIX = "/>"; // flag end
    public static final String TAG_COMPONENT = "cmp"; // NOT LOCALIZABLE, tag name
    public static final String TAG_PROJECT = "project"; // NOT LOCALIZABLE, tag name
    public static final String TAG_ID_POOL = "idpool"; // NOT LOCALIZABLE, tag name
    public static final String TAG_ID = "id"; // NOT LOCALIZABLE, tag name
    public static final String TAG_HEADER = "header"; // NOT LOCALIZABLE, tag name
    public static final String TAG_CONVERTER = "converter"; // NOT LOCALIZABLE, tag name
    public static final String TAG_VERSION = "version"; // NOT LOCALIZABLE, tag name

    public static final String ELEMENT_BEGIN_PREFIX = "<";
    public static final String ELEMENT_BEGIN_SUFFIX = ">";
    public static final String ELEMENT_END_PREFIX = "</";
    public static final String ELEMENT_END_SUFFIX = ">";
    public static final String ELEMENT_EMPTY_PREFIX = "<"; // flag begin
    public static final String ELEMENT_EMPTY_SUFFIX = "/>"; // flag end

    public static final String ATTR_VALUE_PREFIX = "\"";
    public static final String ATTR_VALUE_SUFFIX = "\"";
    public static final String ATTR_NAME_PREFIX = "";
    public static final String ATTR_NAME_SUFFIX = "";
    public static final String ATTR_NAME_VALUE_SEPARATOR = "=";
    public static final String ATTR_NAME_VALUE_TERMINATOR = ";";

    public static final String CHARACTER_DATA_PREFIX = "<![CDATA[";
    public static final String CHARACTER_DATA_SUFFIX = "]]>";

    public static final String ID_ATTRIBUTE_NAME = "id";
    public static final String IDREFS_PATTERN = "{0}:{1}"; // where {0} is the
    // class name and
    // {1} the unique id
    // within the
    // namespace
    public static final String IDREF_PATTERN = "#{0}:{1}"; // where {0} is the
    // class name and
    // {1} the unique id
    // within the
    // namespace

    // This prefix should be removed from all id String
    // public static final String PACKAGE_NAME_PREFIX = "org.modelsphere."; //
    // NOT LOCALIZABLE

    public static final String ID_SHORT = "short"; // NOT LOCALIZABLE
    public static final String ID_LONG = "long"; // NOT LOCALIZABLE
}
