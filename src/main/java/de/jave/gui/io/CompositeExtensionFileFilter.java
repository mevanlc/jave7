package de.jave.gui.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompositeExtensionFileFilter extends ExtensionFileFilter {
   public CompositeExtensionFileFilter(String description, ExtensionFileFilter... filters) {
      super(description, getAllExtensions(filters));
   }

   private static List<FileExtension> getAllExtensions(ExtensionFileFilter[] filters) {
      Set<FileExtension> set = new HashSet<>();

      for (ExtensionFileFilter filter : filters) {
         set.addAll(filter.getExtensions());
      }

      List<FileExtension> list = new ArrayList<>(set);
      Collections.sort(list);
      return list;
   }
}
