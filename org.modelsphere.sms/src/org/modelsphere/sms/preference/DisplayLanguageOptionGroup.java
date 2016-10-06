/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/

package org.modelsphere.sms.preference;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.jack.awt.ThinBevelBorder;
import org.modelsphere.jack.debug.TestableWindow;
import org.modelsphere.jack.graphic.GraphicUtil;
import org.modelsphere.jack.preference.OptionDialog;
import org.modelsphere.jack.preference.OptionGroup;
import org.modelsphere.jack.preference.OptionPanel;
import org.modelsphere.jack.preference.PropertiesManager;
import org.modelsphere.jack.preference.PropertiesSet;
import org.modelsphere.jack.srtool.DefaultMainFrame;
import org.modelsphere.sms.MainFrame;
import org.modelsphere.sms.international.LocaleMgr;

public class DisplayLanguageOptionGroup extends OptionGroup implements TestableWindow {
    private static final Image FR_FLAG_IMAGE = GraphicUtil.loadImage(MainFrame.class,
            "international/resources/banner-fr.jpg");
    private static final Image US_FLAG_IMAGE = GraphicUtil.loadImage(MainFrame.class,
            "international/resources/banner-en.jpg");
    //private static final Image      UK_FLAG_IMAGE                         = GraphicUtil.loadImage(MainFrame.class, "international/resources/banner-en.jgp");

    private static final String LANGUAGE_SELECTION = LocaleMgr.screen.getString("ChooseLanguage");
    private static final String DISPLAY_LANGUAGE = LocaleMgr.screen.getString("DisplayLanguage");
    private static final String ENGLISH_US = LocaleMgr.screen.getString("EnglishUS");
    private static final String ENGLISH_UK = LocaleMgr.screen.getString("EnglishUK");
    private static final String FRENCH = LocaleMgr.screen.getString("French");
    private static final String NOTICE = LocaleMgr.misc.getString("needRestart");

    private static final String EN = "en"; //NOT LOCALIZABLE
    private static final String FR = "fr"; //NOT LOCALIZABLE                        

    private static class DisplayLanguageOptionPanel extends OptionPanel implements ActionListener {
        private JLabel languageLabel = new JLabel();
        private JRadioButton usLanguage = new JRadioButton();
        private JRadioButton frLanguage = new JRadioButton();
        private int selected = -1;
        private ButtonGroup layoutGroup = new ButtonGroup();
        private CardLayout cardlayout = new CardLayout();

        //FLAG PANELS
        private static class FlagPanel extends JPanel {
            private static final Dimension FLAG_DIMENSION = new Dimension(100, 70);
            private Image m_image;

            FlagPanel(Image image) {
                m_image = image;
            }

            protected void paintChildren(Graphics g) {
                Insets insets = getInsets();
                boolean success = g.drawImage(m_image, 0, 0, FLAG_DIMENSION.width,
                        FLAG_DIMENSION.height, null);
            }

            public Dimension getPreferredSize() {
                return FLAG_DIMENSION;
            }

            public Dimension getMinimumSize() {
                return FLAG_DIMENSION;
            }
        };

        private JPanel flagContainerPanel = new JPanel();
        private JPanel frFlagPanel = new FlagPanel(FR_FLAG_IMAGE);
        private JPanel usFlagPanel = new FlagPanel(US_FLAG_IMAGE);

        DisplayLanguageOptionPanel() {
            setLayout(new GridBagLayout());

            // Ensure all images are loaded completely
            GraphicUtil.waitForImage(FR_FLAG_IMAGE);
            GraphicUtil.waitForImage(US_FLAG_IMAGE);

            languageLabel.setText(LANGUAGE_SELECTION + " " + NOTICE);
            usLanguage.setText(ENGLISH_US);
            frLanguage.setText(FRENCH);

            layoutGroup.add(usLanguage);
            layoutGroup.add(frLanguage);

            flagContainerPanel.setBorder(new ThinBevelBorder(ThinBevelBorder.LOWERED));
            flagContainerPanel.setLayout(cardlayout);
            flagContainerPanel.add(usFlagPanel, EN);
            flagContainerPanel.add(frFlagPanel, FR);

            add(languageLabel, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(18, 12, 0, 5), 0,
                    0));
            //Language
            add(usLanguage, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(12, 30, 0, 5), 0, 0));
            add(frLanguage, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(12, 30, 0, 5), 0, 0));
            add(flagContainerPanel, new GridBagConstraints(1, 1, 1, 3, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(12, 0, 11, 5), 0,
                    0));

            add(Box.createVerticalGlue(), new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0,
                    GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

            usLanguage.addActionListener(this);
            frLanguage.addActionListener(this);

            init();
        }

        public void init() {
            Locale locale = org.modelsphere.jack.international.LocaleMgr
                    .getLocaleFromPreferences(Locale.getDefault(), true);

            if ((locale.equals(Locale.ENGLISH)) ||
                	(locale.equals(Locale.CANADA)) ||
                	(locale.equals(Locale.UK)) ||
                	(locale.equals(Locale.US))
                	) {
                    cardlayout.show(flagContainerPanel, EN);
                    usLanguage.setSelected(true);
                } else if ((locale.equals(Locale.FRENCH)) ||
                		  (locale.equals(Locale.CANADA_FRENCH)) ||
                		  (locale.equals(Locale.FRANCE))
                		  ) {
                    cardlayout.show(flagContainerPanel, FR);
                    frLanguage.setSelected(true);
                } else {
                    //US English is the default language
                    cardlayout.show(flagContainerPanel, EN);
                    usLanguage.setSelected(true);
                } //end if

            revalidate();
        } //end init()

        public static String PROPERTY_LANGUAGE = "Language";

        public void actionPerformed(ActionEvent e) {
            PropertiesSet prefs = PropertiesManager.getPreferencePropertiesSet();

            setRequireRestart();

            Object source = e.getSource();
            if (source == usLanguage) {
                if (usLanguage.isSelected()) {
                    cardlayout.show(flagContainerPanel, EN);
                    fireOptionChanged(prefs, DefaultMainFrame.class, PROPERTY_LANGUAGE, EN);

                }
            } else if (source == frLanguage) {
                if (frLanguage.isSelected()) {
                    cardlayout.show(flagContainerPanel, FR);
                    fireOptionChanged(prefs, DefaultMainFrame.class, PROPERTY_LANGUAGE, FR);

                }
            } //end if
        } //actionPerformed()

        private void setAppLanguage(Locale locale) {
            //Set language HERE
        }
    }; //end DisplayLanguageOptionPanel

    public DisplayLanguageOptionGroup() {
        super(DISPLAY_LANGUAGE);
    }

    protected OptionPanel createOptionPanel() {
        return new DisplayLanguageOptionPanel();
    }

    // Service Methods to access this options group values
    public static boolean isComponentsHeaderVisible() {
        return false;
    }

    //
    // UNIT TEST
    //
    public String getName() {
        return "";
    }

    public Window createTestWindow(Container owner) {
        //Create a property dialog
        JFrame mainframe = new JFrame("DisplayLanguageOptionGroupInfo"); //NOT LOCALIZABLE, unit test
        Locale.setDefault(Locale.ENGLISH);
        DisplayLanguageOptionPanel panel = new DisplayLanguageOptionPanel();
        mainframe.getContentPane().add(panel);
        AwtUtil.centerWindow(mainframe);
        return mainframe;
    }

    public static void main(String[] args) {
        //Create a property dialog
        DisplayLanguageOptionGroup group = new DisplayLanguageOptionGroup();
        JFrame mainframe = (JFrame) group.createTestWindow(null);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.pack();
        mainframe.setVisible(true);
    } //end main()

} //end DisplayLanguageOptionGroup

