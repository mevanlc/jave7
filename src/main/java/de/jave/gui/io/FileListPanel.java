package de.jave.gui.io;

import de.jave.core.io.FileSelectionModel;
import de.jave.lib.collections.IntVector;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import net.dizzy.commons.core.model.listener.IChangeListener;

public class FileListPanel {
   private final JList list;
   private final JComponent content;
   private final FileSelectionModel selectionModel;
   private boolean isUpdatingSelection = false;

   public FileListPanel(FileSystemView fileSystemView, final FileSelectionModel selectionModel) {
      this.selectionModel = selectionModel;
      this.list = new JList();
      this.list.setCellRenderer(new FileListCellRenderer(fileSystemView));
      this.content = new JScrollPane(this.list);
      selectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FileListPanel.this.updateListSelection();
         }
      });
      this.list.addListSelectionListener(new ListSelectionListener() {
         @Override
         public void valueChanged(ListSelectionEvent e) {
            if (!FileListPanel.this.isUpdatingSelection) {
               Object[] selectedValues = FileListPanel.this.list.getSelectedValues();
               File[] selectedFiles = new File[selectedValues.length];

               for (int i = 0; i < selectedFiles.length; i++) {
                  selectedFiles[i] = (File)selectedValues[i];
               }

               selectionModel.setSelected(selectedFiles);
            }
         }
      });
   }

   private void updateListSelection() {
      IntVector selectedIndices = new IntVector();

      for (int i = 0; i < this.list.getModel().getSize(); i++) {
         File file = (File)this.list.getModel().getElementAt(i);
         if (this.selectionModel.isSelected(file)) {
            selectedIndices.add(i);
         }
      }

      this.isUpdatingSelection = true;
      this.list.setSelectedIndices(selectedIndices.toArray());
      this.isUpdatingSelection = false;
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setFiles(File[] files) {
      this.list.setListData(files);
   }

   public File[] getFiles() {
      File[] files = new File[this.list.getModel().getSize()];

      for (int i = 0; i < files.length; i++) {
         files[i] = (File)this.list.getModel().getElementAt(i);
      }

      return files;
   }

   public int getFileCount() {
      return this.list.getModel().getSize();
   }
}
