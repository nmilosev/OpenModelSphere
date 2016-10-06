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

/**
 * 
 * A writer that writes a temporary file and automatically deletes it. Cannot inherit directly from
 * FileWriter because cannot call its constructor
 * 
 * @author Marco Savard
 * 
 */
public final class TemporaryFileWriter extends Writer {
    private static final String PREF = "zzz"; // NOT LOCALIZABLE
    private static final String SUFF = "zzz"; // NOT LOCALIZABLE
    private File m_file;
    private FileWriter m_writer;
    private boolean deleteOnExit = true; // by default

    // Create a temporary file
    public TemporaryFileWriter() throws IOException {
        m_file = File.createTempFile(PREF, SUFF);
        m_writer = new FileWriter(m_file);
    }

    // ////////////////////////
    // OVERRIDES Object
    public String toString() {
        try {
            m_writer.flush();
            FileReader reader = new FileReader(m_file);
            StringWriter writer = new StringWriter();
            int buflen = 1024;
            char[] buffer = new char[buflen];
            int bytesRead;
            do {
                bytesRead = reader.read(buffer, 0, buflen);
                if (bytesRead > 0) {
                    writer.write(buffer, 0, bytesRead);
                }
            } while (bytesRead > 0);

            return writer.toString();
        } catch (IOException ex) {
            return ex.toString();
        }
    }

    //
    // ////////////////////////

    // ////////////////////////
    // OVERRIDES Writer
    public void write(char[] cbuf, int off, int len) throws IOException {
        m_writer.write(cbuf, off, len);
    }

    public void flush() throws IOException {
        m_writer.flush();
    }

    public void close() throws IOException {
        m_writer.close();
    }

    //
    // ////////////////////////

    // Dump the content of a reader into the temporary file
    public void dump(Reader reader) throws IOException {
        try {
            char[] cbuf = new char[1024];
            int nbChars;
            do {
                nbChars = reader.read(cbuf, 0, 1023);
                if (nbChars <= 0) {
                    break;
                }
                write(cbuf, 0, nbChars);
            } while (true);
        } catch (EOFException ex) {
            // expecting this exception to exit from the loop
        }

        // complete the dump
        flush();
    }

    public File getFile() {
        return m_file;
    }

    public void cleanup() {
        m_file.delete();
        m_file = null;
        m_writer = null;
        deleteOnExit = false;
    }

    // when this object is finalized, and has not been deleted yet,
    // then ask VM to delete the temporary file after application's end
    // of session.
    public void finalize() {
        if (deleteOnExit) {
            m_file.deleteOnExit();
        }
    }
}
