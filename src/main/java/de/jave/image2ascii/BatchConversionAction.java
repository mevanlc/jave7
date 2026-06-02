package de.jave.image2ascii;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.AnimationExportWizard;
import de.jave.asciimation.export.IAnimationExporter;
import de.jave.image.Rotation;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.image2ascii.dialog.BatchConversionSourceFilePage;
import de.jave.image2ascii.model.Image2AsciiImageProcessingOptionsModel;
import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.image2ascii.model.Image2AsciiOutputOptionsModel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import de.jave.jave.preferences.AnimationExportPreferences;
import java.awt.Component;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.progress.ProgressMonitorDialog;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public final class BatchConversionAction extends SmartAction {
   private final AnimationExportPreferences animationExportPreferences;
   private final Image2AsciiSourceImageModel sourceImageModel;
   private final FontModel displayFontModel;
   private final Image2AsciiOptionsModel optionsModel;

   public BatchConversionAction(
      AnimationExportPreferences animationExportPreferences,
      Image2AsciiOptionsModel optionsModel,
      FontModel displayFontModel,
      Image2AsciiSourceImageModel sourceImageModel
   ) {
      super("Batch Conversion");
      Ensure.ensureArgumentNotNull(animationExportPreferences);
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(sourceImageModel);
      this.animationExportPreferences = animationExportPreferences;
      this.optionsModel = optionsModel;
      this.displayFontModel = displayFontModel;
      this.sourceImageModel = sourceImageModel;
      sourceImageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            BatchConversionAction.this.updateEnabled();
         }
      });
      this.updateEnabled();
   }

   private void updateEnabled() {
      this.setEnabled(!this.sourceImageModel.isEmpty());
   }

   @Override
   protected void execute(Component parentComponent) {
      BatchConversionSourceFilePage sourceFilePage = new BatchConversionSourceFilePage(this.sourceImageModel.getCurrentDirectoryModel());
      AnimationExportOptions exportOptions = AnimationExportWizard.showOptionsDialogs(
         parentComponent,
         this.sourceImageModel.getCurrentDirectoryModel(),
         "Batch Conversion",
         sourceFilePage,
         this.displayFontModel.getFont(),
         this.animationExportPreferences
      );
      if (exportOptions != null) {
         IAnimationExporter exporter = exportOptions.getFormat().createExporter(exportOptions);
         File[] sourceFiles = sourceFilePage.getSourceFiles();
         Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = this.optionsModel.getImageProcessingOptionsModel();
         boolean normalize = imageProcessingOptionsModel.isNormalize();
         boolean invert = imageProcessingOptionsModel.isInvert();
         int highlight = imageProcessingOptionsModel.getHighlightValue();
         int shadow = imageProcessingOptionsModel.getShadowValue();
         double gamma = imageProcessingOptionsModel.getGammaValue();
         double sharpen = imageProcessingOptionsModel.getSharpenValue();
         IGreyscaleDithering dithering = imageProcessingOptionsModel.getDithering();
         Rotation rotate = imageProcessingOptionsModel.getRotation();
         Image2AsciiOutputOptionsModel outputOptionsModel = this.optionsModel.getOutputOptionsModel();
         double shapeFactor = outputOptionsModel.getShapeFactor();
         int newWidth = outputOptionsModel.getOutputWidth();
         ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(parentComponent, "Batch Conversion");
         BatchConversionThread batchConversionThread = new BatchConversionThread(
            sourceFiles,
            newWidth,
            shapeFactor,
            normalize,
            invert,
            highlight,
            shadow,
            gamma,
            sharpen,
            dithering,
            rotate,
            this.optionsModel.getAlgorithm(),
            exporter
         );

         try {
            progressDialog.run(batchConversionThread);
            String message = "Successfuly converted " + sourceFiles.length + " images.";
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("Image2Ascii - Batch Conversion", message, MessageType.INFORMATION));
         } catch (InterruptedException var24) {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("Batch conversion has been canceled.", MessageType.INFORMATION));
         } catch (InvocationTargetException var25) {
            var25.printStackTrace();
            MessageDialogFactory.showMessageDialog(parentComponent, new Message(var25.getCause().getMessage(), var25.getCause()));
         }
      }
   }
}
