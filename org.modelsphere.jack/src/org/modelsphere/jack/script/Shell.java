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

package org.modelsphere.jack.script;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;

import org.modelsphere.jack.awt.ExtensionFileFilter;
import org.modelsphere.jack.awt.JTextAreaFix;
import org.modelsphere.jack.international.LocaleMgr;
import org.modelsphere.jack.srtool.ApplicationContext;
import org.modelsphere.jack.srtool.graphic.CascadingJInternalFrame;
import org.modelsphere.jack.text.MessageFormat;

public abstract class Shell extends CascadingJInternalFrame {
    private static final String kClose = LocaleMgr.screen.getString("Close");
    private static final String kCloseTab0 = LocaleMgr.screen.getString("CloseTab0");
    private static final String kShellResult = LocaleMgr.screen.getString("ShellResult");
    private static final String kExecuteFile = LocaleMgr.screen.getString("ExecuteFile");
    private static final String kCloseAll = LocaleMgr.screen.getString("CloseAllResult");
    private static final String kClearOutput = LocaleMgr.screen.getString("ClearOutput");
    private static final String SHELL = LocaleMgr.screen.getString("Shell");
    private static final String MONOSPACED = "Monospaced"; // NOT LOCALIZABLE, use by Font

    public static final char NEWLINE = (char) KeyEvent.VK_ENTER;
    protected static ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    public JTextArea resultArea = new JTextAreaFix("");
    CommandLine commandLine;
    JScrollPane scrollPane = new JScrollPane(resultArea);
    JScrollPane inputScrollPane;
    JTabbedPane tabbedPanel = new JTabbedPane();
    JButton closeButton = new JButton();
    JButton execFileButton = new JButton();
    JButton closeAllResult = new JButton(kCloseAll);
    JButton clearOutput = new JButton(kClearOutput);
    ShellInitializer shellInitializer;

    // for now, we use the working directory, later maybe a specific directory
    // for script files
    private static String execDir = ApplicationContext.getDefaultWorkingDirectory();

    private class MouseHandler extends MouseAdapter {
        MouseHandler() {
        }

        public void mouseReleased(MouseEvent e) {
            if (!e.isPopupTrigger())
                return;
            triggerTabPopup(e.getPoint());
        }

        public void mousePressed(MouseEvent e) {
            if (!e.isPopupTrigger())
                return;
            triggerTabPopup(e.getPoint());
        }
    }

    private void triggerTabPopup(Point loc) {
        if (!tabbedPanel.isEnabled()) {
            return;
        }

        if (!(tabbedPanel.getUI() instanceof TabbedPaneUI))
            return;
        TabbedPaneUI ui = tabbedPanel.getUI();
        final int tabIndex = ui.tabForCoordinate(tabbedPanel, (int) loc.getX(), (int) loc.getY());
        if (tabIndex > 0 && tabbedPanel.isEnabledAt(tabIndex)) { // Do not close output tab
            JPopupMenu popup = new JPopupMenu();
            String tabTitle = tabbedPanel.getTitleAt(tabIndex);
            JMenuItem close = new JMenuItem(MessageFormat.format(kCloseTab0,
                    new Object[] { tabTitle }));
            close.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabbedPanel.removeTabAt(tabIndex);
                }
            });
            popup.add(close);
            popup.show(tabbedPanel, (int) loc.getX(), (int) loc.getY());
        }
    }

    public Shell() {
        super("", SHELL);
        jbInit();
        setResizable(true);
        setClosable(true);
        setMaximizable(true);
        setIconifiable(true);
        setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        tabbedPanel.addMouseListener(new MouseHandler());
    }

    public CommandLine getCommandLine() {
        return commandLine;
    }

    protected final void addSecondaryOutputPanel(JPanel panel, String title) {
        if (tabbedPanel != null)
            tabbedPanel.add(panel, title);
        closeAllResult.setEnabled(tabbedPanel.getTabCount() > 1);
    }

    protected final void removeSecondaryOutputPanel(JPanel panel) {
        if (tabbedPanel != null)
            tabbedPanel.remove(panel);
        closeAllResult.setEnabled(tabbedPanel.getTabCount() > 1);
    }

    public void init(ShellInitializer aShellInitializer) {
        shellInitializer = aShellInitializer;
        setTitle(shellInitializer.shellTitle);

        commandLine = new CommandLine(resultArea);
        commandLine.refresh();

        ExecFileActionListener execFileActionListener = new ExecFileActionListener();
        execFileActionListener.init();
        execFileButton.addActionListener(execFileActionListener);

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    setClosed(true);
                } catch (PropertyVetoException e) {
                } // should not happen
            }
        });

        closeAllResult.setEnabled(false);
        closeAllResult.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int count = tabbedPanel.getTabCount();
                for (int i = count - 1; i > 0; --i) {
                    tabbedPanel.removeTabAt(i);
                }
                closeAllResult.setEnabled(false);
            }
        });
        clearOutput.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resultArea.setText("");
            }
        });

        // change to fixed-width font
        if (resultArea != null) {
            resultArea.setFont(new Font(MONOSPACED, Font.PLAIN, resultArea.getFont().getSize()));
        }
        if (resultArea != null) {
            commandLine.setFont(new Font(MONOSPACED, Font.PLAIN, commandLine.getFont().getSize()));
        }

        inputScrollPane = new JScrollPane(commandLine);
        JPanel controlBtnPanel = new JPanel();
        controlBtnPanel.setLayout(new GridBagLayout());
        controlBtnPanel.add(closeAllResult, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 0, 0), 0, 0));
        controlBtnPanel.add(clearOutput, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 0, 0), 0, 0));
        controlBtnPanel.add(Box.createHorizontalGlue(), new GridBagConstraints(2, 0, 1, 1, 0.5,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                0));
        controlBtnPanel.add(execFileButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 0, 0), 0, 0));
        controlBtnPanel.add(closeButton, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 6, 0, 0), 0, 0));

        tabbedPanel.add(scrollPane, kShellResult);
        getContentPane().setLayout(new GridBagLayout()); // chris
        getContentPane().add(
                tabbedPanel,
                new GridBagConstraints(0, 0, 4, 1, 0.5, 0.8, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(12, 12, 6, 12), 0, 0));
        getContentPane().add(
                inputScrollPane,
                new GridBagConstraints(0, 1, 4, 1, 0.5, 0.2, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 12, 12, 12), 0, 0));
        getContentPane().add(
                controlBtnPanel,
                new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.SOUTHEAST,
                        GridBagConstraints.BOTH, new Insets(0, 12, 12, 12), 0, 0));
    }

    public abstract String execute(String command) throws ShellException;

    public void updateUI() {
        super.updateUI();
        if (resultArea != null) {
            resultArea.setFont(new Font(MONOSPACED, Font.PLAIN, resultArea.getFont().getSize()));
        }
        if (commandLine != null) {
            commandLine.setFont(new Font(MONOSPACED, Font.PLAIN, commandLine.getFont().getSize()));
        }
    }

    private void jbInit() {
        getContentPane().setBackground(UIManager.getColor("Panel.background")); // NOT LOCALIZABLE : ui property

        resultArea.setEditable(false);
        resultArea.setLineWrap(true);

        closeButton.setText(kClose);
        execFileButton.setText(kExecuteFile);
    }

    // Default execute file. Can be overridden if similar function exists
    // in subclass' interpreter.
    protected void executeFile(File file) {

        String filename = file.getAbsolutePath();
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(filename));
            String prompt = file.getName() + " > ";
            String command;
            // commandLine.executeCommand("", prompt);
            commandLine.addHistory(kExecuteFile);

            // read until meet EOF
            try {
                while (true) {
                    command = reader.readLine();
                    if (command == null)
                        break;

                    command = command.trim();
                    if (command.length() > 0) {
                        String result = null;
                        try {
                            result = execute(command);
                        } catch (ShellException ex) {
                            result = ex.toString();
                        }
                    }

                    commandLine.executeCommand(command, prompt);
                    // executeAndPrintResult(command, prompt);
                } // end while
            } catch (EOFException ex) {
                // expected exception
            } catch (IOException ex) {
                // could not happen
            }

        } catch (FileNotFoundException ex) {
            // could not happen
        }
    } // end executeFile()

    //
    // INNER CLASSES
    //
    protected static class ShellException extends Exception {
        protected ShellException(String msg) {
            super(msg);
        }
    }

    protected static class ShellInitializer {
        public String shellTitle;
        public String promptPattern;
        public String extension;
        public String fileDesc;

        public ShellInitializer(String aShellTitle, String aPromptPattern, String anExtension,
                String aFileDesc) {
            shellTitle = aShellTitle;
            promptPattern = aPromptPattern;
            extension = anExtension;
            fileDesc = aFileDesc;
        }
    }

    private class ExecFileActionListener implements ActionListener {
        private JFileChooser chooser = new JFileChooser(execDir);
        private ExtensionFileFilter filter = new ExtensionFileFilter(shellInitializer.extension,
                shellInitializer.fileDesc);

        ExecFileActionListener() {
        }

        private void init() {
            chooser.setFileFilter(filter);
            chooser.setDialogTitle(LocaleMgr.screen.getString("SelectFileToExec"));
        }

        public void actionPerformed(ActionEvent ev) {
            // Select a file
            int returnVal = chooser.showDialog(ApplicationContext.getDefaultMainFrame(), null);

            // Execute it line by line
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                execDir = file.getParent();
                executeFile(file);
            }
        }
    }

    // CommandLine
    public class CommandLine extends JTextAreaFix {
        private ShellKeyListener shellKeyListener = new ShellKeyListener(this);
        private JTextArea resultArea;
        private ArrayList history = new ArrayList();
        private int callBack = 0;

        CommandLine(JTextArea aResultArea) {
            super();
            resultArea = aResultArea;
            addKeyListener(shellKeyListener);
            setLineWrap(true);
        }

        public void refresh() {
            setText(getPrompt());
        }

        public String getPrompt() {
            int historyCount = history.size() - callBack + 1;
            String prompt = MessageFormat.format(shellInitializer.promptPattern,
                    new Object[] { new Integer(historyCount) });
            return prompt;
        }

        public void addHistory(String command) {
            history.add(command);
            callBack = 0; // reset to 0
        }

        void putCaretAtTheEnd() {
            resultArea.setCaretPosition(resultArea.getText().length());
        }

        void executeCommand(String command, String aPrompt) {
            command = command.trim();
            if (command.length() > 0) {
                String result = null;
                try {
                    result = execute(command);
                } catch (ShellException ex) {
                    result = ex.toString();
                }

                // If the caret position is not at the end, it will not scroll
                // down after append()
                putCaretAtTheEnd();

                callBack = 0;
                String prompt = getPrompt(); // get prompt before add to history
                addHistory(command);

                resultArea.append(prompt + command + NEWLINE);
                resultArea.append(result);
            } // end if
        }

        void setCommand(String text) {
            // remove prompt & newline
            String prompt = getPrompt();
            int len = prompt.length();
            if ((len > 0) && (len < text.length())) {
                String command = text.substring(len);
                int index = command.indexOf(NEWLINE);
                if (index != -1)
                    command = command.substring(0, index);

                // execute command
                executeCommand(command, prompt);
            }
        }

        String callBack(boolean upward) {
            // first, increment positively or negatively
            if (upward)
                callBack++;
            else
                callBack--;

            // be sure that callback stays inside limits
            if (callBack < 0)
                callBack = 0;
            else if (callBack > history.size())
                callBack = history.size();

            String text = "";
            if (callBack != 0) {
                text = (String) history.get(history.size() - callBack);
            }

            String command = getPrompt() + text;
            return command;
        }
    } // end CommandLine

    // ShellKeyListener
    private class ShellKeyListener implements KeyListener {
        private CommandLine commandLine;

        ShellKeyListener(CommandLine aCommandLine) {
            super();
            commandLine = aCommandLine;
        }

        // make sure that prompt is never erased
        private boolean goingToErasePrompt(int promptLen, int caretPosition) {
            return (caretPosition < promptLen) ? true : false;
        }

        public void keyTyped(KeyEvent ev) {
        }

        public void keyPressed(KeyEvent ev) {
            String prompt = commandLine.getPrompt();
            int promptLen = prompt.length();
            int caretPosition = commandLine.getCaretPosition();
            int keyCode = ev.getKeyCode();

            switch (keyCode) {
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_LEFT:
                if (goingToErasePrompt(promptLen, caretPosition)) {
                    ev.consume(); // make sure that prompt is never erased
                    commandLine.putCaretAtTheEnd();
                }
                break;
            case KeyEvent.VK_HOME:
                ev.consume();
                commandLine.setCaretPosition(promptLen);
                break;
            default:
                if (caretPosition < promptLen) {
                    try {
                        commandLine.setCaretPosition(promptLen);
                    } catch (IllegalArgumentException ex) {
                        // ignore exception in promptLen exceeds text area's
                        // length
                    }
                }
                break;
            }
        }

        public void keyReleased(KeyEvent ev) {
            String text;

            switch (ev.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                text = commandLine.getText();
                commandLine.setCommand(text);
                commandLine.setText(commandLine.getPrompt());
                break;
            case KeyEvent.VK_UP:
                text = commandLine.callBack(true);
                commandLine.setText(text);
                break;
            case KeyEvent.VK_DOWN:
                text = commandLine.callBack(false);
                commandLine.setText(text);
                break;
            default:
                break;
            }
        } // end keyReleased()
    }
}
