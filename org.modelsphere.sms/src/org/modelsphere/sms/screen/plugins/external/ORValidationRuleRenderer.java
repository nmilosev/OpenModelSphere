/**
 * 
 */
package org.modelsphere.sms.screen.plugins.external;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import org.modelsphere.jack.awt.AwtUtil;
import org.modelsphere.sms.db.srtypes.SMSMultiplicity;
import org.modelsphere.sms.or.db.srtypes.ORValidationRule;
import org.modelsphere.sms.or.features.ReferentialRules;

@SuppressWarnings("serial")
class ORValidationRuleRenderer extends DefaultListCellRenderer {
    private Font defaultFont;
    private Font bestFont;
    private Color defaultForeground;
    private Color incompatibleForeground;
    private Color defaultSelectionForeground;
    private Color incompatibleSelectionForeground;

    private SMSMultiplicity multiplicity;
    private SMSMultiplicity oppositeMultiplicity;
    private Boolean dependency;
    private int action = 0;
    private Boolean child;

    ORValidationRuleRenderer() {
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) super
                .getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (multiplicity == null || oppositeMultiplicity == null || dependency == null
                || child == null || action == -1) {
            setFont(defaultFont);
            setForeground(isSelected ? defaultSelectionForeground : defaultForeground);
            return renderer;
        }

        int parentmultiplicity = child ? oppositeMultiplicity.getValue() : multiplicity.getValue();
        int childmultiplicity = child ? multiplicity.getValue() : oppositeMultiplicity.getValue();
        ORValidationRule defaultRule = ReferentialRules.getInstance().getDefaultRule(
                parentmultiplicity, childmultiplicity, dependency, action,
                child ? ReferentialRules.CHILD_SIDE : ReferentialRules.PARENT_SIDE);
        ORValidationRule[] allowedRules = ReferentialRules.getInstance().getAllowedRules(
                parentmultiplicity, childmultiplicity, dependency, action,
                child ? ReferentialRules.CHILD_SIDE : ReferentialRules.PARENT_SIDE);

        if (defaultRule == value) {
            setFont(bestFont);
            setForeground(isSelected ? defaultSelectionForeground : defaultForeground);
        } else {
            boolean allowed = false;
            if (allowedRules != null) {
                for (int i = 0; i < allowedRules.length; i++) {
                    if (allowedRules[i] == value) {
                        allowed = true;
                    }
                }
            }
            if (allowed) {
                setFont(defaultFont);
                setForeground(isSelected ? defaultSelectionForeground : defaultForeground);
            } else {
                setFont(defaultFont);
                setForeground(isSelected ? incompatibleSelectionForeground : incompatibleForeground);
            }
        }

        return renderer;
    }

    @Override
    public void updateUI() {
        super.updateUI();
        defaultForeground = UIManager.getColor("List.foreground");
        incompatibleForeground = AwtUtil.midColor(defaultForeground, UIManager
                .getColor("List.background"));

        defaultSelectionForeground = UIManager.getColor("List.selectionForeground");
        incompatibleSelectionForeground = AwtUtil.midColor(defaultSelectionForeground, UIManager
                .getColor("List.selectionBackground"));

        defaultFont = getFont();
        bestFont = new Font(defaultFont.getName(), defaultFont.getStyle() | Font.BOLD, defaultFont
                .getSize());
    }

    public void setMultiplicity(SMSMultiplicity multiplicity) {
        this.multiplicity = multiplicity;
    }

    public void setOppositeMultiplicity(SMSMultiplicity oppositeMultiplicity) {
        this.oppositeMultiplicity = oppositeMultiplicity;
    }

    public void setDependency(Boolean dependency) {
        this.dependency = dependency;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public void setChild(Boolean child) {
        this.child = child;
    }

}