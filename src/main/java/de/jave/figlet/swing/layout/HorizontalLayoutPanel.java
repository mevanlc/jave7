package de.jave.figlet.swing.layout;

import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.primitives.HorizontalLayoutMode;
import de.jave.figlet.engine.primitives.HorizontalSmushingRules;
import de.jave.figlet.engine.primitives.IHorizontalLayoutModeVisitor;
import de.jave.figlet.swing.ui.HorizontalLayoutModeObjectUi;
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
import net.dizzy.commons.swing.button.DropDownButton;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class HorizontalLayoutPanel extends AbstractLayoutPanel {
   private final JPanel panel;
   private JCheckBoxMenuItem cbEqualCharacter;
   private JCheckBoxMenuItem cbUnderscore;
   private JCheckBoxMenuItem cbHierarchy;
   private JCheckBoxMenuItem cbOppositePair;
   private JCheckBoxMenuItem cbBigX;
   private JCheckBoxMenuItem cbHardblank;
   private final JComboBox modeCombo = new JComboBox<>(HorizontalLayoutMode.values());
   private JRadioButtonMenuItem rbUniversal;
   private JRadioButtonMenuItem rbSpecific;
   private final JPopupMenu smushingRulesPopup;
   private final JPopupMenu supersmushingDepthPopup;

   public HorizontalLayoutPanel(ActionListener listener) {
      this.modeCombo.setRenderer(new ObjectUiListCellRenderer(new HorizontalLayoutModeObjectUi()));
      this.modeCombo.addActionListener(listener);
      final DropDownButton popupButton = new DropDownButton();
      popupButton.setToolTipText("Advanced layout options");
      this.modeCombo.setToolTipText("Horizontal layout mode");
      JPanel modePanel = new JPanel(new BorderLayout(0, 0));
      modePanel.add(this.modeCombo, "Center");
      modePanel.add(popupButton.getComponent(), "East");
      this.smushingRulesPopup = this.createSmushingRulesPopupMenu(listener);
      this.supersmushingDepthPopup = this.createSupersmushingDepthPopupMenu(listener);
      this.modeCombo.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            HorizontalLayoutMode mode = (HorizontalLayoutMode)HorizontalLayoutPanel.this.modeCombo.getSelectedItem();
            mode.accept(new IHorizontalLayoutModeVisitor() {
               @Override
               public void visitSmushing(HorizontalLayoutMode mode) {
                  popupButton.setMenu(HorizontalLayoutPanel.this.smushingRulesPopup);
               }

               @Override
               public void visitKerning(HorizontalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitFullWidth(HorizontalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitSpacedKerning(HorizontalLayoutMode mode) {
                  popupButton.setMenu(null);
               }

               @Override
               public void visitSupersmushing(HorizontalLayoutMode mode) {
                  popupButton.setMenu(HorizontalLayoutPanel.this.supersmushingDepthPopup);
               }

               @Override
               public void visitReverseSupersmushing(HorizontalLayoutMode mode) {
                  popupButton.setMenu(HorizontalLayoutPanel.this.supersmushingDepthPopup);
               }

               @Override
               public void visitFixedWidth(HorizontalLayoutMode mode) {
                  popupButton.setMenu(null);
               }
            });
         }
      });
      this.panel = new JPanel(new BorderLayout());
      this.panel.add(modePanel, "North");
   }

   private JPopupMenu createSmushingRulesPopupMenu(ActionListener listener) {
      JPopupMenu menu = new JPopupMenu();
      ActionListener enabledListener = new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            HorizontalLayoutPanel.this.updateEnabled();
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
      this.cbEqualCharacter = new JCheckBoxMenuItem("Equal character smushing");
      this.cbEqualCharacter.addActionListener(listener);
      this.cbUnderscore = new JCheckBoxMenuItem("Underscore smushing");
      this.cbUnderscore.addActionListener(listener);
      this.cbHierarchy = new JCheckBoxMenuItem("Hierarchy smushing");
      this.cbHierarchy.addActionListener(listener);
      this.cbOppositePair = new JCheckBoxMenuItem("Opposite pair smushing");
      this.cbOppositePair.addActionListener(listener);
      this.cbBigX = new JCheckBoxMenuItem("Big X smushing");
      this.cbBigX.addActionListener(listener);
      this.cbHardblank = new JCheckBoxMenuItem("Hardblank smushing");
      this.cbHardblank.addActionListener(listener);
      menu.add(this.rbUniversal);
      menu.add(this.rbSpecific);
      menu.addSeparator();
      menu.add(this.cbEqualCharacter);
      menu.add(this.cbUnderscore);
      menu.add(this.cbHierarchy);
      menu.add(this.cbOppositePair);
      menu.add(this.cbBigX);
      menu.add(this.cbHardblank);
      return menu;
   }

   private void updateEnabled() {
      boolean isUniversalSmushing = this.rbUniversal.isSelected();
      this.cbBigX.setEnabled(!isUniversalSmushing);
      this.cbEqualCharacter.setEnabled(!isUniversalSmushing);
      this.cbHardblank.setEnabled(!isUniversalSmushing);
      this.cbHierarchy.setEnabled(!isUniversalSmushing);
      this.cbOppositePair.setEnabled(!isUniversalSmushing);
      this.cbUnderscore.setEnabled(!isUniversalSmushing);
   }

   public JComponent getContent() {
      return this.panel;
   }

   public void setLayout(FigLayout layout) {
      HorizontalLayoutMode mode = layout.getHorizontalLayoutMode();
      this.modeCombo.setSelectedItem(mode);
      HorizontalSmushingRules rules = layout.getHorizontalSmushingRules();
      this.cbEqualCharacter.setSelected(rules.isEqualCharacter());
      this.cbUnderscore.setSelected(rules.isUnderscore());
      this.cbHierarchy.setSelected(rules.isHierarchy());
      this.cbOppositePair.setSelected(rules.isOppositePair());
      this.cbBigX.setSelected(rules.isBigX());
      this.cbHardblank.setSelected(rules.isHardblank());
      this.rbUniversal.setSelected(rules.isNoRulesSpecified());
      this.rbSpecific.setSelected(!rules.isNoRulesSpecified());
      this.setSelectedSupersmushingDepth(layout.getHorizontalSupersmushingDepth());
   }

   public HorizontalLayoutMode getLayoutMode() {
      return (HorizontalLayoutMode)this.modeCombo.getSelectedItem();
   }

   public HorizontalSmushingRules getSmushingRules() {
      HorizontalSmushingRules rules = new HorizontalSmushingRules();
      if (this.rbUniversal.isSelected()) {
         return rules;
      } else {
         rules.setEqualCharacter(this.cbEqualCharacter.isSelected());
         rules.setUnderscore(this.cbUnderscore.isSelected());
         rules.setHierarchy(this.cbHierarchy.isSelected());
         rules.setOppositePair(this.cbOppositePair.isSelected());
         rules.setBigX(this.cbBigX.isSelected());
         rules.setHardblank(this.cbHardblank.isSelected());
         return rules;
      }
   }

   public void applyTo(FigLayout layout) {
      layout.setHorizontalMode(this.getLayoutMode());
      layout.setHorizontalSmushingRules(this.getSmushingRules());
      layout.setHorizontalSupersmushingDepth(this.getSelectedSuperSmushingDepth());
   }
}
