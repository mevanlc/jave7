package de.jave.javeplayer.persistence;

import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Assert;
import org.junit.Test;

public class JaveAnimationFileWriterTest {
   @Test
   public void unicodeCellsRoundTripThroughJmovFile() throws Exception {
      CharacterPlate content = new CharacterPlate("Ae\u20DD\uD83D\uDE00");
      JaveAnimationFrame frame = new JaveAnimationFrame();
      frame.setContent(content);
      JaveAnimationFile animation = new JaveAnimationFile();
      animation.add(frame);
      Path file = Files.createTempFile("jave-cell-model-", ".jmov");

      try {
         new JaveAnimationFileWriter().write(animation, file.toFile());
         String serialized = Files.readString(file, StandardCharsets.UTF_8);
         JaveAnimationFile loaded = new JaveAnimationFile();
         loaded.load(file.toUri().toURL());

         Assert.assertTrue(serialized.contains("J:C3 1 A%{2:e\u20DD}\uD83D\uDE00"));
         Assert.assertEquals(1, loaded.getFrameCount());
         Assert.assertEquals(content, new CharacterPlate(loaded.getFrame(0).getContent()));
      } finally {
         Files.deleteIfExists(file);
      }
   }
}
