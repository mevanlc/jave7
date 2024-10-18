package net.disy.commons.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.StringUtilities;

public class UrlUtilities {
   public static final String ENCODING_ISO_8859_1 = "ISO-8859-1";
   public static final String ENCODING_UTF_8 = "UTF-8";
   public static final String PROTOCOL_HTTP = "http";
   public static final String PROTOCOL_FILE = "file";

   public static boolean isAccessible(URL url) {
      InputStream openStream = null;

      boolean throwable;
      try {
         if (!UriUtilities.isFile(url.toURI())) {
            openStream = url.openStream();
            return true;
         }

         throwable = FileUtilities.isReadable(new File(url.toURI()));
      } catch (Throwable var7) {
         return false;
      } finally {
         IOUtilities.close(openStream);
      }

      return throwable;
   }

   public static boolean isFile(URL url) {
      try {
         return UriUtilities.isFile(url.toURI());
      } catch (Throwable var2) {
         return false;
      }
   }

   private UrlUtilities() {
      throw new UnreachableCodeReachedException();
   }

   public static void checkUrlExists(String urlString) throws IOException {
      InputStream stream = null;

      try {
         stream = new URL(urlString).openStream();
      } finally {
         IOUtilities.close(stream);
      }
   }

   public static boolean isFileUrl(URL url) {
      return url.getProtocol().equals("file");
   }

   public static boolean isHttpUrl(URL url) {
      return url.getProtocol().equals("http");
   }

   public static boolean isFileOrHttpUrl(URL url) {
      return isFileUrl(url) || isHttpUrl(url);
   }

   public static boolean representsFileUrl(String urlString) {
      return urlString.startsWith("file:");
   }

   public static boolean representsHttpUrl(String urlString) {
      return urlString.startsWith("http:");
   }

   public static String removeExtension(String externalForm) {
      if (!hasExtension(externalForm)) {
         return externalForm;
      } else {
         int pointIndex = externalForm.lastIndexOf(46);
         return externalForm.substring(0, pointIndex);
      }
   }

   public static boolean hasExtension(String externalForm) {
      int pointIndex = externalForm.lastIndexOf(46);
      int slashIndex = externalForm.lastIndexOf(47);
      return pointIndex > 0 && pointIndex > slashIndex;
   }

   public static URL getParent(URL url) throws MalformedURLException {
      String path = url.toExternalForm();
      return new URL(path.substring(0, path.lastIndexOf(47) + 1));
   }

   public static boolean isDirectory(URL url) {
      return url.getPath().endsWith("/");
   }

   public static String getFileName(URL url) {
      String path = url.getPath();
      if (path.endsWith("/")) {
         path = path.substring(0, path.length() - 1);
      }

      return path.substring(path.lastIndexOf(47) + 1);
   }

   public static String getContentAsString(URL url, String encoding) throws IOException {
      Ensure.ensureArgumentNotNull(url);
      InputStream inputStream = null;

      String var4;
      try {
         URLConnection connection = url.openConnection();
         inputStream = connection.getInputStream();
         var4 = IOUtilities.toString(inputStream, encoding);
      } finally {
         IOUtilities.close(inputStream);
      }

      return var4;
   }

   public static String getPathWithoutProtocol(URL url) {
      String urlString = url.toExternalForm();
      if (isHttpUrl(url)) {
         return urlString.substring(7);
      } else if (isFileUrl(url)) {
         return FileUtilities.toFile(url).getAbsolutePath();
      } else {
         throw new UnsupportedOperationException("Protocol not supported: " + url.getProtocol());
      }
   }

   public static boolean isHttpUrl(String string) {
      return string.startsWith("http");
   }

   public static boolean isFileUrl(String string) {
      return string.startsWith("file");
   }

   public static boolean containsAuthentication(String url) {
      if (isHttpUrl(url)) {
         String[] urlParts = url.split("@");
         if (urlParts.length == 2) {
            String authenticationString = urlParts[0].substring(7);
            String[] authenticationParts = authenticationString.split(":");
            return authenticationParts.length == 2
               && !StringUtilities.isNullOrEmpty(authenticationParts[0])
               && !StringUtilities.isNullOrEmpty(authenticationParts[1]);
         }
      }

      return false;
   }

   private static String[] getAuthenticationString(String url) {
      String[] urlParts = url.split("@");
      String authenticationString = urlParts[0].substring(7);
      return authenticationString.split(":");
   }

   public static String getUserName(String url) {
      if (containsAuthentication(url)) {
         String[] authenticationParts = getAuthenticationString(url);
         return authenticationParts[0];
      } else {
         return null;
      }
   }

   public static String getPassword(String url) {
      if (containsAuthentication(url)) {
         String[] authenticationParts = getAuthenticationString(url);
         return authenticationParts[1];
      } else {
         return null;
      }
   }

   public static boolean exists(URL configUrl) throws IOException {
      InputStream openStream = null;

      boolean var3;
      try {
         openStream = configUrl.openStream();
         return true;
      } catch (FileNotFoundException var8) {
         return false;
      } catch (UnknownHostException var9) {
         var3 = false;
      } finally {
         IOUtilities.close(openStream);
      }

      return var3;
   }

   public static String stripJSessionId(String url) {
      return url.replaceAll(";jsessionid=[^?]*", "");
   }
}
