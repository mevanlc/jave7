package net.disy.commons.swing.dialog.input.text.suggest;

import net.disy.commons.core.text.ISuggestionsForStringProvider;
import net.disy.commons.swing.ui.IObjectUi;

public interface ISuggestionTextFieldConfiguration<T> {
   String getToolTipText();

   String getBusyLabelText();

   String getNoResultLabelText();

   IObjectUi<T> getObjectUi();

   String getRawSearchText(T var1);

   ISuggestionActionHandler<T> getActionHandler();

   ISuggestionsForStringProvider<T> getSuggestionsForStringProvider();

   ISwingIOExceptionHandler getErrorHandler();
}
