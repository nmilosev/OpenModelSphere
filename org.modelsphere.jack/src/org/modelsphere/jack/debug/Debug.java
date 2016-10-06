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

/* This class offers some debug-related facilities */
public class Debug {

    /* IMPORTANT: set DEBUG to false for a release version */
    private static final boolean DEBUG = false;
    private static final String DEBUG_STRING = "[DEBUG]"; //indicates that the line is not displayed when debug in disabled  //NOT LOCALIZABLE - Debug only

    //call enableTrace(false) to disable trace output and thus
    //improve performance
    private static boolean g_traceEnabled = true;

    public static final void enableTrace(boolean b) {
        g_traceEnabled = b;
        if (isDebug()) { // Do not replace with Debug.trace() ... tracing may be disabled!
            String line = DEBUG_STRING + "Tracing " + (b ? "enabled" : "disabled"); // NOT LOCALIZABLE
            System.out.println(line); // NOT LOCALIZABLE - Debug only
            Log.add(line, Log.LOG_TRACE);
        }
    }

    /* Constants */
    public static final int DEFAULT = 0;
    public static final int DISPLAY = 1;
    private static final String ASSERTION_FAILED = "Assertion failed"; //NOT LOCALIZABLE, for debugging purposes only

    public static final String DEBUG_PROPERTIES = "Debug"; //NOT LOCALIZABLE, property key
    public static final String DEBUG_PROPERTIES_FILE_NAME = "debug.properties"; //NOT LOCALIZABLE, debug only

    static {
        if (DEBUG) {
            org.modelsphere.jack.preference.PropertiesManager.installPropertiesSet(
                    DEBUG_PROPERTIES, new java.io.File(System.getProperty("user.dir")
                            + System.getProperty("file.separator") + DEBUG_PROPERTIES_FILE_NAME)); //NOT LOCALIZABLE, property key
        }
    }

    public static final boolean isDebug() {
        return DEBUG;
    }

    /* This method is provided because debug breakpoints are buggy on JBuilder */
    public static final void breakpoint() {
        if (DEBUG)
            System.out.println(DEBUG_STRING + "breakpoint"); // Set a breakpoint on this line   //NOT LOCALIZABLE, debug purpose
    }

    /* Default Exception handling */
    public static final void handleException(Exception ex) {
        if (DEBUG) {
            org.modelsphere.jack.util.ExceptionHandler.processUncatchedException(null, ex);
        }
        // else, stay silent and log
        else {
            Log.add(ex);
        }
    }

    public static final void assert2(boolean condition, String errorMessage) {
        if (DEBUG && condition == false)
            throw new AssertionFailedException(errorMessage);
    }

    public static final void assert2(boolean condition) {
        assert2(condition, ASSERTION_FAILED);
    }

    // Tracing functions, disabled in NON-DEBUG version
    public static final void trace(Object o) {
        if (DEBUG) {
            if (g_traceEnabled) { //can be disabled in DEBUG version
                String line = DEBUG_STRING + o;
                System.out.println(line);
                Log.add(line, Log.LOG_TRACE);
                if (o instanceof Throwable) {
                    ((Throwable) o).printStackTrace();
                }
            }
        } //end if
    } //end trace()

    public static final void trace() {
        if (DEBUG) {
            if (g_traceEnabled) { //can be disabled in DEBUG version
                System.out.println();
            }
        } //end if
    } //end trace()

    public static final void printStackTrace(Exception ex) {
        if (DEBUG) {
            ex.printStackTrace(System.out);
        } //end if
    } //end printStackTrace()

    // ******************
    // PROFILING FEATURES
    // ******************

    /********************
     * HOW TO USE PROFILING Debug.Profile profile = Debug.newProfile(2); //start a new profile
     * 
     * //Which task slows down the loop??? while (! done) { profile.markTask(0); longTaskA();
     * profile.unmarkTask(0);
     * 
     * profile.markTask(1); longTaskB(); profile.unmarkTask(1); } //end while
     * 
     * //Display results profile.showResults(); profile.resetResults();
     * 
     * //OUTPUT (in System.out) : [DEBUG] Task #0: 50 ms; [DEBUG] Task #1: 3950 ms;
     */

    public static Profile newProfile(int nbTasks) {
        Profile profile = new Profile(nbTasks);
        return profile;
    }

    public static final class Profile {
        private int m_nbTasks;
        private long[] m_times;
        private long[] m_cumulativeTimes;

        Profile(int nbTasks) {
            m_nbTasks = nbTasks;
            m_times = new long[nbTasks];
            m_cumulativeTimes = new long[nbTasks];
            for (int i = 0; i < nbTasks; i++) {
                m_cumulativeTimes[i] = 0;
            }
        }

        public void markTask(int taskId) {
            m_times[taskId] = System.currentTimeMillis();
        }

        public void unmarkTask(int taskId) {
            long elapsedTime = System.currentTimeMillis() - m_times[taskId];
            m_cumulativeTimes[taskId] += elapsedTime;
        }

        public void showResults() {
            for (int i = 0; i < m_nbTasks; i++) {
                Debug.trace(" Task #" + i + ": " + m_cumulativeTimes[i] + "ms.");
            }
        }

        public void resetResults() {
            for (int i = 0; i < m_nbTasks; i++) {
                m_cumulativeTimes[i] = 0;
            }
        }
    } //end Profile
} //end Debug
