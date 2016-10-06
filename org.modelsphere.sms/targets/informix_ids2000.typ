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

NAME=Informix Dynamic Server
VERSION=9.2
TARGET-ID=18
ROOT-ID=5

// type, logical type (optional)
TYPES
{
BYTE,			BINARY
BLOB,			BLOB
BOOLEAN,		BOOLEAN
CHAR,			CHARACTER
CHARACTER,		CHARACTER
CLOB,			CLOB
DATE,			DATE
DATETIME,		DATE TIME
DEC,			DECIMAL
DECIMAL,		DECIMAL
DOUBLE PRECISION,	DOUBLE PRECISION
FLOAT,			FLOAT
INT,			INTEGER
INTEGER,		INTEGER
INTERVAL,		INTERVAL
INT8,			LONG INTEGER
LVARCHAR,		LONG VARCHAR
MONEY,			MONEY
NCHAR,			NCHAR
NUMERIC,		NUMERIC
NVARCHAR,		NVARCHAR
REAL,			REAL
SERIAL,			ROWID
SERIAL8,			ROWID
SMALLFLOAT,		SMALL FLOAT
SMALLINT,		SMALL INTEGER
TEXT,			TEXT
VARCHAR,		VARIABLE CHARACTER
CHARACTER VARYING,	VARIABLE CHARACTER
}

ALIASES
{
  DEC, DECIMAL, NUMERIC
  INT, INTEGER
  FLOAT, DOUBLE PRECISION
  SMALLFLOAT, REAL
  CHAR, CHARACTER
  VARCHAR, CHARACTER VARYING
}

