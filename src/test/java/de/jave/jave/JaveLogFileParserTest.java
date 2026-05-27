package de.jave.jave;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class JaveLogFileParserTest {
   @Rule
   public TemporaryFolder temporaryFolder = new TemporaryFolder();

   @Test
   public void zeroSizedSelectionInLogIsIgnored() throws IOException {
      File logFile = this.temporaryFolder.newFile("zero-sized-selection.jmov");
      try (FileWriter writer = new FileWriter(logFile)) {
         writer.write("C:#ffffff #000000\n");
         writer.write("J:B1 1 a\n");
         writer.write("S:2 3 B0 0\n");
      }

      CompressedDocumentState[] states = JaveLogFileParser.load(logFile);

      Assert.assertEquals(1, states.length);
      Assert.assertFalse(states[0].hasSelection());
      Assert.assertNull(states[0].getSelectionContent());
      Assert.assertNull(states[0].getSelectionLocation());
   }
}
