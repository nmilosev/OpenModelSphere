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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Date;

import org.modelsphere.jack.debug.Debug;

/**
 * This class and the Controller class provide a framework to handle and execute a task.
 * 
 * Subclasses should only implements methods runJob() and getJobTitle().
 * 
 * IMPORTANT: runJob() method should check getController().checkPoint() to determine if the method
 * should return or continue the job. Do not call stop methods or interrupt. Instead, use the
 * checkPoint() flag of the controller to return from the runJob() method.
 * 
 * If a Db transaction is needed, the runJob() method is responsible for starting and commiting the
 * transaction. (or must be done inside another thread managed within runJob()).
 * 
 * New Thread created within runJob() must also verify with getController().checkPoint() to ensure
 * they return from their run() method if the operation is canceled. An Exception may occurs in
 * 'inner' thread, make sure to check for (getController().getState() != Controller.STATE_ERROR) to
 * avoid more exceptions to occurs since the first one may have corrupted the state of one or more
 * objects.
 * 
 * You may use method startNewThread(Thread) to ensure that a join with this worker thread is done.
 * That means that this worker thread will wait for the completion of all threads started with
 * startNewThread(Thread) before returning from its run() method and terminating (normally, by
 * request or abnormally). The controller allows disposition of the dialog after a call to terminate
 * by this worker. This call will allow the user to dispose of the dialog. This may cause problems
 * (like Db transactions in different threads). Joining threads ensure that when the user will
 * dispose of the dialog, all threads will have completed. If you don't use this method to start
 * other threads, you must ensure that the join is done properly.
 * 
 * Also note that runJob() should not catch Exception that can not be handled properly. These
 * exceptions will be managed properly by the controller.
 * 
 */

public abstract class Worker extends Thread {
    public static final String EOL = System.getProperty("line.separator"); // NOT LOCALIZABLE, property key

    private Controller controller;

    // protected int errorCount = 0;
    private boolean errorCountEnabled = true;

    private ArrayList subThreads = new ArrayList();

    public Worker() {
        super(new ThreadGroup("worker group"), "main worker"); // NOT LOCALIZABLE, thread name not visible to user
    }

    // This method must only be accessed by the Controller class
    final void start(Controller controller) {
        Debug.assert2(controller != null, "Null Controller not allowed."); // NOT LOCALIZABLE, debug only
        this.controller = controller;
        // The low priority ensure a fast gui feedback to the user if needed
        // (the EventDispatchThread has a priority of 1) without affecting
        // performance.
        // All sub thread will have the same priority
        setPriority(Thread.MIN_PRIORITY);
        start();
    }

    // If true, a summary of the errorCount will be given to user
    protected final void setErrorCountEnabled(boolean enable) {
        errorCountEnabled = enable;
    }

    // Return the controller managing this Worker
    protected final Controller getController() {
        return controller;
    }

    // This method override run() method from Thread. Specific Job must be
    // implemented in method runJob().
    // This method control the runJob() method for handling exceptions properly.
    public final void run() {
        Debug.assert2(controller != null, "Null Controller not allowed."); // NOT LOCALIZABLE, debug only
        Debug.trace("Start:  " + new Date(System.currentTimeMillis()));
        try {
            runJob();
            controller.terminate();
            joinAllThread();
            subThreads.clear();
            subThreads = null;
            // after this call, we should return from run() (the application
            // frame GUI controls are available to the user).
            controller.unlock();
            controller = null;
        } catch (Exception e) {
            controller.processException(e);
            subThreads.clear();
            subThreads = null;
            controller = null;
        } catch (Error er) {
            controller.processException(er);
            subThreads.clear();
            subThreads = null;
            controller = null;
        }
        Debug.trace("Finish:  " + new Date(System.currentTimeMillis()));
    }

    // this method will ensure that all threads get the time to make a
    // checkPoint() and terminate before
    // a dispose() on the dialog occurs
    private void joinAllThread() {
        Iterator iter = subThreads.iterator();
        while (iter.hasNext()) {
            Thread t = (Thread) iter.next();
            if (t.isAlive()) {
                try {
                    t.join();
                } catch (InterruptedException e) {
                }
            }
        }

    }

    // Utility method to start a new Thread in the worker. Will ensure that each
    // thread has returned from their run
    // method before terminaging
    protected final void startNewThread(final Thread subThread) {
        if (subThread != null) {
            subThreads.add(subThread);
            subThread.start();
        }
    }

    // Implement the task in this method
    // IMPORTANT: Any exceptions that cannot be handled by the subclass or that
    // can corrupt the operation should be thrown by this method (not cought by
    // this method)
    // If more threads are needed, make sure they make a checkPoint() on a
    // regular basis on the controller
    // (To allow a return from their run() method).
    protected abstract void runJob() throws Exception;

    // Return this job's title
    protected abstract String getJobTitle();

}
