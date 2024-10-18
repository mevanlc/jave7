package de.jave.jave.core;

import de.jave.core.NLS;

public class JaveCoreMessages extends NLS {
   public static String CharacterSets_UserDefined;
   public static String CharacterSets_PureAscii;
   public static String CharacterSets_AnyCharacter;

   static {
      NLS.initializeMessages("de.jave.jave.core.messages", JaveCoreMessages.class);
   }
}
