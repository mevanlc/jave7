package de.jave.braille.table;

public class BrailleTables {
   private static final IBrailleTable[] TABLES = new IBrailleTable[]{
      new EuroBrailleTable(), new GermanBrailleTable(), new FrenchBrailleTable(), new UkBrailleTable(), new UsBrailleTable()
   };

   public static final IBrailleTable getDefaultTable() {
      return TABLES[0];
   }

   public static String[] getAvailableTableNames() {
      String[] result = new String[TABLES.length];

      for (int i = 0; i < result.length; i++) {
         result[i] = TABLES[i].getName();
      }

      return result;
   }

   public static IBrailleTable[] getAvailableTables() {
      return TABLES;
   }

   public static IBrailleTable getByName(String name) {
      for (int i = 0; i < TABLES.length; i++) {
         if (name.equals(TABLES[i].getName())) {
            return TABLES[i];
         }
      }

      return null;
   }
}
