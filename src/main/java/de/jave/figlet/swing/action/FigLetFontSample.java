package de.jave.figlet.swing.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class FigLetFontSample {
   private final String sampleText;
   private final char[] characters;

   public FigLetFontSample(char[] characters, String sampleText) {
      Ensure.ensureArgumentNotNull(characters);
      Ensure.ensureArgumentNotNull(sampleText);
      this.characters = characters;
      this.sampleText = sampleText;
   }

   public boolean contains(char character) {
      for (int i = 0; i < this.characters.length; i++) {
         if (character == this.characters[i]) {
            return true;
         }
      }

      return false;
   }

   public void write(File file) throws IOException {
      FileWriter writer = null;

      try {
         writer = new FileWriter(file);
         writer.write(new String(this.characters));
         writer.write("\n");
         IOUtilities.copyStream(new StringReader(this.sampleText), writer);
      } finally {
         IOUtilities.close(writer);
      }
   }

   public static FigLetFontSample load(File sampleFile) throws IOException {
      FileReader reader = null;

      FigLetFontSample var5;
      try {
         reader = new FileReader(sampleFile);
         BufferedReader bufferedReader = new BufferedReader(reader);
         char[] characters = bufferedReader.readLine().toCharArray();
         String sample = IOUtilities.readString(bufferedReader);
         var5 = new FigLetFontSample(characters, sample);
      } finally {
         IOUtilities.close(reader);
      }

      return var5;
   }
}
