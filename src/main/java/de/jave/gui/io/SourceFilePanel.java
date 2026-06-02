package de.jave.gui.io;

import de.jave.jave.actions.ButtonToolbarBuilder;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.io.NonEditableFileStringTextField;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class SourceFilePanel implements IDialogComponent {
   private final JButton openButton;
   private final NonEditableFileStringTextField tfImageName;
   private final FileModel fileModel;
   private final JButton closeButton;
   private final SmartAction openAction;
   private final ISourceFilePanelConfiguration configuration;

   public SourceFilePanel(final FileModel fileModel, ISourceFilePanelConfiguration configuration) {
      Ensure.ensureArgumentNotNull(fileModel);
      Ensure.ensureArgumentNotNull(configuration);
      this.fileModel = fileModel;
      this.configuration = configuration;
      final ObjectModel<String> filenameModel = new ObjectModel<>();
      this.tfImageName = new NonEditableFileStringTextField(filenameModel);
      this.openAction = new SmartAction(JaveIcons.OPEN_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            SourceFilePanel.this.performOpenImage(parentComponent);
         }
      };
      this.openAction.setToolTipText(configuration.getOpenButtonToolTipText());
      final SmartAction closeAction = new SmartAction(JaveIcons.CLOSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            if (SourceFilePanel.this.performCloseFile(parentComponent)) {
               fileModel.setValue(null);
            }
         }
      };
      closeAction.setToolTipText(configuration.getCloseButtonToolTipText());
      fileModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            SourceFilePanel.this.updateCloseActionEnabled(closeAction);
         }
      });
      this.updateCloseActionEnabled(closeAction);
      this.openButton = ButtonToolbarBuilder.createToolbarButton(this.openAction);
      this.closeButton = ButtonToolbarBuilder.createToolbarButton(closeAction);
      fileModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            SourceFilePanel.this.updateTextField(filenameModel);
            File file = fileModel.getValue();
            if (file != null) {
               SourceFilePanel.this.configuration.getFileChooserConfiguration().getCurrentDirectoryModel().setValue(file.getParentFile());
            }
         }
      });
      this.updateTextField(filenameModel);
   }

   private void updateCloseActionEnabled(SmartAction closeAction) {
      closeAction.setEnabled(this.fileModel.getValue() != null);
   }

   private void updateTextField(ObjectModel<String> filenameModel) {
      File file = this.fileModel.getValue();
      filenameModel.setValue(file == null ? null : file.getAbsolutePath());
   }

   public final void performOpenImage(Component parentComponent) {
      FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(parentComponent, this.configuration.getFileChooserConfiguration());
      if (!fileSelection.isEmpty()) {
         boolean success = this.performOpenFile(parentComponent, fileSelection.getFile());
         if (success) {
            this.fileModel.setValue(fileSelection.getFile());
         }
      }
   }

   public final JComponent createPanel() {
      GridDialogPanelBuilder panel = new GridDialogPanelBuilder();
      panel.add(this);
      return panel.createPanel();
   }

   @Override
   public final int getColumnCount() {
      return 3;
   }

   @Override
   public final void fillInto(JPanel panel, int columnCount) {
      panel.add(new JLabel(this.configuration.getLabel()));
      GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      data.setHorizontalSpan(columnCount - 2);
      panel.add(this.tfImageName.getContent(), data);
      JPanel buttonPanel = new JPanel(new GridLayout(1, 0));
      buttonPanel.add(this.openButton);
      if (this.configuration.isCloseAvailable()) {
         buttonPanel.add(this.closeButton);
      }

      panel.add(buttonPanel);
   }

   public final FileModel getFileModel() {
      return this.fileModel;
   }

   public final void setEnabled(boolean enabled) {
      this.openAction.setEnabled(enabled);
   }

   protected boolean performOpenFile(Component parentComponent, File file) {
      return true;
   }

   protected boolean performCloseFile(Component parentComponent) {
      return true;
   }
}
