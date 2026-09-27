package de.jave.preferences;

import de.jave.jave.actions.preferences.DefaultsPreferencesPanel;
import de.jave.jave.menu.ProbePreferencesFactory;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.io.File;
import javax.swing.JTextField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultFilenameExtensionTest {

   private JaveApplicationPreferences preferences;

   @Before
   public void setUp() {
      JavePreferences javePreferences = new JavePreferences(new ProbePreferencesFactory().userRoot());
      this.preferences = new JaveApplicationPreferences(javePreferences);
   }

   @Test
   public void defaultValueIsTxt() {
      Assert.assertEquals("txt", this.preferences.getDefaultFilenameExtension());
   }

   @Test
   public void preservesLeadingDotsInPreference() {
      this.preferences.setDefaultFilenameExtension("txt");
      Assert.assertEquals("txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension(".txt");
      Assert.assertEquals(".txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension("..txt");
      Assert.assertEquals("..txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension("...txt");
      Assert.assertEquals("...txt", this.preferences.getDefaultFilenameExtension());
   }

   @Test
   public void whitespaceIsRemovedBeforeValidationAndSaving() {
      this.preferences.setDefaultFilenameExtension("  txt  ");
      Assert.assertEquals("txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension("  .  t x t  ");
      Assert.assertEquals(".txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension("  . . txt  ");
      Assert.assertEquals("..txt", this.preferences.getDefaultFilenameExtension());

      this.preferences.setDefaultFilenameExtension("\t. . . txt\n");
      Assert.assertEquals("...txt", this.preferences.getDefaultFilenameExtension());
   }

   @Test(expected = IllegalArgumentException.class)
   public void emptyStringAfterWhitespaceRemovalIsRejected() {
      this.preferences.setDefaultFilenameExtension("   \t  ");
   }

   @Test(expected = IllegalArgumentException.class)
   public void dotsOnlyAfterWhitespaceRemovalIsRejected() {
      this.preferences.setDefaultFilenameExtension("  .  ");
   }

   @Test(expected = IllegalArgumentException.class)
   public void multipleDotsOnlyAfterWhitespaceRemovalIsRejected() {
      this.preferences.setDefaultFilenameExtension("  ..  ");
   }

   @Test(expected = IllegalArgumentException.class)
   public void invalidFilenameCharactersAreRejected() {
      this.preferences.setDefaultFilenameExtension("txt/foo");
   }

   @Test(expected = IllegalArgumentException.class)
   public void asteriskIsRejected() {
      this.preferences.setDefaultFilenameExtension("*.txt");
   }

   @Test
   public void validationMethodReportsCorrectValidity() {
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension("txt"));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension(".txt"));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension("..txt"));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension("...txt"));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension("  .txt  "));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension("asc"));
      Assert.assertTrue(JaveApplicationPreferences.isValidDefaultFilenameExtension(".asc"));

      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension(null));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension(""));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("   "));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("."));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension(".."));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("..."));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension(" . . "));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo/bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo\\bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo:bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo*bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo?bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo\"bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo<bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo>bar"));
      Assert.assertFalse(JaveApplicationPreferences.isValidDefaultFilenameExtension("foo|bar"));
   }

   @Test
   public void namingProcessWithLeadingDotsExamples() {
      // txt -> Untitled.txt
      File file1 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("Untitled"), "txt");
      Assert.assertEquals("Untitled.txt", file1.getName());

      // .txt -> Untitled.txt
      File file2 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("Untitled"), ".txt");
      Assert.assertEquals("Untitled.txt", file2.getName());

      // ..txt -> Untitled..txt
      File file3 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("Untitled"), "..txt");
      Assert.assertEquals("Untitled..txt", file3.getName());

      // ...txt -> Untitled...txt
      File file4 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("Untitled"), "...txt");
      Assert.assertEquals("Untitled...txt", file4.getName());
   }

   @Test
   public void namingProcessPreservesExistingExtensionOrDot() {
      // Untitled.txt -> Untitled.txt
      File file1 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("Untitled.txt"), "txt");
      Assert.assertEquals("Untitled.txt", file1.getName());

      // foo.bar -> foo.bar
      File file2 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("foo.bar"), "txt");
      Assert.assertEquals("foo.bar", file2.getName());

      // foo. -> foo.
      File file3 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("foo."), "txt");
      Assert.assertEquals("foo.", file3.getName());

      // ..other -> ..other (already has dots)
      File file4 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("..other"), "txt");
      Assert.assertEquals("..other", file4.getName());

      // File with parent directory containing dot
      File file5 = JaveApplicationPreferences.appendDefaultExtensionIfNeeded(new File("/path.with.dot", "Untitled"), "txt");
      Assert.assertEquals(new File("/path.with.dot", "Untitled.txt"), file5);
   }

   @Test
   public void defaultsPreferencesPanelInteractions() {
      DefaultsPreferencesPanel panel = new DefaultsPreferencesPanel(this.preferences, null);
      JTextField textField = panel.getDefaultFilenameExtensionTextField();
      Assert.assertEquals("txt", textField.getText());

      // Change text and save
      textField.setText("  .txt  ");
      panel.savePreferences();
      Assert.assertEquals(".txt", this.preferences.getDefaultFilenameExtension());

      // Change to ..txt
      textField.setText("  ..txt  ");
      panel.savePreferences();
      Assert.assertEquals("..txt", this.preferences.getDefaultFilenameExtension());

      // Invalid input does not overwrite preference and restores valid preference
      textField.setText("   ");
      panel.savePreferences();
      Assert.assertEquals("..txt", this.preferences.getDefaultFilenameExtension());
      Assert.assertEquals("..txt", textField.getText());

      // Revert restores preference value to text field
      textField.setText("temp");
      panel.revert();
      Assert.assertEquals("..txt", textField.getText());
   }
}
