package de.jave.jave.actions.enablestrategy;

import de.jave.jave.plate.IDocumentEditor;

public class AnyEditorEnabledStrategy implements IJaveDocumentEditorActionEnabledStrategy {
   private static IJaveDocumentEditorActionEnabledStrategy instance = new AnyEditorEnabledStrategy();

   public static IJaveDocumentEditorActionEnabledStrategy getInstance() {
      return instance;
   }

   private AnyEditorEnabledStrategy() {
   }

   @Override
   public boolean isEnabledFor(IDocumentEditor activeEditor) {
      return activeEditor != null;
   }
}
