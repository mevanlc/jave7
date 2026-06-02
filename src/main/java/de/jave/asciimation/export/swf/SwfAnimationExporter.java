package de.jave.asciimation.export.swf;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.actionscript.ActionScriptAnimationExportFormat;
import de.jave.asciimation.export.actionscript.ActionScriptAnimationExporter;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import net.dizzy.commons.core.exception.MessageException;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.Ensure;

public class SwfAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private ActionScriptAnimationExporter actionscriptExporter;
   private File actionScriptFile;
   private Dimension maxFrameSize;

   public SwfAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.maxFrameSize = maxFrameSize;
      AnimationExportOptions actionScriptExportOptions = new AnimationExportOptions(this.options.getPreferences());
      actionScriptExportOptions.setFormat(new ActionScriptAnimationExportFormat());
      this.actionScriptFile = File.createTempFile("animation", ".as");
      actionScriptExportOptions.setOutputFile(this.actionScriptFile);
      actionScriptExportOptions.getAdditionalOptions().setLoop(this.options.getAdditionalOptions().isLoop());
      this.actionscriptExporter = new ActionScriptAnimationExporter(actionScriptExportOptions);
      this.actionscriptExporter.init(maxFrameSize, animationProperties, frameCount, metaData);
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      this.actionscriptExporter.writeFrame(content);
   }

   @Override
   public void finish() throws Exception {
      this.actionscriptExporter.finish();
      String mtascPath = this.options.getAdditionalOptions().getMtascCompilerBinaryFile().getAbsolutePath();
      String asPath = this.actionScriptFile.getAbsolutePath();
      String outputPath = this.options.getFile().getAbsolutePath();
      Dimension platePixelSize = this.calculateExpectedPlatePixelSize(this.maxFrameSize);
      String fps = "20";
      String command = mtascPath
         + " -v -swf "
         + outputPath
         + " -main -header "
         + platePixelSize.width
         + ":"
         + platePixelSize.height
         + ":"
         + "20"
         + " "
         + asPath;
      System.out.println(command);
      Process process = Runtime.getRuntime().exec(command);
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
         System.out.println(line);
      }

      reader.close();
      int result = process.waitFor();
      this.cleanup();
      if (result != 0) {
         throw new MessageException(new Message("Unable to compile SWF file. External compiler exited with error code " + result + ".", MessageType.ERROR));
      }
   }

   private void cleanup() {
      this.actionScriptFile.delete();
   }

   @Override
   public void rollBack() {
      if (this.actionscriptExporter != null) {
         this.actionscriptExporter.rollBack();
      }
   }
}
