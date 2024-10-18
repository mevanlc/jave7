package de.jave.lib.net;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class HtmlUtilities {
   private static Map<String, String> entityTableDecode;
   private static final Map<Character, String> entityTableEncode = new HashMap<Character, String>() {
      {
         this.put('-', "-");
         this.put('\n', "&#13;");
         this.put(' ', " ");
         this.put('Ü', "&Uuml;");
         this.put('Ä', "&Auml;");
         this.put('Ö', "&Ouml;");
         this.put('Ë', "&Euml;");
         this.put('Ç', "&Ccedil;");
         this.put('Æ', "&AElig;");
         this.put('Å', "&Aring;");
         this.put('Ø', "&Oslash;");
         this.put('\'', "&apos;");
         this.put('ü', "&uuml;");
         this.put('ä', "&auml;");
         this.put('ö', "&ouml;");
         this.put('ë', "&euml;");
         this.put('"', "&#34;");
         this.put('ß', "&szlig;");
         this.put('ç', "&ccedil;");
         this.put('å', "&aring;");
         this.put('ø', "&oslash;");
         this.put('à', "&agrave;");
         this.put('è', "&egrave;");
         this.put('ì', "&igrave;");
         this.put('ò', "&ograve;");
         this.put('ù', "&ugrave;");
         this.put('&', "&amp;");
         this.put('>', "&gt;");
         this.put('<', "&lt;");
         this.put('¢', "&cent;");
         this.put('£', "&pound;");
         this.put('«', "&laquo;");
         this.put('»', "&raquo;");
         this.put('á', "&aacute;");
         this.put('ú', "&uacute;");
         this.put('ó', "&oacute;");
         this.put('é', "&eacute;");
         this.put('í', "&iacute;");
         this.put('ñ', "&ntilde;");
         this.put('§', "&sect;");
         this.put('î', "&icirc;");
         this.put('ô', "&ocirc;");
         this.put('â', "&acirc;");
         this.put('û', "&ucirc;");
         this.put('ê', "&ecirc;");
         this.put('æ', "&aelig;");
         this.put('¡', "&iexcl;");
         this.put('\u007f', "&nbsp;");
         this.put('\u009f', "&#159;");
         this.put('\u009b', "&#155;");
         this.put('\u009e', "&#158;");
         this.put('\u0092', "&#146;");
         this.put('\u0084', "&#132;");
      }

      private void put(char key, String value) {
         Character keyValue = key;
         if (this.containsKey(keyValue)) {
            throw new RuntimeException("Key " + key + " already defined as '" + this.get(keyValue) + "'");
         } else {
            this.put(keyValue, value);
         }
      }
   };
   private static final String[] ENTITIES = new String[]{
      " ",
      " ",
      "-",
      "-",
      "`",
      "`",
      " ",
      " ",
      "&#013;",
      "\n",
      "&#32;",
      " ",
      "&Uuml;",
      "Ü",
      "&Auml;",
      "Ä",
      "&Ouml;",
      "Ö",
      "&Euml;",
      "Ë",
      "&Ccedil;",
      "Ç",
      "&AElig;",
      "Æ",
      "&Aring;",
      "Å",
      "&Oslash;",
      "Ø",
      "&apos;",
      "'",
      "&uuml;",
      "ü",
      "&auml;",
      "ä",
      "&ouml;",
      "ö",
      "&euml;",
      "ë",
      "&ccedil;",
      "ç",
      "&aring;",
      "å",
      "&oslash;",
      "ø",
      "&grave;",
      "`",
      "&agrave;",
      "à",
      "&egrave;",
      "è",
      "&igrave;",
      "ì",
      "&ograve;",
      "ò",
      "&ugrave;",
      "ù",
      "&amp;",
      "&",
      "&#34;",
      "\"",
      "&szlig;",
      "ß",
      "&nbsp;",
      " ",
      "&gt;",
      ">",
      "&lt;",
      "<",
      "&copy;",
      "(C)",
      "&cent;",
      "¢",
      "&pound;",
      "£",
      "&laquo;",
      "«",
      "&raquo;",
      "»",
      "&reg;",
      "(R)",
      "&middot;",
      " - ",
      "&times;",
      " x ",
      "&acute;",
      "'",
      "&aacute;",
      "á",
      "&uacute;",
      "ú",
      "&oacute;",
      "ó",
      "&eacute;",
      "é",
      "&iacute;",
      "í",
      "&ntilde;",
      "ñ",
      "&sect;",
      "§",
      "&egrave;",
      "è",
      "&icirc;",
      "î",
      "&ocirc;",
      "ô",
      "&acirc;",
      "â",
      "&ucirc;",
      "û",
      "&ecirc;",
      "ê",
      "&aelig;",
      "æ",
      "&iexcl;",
      "¡",
      "&#151;",
      "-",
      "&#0151;",
      "-",
      "&#0146;",
      "'",
      "&#146;",
      "'",
      "&#0145;",
      "'",
      "&#145;",
      "'",
      "&quot;",
      "\"",
      "&nbsp;",
      String.valueOf('\u007f'),
      "&#159;",
      String.valueOf('\u009f'),
      "&#155;",
      String.valueOf('\u009b'),
      "&#158;",
      String.valueOf('\u009e'),
      "&#146;",
      String.valueOf('\u0092'),
      "&#132;",
      String.valueOf('\u0084')
   };

   private HtmlUtilities() {
   }

   private static void buildEntityTables() {
      entityTableDecode = new HashMap<>(ENTITIES.length);

      for (int i = 0; i < ENTITIES.length; i += 2) {
         if (!entityTableDecode.containsKey(ENTITIES[i])) {
            entityTableDecode.put(ENTITIES[i], ENTITIES[i + 1]);
         }
      }
   }

   public static String unHTML(String source) {
      return decodeEntities(removeTags(source));
   }

   public static String removeTags(String source) {
      StringBuffer result = new StringBuffer(source.length());
      int i0 = -1;

      for (int i1 = source.indexOf("<"); i1 != -1; i1 = source.indexOf("<", i0)) {
         result.append(source, i0 + 1, i1);
         i0 = source.indexOf(">", i1);
         if (i0 == -1) {
            i0 = source.length() - 1;
            break;
         }
      }

      result.append(source, i0 + 1, source.length());
      return result.toString();
   }

   public static String decodeEntities(String source) {
      StringBuffer result = new StringBuffer(source.length());
      int i0 = -1;

      for (int i1 = source.indexOf("&"); i1 != -1; i1 = source.indexOf("&", i0)) {
         result.append(source, i0 + 1, i1);
         i0 = source.indexOf(";", i1);
         if (i0 == -1) {
            i0 = source.length() - 1;
            break;
         }

         result.append(decodeEntity(source.substring(i1, i0 + 1)));
      }

      result.append(source, i0 + 1, source.length());
      return result.toString();
   }

   public static String decodeEntity(String entity) {
      if (entityTableDecode == null) {
         buildEntityTables();
      }

      String s = entityTableDecode.get(entity);
      if (s == null) {
         System.err.println("Unknown HTML-Entity '" + entity + "' in HTMLTools.decodeEntity(). Ignored");
         return entity;
      } else {
         return s;
      }
   }

   public static final String encode(String s) {
      return encode(s, "\n");
   }

   public static final String encode(String text, String ignore) {
      ignore = "'" + ignore;
      StringBuffer sb = new StringBuffer(text.length() * 2);

      for (int i = 0; i < text.length(); i++) {
         char ch = text.charAt(i);
         if ((ch < '?' || ch > 'Z') && (ch < 'a' || ch > 'z') && !contains(ignore, ch)) {
            sb.append(encodeSingleChar(ch));
         } else {
            sb.append(ch);
         }
      }

      return sb.toString();
   }

   private static boolean contains(String text, char ch) {
      return text.indexOf(ch) != -1;
   }

   private static final String encodeSingleChar(Character ch) {
      String s = entityTableEncode.get(ch);
      return s == null ? ch.toString() : s;
   }

   public static final String toHtml(Color color) {
      return "#" + toHexString(color);
   }

   public static final String toHexString(Color color) {
      String red = convertToHex(color.getRed());
      String green = convertToHex(color.getGreen());
      String blue = convertToHex(color.getBlue());
      return red + green + blue;
   }

   private static String convertToHex(int value) {
      String converted = Integer.toHexString(value);
      if (converted.length() == 1) {
         converted = "0" + converted;
      }

      return converted;
   }
}
