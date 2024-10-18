package de.jave.image2ascii.dialog;

import de.jave.gui.io.ImageIOUtilities;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class BatchSourceImagesFinder {
   public static File[] findSourceFiles(File directory) {
      File[] files = directory.listFiles();
      Map<Integer, File> filesByNumber = new HashMap<>();

      for (int i = 0; i < files.length; i++) {
         File file = files[i];
         if (ImageIOUtilities.hasSupportedImageFileExtension(file)) {
            Integer number = getNumber(file.getName());
            if (number != null) {
               filesByNumber.put(number, file);
            }
         }
      }

      File[] sortedFiles = new File[filesByNumber.size()];
      int index = 0;
      int indexCounter = 0;

      while (index < sortedFiles.length) {
         Integer key = indexCounter++;
         if (filesByNumber.containsKey(key)) {
            sortedFiles[index++] = filesByNumber.get(key);
         }
      }

      return sortedFiles;
   }

   private static Integer getNumber(String name) {
      int i1 = 0;

      while (i1 < name.length() && (name.charAt(i1) < '0' || name.charAt(i1) > '9')) {
         i1++;
      }

      int i2 = i1 + 1;

      while (i2 < name.length() && name.charAt(i2) >= '0' && name.charAt(i2) <= '9') {
         i2++;
      }

      return i1 >= name.length() ? null : Integer.parseInt(name.substring(i1, i2));
   }
}
