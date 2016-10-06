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
import java.util.StringTokenizer;

/**
 * 
 * A class that encodes bytes into printable characters.
 * 
 * @author Marco Savard
 * 
 */
public class UUEncoder {

    private static final String CHAR_MAPPING = "@ABCDEFGHIJKLMNOPQRSTUVWXYZ01234$abcdefghijklmnopqrstuvwxyz56789";

    // Implements Singleton design pattern
    private static UUEncoder g_singleInstance = null;

    private UUEncoder() {
    }

    public static UUEncoder getSingleton() {
        if (g_singleInstance == null)
            g_singleInstance = new UUEncoder();

        return g_singleInstance;
    } // end getSingleton()

    //
    // PUBLIC METHODS
    //
    public String fromBytesToString(byte[] array) {
        OutputStream bytestream = new ByteArrayOutputStream();
        int nbIters = (array.length + 2) / 3; // if len = 11, we have 4
        // iterations
        try {
            for (int i = 0; i < nbIters; i++) {
                if ((i % 25) == 0) { // insert a new line at each 100 characters
                    newLine(bytestream);
                }

                int length = ((i + 1) == nbIters) ? (array.length % 3) : 3;
                if (length == 0)
                    length = 3;
                encodeAtom(bytestream, array, i * 3, length);
            } // end for
        } catch (IOException ex) {
            //
        } // end try

        String strValue = bytestream.toString();
        return strValue;
    } // end fromBytesToString()

    public byte[] fromStringToBytes(String strValue) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        StringTokenizer st = new StringTokenizer(strValue, NEWLINE);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();

            int nbIters = (token.length() + 3) / 4; // if len = 11, we have 3
            // iterations
            try {
                byte[] buffer = new byte[4];
                for (int i = 0; i < nbIters; i++) {
                    buffer[0] = (byte) token.charAt(i * 4);
                    buffer[1] = (byte) token.charAt(i * 4 + 1);
                    buffer[2] = (byte) token.charAt(i * 4 + 2);
                    buffer[3] = (byte) token.charAt(i * 4 + 3);
                    InputStream inStream = new ByteArrayInputStream(buffer);

                    int len = ((i + 1) == nbIters) ? (token.length() % 4) : 3;
                    if (len == 0)
                        len = 3;
                    decodeAtom(inStream, outStream, len);
                } // end for
            } catch (IOException ex) {
            } // end try
        } // end while

        return outStream.toByteArray();
    } // end fromStringToBytes()

    public byte[] fromIntsToBytes(int[] array) {
        int len = array.length;
        byte[] bytes = new byte[len * 4];
        for (int i = 0; i < len; i++) {
            bytes[i * 4] = (byte) (array[i] & 0x000000ff);
            bytes[(i * 4) + 1] = (byte) ((array[i] & 0x0000ff00) / 0x100);
            bytes[(i * 4) + 2] = (byte) ((array[i] & 0x00ff0000) / 0x10000);
            bytes[(i * 4) + 3] = (byte) ((array[i] & 0xff000000) / 0x1000000);
        } // end for

        return bytes;
    } // end fromIntsToBytes()

    public int[] fromBytesToInts(byte[] bytes) {
        int len = bytes.length / 4;
        int[] ints = new int[len];
        for (int i = 0; i < len; i++) {
            int b1 = (0x100 + bytes[i * 4]) % 0x100;
            int b2 = (0x100 + bytes[(i * 4) + 1]) % 0x100;
            int b3 = (0x100 + bytes[(i * 4) + 2]) % 0x100;
            int b4 = (0x100 + bytes[(i * 4) + 3]) % 0x100;
            ints[i] = b1 | b2 * 0x100 | b3 * 0x10000 | b4 * 0x1000000;
        } // end for

        return ints;
    } // end fromBytesToInts()

    //
    // PRIVATE METHODS
    //

    /**
     * encodeAtom - take three bytes and encodes them into 4 characters If len is less than 3 then
     * remaining bytes are filled with '1'. This insures that the last line won't end in spaces and
     * potentiallly be truncated.
     */
    private void encodeAtom(OutputStream outStream, byte data[], int offset, int len)
            throws IOException {
        byte a, b = 1, c = 1;
        int c1, c2, c3, c4;

        a = data[offset];
        if (len > 1) {
            b = data[offset + 1];
        }
        if (len > 2) {
            c = data[offset + 2];
        }

        c1 = (a >>> 2) & 0xff;
        c2 = ((a << 4) & 0x30) | ((b >>> 4) & 0xf);
        c3 = ((b << 2) & 0x3c) | ((c >>> 6) & 0x3);
        c4 = c & 0x3f;
        c1 = CHAR_MAPPING.charAt(c1 % 64);
        c2 = CHAR_MAPPING.charAt(c2 % 64);
        c3 = CHAR_MAPPING.charAt(c3 % 64);
        c4 = CHAR_MAPPING.charAt(c4 % 64);
        outStream.write(c1);
        outStream.write(c2);
        outStream.write(c3);
        outStream.write(c4);
        return;
    } // end encodeAtom()

    /**
     * Decode a UU atom. Note that if l is less than 3 we don't write the extra bits, however the
     * encoder always encodes 4 character groups even when they are not needed.
     */
    private void decodeAtom(InputStream inStream, OutputStream outStream, int l) throws IOException {
        int i, c1, c2, c3, c4;
        int a, b, c;
        StringBuffer x = new StringBuffer();
        byte decoderBuffer[] = new byte[4];

        for (i = 0; i < 4; i++) {
            c1 = inStream.read();
            if (c1 == -1) {
                // throw new CEStreamExhausted();
            }
            x.append((char) c1);
            int pos = CHAR_MAPPING.indexOf(c1);
            decoderBuffer[i] = (byte) (pos & 0x3f);
        } // end for
        a = ((decoderBuffer[0] << 2) & 0xfc) | ((decoderBuffer[1] >>> 4) & 3);
        b = ((decoderBuffer[1] << 4) & 0xf0) | ((decoderBuffer[2] >>> 2) & 0xf);
        c = ((decoderBuffer[2] << 6) & 0xc0) | (decoderBuffer[3] & 0x3f);

        byte b1 = (byte) (a & 0xff);
        byte b2 = (byte) (b & 0xff);
        byte b3 = (byte) (c & 0xff);

        outStream.write(b1);
        if (l > 1) {
            outStream.write(b2);
        }
        if (l > 2) {
            outStream.write(b3);
        }
    } // end decodeAtom()

    private static String NEWLINE = System.getProperty("line.separator") + " "; // /r,

    // /n
    // and
    // ' '
    // are
    // considered
    // line
    // terminators
    private void newLine(OutputStream bytestream) throws IOException {
        int len = NEWLINE.length();
        for (int i = 0; i < len; i++) {
            bytestream.write(NEWLINE.charAt(i));
        } // end for

    } // end newLine()

    //
    // UNIT TEST
    //
    public static void main(String args[]) {

        int buflen = 500;
        byte[] array = new byte[buflen];
        for (int i = 0; i < buflen; i++) {
            array[i] = (byte) ((i * 7) % 256);
        }

        UUEncoder encoder = UUEncoder.getSingleton();
        String strValue = encoder.fromBytesToString(array);
        byte[] newArray = encoder.fromStringToBytes(strValue);
        System.out.println(strValue);
    } // end main()

} // end UUEncoder
