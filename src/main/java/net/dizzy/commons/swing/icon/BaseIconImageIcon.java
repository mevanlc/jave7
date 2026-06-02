package net.dizzy.commons.swing.icon;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class BaseIconImageIcon extends ImageIcon implements IBaseIconProvider {
   private final Icon baseIcon;

   public BaseIconImageIcon(URL location, ImageIcon baseIcon) {
      super(location);
      this.baseIcon = baseIcon;
   }

   @Override
   public Icon getBaseIcon() {
      return baseIcon;
   }
}
