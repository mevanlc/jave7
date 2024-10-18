package de.jave.jave.preferences;

import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import java.io.File;

public class AnimationExportPreferences extends SmartPreferences {
   private static final String KEY_MTASC_COMPILER_BINARY = "mtascCompilerBinary";

   public AnimationExportPreferences(JavePreferences javePreferences) {
      super(javePreferences.getSubPreferences("animationExport"));
   }

   public void setMtascCompilerBinaryFile(File mtascCompilerBinaryFile) {
      this.put("mtascCompilerBinary", mtascCompilerBinaryFile);
      this.flush();
   }

   public File getMtascCompilerBinaryFile() {
      return this.getFile("mtascCompilerBinary");
   }
}
