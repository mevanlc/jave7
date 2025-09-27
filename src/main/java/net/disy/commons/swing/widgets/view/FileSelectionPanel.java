package net.disy.commons.swing.filechooser.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.filechooser.model.FileChooserModel;
import net.disy.commons.swing.filechooser.util.FileComparator;
import net.disy.commons.swing.filechooser.util.FileObjectUi;
import net.disy.commons.swing.list.ListSelectionMode;
import net.disy.commons.swing.smarttable.ITableColumnViewSettings;
import net.disy.commons.swing.smarttable.SmartTable;
import net.disy.commons.swing.smarttable.columnsettings.ObjectUiTableColumnSettings;
import net.disy.commons.swing.smarttable.columnsettings.StringTableColumnSettings;

public class FileSelectionPanel extends AbstractActionComponent {
   private final JComponent content;
   private final FileSystemView fileSystemView;
   private FileTableModel fileTableModel;
   private SmartTable fileTable;
   private final FileChooserModel model;
   private boolean multipleSelection = false;

   public FileSelectionPanel(FileChooserModel model) {
      this(model, FileSystemView.getFileSystemView());
   }

   public FileSelectionPanel(FileChooserModel model, FileSystemView fileSystemView) {
      this.model = model;
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(fileSystemView);
      this.fileSystemView = fileSystemView;
      this.content = this.createContent();
      this.showFiles();
      this.updateSingleListSelection();
      this.initListeners();
   }

   private void initListeners() {
      this.model.getFolderModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileSelectionPanel.this.showFiles();
         }
      });
      this.model.getFileModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileSelectionPanel.this.updateSingleListSelection();
         }
      });
      this.model.getFileFilterModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileSelectionPanel.this.showFiles();
         }
      });
      this.fileTable.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
               int selectedRowIndex = FileSelectionPanel.this.fileTable.getSelectedRowIndex();
               if (selectedRowIndex != -1) {
                  FileSelectionPanel.this.model.getFileModel().setValue(FileSelectionPanel.this.fileTableModel.getFile(selectedRowIndex));
               }
            }
         }
      });
      this.fileTable.addSelectionActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FileSelectionPanel.this.fireActionEvent();
         }
      });
   }

   private void showFiles() {
      File folder = this.model.getFolderModel().getValue();
      File[] files = this.listFiles(folder);
      Arrays.sort(files, new FileComparator(this.fileSystemView));
      this.fileTableModel.setFiles(files);
   }

   private File[] listFiles(File folder) {
      if (folder == null) {
         return new File[0];
      } else {
         File[] allVisibleFiles = FileSystemViewUtilities.listFiles(this.fileSystemView, folder);
         final FileFilter fileFilter = this.model.getFileFilterModel().getFileFilter();
         return fileFilter == null ? allVisibleFiles : ArrayUtilities.filter(allVisibleFiles, new IPredicate<File>() {
            public boolean evaluate(File object) {
               return fileFilter.accept(object);
            }
         });
      }
   }

   private JComponent createContent() {
      this.fileTableModel = new FileTableModel();
      ITableColumnViewSettings<File> fileNameTableColumnSettings = new ObjectUiTableColumnSettings<>(new FileObjectUi(this.fileSystemView));
      ITableColumnViewSettings<String> fileSizeTableColumnSettings = new StringTableColumnSettings();
      this.fileTable = new SmartTable(this.fileTableModel, this.createViewSettings(fileNameTableColumnSettings, fileSizeTableColumnSettings));
      this.setMultipleSelectionEnabled(false);
      JScrollPane scrollPane = new JScrollPane(this.fileTable.getContent());
      scrollPane.setPreferredSize(new Dimension(250, 250));
      return scrollPane;
   }

   private ITableColumnViewSettings[] createViewSettings(
      ITableColumnViewSettings<File> fileNameTableColumnSettings, ITableColumnViewSettings<String> fileSizeTableColumnSettings
   ) {
      return new ITableColumnViewSettings[]{fileNameTableColumnSettings, fileSizeTableColumnSettings};
   }

   public JComponent getContent() {
      return this.content;
   }

   public void refresh() {
      this.showFiles();
   }

   private void updateSingleListSelection() {
      File file = this.model.getFileModel().getValue();
      if (file != null) {
         this.model.getFolderModel().setValue(file.getParentFile());
      }

      for (int index = 0; index < this.fileTableModel.getRowCount(); index++) {
         if (this.fileTableModel.getFile(index).equals(file)) {
            this.fileTable.scrollToAndSelect(index);
            return;
         }
      }

      this.fileTable.getTable().clearSelection();
   }

   public void setMultipleSelectionEnabled(boolean multipleSelection) {
      this.multipleSelection = multipleSelection;
      if (multipleSelection) {
         this.fileTable.setSelectionMode(ListSelectionMode.MULTIPLE_INTERVAL_SELECTION);
      } else {
         this.fileTable.setSelectionMode(ListSelectionMode.SINGLE_SELECTION);
      }
   }
}
