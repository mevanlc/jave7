package de.jave.gui.io;

import de.jave.lib.gui.GuiUtilities;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Window;
import java.io.File;
import java.io.FilenameFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.io.FileModel;

public class FileChooserUtilities {
   public static boolean useAwtFileChooser = false;

   public static FileSelection performOpenFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      return useAwtFileChooser ? performAwtOpenFileChooser(parentComponent, configuration) : performSwingOpenFileChooser(parentComponent, configuration);
   }

   private static FileSelection performAwtOpenFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      FileDialog dialog = createFileDialog(parentComponent);
      dialog.setTitle(configuration.getOpenDialogTitle());
      dialog.setMode(0);
      applyFileFilters(configuration, dialog);
      String fileNameSuggestion = configuration.getFileNameSuggestion();
      if (fileNameSuggestion != null) {
         dialog.setFile(fileNameSuggestion);
      }

      FileModel currentDirectoryModel = configuration.getCurrentDirectoryModel();
      File directory = currentDirectoryModel.getValue();
      if (directory != null) {
         dialog.setDirectory(directory.getAbsolutePath());
      }

      GuiUtilities.centerToParent(dialog);
      dialog.setVisible(true);
      String fileName = dialog.getFile();
      if (fileName == null) {
         return FileSelection.createEmpty();
      } else {
         String directoryName = dialog.getDirectory();
         File newDirectory = directoryName == null ? new File(".") : new File(directoryName);
         File file = new File(newDirectory, fileName);
         configuration.getCurrentDirectoryModel().setValue(newDirectory);
         return new FileSelection(file);
      }
   }

   private static void applyFileFilters(IFileChooserConfiguration configuration, FileDialog dialog) {
      final SmartFileFilter[] fileFilters = configuration.getFileFilters();
      dialog.setFilenameFilter(new FilenameFilter() {
         @Override
         public boolean accept(File dir, String name) {
            if (fileFilters.length == 0) {
               return true;
            } else {
               for (SmartFileFilter filter : fileFilters) {
                  if (filter.accept(new File(dir, name))) {
                     return true;
                  }
               }

               return false;
            }
         }
      });
   }

   private static FileDialog createFileDialog(Component parentComponent) {
      Window window = GuiUtilities.getWindowForComponent(parentComponent);
      if (window instanceof Dialog) {
         return new FileDialog((Dialog)window);
      } else {
         return window instanceof Frame ? new FileDialog((Frame)window) : new FileDialog(JOptionPane.getRootFrame());
      }
   }

   private static FileSelection performSwingOpenFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setMultiSelectionEnabled(configuration.isMultipleOpenFileSelectionAllowed());
      fileChooser.setDialogTitle(configuration.getOpenDialogTitle());
      FileModel currentDirectoryModel = configuration.getCurrentDirectoryModel();
      File directory = currentDirectoryModel.getValue();
      if (directory != null) {
         fileChooser.setCurrentDirectory(directory);
      }

      SmartFileFilter[] fileFilters = configuration.getFileFilters();
      adjustFileFilters(fileChooser, fileFilters);
      String fileNameSuggestion = configuration.getFileNameSuggestion();
      if (fileNameSuggestion != null) {
         File file;
         if (directory == null) {
            file = new File(fileNameSuggestion);
         } else {
            file = new File(directory, fileNameSuggestion);
         }

         fileChooser.setSelectedFile(file);
      }

      int answer = fileChooser.showOpenDialog(parentComponent);
      if (answer != 0) {
         return FileSelection.createEmpty();
      } else if (configuration.isMultipleOpenFileSelectionAllowed()) {
         File[] files = fileChooser.getSelectedFiles();
         if (files.length > 0) {
            currentDirectoryModel.setValue(files[0].getParentFile());
         }

         return new FileSelection(files);
      } else {
         File file = fileChooser.getSelectedFile();
         if (file != null) {
            currentDirectoryModel.setValue(file.getParentFile());
         }

         return new FileSelection(file);
      }
   }

   public static File performSaveFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      return useAwtFileChooser ? performAwtSaveFileChooser(parentComponent, configuration) : performSwingSaveFileChooser(parentComponent, configuration);
   }

   private static File performAwtSaveFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      FileDialog dialog = createFileDialog(parentComponent);
      dialog.setTitle(configuration.getSaveDialogTitle());
      dialog.setMode(1);
      applyFileFilters(configuration, dialog);
      FileModel currentDirectoryModel = configuration.getCurrentDirectoryModel();
      File directory = currentDirectoryModel.getValue();
      if (directory != null) {
         dialog.setDirectory(directory.getAbsolutePath());
      }

      GuiUtilities.centerToParent(dialog);
      dialog.setVisible(true);
      String fileName = dialog.getFile();
      if (fileName == null) {
         return null;
      } else {
         String directoryName = dialog.getDirectory();
         File newDirectory = directoryName == null ? new File(".") : new File(directoryName);
         File file = new File(newDirectory, fileName);
         configuration.getCurrentDirectoryModel().setValue(newDirectory);
         return makeComplete(file, configuration.getFileFilters());
      }
   }

   private static File makeComplete(File file, SmartFileFilter[] fileFilters) {
      if (fileFilters.length == 0) {
         return file;
      } else {
         for (SmartFileFilter filter : fileFilters) {
            if (filter.accept(file)) {
               return file;
            }
         }

         return fileFilters[0].makeComplete(file);
      }
   }

   private static File performSwingSaveFileChooser(Component parentComponent, IFileChooserConfiguration configuration) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle(configuration.getSaveDialogTitle());
      FileModel currentDirectoryModel = configuration.getCurrentDirectoryModel();
      File directory = currentDirectoryModel.getValue();
      if (directory != null) {
         fileChooser.setCurrentDirectory(directory);
      }

      SmartFileFilter[] fileFilters = configuration.getFileFilters();
      adjustFileFilters(fileChooser, fileFilters);
      String fileNameSuggestion = configuration.getFileNameSuggestion();
      if (fileNameSuggestion != null) {
         File file;
         if (directory == null) {
            file = new File(fileNameSuggestion);
         } else {
            file = new File(directory, fileNameSuggestion);
         }

         fileChooser.setSelectedFile(file);
      }

      int answer = fileChooser.showSaveDialog(parentComponent);
      if (answer != 0) {
         return null;
      } else {
         File file = fileChooser.getSelectedFile();
         if (file == null) {
            return null;
         } else {
            currentDirectoryModel.setValue(file.getParentFile());
            FileFilter selectedFilter = fileChooser.getFileFilter();
            if (selectedFilter instanceof SmartFileFilter) {
               SmartFileFilter smartFilter = (SmartFileFilter)selectedFilter;
               file = smartFilter.makeComplete(file);
            }

            return file;
         }
      }
   }

   private static void adjustFileFilters(JFileChooser fileChooser, SmartFileFilter[] filters) {
      if (filters.length != 0) {
         fileChooser.setAcceptAllFileFilterUsed(false);

         for (int i = 0; i < filters.length; i++) {
            fileChooser.addChoosableFileFilter(filters[i]);
         }

         fileChooser.setFileFilter(filters[0]);
      }
   }
}
