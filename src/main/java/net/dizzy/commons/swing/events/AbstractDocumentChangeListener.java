package net.dizzy.commons.swing.events;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class AbstractDocumentChangeListener implements DocumentListener {
   @Override
   public void insertUpdate(DocumentEvent event) {
      documentChanged();
   }

   @Override
   public void removeUpdate(DocumentEvent event) {
      documentChanged();
   }

   @Override
   public void changedUpdate(DocumentEvent event) {
      documentChanged();
   }

   protected abstract void documentChanged();
}
