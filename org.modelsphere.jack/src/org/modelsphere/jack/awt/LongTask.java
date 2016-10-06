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

package org.modelsphere.jack.awt;

public abstract class LongTask {
    private int lengthOfTask;
    private int current = 0;
    private int currentStep = 0;
    private int nextStep = 0;
    private String statMessage;
    private boolean cancelRequiredByUser = false; // User has pressed Cancel
    // button.
    private boolean enableCancel = true;
    private boolean terminate = false;

    public LongTask() {
        // set length of task ...
        lengthOfTask = 0;
    }

    public LongTask(int aLengthOfTask) {
        // set length of task ...
        lengthOfTask = aLengthOfTask;
    }

    // Allows subclasses to compute length of the task
    protected void setLengthOfTask(int aLengthOfTask) {
        lengthOfTask = aLengthOfTask;
    }

    public final void incrementProgress() {
        current += 1;
    }

    public final void incrementProgress(int increment) {
        current += increment;
    }

    /*
     * this is an example of method go()
     * 
     * public void go() { final SwingWorker worker = new SwingWorker() { public Object construct() {
     * return new ActualTask(); } }; }
     */
    /**
     * Called from ProgressBarDemo to start the task
     */
    public abstract void go();

    // Called from ProgressBarDemo to find out how much work needs to be done
    final public int getLengthOfTask() {
        return lengthOfTask;
    }

    // Called from ProgressBarDemo to find out how much has been done
    final public int getCurrent() {
        return current;
    }

    /**
     * A particular step has been reached during the long task progression: set the name & size of
     * the next step.
     * 
     * @param aNote
     *            Text to be displayed to indicate new step (generally current file name)
     * @param increment
     *            size of the next step (generally size of current file in blocks)
     */
    /*
     * final public void setNextStep(String aNote, int nextStepSize) { // note = aNote; currentStep
     * = nextStep; nextStep += nextStepSize; current = currentStep; }
     */

    /**
     * Increment progress within limits of the current step
     * 
     * @param stepProgress
     *            integer between 0 and 100 indicating which percentage of current step has been
     *            done. 0 means beginning of the current step and 100 the end of the current step.
     */
    /*
     * final public void setCurrentStepProgress(int percentage) { try {
     * org.modelsphere.jack.debug.Debug.assert((percentage >= 0) && (percentage <= 100),
     * "setCurrentProgress() expects an integer in the range [0-100].");
     * 
     * current = currentStep + (nextStep - currentStep) * (percentage / 100); } catch
     * (org.modelsphere.jack.debug.AssertionFailedException ex) { //ignore it } }
     */

    /*
     * Set directly the current progression, notwithstanding the steps previously defined. If
     * possible, use setNextStep() & setCurrentStepProgress() instead of setCurrent().
     */
    final public void setCurrent(int aCurrent) {
        current = aCurrent;
    }

    public void terminate() {
        stop();
    }

    final public void stop() {
        current = lengthOfTask;
        terminate = true;
    }

    // called from ProgressBarDemo to find out if the task has completed
    final public boolean done() {
        return terminate;
    }

    final public String getMessage() {
        return statMessage;
    }

    final public void enableCancel(boolean value) {
        enableCancel = value;
    }

    final public boolean cancelIsEnable() {
        return enableCancel;
    }

    final public boolean isCancel() {
        return cancelRequiredByUser;
    }

    final public void setCancel(boolean value) {
        cancelRequiredByUser = value;
    }

}
