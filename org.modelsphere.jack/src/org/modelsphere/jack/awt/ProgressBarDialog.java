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

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import org.modelsphere.jack.debug.Debug;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.international.LocaleMgr;

public class ProgressBarDialog extends JDialog implements TestableWindow {

    public final static int CHECKTIME = 200; // 1sec == 1000
    public static final String kCancel = LocaleMgr.screen.getString("Cancel");
    public static final String kCancelAction = "cancel"; // NOT LOCALIZABLE

    private JProgressBar progressBar;
    private Timer timer;
    private JButton cancelButton;
    private LongTask task;
    private JTextArea taskOutput;
    private String newline;
    private boolean addedTaskOutput;

    public ProgressBarDialog(JFrame frame, LongTask aTask, String title, boolean addTaskOutput) {
        super(frame, title, true);
        newline = System.getProperty("line.separator");
        addedTaskOutput = addTaskOutput;
        task = aTask;

        // create the demo's UI
        progressBar = new JProgressBar(0, task.getLengthOfTask());
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        org.modelsphere.jack.awt.AwtUtil.centerWindow(this);

        cancelButton = new JButton(kCancel);
        cancelButton.setActionCommand(kCancelAction);
        cancelButton.addActionListener(new ButtonListener());

        if (addTaskOutput) {
            taskOutput = new JTextArea(5, 20);
            taskOutput.setMargin(new Insets(5, 5, 5, 5));
            taskOutput.setEditable(false);
        }

        JPanel panel = new JPanel();

        panel.add(progressBar);
        panel.add(cancelButton);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(panel, BorderLayout.NORTH);
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        if (addTaskOutput)
            contentPane.add(new JScrollPane(taskOutput), BorderLayout.CENTER);
        setContentPane(contentPane);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new ClosingListener());
        // create a timer
        timer = new Timer(CHECKTIME, new TimerListener());
        timer.start();
        task.go();
    }

    // for unit testing
    public ProgressBarDialog() {
    }

    public final void show() {
        if (!task.done())
            super.setVisible(true);

    }

    class ClosingListener implements WindowListener {

        public final void windowClosing(WindowEvent e) {
            task.setCancel(true);
            cancelButton.setEnabled(false);
        }

        public void windowClosed(WindowEvent e) {
        }

        public void windowActivated(WindowEvent e) {
        }

        public void windowDeactivated(WindowEvent e) {
        }

        public void windowDeiconified(WindowEvent e) {
        }

        public void windowIconified(WindowEvent e) {
        }

        public void windowOpened(WindowEvent e) {
        }
    }

    // the actionPerformed method in this class
    // is called each time the Timer 'goes off'
    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            progressBar.setValue(task.getCurrent());

            if (addedTaskOutput) {
                taskOutput.append(task.getMessage() + newline);
                taskOutput.setCaretPosition(taskOutput.getDocument().getLength());
            }
            if (!task.cancelIsEnable() && cancelButton.isEnabled()) {
                cancelButton.setEnabled(false);
            }
            if (task.done()) {
                Toolkit.getDefaultToolkit().beep();
                timer.stop();
                dispose();
            }
        }
    }

    // the actionPerformed method in this class
    // is called when the user presses the cancel button
    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent evt) {
            cancelButton.setEnabled(false);
            task.setCancel(true);
        }
    }

    public final void setMaximum(int value) {
        progressBar.setMaximum(value);
    }

    // INNER CLASSES USED BY MAIN FUNCTION
    private static class DummyTask extends LongTask {
        DummyTask(int ms) {
            super(ms);
            setLengthOfTask(100);
        }

        public void go() {
            boolean wait = true;
            final DummyTask thisTask = this;
            final SwingWorker worker = new SwingWorker(wait) {
                public Object construct() {
                    return new DummyActualTask(thisTask);
                }
            };
            worker.start();
        }

        public final void terminate() {
            super.terminate(); // called super.stop()
            if (isCancel())
                Debug.trace("CANCELLED");
            else
                Debug.trace("TERMINATED");
        }
    }

    // Just increment of one 15th at each 800 ms
    private static class DummyActualTask {
        DummyActualTask(DummyTask task) {
            for (int i = 0; i < 15; i++) {
                if (task.isCancel())
                    break;

                try {
                    Thread.sleep(400);
                } catch (InterruptedException ex) {
                    //
                }

                task.incrementProgress(100 / 15);
            }

            // if not cancelled, finish the task
            if (!task.isCancel()) {
                task.setCurrent(task.getLengthOfTask());
                try {
                    Thread.sleep(400); // let time to see completion
                } catch (InterruptedException ex) {
                    //
                }
            }

            task.terminate();
        }
    }

    // *************
    // DEMO FUNCTION
    // *************

    // IMPLEMENTS TestableWindow
    public Window createTestWindow(Container owner) {
        final String kTitle = "ProgressBar Demo.."; // NOT LOCALIZABLE, used in
        // main() function only
        ProgressBarDialog.DummyTask task = new ProgressBarDialog.DummyTask(1000);
        JDialog dialog = new ProgressBarDialog(null, task, kTitle, false);
        return dialog;
    }

    private static void runDemo() {
        ProgressBarDialog instance = new ProgressBarDialog();
        Window dialog = instance.createTestWindow(null);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        dialog.pack();
        dialog.setVisible(true);
    }

    /*
     * //Run the demo public static void main(String[] args) { runDemo(); }
     */
}
