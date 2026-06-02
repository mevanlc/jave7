package de.jave.jave;

import de.jave.jave.configuration.ConfigurationException;
import de.jave.jave.configuration.IJavaInitializationContext;
import de.jave.jave.configuration.IJaveInitializable;
import de.jave.jave.core.JaveCoreMessages;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class CharSetsConfigurationInitializable implements IJaveInitializable<CharSetsConfiguration> {
   private static final String DEFAULT_CHARSET = "!\"#$%&'()*+,-./0123456789:;<=>?@\nABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`\nabcdefghijklmnopqrstuvwxyz{|}~";

   public CharSetsConfiguration initialize(IJavaInitializationContext context) throws ConfigurationException {
      File configurationFile = context.getConfigurationFile("./config/charsets.txt");
      List<String> characters = new ArrayList<>();
      characters.add("");
      characters.add("");
      characters.add("");
      List<String> names = new ArrayList<>();
      names.add(JaveCoreMessages.CharacterSets_AnyCharacter);
      names.add(JaveCoreMessages.CharacterSets_PureAscii);
      names.add(JaveCoreMessages.CharacterSets_Unicode);
      BufferedReader br = null;

      try {
         br = new BufferedReader(new FileReader(configurationFile));
         boolean mode = true;
         String line = null;

         while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() != 0 && line.charAt(0) != '#') {
               if (mode) {
                  if (line.length() > 30) {
                     line = line.substring(0, 30);
                  }

                  names.add(line);
                  mode = false;
               } else {
                  characters.add(" " + line);
                  mode = true;
               }
            }
         }
      } catch (IOException var11) {
         throw new ConfigurationException(configurationFile, var11);
      } finally {
         IOUtilities.close(br);
      }

      names.add(JaveCoreMessages.CharacterSets_UserDefined);
      characters.add("!\"#$%&'()*+,-./0123456789:;<=>?@\nABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`\nabcdefghijklmnopqrstuvwxyz{|}~");
      String[] var13 = names.toArray(new String[0]);
      String[] var16 = characters.toArray(new String[0]);
      return new CharSetsConfiguration(var13, var16);
   }
}
