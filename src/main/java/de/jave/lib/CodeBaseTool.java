package de.jave.lib;

import java.io.File;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.dizzy.commons.core.util.Ensure;

public class CodeBaseTool {
   private CodeBaseTool() {
   }

   public static final File getCodeBase(Class knownClass) {
      Ensure.ensureArgumentNotNull(knownClass);
      String knownClassName = knownClass.getName().replace('.', File.separatorChar);
      String classpath = System.getProperty("java.class.path");
      Enumeration enumeration = new StringTokenizer(classpath, File.pathSeparator);

      while (enumeration.hasMoreElements()) {
         String path = (String)enumeration.nextElement();
         String filename = path + File.separatorChar + knownClassName + ".class";
         File file = new File(filename);
         if (file.exists()) {
            return new File(path).getAbsoluteFile();
         }

         if (path.toLowerCase().endsWith(".jar")) {
            filename = knownClassName + ".class";

            try (ZipFile zip = new ZipFile(path)) {

               Enumeration<? extends ZipEntry> zipEntries = zip.entries();

               while (zipEntries.hasMoreElements()) {
                  String zipEntryName = zipEntries.nextElement().toString();
                  zipEntryName = zipEntryName.replace('/', File.separatorChar);
                  if (filename.equals(zipEntryName)) {
                     int i1 = path.lastIndexOf(File.separatorChar);
                     String result = path.substring(0, i1);
                     File f = new File(result);
                     result = f.getAbsolutePath();
                     return f.getAbsoluteFile();
                  }
               }
            } catch (Exception var13) {
            }
         }
      }

      File f = new File(".");
      return f.getAbsoluteFile();
   }
}
