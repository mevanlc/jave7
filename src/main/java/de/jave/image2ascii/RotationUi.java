package de.jave.image2ascii;

import de.jave.image.Rotation;
import de.jave.jave.icon.JaveIcons;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import net.dizzy.commons.swing.ui.AbstractObjectUi;

public class RotationUi extends AbstractObjectUi<Rotation> {
   private static final Map<Rotation, Icon> ICON_BY_ROTATION = new HashMap<Rotation, Icon>() {
      {
         this.put(Rotation.NONE, JaveIcons.ROTATE_NONE_ICON);
         this.put(Rotation.RIGHT, JaveIcons.ROTATE_90_RIGHT_ICON);
         this.put(Rotation.UPSIDE_DOWN, JaveIcons.ROTATE_180_ICON);
         this.put(Rotation.LEFT, JaveIcons.ROTATE_90_LEFT_ICON);
      }
   };
   private static final Map<Rotation, String> LABEL_BY_ROTATION = new HashMap<Rotation, String>() {
      {
         this.put(Rotation.NONE, "None");
         this.put(Rotation.RIGHT, "90 degrees right");
         this.put(Rotation.UPSIDE_DOWN, "180 degrees");
         this.put(Rotation.LEFT, "90 degrees left");
      }
   };

   public Icon getIcon(Rotation value) {
      return ICON_BY_ROTATION.get(value);
   }

   public String getLabel(Rotation value) {
      return LABEL_BY_ROTATION.get(value);
   }
}
