package de.jave.jave.pattern;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class PatternListInitializable implements IJaveInitializable<PatternList> {
   public PatternList initialize(IJavaInitializationContext context) throws ConfigurationException {
      List<Pattern> patterns = new ArrayList<>();
      File configurationFile = context.getConfigurationFile("./config/pattern.jcf");
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(configurationFile));
         String s1 = null;
         String s2 = null;
         String s3 = null;

         while ((s1 = reader.readLine()) != null) {
            if (s1.trim().length() != 0) {
               s2 = reader.readLine();
               if (s2 == null) {
                  throw new ConfigurationException("Unexpected End of Pattern File '" + configurationFile.getAbsolutePath() + "' !");
               }

               s3 = reader.readLine();
               if (s3 == null) {
                  throw new ConfigurationException("Unexpected End of Pattern File '" + configurationFile.getAbsolutePath() + "' !");
               }

               Pattern pattern = new Pattern(s1, s3, s2);
               patterns.add(pattern);
               s3 = reader.readLine();
            }
         }
      } catch (Exception var12) {
         throw new ConfigurationException("Error loading Pattern-File '" + configurationFile.getAbsolutePath() + "'! " + var12);
      } finally {
         IOUtilities.close(reader);
      }

      final Collator var15 = Collator.getInstance();
      Collections.sort(patterns, new Comparator<Pattern>() {
         public int compare(Pattern o1, Pattern o2) {
            return var15.compare(o1.getName(), o2.getName());
         }
      });
      return new PatternList(patterns, configurationFile);
   }
}
