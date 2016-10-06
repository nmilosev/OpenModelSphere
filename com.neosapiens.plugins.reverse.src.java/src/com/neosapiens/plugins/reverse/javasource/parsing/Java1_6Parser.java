// $ANTLR 3.3 Nov 30, 2010 12:50:56 C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g 2011-04-05 08:20:09

	package com.neosapiens.plugins.reverse.javasource.parsing;
	import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
	import com.neosapiens.plugins.reverse.javasource.utils.ParameterOfMethode;
	import com.neosapiens.plugins.reverse.javasource.utils.FieldOfClass;
	import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfMethod;
	import com.neosapiens.plugins.reverse.javasource.utils.DeclarationOfClassOrInterface;
	import com.neosapiens.plugins.reverse.javasource.utils.InitializationBlockOfClass;
	import java.util.Vector;
	import java.lang.reflect.Modifier;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class Java1_6Parser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENTIFIER", "INTLITERAL", "LONGLITERAL", "FLOATLITERAL", "DOUBLELITERAL", "CHARLITERAL", "STRINGLITERAL", "TRUE", "FALSE", "NULL", "IntegerNumber", "LongSuffix", "HexPrefix", "HexDigit", "Exponent", "NonIntegerNumber", "FloatSuffix", "DoubleSuffix", "EscapeSequence", "WS", "COMMENT", "LINE_COMMENT", "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", "CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", "ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "GOTO", "IF", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "INTERFACE", "LONG", "NATIVE", "NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", "THROW", "THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "LPAREN", "RPAREN", "LBRACE", "RBRACE", "LBRACKET", "RBRACKET", "SEMI", "COMMA", "DOT", "ELLIPSIS", "EQ", "BANG", "TILDE", "QUES", "COLON", "EQEQ", "AMPAMP", "BARBAR", "PLUSPLUS", "SUBSUB", "PLUS", "SUB", "STAR", "SLASH", "AMP", "BAR", "CARET", "PERCENT", "PLUSEQ", "SUBEQ", "STAREQ", "SLASHEQ", "AMPEQ", "BAREQ", "CARETEQ", "PERCENTEQ", "MONKEYS_AT", "BANGEQ", "GT", "LT", "IdentifierStart", "IdentifierPart", "SurrogateIdentifer"
    };
    public static final int EOF=-1;
    public static final int IDENTIFIER=4;
    public static final int INTLITERAL=5;
    public static final int LONGLITERAL=6;
    public static final int FLOATLITERAL=7;
    public static final int DOUBLELITERAL=8;
    public static final int CHARLITERAL=9;
    public static final int STRINGLITERAL=10;
    public static final int TRUE=11;
    public static final int FALSE=12;
    public static final int NULL=13;
    public static final int IntegerNumber=14;
    public static final int LongSuffix=15;
    public static final int HexPrefix=16;
    public static final int HexDigit=17;
    public static final int Exponent=18;
    public static final int NonIntegerNumber=19;
    public static final int FloatSuffix=20;
    public static final int DoubleSuffix=21;
    public static final int EscapeSequence=22;
    public static final int WS=23;
    public static final int COMMENT=24;
    public static final int LINE_COMMENT=25;
    public static final int ABSTRACT=26;
    public static final int ASSERT=27;
    public static final int BOOLEAN=28;
    public static final int BREAK=29;
    public static final int BYTE=30;
    public static final int CASE=31;
    public static final int CATCH=32;
    public static final int CHAR=33;
    public static final int CLASS=34;
    public static final int CONST=35;
    public static final int CONTINUE=36;
    public static final int DEFAULT=37;
    public static final int DO=38;
    public static final int DOUBLE=39;
    public static final int ELSE=40;
    public static final int ENUM=41;
    public static final int EXTENDS=42;
    public static final int FINAL=43;
    public static final int FINALLY=44;
    public static final int FLOAT=45;
    public static final int FOR=46;
    public static final int GOTO=47;
    public static final int IF=48;
    public static final int IMPLEMENTS=49;
    public static final int IMPORT=50;
    public static final int INSTANCEOF=51;
    public static final int INT=52;
    public static final int INTERFACE=53;
    public static final int LONG=54;
    public static final int NATIVE=55;
    public static final int NEW=56;
    public static final int PACKAGE=57;
    public static final int PRIVATE=58;
    public static final int PROTECTED=59;
    public static final int PUBLIC=60;
    public static final int RETURN=61;
    public static final int SHORT=62;
    public static final int STATIC=63;
    public static final int STRICTFP=64;
    public static final int SUPER=65;
    public static final int SWITCH=66;
    public static final int SYNCHRONIZED=67;
    public static final int THIS=68;
    public static final int THROW=69;
    public static final int THROWS=70;
    public static final int TRANSIENT=71;
    public static final int TRY=72;
    public static final int VOID=73;
    public static final int VOLATILE=74;
    public static final int WHILE=75;
    public static final int LPAREN=76;
    public static final int RPAREN=77;
    public static final int LBRACE=78;
    public static final int RBRACE=79;
    public static final int LBRACKET=80;
    public static final int RBRACKET=81;
    public static final int SEMI=82;
    public static final int COMMA=83;
    public static final int DOT=84;
    public static final int ELLIPSIS=85;
    public static final int EQ=86;
    public static final int BANG=87;
    public static final int TILDE=88;
    public static final int QUES=89;
    public static final int COLON=90;
    public static final int EQEQ=91;
    public static final int AMPAMP=92;
    public static final int BARBAR=93;
    public static final int PLUSPLUS=94;
    public static final int SUBSUB=95;
    public static final int PLUS=96;
    public static final int SUB=97;
    public static final int STAR=98;
    public static final int SLASH=99;
    public static final int AMP=100;
    public static final int BAR=101;
    public static final int CARET=102;
    public static final int PERCENT=103;
    public static final int PLUSEQ=104;
    public static final int SUBEQ=105;
    public static final int STAREQ=106;
    public static final int SLASHEQ=107;
    public static final int AMPEQ=108;
    public static final int BAREQ=109;
    public static final int CARETEQ=110;
    public static final int PERCENTEQ=111;
    public static final int MONKEYS_AT=112;
    public static final int BANGEQ=113;
    public static final int GT=114;
    public static final int LT=115;
    public static final int IdentifierStart=116;
    public static final int IdentifierPart=117;
    public static final int SurrogateIdentifer=118;

    // delegates
    // delegators


        public Java1_6Parser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public Java1_6Parser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
            this.state.ruleMemo = new HashMap[383+1];
             
             
        }
        

    public String[] getTokenNames() { return Java1_6Parser.tokenNames; }
    public String getGrammarFileName() { return "C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g"; }


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



    // $ANTLR start "compilationUnit"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:135:1: compilationUnit : ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* ;
    public final void compilationUnit() throws RecognitionException {
        int compilationUnit_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 1) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:2: ( ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:4: ( ( annotations )? packageDeclaration )? ( importDeclaration )* ( typeDeclaration )*
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:4: ( ( annotations )? packageDeclaration )?
            int alt2=2;
            alt2 = dfa2.predict(input);
            switch (alt2) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:6: ( annotations )? packageDeclaration
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:6: ( annotations )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0==MONKEYS_AT) ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:7: annotations
                            {
                            pushFollow(FOLLOW_annotations_in_compilationUnit87);
                            annotations();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    pushFollow(FOLLOW_packageDeclaration_in_compilationUnit91);
                    packageDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:141:3: ( importDeclaration )*
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( (LA3_0==IMPORT) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:141:4: importDeclaration
            	    {
            	    pushFollow(FOLLOW_importDeclaration_in_compilationUnit99);
            	    importDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop3;
                }
            } while (true);

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:142:3: ( typeDeclaration )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( (LA4_0==IDENTIFIER||LA4_0==ABSTRACT||LA4_0==BOOLEAN||LA4_0==BYTE||(LA4_0>=CHAR && LA4_0<=CLASS)||LA4_0==DOUBLE||LA4_0==ENUM||LA4_0==FINAL||LA4_0==FLOAT||(LA4_0>=INT && LA4_0<=NATIVE)||(LA4_0>=PRIVATE && LA4_0<=PUBLIC)||(LA4_0>=SHORT && LA4_0<=STRICTFP)||LA4_0==SYNCHRONIZED||LA4_0==TRANSIENT||(LA4_0>=VOID && LA4_0<=VOLATILE)||LA4_0==SEMI||LA4_0==MONKEYS_AT||LA4_0==LT) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:142:4: typeDeclaration
            	    {
            	    pushFollow(FOLLOW_typeDeclaration_in_compilationUnit106);
            	    typeDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 1, compilationUnit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "compilationUnit"


    // $ANTLR start "packageDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:145:1: packageDeclaration : 'package' packageName= qualifiedName ';' ;
    public final void packageDeclaration() throws RecognitionException {
        int packageDeclaration_StartIndex = input.index();
        Java1_6Parser.qualifiedName_return packageName = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 2) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:146:3: ( 'package' packageName= qualifiedName ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:146:5: 'package' packageName= qualifiedName ';'
            {
            match(input,PACKAGE,FOLLOW_PACKAGE_in_packageDeclaration121); if (state.failed) return ;
            pushFollow(FOLLOW_qualifiedName_in_packageDeclaration128);
            packageName=qualifiedName();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               
              		  setPackageName((packageName!=null?input.toString(packageName.start,packageName.stop):null));
              		  getBuilder().createPackage((packageName!=null?input.toString(packageName.start,packageName.stop):null)); 
              		
            }
            match(input,SEMI,FOLLOW_SEMI_in_packageDeclaration139); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 2, packageDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "packageDeclaration"


    // $ANTLR start "importDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:155:1: importDeclaration : ( 'import' ( 'static' )? importName= IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? importName= IDENTIFIER ( '.' importName= IDENTIFIER )+ ( '.' '*' )? ';' );
    public final void importDeclaration() throws RecognitionException {
        int importDeclaration_StartIndex = input.index();
        Token importName=null;


          StringBuffer stringBuffer = new StringBuffer();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 3) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:159:3: ( 'import' ( 'static' )? importName= IDENTIFIER '.' '*' ';' | 'import' ( 'static' )? importName= IDENTIFIER ( '.' importName= IDENTIFIER )+ ( '.' '*' )? ';' )
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==IMPORT) ) {
                int LA9_1 = input.LA(2);

                if ( (LA9_1==STATIC) ) {
                    int LA9_2 = input.LA(3);

                    if ( (LA9_2==IDENTIFIER) ) {
                        int LA9_3 = input.LA(4);

                        if ( (LA9_3==DOT) ) {
                            int LA9_4 = input.LA(5);

                            if ( (LA9_4==STAR) ) {
                                alt9=1;
                            }
                            else if ( (LA9_4==IDENTIFIER) ) {
                                alt9=2;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                NoViableAltException nvae =
                                    new NoViableAltException("", 9, 4, input);

                                throw nvae;
                            }
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 2, input);

                        throw nvae;
                    }
                }
                else if ( (LA9_1==IDENTIFIER) ) {
                    int LA9_3 = input.LA(3);

                    if ( (LA9_3==DOT) ) {
                        int LA9_4 = input.LA(4);

                        if ( (LA9_4==STAR) ) {
                            alt9=1;
                        }
                        else if ( (LA9_4==IDENTIFIER) ) {
                            alt9=2;
                        }
                        else {
                            if (state.backtracking>0) {state.failed=true; return ;}
                            NoViableAltException nvae =
                                new NoViableAltException("", 9, 4, input);

                            throw nvae;
                        }
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 9, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 9, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 9, 0, input);

                throw nvae;
            }
            switch (alt9) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:159:5: 'import' ( 'static' )? importName= IDENTIFIER '.' '*' ';'
                    {
                    match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration160); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:159:14: ( 'static' )?
                    int alt5=2;
                    int LA5_0 = input.LA(1);

                    if ( (LA5_0==STATIC) ) {
                        alt5=1;
                    }
                    switch (alt5) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:159:15: 'static'
                            {
                            match(input,STATIC,FOLLOW_STATIC_in_importDeclaration163); if (state.failed) return ;

                            }
                            break;

                    }

                    importName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration169); if (state.failed) return ;
                    match(input,DOT,FOLLOW_DOT_in_importDeclaration170); if (state.failed) return ;
                    match(input,STAR,FOLLOW_STAR_in_importDeclaration172); if (state.failed) return ;
                    match(input,SEMI,FOLLOW_SEMI_in_importDeclaration174); if (state.failed) return ;
                    if ( state.backtracking==0 ) {

                            stringBuffer.append((importName!=null?importName.getText():null));
                            stringBuffer.append(".");
                            stringBuffer.append("*");
                            getBuilder().createImport(stringBuffer.toString());   
                          
                    }

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:166:5: 'import' ( 'static' )? importName= IDENTIFIER ( '.' importName= IDENTIFIER )+ ( '.' '*' )? ';'
                    {
                    match(input,IMPORT,FOLLOW_IMPORT_in_importDeclaration192); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:166:14: ( 'static' )?
                    int alt6=2;
                    int LA6_0 = input.LA(1);

                    if ( (LA6_0==STATIC) ) {
                        alt6=1;
                    }
                    switch (alt6) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:166:15: 'static'
                            {
                            match(input,STATIC,FOLLOW_STATIC_in_importDeclaration195); if (state.failed) return ;

                            }
                            break;

                    }

                    importName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration201); if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      stringBuffer.append((importName!=null?importName.getText():null));
                    }
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:167:5: ( '.' importName= IDENTIFIER )+
                    int cnt7=0;
                    loop7:
                    do {
                        int alt7=2;
                        int LA7_0 = input.LA(1);

                        if ( (LA7_0==DOT) ) {
                            int LA7_1 = input.LA(2);

                            if ( (LA7_1==IDENTIFIER) ) {
                                alt7=1;
                            }


                        }


                        switch (alt7) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:167:7: '.' importName= IDENTIFIER
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_importDeclaration212); if (state.failed) return ;
                    	    importName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_importDeclaration216); if (state.failed) return ;
                    	    if ( state.backtracking==0 ) {

                    	              stringBuffer.append(".");
                    	              stringBuffer.append((importName!=null?importName.getText():null));
                    	            
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt7 >= 1 ) break loop7;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(7, input);
                                throw eee;
                        }
                        cnt7++;
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:173:3: ( '.' '*' )?
                    int alt8=2;
                    int LA8_0 = input.LA(1);

                    if ( (LA8_0==DOT) ) {
                        alt8=1;
                    }
                    switch (alt8) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:173:5: '.' '*'
                            {
                            match(input,DOT,FOLLOW_DOT_in_importDeclaration240); if (state.failed) return ;
                            match(input,STAR,FOLLOW_STAR_in_importDeclaration242); if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              stringBuffer.append(".*");
                            }

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_importDeclaration248); if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      getBuilder().createImport(stringBuffer.toString());
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 3, importDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "importDeclaration"


    // $ANTLR start "qualifiedImportName"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:177:1: qualifiedImportName : IDENTIFIER ( '.' IDENTIFIER )* ;
    public final void qualifiedImportName() throws RecognitionException {
        int qualifiedImportName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 4) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:178:3: ( IDENTIFIER ( '.' IDENTIFIER )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:178:5: IDENTIFIER ( '.' IDENTIFIER )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName266); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:179:5: ( '.' IDENTIFIER )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==DOT) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:179:6: '.' IDENTIFIER
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualifiedImportName274); if (state.failed) return ;
            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedImportName276); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 4, qualifiedImportName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "qualifiedImportName"


    // $ANTLR start "typeDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:182:1: typeDeclaration : ( classOrInterfaceDeclaration | ';' );
    public final void typeDeclaration() throws RecognitionException {
        int typeDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 5) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:183:3: ( classOrInterfaceDeclaration | ';' )
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==IDENTIFIER||LA11_0==ABSTRACT||LA11_0==BOOLEAN||LA11_0==BYTE||(LA11_0>=CHAR && LA11_0<=CLASS)||LA11_0==DOUBLE||LA11_0==ENUM||LA11_0==FINAL||LA11_0==FLOAT||(LA11_0>=INT && LA11_0<=NATIVE)||(LA11_0>=PRIVATE && LA11_0<=PUBLIC)||(LA11_0>=SHORT && LA11_0<=STRICTFP)||LA11_0==SYNCHRONIZED||LA11_0==TRANSIENT||(LA11_0>=VOID && LA11_0<=VOLATILE)||LA11_0==MONKEYS_AT||LA11_0==LT) ) {
                alt11=1;
            }
            else if ( (LA11_0==SEMI) ) {
                alt11=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 11, 0, input);

                throw nvae;
            }
            switch (alt11) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:183:5: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration292);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:184:4: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_typeDeclaration297); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 5, typeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeDeclaration"


    // $ANTLR start "classOrInterfaceDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:187:1: classOrInterfaceDeclaration : ( classDeclaration | interfaceDeclaration );
    public final void classOrInterfaceDeclaration() throws RecognitionException {
        int classOrInterfaceDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 6) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:188:3: ( classDeclaration | interfaceDeclaration )
            int alt12=2;
            alt12 = dfa12.predict(input);
            switch (alt12) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:188:5: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_classOrInterfaceDeclaration311);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:189:5: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration321);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 6, classOrInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classOrInterfaceDeclaration"


    // $ANTLR start "modifier"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:193:1: modifier returns [int modifierLocal = 0] : ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' );
    public final int modifier() throws RecognitionException {
        int modifierLocal =  0;
        int modifier_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 7) ) { return modifierLocal; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:194:3: ( annotation | 'public' | 'protected' | 'private' | 'abstract' | 'static' | 'final' | 'native' | 'synchronized' | 'transient' | 'volatile' | 'strictfp' )
            int alt13=12;
            switch ( input.LA(1) ) {
            case MONKEYS_AT:
                {
                alt13=1;
                }
                break;
            case PUBLIC:
                {
                alt13=2;
                }
                break;
            case PROTECTED:
                {
                alt13=3;
                }
                break;
            case PRIVATE:
                {
                alt13=4;
                }
                break;
            case ABSTRACT:
                {
                alt13=5;
                }
                break;
            case STATIC:
                {
                alt13=6;
                }
                break;
            case FINAL:
                {
                alt13=7;
                }
                break;
            case NATIVE:
                {
                alt13=8;
                }
                break;
            case SYNCHRONIZED:
                {
                alt13=9;
                }
                break;
            case TRANSIENT:
                {
                alt13=10;
                }
                break;
            case VOLATILE:
                {
                alt13=11;
                }
                break;
            case STRICTFP:
                {
                alt13=12;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return modifierLocal;}
                NoViableAltException nvae =
                    new NoViableAltException("", 13, 0, input);

                throw nvae;
            }

            switch (alt13) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:194:5: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_modifier345);
                    annotation();

                    state._fsp--;
                    if (state.failed) return modifierLocal;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:195:4: 'public'
                    {
                    match(input,PUBLIC,FOLLOW_PUBLIC_in_modifier350); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.PUBLIC; 
                    }

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:196:4: 'protected'
                    {
                    match(input,PROTECTED,FOLLOW_PROTECTED_in_modifier357); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.PROTECTED; 
                    }

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:197:4: 'private'
                    {
                    match(input,PRIVATE,FOLLOW_PRIVATE_in_modifier364); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.PRIVATE; 
                    }

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:198:4: 'abstract'
                    {
                    match(input,ABSTRACT,FOLLOW_ABSTRACT_in_modifier371); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.ABSTRACT; 
                    }

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:199:4: 'static'
                    {
                    match(input,STATIC,FOLLOW_STATIC_in_modifier378); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.STATIC; 
                    }

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:200:4: 'final'
                    {
                    match(input,FINAL,FOLLOW_FINAL_in_modifier385); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.FINAL; 
                    }

                    }
                    break;
                case 8 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:201:4: 'native'
                    {
                    match(input,NATIVE,FOLLOW_NATIVE_in_modifier393); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.NATIVE; 
                    }

                    }
                    break;
                case 9 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:202:4: 'synchronized'
                    {
                    match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_modifier400); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.SYNCHRONIZED; 
                    }

                    }
                    break;
                case 10 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:203:4: 'transient'
                    {
                    match(input,TRANSIENT,FOLLOW_TRANSIENT_in_modifier407); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.TRANSIENT; 
                    }

                    }
                    break;
                case 11 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:204:4: 'volatile'
                    {
                    match(input,VOLATILE,FOLLOW_VOLATILE_in_modifier414); if (state.failed) return modifierLocal;
                    if ( state.backtracking==0 ) {
                       modifierLocal = Modifier.VOLATILE; 
                    }

                    }
                    break;
                case 12 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:205:4: 'strictfp'
                    {
                    match(input,STRICTFP,FOLLOW_STRICTFP_in_modifier421); if (state.failed) return modifierLocal;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 7, modifier_StartIndex); }
        }
        return modifierLocal;
    }
    // $ANTLR end "modifier"


    // $ANTLR start "modifiers"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:208:1: modifiers returns [int modifiersLocal = 0] : (modifierTemp= modifier )* ;
    public final int modifiers() throws RecognitionException {
        int modifiersLocal =  0;
        int modifiers_StartIndex = input.index();
        int modifierTemp = 0;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 8) ) { return modifiersLocal; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:209:2: ( (modifierTemp= modifier )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:210:2: (modifierTemp= modifier )*
            {
            if ( state.backtracking==0 ) {
                updateJavadocComment(); 
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:211:2: (modifierTemp= modifier )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==MONKEYS_AT) ) {
                    int LA14_2 = input.LA(2);

                    if ( (LA14_2==IDENTIFIER) ) {
                        alt14=1;
                    }


                }
                else if ( (LA14_0==ABSTRACT||LA14_0==FINAL||LA14_0==NATIVE||(LA14_0>=PRIVATE && LA14_0<=PUBLIC)||(LA14_0>=STATIC && LA14_0<=STRICTFP)||LA14_0==SYNCHRONIZED||LA14_0==TRANSIENT||LA14_0==VOLATILE) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:211:4: modifierTemp= modifier
            	    {
            	    pushFollow(FOLLOW_modifier_in_modifiers445);
            	    modifierTemp=modifier();

            	    state._fsp--;
            	    if (state.failed) return modifiersLocal;
            	    if ( state.backtracking==0 ) {
            	       modifiersLocal |= modifierTemp; 
            	    }

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 8, modifiers_StartIndex); }
        }
        return modifiersLocal;
    }
    // $ANTLR end "modifiers"

    public static class variableModifiers_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "variableModifiers"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:213:1: variableModifiers : ( 'final' | annotation )* ;
    public final Java1_6Parser.variableModifiers_return variableModifiers() throws RecognitionException {
        Java1_6Parser.variableModifiers_return retval = new Java1_6Parser.variableModifiers_return();
        retval.start = input.LT(1);
        int variableModifiers_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 9) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:214:3: ( ( 'final' | annotation )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:214:5: ( 'final' | annotation )*
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:214:5: ( 'final' | annotation )*
            loop15:
            do {
                int alt15=3;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==FINAL) ) {
                    alt15=1;
                }
                else if ( (LA15_0==MONKEYS_AT) ) {
                    alt15=2;
                }


                switch (alt15) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:214:7: 'final'
            	    {
            	    match(input,FINAL,FOLLOW_FINAL_in_variableModifiers464); if (state.failed) return retval;

            	    }
            	    break;
            	case 2 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:215:7: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_variableModifiers472);
            	    annotation();

            	    state._fsp--;
            	    if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 9, variableModifiers_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "variableModifiers"


    // $ANTLR start "classDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:220:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );
    public final void classDeclaration() throws RecognitionException {
        int classDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 10) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:221:3: ( normalClassDeclaration | enumDeclaration )
            int alt16=2;
            alt16 = dfa16.predict(input);
            switch (alt16) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:221:5: normalClassDeclaration
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_classDeclaration499);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:222:5: enumDeclaration
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_classDeclaration505);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 10, classDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classDeclaration"


    // $ANTLR start "normalClassDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:225:1: normalClassDeclaration : classModifiers= modifiers 'class' className= IDENTIFIER ( typeParameters )? ( 'extends' classExtendsName= type )? ( 'implements' implementsListTemp= typeList )? classBody ;
    public final void normalClassDeclaration() throws RecognitionException {
        int normalClassDeclaration_StartIndex = input.index();
        Token className=null;
        int classModifiers = 0;

        String classExtendsName = null;

        List<String> implementsListTemp = null;



          List<String> implementsList = new ArrayList<String>();
          List<String> extendsList = new ArrayList<String>();
          DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 11) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:231:3: (classModifiers= modifiers 'class' className= IDENTIFIER ( typeParameters )? ( 'extends' classExtendsName= type )? ( 'implements' implementsListTemp= typeList )? classBody )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:233:3: classModifiers= modifiers 'class' className= IDENTIFIER ( typeParameters )? ( 'extends' classExtendsName= type )? ( 'implements' implementsListTemp= typeList )? classBody
            {
            pushFollow(FOLLOW_modifiers_in_normalClassDeclaration529);
            classModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,CLASS,FOLLOW_CLASS_in_normalClassDeclaration535); if (state.failed) return ;
            className=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalClassDeclaration542); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:236:5: ( typeParameters )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==LT) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:236:7: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalClassDeclaration550);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:237:5: ( 'extends' classExtendsName= type )?
            int alt18=2;
            int LA18_0 = input.LA(1);

            if ( (LA18_0==EXTENDS) ) {
                alt18=1;
            }
            switch (alt18) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:237:7: 'extends' classExtendsName= type
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_normalClassDeclaration561); if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_normalClassDeclaration565);
                    classExtendsName=type();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      extendsList.add(classExtendsName);
                    }

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:238:5: ( 'implements' implementsListTemp= typeList )?
            int alt19=2;
            int LA19_0 = input.LA(1);

            if ( (LA19_0==IMPLEMENTS) ) {
                alt19=1;
            }
            switch (alt19) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:238:7: 'implements' implementsListTemp= typeList
                    {
                    match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_normalClassDeclaration578); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalClassDeclaration582);
                    implementsListTemp=typeList();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      implementsList = implementsListTemp;
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
              	
              		  addCurrentClassOrInterfaceNamePile((className!=null?className.getText():null));	
              		  declarationOfClassOrInterface.setCurrentPackage(getPackageName());
              		  declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
              		  declarationOfClassOrInterface.setJavadoc(getJavadocComment());
              		  declarationOfClassOrInterface.setModifiers(classModifiers);
              		  declarationOfClassOrInterface.setClass(true);
              		  declarationOfClassOrInterface.setClassOrInterfaceName((className!=null?className.getText():null));
              		  declarationOfClassOrInterface.setExtendsList(extendsList);
              		  declarationOfClassOrInterface.setImplementsList(implementsList);
              		  getBuilder().createClass(declarationOfClassOrInterface);	
              		
            }
            pushFollow(FOLLOW_classBody_in_normalClassDeclaration597);
            classBody();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               removeLastElementOfCurrentClassOrInterfaceNamePile(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 11, normalClassDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "normalClassDeclaration"


    // $ANTLR start "typeParameters"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:256:1: typeParameters returns [List<String> typeParametersList = new ArrayList<String>()] : '<' typeParameterString= typeParameter ( ',' typeParameterString= typeParameter )* '>' ;
    public final List<String> typeParameters() throws RecognitionException {
        List<String> typeParametersList =  new ArrayList<String>();
        int typeParameters_StartIndex = input.index();
        String typeParameterString = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 12) ) { return typeParametersList; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:257:3: ( '<' typeParameterString= typeParameter ( ',' typeParameterString= typeParameter )* '>' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:257:5: '<' typeParameterString= typeParameter ( ',' typeParameterString= typeParameter )* '>'
            {
            match(input,LT,FOLLOW_LT_in_typeParameters623); if (state.failed) return typeParametersList;
            pushFollow(FOLLOW_typeParameter_in_typeParameters631);
            typeParameterString=typeParameter();

            state._fsp--;
            if (state.failed) return typeParametersList;
            if ( state.backtracking==0 ) {
               typeParametersList.add(typeParameterString); 
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:259:5: ( ',' typeParameterString= typeParameter )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( (LA20_0==COMMA) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:259:6: ',' typeParameterString= typeParameter
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeParameters640); if (state.failed) return typeParametersList;
            	    pushFollow(FOLLOW_typeParameter_in_typeParameters644);
            	    typeParameterString=typeParameter();

            	    state._fsp--;
            	    if (state.failed) return typeParametersList;
            	    if ( state.backtracking==0 ) {
            	       typeParametersList.add(typeParameterString); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

            match(input,GT,FOLLOW_GT_in_typeParameters655); if (state.failed) return typeParametersList;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 12, typeParameters_StartIndex); }
        }
        return typeParametersList;
    }
    // $ANTLR end "typeParameters"


    // $ANTLR start "typeParameter"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:263:1: typeParameter returns [String typeParameterNameAndExtendsTypeBoundName = null] : typeParameterName= IDENTIFIER ( 'extends' typeboundString= typeBound )? ;
    public final String typeParameter() throws RecognitionException {
        String typeParameterNameAndExtendsTypeBoundName =  null;
        int typeParameter_StartIndex = input.index();
        Token typeParameterName=null;
        String typeboundString = null;



          StringBuffer stringBuffer = new StringBuffer();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 13) ) { return typeParameterNameAndExtendsTypeBoundName; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:267:3: (typeParameterName= IDENTIFIER ( 'extends' typeboundString= typeBound )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:267:5: typeParameterName= IDENTIFIER ( 'extends' typeboundString= typeBound )?
            {
            typeParameterName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_typeParameter678); if (state.failed) return typeParameterNameAndExtendsTypeBoundName;
            if ( state.backtracking==0 ) {
              stringBuffer.append(typeParameterName);
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:268:5: ( 'extends' typeboundString= typeBound )?
            int alt21=2;
            int LA21_0 = input.LA(1);

            if ( (LA21_0==EXTENDS) ) {
                alt21=1;
            }
            switch (alt21) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:268:6: 'extends' typeboundString= typeBound
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_typeParameter687); if (state.failed) return typeParameterNameAndExtendsTypeBoundName;
                    pushFollow(FOLLOW_typeBound_in_typeParameter691);
                    typeboundString=typeBound();

                    state._fsp--;
                    if (state.failed) return typeParameterNameAndExtendsTypeBoundName;
                    if ( state.backtracking==0 ) {
                      stringBuffer.append(typeboundString);
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
              typeParameterNameAndExtendsTypeBoundName = stringBuffer.toString();
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 13, typeParameter_StartIndex); }
        }
        return typeParameterNameAndExtendsTypeBoundName;
    }
    // $ANTLR end "typeParameter"


    // $ANTLR start "typeBound"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:273:1: typeBound returns [String typeBoundString = null] : typeName= type ( '&' typeName= type )* ;
    public final String typeBound() throws RecognitionException {
        String typeBoundString =  null;
        int typeBound_StartIndex = input.index();
        String typeName = null;



            StringBuffer stringBuffer = new StringBuffer();
          
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 14) ) { return typeBoundString; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:277:5: (typeName= type ( '&' typeName= type )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:277:9: typeName= type ( '&' typeName= type )*
            {
            pushFollow(FOLLOW_type_in_typeBound731);
            typeName=type();

            state._fsp--;
            if (state.failed) return typeBoundString;
            if ( state.backtracking==0 ) {
              stringBuffer.append(typeName);
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:278:9: ( '&' typeName= type )*
            loop22:
            do {
                int alt22=2;
                int LA22_0 = input.LA(1);

                if ( (LA22_0==AMP) ) {
                    alt22=1;
                }


                switch (alt22) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:278:10: '&' typeName= type
            	    {
            	    match(input,AMP,FOLLOW_AMP_in_typeBound744); if (state.failed) return typeBoundString;
            	    pushFollow(FOLLOW_type_in_typeBound748);
            	    typeName=type();

            	    state._fsp--;
            	    if (state.failed) return typeBoundString;
            	    if ( state.backtracking==0 ) {
            	      stringBuffer.append(typeName);
            	    }

            	    }
            	    break;

            	default :
            	    break loop22;
                }
            } while (true);

            if ( state.backtracking==0 ) {

                        typeBoundString = stringBuffer.toString();
                      
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 14, typeBound_StartIndex); }
        }
        return typeBoundString;
    }
    // $ANTLR end "typeBound"


    // $ANTLR start "enumDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:286:1: enumDeclaration : enumModifiers= modifiers ( 'enum' ) enumName= IDENTIFIER ( 'implements' implementsListTemp= typeList )? enumBody ;
    public final void enumDeclaration() throws RecognitionException {
        int enumDeclaration_StartIndex = input.index();
        Token enumName=null;
        int enumModifiers = 0;

        List<String> implementsListTemp = null;



          List<String> implementsList = new ArrayList<String>();
          DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();  

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 15) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:291:5: (enumModifiers= modifiers ( 'enum' ) enumName= IDENTIFIER ( 'implements' implementsListTemp= typeList )? enumBody )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:291:9: enumModifiers= modifiers ( 'enum' ) enumName= IDENTIFIER ( 'implements' implementsListTemp= typeList )? enumBody
            {
            pushFollow(FOLLOW_modifiers_in_enumDeclaration798);
            enumModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:292:9: ( 'enum' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:292:10: 'enum'
            {
            match(input,ENUM,FOLLOW_ENUM_in_enumDeclaration810); if (state.failed) return ;

            }

            enumName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumDeclaration824); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:294:9: ( 'implements' implementsListTemp= typeList )?
            int alt23=2;
            int LA23_0 = input.LA(1);

            if ( (LA23_0==IMPLEMENTS) ) {
                alt23=1;
            }
            switch (alt23) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:294:11: 'implements' implementsListTemp= typeList
                    {
                    match(input,IMPLEMENTS,FOLLOW_IMPLEMENTS_in_enumDeclaration836); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_enumDeclaration840);
                    implementsListTemp=typeList();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       implementsList = implementsListTemp; 
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                        addCurrentClassOrInterfaceNamePile((enumName!=null?enumName.getText():null));
                        declarationOfClassOrInterface.setCurrentPackage(getPackageName());
                        declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
                        declarationOfClassOrInterface.setJavadoc(getJavadocComment());
                        declarationOfClassOrInterface.setModifiers(enumModifiers);
                        declarationOfClassOrInterface.setEnum(true);
                        declarationOfClassOrInterface.setClassOrInterfaceName((enumName!=null?enumName.getText():null));
                        declarationOfClassOrInterface.setImplementsList(implementsList);
                        getBuilder().createClass(declarationOfClassOrInterface);
                      
            }
            pushFollow(FOLLOW_enumBody_in_enumDeclaration865);
            enumBody();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               removeLastElementOfCurrentClassOrInterfaceNamePile(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 15, enumDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumDeclaration"


    // $ANTLR start "enumBody"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:311:1: enumBody : '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' ;
    public final void enumBody() throws RecognitionException {
        int enumBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 16) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:312:3: ( '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:312:5: '{' ( enumConstants )? ( ',' )? ( enumBodyDeclarations )? '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_enumBody896); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:313:5: ( enumConstants )?
            int alt24=2;
            int LA24_0 = input.LA(1);

            if ( (LA24_0==IDENTIFIER||LA24_0==MONKEYS_AT) ) {
                alt24=1;
            }
            switch (alt24) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:313:7: enumConstants
                    {
                    pushFollow(FOLLOW_enumConstants_in_enumBody904);
                    enumConstants();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:314:5: ( ',' )?
            int alt25=2;
            int LA25_0 = input.LA(1);

            if ( (LA25_0==COMMA) ) {
                alt25=1;
            }
            switch (alt25) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:0:0: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_enumBody914); if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:315:5: ( enumBodyDeclarations )?
            int alt26=2;
            int LA26_0 = input.LA(1);

            if ( (LA26_0==SEMI) ) {
                alt26=1;
            }
            switch (alt26) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:315:7: enumBodyDeclarations
                    {
                    pushFollow(FOLLOW_enumBodyDeclarations_in_enumBody924);
                    enumBodyDeclarations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_enumBody934); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 16, enumBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumBody"


    // $ANTLR start "enumConstants"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:319:1: enumConstants : enumConstant ( ',' enumConstant )* ;
    public final void enumConstants() throws RecognitionException {
        int enumConstants_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 17) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:320:5: ( enumConstant ( ',' enumConstant )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:320:9: enumConstant ( ',' enumConstant )*
            {
            pushFollow(FOLLOW_enumConstant_in_enumConstants952);
            enumConstant();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:321:9: ( ',' enumConstant )*
            loop27:
            do {
                int alt27=2;
                int LA27_0 = input.LA(1);

                if ( (LA27_0==COMMA) ) {
                    int LA27_1 = input.LA(2);

                    if ( (LA27_1==IDENTIFIER||LA27_1==MONKEYS_AT) ) {
                        alt27=1;
                    }


                }


                switch (alt27) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:321:10: ',' enumConstant
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_enumConstants963); if (state.failed) return ;
            	    pushFollow(FOLLOW_enumConstant_in_enumConstants965);
            	    enumConstant();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop27;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 17, enumConstants_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumConstants"


    // $ANTLR start "enumConstant"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:325:1: enumConstant : ( annotations )? enumName= IDENTIFIER ( arguments )? ( classBody )? ;
    public final void enumConstant() throws RecognitionException {
        int enumConstant_StartIndex = input.index();
        Token enumName=null;


          FieldOfClass fieldOfClass = new FieldOfClass();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 18) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:333:3: ( ( annotations )? enumName= IDENTIFIER ( arguments )? ( classBody )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:333:5: ( annotations )? enumName= IDENTIFIER ( arguments )? ( classBody )?
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:333:5: ( annotations )?
            int alt28=2;
            int LA28_0 = input.LA(1);

            if ( (LA28_0==MONKEYS_AT) ) {
                alt28=1;
            }
            switch (alt28) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:333:7: annotations
                    {
                    pushFollow(FOLLOW_annotations_in_enumConstant1002);
                    annotations();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            enumName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumConstant1013); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:335:5: ( arguments )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==LPAREN) ) {
                alt29=1;
            }
            switch (alt29) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:335:7: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_enumConstant1021);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:336:5: ( classBody )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==LBRACE) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:336:7: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_enumConstant1032);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                    fieldOfClass.setCurrentClass(getCurrentClassOrInterfaceNamePile());
                    fieldOfClass.setCurrentPackage(getPackageName());
                    fieldOfClass.setJavadoc(getJavadocComment());
                    fieldOfClass.setFieldName((enumName!=null?enumName.getText():null));
                    getBuilder().createField(fieldOfClass);
                  
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 18, enumConstant_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumConstant"


    // $ANTLR start "enumBodyDeclarations"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:348:1: enumBodyDeclarations : ';' ( classBodyDeclaration )* ;
    public final void enumBodyDeclarations() throws RecognitionException {
        int enumBodyDeclarations_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 19) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:349:5: ( ';' ( classBodyDeclaration )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:349:9: ';' ( classBodyDeclaration )*
            {
            match(input,SEMI,FOLLOW_SEMI_in_enumBodyDeclarations1073); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:350:9: ( classBodyDeclaration )*
            loop31:
            do {
                int alt31=2;
                int LA31_0 = input.LA(1);

                if ( (LA31_0==IDENTIFIER||LA31_0==ABSTRACT||LA31_0==BOOLEAN||LA31_0==BYTE||(LA31_0>=CHAR && LA31_0<=CLASS)||LA31_0==DOUBLE||LA31_0==ENUM||LA31_0==FINAL||LA31_0==FLOAT||(LA31_0>=INT && LA31_0<=NATIVE)||(LA31_0>=PRIVATE && LA31_0<=PUBLIC)||(LA31_0>=SHORT && LA31_0<=STRICTFP)||LA31_0==SYNCHRONIZED||LA31_0==TRANSIENT||(LA31_0>=VOID && LA31_0<=VOLATILE)||LA31_0==LBRACE||LA31_0==SEMI||LA31_0==MONKEYS_AT||LA31_0==LT) ) {
                    alt31=1;
                }


                switch (alt31) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:350:10: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1085);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop31;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 19, enumBodyDeclarations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumBodyDeclarations"


    // $ANTLR start "interfaceDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:354:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );
    public final void interfaceDeclaration() throws RecognitionException {
        int interfaceDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 20) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:355:5: ( normalInterfaceDeclaration | annotationTypeDeclaration )
            int alt32=2;
            alt32 = dfa32.predict(input);
            switch (alt32) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:355:9: normalInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1116);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:356:9: annotationTypeDeclaration
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1126);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 20, interfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceDeclaration"


    // $ANTLR start "normalInterfaceDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:359:1: normalInterfaceDeclaration : interfaceModifiers= modifiers 'interface' interfaceName= IDENTIFIER (typeParametersListTemp= typeParameters )? ( 'extends' extendsListTemp= typeList )? interfaceBody ;
    public final void normalInterfaceDeclaration() throws RecognitionException {
        int normalInterfaceDeclaration_StartIndex = input.index();
        Token interfaceName=null;
        int interfaceModifiers = 0;

        List<String> typeParametersListTemp = null;

        List<String> extendsListTemp = null;



            List<String> extendsList = new ArrayList<String>();
            List<String> typeParametersList = new ArrayList<String>();
            DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();
          
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 21) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:365:3: (interfaceModifiers= modifiers 'interface' interfaceName= IDENTIFIER (typeParametersListTemp= typeParameters )? ( 'extends' extendsListTemp= typeList )? interfaceBody )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:366:7: interfaceModifiers= modifiers 'interface' interfaceName= IDENTIFIER (typeParametersListTemp= typeParameters )? ( 'extends' extendsListTemp= typeList )? interfaceBody
            {
            pushFollow(FOLLOW_modifiers_in_normalInterfaceDeclaration1163);
            interfaceModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,INTERFACE,FOLLOW_INTERFACE_in_normalInterfaceDeclaration1172); if (state.failed) return ;
            interfaceName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1183); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:369:7: (typeParametersListTemp= typeParameters )?
            int alt33=2;
            int LA33_0 = input.LA(1);

            if ( (LA33_0==LT) ) {
                alt33=1;
            }
            switch (alt33) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:369:9: typeParametersListTemp= typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_normalInterfaceDeclaration1195);
                    typeParametersListTemp=typeParameters();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       typeParametersList = typeParametersListTemp; 
                    }

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:370:7: ( 'extends' extendsListTemp= typeList )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==EXTENDS) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:370:9: 'extends' extendsListTemp= typeList
                    {
                    match(input,EXTENDS,FOLLOW_EXTENDS_in_normalInterfaceDeclaration1210); if (state.failed) return ;
                    pushFollow(FOLLOW_typeList_in_normalInterfaceDeclaration1214);
                    extendsListTemp=typeList();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                       extendsList = extendsListTemp; 
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {

                      addCurrentClassOrInterfaceNamePile((interfaceName!=null?interfaceName.getText():null));
                      declarationOfClassOrInterface.setCurrentPackage(getPackageName());
                      declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
                      declarationOfClassOrInterface.setJavadoc(getJavadocComment());
                      declarationOfClassOrInterface.setModifiers(interfaceModifiers);
                      declarationOfClassOrInterface.setInterface(true);
                      declarationOfClassOrInterface.setClassOrInterfaceName((interfaceName!=null?interfaceName.getText():null));
                      declarationOfClassOrInterface.setExtendsList(extendsList);
                      getBuilder().createClass(declarationOfClassOrInterface);
                    
            }
            pushFollow(FOLLOW_interfaceBody_in_normalInterfaceDeclaration1241);
            interfaceBody();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {
               removeLastElementOfCurrentClassOrInterfaceNamePile(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 21, normalInterfaceDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "normalInterfaceDeclaration"


    // $ANTLR start "typeList"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:386:1: typeList returns [List<String> extendsTypeList = new ArrayList<String>()] : extendsTypeName= type ( ',' extendsTypeName= type )* ;
    public final List<String> typeList() throws RecognitionException {
        List<String> extendsTypeList =  new ArrayList<String>();
        int typeList_StartIndex = input.index();
        String extendsTypeName = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 22) ) { return extendsTypeList; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:387:5: (extendsTypeName= type ( ',' extendsTypeName= type )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:387:9: extendsTypeName= type ( ',' extendsTypeName= type )*
            {
            pushFollow(FOLLOW_type_in_typeList1274);
            extendsTypeName=type();

            state._fsp--;
            if (state.failed) return extendsTypeList;
            if ( state.backtracking==0 ) {
               extendsTypeList.add(extendsTypeName); 
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:388:9: ( ',' extendsTypeName= type )*
            loop35:
            do {
                int alt35=2;
                int LA35_0 = input.LA(1);

                if ( (LA35_0==COMMA) ) {
                    alt35=1;
                }


                switch (alt35) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:388:10: ',' extendsTypeName= type
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeList1287); if (state.failed) return extendsTypeList;
            	    pushFollow(FOLLOW_type_in_typeList1291);
            	    extendsTypeName=type();

            	    state._fsp--;
            	    if (state.failed) return extendsTypeList;
            	    if ( state.backtracking==0 ) {
            	       extendsTypeList.add(extendsTypeName); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop35;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 22, typeList_StartIndex); }
        }
        return extendsTypeList;
    }
    // $ANTLR end "typeList"


    // $ANTLR start "classBody"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:392:1: classBody : '{' ( classBodyDeclaration )* '}' ;
    public final void classBody() throws RecognitionException {
        int classBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 23) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:393:5: ( '{' ( classBodyDeclaration )* '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:393:9: '{' ( classBodyDeclaration )* '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_classBody1324); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:394:9: ( classBodyDeclaration )*
            loop36:
            do {
                int alt36=2;
                int LA36_0 = input.LA(1);

                if ( (LA36_0==IDENTIFIER||LA36_0==ABSTRACT||LA36_0==BOOLEAN||LA36_0==BYTE||(LA36_0>=CHAR && LA36_0<=CLASS)||LA36_0==DOUBLE||LA36_0==ENUM||LA36_0==FINAL||LA36_0==FLOAT||(LA36_0>=INT && LA36_0<=NATIVE)||(LA36_0>=PRIVATE && LA36_0<=PUBLIC)||(LA36_0>=SHORT && LA36_0<=STRICTFP)||LA36_0==SYNCHRONIZED||LA36_0==TRANSIENT||(LA36_0>=VOID && LA36_0<=VOLATILE)||LA36_0==LBRACE||LA36_0==SEMI||LA36_0==MONKEYS_AT||LA36_0==LT) ) {
                    alt36=1;
                }


                switch (alt36) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:394:10: classBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_classBodyDeclaration_in_classBody1336);
            	    classBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop36;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_classBody1358); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 23, classBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classBody"


    // $ANTLR start "interfaceBody"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:399:1: interfaceBody : '{' ( interfaceBodyDeclaration )* '}' ;
    public final void interfaceBody() throws RecognitionException {
        int interfaceBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 24) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:400:3: ( '{' ( interfaceBodyDeclaration )* '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:400:7: '{' ( interfaceBodyDeclaration )* '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_interfaceBody1378); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:401:9: ( interfaceBodyDeclaration )*
            loop37:
            do {
                int alt37=2;
                int LA37_0 = input.LA(1);

                if ( (LA37_0==IDENTIFIER||LA37_0==ABSTRACT||LA37_0==BOOLEAN||LA37_0==BYTE||(LA37_0>=CHAR && LA37_0<=CLASS)||LA37_0==DOUBLE||LA37_0==ENUM||LA37_0==FINAL||LA37_0==FLOAT||(LA37_0>=INT && LA37_0<=NATIVE)||(LA37_0>=PRIVATE && LA37_0<=PUBLIC)||(LA37_0>=SHORT && LA37_0<=STRICTFP)||LA37_0==SYNCHRONIZED||LA37_0==TRANSIENT||(LA37_0>=VOID && LA37_0<=VOLATILE)||LA37_0==SEMI||LA37_0==MONKEYS_AT||LA37_0==LT) ) {
                    alt37=1;
                }


                switch (alt37) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:401:10: interfaceBodyDeclaration
            	    {
            	    pushFollow(FOLLOW_interfaceBodyDeclaration_in_interfaceBody1390);
            	    interfaceBodyDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop37;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_interfaceBody1412); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 24, interfaceBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceBody"


    // $ANTLR start "classBodyDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:406:1: classBodyDeclaration : ( ';' | memberDecl );
    public final void classBodyDeclaration() throws RecognitionException {
        int classBodyDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 25) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:407:5: ( ';' | memberDecl )
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==SEMI) ) {
                alt38=1;
            }
            else if ( (LA38_0==IDENTIFIER||LA38_0==ABSTRACT||LA38_0==BOOLEAN||LA38_0==BYTE||(LA38_0>=CHAR && LA38_0<=CLASS)||LA38_0==DOUBLE||LA38_0==ENUM||LA38_0==FINAL||LA38_0==FLOAT||(LA38_0>=INT && LA38_0<=NATIVE)||(LA38_0>=PRIVATE && LA38_0<=PUBLIC)||(LA38_0>=SHORT && LA38_0<=STRICTFP)||LA38_0==SYNCHRONIZED||LA38_0==TRANSIENT||(LA38_0>=VOID && LA38_0<=VOLATILE)||LA38_0==LBRACE||LA38_0==MONKEYS_AT||LA38_0==LT) ) {
                alt38=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 38, 0, input);

                throw nvae;
            }
            switch (alt38) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:407:9: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_classBodyDeclaration1432); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:409:9: memberDecl
                    {
                    pushFollow(FOLLOW_memberDecl_in_classBodyDeclaration1447);
                    memberDecl();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 25, classBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classBodyDeclaration"


    // $ANTLR start "memberDecl"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:412:1: memberDecl : ( fieldDeclaration | methodDeclaration | classDeclaration | interfaceDeclaration | initializationBlock );
    public final void memberDecl() throws RecognitionException {
        int memberDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 26) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:413:5: ( fieldDeclaration | methodDeclaration | classDeclaration | interfaceDeclaration | initializationBlock )
            int alt39=5;
            alt39 = dfa39.predict(input);
            switch (alt39) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:413:7: fieldDeclaration
                    {
                    pushFollow(FOLLOW_fieldDeclaration_in_memberDecl1465);
                    fieldDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:414:7: methodDeclaration
                    {
                    pushFollow(FOLLOW_methodDeclaration_in_memberDecl1473);
                    methodDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:415:7: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_memberDecl1481);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:416:7: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_memberDecl1489);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:417:7: initializationBlock
                    {
                    pushFollow(FOLLOW_initializationBlock_in_memberDecl1497);
                    initializationBlock();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 26, memberDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "memberDecl"


    // $ANTLR start "initializationBlock"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:420:1: initializationBlock : ( 'static' )? bodyTemp= block ;
    public final void initializationBlock() throws RecognitionException {
        int initializationBlock_StartIndex = input.index();
        String bodyTemp = null;



          InitializationBlockOfClass initializationBlock = new InitializationBlockOfClass();
          String initializationBlockName = "initializationBlock";

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 27) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:425:3: ( ( 'static' )? bodyTemp= block )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:425:6: ( 'static' )? bodyTemp= block
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:425:6: ( 'static' )?
            int alt40=2;
            int LA40_0 = input.LA(1);

            if ( (LA40_0==STATIC) ) {
                alt40=1;
            }
            switch (alt40) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:425:9: 'static'
                    {
                    match(input,STATIC,FOLLOW_STATIC_in_initializationBlock1522); if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      initializationBlock.setStatic(true);
                    }

                    }
                    break;

            }

            pushFollow(FOLLOW_block_in_initializationBlock1538);
            bodyTemp=block();

            state._fsp--;
            if (state.failed) return ;
            if ( state.backtracking==0 ) {


                      initializationBlock.setInitializationBlockName(initializationBlockName + getInitializationBlockCount().toString());
                      initializationBlock.setCurrentClass(getCurrentClassOrInterfaceNamePile());
                      initializationBlock.setCurrentPackage(getPackageName());
                      initializationBlock.setBody(bodyTemp);
                      initializationBlock.setJavadoc(getJavadocComment());
                      
                      getBuilder().createInitializationBlock(initializationBlock);
                      
                      incrementInitializationBlockCount();
                    
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 27, initializationBlock_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "initializationBlock"


    // $ANTLR start "methodDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:441:1: methodDeclaration : (constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}' | methodModifiers= modifiers ( typeParameters )? (returnTypeTemp= type | 'void' ) methodIdentifier= IDENTIFIER formalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? (blockTemp= block | ';' ) );
    public final void methodDeclaration() throws RecognitionException {
        int methodDeclaration_StartIndex = input.index();
        Token constructorIdentifier=null;
        Token methodIdentifier=null;
        int constructorModifiers = 0;

        List<ParameterOfMethode> formalParametersList = null;

        List<String> exceptionListTemp = null;

        Java1_6Parser.explicitConstructorInvocation_return explicitConstructorTemp = null;

        Java1_6Parser.blockStatement_return blockStatementTemp = null;

        int methodModifiers = 0;

        String returnTypeTemp = null;

        String blockTemp = null;



        		String returnType = null;
        		List<String> exceptionList = new ArrayList<String>();
        		DeclarationOfMethod declarationOfMethod = new DeclarationOfMethod();
        		StringBuffer bodyTextBuffer = new StringBuffer();
        		String bodyText = null;
        	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 28) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:450:2: (constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}' | methodModifiers= modifiers ( typeParameters )? (returnTypeTemp= type | 'void' ) methodIdentifier= IDENTIFIER formalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? (blockTemp= block | ';' ) )
            int alt50=2;
            alt50 = dfa50.predict(input);
            switch (alt50) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:452:4: constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}'
                    {
                    pushFollow(FOLLOW_modifiers_in_methodDeclaration1575);
                    constructorModifiers=modifiers();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:453:5: ( typeParameters )?
                    int alt41=2;
                    int LA41_0 = input.LA(1);

                    if ( (LA41_0==LT) ) {
                        alt41=1;
                    }
                    switch (alt41) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:453:7: typeParameters
                            {
                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration1583);
                            typeParameters();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    constructorIdentifier=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration1594); if (state.failed) return ;
                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration1603);
                    formalParametersList=formalParameters();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:456:5: ( 'throws' exceptionListTemp= qualifiedNameList )?
                    int alt42=2;
                    int LA42_0 = input.LA(1);

                    if ( (LA42_0==THROWS) ) {
                        alt42=1;
                    }
                    switch (alt42) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:456:7: 'throws' exceptionListTemp= qualifiedNameList
                            {
                            match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration1611); if (state.failed) return ;
                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration1615);
                            exceptionListTemp=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              exceptionList = exceptionListTemp;
                            }

                            }
                            break;

                    }

                    match(input,LBRACE,FOLLOW_LBRACE_in_methodDeclaration1627); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:9: (explicitConstructorTemp= explicitConstructorInvocation )?
                    int alt43=2;
                    alt43 = dfa43.predict(input);
                    switch (alt43) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:11: explicitConstructorTemp= explicitConstructorInvocation
                            {
                            pushFollow(FOLLOW_explicitConstructorInvocation_in_methodDeclaration1642);
                            explicitConstructorTemp=explicitConstructorInvocation();

                            state._fsp--;
                            if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                               bodyTextBuffer.append((explicitConstructorTemp!=null?input.toString(explicitConstructorTemp.start,explicitConstructorTemp.stop):null)); 
                            }

                            }
                            break;

                    }

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:459:9: (blockStatementTemp= blockStatement )*
                    loop44:
                    do {
                        int alt44=2;
                        int LA44_0 = input.LA(1);

                        if ( ((LA44_0>=IDENTIFIER && LA44_0<=NULL)||(LA44_0>=ABSTRACT && LA44_0<=BYTE)||(LA44_0>=CHAR && LA44_0<=CLASS)||LA44_0==CONTINUE||(LA44_0>=DO && LA44_0<=DOUBLE)||LA44_0==ENUM||LA44_0==FINAL||(LA44_0>=FLOAT && LA44_0<=FOR)||LA44_0==IF||(LA44_0>=INT && LA44_0<=NEW)||(LA44_0>=PRIVATE && LA44_0<=THROW)||(LA44_0>=TRANSIENT && LA44_0<=LPAREN)||LA44_0==LBRACE||LA44_0==SEMI||(LA44_0>=BANG && LA44_0<=TILDE)||(LA44_0>=PLUSPLUS && LA44_0<=SUB)||LA44_0==MONKEYS_AT||LA44_0==LT) ) {
                            alt44=1;
                        }


                        switch (alt44) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:459:11: blockStatementTemp= blockStatement
                    	    {
                    	    pushFollow(FOLLOW_blockStatement_in_methodDeclaration1662);
                    	    blockStatementTemp=blockStatement();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    if ( state.backtracking==0 ) {
                    	       bodyTextBuffer.append((blockStatementTemp!=null?input.toString(blockStatementTemp.start,blockStatementTemp.stop):null)); 
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop44;
                        }
                    } while (true);

                    match(input,RBRACE,FOLLOW_RBRACE_in_methodDeclaration1675); if (state.failed) return ;
                    if ( state.backtracking==0 ) {

                            declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
                            declarationOfMethod.setCurrentPackage(getPackageName());
                            declarationOfMethod.setJavadoc(getJavadocComment());
                            declarationOfMethod.setModifiers(constructorModifiers);
                            declarationOfMethod.setMethodReturnType(null);
                            declarationOfMethod.setMethodName((constructorIdentifier!=null?constructorIdentifier.getText():null));
                            declarationOfMethod.setMethodExceptionsList(exceptionList);
                            declarationOfMethod.setMethodParametersList(formalParametersList);
                            declarationOfMethod.setBody(bodyTextBuffer.toString());
                            getBuilder().createMethod(declarationOfMethod);
                          
                    }

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:475:3: methodModifiers= modifiers ( typeParameters )? (returnTypeTemp= type | 'void' ) methodIdentifier= IDENTIFIER formalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? (blockTemp= block | ';' )
                    {
                    pushFollow(FOLLOW_modifiers_in_methodDeclaration1701);
                    methodModifiers=modifiers();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:476:5: ( typeParameters )?
                    int alt45=2;
                    int LA45_0 = input.LA(1);

                    if ( (LA45_0==LT) ) {
                        alt45=1;
                    }
                    switch (alt45) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:476:7: typeParameters
                            {
                            pushFollow(FOLLOW_typeParameters_in_methodDeclaration1709);
                            typeParameters();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:477:5: (returnTypeTemp= type | 'void' )
                    int alt46=2;
                    int LA46_0 = input.LA(1);

                    if ( (LA46_0==IDENTIFIER||LA46_0==BOOLEAN||LA46_0==BYTE||LA46_0==CHAR||LA46_0==DOUBLE||LA46_0==FLOAT||LA46_0==INT||LA46_0==LONG||LA46_0==SHORT) ) {
                        alt46=1;
                    }
                    else if ( (LA46_0==VOID) ) {
                        alt46=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 46, 0, input);

                        throw nvae;
                    }
                    switch (alt46) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:478:4: returnTypeTemp= type
                            {
                            pushFollow(FOLLOW_type_in_methodDeclaration1725);
                            returnTypeTemp=type();

                            state._fsp--;
                            if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              returnType = returnTypeTemp;
                            }

                            }
                            break;
                        case 2 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:480:4: 'void'
                            {
                            match(input,VOID,FOLLOW_VOID_in_methodDeclaration1742); if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              returnType = "void";
                            }

                            }
                            break;

                    }

                    methodIdentifier=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodDeclaration1758); if (state.failed) return ;
                    pushFollow(FOLLOW_formalParameters_in_methodDeclaration1766);
                    formalParametersList=formalParameters();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:484:5: ( '[' ']' )*
                    loop47:
                    do {
                        int alt47=2;
                        int LA47_0 = input.LA(1);

                        if ( (LA47_0==LBRACKET) ) {
                            alt47=1;
                        }


                        switch (alt47) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:484:7: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_methodDeclaration1774); if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_methodDeclaration1776); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop47;
                        }
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:485:5: ( 'throws' exceptionListTemp= qualifiedNameList )?
                    int alt48=2;
                    int LA48_0 = input.LA(1);

                    if ( (LA48_0==THROWS) ) {
                        alt48=1;
                    }
                    switch (alt48) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:485:7: 'throws' exceptionListTemp= qualifiedNameList
                            {
                            match(input,THROWS,FOLLOW_THROWS_in_methodDeclaration1786); if (state.failed) return ;
                            pushFollow(FOLLOW_qualifiedNameList_in_methodDeclaration1790);
                            exceptionListTemp=qualifiedNameList();

                            state._fsp--;
                            if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              exceptionList = exceptionListTemp;
                            }

                            }
                            break;

                    }

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:486:5: (blockTemp= block | ';' )
                    int alt49=2;
                    int LA49_0 = input.LA(1);

                    if ( (LA49_0==LBRACE) ) {
                        alt49=1;
                    }
                    else if ( (LA49_0==SEMI) ) {
                        alt49=2;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 49, 0, input);

                        throw nvae;
                    }
                    switch (alt49) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:486:9: blockTemp= block
                            {
                            pushFollow(FOLLOW_block_in_methodDeclaration1807);
                            blockTemp=block();

                            state._fsp--;
                            if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              bodyText = blockTemp;
                            }

                            }
                            break;
                        case 2 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:487:9: ';'
                            {
                            match(input,SEMI,FOLLOW_SEMI_in_methodDeclaration1819); if (state.failed) return ;
                            if ( state.backtracking==0 ) {
                              bodyText = ";";
                            }

                            }
                            break;

                    }

                    if ( state.backtracking==0 ) {

                            declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
                            declarationOfMethod.setCurrentPackage(getPackageName());
                            declarationOfMethod.setJavadoc(getJavadocComment());
                            declarationOfMethod.setModifiers(methodModifiers);
                            declarationOfMethod.setMethodReturnType(returnType);
                            declarationOfMethod.setMethodName((methodIdentifier!=null?methodIdentifier.getText():null));
                            declarationOfMethod.setMethodExceptionsList(exceptionList);
                            declarationOfMethod.setMethodParametersList(formalParametersList);
                            declarationOfMethod.setBody(bodyText);
                            getBuilder().createMethod(declarationOfMethod);
                          
                    }

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 28, methodDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "methodDeclaration"


    // $ANTLR start "fieldDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:504:1: fieldDeclaration : fieldModifiers= modifiers fieldType= type variableDeclarator[fieldModifiers,fieldType] ( ',' variableDeclarator[fieldModifiers,fieldType] )* ';' ;
    public final void fieldDeclaration() throws RecognitionException {
        int fieldDeclaration_StartIndex = input.index();
        int fieldModifiers = 0;

        String fieldType = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 29) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:505:3: (fieldModifiers= modifiers fieldType= type variableDeclarator[fieldModifiers,fieldType] ( ',' variableDeclarator[fieldModifiers,fieldType] )* ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:505:5: fieldModifiers= modifiers fieldType= type variableDeclarator[fieldModifiers,fieldType] ( ',' variableDeclarator[fieldModifiers,fieldType] )* ';'
            {
            pushFollow(FOLLOW_modifiers_in_fieldDeclaration1851);
            fieldModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_fieldDeclaration1859);
            fieldType=type();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration1865);
            variableDeclarator(fieldModifiers, fieldType);

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:508:5: ( ',' variableDeclarator[fieldModifiers,fieldType] )*
            loop51:
            do {
                int alt51=2;
                int LA51_0 = input.LA(1);

                if ( (LA51_0==COMMA) ) {
                    alt51=1;
                }


                switch (alt51) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:508:7: ',' variableDeclarator[fieldModifiers,fieldType]
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_fieldDeclaration1874); if (state.failed) return ;
            	    pushFollow(FOLLOW_variableDeclarator_in_fieldDeclaration1876);
            	    variableDeclarator(fieldModifiers, fieldType);

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop51;
                }
            } while (true);

            match(input,SEMI,FOLLOW_SEMI_in_fieldDeclaration1886); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 29, fieldDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "fieldDeclaration"


    // $ANTLR start "variableDeclarator"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:512:1: variableDeclarator[int fieldModifiers, String fieldType] : fieldIdentifier= IDENTIFIER ( '[' ']' )* ( '=' decriptionVariableInitializer= variableInitializer )? ;
    public final void variableDeclarator(int fieldModifiers, String fieldType) throws RecognitionException {
        int variableDeclarator_StartIndex = input.index();
        Token fieldIdentifier=null;
        Java1_6Parser.variableInitializer_return decriptionVariableInitializer = null;



          StringBuffer stringBuffer = new StringBuffer();
          FieldOfClass fieldOfClass = new FieldOfClass();
          String variableInitializerValue = null;

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 30) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:518:3: (fieldIdentifier= IDENTIFIER ( '[' ']' )* ( '=' decriptionVariableInitializer= variableInitializer )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:518:5: fieldIdentifier= IDENTIFIER ( '[' ']' )* ( '=' decriptionVariableInitializer= variableInitializer )?
            {
            fieldIdentifier=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_variableDeclarator1907); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               stringBuffer.append((fieldIdentifier!=null?fieldIdentifier.getText():null)); 
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:519:5: ( '[' ']' )*
            loop52:
            do {
                int alt52=2;
                int LA52_0 = input.LA(1);

                if ( (LA52_0==LBRACKET) ) {
                    alt52=1;
                }


                switch (alt52) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:519:6: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_variableDeclarator1916); if (state.failed) return ;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_variableDeclarator1917); if (state.failed) return ;
            	    if ( state.backtracking==0 ) {
            	       stringBuffer.append("[]"); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop52;
                }
            } while (true);

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:520:5: ( '=' decriptionVariableInitializer= variableInitializer )?
            int alt53=2;
            int LA53_0 = input.LA(1);

            if ( (LA53_0==EQ) ) {
                alt53=1;
            }
            switch (alt53) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:520:6: '=' decriptionVariableInitializer= variableInitializer
                    {
                    match(input,EQ,FOLLOW_EQ_in_variableDeclarator1929); if (state.failed) return ;
                    pushFollow(FOLLOW_variableInitializer_in_variableDeclarator1933);
                    decriptionVariableInitializer=variableInitializer();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      variableInitializerValue = (decriptionVariableInitializer!=null?input.toString(decriptionVariableInitializer.start,decriptionVariableInitializer.stop):null); 
                    }

                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               
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

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 30, variableDeclarator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "variableDeclarator"


    // $ANTLR start "interfaceBodyDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:536:1: interfaceBodyDeclaration : ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' );
    public final void interfaceBodyDeclaration() throws RecognitionException {
        int interfaceBodyDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 31) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:540:5: ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' )
            int alt54=5;
            alt54 = dfa54.predict(input);
            switch (alt54) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:541:9: interfaceFieldDeclaration
                    {
                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration1968);
                    interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:542:9: interfaceMethodDeclaration
                    {
                    pushFollow(FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration1978);
                    interfaceMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:543:9: interfaceDeclaration
                    {
                    pushFollow(FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration1988);
                    interfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:544:9: classDeclaration
                    {
                    pushFollow(FOLLOW_classDeclaration_in_interfaceBodyDeclaration1998);
                    classDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:545:9: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_interfaceBodyDeclaration2008); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 31, interfaceBodyDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceBodyDeclaration"


    // $ANTLR start "interfaceMethodDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:548:1: interfaceMethodDeclaration : interfaceMethodModifiers= modifiers (interfaceMethodTypeParameters= typeParameters )? (returnTypeTemp= type | 'void' ) interfaceMethodName= IDENTIFIER interfaceMethodFormalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? ';' ;
    public final void interfaceMethodDeclaration() throws RecognitionException {
        int interfaceMethodDeclaration_StartIndex = input.index();
        Token interfaceMethodName=null;
        int interfaceMethodModifiers = 0;

        List<String> interfaceMethodTypeParameters = null;

        String returnTypeTemp = null;

        List<ParameterOfMethode> interfaceMethodFormalParametersList = null;

        List<String> exceptionListTemp = null;



            String returnType = null;
            List<String> exceptionList = new ArrayList<String>();
            DeclarationOfMethod declarationOfMethod = new DeclarationOfMethod();
          
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 32) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:554:3: (interfaceMethodModifiers= modifiers (interfaceMethodTypeParameters= typeParameters )? (returnTypeTemp= type | 'void' ) interfaceMethodName= IDENTIFIER interfaceMethodFormalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:554:5: interfaceMethodModifiers= modifiers (interfaceMethodTypeParameters= typeParameters )? (returnTypeTemp= type | 'void' ) interfaceMethodName= IDENTIFIER interfaceMethodFormalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? ';'
            {
            pushFollow(FOLLOW_modifiers_in_interfaceMethodDeclaration2032);
            interfaceMethodModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:555:5: (interfaceMethodTypeParameters= typeParameters )?
            int alt55=2;
            int LA55_0 = input.LA(1);

            if ( (LA55_0==LT) ) {
                alt55=1;
            }
            switch (alt55) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:555:7: interfaceMethodTypeParameters= typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_interfaceMethodDeclaration2042);
                    interfaceMethodTypeParameters=typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:556:5: (returnTypeTemp= type | 'void' )
            int alt56=2;
            int LA56_0 = input.LA(1);

            if ( (LA56_0==IDENTIFIER||LA56_0==BOOLEAN||LA56_0==BYTE||LA56_0==CHAR||LA56_0==DOUBLE||LA56_0==FLOAT||LA56_0==INT||LA56_0==LONG||LA56_0==SHORT) ) {
                alt56=1;
            }
            else if ( (LA56_0==VOID) ) {
                alt56=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 56, 0, input);

                throw nvae;
            }
            switch (alt56) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:556:7: returnTypeTemp= type
                    {
                    pushFollow(FOLLOW_type_in_interfaceMethodDeclaration2055);
                    returnTypeTemp=type();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      returnType = returnTypeTemp;
                    }

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:557:8: 'void'
                    {
                    match(input,VOID,FOLLOW_VOID_in_interfaceMethodDeclaration2066); if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      returnType = "void";
                    }

                    }
                    break;

            }

            interfaceMethodName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration2082); if (state.failed) return ;
            pushFollow(FOLLOW_formalParameters_in_interfaceMethodDeclaration2090);
            interfaceMethodFormalParametersList=formalParameters();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:561:5: ( '[' ']' )*
            loop57:
            do {
                int alt57=2;
                int LA57_0 = input.LA(1);

                if ( (LA57_0==LBRACKET) ) {
                    alt57=1;
                }


                switch (alt57) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:561:7: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_interfaceMethodDeclaration2098); if (state.failed) return ;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_interfaceMethodDeclaration2099); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop57;
                }
            } while (true);

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:562:5: ( 'throws' exceptionListTemp= qualifiedNameList )?
            int alt58=2;
            int LA58_0 = input.LA(1);

            if ( (LA58_0==THROWS) ) {
                alt58=1;
            }
            switch (alt58) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:562:6: 'throws' exceptionListTemp= qualifiedNameList
                    {
                    match(input,THROWS,FOLLOW_THROWS_in_interfaceMethodDeclaration2109); if (state.failed) return ;
                    pushFollow(FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration2113);
                    exceptionListTemp=qualifiedNameList();

                    state._fsp--;
                    if (state.failed) return ;
                    if ( state.backtracking==0 ) {
                      exceptionList = exceptionListTemp;
                    }

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_interfaceMethodDeclaration2124); if (state.failed) return ;
            if ( state.backtracking==0 ) {

                    declarationOfMethod.setCurrentClass(getCurrentClassOrInterfaceNamePile());
                    declarationOfMethod.setCurrentPackage(getPackageName());
                    declarationOfMethod.setJavadoc(getJavadocComment());
                    declarationOfMethod.setModifiers(interfaceMethodModifiers);
                    declarationOfMethod.setMethodReturnType(returnType);
                    declarationOfMethod.setMethodName((interfaceMethodName!=null?interfaceMethodName.getText():null));
                    declarationOfMethod.setMethodExceptionsList(exceptionList);
                    declarationOfMethod.setMethodParametersList(interfaceMethodFormalParametersList);
                    getBuilder().createMethod(declarationOfMethod);
                  
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 32, interfaceMethodDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceMethodDeclaration"


    // $ANTLR start "interfaceFieldDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:577:1: interfaceFieldDeclaration : interfaceFieldModifiers= modifiers interfaceFieldType= type variableDeclarator[interfaceFieldModifiers,interfaceFieldType] ( ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType] )* ';' ;
    public final void interfaceFieldDeclaration() throws RecognitionException {
        int interfaceFieldDeclaration_StartIndex = input.index();
        int interfaceFieldModifiers = 0;

        String interfaceFieldType = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 33) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:583:3: (interfaceFieldModifiers= modifiers interfaceFieldType= type variableDeclarator[interfaceFieldModifiers,interfaceFieldType] ( ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType] )* ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:583:5: interfaceFieldModifiers= modifiers interfaceFieldType= type variableDeclarator[interfaceFieldModifiers,interfaceFieldType] ( ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType] )* ';'
            {
            pushFollow(FOLLOW_modifiers_in_interfaceFieldDeclaration2148);
            interfaceFieldModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_interfaceFieldDeclaration2157);
            interfaceFieldType=type();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2164);
            variableDeclarator(interfaceFieldModifiers, interfaceFieldType);

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:586:5: ( ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType] )*
            loop59:
            do {
                int alt59=2;
                int LA59_0 = input.LA(1);

                if ( (LA59_0==COMMA) ) {
                    alt59=1;
                }


                switch (alt59) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:586:7: ',' variableDeclarator[interfaceFieldModifiers,interfaceFieldType]
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_interfaceFieldDeclaration2173); if (state.failed) return ;
            	    pushFollow(FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2175);
            	    variableDeclarator(interfaceFieldModifiers, interfaceFieldType);

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop59;
                }
            } while (true);

            match(input,SEMI,FOLLOW_SEMI_in_interfaceFieldDeclaration2185); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 33, interfaceFieldDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceFieldDeclaration"


    // $ANTLR start "type"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:591:1: type returns [String typeNameString = null] : (classType= classOrInterfaceType ( '[' ']' )* | primitiveTypeOfClass= primitiveType ( '[' ']' )* ) ;
    public final String type() throws RecognitionException {
        String typeNameString =  null;
        int type_StartIndex = input.index();
        String classType = null;

        Java1_6Parser.primitiveType_return primitiveTypeOfClass = null;



          StringBuffer stringBuffer = new StringBuffer();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 34) ) { return typeNameString; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:595:3: ( (classType= classOrInterfaceType ( '[' ']' )* | primitiveTypeOfClass= primitiveType ( '[' ']' )* ) )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:595:5: (classType= classOrInterfaceType ( '[' ']' )* | primitiveTypeOfClass= primitiveType ( '[' ']' )* )
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:595:5: (classType= classOrInterfaceType ( '[' ']' )* | primitiveTypeOfClass= primitiveType ( '[' ']' )* )
            int alt62=2;
            int LA62_0 = input.LA(1);

            if ( (LA62_0==IDENTIFIER) ) {
                alt62=1;
            }
            else if ( (LA62_0==BOOLEAN||LA62_0==BYTE||LA62_0==CHAR||LA62_0==DOUBLE||LA62_0==FLOAT||LA62_0==INT||LA62_0==LONG||LA62_0==SHORT) ) {
                alt62=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return typeNameString;}
                NoViableAltException nvae =
                    new NoViableAltException("", 62, 0, input);

                throw nvae;
            }
            switch (alt62) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:595:7: classType= classOrInterfaceType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_type2212);
                    classType=classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return typeNameString;
                    if ( state.backtracking==0 ) {
                      stringBuffer.append(classType);
                    }
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:596:7: ( '[' ']' )*
                    loop60:
                    do {
                        int alt60=2;
                        int LA60_0 = input.LA(1);

                        if ( (LA60_0==LBRACKET) ) {
                            alt60=1;
                        }


                        switch (alt60) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:596:9: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_type2224); if (state.failed) return typeNameString;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_type2225); if (state.failed) return typeNameString;
                    	    if ( state.backtracking==0 ) {
                    	      stringBuffer.append("[]");
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop60;
                        }
                    } while (true);


                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:598:4: primitiveTypeOfClass= primitiveType ( '[' ']' )*
                    {
                    pushFollow(FOLLOW_primitiveType_in_type2246);
                    primitiveTypeOfClass=primitiveType();

                    state._fsp--;
                    if (state.failed) return typeNameString;
                    if ( state.backtracking==0 ) {
                      stringBuffer.append((primitiveTypeOfClass!=null?input.toString(primitiveTypeOfClass.start,primitiveTypeOfClass.stop):null));
                    }
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:599:7: ( '[' ']' )*
                    loop61:
                    do {
                        int alt61=2;
                        int LA61_0 = input.LA(1);

                        if ( (LA61_0==LBRACKET) ) {
                            alt61=1;
                        }


                        switch (alt61) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:599:9: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_type2258); if (state.failed) return typeNameString;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_type2259); if (state.failed) return typeNameString;
                    	    if ( state.backtracking==0 ) {
                    	      stringBuffer.append("[]");
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop61;
                        }
                    } while (true);


                    }
                    break;

            }

            if ( state.backtracking==0 ) {
               typeNameString = stringBuffer.toString(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 34, type_StartIndex); }
        }
        return typeNameString;
    }
    // $ANTLR end "type"


    // $ANTLR start "classOrInterfaceType"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:605:1: classOrInterfaceType returns [String classOrInterfaceTypeString = null] : identifier1= IDENTIFIER ( typeArguments )? ( '.' identifier2= IDENTIFIER ( typeArguments )? )* ;
    public final String classOrInterfaceType() throws RecognitionException {
        String classOrInterfaceTypeString =  null;
        int classOrInterfaceType_StartIndex = input.index();
        Token identifier1=null;
        Token identifier2=null;


          StringBuffer stringBuffer = new StringBuffer();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 35) ) { return classOrInterfaceTypeString; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:609:3: (identifier1= IDENTIFIER ( typeArguments )? ( '.' identifier2= IDENTIFIER ( typeArguments )? )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:609:5: identifier1= IDENTIFIER ( typeArguments )? ( '.' identifier2= IDENTIFIER ( typeArguments )? )*
            {
            identifier1=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType2299); if (state.failed) return classOrInterfaceTypeString;
            if ( state.backtracking==0 ) {
              stringBuffer.append((identifier1!=null?identifier1.getText():null));
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:610:5: ( typeArguments )?
            int alt63=2;
            int LA63_0 = input.LA(1);

            if ( (LA63_0==LT) ) {
                int LA63_1 = input.LA(2);

                if ( (LA63_1==IDENTIFIER||LA63_1==BOOLEAN||LA63_1==BYTE||LA63_1==CHAR||LA63_1==DOUBLE||LA63_1==FLOAT||LA63_1==INT||LA63_1==LONG||LA63_1==SHORT||LA63_1==QUES) ) {
                    alt63=1;
                }
            }
            switch (alt63) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:610:7: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2309);
                    typeArguments();

                    state._fsp--;
                    if (state.failed) return classOrInterfaceTypeString;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:611:5: ( '.' identifier2= IDENTIFIER ( typeArguments )? )*
            loop65:
            do {
                int alt65=2;
                int LA65_0 = input.LA(1);

                if ( (LA65_0==DOT) ) {
                    alt65=1;
                }


                switch (alt65) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:611:7: '.' identifier2= IDENTIFIER ( typeArguments )?
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_classOrInterfaceType2320); if (state.failed) return classOrInterfaceTypeString;
            	    if ( state.backtracking==0 ) {
            	      stringBuffer.append(".");
            	    }
            	    identifier2=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classOrInterfaceType2332); if (state.failed) return classOrInterfaceTypeString;
            	    if ( state.backtracking==0 ) {
            	      stringBuffer.append((identifier2!=null?identifier2.getText():null));
            	    }
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:613:7: ( typeArguments )?
            	    int alt64=2;
            	    int LA64_0 = input.LA(1);

            	    if ( (LA64_0==LT) ) {
            	        int LA64_1 = input.LA(2);

            	        if ( (LA64_1==IDENTIFIER||LA64_1==BOOLEAN||LA64_1==BYTE||LA64_1==CHAR||LA64_1==DOUBLE||LA64_1==FLOAT||LA64_1==INT||LA64_1==LONG||LA64_1==SHORT||LA64_1==QUES) ) {
            	            alt64=1;
            	        }
            	    }
            	    switch (alt64) {
            	        case 1 :
            	            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:613:9: typeArguments
            	            {
            	            pushFollow(FOLLOW_typeArguments_in_classOrInterfaceType2344);
            	            typeArguments();

            	            state._fsp--;
            	            if (state.failed) return classOrInterfaceTypeString;

            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop65;
                }
            } while (true);

            if ( state.backtracking==0 ) {
               classOrInterfaceTypeString = stringBuffer.toString(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 35, classOrInterfaceType_StartIndex); }
        }
        return classOrInterfaceTypeString;
    }
    // $ANTLR end "classOrInterfaceType"

    public static class primitiveType_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "primitiveType"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:618:1: primitiveType : ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' );
    public final Java1_6Parser.primitiveType_return primitiveType() throws RecognitionException {
        Java1_6Parser.primitiveType_return retval = new Java1_6Parser.primitiveType_return();
        retval.start = input.LT(1);
        int primitiveType_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 36) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:619:5: ( 'boolean' | 'char' | 'byte' | 'short' | 'int' | 'long' | 'float' | 'double' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:
            {
            if ( input.LA(1)==BOOLEAN||input.LA(1)==BYTE||input.LA(1)==CHAR||input.LA(1)==DOUBLE||input.LA(1)==FLOAT||input.LA(1)==INT||input.LA(1)==LONG||input.LA(1)==SHORT ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 36, primitiveType_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "primitiveType"


    // $ANTLR start "typeArguments"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:629:1: typeArguments : '<' typeArgument ( ',' typeArgument )* '>' ;
    public final void typeArguments() throws RecognitionException {
        int typeArguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 37) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:630:5: ( '<' typeArgument ( ',' typeArgument )* '>' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:630:9: '<' typeArgument ( ',' typeArgument )* '>'
            {
            match(input,LT,FOLLOW_LT_in_typeArguments2469); if (state.failed) return ;
            pushFollow(FOLLOW_typeArgument_in_typeArguments2471);
            typeArgument();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:631:9: ( ',' typeArgument )*
            loop66:
            do {
                int alt66=2;
                int LA66_0 = input.LA(1);

                if ( (LA66_0==COMMA) ) {
                    alt66=1;
                }


                switch (alt66) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:631:10: ',' typeArgument
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_typeArguments2482); if (state.failed) return ;
            	    pushFollow(FOLLOW_typeArgument_in_typeArguments2484);
            	    typeArgument();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop66;
                }
            } while (true);

            match(input,GT,FOLLOW_GT_in_typeArguments2506); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 37, typeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeArguments"


    // $ANTLR start "typeArgument"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:636:1: typeArgument : ( type | '?' ( ( 'extends' | 'super' ) type )? );
    public final void typeArgument() throws RecognitionException {
        int typeArgument_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 38) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:637:5: ( type | '?' ( ( 'extends' | 'super' ) type )? )
            int alt68=2;
            int LA68_0 = input.LA(1);

            if ( (LA68_0==IDENTIFIER||LA68_0==BOOLEAN||LA68_0==BYTE||LA68_0==CHAR||LA68_0==DOUBLE||LA68_0==FLOAT||LA68_0==INT||LA68_0==LONG||LA68_0==SHORT) ) {
                alt68=1;
            }
            else if ( (LA68_0==QUES) ) {
                alt68=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 68, 0, input);

                throw nvae;
            }
            switch (alt68) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:637:9: type
                    {
                    pushFollow(FOLLOW_type_in_typeArgument2526);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:638:9: '?' ( ( 'extends' | 'super' ) type )?
                    {
                    match(input,QUES,FOLLOW_QUES_in_typeArgument2536); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:639:9: ( ( 'extends' | 'super' ) type )?
                    int alt67=2;
                    int LA67_0 = input.LA(1);

                    if ( (LA67_0==EXTENDS||LA67_0==SUPER) ) {
                        alt67=1;
                    }
                    switch (alt67) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:640:13: ( 'extends' | 'super' ) type
                            {
                            if ( input.LA(1)==EXTENDS||input.LA(1)==SUPER ) {
                                input.consume();
                                state.errorRecovery=false;state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }

                            pushFollow(FOLLOW_type_in_typeArgument2604);
                            type();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 38, typeArgument_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeArgument"


    // $ANTLR start "qualifiedNameList"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:647:1: qualifiedNameList returns [List<String> exceptionList = new ArrayList<String>()] : exceptionName= qualifiedName ( ',' exceptionName= qualifiedName )* ;
    public final List<String> qualifiedNameList() throws RecognitionException {
        List<String> exceptionList =  new ArrayList<String>();
        int qualifiedNameList_StartIndex = input.index();
        Java1_6Parser.qualifiedName_return exceptionName = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 39) ) { return exceptionList; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:648:3: (exceptionName= qualifiedName ( ',' exceptionName= qualifiedName )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:648:5: exceptionName= qualifiedName ( ',' exceptionName= qualifiedName )*
            {
            pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2636);
            exceptionName=qualifiedName();

            state._fsp--;
            if (state.failed) return exceptionList;
            if ( state.backtracking==0 ) {
              exceptionList.add((exceptionName!=null?input.toString(exceptionName.start,exceptionName.stop):null));
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:649:5: ( ',' exceptionName= qualifiedName )*
            loop69:
            do {
                int alt69=2;
                int LA69_0 = input.LA(1);

                if ( (LA69_0==COMMA) ) {
                    alt69=1;
                }


                switch (alt69) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:649:7: ',' exceptionName= qualifiedName
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_qualifiedNameList2646); if (state.failed) return exceptionList;
            	    pushFollow(FOLLOW_qualifiedName_in_qualifiedNameList2650);
            	    exceptionName=qualifiedName();

            	    state._fsp--;
            	    if (state.failed) return exceptionList;
            	    if ( state.backtracking==0 ) {
            	      exceptionList.add((exceptionName!=null?input.toString(exceptionName.start,exceptionName.stop):null));
            	    }

            	    }
            	    break;

            	default :
            	    break loop69;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 39, qualifiedNameList_StartIndex); }
        }
        return exceptionList;
    }
    // $ANTLR end "qualifiedNameList"


    // $ANTLR start "formalParameters"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:652:1: formalParameters returns [List<ParameterOfMethode> formalParametersList = new ArrayList<ParameterOfMethode>()] : '(' (formalParametersDeclarationListTemp= formalParameterDecls )? ')' ;
    public final List<ParameterOfMethode> formalParameters() throws RecognitionException {
        List<ParameterOfMethode> formalParametersList =  new ArrayList<ParameterOfMethode>();
        int formalParameters_StartIndex = input.index();
        List<ParameterOfMethode> formalParametersDeclarationListTemp = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 40) ) { return formalParametersList; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:653:5: ( '(' (formalParametersDeclarationListTemp= formalParameterDecls )? ')' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:654:5: '(' (formalParametersDeclarationListTemp= formalParameterDecls )? ')'
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_formalParameters2681); if (state.failed) return formalParametersList;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:655:5: (formalParametersDeclarationListTemp= formalParameterDecls )?
            int alt70=2;
            int LA70_0 = input.LA(1);

            if ( (LA70_0==IDENTIFIER||LA70_0==BOOLEAN||LA70_0==BYTE||LA70_0==CHAR||LA70_0==DOUBLE||LA70_0==FINAL||LA70_0==FLOAT||LA70_0==INT||LA70_0==LONG||LA70_0==SHORT||LA70_0==MONKEYS_AT) ) {
                alt70=1;
            }
            switch (alt70) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:656:6: formalParametersDeclarationListTemp= formalParameterDecls
                    {
                    pushFollow(FOLLOW_formalParameterDecls_in_formalParameters2696);
                    formalParametersDeclarationListTemp=formalParameterDecls();

                    state._fsp--;
                    if (state.failed) return formalParametersList;
                    if ( state.backtracking==0 ) {
                      formalParametersList = formalParametersDeclarationListTemp;
                    }

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_formalParameters2718); if (state.failed) return formalParametersList;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 40, formalParameters_StartIndex); }
        }
        return formalParametersList;
    }
    // $ANTLR end "formalParameters"


    // $ANTLR start "formalParameterDecls"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:662:1: formalParameterDecls returns [List<ParameterOfMethode> formalParametersDeclarationList = new ArrayList<ParameterOfMethode>()] : ( ellipsisParameterDecl | normalParameterType= normalParameterDecl ( ',' normalParameterType= normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl );
    public final List<ParameterOfMethode> formalParameterDecls() throws RecognitionException {
        List<ParameterOfMethode> formalParametersDeclarationList =  new ArrayList<ParameterOfMethode>();
        int formalParameterDecls_StartIndex = input.index();
        ParameterOfMethode normalParameterType = null;


        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 41) ) { return formalParametersDeclarationList; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:663:5: ( ellipsisParameterDecl | normalParameterType= normalParameterDecl ( ',' normalParameterType= normalParameterDecl )* | ( normalParameterDecl ',' )+ ellipsisParameterDecl )
            int alt73=3;
            switch ( input.LA(1) ) {
            case FINAL:
                {
                int LA73_1 = input.LA(2);

                if ( (synpred96_Java1_6()) ) {
                    alt73=1;
                }
                else if ( (synpred98_Java1_6()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 1, input);

                    throw nvae;
                }
                }
                break;
            case MONKEYS_AT:
                {
                int LA73_2 = input.LA(2);

                if ( (synpred96_Java1_6()) ) {
                    alt73=1;
                }
                else if ( (synpred98_Java1_6()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 2, input);

                    throw nvae;
                }
                }
                break;
            case IDENTIFIER:
                {
                int LA73_3 = input.LA(2);

                if ( (synpred96_Java1_6()) ) {
                    alt73=1;
                }
                else if ( (synpred98_Java1_6()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 3, input);

                    throw nvae;
                }
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                int LA73_4 = input.LA(2);

                if ( (synpred96_Java1_6()) ) {
                    alt73=1;
                }
                else if ( (synpred98_Java1_6()) ) {
                    alt73=2;
                }
                else if ( (true) ) {
                    alt73=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 73, 4, input);

                    throw nvae;
                }
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                NoViableAltException nvae =
                    new NoViableAltException("", 73, 0, input);

                throw nvae;
            }

            switch (alt73) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:664:3: ellipsisParameterDecl
                    {
                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls2744);
                    ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return formalParametersDeclarationList;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:665:7: normalParameterType= normalParameterDecl ( ',' normalParameterType= normalParameterDecl )*
                    {
                    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls2754);
                    normalParameterType=normalParameterDecl();

                    state._fsp--;
                    if (state.failed) return formalParametersDeclarationList;
                    if ( state.backtracking==0 ) {
                      formalParametersDeclarationList.add(normalParameterType);
                    }
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:667:5: ( ',' normalParameterType= normalParameterDecl )*
                    loop71:
                    do {
                        int alt71=2;
                        int LA71_0 = input.LA(1);

                        if ( (LA71_0==COMMA) ) {
                            alt71=1;
                        }


                        switch (alt71) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:667:7: ',' normalParameterType= normalParameterDecl
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls2769); if (state.failed) return formalParametersDeclarationList;
                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls2776);
                    	    normalParameterType=normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return formalParametersDeclarationList;
                    	    if ( state.backtracking==0 ) {
                    	      formalParametersDeclarationList.add(normalParameterType);
                    	    }

                    	    }
                    	    break;

                    	default :
                    	    break loop71;
                        }
                    } while (true);


                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:671:7: ( normalParameterDecl ',' )+ ellipsisParameterDecl
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:671:7: ( normalParameterDecl ',' )+
                    int cnt72=0;
                    loop72:
                    do {
                        int alt72=2;
                        switch ( input.LA(1) ) {
                        case FINAL:
                            {
                            int LA72_1 = input.LA(2);

                            if ( (synpred99_Java1_6()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case MONKEYS_AT:
                            {
                            int LA72_2 = input.LA(2);

                            if ( (synpred99_Java1_6()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case IDENTIFIER:
                            {
                            int LA72_3 = input.LA(2);

                            if ( (synpred99_Java1_6()) ) {
                                alt72=1;
                            }


                            }
                            break;
                        case BOOLEAN:
                        case BYTE:
                        case CHAR:
                        case DOUBLE:
                        case FLOAT:
                        case INT:
                        case LONG:
                        case SHORT:
                            {
                            int LA72_4 = input.LA(2);

                            if ( (synpred99_Java1_6()) ) {
                                alt72=1;
                            }


                            }
                            break;

                        }

                        switch (alt72) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:671:8: normalParameterDecl ','
                    	    {
                    	    pushFollow(FOLLOW_normalParameterDecl_in_formalParameterDecls2796);
                    	    normalParameterDecl();

                    	    state._fsp--;
                    	    if (state.failed) return formalParametersDeclarationList;
                    	    match(input,COMMA,FOLLOW_COMMA_in_formalParameterDecls2802); if (state.failed) return formalParametersDeclarationList;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt72 >= 1 ) break loop72;
                    	    if (state.backtracking>0) {state.failed=true; return formalParametersDeclarationList;}
                                EarlyExitException eee =
                                    new EarlyExitException(72, input);
                                throw eee;
                        }
                        cnt72++;
                    } while (true);

                    pushFollow(FOLLOW_ellipsisParameterDecl_in_formalParameterDecls2822);
                    ellipsisParameterDecl();

                    state._fsp--;
                    if (state.failed) return formalParametersDeclarationList;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 41, formalParameterDecls_StartIndex); }
        }
        return formalParametersDeclarationList;
    }
    // $ANTLR end "formalParameterDecls"


    // $ANTLR start "normalParameterDecl"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:677:1: normalParameterDecl returns [ParameterOfMethode normalParameter = new ParameterOfMethode()] : variableModifierTemp= variableModifiers parameterType= type identifier1= IDENTIFIER ( '[' ']' )* ;
    public final ParameterOfMethode normalParameterDecl() throws RecognitionException {
        ParameterOfMethode normalParameter =  new ParameterOfMethode();
        int normalParameterDecl_StartIndex = input.index();
        Token identifier1=null;
        Java1_6Parser.variableModifiers_return variableModifierTemp = null;

        String parameterType = null;



         		StringBuffer stringBuffer = new StringBuffer();
         		ParameterOfMethode parameterLocal = new ParameterOfMethode();
         	
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 42) ) { return normalParameter; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:683:3: (variableModifierTemp= variableModifiers parameterType= type identifier1= IDENTIFIER ( '[' ']' )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:684:3: variableModifierTemp= variableModifiers parameterType= type identifier1= IDENTIFIER ( '[' ']' )*
            {
            pushFollow(FOLLOW_variableModifiers_in_normalParameterDecl2857);
            variableModifierTemp=variableModifiers();

            state._fsp--;
            if (state.failed) return normalParameter;
            pushFollow(FOLLOW_type_in_normalParameterDecl2864);
            parameterType=type();

            state._fsp--;
            if (state.failed) return normalParameter;
            identifier1=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_normalParameterDecl2870); if (state.failed) return normalParameter;
            if ( state.backtracking==0 ) {
              stringBuffer.append((identifier1!=null?identifier1.getText():null));
            }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:687:5: ( '[' ']' )*
            loop74:
            do {
                int alt74=2;
                int LA74_0 = input.LA(1);

                if ( (LA74_0==LBRACKET) ) {
                    alt74=1;
                }


                switch (alt74) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:687:7: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_normalParameterDecl2880); if (state.failed) return normalParameter;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_normalParameterDecl2882); if (state.failed) return normalParameter;
            	    if ( state.backtracking==0 ) {
            	      stringBuffer.append("[]");
            	    }

            	    }
            	    break;

            	default :
            	    break loop74;
                }
            } while (true);

            if ( state.backtracking==0 ) {

                    parameterLocal.setVariableModifier((variableModifierTemp!=null?input.toString(variableModifierTemp.start,variableModifierTemp.stop):null));
                    parameterLocal.setTypeParameter(parameterType);
                    parameterLocal.setNameParameter(stringBuffer.toString());
                  	normalParameter = parameterLocal;
                  
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 42, normalParameterDecl_StartIndex); }
        }
        return normalParameter;
    }
    // $ANTLR end "normalParameterDecl"


    // $ANTLR start "ellipsisParameterDecl"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:696:1: ellipsisParameterDecl : variableModifiers type '...' IDENTIFIER ;
    public final void ellipsisParameterDecl() throws RecognitionException {
        int ellipsisParameterDecl_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 43) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:697:5: ( variableModifiers type '...' IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:697:9: variableModifiers type '...' IDENTIFIER
            {
            pushFollow(FOLLOW_variableModifiers_in_ellipsisParameterDecl2910);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_ellipsisParameterDecl2920);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,ELLIPSIS,FOLLOW_ELLIPSIS_in_ellipsisParameterDecl2923); if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_ellipsisParameterDecl2933); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 43, ellipsisParameterDecl_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "ellipsisParameterDecl"

    public static class explicitConstructorInvocation_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "explicitConstructorInvocation"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:703:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );
    public final Java1_6Parser.explicitConstructorInvocation_return explicitConstructorInvocation() throws RecognitionException {
        Java1_6Parser.explicitConstructorInvocation_return retval = new Java1_6Parser.explicitConstructorInvocation_return();
        retval.start = input.LT(1);
        int explicitConstructorInvocation_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 44) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:5: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' )
            int alt77=2;
            alt77 = dfa77.predict(input);
            switch (alt77) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:9: ( nonWildcardTypeArguments )?
                    int alt75=2;
                    int LA75_0 = input.LA(1);

                    if ( (LA75_0==LT) ) {
                        alt75=1;
                    }
                    switch (alt75) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:10: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2955);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    if ( input.LA(1)==SUPER||input.LA(1)==THIS ) {
                        input.consume();
                        state.errorRecovery=false;state.failed=false;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return retval;}
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation3013);
                    arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation3015); if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:711:9: primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';'
                    {
                    pushFollow(FOLLOW_primary_in_explicitConstructorInvocation3026);
                    primary();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,DOT,FOLLOW_DOT_in_explicitConstructorInvocation3036); if (state.failed) return retval;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:713:9: ( nonWildcardTypeArguments )?
                    int alt76=2;
                    int LA76_0 = input.LA(1);

                    if ( (LA76_0==LT) ) {
                        alt76=1;
                    }
                    switch (alt76) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:713:10: nonWildcardTypeArguments
                            {
                            pushFollow(FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3047);
                            nonWildcardTypeArguments();

                            state._fsp--;
                            if (state.failed) return retval;

                            }
                            break;

                    }

                    match(input,SUPER,FOLLOW_SUPER_in_explicitConstructorInvocation3068); if (state.failed) return retval;
                    pushFollow(FOLLOW_arguments_in_explicitConstructorInvocation3078);
                    arguments();

                    state._fsp--;
                    if (state.failed) return retval;
                    match(input,SEMI,FOLLOW_SEMI_in_explicitConstructorInvocation3080); if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 44, explicitConstructorInvocation_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "explicitConstructorInvocation"

    public static class qualifiedName_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "qualifiedName"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:719:1: qualifiedName : IDENTIFIER ( '.' IDENTIFIER )* ;
    public final Java1_6Parser.qualifiedName_return qualifiedName() throws RecognitionException {
        Java1_6Parser.qualifiedName_return retval = new Java1_6Parser.qualifiedName_return();
        retval.start = input.LT(1);
        int qualifiedName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 45) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:720:5: ( IDENTIFIER ( '.' IDENTIFIER )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:720:9: IDENTIFIER ( '.' IDENTIFIER )*
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName3100); if (state.failed) return retval;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:721:9: ( '.' IDENTIFIER )*
            loop78:
            do {
                int alt78=2;
                int LA78_0 = input.LA(1);

                if ( (LA78_0==DOT) ) {
                    alt78=1;
                }


                switch (alt78) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:721:10: '.' IDENTIFIER
            	    {
            	    match(input,DOT,FOLLOW_DOT_in_qualifiedName3111); if (state.failed) return retval;
            	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_qualifiedName3113); if (state.failed) return retval;

            	    }
            	    break;

            	default :
            	    break loop78;
                }
            } while (true);


            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 45, qualifiedName_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "qualifiedName"


    // $ANTLR start "annotations"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:725:1: annotations : ( annotation )+ ;
    public final void annotations() throws RecognitionException {
        int annotations_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 46) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:726:5: ( ( annotation )+ )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:726:9: ( annotation )+
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:726:9: ( annotation )+
            int cnt79=0;
            loop79:
            do {
                int alt79=2;
                int LA79_0 = input.LA(1);

                if ( (LA79_0==MONKEYS_AT) ) {
                    alt79=1;
                }


                switch (alt79) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:726:10: annotation
            	    {
            	    pushFollow(FOLLOW_annotation_in_annotations3145);
            	    annotation();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    if ( cnt79 >= 1 ) break loop79;
            	    if (state.backtracking>0) {state.failed=true; return ;}
                        EarlyExitException eee =
                            new EarlyExitException(79, input);
                        throw eee;
                }
                cnt79++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 46, annotations_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotations"


    // $ANTLR start "annotation"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:730:1: annotation : '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? ;
    public final void annotation() throws RecognitionException {
        int annotation_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 47) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:735:5: ( '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:735:9: '@' qualifiedName ( '(' ( elementValuePairs | elementValue )? ')' )?
            {
            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_annotation3178); if (state.failed) return ;
            pushFollow(FOLLOW_qualifiedName_in_annotation3180);
            qualifiedName();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:736:9: ( '(' ( elementValuePairs | elementValue )? ')' )?
            int alt81=2;
            int LA81_0 = input.LA(1);

            if ( (LA81_0==LPAREN) ) {
                alt81=1;
            }
            switch (alt81) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:736:13: '(' ( elementValuePairs | elementValue )? ')'
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_annotation3194); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:737:19: ( elementValuePairs | elementValue )?
                    int alt80=3;
                    int LA80_0 = input.LA(1);

                    if ( (LA80_0==IDENTIFIER) ) {
                        int LA80_1 = input.LA(2);

                        if ( (LA80_1==EQ) ) {
                            alt80=1;
                        }
                        else if ( (LA80_1==INSTANCEOF||(LA80_1>=LPAREN && LA80_1<=RPAREN)||LA80_1==LBRACKET||LA80_1==DOT||LA80_1==QUES||(LA80_1>=EQEQ && LA80_1<=PERCENT)||(LA80_1>=BANGEQ && LA80_1<=LT)) ) {
                            alt80=2;
                        }
                    }
                    else if ( ((LA80_0>=INTLITERAL && LA80_0<=NULL)||LA80_0==BOOLEAN||LA80_0==BYTE||LA80_0==CHAR||LA80_0==DOUBLE||LA80_0==FLOAT||LA80_0==INT||LA80_0==LONG||LA80_0==NEW||LA80_0==SHORT||LA80_0==SUPER||LA80_0==THIS||LA80_0==VOID||LA80_0==LPAREN||LA80_0==LBRACE||(LA80_0>=BANG && LA80_0<=TILDE)||(LA80_0>=PLUSPLUS && LA80_0<=SUB)||LA80_0==MONKEYS_AT) ) {
                        alt80=2;
                    }
                    switch (alt80) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:737:23: elementValuePairs
                            {
                            pushFollow(FOLLOW_elementValuePairs_in_annotation3221);
                            elementValuePairs();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;
                        case 2 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:738:23: elementValue
                            {
                            pushFollow(FOLLOW_elementValue_in_annotation3245);
                            elementValue();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,RPAREN,FOLLOW_RPAREN_in_annotation3281); if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 47, annotation_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotation"


    // $ANTLR start "elementValuePairs"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:744:1: elementValuePairs : elementValuePair ( ',' elementValuePair )* ;
    public final void elementValuePairs() throws RecognitionException {
        int elementValuePairs_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 48) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:745:5: ( elementValuePair ( ',' elementValuePair )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:745:9: elementValuePair ( ',' elementValuePair )*
            {
            pushFollow(FOLLOW_elementValuePair_in_elementValuePairs3313);
            elementValuePair();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:746:9: ( ',' elementValuePair )*
            loop82:
            do {
                int alt82=2;
                int LA82_0 = input.LA(1);

                if ( (LA82_0==COMMA) ) {
                    alt82=1;
                }


                switch (alt82) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:746:10: ',' elementValuePair
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_elementValuePairs3324); if (state.failed) return ;
            	    pushFollow(FOLLOW_elementValuePair_in_elementValuePairs3326);
            	    elementValuePair();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop82;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 48, elementValuePairs_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValuePairs"


    // $ANTLR start "elementValuePair"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:750:1: elementValuePair : IDENTIFIER '=' elementValue ;
    public final void elementValuePair() throws RecognitionException {
        int elementValuePair_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 49) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:751:5: ( IDENTIFIER '=' elementValue )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:751:9: IDENTIFIER '=' elementValue
            {
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_elementValuePair3357); if (state.failed) return ;
            match(input,EQ,FOLLOW_EQ_in_elementValuePair3359); if (state.failed) return ;
            pushFollow(FOLLOW_elementValue_in_elementValuePair3361);
            elementValue();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 49, elementValuePair_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValuePair"


    // $ANTLR start "elementValue"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:754:1: elementValue : ( conditionalExpression | annotation | elementValueArrayInitializer );
    public final void elementValue() throws RecognitionException {
        int elementValue_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 50) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:755:5: ( conditionalExpression | annotation | elementValueArrayInitializer )
            int alt83=3;
            switch ( input.LA(1) ) {
            case IDENTIFIER:
            case INTLITERAL:
            case LONGLITERAL:
            case FLOATLITERAL:
            case DOUBLELITERAL:
            case CHARLITERAL:
            case STRINGLITERAL:
            case TRUE:
            case FALSE:
            case NULL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case NEW:
            case SHORT:
            case SUPER:
            case THIS:
            case VOID:
            case LPAREN:
            case BANG:
            case TILDE:
            case PLUSPLUS:
            case SUBSUB:
            case PLUS:
            case SUB:
                {
                alt83=1;
                }
                break;
            case MONKEYS_AT:
                {
                alt83=2;
                }
                break;
            case LBRACE:
                {
                alt83=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 83, 0, input);

                throw nvae;
            }

            switch (alt83) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:755:9: conditionalExpression
                    {
                    pushFollow(FOLLOW_conditionalExpression_in_elementValue3381);
                    conditionalExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:756:9: annotation
                    {
                    pushFollow(FOLLOW_annotation_in_elementValue3391);
                    annotation();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:757:9: elementValueArrayInitializer
                    {
                    pushFollow(FOLLOW_elementValueArrayInitializer_in_elementValue3401);
                    elementValueArrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 50, elementValue_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValue"


    // $ANTLR start "elementValueArrayInitializer"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:760:1: elementValueArrayInitializer : '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' ;
    public final void elementValueArrayInitializer() throws RecognitionException {
        int elementValueArrayInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 51) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:761:5: ( '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:761:9: '{' ( elementValue ( ',' elementValue )* )? ( ',' )? '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_elementValueArrayInitializer3421); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:762:9: ( elementValue ( ',' elementValue )* )?
            int alt85=2;
            int LA85_0 = input.LA(1);

            if ( ((LA85_0>=IDENTIFIER && LA85_0<=NULL)||LA85_0==BOOLEAN||LA85_0==BYTE||LA85_0==CHAR||LA85_0==DOUBLE||LA85_0==FLOAT||LA85_0==INT||LA85_0==LONG||LA85_0==NEW||LA85_0==SHORT||LA85_0==SUPER||LA85_0==THIS||LA85_0==VOID||LA85_0==LPAREN||LA85_0==LBRACE||(LA85_0>=BANG && LA85_0<=TILDE)||(LA85_0>=PLUSPLUS && LA85_0<=SUB)||LA85_0==MONKEYS_AT) ) {
                alt85=1;
            }
            switch (alt85) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:762:10: elementValue ( ',' elementValue )*
                    {
                    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer3432);
                    elementValue();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:763:13: ( ',' elementValue )*
                    loop84:
                    do {
                        int alt84=2;
                        int LA84_0 = input.LA(1);

                        if ( (LA84_0==COMMA) ) {
                            int LA84_1 = input.LA(2);

                            if ( ((LA84_1>=IDENTIFIER && LA84_1<=NULL)||LA84_1==BOOLEAN||LA84_1==BYTE||LA84_1==CHAR||LA84_1==DOUBLE||LA84_1==FLOAT||LA84_1==INT||LA84_1==LONG||LA84_1==NEW||LA84_1==SHORT||LA84_1==SUPER||LA84_1==THIS||LA84_1==VOID||LA84_1==LPAREN||LA84_1==LBRACE||(LA84_1>=BANG && LA84_1<=TILDE)||(LA84_1>=PLUSPLUS && LA84_1<=SUB)||LA84_1==MONKEYS_AT) ) {
                                alt84=1;
                            }


                        }


                        switch (alt84) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:763:14: ',' elementValue
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer3447); if (state.failed) return ;
                    	    pushFollow(FOLLOW_elementValue_in_elementValueArrayInitializer3449);
                    	    elementValue();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop84;
                        }
                    } while (true);


                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:765:12: ( ',' )?
            int alt86=2;
            int LA86_0 = input.LA(1);

            if ( (LA86_0==COMMA) ) {
                alt86=1;
            }
            switch (alt86) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:765:13: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_elementValueArrayInitializer3478); if (state.failed) return ;

                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_elementValueArrayInitializer3482); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 51, elementValueArrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "elementValueArrayInitializer"


    // $ANTLR start "annotationTypeDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:769:1: annotationTypeDeclaration : annotationDeclarationModifiers= modifiers '@' 'interface' interfaceName= IDENTIFIER annotationTypeBody ;
    public final void annotationTypeDeclaration() throws RecognitionException {
        int annotationTypeDeclaration_StartIndex = input.index();
        Token interfaceName=null;
        int annotationDeclarationModifiers = 0;



          List<String> implementsList = new ArrayList<String>();
          List<String> extendsList = new ArrayList<String>();
          DeclarationOfClassOrInterface declarationOfClassOrInterface = new DeclarationOfClassOrInterface();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 52) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:777:6: (annotationDeclarationModifiers= modifiers '@' 'interface' interfaceName= IDENTIFIER annotationTypeBody )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:777:10: annotationDeclarationModifiers= modifiers '@' 'interface' interfaceName= IDENTIFIER annotationTypeBody
            {
            pushFollow(FOLLOW_modifiers_in_annotationTypeDeclaration3510);
            annotationDeclarationModifiers=modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration3521); if (state.failed) return ;
            match(input,INTERFACE,FOLLOW_INTERFACE_in_annotationTypeDeclaration3522); if (state.failed) return ;
            interfaceName=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationTypeDeclaration3534); if (state.failed) return ;
            if ( state.backtracking==0 ) {
               addCurrentClassOrInterfaceNamePile((interfaceName!=null?interfaceName.getText():null)); 
                        declarationOfClassOrInterface.setCurrentPackage(getPackageName());
                        declarationOfClassOrInterface.setCurrentClass(getRootClassOrInterfaceNamePile());
                        declarationOfClassOrInterface.setJavadoc(getJavadocComment());
                        declarationOfClassOrInterface.setModifiers(annotationDeclarationModifiers);
                        declarationOfClassOrInterface.setInterface(true);
                        declarationOfClassOrInterface.setClassOrInterfaceName((interfaceName!=null?interfaceName.getText():null));
                        declarationOfClassOrInterface.setExtendsList(extendsList);
                        declarationOfClassOrInterface.setImplementsList(implementsList);
                        getBuilder().createClass(declarationOfClassOrInterface);  
                      
            }
            pushFollow(FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3554);
            annotationTypeBody();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 52, annotationTypeDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeDeclaration"


    // $ANTLR start "annotationTypeBody"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:795:1: annotationTypeBody : '{' ( annotationTypeElementDeclaration )* '}' ;
    public final void annotationTypeBody() throws RecognitionException {
        int annotationTypeBody_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 53) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:796:5: ( '{' ( annotationTypeElementDeclaration )* '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:796:9: '{' ( annotationTypeElementDeclaration )* '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_annotationTypeBody3575); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:797:9: ( annotationTypeElementDeclaration )*
            loop87:
            do {
                int alt87=2;
                int LA87_0 = input.LA(1);

                if ( (LA87_0==IDENTIFIER||LA87_0==ABSTRACT||LA87_0==BOOLEAN||LA87_0==BYTE||(LA87_0>=CHAR && LA87_0<=CLASS)||LA87_0==DOUBLE||LA87_0==ENUM||LA87_0==FINAL||LA87_0==FLOAT||(LA87_0>=INT && LA87_0<=NATIVE)||(LA87_0>=PRIVATE && LA87_0<=PUBLIC)||(LA87_0>=SHORT && LA87_0<=STRICTFP)||LA87_0==SYNCHRONIZED||LA87_0==TRANSIENT||(LA87_0>=VOID && LA87_0<=VOLATILE)||LA87_0==SEMI||LA87_0==MONKEYS_AT||LA87_0==LT) ) {
                    alt87=1;
                }


                switch (alt87) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:797:10: annotationTypeElementDeclaration
            	    {
            	    pushFollow(FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3587);
            	    annotationTypeElementDeclaration();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop87;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_annotationTypeBody3609); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 53, annotationTypeBody_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeBody"


    // $ANTLR start "annotationTypeElementDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:802:1: annotationTypeElementDeclaration : ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' );
    public final void annotationTypeElementDeclaration() throws RecognitionException {
        int annotationTypeElementDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 54) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:806:5: ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' )
            int alt88=7;
            alt88 = dfa88.predict(input);
            switch (alt88) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:806:9: annotationMethodDeclaration
                    {
                    pushFollow(FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration3631);
                    annotationMethodDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:807:9: interfaceFieldDeclaration
                    {
                    pushFollow(FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration3641);
                    interfaceFieldDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:808:9: normalClassDeclaration
                    {
                    pushFollow(FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration3651);
                    normalClassDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:809:9: normalInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration3661);
                    normalInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:810:9: enumDeclaration
                    {
                    pushFollow(FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration3671);
                    enumDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:811:9: annotationTypeDeclaration
                    {
                    pushFollow(FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration3681);
                    annotationTypeDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:812:9: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_annotationTypeElementDeclaration3691); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 54, annotationTypeElementDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationTypeElementDeclaration"


    // $ANTLR start "annotationMethodDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:815:1: annotationMethodDeclaration : modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' ;
    public final void annotationMethodDeclaration() throws RecognitionException {
        int annotationMethodDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 55) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:816:5: ( modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:816:9: modifiers type IDENTIFIER '(' ')' ( 'default' elementValue )? ';'
            {
            pushFollow(FOLLOW_modifiers_in_annotationMethodDeclaration3711);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_annotationMethodDeclaration3722);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationMethodDeclaration3733); if (state.failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_annotationMethodDeclaration3743); if (state.failed) return ;
            match(input,RPAREN,FOLLOW_RPAREN_in_annotationMethodDeclaration3745); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:820:9: ( 'default' elementValue )?
            int alt89=2;
            int LA89_0 = input.LA(1);

            if ( (LA89_0==DEFAULT) ) {
                alt89=1;
            }
            switch (alt89) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:820:11: 'default' elementValue
                    {
                    match(input,DEFAULT,FOLLOW_DEFAULT_in_annotationMethodDeclaration3758); if (state.failed) return ;
                    pushFollow(FOLLOW_elementValue_in_annotationMethodDeclaration3771);
                    elementValue();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,SEMI,FOLLOW_SEMI_in_annotationMethodDeclaration3792); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 55, annotationMethodDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationMethodDeclaration"


    // $ANTLR start "block"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:826:1: block returns [String bodyTextString = null] : '{' (bodyTemp= blockStatement )* '}' ;
    public final String block() throws RecognitionException {
        String bodyTextString =  null;
        int block_StartIndex = input.index();
        Java1_6Parser.blockStatement_return bodyTemp = null;



            StringBuffer stringBuffer = new StringBuffer();

        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 56) ) { return bodyTextString; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:829:4: ( '{' (bodyTemp= blockStatement )* '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:829:7: '{' (bodyTemp= blockStatement )* '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_block3819); if (state.failed) return bodyTextString;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:830:7: (bodyTemp= blockStatement )*
            loop90:
            do {
                int alt90=2;
                int LA90_0 = input.LA(1);

                if ( ((LA90_0>=IDENTIFIER && LA90_0<=NULL)||(LA90_0>=ABSTRACT && LA90_0<=BYTE)||(LA90_0>=CHAR && LA90_0<=CLASS)||LA90_0==CONTINUE||(LA90_0>=DO && LA90_0<=DOUBLE)||LA90_0==ENUM||LA90_0==FINAL||(LA90_0>=FLOAT && LA90_0<=FOR)||LA90_0==IF||(LA90_0>=INT && LA90_0<=NEW)||(LA90_0>=PRIVATE && LA90_0<=THROW)||(LA90_0>=TRANSIENT && LA90_0<=LPAREN)||LA90_0==LBRACE||LA90_0==SEMI||(LA90_0>=BANG && LA90_0<=TILDE)||(LA90_0>=PLUSPLUS && LA90_0<=SUB)||LA90_0==MONKEYS_AT||LA90_0==LT) ) {
                    alt90=1;
                }


                switch (alt90) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:830:9: bodyTemp= blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_block3833);
            	    bodyTemp=blockStatement();

            	    state._fsp--;
            	    if (state.failed) return bodyTextString;
            	    if ( state.backtracking==0 ) {
            	       stringBuffer.append((bodyTemp!=null?input.toString(bodyTemp.start,bodyTemp.stop):null) + "\n"); 
            	    }

            	    }
            	    break;

            	default :
            	    break loop90;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_block3846); if (state.failed) return bodyTextString;
            if ( state.backtracking==0 ) {
               bodyTextString = stringBuffer.toString(); 
            }

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 56, block_StartIndex); }
        }
        return bodyTextString;
    }
    // $ANTLR end "block"

    public static class blockStatement_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "blockStatement"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:859:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );
    public final Java1_6Parser.blockStatement_return blockStatement() throws RecognitionException {
        Java1_6Parser.blockStatement_return retval = new Java1_6Parser.blockStatement_return();
        retval.start = input.LT(1);
        int blockStatement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 57) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:860:5: ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement )
            int alt91=3;
            alt91 = dfa91.predict(input);
            switch (alt91) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:860:9: localVariableDeclarationStatement
                    {
                    pushFollow(FOLLOW_localVariableDeclarationStatement_in_blockStatement3872);
                    localVariableDeclarationStatement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:861:9: classOrInterfaceDeclaration
                    {
                    pushFollow(FOLLOW_classOrInterfaceDeclaration_in_blockStatement3882);
                    classOrInterfaceDeclaration();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:862:9: statement
                    {
                    pushFollow(FOLLOW_statement_in_blockStatement3892);
                    statement();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 57, blockStatement_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "blockStatement"


    // $ANTLR start "localVariableDeclarationStatement"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:866:1: localVariableDeclarationStatement : localVariableDeclaration ';' ;
    public final void localVariableDeclarationStatement() throws RecognitionException {
        int localVariableDeclarationStatement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 58) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:867:5: ( localVariableDeclaration ';' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:867:9: localVariableDeclaration ';'
            {
            pushFollow(FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3913);
            localVariableDeclaration();

            state._fsp--;
            if (state.failed) return ;
            match(input,SEMI,FOLLOW_SEMI_in_localVariableDeclarationStatement3923); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 58, localVariableDeclarationStatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "localVariableDeclarationStatement"


    // $ANTLR start "localVariableDeclaration"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:871:1: localVariableDeclaration : variableModifiers type variableDeclarator[(int)-1,null] ( ',' variableDeclarator[(int)-1,null] )* ;
    public final void localVariableDeclaration() throws RecognitionException {
        int localVariableDeclaration_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 59) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:872:5: ( variableModifiers type variableDeclarator[(int)-1,null] ( ',' variableDeclarator[(int)-1,null] )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:872:9: variableModifiers type variableDeclarator[(int)-1,null] ( ',' variableDeclarator[(int)-1,null] )*
            {
            pushFollow(FOLLOW_variableModifiers_in_localVariableDeclaration3943);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_localVariableDeclaration3945);
            type();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration3955);
            variableDeclarator((int)-1, null);

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:874:9: ( ',' variableDeclarator[(int)-1,null] )*
            loop92:
            do {
                int alt92=2;
                int LA92_0 = input.LA(1);

                if ( (LA92_0==COMMA) ) {
                    alt92=1;
                }


                switch (alt92) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:874:10: ',' variableDeclarator[(int)-1,null]
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_localVariableDeclaration3967); if (state.failed) return ;
            	    pushFollow(FOLLOW_variableDeclarator_in_localVariableDeclaration3969);
            	    variableDeclarator((int)-1, null);

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop92;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 59, localVariableDeclaration_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "localVariableDeclaration"


    // $ANTLR start "statement"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:878:1: statement : ( block | ( 'assert' ) expression ( ':' expression )? ';' | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( 'else' statement )? | forstatement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( IDENTIFIER )? ';' | 'continue' ( IDENTIFIER )? ';' | expression ';' | IDENTIFIER ':' statement | ';' );
    public final void statement() throws RecognitionException {
        int statement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 60) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:879:5: ( block | ( 'assert' ) expression ( ':' expression )? ';' | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( 'else' statement )? | forstatement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( IDENTIFIER )? ';' | 'continue' ( IDENTIFIER )? ';' | expression ';' | IDENTIFIER ':' statement | ';' )
            int alt99=17;
            alt99 = dfa99.predict(input);
            switch (alt99) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:879:9: block
                    {
                    pushFollow(FOLLOW_block_in_statement4001);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:9: ( 'assert' ) expression ( ':' expression )? ';'
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:9: ( 'assert' )
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:10: 'assert'
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement4025); if (state.failed) return ;

                    }

                    pushFollow(FOLLOW_expression_in_statement4045);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:883:20: ( ':' expression )?
                    int alt93=2;
                    int LA93_0 = input.LA(1);

                    if ( (LA93_0==COLON) ) {
                        alt93=1;
                    }
                    switch (alt93) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:883:21: ':' expression
                            {
                            match(input,COLON,FOLLOW_COLON_in_statement4048); if (state.failed) return ;
                            pushFollow(FOLLOW_expression_in_statement4050);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_statement4054); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:9: 'assert' expression ( ':' expression )? ';'
                    {
                    match(input,ASSERT,FOLLOW_ASSERT_in_statement4064); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_statement4067);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:30: ( ':' expression )?
                    int alt94=2;
                    int LA94_0 = input.LA(1);

                    if ( (LA94_0==COLON) ) {
                        alt94=1;
                    }
                    switch (alt94) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:31: ':' expression
                            {
                            match(input,COLON,FOLLOW_COLON_in_statement4070); if (state.failed) return ;
                            pushFollow(FOLLOW_expression_in_statement4072);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_statement4076); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:885:9: 'if' parExpression statement ( 'else' statement )?
                    {
                    match(input,IF,FOLLOW_IF_in_statement4098); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement4100);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement4102);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:885:38: ( 'else' statement )?
                    int alt95=2;
                    int LA95_0 = input.LA(1);

                    if ( (LA95_0==ELSE) ) {
                        int LA95_1 = input.LA(2);

                        if ( (synpred133_Java1_6()) ) {
                            alt95=1;
                        }
                    }
                    switch (alt95) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:885:39: 'else' statement
                            {
                            match(input,ELSE,FOLLOW_ELSE_in_statement4105); if (state.failed) return ;
                            pushFollow(FOLLOW_statement_in_statement4107);
                            statement();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:886:9: forstatement
                    {
                    pushFollow(FOLLOW_forstatement_in_statement4129);
                    forstatement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:887:9: 'while' parExpression statement
                    {
                    match(input,WHILE,FOLLOW_WHILE_in_statement4139); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement4141);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement4143);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:888:9: 'do' statement 'while' parExpression ';'
                    {
                    match(input,DO,FOLLOW_DO_in_statement4153); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement4155);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,WHILE,FOLLOW_WHILE_in_statement4157); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement4159);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,SEMI,FOLLOW_SEMI_in_statement4161); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:889:9: trystatement
                    {
                    pushFollow(FOLLOW_trystatement_in_statement4171);
                    trystatement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:890:9: 'switch' parExpression '{' switchBlockStatementGroups '}'
                    {
                    match(input,SWITCH,FOLLOW_SWITCH_in_statement4181); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement4183);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,LBRACE,FOLLOW_LBRACE_in_statement4185); if (state.failed) return ;
                    pushFollow(FOLLOW_switchBlockStatementGroups_in_statement4187);
                    switchBlockStatementGroups();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RBRACE,FOLLOW_RBRACE_in_statement4189); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:891:9: 'synchronized' parExpression block
                    {
                    match(input,SYNCHRONIZED,FOLLOW_SYNCHRONIZED_in_statement4199); if (state.failed) return ;
                    pushFollow(FOLLOW_parExpression_in_statement4201);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_block_in_statement4203);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:892:9: 'return' ( expression )? ';'
                    {
                    match(input,RETURN,FOLLOW_RETURN_in_statement4213); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:892:18: ( expression )?
                    int alt96=2;
                    int LA96_0 = input.LA(1);

                    if ( ((LA96_0>=IDENTIFIER && LA96_0<=NULL)||LA96_0==BOOLEAN||LA96_0==BYTE||LA96_0==CHAR||LA96_0==DOUBLE||LA96_0==FLOAT||LA96_0==INT||LA96_0==LONG||LA96_0==NEW||LA96_0==SHORT||LA96_0==SUPER||LA96_0==THIS||LA96_0==VOID||LA96_0==LPAREN||(LA96_0>=BANG && LA96_0<=TILDE)||(LA96_0>=PLUSPLUS && LA96_0<=SUB)) ) {
                        alt96=1;
                    }
                    switch (alt96) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:892:19: expression
                            {
                            pushFollow(FOLLOW_expression_in_statement4216);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_statement4221); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:893:9: 'throw' expression ';'
                    {
                    match(input,THROW,FOLLOW_THROW_in_statement4231); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_statement4233);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,SEMI,FOLLOW_SEMI_in_statement4235); if (state.failed) return ;

                    }
                    break;
                case 13 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:894:9: 'break' ( IDENTIFIER )? ';'
                    {
                    match(input,BREAK,FOLLOW_BREAK_in_statement4245); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:895:13: ( IDENTIFIER )?
                    int alt97=2;
                    int LA97_0 = input.LA(1);

                    if ( (LA97_0==IDENTIFIER) ) {
                        alt97=1;
                    }
                    switch (alt97) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:895:14: IDENTIFIER
                            {
                            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement4260); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_statement4277); if (state.failed) return ;

                    }
                    break;
                case 14 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:897:9: 'continue' ( IDENTIFIER )? ';'
                    {
                    match(input,CONTINUE,FOLLOW_CONTINUE_in_statement4287); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:898:13: ( IDENTIFIER )?
                    int alt98=2;
                    int LA98_0 = input.LA(1);

                    if ( (LA98_0==IDENTIFIER) ) {
                        alt98=1;
                    }
                    switch (alt98) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:898:14: IDENTIFIER
                            {
                            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement4302); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_statement4319); if (state.failed) return ;

                    }
                    break;
                case 15 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:900:9: expression ';'
                    {
                    pushFollow(FOLLOW_expression_in_statement4329);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,SEMI,FOLLOW_SEMI_in_statement4332); if (state.failed) return ;

                    }
                    break;
                case 16 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:901:9: IDENTIFIER ':' statement
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_statement4347); if (state.failed) return ;
                    match(input,COLON,FOLLOW_COLON_in_statement4349); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_statement4351);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 17 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:902:9: ';'
                    {
                    match(input,SEMI,FOLLOW_SEMI_in_statement4361); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 60, statement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "statement"


    // $ANTLR start "switchBlockStatementGroups"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:906:1: switchBlockStatementGroups : ( switchBlockStatementGroup )* ;
    public final void switchBlockStatementGroups() throws RecognitionException {
        int switchBlockStatementGroups_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 61) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:907:5: ( ( switchBlockStatementGroup )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:907:9: ( switchBlockStatementGroup )*
            {
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:907:9: ( switchBlockStatementGroup )*
            loop100:
            do {
                int alt100=2;
                int LA100_0 = input.LA(1);

                if ( (LA100_0==CASE||LA100_0==DEFAULT) ) {
                    alt100=1;
                }


                switch (alt100) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:907:10: switchBlockStatementGroup
            	    {
            	    pushFollow(FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups4383);
            	    switchBlockStatementGroup();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop100;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 61, switchBlockStatementGroups_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroups"


    // $ANTLR start "switchBlockStatementGroup"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:910:1: switchBlockStatementGroup : switchLabel ( blockStatement )* ;
    public final void switchBlockStatementGroup() throws RecognitionException {
        int switchBlockStatementGroup_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 62) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:911:5: ( switchLabel ( blockStatement )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:912:9: switchLabel ( blockStatement )*
            {
            pushFollow(FOLLOW_switchLabel_in_switchBlockStatementGroup4412);
            switchLabel();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:913:9: ( blockStatement )*
            loop101:
            do {
                int alt101=2;
                int LA101_0 = input.LA(1);

                if ( ((LA101_0>=IDENTIFIER && LA101_0<=NULL)||(LA101_0>=ABSTRACT && LA101_0<=BYTE)||(LA101_0>=CHAR && LA101_0<=CLASS)||LA101_0==CONTINUE||(LA101_0>=DO && LA101_0<=DOUBLE)||LA101_0==ENUM||LA101_0==FINAL||(LA101_0>=FLOAT && LA101_0<=FOR)||LA101_0==IF||(LA101_0>=INT && LA101_0<=NEW)||(LA101_0>=PRIVATE && LA101_0<=THROW)||(LA101_0>=TRANSIENT && LA101_0<=LPAREN)||LA101_0==LBRACE||LA101_0==SEMI||(LA101_0>=BANG && LA101_0<=TILDE)||(LA101_0>=PLUSPLUS && LA101_0<=SUB)||LA101_0==MONKEYS_AT||LA101_0==LT) ) {
                    alt101=1;
                }


                switch (alt101) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:913:10: blockStatement
            	    {
            	    pushFollow(FOLLOW_blockStatement_in_switchBlockStatementGroup4423);
            	    blockStatement();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop101;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 62, switchBlockStatementGroup_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchBlockStatementGroup"


    // $ANTLR start "switchLabel"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:917:1: switchLabel : ( 'case' expression ':' | 'default' ':' );
    public final void switchLabel() throws RecognitionException {
        int switchLabel_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 63) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:918:5: ( 'case' expression ':' | 'default' ':' )
            int alt102=2;
            int LA102_0 = input.LA(1);

            if ( (LA102_0==CASE) ) {
                alt102=1;
            }
            else if ( (LA102_0==DEFAULT) ) {
                alt102=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 102, 0, input);

                throw nvae;
            }
            switch (alt102) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:918:9: 'case' expression ':'
                    {
                    match(input,CASE,FOLLOW_CASE_in_switchLabel4454); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_switchLabel4456);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,COLON,FOLLOW_COLON_in_switchLabel4458); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:919:9: 'default' ':'
                    {
                    match(input,DEFAULT,FOLLOW_DEFAULT_in_switchLabel4468); if (state.failed) return ;
                    match(input,COLON,FOLLOW_COLON_in_switchLabel4470); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 63, switchLabel_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "switchLabel"


    // $ANTLR start "trystatement"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:923:1: trystatement : 'try' block ( catches 'finally' block | catches | 'finally' block ) ;
    public final void trystatement() throws RecognitionException {
        int trystatement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 64) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:924:5: ( 'try' block ( catches 'finally' block | catches | 'finally' block ) )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:924:9: 'try' block ( catches 'finally' block | catches | 'finally' block )
            {
            match(input,TRY,FOLLOW_TRY_in_trystatement4491); if (state.failed) return ;
            pushFollow(FOLLOW_block_in_trystatement4493);
            block();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:925:9: ( catches 'finally' block | catches | 'finally' block )
            int alt103=3;
            int LA103_0 = input.LA(1);

            if ( (LA103_0==CATCH) ) {
                int LA103_1 = input.LA(2);

                if ( (synpred153_Java1_6()) ) {
                    alt103=1;
                }
                else if ( (synpred154_Java1_6()) ) {
                    alt103=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 103, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA103_0==FINALLY) ) {
                alt103=3;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 103, 0, input);

                throw nvae;
            }
            switch (alt103) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:925:13: catches 'finally' block
                    {
                    pushFollow(FOLLOW_catches_in_trystatement4507);
                    catches();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,FINALLY,FOLLOW_FINALLY_in_trystatement4509); if (state.failed) return ;
                    pushFollow(FOLLOW_block_in_trystatement4511);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:926:13: catches
                    {
                    pushFollow(FOLLOW_catches_in_trystatement4525);
                    catches();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:927:13: 'finally' block
                    {
                    match(input,FINALLY,FOLLOW_FINALLY_in_trystatement4539); if (state.failed) return ;
                    pushFollow(FOLLOW_block_in_trystatement4541);
                    block();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 64, trystatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "trystatement"


    // $ANTLR start "catches"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:931:1: catches : catchClause ( catchClause )* ;
    public final void catches() throws RecognitionException {
        int catches_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 65) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:932:5: ( catchClause ( catchClause )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:932:9: catchClause ( catchClause )*
            {
            pushFollow(FOLLOW_catchClause_in_catches4572);
            catchClause();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:933:9: ( catchClause )*
            loop104:
            do {
                int alt104=2;
                int LA104_0 = input.LA(1);

                if ( (LA104_0==CATCH) ) {
                    alt104=1;
                }


                switch (alt104) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:933:10: catchClause
            	    {
            	    pushFollow(FOLLOW_catchClause_in_catches4583);
            	    catchClause();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop104;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 65, catches_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "catches"


    // $ANTLR start "catchClause"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:937:1: catchClause : 'catch' '(' formalParameter ')' block ;
    public final void catchClause() throws RecognitionException {
        int catchClause_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 66) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:938:5: ( 'catch' '(' formalParameter ')' block )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:938:9: 'catch' '(' formalParameter ')' block
            {
            match(input,CATCH,FOLLOW_CATCH_in_catchClause4614); if (state.failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_catchClause4616); if (state.failed) return ;
            pushFollow(FOLLOW_formalParameter_in_catchClause4618);
            formalParameter();

            state._fsp--;
            if (state.failed) return ;
            match(input,RPAREN,FOLLOW_RPAREN_in_catchClause4628); if (state.failed) return ;
            pushFollow(FOLLOW_block_in_catchClause4630);
            block();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 66, catchClause_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "catchClause"


    // $ANTLR start "formalParameter"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:942:1: formalParameter : variableModifiers type IDENTIFIER ( '[' ']' )* ;
    public final void formalParameter() throws RecognitionException {
        int formalParameter_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 67) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:943:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:943:9: variableModifiers type IDENTIFIER ( '[' ']' )*
            {
            pushFollow(FOLLOW_variableModifiers_in_formalParameter4651);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_formalParameter4653);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_formalParameter4655); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:944:9: ( '[' ']' )*
            loop105:
            do {
                int alt105=2;
                int LA105_0 = input.LA(1);

                if ( (LA105_0==LBRACKET) ) {
                    alt105=1;
                }


                switch (alt105) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:944:10: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_formalParameter4666); if (state.failed) return ;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_formalParameter4668); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop105;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 67, formalParameter_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "formalParameter"


    // $ANTLR start "forstatement"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:948:1: forstatement : ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement );
    public final void forstatement() throws RecognitionException {
        int forstatement_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 68) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:949:5: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement | 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement )
            int alt109=2;
            int LA109_0 = input.LA(1);

            if ( (LA109_0==FOR) ) {
                int LA109_1 = input.LA(2);

                if ( (synpred157_Java1_6()) ) {
                    alt109=1;
                }
                else if ( (true) ) {
                    alt109=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 109, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 109, 0, input);

                throw nvae;
            }
            switch (alt109) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:951:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
                    {
                    match(input,FOR,FOLLOW_FOR_in_forstatement4717); if (state.failed) return ;
                    match(input,LPAREN,FOLLOW_LPAREN_in_forstatement4719); if (state.failed) return ;
                    pushFollow(FOLLOW_variableModifiers_in_forstatement4721);
                    variableModifiers();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_forstatement4723);
                    type();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_forstatement4725); if (state.failed) return ;
                    match(input,COLON,FOLLOW_COLON_in_forstatement4727); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_forstatement4738);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_forstatement4740); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_forstatement4742);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:955:9: 'for' '(' ( forInit )? ';' ( expression )? ';' ( expressionList )? ')' statement
                    {
                    match(input,FOR,FOLLOW_FOR_in_forstatement4774); if (state.failed) return ;
                    match(input,LPAREN,FOLLOW_LPAREN_in_forstatement4776); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:956:17: ( forInit )?
                    int alt106=2;
                    int LA106_0 = input.LA(1);

                    if ( ((LA106_0>=IDENTIFIER && LA106_0<=NULL)||LA106_0==BOOLEAN||LA106_0==BYTE||LA106_0==CHAR||LA106_0==DOUBLE||LA106_0==FINAL||LA106_0==FLOAT||LA106_0==INT||LA106_0==LONG||LA106_0==NEW||LA106_0==SHORT||LA106_0==SUPER||LA106_0==THIS||LA106_0==VOID||LA106_0==LPAREN||(LA106_0>=BANG && LA106_0<=TILDE)||(LA106_0>=PLUSPLUS && LA106_0<=SUB)||LA106_0==MONKEYS_AT) ) {
                        alt106=1;
                    }
                    switch (alt106) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:956:18: forInit
                            {
                            pushFollow(FOLLOW_forInit_in_forstatement4796);
                            forInit();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_forstatement4817); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:958:17: ( expression )?
                    int alt107=2;
                    int LA107_0 = input.LA(1);

                    if ( ((LA107_0>=IDENTIFIER && LA107_0<=NULL)||LA107_0==BOOLEAN||LA107_0==BYTE||LA107_0==CHAR||LA107_0==DOUBLE||LA107_0==FLOAT||LA107_0==INT||LA107_0==LONG||LA107_0==NEW||LA107_0==SHORT||LA107_0==SUPER||LA107_0==THIS||LA107_0==VOID||LA107_0==LPAREN||(LA107_0>=BANG && LA107_0<=TILDE)||(LA107_0>=PLUSPLUS && LA107_0<=SUB)) ) {
                        alt107=1;
                    }
                    switch (alt107) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:958:18: expression
                            {
                            pushFollow(FOLLOW_expression_in_forstatement4837);
                            expression();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,SEMI,FOLLOW_SEMI_in_forstatement4858); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:960:17: ( expressionList )?
                    int alt108=2;
                    int LA108_0 = input.LA(1);

                    if ( ((LA108_0>=IDENTIFIER && LA108_0<=NULL)||LA108_0==BOOLEAN||LA108_0==BYTE||LA108_0==CHAR||LA108_0==DOUBLE||LA108_0==FLOAT||LA108_0==INT||LA108_0==LONG||LA108_0==NEW||LA108_0==SHORT||LA108_0==SUPER||LA108_0==THIS||LA108_0==VOID||LA108_0==LPAREN||(LA108_0>=BANG && LA108_0<=TILDE)||(LA108_0>=PLUSPLUS && LA108_0<=SUB)) ) {
                        alt108=1;
                    }
                    switch (alt108) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:960:18: expressionList
                            {
                            pushFollow(FOLLOW_expressionList_in_forstatement4878);
                            expressionList();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,RPAREN,FOLLOW_RPAREN_in_forstatement4899); if (state.failed) return ;
                    pushFollow(FOLLOW_statement_in_forstatement4901);
                    statement();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 68, forstatement_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forstatement"


    // $ANTLR start "forInit"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:964:1: forInit : ( localVariableDeclaration | expressionList );
    public final void forInit() throws RecognitionException {
        int forInit_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 69) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:965:5: ( localVariableDeclaration | expressionList )
            int alt110=2;
            alt110 = dfa110.predict(input);
            switch (alt110) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:965:9: localVariableDeclaration
                    {
                    pushFollow(FOLLOW_localVariableDeclaration_in_forInit4921);
                    localVariableDeclaration();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:966:9: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_forInit4931);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 69, forInit_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "forInit"


    // $ANTLR start "parExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:969:1: parExpression : '(' expression ')' ;
    public final void parExpression() throws RecognitionException {
        int parExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 70) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:970:5: ( '(' expression ')' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:970:9: '(' expression ')'
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_parExpression4951); if (state.failed) return ;
            pushFollow(FOLLOW_expression_in_parExpression4953);
            expression();

            state._fsp--;
            if (state.failed) return ;
            match(input,RPAREN,FOLLOW_RPAREN_in_parExpression4955); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 70, parExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "parExpression"


    // $ANTLR start "expressionList"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:973:1: expressionList : expression ( ',' expression )* ;
    public final void expressionList() throws RecognitionException {
        int expressionList_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 71) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:974:5: ( expression ( ',' expression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:974:9: expression ( ',' expression )*
            {
            pushFollow(FOLLOW_expression_in_expressionList4975);
            expression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:975:9: ( ',' expression )*
            loop111:
            do {
                int alt111=2;
                int LA111_0 = input.LA(1);

                if ( (LA111_0==COMMA) ) {
                    alt111=1;
                }


                switch (alt111) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:975:10: ',' expression
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_expressionList4986); if (state.failed) return ;
            	    pushFollow(FOLLOW_expression_in_expressionList4988);
            	    expression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop111;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 71, expressionList_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "expressionList"


    // $ANTLR start "expression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:980:1: expression : conditionalExpression ( assignmentOperator expression )? ;
    public final void expression() throws RecognitionException {
        int expression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 72) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:981:5: ( conditionalExpression ( assignmentOperator expression )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:981:9: conditionalExpression ( assignmentOperator expression )?
            {
            pushFollow(FOLLOW_conditionalExpression_in_expression5020);
            conditionalExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:982:9: ( assignmentOperator expression )?
            int alt112=2;
            int LA112_0 = input.LA(1);

            if ( (LA112_0==EQ||(LA112_0>=PLUSEQ && LA112_0<=PERCENTEQ)||(LA112_0>=GT && LA112_0<=LT)) ) {
                alt112=1;
            }
            switch (alt112) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:982:10: assignmentOperator expression
                    {
                    pushFollow(FOLLOW_assignmentOperator_in_expression5031);
                    assignmentOperator();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_expression5033);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 72, expression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "expression"


    // $ANTLR start "assignmentOperator"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:987:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' );
    public final void assignmentOperator() throws RecognitionException {
        int assignmentOperator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 73) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:988:5: ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' )
            int alt113=12;
            alt113 = dfa113.predict(input);
            switch (alt113) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:988:9: '='
                    {
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator5065); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:989:9: '+='
                    {
                    match(input,PLUSEQ,FOLLOW_PLUSEQ_in_assignmentOperator5075); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:990:9: '-='
                    {
                    match(input,SUBEQ,FOLLOW_SUBEQ_in_assignmentOperator5085); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:991:9: '*='
                    {
                    match(input,STAREQ,FOLLOW_STAREQ_in_assignmentOperator5095); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:992:9: '/='
                    {
                    match(input,SLASHEQ,FOLLOW_SLASHEQ_in_assignmentOperator5105); if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:993:9: '&='
                    {
                    match(input,AMPEQ,FOLLOW_AMPEQ_in_assignmentOperator5115); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:994:9: '|='
                    {
                    match(input,BAREQ,FOLLOW_BAREQ_in_assignmentOperator5125); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:995:9: '^='
                    {
                    match(input,CARETEQ,FOLLOW_CARETEQ_in_assignmentOperator5135); if (state.failed) return ;

                    }
                    break;
                case 9 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:996:9: '%='
                    {
                    match(input,PERCENTEQ,FOLLOW_PERCENTEQ_in_assignmentOperator5145); if (state.failed) return ;

                    }
                    break;
                case 10 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:997:10: '<' '<' '='
                    {
                    match(input,LT,FOLLOW_LT_in_assignmentOperator5156); if (state.failed) return ;
                    match(input,LT,FOLLOW_LT_in_assignmentOperator5158); if (state.failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator5160); if (state.failed) return ;

                    }
                    break;
                case 11 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:998:10: '>' '>' '>' '='
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator5171); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_assignmentOperator5173); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_assignmentOperator5175); if (state.failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator5177); if (state.failed) return ;

                    }
                    break;
                case 12 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:999:10: '>' '>' '='
                    {
                    match(input,GT,FOLLOW_GT_in_assignmentOperator5188); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_assignmentOperator5190); if (state.failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_assignmentOperator5192); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 73, assignmentOperator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "assignmentOperator"


    // $ANTLR start "conditionalExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1003:1: conditionalExpression : conditionalOrExpression ( '?' expression ':' conditionalExpression )? ;
    public final void conditionalExpression() throws RecognitionException {
        int conditionalExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 74) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1004:5: ( conditionalOrExpression ( '?' expression ':' conditionalExpression )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1004:9: conditionalOrExpression ( '?' expression ':' conditionalExpression )?
            {
            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalExpression5213);
            conditionalOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1005:9: ( '?' expression ':' conditionalExpression )?
            int alt114=2;
            int LA114_0 = input.LA(1);

            if ( (LA114_0==QUES) ) {
                alt114=1;
            }
            switch (alt114) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1005:10: '?' expression ':' conditionalExpression
                    {
                    match(input,QUES,FOLLOW_QUES_in_conditionalExpression5224); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_conditionalExpression5226);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,COLON,FOLLOW_COLON_in_conditionalExpression5228); if (state.failed) return ;
                    pushFollow(FOLLOW_conditionalExpression_in_conditionalExpression5230);
                    conditionalExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 74, conditionalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalExpression"


    // $ANTLR start "conditionalOrExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1009:1: conditionalOrExpression : conditionalAndExpression ( '||' conditionalAndExpression )* ;
    public final void conditionalOrExpression() throws RecognitionException {
        int conditionalOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 75) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1010:5: ( conditionalAndExpression ( '||' conditionalAndExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1010:9: conditionalAndExpression ( '||' conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression5261);
            conditionalAndExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1011:9: ( '||' conditionalAndExpression )*
            loop115:
            do {
                int alt115=2;
                int LA115_0 = input.LA(1);

                if ( (LA115_0==BARBAR) ) {
                    alt115=1;
                }


                switch (alt115) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1011:10: '||' conditionalAndExpression
            	    {
            	    match(input,BARBAR,FOLLOW_BARBAR_in_conditionalOrExpression5272); if (state.failed) return ;
            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalOrExpression5274);
            	    conditionalAndExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop115;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 75, conditionalOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalOrExpression"


    // $ANTLR start "conditionalAndExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1015:1: conditionalAndExpression : inclusiveOrExpression ( '&&' inclusiveOrExpression )* ;
    public final void conditionalAndExpression() throws RecognitionException {
        int conditionalAndExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 76) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1016:5: ( inclusiveOrExpression ( '&&' inclusiveOrExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1016:9: inclusiveOrExpression ( '&&' inclusiveOrExpression )*
            {
            pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5305);
            inclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1017:9: ( '&&' inclusiveOrExpression )*
            loop116:
            do {
                int alt116=2;
                int LA116_0 = input.LA(1);

                if ( (LA116_0==AMPAMP) ) {
                    alt116=1;
                }


                switch (alt116) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1017:10: '&&' inclusiveOrExpression
            	    {
            	    match(input,AMPAMP,FOLLOW_AMPAMP_in_conditionalAndExpression5316); if (state.failed) return ;
            	    pushFollow(FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5318);
            	    inclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop116;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 76, conditionalAndExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "conditionalAndExpression"


    // $ANTLR start "inclusiveOrExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1021:1: inclusiveOrExpression : exclusiveOrExpression ( '|' exclusiveOrExpression )* ;
    public final void inclusiveOrExpression() throws RecognitionException {
        int inclusiveOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 77) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1022:5: ( exclusiveOrExpression ( '|' exclusiveOrExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1022:9: exclusiveOrExpression ( '|' exclusiveOrExpression )*
            {
            pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5349);
            exclusiveOrExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1023:9: ( '|' exclusiveOrExpression )*
            loop117:
            do {
                int alt117=2;
                int LA117_0 = input.LA(1);

                if ( (LA117_0==BAR) ) {
                    alt117=1;
                }


                switch (alt117) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1023:10: '|' exclusiveOrExpression
            	    {
            	    match(input,BAR,FOLLOW_BAR_in_inclusiveOrExpression5360); if (state.failed) return ;
            	    pushFollow(FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5362);
            	    exclusiveOrExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop117;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 77, inclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "inclusiveOrExpression"


    // $ANTLR start "exclusiveOrExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1027:1: exclusiveOrExpression : andExpression ( '^' andExpression )* ;
    public final void exclusiveOrExpression() throws RecognitionException {
        int exclusiveOrExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 78) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1028:5: ( andExpression ( '^' andExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1028:9: andExpression ( '^' andExpression )*
            {
            pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression5393);
            andExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1029:9: ( '^' andExpression )*
            loop118:
            do {
                int alt118=2;
                int LA118_0 = input.LA(1);

                if ( (LA118_0==CARET) ) {
                    alt118=1;
                }


                switch (alt118) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1029:10: '^' andExpression
            	    {
            	    match(input,CARET,FOLLOW_CARET_in_exclusiveOrExpression5404); if (state.failed) return ;
            	    pushFollow(FOLLOW_andExpression_in_exclusiveOrExpression5406);
            	    andExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop118;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 78, exclusiveOrExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "exclusiveOrExpression"


    // $ANTLR start "andExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1033:1: andExpression : equalityExpression ( '&' equalityExpression )* ;
    public final void andExpression() throws RecognitionException {
        int andExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 79) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1034:5: ( equalityExpression ( '&' equalityExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1034:9: equalityExpression ( '&' equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_andExpression5437);
            equalityExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1035:9: ( '&' equalityExpression )*
            loop119:
            do {
                int alt119=2;
                int LA119_0 = input.LA(1);

                if ( (LA119_0==AMP) ) {
                    alt119=1;
                }


                switch (alt119) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1035:10: '&' equalityExpression
            	    {
            	    match(input,AMP,FOLLOW_AMP_in_andExpression5448); if (state.failed) return ;
            	    pushFollow(FOLLOW_equalityExpression_in_andExpression5450);
            	    equalityExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop119;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 79, andExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "andExpression"


    // $ANTLR start "equalityExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1039:1: equalityExpression : instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* ;
    public final void equalityExpression() throws RecognitionException {
        int equalityExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 80) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1040:5: ( instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1040:9: instanceOfExpression ( ( '==' | '!=' ) instanceOfExpression )*
            {
            pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression5481);
            instanceOfExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1041:9: ( ( '==' | '!=' ) instanceOfExpression )*
            loop120:
            do {
                int alt120=2;
                int LA120_0 = input.LA(1);

                if ( (LA120_0==EQEQ||LA120_0==BANGEQ) ) {
                    alt120=1;
                }


                switch (alt120) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1042:13: ( '==' | '!=' ) instanceOfExpression
            	    {
            	    if ( input.LA(1)==EQEQ||input.LA(1)==BANGEQ ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_instanceOfExpression_in_equalityExpression5558);
            	    instanceOfExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop120;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 80, equalityExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "equalityExpression"


    // $ANTLR start "instanceOfExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1049:1: instanceOfExpression : relationalExpression ( 'instanceof' type )? ;
    public final void instanceOfExpression() throws RecognitionException {
        int instanceOfExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 81) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1050:5: ( relationalExpression ( 'instanceof' type )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1050:9: relationalExpression ( 'instanceof' type )?
            {
            pushFollow(FOLLOW_relationalExpression_in_instanceOfExpression5589);
            relationalExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1051:9: ( 'instanceof' type )?
            int alt121=2;
            int LA121_0 = input.LA(1);

            if ( (LA121_0==INSTANCEOF) ) {
                alt121=1;
            }
            switch (alt121) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1051:10: 'instanceof' type
                    {
                    match(input,INSTANCEOF,FOLLOW_INSTANCEOF_in_instanceOfExpression5600); if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_instanceOfExpression5602);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 81, instanceOfExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "instanceOfExpression"


    // $ANTLR start "relationalExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1055:1: relationalExpression : shiftExpression ( relationalOp shiftExpression )* ;
    public final void relationalExpression() throws RecognitionException {
        int relationalExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 82) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1056:5: ( shiftExpression ( relationalOp shiftExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1056:9: shiftExpression ( relationalOp shiftExpression )*
            {
            pushFollow(FOLLOW_shiftExpression_in_relationalExpression5633);
            shiftExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1057:9: ( relationalOp shiftExpression )*
            loop122:
            do {
                int alt122=2;
                int LA122_0 = input.LA(1);

                if ( (LA122_0==LT) ) {
                    int LA122_2 = input.LA(2);

                    if ( ((LA122_2>=IDENTIFIER && LA122_2<=NULL)||LA122_2==BOOLEAN||LA122_2==BYTE||LA122_2==CHAR||LA122_2==DOUBLE||LA122_2==FLOAT||LA122_2==INT||LA122_2==LONG||LA122_2==NEW||LA122_2==SHORT||LA122_2==SUPER||LA122_2==THIS||LA122_2==VOID||LA122_2==LPAREN||(LA122_2>=EQ && LA122_2<=TILDE)||(LA122_2>=PLUSPLUS && LA122_2<=SUB)) ) {
                        alt122=1;
                    }


                }
                else if ( (LA122_0==GT) ) {
                    int LA122_3 = input.LA(2);

                    if ( ((LA122_3>=IDENTIFIER && LA122_3<=NULL)||LA122_3==BOOLEAN||LA122_3==BYTE||LA122_3==CHAR||LA122_3==DOUBLE||LA122_3==FLOAT||LA122_3==INT||LA122_3==LONG||LA122_3==NEW||LA122_3==SHORT||LA122_3==SUPER||LA122_3==THIS||LA122_3==VOID||LA122_3==LPAREN||(LA122_3>=EQ && LA122_3<=TILDE)||(LA122_3>=PLUSPLUS && LA122_3<=SUB)) ) {
                        alt122=1;
                    }


                }


                switch (alt122) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1057:10: relationalOp shiftExpression
            	    {
            	    pushFollow(FOLLOW_relationalOp_in_relationalExpression5644);
            	    relationalOp();

            	    state._fsp--;
            	    if (state.failed) return ;
            	    pushFollow(FOLLOW_shiftExpression_in_relationalExpression5646);
            	    shiftExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop122;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 82, relationalExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "relationalExpression"


    // $ANTLR start "relationalOp"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1061:1: relationalOp : ( '<' '=' | '>' '=' | '<' | '>' );
    public final void relationalOp() throws RecognitionException {
        int relationalOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 83) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1062:5: ( '<' '=' | '>' '=' | '<' | '>' )
            int alt123=4;
            int LA123_0 = input.LA(1);

            if ( (LA123_0==LT) ) {
                int LA123_1 = input.LA(2);

                if ( (LA123_1==EQ) ) {
                    alt123=1;
                }
                else if ( ((LA123_1>=IDENTIFIER && LA123_1<=NULL)||LA123_1==BOOLEAN||LA123_1==BYTE||LA123_1==CHAR||LA123_1==DOUBLE||LA123_1==FLOAT||LA123_1==INT||LA123_1==LONG||LA123_1==NEW||LA123_1==SHORT||LA123_1==SUPER||LA123_1==THIS||LA123_1==VOID||LA123_1==LPAREN||(LA123_1>=BANG && LA123_1<=TILDE)||(LA123_1>=PLUSPLUS && LA123_1<=SUB)) ) {
                    alt123=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 123, 1, input);

                    throw nvae;
                }
            }
            else if ( (LA123_0==GT) ) {
                int LA123_2 = input.LA(2);

                if ( (LA123_2==EQ) ) {
                    alt123=2;
                }
                else if ( ((LA123_2>=IDENTIFIER && LA123_2<=NULL)||LA123_2==BOOLEAN||LA123_2==BYTE||LA123_2==CHAR||LA123_2==DOUBLE||LA123_2==FLOAT||LA123_2==INT||LA123_2==LONG||LA123_2==NEW||LA123_2==SHORT||LA123_2==SUPER||LA123_2==THIS||LA123_2==VOID||LA123_2==LPAREN||(LA123_2>=BANG && LA123_2<=TILDE)||(LA123_2>=PLUSPLUS && LA123_2<=SUB)) ) {
                    alt123=4;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 123, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 123, 0, input);

                throw nvae;
            }
            switch (alt123) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1062:10: '<' '='
                    {
                    match(input,LT,FOLLOW_LT_in_relationalOp5678); if (state.failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_relationalOp5680); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1063:10: '>' '='
                    {
                    match(input,GT,FOLLOW_GT_in_relationalOp5691); if (state.failed) return ;
                    match(input,EQ,FOLLOW_EQ_in_relationalOp5693); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1064:9: '<'
                    {
                    match(input,LT,FOLLOW_LT_in_relationalOp5703); if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1065:9: '>'
                    {
                    match(input,GT,FOLLOW_GT_in_relationalOp5713); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 83, relationalOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "relationalOp"


    // $ANTLR start "shiftExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1068:1: shiftExpression : additiveExpression ( shiftOp additiveExpression )* ;
    public final void shiftExpression() throws RecognitionException {
        int shiftExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 84) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1069:5: ( additiveExpression ( shiftOp additiveExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1069:9: additiveExpression ( shiftOp additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_shiftExpression5733);
            additiveExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1070:9: ( shiftOp additiveExpression )*
            loop124:
            do {
                int alt124=2;
                int LA124_0 = input.LA(1);

                if ( (LA124_0==LT) ) {
                    int LA124_1 = input.LA(2);

                    if ( (LA124_1==LT) ) {
                        int LA124_4 = input.LA(3);

                        if ( ((LA124_4>=IDENTIFIER && LA124_4<=NULL)||LA124_4==BOOLEAN||LA124_4==BYTE||LA124_4==CHAR||LA124_4==DOUBLE||LA124_4==FLOAT||LA124_4==INT||LA124_4==LONG||LA124_4==NEW||LA124_4==SHORT||LA124_4==SUPER||LA124_4==THIS||LA124_4==VOID||LA124_4==LPAREN||(LA124_4>=BANG && LA124_4<=TILDE)||(LA124_4>=PLUSPLUS && LA124_4<=SUB)) ) {
                            alt124=1;
                        }


                    }


                }
                else if ( (LA124_0==GT) ) {
                    int LA124_2 = input.LA(2);

                    if ( (LA124_2==GT) ) {
                        int LA124_5 = input.LA(3);

                        if ( (LA124_5==GT) ) {
                            int LA124_7 = input.LA(4);

                            if ( ((LA124_7>=IDENTIFIER && LA124_7<=NULL)||LA124_7==BOOLEAN||LA124_7==BYTE||LA124_7==CHAR||LA124_7==DOUBLE||LA124_7==FLOAT||LA124_7==INT||LA124_7==LONG||LA124_7==NEW||LA124_7==SHORT||LA124_7==SUPER||LA124_7==THIS||LA124_7==VOID||LA124_7==LPAREN||(LA124_7>=BANG && LA124_7<=TILDE)||(LA124_7>=PLUSPLUS && LA124_7<=SUB)) ) {
                                alt124=1;
                            }


                        }
                        else if ( ((LA124_5>=IDENTIFIER && LA124_5<=NULL)||LA124_5==BOOLEAN||LA124_5==BYTE||LA124_5==CHAR||LA124_5==DOUBLE||LA124_5==FLOAT||LA124_5==INT||LA124_5==LONG||LA124_5==NEW||LA124_5==SHORT||LA124_5==SUPER||LA124_5==THIS||LA124_5==VOID||LA124_5==LPAREN||(LA124_5>=BANG && LA124_5<=TILDE)||(LA124_5>=PLUSPLUS && LA124_5<=SUB)) ) {
                            alt124=1;
                        }


                    }


                }


                switch (alt124) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1070:10: shiftOp additiveExpression
            	    {
            	    pushFollow(FOLLOW_shiftOp_in_shiftExpression5744);
            	    shiftOp();

            	    state._fsp--;
            	    if (state.failed) return ;
            	    pushFollow(FOLLOW_additiveExpression_in_shiftExpression5746);
            	    additiveExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop124;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 84, shiftExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "shiftExpression"


    // $ANTLR start "shiftOp"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1075:1: shiftOp : ( '<' '<' | '>' '>' '>' | '>' '>' );
    public final void shiftOp() throws RecognitionException {
        int shiftOp_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 85) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1076:5: ( '<' '<' | '>' '>' '>' | '>' '>' )
            int alt125=3;
            int LA125_0 = input.LA(1);

            if ( (LA125_0==LT) ) {
                alt125=1;
            }
            else if ( (LA125_0==GT) ) {
                int LA125_2 = input.LA(2);

                if ( (LA125_2==GT) ) {
                    int LA125_3 = input.LA(3);

                    if ( (LA125_3==GT) ) {
                        alt125=2;
                    }
                    else if ( ((LA125_3>=IDENTIFIER && LA125_3<=NULL)||LA125_3==BOOLEAN||LA125_3==BYTE||LA125_3==CHAR||LA125_3==DOUBLE||LA125_3==FLOAT||LA125_3==INT||LA125_3==LONG||LA125_3==NEW||LA125_3==SHORT||LA125_3==SUPER||LA125_3==THIS||LA125_3==VOID||LA125_3==LPAREN||(LA125_3>=BANG && LA125_3<=TILDE)||(LA125_3>=PLUSPLUS && LA125_3<=SUB)) ) {
                        alt125=3;
                    }
                    else {
                        if (state.backtracking>0) {state.failed=true; return ;}
                        NoViableAltException nvae =
                            new NoViableAltException("", 125, 3, input);

                        throw nvae;
                    }
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 125, 2, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 125, 0, input);

                throw nvae;
            }
            switch (alt125) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1076:10: '<' '<'
                    {
                    match(input,LT,FOLLOW_LT_in_shiftOp5779); if (state.failed) return ;
                    match(input,LT,FOLLOW_LT_in_shiftOp5781); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1077:10: '>' '>' '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp5792); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_shiftOp5794); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_shiftOp5796); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1078:10: '>' '>'
                    {
                    match(input,GT,FOLLOW_GT_in_shiftOp5807); if (state.failed) return ;
                    match(input,GT,FOLLOW_GT_in_shiftOp5809); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 85, shiftOp_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "shiftOp"


    // $ANTLR start "additiveExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1082:1: additiveExpression : multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* ;
    public final void additiveExpression() throws RecognitionException {
        int additiveExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 86) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1083:5: ( multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1083:9: multiplicativeExpression ( ( '+' | '-' ) multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5830);
            multiplicativeExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1084:9: ( ( '+' | '-' ) multiplicativeExpression )*
            loop126:
            do {
                int alt126=2;
                int LA126_0 = input.LA(1);

                if ( ((LA126_0>=PLUS && LA126_0<=SUB)) ) {
                    alt126=1;
                }


                switch (alt126) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1085:13: ( '+' | '-' ) multiplicativeExpression
            	    {
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=SUB) ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression5907);
            	    multiplicativeExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop126;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 86, additiveExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "additiveExpression"


    // $ANTLR start "multiplicativeExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1092:1: multiplicativeExpression : unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* ;
    public final void multiplicativeExpression() throws RecognitionException {
        int multiplicativeExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 87) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1093:5: ( unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )* )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1094:9: unaryExpression ( ( '*' | '/' | '%' ) unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression5945);
            unaryExpression();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1095:9: ( ( '*' | '/' | '%' ) unaryExpression )*
            loop127:
            do {
                int alt127=2;
                int LA127_0 = input.LA(1);

                if ( ((LA127_0>=STAR && LA127_0<=SLASH)||LA127_0==PERCENT) ) {
                    alt127=1;
                }


                switch (alt127) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1096:13: ( '*' | '/' | '%' ) unaryExpression
            	    {
            	    if ( (input.LA(1)>=STAR && input.LA(1)<=SLASH)||input.LA(1)==PERCENT ) {
            	        input.consume();
            	        state.errorRecovery=false;state.failed=false;
            	    }
            	    else {
            	        if (state.backtracking>0) {state.failed=true; return ;}
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression6040);
            	    unaryExpression();

            	    state._fsp--;
            	    if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop127;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 87, multiplicativeExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "multiplicativeExpression"


    // $ANTLR start "unaryExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1104:1: unaryExpression : ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus );
    public final void unaryExpression() throws RecognitionException {
        int unaryExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 88) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1109:5: ( '+' unaryExpression | '-' unaryExpression | '++' unaryExpression | '--' unaryExpression | unaryExpressionNotPlusMinus )
            int alt128=5;
            switch ( input.LA(1) ) {
            case PLUS:
                {
                alt128=1;
                }
                break;
            case SUB:
                {
                alt128=2;
                }
                break;
            case PLUSPLUS:
                {
                alt128=3;
                }
                break;
            case SUBSUB:
                {
                alt128=4;
                }
                break;
            case IDENTIFIER:
            case INTLITERAL:
            case LONGLITERAL:
            case FLOATLITERAL:
            case DOUBLELITERAL:
            case CHARLITERAL:
            case STRINGLITERAL:
            case TRUE:
            case FALSE:
            case NULL:
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case NEW:
            case SHORT:
            case SUPER:
            case THIS:
            case VOID:
            case LPAREN:
            case BANG:
            case TILDE:
                {
                alt128=5;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 128, 0, input);

                throw nvae;
            }

            switch (alt128) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1109:9: '+' unaryExpression
                    {
                    match(input,PLUS,FOLLOW_PLUS_in_unaryExpression6073); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression6076);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1110:9: '-' unaryExpression
                    {
                    match(input,SUB,FOLLOW_SUB_in_unaryExpression6086); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression6088);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1111:9: '++' unaryExpression
                    {
                    match(input,PLUSPLUS,FOLLOW_PLUSPLUS_in_unaryExpression6098); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression6100);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1112:9: '--' unaryExpression
                    {
                    match(input,SUBSUB,FOLLOW_SUBSUB_in_unaryExpression6110); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression6112);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1113:9: unaryExpressionNotPlusMinus
                    {
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression6122);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 88, unaryExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "unaryExpression"


    // $ANTLR start "unaryExpressionNotPlusMinus"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1116:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );
    public final void unaryExpressionNotPlusMinus() throws RecognitionException {
        int unaryExpressionNotPlusMinus_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 89) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1117:5: ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? )
            int alt131=4;
            alt131 = dfa131.predict(input);
            switch (alt131) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1117:9: '~' unaryExpression
                    {
                    match(input,TILDE,FOLLOW_TILDE_in_unaryExpressionNotPlusMinus6142); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6144);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1118:9: '!' unaryExpression
                    {
                    match(input,BANG,FOLLOW_BANG_in_unaryExpressionNotPlusMinus6154); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6156);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1119:9: castExpression
                    {
                    pushFollow(FOLLOW_castExpression_in_unaryExpressionNotPlusMinus6166);
                    castExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1120:9: primary ( selector )* ( '++' | '--' )?
                    {
                    pushFollow(FOLLOW_primary_in_unaryExpressionNotPlusMinus6176);
                    primary();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1121:9: ( selector )*
                    loop129:
                    do {
                        int alt129=2;
                        int LA129_0 = input.LA(1);

                        if ( (LA129_0==LBRACKET||LA129_0==DOT) ) {
                            alt129=1;
                        }


                        switch (alt129) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1121:10: selector
                    	    {
                    	    pushFollow(FOLLOW_selector_in_unaryExpressionNotPlusMinus6187);
                    	    selector();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop129;
                        }
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1123:9: ( '++' | '--' )?
                    int alt130=2;
                    int LA130_0 = input.LA(1);

                    if ( ((LA130_0>=PLUSPLUS && LA130_0<=SUBSUB)) ) {
                        alt130=1;
                    }
                    switch (alt130) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:
                            {
                            if ( (input.LA(1)>=PLUSPLUS && input.LA(1)<=SUBSUB) ) {
                                input.consume();
                                state.errorRecovery=false;state.failed=false;
                            }
                            else {
                                if (state.backtracking>0) {state.failed=true; return ;}
                                MismatchedSetException mse = new MismatchedSetException(null,input);
                                throw mse;
                            }


                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 89, unaryExpressionNotPlusMinus_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "unaryExpressionNotPlusMinus"


    // $ANTLR start "castExpression"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1128:1: castExpression : ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus );
    public final void castExpression() throws RecognitionException {
        int castExpression_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 90) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1129:5: ( '(' primitiveType ')' unaryExpression | '(' type ')' unaryExpressionNotPlusMinus )
            int alt132=2;
            int LA132_0 = input.LA(1);

            if ( (LA132_0==LPAREN) ) {
                int LA132_1 = input.LA(2);

                if ( (synpred206_Java1_6()) ) {
                    alt132=1;
                }
                else if ( (true) ) {
                    alt132=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 132, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 132, 0, input);

                throw nvae;
            }
            switch (alt132) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1129:9: '(' primitiveType ')' unaryExpression
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_castExpression6257); if (state.failed) return ;
                    pushFollow(FOLLOW_primitiveType_in_castExpression6259);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_castExpression6261); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpression_in_castExpression6263);
                    unaryExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1130:9: '(' type ')' unaryExpressionNotPlusMinus
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_castExpression6273); if (state.failed) return ;
                    pushFollow(FOLLOW_type_in_castExpression6275);
                    type();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RPAREN,FOLLOW_RPAREN_in_castExpression6277); if (state.failed) return ;
                    pushFollow(FOLLOW_unaryExpressionNotPlusMinus_in_castExpression6279);
                    unaryExpressionNotPlusMinus();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 90, castExpression_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "castExpression"


    // $ANTLR start "primary"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1133:1: primary : ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' );
    public final void primary() throws RecognitionException {
        int primary_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 91) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1137:5: ( parExpression | 'this' ( '.' IDENTIFIER )* ( identifierSuffix )? | IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )? | 'super' superSuffix | literal | creator | primitiveType ( '[' ']' )* '.' 'class' | 'void' '.' 'class' )
            int alt138=8;
            switch ( input.LA(1) ) {
            case LPAREN:
                {
                alt138=1;
                }
                break;
            case THIS:
                {
                alt138=2;
                }
                break;
            case IDENTIFIER:
                {
                alt138=3;
                }
                break;
            case SUPER:
                {
                alt138=4;
                }
                break;
            case INTLITERAL:
            case LONGLITERAL:
            case FLOATLITERAL:
            case DOUBLELITERAL:
            case CHARLITERAL:
            case STRINGLITERAL:
            case TRUE:
            case FALSE:
            case NULL:
                {
                alt138=5;
                }
                break;
            case NEW:
                {
                alt138=6;
                }
                break;
            case BOOLEAN:
            case BYTE:
            case CHAR:
            case DOUBLE:
            case FLOAT:
            case INT:
            case LONG:
            case SHORT:
                {
                alt138=7;
                }
                break;
            case VOID:
                {
                alt138=8;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 138, 0, input);

                throw nvae;
            }

            switch (alt138) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1137:9: parExpression
                    {
                    pushFollow(FOLLOW_parExpression_in_primary6301);
                    parExpression();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1138:9: 'this' ( '.' IDENTIFIER )* ( identifierSuffix )?
                    {
                    match(input,THIS,FOLLOW_THIS_in_primary6323); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1139:9: ( '.' IDENTIFIER )*
                    loop133:
                    do {
                        int alt133=2;
                        int LA133_0 = input.LA(1);

                        if ( (LA133_0==DOT) ) {
                            int LA133_2 = input.LA(2);

                            if ( (LA133_2==IDENTIFIER) ) {
                                int LA133_3 = input.LA(3);

                                if ( (synpred208_Java1_6()) ) {
                                    alt133=1;
                                }


                            }


                        }


                        switch (alt133) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1139:10: '.' IDENTIFIER
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_primary6334); if (state.failed) return ;
                    	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary6336); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop133;
                        }
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1141:9: ( identifierSuffix )?
                    int alt134=2;
                    alt134 = dfa134.predict(input);
                    switch (alt134) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1141:10: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary6358);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1143:9: IDENTIFIER ( '.' IDENTIFIER )* ( identifierSuffix )?
                    {
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary6379); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1144:9: ( '.' IDENTIFIER )*
                    loop135:
                    do {
                        int alt135=2;
                        int LA135_0 = input.LA(1);

                        if ( (LA135_0==DOT) ) {
                            int LA135_2 = input.LA(2);

                            if ( (LA135_2==IDENTIFIER) ) {
                                int LA135_3 = input.LA(3);

                                if ( (synpred211_Java1_6()) ) {
                                    alt135=1;
                                }


                            }


                        }


                        switch (alt135) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1144:10: '.' IDENTIFIER
                    	    {
                    	    match(input,DOT,FOLLOW_DOT_in_primary6390); if (state.failed) return ;
                    	    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_primary6392); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop135;
                        }
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1146:9: ( identifierSuffix )?
                    int alt136=2;
                    alt136 = dfa136.predict(input);
                    switch (alt136) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1146:10: identifierSuffix
                            {
                            pushFollow(FOLLOW_identifierSuffix_in_primary6414);
                            identifierSuffix();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1148:9: 'super' superSuffix
                    {
                    match(input,SUPER,FOLLOW_SUPER_in_primary6435); if (state.failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_primary6445);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1150:9: literal
                    {
                    pushFollow(FOLLOW_literal_in_primary6455);
                    literal();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1151:9: creator
                    {
                    pushFollow(FOLLOW_creator_in_primary6465);
                    creator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1152:9: primitiveType ( '[' ']' )* '.' 'class'
                    {
                    pushFollow(FOLLOW_primitiveType_in_primary6475);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1153:9: ( '[' ']' )*
                    loop137:
                    do {
                        int alt137=2;
                        int LA137_0 = input.LA(1);

                        if ( (LA137_0==LBRACKET) ) {
                            alt137=1;
                        }


                        switch (alt137) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1153:10: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_primary6486); if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_primary6488); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop137;
                        }
                    } while (true);

                    match(input,DOT,FOLLOW_DOT_in_primary6509); if (state.failed) return ;
                    match(input,CLASS,FOLLOW_CLASS_in_primary6511); if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1156:9: 'void' '.' 'class'
                    {
                    match(input,VOID,FOLLOW_VOID_in_primary6521); if (state.failed) return ;
                    match(input,DOT,FOLLOW_DOT_in_primary6523); if (state.failed) return ;
                    match(input,CLASS,FOLLOW_CLASS_in_primary6525); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 91, primary_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "primary"


    // $ANTLR start "superSuffix"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1160:1: superSuffix : ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? );
    public final void superSuffix() throws RecognitionException {
        int superSuffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 92) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1161:5: ( arguments | '.' ( typeArguments )? IDENTIFIER ( arguments )? )
            int alt141=2;
            int LA141_0 = input.LA(1);

            if ( (LA141_0==LPAREN) ) {
                alt141=1;
            }
            else if ( (LA141_0==DOT) ) {
                alt141=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 141, 0, input);

                throw nvae;
            }
            switch (alt141) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1161:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_superSuffix6551);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1162:9: '.' ( typeArguments )? IDENTIFIER ( arguments )?
                    {
                    match(input,DOT,FOLLOW_DOT_in_superSuffix6561); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1162:13: ( typeArguments )?
                    int alt139=2;
                    int LA139_0 = input.LA(1);

                    if ( (LA139_0==LT) ) {
                        alt139=1;
                    }
                    switch (alt139) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1162:14: typeArguments
                            {
                            pushFollow(FOLLOW_typeArguments_in_superSuffix6564);
                            typeArguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_superSuffix6585); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1165:9: ( arguments )?
                    int alt140=2;
                    int LA140_0 = input.LA(1);

                    if ( (LA140_0==LPAREN) ) {
                        alt140=1;
                    }
                    switch (alt140) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1165:10: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_superSuffix6596);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 92, superSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "superSuffix"


    // $ANTLR start "identifierSuffix"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1170:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator );
    public final void identifierSuffix() throws RecognitionException {
        int identifierSuffix_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 93) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1171:5: ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator )
            int alt144=8;
            alt144 = dfa144.predict(input);
            switch (alt144) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1171:9: ( '[' ']' )+ '.' 'class'
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1171:9: ( '[' ']' )+
                    int cnt142=0;
                    loop142:
                    do {
                        int alt142=2;
                        int LA142_0 = input.LA(1);

                        if ( (LA142_0==LBRACKET) ) {
                            alt142=1;
                        }


                        switch (alt142) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1171:10: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix6629); if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix6631); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt142 >= 1 ) break loop142;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(142, input);
                                throw eee;
                        }
                        cnt142++;
                    } while (true);

                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix6652); if (state.failed) return ;
                    match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix6654); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1174:9: ( '[' expression ']' )+
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1174:9: ( '[' expression ']' )+
                    int cnt143=0;
                    loop143:
                    do {
                        int alt143=2;
                        alt143 = dfa143.predict(input);
                        switch (alt143) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1174:10: '[' expression ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_identifierSuffix6665); if (state.failed) return ;
                    	    pushFollow(FOLLOW_expression_in_identifierSuffix6667);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_identifierSuffix6669); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    if ( cnt143 >= 1 ) break loop143;
                    	    if (state.backtracking>0) {state.failed=true; return ;}
                                EarlyExitException eee =
                                    new EarlyExitException(143, input);
                                throw eee;
                        }
                        cnt143++;
                    } while (true);


                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1176:9: arguments
                    {
                    pushFollow(FOLLOW_arguments_in_identifierSuffix6690);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1177:9: '.' 'class'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix6700); if (state.failed) return ;
                    match(input,CLASS,FOLLOW_CLASS_in_identifierSuffix6702); if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1178:9: '.' nonWildcardTypeArguments IDENTIFIER arguments
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix6712); if (state.failed) return ;
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_identifierSuffix6714);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_identifierSuffix6716); if (state.failed) return ;
                    pushFollow(FOLLOW_arguments_in_identifierSuffix6718);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 6 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1179:9: '.' 'this'
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix6728); if (state.failed) return ;
                    match(input,THIS,FOLLOW_THIS_in_identifierSuffix6730); if (state.failed) return ;

                    }
                    break;
                case 7 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1180:9: '.' 'super' arguments
                    {
                    match(input,DOT,FOLLOW_DOT_in_identifierSuffix6740); if (state.failed) return ;
                    match(input,SUPER,FOLLOW_SUPER_in_identifierSuffix6742); if (state.failed) return ;
                    pushFollow(FOLLOW_arguments_in_identifierSuffix6744);
                    arguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 8 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1181:9: innerCreator
                    {
                    pushFollow(FOLLOW_innerCreator_in_identifierSuffix6754);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 93, identifierSuffix_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "identifierSuffix"


    // $ANTLR start "selector"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1185:1: selector : ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' );
    public final void selector() throws RecognitionException {
        int selector_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 94) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1186:5: ( '.' IDENTIFIER ( arguments )? | '.' 'this' | '.' 'super' superSuffix | innerCreator | '[' expression ']' )
            int alt146=5;
            int LA146_0 = input.LA(1);

            if ( (LA146_0==DOT) ) {
                switch ( input.LA(2) ) {
                case IDENTIFIER:
                    {
                    alt146=1;
                    }
                    break;
                case THIS:
                    {
                    alt146=2;
                    }
                    break;
                case SUPER:
                    {
                    alt146=3;
                    }
                    break;
                case NEW:
                    {
                    alt146=4;
                    }
                    break;
                default:
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 146, 1, input);

                    throw nvae;
                }

            }
            else if ( (LA146_0==LBRACKET) ) {
                alt146=5;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 146, 0, input);

                throw nvae;
            }
            switch (alt146) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1186:9: '.' IDENTIFIER ( arguments )?
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector6776); if (state.failed) return ;
                    match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_selector6778); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1187:9: ( arguments )?
                    int alt145=2;
                    int LA145_0 = input.LA(1);

                    if ( (LA145_0==LPAREN) ) {
                        alt145=1;
                    }
                    switch (alt145) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1187:10: arguments
                            {
                            pushFollow(FOLLOW_arguments_in_selector6789);
                            arguments();

                            state._fsp--;
                            if (state.failed) return ;

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1189:9: '.' 'this'
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector6810); if (state.failed) return ;
                    match(input,THIS,FOLLOW_THIS_in_selector6812); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1190:9: '.' 'super' superSuffix
                    {
                    match(input,DOT,FOLLOW_DOT_in_selector6822); if (state.failed) return ;
                    match(input,SUPER,FOLLOW_SUPER_in_selector6824); if (state.failed) return ;
                    pushFollow(FOLLOW_superSuffix_in_selector6834);
                    superSuffix();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 4 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1192:9: innerCreator
                    {
                    pushFollow(FOLLOW_innerCreator_in_selector6844);
                    innerCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 5 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1193:9: '[' expression ']'
                    {
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_selector6854); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_selector6856);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_selector6858); if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 94, selector_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "selector"


    // $ANTLR start "creator"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1196:1: creator : ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator );
    public final void creator() throws RecognitionException {
        int creator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 95) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1197:5: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest | 'new' classOrInterfaceType classCreatorRest | arrayCreator )
            int alt147=3;
            int LA147_0 = input.LA(1);

            if ( (LA147_0==NEW) ) {
                int LA147_1 = input.LA(2);

                if ( (synpred236_Java1_6()) ) {
                    alt147=1;
                }
                else if ( (synpred237_Java1_6()) ) {
                    alt147=2;
                }
                else if ( (true) ) {
                    alt147=3;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 147, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 147, 0, input);

                throw nvae;
            }
            switch (alt147) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1197:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
                    {
                    match(input,NEW,FOLLOW_NEW_in_creator6878); if (state.failed) return ;
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_creator6880);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_classOrInterfaceType_in_creator6882);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_classCreatorRest_in_creator6884);
                    classCreatorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1198:9: 'new' classOrInterfaceType classCreatorRest
                    {
                    match(input,NEW,FOLLOW_NEW_in_creator6894); if (state.failed) return ;
                    pushFollow(FOLLOW_classOrInterfaceType_in_creator6896);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return ;
                    pushFollow(FOLLOW_classCreatorRest_in_creator6898);
                    classCreatorRest();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1199:9: arrayCreator
                    {
                    pushFollow(FOLLOW_arrayCreator_in_creator6908);
                    arrayCreator();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 95, creator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "creator"


    // $ANTLR start "arrayCreator"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1202:1: arrayCreator : ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* );
    public final void arrayCreator() throws RecognitionException {
        int arrayCreator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 96) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1203:5: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer | 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )* )
            int alt151=2;
            int LA151_0 = input.LA(1);

            if ( (LA151_0==NEW) ) {
                int LA151_1 = input.LA(2);

                if ( (synpred239_Java1_6()) ) {
                    alt151=1;
                }
                else if ( (true) ) {
                    alt151=2;
                }
                else {
                    if (state.backtracking>0) {state.failed=true; return ;}
                    NoViableAltException nvae =
                        new NoViableAltException("", 151, 1, input);

                    throw nvae;
                }
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 151, 0, input);

                throw nvae;
            }
            switch (alt151) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1203:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
                    {
                    match(input,NEW,FOLLOW_NEW_in_arrayCreator6928); if (state.failed) return ;
                    pushFollow(FOLLOW_createdName_in_arrayCreator6930);
                    createdName();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator6940); if (state.failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator6942); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1205:9: ( '[' ']' )*
                    loop148:
                    do {
                        int alt148=2;
                        int LA148_0 = input.LA(1);

                        if ( (LA148_0==LBRACKET) ) {
                            alt148=1;
                        }


                        switch (alt148) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1205:10: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator6953); if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator6955); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop148;
                        }
                    } while (true);

                    pushFollow(FOLLOW_arrayInitializer_in_arrayCreator6976);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1209:9: 'new' createdName '[' expression ']' ( '[' expression ']' )* ( '[' ']' )*
                    {
                    match(input,NEW,FOLLOW_NEW_in_arrayCreator6987); if (state.failed) return ;
                    pushFollow(FOLLOW_createdName_in_arrayCreator6989);
                    createdName();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator6999); if (state.failed) return ;
                    pushFollow(FOLLOW_expression_in_arrayCreator7001);
                    expression();

                    state._fsp--;
                    if (state.failed) return ;
                    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator7011); if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1212:9: ( '[' expression ']' )*
                    loop149:
                    do {
                        int alt149=2;
                        alt149 = dfa149.predict(input);
                        switch (alt149) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1212:13: '[' expression ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator7025); if (state.failed) return ;
                    	    pushFollow(FOLLOW_expression_in_arrayCreator7027);
                    	    expression();

                    	    state._fsp--;
                    	    if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator7041); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop149;
                        }
                    } while (true);

                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1215:9: ( '[' ']' )*
                    loop150:
                    do {
                        int alt150=2;
                        int LA150_0 = input.LA(1);

                        if ( (LA150_0==LBRACKET) ) {
                            int LA150_2 = input.LA(2);

                            if ( (LA150_2==RBRACKET) ) {
                                alt150=1;
                            }


                        }


                        switch (alt150) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1215:10: '[' ']'
                    	    {
                    	    match(input,LBRACKET,FOLLOW_LBRACKET_in_arrayCreator7063); if (state.failed) return ;
                    	    match(input,RBRACKET,FOLLOW_RBRACKET_in_arrayCreator7065); if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop150;
                        }
                    } while (true);


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 96, arrayCreator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arrayCreator"

    public static class variableInitializer_return extends ParserRuleReturnScope {
    };

    // $ANTLR start "variableInitializer"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1219:1: variableInitializer : ( arrayInitializer | expression );
    public final Java1_6Parser.variableInitializer_return variableInitializer() throws RecognitionException {
        Java1_6Parser.variableInitializer_return retval = new Java1_6Parser.variableInitializer_return();
        retval.start = input.LT(1);
        int variableInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 97) ) { return retval; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1220:5: ( arrayInitializer | expression )
            int alt152=2;
            int LA152_0 = input.LA(1);

            if ( (LA152_0==LBRACE) ) {
                alt152=1;
            }
            else if ( ((LA152_0>=IDENTIFIER && LA152_0<=NULL)||LA152_0==BOOLEAN||LA152_0==BYTE||LA152_0==CHAR||LA152_0==DOUBLE||LA152_0==FLOAT||LA152_0==INT||LA152_0==LONG||LA152_0==NEW||LA152_0==SHORT||LA152_0==SUPER||LA152_0==THIS||LA152_0==VOID||LA152_0==LPAREN||(LA152_0>=BANG && LA152_0<=TILDE)||(LA152_0>=PLUSPLUS && LA152_0<=SUB)) ) {
                alt152=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return retval;}
                NoViableAltException nvae =
                    new NoViableAltException("", 152, 0, input);

                throw nvae;
            }
            switch (alt152) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1220:9: arrayInitializer
                    {
                    pushFollow(FOLLOW_arrayInitializer_in_variableInitializer7096);
                    arrayInitializer();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1221:9: expression
                    {
                    pushFollow(FOLLOW_expression_in_variableInitializer7106);
                    expression();

                    state._fsp--;
                    if (state.failed) return retval;

                    }
                    break;

            }
            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 97, variableInitializer_StartIndex); }
        }
        return retval;
    }
    // $ANTLR end "variableInitializer"


    // $ANTLR start "arrayInitializer"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1224:1: arrayInitializer : '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' ;
    public final void arrayInitializer() throws RecognitionException {
        int arrayInitializer_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 98) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1225:5: ( '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1225:9: '{' ( variableInitializer ( ',' variableInitializer )* )? ( ',' )? '}'
            {
            match(input,LBRACE,FOLLOW_LBRACE_in_arrayInitializer7126); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1226:13: ( variableInitializer ( ',' variableInitializer )* )?
            int alt154=2;
            int LA154_0 = input.LA(1);

            if ( ((LA154_0>=IDENTIFIER && LA154_0<=NULL)||LA154_0==BOOLEAN||LA154_0==BYTE||LA154_0==CHAR||LA154_0==DOUBLE||LA154_0==FLOAT||LA154_0==INT||LA154_0==LONG||LA154_0==NEW||LA154_0==SHORT||LA154_0==SUPER||LA154_0==THIS||LA154_0==VOID||LA154_0==LPAREN||LA154_0==LBRACE||(LA154_0>=BANG && LA154_0<=TILDE)||(LA154_0>=PLUSPLUS && LA154_0<=SUB)) ) {
                alt154=1;
            }
            switch (alt154) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1226:14: variableInitializer ( ',' variableInitializer )*
                    {
                    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer7142);
                    variableInitializer();

                    state._fsp--;
                    if (state.failed) return ;
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1227:17: ( ',' variableInitializer )*
                    loop153:
                    do {
                        int alt153=2;
                        int LA153_0 = input.LA(1);

                        if ( (LA153_0==COMMA) ) {
                            int LA153_1 = input.LA(2);

                            if ( ((LA153_1>=IDENTIFIER && LA153_1<=NULL)||LA153_1==BOOLEAN||LA153_1==BYTE||LA153_1==CHAR||LA153_1==DOUBLE||LA153_1==FLOAT||LA153_1==INT||LA153_1==LONG||LA153_1==NEW||LA153_1==SHORT||LA153_1==SUPER||LA153_1==THIS||LA153_1==VOID||LA153_1==LPAREN||LA153_1==LBRACE||(LA153_1>=BANG && LA153_1<=TILDE)||(LA153_1>=PLUSPLUS && LA153_1<=SUB)) ) {
                                alt153=1;
                            }


                        }


                        switch (alt153) {
                    	case 1 :
                    	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1227:18: ',' variableInitializer
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer7161); if (state.failed) return ;
                    	    pushFollow(FOLLOW_variableInitializer_in_arrayInitializer7163);
                    	    variableInitializer();

                    	    state._fsp--;
                    	    if (state.failed) return ;

                    	    }
                    	    break;

                    	default :
                    	    break loop153;
                        }
                    } while (true);


                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1230:13: ( ',' )?
            int alt155=2;
            int LA155_0 = input.LA(1);

            if ( (LA155_0==COMMA) ) {
                alt155=1;
            }
            switch (alt155) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1230:14: ','
                    {
                    match(input,COMMA,FOLLOW_COMMA_in_arrayInitializer7213); if (state.failed) return ;

                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_arrayInitializer7226); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 98, arrayInitializer_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arrayInitializer"


    // $ANTLR start "createdName"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1235:1: createdName : ( classOrInterfaceType | primitiveType );
    public final void createdName() throws RecognitionException {
        int createdName_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 99) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1236:5: ( classOrInterfaceType | primitiveType )
            int alt156=2;
            int LA156_0 = input.LA(1);

            if ( (LA156_0==IDENTIFIER) ) {
                alt156=1;
            }
            else if ( (LA156_0==BOOLEAN||LA156_0==BYTE||LA156_0==CHAR||LA156_0==DOUBLE||LA156_0==FLOAT||LA156_0==INT||LA156_0==LONG||LA156_0==SHORT) ) {
                alt156=2;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 156, 0, input);

                throw nvae;
            }
            switch (alt156) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1236:9: classOrInterfaceType
                    {
                    pushFollow(FOLLOW_classOrInterfaceType_in_createdName7260);
                    classOrInterfaceType();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1237:9: primitiveType
                    {
                    pushFollow(FOLLOW_primitiveType_in_createdName7270);
                    primitiveType();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 99, createdName_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "createdName"


    // $ANTLR start "innerCreator"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1240:1: innerCreator : '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest ;
    public final void innerCreator() throws RecognitionException {
        int innerCreator_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 100) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1241:5: ( '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1241:9: '.' 'new' ( nonWildcardTypeArguments )? IDENTIFIER ( typeArguments )? classCreatorRest
            {
            match(input,DOT,FOLLOW_DOT_in_innerCreator7291); if (state.failed) return ;
            match(input,NEW,FOLLOW_NEW_in_innerCreator7293); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1242:9: ( nonWildcardTypeArguments )?
            int alt157=2;
            int LA157_0 = input.LA(1);

            if ( (LA157_0==LT) ) {
                alt157=1;
            }
            switch (alt157) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1242:10: nonWildcardTypeArguments
                    {
                    pushFollow(FOLLOW_nonWildcardTypeArguments_in_innerCreator7304);
                    nonWildcardTypeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_innerCreator7325); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1245:9: ( typeArguments )?
            int alt158=2;
            int LA158_0 = input.LA(1);

            if ( (LA158_0==LT) ) {
                alt158=1;
            }
            switch (alt158) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1245:10: typeArguments
                    {
                    pushFollow(FOLLOW_typeArguments_in_innerCreator7336);
                    typeArguments();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            pushFollow(FOLLOW_classCreatorRest_in_innerCreator7357);
            classCreatorRest();

            state._fsp--;
            if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 100, innerCreator_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "innerCreator"


    // $ANTLR start "classCreatorRest"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1251:1: classCreatorRest : arguments ( classBody )? ;
    public final void classCreatorRest() throws RecognitionException {
        int classCreatorRest_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 101) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1252:5: ( arguments ( classBody )? )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1252:9: arguments ( classBody )?
            {
            pushFollow(FOLLOW_arguments_in_classCreatorRest7378);
            arguments();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1253:9: ( classBody )?
            int alt159=2;
            int LA159_0 = input.LA(1);

            if ( (LA159_0==LBRACE) ) {
                alt159=1;
            }
            switch (alt159) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1253:10: classBody
                    {
                    pushFollow(FOLLOW_classBody_in_classCreatorRest7389);
                    classBody();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 101, classCreatorRest_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classCreatorRest"


    // $ANTLR start "nonWildcardTypeArguments"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1258:1: nonWildcardTypeArguments : '<' typeList '>' ;
    public final void nonWildcardTypeArguments() throws RecognitionException {
        int nonWildcardTypeArguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 102) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1259:5: ( '<' typeList '>' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1259:9: '<' typeList '>'
            {
            match(input,LT,FOLLOW_LT_in_nonWildcardTypeArguments7421); if (state.failed) return ;
            pushFollow(FOLLOW_typeList_in_nonWildcardTypeArguments7423);
            typeList();

            state._fsp--;
            if (state.failed) return ;
            match(input,GT,FOLLOW_GT_in_nonWildcardTypeArguments7433); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 102, nonWildcardTypeArguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "nonWildcardTypeArguments"


    // $ANTLR start "arguments"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1263:1: arguments : '(' ( expressionList )? ')' ;
    public final void arguments() throws RecognitionException {
        int arguments_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 103) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1264:5: ( '(' ( expressionList )? ')' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1264:9: '(' ( expressionList )? ')'
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_arguments7453); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1264:13: ( expressionList )?
            int alt160=2;
            int LA160_0 = input.LA(1);

            if ( ((LA160_0>=IDENTIFIER && LA160_0<=NULL)||LA160_0==BOOLEAN||LA160_0==BYTE||LA160_0==CHAR||LA160_0==DOUBLE||LA160_0==FLOAT||LA160_0==INT||LA160_0==LONG||LA160_0==NEW||LA160_0==SHORT||LA160_0==SUPER||LA160_0==THIS||LA160_0==VOID||LA160_0==LPAREN||(LA160_0>=BANG && LA160_0<=TILDE)||(LA160_0>=PLUSPLUS && LA160_0<=SUB)) ) {
                alt160=1;
            }
            switch (alt160) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1264:14: expressionList
                    {
                    pushFollow(FOLLOW_expressionList_in_arguments7456);
                    expressionList();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_arguments7469); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 103, arguments_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "arguments"


    // $ANTLR start "literal"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1268:1: literal : ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL );
    public final void literal() throws RecognitionException {
        int literal_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 104) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1269:5: ( INTLITERAL | LONGLITERAL | FLOATLITERAL | DOUBLELITERAL | CHARLITERAL | STRINGLITERAL | TRUE | FALSE | NULL )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:
            {
            if ( (input.LA(1)>=INTLITERAL && input.LA(1)<=NULL) ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 104, literal_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "literal"


    // $ANTLR start "classHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1280:1: classHeader : modifiers 'class' IDENTIFIER ;
    public final void classHeader() throws RecognitionException {
        int classHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 105) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1285:5: ( modifiers 'class' IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1285:9: modifiers 'class' IDENTIFIER
            {
            pushFollow(FOLLOW_modifiers_in_classHeader7593);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,CLASS,FOLLOW_CLASS_in_classHeader7595); if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_classHeader7597); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 105, classHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "classHeader"


    // $ANTLR start "enumHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1288:1: enumHeader : modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER ;
    public final void enumHeader() throws RecognitionException {
        int enumHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 106) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1289:5: ( modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1289:9: modifiers ( 'enum' | IDENTIFIER ) IDENTIFIER
            {
            pushFollow(FOLLOW_modifiers_in_enumHeader7617);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            if ( input.LA(1)==IDENTIFIER||input.LA(1)==ENUM ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_enumHeader7625); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 106, enumHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "enumHeader"


    // $ANTLR start "interfaceHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1292:1: interfaceHeader : modifiers 'interface' IDENTIFIER ;
    public final void interfaceHeader() throws RecognitionException {
        int interfaceHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 107) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1293:5: ( modifiers 'interface' IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1293:9: modifiers 'interface' IDENTIFIER
            {
            pushFollow(FOLLOW_modifiers_in_interfaceHeader7645);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,INTERFACE,FOLLOW_INTERFACE_in_interfaceHeader7647); if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_interfaceHeader7649); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 107, interfaceHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "interfaceHeader"


    // $ANTLR start "annotationHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1296:1: annotationHeader : modifiers '@' 'interface' IDENTIFIER ;
    public final void annotationHeader() throws RecognitionException {
        int annotationHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 108) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1297:5: ( modifiers '@' 'interface' IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1297:9: modifiers '@' 'interface' IDENTIFIER
            {
            pushFollow(FOLLOW_modifiers_in_annotationHeader7669);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_annotationHeader7671); if (state.failed) return ;
            match(input,INTERFACE,FOLLOW_INTERFACE_in_annotationHeader7673); if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_annotationHeader7675); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 108, annotationHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "annotationHeader"


    // $ANTLR start "typeHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1300:1: typeHeader : modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER ;
    public final void typeHeader() throws RecognitionException {
        int typeHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 109) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:5: ( modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:9: modifiers ( 'class' | 'enum' | ( ( '@' )? 'interface' ) ) IDENTIFIER
            {
            pushFollow(FOLLOW_modifiers_in_typeHeader7695);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:19: ( 'class' | 'enum' | ( ( '@' )? 'interface' ) )
            int alt162=3;
            switch ( input.LA(1) ) {
            case CLASS:
                {
                alt162=1;
                }
                break;
            case ENUM:
                {
                alt162=2;
                }
                break;
            case INTERFACE:
            case MONKEYS_AT:
                {
                alt162=3;
                }
                break;
            default:
                if (state.backtracking>0) {state.failed=true; return ;}
                NoViableAltException nvae =
                    new NoViableAltException("", 162, 0, input);

                throw nvae;
            }

            switch (alt162) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:20: 'class'
                    {
                    match(input,CLASS,FOLLOW_CLASS_in_typeHeader7698); if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:28: 'enum'
                    {
                    match(input,ENUM,FOLLOW_ENUM_in_typeHeader7700); if (state.failed) return ;

                    }
                    break;
                case 3 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:35: ( ( '@' )? 'interface' )
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:35: ( ( '@' )? 'interface' )
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:36: ( '@' )? 'interface'
                    {
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1301:36: ( '@' )?
                    int alt161=2;
                    int LA161_0 = input.LA(1);

                    if ( (LA161_0==MONKEYS_AT) ) {
                        alt161=1;
                    }
                    switch (alt161) {
                        case 1 :
                            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:0:0: '@'
                            {
                            match(input,MONKEYS_AT,FOLLOW_MONKEYS_AT_in_typeHeader7703); if (state.failed) return ;

                            }
                            break;

                    }

                    match(input,INTERFACE,FOLLOW_INTERFACE_in_typeHeader7707); if (state.failed) return ;

                    }


                    }
                    break;

            }

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_typeHeader7711); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 109, typeHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "typeHeader"


    // $ANTLR start "methodHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1304:1: methodHeader : modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '(' ;
    public final void methodHeader() throws RecognitionException {
        int methodHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 110) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:5: ( modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '(' )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:9: modifiers ( typeParameters )? ( type | 'void' )? IDENTIFIER '('
            {
            pushFollow(FOLLOW_modifiers_in_methodHeader7731);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:19: ( typeParameters )?
            int alt163=2;
            int LA163_0 = input.LA(1);

            if ( (LA163_0==LT) ) {
                alt163=1;
            }
            switch (alt163) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:0:0: typeParameters
                    {
                    pushFollow(FOLLOW_typeParameters_in_methodHeader7733);
                    typeParameters();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;

            }

            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:35: ( type | 'void' )?
            int alt164=3;
            switch ( input.LA(1) ) {
                case IDENTIFIER:
                    {
                    int LA164_1 = input.LA(2);

                    if ( (LA164_1==IDENTIFIER||LA164_1==LBRACKET||LA164_1==DOT||LA164_1==LT) ) {
                        alt164=1;
                    }
                    }
                    break;
                case BOOLEAN:
                case BYTE:
                case CHAR:
                case DOUBLE:
                case FLOAT:
                case INT:
                case LONG:
                case SHORT:
                    {
                    alt164=1;
                    }
                    break;
                case VOID:
                    {
                    alt164=2;
                    }
                    break;
            }

            switch (alt164) {
                case 1 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:36: type
                    {
                    pushFollow(FOLLOW_type_in_methodHeader7737);
                    type();

                    state._fsp--;
                    if (state.failed) return ;

                    }
                    break;
                case 2 :
                    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1305:41: 'void'
                    {
                    match(input,VOID,FOLLOW_VOID_in_methodHeader7739); if (state.failed) return ;

                    }
                    break;

            }

            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_methodHeader7743); if (state.failed) return ;
            match(input,LPAREN,FOLLOW_LPAREN_in_methodHeader7745); if (state.failed) return ;

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 110, methodHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "methodHeader"


    // $ANTLR start "fieldHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1308:1: fieldHeader : modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) ;
    public final void fieldHeader() throws RecognitionException {
        int fieldHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 111) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1309:5: ( modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1309:9: modifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' )
            {
            pushFollow(FOLLOW_modifiers_in_fieldHeader7765);
            modifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_fieldHeader7767);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_fieldHeader7769); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1309:35: ( '[' ']' )*
            loop165:
            do {
                int alt165=2;
                int LA165_0 = input.LA(1);

                if ( (LA165_0==LBRACKET) ) {
                    alt165=1;
                }


                switch (alt165) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1309:36: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_fieldHeader7772); if (state.failed) return ;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_fieldHeader7773); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop165;
                }
            } while (true);

            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA)||input.LA(1)==EQ ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 111, fieldHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "fieldHeader"


    // $ANTLR start "localVariableHeader"
    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1312:1: localVariableHeader : variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) ;
    public final void localVariableHeader() throws RecognitionException {
        int localVariableHeader_StartIndex = input.index();
        try {
            if ( state.backtracking>0 && alreadyParsedRule(input, 112) ) { return ; }
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1313:5: ( variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' ) )
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1313:9: variableModifiers type IDENTIFIER ( '[' ']' )* ( '=' | ',' | ';' )
            {
            pushFollow(FOLLOW_variableModifiers_in_localVariableHeader7803);
            variableModifiers();

            state._fsp--;
            if (state.failed) return ;
            pushFollow(FOLLOW_type_in_localVariableHeader7805);
            type();

            state._fsp--;
            if (state.failed) return ;
            match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_localVariableHeader7807); if (state.failed) return ;
            // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1313:43: ( '[' ']' )*
            loop166:
            do {
                int alt166=2;
                int LA166_0 = input.LA(1);

                if ( (LA166_0==LBRACKET) ) {
                    alt166=1;
                }


                switch (alt166) {
            	case 1 :
            	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1313:44: '[' ']'
            	    {
            	    match(input,LBRACKET,FOLLOW_LBRACKET_in_localVariableHeader7810); if (state.failed) return ;
            	    match(input,RBRACKET,FOLLOW_RBRACKET_in_localVariableHeader7811); if (state.failed) return ;

            	    }
            	    break;

            	default :
            	    break loop166;
                }
            } while (true);

            if ( (input.LA(1)>=SEMI && input.LA(1)<=COMMA)||input.LA(1)==EQ ) {
                input.consume();
                state.errorRecovery=false;state.failed=false;
            }
            else {
                if (state.backtracking>0) {state.failed=true; return ;}
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
            if ( state.backtracking>0 ) { memoize(input, 112, localVariableHeader_StartIndex); }
        }
        return ;
    }
    // $ANTLR end "localVariableHeader"

    // $ANTLR start synpred2_Java1_6
    public final void synpred2_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:6: ( ( annotations )? packageDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:6: ( annotations )? packageDeclaration
        {
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:6: ( annotations )?
        int alt167=2;
        int LA167_0 = input.LA(1);

        if ( (LA167_0==MONKEYS_AT) ) {
            alt167=1;
        }
        switch (alt167) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:140:7: annotations
                {
                pushFollow(FOLLOW_annotations_in_synpred2_Java1_687);
                annotations();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        pushFollow(FOLLOW_packageDeclaration_in_synpred2_Java1_691);
        packageDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred2_Java1_6

    // $ANTLR start synpred12_Java1_6
    public final void synpred12_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:188:5: ( classDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:188:5: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred12_Java1_6311);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred12_Java1_6

    // $ANTLR start synpred27_Java1_6
    public final void synpred27_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:221:5: ( normalClassDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:221:5: normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred27_Java1_6499);
        normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred27_Java1_6

    // $ANTLR start synpred43_Java1_6
    public final void synpred43_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:355:9: ( normalInterfaceDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:355:9: normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1_61116);
        normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred43_Java1_6

    // $ANTLR start synpred50_Java1_6
    public final void synpred50_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:413:7: ( fieldDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:413:7: fieldDeclaration
        {
        pushFollow(FOLLOW_fieldDeclaration_in_synpred50_Java1_61465);
        fieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred50_Java1_6

    // $ANTLR start synpred51_Java1_6
    public final void synpred51_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:414:7: ( methodDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:414:7: methodDeclaration
        {
        pushFollow(FOLLOW_methodDeclaration_in_synpred51_Java1_61473);
        methodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred51_Java1_6

    // $ANTLR start synpred52_Java1_6
    public final void synpred52_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:415:7: ( classDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:415:7: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred52_Java1_61481);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred52_Java1_6

    // $ANTLR start synpred53_Java1_6
    public final void synpred53_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:416:7: ( interfaceDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:416:7: interfaceDeclaration
        {
        pushFollow(FOLLOW_interfaceDeclaration_in_synpred53_Java1_61489);
        interfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred53_Java1_6

    // $ANTLR start synpred57_Java1_6
    public final void synpred57_Java1_6_fragment() throws RecognitionException {   
        Java1_6Parser.explicitConstructorInvocation_return explicitConstructorTemp = null;


        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:11: (explicitConstructorTemp= explicitConstructorInvocation )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:11: explicitConstructorTemp= explicitConstructorInvocation
        {
        pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred57_Java1_61642);
        explicitConstructorTemp=explicitConstructorInvocation();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred57_Java1_6

    // $ANTLR start synpred59_Java1_6
    public final void synpred59_Java1_6_fragment() throws RecognitionException {   
        Token constructorIdentifier=null;
        int constructorModifiers = 0;

        List<ParameterOfMethode> formalParametersList = null;

        List<String> exceptionListTemp = null;

        Java1_6Parser.explicitConstructorInvocation_return explicitConstructorTemp = null;

        Java1_6Parser.blockStatement_return blockStatementTemp = null;


        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:452:4: (constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:452:4: constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}'
        {
        pushFollow(FOLLOW_modifiers_in_synpred59_Java1_61575);
        constructorModifiers=modifiers();

        state._fsp--;
        if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:453:5: ( typeParameters )?
        int alt169=2;
        int LA169_0 = input.LA(1);

        if ( (LA169_0==LT) ) {
            alt169=1;
        }
        switch (alt169) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:453:7: typeParameters
                {
                pushFollow(FOLLOW_typeParameters_in_synpred59_Java1_61583);
                typeParameters();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        constructorIdentifier=(Token)match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred59_Java1_61594); if (state.failed) return ;
        pushFollow(FOLLOW_formalParameters_in_synpred59_Java1_61603);
        formalParametersList=formalParameters();

        state._fsp--;
        if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:456:5: ( 'throws' exceptionListTemp= qualifiedNameList )?
        int alt170=2;
        int LA170_0 = input.LA(1);

        if ( (LA170_0==THROWS) ) {
            alt170=1;
        }
        switch (alt170) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:456:7: 'throws' exceptionListTemp= qualifiedNameList
                {
                match(input,THROWS,FOLLOW_THROWS_in_synpred59_Java1_61611); if (state.failed) return ;
                pushFollow(FOLLOW_qualifiedNameList_in_synpred59_Java1_61615);
                exceptionListTemp=qualifiedNameList();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        match(input,LBRACE,FOLLOW_LBRACE_in_synpred59_Java1_61627); if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:9: (explicitConstructorTemp= explicitConstructorInvocation )?
        int alt171=2;
        alt171 = dfa171.predict(input);
        switch (alt171) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:458:11: explicitConstructorTemp= explicitConstructorInvocation
                {
                pushFollow(FOLLOW_explicitConstructorInvocation_in_synpred59_Java1_61642);
                explicitConstructorTemp=explicitConstructorInvocation();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:459:9: (blockStatementTemp= blockStatement )*
        loop172:
        do {
            int alt172=2;
            int LA172_0 = input.LA(1);

            if ( ((LA172_0>=IDENTIFIER && LA172_0<=NULL)||(LA172_0>=ABSTRACT && LA172_0<=BYTE)||(LA172_0>=CHAR && LA172_0<=CLASS)||LA172_0==CONTINUE||(LA172_0>=DO && LA172_0<=DOUBLE)||LA172_0==ENUM||LA172_0==FINAL||(LA172_0>=FLOAT && LA172_0<=FOR)||LA172_0==IF||(LA172_0>=INT && LA172_0<=NEW)||(LA172_0>=PRIVATE && LA172_0<=THROW)||(LA172_0>=TRANSIENT && LA172_0<=LPAREN)||LA172_0==LBRACE||LA172_0==SEMI||(LA172_0>=BANG && LA172_0<=TILDE)||(LA172_0>=PLUSPLUS && LA172_0<=SUB)||LA172_0==MONKEYS_AT||LA172_0==LT) ) {
                alt172=1;
            }


            switch (alt172) {
        	case 1 :
        	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:459:11: blockStatementTemp= blockStatement
        	    {
        	    pushFollow(FOLLOW_blockStatement_in_synpred59_Java1_61662);
        	    blockStatementTemp=blockStatement();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop172;
            }
        } while (true);

        match(input,RBRACE,FOLLOW_RBRACE_in_synpred59_Java1_61675); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred59_Java1_6

    // $ANTLR start synpred68_Java1_6
    public final void synpred68_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:541:9: ( interfaceFieldDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:541:9: interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred68_Java1_61968);
        interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred68_Java1_6

    // $ANTLR start synpred69_Java1_6
    public final void synpred69_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:542:9: ( interfaceMethodDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:542:9: interfaceMethodDeclaration
        {
        pushFollow(FOLLOW_interfaceMethodDeclaration_in_synpred69_Java1_61978);
        interfaceMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred69_Java1_6

    // $ANTLR start synpred70_Java1_6
    public final void synpred70_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:543:9: ( interfaceDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:543:9: interfaceDeclaration
        {
        pushFollow(FOLLOW_interfaceDeclaration_in_synpred70_Java1_61988);
        interfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred70_Java1_6

    // $ANTLR start synpred71_Java1_6
    public final void synpred71_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:544:9: ( classDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:544:9: classDeclaration
        {
        pushFollow(FOLLOW_classDeclaration_in_synpred71_Java1_61998);
        classDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred71_Java1_6

    // $ANTLR start synpred96_Java1_6
    public final void synpred96_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:664:3: ( ellipsisParameterDecl )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:664:3: ellipsisParameterDecl
        {
        pushFollow(FOLLOW_ellipsisParameterDecl_in_synpred96_Java1_62744);
        ellipsisParameterDecl();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred96_Java1_6

    // $ANTLR start synpred98_Java1_6
    public final void synpred98_Java1_6_fragment() throws RecognitionException {   
        ParameterOfMethode normalParameterType = null;


        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:665:7: (normalParameterType= normalParameterDecl ( ',' normalParameterType= normalParameterDecl )* )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:665:7: normalParameterType= normalParameterDecl ( ',' normalParameterType= normalParameterDecl )*
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java1_62754);
        normalParameterType=normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:667:5: ( ',' normalParameterType= normalParameterDecl )*
        loop175:
        do {
            int alt175=2;
            int LA175_0 = input.LA(1);

            if ( (LA175_0==COMMA) ) {
                alt175=1;
            }


            switch (alt175) {
        	case 1 :
        	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:667:7: ',' normalParameterType= normalParameterDecl
        	    {
        	    match(input,COMMA,FOLLOW_COMMA_in_synpred98_Java1_62769); if (state.failed) return ;
        	    pushFollow(FOLLOW_normalParameterDecl_in_synpred98_Java1_62776);
        	    normalParameterType=normalParameterDecl();

        	    state._fsp--;
        	    if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop175;
            }
        } while (true);


        }
    }
    // $ANTLR end synpred98_Java1_6

    // $ANTLR start synpred99_Java1_6
    public final void synpred99_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:671:8: ( normalParameterDecl ',' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:671:8: normalParameterDecl ','
        {
        pushFollow(FOLLOW_normalParameterDecl_in_synpred99_Java1_62796);
        normalParameterDecl();

        state._fsp--;
        if (state.failed) return ;
        match(input,COMMA,FOLLOW_COMMA_in_synpred99_Java1_62802); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred99_Java1_6

    // $ANTLR start synpred103_Java1_6
    public final void synpred103_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:9: ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:9: ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';'
        {
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:9: ( nonWildcardTypeArguments )?
        int alt176=2;
        int LA176_0 = input.LA(1);

        if ( (LA176_0==LT) ) {
            alt176=1;
        }
        switch (alt176) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:704:10: nonWildcardTypeArguments
                {
                pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred103_Java1_62955);
                nonWildcardTypeArguments();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        if ( input.LA(1)==SUPER||input.LA(1)==THIS ) {
            input.consume();
            state.errorRecovery=false;state.failed=false;
        }
        else {
            if (state.backtracking>0) {state.failed=true; return ;}
            MismatchedSetException mse = new MismatchedSetException(null,input);
            throw mse;
        }

        pushFollow(FOLLOW_arguments_in_synpred103_Java1_63013);
        arguments();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred103_Java1_63015); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred103_Java1_6

    // $ANTLR start synpred117_Java1_6
    public final void synpred117_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:806:9: ( annotationMethodDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:806:9: annotationMethodDeclaration
        {
        pushFollow(FOLLOW_annotationMethodDeclaration_in_synpred117_Java1_63631);
        annotationMethodDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred117_Java1_6

    // $ANTLR start synpred118_Java1_6
    public final void synpred118_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:807:9: ( interfaceFieldDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:807:9: interfaceFieldDeclaration
        {
        pushFollow(FOLLOW_interfaceFieldDeclaration_in_synpred118_Java1_63641);
        interfaceFieldDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred118_Java1_6

    // $ANTLR start synpred119_Java1_6
    public final void synpred119_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:808:9: ( normalClassDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:808:9: normalClassDeclaration
        {
        pushFollow(FOLLOW_normalClassDeclaration_in_synpred119_Java1_63651);
        normalClassDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred119_Java1_6

    // $ANTLR start synpred120_Java1_6
    public final void synpred120_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:809:9: ( normalInterfaceDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:809:9: normalInterfaceDeclaration
        {
        pushFollow(FOLLOW_normalInterfaceDeclaration_in_synpred120_Java1_63661);
        normalInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred120_Java1_6

    // $ANTLR start synpred121_Java1_6
    public final void synpred121_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:810:9: ( enumDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:810:9: enumDeclaration
        {
        pushFollow(FOLLOW_enumDeclaration_in_synpred121_Java1_63671);
        enumDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred121_Java1_6

    // $ANTLR start synpred122_Java1_6
    public final void synpred122_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:811:9: ( annotationTypeDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:811:9: annotationTypeDeclaration
        {
        pushFollow(FOLLOW_annotationTypeDeclaration_in_synpred122_Java1_63681);
        annotationTypeDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred122_Java1_6

    // $ANTLR start synpred125_Java1_6
    public final void synpred125_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:860:9: ( localVariableDeclarationStatement )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:860:9: localVariableDeclarationStatement
        {
        pushFollow(FOLLOW_localVariableDeclarationStatement_in_synpred125_Java1_63872);
        localVariableDeclarationStatement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred125_Java1_6

    // $ANTLR start synpred126_Java1_6
    public final void synpred126_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:861:9: ( classOrInterfaceDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:861:9: classOrInterfaceDeclaration
        {
        pushFollow(FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java1_63882);
        classOrInterfaceDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred126_Java1_6

    // $ANTLR start synpred130_Java1_6
    public final void synpred130_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:9: ( ( 'assert' ) expression ( ':' expression )? ';' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:9: ( 'assert' ) expression ( ':' expression )? ';'
        {
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:9: ( 'assert' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:881:10: 'assert'
        {
        match(input,ASSERT,FOLLOW_ASSERT_in_synpred130_Java1_64025); if (state.failed) return ;

        }

        pushFollow(FOLLOW_expression_in_synpred130_Java1_64045);
        expression();

        state._fsp--;
        if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:883:20: ( ':' expression )?
        int alt179=2;
        int LA179_0 = input.LA(1);

        if ( (LA179_0==COLON) ) {
            alt179=1;
        }
        switch (alt179) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:883:21: ':' expression
                {
                match(input,COLON,FOLLOW_COLON_in_synpred130_Java1_64048); if (state.failed) return ;
                pushFollow(FOLLOW_expression_in_synpred130_Java1_64050);
                expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        match(input,SEMI,FOLLOW_SEMI_in_synpred130_Java1_64054); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred130_Java1_6

    // $ANTLR start synpred132_Java1_6
    public final void synpred132_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:9: ( 'assert' expression ( ':' expression )? ';' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:9: 'assert' expression ( ':' expression )? ';'
        {
        match(input,ASSERT,FOLLOW_ASSERT_in_synpred132_Java1_64064); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred132_Java1_64067);
        expression();

        state._fsp--;
        if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:30: ( ':' expression )?
        int alt180=2;
        int LA180_0 = input.LA(1);

        if ( (LA180_0==COLON) ) {
            alt180=1;
        }
        switch (alt180) {
            case 1 :
                // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:884:31: ':' expression
                {
                match(input,COLON,FOLLOW_COLON_in_synpred132_Java1_64070); if (state.failed) return ;
                pushFollow(FOLLOW_expression_in_synpred132_Java1_64072);
                expression();

                state._fsp--;
                if (state.failed) return ;

                }
                break;

        }

        match(input,SEMI,FOLLOW_SEMI_in_synpred132_Java1_64076); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred132_Java1_6

    // $ANTLR start synpred133_Java1_6
    public final void synpred133_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:885:39: ( 'else' statement )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:885:39: 'else' statement
        {
        match(input,ELSE,FOLLOW_ELSE_in_synpred133_Java1_64105); if (state.failed) return ;
        pushFollow(FOLLOW_statement_in_synpred133_Java1_64107);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred133_Java1_6

    // $ANTLR start synpred148_Java1_6
    public final void synpred148_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:900:9: ( expression ';' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:900:9: expression ';'
        {
        pushFollow(FOLLOW_expression_in_synpred148_Java1_64329);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,SEMI,FOLLOW_SEMI_in_synpred148_Java1_64332); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred148_Java1_6

    // $ANTLR start synpred149_Java1_6
    public final void synpred149_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:901:9: ( IDENTIFIER ':' statement )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:901:9: IDENTIFIER ':' statement
        {
        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred149_Java1_64347); if (state.failed) return ;
        match(input,COLON,FOLLOW_COLON_in_synpred149_Java1_64349); if (state.failed) return ;
        pushFollow(FOLLOW_statement_in_synpred149_Java1_64351);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred149_Java1_6

    // $ANTLR start synpred153_Java1_6
    public final void synpred153_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:925:13: ( catches 'finally' block )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:925:13: catches 'finally' block
        {
        pushFollow(FOLLOW_catches_in_synpred153_Java1_64507);
        catches();

        state._fsp--;
        if (state.failed) return ;
        match(input,FINALLY,FOLLOW_FINALLY_in_synpred153_Java1_64509); if (state.failed) return ;
        pushFollow(FOLLOW_block_in_synpred153_Java1_64511);
        block();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred153_Java1_6

    // $ANTLR start synpred154_Java1_6
    public final void synpred154_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:926:13: ( catches )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:926:13: catches
        {
        pushFollow(FOLLOW_catches_in_synpred154_Java1_64525);
        catches();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred154_Java1_6

    // $ANTLR start synpred157_Java1_6
    public final void synpred157_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:951:9: ( 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:951:9: 'for' '(' variableModifiers type IDENTIFIER ':' expression ')' statement
        {
        match(input,FOR,FOLLOW_FOR_in_synpred157_Java1_64717); if (state.failed) return ;
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred157_Java1_64719); if (state.failed) return ;
        pushFollow(FOLLOW_variableModifiers_in_synpred157_Java1_64721);
        variableModifiers();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_type_in_synpred157_Java1_64723);
        type();

        state._fsp--;
        if (state.failed) return ;
        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred157_Java1_64725); if (state.failed) return ;
        match(input,COLON,FOLLOW_COLON_in_synpred157_Java1_64727); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred157_Java1_64738);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred157_Java1_64740); if (state.failed) return ;
        pushFollow(FOLLOW_statement_in_synpred157_Java1_64742);
        statement();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred157_Java1_6

    // $ANTLR start synpred161_Java1_6
    public final void synpred161_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:965:9: ( localVariableDeclaration )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:965:9: localVariableDeclaration
        {
        pushFollow(FOLLOW_localVariableDeclaration_in_synpred161_Java1_64921);
        localVariableDeclaration();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred161_Java1_6

    // $ANTLR start synpred202_Java1_6
    public final void synpred202_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1119:9: ( castExpression )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1119:9: castExpression
        {
        pushFollow(FOLLOW_castExpression_in_synpred202_Java1_66166);
        castExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred202_Java1_6

    // $ANTLR start synpred206_Java1_6
    public final void synpred206_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1129:9: ( '(' primitiveType ')' unaryExpression )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1129:9: '(' primitiveType ')' unaryExpression
        {
        match(input,LPAREN,FOLLOW_LPAREN_in_synpred206_Java1_66257); if (state.failed) return ;
        pushFollow(FOLLOW_primitiveType_in_synpred206_Java1_66259);
        primitiveType();

        state._fsp--;
        if (state.failed) return ;
        match(input,RPAREN,FOLLOW_RPAREN_in_synpred206_Java1_66261); if (state.failed) return ;
        pushFollow(FOLLOW_unaryExpression_in_synpred206_Java1_66263);
        unaryExpression();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred206_Java1_6

    // $ANTLR start synpred208_Java1_6
    public final void synpred208_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1139:10: ( '.' IDENTIFIER )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1139:10: '.' IDENTIFIER
        {
        match(input,DOT,FOLLOW_DOT_in_synpred208_Java1_66334); if (state.failed) return ;
        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred208_Java1_66336); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred208_Java1_6

    // $ANTLR start synpred209_Java1_6
    public final void synpred209_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1141:10: ( identifierSuffix )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1141:10: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred209_Java1_66358);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred209_Java1_6

    // $ANTLR start synpred211_Java1_6
    public final void synpred211_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1144:10: ( '.' IDENTIFIER )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1144:10: '.' IDENTIFIER
        {
        match(input,DOT,FOLLOW_DOT_in_synpred211_Java1_66390); if (state.failed) return ;
        match(input,IDENTIFIER,FOLLOW_IDENTIFIER_in_synpred211_Java1_66392); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred211_Java1_6

    // $ANTLR start synpred212_Java1_6
    public final void synpred212_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1146:10: ( identifierSuffix )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1146:10: identifierSuffix
        {
        pushFollow(FOLLOW_identifierSuffix_in_synpred212_Java1_66414);
        identifierSuffix();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred212_Java1_6

    // $ANTLR start synpred224_Java1_6
    public final void synpred224_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1174:10: ( '[' expression ']' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1174:10: '[' expression ']'
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred224_Java1_66665); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred224_Java1_66667);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred224_Java1_66669); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred224_Java1_6

    // $ANTLR start synpred236_Java1_6
    public final void synpred236_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1197:9: ( 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1197:9: 'new' nonWildcardTypeArguments classOrInterfaceType classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred236_Java1_66878); if (state.failed) return ;
        pushFollow(FOLLOW_nonWildcardTypeArguments_in_synpred236_Java1_66880);
        nonWildcardTypeArguments();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_classOrInterfaceType_in_synpred236_Java1_66882);
        classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_classCreatorRest_in_synpred236_Java1_66884);
        classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred236_Java1_6

    // $ANTLR start synpred237_Java1_6
    public final void synpred237_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1198:9: ( 'new' classOrInterfaceType classCreatorRest )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1198:9: 'new' classOrInterfaceType classCreatorRest
        {
        match(input,NEW,FOLLOW_NEW_in_synpred237_Java1_66894); if (state.failed) return ;
        pushFollow(FOLLOW_classOrInterfaceType_in_synpred237_Java1_66896);
        classOrInterfaceType();

        state._fsp--;
        if (state.failed) return ;
        pushFollow(FOLLOW_classCreatorRest_in_synpred237_Java1_66898);
        classCreatorRest();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred237_Java1_6

    // $ANTLR start synpred239_Java1_6
    public final void synpred239_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1203:9: ( 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1203:9: 'new' createdName '[' ']' ( '[' ']' )* arrayInitializer
        {
        match(input,NEW,FOLLOW_NEW_in_synpred239_Java1_66928); if (state.failed) return ;
        pushFollow(FOLLOW_createdName_in_synpred239_Java1_66930);
        createdName();

        state._fsp--;
        if (state.failed) return ;
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred239_Java1_66940); if (state.failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred239_Java1_66942); if (state.failed) return ;
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1205:9: ( '[' ']' )*
        loop193:
        do {
            int alt193=2;
            int LA193_0 = input.LA(1);

            if ( (LA193_0==LBRACKET) ) {
                alt193=1;
            }


            switch (alt193) {
        	case 1 :
        	    // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1205:10: '[' ']'
        	    {
        	    match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred239_Java1_66953); if (state.failed) return ;
        	    match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred239_Java1_66955); if (state.failed) return ;

        	    }
        	    break;

        	default :
        	    break loop193;
            }
        } while (true);

        pushFollow(FOLLOW_arrayInitializer_in_synpred239_Java1_66976);
        arrayInitializer();

        state._fsp--;
        if (state.failed) return ;

        }
    }
    // $ANTLR end synpred239_Java1_6

    // $ANTLR start synpred240_Java1_6
    public final void synpred240_Java1_6_fragment() throws RecognitionException {   
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1212:13: ( '[' expression ']' )
        // C:\\Users\\Admin\\Documents\\My Workspaces\\Open Modelsphere-18\\workspace\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\parsing\\Java1_6.g:1212:13: '[' expression ']'
        {
        match(input,LBRACKET,FOLLOW_LBRACKET_in_synpred240_Java1_67025); if (state.failed) return ;
        pushFollow(FOLLOW_expression_in_synpred240_Java1_67027);
        expression();

        state._fsp--;
        if (state.failed) return ;
        match(input,RBRACKET,FOLLOW_RBRACKET_in_synpred240_Java1_67041); if (state.failed) return ;

        }
    }
    // $ANTLR end synpred240_Java1_6

    // Delegated rules

    public final boolean synpred224_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred224_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred117_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred117_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred133_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred133_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred122_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred122_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred98_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred98_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred125_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred125_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred239_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred239_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred237_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred237_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred209_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred209_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred202_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred202_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred149_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred149_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred71_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred71_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred96_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred96_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred50_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred50_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred206_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred206_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred119_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred119_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred153_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred153_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred126_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred126_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred53_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred53_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred69_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred69_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred161_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred161_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred157_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred157_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred118_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred118_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred99_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred99_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred70_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred70_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred12_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred12_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred208_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred208_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred130_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred130_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred59_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred59_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred240_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred240_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred236_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred236_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred212_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred212_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred57_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred57_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred27_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred27_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred68_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred68_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred103_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred103_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred132_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred132_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred120_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred120_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred211_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred211_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred121_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred121_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred148_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred148_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred43_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred43_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred2_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred2_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred154_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred154_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred51_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred51_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }
    public final boolean synpred52_Java1_6() {
        state.backtracking++;
        int start = input.mark();
        try {
            synpred52_Java1_6_fragment(); // can never throw exception
        } catch (RecognitionException re) {
            System.err.println("impossible: "+re);
        }
        boolean success = !state.failed;
        input.rewind(start);
        state.backtracking--;
        state.failed=false;
        return success;
    }


    protected DFA2 dfa2 = new DFA2(this);
    protected DFA12 dfa12 = new DFA12(this);
    protected DFA16 dfa16 = new DFA16(this);
    protected DFA32 dfa32 = new DFA32(this);
    protected DFA39 dfa39 = new DFA39(this);
    protected DFA50 dfa50 = new DFA50(this);
    protected DFA43 dfa43 = new DFA43(this);
    protected DFA54 dfa54 = new DFA54(this);
    protected DFA77 dfa77 = new DFA77(this);
    protected DFA88 dfa88 = new DFA88(this);
    protected DFA91 dfa91 = new DFA91(this);
    protected DFA99 dfa99 = new DFA99(this);
    protected DFA110 dfa110 = new DFA110(this);
    protected DFA113 dfa113 = new DFA113(this);
    protected DFA131 dfa131 = new DFA131(this);
    protected DFA134 dfa134 = new DFA134(this);
    protected DFA136 dfa136 = new DFA136(this);
    protected DFA144 dfa144 = new DFA144(this);
    protected DFA143 dfa143 = new DFA143(this);
    protected DFA149 dfa149 = new DFA149(this);
    protected DFA171 dfa171 = new DFA171(this);
    static final String DFA2_eotS =
        "\24\uffff";
    static final String DFA2_eofS =
        "\1\3\23\uffff";
    static final String DFA2_minS =
        "\1\32\1\0\22\uffff";
    static final String DFA2_maxS =
        "\1\160\1\0\22\uffff";
    static final String DFA2_acceptS =
        "\2\uffff\1\1\1\2\20\uffff";
    static final String DFA2_specialS =
        "\1\uffff\1\0\22\uffff}>";
    static final String[] DFA2_transitionS = {
            "\1\3\7\uffff\1\3\6\uffff\1\3\1\uffff\1\3\6\uffff\1\3\2\uffff"+
            "\1\3\1\uffff\1\3\1\uffff\1\2\3\3\2\uffff\2\3\2\uffff\1\3\3\uffff"+
            "\1\3\2\uffff\1\3\7\uffff\1\3\35\uffff\1\1",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA2_eot = DFA.unpackEncodedString(DFA2_eotS);
    static final short[] DFA2_eof = DFA.unpackEncodedString(DFA2_eofS);
    static final char[] DFA2_min = DFA.unpackEncodedStringToUnsignedChars(DFA2_minS);
    static final char[] DFA2_max = DFA.unpackEncodedStringToUnsignedChars(DFA2_maxS);
    static final short[] DFA2_accept = DFA.unpackEncodedString(DFA2_acceptS);
    static final short[] DFA2_special = DFA.unpackEncodedString(DFA2_specialS);
    static final short[][] DFA2_transition;

    static {
        int numStates = DFA2_transitionS.length;
        DFA2_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA2_transition[i] = DFA.unpackEncodedString(DFA2_transitionS[i]);
        }
    }

    class DFA2 extends DFA {

        public DFA2(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 2;
            this.eot = DFA2_eot;
            this.eof = DFA2_eof;
            this.min = DFA2_min;
            this.max = DFA2_max;
            this.accept = DFA2_accept;
            this.special = DFA2_special;
            this.transition = DFA2_transition;
        }
        public String getDescription() {
            return "140:4: ( ( annotations )? packageDeclaration )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA2_1 = input.LA(1);

                         
                        int index2_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred2_Java1_6()) ) {s = 2;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index2_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 2, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA12_eotS =
        "\20\uffff";
    static final String DFA12_eofS =
        "\20\uffff";
    static final String DFA12_minS =
        "\1\32\14\0\3\uffff";
    static final String DFA12_maxS =
        "\1\160\14\0\3\uffff";
    static final String DFA12_acceptS =
        "\15\uffff\1\1\1\uffff\1\2";
    static final String DFA12_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\3"+
        "\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\5\7\uffff\1\15\6\uffff\1\15\1\uffff\1\7\11\uffff\1\17\1"+
            "\uffff\1\10\2\uffff\1\4\1\3\1\2\2\uffff\1\6\1\14\2\uffff\1\11"+
            "\3\uffff\1\12\2\uffff\1\13\45\uffff\1\1",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "187:1: classOrInterfaceDeclaration : ( classDeclaration | interfaceDeclaration );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA12_1 = input.LA(1);

                         
                        int index12_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA12_2 = input.LA(1);

                         
                        int index12_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA12_3 = input.LA(1);

                         
                        int index12_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA12_4 = input.LA(1);

                         
                        int index12_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA12_5 = input.LA(1);

                         
                        int index12_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA12_6 = input.LA(1);

                         
                        int index12_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA12_7 = input.LA(1);

                         
                        int index12_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA12_8 = input.LA(1);

                         
                        int index12_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA12_9 = input.LA(1);

                         
                        int index12_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA12_10 = input.LA(1);

                         
                        int index12_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA12_11 = input.LA(1);

                         
                        int index12_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA12_12 = input.LA(1);

                         
                        int index12_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred12_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index12_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 12, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA16_eotS =
        "\17\uffff";
    static final String DFA16_eofS =
        "\17\uffff";
    static final String DFA16_minS =
        "\1\32\14\0\2\uffff";
    static final String DFA16_maxS =
        "\1\160\14\0\2\uffff";
    static final String DFA16_acceptS =
        "\15\uffff\1\1\1\2";
    static final String DFA16_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\2"+
        "\uffff}>";
    static final String[] DFA16_transitionS = {
            "\1\5\7\uffff\1\15\6\uffff\1\16\1\uffff\1\7\13\uffff\1\10\2"+
            "\uffff\1\4\1\3\1\2\2\uffff\1\6\1\14\2\uffff\1\11\3\uffff\1\12"+
            "\2\uffff\1\13\45\uffff\1\1",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA16_eot = DFA.unpackEncodedString(DFA16_eotS);
    static final short[] DFA16_eof = DFA.unpackEncodedString(DFA16_eofS);
    static final char[] DFA16_min = DFA.unpackEncodedStringToUnsignedChars(DFA16_minS);
    static final char[] DFA16_max = DFA.unpackEncodedStringToUnsignedChars(DFA16_maxS);
    static final short[] DFA16_accept = DFA.unpackEncodedString(DFA16_acceptS);
    static final short[] DFA16_special = DFA.unpackEncodedString(DFA16_specialS);
    static final short[][] DFA16_transition;

    static {
        int numStates = DFA16_transitionS.length;
        DFA16_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA16_transition[i] = DFA.unpackEncodedString(DFA16_transitionS[i]);
        }
    }

    class DFA16 extends DFA {

        public DFA16(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 16;
            this.eot = DFA16_eot;
            this.eof = DFA16_eof;
            this.min = DFA16_min;
            this.max = DFA16_max;
            this.accept = DFA16_accept;
            this.special = DFA16_special;
            this.transition = DFA16_transition;
        }
        public String getDescription() {
            return "220:1: classDeclaration : ( normalClassDeclaration | enumDeclaration );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA16_1 = input.LA(1);

                         
                        int index16_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA16_2 = input.LA(1);

                         
                        int index16_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA16_3 = input.LA(1);

                         
                        int index16_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA16_4 = input.LA(1);

                         
                        int index16_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA16_5 = input.LA(1);

                         
                        int index16_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA16_6 = input.LA(1);

                         
                        int index16_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA16_7 = input.LA(1);

                         
                        int index16_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA16_8 = input.LA(1);

                         
                        int index16_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA16_9 = input.LA(1);

                         
                        int index16_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA16_10 = input.LA(1);

                         
                        int index16_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA16_11 = input.LA(1);

                         
                        int index16_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA16_12 = input.LA(1);

                         
                        int index16_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred27_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index16_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 16, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA32_eotS =
        "\17\uffff";
    static final String DFA32_eofS =
        "\17\uffff";
    static final String DFA32_minS =
        "\1\32\14\0\2\uffff";
    static final String DFA32_maxS =
        "\1\160\14\0\2\uffff";
    static final String DFA32_acceptS =
        "\15\uffff\1\1\1\2";
    static final String DFA32_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\2"+
        "\uffff}>";
    static final String[] DFA32_transitionS = {
            "\1\5\20\uffff\1\7\11\uffff\1\15\1\uffff\1\10\2\uffff\1\4\1"+
            "\3\1\2\2\uffff\1\6\1\14\2\uffff\1\11\3\uffff\1\12\2\uffff\1"+
            "\13\45\uffff\1\1",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            ""
    };

    static final short[] DFA32_eot = DFA.unpackEncodedString(DFA32_eotS);
    static final short[] DFA32_eof = DFA.unpackEncodedString(DFA32_eofS);
    static final char[] DFA32_min = DFA.unpackEncodedStringToUnsignedChars(DFA32_minS);
    static final char[] DFA32_max = DFA.unpackEncodedStringToUnsignedChars(DFA32_maxS);
    static final short[] DFA32_accept = DFA.unpackEncodedString(DFA32_acceptS);
    static final short[] DFA32_special = DFA.unpackEncodedString(DFA32_specialS);
    static final short[][] DFA32_transition;

    static {
        int numStates = DFA32_transitionS.length;
        DFA32_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA32_transition[i] = DFA.unpackEncodedString(DFA32_transitionS[i]);
        }
    }

    class DFA32 extends DFA {

        public DFA32(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 32;
            this.eot = DFA32_eot;
            this.eof = DFA32_eof;
            this.min = DFA32_min;
            this.max = DFA32_max;
            this.accept = DFA32_accept;
            this.special = DFA32_special;
            this.transition = DFA32_transition;
        }
        public String getDescription() {
            return "354:1: interfaceDeclaration : ( normalInterfaceDeclaration | annotationTypeDeclaration );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA32_1 = input.LA(1);

                         
                        int index32_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA32_2 = input.LA(1);

                         
                        int index32_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA32_3 = input.LA(1);

                         
                        int index32_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA32_4 = input.LA(1);

                         
                        int index32_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA32_5 = input.LA(1);

                         
                        int index32_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA32_6 = input.LA(1);

                         
                        int index32_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA32_7 = input.LA(1);

                         
                        int index32_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA32_8 = input.LA(1);

                         
                        int index32_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA32_9 = input.LA(1);

                         
                        int index32_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA32_10 = input.LA(1);

                         
                        int index32_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA32_11 = input.LA(1);

                         
                        int index32_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA32_12 = input.LA(1);

                         
                        int index32_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred43_Java1_6()) ) {s = 13;}

                        else if ( (true) ) {s = 14;}

                         
                        input.seek(index32_12);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 32, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA39_eotS =
        "\26\uffff";
    static final String DFA39_eofS =
        "\26\uffff";
    static final String DFA39_minS =
        "\1\4\16\0\7\uffff";
    static final String DFA39_maxS =
        "\1\163\16\0\7\uffff";
    static final String DFA39_acceptS =
        "\17\uffff\1\2\1\uffff\1\3\1\uffff\1\4\1\5\1\1";
    static final String DFA39_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\7\uffff}>";
    static final String[] DFA39_transitionS = {
            "\1\15\25\uffff\1\5\1\uffff\1\16\1\uffff\1\16\2\uffff\1\16\1"+
            "\21\4\uffff\1\16\1\uffff\1\21\1\uffff\1\7\1\uffff\1\16\6\uffff"+
            "\1\16\1\23\1\16\1\10\2\uffff\1\4\1\3\1\2\1\uffff\1\16\1\6\1"+
            "\14\2\uffff\1\11\3\uffff\1\12\1\uffff\1\17\1\13\3\uffff\1\24"+
            "\41\uffff\1\1\2\uffff\1\17",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA39_eot = DFA.unpackEncodedString(DFA39_eotS);
    static final short[] DFA39_eof = DFA.unpackEncodedString(DFA39_eofS);
    static final char[] DFA39_min = DFA.unpackEncodedStringToUnsignedChars(DFA39_minS);
    static final char[] DFA39_max = DFA.unpackEncodedStringToUnsignedChars(DFA39_maxS);
    static final short[] DFA39_accept = DFA.unpackEncodedString(DFA39_acceptS);
    static final short[] DFA39_special = DFA.unpackEncodedString(DFA39_specialS);
    static final short[][] DFA39_transition;

    static {
        int numStates = DFA39_transitionS.length;
        DFA39_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA39_transition[i] = DFA.unpackEncodedString(DFA39_transitionS[i]);
        }
    }

    class DFA39 extends DFA {

        public DFA39(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 39;
            this.eot = DFA39_eot;
            this.eof = DFA39_eof;
            this.min = DFA39_min;
            this.max = DFA39_max;
            this.accept = DFA39_accept;
            this.special = DFA39_special;
            this.transition = DFA39_transition;
        }
        public String getDescription() {
            return "412:1: memberDecl : ( fieldDeclaration | methodDeclaration | classDeclaration | interfaceDeclaration | initializationBlock );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA39_1 = input.LA(1);

                         
                        int index39_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA39_2 = input.LA(1);

                         
                        int index39_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA39_3 = input.LA(1);

                         
                        int index39_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA39_4 = input.LA(1);

                         
                        int index39_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA39_5 = input.LA(1);

                         
                        int index39_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA39_6 = input.LA(1);

                         
                        int index39_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                        else if ( (true) ) {s = 20;}

                         
                        input.seek(index39_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA39_7 = input.LA(1);

                         
                        int index39_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA39_8 = input.LA(1);

                         
                        int index39_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA39_9 = input.LA(1);

                         
                        int index39_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA39_10 = input.LA(1);

                         
                        int index39_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA39_11 = input.LA(1);

                         
                        int index39_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA39_12 = input.LA(1);

                         
                        int index39_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                        else if ( (synpred52_Java1_6()) ) {s = 17;}

                        else if ( (synpred53_Java1_6()) ) {s = 19;}

                         
                        input.seek(index39_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA39_13 = input.LA(1);

                         
                        int index39_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                         
                        input.seek(index39_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA39_14 = input.LA(1);

                         
                        int index39_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred50_Java1_6()) ) {s = 21;}

                        else if ( (synpred51_Java1_6()) ) {s = 15;}

                         
                        input.seek(index39_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 39, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA50_eotS =
        "\22\uffff";
    static final String DFA50_eofS =
        "\22\uffff";
    static final String DFA50_minS =
        "\1\4\16\0\3\uffff";
    static final String DFA50_maxS =
        "\1\163\16\0\3\uffff";
    static final String DFA50_acceptS =
        "\17\uffff\1\2\1\uffff\1\1";
    static final String DFA50_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\3\uffff}>";
    static final String[] DFA50_transitionS = {
            "\1\16\25\uffff\1\5\1\uffff\1\17\1\uffff\1\17\2\uffff\1\17\5"+
            "\uffff\1\17\3\uffff\1\7\1\uffff\1\17\6\uffff\1\17\1\uffff\1"+
            "\17\1\10\2\uffff\1\4\1\3\1\2\1\uffff\1\17\1\6\1\14\2\uffff\1"+
            "\11\3\uffff\1\12\1\uffff\1\17\1\13\45\uffff\1\1\2\uffff\1\15",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            ""
    };

    static final short[] DFA50_eot = DFA.unpackEncodedString(DFA50_eotS);
    static final short[] DFA50_eof = DFA.unpackEncodedString(DFA50_eofS);
    static final char[] DFA50_min = DFA.unpackEncodedStringToUnsignedChars(DFA50_minS);
    static final char[] DFA50_max = DFA.unpackEncodedStringToUnsignedChars(DFA50_maxS);
    static final short[] DFA50_accept = DFA.unpackEncodedString(DFA50_acceptS);
    static final short[] DFA50_special = DFA.unpackEncodedString(DFA50_specialS);
    static final short[][] DFA50_transition;

    static {
        int numStates = DFA50_transitionS.length;
        DFA50_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA50_transition[i] = DFA.unpackEncodedString(DFA50_transitionS[i]);
        }
    }

    class DFA50 extends DFA {

        public DFA50(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 50;
            this.eot = DFA50_eot;
            this.eof = DFA50_eof;
            this.min = DFA50_min;
            this.max = DFA50_max;
            this.accept = DFA50_accept;
            this.special = DFA50_special;
            this.transition = DFA50_transition;
        }
        public String getDescription() {
            return "441:1: methodDeclaration : (constructorModifiers= modifiers ( typeParameters )? constructorIdentifier= IDENTIFIER formalParametersList= formalParameters ( 'throws' exceptionListTemp= qualifiedNameList )? '{' (explicitConstructorTemp= explicitConstructorInvocation )? (blockStatementTemp= blockStatement )* '}' | methodModifiers= modifiers ( typeParameters )? (returnTypeTemp= type | 'void' ) methodIdentifier= IDENTIFIER formalParametersList= formalParameters ( '[' ']' )* ( 'throws' exceptionListTemp= qualifiedNameList )? (blockTemp= block | ';' ) );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA50_1 = input.LA(1);

                         
                        int index50_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA50_2 = input.LA(1);

                         
                        int index50_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA50_3 = input.LA(1);

                         
                        int index50_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA50_4 = input.LA(1);

                         
                        int index50_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA50_5 = input.LA(1);

                         
                        int index50_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA50_6 = input.LA(1);

                         
                        int index50_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA50_7 = input.LA(1);

                         
                        int index50_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA50_8 = input.LA(1);

                         
                        int index50_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA50_9 = input.LA(1);

                         
                        int index50_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA50_10 = input.LA(1);

                         
                        int index50_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA50_11 = input.LA(1);

                         
                        int index50_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA50_12 = input.LA(1);

                         
                        int index50_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA50_13 = input.LA(1);

                         
                        int index50_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA50_14 = input.LA(1);

                         
                        int index50_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred59_Java1_6()) ) {s = 17;}

                        else if ( (true) ) {s = 15;}

                         
                        input.seek(index50_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 50, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA43_eotS =
        "\55\uffff";
    static final String DFA43_eofS =
        "\55\uffff";
    static final String DFA43_minS =
        "\1\4\1\uffff\10\0\43\uffff";
    static final String DFA43_maxS =
        "\1\163\1\uffff\10\0\43\uffff";
    static final String DFA43_acceptS =
        "\1\uffff\1\1\10\uffff\1\2\42\uffff";
    static final String DFA43_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\43\uffff}>";
    static final String[] DFA43_transitionS = {
            "\1\5\11\6\14\uffff\2\12\1\10\1\12\1\10\2\uffff\1\10\1\12\1"+
            "\uffff\1\12\1\uffff\1\12\1\10\1\uffff\1\12\1\uffff\1\12\1\uffff"+
            "\1\10\1\12\1\uffff\1\12\3\uffff\1\10\1\12\1\10\1\12\1\7\1\uffff"+
            "\4\12\1\10\2\12\1\4\2\12\1\2\1\12\1\uffff\2\12\1\11\2\12\1\3"+
            "\1\uffff\2\12\2\uffff\1\12\4\uffff\2\12\5\uffff\4\12\16\uffff"+
            "\1\12\2\uffff\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA43_eot = DFA.unpackEncodedString(DFA43_eotS);
    static final short[] DFA43_eof = DFA.unpackEncodedString(DFA43_eofS);
    static final char[] DFA43_min = DFA.unpackEncodedStringToUnsignedChars(DFA43_minS);
    static final char[] DFA43_max = DFA.unpackEncodedStringToUnsignedChars(DFA43_maxS);
    static final short[] DFA43_accept = DFA.unpackEncodedString(DFA43_acceptS);
    static final short[] DFA43_special = DFA.unpackEncodedString(DFA43_specialS);
    static final short[][] DFA43_transition;

    static {
        int numStates = DFA43_transitionS.length;
        DFA43_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA43_transition[i] = DFA.unpackEncodedString(DFA43_transitionS[i]);
        }
    }

    class DFA43 extends DFA {

        public DFA43(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 43;
            this.eot = DFA43_eot;
            this.eof = DFA43_eof;
            this.min = DFA43_min;
            this.max = DFA43_max;
            this.accept = DFA43_accept;
            this.special = DFA43_special;
            this.transition = DFA43_transition;
        }
        public String getDescription() {
            return "458:9: (explicitConstructorTemp= explicitConstructorInvocation )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA43_2 = input.LA(1);

                         
                        int index43_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA43_3 = input.LA(1);

                         
                        int index43_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA43_4 = input.LA(1);

                         
                        int index43_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA43_5 = input.LA(1);

                         
                        int index43_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA43_6 = input.LA(1);

                         
                        int index43_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA43_7 = input.LA(1);

                         
                        int index43_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA43_8 = input.LA(1);

                         
                        int index43_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA43_9 = input.LA(1);

                         
                        int index43_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index43_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 43, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA54_eotS =
        "\26\uffff";
    static final String DFA54_eofS =
        "\26\uffff";
    static final String DFA54_minS =
        "\1\4\16\0\7\uffff";
    static final String DFA54_maxS =
        "\1\163\16\0\7\uffff";
    static final String DFA54_acceptS =
        "\17\uffff\1\2\1\uffff\1\3\1\4\1\uffff\1\5\1\1";
    static final String DFA54_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\7\uffff}>";
    static final String[] DFA54_transitionS = {
            "\1\15\25\uffff\1\5\1\uffff\1\16\1\uffff\1\16\2\uffff\1\16\1"+
            "\22\4\uffff\1\16\1\uffff\1\22\1\uffff\1\7\1\uffff\1\16\6\uffff"+
            "\1\16\1\21\1\16\1\10\2\uffff\1\4\1\3\1\2\1\uffff\1\16\1\6\1"+
            "\14\2\uffff\1\11\3\uffff\1\12\1\uffff\1\17\1\13\7\uffff\1\24"+
            "\35\uffff\1\1\2\uffff\1\17",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA54_eot = DFA.unpackEncodedString(DFA54_eotS);
    static final short[] DFA54_eof = DFA.unpackEncodedString(DFA54_eofS);
    static final char[] DFA54_min = DFA.unpackEncodedStringToUnsignedChars(DFA54_minS);
    static final char[] DFA54_max = DFA.unpackEncodedStringToUnsignedChars(DFA54_maxS);
    static final short[] DFA54_accept = DFA.unpackEncodedString(DFA54_acceptS);
    static final short[] DFA54_special = DFA.unpackEncodedString(DFA54_specialS);
    static final short[][] DFA54_transition;

    static {
        int numStates = DFA54_transitionS.length;
        DFA54_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA54_transition[i] = DFA.unpackEncodedString(DFA54_transitionS[i]);
        }
    }

    class DFA54 extends DFA {

        public DFA54(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 54;
            this.eot = DFA54_eot;
            this.eof = DFA54_eof;
            this.min = DFA54_min;
            this.max = DFA54_max;
            this.accept = DFA54_accept;
            this.special = DFA54_special;
            this.transition = DFA54_transition;
        }
        public String getDescription() {
            return "536:1: interfaceBodyDeclaration : ( interfaceFieldDeclaration | interfaceMethodDeclaration | interfaceDeclaration | classDeclaration | ';' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA54_1 = input.LA(1);

                         
                        int index54_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA54_2 = input.LA(1);

                         
                        int index54_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA54_3 = input.LA(1);

                         
                        int index54_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA54_4 = input.LA(1);

                         
                        int index54_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA54_5 = input.LA(1);

                         
                        int index54_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA54_6 = input.LA(1);

                         
                        int index54_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA54_7 = input.LA(1);

                         
                        int index54_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA54_8 = input.LA(1);

                         
                        int index54_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA54_9 = input.LA(1);

                         
                        int index54_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA54_10 = input.LA(1);

                         
                        int index54_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA54_11 = input.LA(1);

                         
                        int index54_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA54_12 = input.LA(1);

                         
                        int index54_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                        else if ( (synpred70_Java1_6()) ) {s = 17;}

                        else if ( (synpred71_Java1_6()) ) {s = 18;}

                         
                        input.seek(index54_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA54_13 = input.LA(1);

                         
                        int index54_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                         
                        input.seek(index54_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA54_14 = input.LA(1);

                         
                        int index54_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred68_Java1_6()) ) {s = 21;}

                        else if ( (synpred69_Java1_6()) ) {s = 15;}

                         
                        input.seek(index54_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 54, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA77_eotS =
        "\12\uffff";
    static final String DFA77_eofS =
        "\12\uffff";
    static final String DFA77_minS =
        "\1\4\1\uffff\1\0\1\uffff\1\0\5\uffff";
    static final String DFA77_maxS =
        "\1\163\1\uffff\1\0\1\uffff\1\0\5\uffff";
    static final String DFA77_acceptS =
        "\1\uffff\1\1\1\uffff\1\2\6\uffff";
    static final String DFA77_specialS =
        "\2\uffff\1\0\1\uffff\1\1\5\uffff}>";
    static final String[] DFA77_transitionS = {
            "\12\3\16\uffff\1\3\1\uffff\1\3\2\uffff\1\3\5\uffff\1\3\5\uffff"+
            "\1\3\6\uffff\1\3\1\uffff\1\3\1\uffff\1\3\5\uffff\1\3\2\uffff"+
            "\1\4\2\uffff\1\2\4\uffff\1\3\2\uffff\1\3\46\uffff\1\1",
            "",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA77_eot = DFA.unpackEncodedString(DFA77_eotS);
    static final short[] DFA77_eof = DFA.unpackEncodedString(DFA77_eofS);
    static final char[] DFA77_min = DFA.unpackEncodedStringToUnsignedChars(DFA77_minS);
    static final char[] DFA77_max = DFA.unpackEncodedStringToUnsignedChars(DFA77_maxS);
    static final short[] DFA77_accept = DFA.unpackEncodedString(DFA77_acceptS);
    static final short[] DFA77_special = DFA.unpackEncodedString(DFA77_specialS);
    static final short[][] DFA77_transition;

    static {
        int numStates = DFA77_transitionS.length;
        DFA77_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA77_transition[i] = DFA.unpackEncodedString(DFA77_transitionS[i]);
        }
    }

    class DFA77 extends DFA {

        public DFA77(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 77;
            this.eot = DFA77_eot;
            this.eof = DFA77_eof;
            this.min = DFA77_min;
            this.max = DFA77_max;
            this.accept = DFA77_accept;
            this.special = DFA77_special;
            this.transition = DFA77_transition;
        }
        public String getDescription() {
            return "703:1: explicitConstructorInvocation : ( ( nonWildcardTypeArguments )? ( 'this' | 'super' ) arguments ';' | primary '.' ( nonWildcardTypeArguments )? 'super' arguments ';' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA77_2 = input.LA(1);

                         
                        int index77_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index77_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA77_4 = input.LA(1);

                         
                        int index77_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred103_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 3;}

                         
                        input.seek(index77_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 77, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA88_eotS =
        "\26\uffff";
    static final String DFA88_eofS =
        "\26\uffff";
    static final String DFA88_minS =
        "\1\4\16\0\7\uffff";
    static final String DFA88_maxS =
        "\1\160\16\0\7\uffff";
    static final String DFA88_acceptS =
        "\17\uffff\1\3\1\4\1\5\1\7\1\1\1\2\1\6";
    static final String DFA88_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1"+
        "\14\1\15\7\uffff}>";
    static final String[] DFA88_transitionS = {
            "\1\15\25\uffff\1\5\1\uffff\1\16\1\uffff\1\16\2\uffff\1\16\1"+
            "\17\4\uffff\1\16\1\uffff\1\21\1\uffff\1\7\1\uffff\1\16\6\uffff"+
            "\1\16\1\20\1\16\1\10\2\uffff\1\4\1\3\1\2\1\uffff\1\16\1\6\1"+
            "\14\2\uffff\1\11\3\uffff\1\12\2\uffff\1\13\7\uffff\1\22\35\uffff"+
            "\1\1",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA88_eot = DFA.unpackEncodedString(DFA88_eotS);
    static final short[] DFA88_eof = DFA.unpackEncodedString(DFA88_eofS);
    static final char[] DFA88_min = DFA.unpackEncodedStringToUnsignedChars(DFA88_minS);
    static final char[] DFA88_max = DFA.unpackEncodedStringToUnsignedChars(DFA88_maxS);
    static final short[] DFA88_accept = DFA.unpackEncodedString(DFA88_acceptS);
    static final short[] DFA88_special = DFA.unpackEncodedString(DFA88_specialS);
    static final short[][] DFA88_transition;

    static {
        int numStates = DFA88_transitionS.length;
        DFA88_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA88_transition[i] = DFA.unpackEncodedString(DFA88_transitionS[i]);
        }
    }

    class DFA88 extends DFA {

        public DFA88(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 88;
            this.eot = DFA88_eot;
            this.eof = DFA88_eof;
            this.min = DFA88_min;
            this.max = DFA88_max;
            this.accept = DFA88_accept;
            this.special = DFA88_special;
            this.transition = DFA88_transition;
        }
        public String getDescription() {
            return "802:1: annotationTypeElementDeclaration : ( annotationMethodDeclaration | interfaceFieldDeclaration | normalClassDeclaration | normalInterfaceDeclaration | enumDeclaration | annotationTypeDeclaration | ';' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA88_1 = input.LA(1);

                         
                        int index88_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA88_2 = input.LA(1);

                         
                        int index88_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA88_3 = input.LA(1);

                         
                        int index88_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA88_4 = input.LA(1);

                         
                        int index88_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA88_5 = input.LA(1);

                         
                        int index88_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_5);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA88_6 = input.LA(1);

                         
                        int index88_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_6);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA88_7 = input.LA(1);

                         
                        int index88_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_7);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA88_8 = input.LA(1);

                         
                        int index88_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_8);
                        if ( s>=0 ) return s;
                        break;
                    case 8 : 
                        int LA88_9 = input.LA(1);

                         
                        int index88_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_9);
                        if ( s>=0 ) return s;
                        break;
                    case 9 : 
                        int LA88_10 = input.LA(1);

                         
                        int index88_10 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_10);
                        if ( s>=0 ) return s;
                        break;
                    case 10 : 
                        int LA88_11 = input.LA(1);

                         
                        int index88_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_11);
                        if ( s>=0 ) return s;
                        break;
                    case 11 : 
                        int LA88_12 = input.LA(1);

                         
                        int index88_12 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                        else if ( (synpred119_Java1_6()) ) {s = 15;}

                        else if ( (synpred120_Java1_6()) ) {s = 16;}

                        else if ( (synpred121_Java1_6()) ) {s = 17;}

                        else if ( (synpred122_Java1_6()) ) {s = 21;}

                         
                        input.seek(index88_12);
                        if ( s>=0 ) return s;
                        break;
                    case 12 : 
                        int LA88_13 = input.LA(1);

                         
                        int index88_13 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                         
                        input.seek(index88_13);
                        if ( s>=0 ) return s;
                        break;
                    case 13 : 
                        int LA88_14 = input.LA(1);

                         
                        int index88_14 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred117_Java1_6()) ) {s = 19;}

                        else if ( (synpred118_Java1_6()) ) {s = 20;}

                         
                        input.seek(index88_14);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 88, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA91_eotS =
        "\54\uffff";
    static final String DFA91_eofS =
        "\54\uffff";
    static final String DFA91_minS =
        "\1\4\4\0\6\uffff\1\0\40\uffff";
    static final String DFA91_maxS =
        "\1\160\4\0\6\uffff\1\0\40\uffff";
    static final String DFA91_acceptS =
        "\5\uffff\1\2\14\uffff\1\3\30\uffff\1\1";
    static final String DFA91_specialS =
        "\1\uffff\1\0\1\1\1\2\1\3\6\uffff\1\4\40\uffff}>";
    static final String[] DFA91_transitionS = {
            "\1\3\11\22\14\uffff\1\5\1\22\1\4\1\22\1\4\2\uffff\1\4\1\5\1"+
            "\uffff\1\22\1\uffff\1\22\1\4\1\uffff\1\5\1\uffff\1\1\1\uffff"+
            "\1\4\1\22\1\uffff\1\22\3\uffff\1\4\1\5\1\4\1\5\1\22\1\uffff"+
            "\3\5\1\22\1\4\2\5\2\22\1\13\2\22\1\uffff\1\5\2\22\1\5\2\22\1"+
            "\uffff\1\22\3\uffff\1\22\4\uffff\2\22\5\uffff\4\22\16\uffff"+
            "\1\2",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA91_eot = DFA.unpackEncodedString(DFA91_eotS);
    static final short[] DFA91_eof = DFA.unpackEncodedString(DFA91_eofS);
    static final char[] DFA91_min = DFA.unpackEncodedStringToUnsignedChars(DFA91_minS);
    static final char[] DFA91_max = DFA.unpackEncodedStringToUnsignedChars(DFA91_maxS);
    static final short[] DFA91_accept = DFA.unpackEncodedString(DFA91_acceptS);
    static final short[] DFA91_special = DFA.unpackEncodedString(DFA91_specialS);
    static final short[][] DFA91_transition;

    static {
        int numStates = DFA91_transitionS.length;
        DFA91_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA91_transition[i] = DFA.unpackEncodedString(DFA91_transitionS[i]);
        }
    }

    class DFA91 extends DFA {

        public DFA91(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 91;
            this.eot = DFA91_eot;
            this.eof = DFA91_eof;
            this.min = DFA91_min;
            this.max = DFA91_max;
            this.accept = DFA91_accept;
            this.special = DFA91_special;
            this.transition = DFA91_transition;
        }
        public String getDescription() {
            return "859:1: blockStatement : ( localVariableDeclarationStatement | classOrInterfaceDeclaration | statement );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA91_1 = input.LA(1);

                         
                        int index91_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred125_Java1_6()) ) {s = 43;}

                        else if ( (synpred126_Java1_6()) ) {s = 5;}

                         
                        input.seek(index91_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA91_2 = input.LA(1);

                         
                        int index91_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred125_Java1_6()) ) {s = 43;}

                        else if ( (synpred126_Java1_6()) ) {s = 5;}

                         
                        input.seek(index91_2);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA91_3 = input.LA(1);

                         
                        int index91_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred125_Java1_6()) ) {s = 43;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index91_3);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA91_4 = input.LA(1);

                         
                        int index91_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred125_Java1_6()) ) {s = 43;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index91_4);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA91_11 = input.LA(1);

                         
                        int index91_11 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred126_Java1_6()) ) {s = 5;}

                        else if ( (true) ) {s = 18;}

                         
                        input.seek(index91_11);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 91, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA99_eotS =
        "\40\uffff";
    static final String DFA99_eofS =
        "\40\uffff";
    static final String DFA99_minS =
        "\1\4\1\uffff\1\0\23\uffff\1\0\11\uffff";
    static final String DFA99_maxS =
        "\1\141\1\uffff\1\0\23\uffff\1\0\11\uffff";
    static final String DFA99_acceptS =
        "\1\uffff\1\1\1\uffff\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13\1\14\1"+
        "\15\1\16\1\17\15\uffff\1\21\1\2\1\3\1\20";
    static final String DFA99_specialS =
        "\2\uffff\1\0\23\uffff\1\1\11\uffff}>";
    static final String[] DFA99_transitionS = {
            "\1\26\11\16\15\uffff\1\2\1\16\1\14\1\16\2\uffff\1\16\2\uffff"+
            "\1\15\1\uffff\1\6\1\16\5\uffff\1\16\1\4\1\uffff\1\3\3\uffff"+
            "\1\16\1\uffff\1\16\1\uffff\1\16\4\uffff\1\12\1\16\2\uffff\1"+
            "\16\1\10\1\11\1\16\1\13\2\uffff\1\7\1\16\1\uffff\1\5\1\16\1"+
            "\uffff\1\1\3\uffff\1\34\4\uffff\2\16\5\uffff\4\16",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA99_eot = DFA.unpackEncodedString(DFA99_eotS);
    static final short[] DFA99_eof = DFA.unpackEncodedString(DFA99_eofS);
    static final char[] DFA99_min = DFA.unpackEncodedStringToUnsignedChars(DFA99_minS);
    static final char[] DFA99_max = DFA.unpackEncodedStringToUnsignedChars(DFA99_maxS);
    static final short[] DFA99_accept = DFA.unpackEncodedString(DFA99_acceptS);
    static final short[] DFA99_special = DFA.unpackEncodedString(DFA99_specialS);
    static final short[][] DFA99_transition;

    static {
        int numStates = DFA99_transitionS.length;
        DFA99_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA99_transition[i] = DFA.unpackEncodedString(DFA99_transitionS[i]);
        }
    }

    class DFA99 extends DFA {

        public DFA99(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 99;
            this.eot = DFA99_eot;
            this.eof = DFA99_eof;
            this.min = DFA99_min;
            this.max = DFA99_max;
            this.accept = DFA99_accept;
            this.special = DFA99_special;
            this.transition = DFA99_transition;
        }
        public String getDescription() {
            return "878:1: statement : ( block | ( 'assert' ) expression ( ':' expression )? ';' | 'assert' expression ( ':' expression )? ';' | 'if' parExpression statement ( 'else' statement )? | forstatement | 'while' parExpression statement | 'do' statement 'while' parExpression ';' | trystatement | 'switch' parExpression '{' switchBlockStatementGroups '}' | 'synchronized' parExpression block | 'return' ( expression )? ';' | 'throw' expression ';' | 'break' ( IDENTIFIER )? ';' | 'continue' ( IDENTIFIER )? ';' | expression ';' | IDENTIFIER ':' statement | ';' );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA99_2 = input.LA(1);

                         
                        int index99_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred130_Java1_6()) ) {s = 29;}

                        else if ( (synpred132_Java1_6()) ) {s = 30;}

                         
                        input.seek(index99_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA99_22 = input.LA(1);

                         
                        int index99_22 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred148_Java1_6()) ) {s = 14;}

                        else if ( (synpred149_Java1_6()) ) {s = 31;}

                         
                        input.seek(index99_22);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 99, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA110_eotS =
        "\21\uffff";
    static final String DFA110_eofS =
        "\21\uffff";
    static final String DFA110_minS =
        "\1\4\2\uffff\2\0\14\uffff";
    static final String DFA110_maxS =
        "\1\160\2\uffff\2\0\14\uffff";
    static final String DFA110_acceptS =
        "\1\uffff\1\1\3\uffff\1\2\13\uffff";
    static final String DFA110_specialS =
        "\3\uffff\1\0\1\1\14\uffff}>";
    static final String[] DFA110_transitionS = {
            "\1\3\11\5\16\uffff\1\4\1\uffff\1\4\2\uffff\1\4\5\uffff\1\4"+
            "\3\uffff\1\1\1\uffff\1\4\6\uffff\1\4\1\uffff\1\4\1\uffff\1\5"+
            "\5\uffff\1\4\2\uffff\1\5\2\uffff\1\5\4\uffff\1\5\2\uffff\1\5"+
            "\12\uffff\2\5\5\uffff\4\5\16\uffff\1\1",
            "",
            "",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA110_eot = DFA.unpackEncodedString(DFA110_eotS);
    static final short[] DFA110_eof = DFA.unpackEncodedString(DFA110_eofS);
    static final char[] DFA110_min = DFA.unpackEncodedStringToUnsignedChars(DFA110_minS);
    static final char[] DFA110_max = DFA.unpackEncodedStringToUnsignedChars(DFA110_maxS);
    static final short[] DFA110_accept = DFA.unpackEncodedString(DFA110_acceptS);
    static final short[] DFA110_special = DFA.unpackEncodedString(DFA110_specialS);
    static final short[][] DFA110_transition;

    static {
        int numStates = DFA110_transitionS.length;
        DFA110_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA110_transition[i] = DFA.unpackEncodedString(DFA110_transitionS[i]);
        }
    }

    class DFA110 extends DFA {

        public DFA110(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 110;
            this.eot = DFA110_eot;
            this.eof = DFA110_eof;
            this.min = DFA110_min;
            this.max = DFA110_max;
            this.accept = DFA110_accept;
            this.special = DFA110_special;
            this.transition = DFA110_transition;
        }
        public String getDescription() {
            return "964:1: forInit : ( localVariableDeclaration | expressionList );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA110_3 = input.LA(1);

                         
                        int index110_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred161_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index110_3);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA110_4 = input.LA(1);

                         
                        int index110_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred161_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 5;}

                         
                        input.seek(index110_4);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 110, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA113_eotS =
        "\17\uffff";
    static final String DFA113_eofS =
        "\17\uffff";
    static final String DFA113_minS =
        "\1\126\12\uffff\1\162\1\126\2\uffff";
    static final String DFA113_maxS =
        "\1\163\12\uffff\2\162\2\uffff";
    static final String DFA113_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\12\2\uffff\1\13"+
        "\1\14";
    static final String DFA113_specialS =
        "\17\uffff}>";
    static final String[] DFA113_transitionS = {
            "\1\1\21\uffff\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\2\uffff\1\13"+
            "\1\12",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\14",
            "\1\16\33\uffff\1\15",
            "",
            ""
    };

    static final short[] DFA113_eot = DFA.unpackEncodedString(DFA113_eotS);
    static final short[] DFA113_eof = DFA.unpackEncodedString(DFA113_eofS);
    static final char[] DFA113_min = DFA.unpackEncodedStringToUnsignedChars(DFA113_minS);
    static final char[] DFA113_max = DFA.unpackEncodedStringToUnsignedChars(DFA113_maxS);
    static final short[] DFA113_accept = DFA.unpackEncodedString(DFA113_acceptS);
    static final short[] DFA113_special = DFA.unpackEncodedString(DFA113_specialS);
    static final short[][] DFA113_transition;

    static {
        int numStates = DFA113_transitionS.length;
        DFA113_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA113_transition[i] = DFA.unpackEncodedString(DFA113_transitionS[i]);
        }
    }

    class DFA113 extends DFA {

        public DFA113(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 113;
            this.eot = DFA113_eot;
            this.eof = DFA113_eof;
            this.min = DFA113_min;
            this.max = DFA113_max;
            this.accept = DFA113_accept;
            this.special = DFA113_special;
            this.transition = DFA113_transition;
        }
        public String getDescription() {
            return "987:1: assignmentOperator : ( '=' | '+=' | '-=' | '*=' | '/=' | '&=' | '|=' | '^=' | '%=' | '<' '<' '=' | '>' '>' '>' '=' | '>' '>' '=' );";
        }
    }
    static final String DFA131_eotS =
        "\14\uffff";
    static final String DFA131_eofS =
        "\14\uffff";
    static final String DFA131_minS =
        "\1\4\2\uffff\1\0\10\uffff";
    static final String DFA131_maxS =
        "\1\130\2\uffff\1\0\10\uffff";
    static final String DFA131_acceptS =
        "\1\uffff\1\1\1\2\1\uffff\1\4\6\uffff\1\3";
    static final String DFA131_specialS =
        "\3\uffff\1\0\10\uffff}>";
    static final String[] DFA131_transitionS = {
            "\12\4\16\uffff\1\4\1\uffff\1\4\2\uffff\1\4\5\uffff\1\4\5\uffff"+
            "\1\4\6\uffff\1\4\1\uffff\1\4\1\uffff\1\4\5\uffff\1\4\2\uffff"+
            "\1\4\2\uffff\1\4\4\uffff\1\4\2\uffff\1\3\12\uffff\1\2\1\1",
            "",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA131_eot = DFA.unpackEncodedString(DFA131_eotS);
    static final short[] DFA131_eof = DFA.unpackEncodedString(DFA131_eofS);
    static final char[] DFA131_min = DFA.unpackEncodedStringToUnsignedChars(DFA131_minS);
    static final char[] DFA131_max = DFA.unpackEncodedStringToUnsignedChars(DFA131_maxS);
    static final short[] DFA131_accept = DFA.unpackEncodedString(DFA131_acceptS);
    static final short[] DFA131_special = DFA.unpackEncodedString(DFA131_specialS);
    static final short[][] DFA131_transition;

    static {
        int numStates = DFA131_transitionS.length;
        DFA131_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA131_transition[i] = DFA.unpackEncodedString(DFA131_transitionS[i]);
        }
    }

    class DFA131 extends DFA {

        public DFA131(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 131;
            this.eot = DFA131_eot;
            this.eof = DFA131_eof;
            this.min = DFA131_min;
            this.max = DFA131_max;
            this.accept = DFA131_accept;
            this.special = DFA131_special;
            this.transition = DFA131_transition;
        }
        public String getDescription() {
            return "1116:1: unaryExpressionNotPlusMinus : ( '~' unaryExpression | '!' unaryExpression | castExpression | primary ( selector )* ( '++' | '--' )? );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA131_3 = input.LA(1);

                         
                        int index131_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred202_Java1_6()) ) {s = 11;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index131_3);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 131, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA134_eotS =
        "\41\uffff";
    static final String DFA134_eofS =
        "\1\4\40\uffff";
    static final String DFA134_minS =
        "\1\63\1\0\1\uffff\1\0\35\uffff";
    static final String DFA134_maxS =
        "\1\163\1\0\1\uffff\1\0\35\uffff";
    static final String DFA134_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\34\uffff";
    static final String DFA134_specialS =
        "\1\uffff\1\0\1\uffff\1\1\35\uffff}>";
    static final String[] DFA134_transitionS = {
            "\1\4\30\uffff\1\2\1\4\1\uffff\1\4\1\1\3\4\1\3\1\uffff\1\4\2"+
            "\uffff\27\4\1\uffff\3\4",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA134_eot = DFA.unpackEncodedString(DFA134_eotS);
    static final short[] DFA134_eof = DFA.unpackEncodedString(DFA134_eofS);
    static final char[] DFA134_min = DFA.unpackEncodedStringToUnsignedChars(DFA134_minS);
    static final char[] DFA134_max = DFA.unpackEncodedStringToUnsignedChars(DFA134_maxS);
    static final short[] DFA134_accept = DFA.unpackEncodedString(DFA134_acceptS);
    static final short[] DFA134_special = DFA.unpackEncodedString(DFA134_specialS);
    static final short[][] DFA134_transition;

    static {
        int numStates = DFA134_transitionS.length;
        DFA134_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA134_transition[i] = DFA.unpackEncodedString(DFA134_transitionS[i]);
        }
    }

    class DFA134 extends DFA {

        public DFA134(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 134;
            this.eot = DFA134_eot;
            this.eof = DFA134_eof;
            this.min = DFA134_min;
            this.max = DFA134_max;
            this.accept = DFA134_accept;
            this.special = DFA134_special;
            this.transition = DFA134_transition;
        }
        public String getDescription() {
            return "1141:9: ( identifierSuffix )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA134_1 = input.LA(1);

                         
                        int index134_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred209_Java1_6()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index134_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA134_3 = input.LA(1);

                         
                        int index134_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred209_Java1_6()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index134_3);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 134, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA136_eotS =
        "\41\uffff";
    static final String DFA136_eofS =
        "\1\4\40\uffff";
    static final String DFA136_minS =
        "\1\63\1\0\1\uffff\1\0\35\uffff";
    static final String DFA136_maxS =
        "\1\163\1\0\1\uffff\1\0\35\uffff";
    static final String DFA136_acceptS =
        "\2\uffff\1\1\1\uffff\1\2\34\uffff";
    static final String DFA136_specialS =
        "\1\uffff\1\0\1\uffff\1\1\35\uffff}>";
    static final String[] DFA136_transitionS = {
            "\1\4\30\uffff\1\2\1\4\1\uffff\1\4\1\1\3\4\1\3\1\uffff\1\4\2"+
            "\uffff\27\4\1\uffff\3\4",
            "\1\uffff",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA136_eot = DFA.unpackEncodedString(DFA136_eotS);
    static final short[] DFA136_eof = DFA.unpackEncodedString(DFA136_eofS);
    static final char[] DFA136_min = DFA.unpackEncodedStringToUnsignedChars(DFA136_minS);
    static final char[] DFA136_max = DFA.unpackEncodedStringToUnsignedChars(DFA136_maxS);
    static final short[] DFA136_accept = DFA.unpackEncodedString(DFA136_acceptS);
    static final short[] DFA136_special = DFA.unpackEncodedString(DFA136_specialS);
    static final short[][] DFA136_transition;

    static {
        int numStates = DFA136_transitionS.length;
        DFA136_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA136_transition[i] = DFA.unpackEncodedString(DFA136_transitionS[i]);
        }
    }

    class DFA136 extends DFA {

        public DFA136(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 136;
            this.eot = DFA136_eot;
            this.eof = DFA136_eof;
            this.min = DFA136_min;
            this.max = DFA136_max;
            this.accept = DFA136_accept;
            this.special = DFA136_special;
            this.transition = DFA136_transition;
        }
        public String getDescription() {
            return "1146:9: ( identifierSuffix )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA136_1 = input.LA(1);

                         
                        int index136_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred212_Java1_6()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index136_1);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA136_3 = input.LA(1);

                         
                        int index136_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred212_Java1_6()) ) {s = 2;}

                        else if ( (true) ) {s = 4;}

                         
                        input.seek(index136_3);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 136, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA144_eotS =
        "\13\uffff";
    static final String DFA144_eofS =
        "\13\uffff";
    static final String DFA144_minS =
        "\1\114\1\4\1\uffff\1\42\7\uffff";
    static final String DFA144_maxS =
        "\1\124\1\141\1\uffff\1\163\7\uffff";
    static final String DFA144_acceptS =
        "\2\uffff\1\3\1\uffff\1\1\1\2\1\4\1\6\1\7\1\10\1\5";
    static final String DFA144_specialS =
        "\13\uffff}>";
    static final String[] DFA144_transitionS = {
            "\1\2\3\uffff\1\1\3\uffff\1\3",
            "\12\5\16\uffff\1\5\1\uffff\1\5\2\uffff\1\5\5\uffff\1\5\5\uffff"+
            "\1\5\6\uffff\1\5\1\uffff\1\5\1\uffff\1\5\5\uffff\1\5\2\uffff"+
            "\1\5\2\uffff\1\5\4\uffff\1\5\2\uffff\1\5\4\uffff\1\4\5\uffff"+
            "\2\5\5\uffff\4\5",
            "",
            "\1\6\25\uffff\1\11\10\uffff\1\10\2\uffff\1\7\56\uffff\1\12",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA144_eot = DFA.unpackEncodedString(DFA144_eotS);
    static final short[] DFA144_eof = DFA.unpackEncodedString(DFA144_eofS);
    static final char[] DFA144_min = DFA.unpackEncodedStringToUnsignedChars(DFA144_minS);
    static final char[] DFA144_max = DFA.unpackEncodedStringToUnsignedChars(DFA144_maxS);
    static final short[] DFA144_accept = DFA.unpackEncodedString(DFA144_acceptS);
    static final short[] DFA144_special = DFA.unpackEncodedString(DFA144_specialS);
    static final short[][] DFA144_transition;

    static {
        int numStates = DFA144_transitionS.length;
        DFA144_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA144_transition[i] = DFA.unpackEncodedString(DFA144_transitionS[i]);
        }
    }

    class DFA144 extends DFA {

        public DFA144(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 144;
            this.eot = DFA144_eot;
            this.eof = DFA144_eof;
            this.min = DFA144_min;
            this.max = DFA144_max;
            this.accept = DFA144_accept;
            this.special = DFA144_special;
            this.transition = DFA144_transition;
        }
        public String getDescription() {
            return "1170:1: identifierSuffix : ( ( '[' ']' )+ '.' 'class' | ( '[' expression ']' )+ | arguments | '.' 'class' | '.' nonWildcardTypeArguments IDENTIFIER arguments | '.' 'this' | '.' 'super' arguments | innerCreator );";
        }
    }
    static final String DFA143_eotS =
        "\41\uffff";
    static final String DFA143_eofS =
        "\1\1\40\uffff";
    static final String DFA143_minS =
        "\1\63\1\uffff\1\0\36\uffff";
    static final String DFA143_maxS =
        "\1\163\1\uffff\1\0\36\uffff";
    static final String DFA143_acceptS =
        "\1\uffff\1\2\36\uffff\1\1";
    static final String DFA143_specialS =
        "\2\uffff\1\0\36\uffff}>";
    static final String[] DFA143_transitionS = {
            "\1\1\31\uffff\1\1\1\uffff\1\1\1\2\4\1\1\uffff\1\1\2\uffff\27"+
            "\1\1\uffff\3\1",
            "",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA143_eot = DFA.unpackEncodedString(DFA143_eotS);
    static final short[] DFA143_eof = DFA.unpackEncodedString(DFA143_eofS);
    static final char[] DFA143_min = DFA.unpackEncodedStringToUnsignedChars(DFA143_minS);
    static final char[] DFA143_max = DFA.unpackEncodedStringToUnsignedChars(DFA143_maxS);
    static final short[] DFA143_accept = DFA.unpackEncodedString(DFA143_acceptS);
    static final short[] DFA143_special = DFA.unpackEncodedString(DFA143_specialS);
    static final short[][] DFA143_transition;

    static {
        int numStates = DFA143_transitionS.length;
        DFA143_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA143_transition[i] = DFA.unpackEncodedString(DFA143_transitionS[i]);
        }
    }

    class DFA143 extends DFA {

        public DFA143(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 143;
            this.eot = DFA143_eot;
            this.eof = DFA143_eof;
            this.min = DFA143_min;
            this.max = DFA143_max;
            this.accept = DFA143_accept;
            this.special = DFA143_special;
            this.transition = DFA143_transition;
        }
        public String getDescription() {
            return "()+ loopback of 1174:9: ( '[' expression ']' )+";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA143_2 = input.LA(1);

                         
                        int index143_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred224_Java1_6()) ) {s = 32;}

                        else if ( (true) ) {s = 1;}

                         
                        input.seek(index143_2);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 143, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA149_eotS =
        "\41\uffff";
    static final String DFA149_eofS =
        "\1\2\40\uffff";
    static final String DFA149_minS =
        "\1\63\1\0\37\uffff";
    static final String DFA149_maxS =
        "\1\163\1\0\37\uffff";
    static final String DFA149_acceptS =
        "\2\uffff\1\2\35\uffff\1\1";
    static final String DFA149_specialS =
        "\1\uffff\1\0\37\uffff}>";
    static final String[] DFA149_transitionS = {
            "\1\2\31\uffff\1\2\1\uffff\1\2\1\1\4\2\1\uffff\1\2\2\uffff\27"+
            "\2\1\uffff\3\2",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA149_eot = DFA.unpackEncodedString(DFA149_eotS);
    static final short[] DFA149_eof = DFA.unpackEncodedString(DFA149_eofS);
    static final char[] DFA149_min = DFA.unpackEncodedStringToUnsignedChars(DFA149_minS);
    static final char[] DFA149_max = DFA.unpackEncodedStringToUnsignedChars(DFA149_maxS);
    static final short[] DFA149_accept = DFA.unpackEncodedString(DFA149_acceptS);
    static final short[] DFA149_special = DFA.unpackEncodedString(DFA149_specialS);
    static final short[][] DFA149_transition;

    static {
        int numStates = DFA149_transitionS.length;
        DFA149_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA149_transition[i] = DFA.unpackEncodedString(DFA149_transitionS[i]);
        }
    }

    class DFA149 extends DFA {

        public DFA149(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 149;
            this.eot = DFA149_eot;
            this.eof = DFA149_eof;
            this.min = DFA149_min;
            this.max = DFA149_max;
            this.accept = DFA149_accept;
            this.special = DFA149_special;
            this.transition = DFA149_transition;
        }
        public String getDescription() {
            return "()* loopback of 1212:9: ( '[' expression ']' )*";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA149_1 = input.LA(1);

                         
                        int index149_1 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred240_Java1_6()) ) {s = 32;}

                        else if ( (true) ) {s = 2;}

                         
                        input.seek(index149_1);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 149, _s, input);
            error(nvae);
            throw nvae;
        }
    }
    static final String DFA171_eotS =
        "\55\uffff";
    static final String DFA171_eofS =
        "\55\uffff";
    static final String DFA171_minS =
        "\1\4\1\uffff\10\0\43\uffff";
    static final String DFA171_maxS =
        "\1\163\1\uffff\10\0\43\uffff";
    static final String DFA171_acceptS =
        "\1\uffff\1\1\10\uffff\1\2\42\uffff";
    static final String DFA171_specialS =
        "\2\uffff\1\0\1\1\1\2\1\3\1\4\1\5\1\6\1\7\43\uffff}>";
    static final String[] DFA171_transitionS = {
            "\1\5\11\6\14\uffff\2\12\1\10\1\12\1\10\2\uffff\1\10\1\12\1"+
            "\uffff\1\12\1\uffff\1\12\1\10\1\uffff\1\12\1\uffff\1\12\1\uffff"+
            "\1\10\1\12\1\uffff\1\12\3\uffff\1\10\1\12\1\10\1\12\1\7\1\uffff"+
            "\4\12\1\10\2\12\1\4\2\12\1\2\1\12\1\uffff\2\12\1\11\2\12\1\3"+
            "\1\uffff\2\12\2\uffff\1\12\4\uffff\2\12\5\uffff\4\12\16\uffff"+
            "\1\12\2\uffff\1\1",
            "",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "\1\uffff",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA171_eot = DFA.unpackEncodedString(DFA171_eotS);
    static final short[] DFA171_eof = DFA.unpackEncodedString(DFA171_eofS);
    static final char[] DFA171_min = DFA.unpackEncodedStringToUnsignedChars(DFA171_minS);
    static final char[] DFA171_max = DFA.unpackEncodedStringToUnsignedChars(DFA171_maxS);
    static final short[] DFA171_accept = DFA.unpackEncodedString(DFA171_acceptS);
    static final short[] DFA171_special = DFA.unpackEncodedString(DFA171_specialS);
    static final short[][] DFA171_transition;

    static {
        int numStates = DFA171_transitionS.length;
        DFA171_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA171_transition[i] = DFA.unpackEncodedString(DFA171_transitionS[i]);
        }
    }

    class DFA171 extends DFA {

        public DFA171(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 171;
            this.eot = DFA171_eot;
            this.eof = DFA171_eof;
            this.min = DFA171_min;
            this.max = DFA171_max;
            this.accept = DFA171_accept;
            this.special = DFA171_special;
            this.transition = DFA171_transition;
        }
        public String getDescription() {
            return "458:9: (explicitConstructorTemp= explicitConstructorInvocation )?";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            TokenStream input = (TokenStream)_input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA171_2 = input.LA(1);

                         
                        int index171_2 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_2);
                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA171_3 = input.LA(1);

                         
                        int index171_3 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_3);
                        if ( s>=0 ) return s;
                        break;
                    case 2 : 
                        int LA171_4 = input.LA(1);

                         
                        int index171_4 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_4);
                        if ( s>=0 ) return s;
                        break;
                    case 3 : 
                        int LA171_5 = input.LA(1);

                         
                        int index171_5 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_5);
                        if ( s>=0 ) return s;
                        break;
                    case 4 : 
                        int LA171_6 = input.LA(1);

                         
                        int index171_6 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_6);
                        if ( s>=0 ) return s;
                        break;
                    case 5 : 
                        int LA171_7 = input.LA(1);

                         
                        int index171_7 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_7);
                        if ( s>=0 ) return s;
                        break;
                    case 6 : 
                        int LA171_8 = input.LA(1);

                         
                        int index171_8 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_8);
                        if ( s>=0 ) return s;
                        break;
                    case 7 : 
                        int LA171_9 = input.LA(1);

                         
                        int index171_9 = input.index();
                        input.rewind();
                        s = -1;
                        if ( (synpred57_Java1_6()) ) {s = 1;}

                        else if ( (true) ) {s = 10;}

                         
                        input.seek(index171_9);
                        if ( s>=0 ) return s;
                        break;
            }
            if (state.backtracking>0) {state.failed=true; return -1;}
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 171, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

    public static final BitSet FOLLOW_annotations_in_compilationUnit87 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_packageDeclaration_in_compilationUnit91 = new BitSet(new long[]{0x9CA40A0404000002L,0x0001000000040489L});
    public static final BitSet FOLLOW_importDeclaration_in_compilationUnit99 = new BitSet(new long[]{0x9CA40A0404000002L,0x0001000000040489L});
    public static final BitSet FOLLOW_typeDeclaration_in_compilationUnit106 = new BitSet(new long[]{0x9CA00A0404000002L,0x0001000000040489L});
    public static final BitSet FOLLOW_PACKAGE_in_packageDeclaration121 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_packageDeclaration128 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_packageDeclaration139 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration160 = new BitSet(new long[]{0x8000000000000010L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration163 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration169 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration170 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration172 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration174 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IMPORT_in_importDeclaration192 = new BitSet(new long[]{0x8000000000000010L});
    public static final BitSet FOLLOW_STATIC_in_importDeclaration195 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration201 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration212 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_importDeclaration216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000140000L});
    public static final BitSet FOLLOW_DOT_in_importDeclaration240 = new BitSet(new long[]{0x0000000000000000L,0x0000000400000000L});
    public static final BitSet FOLLOW_STAR_in_importDeclaration242 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_importDeclaration248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName266 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_qualifiedImportName274 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedImportName276 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_typeDeclaration292 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_typeDeclaration297 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_classOrInterfaceDeclaration311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_classOrInterfaceDeclaration321 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_modifier345 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PUBLIC_in_modifier350 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PROTECTED_in_modifier357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PRIVATE_in_modifier364 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ABSTRACT_in_modifier371 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATIC_in_modifier378 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FINAL_in_modifier385 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NATIVE_in_modifier393 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_modifier400 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRANSIENT_in_modifier407 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOLATILE_in_modifier414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRICTFP_in_modifier421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifier_in_modifiers445 = new BitSet(new long[]{0x9C80080004000002L,0x0001000000000489L});
    public static final BitSet FOLLOW_FINAL_in_variableModifiers464 = new BitSet(new long[]{0x0000080000000002L,0x0001000000000000L});
    public static final BitSet FOLLOW_annotation_in_variableModifiers472 = new BitSet(new long[]{0x0000080000000002L,0x0001000000000000L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_classDeclaration499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_classDeclaration505 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalClassDeclaration529 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_normalClassDeclaration535 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalClassDeclaration542 = new BitSet(new long[]{0x0002040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_typeParameters_in_normalClassDeclaration550 = new BitSet(new long[]{0x0002040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_EXTENDS_in_normalClassDeclaration561 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_normalClassDeclaration565 = new BitSet(new long[]{0x0002040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_normalClassDeclaration578 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_typeList_in_normalClassDeclaration582 = new BitSet(new long[]{0x0002040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_classBody_in_normalClassDeclaration597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeParameters623 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters631 = new BitSet(new long[]{0x0000000000000000L,0x0004000000080000L});
    public static final BitSet FOLLOW_COMMA_in_typeParameters640 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_typeParameter_in_typeParameters644 = new BitSet(new long[]{0x0000000000000000L,0x0004000000080000L});
    public static final BitSet FOLLOW_GT_in_typeParameters655 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_typeParameter678 = new BitSet(new long[]{0x0000040000000002L});
    public static final BitSet FOLLOW_EXTENDS_in_typeParameter687 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_typeBound_in_typeParameter691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeBound731 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_AMP_in_typeBound744 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_typeBound748 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_modifiers_in_enumDeclaration798 = new BitSet(new long[]{0x0000020000000000L});
    public static final BitSet FOLLOW_ENUM_in_enumDeclaration810 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumDeclaration824 = new BitSet(new long[]{0x0002000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_IMPLEMENTS_in_enumDeclaration836 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_typeList_in_enumDeclaration840 = new BitSet(new long[]{0x0002000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_enumBody_in_enumDeclaration865 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_enumBody896 = new BitSet(new long[]{0x0000000000000010L,0x00010000000C8000L});
    public static final BitSet FOLLOW_enumConstants_in_enumBody904 = new BitSet(new long[]{0x0000000000000000L,0x00000000000C8000L});
    public static final BitSet FOLLOW_COMMA_in_enumBody914 = new BitSet(new long[]{0x0000000000000000L,0x0000000000048000L});
    public static final BitSet FOLLOW_enumBodyDeclarations_in_enumBody924 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RBRACE_in_enumBody934 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants952 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_enumConstants963 = new BitSet(new long[]{0x0000000000000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_enumConstant_in_enumConstants965 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_annotations_in_enumConstant1002 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumConstant1013 = new BitSet(new long[]{0x0002040000000002L,0x0008000000005000L});
    public static final BitSet FOLLOW_arguments_in_enumConstant1021 = new BitSet(new long[]{0x0002040000000002L,0x0008000000004000L});
    public static final BitSet FOLLOW_classBody_in_enumConstant1032 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_enumBodyDeclarations1073 = new BitSet(new long[]{0xDCF02A8654000012L,0x0009000000044689L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_enumBodyDeclarations1085 = new BitSet(new long[]{0xDCF02A8654000012L,0x0009000000044689L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_interfaceDeclaration1116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_interfaceDeclaration1126 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_normalInterfaceDeclaration1163 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_normalInterfaceDeclaration1172 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalInterfaceDeclaration1183 = new BitSet(new long[]{0x0000040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_typeParameters_in_normalInterfaceDeclaration1195 = new BitSet(new long[]{0x0000040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_EXTENDS_in_normalInterfaceDeclaration1210 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_typeList_in_normalInterfaceDeclaration1214 = new BitSet(new long[]{0x0000040000000000L,0x0008000000004000L});
    public static final BitSet FOLLOW_interfaceBody_in_normalInterfaceDeclaration1241 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeList1274 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_typeList1287 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_typeList1291 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_LBRACE_in_classBody1324 = new BitSet(new long[]{0xDCF02A8654000010L,0x000900000004C689L});
    public static final BitSet FOLLOW_classBodyDeclaration_in_classBody1336 = new BitSet(new long[]{0xDCF02A8654000010L,0x000900000004C689L});
    public static final BitSet FOLLOW_RBRACE_in_classBody1358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_interfaceBody1378 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000048689L});
    public static final BitSet FOLLOW_interfaceBodyDeclaration_in_interfaceBody1390 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000048689L});
    public static final BitSet FOLLOW_RBRACE_in_interfaceBody1412 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_classBodyDeclaration1432 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_memberDecl_in_classBodyDeclaration1447 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_memberDecl1465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_memberDecl1473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_memberDecl1481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_memberDecl1489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_initializationBlock_in_memberDecl1497 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STATIC_in_initializationBlock1522 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_initializationBlock1538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration1575 = new BitSet(new long[]{0x0000000000000010L,0x0008000000000000L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration1583 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration1594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration1603 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004040L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration1611 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration1615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LBRACE_in_methodDeclaration1627 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_methodDeclaration1642 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_blockStatement_in_methodDeclaration1662 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_RBRACE_in_methodDeclaration1675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodDeclaration1701 = new BitSet(new long[]{0x4050208250000010L,0x0008000000000200L});
    public static final BitSet FOLLOW_typeParameters_in_methodDeclaration1709 = new BitSet(new long[]{0x4050208250000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_type_in_methodDeclaration1725 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_VOID_in_methodDeclaration1742 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodDeclaration1758 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_formalParameters_in_methodDeclaration1766 = new BitSet(new long[]{0xDCF02A8654000010L,0x00090000000546C9L});
    public static final BitSet FOLLOW_LBRACKET_in_methodDeclaration1774 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_methodDeclaration1776 = new BitSet(new long[]{0xDCF02A8654000010L,0x00090000000546C9L});
    public static final BitSet FOLLOW_THROWS_in_methodDeclaration1786 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_methodDeclaration1790 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_methodDeclaration1807 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_methodDeclaration1819 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_fieldDeclaration1851 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_fieldDeclaration1859 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration1865 = new BitSet(new long[]{0x0000000000000000L,0x00000000000C0000L});
    public static final BitSet FOLLOW_COMMA_in_fieldDeclaration1874 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_fieldDeclaration1876 = new BitSet(new long[]{0x0000000000000000L,0x00000000000C0000L});
    public static final BitSet FOLLOW_SEMI_in_fieldDeclaration1886 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_variableDeclarator1907 = new BitSet(new long[]{0x0000000000000002L,0x0000000000410000L});
    public static final BitSet FOLLOW_LBRACKET_in_variableDeclarator1916 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_variableDeclarator1917 = new BitSet(new long[]{0x0000000000000002L,0x0000000000410000L});
    public static final BitSet FOLLOW_EQ_in_variableDeclarator1929 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1805212L});
    public static final BitSet FOLLOW_variableInitializer_in_variableDeclarator1933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_interfaceBodyDeclaration1968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_interfaceBodyDeclaration1978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_interfaceBodyDeclaration1988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_interfaceBodyDeclaration1998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_interfaceBodyDeclaration2008 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceMethodDeclaration2032 = new BitSet(new long[]{0x4050208250000010L,0x0008000000000200L});
    public static final BitSet FOLLOW_typeParameters_in_interfaceMethodDeclaration2042 = new BitSet(new long[]{0x4050208250000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_type_in_interfaceMethodDeclaration2055 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_VOID_in_interfaceMethodDeclaration2066 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_interfaceMethodDeclaration2082 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_formalParameters_in_interfaceMethodDeclaration2090 = new BitSet(new long[]{0x0000000000000000L,0x0000000000050040L});
    public static final BitSet FOLLOW_LBRACKET_in_interfaceMethodDeclaration2098 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_interfaceMethodDeclaration2099 = new BitSet(new long[]{0x0000000000000000L,0x0000000000050040L});
    public static final BitSet FOLLOW_THROWS_in_interfaceMethodDeclaration2109 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_interfaceMethodDeclaration2113 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceMethodDeclaration2124 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceFieldDeclaration2148 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_interfaceFieldDeclaration2157 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2164 = new BitSet(new long[]{0x0000000000000000L,0x00000000000C0000L});
    public static final BitSet FOLLOW_COMMA_in_interfaceFieldDeclaration2173 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_interfaceFieldDeclaration2175 = new BitSet(new long[]{0x0000000000000000L,0x00000000000C0000L});
    public static final BitSet FOLLOW_SEMI_in_interfaceFieldDeclaration2185 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_type2212 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_type2224 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_type2225 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_primitiveType_in_type2246 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_type2258 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_type2259 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType2299 = new BitSet(new long[]{0x0000000000000002L,0x0008000000100000L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2309 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_classOrInterfaceType2320 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classOrInterfaceType2332 = new BitSet(new long[]{0x0000000000000002L,0x0008000000100000L});
    public static final BitSet FOLLOW_typeArguments_in_classOrInterfaceType2344 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_set_in_primitiveType0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_typeArguments2469 = new BitSet(new long[]{0x4050208250000010L,0x0000000002000000L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2471 = new BitSet(new long[]{0x0000000000000000L,0x0004000000080000L});
    public static final BitSet FOLLOW_COMMA_in_typeArguments2482 = new BitSet(new long[]{0x4050208250000010L,0x0000000002000000L});
    public static final BitSet FOLLOW_typeArgument_in_typeArguments2484 = new BitSet(new long[]{0x0000000000000000L,0x0004000000080000L});
    public static final BitSet FOLLOW_GT_in_typeArguments2506 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeArgument2526 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_QUES_in_typeArgument2536 = new BitSet(new long[]{0x0000040000000002L,0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_typeArgument2560 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_typeArgument2604 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2636 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_qualifiedNameList2646 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_qualifiedNameList2650 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_LPAREN_in_formalParameters2681 = new BitSet(new long[]{0x4050288250000010L,0x0001000000002000L});
    public static final BitSet FOLLOW_formalParameterDecls_in_formalParameters2696 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_formalParameters2718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls2744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls2754 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls2769 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls2776 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_normalParameterDecl_in_formalParameterDecls2796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_formalParameterDecls2802 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_formalParameterDecls2822 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_normalParameterDecl2857 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_normalParameterDecl2864 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_normalParameterDecl2870 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_normalParameterDecl2880 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_normalParameterDecl2882 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_variableModifiers_in_ellipsisParameterDecl2910 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_ellipsisParameterDecl2920 = new BitSet(new long[]{0x0000000000000000L,0x0000000000200000L});
    public static final BitSet FOLLOW_ELLIPSIS_in_ellipsisParameterDecl2923 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_ellipsisParameterDecl2933 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation2955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000012L});
    public static final BitSet FOLLOW_set_in_explicitConstructorInvocation2981 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation3013 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation3015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_explicitConstructorInvocation3026 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_explicitConstructorInvocation3036 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_explicitConstructorInvocation3047 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_explicitConstructorInvocation3068 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_explicitConstructorInvocation3078 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_explicitConstructorInvocation3080 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName3100 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_qualifiedName3111 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_qualifiedName3113 = new BitSet(new long[]{0x0000000000000002L,0x0000000000100000L});
    public static final BitSet FOLLOW_annotation_in_annotations3145 = new BitSet(new long[]{0x0000000000000002L,0x0001000000000000L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_annotation3178 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedName_in_annotation3180 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_annotation3194 = new BitSet(new long[]{0x4150208250003FF0L,0x00090003C1807212L});
    public static final BitSet FOLLOW_elementValuePairs_in_annotation3221 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_elementValue_in_annotation3245 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_annotation3281 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs3313 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_elementValuePairs3324 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elementValuePair_in_elementValuePairs3326 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_elementValuePair3357 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_elementValuePair3359 = new BitSet(new long[]{0x4150208250003FF0L,0x00090003C1805212L});
    public static final BitSet FOLLOW_elementValue_in_elementValuePair3361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalExpression_in_elementValue3381 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotation_in_elementValue3391 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_elementValueArrayInitializer_in_elementValue3401 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_elementValueArrayInitializer3421 = new BitSet(new long[]{0x4150208250003FF0L,0x00090003C188D212L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer3432 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer3447 = new BitSet(new long[]{0x4150208250003FF0L,0x00090003C1805212L});
    public static final BitSet FOLLOW_elementValue_in_elementValueArrayInitializer3449 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_elementValueArrayInitializer3478 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RBRACE_in_elementValueArrayInitializer3482 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationTypeDeclaration3510 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_annotationTypeDeclaration3521 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_annotationTypeDeclaration3522 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationTypeDeclaration3534 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_annotationTypeBody_in_annotationTypeDeclaration3554 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_annotationTypeBody3575 = new BitSet(new long[]{0xDCF02A8654000010L,0x0001000000048489L});
    public static final BitSet FOLLOW_annotationTypeElementDeclaration_in_annotationTypeBody3587 = new BitSet(new long[]{0xDCF02A8654000010L,0x0001000000048489L});
    public static final BitSet FOLLOW_RBRACE_in_annotationTypeBody3609 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_annotationTypeElementDeclaration3631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_annotationTypeElementDeclaration3641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_annotationTypeElementDeclaration3651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_annotationTypeElementDeclaration3661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_annotationTypeElementDeclaration3671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_annotationTypeElementDeclaration3681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_annotationTypeElementDeclaration3691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationMethodDeclaration3711 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_annotationMethodDeclaration3722 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationMethodDeclaration3733 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_annotationMethodDeclaration3743 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_annotationMethodDeclaration3745 = new BitSet(new long[]{0x0000002000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_DEFAULT_in_annotationMethodDeclaration3758 = new BitSet(new long[]{0x4150208250003FF0L,0x00090003C1805212L});
    public static final BitSet FOLLOW_elementValue_in_annotationMethodDeclaration3771 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_annotationMethodDeclaration3792 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_block3819 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_blockStatement_in_block3833 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_RBRACE_in_block3846 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_blockStatement3872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_blockStatement3882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statement_in_blockStatement3892 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_localVariableDeclarationStatement3913 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_localVariableDeclarationStatement3923 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableDeclaration3943 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_localVariableDeclaration3945 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration3955 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_localVariableDeclaration3967 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclarator_in_localVariableDeclaration3969 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_block_in_statement4001 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement4025 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_statement4045 = new BitSet(new long[]{0x0000000000000000L,0x0000000004040000L});
    public static final BitSet FOLLOW_COLON_in_statement4048 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_statement4050 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_statement4064 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_statement4067 = new BitSet(new long[]{0x0000000000000000L,0x0000000004040000L});
    public static final BitSet FOLLOW_COLON_in_statement4070 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_statement4072 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IF_in_statement4098 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_parExpression_in_statement4100 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_statement4102 = new BitSet(new long[]{0x0000010000000002L});
    public static final BitSet FOLLOW_ELSE_in_statement4105 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_statement4107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_forstatement_in_statement4129 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_WHILE_in_statement4139 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_parExpression_in_statement4141 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_statement4143 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DO_in_statement4153 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_statement4155 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000800L});
    public static final BitSet FOLLOW_WHILE_in_statement4157 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_parExpression_in_statement4159 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4161 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_trystatement_in_statement4171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SWITCH_in_statement4181 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_parExpression_in_statement4183 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LBRACE_in_statement4185 = new BitSet(new long[]{0x0000002080000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_switchBlockStatementGroups_in_statement4187 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RBRACE_in_statement4189 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SYNCHRONIZED_in_statement4199 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_parExpression_in_statement4201 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_statement4203 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_RETURN_in_statement4213 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1841212L});
    public static final BitSet FOLLOW_expression_in_statement4216 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4221 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THROW_in_statement4231 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_statement4233 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4235 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BREAK_in_statement4245 = new BitSet(new long[]{0x0000000000000010L,0x0000000000040000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement4260 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4277 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CONTINUE_in_statement4287 = new BitSet(new long[]{0x0000000000000010L,0x0000000000040000L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement4302 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4319 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_statement4329 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_statement4332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_statement4347 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_statement4349 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_statement4351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SEMI_in_statement4361 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_switchBlockStatementGroup_in_switchBlockStatementGroups4383 = new BitSet(new long[]{0x0000002080000002L});
    public static final BitSet FOLLOW_switchLabel_in_switchBlockStatementGroup4412 = new BitSet(new long[]{0xFDF16AD67C003FF2L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_blockStatement_in_switchBlockStatementGroup4423 = new BitSet(new long[]{0xFDF16AD67C003FF2L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_CASE_in_switchLabel4454 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_switchLabel4456 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel4458 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DEFAULT_in_switchLabel4468 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_switchLabel4470 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TRY_in_trystatement4491 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_trystatement4493 = new BitSet(new long[]{0x0000100100000000L});
    public static final BitSet FOLLOW_catches_in_trystatement4507 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement4509 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_trystatement4511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_trystatement4525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FINALLY_in_trystatement4539 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_trystatement4541 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catchClause_in_catches4572 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_catchClause_in_catches4583 = new BitSet(new long[]{0x0000000100000002L});
    public static final BitSet FOLLOW_CATCH_in_catchClause4614 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_catchClause4616 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_formalParameter_in_catchClause4618 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_catchClause4628 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_catchClause4630 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_formalParameter4651 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_formalParameter4653 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_formalParameter4655 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_formalParameter4666 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_formalParameter4668 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_FOR_in_forstatement4717 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement4719 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_variableModifiers_in_forstatement4721 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_forstatement4723 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_forstatement4725 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_forstatement4727 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_forstatement4738 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement4740 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_forstatement4742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_forstatement4774 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_forstatement4776 = new BitSet(new long[]{0x4150288250003FF0L,0x00090003C1841212L});
    public static final BitSet FOLLOW_forInit_in_forstatement4796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement4817 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1841212L});
    public static final BitSet FOLLOW_expression_in_forstatement4837 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_forstatement4858 = new BitSet(new long[]{0x4150288250003FF0L,0x00090003C1803212L});
    public static final BitSet FOLLOW_expressionList_in_forstatement4878 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_forstatement4899 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_forstatement4901 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_forInit4921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expressionList_in_forInit4931 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_parExpression4951 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_parExpression4953 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_parExpression4955 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_expressionList4975 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_expressionList4986 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_expressionList4988 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_conditionalExpression_in_expression5020 = new BitSet(new long[]{0x0000000000000002L,0x000CFF0000400000L});
    public static final BitSet FOLLOW_assignmentOperator_in_expression5031 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_expression5033 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator5065 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSEQ_in_assignmentOperator5075 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBEQ_in_assignmentOperator5085 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STAREQ_in_assignmentOperator5095 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SLASHEQ_in_assignmentOperator5105 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_AMPEQ_in_assignmentOperator5115 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BAREQ_in_assignmentOperator5125 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_CARETEQ_in_assignmentOperator5135 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PERCENTEQ_in_assignmentOperator5145 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator5156 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_LT_in_assignmentOperator5158 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator5160 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator5171 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator5173 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator5175 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator5177 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator5188 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_assignmentOperator5190 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_assignmentOperator5192 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalExpression5213 = new BitSet(new long[]{0x0000000000000002L,0x0000000002000000L});
    public static final BitSet FOLLOW_QUES_in_conditionalExpression5224 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_conditionalExpression5226 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_conditionalExpression5228 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_conditionalExpression_in_conditionalExpression5230 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression5261 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_BARBAR_in_conditionalOrExpression5272 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalOrExpression5274 = new BitSet(new long[]{0x0000000000000002L,0x0000000020000000L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5305 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_AMPAMP_in_conditionalAndExpression5316 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_inclusiveOrExpression_in_conditionalAndExpression5318 = new BitSet(new long[]{0x0000000000000002L,0x0000000010000000L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5349 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_BAR_in_inclusiveOrExpression5360 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_exclusiveOrExpression_in_inclusiveOrExpression5362 = new BitSet(new long[]{0x0000000000000002L,0x0000002000000000L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression5393 = new BitSet(new long[]{0x0000000000000002L,0x0000004000000000L});
    public static final BitSet FOLLOW_CARET_in_exclusiveOrExpression5404 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_andExpression_in_exclusiveOrExpression5406 = new BitSet(new long[]{0x0000000000000002L,0x0000004000000000L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression5437 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_AMP_in_andExpression5448 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_equalityExpression_in_andExpression5450 = new BitSet(new long[]{0x0000000000000002L,0x0000001000000000L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression5481 = new BitSet(new long[]{0x0000000000000002L,0x0002000008000000L});
    public static final BitSet FOLLOW_set_in_equalityExpression5508 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_instanceOfExpression_in_equalityExpression5558 = new BitSet(new long[]{0x0000000000000002L,0x0002000008000000L});
    public static final BitSet FOLLOW_relationalExpression_in_instanceOfExpression5589 = new BitSet(new long[]{0x0008000000000002L});
    public static final BitSet FOLLOW_INSTANCEOF_in_instanceOfExpression5600 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_instanceOfExpression5602 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression5633 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_relationalOp_in_relationalExpression5644 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_shiftExpression_in_relationalExpression5646 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_LT_in_relationalOp5678 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp5680 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp5691 = new BitSet(new long[]{0x0000000000000000L,0x0000000000400000L});
    public static final BitSet FOLLOW_EQ_in_relationalOp5693 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_relationalOp5703 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_relationalOp5713 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression5733 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_shiftOp_in_shiftExpression5744 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_additiveExpression_in_shiftExpression5746 = new BitSet(new long[]{0x0000000000000002L,0x000C000000000000L});
    public static final BitSet FOLLOW_LT_in_shiftOp5779 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_LT_in_shiftOp5781 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp5792 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp5794 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp5796 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_GT_in_shiftOp5807 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_shiftOp5809 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5830 = new BitSet(new long[]{0x0000000000000002L,0x0000000300000000L});
    public static final BitSet FOLLOW_set_in_additiveExpression5857 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression5907 = new BitSet(new long[]{0x0000000000000002L,0x0000000300000000L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression5945 = new BitSet(new long[]{0x0000000000000002L,0x0000008C00000000L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression5972 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression6040 = new BitSet(new long[]{0x0000000000000002L,0x0000008C00000000L});
    public static final BitSet FOLLOW_PLUS_in_unaryExpression6073 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUB_in_unaryExpression6086 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6088 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_PLUSPLUS_in_unaryExpression6098 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUBSUB_in_unaryExpression6110 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression6112 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_unaryExpression6122 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_TILDE_in_unaryExpressionNotPlusMinus6142 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6144 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_BANG_in_unaryExpressionNotPlusMinus6154 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpressionNotPlusMinus6156 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_unaryExpressionNotPlusMinus6166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primary_in_unaryExpressionNotPlusMinus6176 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0110000L});
    public static final BitSet FOLLOW_selector_in_unaryExpressionNotPlusMinus6187 = new BitSet(new long[]{0x0000000000000002L,0x00000000C0110000L});
    public static final BitSet FOLLOW_set_in_unaryExpressionNotPlusMinus6208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression6257 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_primitiveType_in_castExpression6259 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression6261 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_castExpression6263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_castExpression6273 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_castExpression6275 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_castExpression6277 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpressionNotPlusMinus_in_castExpression6279 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_parExpression_in_primary6301 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_THIS_in_primary6323 = new BitSet(new long[]{0x0000000000000002L,0x0000000000111000L});
    public static final BitSet FOLLOW_DOT_in_primary6334 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary6336 = new BitSet(new long[]{0x0000000000000002L,0x0000000000111000L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary6358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary6379 = new BitSet(new long[]{0x0000000000000002L,0x0000000000111000L});
    public static final BitSet FOLLOW_DOT_in_primary6390 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_primary6392 = new BitSet(new long[]{0x0000000000000002L,0x0000000000111000L});
    public static final BitSet FOLLOW_identifierSuffix_in_primary6414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_primary6435 = new BitSet(new long[]{0x0000000000000000L,0x0000000000101000L});
    public static final BitSet FOLLOW_superSuffix_in_primary6445 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_literal_in_primary6455 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_creator_in_primary6465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_primary6475 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_LBRACKET_in_primary6486 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_primary6488 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_DOT_in_primary6509 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_primary6511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_VOID_in_primary6521 = new BitSet(new long[]{0x0000000000000000L,0x0000000000100000L});
    public static final BitSet FOLLOW_DOT_in_primary6523 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_primary6525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_superSuffix6551 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_superSuffix6561 = new BitSet(new long[]{0x0000000000000010L,0x0008000000000000L});
    public static final BitSet FOLLOW_typeArguments_in_superSuffix6564 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_superSuffix6585 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_superSuffix6596 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix6629 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix6631 = new BitSet(new long[]{0x0000000000000000L,0x0000000000110000L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix6652 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix6654 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_identifierSuffix6665 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_identifierSuffix6667 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_identifierSuffix6669 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix6690 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix6700 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_identifierSuffix6702 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix6712 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_identifierSuffix6714 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_identifierSuffix6716 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix6718 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix6728 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_THIS_in_identifierSuffix6730 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_identifierSuffix6740 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_identifierSuffix6742 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_identifierSuffix6744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_identifierSuffix6754 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector6776 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_selector6778 = new BitSet(new long[]{0x0000000000000002L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_selector6789 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector6810 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000010L});
    public static final BitSet FOLLOW_THIS_in_selector6812 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_selector6822 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_SUPER_in_selector6824 = new BitSet(new long[]{0x0000000000000000L,0x0000000000101000L});
    public static final BitSet FOLLOW_superSuffix_in_selector6834 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_innerCreator_in_selector6844 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_selector6854 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_selector6856 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_selector6858 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator6878 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_creator6880 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator6882 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator6884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_creator6894 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_creator6896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_classCreatorRest_in_creator6898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arrayCreator_in_creator6908 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator6928 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator6930 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator6940 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator6942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000014000L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator6953 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator6955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000014000L});
    public static final BitSet FOLLOW_arrayInitializer_in_arrayCreator6976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_arrayCreator6987 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_createdName_in_arrayCreator6989 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator6999 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_arrayCreator7001 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7011 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7025 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_arrayCreator7027 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7041 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_arrayCreator7063 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_arrayCreator7065 = new BitSet(new long[]{0x0000000000000002L,0x0000000000010000L});
    public static final BitSet FOLLOW_arrayInitializer_in_variableInitializer7096 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_variableInitializer7106 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACE_in_arrayInitializer7126 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C188D212L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer7142 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer7161 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1805212L});
    public static final BitSet FOLLOW_variableInitializer_in_arrayInitializer7163 = new BitSet(new long[]{0x0000000000000000L,0x0000000000088000L});
    public static final BitSet FOLLOW_COMMA_in_arrayInitializer7213 = new BitSet(new long[]{0x0000000000000000L,0x0000000000008000L});
    public static final BitSet FOLLOW_RBRACE_in_arrayInitializer7226 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_createdName7260 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primitiveType_in_createdName7270 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_innerCreator7291 = new BitSet(new long[]{0x0100000000000000L});
    public static final BitSet FOLLOW_NEW_in_innerCreator7293 = new BitSet(new long[]{0x0000000000000010L,0x0008000000000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_innerCreator7304 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_innerCreator7325 = new BitSet(new long[]{0x0000000000000000L,0x0008000000001000L});
    public static final BitSet FOLLOW_typeArguments_in_innerCreator7336 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_classCreatorRest_in_innerCreator7357 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_arguments_in_classCreatorRest7378 = new BitSet(new long[]{0x0002040000000002L,0x0008000000004000L});
    public static final BitSet FOLLOW_classBody_in_classCreatorRest7389 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LT_in_nonWildcardTypeArguments7421 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_typeList_in_nonWildcardTypeArguments7423 = new BitSet(new long[]{0x0000000000000000L,0x0004000000000000L});
    public static final BitSet FOLLOW_GT_in_nonWildcardTypeArguments7433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_arguments7453 = new BitSet(new long[]{0x4150288250003FF0L,0x00090003C1803212L});
    public static final BitSet FOLLOW_expressionList_in_arguments7456 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_arguments7469 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_literal0 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_classHeader7593 = new BitSet(new long[]{0x0000000400000000L});
    public static final BitSet FOLLOW_CLASS_in_classHeader7595 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_classHeader7597 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_enumHeader7617 = new BitSet(new long[]{0x0000020000000010L});
    public static final BitSet FOLLOW_set_in_enumHeader7619 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_enumHeader7625 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_interfaceHeader7645 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_interfaceHeader7647 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_interfaceHeader7649 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_annotationHeader7669 = new BitSet(new long[]{0x0000000000000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_annotationHeader7671 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_annotationHeader7673 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_annotationHeader7675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_typeHeader7695 = new BitSet(new long[]{0x0020020400000000L,0x0001000000000000L});
    public static final BitSet FOLLOW_CLASS_in_typeHeader7698 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_ENUM_in_typeHeader7700 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_MONKEYS_AT_in_typeHeader7703 = new BitSet(new long[]{0x0020000000000000L});
    public static final BitSet FOLLOW_INTERFACE_in_typeHeader7707 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_typeHeader7711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_methodHeader7731 = new BitSet(new long[]{0x4050208250000010L,0x0008000000000200L});
    public static final BitSet FOLLOW_typeParameters_in_methodHeader7733 = new BitSet(new long[]{0x4050208250000010L,0x0000000000000200L});
    public static final BitSet FOLLOW_type_in_methodHeader7737 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_VOID_in_methodHeader7739 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_methodHeader7743 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_methodHeader7745 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_fieldHeader7765 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_fieldHeader7767 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_fieldHeader7769 = new BitSet(new long[]{0x0000000000000000L,0x00000000004D0000L});
    public static final BitSet FOLLOW_LBRACKET_in_fieldHeader7772 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_fieldHeader7773 = new BitSet(new long[]{0x0000000000000000L,0x00000000004D0000L});
    public static final BitSet FOLLOW_set_in_fieldHeader7777 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_variableModifiers_in_localVariableHeader7803 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_localVariableHeader7805 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_localVariableHeader7807 = new BitSet(new long[]{0x0000000000000000L,0x00000000004D0000L});
    public static final BitSet FOLLOW_LBRACKET_in_localVariableHeader7810 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_localVariableHeader7811 = new BitSet(new long[]{0x0000000000000000L,0x00000000004D0000L});
    public static final BitSet FOLLOW_set_in_localVariableHeader7815 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotations_in_synpred2_Java1_687 = new BitSet(new long[]{0x0200000000000000L});
    public static final BitSet FOLLOW_packageDeclaration_in_synpred2_Java1_691 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred12_Java1_6311 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred27_Java1_6499 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred43_Java1_61116 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_fieldDeclaration_in_synpred50_Java1_61465 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_methodDeclaration_in_synpred51_Java1_61473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred52_Java1_61481 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_synpred53_Java1_61489 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred57_Java1_61642 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_modifiers_in_synpred59_Java1_61575 = new BitSet(new long[]{0x0000000000000010L,0x0008000000000000L});
    public static final BitSet FOLLOW_typeParameters_in_synpred59_Java1_61583 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred59_Java1_61594 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_formalParameters_in_synpred59_Java1_61603 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004040L});
    public static final BitSet FOLLOW_THROWS_in_synpred59_Java1_61611 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_qualifiedNameList_in_synpred59_Java1_61615 = new BitSet(new long[]{0x0000000000000000L,0x0000000000004000L});
    public static final BitSet FOLLOW_LBRACE_in_synpred59_Java1_61627 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_explicitConstructorInvocation_in_synpred59_Java1_61642 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_blockStatement_in_synpred59_Java1_61662 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C184DFBFL});
    public static final BitSet FOLLOW_RBRACE_in_synpred59_Java1_61675 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred68_Java1_61968 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceMethodDeclaration_in_synpred69_Java1_61978 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceDeclaration_in_synpred70_Java1_61988 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classDeclaration_in_synpred71_Java1_61998 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ellipsisParameterDecl_in_synpred96_Java1_62744 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java1_62754 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_synpred98_Java1_62769 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred98_Java1_62776 = new BitSet(new long[]{0x0000000000000002L,0x0000000000080000L});
    public static final BitSet FOLLOW_normalParameterDecl_in_synpred99_Java1_62796 = new BitSet(new long[]{0x0000000000000000L,0x0000000000080000L});
    public static final BitSet FOLLOW_COMMA_in_synpred99_Java1_62802 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred103_Java1_62955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000012L});
    public static final BitSet FOLLOW_set_in_synpred103_Java1_62981 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_arguments_in_synpred103_Java1_63013 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_synpred103_Java1_63015 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationMethodDeclaration_in_synpred117_Java1_63631 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_interfaceFieldDeclaration_in_synpred118_Java1_63641 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalClassDeclaration_in_synpred119_Java1_63651 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_normalInterfaceDeclaration_in_synpred120_Java1_63661 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_enumDeclaration_in_synpred121_Java1_63671 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_annotationTypeDeclaration_in_synpred122_Java1_63681 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclarationStatement_in_synpred125_Java1_63872 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_classOrInterfaceDeclaration_in_synpred126_Java1_63882 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_synpred130_Java1_64025 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred130_Java1_64045 = new BitSet(new long[]{0x0000000000000000L,0x0000000004040000L});
    public static final BitSet FOLLOW_COLON_in_synpred130_Java1_64048 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred130_Java1_64050 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_synpred130_Java1_64054 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ASSERT_in_synpred132_Java1_64064 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred132_Java1_64067 = new BitSet(new long[]{0x0000000000000000L,0x0000000004040000L});
    public static final BitSet FOLLOW_COLON_in_synpred132_Java1_64070 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred132_Java1_64072 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_synpred132_Java1_64076 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ELSE_in_synpred133_Java1_64105 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_synpred133_Java1_64107 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_synpred148_Java1_64329 = new BitSet(new long[]{0x0000000000000000L,0x0000000000040000L});
    public static final BitSet FOLLOW_SEMI_in_synpred148_Java1_64332 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred149_Java1_64347 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_synpred149_Java1_64349 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_synpred149_Java1_64351 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred153_Java1_64507 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_FINALLY_in_synpred153_Java1_64509 = new BitSet(new long[]{0xDCF02A8654000010L,0x0009000000044689L});
    public static final BitSet FOLLOW_block_in_synpred153_Java1_64511 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_catches_in_synpred154_Java1_64525 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_FOR_in_synpred157_Java1_64717 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_LPAREN_in_synpred157_Java1_64719 = new BitSet(new long[]{0x4050288250000010L,0x0001000000000000L});
    public static final BitSet FOLLOW_variableModifiers_in_synpred157_Java1_64721 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_type_in_synpred157_Java1_64723 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred157_Java1_64725 = new BitSet(new long[]{0x0000000000000000L,0x0000000004000000L});
    public static final BitSet FOLLOW_COLON_in_synpred157_Java1_64727 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred157_Java1_64738 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred157_Java1_64740 = new BitSet(new long[]{0xFDF16AD67C003FF0L,0x00090003C1845FBFL});
    public static final BitSet FOLLOW_statement_in_synpred157_Java1_64742 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_localVariableDeclaration_in_synpred161_Java1_64921 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_castExpression_in_synpred202_Java1_66166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_synpred206_Java1_66257 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_primitiveType_in_synpred206_Java1_66259 = new BitSet(new long[]{0x0000000000000000L,0x0000000000002000L});
    public static final BitSet FOLLOW_RPAREN_in_synpred206_Java1_66261 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_unaryExpression_in_synpred206_Java1_66263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred208_Java1_66334 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred208_Java1_66336 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred209_Java1_66358 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_DOT_in_synpred211_Java1_66390 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENTIFIER_in_synpred211_Java1_66392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_identifierSuffix_in_synpred212_Java1_66414 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred224_Java1_66665 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred224_Java1_66667 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred224_Java1_66669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred236_Java1_66878 = new BitSet(new long[]{0x0000000000000000L,0x0008000000000000L});
    public static final BitSet FOLLOW_nonWildcardTypeArguments_in_synpred236_Java1_66880 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred236_Java1_66882 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred236_Java1_66884 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred237_Java1_66894 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_classOrInterfaceType_in_synpred237_Java1_66896 = new BitSet(new long[]{0x0000000000000000L,0x0000000000001000L});
    public static final BitSet FOLLOW_classCreatorRest_in_synpred237_Java1_66898 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NEW_in_synpred239_Java1_66928 = new BitSet(new long[]{0x4050208250000010L});
    public static final BitSet FOLLOW_createdName_in_synpred239_Java1_66930 = new BitSet(new long[]{0x0000000000000000L,0x0000000000010000L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred239_Java1_66940 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred239_Java1_66942 = new BitSet(new long[]{0x0000000000000000L,0x0000000000014000L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred239_Java1_66953 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred239_Java1_66955 = new BitSet(new long[]{0x0000000000000000L,0x0000000000014000L});
    public static final BitSet FOLLOW_arrayInitializer_in_synpred239_Java1_66976 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LBRACKET_in_synpred240_Java1_67025 = new BitSet(new long[]{0x4150208250003FF0L,0x00080003C1801212L});
    public static final BitSet FOLLOW_expression_in_synpred240_Java1_67027 = new BitSet(new long[]{0x0000000000000000L,0x0000000000020000L});
    public static final BitSet FOLLOW_RBRACKET_in_synpred240_Java1_67041 = new BitSet(new long[]{0x0000000000000002L});

}