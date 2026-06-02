package de.jave.jave.tool.linealgorithmic;

import de.jave.preferences.SmartPreferences;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.util.Ensure;

public class LineAlgorithmicOptions extends AbstractChangeableModel {
   private static final String KEY_STYLE = "style";
   private static final String KEY_ARROW = "arrow";
   private static final String KEY_ARROWHEAD_SIZE = "arrowheadSize";
   private static final String KEY_ARROWHEAD_ANGLE = "arrowheadAngle";
   private static final String KEY_CARDINAL_TIPS = "cardinalTips";
   public static final int ARROWHEAD_SIZE_MIN = 1;
   public static final int ARROWHEAD_SIZE_MAX = 12;
   public static final int ARROWHEAD_ANGLE_MIN = 10;
   public static final int ARROWHEAD_ANGLE_MAX = 80;
   private static final AlgorithmicLineStyle DEFAULT_STYLE = AlgorithmicLineStyle.VERONICA;
   private static final ArrowheadPlacement DEFAULT_ARROWHEAD_PLACEMENT = ArrowheadPlacement.END;
   private static final int DEFAULT_ARROWHEAD_SIZE = 3;
   private static final int DEFAULT_ARROWHEAD_ANGLE = 35;
   private static final boolean DEFAULT_CARDINAL_TIPS = true;

   private AlgorithmicLineStyle style = DEFAULT_STYLE;
   private ArrowheadPlacement arrowheadPlacement = DEFAULT_ARROWHEAD_PLACEMENT;
   private int arrowheadSize = DEFAULT_ARROWHEAD_SIZE;
   private int arrowheadAngle = DEFAULT_ARROWHEAD_ANGLE;
   private boolean cardinalTips = DEFAULT_CARDINAL_TIPS;

   public void loadFrom(SmartPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.style = getStyle(preferences.get(KEY_STYLE, null), DEFAULT_STYLE);
      this.arrowheadPlacement = getArrowheadPlacement(preferences.get(KEY_ARROW, null), DEFAULT_ARROWHEAD_PLACEMENT);
      this.arrowheadSize = getIntInRange(
         preferences, KEY_ARROWHEAD_SIZE, DEFAULT_ARROWHEAD_SIZE, ARROWHEAD_SIZE_MIN, ARROWHEAD_SIZE_MAX
      );
      this.arrowheadAngle = getIntInRange(
         preferences, KEY_ARROWHEAD_ANGLE, DEFAULT_ARROWHEAD_ANGLE, ARROWHEAD_ANGLE_MIN, ARROWHEAD_ANGLE_MAX
      );
      this.cardinalTips = preferences.getBoolean(KEY_CARDINAL_TIPS, DEFAULT_CARDINAL_TIPS);
   }

   public void saveTo(SmartPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      preferences.put(KEY_STYLE, this.style.name());
      preferences.put(KEY_ARROW, this.arrowheadPlacement.name());
      preferences.put(KEY_ARROWHEAD_SIZE, this.arrowheadSize);
      preferences.put(KEY_ARROWHEAD_ANGLE, this.arrowheadAngle);
      preferences.put(KEY_CARDINAL_TIPS, this.cardinalTips);
   }

   public AlgorithmicLineStyle getStyle() {
      return this.style;
   }

   public void setStyle(AlgorithmicLineStyle style) {
      Ensure.ensureArgumentNotNull(style);
      if (this.style != style) {
         this.style = style;
         this.fireChangeEvent();
      }
   }

   public ArrowheadPlacement getArrowheadPlacement() {
      return this.arrowheadPlacement;
   }

   public void setArrowheadPlacement(ArrowheadPlacement arrowheadPlacement) {
      Ensure.ensureArgumentNotNull(arrowheadPlacement);
      if (this.arrowheadPlacement != arrowheadPlacement) {
         this.arrowheadPlacement = arrowheadPlacement;
         this.fireChangeEvent();
      }
   }

   public int getArrowheadSize() {
      return this.arrowheadSize;
   }

   public void setArrowheadSize(int arrowheadSize) {
      int value = clamp(arrowheadSize, ARROWHEAD_SIZE_MIN, ARROWHEAD_SIZE_MAX);
      if (this.arrowheadSize != value) {
         this.arrowheadSize = value;
         this.fireChangeEvent();
      }
   }

   public int getArrowheadAngle() {
      return this.arrowheadAngle;
   }

   public void setArrowheadAngle(int arrowheadAngle) {
      int value = clamp(arrowheadAngle, ARROWHEAD_ANGLE_MIN, ARROWHEAD_ANGLE_MAX);
      if (this.arrowheadAngle != value) {
         this.arrowheadAngle = value;
         this.fireChangeEvent();
      }
   }

   public boolean isCardinalTips() {
      return this.cardinalTips;
   }

   public void setCardinalTips(boolean cardinalTips) {
      if (this.cardinalTips != cardinalTips) {
         this.cardinalTips = cardinalTips;
         this.fireChangeEvent();
      }
   }

   private static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   private static AlgorithmicLineStyle getStyle(String rawStyle, AlgorithmicLineStyle defaultStyle) {
      if (rawStyle == null) {
         return defaultStyle;
      }

      try {
         return AlgorithmicLineStyle.valueOf(rawStyle);
      } catch (IllegalArgumentException e) {
         return defaultStyle;
      }
   }

   private static ArrowheadPlacement getArrowheadPlacement(String rawPlacement, ArrowheadPlacement defaultPlacement) {
      if (rawPlacement == null) {
         return defaultPlacement;
      }

      try {
         return ArrowheadPlacement.valueOf(rawPlacement);
      } catch (IllegalArgumentException e) {
         return defaultPlacement;
      }
   }

   private static int getIntInRange(SmartPreferences preferences, String key, int defaultValue, int min, int max) {
      int value = preferences.getInt(key, defaultValue);
      return value >= min && value <= max ? value : defaultValue;
   }
}
