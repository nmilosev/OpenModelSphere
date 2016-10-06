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

NAME=PostgreSQL
VERSION=8.0
TARGET-ID=68
ROOT-ID=28

// type, logical type (optional)
TYPES
{
BIGINT, 	  		INTEGER
BIGSERIAL,			INTEGER
BIT,				BIT
BIT VARYING,		BIT
BOOLEAN,			BOOLEAN
CHAR,				CHARACTER
CHARACTER VARYING ,	VARIABLE CHARACTER	
CHARACTER,			CHARACTER
CIDR,				VARIABLE CHARACTER
DATE,				DATE
DOUBLE PRECISION,		DOUBLE PRECISION
INET,				VARIABLE CHARACTER
INTEGER,			INTEGER
INTERVAL,			INTERVAL
INTERVAL DAY,		INTERVAL
INTERVAL YEAR,		INTERVAL
MACADDR,			VARIABLE CHARACTER
MONEY,			DOUBLE PRECISION
NUMERIC,			NUMERIC
REAL,				REAL
SERIAL,			INTEGER
SMALLINT,			SMALL INTEGER
TEXT, 			VARIABLE CHARACTER
TIME,				TIME
TIMESTAMP,			TIMESTAMP
}

ALIASES
{
CHAR, CHARACTER
DEC, DECIMAL
INT, INTEGER
CHAR VARYING, CHARACTER VARYING
INT8, BIGINT
SERIAL8, BIGSERIAL
VARBIT,BIT VARYING
FLOAT8, DOUBLE PRECISION
INT4, INTEGER
REAL, FLOAT4
SMALLINT, INT2
SERIAL, SERIAL4
}
