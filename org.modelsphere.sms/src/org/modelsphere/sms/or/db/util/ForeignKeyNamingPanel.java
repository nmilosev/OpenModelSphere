/*************************************************************************

Copyright (C) 2011 Grandite

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
package org.modelsphere.sms.or.db.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.MessageFormat;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.sms.or.international.LocaleMgr;

@SuppressWarnings("serial")
public class ForeignKeyNamingPanel extends JPanel implements ActionListener, KeyListener {
	public static final String FOREIGN_COLUMN_NAME_PATTERN = "foreignColumnNamePattern"; //property key
	public static final String FOREIGN_KEY_NAME_PATTERN = "foreignKeyNamePattern"; //property key
	
	public static final String TABLE_COLUMN = "{0} {1}"; 
	public static final String COLUMN_TABLE = "{1} {0}"; 
	public static final String ROLE = "{2}"; 
	
	private String _fkDisplayName;
	private JDialog _owner;
	private JRadioButton _fcOption1, _fcOption2, _fcOption3;
	private JRadioButton _fkOption1, _fkOption2, _fkOption3;
	private JTextField _fcTextField, _fkTextField;
	private JPanel _canvas;
	private JButton _okButton, _cancelButton; 

	/**
	 * Main
	 */
	public static void main(String[] args) {
		 //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
	}
		
	 /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    
    	try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			//ignore
		}
    	
        //Create and set up the window.
		JFrame frame = new JFrame("Foreign Key Generation"); //unit test
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        ForeignKeyNamingPanel newContentPane = new ForeignKeyNamingPanel(frame);
        frame.setContentPane(newContentPane);
        
        Color bgColor = new Color(220, 200, 240); 
        String fcPattern = "{0} {1}";
        String fkPattern = "Foreign Key";
        newContentPane.createContent(bgColor, "Foreign Key", fcPattern , fkPattern);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
	public ForeignKeyNamingPanel(Window owner) {
		super(new GridBagLayout());
		setOpaque(true); //content panes must be opaque
		
		if (owner instanceof JFrame) {
			((JFrame)owner).setContentPane(this);	
		} else if (owner instanceof JDialog) {
			_owner = (JDialog)owner;
			_owner.setContentPane(this);	
		}
	}

	public void createContent(Color bgColor, String fkDisplayName, String fcPattern, String fkPattern) {
    	GridBagConstraints c;
    	int row = 0;
    	_fkDisplayName = fkDisplayName;
    	
		JLabel fcLabel = new JLabel(LocaleMgr.screen.getString("ForeignColumnNamingPolicy")); 
		c = new GridBagConstraints(0, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6,6,6,6), 0, 0);
		add(fcLabel, c); 
		
		JLabel fkLabel = new JLabel(LocaleMgr.screen.getString("ForeignKeyNamingPolicy")); 
		c = new GridBagConstraints(3, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(6,6,6,6), 0, 0);
		add(fkLabel, c); 
		row++;
		
		String oppositeTable = LocaleMgr.screen.getString("ParentTable"); 
		String oppositeKey = LocaleMgr.screen.getString("ParentColumn"); 
		String oppositeRole = LocaleMgr.screen.getString("Role"); 
		
		_fcOption1 = new JRadioButton(oppositeTable + " + " + oppositeKey);
		_fcOption2 = new JRadioButton(oppositeKey + " + " + oppositeTable);
		_fcOption3 = new JRadioButton(LocaleMgr.screen.getString("CustomPattern"));
		
		_fkOption1 = new JRadioButton(fkDisplayName);
		_fkOption2 = new JRadioButton(oppositeRole);
		_fkOption3 = new JRadioButton(LocaleMgr.screen.getString("CustomPattern"));
		
		c = new GridBagConstraints(0, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,18,0,6), 0, 0);
		add(_fcOption1, c);
		
		c = new GridBagConstraints(3, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,18,0,6), 0, 0);
		add(_fkOption1, c);
		
		row++; 
		
		c = new GridBagConstraints(0, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,18,0,6), 0, 0);
		add(_fcOption2, c);
		
		c = new GridBagConstraints(3, row, 2, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,18,0,6), 0, 0);
		add(_fkOption2, c);
		
		row++; 
		
		c = new GridBagConstraints(0, row, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3,18,0,6), 0, 0);
		add(_fcOption3, c);
		
		c = new GridBagConstraints(3, row, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3,18,0,6), 0, 0);
		add(_fkOption3, c);
		
		//Group the radio buttons.
	    ButtonGroup fcGroup = new ButtonGroup();
	    fcGroup.add(_fcOption1);
	    fcGroup.add(_fcOption2);
	    fcGroup.add(_fcOption3);
	    
	    ButtonGroup fkGroup = new ButtonGroup();
	    fkGroup.add(_fkOption1);
	    fkGroup.add(_fkOption2);
	    fkGroup.add(_fkOption3);
	    
	    _fcTextField = new JTextField();
	    c = new GridBagConstraints(1, row, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,24,3,6), 60, 0);
		add(_fcTextField, c);
		
		_fkTextField = new JTextField();
	    c = new GridBagConstraints(4, row, 2, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(3,24,3,6), 60, 0);
		add(_fkTextField, c);
		row++;
		
		JLabel legend = new JLabel(LocaleMgr.screen.getString("LegendPattern")); 
		c = new GridBagConstraints(0, row, 5, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0,40,3,6), 0, 0);
		add(legend, c); 
		row++;
		
		JLabel preview = new JLabel(LocaleMgr.screen.getString("Preview")); 
		c = new GridBagConstraints(0, row, 5, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(18,6,6,6), 0, 0);
		add(preview, c); 
		row++;
		
		_canvas = new Canvas(bgColor);
		c = new GridBagConstraints(0, row, 6, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0,6,6,6), 600, 180);
		add(_canvas, c); 
		row++;
		
		_okButton = new JButton(LocaleMgr.screen.getString("OK"));
		c = new GridBagConstraints(4, row, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(6,3,6,3), 0, 0);
		add(_okButton, c); 
		
		_cancelButton = new JButton(LocaleMgr.screen.getString("Cancel"));
		c = new GridBagConstraints(5, row, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(6,3,6,6), 0, 0);
		add(_cancelButton, c); 
		row++;
		
		AwtUtil.normalizeComponentDimension(new JComponent[] {_okButton, _cancelButton});
		
	    _fcOption1.addActionListener(this);
	    _fcOption2.addActionListener(this);
	    _fcOption3.addActionListener(this);
	    _fcTextField.addKeyListener(this);
	    
	    _fkOption1.addActionListener(this);
	    _fkOption2.addActionListener(this);
	    _fkOption3.addActionListener(this);
	    _fkTextField.addKeyListener(this);
	    
	    _okButton.addActionListener(this);
	    _cancelButton.addActionListener(this);
	    
	    //initialize
	    initializeWidgets(fcPattern, fkPattern);
		updateFcTextBox();
		updateFkTextBox();
		updateCanvas();
	}

	private void initializeWidgets(String fcPattern, String fkPattern) {
				
		if (TABLE_COLUMN.equals(fcPattern)) {
			_fcOption1.setSelected(true);
		} else if (COLUMN_TABLE.equals(fcPattern)) {
			_fcOption2.setSelected(true);
		} else {
			_fcOption3.setSelected(true);
			_fcTextField.setText(fcPattern);
		} //end if
		
		if (_fkDisplayName.equals(fkPattern)) {
			_fkOption1.setSelected(true);
		} else if (ROLE.equals(fcPattern)) {
			_fkOption2.setSelected(true);
		} else {
			_fkOption3.setSelected(true);
			_fkTextField.setText(fkPattern);
		} //end if
	} //end initializeWidget

	@Override
	public void actionPerformed(ActionEvent ev) {
		Object src = ev.getSource();
		
		if (src.equals(_fcOption1)) {
			updateFcTextBox();
			updateCanvas();
		} else if (src.equals(_fcOption2)) {
			updateFcTextBox();
			updateCanvas();
		} else if (src.equals(_fcOption3)) {
			updateFcTextBox();
			updateCanvas();
		} else if (src.equals(_fkOption1)) {
			updateFkTextBox();
			updateCanvas();
		} else if (src.equals(_fkOption2)) {
			updateFkTextBox();
			updateCanvas();
		} else if (src.equals(_fkOption3)) {
			updateFkTextBox();
			updateCanvas();
		} else if (src.equals(_okButton)) {
			PropertiesSet applOptions = PropertiesManager.APPLICATION_PROPERTIES_SET;
			String pattern = _fcTextField.getText();
			applOptions.setProperty(ForeignKeyNamingPanel.class, FOREIGN_COLUMN_NAME_PATTERN, pattern); 
			
			pattern = _fkTextField.getText();
			applOptions.setProperty(ForeignKeyNamingPanel.class, FOREIGN_KEY_NAME_PATTERN, pattern); 
			
			if (_owner != null) {
				_owner.dispose();
			}
		} else if (src.equals(_cancelButton)) {
			if (_owner != null) {
				_owner.dispose();
			}
		} 
	} //end actionPerformed()
	
	@Override
	public void keyTyped(KeyEvent ev) {
		Object src = ev.getSource();
		
		if (src.equals(_fcTextField)) {
			updateCanvas();
		} else if (src.equals(_fkTextField)) {
			updateCanvas();
		} 
	}

	@Override
	public void keyPressed(KeyEvent e) {	
	}

	@Override
	public void keyReleased(KeyEvent e) {	
	}
	
	private void updateFcTextBox() {
		if (_fcOption1.isSelected()) {
			_fcTextField.setText(TABLE_COLUMN);
		} else if (_fcOption2.isSelected()) { 
			_fcTextField.setText(COLUMN_TABLE);
		} //end if
		 
		boolean isCustom = _fcOption3.isSelected();
		_fcTextField.setEnabled(isCustom);
	}
	
	private void updateFkTextBox() {
		if (_fkOption1.isSelected()) {
			_fkTextField.setText(_fkDisplayName);
		} else if (_fkOption2.isSelected()) { 
			_fkTextField.setText(ROLE);
		} //end if
		 
		boolean isCustom = _fkOption3.isSelected();
		_fkTextField.setEnabled(isCustom);
	}
	
	private void updateCanvas() {
		_canvas.repaint();
	}
	
	private class Canvas extends JPanel {
		private static final int BOX_WIDTH = 160;
		private static final int BOX_HEIGHT = 100;
		private static final int MARGIN = 4;
		private Color _lineColor = Color.BLACK; 
		private Color _bgColor;
		private String _oppTable = LocaleMgr.screen.getString("Company"); 
		private String _oppKey = "id"; 
		private String _oppRole = LocaleMgr.screen.getString("employer"); 
		
		public Canvas(Color bgColor) {
			_bgColor = bgColor;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			int width = getWidth() -2;
		    int height = getHeight() -2;
		    
		    //draw BG and border
		    g.setColor(Color.WHITE);
		    g.fillRect(0, 0, width, height);
		    g.setColor(_lineColor);
		    g.drawRect(0, 0, width, height); 
		    
		    drawLine(g, width/4, height/2, width/2); //association
		    drawEmployeeTable(g, (int)(width * 0.15), height/2);
		    drawCompanyTable(g, (int)(width * 0.85), height/2);
		    drawString(g, (int)(width * 0.15) + BOX_WIDTH/2, height/2, "0..1 " + _oppRole, false, false);
		}
		
		private void drawEmployeeTable(Graphics g, int x, int y) {
			int w = BOX_WIDTH;
			int h = BOX_HEIGHT;
			drawBox(g, x-w/2, y-h/2, w, h);
			
			drawString(g, x-w/2, y-h/2, LocaleMgr.screen.getString("Employee"), true, false);
			drawLine(g, x-w/2, y-h/2 + 20, w); 
			drawLine(g, x-w/2, y+h/2 - 20, w); 
			
			String fcText = buildForeignColumn(); 
			String fkText = buildForeignKey(fcText);
			drawString(g, x-w/2, y-h/2 + 20, "     id", false, false);
			drawString(g, x-w/2, y-h/2 + 36, "FK " + fcText, false, false);
			drawString(g, x-w/2, y+h/2 - 18, fkText, false, false);
		}
		
		private String buildForeignColumn() {
			String pattern = _fcTextField.getText();
			String msg; 
			
			try {
				msg = MessageFormat.format(pattern, _oppTable, _oppKey, _oppRole);
			} catch (IllegalArgumentException ex) {
				msg = "???";
			}
			
			if (msg.length() > 20) {
				msg = msg.substring(0, 19) + "...";
			}
			
			return msg;
		}
		
		private String buildForeignKey(String fcText) {
			String pattern = _fkTextField.getText();
			
			String msg; 
			
			try {
				msg = "FK " + MessageFormat.format(pattern, _oppTable, _oppKey, _oppRole);
			} catch (IllegalArgumentException ex) {
				msg = "FK ???";
			}
			
			msg = msg + " (" + fcText + ")";
			return msg;
		}

		private void drawCompanyTable(Graphics g, int x, int y) {
			int w = BOX_WIDTH;
			int h = BOX_HEIGHT;
			drawBox(g, x-w/2, y-h/2, w, h);
			
			drawString(g, x-w/2, y-h/2, _oppTable, true, false);
			drawLine(g, x-w/2, y-h/2 + 20, w); 
			
			drawString(g, x-w/2, y-h/2 + 20, "PK " + _oppKey, false, true);
		}
		
		private void drawBox(Graphics g, int x, int y, int w, int h) {
			g.setColor(_bgColor);
		    g.fillRect(x, y, w, h);
		    
		    g.setColor(_lineColor);
			g.drawRect(x, y, w, h);
		}

		private void drawString(Graphics g, int x, int y, String text, boolean isBold, boolean isUnderline) {
			Font baseFont = g.getFont();
			Font font = isBold ? baseFont.deriveFont(Font.BOLD) : baseFont; 
			g.setFont(font); 
			
			FontMetrics metrics = g.getFontMetrics(font);
			int th = metrics.getHeight(); 
			
			g.drawString(text, x+MARGIN, y+th); 
			
			if (isUnderline) {
				int len = metrics.stringWidth(text);
				drawLine(g, x+MARGIN, y+th, len);
			}
			
			//restore font
			g.setFont(baseFont);
		}
		
		private void drawLine(Graphics g, int x, int y, int len) {
			 g.setColor(_lineColor);
			 g.drawLine(x, y, x+len, y); 
		}

		
	}







}
