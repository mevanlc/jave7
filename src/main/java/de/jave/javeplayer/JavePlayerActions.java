package de.jave.javeplayer;

import java.awt.Component;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.action.SmartToggleAction;

public class JavePlayerActions {
   private final SmartAction zoomOutAction;
   private final SmartAction zoomInAction;
   private final SmartAction stopAction;
   private final SmartAction pauseAction;
   private final SmartAction playAction;
   private final SmartAction forwardAction;
   private final SmartAction backwardAction;
   private final SmartToggleAction toggleLoopAction;

   public JavePlayerActions(final JavePlayer player, BooleanModel loop) {
      this.backwardAction = new SmartAction(JavePlayerResources.REVERSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doPrev();
         }
      };
      this.backwardAction.setToolTipText("Backward");
      this.forwardAction = new SmartAction(JavePlayerResources.FORWARD_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doNext();
         }
      };
      this.forwardAction.setToolTipText("Forward");
      this.playAction = new SmartAction(JavePlayerResources.PLAY_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doPlay();
         }
      };
      this.playAction.setToolTipText("Play");
      this.pauseAction = new SmartAction(JavePlayerResources.PAUSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doPause();
         }
      };
      this.pauseAction.setToolTipText("Pause");
      this.stopAction = new SmartAction(JavePlayerResources.STOP_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doStop();
         }
      };
      this.stopAction.setToolTipText("Stop");
      this.zoomInAction = new SmartAction(JavePlayerResources.ZOOM_PLUS_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doZoomIn();
         }
      };
      this.zoomInAction.setToolTipText("Zoom in");
      this.zoomOutAction = new SmartAction(JavePlayerResources.ZOOM_MINUS_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            player.doZoomOut();
         }
      };
      this.zoomOutAction.setToolTipText("Zoom out");
      this.toggleLoopAction = new SmartToggleAction(loop, JavePlayerResources.LOOP_ICON);
      this.toggleLoopAction.setToolTipText("Loop animation");
   }

   public SmartAction getNextAction() {
      return this.forwardAction;
   }

   public SmartAction getPauseAction() {
      return this.pauseAction;
   }

   public SmartAction getPlayAction() {
      return this.playAction;
   }

   public SmartAction getPrevAction() {
      return this.backwardAction;
   }

   public SmartAction getStopAction() {
      return this.stopAction;
   }

   public SmartAction getZoomInAction() {
      return this.zoomInAction;
   }

   public SmartAction getZoomOutAction() {
      return this.zoomOutAction;
   }

   public SmartToggleAction getToggleLoopAction() {
      return this.toggleLoopAction;
   }

   public void setNavigationEnabled(boolean enabled) {
      this.stopAction.setEnabled(enabled);
      this.pauseAction.setEnabled(enabled);
      this.playAction.setEnabled(enabled);
      this.forwardAction.setEnabled(enabled);
      this.backwardAction.setEnabled(enabled);
      this.toggleLoopAction.setEnabled(enabled);
   }
}
