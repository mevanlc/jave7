package net.dizzy.commons.swing.action;

import javax.swing.Icon;

public abstract class AbstractDisyAction extends SmartAction {
   public static final String BASE_ICON = "baseIcon";

   public AbstractDisyAction() {
   }

   public AbstractDisyAction(String name) {
      super(name);
   }

   public AbstractDisyAction(String name, Icon icon) {
      super(name, icon);
   }
}
