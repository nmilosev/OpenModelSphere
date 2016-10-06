package com.neosapiens.plugins.reverse.javasource;

import java.io.IOException;

import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import com.neosapiens.plugins.reverse.javasource.actions.OutputSemanticAction;
import com.neosapiens.plugins.reverse.javasource.actions.SemanticAction;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Lexer;
import com.neosapiens.plugins.reverse.javasource.parsing.Java1_6Parser;

public class Main {

    /**
     * @param args
     * @throws IOException 
     * @throws RecognitionException 
     */
    public static void main(String[] args) throws IOException, RecognitionException {
        ANTLRFileStream fileStream = new ANTLRFileStream("C:\\Documents and Settings\\Patrice\\Mes documents\\Eclipse\\workspace\\OMS\\javasource_reverse\\helloworldtest.java");
        //ANTLRFileStream fileStream = new ANTLRFileStream("C:\\Documents and Settings\\Patrice\\Mes documents\\Eclipse\\workspace\\OMS\\javasource_reverse\\GrammarTestInterfaceDeclaration.java");
        //ANTLRFileStream fileStream = new ANTLRFileStream("C:\\Documents and Settings\\Patrice\\Mes documents\\Eclipse\\workspace\\OMS\\javasource_reverse\\EnumerationTest.java");
        //ANTLRFileStream fileStream = new ANTLRFileStream("C:\\Documents and Settings\\Patrice\\Mes documents\\Eclipse\\workspace\\OMS\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\actions\\SemanticAction.java");
        //ANTLRFileStream fileStream = new ANTLRFileStream("C:\\Documents and Settings\\Patrice\\Mes documents\\Eclipse\\workspace\\OMS\\javasource_reverse\\src\\com\\neosapiens\\plugins\\reverse\\javasource\\ReverseJavaSourceCodeWorker.java");
        
        
        Java1_6Lexer lexer = new Java1_6Lexer(fileStream);
        TokenStream tokenStream = new CommonTokenStream(lexer);
        SemanticAction semAct = new OutputSemanticAction();
        Java1_6Parser parser = new Java1_6Parser(tokenStream,semAct);
        parser.compilationUnit();

        System.out.println("done!");

    }

}
