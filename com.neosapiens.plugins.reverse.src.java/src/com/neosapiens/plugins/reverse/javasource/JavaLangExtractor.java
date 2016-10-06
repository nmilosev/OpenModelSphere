package com.neosapiens.plugins.reverse.javasource;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaLangExtractor {
    
    public static void main(String[] args) throws IOException {
        URL url = new URL("http://download.oracle.com/javase/6/docs/api/java/lang/package-summary.html");
        BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                    url.openStream()));

        String inputLine;
        StringWriter content = new StringWriter();
        while ((inputLine = in.readLine()) != null)
            content.write(inputLine + "\n");
        in.close();
        content.flush();
        String page = content.toString();
        page = page.split("<H2>")[1];
       
        String expr = "<A HREF.*?/(\\w+)\\.html.*?title=\"(\\w+).*?</A>";
        Pattern pattern = Pattern.compile(expr, Pattern.DOTALL | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(page);
        
        File f = new File("java.lang declarations.txt");
        FileWriter out = new FileWriter(f,false);
        BufferedWriter output = new BufferedWriter(out);
        output.write("DeclarationOfClassOrInterface decl;");
        output.newLine();
        while (matcher.find()){
            output.write("decl = new DeclarationOfClassOrInterface();");
            output.newLine();
            output.write("decl.setClassOrInterfaceName(\""+matcher.group(1)+"\");");
            output.newLine();
            if (matcher.group(2).equals("interface") || matcher.group(2).equals("annotation")){
                output.write("decl.setInterface(true);");
                output.newLine();
            } else if (matcher.group(1).contains("Exception")){
                //Class is exception
            } else {
                output.write("decl.setClass(true);");
                output.newLine();
            }
            output.write("decl.setCurrentPackage(\"java.lang\");");
            output.newLine();
            output.write("decl.setModifiers(Modifier.PUBLIC);");
            output.newLine();
            output.write("fullTypes.put(\"java.lang."+matcher.group(1)+"\", decl);");
            output.newLine();
        }
        output.close();
    }
}
