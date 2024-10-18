package de.jave.asciimation.export;

import de.jave.ascii.plate.CharacterMetrics;
import java.awt.Dimension;
import java.awt.Font;

public abstract class AbstractAnimationExporter implements IAnimationExporter {
   protected final Dimension calculateExpectedPlatePixelSize(Dimension size) {
      CharacterMetrics metrics = CharacterMetrics.createCharacterMetrics(new Font("Monospaced", 0, 11));
      return new Dimension(size.width * metrics.getWidth() + 6, size.height * metrics.getHeight() + 4);
   }
}
