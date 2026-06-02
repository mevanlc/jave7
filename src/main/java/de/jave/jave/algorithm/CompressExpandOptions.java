package de.jave.jave.algorithm;

import de.jave.jave.JaveSelection;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class CompressExpandOptions extends JaveAlgorithmOptions {
   private int defaultHeight;
   private int defaultWidth;
   private int maxHeight;
   private int maxWidth;
   private int newWidth;
   private int newHeight;

   public void setDefaultWidth(int defaultWidth) {
      this.defaultWidth = defaultWidth;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setDefaultHeight(int defaultHeight) {
      this.defaultHeight = defaultHeight;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setMaxWidth(int maxWidth) {
      this.maxWidth = maxWidth;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setMaxHeight(int maxHeight) {
      this.maxHeight = maxHeight;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public int getDefaultHeight() {
      return this.defaultHeight;
   }

   public int getDefaultWidth() {
      return this.defaultWidth;
   }

   public int getMaxHeight() {
      return this.maxHeight;
   }

   public int getMaxWidth() {
      return this.maxWidth;
   }

   @Override
   public void adjustTo(JaveSelection sel) {
      this.defaultWidth = sel.getWidth();
      this.newWidth = this.defaultWidth;
      this.defaultHeight = sel.getHeight();
      this.newHeight = this.defaultHeight;
      this.maxWidth = 2 * this.defaultWidth;
      this.maxHeight = 2 * this.defaultHeight;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setNewWidth(int newWidth) {
      this.newWidth = newWidth;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public void setNewHeight(int newHeight) {
      this.newHeight = newHeight;
      this.fireAlgorithmOptionsChangeEvent();
   }

   public int getNewWidth() {
      return this.newWidth;
   }

   public int getNewHeight() {
      return this.newHeight;
   }

   @Override
   public JaveAlgorithmOptionsPanel getPanel(FontModel displayFontModel) {
      return new CompressExpandOptionsPanel(this);
   }
}
