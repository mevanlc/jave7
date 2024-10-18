package net.disy.commons.swing.filechooser.chooser;

import java.awt.Component;
import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.event.AncestorListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.BasicFileChooserUI;
import net.disy.commons.core.util.Ensure;

public class DefaultFileChooser implements IFileChooser {
   private final JFileChooser fileChooser;

   public DefaultFileChooser() {
      this(new JFileChooser());
   }

   public DefaultFileChooser(JFileChooser fileChooser) {
      Ensure.ensureArgumentNotNull(fileChooser);
      this.fileChooser = fileChooser;
   }

   @Override
   public void setFileSelectionMode(int fileSelectionMode) {
      this.fileChooser.setFileSelectionMode(fileSelectionMode);
   }

   @Override
   public void setAccessory(JComponent accessory) {
      this.fileChooser.setAccessory(accessory);
   }

   @Override
   public void setMinimumSize(Dimension minimumSize) {
      this.fileChooser.setMinimumSize(minimumSize);
   }

   @Override
   public void setPreferredSize(Dimension preferredSize) {
      this.fileChooser.setPreferredSize(preferredSize);
   }

   @Override
   public File getCurrentDirectory() {
      return this.fileChooser.getCurrentDirectory();
   }

   @Override
   public void setCurrentDirectory(File currentDirectory) {
      this.fileChooser.setCurrentDirectory(currentDirectory);
   }

   @Override
   public void setDialogTitle(String title) {
      this.fileChooser.setDialogTitle(title);
   }

   @Override
   public void setAcceptAllFileFilterUsed(boolean acceptAllFileFilterUsed) {
      this.fileChooser.setAcceptAllFileFilterUsed(acceptAllFileFilterUsed);
   }

   @Override
   public String getSuggestedFileName() {
      return ((BasicFileChooserUI)this.fileChooser.getUI()).getFileName();
   }

   @Override
   public void setSuggestedFileName(String suggestedFileName) {
      ((BasicFileChooserUI)this.fileChooser.getUI()).setFileName(suggestedFileName);
   }

   @Override
   public void setFileFilter(FileFilter fileFilter) {
      this.fileChooser.setFileFilter(fileFilter);
   }

   @Override
   public File getSelectedFile() {
      return this.fileChooser.getSelectedFile();
   }

   @Override
   public void setSelectedFile(File selectedFile) {
      this.fileChooser.setSelectedFile(selectedFile);
   }

   @Override
   public int showOpenDialog(Component parent) {
      return this.fileChooser.showOpenDialog(parent);
   }

   @Override
   public int showSaveDialog(Component parent) {
      return this.fileChooser.showSaveDialog(parent);
   }

   @Override
   public void resetChoosableFileFilters() {
      this.fileChooser.resetChoosableFileFilters();
   }

   @Override
   public void addChoosableFileFilter(FileFilter filter) {
      this.fileChooser.addChoosableFileFilter(filter);
   }

   @Override
   public String getUiDialogTitle() {
      return this.fileChooser.getUI().getDialogTitle(this.fileChooser);
   }

   @Override
   public Icon getSystemIcon(File file) {
      return this.fileChooser.getFileSystemView().getSystemIcon(file);
   }

   @Override
   public JComponent getContent() {
      return this.fileChooser;
   }

   @Override
   public FileFilter getFileFilter() {
      return this.fileChooser.getFileFilter();
   }

   @Override
   public void addPropertyChangeListener(PropertyChangeListener listener) {
      this.fileChooser.addPropertyChangeListener(listener);
   }

   @Override
   public void addAncestorListener(AncestorListener listener) {
      this.fileChooser.addAncestorListener(listener);
   }

   @Override
   public void requestFocus() {
      this.fileChooser.requestFocus();
   }

   @Override
   public FileFilter getAcceptAllFileFilter() {
      return this.fileChooser.getAcceptAllFileFilter();
   }

   @Override
   public void setMultipleSelectionEnabled(boolean multipleSelection) {
      this.fileChooser.setMultiSelectionEnabled(multipleSelection);
   }

   @Override
   public File[] getSelectedFiles() {
      return this.fileChooser.getSelectedFiles();
   }

   @Override
   public void removePropertyChangeListener(PropertyChangeListener listener) {
      this.fileChooser.removePropertyChangeListener(listener);
   }
}
