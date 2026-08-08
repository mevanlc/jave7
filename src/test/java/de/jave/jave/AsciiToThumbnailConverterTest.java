package de.jave.jave;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import org.junit.Assert;
import org.junit.Test;

public class AsciiToThumbnailConverterTest {
   @Test
   public void complexGlyphsUseCellAlignedFontRenderingAtThumbnailScales() {
      CharacterPlate content = new CharacterPlate("Ae\u20DDB");
      Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);

      BufferedImage image = AsciiToThumbnailConverter.convert(content, 1, font, Color.WHITE, Color.BLACK, false);

      CharacterMetrics metrics = CharacterMetrics.createCharacterMetrics(font);
      Assert.assertEquals(content.getWidth() * metrics.getWidth(), image.getWidth());
      Assert.assertEquals(content.getHeight() * metrics.getHeight(), image.getHeight());
   }
}
