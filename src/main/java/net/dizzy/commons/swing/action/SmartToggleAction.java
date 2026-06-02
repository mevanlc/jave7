package net.dizzy.commons.swing.action;

import javax.swing.Icon;

import net.dizzy.commons.core.model.BooleanModel;

public class SmartToggleAction extends SmartAction {
   private BooleanModel model;

   public SmartToggleAction(String name) {
      super(name);
   }

   public SmartToggleAction(String name, Icon icon) {
      super(name, icon);
   }

   public SmartToggleAction(BooleanModel model, Icon icon) {
      super(icon);
      this.model = model;
      setSelected(Boolean.TRUE.equals(model.getValue()));
   }

   public SmartToggleAction(BooleanModel model, String name) {
      super(name);
      this.model = model;
      setSelected(Boolean.TRUE.equals(model.getValue()));
   }

   public SmartToggleAction(BooleanModel model, String name, Icon icon) {
      super(name, icon);
      this.model = model;
      setSelected(Boolean.TRUE.equals(model.getValue()));
   }

   @Override
   protected void execute(java.awt.Component parent) {
      setSelected(!isSelected());
   }

   public boolean isSelected() {
      Object value = getValue(SELECTED_KEY);
      return Boolean.TRUE.equals(value);
   }

   public void setSelected(boolean selected) {
      putValue(SELECTED_KEY, Boolean.valueOf(selected));
      if (model != null && !Boolean.valueOf(selected).equals(model.getValue())) {
         model.setValue(Boolean.valueOf(selected));
      }
   }
}
