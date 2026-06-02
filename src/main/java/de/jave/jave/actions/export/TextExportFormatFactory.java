package de.jave.jave.actions.export;

import de.jave.gui.io.ExtensionFileFilters;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.util.Ensure;

public class TextExportFormatFactory {
   private static final ResultConverter[] CONVERTERS = new ResultConverter[]{
      new ResultConverter("plain", "plain text", ExtensionFileFilters.TXT, "", "", "", false),
      new ResultConverter("html", "HTML", ExtensionFileFilters.HTML, "<pre>", "", "</pre>", true),
      new ResultConverter("javadoccomment", "JavaDoc comment", ExtensionFileFilters.TXT, "/**\n", "* ", "\n*/", true),
      new ResultConverter("javacomment", "Java comment", ExtensionFileFilters.TXT, "/*\n", "", "\n*/", false),
      new ResultConverter("shellcomment", "Shell comment", ExtensionFileFilters.TXT, "", "# ", "", false),
      new ResultConverter("cpluspluscomment", "C++ comment", ExtensionFileFilters.TXT, "", "// ", "", false),
      new ResultConverter("ccomment", "C comment", ExtensionFileFilters.TXT, "/*\n", "", "\n*/", false),
      new ResultConverter("htmlcomment", "HTML comment", ExtensionFileFilters.HTML, "<!--\n", "", "\n-->", false),
      new ResultConverter("modula2comment", "Modula2 comment", ExtensionFileFilters.TXT, "(*\n", "", "\n*)", false),
      new ResultConverter("texcomment", "TeX comment", ExtensionFileFilters.TXT, "", "% ", "", false),
      new ResultConverter("textext", "TeX text", ExtensionFileFilters.TXT, "\\begin{verbatim}", "", "\\end{verbatim}", false)
   };
   private static ITextExportFormat[] cachedFormats;

   public static synchronized ITextExportFormat[] getExportFormats() {
      if (cachedFormats == null) {
         cachedFormats = createExportFormats();
      }

      return cachedFormats;
   }

   private static ITextExportFormat[] createExportFormats() {
      List<ITextExportFormat> list = new ArrayList<>();
      list.add(new GifTextExportFormat());

      for (ResultConverter converter : CONVERTERS) {
         list.add(new ResultConverterTextExportFormat(converter));
      }

      return list.toArray(new ITextExportFormat[0]);
   }

   public static ITextExportFormat getExportFormatById(String id) {
      Ensure.ensureArgumentNotNull(id);
      ITextExportFormat[] formats = getExportFormats();

      for (ITextExportFormat format : formats) {
         if (id.equals(format.getId())) {
            return format;
         }
      }

      return null;
   }
}
