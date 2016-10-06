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

package org.modelsphere.jack.io;

import java.io.*;
import java.util.ArrayList;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.TestException;
import org.modelsphere.jack.debug.Testable;
import org.modelsphere.jack.util.StringUtil;

/**
 * 
 * I/O facilities that java.io does not provide
 * 
 */
/*
 * Extension of RandomAccessFile with direct line access facilities. File Implementation of Seekable
 */
public final class LineRandomAccessFile extends RandomAccessFile implements Testable {

    private static final String readOnly = "r"; // NOT LOCALIZABLE, internal
    // code

    // this array list contains the absolute position for each line
    private ArrayList m_absolutePositions = new ArrayList();

    public LineRandomAccessFile(File file) throws FileNotFoundException, IOException {

        super(file, readOnly);

        // Build a vector containing positions of each line
        do {
            long filepos = getFilePointer();
            m_absolutePositions.add(new Long(filepos));
        } while (readLine() != null);
    }

    /** Get current line number */
    public int getLineNo() throws IOException {
        int len = m_absolutePositions.size();
        int lineNo = len;
        long filepos = getFilePointer();

        // TODO: perform binary search (valuable if nb lines > 100)
        for (int i = 0; i < len; i++) {
            Long lineStart = (Long) m_absolutePositions.get(i);
            if (filepos <= lineStart.longValue()) {
                lineNo = i;
                break;
            }
        }

        try {
            if (lineNo < 0)
                Debug.assert2(false);
        } catch (Exception ex) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, ex);
        }

        return lineNo;
    }

    /** seek ith line */
    public void seekLine(int lineno) throws IOException {
        Long filepos = (Long) m_absolutePositions.get(lineno - 1);
        seek(filepos.longValue());
    }

    public int getNbLines() throws IOException {
        return m_absolutePositions.size();
    }

    public long getAbsolutePosition(int line, int column) {
        Long pos = (Long) m_absolutePositions.get(line - 1);
        long absolutePosition = pos.longValue() + column - 1; // line starts at
        // column 1
        return absolutePosition;
    }

    //
    // UNIT TEST: create a buffer, and print it line by line
    //
    public void test() throws TestException {
        if (Testable.ENABLE_TEST) {
            try {
                String s;
                int nb = this.getNbLines();
                StringWriter writer = new StringWriter();

                // perform the test : only read one line out of two
                for (int i = 1; i < nb; i += 2) {
                    this.seekLine(i);
                    s = this.readLine();
                    writer.write(s);
                    writer.write("\n"); // NOT LOCALIZABLE, escape code
                }

                // check results
                g_output.append(writer.toString());
                int nbLines = StringUtil.getNbCharacters(g_output.toString(), '\n');
                if (nbLines != 7) {
                    String msg = "expected 7"; // NOT LOCALIZABLE, unit test
                    throw new TestException(msg);
                }

            } catch (java.io.IOException ex) {
                throw new TestException(ex.toString());
            } // end try
        } // end if
    } // end test()

    public static Testable createInstanceForTesting() throws TestException {
        String text = "Bob Smith, my assistant programmer, can always be found\n" + // NOT LOCALIZABLE, used in main() function only
                "hard at work in his cubicle.  Bob works independently, without\n" + // NOT LOCALIZABLE, used in main() function only
                "wasting company time talking to colleagues.  Bob never\n" + // NOT LOCALIZABLE, used in main() function only
                "thinks twice about assisting fellow employees, and he always\n" + // NOT LOCALIZABLE, used in main() function only
                "finishes given assignments on time.  Often Bob takes extended\n" + // NOT LOCALIZABLE, used in main() function only
                "measures to complete his work, sometimes skipping coffee\n" + // NOT LOCALIZABLE, used in main() function only
                "breaks.  Bob is a dedicated individual who has absolutely no\n" + // NOT LOCALIZABLE, used in main() function only
                "vanity in spite of his high accomplishments and profound\n" + // NOT LOCALIZABLE, used in main() function only
                "knowledge in his field.  I firmly believe that Bob can be\n" + // NOT LOCALIZABLE, used in main() function only
                "classed as a high-calibre employee, the type which cannot\n" + // NOT LOCALIZABLE, used in main() function only
                "dispensed with.  Consequently, I duly recommend that Bob be\n" + // NOT LOCALIZABLE, used in main() function only
                "promoted to executive management, and a proposal will be\n" + // NOT
                // LOCALIZABLE,
                // used
                // in
                // main()
                // function
                // only
                "sent away as soon as possible.\n"; // NOT LOCALIZABLE, used in
        // main() function only
        LineRandomAccessFile input;

        try {
            TemporaryFileWriter writer = new TemporaryFileWriter();
            writer.write(text);
            writer.close();
            File file = writer.getFile();
            input = new LineRandomAccessFile(file);
            file.delete();
        } catch (IOException ex) {
            throw new TestException(ex.toString());
        }

        return input;
    }

    private static StringBuffer g_output = new StringBuffer();

    public static void main(String argv[]) throws TestException {
        Testable testable = LineRandomAccessFile.createInstanceForTesting();
        testable.test();
        System.out.print(g_output.toString());
    } // end main()
}
