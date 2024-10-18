package net.disy.commons.core.io;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.ITransformer;
import net.disy.commons.core.util.StringUtilities;

public class FileUtilities {
   private static IFileSystem fileSystem = new JavaFileSystem();

   protected FileUtilities() {
      throw new UnreachableCodeReachedException();
   }

   public static void deleteFileOrDirectory(File file) throws IOException {
      if (file.exists()) {
         if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (File file2 : files) {
               deleteFileOrDirectory(file2);
            }
         }

         if (!file.delete()) {
            throw new IOException("delete failed for file '" + file.getAbsolutePath() + "'");
         }
      }
   }

   public static String getExtension(File file) {
      return getExtension(file.getName());
   }

   public static String getExtension(String filename) {
      int lastDot = filename.lastIndexOf(46);
      return lastDot >= 0 ? filename.substring(lastDot + 1) : "";
   }

   public static File addExtension(File file, String extension) {
      return new File(addExtension(file.getPath(), extension));
   }

   public static File changeExtensionTo(File file, String extension) {
      return addExtension(getWithoutExtension(file), extension);
   }

   public static String addExtension(String fileName, String fileNameExtension) {
      String extension = fileNameExtension;
      if (!fileNameExtension.startsWith(".")) {
         extension = "." + fileNameExtension;
      }

      return !StringUtilities.endsWithIgnoreCase(fileName, extension) ? fileName + extension : fileName;
   }

   public static File createTempFile(String prefix, String postfix, File directory) throws IOException {
      JavaFile parentDirectory = directory == null ? null : new JavaFile(directory);
      return createTempFile(prefix, postfix, parentDirectory).getFile();
   }

   public static IFile createTempFile(String prefix, String postfix, IFile parentDirectory) throws IOException {
      IFile directory = parentDirectory;
      if (parentDirectory == null) {
         directory = fileSystem.getDefaultTempDir();
      }

      if (!directory.exists()) {
         boolean directoriesCreated = directory.mkDirs();
         if (!directoriesCreated) {
            if (!directory.equals(fileSystem.getDefaultTempDir())) {
               try {
                  return createTempFile(prefix, postfix, (IFile)null);
               } catch (IOException var6) {
                  throw new IOException("Could not create full path for temporary file: " + directory + '/' + prefix + '.' + postfix);
               }
            }

            throw new IOException("Could not create temporary directory at: " + directory);
         }
      }

      try {
         return fileSystem.createTempFile(prefix, postfix, directory);
      } catch (IOException var7) {
         throw new IOException(MessageFormat.format("Could not create temp file in {0}: {1}", directory, var7.getMessage()), var7);
      }
   }

   public static void setFileSystem(IFileSystem fileSystem) {
      FileUtilities.fileSystem = fileSystem;
   }

   public static File createFileNameSuggestion(IWorkingDirectoryProvider workingDirectoryProvider, String fileName, String fileExtension) {
      Ensure.ensureArgumentNotNull(workingDirectoryProvider);
      Ensure.ensureArgumentNotNull(fileName);
      Ensure.ensureArgumentNotNull(fileExtension);
      File workingDirectory = workingDirectoryProvider.getWorkingDirectory();
      File directory = workingDirectory == null ? new File(".") : workingDirectory;
      return createNonExistingFile(directory, fileName, fileExtension);
   }

   public static File createNonExistingFile(File directory, String fileName, String fileExtension) {
      Ensure.ensureArgumentNotNull(directory);
      Ensure.ensureArgumentNotNull(fileName);
      Ensure.ensureArgumentNotNull(fileExtension);
      File file = new File(directory, fileName + fileExtension);

      for (int index = 1; file.exists(); index++) {
         file = new File(directory, fileName + index + fileExtension);
      }

      return file;
   }

   @Deprecated
   public static boolean isReadable(URL url) {
      return UrlUtilities.isAccessible(url);
   }

   public static File getTempDir() {
      return fileSystem.getDefaultTempDir().getFile();
   }

   public static void createFileParent(String fileName) throws IOException {
      createFileParent(new File(fileName));
   }

   private static void createFileParent(File file) throws IOException {
      File canonicalFile = file.getCanonicalFile();
      File parent = canonicalFile.getParentFile();
      if (!parent.exists()) {
         parent.mkdirs();
      }
   }

   public static File getWithoutExtension(File file) {
      String fileName = file.getName();
      if (!fileName.contains(".")) {
         return file;
      } else {
         File parentFile = file.getParentFile();
         return new File(parentFile, fileName.substring(0, fileName.lastIndexOf(".")));
      }
   }

   public static String getWithoutExtension(String fileName) {
      String extension = getExtension(fileName);
      return extension.isEmpty() ? fileName : fileName.substring(0, fileName.length() - extension.length() - 1);
   }

   public static boolean isReadable(File file) {
      return file != null && file.canRead();
   }

   public static URL createUrl(URL baseUrl, String fileName) {
      Ensure.ensureArgumentNotNull(baseUrl);
      Ensure.ensureArgumentNotNull(fileName);

      try {
         if (!baseUrl.getPath().endsWith("/")) {
            baseUrl = new URL(baseUrl.toString() + "/");
         }

         if (!UrlUtilities.isFileUrl(fileName) && !UrlUtilities.isHttpUrl(fileName) && !UrlUtilities.isFileUrl(baseUrl)) {
            String encodedFileName = createEncodedFileName(fileName);
            return new URL(baseUrl, encodedFileName);
         } else {
            return new URL(baseUrl, fileName);
         }
      } catch (MalformedURLException var3) {
         throw new RuntimeException(var3);
      }
   }

   private static String createEncodedFileName(String fileName) {
      if (fileName.isEmpty()) {
         return fileName;
      } else {
         String[] tokens = fileName.split("/|\\\\");
         String[] encodedTokens = ArrayUtilities.transform(tokens, String.class, new ITransformer<String, String>() {
            public String transform(String input) {
               try {
                  return URLEncoder.encode(input, "ISO-8859-1");
               } catch (UnsupportedEncodingException var3) {
                  throw new RuntimeException(var3);
               }
            }
         });
         StringBuffer buffer = new StringBuffer();
         buffer.append(encodedTokens[0]);

         for (int index = 1; index < tokens.length; index++) {
            buffer.append("/");
            buffer.append(encodedTokens[index]);
         }

         return buffer.toString();
      }
   }

   public static File toFile(URL url) {
      if (!UrlUtilities.isFileUrl(url)) {
         throw new IllegalArgumentException("URL \"" + url + "\" is no file URL");
      } else {
         String filename;
         try {
            filename = URLDecoder.decode(url.getFile().replace('/', File.separatorChar), "UTF-8");
         } catch (UnsupportedEncodingException var4) {
            throw new UnreachableCodeReachedException("(ip)", var4);
         }

         String authorization = url.getAuthority();
         if (authorization == null) {
            return new File(filename);
         } else {
            String fileSeperator = String.valueOf(File.separatorChar);
            return new File(fileSeperator + fileSeperator + authorization + filename);
         }
      }
   }

   public static URL toUrl(File file) {
      try {
         return file.toURI().toURL();
      } catch (MalformedURLException var2) {
         throw new UnreachableCodeReachedException(var2);
      }
   }
}
