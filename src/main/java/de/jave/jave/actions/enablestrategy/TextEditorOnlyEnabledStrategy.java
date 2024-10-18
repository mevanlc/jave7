package de.jave.jave.actions.enablestrategy;

import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.IDocumentEditor;

public class TextEditorOnlyEnabledStrategy implements IJaveDocumentEditorActionEnabledStrategy {
   private static IJaveDocumentEditorActionEnabledStrategy instance = new TextEditorOnlyEnabledStrategy();

   public static IJaveDocumentEditorActionEnabledStrategy getInstance() {
      return instance;
   }

   private TextEditorOnlyEnabledStrategy() {
   }

   @Override
   public boolean isEnabledFor(IDocumentEditor activeEditor) {
      return activeEditor != null && activeEditor.getType() == JaveDocumentType.TEXT;
   }
}
