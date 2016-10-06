//*************************************************************************
// Copyright (C) 2008 Grandite
//
// This file is part of Open ModelSphere.
// 
// Open ModelSphere is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 3 of the License, or
// (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
// or see http://www.gnu.org/licenses/.
// 
// You can reach Grandite at: 
// 
// 20-1220 Lebourgneuf Blvd.
// Quebec, QC
// Canada  G2K 2G4
// 
// or
// 
// open-modelsphere@grandite.com
// 
//************************************************************************* 
// 	DO NOT MODIFY THIS FILE
//
//	Built-in Types

NAME=HSQLDB
VERSION=1.8
TARGET-ID=71
ROOT-ID=29

// type, logical type (optional)
TYPES
{
BIGINT, 	  		BIGINT
BIT,				BIT
BOOLEAN,			BOOLEAN
CHAR,				CHARACTER
CHARACTER,			CHARACTER
DATE,				DATE
DECIMAL,			NUMERIC
DOUBLE,				DOUBLE 
INTEGER,			INTEGER
SMALLINT,			INTEGER
TINYINT,			TINYINT
NUMERIC,			NUMERIC
REAL,				REAL
TIME,				TIME
TIMESTAMP,			TIMESTAMP
VARCHAR,			VARCHAR
VARCHAR_IGNORECASE,		VARCHAR_IGNORECASE
LONGVARCHAR,			LONGVARCHAR
BINARY,				BINARY
VARBINARY,			VARBINARY
LONGVARBINARY,			LONGVARBINARY


}

ALIASES
{
CHAR, CHARACTER
INT, INTEGER
CHAR VARYING, CHARACTER VARYING
}
