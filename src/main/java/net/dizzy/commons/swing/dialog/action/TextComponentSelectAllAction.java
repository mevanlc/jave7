package net.dizzy.commons.swing.dialog.action;

import java.awt.Component;

import javax.swing.text.JTextComponent;

import net.dizzy.commons.swing.action.SmartAction;

public class TextComponentSelectAllAction extends SmartAction {
   private final JTextComponent textComponent;

   public TextComponentSelectAllAction(JTextComponent textComponent) {
      super("Select all");
      this.textComponent = textComponent;
   }

   @Override
   protected void execute(Component parent) {
      textComponent.selectAll();
   }
}
