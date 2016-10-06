REM ***********************************************************
REM     Forward Engineering
REM     Plug-in: Oracle SQL (Revision: 42  - Date: 9/12/02 3:33p ), Grandite
REM 
REM     Database Name : "Development-1"
REM     Source file   : "...\examples\oracle\ora_demo.sms"
REM ***********************************************************


REM ***********************************************************
REM     CREATE STATEMENTS
REM ***********************************************************


/**************************************************************
    Table 'BUSINESS_TYPE'
***************************************************************/

CREATE TABLE BUSINESS_TYPE 
(
  BT_CODE NUMBER NOT NULL,
  BT_NAME VARCHAR2(100)  NOT NULL
);

/**************************************************************
    Table 'CLIENT'
***************************************************************/

CREATE TABLE CLIENT 
(
  CLI_NUMBER NUMBER NOT NULL,
  CLI_NAME VARCHAR2(100)  NOT NULL,
  CLI_ADDRESS VARCHAR2(100)  NULL,
  CLI_PHONE CHARACTER(14)  NULL,
  CLI_CIE_CODE NUMBER NULL,
  CLI_CLTY_CODE NUMBER NOT NULL
);

COMMENT ON TABLE CLIENT IS
 'This table represent all the company''s clients.';

COMMENT ON COLUMN CLIENT.CLI_CLTY_CODE IS
 'This column source is the column client-type from table client-type.';

/**************************************************************
    Table 'CLIENT_TYPE'
***************************************************************/

CREATE TABLE CLIENT_TYPE 
(
  CLTY_CODE NUMBER NOT NULL,
  CLTY_DESCRIPTION VARCHAR2(2000)  NULL
);

COMMENT ON COLUMN CLIENT_TYPE.CLTY_CODE IS
 'This code represent one and only one client-type.';

/**************************************************************
    Table 'COMPANY'
***************************************************************/

CREATE TABLE COMPANY 
(
  COMP_CODE NUMBER NOT NULL,
  COMP_NAME VARCHAR2(100)  NOT NULL,
  COMP_PHONE CHARACTER(14)  NULL,
  COMP_EMP_NBR NUMBER NOT NULL,
  COMP_ADDRESS VARCHAR2(100)  NULL,
  COMP_FAX CHARACTER(14)  NULL,
  COMP_BUSINESS_CODE NUMBER NOT NULL
);

/**************************************************************
    Table 'INVOICE'
***************************************************************/

CREATE TABLE INVOICE 
(
  INVC_NUMBER NUMBER NOT NULL,
  INVC_DATE DATE NOT NULL,
  INVC_AMOUNT DECIMAL(8,2)  NOT NULL,
  INVC_CLI_NUMBER NUMBER NOT NULL
);

/**************************************************************
    Table 'INVOICE_LINE'
***************************************************************/

CREATE TABLE INVOICE_LINE 
(
  INVL_INVOICE_NUMBER NUMBER NOT NULL,
  INVL_NUMBER NUMBER NOT NULL,
  INVL_PROD_CODE NUMBER NOT NULL
);

/**************************************************************
    Table 'ORDER_LINE'
***************************************************************/

CREATE TABLE ORDER_LINE 
(
  ORDL_CODE NUMBER NOT NULL,
  ORDL_PRODUCT_CODE NUMBER NOT NULL,
  ORDL_NUMBER NUMBER NOT NULL,
  ORDL_AMOUNT DECIMAL(8,2)  NOT NULL
);

/**************************************************************
    Table 'ORDER1'
***************************************************************/

CREATE TABLE ORDER1 
(
  ORD_CODE NUMBER NOT NULL,
  ORD_DATE DATE NOT NULL,
  ORD_SUB_TOTAL DECIMAL(10,2)  NOT NULL,
  ORD_AMOUNT DECIMAL(8,2)  NOT NULL,
  ORD_CLIENT_NUMBER NUMBER NOT NULL
);

/**************************************************************
    Table 'PAYMENT'
***************************************************************/

CREATE TABLE PAYMENT 
(
  PMT_INVOICE_NUMBER NUMBER NOT NULL,
  PMT_NUMBER NUMBER NOT NULL,
  PMT_AMOUNT DECIMAL(8,2)  NOT NULL
);

COMMENT ON TABLE PAYMENT IS
 'Each client must pay in 3 payments or less.';

/**************************************************************
    Table 'PAYMENT_SUMMARY'
***************************************************************/

CREATE TABLE PAYMENT_SUMMARY 
(
  PMTS_INVOICE_NUMBER NUMBER NOT NULL,
  PMTS_PAYMENT_NUMBER NUMBER NOT NULL,
  PMTS_PAYMENT_AMOUNT DECIMAL(8,2)  NOT NULL,
  PMTS_INVOICE_DATE DATE NOT NULL,
  PMTS_INVOICE_AMOUNT DECIMAL(8,2)  NOT NULL
)
  PARTITION BY RANGE (PMTS_PAYMENT_AMOUNT) (
    PARTITION PMTS_PRT_PMT_AMOUNT1  VALUES LESS THAN (100) ,
    PARTITION PMTS_PRT_PMT_AMOUNT2  VALUES LESS THAN (200) ,
    PARTITION PMTS_PRT_PMT_AMOUNT3  VALUES LESS THAN (300) 
  );

/**************************************************************
    Table 'PRODUCT'
***************************************************************/

CREATE TABLE PRODUCT 
(
  PROD_CODE NUMBER NOT NULL,
  PROD_NAME VARCHAR2(100)  NOT NULL,
  PROD_PRICE NUMBER(10,2)  NOT NULL,
  PROD_STOCK DECIMAL(6,1)  NOT NULL,
  PROD_EOQ DECIMAL(6,1)  NOT NULL,
  PROD_IMAGE BLOB NULL
)
  LOB(PROD_IMAGE) STORE AS PROD_IMAGE_LOB_STORAGE(
  ENABLE STORAGE IN ROW
  NOCACHE )
  ;

/**************************************************************
    Table 'SUPPLIER'
***************************************************************/

CREATE TABLE SUPPLIER 
(
  SUPL_NUMBER NUMBER NOT NULL,
  SUPL_NAME VARCHAR2(100)  NOT NULL,
  SUPL_ADDRESS VARCHAR2(100)  NULL,
  SUPL_PHONE CHARACTER(14)  NULL
);

/**************************************************************
    Table 'SUPPLIER_ORDER'
***************************************************************/

CREATE TABLE SUPPLIER_ORDER 
(
  SUPRD_SUPPLIER_NUMBER NUMBER NOT NULL,
  SUPRD_NUMBER NUMBER NOT NULL,
  SUPRD_AMOUNT DECIMAL(8,2)  NOT NULL
);

/**************************************************************
    Table 'SUPPLIER_ORDER_LINE'
***************************************************************/

CREATE TABLE SUPPLIER_ORDER_LINE 
(
  SUPORL_SUPPLIER_ORDER_NUMBER NUMBER NOT NULL,
  SUPORL_SUPPLIER_NUMBER NUMBER NOT NULL,
  SUPORL_NUMBER NUMBER NOT NULL,
  SUPORL_AMOUNT DECIMAL(8,2)  NOT NULL,
  SUPORL_PRODUCT_CODE NUMBER NOT NULL
);

/**************************************************************
    Table 'SUPPLIER_PRODUCT'
***************************************************************/

CREATE TABLE SUPPLIER_PRODUCT 
(
  SUPPRD_SUPPLIER_NUMBER NUMBER NOT NULL,
  SUPPRD_CODE NUMBER NOT NULL,
  SUPPRD_NAME VARCHAR2(100)  NOT NULL,
  SUPPRD_PRICE NUMBER(10,2)  NOT NULL,
  SUPPRD_PRODUCT_CODE NUMBER NOT NULL
);

/**************************************************************
    Index 'BT_IDX_PK'
***************************************************************/

CREATE UNIQUE INDEX BT_IDX_PK 
  ON BUSINESS_TYPE (
    BT_CODE ASC
    )  ;

/**************************************************************
    Index 'CLI_IDX_CLIENT_COMPANY'
***************************************************************/

CREATE INDEX CLI_IDX_CLIENT_COMPANY 
  ON CLIENT (
    CLI_CIE_CODE ASC
    )  ;

/**************************************************************
    Index 'CLI_IDX_CLIENT_TYPE_CODE'
***************************************************************/

CREATE INDEX CLI_IDX_CLIENT_TYPE_CODE 
  ON CLIENT (
    CLI_CLTY_CODE ASC
    )  ;

/**************************************************************
    Index 'CLI_IDX_PK_CLIENT_NUMBER'
***************************************************************/

CREATE UNIQUE INDEX CLI_IDX_PK_CLIENT_NUMBER 
  ON CLIENT (
    CLI_NUMBER ASC
    )  ;

/**************************************************************
    Index 'CLI_IDX_UK1_CLIENT_NAME'
***************************************************************/

CREATE UNIQUE INDEX CLI_IDX_UK1_CLIENT_NAME 
  ON CLIENT (
    CLI_NAME ASC
    )  ;

/**************************************************************
    Index 'CLTY_IDX_CODE'
***************************************************************/

CREATE UNIQUE INDEX CLTY_IDX_CODE 
  ON CLIENT_TYPE (
    CLTY_CODE ASC
    )  ;

/**************************************************************
    Index 'COMP_IDX_BUSINESS_TYPE'
***************************************************************/

CREATE INDEX COMP_IDX_BUSINESS_TYPE 
  ON COMPANY (
    COMP_BUSINESS_CODE ASC
    )  ;

/**************************************************************
    Index 'COMP_IDX_PK_COMP_CODE'
***************************************************************/

CREATE UNIQUE INDEX COMP_IDX_PK_COMP_CODE 
  ON COMPANY (
    COMP_CODE ASC
    )  ;

/**************************************************************
    Index 'COMP_IDX_UK1'
***************************************************************/

CREATE UNIQUE INDEX COMP_IDX_UK1 
  ON COMPANY (
    COMP_NAME ASC,
    COMP_PHONE ASC
    )  ;

/**************************************************************
    Index 'INVC_IDX_CLIENT_NUMBER'
***************************************************************/

CREATE INDEX INVC_IDX_CLIENT_NUMBER 
  ON INVOICE (
    INVC_CLI_NUMBER ASC
    )  ;

/**************************************************************
    Index 'INVC_IDX_INVOICE_NUMBER'
***************************************************************/

CREATE UNIQUE INDEX INVC_IDX_INVOICE_NUMBER 
  ON INVOICE (
    INVC_NUMBER ASC
    )  ;

/**************************************************************
    Index 'INVL_IDX_INVOICE_NUMBER'
***************************************************************/

CREATE INDEX INVL_IDX_INVOICE_NUMBER 
  ON INVOICE_LINE (
    INVL_INVOICE_NUMBER ASC
    )  ;

/**************************************************************
    Index 'INVL_IDX_PK'
***************************************************************/

CREATE UNIQUE INDEX INVL_IDX_PK 
  ON INVOICE_LINE (
    INVL_INVOICE_NUMBER ASC,
    INVL_NUMBER ASC
    )  ;

/**************************************************************
    Index 'INVL_IDX_PRODUCT_CODE'
***************************************************************/

CREATE INDEX INVL_IDX_PRODUCT_CODE 
  ON INVOICE_LINE (
    INVL_PROD_CODE ASC
    )  ;

/**************************************************************
    Index 'ORD_IDX_CLIENT_NUMBER'
***************************************************************/

CREATE INDEX ORD_IDX_CLIENT_NUMBER 
  ON ORDER1 (
    ORD_CLIENT_NUMBER ASC
    )  ;

/**************************************************************
    Index 'ORD_IDX_ORDER_CODE'
***************************************************************/

CREATE UNIQUE INDEX ORD_IDX_ORDER_CODE 
  ON ORDER1 (
    ORD_CODE ASC
    )  ;

/**************************************************************
    Index 'ORDL_IDX_ORDER_CODE'
***************************************************************/

CREATE INDEX ORDL_IDX_ORDER_CODE 
  ON ORDER_LINE (
    ORDL_CODE ASC
    )  ;

/**************************************************************
    Index 'ORDL_IDX_PK'
***************************************************************/

CREATE UNIQUE INDEX ORDL_IDX_PK 
  ON ORDER_LINE (
    ORDL_CODE ASC,
    ORDL_PRODUCT_CODE ASC,
    ORDL_NUMBER ASC
    )  ;

/**************************************************************
    Index 'ORDL_IDX_PRODUCT_CODE'
***************************************************************/

CREATE INDEX ORDL_IDX_PRODUCT_CODE 
  ON ORDER_LINE (
    ORDL_PRODUCT_CODE ASC
    )  ;

/**************************************************************
    Index 'PMT_IDX_INVOICE_NUMBER'
***************************************************************/

CREATE INDEX PMT_IDX_INVOICE_NUMBER 
  ON PAYMENT (
    PMT_INVOICE_NUMBER ASC
    )  ;

/**************************************************************
    Index 'PMT_IDX_PK'
***************************************************************/

CREATE UNIQUE INDEX PMT_IDX_PK 
  ON PAYMENT (
    PMT_INVOICE_NUMBER ASC,
    PMT_NUMBER ASC
    )  ;

/**************************************************************
    Index 'PMTS_INVOICE_NUMBER'
***************************************************************/

CREATE UNIQUE INDEX PMTS_INVOICE_NUMBER 
  ON PAYMENT_SUMMARY (
    PMTS_INVOICE_NUMBER ASC
    )  ;

/**************************************************************
    Index 'PROD_IDX_PRODUCT_CODE'
***************************************************************/

CREATE UNIQUE INDEX PROD_IDX_PRODUCT_CODE 
  ON PRODUCT (
    PROD_CODE ASC
    )  ;

/**************************************************************
    Index 'PROD_IDX_PRODUCT_NAME'
***************************************************************/

CREATE UNIQUE INDEX PROD_IDX_PRODUCT_NAME 
  ON PRODUCT (
    PROD_NAME ASC
    )  ;

/**************************************************************
    Index 'SUPL_IDX_SUPPLIER_NAME'
***************************************************************/

CREATE UNIQUE INDEX SUPL_IDX_SUPPLIER_NAME 
  ON SUPPLIER (
    SUPL_NAME ASC
    )  ;

/**************************************************************
    Index 'SUPL_IDX_SUPPLIER_NUMBER'
***************************************************************/

CREATE UNIQUE INDEX SUPL_IDX_SUPPLIER_NUMBER 
  ON SUPPLIER (
    SUPL_NUMBER ASC
    )  ;

/**************************************************************
    Index 'SUPORL_IDX_PRODUCT_CODE'
***************************************************************/

CREATE INDEX SUPORL_IDX_PRODUCT_CODE 
  ON SUPPLIER_ORDER_LINE (
    SUPORL_PRODUCT_CODE ASC
    )  ;

/**************************************************************
    Index 'SUPORL_IDX_SUPPLIER_ORDER'
***************************************************************/

CREATE INDEX SUPORL_IDX_SUPPLIER_ORDER 
  ON SUPPLIER_ORDER_LINE (
    SUPORL_SUPPLIER_ORDER_NUMBER ASC,
    SUPORL_SUPPLIER_NUMBER ASC
    )  ;

/**************************************************************
    Index 'SUPORL_IDX1'
***************************************************************/

CREATE UNIQUE INDEX SUPORL_IDX1 
  ON SUPPLIER_ORDER_LINE (
    SUPORL_SUPPLIER_ORDER_NUMBER ASC,
    SUPORL_SUPPLIER_NUMBER ASC,
    SUPORL_NUMBER ASC
    )  ;

/**************************************************************
    Index 'SUPPRD_IDX_PRODUCT_CODE'
***************************************************************/

CREATE INDEX SUPPRD_IDX_PRODUCT_CODE 
  ON SUPPLIER_PRODUCT (
    SUPPRD_PRODUCT_CODE ASC
    )  ;

/**************************************************************
    Index 'SUPPRD_IDX_SUPPLIER_NUMBER'
***************************************************************/

CREATE INDEX SUPPRD_IDX_SUPPLIER_NUMBER 
  ON SUPPLIER_PRODUCT (
    SUPPRD_SUPPLIER_NUMBER ASC
    )  ;

/**************************************************************
    Index 'SUPPRD_IDX1'
***************************************************************/

CREATE UNIQUE INDEX SUPPRD_IDX1 
  ON SUPPLIER_PRODUCT (
    SUPPRD_SUPPLIER_NUMBER ASC,
    SUPPRD_CODE ASC
    )  ;

/**************************************************************
    Index 'SUPRD_IDX_SUPPLIER_NUMBER'
***************************************************************/

CREATE INDEX SUPRD_IDX_SUPPLIER_NUMBER 
  ON SUPPLIER_ORDER (
    SUPRD_SUPPLIER_NUMBER ASC
    )  ;

/**************************************************************
    Index 'SUPRD_IDX1'
***************************************************************/

CREATE UNIQUE INDEX SUPRD_IDX1 
  ON SUPPLIER_ORDER (
    SUPRD_SUPPLIER_NUMBER ASC,
    SUPRD_NUMBER ASC
    )  ;

/**************************************************************
    Primary Key 'CLIENT.CLI_PK'
***************************************************************/

ALTER TABLE CLIENT ADD
(
  CONSTRAINT CLI_PK PRIMARY KEY (CLI_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'CLIENT_TYPE.CLTY_PK_CODE'
***************************************************************/

ALTER TABLE CLIENT_TYPE ADD
(
  CONSTRAINT CLTY_PK_CODE PRIMARY KEY (CLTY_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'COMPANY.COMP_PK_CODE'
***************************************************************/

ALTER TABLE COMPANY ADD
(
  CONSTRAINT COMP_PK_CODE PRIMARY KEY (COMP_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'INVOICE.INVC_PK_INVOICE_NUMBER'
***************************************************************/

ALTER TABLE INVOICE ADD
(
  CONSTRAINT INVC_PK_INVOICE_NUMBER PRIMARY KEY (INVC_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'INVOICE_LINE.INVL_PK'
***************************************************************/

ALTER TABLE INVOICE_LINE ADD
(
  CONSTRAINT INVL_PK PRIMARY KEY (INVL_INVOICE_NUMBER, INVL_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'ORDER1.ORD_PK_ORDER_CODE'
***************************************************************/

ALTER TABLE ORDER1 ADD
(
  CONSTRAINT ORD_PK_ORDER_CODE PRIMARY KEY (ORD_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'ORDER_LINE.ORDL_PK'
***************************************************************/

ALTER TABLE ORDER_LINE ADD
(
  CONSTRAINT ORDL_PK PRIMARY KEY (ORDL_CODE, ORDL_PRODUCT_CODE, ORDL_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'BUSINESS_TYPE.PK_BT'
***************************************************************/

ALTER TABLE BUSINESS_TYPE ADD
(
  CONSTRAINT PK_BT PRIMARY KEY (BT_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'PAYMENT.PMT_PK'
***************************************************************/

ALTER TABLE PAYMENT ADD
(
  CONSTRAINT PMT_PK PRIMARY KEY (PMT_INVOICE_NUMBER, PMT_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'PAYMENT_SUMMARY.PMTS_INVOICE_NUMBER'
***************************************************************/

ALTER TABLE PAYMENT_SUMMARY ADD
(
  CONSTRAINT PMTS_INVOICE_NUMBER PRIMARY KEY (PMTS_INVOICE_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'PRODUCT.PROD_CODE'
***************************************************************/

ALTER TABLE PRODUCT ADD
(
  CONSTRAINT PROD_CODE PRIMARY KEY (PROD_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'SUPPLIER.SUPL_PK_SUPPLIER_NUMBER'
***************************************************************/

ALTER TABLE SUPPLIER ADD
(
  CONSTRAINT SUPL_PK_SUPPLIER_NUMBER PRIMARY KEY (SUPL_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'SUPPLIER_ORDER_LINE.SUPORL_PK'
***************************************************************/

ALTER TABLE SUPPLIER_ORDER_LINE ADD
(
  CONSTRAINT SUPORL_PK PRIMARY KEY (SUPORL_SUPPLIER_ORDER_NUMBER, SUPORL_SUPPLIER_NUMBER, SUPORL_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'SUPPLIER_PRODUCT.SUPPRD_PK'
***************************************************************/

ALTER TABLE SUPPLIER_PRODUCT ADD
(
  CONSTRAINT SUPPRD_PK PRIMARY KEY (SUPPRD_SUPPLIER_NUMBER, SUPPRD_CODE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Primary Key 'SUPPLIER_ORDER.SUPRD_PK'
***************************************************************/

ALTER TABLE SUPPLIER_ORDER ADD
(
  CONSTRAINT SUPRD_PK PRIMARY KEY (SUPRD_SUPPLIER_NUMBER, SUPRD_NUMBER) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Unique Key 'CLIENT.CLI_UK1_CLIENT_NAME'
***************************************************************/

ALTER TABLE CLIENT ADD
(
  CONSTRAINT CLI_UK1_CLIENT_NAME UNIQUE (CLI_NAME) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Unique Key 'COMPANY.COMP_UK1'
***************************************************************/

ALTER TABLE COMPANY ADD
(
  CONSTRAINT COMP_UK1 UNIQUE (COMP_NAME, COMP_PHONE) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Unique Key 'PRODUCT.PROD_NAME'
***************************************************************/

ALTER TABLE PRODUCT ADD
(
  CONSTRAINT PROD_NAME UNIQUE (PROD_NAME) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Unique Key 'SUPPLIER.SUPL_UK_SUPPLIER_NAME'
***************************************************************/

ALTER TABLE SUPPLIER ADD
(
  CONSTRAINT SUPL_UK_SUPPLIER_NAME UNIQUE (SUPL_NAME) NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Foreign Key 'CLI_FK_CLIENT_TYPE'
***************************************************************/

ALTER TABLE CLIENT ADD
(
  CONSTRAINT CLI_FK_CLIENT_TYPE 
    FOREIGN KEY (CLI_CLTY_CODE)
    REFERENCES CLIENT_TYPE (CLTY_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'CLI_FK_COMPANY_CODE'
***************************************************************/

ALTER TABLE CLIENT ADD
(
  CONSTRAINT CLI_FK_COMPANY_CODE 
    FOREIGN KEY (CLI_CIE_CODE)
    REFERENCES COMPANY (COMP_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'COMP_FK_BUSINESS_TYPE'
***************************************************************/

ALTER TABLE COMPANY ADD
(
  CONSTRAINT COMP_FK_BUSINESS_TYPE 
    FOREIGN KEY (COMP_BUSINESS_CODE)
    REFERENCES BUSINESS_TYPE (BT_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'INVC_FK_CLIENT_NUMBER'
***************************************************************/

ALTER TABLE INVOICE ADD
(
  CONSTRAINT INVC_FK_CLIENT_NUMBER 
    FOREIGN KEY (INVC_CLI_NUMBER)
    REFERENCES CLIENT (CLI_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'INVL_FK_INVOICE_NUMBER'
***************************************************************/

ALTER TABLE INVOICE_LINE ADD
(
  CONSTRAINT INVL_FK_INVOICE_NUMBER 
    FOREIGN KEY (INVL_INVOICE_NUMBER)
    REFERENCES INVOICE (INVC_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'INVL_FK_PRODUCT_CODE'
***************************************************************/

ALTER TABLE INVOICE_LINE ADD
(
  CONSTRAINT INVL_FK_PRODUCT_CODE 
    FOREIGN KEY (INVL_PROD_CODE)
    REFERENCES PRODUCT (PROD_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'ORD_CLIENT_NUMBER'
***************************************************************/

ALTER TABLE ORDER1 ADD
(
  CONSTRAINT ORD_CLIENT_NUMBER 
    FOREIGN KEY (ORD_CLIENT_NUMBER)
    REFERENCES CLIENT (CLI_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'ORDL_FK_ORDER_CODE'
***************************************************************/

ALTER TABLE ORDER_LINE ADD
(
  CONSTRAINT ORDL_FK_ORDER_CODE 
    FOREIGN KEY (ORDL_CODE)
    REFERENCES ORDER1 (ORD_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'ORDL_FK_PRODUCT_CODE'
***************************************************************/

ALTER TABLE ORDER_LINE ADD
(
  CONSTRAINT ORDL_FK_PRODUCT_CODE 
    FOREIGN KEY (ORDL_PRODUCT_CODE)
    REFERENCES PRODUCT (PROD_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'PMT_FK_INVOICE_NUMBER'
***************************************************************/

ALTER TABLE PAYMENT ADD
(
  CONSTRAINT PMT_FK_INVOICE_NUMBER 
    FOREIGN KEY (PMT_INVOICE_NUMBER)
    REFERENCES INVOICE (INVC_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'SUPORL_FK_PRODUCT_CODE'
***************************************************************/

ALTER TABLE SUPPLIER_ORDER_LINE ADD
(
  CONSTRAINT SUPORL_FK_PRODUCT_CODE 
    FOREIGN KEY (SUPORL_PRODUCT_CODE)
    REFERENCES PRODUCT (PROD_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'SUPORL_FK_SUPPLIER_ORDER'
***************************************************************/

ALTER TABLE SUPPLIER_ORDER_LINE ADD
(
  CONSTRAINT SUPORL_FK_SUPPLIER_ORDER 
    FOREIGN KEY (SUPORL_SUPPLIER_ORDER_NUMBER, SUPORL_SUPPLIER_NUMBER)
    REFERENCES SUPPLIER_ORDER (SUPRD_SUPPLIER_NUMBER, SUPRD_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'SUPPRD_FK_PRODUCT_CODE'
***************************************************************/

ALTER TABLE SUPPLIER_PRODUCT ADD
(
  CONSTRAINT SUPPRD_FK_PRODUCT_CODE 
    FOREIGN KEY (SUPPRD_PRODUCT_CODE)
    REFERENCES PRODUCT (PROD_CODE) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'SUPPRD_FK_SUPPLIER_NUMBER'
***************************************************************/

ALTER TABLE SUPPLIER_PRODUCT ADD
(
  CONSTRAINT SUPPRD_FK_SUPPLIER_NUMBER 
    FOREIGN KEY (SUPPRD_SUPPLIER_NUMBER)
    REFERENCES SUPPLIER (SUPL_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Foreign Key 'SUPRD_FK_SUPPLIER_NUMBER'
***************************************************************/

ALTER TABLE SUPPLIER_ORDER ADD
(
  CONSTRAINT SUPRD_FK_SUPPLIER_NUMBER 
    FOREIGN KEY (SUPRD_SUPPLIER_NUMBER)
    REFERENCES SUPPLIER (SUPL_NUMBER) 
    NOT DEFERRABLE INITIALLY IMMEDIATE
    ENABLE
    VALIDATE
);

/**************************************************************
    Check Constraint 'CLIENT.CLI_CHK_CLI_PHONE'
***************************************************************/

ALTER TABLE CLIENT ADD
(
  CONSTRAINT CLI_CHK_CLI_PHONE CHECK (CLI_PHONE LIKE '(___) ___-____')
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'COMPANY.COMP_CK_COMP_FAX'
***************************************************************/

ALTER TABLE COMPANY ADD
(
  CONSTRAINT COMP_CK_COMP_FAX CHECK ((COMP_FAX LIKE '(___) ___-____'))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'COMPANY.COMP_CK_COMP_PHONE'
***************************************************************/

ALTER TABLE COMPANY ADD
(
  CONSTRAINT COMP_CK_COMP_PHONE CHECK ((COMP_PHONE LIKE '(___) ___-____'))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'PAYMENT.PMT_CK_PAYMENT_NUMBER'
***************************************************************/

ALTER TABLE PAYMENT ADD
(
  CONSTRAINT PMT_CK_PAYMENT_NUMBER CHECK (( PMT_NUMBER > 0 ) AND ( PMT_NUMBER < 4 ))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'PRODUCT.PROD_CK_EOQ'
***************************************************************/

ALTER TABLE PRODUCT ADD
(
  CONSTRAINT PROD_CK_EOQ CHECK ((PROD_EOQ BETWEEN 0 AND 5000))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'PRODUCT.PROD_CK_STOCK'
***************************************************************/

ALTER TABLE PRODUCT ADD
(
  CONSTRAINT PROD_CK_STOCK CHECK ((PROD_STOCK BETWEEN 0 AND 5000))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'SUPPLIER.SUPL_CK_SUPPLIER_PHONE'
***************************************************************/

ALTER TABLE SUPPLIER ADD
(
  CONSTRAINT SUPL_CK_SUPPLIER_PHONE CHECK (SUPL_PHONE LIKE '(___) ___-____')
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    Check Constraint 'SUPPLIER_PRODUCT.SUPPRD_CK_PRODUCT_PRICE'
***************************************************************/

ALTER TABLE SUPPLIER_PRODUCT ADD
(
  CONSTRAINT SUPPRD_CK_PRODUCT_PRICE CHECK ((SUPPRD_PRICE BETWEEN 0 AND 9999999.99))
  NOT DEFERRABLE INITIALLY IMMEDIATE
  ENABLE
  VALIDATE
);

/**************************************************************
    View 'CLIENT_CORPO_VIEW'
***************************************************************/

CREATE OR REPLACE VIEW CLIENT_CORPO_VIEW 
  (CLI_NAME, CLI_PHONE, CLI_CLTY_CODE)
  AS SELECT CLI_NAME,
   CLI_PHONE,
   CLI_CLTY_CODE
  FROM CLIENT CLI, CLIENT_TYPE CLTY
  WHERE CLI.CLI_CLTY_CODE = CLTY.CLTY_CODE AND
  UPPER(CLTY.CLTY_DESCRIPTION) LIKE 'CORPO%'
  WITH CHECK OPTION CONSTRAINT CK_CLIV_CORPO;



/**************************************************************
    Sequence 'seq_cli_num'
***************************************************************/

CREATE SEQUENCE seq_cli_num
  INCREMENT BY 1
  START WITH 101
  MINVALUE 1
  MAXVALUE 10000
  NOCYCLE
  CACHE 5
  NOORDER;

/**************************************************************
    Sequence 'seq_inv_num'
***************************************************************/

CREATE SEQUENCE seq_inv_num
  INCREMENT BY 1
  START WITH 10001
  MINVALUE 1
  MAXVALUE 100000
  NOCYCLE
  NOCACHE 
  ORDER;

/**************************************************************
    Function 'FUNCT_FIND_CLI'
***************************************************************/

CREATE OR REPLACE FUNCTION FUNCT_FIND_CLI(
  P_CLI_NUMBER IN NUMBER
  )  RETURN NUMBER  
  IS
        NOCLIOUT NUMBER(11,2);
        BEGIN
        SELECT CLI_NUMBER
        INTO NOCLIOUT
        FROM CLIENT
        WHERE CLI_NUMBER = P_CLI_NUMBER;
        RETURN NOCLIOUT;
        END
;
/
/**************************************************************
    Procedure 'PROC_DEL_CLI'
***************************************************************/

CREATE OR REPLACE PROCEDURE PROC_DEL_CLI(
  P_CLI_NUMBER IN NUMBER
  ) 
  AS
      BEGIN
      DELETE
      FROM CLIENT
      WHERE CLI_NUMBER = P_CLI_NUMBER;
      END
;
/
/**************************************************************
    Package 'PCK_FIND_CLI'
***************************************************************/

CREATE OR REPLACE PACKAGE PCK_FIND_CLI
   IS
        PROCEDURE PROC_DEL_CLI(P_CLI_NUMBER IN NUMBER);
        FUNCTION FUNCT_FIND_CLI(P_CLI_NUMBER IN NUMBER) RETURN NUMBER;
        END
  ;
/

CREATE OR REPLACE PACKAGE BODY PCK_FIND_CLI
  IS
        FUNCTION FUNCT_FIND_CLI(P_CLI_NUMBER IN NUMBER)
        RETURN NUMBER
        IS
        NOCLIOUT NUMBER(11,2);
        BEGIN
        SELECT CLI_NUMBER
        INTO NOCLIOUT
        FROM CLIENT
        WHERE CLI_NUMBER = P_CLI_NUMBER;
        RETURN NOCLIOUT;
        END;
        PROCEDURE PROC_DEL_CLI(P_CLI_NUMBER IN NUMBER)
        AS
        BEGIN
        DELETE
        FROM CLIENT
        WHERE CLI_NUMBER = P_CLI_NUMBER;
        END;
        END
  ;
/
/**************************************************************
    Trigger 'SR_TRG_DEL_BT'
***************************************************************/

CREATE OR REPLACE TRIGGER SR_TRG_DEL_BT 
  AFTER
  DELETE
  ON BUSINESS_TYPE
  REFERENCING OLD AS OLD NEW AS NEW 
  FOR EACH ROW
  DECLARE
      NB_VALID_ROWS INTEGER;
      NB_TOTAL INTEGER;
      MUTATING_TABLE EXCEPTION;
      PRAGMA EXCEPTION_INIT(MUTATING_TABLE, -4091);
    BEGIN
      SELECT COUNT(*) INTO NB_VALID_ROWS
      FROM COMPANY
      WHERE COMPANY.COMP_BUSINESS_CODE = :OLD.BT_CODE;
      IF (NB_VALID_ROWS > 0)
      THEN
        RAISE_APPLICATION_ERROR(-20021, 'Delete on BUSINESS_TYPE failed: used by at least one COMPANY');
      END IF;
      EXCEPTION
        WHEN MUTATING_TABLE THEN NULL;
      END;
/

/**************************************************************
    Trigger 'SR_TRG_INS_UPD_INVOICE'
***************************************************************/

CREATE OR REPLACE TRIGGER SR_TRG_INS_UPD_INVOICE 
  AFTER
  INSERT OR UPDATE
  ON INVOICE
  REFERENCING OLD AS OLD NEW AS NEW 
  FOR EACH ROW
  DECLARE
          NB_VALID_ROWS INTEGER;
          NB_TOTAL INTEGER;
          MUTATING_TABLE EXCEPTION;
          PRAGMA EXCEPTION_INIT(MUTATING_TABLE, -4091);
        BEGIN
          SELECT COUNT(*) INTO NB_VALID_ROWS
          FROM CLIENT
          WHERE CLIENT.CLI_NUMBER = :NEW.INVC_CLI_NUMBER;
          IF (NB_VALID_ROWS > 0)
          THEN
            RAISE_APPLICATION_ERROR(-20021, 'Update on INVOICE failed: CLIENT does not exist.');
          END IF;
          EXCEPTION
            WHEN MUTATING_TABLE THEN NULL;
        END;
/



REM     END

