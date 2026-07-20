package net.dizzy.commons.swing.action;

import javax.swing.Icon;

public abstract class AbstractDizzyAction extends SmartAction {
   public static final String BASE_ICON = "baseIcon";

   public AbstractDizzyAction() {
   }

   public AbstractDizzyAction(String name) {
      super(name);
   }

   public AbstractDizzyAction(String name, Icon icon) {
      super(name, icon);
   }
}
