package net.disy.commons.swing.dialog.foldout;

import javax.swing.JComponent;

public abstract class AbstractFoldOutPage implements IFoldOutPage {
   private JComponent content;

   @Override
   public JComponent getContent() {
      if (this.content == null) {
         this.content = this.createContent();
      }

      return this.content;
   }

   protected abstract JComponent createContent();

   @Override
   public void dispose() {
   }
}
