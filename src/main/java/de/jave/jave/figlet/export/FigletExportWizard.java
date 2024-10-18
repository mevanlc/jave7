package de.jave.jave.figlet.export;

import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.file.BaseFolderFigFileResource;
import de.jave.figlet.file.IFigFileResource;
import de.jave.figlet.util.FigException;
import de.jave.jave.JaveMessages;
import de.jave.jave.Plate;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.selection.SelectionAlgorithms;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.watermark.IWatermarkPainter;
import java.awt.Component;
import java.awt.Rectangle;
import java.io.File;
import java.text.MessageFormat;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.message.MessageDialogUtilities;
import net.disy.commons.swing.dialog.message.YesNoCancel;
import net.disy.commons.swing.dialog.wizard.AbstractWizardConfiguration;
import net.disy.commons.swing.dialog.wizard.IWizardPage;

public class FigletExportWizard extends AbstractWizardConfiguration {
   private static final String SUCCEED_MESSAGE = "Your new FIGlet font has been saved into JavE''s font folder as:\n      {0}\nThe font is now available in the category 'New Fonts'.\n\nPlease consider making your font public by informing the\npeople on the newsgroup alt.ascii-art or JavE's program author\n     markus@jave.de.\n\nDon't forget to backup a copy of this font so you will not\naccidently lose it!";
   private FigletExportWizardPageManager pageManager;
   private final FigletExportModel model;
   private final BooleanModel gridVisibilityModel;
   private final boolean rememberGrid;
   private final Plate plate;
   private final IWatermarkPainter painter;
   private final IFigDriver figDriver;

   public FigletExportWizard(IDocumentEditor editor, JaveApplicationPreferences preferences, IFigDriver figDriver) {
      Ensure.ensureArgumentNotNull(editor);
      Ensure.ensureArgumentNotNull(figDriver);
      this.plate = editor.getPlate();
      this.figDriver = figDriver;
      if (this.plate.hasSelection()) {
         Rectangle r = this.plate.getSelectionRegion();
         SelectionAlgorithms.dropSelection(editor);
         this.model = new FigletExportModel(this.plate, preferences);
         this.model.setCharacterWidth(r.width);
         this.model.setCharacterHeight(r.height);
         this.model.setCharacterDescent(1);
      } else {
         this.model = new FigletExportModel(this.plate, preferences);
      }

      this.painter = new FigletExportWatermarkPainter(this.model);
      this.gridVisibilityModel = this.plate.getPlatePreferences().getGridVisibilityModel();
      this.rememberGrid = this.gridVisibilityModel.getValue();
      this.gridVisibilityModel.setValue(false);
      this.plate.addWatermarkPainter(this.painter);
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FigletExportWizard.this.plate.repaint();
         }
      });
   }

   @Override
   public void addPages() {
      this.pageManager = new FigletExportWizardPageManager(this, this.model, this.figDriver);
   }

   @Override
   public IWizardPage getStartingPage() {
      return this.pageManager.getStartingPage();
   }

   @Override
   public IWizardPage getNextPage(IWizardPage page) {
      return this.pageManager.getNextPage(page);
   }

   @Override
   public IWizardPage getPreviousPage(IWizardPage page) {
      return this.pageManager.getPreviousPage(page);
   }

   @Override
   public IVetoDialogCloseHandler getVetoCloseHandler() {
      return new IVetoDialogCloseHandler() {
         @Override
         public boolean handleDialogAboutToClose(IDialogResult result, Component parentComponent) {
            if (result.isCanceled()) {
               return true;
            } else {
               IFigFileResource fileResource = FigletExportWizard.this.figDriver.getFileLibrary().getFileResource();
               BaseFolderFigFileResource folderResource = (BaseFolderFigFileResource)fileResource;
               File folder = folderResource.getBaseFolder();
               File file = new File(folder, FigletExportWizard.this.model.getName() + ".flf");
               if (file.exists()) {
                  YesNoCancel answer = MessageDialogUtilities.showYesNoCancelDialog(
                     parentComponent,
                     new Message(
                        JaveMessages.JavE,
                        "The font '" + FigletExportWizard.this.model.getName() + "' already exists.\nDo you want to replace the existing file?",
                        MessageType.WARNING
                     )
                  );
                  if (answer != YesNoCancel.YES) {
                     return false;
                  }
               }

               FigletFileExporter.export(FigletExportWizard.this.model, file);

               try {
                  FigletExportWizard.this.figDriver.getFileLibrary().initialize();
               } catch (FigException var9) {
                  var9.printStackTrace();
               }

               String filePath = file.getAbsoluteFile().getAbsolutePath();
               String message = MessageFormat.format(
                  "Your new FIGlet font has been saved into JavE''s font folder as:\n      {0}\nThe font is now available in the category 'New Fonts'.\n\nPlease consider making your font public by informing the\npeople on the newsgroup alt.ascii-art or JavE's program author\n     markus@jave.de.\n\nDon't forget to backup a copy of this font so you will not\naccidently lose it!",
                  filePath
               );
               MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE FIGlet font export assistant", message, MessageType.INFORMATION));
               return true;
            }
         }
      };
   }

   public void resetPlateView() {
      this.gridVisibilityModel.setValue(this.rememberGrid);
      this.plate.removeWatermarkPainter(this.painter);
      this.plate.repaint();
   }

   @Override
   public boolean isHelpAvailable() {
      return true;
   }
}
