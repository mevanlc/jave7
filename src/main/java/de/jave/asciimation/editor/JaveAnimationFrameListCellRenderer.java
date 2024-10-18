package de.jave.asciimation.editor;

import de.jave.jave.icon.JaveIcons;
import de.jave.javeplayer.JaveAnimationFrame;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import net.disy.commons.core.util.Ensure;

public class JaveAnimationFrameListCellRenderer extends DefaultListCellRenderer {
   private final AnimationEditorModel model;

   public JaveAnimationFrameListCellRenderer(AnimationEditorModel model) {
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      JaveAnimationFrame frame = (JaveAnimationFrame)value;
      String frameTitle = frame.getTool();
      if (frameTitle == null) {
         frameTitle = "Frame " + (index + 1);
      }

      String text = index + 1 + ": " + frameTitle;
      this.setIcon(
         index == this.model.getCurrentFrameIndexModel().getCurrentFrameIndex()
            ? JaveIcons.ANIMATION_CURRENT_FRAME_ITEM_ICON
            : JaveIcons.ANIMATION_FRAME_ITEM_ICON
      );
      this.setText(text);
      return this;
   }
}
