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

import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.text.DateFormat;
import java.util.Calendar;
import javax.swing.*;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.baseDb.db.Db;
import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.jack.srtool.forward.AbortRuleException;
import org.modelsphere.jack.text.MessageFormat;
import org.modelsphere.jack.util.ExceptionHandler;

/**
 * This class used with class Worker provides a thread safe way to control a task It provides log
 * file support, cancel request from user, exceptions handling, and some outputs to user. Subclasses
 * should only provide the GUI components.
 */

public abstract class GuiController extends Controller implements ActionListener {

    public static final String EOL = System.getProperty("line.separator"); // NOT LOCALIZABLE, property key

    private static final String HOURS_MIN_SEC = LocaleMgr.screen.getString("{0}{1}{2}HoMinSec");
    private static final String MIN_SEC = LocaleMgr.screen.getString("{0}{1}MinSec");
    private static final String SEC = LocaleMgr.screen.getString("{0}Sec");
    private boolean disposable = false;

    private Window window;
    private JLabel statusLabel;
    private JButton okButton;
    private JButton cancelButton;

    private JButton saveAsButton;

    private JTextArea textArea;

    private JLabel timeElapsedLabel;
    private JProgressBar progressBar;

    private JLabel iconLabel;
    private Icon defaultIcon;

    private Calendar calendar;
    private long lastTime = 0;
    private long startTime = 0;
    private Worker worker;

    private boolean closeOnTerminate = false;
    private Runnable runnable = null;

    public JDialog getDialog() {
        return null;
    }

    // start the job - window parameter can be null (no GUI feedback)
    // if a log file is specified, all output will also be sent to log file
    public void start(final Worker worker) {
        Debug.assert2(worker != null, "Null ReverseWorker not allowed in ReverseController"); // NOT
        // LOCALIZABLE,
        // debug
        // only

        okButton = getOKButton();
        cancelButton = getCancelButton();
        saveAsButton = getSaveAsButton();

        if (cancelButton != null) {
            cancelButton.setEnabled(true);
            cancelButton.addActionListener(this);
        }
        if (okButton != null) {
            okButton.setEnabled(false);
            okButton.addActionListener(this);
        }
        if (saveAsButton != null) {
            saveAsButton.setEnabled(false);
            saveAsButton.addActionListener(this);
        }

        logFileWriter = getLogFileWriter();

        textArea = getTextArea();
        if (textArea != null)
            textArea.setLineWrap(true);

        statusLabel = getStatusLabel();
        timeElapsedLabel = getTimeElapsedLabel();
        progressBar = getProgressBar();
        iconLabel = getIconLabel();
        if (iconLabel != null) {
            iconLabel.setText("");
            defaultIcon = getDefaultIcon();
            iconLabel.setIcon(defaultIcon);
        }

        if (progressBar != null) {
            progressBar.setMaximum(100);
            progressBar.setMinimum(0);
            progressBar.setStringPainted(true);
        }

        // This is a workaround to avoid a dead lock between a call to
        // invalidate() in the JTree renderer (by the way of a setTooltip or
        // setFont)
        // within the main worker thread (occurs after a db.commitTrans() -
        // refresh listeners - node inserted)
        // and the EventDispatchThread (repaint manager - tree renderer -
        // setFont ,...)
        // This problem is not systematic (must have some expands in the
        // explorer)
        DefaultMainFrame mf = ApplicationContext.getDefaultMainFrame();
        if (mf != null) {
            mf.lockDbRefreshInGUI(this);
        }
        // End of workaround

        SwingUtilities.invokeLater(new Runnable() { // Make sure the window is
                    // visible (especially for
                    // modal)
                    public void run() {
                        setState(STATE_IN_PROGRESS);
                        setStartTime(System.currentTimeMillis());
                        initCalendar(Calendar.getInstance());

                        jobTitle = worker.getJobTitle();
                        if (jobTitle == null)
                            jobTitle = kDefaultJobName;
                        print(jobTitle + EOL);
                        Date date = new Date(System.currentTimeMillis());
                        String localeTime = DateFormat.getDateTimeInstance(DateFormat.FULL,
                                DateFormat.LONG).format(date);
                        localeTime = Character.toTitleCase(localeTime.charAt(0))
                                + localeTime.substring(1);
                        print(localeTime);

                        worker.start(GuiController.this);
                        if (timeElapsedLabel != null) {
                            (new Thread("controller timer") { // NOT
                                // LOCALIZABLE,
                                // thread name
                                public void run() {
                                    while (checkPoint()) {
                                        doUpdate(new Runnable() {
                                            public void run() {
                                                updateTime();
                                            }
                                        });
                                        try {
                                            sleep(2000);
                                        } catch (InterruptedException e) {
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                });

        window = getWindow();
        if (window != null) {
            window.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    dispose_();
                }
            });
            if (window instanceof JDialog)
                ((JDialog) window).setLocationRelativeTo(ApplicationContext.getDefaultMainFrame());
            window.setVisible(true);
        } else
            Debug.assert2(false, "Controller:  Null Window. "); // NOT
        // LOCALIZABLE,
        // debug only
    }

    // If true, this controller window will be disposed without the user
    // intervention.
    public void setCloseOnTerminate(boolean b) {
        closeOnTerminate = b;
    }

    // The specified Runnable will be executed on the event dispatch thread
    // after disposition
    // of this Controller window.
    // Allow chained job.
    public void invokeOnDispose(Runnable r) {
        runnable = r;
    }

    // Called on successfull completion for disposing of the window.
    private void dispose_() {
        if (getState() == STATE_IN_PROGRESS)
            cancel();
        if (isDisposable()) {
            window.dispose();
            ApplicationContext.getDefaultMainFrame().unlockDbRefreshInGUI(this);
        }
    }

    // Cancel the reverse job
    // Can be called at any time
    public final void cancel() {
        if (getState() == STATE_IN_PROGRESS) {
            setState(STATE_ABORTING);
            if (cancelButton != null) {
                doUpdate(new Runnable() {
                    public void run() {
                        cancelButton.setEnabled(false);
                        if (iconLabel != null)
                            iconLabel.setIcon(defaultIcon);
                    }
                });
            }
            println(kAbortingWait + EOL);
        }
    }

    private synchronized boolean isDisposable() {
        return disposable;
    }

    private synchronized void setDisposable() {
        disposable = true;
    }

    // return true if job can continue
    // jobDone: The percentage of job completed
    public synchronized boolean checkPoint(int jobDone) {
        if (jobDone > 100)
            jobDone = 100;
        else if (jobDone < 0)
            jobDone = 0;
        this.jobDone = jobDone;

        if (progressBar != null) {
            progressBar.setValue(this.jobDone);
            progressBar.setString(this.jobDone + "%"); // NOT LOCALIZABLE,
        }
        return getState() == STATE_IN_PROGRESS;
    }

    public final synchronized void setErrorsCountEnabled(boolean enable) {
        errorsCountEnabled = enable;
    }

    public final synchronized boolean isErrorsCountEnabled() {
        return errorsCountEnabled;
    }

    private synchronized void setStartTime(long startTime) {
        this.startTime = startTime;
        this.lastTime = startTime;
    }

    private synchronized void initCalendar(Calendar calendar) {
        this.calendar = calendar;
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private synchronized Calendar getCalendar() {
        return calendar;
    }

    private synchronized void updateTime() {
        if (calendar == null)
            return;

        long actualTime = System.currentTimeMillis();
        calendar.add(Calendar.MILLISECOND, (int) (actualTime - lastTime));

        if (timeElapsedLabel != null) {
            int hours = calendar.get(Calendar.HOUR);
            int minutes = calendar.get(Calendar.MINUTE);
            int seconds = calendar.get(Calendar.SECOND);
            String dateString = "";
            if (hours != 0)
                dateString = MessageFormat.format(HOURS_MIN_SEC, new Object[] { new Integer(hours),
                        new Integer(minutes), new Integer(seconds) });
            else if (minutes != 0)
                dateString = MessageFormat.format(MIN_SEC, new Object[] { new Integer(minutes),
                        new Integer(seconds) });
            else
                dateString = MessageFormat.format(SEC, new Object[] { new Integer(seconds) });

            final String updateString = dateString;
            doUpdate(new Runnable() {
                public void run() {
                    timeElapsedLabel.setText(kElapsedTime + "  " + updateString); // NOT
                    // LOCALIZABLE
                }
            });
        }

        lastTime = actualTime;
    }

    protected synchronized void setState(int newState) {
        if (getState() != newState) {
            super.setState(newState);
            String stateText = "";
            switch (getState()) {
            case STATE_ABORTING:
                stateText = kAborting;
                break;
            case STATE_ABORTED:
                stateText = kAborted;
                break;
            case STATE_COMPLETED:
                if (errorsCount > 0)
                    stateText = kCompletedError;
                else if (warningsCount > 0)
                    stateText = kCompletedWarning;
                else
                    stateText = kCompleted2;
                break;
            case STATE_ERROR:
                stateText = kError;
                break;
            default:
                stateText = kInProgress;
                break;
            }
            setStatusText(stateText);
            if (getState() != STATE_ABORTING && getState() != STATE_IN_PROGRESS
                    && getState() != STATE_NONE && progressBar != null) {
                doUpdate(new Runnable() {
                    public void run() {
                        progressBar.setValue(100);
                        progressBar.setString("100%"); // NOT LOCALIZABLE
                    }
                });
            }
        }
    }

    // set a status text (may depends of the GUI) representing the actual job
    // being done
    public final void setStatusText(final String text) {
        if (statusLabel != null) {
            doUpdate(new Runnable() {
                public void run() {
                    statusLabel.setText(text);
                }
            });
        }
    }

    /**
     * Append this string to an output repport or gui text component
     ** 
     * @deprecated
     **/
    public final void appendOutputText(final String s, final boolean newline) {
        if (s != null) {
            try {
                if (logFileWriter != null)
                    logFileWriter.write((newline ? EOL : "") + s);
            } catch (IOException e) {
            }
            if (textArea != null) {
                doUpdate(new Runnable() {
                    public void run() {
                        if (textArea != null) {
                            textArea.append((newline ? EOL : "") + s);
                        }
                    }
                });
            }
        }
    }

    /**
     * Print a string to an output report or gui text component.
     **/
    public final void print(final String s) {
        appendOutputText(s, false);
    }

    /**
     * Print a string to an output report or gui text component, and print a new line character.
     **/
    public void println(final String s) {
        appendOutputText(s, true);
    }

    public void println() {
        appendOutputText("", true);
    }

    // This text area contains informations on the completion of the job
    // if null, feature will be ignored
    protected JTextArea getTextArea() {
        return null;
    }

    // This label display an icon upon completion (error, warning or none) or
    // during execution if provided
    // if null, feature will be ignored
    protected JLabel getIconLabel() {
        return null;
    }

    // The default icon for Label returned by getIconLabel(). Displayed during
    // work.
    // if null, no icon will be displayed during work
    protected Icon getDefaultIcon() {
        return null;
    }

    // Display the actual status of the job
    // if null, feature will be ignored
    protected JLabel getStatusLabel() {
        return null;
    }

    // All output text added by appendOutputText() will be logged in this stream
    // if null, feature will be ignored
    // Note: The stream will be closed by this Class.
    // Subclass should only provide it.
    protected FileWriter getLogFileWriter() {
        return null;
    }

    // if null, feature will be ignored
    protected JButton getOKButton() {
        return null;
    }

    // if null, feature will be ignored
    protected JButton getCancelButton() {
        return null;
    }

    // if null, feature will be ignored
    protected JButton getSaveAsButton() {
        return null;
    }

    // The window to display to the user.
    // Null not allowed.
    // Note: This Class will manage the Visibility and the dispose on the
    // window.
    // Subclass should only provide it.
    protected Window getWindow() {
        return null;
    }

    // if null, feature will be ignored
    protected JLabel getTimeElapsedLabel() {
        return null;
    }

    // if null, feature will be ignored
    // Activation this feature will display a progression of the task in
    // percent.
    // NOTE: The Worker must determine the actual value. This value will be
    // updated only
    // during a call to checkPoint(int) (int representing the percent of
    // completed task).
    protected JProgressBar getProgressBar() {
        return null;
    }

    // Utility method for GUI updates - will update on event thread and make
    // sure the output is visible rapidly to user
    // IMPORTANT: This method should be used for all GUI updates
    protected final void doUpdate(Runnable r) {
        if (r == null)
            return;

        if (SwingUtilities.isEventDispatchThread()) {
            r.run();
            return;
        }
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InvocationTargetException e1) {
        } catch (InterruptedException e2) {
        }
    }

    // This method will process any non caught exceptions and ensure that the
    // task is aborted
    public synchronized void processException(Throwable t) {
        if (t instanceof AbortRuleException) {
            cancel();
            Db.abortAllTrans();
            ApplicationContext.getDefaultMainFrame().unlockDbRefreshInGUI(this);
            setState(STATE_ABORTED);
            okButton.setEnabled(true);
        } else {
            println(kFatalError + " " + t.getMessage() + EOL);
            cancel();
            setState(STATE_ERROR);
            if (window != null)
                window.dispose();
            ExceptionHandler.processUncatchedException(ApplicationContext.getDefaultMainFrame(), t);
            ApplicationContext.getDefaultMainFrame().unlockDbRefreshInGUI(this);
        }
    }

    // Called by the worker to signal that all threads have joined and that the
    // dialog may be disposed.
    // A RuntimeException will occurs if this method is called before method
    // terminate().
    void unlock() {
        int s = getState();
        if (s == STATE_ABORTED || s == STATE_COMPLETED) {
            doUpdate(new Runnable() {
                public void run() {
                    if (cancelButton != null)
                        cancelButton.setEnabled(false);
                    if (okButton != null)
                        okButton.setEnabled(true);
                    if (saveAsButton != null)
                        saveAsButton.setEnabled(true);
                    if (iconLabel != null) {
                        if (getErrorsCount() > 0)
                            iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
                        else if (getWarningsCount() > 0)
                            iconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                        else
                            iconLabel.setIcon(defaultIcon);
                    }
                }
            });
            setDisposable();
        } else {
            Debug.assert2(false, "Invalid Controller State"); // NOT
            // LOCALIZABLE,
            // debug only
            throw new RuntimeException();
        }
        if (closeOnTerminate) {
            doUpdate(new Runnable() {
                public void run() {
                    dispose_();
                }
            });
        }
        if (s == STATE_COMPLETED && runnable != null) {
            SwingUtilities.invokeLater(runnable);
        }
        Toolkit.getDefaultToolkit().beep();

    }

    public final void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == saveAsButton && source != null && textArea != null) {
            try {
                File defaultFile = new File(ApplicationContext.getDefaultWorkingDirectory());
                ExtensionFileFilter[] filters = null;
                AwtUtil.FileAndFilter selection = AwtUtil.showSaveAsDialog(getDialog(), 
                        kSaveReportAs, ExtensionFileFilter.txtFileFilter, 
                        filters, defaultFile);
                File file = (selection == null) ? null : selection.getFile(); 
                
                if (file == null)
                    return;
                FileWriter writer = new FileWriter(file);
                writer.write(textArea.getText());
                writer.close();
            } catch (Exception ex) {
            }
        } else if (source == okButton) {
            window.dispose();
            ApplicationContext.getDefaultMainFrame().unlockDbRefreshInGUI(this);
        } else if (source == cancelButton) {
            cancel();
        }
    }

}
