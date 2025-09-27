package net.disy.commons.swing.icon;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.icon.util.IconUtilities;

public class IconImageIcon extends ImageIcon {
   public IconImageIcon(final Icon icon) {
      super(createImage(icon));
      if (icon instanceof IChangeableModel) {
         IChangeableModel changeableModel = (IChangeableModel)icon;
         changeableModel.addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               IconImageIcon.this.setImage(IconImageIcon.createImage(icon));
            }
         });
      }
   }

   private static Image createImage(Icon icon) {
      return IconUtilities.createBufferedImage(icon);
   }
}
