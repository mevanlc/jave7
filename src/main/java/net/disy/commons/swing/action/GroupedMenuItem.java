package net.disy.commons.swing.action;

import javax.swing.Action;
import javax.swing.JMenuItem;
import net.disy.commons.core.util.Ensure;

public class GroupedMenuItem {
   public static final GroupedMenuItem NO_ITEM = null;
   private final ActionGroupId actionGroupId;
   private final JMenuItem menuItem;

   public GroupedMenuItem(Action action, ActionGroupId actionGroupId) {
      this(new JMenuItem(getEnsuredNotNull(action)), actionGroupId);
   }

   private static Action getEnsuredNotNull(Action action) {
      Ensure.ensureArgumentNotNull(action);
      return action;
   }

   public GroupedMenuItem(JMenuItem menuItem, ActionGroupId actionGroupId) {
      Ensure.ensureArgumentNotNull(menuItem);
      Ensure.ensureArgumentNotNull(actionGroupId);
      this.menuItem = menuItem;
      this.actionGroupId = actionGroupId;
   }

   public ActionGroupId getActionGroupId() {
      return this.actionGroupId;
   }

   public JMenuItem getMenuItem() {
      return this.menuItem;
   }
}
