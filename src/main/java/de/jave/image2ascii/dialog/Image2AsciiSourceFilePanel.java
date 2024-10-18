package de.jave.image2ascii.dialog;

import de.jave.gui.io.AbstractSourceFilePanelConfiguration;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.ISourceFilePanelConfiguration;
import de.jave.gui.io.ImageIOUtilities;
import de.jave.gui.io.SourceFilePanel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import java.awt.Component;
import java.io.File;

public final class Image2AsciiSourceFilePanel extends SourceFilePanel {
   private final Image2AsciiSourceImageModel model;

   public Image2AsciiSourceFilePanel(Image2AsciiSourceImageModel model) {
      super(model.getFileModel(), createConfiguration(model));
      this.model = model;
   }

   private static ISourceFilePanelConfiguration createConfiguration(Image2AsciiSourceImageModel model) {
      final IFileChooserConfiguration fileChooserConfiguration = ImageIOUtilities.createImageOpenFileChooserConfiguration(model.getCurrentDirectoryModel());
      ISourceFilePanelConfiguration configuration = new AbstractSourceFilePanelConfiguration() {
         @Override
         public String getOpenButtonToolTipText() {
            return "Open Image File";
         }

         @Override
         public IFileChooserConfiguration getFileChooserConfiguration() {
            return fileChooserConfiguration;
         }

         @Override
         public String getLabel() {
            return "File:";
         }
      };
      return configuration;
   }

   @Override
   protected boolean performOpenFile(Component parentComponent, File file) {
      return new ImageOpenPerformer(this.model).performOpen(parentComponent, file);
   }
}
