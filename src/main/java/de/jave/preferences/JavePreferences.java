package de.jave.preferences;

import de.jave.ascii.IAsciiGuiConstants;
import java.awt.Font;
import java.util.prefs.Preferences;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.font.FontFactory;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class JavePreferences extends SmartPreferences {
   public static final String KEY_FONT_SIZE = "fontSize";
   public static final String KEY_FONT_STYLE = "fontStyle";
   public static final String KEY_FONT_FAMILY_NAME = "fontFamilyName";
   public static final String KEY_ICON_SIZE = "iconSize";
   public static final String KEY_SHOW_LAYERS_PANEL_BY_DEFAULT = "showLayersPanelByDefault";
   public static final int DEFAULT_ICON_SIZE = 16;
   public static final boolean DEFAULT_SHOW_LAYERS_PANEL_BY_DEFAULT = false;
   public static final int[] SUPPORTED_ICON_SIZES = {16, 24, 32};
   private static final int DEFAULT_FONT_SIZE = IAsciiGuiConstants.DEFAULT_ASCII_FONT.getSize();
   private static final int DEFAULT_FONT_STYLE = IAsciiGuiConstants.DEFAULT_ASCII_FONT.getStyle();
   private static final String DEFAULT_FONT_FAMILY_NAME = IAsciiGuiConstants.DEFAULT_ASCII_FONT.getFamily();
   private final FontModel displayFontModel;

   public JavePreferences() {
      super(Preferences.userRoot().node("JavE"));
      String fontFamilyName = this.get("fontFamilyName", DEFAULT_FONT_FAMILY_NAME);
      int fontStyle = this.getInt("fontStyle", DEFAULT_FONT_STYLE);
      int fontSize = this.getInt("fontSize", DEFAULT_FONT_SIZE);
      this.displayFontModel = new FontModel(new Font(fontFamilyName, fontStyle, fontSize));
      this.displayFontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JavePreferences.this.put("fontFamilyName", JavePreferences.this.displayFontModel.getFontFamilyName());
            JavePreferences.this.put("fontStyle", FontFactory.getAwtStyle(JavePreferences.this.displayFontModel.getFontStyle()));
            JavePreferences.this.put("fontSize", JavePreferences.this.displayFontModel.getFontSize());
            JavePreferences.this.flush();
         }
      });
   }

   public FontModel getDisplayFontModel() {
      return this.displayFontModel;
   }

   public int getIconSize() {
      int v = this.getInt(KEY_ICON_SIZE, DEFAULT_ICON_SIZE);
      for (int allowed : SUPPORTED_ICON_SIZES) {
         if (v == allowed) {
            return v;
         }
      }
      return DEFAULT_ICON_SIZE;
   }

   public void setIconSize(int size) {
      for (int allowed : SUPPORTED_ICON_SIZES) {
         if (size == allowed) {
            this.put(KEY_ICON_SIZE, size);
            this.flush();
            return;
         }
      }
   }

   public boolean isLayersPanelShownByDefault() {
      return this.getBoolean(KEY_SHOW_LAYERS_PANEL_BY_DEFAULT, DEFAULT_SHOW_LAYERS_PANEL_BY_DEFAULT);
   }

   public void setLayersPanelShownByDefault(boolean show) {
      this.put(KEY_SHOW_LAYERS_PANEL_BY_DEFAULT, show);
      this.flush();
   }
}
