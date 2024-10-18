package net.disy.commons.swing.filechooser.filefilter;

import java.io.File;
import java.util.Arrays;
import javax.swing.filechooser.FileFilter;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.ITransformer;

public class ExtensionFileFilter extends FileFilter {
   public static final String CSV_EXTENSION = "csv";
   public static final String DBF_EXTENSION = "dbf";
   public static final String EXCEL_EXTENSION = "xls";
   public static final String GIF_EXTENSION = "gif";
   public static final String GROOVY_EXTENSION = "groovy";
   public static final String HTM_EXTENSION = "htm";
   public static final String HTML_EXTENSION = "html";
   public static final String ICAT_EXTENSION = "jgw";
   public static final String JPEG_EXTENSION = "jpeg";
   public static final String JPG_EXTENSION = "jpg";
   public static final String PDF_EXTENSION = "pdf";
   public static final String PNG_EXTENSION = "png";
   public static final String RPT_EXTENSION = "rpt";
   public static final String RTF_EXTENSION = "rtf";
   public static final String TIF_EXTENSION = "tif";
   public static final String TIFF_EXTENSION = "tiff";
   public static final String TXT_EXTENSION = "txt";
   public static final String XML_EXTENSION = "xml";
   public static final String ZIP_EXTENSION = "zip";
   public static final ExtensionFileFilter PDF_FILE_FILTER = new ExtensionFileFilter("pdf", "Portable Document Format");
   public static final ExtensionFileFilter RTF_FILE_FILTER = new ExtensionFileFilter("rtf", "Rich Text Format");
   public static final ExtensionFileFilter CSV_FILE_FILTER = new ExtensionFileFilter("csv", "Comma Separated Values");
   public static final ExtensionFileFilter TXT_FILE_FILTER = new ExtensionFileFilter("txt", "Text-Dateien");
   public static final ExtensionFileFilter EXCEL_FILE_FILTER = new ExtensionFileFilter("xls", "Excel-Dateien");
   public static final ExtensionFileFilter HTML_FILE_FILTER = new ExtensionFileFilter(new String[]{"html", "htm"}, "HTML-Dateien");
   public static final ExtensionFileFilter GIF_FILE_FILTER = new ExtensionFileFilter(new String[]{"gif"}, "GIF-Bilder");
   public static final ExtensionFileFilter GROOVY_FILE_FILTER = new ExtensionFileFilter(new String[]{"groovy"}, "Groovy-Skripte & -Klassen");
   public static final ExtensionFileFilter RPT_FILE_FILTER = new ExtensionFileFilter(new String[]{"rpt"}, "Reportdatei");
   public static final ExtensionFileFilter JPEG_FILE_FILTER = new ExtensionFileFilter(
      new String[]{"jpeg", "jpg"}, "JPEG (Joint Photographic Experts Group)-Datei"
   );
   public static final ExtensionFileFilter TIFF_FILE_FILTER = new ExtensionFileFilter(new String[]{"tiff", "tif"}, "TIFF (Tagged-Image File Format)-Dateien");
   public static final ExtensionFileFilter ICAT_FILE_FILTER = new ExtensionFileFilter(new String[]{"jgw"}, "Bildkatalog-Dateien");
   public static final ExtensionFileFilter DBF_FILE_FILTER = new ExtensionFileFilter(new String[]{"dbf"}, "dBASE-Dateien");
   public static final ExtensionFileFilter PNG_FILE_FILTER = new ExtensionFileFilter(new String[]{"png"}, "Portable Network Graphics");
   public static final ExtensionFileFilter IMAGE_FILE_FILTER = createComposite(
      "Bild-Dateien", GIF_FILE_FILTER, JPEG_FILE_FILTER, PNG_FILE_FILTER, TIFF_FILE_FILTER
   );
   public static final ExtensionFileFilter XML_FILE_FILTER = new ExtensionFileFilter("xml", "XML Datei");
   public static final ExtensionFileFilter ZIP_FILE_FILTER = new ExtensionFileFilter("zip", "ZIP Archiv");
   private final String name;
   private final String[] extensions;
   public static final FileFilter[] ALL_IMAGE_FILE_FILTERS = new FileFilter[]{
      IMAGE_FILE_FILTER, GIF_FILE_FILTER, JPEG_FILE_FILTER, PNG_FILE_FILTER, TIFF_FILE_FILTER
   };

   public static ExtensionFileFilter createComposite(String name, ExtensionFileFilter... filters) {
      String[][] extensionsArray = ArrayUtilities.transform(filters, String[].class, new ITransformer<ExtensionFileFilter, String[]>() {
         public String[] transform(ExtensionFileFilter input) {
            return input.extensions;
         }
      });
      String[] allExtensions = ArrayUtilities.concat(String.class, extensionsArray);
      return new ExtensionFileFilter(allExtensions, name);
   }

   public ExtensionFileFilter(String extension) {
      this(extension, extension.toUpperCase());
   }

   public ExtensionFileFilter(String extension, String name) {
      this(new String[]{extension}, name);
   }

   public ExtensionFileFilter(String[] extensions, String name) {
      this.extensions = extensions;
      this.name = name;
   }

   @Override
   public String getDescription() {
      StringBuilder description = new StringBuilder(this.name + " (*." + this.extensions[0]);

      for (int i = 1; i < this.extensions.length; i++) {
         description.append(", *.").append(this.extensions[i]);
      }

      return description + ")";
   }

   @Override
   public boolean accept(File file) {
      return file.isDirectory() || this.conformsToExtensions(file.getName());
   }

   public boolean conformsToExtensions(String filenname) {
      for (String extension : this.extensions) {
         if (filenname.toLowerCase().endsWith("." + extension.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   public String getDefaultExtension() {
      return this.extensions[0];
   }

   public File augmentExtension(File file) {
      return file.getName().indexOf(46) > -1 ? file : new File(file.getPath() + '.' + this.extensions[0]);
   }

   public String augmentExtension(String fileName) {
      return fileName.indexOf(46) > -1 ? fileName : fileName + '.' + this.extensions[0];
   }

   @Override
   public boolean equals(Object obj) {
      return obj instanceof ExtensionFileFilter ? this.equals((ExtensionFileFilter)obj) : super.equals(obj);
   }

   public boolean equals(ExtensionFileFilter obj) {
      if (obj.extensions.length != this.extensions.length) {
         return false;
      } else {
         for (String extension : obj.extensions) {
            final String lowerCasedExtension = extension.toLowerCase();
            if (!ArrayUtilities.contains(this.extensions, new IPredicate<String>() {
               public boolean evaluate(String value) {
                  return lowerCasedExtension.equals(value.toLowerCase());
               }
            })) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public int hashCode() {
      return Arrays.hashCode(this.extensions);
   }
}
