package de.jave.figlet.swing.preferences;

import de.jave.preferences.SmartPreferences;

public class JFigletPreferences extends SmartPreferences {
   public static final String KEY_FIGLET_FONT_NAME = "figletFontName";
   public static final String KEY_FIGLET_FONT_CATEGORY_NAME = "figletFontCategoryName";

   public JFigletPreferences(SmartPreferences parentPreferences) {
      super(parentPreferences.getSubPreferences("figlet"));
   }

   public void setFigFontName(String figFontName) {
      this.put("figletFontName", figFontName);
   }

   public void setFigFontCategoryName(String categoryName) {
      this.put("figletFontCategoryName", categoryName);
   }

   public String getFigFontName() {
      return this.get("figletFontName", null);
   }

   public String getFigFontCategoryName() {
      return this.get("figletFontCategoryName", null);
   }
}
