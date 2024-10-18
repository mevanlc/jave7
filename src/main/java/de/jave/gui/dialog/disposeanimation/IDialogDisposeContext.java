package de.jave.gui.dialog.disposeanimation;

import java.awt.Rectangle;
import javax.swing.JFrame;

public interface IDialogDisposeContext {
   JFrame getParentFrame();

   Rectangle getDialogAreaOnScreen();

   Rectangle getTargetAreaOnScreen();
}
