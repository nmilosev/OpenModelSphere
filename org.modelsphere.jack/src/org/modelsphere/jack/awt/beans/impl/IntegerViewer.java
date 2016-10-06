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

package org.modelsphere.jack.awt.beans.impl;

import java.awt.Toolkit;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.EventObject;

import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.BadLocationException;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.event.CellEditorListener;

final class IntegerViewer extends AbstractViewer {
    private static final int DEFAULT_VALUE = 0;
    private static final int NB_COLUMNS = 5;

    private WholeNumberField m_textField = new WholeNumberField(DEFAULT_VALUE, NB_COLUMNS); // integer viewed as a textfield

    private TableCellRenderer m_renderer = new TableCellRenderer() {
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Integer integerValue = (Integer) value;
            m_textField.setText(integerValue.toString());
            return m_textField;
        }
    }; // end m_renderer

    private TableCellEditor m_editor = new TableCellEditor() {
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            setEditorInfo(table, row);
            Integer integerValue = (Integer) value;
            m_textField.setText(integerValue.toString());
            return m_textField;
        }

        public Object getCellEditorValue() {
            return Boolean.TRUE;
        }

        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        public void removeCellEditorListener(CellEditorListener l) {
        }

        public void addCellEditorListener(CellEditorListener l) {
        }

        public void cancelCellEditing() {
        }

        // tells editor to stop editing
        // retrieve any partially edited value
        public boolean stopCellEditing() {
            String text = m_textField.getText();
            text = normalizeInteger(text);

            try {
                Integer value = Integer.decode(text);
                setValueInModel(value);
                m_textField.setText(text);
            } catch (NumberFormatException ex) {
                // value was invalid, so retrieve previous value
                Integer value = (Integer) getValueInModel();
                if (value != null) {
                    m_textField.setText(value.toString());
                }
            } // end try

            return true;
        } // end stopCellEditing()
    }; // end m_editor

    void negativeValuesAllowed(boolean b) {
        m_textField.negativeValuesAllowed(b);
    } // end negativeValuesAllowed

    // Non-zero integers cannot begin by 0, otherwise
    // Integer.decode() will generate a NumberFormatException,
    // so remove them.
    // "12" -> returns "12"
    // "0012" -> returns "12" //Trailing zeroes removed
    // "0" -> returns "0"
    // "000" -> returns "0"
    // "-12" -> returns "-12"
    // "-0012" -> returns "-12"
    // "-0" -> returns "0"
    // "-00" -> returns "0"
    private String normalizeInteger(String text) {
        boolean isNegative = false;

        // ignore those special values
        if ((text == null) || (text.equals(""))) {
            return text;
        }

        // remove trailing minus sign, if any
        if (text.charAt(0) == '-') {
            text = text.substring(1);
            isNegative = true;
        }

        // count number of trailing zeroes
        int idx = 0;
        int len = text.length();
        boolean isZero = false;
        if (len > 0) {
            while (true) {
                char ch = text.charAt(idx);
                if (ch != '0')
                    break;

                idx++;

                if (idx >= len) {
                    isZero = true;
                    break;
                } // end if
            } // end while
        } // end if

        // remove trailing zeroes
        String normalized;
        if (isZero) {
            normalized = "0";
        } else {
            normalized = (len > 1) ? text.substring(idx) : text;
        }

        // add trailing minus sign, if is negative
        if ((isNegative) && (!isZero)) {
            normalized = "-" + normalized;
        }

        return normalized;
    } // end normalizeInteger()

    private ActionListener m_actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent ev) {
            String text = m_textField.getText();
            Integer intValue = Integer.decode(text);
            setValueInModel(intValue);
        }
    };

    private MouseListener m_mouseListener = new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
            boolean success = m_editor.stopCellEditing();
        }

        public void mouseExited(MouseEvent e) {
            boolean success = m_editor.stopCellEditing();
        }
    };

    IntegerViewer() {
        super();
        setViewers(m_renderer, m_editor);
        m_textField.addActionListener(m_actionListener);
        m_textField.addMouseListener(m_mouseListener);
    }

    // INNER CLASSES
    private static final class WholeNumberField extends JTextField {
        private Toolkit toolkit;
        private NumberFormat integerFormatter;
        private boolean m_negativeValuesAllowed = false;

        public WholeNumberField(int value, int columns) {
            super(columns);
            toolkit = Toolkit.getDefaultToolkit();
            integerFormatter = NumberFormat.getNumberInstance(Locale.US);
            integerFormatter.setParseIntegerOnly(true);
            setValue(value);
        }

        public int getValue() {
            int retVal = 0;
            try {
                retVal = integerFormatter.parse(getText()).intValue();
            } catch (ParseException e) {
                // This should never happen because insertString allows
                // only properly formatted data to get in the field.
                toolkit.beep();
            }
            return retVal;
        }

        public void setValue(int value) {
            setText(integerFormatter.format(value));
        }

        protected Document createDefaultModel() {
            return new WholeNumberDocument();
        }

        void negativeValuesAllowed(boolean b) {
            m_negativeValuesAllowed = b;
        } // end negativeValuesAllowed

        protected class WholeNumberDocument extends PlainDocument {
            public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                char[] source = str.toCharArray();
                char[] result = new char[source.length];
                int j = 0;
                for (int i = 0; i < result.length; i++) {
                    // accept digits
                    if (Character.isDigit(source[i])) {
                        result[j++] = source[i];
                        // accept minus signs is nagative values are allowed
                    } else if (source[i] == '-') {
                        if (m_negativeValuesAllowed) {
                            result[j++] = source[i];
                        } else {
                            toolkit.beep();
                        }
                        // reject anything else
                    } else {
                        toolkit.beep();
                    }
                }
                super.insertString(offs, new String(result, 0, j), a);
            }
        } // end WholeNumberDocument()
    } // end WholeNumberField
} // end IntegerViewer
