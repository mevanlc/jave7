package de.jave.asciimation.export.gif;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.AsciiToThumbnailConverter;
import de.jave.jave.export.Ascii2ImageOptions;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import net.disy.commons.core.util.Ensure;
import net.jmge.gif.facade.AnimatedGifFileBuilder;

public class AnimatedGifAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private AnimationProperties animationProperties;
   private AnimatedGifFileBuilder gifFileBuilder;

   public AnimatedGifAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties properties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.animationProperties = properties;
      this.gifFileBuilder = new AnimatedGifFileBuilder();
      this.gifFileBuilder.setComments(JaveTitleProvider.CREATED_BY_JAVE);
      this.gifFileBuilder.setLoopCount(this.options.getAdditionalOptions().isLoop() ? 0 : 1);
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      Ascii2ImageOptions ascii2ImageOptions = new Ascii2ImageOptions(
         this.options.getAdditionalOptions().getGifFont(),
         this.options.getAdditionalOptions().isConnectedLinesView(),
         this.animationProperties.getForegroundColor(),
         this.animationProperties.getBackgroundColor()
      );
      BufferedImage image = AsciiToThumbnailConverter.convert(content, ascii2ImageOptions);
      this.gifFileBuilder.add(image, this.animationProperties.getFrameDuration());
   }

   @Override
   public void finish() throws Exception {
      this.gifFileBuilder.write(this.options.getFile());
   }

   @Override
   public void rollBack() {
   }
}
