package net.disy.commons.swing.icon;

import java.awt.Image;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class BaseIconImageIcon extends ImageIcon implements IBaseIconProvider {
   private final Icon baseIcon;

   public BaseIconImageIcon(Image image, Icon baseIcon) {
      super(image);
      this.baseIcon = baseIcon == null ? this : baseIcon;
   }

   public BaseIconImageIcon(URL location, Icon baseIcon) {
      super(location);
      this.baseIcon = baseIcon == null ? this : baseIcon;
   }

   @Override
   public Icon getBaseIcon() {
      return this.baseIcon;
   }
}
