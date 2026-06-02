package de.jave.jave.actions.export;

import de.jave.gui.io.ExtensionFileFilter;
import de.jave.gui.io.SmartFileFilter;
import de.jave.lib.CharacterPlate;
import de.jave.lib.net.HtmlUtilities;
import java.util.StringTokenizer;
import net.dizzy.commons.core.util.Ensure;

public class ResultConverter {
   private final ExtensionFileFilter fileFilter;
   private final String name;
   private final String blockStart;
   private final String lineStart;
   private final String blockEnd;
   private final boolean html;
   private final String id;

   public ResultConverter(String id, String name, ExtensionFileFilter fileFilter, String blockStart, String lineStart, String blockEnd, boolean html) {
      Ensure.ensureArgumentNotNull(id);
      Ensure.ensureArgumentNotNull(name);
      Ensure.ensureArgumentNotNull(fileFilter);
      this.id = id;
      this.name = name;
      this.fileFilter = fileFilter;
      this.lineStart = lineStart;
      this.blockStart = blockStart;
      this.blockEnd = blockEnd;
      this.html = html;
   }

   public String getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public String convert(CharacterPlate plate) {
      StringBuilder result = new StringBuilder();
      result.append(this.blockStart);
      String text = plate.toString();
      StringTokenizer st = new StringTokenizer(text, "\n\r", true);
      boolean flag = true;

      while (st.hasMoreTokens()) {
         String t = st.nextToken();
         if (flag) {
            result.append(this.lineStart);
         }

         if (flag && !t.equals("\n") && !t.equals("\r")) {
            flag = false;
         } else {
            flag = true;
         }

         if (this.html) {
            t = HtmlUtilities.encode(t);
         }

         result.append(t);
      }

      result.append(this.blockEnd);
      return result.toString();
   }

   public SmartFileFilter getFileFilter() {
      return this.fileFilter;
   }
}
