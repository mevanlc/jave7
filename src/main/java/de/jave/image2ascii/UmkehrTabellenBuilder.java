package de.jave.image2ascii;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class UmkehrTabellenBuilder {
   protected static final String CONFIGURATION_FOLDER_NAME = "./greyscaletables/3_hand_classified_tables/";
   protected static final String OUTPUT_FOLDER_NAME = "./greyscaletables/4_jgt_tables/";

   public static void main(String[] args) throws Exception {
      parseFolder();
   }

   public static void parseFolder() throws Exception {
      File file = new File("./greyscaletables/3_hand_classified_tables/");
      if (!file.isDirectory()) {
         throw new Exception("There is no folder '" + file + "' containing cofiguration files.");
      } else {
         String[] fileNames = file.list();
         int counter = 0;

         for (int i = 0; i < fileNames.length; i++) {
            if (fileNames[i].toLowerCase().endsWith(".cfg")) {
               counter++;
            }
         }

         if (counter == 0) {
            throw new Exception("There are no configuration files in folder '" + file + "'.");
         } else {
            for (int ix = 0; ix < fileNames.length; ix++) {
               if (fileNames[ix].toLowerCase().endsWith(".cfg")) {
                  buildFile(file, fileNames[ix]);
               }
            }
         }
      }
   }

   protected static void buildFile(File folder, String fileName) throws Exception {
      File fileIn = new File(folder, fileName);
      File fileOut = new File(new File("./greyscaletables/4_jgt_tables/"), fileName.substring(0, fileName.length() - 4) + ".jgt");

      try {
         int[] greyscales = new int[95];
         int[][] g4 = new int[95][4];
         char[] g4ch = new char[95];
         boolean[] ignores = new boolean[95];
         boolean[] i4 = new boolean[95];
         BufferedReader br = new BufferedReader(new FileReader(fileIn));
         BufferedWriter bw = new BufferedWriter(new FileWriter(fileOut));
         String line = null;

         for (int i = 0; i < 95; i++) {
            line = br.readLine();
            if (line == null) {
               throw new Exception("Configuration file '" + fileIn + "' is truncated.");
            }

            System.out.println(line);
            bw.write(line);
            bw.newLine();
            String value = null;
            if (line.endsWith("##")) {
               ignores[i] = true;
               i4[i] = true;
               value = line.substring(2, line.length() - 2);
            } else if (line.endsWith(" #")) {
               ignores[i] = false;
               i4[i] = true;
               value = line.substring(2, line.length() - 2);
            } else if (line.endsWith("#")) {
               ignores[i] = true;
               i4[i] = false;
               value = line.substring(2, line.length() - 1);
            } else {
               ignores[i] = false;
               i4[i] = false;
               value = line.substring(2);
            }

            try {
               int ii1 = value.indexOf(32);
               int ii2 = value.indexOf(32, ii1 + 1);
               int ii3 = value.indexOf(32, ii2 + 1);
               int ii4 = value.indexOf(32, ii3 + 1);
               greyscales[i] = Integer.parseInt(value.substring(0, ii1));
               g4[i][0] = Integer.parseInt(value.substring(ii1 + 1, ii2));
               g4[i][1] = Integer.parseInt(value.substring(ii2 + 1, ii3));
               g4[i][2] = Integer.parseInt(value.substring(ii3 + 1, ii4));
               g4[i][3] = Integer.parseInt(value.substring(ii4 + 1));
               g4ch[i] = (char)(32 + i);
            } catch (Exception var18) {
               throw new Exception("Syntax error in configuration file '" + fileIn + "': " + line);
            }
         }

         int size = g4ch.length;
         boolean done = false;

         while (!done) {
            done = true;

            for (int i = 0; i < size - 1; i++) {
               if (g4[i][0] > g4[i + 1][0]
                  || g4[i][0] == g4[i + 1][0] && g4[i][1] > g4[i + 1][1]
                  || g4[i][0] == g4[i + 1][0] && g4[i][1] == g4[i + 1][1] && g4[i][2] > g4[i + 1][2]
                  || g4[i][0] == g4[i + 1][0] && g4[i][1] == g4[i + 1][1] && g4[i][2] == g4[i + 1][2] && g4[i][3] > g4[i + 1][3]) {
                  int[] t = g4[i];
                  g4[i] = g4[i + 1];
                  g4[i + 1] = t;
                  char ct = g4ch[i];
                  g4ch[i] = g4ch[i + 1];
                  g4ch[i + 1] = ct;
                  boolean bt = i4[i];
                  i4[i] = i4[i + 1];
                  i4[i + 1] = bt;
                  done = false;
               }
            }
         }

         for (int ix = 0; ix < size; ix++) {
            if (!i4[ix]) {
               System.out.println(g4[ix][0] + " " + g4[ix][1] + " " + g4[ix][2] + " " + g4[ix][3] + " " + g4ch[ix]);
               bw.write(g4[ix][0] + " " + g4[ix][1] + " " + g4[ix][2] + " " + g4[ix][3] + " " + g4ch[ix]);
               bw.newLine();
            }
         }

         br.close();
         bw.close();
      } catch (Exception var19) {
         var19.printStackTrace();
         throw new Exception("Error loading configuration file '" + fileIn + "'.");
      }
   }
}
