package de.jave.asciimation.editor;

import de.jave.asciimation.action.NavigateNextAction;
import de.jave.asciimation.action.NavigatePreviousAction;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class AnimationEditorAdditionalControlsPanel {
   private final JPanel content;

   public AnimationEditorAdditionalControlsPanel(AnimationEditorModel model) {
      JPanel pStrip = new JPanel(new BorderLayout());
      pStrip.add(new JButton(new NavigatePreviousAction(model)), "West");
      pStrip.add(new JButton(new NavigateNextAction(model)), "East");
      pStrip.add(new AnimationThumbnailsPanel(model).getContent(), "Center");
      pStrip.add(new FilmStripBorder(model), "North");
      pStrip.add(new FilmStripBorder(model), "South");
      FrameListPanel frameListPanel = new FrameListPanel(model);
      AnimationScrollbar animationScrollbar = new AnimationScrollbar(model);
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(pStrip, "Center");
      mainPanel.add(animationScrollbar.getContent(), "South");
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(mainPanel, "Center");
      panel.add(frameListPanel.getContent(), "West");
      this.content = panel;
   }

   public JComponent getContent() {
      return this.content;
   }
}
