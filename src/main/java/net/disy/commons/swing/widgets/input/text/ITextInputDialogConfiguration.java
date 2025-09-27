package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.input.IInputDialogConfiguration;

public interface ITextInputDialogConfiguration extends IInputDialogConfiguration {
   String getLabelText();

   IBasicMessage createCurrentMessage(String var1);
}
