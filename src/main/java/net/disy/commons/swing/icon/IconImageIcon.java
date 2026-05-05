package net.disy.commons.swing.icon;

import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.icon.util.IconUtilities;

public class IconImageIcon extends ImageIcon implements IBaseIconProvider {
   private final Icon baseIcon;

   public IconImageIcon(final Icon icon) {
      super(createImage(icon));
      this.baseIcon = icon instanceof IBaseIconProvider ? ((IBaseIconProvider)icon).getBaseIcon() : icon;
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

   @Override
   public Icon getBaseIcon() {
      return this.baseIcon;
   }

   private static Image createImage(Icon icon) {
      return IconUtilities.createBufferedImage(icon);
   }
}
