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

NAME=DB2 MVS
VERSION=4.1
TARGET-ID=12
ROOT-ID=4

// type, logical type (optional)
TYPES
{
GRAPHIC,		BINARY
CHAR,			CHARACTER
CHAR VARYING,		CHARACTER
CHARACTER,		CHARACTER
CHARACTER VARYING,	CHARACTER
DATE,			DATE
DEC,			DECIMAL
DECIMAL,		DECIMAL
DOUBLE,		DOUBLE PRECISION
DOUBLE PRECISION,	DOUBLE PRECISION
FLOAT,			FLOAT
INT,			INTEGER
INTEGER,		INTEGER
LONG VARGRAPHIC,	LARGE VARBINARY
LONG VARCHAR,		LONG VARCHAR
NUMERIC,		NUMERIC
REAL,			REAL
SMALLINT,		SMALL INTEGER
TIME,			TIME
TIMESTAMP,		TIMESTAMP
VARCHAR,		VARIABLE CHARACTER
VARGRAPHIC,		VARIABLE BINARY
}

ALIASES
{
CHAR, CHARACTER
DEC, DECIMAL
DOUBLE, DOUBLE PRECISION
INT, INTEGER
}
