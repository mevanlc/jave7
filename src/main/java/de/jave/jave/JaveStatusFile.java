package de.jave.jave;

import de.jave.lib.Toolbox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class JaveStatusFile {
   private static final File FILE = new File(JaveGlobalRessources.TMP_FOLDER, "status.log");
   public static final File logDir = JaveGlobalRessources.TMP_FOLDER;

   public static synchronized boolean exists() {
      return FILE.exists();
   }

   public static synchronized String[] load() {
      BufferedReader reader = null;

      Object count;
      try {
         reader = new BufferedReader(new FileReader(FILE));
         String date = reader.readLine();
         int countx = Integer.parseInt(reader.readLine());
         String[] statusData = new String[countx * 2 + 2];
         statusData[0] = date;
         statusData[1] = String.valueOf(countx);

         for (int i = 0; i < countx * 2; i++) {
            statusData[i + 2] = reader.readLine();
         }

         return statusData;
      } catch (Exception var8) {
         count = null;
      } finally {
         IOUtilities.close(reader);
      }

      return (String[])count;
   }

   public static void deleteAllLogFiles() {
      if (logDir.exists() && logDir.canRead() && logDir.isDirectory()) {
         File[] list = logDir.listFiles();

         for (int i = 0; i < list.length; i++) {
            if (list[i].getName().endsWith(".jlog")) {
               list[i].delete();
            }
         }
      }
   }

   public static synchronized void saveLog(DocumentManager documentManager) {
      String[] s = documentManager.getDocumentStatusList();
      File newFile = new File(FILE.getAbsolutePath() + "1");
      BufferedWriter writer = null;

      try {
         if (!logDir.exists()) {
            logDir.mkdirs();
         }

         writer = new BufferedWriter(new FileWriter(newFile));
         writer.write(Toolbox.getDateString());
         writer.newLine();
         writer.write(String.valueOf(s.length / 2));
         writer.newLine();

         for (int i = 0; i < s.length; i++) {
            writer.write(s[i]);
            writer.newLine();
         }
      } catch (IOException var13) {
         System.err.println(var13);
         var13.printStackTrace();
      } finally {
         if (writer != null) {
            try {
               writer.close();
               if (FILE.exists()) {
                  FILE.delete();
               }

               newFile.renameTo(FILE);
            } catch (IOException var12) {
            }
         }
      }
   }

   public static synchronized boolean delete() {
      return FILE.exists() ? FILE.delete() : false;
   }
}
