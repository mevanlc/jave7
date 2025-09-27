package de.jave.jave.actions.enablestrategy;

import de.jave.jave.plate.IDocumentEditor;

public class AlwaysEnabledStrategy implements IJaveDocumentEditorActionEnabledStrategy {
   private static IJaveDocumentEditorActionEnabledStrategy instance = new AlwaysEnabledStrategy();

   public static IJaveDocumentEditorActionEnabledStrategy getInstance() {
      return instance;
   }

   private AlwaysEnabledStrategy() {
   }

   @Override
   public boolean isEnabledFor(IDocumentEditor activeEditor) {
      return true;
   }
}
