package de.jave.ascii.plate.ruler;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.ascii.plate.CharacterSizeModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import net.dizzy.commons.core.model.AbstractChangeableModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.core.util.ObjectUtilities;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.util.LayoutDirection;

public class AsciiRulerProperties extends AbstractChangeableModel {
   private int printMarginColumn = 72;
   private boolean printMarginColumnVisible = false;
   private boolean horizontalRulerVisible = false;
   private boolean verticalRulerVisible = false;
   private boolean showMouseLocation = false;
   private Color backgroundColor = SystemColor.control;
   private Color foregroundColor = new Color(64, 64, 64);
   private static final Font rulerFont = new Font("Dialog", 0, 9);
   private final CharacterSizeModel characterSizeModel;
   private Point plateOrigin = new Point(0, 0);
   private Dimension documentSize = null;

   public AsciiRulerProperties(FontModel fontModel) {
      this(createCharacterSizeModel(fontModel));
   }

   private static CharacterSizeModel createCharacterSizeModel(final FontModel fontModel) {
      final CharacterSizeModel sizeModel = new CharacterSizeModel();
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiRulerProperties.updateSize(fontModel, sizeModel);
         }
      });
      updateSize(fontModel, sizeModel);
      return sizeModel;
   }

   private static void updateSize(FontModel fontModel, CharacterSizeModel sizeModel) {
      sizeModel.setCharSize(CharacterMetrics.createCharacterMetrics(fontModel.getFont()));
   }

   public AsciiRulerProperties(CharacterSizeModel characterSizeModel) {
      this.characterSizeModel = characterSizeModel;
      characterSizeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AsciiRulerProperties.this.fireChangeEvent();
         }
      });
   }

   public double getCharacterWidth() {
      return this.characterSizeModel.getCharacterSize().getWidth();
   }

   public double getCharacterHeight() {
      return this.characterSizeModel.getCharacterSize().getHeight();
   }

   public boolean isPrintMarginColumnVisible() {
      return this.printMarginColumnVisible;
   }

   public void setPrintMarginColumnVisible(boolean printMarginColumnVisible) {
      if (this.printMarginColumnVisible != printMarginColumnVisible) {
         this.printMarginColumnVisible = printMarginColumnVisible;
         this.fireChangeEvent();
      }
   }

   public Color getBackgroundColor() {
      return this.backgroundColor;
   }

   public Font getFont() {
      return rulerFont;
   }

   public void setBackgroundColor(Color backgroundColor) {
      Ensure.ensureArgumentNotNull(backgroundColor);
      if (!this.backgroundColor.equals(backgroundColor)) {
         this.backgroundColor = backgroundColor;
         this.fireChangeEvent();
      }
   }

   public void setForegroundColor(Color foregroundColor) {
      Ensure.ensureArgumentNotNull(foregroundColor);
      if (!this.foregroundColor.equals(foregroundColor)) {
         this.foregroundColor = foregroundColor;
         this.fireChangeEvent();
      }
   }

   public Color getForegroundColor() {
      return this.foregroundColor;
   }

   public int getPrintMarginColumn() {
      return this.printMarginColumn;
   }

   public void setPrintMarginColumn(int columnMarkerColumnIndex) {
      if (this.printMarginColumn != columnMarkerColumnIndex) {
         this.printMarginColumn = columnMarkerColumnIndex;
         this.fireChangeEvent();
      }
   }

   public void setHorizontalRulerVisible(boolean horizontalRulerVisible) {
      if (this.horizontalRulerVisible != horizontalRulerVisible) {
         this.horizontalRulerVisible = horizontalRulerVisible;
         this.fireChangeEvent();
      }
   }

   public void setVerticalRulerVisible(boolean verticalRulerVisible) {
      if (this.verticalRulerVisible != verticalRulerVisible) {
         this.verticalRulerVisible = verticalRulerVisible;
         this.fireChangeEvent();
      }
   }

   public void setPlateOrigin(Point plateOrigin) {
      Ensure.ensureArgumentNotNull(plateOrigin);
      if (!this.plateOrigin.equals(plateOrigin)) {
         this.plateOrigin = plateOrigin;
         this.fireChangeEvent();
      }
   }

   public Point getPlateOrigin() {
      return this.plateOrigin;
   }

   public void setDocumentSize(Dimension documentSize) {
      if (!ObjectUtilities.equals(this.documentSize, documentSize)) {
         this.documentSize = documentSize;
         this.fireChangeEvent();
      }
   }

   public Dimension getDocumentSize() {
      return this.documentSize;
   }

   public void setShowMouseLocation(boolean showMouseLocation) {
      if (this.showMouseLocation != showMouseLocation) {
         this.showMouseLocation = showMouseLocation;
         this.fireChangeEvent();
      }
   }

   public boolean isShowMouseLocation() {
      return this.showMouseLocation;
   }

   public boolean isRulerVisible(LayoutDirection direction) {
      return direction == LayoutDirection.HORIZONTAL ? this.horizontalRulerVisible : this.verticalRulerVisible;
   }
}
