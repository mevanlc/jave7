package net.disy.commons.swing.action;

import java.awt.Component;
import javax.swing.Icon;
import net.disy.commons.core.model.IModifiableBooleanModel;

public class SmartToggleAction extends SmartAction {
   private final IModifiableBooleanModel model;

   public SmartToggleAction(IModifiableBooleanModel model) {
      this.model = model;
   }

   public SmartToggleAction(IModifiableBooleanModel model, String name) {
      super(name);
      this.model = model;
   }

   public SmartToggleAction(IModifiableBooleanModel model, Icon icon) {
      super(icon);
      this.model = model;
   }

   public SmartToggleAction(IModifiableBooleanModel model, String name, Icon icon) {
      super(name, icon);
      this.model = model;
   }

   public IModifiableBooleanModel getSelectionModel() {
      return this.model;
   }

   @Override
   protected void execute(Component parentComponent) {
      this.model.setValue(!this.model.getValue());
   }
}
