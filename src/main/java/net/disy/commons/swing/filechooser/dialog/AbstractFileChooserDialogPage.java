package net.disy.commons.swing.filechooser.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.io.FileUtilities;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.input.text.ITextInputDialogConfiguration;
import net.disy.commons.swing.dialog.input.text.ITextInputDialogResult;
import net.disy.commons.swing.dialog.input.text.SmartTextInputDialog;
import net.disy.commons.swing.dialog.message.MessageDialogUtilities;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.filechooser.model.FileChooserModel;
import net.disy.commons.swing.filechooser.view.FileChooserPanel;
import net.disy.commons.swing.icon.CommonIcons;
import net.disy.commons.swing.icon.SwingIcons;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.toolbar.ToolBarBuilder;

public abstract class AbstractFileChooserDialogPage extends AbstractDialogPage {
   private final FileChooserPanel fileChooserPanel;
   private final FileChooserModel model;
   private final FileChooserDialogConfiguration configuration;
   private Action deleteAction = null;

   public AbstractFileChooserDialogPage(FileChooserModel model, FileChooserDialogConfiguration configuration, String defaultMessageText) {
      super(defaultMessageText);
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(configuration);
      this.model = model;
      this.configuration = this.getConfiguration(configuration);
      this.fileChooserPanel = new FileChooserPanel(model, this.configuration);
      this.fileChooserPanel.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            AbstractFileChooserDialogPage.this.fileChooserPanelActionPerformed();
         }
      });
      model.getFileModel().addChangeListener(this.getCheckInputValidListener());
   }

   protected FileChooserDialogConfiguration getConfiguration(FileChooserDialogConfiguration configurationData) {
      return configurationData;
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new BorderLayout(LayoutUtilities.getComponentSpacing(), LayoutUtilities.getComponentSpacing()));
      ToolBarBuilder builder = new ToolBarBuilder();
      builder.add(this.createRefreshAction());
      builder.addSeparator();
      builder.add(this.createDeleteAction());
      builder.add(this.createNewFolderAction(panel));
      IFileModelActionFactory[] actionFactories = this.getConfiguration().getActionFactories();

      for (int i = 0; i < actionFactories.length; i++) {
         builder.add(actionFactories[i].createAction(this.model.getFileModel()));
      }

      panel.add(builder.createToolBar(), "North");
      panel.add(this.fileChooserPanel.getContent(), "Center");
      return panel;
   }

   private Action createDeleteAction() {
      if (this.deleteAction == null) {
         this.deleteAction = new SmartAction("AusgewÃ¤hlte Datei oder Verzeichnis lÃ¶schen", CommonIcons.DELETE) {
            @Override
            protected void execute(Component parentComponent) {
               AbstractFileChooserDialogPage.this.executeDelete(parentComponent);
            }
         };
         this.model.getFileModel().addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               AbstractFileChooserDialogPage.this.updateDeleteActionEnabled(AbstractFileChooserDialogPage.this.deleteAction);
            }
         });
         this.updateDeleteActionEnabled(this.deleteAction);
      }

      return this.deleteAction;
   }

   private void executeDelete(Component parentComponent) {
      File file = this.model.getFileModel().getValue();
      if (file.isDirectory()) {
         if (!MessageDialogUtilities.confirmUserOperation(
            parentComponent, "Wollen Sie das ausgewÃ¤hlte Verzeichnis und alle Dateien, die darin enthalten sind, wirklich lÃ¶schen?", "BestÃ¤tigen"
         )) {
            return;
         }

         try {
            FileUtilities.deleteFileOrDirectory(file);
            this.fileChooserPanel.refresh();
         } catch (IOException var4) {
         }
      } else {
         if (!MessageDialogUtilities.confirmUserOperation(parentComponent, "Wollen Sie die ausgewÃ¤hlte Datei wirklich lÃ¶schen?", "BestÃ¤tigen")) {
            return;
         }

         if (file.delete()) {
            this.fileChooserPanel.refresh();
         }
      }
   }

   private Action createRefreshAction() {
      return new SmartAction("Ansicht aktualisieren", CommonIcons.REFRESH) {
         @Override
         protected void execute(Component parentComponent) {
            AbstractFileChooserDialogPage.this.fileChooserPanel.refresh();
         }
      };
   }

   private Action createNewFolderAction(final Component parent) {
      Action newFolderAction = new SmartAction("Neuen Ordner anlegen", SwingIcons.getFileViewNewFolderIcon()) {
         @Override
         protected void execute(Component parentComponent) {
            AbstractFileChooserDialogPage.this.executeCreateNewFolder(parent);
         }
      };
      return newFolderAction;
   }

   private void executeCreateNewFolder(Component parent) {
      File folder = this.model.getFolderModel().getValue();
      ITextInputDialogConfiguration dialogConfiguration = new NewFolderInputDialogConfiguration(folder);
      ITextInputDialogResult result = SmartTextInputDialog.showTextInputDialog(
         parent, dialogConfiguration, this.createNonExistingFileName(folder, "Neuer Ordner")
      );
      if (!result.isCanceled()) {
         File file = new File(folder, result.getText().trim());
         file.mkdir();
         this.fileChooserPanel.refresh();
         this.model.getFileModel().setValue(file);
      }
   }

   private String createNonExistingFileName(File folder, String name) {
      if (!new File(folder, name).exists()) {
         return name;
      } else {
         int index = 1;

         while (true) {
            String newName = name + " " + index;
            if (!new File(folder, newName).exists()) {
               return newName;
            }

            index++;
         }
      }
   }

   private void updateDeleteActionEnabled(Action deleteAction) {
      File file = this.model.getFileModel().getValue();
      deleteAction.setEnabled(file != null && file.exists());
   }

   protected void fileChooserPanelActionPerformed() {
      this.fireRequestFinish();
   }

   protected FileModel getFileModel() {
      return this.model.getFileModel();
   }

   protected FileChooserDialogConfiguration getConfiguration() {
      return this.configuration;
   }

   public void setAccessory(JComponent accessory) {
      this.fileChooserPanel.setAccessory(accessory);
   }
}
