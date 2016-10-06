/****************************************************************************

	Grandite.

	MySQL 5.0 bridge
	based on the greatest common denominator of major DBMSs
	there is no standard defined for any CREATE statements

	15 février 2008
 ****************************************************************************/

//
// Section defining the variables
//


NEW EXTERN(DISPLAY="Generate DROP statements", FR DISPLAY="Clauses DROP générées", ORDER = 10)
  BOOLEAN generateDropStatements = TRUE;

NEW EXTERN(DISPLAY="Do not Generate Nameless Tables", FR DISPLAY="Ne pas générer les tables anonymes", ORDER = 15)
  BOOLEAN doNotGenerateNamelessTables = TRUE;

NEW EXTERN(DISPLAY="Generate PK generated as constraints", FR DISPLAY="Clés primaires générées comme contraintes", ORDER = 20)
  BOOLEAN pkAsConstraints = TRUE;

NEW EXTERN(DISPLAY="Multi-level domains", FR DISPLAY="Domain multi-niveaux", ORDER = 25)
  BOOLEAN multiLevelDomains = TRUE;

NEW EXTERN(DISPLAY="ASC index option always generated", FR DISPLAY="Option d'index ASC générée", ORDER = 30)
  BOOLEAN ascOptionGenerated = TRUE;

NEW EXTERN(DISPLAY="NULL option always generated", FR DISPLAY="Option NULL toujours générée", ORDER = 35)
  BOOLEAN nullOptionGenerated = TRUE;

NEW EXTERN(DISPLAY="Enable MySQL foreign key constraints", FR DISPLAY="Activer les contraintes de clés étrangères MySQL", ORDER = 40)
  BOOLEAN enableMySQLFKS = FALSE;


NEW EXTERN(DISPLAY="Generate comments in the SQL script", FR DISPLAY="Générer les commentaires dans le script SQL", ORDER = 45)
  BOOLEAN generateComments = FALSE;

NEW EXTERN(DISPLAY="Generate in HTML format", FR DISPLAY="Générer en format HTML", ORDER = 50)
  BOOLEAN html = FALSE;



//   All those entry points are activated and available
//   returns "" if you do not want an error message to be displayed in the SQL
//   preview tab.
//

dataModelEntryPoint         TEMPL	"$begin;$modelContent;$end;"
			    EXTERN(DISPLAY="Generate Data Model", FR DISPLAY="Générer le modèle de données", ORDER = 40);

tableEntryPoint             TEMPL	"$begin;$createtableClause;$end;";
viewEntryPoint              TEMPL	"$begin;$createviewClause;$end;";
columnEntryPoint            TEMPL   "$begin;$columnDecl;$end;";
pkEntryPoint                TEMPL	"$begin;$createPKClause;$end;";
ukEntryPoint                TEMPL	"$begin;$createPKClause;$end;";
fkEntryPoint                TEMPL	"$begin;$fkeyDecl;$end;";
ckEntryPoint                TEMPL	"$begin;$checkDecl;$end;";
indexEntryPoint             TEMPL	"$begin;$createindexClause;$end;";
triggerEntryPoint           TEMPL	"$begin;$createtriggerClause;$end;";
operationLibraryEntryPoint  TEMPL	"$begin;$getProcedures;$end;";
procedureEntryPoint         TEMPL	"$begin;$createProcedureDecl;$end;";
domainModelEntryPoint       TEMPL	"";
domainEntryPoint            TEMPL	"";
userEntryPoint              TEMPL	"";
databaseEntryPoint  	    TEMPL	"$begin;$databaseSchema;$eol;$eol;$allProcedures;$end;";
dropTableEntryPoint	    	TEMPL	"$begin;$dropTableClause;$end;";





/**********************************************************************************
    HTML
***********************************************************************************/

begin	    	COND BOOLEAN(html)
            	TRUE = beginTempl;

beginTempl 	TEMPL	    "<body bgcolor='#ffffff'><pre>";

end	        COND BOOLEAN(html)
                TRUE = endTempl;

endTempl        TEMPL	    "</pre></body>";

kw	        COND BOOLEAN(html)
                TRUE = kwTempl;

kwTempl     TEMPL	    "<font color=blue><b>";

ekw		    	COND BOOLEAN(html)
            TRUE = ekwTempl;

ekwTempl  	TEMPL   	"</b></font>";

begError	    COND BOOLEAN(html)
            TRUE = errorTempl;

errorTempl  TEMPL     "<font color=red>";

endError	  COND BOOLEAN(html)
            TRUE = endErrorTempl;

endErrorTempl TEMPL "</font>";

comment		  COND BOOLEAN(html)
            TRUE = commentTempl;

commentTempl TEMPL	    "<font color=green>";

endComment	COND BOOLEAN(html)
            TRUE = endCommentTempl;

endCommentTempl	TEMPL     "</font>";

/**********************************************************************************
    GENERAL TEMPLATES
***********************************************************************************/

logicalName			ATTR	ObjectName;

name				ATTR	ObjectPhysicalname
				    NULL =  "$logicalName;";

namePhys			ATTR	ObjectPhysicalname;

error				TEMPL  "$begError;???$endError;";

eol					TEMPL 	"$n;" ;

tab					TEMPL	"	";

projName			ATTR	ProjectName;

tableName			TEMPL	"$name;";

databaseSchema			CONN	DatabaseSchema,
                   	 	CHILD = modelContent;

modelContent		        TEMPL	"$modelContentComment;"
					"$eol;$eol;$allTables;"
					"$eol;$eol;$allViews;"
					"$eol;$eol;$allIndexes;"
					"$eol;$eol;$allPUKs;"
					"$eol;$eol;$allFKs;";

allTables			CONN	DatamodelTables,
					CHILD = createtableClause
					SEP =   "$eol;$eol;"
					SUF =   "$eol;$eol;";

allIndexes			CONN	DatamodelTables,
					CHILD = tableIndexes
					SEP =   "$eol;$eol;"
					SUF =   "$eol;$eol;";

allPUKs				CONN	DatamodelTables,
					CHILD = tableConstrs;

allFKs				CONN	DatamodelTables,
					CHILD = tableFKs;

allViews			CONN	DatamodelViews,
					CHILD =	createviewClause
					SEP =   "$eol;$eol;"
					SUF =   "$eol;$eol;";

allProcedures		CONN	DatabaseOperationLibrary,
					CHILD = getProcedures;

getProcedures		CONN	OperationlibraryProcedures,
					CHILD = createProcedureDecl
					SEP =   "$eol;$eol;"
					SUF =   "$eol;$eol;";

allTriggers			CONN	DatamodelTables,
					CHILD = tableTriggers
					SEP =   "$eol;$eol;"
					SUF =   "$eol;go$eol;";

notEmpty                  	TEMPL     "-";

//***********************************************************
// Can contain special HTML characters such as '<', '>', etc.
//***********************************************************

description                COND    BOOLEAN(html)
                           TRUE  = descriptionHtml
                           FALSE = descriptionAscii;

descriptionAscii           ATTR    ObjectDescription;

descriptionHtml            PLUGIN FUNCTION  UtilSGMLConverter
                           CHILD  = descriptionAscii;

viewSelectStatement        COND    BOOLEAN(html)
                           TRUE  = viewSelectStatementHtml
                           FALSE = viewSelectStatementAscii;

viewSelectStatementAscii   ATTR    ViewSelectionrule;

viewSelectStatementHtml    PLUGIN FUNCTION UtilSGMLConverter
                           CHILD  = viewSelectStatementAscii;


checkCondition             COND    BOOLEAN(html)
                           TRUE  = checkConditionHtml
                           FALSE = checkConditionAscii;

checkConditionAscii        ATTR    CheckCondition;

checkConditionHtml         PLUGIN FUNCTION  UtilSGMLConverter
                           CHILD  = checkConditionAscii;

triggerBodyAttr            COND    BOOLEAN(html)
                           TRUE  = triggerBodyHtml
                           FALSE = triggerBodyAscii;

triggerBodyAscii           ATTR    TriggerBody;

triggerBodyHtml            PLUGIN FUNCTION UtilSGMLConverter
                           CHILD = triggerBodyAscii;

procedureBody              COND    BOOLEAN(html)
                           TRUE  = procedureBodyHtml
                           FALSE = procedureBodyAscii;

procedureBodyAscii         ATTR    ProcedureBody;

procedureBodyHtml          PLUGIN FUNCTION  UtilSGMLConverter
                           CHILD  = procedureBodyAscii;

columnDefaultAttr          COND    BOOLEAN(html)
                           TRUE  = columnDefaultHtml
                           FALSE = columnDefaultAscii;

columnDefaultAscii         ATTR    ColumnDefault;

columnDefaultHtml          PLUGIN FUNCTION  UtilSGMLConverter
                           CHILD  = columnDefaultAscii;

/**********************************************************************************
    CREATE TABLE
***********************************************************************************/

createtableClause       COND BOOLEAN(doNotGenerateNamelessTables)
                        TRUE = createtableClause2
                        FALSE = createtableClause3;

createtableClause2      ATTR ObjectPhysicalname,
                        DOM = objectPhysicalnameDOM;

objectPhysicalnameDOM   CDOM ("", "$doNotGenerateNamelessTableComment;")
		   	     (DEFAULT, "$createtableClause3;");

createtableClause3	TEMPL	"$tableComment;"
                            "$createTableClause;";

dropTableClause         COND BOOLEAN(generateDropStatements)
                        TRUE = generateDropTableClause;

generateDropTableClause TEMPL   "$kw;DROP TABLE$ekw; $name;$kw;$ekw;;$eol;$eol;";

createTableClause       TEMPL	"$kw;CREATE TABLE$ekw; $name; $tableContent;";


tableContent		TEMPL	"$eol;($m+4;$eol;$tableColumns;$createConstr;$m-4;$eol;);$eol;$eol;$eol;";

createConstr            COND BOOLEAN(pkAsConstraints)
                        FALSE = createInnerConstr;

createInnerConstr	GROUP	(tablePukeys,/*tableFKs,*/tableChecks)
			SEP =	",";

tableColumns		CONN	TableColumns,
			CHILD = columnDecl
			SEP =   ",$eol;"
                        NULL =  "$noColumnComment;";


/**********************************************************************************
    CREATE COLUMN
***********************************************************************************/

columnDecl		TEMPL	"$name; $columnTypeDecl;$columnDefault;$columnIsNullPossible;$MySQL_Auto_Inc_TPL;";


MySQL_Auto_Inc_TPL	TEMPL 	"$MySQL_Auto_Inc;"
				DOM=MySQL_Auto_IncDOM;


MySQL_Auto_Inc		TEMPL "$MySQL_Auto_IncPDE;";
			

MySQL_Auto_IncPDE	PLUGIN FUNCTION	UserDefinedProperty
				CHILD = "AUTO_INCREMENT";


MySQL_Auto_IncDOM	CDOM("y"," AUTO_INCREMENT")
			    (DEFAULT,"");


columnTypeDecl			TEMPL	"$columnType;"
				DOM   = columnTypeDOM;


columnTypeDOM       		CDOM    ("ENUM",  "$kw;$columnType;$columnTypeParam;")
                                   	(DEFAULT, "$columnType;$columnLenDec;");

columnDefault		        TEMPL "$columnDefaultAttr;"
			        PREF =  " $kw;DEFAULT$ekw; ";

columnIsNullPossible	        ATTR	ColumnIsnullpossible,
				DOM =   isNullPossibleDomain;

isNullPossibleDomain	        CDOM	("true", "$nullOption;")
				        ("false", " $kw;NOT NULL$ekw;");

nullOption    	                COND BOOLEAN(nullOptionGenerated)
                                TRUE = nullOptionAlwaysGenerated;

nullOptionAlwaysGenerated       TEMPL " $kw;NULL$ekw;";

//
// Column type
//

columnType      	        CONN	ColumnDomain,
			        CHILD = domain
			        NULL  = "$columnBaseType;";

columnBaseType      	        CONN	ColumnType,
			        CHILD = baseType
			        NULL  = "$error;";

baseType			TEMPL	"$name;";

domain                          COND BOOLEAN(multiLevelDomains)
                                TRUE = domainSource
                                FALSE = name;
//
// Column type param
//
			
columnTypeParam      		PLUGIN FUNCTION	UserDefinedProperty
				CHILD = "VAL_ENUM";

//Iterate until built-in type is found

domainSource                    GROUP(domainSourceBuiltin, domainSourceDomain);

domainSourceBuiltin             CONN DomainSourcebuiltintype,
                                CHILD = baseType;

domainSourceDomain              CONN DomainSourcedomain,
                                CHILD = domainSource;

//Get type's length & precision; if type has no length and precision,
//then iterate to source type until a length and precision is found

columnLenDec                    GROUP   (columnLen, columnDecimal)
                                PREF = 	"("
                                SEP = 	","
                                SUF =	")"
                                NULL =  "$columnDomain;";

columnLen			ATTR	ColumnLength;

columnDecimal			ATTR	ColumnDecimal;

columnDomain     	        CONN	ColumnDomain,
			        CHILD = sourceDomain;

sourceDomain                    GROUP   (domainLen, domainDecimal)
                                PREF = 	"("
                                SEP = 	","
                                SUF =	")"
                                NULL =  "$domainSourcedomain;";

domainSourcedomain              CONN    DomainSourcedomain,
                                CHILD = sourceDomain;

domainLen			ATTR	DomainLength;

domainDecimal			ATTR	DomainNbdecimals;


/**********************************************************************************
    PRIMARY/UNIQUE CONSTRAINTS (inner constraints)
***********************************************************************************/

tablePukeys			CONN	TablePrimaryuniquekeys,
				CHILD = pukeyDecl
				PREF =  ",$eol;"
				SEP =   ",$eol;";

pukeyDecl			TEMPL	"$pukeyIsprimary; ($pukeyColumns;)";

pukeyIsprimary			ATTR	PrimaryuniquekeyIsprimary
				DOM =	pukeyDomain;

pukeyColumns			CONN	PrimaryuniquekeyColumns,
				CHILD = pukeyColumn
				SEP =   ", ";

pukeyColumn			TEMPL	"$name;";

pukeyDomain			CDOM	("true", "$kw;PRIMARY KEY$ekw;")
					("false", "$kw;UNIQUE$ekw;");

/**********************************************************************************
    PRIMARY/UNIQUE CONSTRAINTS (outer constraints)
***********************************************************************************/

tableConstrs                    COND BOOLEAN(pkAsConstraints)
                                TRUE = pkAsOuterConstraints;

pkAsOuterConstraints            CONN	TablePrimaryuniquekeys,
				CHILD = createPKClause
				SEP =   "$eol;"
                                SUF =  "$eol;";

createPKClause 			GROUP	(pkComment, pukeyConstraintDecl2); 

pukeyConstraintDecl2            TEMPL   "$kw;ALTER TABLE$ekw; $parentName; $m+2;$eol;"
                                        "$kw;ADD$ekw; $kw;CONSTRAINT$ekw; $name; $pukeyIsprimary2; ($m+2;$eol;"
                                        "$pukeyColumns;"
                                        ")$m-2; $m-2; ;$eol;";

parentName                      CONN    ObjectComposite,
                                CHILD = name;

pukeyIsprimary2			ATTR	PrimaryuniquekeyIsprimary
				DOM =	pukeyDomain2;

pukeyDomain2		        CDOM	("true", "$kw;PRIMARY KEY$ekw;")
					("false", "$kw;UNIQUE$ekw;");

/**********************************************************************************
    FOREIGN CONSTRAINTS
***********************************************************************************/

tableFKs				CONN	TableForeignkeys,
						CHILD = fkeyDecl
						PREF  = "$fkComment;";

fkeyDecl	       		COND BOOLEAN(enableMySQLFKS)
                        TRUE =  fkeyDeclMYSQL
                        FALSE = fkeyDeclGeneric;
                        
fkeyDeclGeneric         TEMPL   "$kw;ALTER TABLE$ekw; $constraintTable;"
								"$m+2;$eol;$kw;ADD$ekw; $constraintName;$m+2;$eol;"
                                "$kw;FOREIGN KEY$ekw; ($fkeyColumns;)$m+2;$fkeyReferences;$m-6;"
                        SUF   = ";$eol;$eol;";
                        
fkeyDeclMYSQL           TEMPL   "$kw;ALTER TABLE$ekw; $constraintTable; ENGINE=InnoDB;$eol;"
								"$kw;ALTER TABLE$ekw; $constraintTable;"
								"$m+2;$eol;$kw;ADD$ekw; $constraintName;$m+2;$eol;"
                                "$kw;FOREIGN KEY$ekw; ($fkeyColumns;)$m+2; $eol;$kw;REFERENCES$ekw; $sourceTable;($fkeyColumnsSources;)"
				"$fkeyReferencesMYSQL;$m-6;"
                        SUF   = ";$eol;";
                    

fkeyColumns		CONN	ForeignkeyColumns,
                        CHILD = fkeyColumnColumns
                        SEP   = ", ";

fkeyColumnColumns   	CONN    FkeycolumnColumn,
                        CHILD = name;

fkeyColumnsSources	CONN	ForeignkeyColumns,
                        CHILD = fkeyColumnSourceColumn
                        SEP   = ", ";

fkeyColumnSourceColumn 	CONN    FkeycolumnSourceColumn,
                        CHILD = name;

fkeyReferences      	CONN    ForeignkeyAssociationend,
                        CHILD = fkeyRefconstraint
                        PREF  = "$eol;$kw;REFERENCES$ekw; ";

fkeyReferencesMYSQL     CONN    ForeignkeyAssociationend,
                        CHILD = fkeyRefconstraintMYSQL;

fkeyRefconstraint	GROUP	(fkeyRefconstraint2,fkeyDeleteRule,fkeyUpdateRule)
						SEP = 	"$eol;";

fkeyRefconstraintMYSQL	GROUP	(fkeyDeleteRule,fkeyUpdateRule)
						SEP = 	"$eol;";

fkeyRefconstraint2	CONN    AssociationendReferencedconstraint,
                        CHILD = referencedConstraint;

referencedConstraint	TEMPL 	"$constraintTable;";


sourceTable         	CONN    ForeignkeyAssociationend,
                        CHILD = fkeyRefconstraint2;

constraintTable         CONN    ObjectComposite,
                        CHILD = tableName;

fkeyDeleteRule         PLUGIN FUNCTION AssociationendOppositeassociationend
                       CHILD =  oppositeEndDeleteRule;

oppositeEndDeleteRule ATTR    AssociationendDeleterule,
                              DOM = fkeyDeleteRuleDomain;

fkeyDeleteRuleDomain  	CDOM  	("2", "  $kw;ON DELETE CASCADE$ekw;")   	//Cascade
								("7", "  $kw;ON DELETE SET DEFAULT$ekw;") //Set Default
								("8", "  $kw;ON DELETE SET NULL$ekw;")  	//Set Null
                        		(DEFAULT, "");

fkeyUpdateRule			ATTR 	AssociationendUpdaterule,
                        DOM = 	fkeyUpdateRuleDomain;

fkeyUpdateRuleDomain  	CDOM  	("2", "  $kw;ON UPDATE CASCADE$ekw;")   	//Cascade
				("7", "  $kw;ON UPDATE SET DEFAULT$ekw;") //Set Default
				("8", "  $kw;ON UPDATE SET NULL$ekw;")  	//Set Null
                        	(DEFAULT, "");

constraintName			ATTR	ObjectPhysicalname
						DOM = 	constraintNameDomain;

constraintNameDomain	CDOM	("",      "")
								(DEFAULT, "$constraintNamed;");

constraintNamed			TEMPL	"$kw;CONSTRAINT$ekw; $name; ";


/**********************************************************************************
    CHECK CONSTRAINTS
***********************************************************************************/

tableChecks				CONN	TableConstraints,
						CHILD = checkDecl
						PREF =  "$eol;"
						SEP =   ",$eol;";

checkDecl				TEMPL	"$constraintName;$kw;CHECK$ekw; ($ChkColumn; $checkCondition;)";

ChkColumn				CONN	CheckColumn,
						CHILD = chkColName;

chkColName				ATTR	ColumnName;


/**********************************************************************************
    CREATE INDEX
***********************************************************************************/

tableIndexes			CONN	TableIndexes,
				CHILD = createindexClause
				SEP =   "$eol;$eol;";

createindexClause		GROUP	(indexComment, createindexClause2);


createindexClause2		TEMPL	"$kw;CREATE $chkUnique;INDEX$ekw; $name; $kw;ON$ekw; $indexTable;$eol;"
                                        "($m+2;$eol;"
   				        "$indexContent;$m-2;$eol;"
                                        ");$eol;";

chkUnique			ATTR	IndexUnique
					DOM = 	unique;

unique				CDOM	("true","UNIQUE ")
					("false","");

indexTable			CONN	IndexTable,
				CHILD = indexTableName;

indexTableName			TEMPL	"$name;";

indexContent			TEMPL	"$indexColumns;";

indexColumns			CONN	IndexIndexkeys,
				CHILD = indexKey
				SEP =   ",$eol;"
				NULL  = "$indexHasNoColumns;";

indexHasNoColumns		TEMPL	"$begError;--Error : An index must be linked to at least one column.$endError;$eol;";

indexKey			TEMPL 	"$indexKeyColumn;$indexKeySort;";

indexKeyColumn			CONN	IndexkeyColumn,
				CHILD = columnName;

columnName	        	TEMPL "$name;";

indexKeySort			ATTR	IndexkeySortoption
				DOM =   indexKeySortOptionDomain;

indexKeySortOptionDomain CDOM	("1", "$ascOption;")
				("2", " $kw;DESC$ekw;");

ascOption    	                COND BOOLEAN(ascOptionGenerated)
                                TRUE = ascOptionAlwaysGenerated;

ascOptionAlwaysGenerated        TEMPL " $kw;ASC$ekw;";

/**********************************************************************************
    CREATE VIEW
***********************************************************************************/

createviewClause	TEMPL	"$viewComment;"
				"$kw;CREATE VIEW$ekw; $name; $viewColumns;$m+4;$eol;$kw;AS SELECT$ekw; $viewContent;;$m-4;$eol;";

viewColumns             CONN    ViewColumns,
                        CHILD = name
                        PREF  = "("
                        SEP   = ", "
                        SUF   = ")";

viewContent		TEMPL	"$viewSelectStatement;";


/**********************************************************************************
    CREATE TRIGGER
***********************************************************************************/

tableTriggers			CONN	TableTriggers,
				CHILD = createtriggerClause
				SEP =   "$eol;$eol;";

createtriggerClause     TEMPL  "$createtriggerClauseComment;";


/**********************************************************************************
    CREATE PROCEDURE / FUNCTION
***********************************************************************************/

createProcedureDecl	TEMPL	"$procComment;"
				"$kw;CREATE $checkIfFunction;$ekw; $name;$procedureContent;";

checkIfFunction		ATTR	ProcedureIsfunction
				DOM	=	isProcFunction;

isProcFunction		CDOM	("true", "FUNCTION")
				("false", "PROCEDURE");

procedureContent	TEMPL	"$m+4;$eol;$createProcedureClause;$m-4;$eol;";

createProcedureClause	TEMPL   "($procedureParameters;)$eol;$procedureReturnvalues; $kw;AS$ekw;$m+2;$eol;$procedureBody;$m-2;"
			SUF = 	";";

procedureParameters		CONN	ProcedureOrparameters,
						CHILD = parameterData
						SEP =	", ";

parameterData			GROUP	(getPassingConvention,name,procedureParameterType)
						SEP = 	" ";

getPassingConvention	ATTR	OrparameterPassingconvention
						DOM = 	passingConvention;

passingConvention		CDOM	("1", "$kw;IN$ekw;")
								("2", "$kw;OUT$ekw;")
								("3", "$kw;INOUT$ekw;");

procedureParameterType     CONN    OrparameterType,
                           CHILD = type
                           SUF   = "$procedureParameterLength;";

procedureParameterLength   ATTR    OrparameterLength
                           PREF  = "("
                           SUF   = "$procedureParameterDecimal;)";

procedureParameterDecimal  ATTR    OrparameterDecimal
                           PREF  = ",";

procedureReturnvalues      CONN    ProcedureReturntype,
                           CHILD = returnvalue
                           PREF  = "$kw;RETURNS$ekw; ";

returnvalue                TEMPL   "$name;";

/**********************************************************************************
    COMMENTS
***********************************************************************************/

modelContentComment	   COND BOOLEAN(generateComments)
            	           TRUE = modelContentComment2;

modelContentComment2       TEMPL	"$comment;--/***********************************************************"
					"$eol;--$tab;DDL generation"
					"$eol;--$eol;--$tab;Data Model: $projName;"
					"$eol;--***********************************************************/$endComment;";

tableComment	           COND BOOLEAN(generateComments)
            	           TRUE = tableComment2;

tableComment2              TEMPL        "$comment;--/***********************************************************"
				        "$eol;--$tab;TABLE '$name;'"
				        "$eol;--***********************************************************/$endComment;$eol;$eol;";

pkComment	           COND BOOLEAN(generateComments)
            	           TRUE = pkComment2;

pkComment2                 TEMPL        "$comment;--/***********************************************************$eol;"
					"--$tab;PRIMARY/UNIQUE KEY '$name;'$eol;"
					"--***********************************************************/$endComment;$eol;$eol;";


fkComment	           COND BOOLEAN(generateComments)
            	           TRUE = fkComment2;

fkComment2                 TEMPL        "$comment;--/***********************************************************$eol;"
					"--$tab;Foreign keys of table '$name;'$eol;"
					"--***********************************************************/$endComment;$eol;$eol;";

viewComment	           COND BOOLEAN(generateComments)
            	       TRUE = viewComment2;

viewComment2               TEMPL        "$comment;--/***********************************************************"
				         "$eol;--$tab;VIEW '$name;'$eol;"
                                        "--***********************************************************/$endComment;$eol;$eol;";

indexComment	           COND BOOLEAN(generateComments)
            	           TRUE = indexComment2;

indexComment2              TEMPL        "$comment;--/***********************************************************"
				        "$eol;--$tab;INDEX '$name;'"
				        "$eol;--***********************************************************/$endComment;$eol;$eol;";

procComment	           COND BOOLEAN(generateComments)
            	           TRUE = procComment2;

procComment2               TEMPL        "$comment;--/***********************************************************"
				        "$eol;--$tab;PROCEDURE/FUNCTION '$name;'$eol;"
                                        "--***********************************************************/$endComment;$eol;$eol;";


doNotGenerateNamelessTableComment COND BOOLEAN(generateComments)
            	           TRUE = doNotGenerateNamelessTableComment2;

doNotGenerateNamelessTableComment2 TEMPL "$comment;--Do not generate table '$name;' because it has no physical name.$endComment;$n;$n;";


noColumnComment            COND BOOLEAN(generateComments)
            	           TRUE = noColumnComment2;

noColumnComment2           TEMPL "$begError;--Error: table does not have any column.$endError;$n;";


createtriggerClauseComment COND BOOLEAN(generateComments)
            	           TRUE = createtriggerClauseComment2;

createtriggerClauseComment2 TEMPL "$comment;--CREATE TRIGGER clause not supported.$endComment;$n;$n;";
