package net.disy.commons.core.text.font;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.util.ObjectUtilities;

public enum FontStyle {
   PLAIN("Plain") {
      @Override
      public void accept(IFontStyleVisitor visitor) {
         visitor.visitPlainFontStyle(this);
      }
   },
   ITALIC("Italic", FontStyleProperty.ITALICS) {
      @Override
      public void accept(IFontStyleVisitor visitor) {
         visitor.visitItalicFontStyle(this);
      }
   },
   BOLD("Bold", FontStyleProperty.BOLD) {
      @Override
      public void accept(IFontStyleVisitor visitor) {
         visitor.visitBoldFontStyle(this);
      }
   },
   BOLD_ITALIC("Bold italic", FontStyleProperty.BOLD, FontStyleProperty.ITALICS) {
      @Override
      public void accept(IFontStyleVisitor visitor) {
         visitor.visitBoldItalicFontStyle(this);
      }
   };

   private final String name;
   private final Set<FontStyleProperty> properties;

   private FontStyle(String name, FontStyleProperty... properties) {
      this.name = name;
      this.properties = new HashSet<>(Arrays.asList(properties));
   }

   public abstract void accept(IFontStyleVisitor var1);

   public boolean isItalic() {
      return this.properties.contains(FontStyleProperty.ITALICS);
   }

   public boolean isBold() {
      return this.properties.contains(FontStyleProperty.BOLD);
   }

   public boolean isPlain() {
      return !this.isBold() && !this.isItalic();
   }

   public String getName() {
      return this.name;
   }

   public static FontStyle getByName(String name) {
      for (FontStyle fontStyle : values()) {
         if (name.equals(fontStyle.getName())) {
            return fontStyle;
         }
      }

      throw new IllegalArgumentException("No font style defined for name " + name);
   }

   public static boolean nameExists(String name) {
      for (FontStyle fontStyle : values()) {
         if (ObjectUtilities.equals(name, fontStyle.getName())) {
            return true;
         }
      }

      return false;
   }

   public static FontStyle getStyle(boolean isBold, boolean isItalic) {
      Set<FontStyleProperty> properties = new HashSet<>();
      if (isBold) {
         properties.add(FontStyleProperty.BOLD);
      }

      if (isItalic) {
         properties.add(FontStyleProperty.ITALICS);
      }

      return getStyle(properties);
   }

   public static FontStyle getStyle(Set<FontStyleProperty> properties) {
      for (FontStyle style : values()) {
         if (style.properties.equals(properties)) {
            return style;
         }
      }

      throw new UnreachableCodeReachedException("(ip)");
   }

   public FontStyle derive(FontStyleProperty fontStyle, boolean enabled) {
      HashSet<FontStyleProperty> newProperties = new HashSet<>(this.properties);
      if (enabled) {
         newProperties.add(fontStyle);
      } else {
         newProperties.remove(fontStyle);
      }

      return getStyle(newProperties);
   }

   public boolean isEnabled(FontStyleProperty fontStyle) {
      return this.properties.contains(fontStyle);
   }
}
