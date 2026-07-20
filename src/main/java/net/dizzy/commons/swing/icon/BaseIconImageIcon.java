package net.dizzy.commons.swing.icon;

import java.awt.Image;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class BaseIconImageIcon extends ImageIcon implements IBaseIconProvider {
   private final Icon baseIcon;

   public BaseIconImageIcon(URL location, ImageIcon baseIcon) {
      super(location);
      this.baseIcon = baseIcon;
   }

   /**
    * Wraps an already-prepared display image (e.g. a rescaled icon) while carrying a separate
    * {@code baseIcon} — typically the native, unscaled artwork — for consumers such as menus that
    * want the original size via {@link #getBaseIcon()}.
    */
   public BaseIconImageIcon(Image image, Icon baseIcon) {
      super(image);
      this.baseIcon = baseIcon;
   }

   @Override
   public Icon getBaseIcon() {
      return baseIcon;
   }
}
