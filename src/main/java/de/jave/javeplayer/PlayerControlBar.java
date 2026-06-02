package de.jave.javeplayer;

import de.jave.lib.gui.GuiUtilities;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.ActionWidgetFactory;
import net.dizzy.commons.swing.action.SmartAction;

public class PlayerControlBar implements IPlayerControlBar {
   private final JComponent content;
   private final JSlider slider;
   private final JavePlayerActions actions;

   public PlayerControlBar(DefaultBoundedRangeModel progressModel, JavePlayer player, BooleanModel loopModel) {
      Ensure.ensureArgumentNotNull(progressModel);
      Ensure.ensureArgumentNotNull(player);
      Ensure.ensureArgumentNotNull(loopModel);
      this.actions = new JavePlayerActions(player, loopModel);
      this.slider = new JSlider(progressModel);
      this.slider.setSnapToTicks(true);
      this.slider.setPreferredSize(new Dimension(120, this.slider.getPreferredSize().height));
      JPanel panel = new JPanel(new FlowLayout(1, 0, 0));
      panel.add(createControlButton(this.actions.getPrevAction()));
      panel.add(createControlButton(this.actions.getPlayAction()));
      panel.add(createControlButton(this.actions.getPauseAction()));
      panel.add(createControlButton(this.actions.getStopAction()));
      panel.add(createControlButton(this.actions.getNextAction()));
      panel.add(Box.createHorizontalStrut(6));
      panel.add(this.slider);
      panel.add(Box.createHorizontalStrut(6));
      JToggleButton button = ActionWidgetFactory.createToggleButton(this.actions.getToggleLoopAction());
      adjustForToolbar(button);
      panel.add(button);
      panel.add(Box.createHorizontalStrut(6));
      panel.add(createControlButton(this.actions.getZoomInAction()));
      panel.add(createControlButton(this.actions.getZoomOutAction()));
      this.content = panel;
   }

   private static void adjustForToolbar(AbstractButton button) {
      button.setPreferredSize(GuiUtilities.TOOLBAR_BUTTON_SIZE);
      button.setFocusPainted(false);
   }

   private static JButton createControlButton(SmartAction action) {
      JButton button = new JButton(action);
      adjustForToolbar(button);
      return button;
   }

   public JComponent getContent() {
      return this.content;
   }

   @Override
   public void setProgressToolTip(String text) {
      this.slider.setToolTipText(text);
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.slider.setEnabled(enabled);
      this.actions.setNavigationEnabled(enabled);
   }
}
