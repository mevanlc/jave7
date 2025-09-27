package net.disy.commons.swing.dialog.core.preferences;

import java.awt.Rectangle;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.preferences.IPreferences;

public abstract class AbstractDialogPreferences implements IDialogPreferences {
   private static final String LOCATION_X = "X";
   private static final String LOCATION_Y = "Y";
   private static final String SIZE_WIDTH = "WIDTH";
   private static final String SIZE_HEIGHT = "HEIGHT";
   private final IPreferences preferences;

   public AbstractDialogPreferences(IPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
   }

   @Override
   public Rectangle getBounds() {
      int x = this.preferences.getInt("X", -1);
      int y = this.preferences.getInt("Y", -1);
      int width = this.preferences.getInt("WIDTH", -1);
      int height = this.preferences.getInt("HEIGHT", -1);
      return x != -1 && y != -1 && width != -1 && height != -1 ? new Rectangle(x, y, width, height) : null;
   }

   @Override
   public void setBounds(Rectangle rectangle) {
      int x = rectangle.x;
      int y = rectangle.y;
      int width = rectangle.width;
      int height = rectangle.height;
      if (width != 0 && height != 0) {
         this.preferences.putInt("X", x, -1);
         this.preferences.putInt("Y", y, -1);
         this.preferences.putInt("WIDTH", width, -1);
         this.preferences.putInt("HEIGHT", height, -1);
         this.preferences.flush();
      }
   }
}
