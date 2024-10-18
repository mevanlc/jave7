package de.jave.jave.actions.enablestrategy;

import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.IDocumentEditor;

public class TextAndAnimationEditorEnabledStrategy implements IJaveDocumentEditorActionEnabledStrategy {
   private static IJaveDocumentEditorActionEnabledStrategy instance = new TextAndAnimationEditorEnabledStrategy();

   public static IJaveDocumentEditorActionEnabledStrategy getInstance() {
      return instance;
   }

   private TextAndAnimationEditorEnabledStrategy() {
   }

   @Override
   public boolean isEnabledFor(IDocumentEditor activeEditor) {
      return activeEditor != null && (activeEditor.getType() == JaveDocumentType.TEXT || activeEditor.getType() == JaveDocumentType.ANIMATION);
   }
}
