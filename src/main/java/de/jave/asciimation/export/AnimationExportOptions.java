package de.jave.asciimation.export;

import de.jave.jave.preferences.AnimationExportPreferences;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import net.dizzy.commons.core.util.Ensure;

public class AnimationExportOptions {
   private IAnimationExportFormat format;
   private File outputFile;
   private final Map<IAnimationExportFormat, AdditionalAnimationExportOptions> additionalOptionsByFormat = new HashMap<>();
   private final AnimationExportPreferences preferences;

   public AnimationExportOptions(AnimationExportPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
   }

   public IAnimationExportFormat getFormat() {
      return this.format;
   }

   public File getFile() {
      return this.outputFile;
   }

   public AdditionalAnimationExportOptions getAdditionalOptions() {
      if (!this.additionalOptionsByFormat.containsKey(this.getFormat())) {
         this.additionalOptionsByFormat.put(this.format, new AdditionalAnimationExportOptions(this.preferences));
      }

      return this.additionalOptionsByFormat.get(this.getFormat());
   }

   public String getFileNameWithoutExtension() {
      String outputFilename = this.outputFile.getAbsolutePath();
      if (this.outputFile.getName().indexOf(46) != -1) {
         int index = outputFilename.lastIndexOf(46);
         outputFilename = outputFilename.substring(0, index);
      }

      return outputFilename;
   }

   public void setOutputFile(File outputFile) {
      this.outputFile = outputFile;
   }

   public void setFormat(IAnimationExportFormat format) {
      this.format = format;
   }

   public AnimationExportPreferences getPreferences() {
      return this.preferences;
   }
}
