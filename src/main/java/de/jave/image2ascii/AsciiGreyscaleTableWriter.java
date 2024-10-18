package de.jave.image2ascii;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.disy.commons.core.io.IOUtilities;

public class AsciiGreyscaleTableWriter {
   public static void write(AsciiGreyscaleTable table, File file) throws IOException {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(file));
         write(table, writer);
      } finally {
         IOUtilities.close(writer);
      }
   }

   public static void write(AsciiGreyscaleTable table, BufferedWriter writer) throws IOException {
      for (int i = 0; i < 95; i++) {
         writer.write((char)(i + 32));
         writer.write(32);
         writer.write(String.valueOf(table.getBrightnessForChar((char)(i + 32))));
         writer.write(32);
         writer.write(String.valueOf(table.getBrightness4ForChar((char)(i + 32))[0]));
         writer.write(32);
         writer.write(String.valueOf(table.getBrightness4ForChar((char)(i + 32))[1]));
         writer.write(32);
         writer.write(String.valueOf(table.getBrightness4ForChar((char)(i + 32))[2]));
         writer.write(32);
         writer.write(String.valueOf(table.getBrightness4ForChar((char)(i + 32))[3]));
         writer.newLine();
      }

      char[][] characters4 = table.getCharacters4();

      for (int i = 0; i < characters4.length; i++) {
         writer.write(String.valueOf(characters4[i][0]));
         writer.write(32);
         writer.write(String.valueOf(characters4[i][1]));
         writer.write(32);
         writer.write(String.valueOf(characters4[i][2]));
         writer.write(32);
         writer.write(String.valueOf(characters4[i][3]));
         writer.write(32);
         writer.write(characters4[i][4]);
         writer.newLine();
      }

      writer.flush();
   }
}
