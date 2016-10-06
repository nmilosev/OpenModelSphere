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

import javax.swing.*;

public final class WaitDialog extends JDialog {
    private JLabel text = new JLabel();
    private JProgressBar waitBar = new JProgressBar();

    private WaitDialog(JFrame owner) {
        super(owner, true);
        initGUI();
    }

    private WaitDialog(JDialog owner) {
        super(owner, true);
        initGUI();
    }

    private void initGUI() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        waitBar.setIndeterminate(true);

        contentPane.add(text, new GridBagConstraints(0, 0, 1, 1, 1.0, 0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(12, 12, 6, 12), 20, 0));
        contentPane.add(waitBar, new GridBagConstraints(0, 1, 1, 1, 1.0, 0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 12, 12, 12),
                50, 0));

    }

    public static void wait(Component owner, String title, String text, Runnable runnable) {
        final WaitDialog dialog;
        if (owner instanceof JDialog) {
            dialog = new WaitDialog((JDialog) owner);
        } else if (owner instanceof JFrame) {
            dialog = new WaitDialog((JFrame) owner);
        } else {
            dialog = new WaitDialog(NullFrame.singleton);
        }
        dialog.setTitle(title);
        dialog.text.setText(text);

        dialog.pack();
        dialog.setResizable(false);
        final TaskThread thread = new TaskThread(dialog, runnable);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                thread.start();
            }
        });
        AwtUtil.centerWindow(dialog);
        dialog.setVisible(true);
    }

    private static class TaskThread extends Thread {
        Runnable runnable;
        JDialog dialog;

        TaskThread(JDialog dialog, Runnable runnable) {
            this.runnable = runnable;
            this.dialog = dialog;
        }

        public void run() {
            if (runnable != null)
                runnable.run();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    dialog.dispose();
                }
            });
        }
    }

}
