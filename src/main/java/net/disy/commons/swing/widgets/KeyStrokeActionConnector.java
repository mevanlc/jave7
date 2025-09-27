package net.disy.commons.swing.action;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeyStrokeActionConnector {
   public static void connect(JComponent component, Action action, String keyStrokeString) {
      connect(component, action, KeyStroke.getKeyStroke(keyStrokeString));
   }

   private static void connect(JComponent component, Action action, String actionKey, KeyStroke keyStroke) {
      InputMap inputMap = component.getInputMap();
      inputMap.put(keyStroke, actionKey);
      ActionMap actionMap = component.getActionMap();
      actionMap.put(actionKey, action);
   }

   public static void connect(JComponent component, Action action, KeyStroke keyStroke) {
      String actionKey = "ModifierBasedKeyStrokeActionKey" + keyStroke.getKeyCode() + "-" + keyStroke.getModifiers();
      connect(component, action, actionKey, keyStroke);
   }
}
