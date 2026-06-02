package net.dizzy.commons.swing.showhide;

import javax.swing.JToggleButton;

import net.dizzy.commons.core.model.BooleanModel;

public class ShowHideButton extends JToggleButton {
   public ShowHideButton(BooleanModel model) {
      super();
      setSelected(Boolean.TRUE.equals(model.getValue()));
      addActionListener(event -> model.setValue(Boolean.valueOf(isSelected())));
      model.addChangeListener(() -> setSelected(Boolean.TRUE.equals(model.getValue())));
   }

   public ShowHideButton(BooleanModel model, String hideText, String showText) {
      this(model);
      setText(Boolean.TRUE.equals(model.getValue()) ? hideText : showText);
      model.addChangeListener(() -> setText(Boolean.TRUE.equals(model.getValue()) ? hideText : showText));
   }

   public JToggleButton getContent() {
      return this;
   }
}
