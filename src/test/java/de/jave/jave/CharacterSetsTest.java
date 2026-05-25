package de.jave.jave;

import org.junit.Assert;
import org.junit.Test;

public class CharacterSetsTest {
   @Test
   public void defaultCharsetIsUnicode() {
      Assert.assertEquals(2, CharacterSets.getDefaultCharsetIndex());
   }

   @Test
   public void unicodeCharsetAllowsBmpUnicodeButRejectsAsciiControlsSurrogatesAndPrivateUse() {
      CharacterSets characterSets = new CharacterSets(new CharSetsConfiguration(
         new String[]{"Any Characters", "Pure ASCII", "Unicode (BMP)", "User Defined"},
         new String[]{"", "", "", " abc"}
      ));
      characterSets.setCurrentCharsetIndex(CharacterSets.getDefaultCharsetIndex());

      Assert.assertTrue(characterSets.isLegal('A'));
      Assert.assertTrue(characterSets.isLegal('\u00e9'));
      Assert.assertFalse(characterSets.isLegal('\u001f'));
      Assert.assertFalse(characterSets.isLegal('\u007f'));
      Assert.assertFalse(characterSets.isLegal('\uD83D'));
      Assert.assertFalse(characterSets.isLegal('\uDE00'));
      Assert.assertFalse(characterSets.isLegal('\ue000'));
   }

   @Test
   public void pureAsciiCharsetKeepsExistingRange() {
      CharacterSets characterSets = new CharacterSets(new CharSetsConfiguration(
         new String[]{"Any Characters", "Pure ASCII", "Unicode (BMP)", "User Defined"},
         new String[]{"", "", "", " abc"}
      ));
      characterSets.setCurrentCharsetIndex(1);

      Assert.assertTrue(characterSets.isLegal(' '));
      Assert.assertTrue(characterSets.isLegal('~'));
      Assert.assertFalse(characterSets.isLegal('\u001f'));
      Assert.assertFalse(characterSets.isLegal('\u00e9'));
   }

   @Test
   public void userDefinedCharsetStillUsesConfiguredCharactersAfterBuiltinUnicode() {
      CharacterSets characterSets = new CharacterSets(new CharSetsConfiguration(
         new String[]{"Any Characters", "Pure ASCII", "Unicode (BMP)", "User Defined"},
         new String[]{"", "", "", " abc"}
      ));
      characterSets.setCurrentCharsetIndex(3);

      Assert.assertTrue(characterSets.isLegal('a'));
      Assert.assertFalse(characterSets.isLegal('z'));
   }
}
