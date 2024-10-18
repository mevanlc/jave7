package de.jave.figlet.swing.layout;

import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.primitives.IVerticalLayoutModeVisitor;
import de.jave.figlet.engine.primitives.VerticalLayoutMode;
import de.jave.figlet.engine.primitives.VerticalSmushingRules;
import de.jave.figlet.swing.ui.VerticalLayoutModeObjectUi;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import net.disy.commons.swing.button.DropDownButton;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class VerticalLayoutPanel extends AbstractLayoutPanel {
   private final JPanel panel;
   private final JComboBox modeCombo = new JComboBox<>(VerticalLayoutMode.values());
   private JCheckBoxMenuItem cbVerticalEqualCharacter;
   private JCheckBoxMenuItem cbVerticalHierarchy;
   private JCheckBoxMenuItem cbVerticalUnderscore;
   private JCheckBoxMenuItem cbVerticalHorizontalLine;
   private JCheckBoxMenuItem cbVerticalVerticalLine;
   private JRadioButtonMenuItem rbUniversal;
   private JRadioButtonMenuItem rbSpecific;

   public VerticalLayoutPanel(ActionListener listener) {
      this.modeCombo.setRenderer(new ObjectUiListCellRenderer(new VerticalLayoutModeObjectUi()));
      this.modeCombo.addActionListener(listener);
      final DropDownButton popupButton = new DropDownButton();
      popupButton.setToolTipText("Advanced layout options");
      this.modeCombo.setToolTipText("Vertical layout mode");
      JPanel modePanel = new JPanel(new BorderLayout(0, 0));
      modePanel.add(this.modeCombo, "Center");
      modePanel.add(popupButton.getComponent(), "East");
      final JPopupMenu smushingRulesPopupMenu = this.createSmushingRulesPopupMenu(listener);
      final JPopupMenu superSmushingDepthPopupMenu = this.createSupersmushingDepthPopupMenu(listener);
      this.modeCombo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            VerticalLayoutMode mode = (VerticalLayoutMode)VerticalLayoutPanel.this.modeCombo.getSelectedItem();
            mode.accept(new IVerticalLayoutModeVisitor() {
               @Override
               public void visitSmushing(VerticalLayoutMode mode) {
                  popupButton.setMenu(smushingRulesPopupMenu);
               }

               @Override
               public void visitVerticalFitting(VerticalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitFullHeight(VerticalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitSpacedVerticalFitting(VerticalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitSupersmushing(VerticalLayoutMode mode) {
                  popupButton.setMenu(superSmushingDepthPopupMenu);
               }

               @Override
               public void visitReverseSupersmushing(VerticalLayoutMode mode) {
                  popupButton.setMenu(superSmushingDepthPopupMenu);
               }
            });
         }
      });
      this.panel = modePanel;
   }

   private JPopupMenu createSmushingRulesPopupMenu(ActionListener listener) {
      ActionListener enabledListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            VerticalLayoutPanel.this.updateEnabled();
         }
      };
      this.rbUniversal = new JRadioButtonMenuItem("Universal smushing");
      this.rbUniversal.addActionListener(listener);
      this.rbUniversal.addActionListener(enabledListener);
      this.rbSpecific = new JRadioButtonMenuItem("Smushing rules:");
      this.rbSpecific.addActionListener(listener);
      this.rbSpecific.addActionListener(enabledListener);
      ButtonGroup group = new ButtonGroup();
      group.add(this.rbUniversal);
      group.add(this.rbSpecific);
      this.cbVerticalEqualCharacter = new JCheckBoxMenuItem("Equal character smushing");
      this.cbVerticalEqualCharacter.addActionListener(listener);
      this.cbVerticalUnderscore = new JCheckBoxMenuItem("Underscore smushing");
      this.cbVerticalUnderscore.addActionListener(listener);
      this.cbVerticalHierarchy = new JCheckBoxMenuItem("Hierarchy smushing");
      this.cbVerticalHierarchy.addActionListener(listener);
      this.cbVerticalHorizontalLine = new JCheckBoxMenuItem("Horizontal line smushing");
      this.cbVerticalHorizontalLine.addActionListener(listener);
      this.cbVerticalVerticalLine = new JCheckBoxMenuItem("Vertical line supersmushing");
      this.cbVerticalVerticalLine.addActionListener(listener);
      JPopupMenu menu = new JPopupMenu();
      menu.add(this.rbUniversal);
      menu.add(this.rbSpecific);
      menu.addSeparator();
      menu.add(this.cbVerticalEqualCharacter);
      menu.add(this.cbVerticalUnderscore);
      menu.add(this.cbVerticalHierarchy);
      menu.add(this.cbVerticalHorizontalLine);
      menu.add(this.cbVerticalVerticalLine);
      return menu;
   }

   private void updateEnabled() {
      boolean isUniversalSmushing = this.rbUniversal.isSelected();
      this.cbVerticalEqualCharacter.setEnabled(!isUniversalSmushing);
      this.cbVerticalHierarchy.setEnabled(!isUniversalSmushing);
      this.cbVerticalHorizontalLine.setEnabled(!isUniversalSmushing);
      this.cbVerticalUnderscore.setEnabled(!isUniversalSmushing);
      this.cbVerticalVerticalLine.setEnabled(!isUniversalSmushing);
   }

   public JComponent getContent() {
      return this.panel;
   }

   public void setLayout(FigLayout layout) {
      VerticalLayoutMode mode = layout.getVerticalLayoutMode();
      this.modeCombo.setSelectedItem(mode);
      VerticalSmushingRules rules = layout.getVerticalSmushingRules();
      this.cbVerticalEqualCharacter.setSelected(rules.isEqualCharacter());
      this.cbVerticalHierarchy.setSelected(rules.isHierarchy());
      this.cbVerticalUnderscore.setSelected(rules.isUnderscore());
      this.cbVerticalHorizontalLine.setSelected(rules.isHorizontalLine());
      this.cbVerticalVerticalLine.setSelected(rules.isVerticalLine());
      this.rbUniversal.setSelected(rules.isNoRulesSpecified());
      this.rbSpecific.setSelected(!rules.isNoRulesSpecified());
      this.setSelectedSupersmushingDepth(layout.getVerticalSupersmushingDepth());
   }

   public VerticalLayoutMode getLayoutMode() {
      return (VerticalLayoutMode)this.modeCombo.getSelectedItem();
   }

   public VerticalSmushingRules getSmushingRules() {
      VerticalSmushingRules rules = new VerticalSmushingRules();
      if (this.rbUniversal.isSelected()) {
         return rules;
      } else {
         rules.setEqualCharacter(this.cbVerticalEqualCharacter.isSelected());
         rules.setHierarchy(this.cbVerticalHierarchy.isSelected());
         rules.setUnderscore(this.cbVerticalUnderscore.isSelected());
         rules.setHorizontalLine(this.cbVerticalHorizontalLine.isSelected());
         rules.setVerticalLine(this.cbVerticalVerticalLine.isSelected());
         return rules;
      }
   }

   public void applyTo(FigLayout layout) {
      layout.setVerticalMode(this.getLayoutMode());
      layout.setVerticalSmushingRules(this.getSmushingRules());
      layout.setVerticalSupersmushingDepth(this.getSelectedSuperSmushingDepth());
   }
}
