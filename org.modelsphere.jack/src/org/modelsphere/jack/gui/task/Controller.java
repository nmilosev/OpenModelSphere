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

package org.modelsphere.jack.gui.task;

import java.io.FileWriter;
import java.util.Date;
import java.text.DateFormat;

import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.text.MessageFormat;

public class Controller {
    protected static final String kAborted = LocaleMgr.screen.getString("Aborted");
    protected static final String kAborting = LocaleMgr.screen.getString("Aborting");
    protected static final String kAbortingWait = LocaleMgr.screen.getString("Aborting..");
    protected static final String kCompletedError = LocaleMgr.screen.getString("CompletedError");
    protected static final String kCompletedWarning = LocaleMgr.screen
            .getString("CompletedWarning");
    protected static final String kElapsedTime = LocaleMgr.screen.getString("ElapsedTime_");
    protected static final String kCanceled = LocaleMgr.screen.getString("{0}canceled");
    protected static final String kError = LocaleMgr.screen.getString("Error");
    protected static final String kInProgress = LocaleMgr.screen.getString("InProgress");
    protected static final String kDefaultJobName = LocaleMgr.screen.getString("defaultJobName");
    protected static final String kCanceledErrorCount = LocaleMgr.screen
            .getString("{0}{1}cancelErrorCount");
    protected static final String kCanceledWarningCount = LocaleMgr.screen
            .getString("{0}{1}cancelWarningCount");
    protected static final String kCanceledWECount = LocaleMgr.screen
            .getString("{0}{1}{2}cancelWECount");
    protected static final String kCompletedErrorCount = LocaleMgr.screen
            .getString("{0}{1}completedErrorCount");
    protected static final String kCompletedWarningCount = LocaleMgr.screen
            .getString("{0}{1}completedWarningCount");
    protected static final String kCompletedWECount = LocaleMgr.screen
            .getString("{0}{1}{2}completedWECount");
    protected static final String kCompleted = LocaleMgr.screen.getString("{0}completedSuccess");
    protected static final String kCompleted2 = LocaleMgr.screen.getString("completedSuccess");
    protected static final String kFatalError = LocaleMgr.screen.getString("FatalError");
    protected static final String kSaveReportAs = LocaleMgr.screen.getString("SaveReportAs");

    public static final int STATE_NONE = 0; // Initial state
    public static final int STATE_IN_PROGRESS = 1; // Intermediate state
    public static final int STATE_ABORTING = 2; // Intermediate state
    public static final int STATE_ABORTED = 3; // Final state
    public static final int STATE_COMPLETED = 4; // Final state
    public static final int STATE_ERROR = 5; // Final state

    protected int errorsCount = 0;
    protected int warningsCount = 0;
    protected String jobTitle = null;
    protected boolean errorsCountEnabled = true;
    protected FileWriter logFileWriter;
    protected int jobDone = 0;

    // State property
    private int m_state = STATE_NONE;

    protected synchronized void setState(int newState) {
        m_state = newState;
    }

    public final synchronized int getState() {
        return m_state;
    } // return the actual state

    public Controller() {
    }

    // Counter
    private Long m_counter = null;

    public final synchronized void setCounter(Long counter) {
        m_counter = counter;
    }

    public final synchronized Long getCounter() {
        return m_counter;
    }

    public final synchronized String getAbortedString() {
        return kAborted;
    }

    public final synchronized int getWarningsCount() {
        return warningsCount;
    }

    public final synchronized int getErrorsCount() {
        return errorsCount;
    }

    public final synchronized void incrementWarningsCounter() {
        warningsCount++;
    }

    public final synchronized void incrementErrorsCounter() {
        errorsCount++;
    }

    public void start(final Worker worker) {
        worker.start(this);
    } // end start()

    //
    // print methods
    //
    public void print(String s) {
        System.out.print(s);
    } // end println()

    public void println(String s) {
        System.out.println(s);
    } // end println()

    public void println() {
        System.out.println();
    } // end println()

    public synchronized void processException(Throwable t) {
        println(kFatalError + " " + t.getMessage());
        println();
        cancel();
        setState(STATE_ERROR);
    }

    // Cancel the reverse job
    // Can be called at any time
    public void cancel() {
        if (m_state == STATE_IN_PROGRESS) {
            setState(STATE_ABORTING);
        }
    } // end cancel()

    public final synchronized boolean isFinalState() {
        if ((m_state == STATE_ABORTED) || (m_state == STATE_COMPLETED) || (m_state == STATE_ERROR)) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean checkPoint(int jobDone) {
        if (jobDone > 100)
            jobDone = 100;
        else if (jobDone < 0)
            jobDone = 0;
        this.jobDone = jobDone;

        return m_state == STATE_IN_PROGRESS;
    }

    // return true if job can continue
    public final synchronized boolean checkPoint() {
        return m_state == STATE_IN_PROGRESS;
    }

    void unlock() {
    }

    public void setStatusText(final String text) {
    }

    // Called by the worker to signal a completion of the job (canceled or
    // successfull)
    final void terminate() {
        println("");
        int errorsCount = getErrorsCount();
        int warningsCount = getWarningsCount();
        int state = getState();
        if (state == STATE_ABORTED || state == STATE_ABORTING) {
            if (errorsCountEnabled && errorsCount > 0 && warningsCount > 0) {
                println(MessageFormat.format(kCanceledWECount, new Object[] { jobTitle,
                        new Integer(errorsCount), new Integer(warningsCount) }));
            } else if (errorsCountEnabled && errorsCount > 0) {
                println(MessageFormat.format(kCanceledErrorCount, new Object[] { jobTitle,
                        new Integer(errorsCount) }));
            } else if (errorsCountEnabled && warningsCount > 0) {
                println(MessageFormat.format(kCanceledWarningCount, new Object[] { jobTitle,
                        new Integer(warningsCount) }));
            } else {
                println(MessageFormat.format(kCanceled, new Object[] { jobTitle }));
            } // end if
        } else if (state == STATE_ERROR) {
            println(kFatalError);
        } else {
            if (errorsCountEnabled && errorsCount > 0 && warningsCount > 0)
                println(MessageFormat.format(kCompletedWECount, new Object[] { jobTitle,
                        new Integer(errorsCount), new Integer(warningsCount) }));
            else if (errorsCountEnabled && errorsCount > 0)
                println(MessageFormat.format(kCompletedErrorCount, new Object[] { jobTitle,
                        new Integer(errorsCount) }));
            else if (errorsCountEnabled && warningsCount > 0)
                println(MessageFormat.format(kCompletedWarningCount, new Object[] { jobTitle,
                        new Integer(warningsCount) }));
            else
                println(MessageFormat.format(kCompleted, new Object[] { jobTitle }));
        }

        Date date = new Date(System.currentTimeMillis());
        String localeTime = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG)
                .format(date);
        localeTime = Character.toTitleCase(localeTime.charAt(0)) + localeTime.substring(1);
        println(localeTime);

        try {
            if (logFileWriter != null)
                logFileWriter.close();
        } catch (Exception e1) {
        }
        setState(getState() == STATE_ABORTING ? STATE_ABORTED : STATE_COMPLETED);
    } // end terminate

} // end TextController

