package net.dizzy.commons.core.io;

import java.io.File;

public final class FileUtilities {
   private FileUtilities() {
   }

   public static File createFileNameSuggestion(IWorkingDirectoryProvider provider, String base, String extension) {
      File directory = provider == null ? new File(".") : provider.getWorkingDirectory();
      String normalizedExtension = extension == null || extension.isEmpty() ? "" : extension.startsWith(".") ? extension : "." + extension;
      File file = new File(directory, base + normalizedExtension);
      int suffix = 1;
      while (file.exists()) {
         file = new File(directory, base + suffix + normalizedExtension);
         suffix++;
      }
      return file;
   }
}
