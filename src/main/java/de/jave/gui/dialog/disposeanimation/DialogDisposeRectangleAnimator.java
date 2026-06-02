package de.jave.gui.dialog.disposeanimation;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;

public class DialogDisposeRectangleAnimator {
   private static final int MILLISECONDS_FOR_ANIMATION = 400;

   public static void attachTo(final BooleanModel dialogVisibilityModel, final IDialogDisposeContext context) {
      dialogVisibilityModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (!dialogVisibilityModel.getValue()) {
               Rectangle dialogArea = context.getDialogAreaOnScreen();
               if (dialogArea != null) {
                  Rectangle targetRectangle = context.getTargetAreaOnScreen();
                  if (targetRectangle != null) {
                     JFrame parentFrame = context.getParentFrame();
                     if (parentFrame != null) {
                        Component previousGlassPane = parentFrame.getGlassPane();
                        RectangleGlassPane glassPane = new RectangleGlassPane();
                        parentFrame.setGlassPane(glassPane);
                        glassPane.setVisible(true);
                        Point translate = glassPane.getLocationOnScreen();
                        Rectangle startRectangle = new Rectangle(dialogArea);
                        Rectangle actualTargetRectangle = new Rectangle(targetRectangle);
                        startRectangle.translate(-translate.x, -translate.y);
                        actualTargetRectangle.translate(-translate.x, -translate.y);
                        DialogDisposeRectangleAnimator.startAnimationThread(parentFrame, previousGlassPane, glassPane, startRectangle, actualTargetRectangle);
                     }
                  }
               }
            }
         }
      });
   }

   private static void startAnimationThread(
      final JFrame parentFrame,
      final Component previousGlassPane,
      final RectangleGlassPane glassPane,
      final Rectangle startRectangle,
      final Rectangle actualTargetRectangle
   ) {
      Thread thread = new Thread(new Runnable() {
         @Override
         public void run() {
            RectangleInterpolator rectangleInterpolator = new RectangleInterpolator();
            long startTime = System.currentTimeMillis();

            while (true) {
               long millis = System.currentTimeMillis() - startTime;
               if (millis > 400L) {
                  break;
               }

               double t = (double)millis / 400.0;
               Rectangle interpolated = rectangleInterpolator.interpolate(startRectangle, actualTargetRectangle, t);
               glassPane.setRectangle(interpolated);

               try {
                  Thread.sleep(10L);
               } catch (InterruptedException var10) {
                  break;
               }
            }

            glassPane.setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  parentFrame.setGlassPane(previousGlassPane);
               }
            });
         }
      }, "DialogDisposeRectangleAnimator");
      thread.start();
   }
}
