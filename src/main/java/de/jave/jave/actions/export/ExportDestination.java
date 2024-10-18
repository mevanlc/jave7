package de.jave.jave.actions.export;

import net.disy.commons.core.util.Ensure;

public enum ExportDestination {
   CLIPBOARD("Copy to System Clipboard"),
   FILE("Save as File");

   private String name;

   private ExportDestination(String name) {
      Ensure.ensureArgumentNotNull(name);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
