package de.jave.jave;

import de.jave.jave.layers.LayeredDocument;
import de.jave.jave.layers.SecondaryLayer;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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

   @Test
   public void recoveryLogRoundTripsLayersAndKeepsCompositeFramesForAnimation() throws IOException {
      LayeredDocument document = LayeredDocument.fromContent(new CharacterPlate("abcde"));
      SecondaryLayer layer = document.addSecondaryLayerAboveActive();
      document.setActiveGlyph(1, 0, 'X');
      document.setActiveGlyph(3, 0, 'Y');
      layer.setName("Ink: \uD83D\uDE00\nsecond line");
      layer.setOpaque(false);
      layer.setVisible(false);
      File logFile = this.temporaryFolder.newFile("layers.jlog");
      CompressedDocumentState beforeFlatten = new CompressedDocumentState(
         document, new Point(), null, null, null, new Point(2, 0), null, "hide layer", ColorScheme.BLACK_ON_WHITE
      );
      document.flatten(true);
      CompressedDocumentState afterFlatten = new CompressedDocumentState(
         document, new Point(), null, null, null, new Point(2, 0), null, "flatten layers", ColorScheme.BLACK_ON_WHITE
      );
      Files.writeString(logFile.toPath(), beforeFlatten.toString() + afterFlatten.toString(), StandardCharsets.UTF_8);

      CompressedDocumentState[] states = JaveLogFileParser.load(logFile);

      Assert.assertEquals(2, states.length);
      LayeredDocument restored = states[0].getLayeredDocument();
      Assert.assertEquals(2, restored.getLayerCount());
      Assert.assertEquals(layer.getId(), restored.getActiveLayerId());
      Assert.assertEquals(layer.getName(), restored.getActiveSecondaryLayer().getName());
      Assert.assertFalse(restored.getActiveSecondaryLayer().isVisible());
      Assert.assertFalse(restored.getActiveSecondaryLayer().isOpaque());
      Assert.assertEquals(new Point(1, 0), restored.getActiveSecondaryLayer().getPosition());
      Assert.assertEquals(layer.getContent(), restored.getActiveSecondaryLayer().getContent());
      Assert.assertArrayEquals(new String[]{"abcde"}, new CharacterPlate(states[0].getContent()).toStringArray());
      Assert.assertEquals(1, states[1].getLayeredDocument().getLayerCount());
      Assert.assertArrayEquals(new String[]{"aXcYe"}, new CharacterPlate(states[1].getContent()).toStringArray());
   }
}
