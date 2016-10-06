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

NAME=Oracle
VERSION=7.0
TARGET-ID=28
ROOT-ID=11

// type, logical type (optional)
TYPES
{
RAW,			BINARY
CHAR,			CHARACTER
CHARACTER,		CHARACTER
DATE,			DATE
DEC,			DECIMAL
DECIMAL,		DECIMAL
DOUBLE PRECISION,	DOUBLE PRECISION
FLOAT,			FLOAT
INT,			INTEGER
INTEGER,		INTEGER
LONG RAW,		LARGE VARBINARY
LONG,			LONG VARCHAR
LONG VARCHAR,		LONG VARCHAR
NUMERIC,		NUMERIC
NUMBER,		NUMERIC
REAL,			REAL
ROWID,			ROWID
SMALLINT,		SMALL INTEGER
VARCHAR2,		VARIABLE CHARACTER
CHAR VARYING,		VARIABLE CHARACTER
CHARACTER VARYING,	VARIABLE CHARACTER
VARCHAR,		VARIABLE CHARACTER
}

ALIASES
{
CHAR, CHARACTER
LONG, LONG VARCHAR
NUMBER, NUMERIC, DECIMAL, DEC, REAL
VARCHAR, CHARACTER VARYING,  CHAR VARYING
}

