package net.disy.commons.swing.dialog.input.text.suggest;

public abstract class AbstractSuggestionTextFieldConfiguration<T> implements ISuggestionTextFieldConfiguration<T> {
   @Override
   public String getRawSearchText(T value) {
      return null;
   }
}
