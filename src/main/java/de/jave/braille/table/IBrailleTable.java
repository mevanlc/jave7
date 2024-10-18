package de.jave.braille.table;

public interface IBrailleTable {
   String getName();

   int getBraillePattern(char var1);

   char getCharacterForBraillePattern(int var1);
}
