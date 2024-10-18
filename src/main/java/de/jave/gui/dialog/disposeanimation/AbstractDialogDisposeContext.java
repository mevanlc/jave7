package de.jave.gui.dialog.disposeanimation;

import java.awt.Component;
import java.awt.Rectangle;

public abstract class AbstractDialogDisposeContext implements IDialogDisposeContext {
   protected Rectangle getAreaOnScreen(Component component) {
      return component == null ? null : new Rectangle(component.getLocationOnScreen(), component.getSize());
   }
}
