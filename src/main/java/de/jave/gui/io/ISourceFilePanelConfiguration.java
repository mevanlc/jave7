package de.jave.gui.io;

public interface ISourceFilePanelConfiguration {
   IFileChooserConfiguration getFileChooserConfiguration();

   String getLabel();

   String getOpenButtonToolTipText();

   boolean isCloseAvailable();

   String getCloseButtonToolTipText();
}
