package net.disy.commons.core.logging;

@Deprecated
public enum Level {
   ALL,
   DEBUG,
   INFO,
   WARN,
   ERROR,
   FATAL,
   OFF;

   public boolean includes(Level otherLevel) {
      return this.compareTo(otherLevel) < 1;
   }

   public static Level getByName(String levelName) {
      try {
         return valueOf(levelName.toUpperCase());
      } catch (IllegalArgumentException var2) {
         throw new IllegalArgumentException("No logging level for name '" + levelName + "' defined.");
      }
   }

   public String getName() {
      return this.name();
   }
}
