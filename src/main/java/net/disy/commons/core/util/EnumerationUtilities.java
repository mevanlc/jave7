package net.disy.commons.core.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class EnumerationUtilities {
   public static String getAsString(Enumeration<?> enumeration) {
      StringBuffer buffer = new StringBuffer("{");

      while (enumeration.hasMoreElements()) {
         buffer.append(enumeration.nextElement());
         buffer.append(", ");
      }

      if (buffer.length() > 1) {
         buffer.delete(buffer.length() - 2, buffer.length());
      }

      buffer.append("}");
      return buffer.toString();
   }

   public static <T> T[] toArray(Enumeration<T> items, Class<T> itemClass) {
      List<T> itemList = new ArrayList<>();

      while (items.hasMoreElements()) {
         itemList.add(items.nextElement());
      }

      return itemList.toArray((T[]) Array.newInstance(itemClass, itemList.size()));
   }
}
