package de.jave.jave.plate;

import net.dizzy.commons.core.model.AbstractChangeableModel;

public class ActiveEditorModel extends AbstractChangeableModel {
   private IDocumentEditor activeEditor;

   public void setActiveEditor(IDocumentEditor activeEditor) {
      if (this.activeEditor != activeEditor) {
         this.activeEditor = activeEditor;
         this.fireChangeEvent();
      }
   }

   public IDocumentEditor getActiveEditor() {
      return this.activeEditor;
   }
}
