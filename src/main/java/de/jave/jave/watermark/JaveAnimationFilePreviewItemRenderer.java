package de.jave.jave.browser;

import de.jave.core.io.FileSelectionModel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.SystemColor;

public class JaveAnimationFilePreviewItemRenderer extends JaveFilePreviewItemRenderer {
   @Override
   protected void paintBackground(JaveFilePreviewItem data, Graphics g, Rectangle labelBounds, Rectangle diaBounds, FileSelectionModel selectionModel) {
      super.paintBackground(data, g, labelBounds, diaBounds, selectionModel);
      g.setColor(SystemColor.controlShadow);
      g.fillRect(diaBounds.x + 2, diaBounds.y + 2, diaBounds.width - 4, diaBounds.height - 4);
      if (selectionModel.isSelected(data.getFile())) {
         g.setColor(SystemColor.textHighlight);
      } else {
         g.setColor(SystemColor.control);
      }

      for (int i = 0; i < diaBounds.width / 12; i++) {
         g.fillRoundRect(diaBounds.x + 4 + i * 12, diaBounds.y + 5, 7, 8, 3, 3);
         g.fillRoundRect(diaBounds.x + 4 + i * 12, diaBounds.y + diaBounds.height - 12, 7, 8, 3, 3);
      }
   }
}
