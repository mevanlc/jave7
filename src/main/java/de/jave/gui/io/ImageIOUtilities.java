package de.jave.gui.io;

import de.jave.jave.swing.JaveSwingMessages;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.util.CollectionUtilities;
import net.dizzy.commons.core.util.ITransformer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class ImageIOUtilities {
   public static List<FileExtension> getSupportedReaderFileFormatExtensions() {
      String[] formats = ImageIO.getReaderFormatNames();
      Set<String> suffixes = new HashSet<>();

      for (int i = 0; i < formats.length; i++) {
         suffixes.add(formats[i].toLowerCase());
      }

      List<String> extensions = new ArrayList<>(suffixes);
      Collections.sort(extensions);
      return CollectionUtilities.transform(extensions, new ITransformer<String, FileExtension>() {
         public FileExtension transform(String extension) {
            return new FileExtension(extension);
         }
      });
   }

   public static IFileChooserConfiguration createImageOpenFileChooserConfiguration(final FileModel currentDirectoryModel) {
      return new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return currentDirectoryModel;
         }

         @Override
         public String getSaveDialogTitle() {
            return null;
         }

         @Override
         public String getOpenDialogTitle() {
            return JaveSwingMessages.ImageFileChooser_OpenImage_DialogTitle;
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[]{ExtensionFileFilters.SUPPORTED_IMAGES};
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }
      };
   }

   public static boolean hasSupportedImageFileExtension(File file) {
      FileExtension fileExtension = FileExtension.getFrom(file);
      List<FileExtension> supportedExtensions = getSupportedReaderFileFormatExtensions();
      return supportedExtensions.contains(fileExtension);
   }

   public static void write(RenderedImage image, String format, File file) throws IOException {
      //    ImageIO.scanForPlugins();
      if (format == null) {
         format = guessImageFormat(file.getName());
      }
      if (format == null) {
         throw new IllegalArgumentException("No image format specified and unable to guess from file name '" //$NON-NLS-1$
                 + file.getAbsolutePath()
                 + "'"); //$NON-NLS-1$
      }

      File folder = file.getParentFile();
      if (folder != null) {
         folder.mkdirs();
      }
      boolean written = ImageIO.write(image, format, file);
      if (!written) {
         throw new IOException("Unable to write image in format '" //$NON-NLS-1$
                 + format + "'. Supported formats: " //$NON-NLS-1$
                 + getAvailableFormatNamesAsString());
      }
   }
   public static String guessImageFormat(String fileName) {
      int index = fileName.lastIndexOf('.');
      if (index == -1) {
         return null;
      }
      return fileName.substring(index + 1);
   }

   private static String getAvailableFormatNamesAsString() {
      String[] formatNames = ImageIO.getWriterFormatNames();
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < formatNames.length; ++i) {
         sb.append(formatNames[i]);
         if (i < formatNames.length - 1) {
            sb.append(", "); //$NON-NLS-1$
         }
      }
      return sb.toString();
   }

}
