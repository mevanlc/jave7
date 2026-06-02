package de.jave.gui.io;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import net.dizzy.commons.core.string.StringConcatenationBuilder;
import net.dizzy.commons.core.util.Ensure;

public class ExtensionFileFilter extends SmartFileFilter {
   private final String description;
   private final List<FileExtension> extensions;

   public ExtensionFileFilter(String description, List<FileExtension> extensions) {
      Ensure.ensureArgumentNotNull(description);
      Ensure.ensureArgumentNotNull(extensions);
      Ensure.ensureArgumentTrue("At least one extension must be given, was 0.", extensions.size() > 0);
      this.description = description;
      this.extensions = extensions;
   }

   public ExtensionFileFilter(String description, FileExtension... extensions) {
      this(description, Arrays.asList(extensions));
   }

   @Override
   public boolean accept(File file) {
      return file.isDirectory() ? true : this.fitsExtensions(file);
   }

   private boolean fitsExtensions(File file) {
      FileExtension fileExtension = FileExtension.getFrom(file);
      return this.extensions.contains(fileExtension);
   }

   @Override
   public String getDescription() {
      StringConcatenationBuilder builder = new StringConcatenationBuilder(",");

      for (FileExtension extension : this.extensions) {
         builder.append("*." + extension.getString());
      }

      return this.description + " (" + builder.getString() + ")";
   }

   @Override
   public File makeComplete(File file) {
      return this.fitsExtensions(file) ? file : new File(file.getAbsolutePath() + "." + this.extensions.get(0).getString());
   }

   public List<FileExtension> getExtensions() {
      return this.extensions;
   }
}
