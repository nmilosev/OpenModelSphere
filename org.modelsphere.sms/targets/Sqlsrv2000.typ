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

NAME=SQL Server
VERSION=2000, 8.0
TARGET-ID=58
ROOT-ID=19

// type, logical type (optional)
TYPES
{
BINARY,				BINARY
UNIQUEIDENTIFIER,		BINARY
BIT,				BIT
TINYINT,			BYTE
CHAR,				CHARACTER
CHARACTER,			CHARACTER
DATETIME,			DATETIME
DEC,				DECIMAL
DECIMAL,			DECIMAL
FLOAT,				FLOAT
DOUBLE PRECISION,		FLOAT
INT,				INTEGER
INTEGER,			INTEGER
IMAGE,				LARGE VARBINARY
BIGINT,				LONG INTEGER
MONEY,				MONEY
NCHAR,				NCHAR
NATIONAL CHAR,			NCHAR
NATIONAL CHARACTER,		NCHAR
NUMERIC,			NUMERIC
NVARCHAR,			NVARCHAR
NATIONAL CHARACTER VARYING,	NVARCHAR
NATIONAL CHAR VARYING,		NVARCHAR
NATIONAL TEXT,			NVARCHAR
NTEXT,				NVARCHAR
REAL,				REAL
SMALLDATETIME,			SMALL DATE TIME
SMALLINT,			SMALL INTEGER
SMALLMONEY,			SMALL MONEY
TEXT,				TEXT
TIMESTAMP,			TIMESTAMP
BINARY VARYING,			VARIABLE BINARY
VARBINARY,			VARIABLE BINARY
VARCHAR,			VARIABLE CHARACTER
CHARACTER VARYING,		VARIABLE CHARACTER
CHARVARYING,			VARIABLE CHARACTER
SQL_VARIANT,
}

ALIASES
{
  CHAR, CHARACTER
  DECIMAL, DEC
  FLOAT, DOUBLE PRECISION
  INT, INTEGER
  NCHAR, NATIONAL CHARACTER, NATIONAL CHAR
  NTEXT, NATIONAL TEXT
  NUMERIC, DECIMAL
  NVARCHAR, NATIONAL CHAR VARYING, NATIONAL CHARACTER VARYING
  VARBINARY, BINARY VARYING
  VARCHAR, CHAR VARYING, CHARACTER VARYING
}



