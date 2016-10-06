/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.jack.srtool.forward;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.io.PathFile;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.forward.exceptions.DomainNotFoundRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.EndlessLoopRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.IllformedNumberException;
import org.modelsphere.jack.srtool.forward.exceptions.NotADomainRuleException;
import org.modelsphere.jack.srtool.forward.exceptions.SubruleMissingRuleException;
import org.modelsphere.jack.text.MessageFormat;

/**
 * A template is a string containing template variables and edition codes. Processing (expanding) a
 * template consists in replacing the template variables and edition codes by the strings they
 * represent. Within a template, template variables and edition codes are preceded by a dollar sign
 * and followed by a semi-colon, in order to distinguish them from the surrounding text. To
 * specified a dollar sign as a single character in a template, enter two consecutive dollar signs. <BR>
 * <BR>
 * A template variable essentially produces a character string from its parameters; most parameters
 * are templates referring to other template variables. <BR>
 * <BR>
 * Parameter: character string representing a template. Optional keywords: IfModifier, NullModifier,
 * PrefixModifier, SuffixModifier <BR>
 * <BR>
 * Algorithm<BR>
 * -If the condition specified by IfModifier is false, returns an empty string. -Process the
 * template by replacing all the $..; tokens. -If the result string is not empty, then processes
 * templates specified by PrefixModifier and SuffixModifier, if any. -If the result string is empty,
 * processes the template specified by NullModifier, and returns the NullModifier results; otherwise
 * results the processed string. <BR>
 * <BR>
 * Edition codes<BR>
 * Edition codes are used to format the output of the template processed. They allow users to
 * specify left margin values, carriage returns and various positionning movements in the template
 * file. <BR>
 * <BR>
 * 1.$m;<BR>
 * Format: $m(+ or -)number<BR>
 * Action: increments or decrements the left margin according to the sign. Does not make a return;
 * the new setting takes effect at the next return code ($n). 2.n$;<BR>
 * Format: $n; Action: makes a return, and advances to the left margin. Does not change the left
 * margin. 3.t$;<BR>
 * Not implemented now.
 */

public final class Template extends Rule {
    private String format; // unparsed format string
    private String subStrings[] = null;
    private Rule[] subRawRules = null; // rules specified by user
    private Rule[] subActualRules = null; // rules including edition codes
    private int nbRules;
    private String m_domain;
    private static final String SEPARATOR = System.getProperty("file.separator");
    private static final String ALT_SEPARATOR = "/"; // also used in Windows
    private static final String NULL_FORMAT_ERROR_PATTERN = "Template named {0} cannot have a null format string : {1}."; // NOT
    // LOCALIZABLE,
    // used
    // in
    // debug
    // mode
    // only

    // //////////////////////
    // STRING RULES' SUPPORT
    protected String[] stringRules = null;

    protected void setStringRules(String[] someStringRules) {
        stringRules = someStringRules;
    }

    // result can be null
    public String[] getStringRules() {
        return stringRules;
    }

    public void setActualRules(Rule[] rules) {
        subRawRules = rules;
    }

    //
    // ///////////////////////

    public static void resetMargin() {
        EditionCode.reset(0);
    }

    // set actual actual subrules from its string rule;
    public void setActualSubRules(Hashtable table) throws RuleException {

        // change dom with its actual rule
        if (m_domain != null) {
            Rule rule = (Rule) table.get(m_domain);
            if (rule instanceof CharacterDomain) {
                CharacterDomain domainRule = (CharacterDomain) rule;
                domainModifier = new DomainModifier(domainRule);
            } else if (rule == null) {
                String msg = DomainNotFoundRuleException.buildMessage(m_ruleName);
                throw new DomainNotFoundRuleException(msg);
            } else {
                String msg = NotADomainRuleException.buildMessage(rule.m_ruleName);
                throw new NotADomainRuleException(msg);
            } // end if
        } // end if

        // find Rule[] subRawRules from String[] stringRules
        int nb = 0;
        if (stringRules != null) {
            nb = stringRules.length;
        }

        subRawRules = new Rule[nb];

        // for each sub rule
        for (int i = 0; i < nb; i++) {
            String s = stringRules[i];
            subRawRules[i] = (Rule) table.get(s);
        } // end for

        // change modifiers with their actual rules
        super.setActualModifiers(table);
    }

    /* This is called only once per template variable */
    private void parseFormatString() throws RuleException {
        TemplateEnumeration enumeration = new TemplateEnumeration(format);
        int nbSubStrings = 0;
        java.util.Vector vecOfSubStrings = new java.util.Vector();

        while (enumeration.hasMoreElements()) {
            StringStructure ss = (StringStructure) enumeration.nextElement();
            vecOfSubStrings.addElement(ss);
            nbSubStrings++;
        } // end while

        subActualRules = new Rule[nbSubStrings];

        // Store in an array
        subStrings = new String[nbSubStrings];
        int rawRulesIndex = 0;
        try {
            for (int i = 0; i < nbSubStrings; i++) {
                StringStructure ss = (StringStructure) vecOfSubStrings.elementAt(i);
                subStrings[i] = ss.str;

                if (i < (nbSubStrings - 1)) {
                    if (ss.editionCode != null)
                        subActualRules[i] = ss.editionCode;
                    else
                        subActualRules[i] = subRawRules[rawRulesIndex++];
                } // end if
            } // end for
        } catch (ArrayIndexOutOfBoundsException ex) {
            String msg = SubruleMissingRuleException.buildMessage(m_ruleName, format);
            throw new SubruleMissingRuleException(msg);
        }
    }

    /*
     * The definition of a template variable is made up of a variable class, a variable name, of a
     * mandatory format string defining the template, of a list of subtemplates, and of optional
     * modifiers without any specific order; the definition ends with a semi-colon. <BR><BR> <PRE>
     * &#32&#32 static private Template g_point = new Template( &#32&#32&#32&#32
     * "Point = ($xCoordonate;, $yCoordonate;).$n;", //NOT LOCALIZABLE &#32&#32&#32&#32 new Rule[]
     * {g_xCoordonate, g_yCoordonate}, &#32&#32&#32&#32 new Modifier[] {ifValidCoords} &#32&#32 );
     * </PRE> <BR><BR>
     * 
     * @param aFormat The format string to be processed. In the example, the format string contains
     * two subtemplates ($xCoordonate; and $yCoordonate;) and an edition code ($n;). Format string
     * is enclosed in double-quotes; a long string can be split into a number of strings separated
     * by a plus sign ('+').
     * 
     * @param someRules (optional) The list of subrules for each rule declared in the format string.
     * The number of elements in the list of subrules (in the example: {g_xCoordnate,
     * g_yCoordonate}) must match the number of subtemplates in the format string; the name of
     * subtemplates in the format string is purely mnemonic: $xCoordonate; can be renamed and it
     * will always match with the first element of the Rule[]; all the rules defined (here
     * g_xCoordonate and g_yCoordonate) must be previously defined in the file.
     * 
     * @param someModifiers (optional) The list of optional modifiers. In the example, there is one
     * optional modifier, named ifValiCoords. <BR><BR>
     */
    // deprecated
    /*
     * public Template( String ruleName, String aFormat, Rule[] someSubRules, Modifier[]
     * someModifiers) { //String nullString = null; this(ruleName, aFormat, someSubRules,
     * (String)null, someModifiers); }
     */

    public Template(String ruleName, String aFormat, Rule[] someSubRules, String domain,
            Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        String msg = MessageFormat.format(NULL_FORMAT_ERROR_PATTERN, new Object[] { ruleName,
                aFormat });
        Debug.assert2(aFormat != null, msg);
        m_domain = domain; // can be null
        init(aFormat, someSubRules);
    }

    /*
     * //deprecated public Template( String ruleName, String aFormat, String[] someStringRules,
     * Modifier[] someModifiers) { //String nullString = null; this(ruleName, aFormat,
     * someStringRules, (String)null, someModifiers); }
     */

    public Template(String ruleName, String aFormat, String[] someStringRules, String domain,
            Modifier[] someModifiers) {
        super(ruleName, someModifiers);
        String msg = MessageFormat.format(NULL_FORMAT_ERROR_PATTERN, new Object[] { ruleName,
                aFormat });
        Debug.assert2(aFormat != null, msg);
        m_domain = domain; // can be null
        init(aFormat, new Rule[] {});
        setStringRules(someStringRules);
    }

    public Template(String ruleName, String aFormat) {
        super(ruleName, new Modifier[] {});
        String msg = MessageFormat.format(NULL_FORMAT_ERROR_PATTERN, new Object[] { ruleName,
                aFormat });
        Debug.assert2(aFormat != null, msg);
        init(aFormat, new Rule[] {});
    }

    // End of constructors

    // ////////////////
    // OVERRIDES Object
    public String toString() {
        return (m_ruleName + "(" + format + ")");
    } // NOT LOCALIZABLE

    //
    // ////////////////

    private void init(String aFormat, Rule[] someSubRules) {
        format = aFormat;
        subRawRules = someSubRules;
        nbRules = someSubRules.length;
    }

    // Process txt to replace \n by its actual margin
    private String processEndOfLine(String txt) {
        StringWriter processText = new StringWriter();

        StringTokenizer st = new StringTokenizer(txt, "\n"); // NOT LOCALIZABLE
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            processText.write(token);
            if (st.hasMoreTokens()) {
                processText.write("\n"); // NOT LOCALIZABLE

                // add margin
                for (int i = 0; i < EditionCode.getMargin(); i++) {
                    processText.write("<->");
                } // end for
            } // end if
        } // end while

        return processText.toString();
    }

    private String expandSubRules(Serializable object, RuleOptions options) throws RuleException,
            IOException {
        StringWriter buffer = new StringWriter();
        boolean expanded = false;

        // write each ith substring and expand each ith rule in buffer
        // TODO: don't process $n; here
        int len = subStrings.length - 1;
        for (int i = 0; i < len; i++) {
            String s = subStrings[i];
            buffer.write(s);
            if (!s.equals("")) {
                expanded = true;
            }

            Rule rule = subActualRules[i];
            if (rule == null) {
                String msg = SubruleMissingRuleException.buildMessage(m_ruleName, format);
                throw new SubruleMissingRuleException(msg);
            }

            expanded |= rule.expand(buffer, object, options);
        } // end for

        // write the last substring
        String s = subStrings[len];
        if (!s.equals("")) {
            expanded = true;
        }

        buffer.write(s);

        String txt = (expanded ? buffer.toString() : "");
        return txt;
    }

    private boolean m_domainMode = false;

    private boolean expandWithDomainModifier(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded;

        CharacterDomain cdom = domainModifier.getDomain();
        StringWriter sw = new StringWriter();
        m_domainMode = true;
        expand(sw, object, options);
        m_domainMode = false;
        String strValue = sw.toString();

        if (cdom == null) {
            String msg = DomainNotFoundRuleException.buildMessage(m_ruleName);
            throw new DomainNotFoundRuleException(msg);
        }

        expanded = cdom.expand(output, object, options, strValue);
        return expanded;
    }

    /*
     * private String setFullPathFilename(String filename) {
     * 
     * char firstChar = filename.charAt(0); switch (firstChar) { //absolute directory case '/' :
     * case '\\' : break; //relative directory case '.' : filename = FileModifier.getBaseDirectory()
     * + SEPARATOR + filename; break; default : filename = FileModifier.getBaseDirectory() +
     * SEPARATOR + filename; break; } //end switch
     * 
     * return filename; }
     */

    private boolean expandInAFile(FileModifier fileModifier, Serializable object,
            RuleOptions options) throws IOException, RuleException {
        boolean expanded = false;

        // expand filename
        Rule filerule = fileModifier.getRule();
        StringWriter swriter = new StringWriter();
        filerule.expand(swriter, object, options);
        String filename = swriter.toString();
        if (!PathFile.isAbsolutePath(filename)) {
            String outputDir = ApplicationContext.getDefaultWorkingDirectory();
            filename = outputDir + PathFile.SEPARATOR + filename;
        } // end if

        // expand in a string
        try {
            swriter = new StringWriter();
            expanded = wrappedExpand(swriter, object, options);
        } catch (EndlessLoopException ex) {
            String msg = EndlessLoopRuleException.buildMessage(m_ruleName);
            throw new EndlessLoopRuleException(msg);
        }

        // open and write in the file
        File actualFile = PathFile.createFile(filename, true);
        FileWriter output = new FileWriter(actualFile);
        String unprocessedEditionCode = swriter.toString();
        String s = EditionCode.processEditionCode(unprocessedEditionCode);
        output.write(s);
        output.close();
        FileModifier.g_generatedFiles.add(filename);

        return expanded;
    } // end

    private static int g_counter = 0; // to detect endless loops
    private static final int COUNTER_LIMIT = 128; // deepest recursion allowed

    public boolean expand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded;

        g_counter++;
        if (g_counter > COUNTER_LIMIT) {
            g_counter = 0; // reset for furthur calls
            String msg = EndlessLoopRuleException.buildMessage(m_ruleName);
            throw new EndlessLoopRuleException(msg);
        }

        boolean excluded = verifyExclusion(object, options);

        if (excluded) {
            return false;
        }

        try {
            // if FILE modifier, set output to file specified by the FILE
            // modifier
            String filename = null;
            if (fileModifier != null) {
                expanded = expandInAFile(fileModifier, object, options);
            } else {
                try {
                    expanded = wrappedExpand(output, object, options);
                } catch (EndlessLoopException ex) {
                    String msg = EndlessLoopRuleException.buildMessage(m_ruleName);
                    throw new EndlessLoopRuleException(msg);
                }
            } // end if
        } catch (NumberFormatException ex) {
            String msg = IllformedNumberException.buildMessage("rule1", "c", "tepl"); // NOT LOCALIZABLE
            throw new RuleException(msg);
        }

        g_counter--;
        return expanded;
    }

    private boolean wrappedExpand(Writer output, Serializable object, RuleOptions options)
            throws IOException, RuleException {
        boolean expanded = false;

        // special case if UPPER or LOWER
        if (m_caseModifier != null) {
            if (m_caseModifier.getCasePolicy() != CaseModifier.UNCHANGE) {
                int casePolicy = m_caseModifier.getCasePolicy(); // keep case
                // policy
                m_caseModifier.resetCasePolicy(); // to avoid endless-looping in
                // recursive call
                StringWriter swriter = new StringWriter();
                expanded = wrappedExpand(swriter, object, options); // recursive
                // call
                String result = swriter.toString();
                result = result.toUpperCase();
                if (casePolicy == CaseModifier.UPPER) {
                    result = result.toUpperCase();
                } else if (casePolicy == CaseModifier.LOWER) {
                    result = result.toLowerCase();
                } // end if

                output.write(result);
                super.terminate(output, options);
                return expanded;
            } // end if
        } // end if

        // special case if domain is specified
        if (!m_domainMode) { // to avoid endless loop
            if (domainModifier != null) {
                expanded |= expandWithDomainModifier(output, object, options);

                // if no expansion, and a null modifier exists
                if ((expanded == false) && (nullModifier != null)) {
                    expanded = nullModifier.expand(output, object, options);
                } // end if

                super.terminate(output, options);
                return expanded;
            }
        } // end if

        try {
            if (subStrings == null) {
                // parse format string and break it in substrings;
                // do it only once, for efficiency.
                parseFormatString();
            }

            // Execution variables are always processed, regardless IfModifier
            if ((variableVector != null) && (!variableVector.isEmpty())) {
                Enumeration enumeration = variableVector.elements();
                while (enumeration.hasMoreElements()) {
                    VariableModifier variable = (VariableModifier) enumeration.nextElement();
                    variable.expand(null, object);
                } // end while
            } // end if

            // If no 'IF MODIFIER' defined, or one defined that returns true
            boolean condition = true;
            if (m_booleanModifier != null) {
                condition = m_booleanModifier.evaluate(object);
            }

            if (condition) {
                int margin = EditionCode.getMargin();
                String txt = expandSubRules(object, options);

                if (!txt.equals("")) {
                    // write prefix, if any
                    if (prefixModifier != null) {
                        EditionCode.reset(margin); // restore to previous value
                        prefixModifier.expand(output, object, options);

                        // expand again the subrules (prefix may have changed
                        // the margin)
                        txt = expandSubRules(object, options);
                    } // end if

                    // Process txt to replace \n by its actual margin
                    String processed = processEndOfLine(txt);

                    // write the TEMPL pattern
                    output.write(processed);

                    // write suffix, if any
                    if (suffixModifier != null) {
                        suffixModifier.expand(output, object, options);
                    }
                    expanded = true;
                } // end if
            } // end if

            // if no expansion, and a null modifier exists
            if ((expanded == false) && (nullModifier != null)) {
                expanded = nullModifier.expand(output, object, options);
            }

            super.terminate(output, options);
        } catch (NumberFormatException ex) {
            throw new RuleException(format);
        } catch (RuntimeException ex) {
            String msg = ex.getMessage();
            if (msg == null)
                msg = ex.toString();

            throw new RuleException(msg);
        }

        return expanded;
    }

    // INNER CLASS
    private static class EndlessLoopException extends RuleException {
        EndlessLoopException(String name) {
            super(name);
        }
    } // end EndlessLoopException()
}
