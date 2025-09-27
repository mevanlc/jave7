package de.jave.jave.browser;

import de.jave.gui.io.FileExtension;
import de.jave.gui.io.FileExtensions;
import de.jave.gui.io.ImageIOUtilities;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.disy.commons.core.util.Ensure;

public enum JaveFileType {
   RASTER_IMAGE(ImageIOUtilities.getSupportedReaderFileFormatExtensions()),
   TEXT(FileExtensions.TXT),
   ANIMATION(FileExtensions.JMOV),
   VT(FileExtensions.VT);

   private final List<FileExtension> extensions;

   private JaveFileType(FileExtension extension) {
      this(Arrays.asList(extension));
   }

   private JaveFileType(List<FileExtension> extensions) {
      Ensure.ensureArgumentNotNull(extensions);
      this.extensions = extensions;
   }

   public static JaveFileType guessType(File file) {
      FileExtension fileExtension = FileExtension.getFrom(file);

      for (JaveFileType type : values()) {
         if (type.extensions.contains(fileExtension)) {
            return type;
         }
      }

      return null;
   }
}
