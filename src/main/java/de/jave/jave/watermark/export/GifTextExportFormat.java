package de.jave.jave.actions.export;

import de.jave.asciimation.export.FileTypeIcons;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.SmartFileFilter;
import de.jave.image.swing.ImagePanel;
import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.jave.export.Ascii2ImageOptions;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.swing.image.ClipboardImage;
import net.jmge.gif.TooManyColorsException;
import net.jmge.gif.facade.GifFileWriter;

public final class GifTextExportFormat implements ITextExportFormat {
   @Override
   public String getId() {
      return "gifimage";
   }

   @Override
   public String getName() {
      return "GIF image";
   }

   @Override
   public Icon getIcon() {
      return FileTypeIcons.GIF_ICON;
   }

   @Override
   public boolean isConnectedLinesViewSupported() {
      return true;
   }

   @Override
   public boolean isFontSupported() {
      return true;
   }

   @Override
   public JComponent createPreviewComponent(CharacterPlate plate, ITextExportOptions options) {
      BufferedImage image = this.convert(plate, options);
      ImagePanel imageCanvas = new ImagePanel(image);
      return imageCanvas.getContent();
   }

   private BufferedImage convert(CharacterPlate plate, ITextExportOptions options) {
      CharacterPlate exportPlate;
      if (options.isTrim()) {
         Insets insets = plate.getEmptyInsets();
         int newWidth = plate.getWidth() - insets.left - insets.right;
         int newHeight = plate.getHeight() - insets.bottom - insets.top;
         if (newWidth > 0 && newHeight > 0) {
            exportPlate = plate.getCopy(new Rectangle(insets.left, insets.top, newWidth, newHeight));
         } else {
            exportPlate = new CharacterPlate(new Dimension(1, 1));
         }
      } else {
         exportPlate = plate;
      }

      Font font = options.getFont();
      boolean connectedLinesView = options.isConnectedLinesView();
      Color foregroundColor = options.getForegroundColor();
      Color backgroundColor = options.getBackgroundColor();
      Ascii2ImageOptions selectedOptions = new Ascii2ImageOptions(font, connectedLinesView, foregroundColor, backgroundColor);
      return AsciiToThumbnailConverter.convert(exportPlate, selectedOptions);
   }

   @Override
   public void convertTo(CharacterPlate plate, ITextExportOptions options, Clipboard clipboard) {
      ClipboardImage clipboardImage = new ClipboardImage(this.convert(plate, options));
      clipboard.setContents(clipboardImage, clipboardImage);
   }

   @Override
   public void convertTo(CharacterPlate plate, ITextExportOptions options, File file) throws IOException {
      BufferedImage image = this.convert(plate, options);

      try {
         GifFileWriter gifFileWriter = new GifFileWriter(image);
         gifFileWriter.setComments(JaveTitleProvider.CREATED_BY_JAVE);
         gifFileWriter.write(file);
      } catch (TooManyColorsException var6) {
         throw new UnreachableCodeReachedException(var6);
      }
   }

   @Override
   public SmartFileFilter[] getFileFilters() {
      return new SmartFileFilter[]{ExtensionFileFilters.GIF};
   }
}
