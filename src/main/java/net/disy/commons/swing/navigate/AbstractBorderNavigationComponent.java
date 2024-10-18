package net.disy.commons.swing.navigate;

import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;

public abstract class AbstractBorderNavigationComponent extends JComponent {
   private final NavigationMouseLocationModel mouseLocationModel = new NavigationMouseLocationModel();

   public AbstractBorderNavigationComponent() {
      this(true);
   }

   public AbstractBorderNavigationComponent(boolean navigationEnabled) {
      if (navigationEnabled) {
         MouseAdapter mouseListener = new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
               NavigationDirection direction = AbstractBorderNavigationComponent.this.getNavigationDirection(e);
               AbstractBorderNavigationComponent.this.mouseLocationModel.setValue(direction);
            }

            @Override
            public void mouseExited(MouseEvent e) {
               AbstractBorderNavigationComponent.this.mouseLocationModel.setValue(null);
            }
         };
         this.addMouseMotionListener(mouseListener);
         this.addMouseListener(mouseListener);
         ObjectModel<NavigationDirection> delayedModel = DelayedObjectModelFactory.createDelayedModel(this.mouseLocationModel, 100L);
         delayedModel.addChangeListener(
            new IChangeListener() {
               @Override
               public void stateChanged() {
                  final NavigationDirection newNavigationDirection = AbstractBorderNavigationComponent.this.mouseLocationModel.getValue();
                  if (newNavigationDirection != null) {
                     final INavigationHandler handler = new INavigationHandler() {
                        @Override
                        public void navigateTo(NavigationDirection navigationDirection) {
                           AbstractBorderNavigationComponent.this.navigateTo(navigationDirection);
                        }
                     };
                     SwingUtilities.invokeLater(
                        new Runnable() {
                           @Override
                           public void run() {
                              BorderNavigationGlassPaneAttacher.attachNavigationGlassPane(
                                 AbstractBorderNavigationComponent.this, 20, 4, newNavigationDirection, handler
                              );
                           }
                        }
                     );
                  }
               }
            }
         );
      }
   }

   private NavigationDirection getNavigationDirection(MouseEvent event) {
      for (NavigationDirection direction : NavigationDirection.values()) {
         Shape shape = BorderNavigationGlassPaneAttacher.createNavigationArea(this, 20, 4, direction);
         if (shape.contains(event.getPoint())) {
            return direction;
         }
      }

      return null;
   }

   protected abstract void navigateTo(NavigationDirection var1);
}
