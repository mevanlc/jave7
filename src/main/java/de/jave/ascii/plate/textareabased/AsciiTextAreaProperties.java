package de.jave.ascii.plate.textareabased;

import de.jave.ascii.IAsciiGuiConstants;
import de.jave.ascii.plate.ruler.AsciiRulerProperties;
import java.awt.Color;
import java.awt.Font;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class AsciiTextAreaProperties extends AbstractChangeableModel {
   private boolean editable = true;
   private final FontModel fontModel;
   private Color backgroundColor = Color.WHITE;
   private Color foregroundColor = Color.BLACK;
   private final AsciiRulerProperties rulerProperties;
   private boolean scrollingEnabled = true;
   private boolean defaultPopupEnabled = true;

   public AsciiTextAreaProperties() {
      this(new FontModel(IAsciiGuiConstants.DEFAULT_ASCII_FONT));
   }

   public AsciiTextAreaProperties(FontModel fontModel) {
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiTextAreaProperties.this.fireChangeEvent();
         }
      });
      this.fontModel = fontModel;
      this.rulerProperties = new AsciiRulerProperties(fontModel);
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   public boolean isEditable() {
      return this.editable;
   }

   public AsciiTextAreaProperties setEditable(boolean editable) {
      if (this.editable == editable) {
         return this;
      } else {
         this.editable = editable;
         this.fireChangeEvent();
         return this;
      }
   }

   public Font getFont() {
      return this.fontModel.getFont();
   }

   public AsciiRulerProperties getRulerProperties() {
      return this.rulerProperties;
   }

   public AsciiTextAreaProperties setBackground(Color backgroundColor) {
      if (this.backgroundColor.equals(backgroundColor)) {
         return this;
      } else {
         this.backgroundColor = backgroundColor;
         this.fireChangeEvent();
         return this;
      }
   }

   public AsciiTextAreaProperties setForeground(Color foregroundColor) {
      if (this.foregroundColor.equals(foregroundColor)) {
         return this;
      } else {
         this.foregroundColor = foregroundColor;
         this.fireChangeEvent();
         return this;
      }
   }

   public boolean isScrollingEnabled() {
      return this.scrollingEnabled;
   }

   public AsciiTextAreaProperties setScrollingEnabled(boolean scrollingEnabled) {
      this.scrollingEnabled = scrollingEnabled;
      return this;
   }

   public AsciiTextAreaProperties setDefaultPopupEnabled(boolean defaultPopupEnabled) {
      this.defaultPopupEnabled = defaultPopupEnabled;
      return this;
   }

   public boolean isDefaultPopupEnabled() {
      return this.defaultPopupEnabled;
   }
}
