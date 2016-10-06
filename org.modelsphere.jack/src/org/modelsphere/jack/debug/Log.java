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

package org.modelsphere.jack.debug;

import java.io.*;
import java.util.Date;

import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;

/**
 * This class provide services to log informations to a log file according to the configurations.
 * <p>
 * The Log can be configure using methods {@link #configureOptions(int)} and
 * {@link #configureMaxSize(int,int)}.
 * <p>
 * To start logging, use {@link #start(String,boolean)}. To stop logging, use {@link #stop()}. To
 * log informations to the log file, use {@link #add(String,int)} or {@link #add(Throwable)}.
 * <p>
 * It is not necessary to check if the Log is running (using method {@link #isRunning()}) before
 * adding log informations. Same apply for configurations.
 * <p>
 * 
 * @see org.modelsphere.jack.debug.LogDialog
 * 
 */

public final class Log {
    private static final String EOL = "\r\n"; // NOT LOCALIZABLE, End of line

    // Keys for saving preferences to file
    private static final String MAX_SIZE_PROPERTY = "MaximumSize"; // NOT LOCALIZABLE, property key
    private static final String CLEANUP_SIZE_PROPERTY = "CleanupSize"; // NOT LOCALIZABLE, property key
    private static final String CONFIG_PROPERTY = "Configuration"; // NOT LOCALIZABLE, property key

    /** Log category. Reserved for this class */
    static final int LOG_INFO = 0x0001;
    /** Log category. Used for other */
    public static final int LOG_OTHER = 0x0002;
    /** Log category. Reserved for db operations */
    public static final int LOG_DB = 0x0004;
    /** Log category. Reserved for graphic operations */
    public static final int LOG_GRAPHIC = 0x0008;
    /** Log category. Reserved for instanceof Action */
    public static final int LOG_ACTION = 0x0010;
    /** Log category. Used for statistic purposes */
    public static final int LOG_STATISTIC = 0x0020;
    /** Log category. Reserved for system informations (virtual machine, ...) */
    public static final int LOG_SYSTEM = 0x0040;
    /** Log category. Reserved for exception */
    public static final int LOG_EXCEPTION = 0x0080;
    /** Log category. Reserved for plugins management */
    public static final int LOG_PLUGIN = 0x0100;
    /** Log category. Reserved for services */
    public static final int LOG_SERVICES = 0x0400;
    /** Log category. Reserved for Tracing */
    public static final int LOG_TRACE = 0x0200;

    private static final int MAX_BLOCK_SIZE = 128 * 1024; // 128Kb
    private static final String TEMP_FILE_EXTENTION = ".tmp"; // NOT LOCALIZABLE, file extention
    private static final int MIN_LOG_FILE_SIZE = 32; // in KBytes

    private static boolean running = false;
    private static FileOutputStream stream = null;
    private static long maxSize = 1000 * 1024; // in Bytes - default 1 Mb
    private static int cleanupSize = 256 * 1024; // in Bytes - default 256 Kb
    private static int configuration = LOG_INFO | LOG_OTHER | LOG_DB | LOG_GRAPHIC | LOG_ACTION
            | LOG_STATISTIC | LOG_SYSTEM | LOG_EXCEPTION | LOG_PLUGIN | LOG_TRACE;
    private static String logFileName = null;
    private static File file = null;

    static {
        if (Debug.isDebug()) {
            PropertiesSet debugpreference = PropertiesManager
                    .getPropertiesSet(Debug.DEBUG_PROPERTIES);
            int maxsize = debugpreference.getPropertyInteger(Log.class, MAX_SIZE_PROPERTY,
                    new Integer((int) (maxSize / 1024))).intValue();
            int cleanupsize = debugpreference.getPropertyInteger(Log.class, CLEANUP_SIZE_PROPERTY,
                    new Integer((int) (cleanupSize / 1024))).intValue();
            configureMaxSize(maxsize, cleanupsize);
            int config = debugpreference.getPropertyInteger(Log.class, CONFIG_PROPERTY,
                    new Integer(configuration)).intValue();
            configureOptions(config);
        }
    }

    /**
     * Start logging in the specified file.
     * 
     * @param logfilename
     *            the fully qualified name of the file.
     * @param append
     *            specify if the Log should append to the specified file or replace it.
     * 
     */
    public static final void start(String logfilename, boolean append) {
        if (!running) {
            try {
                stream = new FileOutputStream(logfilename, append);
                logFileName = logfilename;
                file = new File(logfilename);
                running = true;
            } catch (FileNotFoundException e) {
                running = false;
            }
            checkMaxSize(0);
            add("Logging session started.", LOG_INFO); // NOT LOCALIZABLE, Debug
            add("VM: " + EOL + System.getProperties(), LOG_SYSTEM); // NOT LOCALIZABLE, Debug
        }
    }

    /**
     * Indicate if the log is running.
     * 
     * @return true if the logging session is running.
     * 
     */
    public static final boolean isRunning() {
        return running;
    }

    /**
     * Stop (disable) logging.
     */
    public static final void stop() {
        add("Logging session stopped.", LOG_INFO); // NOT LOCALIZABLE, Debug
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
            }
            stream = null;
        }
        running = false;
        file = null;
    }

    /**
     * Configure options for the log.
     * 
     * @param options
     *            specify the informations to be logged. Specified values may be any logical
     *            combination of Log categories ({@link #LOG_OTHER}, {@link #LOG_DB} ,...)
     * @see org.modelsphere.jack.debug.LogDialog
     */
    public static final void configureOptions(int options) {
        configuration = options;
        if (Debug.isDebug()) {
            PropertiesSet debugpreference = PropertiesManager
                    .getPropertiesSet(Debug.DEBUG_PROPERTIES);
            debugpreference.setProperty(Log.class, CONFIG_PROPERTY, configuration);
        }
    }

    public static final int getConfiguration() {
        return configuration;
    }

    /**
     * Return the log file maximum size in KBytes.
     */
    public static final int getMaxSize() {
        return (int) (maxSize / 1024);
    }

    /**
     * Return the log file cleanup size in KBytes.
     */
    public static final int getCleanupSize() {
        return (int) (cleanupSize / 1024);
    }

    /**
     * Configure maximum size and cleanup size for the log file. If maxsize (in KBytes) is reached,
     * the first cleanupsize (in KBytes) will be removed from the log file.
     * 
     * @param maxsize
     *            the maximum log file size in KBytes.
     * @param cleanupsize
     *            the amount of KBytes to remove from the log file when it reaches the maximum size.
     * @see org.modelsphere.jack.debug.LogDialog
     */
    public static final void configureMaxSize(int maxsize, int cleanupsize) {
        if ((maxsize * 1024) < MIN_LOG_FILE_SIZE)
            maxsize = MIN_LOG_FILE_SIZE;
        maxSize = maxsize * 1024;
        cleanupSize = ((cleanupsize < maxsize) ? cleanupsize : maxsize) * 1024;
        cleanupSize = Math.max(16 * 1024, cleanupSize);
        if (Debug.isDebug()) {
            PropertiesSet debugpreference = PropertiesManager
                    .getPropertiesSet(Debug.DEBUG_PROPERTIES);
            debugpreference.setProperty(Log.class, MAX_SIZE_PROPERTY, (int) (maxSize / 1024));
            debugpreference.setProperty(Log.class, CLEANUP_SIZE_PROPERTY,
                    (int) (cleanupSize / 1024));
        }
        checkMaxSize(0);
    }

    private static void checkMaxSize(int appendsize) {
        if (running) {
            long filesize = file.length();
            if ((filesize + appendsize) > maxSize) {
                try {
                    stream.close();
                    FileOutputStream out = new FileOutputStream(logFileName + TEMP_FILE_EXTENTION,
                            false);
                    File tempFile = new File(logFileName + TEMP_FILE_EXTENTION);
                    DataOutputStream outStream = new DataOutputStream(out);
                    FileInputStream in = new FileInputStream(logFileName);
                    DataInputStream inStream = new DataInputStream(in);

                    int removedsize = Math.max(cleanupSize,
                            (int) (filesize - maxSize + cleanupSize)); // check for modified max size

                    // remove cleanupsize
                    inStream.skipBytes(removedsize);
                    // copy remaining in the temp file
                    byte[] data = new byte[MAX_BLOCK_SIZE];
                    int blockreadsize = 0;
                    do {
                        blockreadsize = inStream.read(data);
                        if (blockreadsize > 0)
                            outStream.write(data, 0, blockreadsize);
                    } while (blockreadsize > 0);
                    outStream.close();
                    inStream.close();
                    out.close();
                    in.close();

                    file.delete();
                    tempFile.renameTo(file);
                    tempFile.delete();
                    stream = new FileOutputStream(logFileName, true);
                } catch (Exception e) {
                    running = false;
                }
            }
        }
    }

    /**
     * Add the following log information to the log file if logging is enabled and if the specified
     * category is set in the log options.
     * 
     * @param info
     *            the information to add. Note that the current time information is added
     *            automatically
     * @param infocategory
     *            the category of the information. Should be any integer identifying a category (
     *            {@link #LOG_OTHER}, {@link #LOG_DB} ,...)
     */
    public static final void add(String info, int infocategory) {
        if (running && ((infocategory & configuration) != 0)) {
            long timemilli = System.currentTimeMillis();
            Date date = new Date(timemilli);
            String logblock = "[" + infocategory + "] " + date.toString() + " (" + timemilli
                    + "):  "; // NOT LOCALIZABLE, Debug
            logblock = logblock.concat(info + EOL);
            checkMaxSize(logblock.length() * 2); // *2 because length is amount of 16 bits unicode
            try {
                stream.write(logblock.getBytes());
            } catch (IOException e) {
                running = false;
            }
        }
    }

    /**
     * Add log information for the specified Throwable.
     */
    public static final void add(Throwable th) {
        String info = (th == null) ? "" : th.toString();
        add(info, LOG_EXCEPTION);
    }
}
