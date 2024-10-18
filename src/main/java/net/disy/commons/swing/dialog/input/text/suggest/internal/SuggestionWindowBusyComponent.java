package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Cursor;
import net.disy.commons.swing.icon.CommonIcons;

public class SuggestionWindowBusyComponent extends AbstractSuggestionWindowLabelComponent {
   public SuggestionWindowBusyComponent(String busyLabelText) {
      super(busyLabelText, CommonIcons.REFRESH_ANIMATION);
   }

   @Override
   protected Cursor getCursor() {
      return Cursor.getPredefinedCursor(3);
   }
}
