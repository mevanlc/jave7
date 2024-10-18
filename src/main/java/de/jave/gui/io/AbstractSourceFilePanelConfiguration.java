package de.jave.gui.io;

public abstract class AbstractSourceFilePanelConfiguration implements ISourceFilePanelConfiguration {
   @Override
   public String getOpenButtonToolTipText() {
      return null;
   }

   @Override
   public boolean isCloseAvailable() {
      return false;
   }

   @Override
   public String getCloseButtonToolTipText() {
      return null;
   }
}
