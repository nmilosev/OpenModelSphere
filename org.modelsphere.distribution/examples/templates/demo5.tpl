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

  //Description: Introduces PREF, SUF and SEP modifiers.      
		

entryPoint	TEMPL	"Some subrules and their content:$m+4;$n;$subrules;$m-4;$n;End.$n;"
		FILE = "demo5.txt"
		EXTERN;

subrules	GROUP	(subrule1, subrule2, subrule3)
		SEP = ",$eol;"
		SUF = ".$eol;";

eol		TEMPL	"$n;"; 

subrule1	TEMPL	"$content; 'subrule1' is: $subrule1Content;";

subrule1Content	TEMPL	"first subrule";

subrule2	TEMPL	"$content; 'subrule2' is: $subrule2Content;";

subrule2Content	TEMPL	"second subrule";

subrule3	TEMPL	"$content; 'subrule3' is: $subrule3Content;";

subrule3Content	TEMPL	"third subrule";

content		TEMPL	"- the content of";






