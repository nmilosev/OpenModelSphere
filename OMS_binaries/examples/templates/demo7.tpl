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

  //Description: Introduces CONN in order to navigate thru a model.     	

entryPoint		TEMPL  "$datamodel;$n;"
			FILE = "demo7.txt"
			EXTERN;

datamodel		TEMPL	"-Current Model : $objectName;$m+4;$n;$datamodelTables;$m-4;$n;";
		
datamodelTables		CONN    DatamodelTables,
			CHILD = table
			SUF   = "$eol;"
			NULL  = "-Does not contain any table.$eol;";

table			TEMPL	"-Table : $objectName;$m+4;$n;$tableColumns;$m-4;$n;";


tableColumns		CONN	TableColumns,
			CHILD = column
			NULL  = "Does not contain any column.";

column			TEMPL	"-Column: $objectName;; is null possible: $columnIsnullpossible;$n;";

columnIsnullpossible	ATTR	ColumnIsnullpossible;
 

eol			TEMPL	"$n;"; 

objectName		ATTR    ObjectName
			NULL =  "<empty>";









