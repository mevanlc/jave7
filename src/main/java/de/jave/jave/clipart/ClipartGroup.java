package de.jave.jave.clipart;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;

public class ClipartGroup {
   private final String name;
   private final List<Clipart> cliparts = new ArrayList<>();

   public ClipartGroup(String name, Clipart[] cliparts) {
      this.name = name;
      this.cliparts.addAll(Arrays.asList(cliparts));
      this.sort();
   }

   public ClipartGroup(String name) {
      this(name, new Clipart[0]);
   }

   private void sort() {
      final Collator collator = Collator.getInstance();
      Collections.sort(this.cliparts, new Comparator<Clipart>() {
         public int compare(Clipart clipart1, Clipart clipart2) {
            return collator.compare(clipart1.getName(), clipart2.getName());
         }
      });
   }

   public void add(Clipart clipart) {
      this.cliparts.add(clipart);
      this.sort();
   }

   public String getName() {
      return this.name;
   }

   public void performSave(Component parentComponent, File baseDir) {
      BufferedWriter writer = null;

      try {
         File file = new File(baseDir, this.name + ".jcf");
         writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8);

         for (int i = 0; i < this.cliparts.size(); i++) {
            Clipart clipart = this.cliparts.get(i);
            writer.write(clipart.getName());
            writer.newLine();
            writer.write(clipart.getAuthor());
            writer.newLine();
            writer.write(clipart.getCode());
            writer.newLine();
            writer.newLine();
         }
      } catch (Exception var10) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("Error saving Clipart-File", var10));
      } finally {
         IOUtilities.close(writer);
      }
   }

   public void print() {
      System.out.println(this.toString());
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer("ClipartGroup '" + this.name + "':\n");

      for (int i = 0; i < this.cliparts.size(); i++) {
         sb.append(" ").append(i).append(": ").append(this.cliparts.get(i).toString()).append("\n");
      }

      return sb.toString();
   }

   public int getClipartCount() {
      return this.cliparts.size();
   }

   public Clipart getClipart(int index) {
      return this.cliparts.get(index);
   }

   public static ClipartGroup load(File file) throws Exception {
      String name = file.getName();
      if (name.endsWith(".jcf")) {
         name = name.substring(0, name.length() - 4);
      }

      BufferedReader br = null;

      ClipartGroup var14;
      try {
         br = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8);
         List<Clipart> clipartList = new ArrayList<>();

         String s1;
         while ((s1 = br.readLine()) != null) {
            String s2 = br.readLine();
            if (s2 == null) {
               throw new Exception("Unexpected End of Clipart File '" + file.getAbsolutePath() + ".jcf'.");
            }

            String s3 = br.readLine();
            if (s3 == null) {
               throw new Exception("Unexpected End of Clipart File '" + file.getAbsolutePath() + ".jcf'.");
            }

            clipartList.add(new Clipart(s1, s3, s2));
            String var13 = br.readLine();
         }

         Clipart[] cliparts = clipartList.toArray(new Clipart[0]);
         var14 = new ClipartGroup(name, cliparts);
      } catch (IOException var10) {
         throw new Exception("Error loading clipart file '" + file.getAbsolutePath() + "'", var10);
      } finally {
         IOUtilities.close(br);
      }

      return var14;
   }
}
