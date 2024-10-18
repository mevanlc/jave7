package de.jave.asciimation.export;

import de.jave.asciimation.export.actionscript.ActionScriptAnimationExportFormat;
import de.jave.asciimation.export.applet.AppletAnimationExportFormat;
import de.jave.asciimation.export.compressedjavascript.CompressedJavaScriptAnimationExportFormat;
import de.jave.asciimation.export.gif.AnimatedGifAnimationExportFormat;
import de.jave.asciimation.export.gif.MultipleGifsAnimationExportFormat;
import de.jave.asciimation.export.jmov.JmovAnimationExportFormat;
import de.jave.asciimation.export.multipletexts.MultipleTextsAnimationExportFormat;
import de.jave.asciimation.export.onetext.OneTextAnimationExportFormat;
import de.jave.asciimation.export.scrollbar.ScrollbarAnimationExportFormat;
import de.jave.asciimation.export.swf.SwfAnimationExportFormat;

public class AnimationExportFormatRegistry {
   public static IAnimationExportFormat[] getAllExportFormats() {
      return new IAnimationExportFormat[]{
         new JmovAnimationExportFormat(),
         new CompressedJavaScriptAnimationExportFormat(),
         new AppletAnimationExportFormat(),
         new ScrollbarAnimationExportFormat(),
         new OneTextAnimationExportFormat(),
         new MultipleTextsAnimationExportFormat(),
         new AnimatedGifAnimationExportFormat(),
         new MultipleGifsAnimationExportFormat(),
         new ActionScriptAnimationExportFormat(),
         new SwfAnimationExportFormat()
      };
   }
}
