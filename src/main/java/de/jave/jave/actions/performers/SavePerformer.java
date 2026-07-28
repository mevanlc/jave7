package de.jave.jave.actions.performers;

import de.jave.asciimation.JmovFileChooserConfiguration;
import de.jave.gui.io.AcceptAllFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.jave.JaveMessages;
import de.jave.jave.PlateDocument;
import de.jave.jave.browser.IJaveDocumentTypeVisitor;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.persistence.JaveAnimationFileWriter;
import de.jave.lib.gui.IStatusDisplay;
import de.jave.maxosx.MacOsXInitializer;
import de.jave.util.RecentFileList;
import java.awt.Component;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.message.MessageDialogUtilities;
import net.dizzy.commons.swing.dialog.message.YesNoCancel;

public class SavePerformer {
   private static final int MAC_DIALOG_TRANSITION_DELAY_MS = 100;
   private static final String LAYERED_SAVE_REMINDER = "Your document contains multiple layers and must be saved in a dedicated\n"
      + ".javedoc file to preserve the layers. If you would like to save the document as\n"
      + "a single text file, first use the Layers menu to flatten the document to a\n"
      + "single layer.";

   private static boolean performSaveDocumentAs(
      Component parentComponent,
      IDocumentEditor editor,
      RecentFileList recentFileList,
      final FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener listener
   ) {
      PlateDocument doc = editor.getPlate().getDocument();
      if (doc.hasSecondaryLayers()) {
         return performSaveLayeredDocumentAs(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, listener, true);
      }
      File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return currentDirectoryModel;
         }

         @Override
         public String getSaveDialogTitle() {
            return "Save ASCII File";
         }

         @Override
         public String getOpenDialogTitle() {
            return null;
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[]{new AcceptAllFileFilter(), ExtensionFileFilters.TXT};
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }
      });
      if (file == null) {
         return false;
      } else {
         currentDirectoryModel.setValue(file.getParentFile());
         doc.setFile(file);
         boolean success = performSaveDocument(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, listener);
         if (!success) {
            doc.setFile(null);
         }

         listener.savePerformed();
         return success;
      }
   }

   private static boolean performSaveLayeredDocumentAs(
      Component parentComponent,
      IDocumentEditor editor,
      RecentFileList recentFileList,
      final FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener listener,
      boolean showReminder
   ) {
      if (showReminder && !showLayeredSaveReminder(parentComponent)) {
         return false;
      }
      PlateDocument doc = editor.getPlate().getDocument();
      File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return currentDirectoryModel;
         }

         @Override
         public String getSaveDialogTitle() {
            return "Save JavE Layered Document";
         }

         @Override
         public String getOpenDialogTitle() {
            return null;
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[]{ExtensionFileFilters.JAVEDOC};
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return false;
         }
      });
      if (file == null) {
         return false;
      } else {
         currentDirectoryModel.setValue(file.getParentFile());
         doc.setFile(file);
         boolean success = performSaveDocument(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, listener);
         if (!success) {
            doc.setFile(null);
         }
         listener.savePerformed();
         return success;
      }
   }

   private static boolean performSaveDocument(
      Component parentComponent,
      IDocumentEditor editor,
      RecentFileList recentFileList,
      FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener listener
   ) {
      PlateDocument doc = editor.getPlate().getDocument();
      if (!doc.hasFile()) {
         return performSaveDocumentAs(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, listener);
      } else if (doc.hasSecondaryLayers() && !doc.isJaveDocBacked()) {
         return performSaveLayeredDocumentAs(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, listener, true);
      } else {
         editor.getPlate().getToolManager().getCurrentTool().prepareForSave();

         try {
            doc.save(recentFileList);
            statusDisplay.showStatus("File saved.");
            listener.savePerformed();
            return true;
         } catch (IOException var8) {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message(JaveMessages.JavE, "Error saving file.", MessageType.ERROR, var8));
            return false;
         }
      }
   }

   private static boolean showLayeredSaveReminder(Component parentComponent) {
      return MessageDialogUtilities.showOkCancelDialog(
         parentComponent, new Message("JavE Layered Document", LAYERED_SAVE_REMINDER, MessageType.WARNING)
      );
   }

   private static boolean performSaveAnimationAs(
      Component parentComponent,
      AnimationDocumentEditor animationEditor,
      RecentFileList recentFileList,
      FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener listener
   ) {
      File file = FileChooserUtilities.performSaveFileChooser(parentComponent, new JmovFileChooserConfiguration(currentDirectoryModel));
      if (file == null) {
         return false;
      } else {
         JaveAnimationFile animationFile = animationEditor.getModel().getAnimationFile();
         animationFile.setFile(file);
         boolean success = performSaveAnimation(parentComponent, animationEditor, recentFileList, currentDirectoryModel, statusDisplay, listener);
         if (!success) {
            animationFile.setFile(null);
         }

         listener.savePerformed();
         return success;
      }
   }

   private static boolean performSaveAnimation(
      Component parentComponent,
      AnimationDocumentEditor animationEditor,
      RecentFileList recentFileList,
      FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener listener
   ) {
      JaveAnimationFile animationFile = animationEditor.getModel().getAnimationFile();
      if (animationFile.getFile() == null) {
         return performSaveAnimationAs(parentComponent, animationEditor, recentFileList, currentDirectoryModel, statusDisplay, listener);
      } else {
         try {
            new JaveAnimationFileWriter().write(animationFile, animationFile.getFile());
            animationEditor.getModel().setModified(false);
            recentFileList.add(animationFile.getFile());
            statusDisplay.showStatus("File saved.");
            listener.savePerformed();
            return true;
         } catch (Exception var8) {
            MessageDialogFactory.showMessageDialog(parentComponent, new Message("JavE Animation Editor", "Error saving file", var8));
            return false;
         }
      }
   }

   public static boolean performSaveAs(
      final Component parentComponent,
      final IDocumentEditor editor,
      final RecentFileList recentFileList,
      final FileModel currentDirectoryModel,
      final IStatusDisplay statusDisplay,
      final IDocumentSaveListener documentSaveListener
   ) {
      return editor.getType()
         .accept(
            new IJaveDocumentTypeVisitor<Boolean>() {
               public Boolean visitGame(JaveDocumentType type) {
                  return SavePerformer.performSaveDocumentAs(
                     parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }

               public Boolean visitAnimation(JaveDocumentType type) {
                  AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
                  return SavePerformer.performSaveAnimationAs(
                     parentComponent, animationEditor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }

               public Boolean visitText(JaveDocumentType type) {
                  return SavePerformer.performSaveDocumentAs(
                     parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }
            }
         );
   }

   public static boolean performSave(
      final Component parentComponent,
      final IDocumentEditor editor,
      final RecentFileList recentFileList,
      final FileModel currentDirectoryModel,
      final IStatusDisplay statusDisplay,
      final IDocumentSaveListener documentSaveListener
   ) {
      return editor.getType()
         .accept(
            new IJaveDocumentTypeVisitor<Boolean>() {
               public Boolean visitGame(JaveDocumentType type) {
                  return SavePerformer.performSaveDocument(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener);
               }

               public Boolean visitAnimation(JaveDocumentType type) {
                  AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
                  return SavePerformer.performSaveAnimation(
                     parentComponent, animationEditor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }

               public Boolean visitText(JaveDocumentType type) {
                  return SavePerformer.performSaveDocument(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener);
               }
            }
         );
   }

   public static boolean performSaveBeforeClose(
      final Component parentComponent,
      final IDocumentEditor editor,
      final RecentFileList recentFileList,
      final FileModel currentDirectoryModel,
      final IStatusDisplay statusDisplay,
      final IDocumentSaveListener documentSaveListener
   ) {
      return editor.getType()
         .accept(
            new IJaveDocumentTypeVisitor<Boolean>() {
               public Boolean visitGame(JaveDocumentType type) {
                  return SavePerformer.performSaveBeforeCloseDocument(
                     parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }

               public Boolean visitAnimation(JaveDocumentType type) {
                  AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
                  return SavePerformer.performSaveBeforeCloseAnimation(
                     parentComponent, animationEditor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }

               public Boolean visitText(JaveDocumentType type) {
                  return SavePerformer.performSaveBeforeCloseDocument(
                     parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener
                  );
               }
            }
         );
   }

   private static boolean performSaveBeforeCloseAnimation(
      Component parentComponent,
      AnimationDocumentEditor editor,
      RecentFileList recentFileList,
      FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener documentSaveListener
   ) {
      if (!editor.getModel().isModified()) {
         return true;
      } else {
         String question = "The animation "
            + DocumentEditorTitleFactory.createShortEditorTitle(editor)
            + " has been modified.\n"
            + "Do you want to save changes?";
         YesNoCancel answer = MessageDialogUtilities.showSaveDiscardCancelDialog(
            parentComponent, new Message("JavE Animation Editor", question, MessageType.WARNING)
         );
         if (answer == YesNoCancel.NO) {
            editor.getModel().setModified(false);
            return true;
         } else {
            return answer == YesNoCancel.CANCEL
               ? false
               : performSaveAfterPrompt(
                  () -> performSaveAnimation(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener)
               );
         }
      }
   }

   private static boolean performSaveBeforeCloseDocument(
      Component parentComponent,
      IDocumentEditor editor,
      RecentFileList recentFileList,
      FileModel currentDirectoryModel,
      IStatusDisplay statusDisplay,
      IDocumentSaveListener documentSaveListener
   ) {
      PlateDocument doc = editor.getPlate().getDocument();
      String question = "Document " + DocumentEditorTitleFactory.createShortEditorTitle(editor) + " has been modified.\n" + "Do you want to save changes?";
      YesNoCancel result = MessageDialogUtilities.showSaveDiscardCancelDialog(
         parentComponent, new Message(JaveMessages.JavE, question, MessageType.WARNING)
      );
      if (result == YesNoCancel.CANCEL) {
         return false;
      } else if (result == YesNoCancel.NO) {
         doc.setModified(false);
         return true;
      } else {
         return performSaveAfterPrompt(
            () -> performSaveDocument(parentComponent, editor, recentFileList, currentDirectoryModel, statusDisplay, documentSaveListener)
         );
      }
   }

   private static boolean performSaveAfterPrompt(BooleanSupplier saveOperation) {
      if (!MacOsXInitializer.isMacOs() || !SwingUtilities.isEventDispatchThread()) {
         return saveOperation.getAsBoolean();
      }

      AtomicBoolean result = new AtomicBoolean();
      AtomicReference<Throwable> failure = new AtomicReference<>();
      SecondaryLoop loop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
      // Aqua can discard a native save panel opened while the confirmation dialog is still tearing down.
      Timer timer = new Timer(MAC_DIALOG_TRANSITION_DELAY_MS, event -> {
         try {
            result.set(saveOperation.getAsBoolean());
         } catch (Throwable throwable) {
            failure.set(throwable);
         } finally {
            loop.exit();
         }
      });
      timer.setRepeats(false);
      timer.start();
      loop.enter();

      Throwable throwable = failure.get();
      if (throwable instanceof RuntimeException) {
         throw (RuntimeException)throwable;
      }
      if (throwable instanceof Error) {
         throw (Error)throwable;
      }
      return result.get();
   }
}
