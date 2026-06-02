package de.jave.jave.preferences;

import de.jave.jave.icon.JaveIcons;
import java.awt.Color;
import javax.swing.Icon;
import net.dizzy.commons.core.util.Ensure;

public class ColorScheme {
   private final String name;
   private final int index;
   private static final Color DARK_YELLOW = new Color(192, 192, 0);
   private static final Color GRAY_128 = new Color(128, 128, 128);
   private static final Color[] COLOR_CURSOR = new Color[]{Color.black, Color.white, Color.black};
   private static final Color[] COLOR_TOOL_REGION = new Color[]{Color.blue, Color.blue, Color.blue};
   private static final Color[] COLOR_TOOL = new Color[]{Color.gray, Color.darkGray, Color.gray};
   private static final Color[] COLOR_TOOL_DARKER = new Color[]{DARK_YELLOW, DARK_YELLOW, DARK_YELLOW};
   private static final Color[] COLOR_PLATE_SHADOW = new Color[]{Color.black, Color.darkGray, Color.black};
   private static final Color[] COLOR_TOOL_PREVIEW = new Color[]{new Color(0, 0, 160), new Color(160, 160, 255), new Color(0, 0, 192)};
   private static final Color[] COLOR_TOOL_PREVIEW_DELETE = new Color[]{new Color(160, 160, 255), new Color(0, 0, 192), new Color(160, 160, 255)};
   private static final Color[] COLOR_TOOL_HELPING = new Color[]{new Color(96, 96, 96), new Color(128, 128, 128), new Color(160, 160, 160)};
   private static final Color[] COLOR_WATERMARK = new Color[]{new Color(0, 0, 192), new Color(0, 0, 192), new Color(0, 0, 192)};
   private static final Color[] COLOR_WATERMARK_FILL = new Color[]{new Color(192, 192, 255), new Color(192, 192, 255), new Color(192, 192, 255)};
   private static final Color[] COLOR_SELECTION_TEXT = new Color[]{Color.gray, Color.gray, Color.gray};
   private static final Color[] COLOR_PLATE_LINES = new Color[]{new Color(224, 224, 224), new Color(32, 32, 32), new Color(178, 178, 178)};
   private static final Color[] COLOR_PLATE_EMPTY = new Color[]{GRAY_128, GRAY_128, GRAY_128};
   private static final Color[] COLOR_SELECTION_BACKGROUND = new Color[]{new Color(230, 230, 230), new Color(32, 32, 32), new Color(205, 205, 205)};
   private static final Color[] COLOR_PLATE_BACKGROUND = new Color[]{Color.white, Color.black, new Color(192, 192, 192)};
   private static final Color[] COLOR_TEXT = new Color[]{Color.black, Color.white, Color.black};
   private static final String[] COLOR_SCHEME_HEX = new String[]{"#FFFFFF #000000", "#000000 #FFFFFF", "#C0C0C0 #000000"};
   private final Color colorSelectionBackground;
   private final Color colorSelectionText;
   private final Color colorPlateEmpty;
   private final Color colorPlateShadow;
   private final Color colorPlateBackground;
   private final Color colorPlateLines;
   private final Color colorText;
   private final Color colorCursor;
   private final Color colorToolHelping;
   private final Color colorToolPreview;
   private final Color colorToolPreviewDelete;
   private final Color colorToolRegion;
   private final Color colorTool;
   private final Color colorToolDarker;
   private final Color colorWatermarkFill;
   private final Color colorWatermark;
   private final String colorHex;
   public static final ColorScheme BLACK_ON_WHITE = new ColorScheme("bow", "black on white", 0, JaveIcons.COLOR_SCHEME_BLACK_ON_WHITE);
   public static final ColorScheme WHITE_ON_BLACK = new ColorScheme("wob", "white on black", 1, JaveIcons.COLOR_SCHEME_WHITE_ON_BLACK);
   public static final ColorScheme BLACK_ON_GRAY = new ColorScheme("bog", "black on gray", 2, JaveIcons.COLOR_SCHEME_BLACK_ON_GRAY);
   private final Icon icon;
   private final String id;

   private ColorScheme(String id, String name, int index, Icon icon) {
      Ensure.ensureArgumentNotNull(id);
      this.id = id;
      this.name = name;
      this.index = index;
      this.icon = icon;
      this.colorSelectionBackground = COLOR_SELECTION_BACKGROUND[index];
      this.colorSelectionText = COLOR_SELECTION_TEXT[index];
      this.colorPlateLines = COLOR_PLATE_LINES[index];
      this.colorPlateEmpty = COLOR_PLATE_EMPTY[index];
      this.colorPlateBackground = COLOR_PLATE_BACKGROUND[index];
      this.colorPlateShadow = COLOR_PLATE_SHADOW[index];
      this.colorText = COLOR_TEXT[index];
      this.colorCursor = COLOR_CURSOR[index];
      this.colorToolHelping = COLOR_TOOL_HELPING[index];
      this.colorToolPreview = COLOR_TOOL_PREVIEW[index];
      this.colorToolPreviewDelete = COLOR_TOOL_PREVIEW_DELETE[index];
      this.colorToolRegion = COLOR_TOOL_REGION[index];
      this.colorTool = COLOR_TOOL[index];
      this.colorToolDarker = COLOR_TOOL_DARKER[index];
      this.colorWatermarkFill = COLOR_WATERMARK_FILL[index];
      this.colorWatermark = COLOR_WATERMARK[index];
      this.colorHex = COLOR_SCHEME_HEX[index];
   }

   public Icon getIcon() {
      return this.icon;
   }

   public String getName() {
      return this.name;
   }

   public int getIndex() {
      return this.index;
   }

   public Color getColorCursor() {
      return this.colorCursor;
   }

   public Color getColorPlateBackground() {
      return this.colorPlateBackground;
   }

   public Color getColorPlateEmpty() {
      return this.colorPlateEmpty;
   }

   public Color getColorPlateLines() {
      return this.colorPlateLines;
   }

   public Color getColorPlateShadow() {
      return this.colorPlateShadow;
   }

   public Color getColorSelectionBackground() {
      return this.colorSelectionBackground;
   }

   public Color getColorSelectionText() {
      return this.colorSelectionText;
   }

   public Color getColorText() {
      return this.colorText;
   }

   public Color getColorTool() {
      return this.colorTool;
   }

   public Color getColorToolDarker() {
      return this.colorToolDarker;
   }

   public Color getColorToolHelping() {
      return this.colorToolHelping;
   }

   public Color getColorToolPreview() {
      return this.colorToolPreview;
   }

   public Color getColorToolPreviewDelete() {
      return this.colorToolPreviewDelete;
   }

   public Color getColorToolRegion() {
      return this.colorToolRegion;
   }

   public Color getColorWatermark() {
      return this.colorWatermark;
   }

   public Color getColorWatermarkFill() {
      return this.colorWatermarkFill;
   }

   public String getColorHex() {
      return this.colorHex;
   }

   public static ColorScheme[] getAll() {
      return new ColorScheme[]{BLACK_ON_WHITE, WHITE_ON_BLACK, BLACK_ON_GRAY};
   }

   public static ColorScheme getById(String id) {
      for (ColorScheme scheme : getAll()) {
         if (id.equals(scheme.getId())) {
            return scheme;
         }
      }

      return null;
   }

   public String getId() {
      return this.id;
   }
}
