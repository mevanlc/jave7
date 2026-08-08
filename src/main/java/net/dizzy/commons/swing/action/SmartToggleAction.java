package net.dizzy.commons.swing.action;

import javax.swing.AbstractButton;
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
   protected void execute(java.awt.Component source) {
      // An AbstractButton flips its own selected state - and mirrors it into
      // SELECTED_KEY - before it fires this action, so adopt that state instead
      // of flipping again, which would cancel the click out. A source that is
      // not a button (programmatic invocation) still needs the flip.
      setSelected(source instanceof AbstractButton ? ((AbstractButton)source).isSelected() : !isSelected());
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
