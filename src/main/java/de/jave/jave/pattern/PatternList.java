package de.jave.jave.pattern;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import net.disy.commons.core.io.IOUtilities;
import net.disy.commons.core.util.Ensure;

public class PatternList {
   private final List<Pattern> patterns;
   private final File configurationFile;

   public PatternList(List<Pattern> patterns, File configurationFile) {
      Ensure.ensureArgumentNotNull(patterns);
      Ensure.ensureArgumentNotNull(configurationFile);
      this.patterns = patterns;
      this.configurationFile = configurationFile;
   }

   public int getPatternCount() {
      return this.patterns.size();
   }

   public Pattern getPattern(int index) {
      return this.patterns.get(index);
   }

   public Pattern getPattern(String name) {
      for (Pattern pattern : this.patterns) {
         if (pattern.getName().equals(name)) {
            return pattern;
         }
      }

      return null;
   }

   public void delete(int index) {
      this.patterns.remove(index);
   }

   public void add(Pattern pat) {
      this.patterns.add(pat);
   }

   public void save() {
      BufferedWriter br = null;

      try {
         br = new BufferedWriter(new FileWriter(this.configurationFile));

         for (int i = 0; i < this.patterns.size(); i++) {
            Pattern pattern = this.patterns.get(i);
            br.write(pattern.getName());
            br.newLine();
            br.write(pattern.getAuthor());
            br.newLine();
            br.write(pattern.getCode());
            br.newLine();
            br.newLine();
         }
      } catch (Exception var7) {
         System.err.println("Error saving Pattern-File! " + var7);
      } finally {
         IOUtilities.close(br);
      }
   }
}
