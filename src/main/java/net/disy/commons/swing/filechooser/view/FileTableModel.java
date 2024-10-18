package net.disy.commons.swing.filechooser.view;

import java.io.File;
import javax.swing.table.AbstractTableModel;
import net.disy.commons.core.io.IOUtilities;

public class FileTableModel extends AbstractTableModel {
   private static final String[] COLUMN_NAMES = new String[]{"Name", "GrÃ¶ÃŸe"};
   private static final Class[] COLUMN_CLASSES = new Class[]{File.class, String.class};
   private File[] files = new File[0];

   public void setFiles(File[] files) {
      this.files = files;
      this.fireTableDataChanged();
   }

   @Override
   public int getColumnCount() {
      return 2;
   }

   @Override
   public int getRowCount() {
      return this.files.length;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex) {
      File file = this.getFile(rowIndex);
      if (columnIndex == 0) {
         return file;
      } else {
         return file.isDirectory() ? "" : IOUtilities.byteCountToDisplaySize(file.length());
      }
   }

   public File getFile(int rowIndex) {
      return this.files[rowIndex];
   }

   @Override
   public Class<?> getColumnClass(int columnIndex) {
      return COLUMN_CLASSES[columnIndex];
   }

   @Override
   public String getColumnName(int column) {
      return COLUMN_NAMES[column];
   }

   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
   }
}
