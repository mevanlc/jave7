package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.core.model.listener.IChangeListener;

public class UpdateDialogPanelsListener implements IChangeListener {
   private IUpdatableSmartDialogPanel[] panels = new IUpdatableSmartDialogPanel[0];

   public void setPanels(IUpdatableSmartDialogPanel... panels) {
      this.panels = panels;
   }

   @Override
   public void stateChanged() {
      for (IUpdatableSmartDialogPanel panel : this.panels) {
         panel.update();
      }
   }
}
