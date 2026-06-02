package net.dizzy.commons.swing.dialog.input.text;

import net.dizzy.commons.core.message.IBasicMessage;

public interface ITextInputDialogConfiguration {
   String getTitle();
   default String getMessage() { return getDefaultMessageText(); }
   default String getDefaultMessageText() { return ""; }
   default boolean isInputValid(String value) { return createCurrentMessage(value) == null; }
   default IBasicMessage createCurrentMessage(String selectedText) { return null; }
   default String getLabelText() { return ""; }
}
