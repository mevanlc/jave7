package net.disy.commons.swing.action;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import net.disy.commons.core.model.IModifiableBooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class ActionWidgetFactory {
   public static JToggleButton createToggleButton(final SmartToggleAction action) {
      final JToggleButton button = new JToggleButton(action);
      button.setSelected(action.getSelectionModel().getValue());
      action.getSelectionModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            button.setSelected(action.getSelectionModel().getValue());
         }
      });
      return button;
   }

   public static JMenuItem createToggleMenuItem(final SmartToggleAction action) {
      final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(action);
      menuItem.setSelected(action.getSelectionModel().getValue());
      action.getSelectionModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            menuItem.setSelected(action.getSelectionModel().getValue());
         }
      });
      return menuItem;
   }

   public static JCheckBox createCheckBox(final SmartToggleAction action) {
      final JCheckBox checkBox = new JCheckBox(action);
      checkBox.setSelected(action.getSelectionModel().getValue());
      action.getSelectionModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            checkBox.setSelected(action.getSelectionModel().getValue());
         }
      });
      return checkBox;
   }

   public static JButton createButton(SmartAction action) {
      JButton button = new JButton(action);
      button.setMargin(getInsets(action.getActionConfiguration()));
      button.setFocusPainted(isFocusPainted(action.getActionConfiguration()));
      return button;
   }

   private static Insets getInsets(IActionConfiguration configuration) {
      return isIconOnly(configuration) ? new Insets(0, 0, 0, 0) : null;
   }

   private static boolean isIconOnly(IActionConfiguration configuration) {
      return configuration.getIcon() != null && configuration.getName() == null;
   }

   private static boolean isFocusPainted(IActionConfiguration configuration) {
      return !isIconOnly(configuration);
   }

   public static JRadioButton createRadioButton(String label, final IModifiableBooleanModel model, ButtonGroup buttonGroup) {
      final JRadioButton radioButton = new JRadioButton(label);
      buttonGroup.add(radioButton);
      radioButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            model.setValue(radioButton.isSelected());
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            radioButton.setSelected(model.getValue());
         }
      });
      radioButton.setSelected(model.getValue());
      return radioButton;
   }
}
