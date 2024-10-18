package de.jave.figlet.swing.preferences;

import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import java.awt.Rectangle;

public class JFigletApplicationPreferences extends SmartPreferences {
   private static final String KEY_FRAME_HEIGHT = "frameHeight";
   private static final String KEY_FRAME_WIDTH = "frameWidth";
   private static final String KEY_FRAME_Y = "frameY";
   private static final String KEY_FRAME_X = "frameX";
   private static final int DEFAULT_FRAME_X = 0;
   private static final int DEFAULT_FRAME_Y = 10;
   private static final int DEFAULT_FRAME_WIDTH = 787;
   private static final int DEFAULT_FRAME_HEIGHT = 537;

   public JFigletApplicationPreferences(JavePreferences javePreferences) {
      super(javePreferences.getSubPreferences("JFIGlet/application"));
   }

   public void setApplicationFrameBounds(Rectangle bounds) {
      this.put("frameX", bounds.x);
      this.put("frameY", bounds.y);
      this.put("frameWidth", bounds.width);
      this.put("frameHeight", bounds.height);
   }

   public Rectangle getApplicationFrameBounds() {
      int x = this.getInt("frameX", 0);
      int y = this.getInt("frameY", 10);
      int w = this.getInt("frameWidth", 787);
      int h = this.getInt("frameHeight", 537);
      return new Rectangle(x, y, w, h);
   }
}
