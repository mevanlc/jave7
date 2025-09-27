package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Cursor;

public class SuggestionWindowNoResultsComponent extends AbstractSuggestionWindowLabelComponent {
   public SuggestionWindowNoResultsComponent(String noResultsLabelText) {
      super(noResultsLabelText, null);
   }

   @Override
   protected Cursor getCursor() {
      return Cursor.getPredefinedCursor(0);
   }
}
