package de.jave.jave.open;

import de.jave.jave.actions.Image2AsciiAction;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public final class Image2AsciiImageOpenPerformStrategy implements IImageOpenPerformStrategy {
   private final Image2AsciiAction action;

   public Image2AsciiImageOpenPerformStrategy(Image2AsciiAction action) {
      Ensure.ensureArgumentNotNull(action);
      this.action = action;
   }

   @Override
   public Icon getIcon() {
      return this.action.getIcon();
   }

   @Override
   public String getName() {
      return "Image2Ascii Conversion";
   }

   @Override
   public String getToolTipText() {
      return "Opens the image in the image to Ascii converter for automatically creating an Ascii representation";
   }

   @Override
   public void perform(Component parent, File file) {
      this.action.execute(parent, file, null);
   }
}
