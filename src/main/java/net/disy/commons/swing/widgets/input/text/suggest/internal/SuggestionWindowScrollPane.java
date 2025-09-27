package net.disy.commons.swing.dialog.input.text.suggest.internal;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public final class SuggestionWindowScrollPane extends JScrollPane {
   private static final int ASSUMED_SCROLLBAR_SIZE = 18;

   public SuggestionWindowScrollPane(JComponent content) {
      super(content, 20, 30);
   }

   @Override
   public final Dimension getMaximumSize() {
      Dimension screenSize = this.getToolkit().getScreenSize();
      return new Dimension(screenSize.width / 3, screenSize.height / 3);
   }

   @Override
   public final Dimension getMinimumSize() {
      return new Dimension(LayoutUtilities.getDpiAdjusted(130), LayoutUtilities.getDpiAdjusted(100));
   }

   @Override
   public final Dimension getPreferredSize() {
      Dimension contentSize = this.getPreferredContentSizeRespectingDefaultBorder();
      Dimension maxSize = this.getMaximumSize();
      if (contentSize.width <= maxSize.width && contentSize.height <= maxSize.height) {
         return this.adjustToMinimumSize(contentSize);
      } else if (contentSize.width > maxSize.width && contentSize.height > maxSize.height) {
         return maxSize;
      } else if (contentSize.width > maxSize.width) {
         int height = Math.min(maxSize.height, contentSize.height + 18);
         return this.adjustToMinimumSize(new Dimension(maxSize.width, height));
      } else {
         int width = Math.min(maxSize.width, contentSize.width + 18);
         return this.adjustToMinimumSize(new Dimension(width, maxSize.height));
      }
   }

   private Dimension adjustToMinimumSize(Dimension size) {
      Dimension minimumSize = this.getMinimumSize();
      int width = Math.max(size.width, minimumSize.width);
      int height = Math.max(size.height, minimumSize.height);
      return new Dimension(width, height);
   }

   private Dimension getPreferredContentSizeRespectingDefaultBorder() {
      Component content = this.getViewport().getView();
      Dimension preferredSize = content.getPreferredSize();
      return new Dimension(preferredSize.width + 6, preferredSize.height + 6);
   }
}
