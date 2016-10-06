/*
 [The "BSD licence"]
 Copyright (c) 2007-2008 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
  
grammar Java1_6;


options {
    backtrack=true;
    memoize=true;
}

@header {
	package com.neosapiens.plugins.reverse.javasource.parsing;
	import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
	import com.neosapiens.plugins.reverse.javasource.utils.ParameterOfMethode;
	import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
	import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
	import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;
	import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;
	import java.util.Vector;
	import java.lang.reflect.Modifier;
}

@lexer::header {
	package com.neosapiens.plugins.reverse.javasource.parsing;
	import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
	import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
}

@lexer::members {
  protected boolean enumIsKeyword = true;
}

@members {
  // Instance of semanticAction
  private SemanticAction builder;
  
  // Getter
  public SemanticAction getBuilder(){
    return builder;
  }
  
  //Override du constructor pour ajouter notre builder
  public Java1_6Parser(TokenStream input, SemanticAction builder) {
  this(input, new RecognizerSharedState());
  this.builder = builder; }
  
  // variable pour retenir le nom du package courant
  private String packageName = null;
  public String getPackageName(){
    return packageName;
  }
  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }
  //Vector simulant une pile pour retenir le nom de la class courant
  //Utils dans le cas des class declarer a l'interne.
  private Vector<String> currentClassOrInterfaceNamePile = new Vector<String>();
  public String getCurrentClassOrInterfaceNamePile(){
    return this.currentClassOrInterfaceNamePile.lastElement();
  }
  public void addCurrentClassOrInterfaceNamePile(String classOrInterfaceName){
    this.currentClassOrInterfaceNamePile.add(classOrInterfaceName);
  }
  public void removeLastElementOfCurrentClassOrInterfaceNamePile(){
    this.currentClassOrInterfaceNamePile.remove(currentClassOrInterfaceNamePile.size() - 1);
  }
  public String getRootClassOrInterfaceNamePile(){
    return this.currentClassOrInterfaceNamePile.firstElement();
  }
  //A String buffer local for keeping the last javadoc parse.
  private String javadocCommentBuffer = null;

  //Getter for the currentJavadoc
  public String getJavadocComment() {
    String result = javadocCommentBuffer;
    javadocCommentBuffer = null;  // Reset the buffer
    return result;
  }

  private void updateJavadocComment() {
    int i = input.index();
    if (i == 0) {
      return;
    }
    Token t = (Token)input.get(--i);
    //Skip all token representing a WS(white space)
    while (i > 0 && t.getType()== WS) {
        t = (Token)input.get(--i);
    }
    //If the tokent represent a COMMENT, it is javadoc.
    //else there's no javadoc in this token
    if (t.getType() == COMMENT){
      javadocCommentBuffer = t.getText();
    }
  }
  
  //Increment counter for identifing the initialization block with a name.
  private Integer initializationBlockCount = 1;
  
  public Integer getInitializationBlockCount(){
    return this.initializationBlockCount;
  }
    
  public void incrementInitializationBlockCount() {
    this.initializationBlockCount = this.initializationBlockCount + 1;
  }
}

/********************************************************************************************
                          Parser section
*********************************************************************************************/
           
compilationUnit
	: ( (annotations)?	packageDeclaration	)?
		(importDeclaration)*
		(typeDeclaration)*
	;

packageDeclaration 
  : 'package' 
		packageName=qualifiedName	
		{ 
		  setPackageName($packageName.text);
		  getBuilder().createPackage($packageName.text); 
		}
    ';'
  ;

importDeclaration  
@init{
  StringBuffer stringBuffer = new StringBuffer();
}  
  : 'import' ('static')? importName=IDENTIFIER'.' '*' ';' 
    {
      stringBuffer.append($importName.text);
      stringBuffer.append(".");
      stringBuffer.append("*");
      getBuilder().createImport(stringBuffer.toString());   
    }     
  | 'import' ('static')? importName=IDENTIFIER {stringBuffer.append($importName.text);} 
    ( '.'	importName=IDENTIFIER 
      {
        stringBuffer.append(".");
        stringBuffer.append($importName.text);
      }
     )+ 
  ( '.'	'*'{stringBuffer.append(".*");} )? ';'
  {getBuilder().createImport(stringBuffer.toString());}
  ;

qualifiedImportName 
  : IDENTIFIER 
    ('.' IDENTIFIER)*
  ;

typeDeclaration 
  : classOrInterfaceDeclaration
	| ';'
  ;

classOrInterfaceDeclaration 
  : classDeclaration    
  | interfaceDeclaration
  ;
    
  
modifier returns [int modifierLocal = 0]
  : annotation
	| 'public' { modifierLocal = Modifier.PUBLIC; }
	| 'protected' { modifierLocal = Modifier.PROTECTED; }
	| 'private' { modifierLocal = Modifier.PRIVATE; }
	| 'abstract' { modifierLocal = Modifier.ABSTRACT; }
	| 'static' { modifierLocal = Modifier.STATIC; }
	| 'final'	{ modifierLocal = Modifier.FINAL; } // class only -- does not apply to interfaces
	| 'native' { modifierLocal = Modifier.NATIVE; }
	| 'synchronized' { modifierLocal = Modifier.SYNCHRONIZED; }
	| 'transient' { modifierLocal = Modifier.TRANSIENT; }
	| 'volatile' { modifierLocal = Modifier.VOLATILE; }
	| 'strictfp'
  ;

modifiers returns [int modifiersLocal = 0]
	:
	{  updateJavadocComment(); }
	( modifierTemp=modifier { modifiersLocal |= modifierTemp; } )*
	;
variableModifiers //returns [int variableModifiers = 0]
  : ( 'final'
    | annotation //Non traiter pour l'instant
    )*
  ;
    

classDeclaration 
  : normalClassDeclaration
  | enumDeclaration
  ;

normalClassDeclaration 
@init{
  List<String> implementsList = new ArrayList<String>();
  List<String> extendsList = new ArrayList<String>();
  DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();
}
  : 

  classModifiers=modifiers  
		'class' 
		className=IDENTIFIER
    ( typeParameters )?
    ( 'extends'	classExtendsName=type {extendsList.add(classExtendsName);} )?
    (	'implements' implementsListTemp=typeList {implementsList = implementsListTemp;} )?
		{	
		  addCurrentClassOrInterfaceNamePile($className.text);	
		  declarationOfClassOrInterface.setCurrentPackage(getPackageName());
		  declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
		  declarationOfClassOrInterface.setJavadoc(getJavadocComment());
		  declarationOfClassOrInterface.setModifiers(classModifiers);
		  declarationOfClassOrInterface.setClass(true);
		  declarationOfClassOrInterface.setClassOrInterfaceName($className.text);
		  declarationOfClassOrInterface.setExtendsList(extendsList);
		  declarationOfClassOrInterface.setImplementsList(implementsList);
		  getBuilder().createClass(declarationOfClassOrInterface);	
		}
    classBody
    { removeLastElementOfCurrentClassOrInterfaceNamePile(); }
    ;


typeParameters returns [List<String> typeParametersList = new ArrayList<String>()]
  : '<'
    typeParameterString=typeParameter { typeParametersList.add(typeParameterString); }
    (',' typeParameterString=typeParameter { typeParametersList.add(typeParameterString); } )*
    '>'
  ;

typeParameter returns [String typeParameterNameAndExtendsTypeBoundName = null]
@init{
  StringBuffer stringBuffer = new StringBuffer();
}
  : typeParameterName=IDENTIFIER {stringBuffer.append(typeParameterName);}
    ('extends' typeboundString=typeBound {stringBuffer.append(typeboundString);})?
    {typeParameterNameAndExtendsTypeBoundName = stringBuffer.toString();}
  ;


typeBound returns [String typeBoundString = null]
  @init{
    StringBuffer stringBuffer = new StringBuffer();
  }
    :   typeName=type {stringBuffer.append(typeName);}
        ('&' typeName=type {stringBuffer.append(typeName);}
        )*
        {
          typeBoundString = stringBuffer.toString();
        }
    ;


enumDeclaration 
@init{
  List<String> implementsList = new ArrayList<String>();
  DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();  
}
    :   enumModifiers=modifiers 
        ('enum') 
        enumName=IDENTIFIER
        ( 'implements' implementsListTemp=typeList { implementsList = implementsListTemp; } )?
        {
          addCurrentClassOrInterfaceNamePile($enumName.text);
          declarationOfClassOrInterface.setCurrentPackage(getPackageName());
          declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
          declarationOfClassOrInterface.setJavadoc(getJavadocComment());
          declarationOfClassOrInterface.setModifiers(enumModifiers);
          declarationOfClassOrInterface.setEnum(true);
          declarationOfClassOrInterface.setClassOrInterfaceName($enumName.text);
          declarationOfClassOrInterface.setImplementsList(implementsList);
          getBuilder().createClass(declarationOfClassOrInterface);
        }
        enumBody
        { removeLastElementOfCurrentClassOrInterfaceNamePile(); }
    ;
    

enumBody 
  : '{'
    ( enumConstants )? 
    ','? 
    ( enumBodyDeclarations )? 
    '}'
  ;

enumConstants 
    :   enumConstant
        (',' enumConstant
        )*
    ;

/**
 * NOTE: here differs from the javac grammar, missing TypeArguments.
 * EnumeratorDeclaration = AnnotationsOpt [TypeArguments] IDENTIFIER [ Arguments ] [ "{" ClassBody "}" ]
 */
enumConstant 
@init{
  FieldOfClass fieldOfClass = new FieldOfClass();
}  
  : ( annotations )?
    enumName=IDENTIFIER
    ( arguments )?
    ( classBody )?        
    {
      fieldOfClass.setCurrentClass(getCurrentClassOrInterfaceNamePile());
      fieldOfClass.setCurrentPackage(getPackageName());
      fieldOfClass.setJavadoc(getJavadocComment());
      fieldOfClass.setFieldName($enumName.text);
      getBuilder().createField(fieldOfClass);
    }
    /* TODO: $GScope::name = names.empty. enum constant body is actually
    an anonymous class, where constructor isn't allowed, have to add this check*/
  ;

enumBodyDeclarations 
    :   ';' 
        (classBodyDeclaration
        )*
    ;

interfaceDeclaration 
    :   normalInterfaceDeclaration
    |   annotationTypeDeclaration
    ;
    
normalInterfaceDeclaration 
  @init{
    List<String> extendsList = new ArrayList<String>();
    List<String> typeParametersList = new ArrayList<String>();
    DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();
  }
  :   
      interfaceModifiers=modifiers 
      'interface' 
      interfaceName=IDENTIFIER
      ( typeParametersListTemp=typeParameters { typeParametersList = typeParametersListTemp; } )?
      ( 'extends' extendsListTemp=typeList { extendsList = extendsListTemp; } )?
      {
        addCurrentClassOrInterfaceNamePile($interfaceName.text);
        declarationOfClassOrInterface.setCurrentPackage(getPackageName());
        declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
        declarationOfClassOrInterface.setJavadoc(getJavadocComment());
        declarationOfClassOrInterface.setModifiers(interfaceModifiers);
        declarationOfClassOrInterface.setInterface(true);
        declarationOfClassOrInterface.setClassOrInterfaceName($interfaceName.text);
        declarationOfClassOrInterface.setExtendsList(extendsList);
        getBuilder().createClass(declarationOfClassOrInterface);
      }      
      interfaceBody
      { removeLastElementOfCurrentClassOrInterfaceNamePile(); }
    ;

typeList returns [List<String> extendsTypeList = new ArrayList<String>()]
    :   extendsTypeName=type { extendsTypeList.add(extendsTypeName); }
        (',' extendsTypeName=type { extendsTypeList.add(extendsTypeName); }
        )*
    ;

classBody 
    :   '{' 
        (classBodyDeclaration
        )* 
        '}'
    ;

interfaceBody   
  :   '{' 
        (interfaceBodyDeclaration
        )* 
        '}'
    ;

classBodyDeclaration 
    :   ';'
    //|   ('static')? block
    |   memberDecl
    ;

memberDecl 
    : fieldDeclaration
    | methodDeclaration
    | classDeclaration
    | interfaceDeclaration
    | initializationBlock
    ;

initializationBlock
@init{
  InitializationBlockOfClass initializationBlock = new InitializationBlockOfClass();
  String initializationBlockName = "initializationBlock";
}  
  :  (  'static' {initializationBlock.setStatic(true);} )? 
      bodyTemp=block
      {

        initializationBlock.setInitializationBlockName(initializationBlockName + getInitializationBlockCount().toString());
        initializationBlock.setCurrentClass(getCurrentClassOrInterfaceNamePile());
        initializationBlock.setCurrentPackage(getPackageName());
        initializationBlock.setBody(bodyTemp);
        initializationBlock.setJavadoc(getJavadocComment());
        
        getBuilder().createInitializationBlock(initializationBlock);
        
        incrementInitializationBlockCount();
      }
  ;

methodDeclaration 
	@init
	{
		String returnType = null;
		List<String> exceptionList = new ArrayList<String>();
		DeclarationOfMethod declarationOfMethod = new DeclarationOfMethod();
		StringBuffer bodyTextBuffer = new StringBuffer();
		String bodyText = null;
	}
	:
  /* For constructor, return type is null, name is 'init' */
  	constructorModifiers=modifiers
    ( typeParameters )?
    constructorIdentifier=IDENTIFIER 
    formalParametersList=formalParameters
    ( 'throws' exceptionListTemp=qualifiedNameList {exceptionList = exceptionListTemp;} )?
     '{'	
        ( explicitConstructorTemp=explicitConstructorInvocation { bodyTextBuffer.append($explicitConstructorTemp.text); } )? 
        (	blockStatementTemp=blockStatement { bodyTextBuffer.append($blockStatementTemp.text); } )*	
     '}'
    {
      declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
      declarationOfMethod.setCurrentPackage(getPackageName());
      declarationOfMethod.setJavadoc(getJavadocComment());
      declarationOfMethod.setModifiers(constructorModifiers);
      declarationOfMethod.setMethodReturnType(null);
      declarationOfMethod.setMethodName($constructorIdentifier.text);
      declarationOfMethod.setMethodExceptionsList(exceptionList);
      declarationOfMethod.setMethodParametersList(formalParametersList);
      declarationOfMethod.setBody(bodyTextBuffer.toString());
      getBuilder().createMethod(declarationOfMethod);
    }   
    |
    //For other methode
		methodModifiers=modifiers
    ( typeParameters )?
    (
			returnTypeTemp=type {returnType = returnTypeTemp;}
    	|   
			'void' {returnType = "void";}
    )
    methodIdentifier=IDENTIFIER
    formalParametersList=formalParameters
    ( '[' ']')*
    ( 'throws' exceptionListTemp=qualifiedNameList {exceptionList = exceptionListTemp;} )?
    (   blockTemp=block {bodyText = blockTemp;}
      | ';'{bodyText = ";";}
    )  
    {
      declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
      declarationOfMethod.setCurrentPackage(getPackageName());
      declarationOfMethod.setJavadoc(getJavadocComment());
      declarationOfMethod.setModifiers(methodModifiers);
      declarationOfMethod.setMethodReturnType(returnType);
      declarationOfMethod.setMethodName($methodIdentifier.text);
      declarationOfMethod.setMethodExceptionsList(exceptionList);
      declarationOfMethod.setMethodParametersList(formalParametersList);
      declarationOfMethod.setBody(bodyText);
      getBuilder().createMethod(declarationOfMethod);
    }  
;


fieldDeclaration 
  : fieldModifiers=modifiers
    fieldType=type
    variableDeclarator[fieldModifiers,fieldType]
    ( ',' variableDeclarator[fieldModifiers,fieldType] )*
    ';'
  ;

variableDeclarator [int fieldModifiers, String fieldType]
@init{
  StringBuffer stringBuffer = new StringBuffer();
  FieldOfClass fieldOfClass = new FieldOfClass();
  String variableInitializerValue = null;
}
  : fieldIdentifier=IDENTIFIER { stringBuffer.append($fieldIdentifier.text); }
    ('['']' { stringBuffer.append("[]"); } )*
    ('='	decriptionVariableInitializer=variableInitializer {variableInitializerValue = $decriptionVariableInitializer.text; } )?
		{ 
		  if (fieldModifiers != -1)
		  {
		    fieldOfClass.setFieldName(stringBuffer.toString());
		    fieldOfClass.setCurrentClass(getCurrentClassOrInterfaceNamePile());
        fieldOfClass.setCurrentPackage(getPackageName());
        fieldOfClass.setJavadoc(getJavadocComment());
        fieldOfClass.setModifiers(fieldModifiers);
		    fieldOfClass.setFieldType(fieldType);
		    fieldOfClass.setFieldValue(variableInitializerValue);		    
        getBuilder().createField(fieldOfClass);
      }
		}
  ;

/**
 *TODO: add predicates
 */
interfaceBodyDeclaration 
    :
        interfaceFieldDeclaration
    |   interfaceMethodDeclaration
    |   interfaceDeclaration
    |   classDeclaration
    |   ';'
    ;

interfaceMethodDeclaration 
  @init{
    String returnType = null;
    List<String> exceptionList = new ArrayList<String>();
    DeclarationOfMethod declarationOfMethod = new DeclarationOfMethod();
  }
  : interfaceMethodModifiers=modifiers
    ( interfaceMethodTypeParameters=typeParameters )?
    ( returnTypeTemp=type {returnType = returnTypeTemp;}
      |'void' {returnType = "void";}
    )
    interfaceMethodName=IDENTIFIER
    interfaceMethodFormalParametersList=formalParameters
    ( '['']' )*
    ('throws' exceptionListTemp=qualifiedNameList {exceptionList = exceptionListTemp;})? 
    ';'
    {
      declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
      declarationOfMethod.setCurrentPackage(getPackageName());
      declarationOfMethod.setJavadoc(getJavadocComment());
      declarationOfMethod.setModifiers(interfaceMethodModifiers);
      declarationOfMethod.setMethodReturnType(returnType);
      declarationOfMethod.setMethodName($interfaceMethodName.text);
      declarationOfMethod.setMethodExceptionsList(exceptionList);
      declarationOfMethod.setMethodParametersList(interfaceMethodFormalParametersList);
      getBuilder().createMethod(declarationOfMethod);
    }
  ;

/**
 * NOTE, should not use variableDeclarator here, as it doesn't necessary require
 * an initializer, while an interface field does, or judge by the returned value.
 * But this gives better diagnostic message, or antlr won't predict this rule.
 */
interfaceFieldDeclaration 
  : interfaceFieldModifiers=modifiers 
    interfaceFieldType=type 
    variableDeclarator[interfaceFieldModifiers,interfaceFieldType]
    ( ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType] )*
    ';'
  ;


type returns [String typeNameString = null]
@init {
  StringBuffer stringBuffer = new StringBuffer();
}
  : ( classType=classOrInterfaceType {stringBuffer.append(classType);}
      ( '['']' {stringBuffer.append("[]");} )*
		  |   
			primitiveTypeOfClass=primitiveType {stringBuffer.append($primitiveTypeOfClass.text);}
      ( '['']' {stringBuffer.append("[]");} )*
		)
		{ typeNameString = stringBuffer.toString(); }
    ;


classOrInterfaceType returns [String classOrInterfaceTypeString = null]
@init	{
  StringBuffer stringBuffer = new StringBuffer();
}
  : identifier1=IDENTIFIER {stringBuffer.append($identifier1.text);}
    ( typeArguments )?
    (	'.' {stringBuffer.append(".");}
      identifier2=IDENTIFIER {stringBuffer.append($identifier2.text);}
      ( typeArguments )?
    )*
		{ classOrInterfaceTypeString = stringBuffer.toString(); }
    ;

primitiveType  
    :   'boolean'
    |   'char'
    |   'byte'
    |   'short'
    |   'int'
    |   'long'
    |   'float'
    |   'double'
    ;

typeArguments 
    :   '<' typeArgument
        (',' typeArgument
        )* 
        '>'
    ;

typeArgument 
    :   type
    |   '?'
        (
            ('extends'
            |'super'
            )
            type
        )?
    ;

qualifiedNameList returns [List<String> exceptionList = new ArrayList<String>()]
  : exceptionName=qualifiedName {exceptionList.add($exceptionName.text);}
    ( ',' exceptionName=qualifiedName {exceptionList.add($exceptionName.text);} )*
  ;

formalParameters returns [List<ParameterOfMethode> formalParametersList = new ArrayList<ParameterOfMethode>()]
    :   
    '('
    (
    	formalParametersDeclarationListTemp=formalParameterDecls 
    	{formalParametersList = formalParametersDeclarationListTemp;}
    )? 
    ')'
    ;

formalParameterDecls returns [List<ParameterOfMethode> formalParametersDeclarationList = new ArrayList<ParameterOfMethode>()]
    :   
		ellipsisParameterDecl
    | normalParameterType=normalParameterDecl 
		  {formalParametersDeclarationList.add(normalParameterType);}
    (	','
			normalParameterType=normalParameterDecl 
			{formalParametersDeclarationList.add(normalParameterType);}
		)*
    | (normalParameterDecl
			 ','
       )+ 
       ellipsisParameterDecl
    ;

normalParameterDecl returns [ParameterOfMethode normalParameter = new ParameterOfMethode()]
 	@init
 	{
 		StringBuffer stringBuffer = new StringBuffer();
 		ParameterOfMethode parameterLocal = new ParameterOfMethode();
 	}
 	:   
		variableModifierTemp=variableModifiers	
		parameterType=type
		identifier1=IDENTIFIER {stringBuffer.append($identifier1.text);}
    ( '[' ']' {stringBuffer.append("[]");} )*
    {
      parameterLocal.setVariableModifier($variableModifierTemp.text);
      parameterLocal.setTypeParameter(parameterType);
      parameterLocal.setNameParameter(stringBuffer.toString());
    	normalParameter = parameterLocal;
    }
	;

ellipsisParameterDecl 
    :   variableModifiers
        type  '...'
        IDENTIFIER
    ;


explicitConstructorInvocation 
    :   (nonWildcardTypeArguments
        )?     //NOTE: the position of Identifier 'super' is set to the type args position here
        ('this'
        |'super'
        )
        arguments ';'

    |   primary
        '.'
        (nonWildcardTypeArguments
        )?
        'super'
        arguments ';'
    ;

qualifiedName 
    :   IDENTIFIER
        ('.' IDENTIFIER
        )*
    ;

annotations 
    :   (annotation
        )+
    ;

/**
 *  Using an annotation. 
 * '@' is flaged in modifier
 */
annotation 
    :   '@' qualifiedName
        (   '('   
                  (   elementValuePairs
                  |   elementValue
                  )? 
            ')' 
        )?
    ;

elementValuePairs 
    :   elementValuePair
        (',' elementValuePair
        )*
    ;

elementValuePair 
    :   IDENTIFIER '=' elementValue
    ;

elementValue 
    :   conditionalExpression
    |   annotation
    |   elementValueArrayInitializer
    ;

elementValueArrayInitializer 
    :   '{'
        (elementValue
            (',' elementValue
            )*
        )? (',')? '}'
    ;


/**
 * Annotation declaration.
 */
annotationTypeDeclaration 
@init{
  List<String> implementsList = new ArrayList<String>();
  List<String> extendsList = new ArrayList<String>();
  DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();
}    :   annotationDeclarationModifiers=modifiers 
        '@''interface'
        interfaceName=IDENTIFIER
        { addCurrentClassOrInterfaceNamePile($interfaceName.text); 
          declarationOfClassOrInterface.setCurrentPackage(getPackageName());
          declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
          declarationOfClassOrInterface.setJavadoc(getJavadocComment());
          declarationOfClassOrInterface.setModifiers(annotationDeclarationModifiers);
          declarationOfClassOrInterface.setInterface(true);
          declarationOfClassOrInterface.setClassOrInterfaceName($interfaceName.text);
          declarationOfClassOrInterface.setExtendsList(extendsList);
          declarationOfClassOrInterface.setImplementsList(implementsList);
          getBuilder().createClass(declarationOfClassOrInterface);  
        }
        annotationTypeBody
    ;


annotationTypeBody 
    :   '{' 
        (annotationTypeElementDeclaration
        )* 
        '}'
    ;

/**
 * NOTE: here use interfaceFieldDeclaration for field declared inside annotation. they are sytactically the same.
 */
annotationTypeElementDeclaration 
    :   annotationMethodDeclaration
    |   interfaceFieldDeclaration
    |   normalClassDeclaration
    |   normalInterfaceDeclaration
    |   enumDeclaration
    |   annotationTypeDeclaration
    |   ';'
    ;

annotationMethodDeclaration 
    :   modifiers 
        type 
        IDENTIFIER
        '(' ')' 
        ( 'default' 
          elementValue
        )?
        ';'
        ;

block returns [String bodyTextString = null]
@init{
    StringBuffer stringBuffer = new StringBuffer();
}  :  '{'
      ( bodyTemp = blockStatement { stringBuffer.append($bodyTemp.text + "\n"); } )*
      '}'
    { bodyTextString = stringBuffer.toString(); }
  ;

/*
staticBlock returns [JCBlock tree]
        @init {
            ListBuffer<JCStatement> stats = new ListBuffer<JCStatement>();
            int pos = ((AntlrJavacToken) $start).getStartIndex();
        }
        @after {
            $tree = T.at(pos).Block(Flags.STATIC, stats.toList());
            pu.storeEnd($tree, $stop);
            // construct a dummy static modifiers for end position
            pu.storeEnd(T.at(pos).Modifiers(Flags.STATIC,  com.sun.tools.javac.util.List.<JCAnnotation>nil()),$st);
        }
    :   st_1='static' '{' 
        (blockStatement
            {
                if ($blockStatement.tree == null) {
                    stats.appendList($blockStatement.list);
                } else {
                    stats.append($blockStatement.tree);
                }
            }
        )* '}'
    ;
*/
blockStatement 
    :   localVariableDeclarationStatement
    |   classOrInterfaceDeclaration
    |   statement
    ;


localVariableDeclarationStatement 
    :   localVariableDeclaration
        ';'
    ;

localVariableDeclaration 
    :   variableModifiers type
        variableDeclarator[(int)-1,null]
        (',' variableDeclarator[(int)-1,null]
        )*
    ;

statement 
    :   block
            
    |   ('assert'
        )
        expression (':' expression)? ';'
    |   'assert'  expression (':' expression)? ';'            
    |   'if' parExpression statement ('else' statement)?          
    |   forstatement
    |   'while' parExpression statement
    |   'do' statement 'while' parExpression ';'
    |   trystatement
    |   'switch' parExpression '{' switchBlockStatementGroups '}'
    |   'synchronized' parExpression block
    |   'return' (expression )? ';'
    |   'throw' expression ';'
    |   'break'
            (IDENTIFIER
            )? ';'
    |   'continue'
            (IDENTIFIER
            )? ';'
    |   expression  ';'     
    |   IDENTIFIER ':' statement
    |   ';'

    ;

switchBlockStatementGroups 
    :   (switchBlockStatementGroup )*
    ;

switchBlockStatementGroup 
    :
        switchLabel
        (blockStatement
        )*
    ;

switchLabel 
    :   'case' expression ':'
    |   'default' ':'
    ;


trystatement 
    :   'try' block
        (   catches 'finally' block
        |   catches
        |   'finally' block
        )
     ;

catches 
    :   catchClause
        (catchClause
        )*
    ;

catchClause 
    :   'catch' '(' formalParameter
        ')' block 
    ;

formalParameter 
    :   variableModifiers type IDENTIFIER
        ('[' ']'
        )*
    ;

forstatement 
    :   
        // enhanced for loop
        'for' '(' variableModifiers type IDENTIFIER ':' 
        expression ')' statement
            
        // normal for loop
    |   'for' '(' 
                (forInit
                )? ';' 
                (expression
                )? ';' 
                (expressionList
                )? ')' statement
    ;

forInit 
    :   localVariableDeclaration
    |   expressionList
    ;

parExpression 
    :   '(' expression ')'
    ;

expressionList 
    :   expression
        (',' expression
        )*
    ;


expression 
    :   conditionalExpression
        (assignmentOperator expression
        )?
    ;


assignmentOperator 
    :   '='
    |   '+='
    |   '-='
    |   '*='
    |   '/='
    |   '&='
    |   '|='
    |   '^='
    |   '%='
    |    '<' '<' '='
    |    '>' '>' '>' '='
    |    '>' '>' '='
    ;


conditionalExpression 
    :   conditionalOrExpression
        ('?' expression ':' conditionalExpression
        )?
    ;

conditionalOrExpression 
    :   conditionalAndExpression
        ('||' conditionalAndExpression
        )*
    ;

conditionalAndExpression 
    :   inclusiveOrExpression
        ('&&' inclusiveOrExpression
        )*
    ;

inclusiveOrExpression 
    :   exclusiveOrExpression
        ('|' exclusiveOrExpression
        )*
    ;

exclusiveOrExpression 
    :   andExpression
        ('^' andExpression
        )*
    ;

andExpression 
    :   equalityExpression
        ('&' equalityExpression
        )*
    ;

equalityExpression 
    :   instanceOfExpression
        (   
            (   '=='
            |   '!='
            )
            instanceOfExpression
        )*
    ;

instanceOfExpression 
    :   relationalExpression
        ('instanceof' type
        )?
    ;

relationalExpression 
    :   shiftExpression
        (relationalOp shiftExpression
        )*
    ;

relationalOp 
    :    '<' '='
    |    '>' '='
    |   '<'
    |   '>'
    ;

shiftExpression 
    :   additiveExpression
        (shiftOp additiveExpression
        )*
    ;


shiftOp 
    :    '<' '<'
    |    '>' '>' '>'
    |    '>' '>'
    ;


additiveExpression 
    :   multiplicativeExpression
        (   
            (   '+'
            |   '-'
            )
            multiplicativeExpression
         )*
    ;

multiplicativeExpression 
    :
        unaryExpression
        (   
            (   '*'
            |   '/'
            |   '%'
            )
            unaryExpression
        )*
    ;

/**
 * NOTE: for '+' and '-', if the next token is int or long interal, then it's not a unary expression.
 *       it's a literal with signed value. INTLTERAL AND LONG LITERAL are added here for this.
 */
unaryExpression 
    :   '+'  unaryExpression
    |   '-' unaryExpression
    |   '++' unaryExpression
    |   '--' unaryExpression
    |   unaryExpressionNotPlusMinus
    ;

unaryExpressionNotPlusMinus 
    :   '~' unaryExpression
    |   '!' unaryExpression
    |   castExpression
    |   primary
        (selector
        )*
        (   '++'
        |   '--'
        )?
    ;

castExpression 
    :   '(' primitiveType ')' unaryExpression
    |   '(' type ')' unaryExpressionNotPlusMinus
    ;

/**
 * have to use scope here, parameter passing isn't well supported in antlr.
 */
primary 
    :   parExpression            
    |   'this'
        ('.' IDENTIFIER
        )*
        (identifierSuffix
        )?
    |   IDENTIFIER
        ('.' IDENTIFIER
        )*
        (identifierSuffix
        )?
    |   'super'
        superSuffix
    |   literal
    |   creator
    |   primitiveType
        ('[' ']'
        )*
        '.' 'class'
    |   'void' '.' 'class'
    ;
    

superSuffix  
    :   arguments
    |   '.' (typeArguments
        )?
        IDENTIFIER
        (arguments
        )?
    ;


identifierSuffix 
    :   ('[' ']'
        )+
        '.' 'class'
    |   ('[' expression ']'
        )+
    |   arguments
    |   '.' 'class'
    |   '.' nonWildcardTypeArguments IDENTIFIER arguments
    |   '.' 'this'
    |   '.' 'super' arguments
    |   innerCreator
    ;


selector  
    :   '.' IDENTIFIER
        (arguments
        )?
    |   '.' 'this'
    |   '.' 'super'
        superSuffix
    |   innerCreator
    |   '[' expression ']'
    ;

creator 
    :   'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
    |   'new' classOrInterfaceType classCreatorRest
    |   arrayCreator
    ;

arrayCreator 
    :   'new' createdName
        '[' ']'
        ('[' ']'
        )*
        arrayInitializer

    |   'new' createdName
        '[' expression
        ']'
        (   '[' expression
            ']'
        )*
        ('[' ']'
        )*
    ;

variableInitializer 
    :   arrayInitializer
    |   expression
    ;

arrayInitializer 
    :   '{' 
            (variableInitializer
                (',' variableInitializer
                )*
            )? 
            (',')? 
        '}'             //Yang's fix, position change.
    ;


createdName 
    :   classOrInterfaceType
    |   primitiveType
    ;

innerCreator  
    :   '.' 'new'
        (nonWildcardTypeArguments
        )?
        IDENTIFIER
        (typeArguments
        )?
        classCreatorRest
    ;


classCreatorRest 
    :   arguments
        (classBody
        )?
    ;


nonWildcardTypeArguments 
    :   '<' typeList
        '>'
    ;

arguments 
    :   '(' (expressionList
        )? ')'
    ;

literal 
    :   INTLITERAL
    |   LONGLITERAL
    |   FLOATLITERAL
    |   DOUBLELITERAL
    |   CHARLITERAL
    |   STRINGLITERAL
    |   TRUE
    |   FALSE
    |   NULL
    ;

/**
 * These are headers help to make syntatical predicates, not necessary but helps to make grammar faster.
 */
 
classHeader 
    :   modifiers 'class' IDENTIFIER
    ;

enumHeader 
    :   modifiers ('enum'|IDENTIFIER) IDENTIFIER
    ;

interfaceHeader 
    :   modifiers 'interface' IDENTIFIER
    ;

annotationHeader 
    :   modifiers '@' 'interface' IDENTIFIER
    ;

typeHeader 
    :   modifiers ('class'|'enum'|('@' ? 'interface')) IDENTIFIER
    ;

methodHeader 
    :   modifiers typeParameters? (type|'void')? IDENTIFIER '('
    ;

fieldHeader 
    :   modifiers type IDENTIFIER ('['']')* ('='|','|';')
    ;

localVariableHeader 
    :   variableModifiers type IDENTIFIER ('['']')* ('='|','|';')
    ;




/********************************************************************************************
                  Lexer section
*********************************************************************************************/

LONGLITERAL
    :   IntegerNumber LongSuffix
    ;

    
INTLITERAL
    :   IntegerNumber 
    ;
    
fragment
IntegerNumber
    :   '0' 
    |   '1'..'9' ('0'..'9')*    
    |   '0' ('0'..'7')+         
    |   HexPrefix HexDigit+        
    ;

fragment
HexPrefix
    :   '0x' | '0X'
    ;
        
fragment
HexDigit
    :   ('0'..'9'|'a'..'f'|'A'..'F')
    ;

fragment
LongSuffix
    :   'l' | 'L'
    ;


fragment
NonIntegerNumber
    :   ('0' .. '9')+ '.' ('0' .. '9')* Exponent?  
    |   '.' ( '0' .. '9' )+ Exponent?  
    |   ('0' .. '9')+ Exponent  
    |   ('0' .. '9')+ 
    |   
        HexPrefix (HexDigit )* 
        (    () 
        |    ('.' (HexDigit )* ) 
        ) 
        ( 'p' | 'P' ) 
        ( '+' | '-' )? 
        ( '0' .. '9' )+
        ;
        
fragment 
Exponent    
    :   ( 'e' | 'E' ) ( '+' | '-' )? ( '0' .. '9' )+ 
    ;
    
fragment 
FloatSuffix
    :   'f' | 'F' 
    ;     

fragment
DoubleSuffix
    :   'd' | 'D'
    ;
        
FLOATLITERAL
    :   NonIntegerNumber FloatSuffix
    ;
    
DOUBLELITERAL
    :   NonIntegerNumber DoubleSuffix?
    ;

CHARLITERAL
    :   '\'' 
        (   EscapeSequence 
        |   ~( '\'' | '\\' | '\r' | '\n' )
        ) 
        '\''
    ; 

STRINGLITERAL
    :   '"' 
        (   EscapeSequence
        |   ~( '\\' | '"' | '\r' | '\n' )        
        )* 
        '"' 
    ;

fragment
EscapeSequence 
    :   '\\' (
                 'b' 
             |   't' 
             |   'n' 
             |   'f' 
             |   'r' 
             |   '\"' 
             |   '\'' 
             |   '\\' 
             |       
                 ('0'..'3') ('0'..'7') ('0'..'7')
             |       
                 ('0'..'7') ('0'..'7') 
             |       
                 ('0'..'7')
             )          
;     

WS  
  : ( 
    ' '
    | '\n'
    | '\r' 
    | '\t'  
    | '\u000C' 
    ) 
    {$channel=HIDDEN;}          
    ;
    
COMMENT
@init{
  boolean isJavaDoc = false;
}
    :   '/*'
            {
                if((char)input.LA(1) == '*'){
                    isJavaDoc = true;
                }
            }
        (options {greedy=false;} : . )* 
        '*/'
            {
                if(isJavaDoc==true){
                    $channel=HIDDEN;
                }else{
                    skip();
                }
            }
    ;

LINE_COMMENT
    :   '//' ~('\n'|'\r')*  ('\r\n' | '\r' | '\n') 
            {
                skip();
            }
    |   '//' ~('\n'|'\r')*     // a line comment could appear at the end of the file without CR/LF
            {
                skip();
            }
    ;   
        
ABSTRACT
    :   'abstract'
    ;
    
ASSERT
    :   'assert'
    ;
    
BOOLEAN
    :   'boolean'
    ;
    
BREAK
    :   'break'
    ;
    
BYTE
    :   'byte'
    ;
    
CASE
    :   'case'
    ;
    
CATCH
    :   'catch'
    ;
    
CHAR
    :   'char'
    ;
    
CLASS
    :   'class'
    ;
    
CONST
    :   'const'
    ;

CONTINUE
    :   'continue'
    ;

DEFAULT
    :   'default'
    ;

DO
    :   'do'
    ;

DOUBLE
    :   'double'
    ;

ELSE
    :   'else'
    ;

ENUM
    :   'enum'
    ;             

EXTENDS
    :   'extends'
    ;

FINAL
    :   'final'
    ;

FINALLY
    :   'finally'
    ;

FLOAT
    :   'float'
    ;

FOR
    :   'for'
    ;

GOTO
    :   'goto'
    ;

IF
    :   'if'
    ;

IMPLEMENTS
    :   'implements'
    ;

IMPORT
    :   'import'
    ;

INSTANCEOF
    :   'instanceof'
    ;

INT
    :   'int'
    ;

INTERFACE
    :   'interface'
    ;

LONG
    :   'long'
    ;

NATIVE
    :   'native'
    ;

NEW
    :   'new'
    ;

PACKAGE
    :   'package'
    ;

PRIVATE
    :   'private'
    ;

PROTECTED
    :   'protected'
    ;

PUBLIC
    :   'public'
    ;

RETURN
    :   'return'
    ;

SHORT
    :   'short'
    ;

STATIC
    :   'static'
    ;

STRICTFP
    :   'strictfp'
    ;

SUPER
    :   'super'
    ;

SWITCH
    :   'switch'
    ;

SYNCHRONIZED
    :   'synchronized'
    ;

THIS
    :   'this'
    ;

THROW
    :   'throw'
    ;

THROWS
    :   'throws'
    ;

TRANSIENT
    :   'transient'
    ;

TRY
    :   'try'
    ;

VOID
    :   'void'
    ;

VOLATILE
    :   'volatile'
    ;

WHILE
    :   'while'
    ;

TRUE
    :   'true'
    ;

FALSE
    :   'false'
    ;

NULL
    :   'null'
    ;

LPAREN
    :   '('
    ;

RPAREN
    :   ')'
    ;

LBRACE
    :   '{'
    ;

RBRACE
    :   '}'
    ;

LBRACKET
    :   '['
    ;

RBRACKET
    :   ']'
    ;

SEMI
    :   ';'
    ;

COMMA
    :   ','
    ;

DOT
    :   '.'
    ;

ELLIPSIS
    :   '...'
    ;

EQ
    :   '='
    ;

BANG
    :   '!'
    ;

TILDE
    :   '~'
    ;

QUES
    :   '?'
    ;

COLON
    :   ':'
    ;

EQEQ
    :   '=='
    ;

AMPAMP
    :   '&&'
    ;

BARBAR
    :   '||'
    ;

PLUSPLUS
    :   '++'
    ;

SUBSUB
    :   '--'
    ;

PLUS
    :   '+'
    ;

SUB
    :   '-'
    ;

STAR
    :   '*'
    ;

SLASH
    :   '/'
    ;

AMP
    :   '&'
    ;

BAR
    :   '|'
    ;

CARET
    :   '^'
    ;

PERCENT
    :   '%'
    ;

PLUSEQ
    :   '+='
    ; 
    
SUBEQ
    :   '-='
    ;

STAREQ
    :   '*='
    ;

SLASHEQ
    :   '/='
    ;

AMPEQ
    :   '&='
    ;

BAREQ
    :   '|='
    ;

CARETEQ
    :   '^='
    ;

PERCENTEQ
    :   '%='
    ;

MONKEYS_AT
    :   '@'
    ;

BANGEQ
    :   '!='
    ;

GT
    :   '>'
    ;

LT
    :   '<'
    ;        
              
IDENTIFIER
    :   IdentifierStart IdentifierPart*
    ;

fragment
SurrogateIdentifer 
    :   ('\ud800'..'\udbff') ('\udc00'..'\udfff') 
    ;                 

fragment
IdentifierStart
    :   '\u0024'
    |   '\u0041'..'\u005a'
    |   '\u005f'
    |   '\u0061'..'\u007a'
    |   '\u00a2'..'\u00a5'
    |   '\u00aa'
    |   '\u00b5'
    |   '\u00ba'
    |   '\u00c0'..'\u00d6'
    |   '\u00d8'..'\u00f6'
    |   '\u00f8'..'\u0236'
    |   '\u0250'..'\u02c1'
    |   '\u02c6'..'\u02d1'
    |   '\u02e0'..'\u02e4'
    |   '\u02ee'
    |   '\u037a'
    |   '\u0386'
    |   '\u0388'..'\u038a'
    |   '\u038c'
    |   '\u038e'..'\u03a1'
    |   '\u03a3'..'\u03ce'
    |   '\u03d0'..'\u03f5'
    |   '\u03f7'..'\u03fb'
    |   '\u0400'..'\u0481'
    |   '\u048a'..'\u04ce'
    |   '\u04d0'..'\u04f5'
    |   '\u04f8'..'\u04f9'
    |   '\u0500'..'\u050f'
    |   '\u0531'..'\u0556'
    |   '\u0559'
    |   '\u0561'..'\u0587'
    |   '\u05d0'..'\u05ea'
    |   '\u05f0'..'\u05f2'
    |   '\u0621'..'\u063a'
    |   '\u0640'..'\u064a'
    |   '\u066e'..'\u066f'
    |   '\u0671'..'\u06d3'
    |   '\u06d5'
    |   '\u06e5'..'\u06e6'
    |   '\u06ee'..'\u06ef'
    |   '\u06fa'..'\u06fc'
    |   '\u06ff'
    |   '\u0710'
    |   '\u0712'..'\u072f'
    |   '\u074d'..'\u074f'
    |   '\u0780'..'\u07a5'
    |   '\u07b1'
    |   '\u0904'..'\u0939'
    |   '\u093d'
    |   '\u0950'
    |   '\u0958'..'\u0961'
    |   '\u0985'..'\u098c'
    |   '\u098f'..'\u0990'
    |   '\u0993'..'\u09a8'
    |   '\u09aa'..'\u09b0'
    |   '\u09b2'
    |   '\u09b6'..'\u09b9'
    |   '\u09bd'
    |   '\u09dc'..'\u09dd'
    |   '\u09df'..'\u09e1'
    |   '\u09f0'..'\u09f3'
    |   '\u0a05'..'\u0a0a'
    |   '\u0a0f'..'\u0a10'
    |   '\u0a13'..'\u0a28'
    |   '\u0a2a'..'\u0a30'
    |   '\u0a32'..'\u0a33'
    |   '\u0a35'..'\u0a36'
    |   '\u0a38'..'\u0a39'
    |   '\u0a59'..'\u0a5c'
    |   '\u0a5e'
    |   '\u0a72'..'\u0a74'
    |   '\u0a85'..'\u0a8d'
    |   '\u0a8f'..'\u0a91'
    |   '\u0a93'..'\u0aa8'
    |   '\u0aaa'..'\u0ab0'
    |   '\u0ab2'..'\u0ab3'
    |   '\u0ab5'..'\u0ab9'
    |   '\u0abd'
    |   '\u0ad0'
    |   '\u0ae0'..'\u0ae1'
    |   '\u0af1'
    |   '\u0b05'..'\u0b0c'
    |   '\u0b0f'..'\u0b10'
    |   '\u0b13'..'\u0b28'
    |   '\u0b2a'..'\u0b30'
    |   '\u0b32'..'\u0b33'
    |   '\u0b35'..'\u0b39'
    |   '\u0b3d'
    |   '\u0b5c'..'\u0b5d'
    |   '\u0b5f'..'\u0b61'
    |   '\u0b71'
    |   '\u0b83'
    |   '\u0b85'..'\u0b8a'
    |   '\u0b8e'..'\u0b90'
    |   '\u0b92'..'\u0b95'
    |   '\u0b99'..'\u0b9a'
    |   '\u0b9c'
    |   '\u0b9e'..'\u0b9f'
    |   '\u0ba3'..'\u0ba4'
    |   '\u0ba8'..'\u0baa'
    |   '\u0bae'..'\u0bb5'
    |   '\u0bb7'..'\u0bb9'
    |   '\u0bf9'
    |   '\u0c05'..'\u0c0c'
    |   '\u0c0e'..'\u0c10'
    |   '\u0c12'..'\u0c28'
    |   '\u0c2a'..'\u0c33'
    |   '\u0c35'..'\u0c39'
    |   '\u0c60'..'\u0c61'
    |   '\u0c85'..'\u0c8c'
    |   '\u0c8e'..'\u0c90'
    |   '\u0c92'..'\u0ca8'
    |   '\u0caa'..'\u0cb3'
    |   '\u0cb5'..'\u0cb9'
    |   '\u0cbd'
    |   '\u0cde'
    |   '\u0ce0'..'\u0ce1'
    |   '\u0d05'..'\u0d0c'
    |   '\u0d0e'..'\u0d10'
    |   '\u0d12'..'\u0d28'
    |   '\u0d2a'..'\u0d39'
    |   '\u0d60'..'\u0d61'
    |   '\u0d85'..'\u0d96'
    |   '\u0d9a'..'\u0db1'
    |   '\u0db3'..'\u0dbb'
    |   '\u0dbd'
    |   '\u0dc0'..'\u0dc6'
    |   '\u0e01'..'\u0e30'
    |   '\u0e32'..'\u0e33'
    |   '\u0e3f'..'\u0e46'
    |   '\u0e81'..'\u0e82'
    |   '\u0e84'
    |   '\u0e87'..'\u0e88'
    |   '\u0e8a'
    |   '\u0e8d'
    |   '\u0e94'..'\u0e97'
    |   '\u0e99'..'\u0e9f'
    |   '\u0ea1'..'\u0ea3'
    |   '\u0ea5'
    |   '\u0ea7'
    |   '\u0eaa'..'\u0eab'
    |   '\u0ead'..'\u0eb0'
    |   '\u0eb2'..'\u0eb3'
    |   '\u0ebd'
    |   '\u0ec0'..'\u0ec4'
    |   '\u0ec6'
    |   '\u0edc'..'\u0edd'
    |   '\u0f00'
    |   '\u0f40'..'\u0f47'
    |   '\u0f49'..'\u0f6a'
    |   '\u0f88'..'\u0f8b'
    |   '\u1000'..'\u1021'
    |   '\u1023'..'\u1027'
    |   '\u1029'..'\u102a'
    |   '\u1050'..'\u1055'
    |   '\u10a0'..'\u10c5'
    |   '\u10d0'..'\u10f8'
    |   '\u1100'..'\u1159'
    |   '\u115f'..'\u11a2'
    |   '\u11a8'..'\u11f9'
    |   '\u1200'..'\u1206'
    |   '\u1208'..'\u1246'
    |   '\u1248'
    |   '\u124a'..'\u124d'
    |   '\u1250'..'\u1256'
    |   '\u1258'
    |   '\u125a'..'\u125d'
    |   '\u1260'..'\u1286'
    |   '\u1288'
    |   '\u128a'..'\u128d'
    |   '\u1290'..'\u12ae'
    |   '\u12b0'
    |   '\u12b2'..'\u12b5'
    |   '\u12b8'..'\u12be'
    |   '\u12c0'
    |   '\u12c2'..'\u12c5'
    |   '\u12c8'..'\u12ce'
    |   '\u12d0'..'\u12d6'
    |   '\u12d8'..'\u12ee'
    |   '\u12f0'..'\u130e'
    |   '\u1310'
    |   '\u1312'..'\u1315'
    |   '\u1318'..'\u131e'
    |   '\u1320'..'\u1346'
    |   '\u1348'..'\u135a'
    |   '\u13a0'..'\u13f4'
    |   '\u1401'..'\u166c'
    |   '\u166f'..'\u1676'
    |   '\u1681'..'\u169a'
    |   '\u16a0'..'\u16ea'
    |   '\u16ee'..'\u16f0'
    |   '\u1700'..'\u170c'
    |   '\u170e'..'\u1711'
    |   '\u1720'..'\u1731'
    |   '\u1740'..'\u1751'
    |   '\u1760'..'\u176c'
    |   '\u176e'..'\u1770'
    |   '\u1780'..'\u17b3'
    |   '\u17d7' 
    |   '\u17db'..'\u17dc'
    |   '\u1820'..'\u1877'
    |   '\u1880'..'\u18a8'
    |   '\u1900'..'\u191c'
    |   '\u1950'..'\u196d'
    |   '\u1970'..'\u1974'
    |   '\u1d00'..'\u1d6b'
    |   '\u1e00'..'\u1e9b'
    |   '\u1ea0'..'\u1ef9'
    |   '\u1f00'..'\u1f15'
    |   '\u1f18'..'\u1f1d'
    |   '\u1f20'..'\u1f45'
    |   '\u1f48'..'\u1f4d'
    |   '\u1f50'..'\u1f57'
    |   '\u1f59'
    |   '\u1f5b'
    |   '\u1f5d'
    |   '\u1f5f'..'\u1f7d'
    |   '\u1f80'..'\u1fb4'
    |   '\u1fb6'..'\u1fbc'
    |   '\u1fbe'
    |   '\u1fc2'..'\u1fc4'
    |   '\u1fc6'..'\u1fcc'
    |   '\u1fd0'..'\u1fd3'
    |   '\u1fd6'..'\u1fdb'
    |   '\u1fe0'..'\u1fec'
    |   '\u1ff2'..'\u1ff4'
    |   '\u1ff6'..'\u1ffc'
    |   '\u203f'..'\u2040'
    |   '\u2054'
    |   '\u2071'
    |   '\u207f'
    |   '\u20a0'..'\u20b1'
    |   '\u2102'
    |   '\u2107'
    |   '\u210a'..'\u2113'
    |   '\u2115'
    |   '\u2119'..'\u211d'
    |   '\u2124'
    |   '\u2126'
    |   '\u2128'
    |   '\u212a'..'\u212d'
    |   '\u212f'..'\u2131'
    |   '\u2133'..'\u2139'
    |   '\u213d'..'\u213f'
    |   '\u2145'..'\u2149'
    |   '\u2160'..'\u2183'
    |   '\u3005'..'\u3007'
    |   '\u3021'..'\u3029'
    |   '\u3031'..'\u3035'
    |   '\u3038'..'\u303c'
    |   '\u3041'..'\u3096'
    |   '\u309d'..'\u309f'
    |   '\u30a1'..'\u30ff'
    |   '\u3105'..'\u312c'
    |   '\u3131'..'\u318e'
    |   '\u31a0'..'\u31b7'
    |   '\u31f0'..'\u31ff'
    |   '\u3400'..'\u4db5'
    |   '\u4e00'..'\u9fa5'
    |   '\ua000'..'\ua48c'
    |   '\uac00'..'\ud7a3'
    |   '\uf900'..'\ufa2d'
    |   '\ufa30'..'\ufa6a'
    |   '\ufb00'..'\ufb06'
    |   '\ufb13'..'\ufb17'
    |   '\ufb1d'
    |   '\ufb1f'..'\ufb28'
    |   '\ufb2a'..'\ufb36'
    |   '\ufb38'..'\ufb3c'
    |   '\ufb3e'
    |   '\ufb40'..'\ufb41'
    |   '\ufb43'..'\ufb44'
    |   '\ufb46'..'\ufbb1'
    |   '\ufbd3'..'\ufd3d'
    |   '\ufd50'..'\ufd8f'
    |   '\ufd92'..'\ufdc7'
    |   '\ufdf0'..'\ufdfc'
    |   '\ufe33'..'\ufe34'
    |   '\ufe4d'..'\ufe4f'
    |   '\ufe69'
    |   '\ufe70'..'\ufe74'
    |   '\ufe76'..'\ufefc'
    |   '\uff04'
    |   '\uff21'..'\uff3a'
    |   '\uff3f'
    |   '\uff41'..'\uff5a'
    |   '\uff65'..'\uffbe'
    |   '\uffc2'..'\uffc7'
    |   '\uffca'..'\uffcf'
    |   '\uffd2'..'\uffd7'
    |   '\uffda'..'\uffdc'
    |   '\uffe0'..'\uffe1'
    |   '\uffe5'..'\uffe6'
    |   ('\ud800'..'\udbff') ('\udc00'..'\udfff') 
    ;                
                       
fragment 
IdentifierPart
    :   '\u0000'..'\u0008'
    |   '\u000e'..'\u001b'
    |   '\u0024'
    |   '\u0030'..'\u0039'
    |   '\u0041'..'\u005a'
    |   '\u005f'
    |   '\u0061'..'\u007a'
    |   '\u007f'..'\u009f'
    |   '\u00a2'..'\u00a5'
    |   '\u00aa'
    |   '\u00ad'
    |   '\u00b5'
    |   '\u00ba'
    |   '\u00c0'..'\u00d6'
    |   '\u00d8'..'\u00f6'
    |   '\u00f8'..'\u0236'
    |   '\u0250'..'\u02c1'
    |   '\u02c6'..'\u02d1'
    |   '\u02e0'..'\u02e4'
    |   '\u02ee'
    |   '\u0300'..'\u0357'
    |   '\u035d'..'\u036f'
    |   '\u037a'
    |   '\u0386'
    |   '\u0388'..'\u038a'
    |   '\u038c'
    |   '\u038e'..'\u03a1'
    |   '\u03a3'..'\u03ce'
    |   '\u03d0'..'\u03f5'
    |   '\u03f7'..'\u03fb'
    |   '\u0400'..'\u0481'
    |   '\u0483'..'\u0486'
    |   '\u048a'..'\u04ce'
    |   '\u04d0'..'\u04f5'
    |   '\u04f8'..'\u04f9'
    |   '\u0500'..'\u050f'
    |   '\u0531'..'\u0556'
    |   '\u0559'
    |   '\u0561'..'\u0587'
    |   '\u0591'..'\u05a1'
    |   '\u05a3'..'\u05b9'
    |   '\u05bb'..'\u05bd'
    |   '\u05bf'
    |   '\u05c1'..'\u05c2'
    |   '\u05c4'
    |   '\u05d0'..'\u05ea'
    |   '\u05f0'..'\u05f2'
    |   '\u0600'..'\u0603'
    |   '\u0610'..'\u0615'
    |   '\u0621'..'\u063a'
    |   '\u0640'..'\u0658'
    |   '\u0660'..'\u0669'
    |   '\u066e'..'\u06d3'
    |   '\u06d5'..'\u06dd'
    |   '\u06df'..'\u06e8'
    |   '\u06ea'..'\u06fc'
    |   '\u06ff'
    |   '\u070f'..'\u074a'
    |   '\u074d'..'\u074f'
    |   '\u0780'..'\u07b1'
    |   '\u0901'..'\u0939'
    |   '\u093c'..'\u094d'
    |   '\u0950'..'\u0954'
    |   '\u0958'..'\u0963'
    |   '\u0966'..'\u096f'
    |   '\u0981'..'\u0983'
    |   '\u0985'..'\u098c'
    |   '\u098f'..'\u0990'
    |   '\u0993'..'\u09a8'
    |   '\u09aa'..'\u09b0'
    |   '\u09b2'
    |   '\u09b6'..'\u09b9'
    |   '\u09bc'..'\u09c4'
    |   '\u09c7'..'\u09c8'
    |   '\u09cb'..'\u09cd'
    |   '\u09d7'
    |   '\u09dc'..'\u09dd'
    |   '\u09df'..'\u09e3'
    |   '\u09e6'..'\u09f3'
    |   '\u0a01'..'\u0a03'
    |   '\u0a05'..'\u0a0a'
    |   '\u0a0f'..'\u0a10'
    |   '\u0a13'..'\u0a28'
    |   '\u0a2a'..'\u0a30'
    |   '\u0a32'..'\u0a33'
    |   '\u0a35'..'\u0a36'
    |   '\u0a38'..'\u0a39'
    |   '\u0a3c'
    |   '\u0a3e'..'\u0a42'
    |   '\u0a47'..'\u0a48'
    |   '\u0a4b'..'\u0a4d'
    |   '\u0a59'..'\u0a5c'
    |   '\u0a5e'
    |   '\u0a66'..'\u0a74'
    |   '\u0a81'..'\u0a83'
    |   '\u0a85'..'\u0a8d'
    |   '\u0a8f'..'\u0a91'
    |   '\u0a93'..'\u0aa8'
    |   '\u0aaa'..'\u0ab0'
    |   '\u0ab2'..'\u0ab3'
    |   '\u0ab5'..'\u0ab9'
    |   '\u0abc'..'\u0ac5'
    |   '\u0ac7'..'\u0ac9'
    |   '\u0acb'..'\u0acd'
    |   '\u0ad0'
    |   '\u0ae0'..'\u0ae3'
    |   '\u0ae6'..'\u0aef'
    |   '\u0af1'
    |   '\u0b01'..'\u0b03'
    |   '\u0b05'..'\u0b0c'        
    |   '\u0b0f'..'\u0b10'
    |   '\u0b13'..'\u0b28'
    |   '\u0b2a'..'\u0b30'
    |   '\u0b32'..'\u0b33'
    |   '\u0b35'..'\u0b39'
    |   '\u0b3c'..'\u0b43'
    |   '\u0b47'..'\u0b48'
    |   '\u0b4b'..'\u0b4d'
    |   '\u0b56'..'\u0b57'
    |   '\u0b5c'..'\u0b5d'
    |   '\u0b5f'..'\u0b61'
    |   '\u0b66'..'\u0b6f'
    |   '\u0b71'
    |   '\u0b82'..'\u0b83'
    |   '\u0b85'..'\u0b8a'
    |   '\u0b8e'..'\u0b90'
    |   '\u0b92'..'\u0b95'
    |   '\u0b99'..'\u0b9a'
    |   '\u0b9c'
    |   '\u0b9e'..'\u0b9f'
    |   '\u0ba3'..'\u0ba4'
    |   '\u0ba8'..'\u0baa'
    |   '\u0bae'..'\u0bb5'
    |   '\u0bb7'..'\u0bb9'
    |   '\u0bbe'..'\u0bc2'
    |   '\u0bc6'..'\u0bc8'
    |   '\u0bca'..'\u0bcd'
    |   '\u0bd7'
    |   '\u0be7'..'\u0bef'
    |   '\u0bf9'
    |   '\u0c01'..'\u0c03'
    |   '\u0c05'..'\u0c0c'
    |   '\u0c0e'..'\u0c10'
    |   '\u0c12'..'\u0c28'
    |   '\u0c2a'..'\u0c33'
    |   '\u0c35'..'\u0c39'
    |   '\u0c3e'..'\u0c44'
    |   '\u0c46'..'\u0c48'
    |   '\u0c4a'..'\u0c4d'
    |   '\u0c55'..'\u0c56'
    |   '\u0c60'..'\u0c61'
    |   '\u0c66'..'\u0c6f'        
    |   '\u0c82'..'\u0c83'
    |   '\u0c85'..'\u0c8c'
    |   '\u0c8e'..'\u0c90'
    |   '\u0c92'..'\u0ca8'
    |   '\u0caa'..'\u0cb3'
    |   '\u0cb5'..'\u0cb9'
    |   '\u0cbc'..'\u0cc4'
    |   '\u0cc6'..'\u0cc8'
    |   '\u0cca'..'\u0ccd'
    |   '\u0cd5'..'\u0cd6'
    |   '\u0cde'
    |   '\u0ce0'..'\u0ce1'
    |   '\u0ce6'..'\u0cef'
    |   '\u0d02'..'\u0d03'
    |   '\u0d05'..'\u0d0c'
    |   '\u0d0e'..'\u0d10'
    |   '\u0d12'..'\u0d28'
    |   '\u0d2a'..'\u0d39'
    |   '\u0d3e'..'\u0d43'
    |   '\u0d46'..'\u0d48'
    |   '\u0d4a'..'\u0d4d'
    |   '\u0d57'
    |   '\u0d60'..'\u0d61'
    |   '\u0d66'..'\u0d6f'
    |   '\u0d82'..'\u0d83'
    |   '\u0d85'..'\u0d96'
    |   '\u0d9a'..'\u0db1'
    |   '\u0db3'..'\u0dbb'
    |   '\u0dbd'
    |   '\u0dc0'..'\u0dc6'
    |   '\u0dca'
    |   '\u0dcf'..'\u0dd4'
    |   '\u0dd6'
    |   '\u0dd8'..'\u0ddf'
    |   '\u0df2'..'\u0df3'
    |   '\u0e01'..'\u0e3a'
    |   '\u0e3f'..'\u0e4e'
    |   '\u0e50'..'\u0e59'
    |   '\u0e81'..'\u0e82'
    |   '\u0e84'
    |   '\u0e87'..'\u0e88'        
    |   '\u0e8a'
    |   '\u0e8d'
    |   '\u0e94'..'\u0e97'
    |   '\u0e99'..'\u0e9f'
    |   '\u0ea1'..'\u0ea3'
    |   '\u0ea5'
    |   '\u0ea7'
    |   '\u0eaa'..'\u0eab'
    |   '\u0ead'..'\u0eb9'
    |   '\u0ebb'..'\u0ebd'
    |   '\u0ec0'..'\u0ec4'
    |   '\u0ec6'
    |   '\u0ec8'..'\u0ecd'
    |   '\u0ed0'..'\u0ed9'
    |   '\u0edc'..'\u0edd'
    |   '\u0f00'
    |   '\u0f18'..'\u0f19'
    |   '\u0f20'..'\u0f29'
    |   '\u0f35'
    |   '\u0f37'
    |   '\u0f39'
    |   '\u0f3e'..'\u0f47'
    |   '\u0f49'..'\u0f6a'
    |   '\u0f71'..'\u0f84'
    |   '\u0f86'..'\u0f8b'
    |   '\u0f90'..'\u0f97'
    |   '\u0f99'..'\u0fbc'
    |   '\u0fc6'
    |   '\u1000'..'\u1021'
    |   '\u1023'..'\u1027'
    |   '\u1029'..'\u102a'
    |   '\u102c'..'\u1032'
    |   '\u1036'..'\u1039'
    |   '\u1040'..'\u1049'
    |   '\u1050'..'\u1059'
    |   '\u10a0'..'\u10c5'
    |   '\u10d0'..'\u10f8'
    |   '\u1100'..'\u1159'
    |   '\u115f'..'\u11a2'
    |   '\u11a8'..'\u11f9'
    |   '\u1200'..'\u1206'        
    |   '\u1208'..'\u1246'
    |   '\u1248'
    |   '\u124a'..'\u124d'
    |   '\u1250'..'\u1256'
    |   '\u1258'
    |   '\u125a'..'\u125d'
    |   '\u1260'..'\u1286'
    |   '\u1288'        
    |   '\u128a'..'\u128d'
    |   '\u1290'..'\u12ae'
    |   '\u12b0'
    |   '\u12b2'..'\u12b5'
    |   '\u12b8'..'\u12be'
    |   '\u12c0'
    |   '\u12c2'..'\u12c5'
    |   '\u12c8'..'\u12ce'
    |   '\u12d0'..'\u12d6'
    |   '\u12d8'..'\u12ee'
    |   '\u12f0'..'\u130e'
    |   '\u1310'
    |   '\u1312'..'\u1315'
    |   '\u1318'..'\u131e'
    |   '\u1320'..'\u1346'
    |   '\u1348'..'\u135a'
    |   '\u1369'..'\u1371'
    |   '\u13a0'..'\u13f4'
    |   '\u1401'..'\u166c'
    |   '\u166f'..'\u1676'
    |   '\u1681'..'\u169a'
    |   '\u16a0'..'\u16ea'
    |   '\u16ee'..'\u16f0'
    |   '\u1700'..'\u170c'
    |   '\u170e'..'\u1714'
    |   '\u1720'..'\u1734'
    |   '\u1740'..'\u1753'
    |   '\u1760'..'\u176c'
    |   '\u176e'..'\u1770'
    |   '\u1772'..'\u1773'
    |   '\u1780'..'\u17d3'
    |   '\u17d7'
    |   '\u17db'..'\u17dd'
    |   '\u17e0'..'\u17e9'
    |   '\u180b'..'\u180d'
    |   '\u1810'..'\u1819'
    |   '\u1820'..'\u1877'
    |   '\u1880'..'\u18a9'
    |   '\u1900'..'\u191c'
    |   '\u1920'..'\u192b'
    |   '\u1930'..'\u193b'
    |   '\u1946'..'\u196d'
    |   '\u1970'..'\u1974'
    |   '\u1d00'..'\u1d6b'
    |   '\u1e00'..'\u1e9b'
    |   '\u1ea0'..'\u1ef9'
    |   '\u1f00'..'\u1f15'
    |   '\u1f18'..'\u1f1d'
    |   '\u1f20'..'\u1f45'
    |   '\u1f48'..'\u1f4d'
    |   '\u1f50'..'\u1f57'
    |   '\u1f59'
    |   '\u1f5b'
    |   '\u1f5d'
    |   '\u1f5f'..'\u1f7d'
    |   '\u1f80'..'\u1fb4'
    |   '\u1fb6'..'\u1fbc'        
    |   '\u1fbe'
    |   '\u1fc2'..'\u1fc4'
    |   '\u1fc6'..'\u1fcc'
    |   '\u1fd0'..'\u1fd3'
    |   '\u1fd6'..'\u1fdb'
    |   '\u1fe0'..'\u1fec'
    |   '\u1ff2'..'\u1ff4'
    |   '\u1ff6'..'\u1ffc'
    |   '\u200c'..'\u200f'
    |   '\u202a'..'\u202e'
    |   '\u203f'..'\u2040'
    |   '\u2054'
    |   '\u2060'..'\u2063'
    |   '\u206a'..'\u206f'
    |   '\u2071'
    |   '\u207f'
    |   '\u20a0'..'\u20b1'
    |   '\u20d0'..'\u20dc'
    |   '\u20e1'
    |   '\u20e5'..'\u20ea'
    |   '\u2102'
    |   '\u2107'
    |   '\u210a'..'\u2113'
    |   '\u2115'
    |   '\u2119'..'\u211d'
    |   '\u2124'
    |   '\u2126'
    |   '\u2128'
    |   '\u212a'..'\u212d'
    |   '\u212f'..'\u2131'
    |   '\u2133'..'\u2139'
    |   '\u213d'..'\u213f'
    |   '\u2145'..'\u2149'
    |   '\u2160'..'\u2183'
    |   '\u3005'..'\u3007'
    |   '\u3021'..'\u302f'        
    |   '\u3031'..'\u3035'
    |   '\u3038'..'\u303c'
    |   '\u3041'..'\u3096'
    |   '\u3099'..'\u309a'
    |   '\u309d'..'\u309f'
    |   '\u30a1'..'\u30ff'
    |   '\u3105'..'\u312c'
    |   '\u3131'..'\u318e'
    |   '\u31a0'..'\u31b7'
    |   '\u31f0'..'\u31ff'
    |   '\u3400'..'\u4db5'
    |   '\u4e00'..'\u9fa5'
    |   '\ua000'..'\ua48c'
    |   '\uac00'..'\ud7a3'
    |   '\uf900'..'\ufa2d'
    |   '\ufa30'..'\ufa6a'
    |   '\ufb00'..'\ufb06'
    |   '\ufb13'..'\ufb17'
    |   '\ufb1d'..'\ufb28'
    |   '\ufb2a'..'\ufb36'
    |   '\ufb38'..'\ufb3c'
    |   '\ufb3e'
    |   '\ufb40'..'\ufb41'
    |   '\ufb43'..'\ufb44'
    |   '\ufb46'..'\ufbb1'
    |   '\ufbd3'..'\ufd3d'
    |   '\ufd50'..'\ufd8f'
    |   '\ufd92'..'\ufdc7'
    |   '\ufdf0'..'\ufdfc'
    |   '\ufe00'..'\ufe0f'
    |   '\ufe20'..'\ufe23'
    |   '\ufe33'..'\ufe34'
    |   '\ufe4d'..'\ufe4f'
    |   '\ufe69'
    |   '\ufe70'..'\ufe74'
    |   '\ufe76'..'\ufefc'
    |   '\ufeff'
    |   '\uff04'
    |   '\uff10'..'\uff19'
    |   '\uff21'..'\uff3a'
    |   '\uff3f'
    |   '\uff41'..'\uff5a'
    |   '\uff65'..'\uffbe'
    |   '\uffc2'..'\uffc7'
    |   '\uffca'..'\uffcf'
    |   '\uffd2'..'\uffd7'
    |   '\uffda'..'\uffdc'
    |   '\uffe0'..'\uffe1'
    |   '\uffe5'..'\uffe6'
    |   '\ufff9'..'\ufffb' 
    |   ('\ud800'..'\udbff') ('\udc00'..'\udfff')
    ;