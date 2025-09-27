package de.jave.jave.actions.enablestrategy;

import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.IDocumentEditor;

public class AnimationEditorOnlyEnabledStrategy implements IJaveDocumentEditorActionEnabledStrategy {
   private static IJaveDocumentEditorActionEnabledStrategy instance = new AnimationEditorOnlyEnabledStrategy();

   public static IJaveDocumentEditorActionEnabledStrategy getInstance() {
      return instance;
   }

   private AnimationEditorOnlyEnabledStrategy() {
   }

   @Override
   public boolean isEnabledFor(IDocumentEditor activeEditor) {
      return activeEditor != null && activeEditor.getType() == JaveDocumentType.ANIMATION;
   }
}
