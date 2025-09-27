package net.disy.commons.swing.icon;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import net.disy.commons.core.model.AbstractChangeableModel;
import net.disy.commons.core.model.IChangeableModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class CompositeIcon extends AbstractChangeableModel implements Icon {
   private final Icon[] icons;
   private final IChangeListener delegatingChangeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         CompositeIcon.this.fireChangeEvent();
      }
   };

   public static Icon createDecoratedIcon(Icon baseIcon, Icon decoratingIcon) {
      return new CompositeIcon(baseIcon, decoratingIcon);
   }

   public CompositeIcon(Icon... icons) {
      Ensure.ensureArgumentNotNull(icons);
      Ensure.ensureArgumentArrayContentsNotNull(icons);
      this.icons = icons;

      for (Icon icon : icons) {
         if (icon instanceof IChangeableModel) {
            ((IChangeableModel)icon).addChangeListener(this.delegatingChangeListener);
         }
      }
   }

   @Override
   public void paintIcon(Component c, Graphics g, int x, int y) {
      for (Icon icon : this.icons) {
         icon.paintIcon(c, g, x, y);
      }
   }

   @Override
   public int getIconWidth() {
      if (this.icons.length == 0) {
         return 0;
      } else {
         int width = this.icons[0].getIconWidth();

         for (int i = 1; i < this.icons.length; i++) {
            if (this.icons[i].getIconWidth() > width) {
               width = this.icons[i].getIconWidth();
            }
         }

         return width;
      }
   }

   @Override
   public int getIconHeight() {
      if (this.icons.length == 0) {
         return 0;
      } else {
         int height = this.icons[0].getIconHeight();

         for (int i = 0; i < this.icons.length; i++) {
            if (this.icons[i].getIconHeight() > height) {
               height = this.icons[i].getIconHeight();
            }
         }

         return height;
      }
   }
}
