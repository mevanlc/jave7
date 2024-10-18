package net.disy.commons.core.io;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;
import net.disy.commons.core.util.Ensure;

public class UriUtilities {
   public static boolean isFile(URI uri) {
      try {
         return uri != null && new File(uri) != null;
      } catch (IllegalArgumentException var2) {
         return false;
      }
   }

   public static URI changeExtensionTo(URI uri, String extension) throws URISyntaxException {
      File pathFile = FileUtilities.changeExtensionTo(new File(uri.getPath()), extension);
      String path = pathFile == null ? "" : pathFile.getAbsolutePath();
      path = path.replaceAll(Pattern.quote("\\"), "/");
      if (!path.startsWith("/")) {
         path = "/" + path;
      }

      return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment());
   }

   public static URI getWithoutExtension(URI uri) throws URISyntaxException {
      File pathFile = FileUtilities.getWithoutExtension(new File(uri.getPath()));
      String path = pathFile == null ? "" : pathFile.getAbsolutePath();
      path = path.replaceAll(Pattern.quote("\\"), "/");
      if (!path.startsWith("/")) {
         path = "/" + path;
      }

      return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), path, uri.getQuery(), uri.getFragment());
   }

   public static boolean isAccessible(URI uri) {
      Ensure.ensureArgumentNotNull(uri);

      try {
         if (isFile(uri)) {
            return FileUtilities.isReadable(new File(uri));
         } else {
            return uri.getScheme() == null ? FileUtilities.isReadable(new File(uri.getPath())) : UrlUtilities.isAccessible(uri.toURL());
         }
      } catch (Throwable var2) {
         return false;
      }
   }
}
