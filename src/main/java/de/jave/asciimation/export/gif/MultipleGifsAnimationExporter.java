package de.jave.asciimation.export.gif;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import net.dizzy.commons.core.util.Ensure;
import net.jmge.gif.facade.GifFileWriter;

public class MultipleGifsAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private int ciffers;
   private int frameCount = 0;
   private AnimationProperties animationProperties;

   public MultipleGifsAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.animationProperties = animationProperties;
      this.ciffers = 1;
      frameCount--;

      while (frameCount >= 10) {
         frameCount /= 10;
         this.ciffers++;
      }
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      BufferedImage image = AsciiToThumbnailConverter.convert(
         content,
         this.options.getAdditionalOptions().getGifScale(),
         this.options.getAdditionalOptions().getGifFont(),
         this.animationProperties.getBackgroundColor(),
         this.animationProperties.getForegroundColor(),
         this.options.getAdditionalOptions().isConnectedLinesView()
      );
      String fileName = this.options.getFileNameWithoutExtension() + "_" + TextTools.fillFront(String.valueOf(this.frameCount), '0', this.ciffers) + ".gif";
      GifFileWriter writer = new GifFileWriter(image);
      writer.setComments(JaveTitleProvider.CREATED_BY_JAVE);
      writer.write(new File(fileName));
      this.frameCount++;
   }

   @Override
   public void finish() throws Exception {
   }

   @Override
   public void rollBack() {
   }
}
