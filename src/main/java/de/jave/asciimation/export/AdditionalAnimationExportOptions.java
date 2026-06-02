package de.jave.asciimation.export;

import de.jave.jave.preferences.AnimationExportPreferences;
import java.awt.Font;
import java.io.File;
import net.dizzy.commons.core.util.Ensure;

public class AdditionalAnimationExportOptions {
   private int gifScale;
   private Font gifFont;
   private boolean connectedLinesView;
   private boolean loop = true;
   private boolean controls = true;
   private final AnimationExportPreferences preferences;

   public AdditionalAnimationExportOptions(AnimationExportPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
   }

   public void setGifScale(int gifScale) {
      this.gifScale = gifScale;
   }

   public int getGifScale() {
      return this.gifScale;
   }

   public void setGifFont(Font gifFont) {
      this.gifFont = gifFont;
   }

   public Font getGifFont() {
      return this.gifFont;
   }

   public void setConnectedLinesView(boolean connectedLinesView) {
      this.connectedLinesView = connectedLinesView;
   }

   public boolean isConnectedLinesView() {
      return this.connectedLinesView;
   }

   public boolean isLoop() {
      return this.loop;
   }

   public void setLoop(boolean loop) {
      this.loop = loop;
   }

   public void setControls(boolean controls) {
      this.controls = controls;
   }

   public boolean isControls() {
      return this.controls;
   }

   public void setMtascCompilerBinaryFile(File mtascCompilerBinaryFile) {
      this.preferences.setMtascCompilerBinaryFile(mtascCompilerBinaryFile);
   }

   public File getMtascCompilerBinaryFile() {
      return this.preferences.getMtascCompilerBinaryFile();
   }
}
