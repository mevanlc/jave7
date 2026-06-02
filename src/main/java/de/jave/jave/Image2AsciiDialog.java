package de.jave.jave;

import de.jave.gui.dialog.JDialogFactory;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.BatchConversionAction;
import de.jave.image2ascii.DefaultSettingsAction;
import de.jave.image2ascii.IImage2AsciiResultTaker;
import de.jave.image2ascii.Image2Texter;
import de.jave.image2ascii.dialog.Image2AsciiSourceFilePanel;
import de.jave.image2ascii.dialog.ImageOpenPerformer;
import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.watermark.WatermarkImageFile;
import de.jave.lib.CharacterPlate;
import de.jave.lib.gui.GuiUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.dizzy.commons.swing.dialog.message.MessageDialogUtilities;
import net.dizzy.commons.swing.dialog.message.YesNoCancel;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;

public class Image2AsciiDialog {
   private final IImage2AsciiResultTaker resultTaker;
   private final Image2Texter editor;
   private final JDialog dialog;
   private final Image2AsciiSourceImageModel sourceImageModel;

   public Image2AsciiDialog(
      Component parentComponent,
      IImage2AsciiResultTaker resultTaker,
      FileModel currentDirectoryModel,
      FontModel displayFontModel,
      AnimationExportPreferences animationExportPreferences,
      ObjectModel<ColorScheme> colorSchemeModel,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      Ensure.ensureArgumentNotNull(resultTaker);
      Ensure.ensureArgumentNotNull(colorSchemeModel);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.dialog = JDialogFactory.createJDialog(parentComponent, "Image2Ascii", true);
      this.dialog.setModal(true);
      this.resultTaker = resultTaker;
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            Image2AsciiDialog.this.performCancel();
         }
      });
      this.sourceImageModel = new Image2AsciiSourceImageModel(currentDirectoryModel);
      Image2AsciiOptionsModel optionsModel = new Image2AsciiOptionsModel();
      this.editor = new Image2Texter(
         this.sourceImageModel, optionsModel, displayFontModel, colorSchemeModel, gradientConfiguration, greyscaleTableConfiguration, filter
      );
      SmartAction insertAction = new SmartAction(DisyCommonsSwingDialogMessages.OK) {
         @Override
         protected void execute(Component parent) {
            Image2AsciiDialog.this.performOk(parent);
         }
      };
      SmartAction closeAction = new SmartAction(DisyCommonsSwingDialogMessages.CANCEL) {
         @Override
         protected void execute(Component parent) {
            Image2AsciiDialog.this.performCancel();
         }
      };
      JButton insertButton = new JButton(insertAction);
      ButtonPanelBuilder buttonPanelBuilder = new ButtonPanelBuilder();
      buttonPanelBuilder.add(new DefaultSettingsAction(optionsModel, this.sourceImageModel));
      buttonPanelBuilder.add(new BatchConversionAction(animationExportPreferences, optionsModel, displayFontModel, this.sourceImageModel));
      buttonPanelBuilder.add(insertButton);
      buttonPanelBuilder.add(closeAction);
      this.dialog.getRootPane().setDefaultButton(insertButton);
      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(this.editor.getContent(), "Center");
      this.dialog.getContentPane().add(buttonPanelBuilder.createPanel(), "South");
      this.dialog.pack();
      GuiUtilities.centerOnScreen(this.dialog);
   }

   private void performOk(Component parentComponent) {
      CharacterPlate cp = this.editor.getResult();
      if (cp != null) {
         YesNoCancel answer = MessageDialogUtilities.showYesNoCancelDialog(
            parentComponent, new Message("Image2Ascii", "Do you want to set the original image as watermark?", MessageType.QUESTION)
         );
         if (answer == YesNoCancel.CANCEL) {
            return;
         }

         WatermarkImageFile watermarkImageFile;
         if (answer == YesNoCancel.YES) {
            watermarkImageFile = new WatermarkImageFile(
               this.sourceImageModel.getSourceImage().getOriginalImage(), this.sourceImageModel.getFileModel().getValue()
            );
         } else {
            watermarkImageFile = null;
         }

         this.resultTaker.takeImage2AsciiResult(cp, watermarkImageFile);
      }

      this.dialog.setVisible(false);
   }

   public boolean open(File file) {
      return new ImageOpenPerformer(this.sourceImageModel).performOpen(this.dialog, file);
   }

   private void performCancel() {
      this.dialog.setVisible(false);
   }

   public void show() {
      if (this.sourceImageModel.getSourceImage() == null) {
         WindowListener loadImageOnFirstShowDialogListener = new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
               Image2AsciiDialog.this.dialog.removeWindowListener(this);
               Image2AsciiSourceFilePanel sourceFilePanel = new Image2AsciiSourceFilePanel(Image2AsciiDialog.this.sourceImageModel);
               sourceFilePanel.performOpenImage(Image2AsciiDialog.this.dialog);
            }
         };
         this.dialog.addWindowListener(loadImageOnFirstShowDialogListener);
      }

      this.dialog.setVisible(true);
   }
}
