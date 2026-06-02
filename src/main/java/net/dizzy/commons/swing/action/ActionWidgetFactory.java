package net.dizzy.commons.swing.action;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;

public final class ActionWidgetFactory {
   private ActionWidgetFactory() {
   }

   public static JButton createButton(Action action) {
      return new JButton(action);
   }

   public static JToggleButton createToggleButton(Action action) {
      return new JToggleButton(action);
   }

   public static JMenuItem createMenuItem(Action action) {
      return new JMenuItem(action);
   }

   public static JCheckBoxMenuItem createToggleMenuItem(Action action) {
      return new JCheckBoxMenuItem(action);
   }

   public static JCheckBox createCheckBox(Action action) {
      return new JCheckBox(action);
   }
}
