package de.jave.image2ascii.dialog;

import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.layout.util.ButtonPanelBuilder;
import net.dizzy.commons.swing.layout.util.LayoutDirection;
import net.dizzy.commons.swing.list.ListSelectionMode;
import net.dizzy.commons.swing.ui.AbstractObjectUi;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class SourceFileListPanel {
   private final JComponent content;
   private final SmartAction moveDownAction;
   private final SmartAction removeAction;
   private final SmartAction moveUpAction;
   private final BatchSourceImageModel model;
   private final JList list;
   private final FileModel currentDirectoryModel;

   public SourceFileListPanel(BatchSourceImageModel model, FileModel currentDirectoryModel) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.model = model;
      this.currentDirectoryModel = currentDirectoryModel;
      this.list = new JList(new BatchSourceImageModelListModel(model));
      this.list.setCellRenderer(new ObjectUiListCellRenderer(new AbstractObjectUi<File>() {
         public String getLabel(File value) {
            return value.getAbsolutePath();
         }
      }));
      SmartAction addAction = new SmartAction("Add...") {
         @Override
         protected void execute(Component parentComponent) {
            SourceFileListPanel.this.performAdd(parentComponent);
         }
      };
      this.removeAction = new SmartAction("Remove") {
         @Override
         protected void execute(Component parentComponent) {
            SourceFileListPanel.this.performRemoveSelected();
         }
      };
      this.moveUpAction = new SmartAction("Move up") {
         @Override
         protected void execute(Component parentComponent) {
            SourceFileListPanel.this.performMoveUp();
         }
      };
      this.moveDownAction = new SmartAction("Move down") {
         @Override
         protected void execute(Component parentComponent) {
            SourceFileListPanel.this.performMoveDown();
         }
      };
      this.list.getSelectionModel().setSelectionMode(ListSelectionMode.MULTIPLE_INTERVAL_SELECTION.getListSelectionMode());
      this.list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            SourceFileListPanel.this.updateActionsEnabled();
         }
      });
      this.updateActionsEnabled();
      ButtonPanelBuilder builder = new ButtonPanelBuilder(LayoutDirection.VERTICAL);
      builder.add(addAction);
      builder.add(this.removeAction);
      builder.add(this.moveUpAction);
      builder.add(this.moveDownAction);
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JScrollPane(this.list), "Center");
      panel.add(builder.createPanel(), "East");
      panel.add(new JLabel("Source files:"), "North");
      this.content = panel;
   }

   private void performMoveDown() {
      int index = this.list.getSelectedIndex();
      this.model.moveFileDown(index);
      this.list.setSelectedIndex(index + 1);
   }

   private void performMoveUp() {
      int index = this.list.getSelectedIndex();
      this.model.moveFileUp(index);
      this.list.setSelectedIndex(index - 1);
   }

   private void performRemoveSelected() {
      int[] selectedIndices = this.list.getSelectedIndices();

      for (int i = selectedIndices.length - 1; i >= 0; i--) {
         this.model.removeFileAt(selectedIndices[i]);
      }

      this.list.getSelectionModel().clearSelection();
   }

   private void performAdd(Component parentComponent) {
      FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(parentComponent, new IFileChooserConfiguration() {
         @Override
         public FileModel getCurrentDirectoryModel() {
            return SourceFileListPanel.this.currentDirectoryModel;
         }

         @Override
         public String getSaveDialogTitle() {
            return null;
         }

         @Override
         public String getOpenDialogTitle() {
            return "Open image file(s)";
         }

         @Override
         public SmartFileFilter[] getFileFilters() {
            return new SmartFileFilter[]{ExtensionFileFilters.SUPPORTED_IMAGES};
         }

         @Override
         public String getFileNameSuggestion() {
            return null;
         }

         @Override
         public boolean isMultipleOpenFileSelectionAllowed() {
            return true;
         }
      });
      if (!fileSelection.isEmpty()) {
         this.model.add(fileSelection.getFiles());
      }
   }

   private void updateActionsEnabled() {
      this.moveDownAction.setEnabled(this.list.getSelectedIndices().length == 1 && this.list.getSelectedIndex() != this.model.getFileCount() - 1);
      this.moveUpAction.setEnabled(this.list.getSelectedIndices().length == 1 && this.list.getSelectedIndex() > 0);
      this.removeAction.setEnabled(!this.list.getSelectionModel().isSelectionEmpty());
   }

   public JComponent getContent() {
      return this.content;
   }

   public void requestFocus() {
      this.list.requestFocus();
   }
}
