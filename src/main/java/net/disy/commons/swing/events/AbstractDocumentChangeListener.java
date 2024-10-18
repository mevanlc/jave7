package net.disy.commons.swing.events;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class AbstractDocumentChangeListener implements DocumentListener {
   @Override
   public void insertUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   @Override
   public void removeUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   @Override
   public void changedUpdate(DocumentEvent e) {
      this.documentChanged();
   }

   protected abstract void documentChanged();
}
