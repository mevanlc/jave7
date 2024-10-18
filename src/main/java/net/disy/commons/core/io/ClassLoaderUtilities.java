package net.disy.commons.core.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.ITransformer;

public class ClassLoaderUtilities {
   private static Map<URI, Manifest> manifests = new HashMap<>();

   public static void addToClassPath(ClassLoader classLoader, String name) throws IOException {
      addToClassPath(classLoader, new File(name));
   }

   public static void addToClassPath(ClassLoader classLoader, File path) throws IOException {
      if (path.isDirectory()) {
         File[] files = listJarFiles(path);
         if (files.length != 0) {
            for (File file : files) {
               addToClassPath(classLoader, file);
            }

            return;
         }
      }

      addToClassPath(classLoader, path.toURI().toURL());
   }

   private static File[] listJarFiles(File path) {
      return path.listFiles(new FileFilter() {
         @Override
         public boolean accept(File file) {
            if (file.isDirectory()) {
               return false;
            } else {
               String extension = FileUtilities.getExtension(file);
               return "jar".equalsIgnoreCase(extension) || "zip".equalsIgnoreCase(extension);
            }
         }
      });
   }

   public static void addToClassPath(ClassLoader classLoader, URL url) throws IOException {
      if (!(classLoader instanceof URLClassLoader)) {
         throw new IOException("Error, could not add URL to system classloader");
      } else {
         URLClassLoader sysloader = (URLClassLoader)classLoader;
         Class sysclass = URLClassLoader.class;

         try {
            Class[] parameters = new Class[]{URL.class};
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, url);
         } catch (Throwable var6) {
            throw new IOException("Error, could not add URL to system classloader", var6);
         }
      }
   }

   public static String getClassPath(ClassLoader classLoader) {
      URI[] classPathList = getClassPathUris(classLoader);
      StringBuilder classPath = new StringBuilder();

      for (URI url : classPathList) {
         classPath.append(" ").append(url.toString());
      }

      return classPath.toString();
   }

   public static Manifest getManifest(URI uri) {
      if (uri == null) {
         return null;
      } else {
         Manifest manifest;
         if ((manifest = manifests.get(uri)) != null) {
            return manifest;
         } else {
            InputStream inputStream = null;

            Object var4;
            try {
               inputStream = uri.toURL().openStream();
               JarInputStream jarInputStream = new JarInputStream(inputStream);
               if ((manifest = jarInputStream.getManifest()) != null) {
                  manifests.put(uri, manifest);
               }

               return manifest;
            } catch (IOException var8) {
               var4 = null;
            } finally {
               IOUtilities.close(inputStream);
            }

            return (Manifest)var4;
         }
      }
   }

   public static URI[] getLibraries(Manifest manifest, String parent) {
      String classPath = getClassPath(manifest);
      if (classPath == null) {
         return new URI[0];
      } else {
         List<URI> urls = new ArrayList<>();
         StringTokenizer stringTokenizer = new StringTokenizer(classPath, " ");

         while (stringTokenizer.hasMoreTokens()) {
            String string = stringTokenizer.nextToken();

            try {
               URI uri = new URI(string);
               if (!uri.isAbsolute()) {
                  try {
                     File file = new File(new File(parent == null ? System.getProperty("user.dir") : parent), string);
                     urls.add(file.getCanonicalFile().toURI());
                  } catch (IOException var9) {
                     urls.add(new URI(string));
                  }
               } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                  try {
                     urls.add(new File(uri).getCanonicalFile().toURI());
                  } catch (IOException var8) {
                     urls.add(new URI(string));
                  }
               } else {
                  urls.add(uri);
               }
            } catch (URISyntaxException var10) {
            }
         }

         return urls.toArray(new URI[0]);
      }
   }

   private static String getClassPath(Manifest manifest) {
      if (manifest == null) {
         return null;
      } else {
         Attributes attributes = manifest.getMainAttributes();
         String classPath = attributes.getValue("Class-Path");
         return classPath == null ? null : classPath;
      }
   }

   public static URI[] getClassPathUris(ClassLoader classLoader) {
      if (!(classLoader instanceof URLClassLoader)) {
         return new URI[0];
      } else {
         URLClassLoader sysloader = (URLClassLoader)classLoader;
         return ArrayUtilities.transform(sysloader.getURLs(), URI.class, new ITransformer<URL, URI>() {
            public URI transform(URL input) {
               try {
                  return input.toURI();
               } catch (URISyntaxException var3) {
                  return null;
               }
            }
         });
      }
   }

   public static URI[] getLibraries(ClassLoader classLoader) {
      URI[] classPathUrls = getClassPathUris(classLoader);
      if (classPathUrls.length == 0) {
         return classPathUrls;
      } else {
         List<URI> libraryList = new ArrayList<>();

         for (URI url : classPathUrls) {
            String string = url.toString();
            int length = string.length();
            if (string.substring(length - 4 < 0 ? 0 : length - 4, length).equalsIgnoreCase(".jar")) {
               libraryList.add(url);
            }
         }

         List<URI> subLibraryList = new ArrayList<>();

         for (URI uri : libraryList) {
            Manifest manifest = getManifest(uri);
            if (manifest != null) {
               URI[] classPaths = getLibraries(manifest, getParent(uri));

               for (URI classPath : classPaths) {
                  if (!libraryList.contains(classPath)
                     && (!classPath.isAbsolute() || !"file".equalsIgnoreCase(classPath.getScheme()) || new File(classPath).exists())) {
                     subLibraryList.add(classPath);
                  }
               }
            }
         }

         libraryList.addAll(subLibraryList);
         return libraryList.toArray(new URI[0]);
      }
   }

   public static String getParent(URI uri) {
      return UriUtilities.isFile(uri) ? new File(uri).getParent() : null;
   }
}
