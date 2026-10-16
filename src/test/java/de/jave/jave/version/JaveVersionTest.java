package de.jave.jave.version;

import de.jave.jave.Jave;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.Assert;
import org.junit.Test;

public class JaveVersionTest {
   @Test
   public void generatedPropertiesMatchBuildInputs() {
      Assert.assertEquals(System.getProperty("jave.expectedVersion"), JaveVersion.getFullVersionNumber());
      Assert.assertEquals(System.getProperty("jave.expectedBuildDate"), JaveVersion.getBuildDate());
      Assert.assertEquals("JavE " + JaveVersion.getFullVersionNumber(), JaveTitleProvider.JAVE);
   }

   @Test
   public void versionArgumentReturnsWithoutStartingTheGui() {
      ByteArrayOutputStream output = new ByteArrayOutputStream();
      PrintStream original = System.out;
      try (PrintStream capture = new PrintStream(output, true, StandardCharsets.UTF_8)) {
         System.setOut(capture);
         Jave.main(new String[]{"--version"});
      } finally {
         System.setOut(original);
      }
      Assert.assertEquals(JaveTitleProvider.JAVE + System.lineSeparator(), output.toString(StandardCharsets.UTF_8));
   }
}
