package net.disy.commons.swing.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.InvocationTargetException;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractSpinnerModel;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.IBooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.geometry.SmartRectangle;
import net.disy.commons.swing.layout.util.LayoutUtilities;

public class GuiUtilities {
   public static final String ENABLED_PROPERTY_NAME = "enabled";

   protected GuiUtilities() {
      throw new UnreachableCodeReachedException();
   }

   @Deprecated
   public static final Window getWindowForComponent(EventObject event) {
      return getWindowFor(event);
   }

   public static final Window getWindowFor(EventObject event) {
      if (event == null) {
         return JOptionPane.getRootFrame();
      } else {
         Object object = event.getSource();
         return object instanceof Component ? getWindowFor((Component)object) : JOptionPane.getRootFrame();
      }
   }

   @Deprecated
   public static final Window getWindowForComponent(Component component) {
      return getWindowFor(component);
   }

   public static final Window getWindowFor(Component component) {
      if (component == null) {
         return JOptionPane.getRootFrame();
      } else if (component instanceof JPopupMenu) {
         return getWindowFor(((JPopupMenu)component).getInvoker());
      } else {
         return component instanceof Window ? (Window)component : getWindowFor(component.getParent());
      }
   }

   public static final void centerToComponent(Window window, Component component) {
      Window parentWindow = getWindowFor(component);
      if (parentWindow != null && parentWindow.isVisible()) {
         SmartRectangle parentBounds = getSizeOnScreen(parentWindow);
         centerToPoint(window, parentBounds.getCenter());
      } else {
         centerOnScreen(window);
      }
   }

   private static SmartRectangle getSizeOnScreen(Component component) {
      return new SmartRectangle(component.getLocationOnScreen(), component.getSize());
   }

   public static void placeRelativeToOwner(Window window, RelativePosition position) {
      position.place(window);
      assureIsOnScreen(window);
   }

   public static final void centerOnScreen(Window window) {
      Point screenCenter = new SmartRectangle(getScreenBounds(window)).getCenter();
      centerToPoint(window, screenCenter);
   }

   public static final void show(Window window) {
      centerToParent(window);
      window.setVisible(true);
   }

   public static final Dimension getScreenSize(Window window) {
      return getScreenBounds(window).getSize();
   }

   public static Rectangle getScreenBounds(Component component) {
      return component.getGraphicsConfiguration().getBounds();
   }

   public static final void centerToPoint(Window window, Point center) {
      Dimension size = window.getSize();
      int x = center.x - size.width / 2;
      int y = center.y - size.height / 2;
      x = x < 0 ? 0 : x;
      y = y < 0 ? 0 : y;
      window.setLocation(x, y);
   }

   public static final void centerToParent(Window window) {
      centerToComponent(window, window.getParent());
   }

   public static final JDialog createDialog(Component parentComponent, String title) {
      JDialog dialog = createRawDialogForParentComponent(parentComponent);
      dialog.setTitle(title);
      accountForScreenSize(dialog);
      return dialog;
   }

   private static JDialog createRawDialogForParentComponent(Component parentComponent) {
      Window window = getWindowFor(parentComponent);
      if (window instanceof Frame) {
         return new JDialog((Frame)window);
      } else {
         return window instanceof Dialog ? new JDialog((Dialog)window) : new JDialog();
      }
   }

   public static final void accountForScreenSize(Window window) {
      window.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            GuiUtilities.assureIsOnScreen((Window)e.getSource());
         }
      });
   }

   public static final void assureIsOnScreen(Window window) {
      assureIsOnScreen(window, calculateScreenBounds(window));
   }

   public static final void assureIsOnScreen(Window window, Rectangle screenBounds) {
      if (!(window instanceof Frame) || ((Frame)window).getExtendedState() != 6) {
         window.setBounds(calculateOnScreenBounds(window.getBounds(), screenBounds));
         window.validate();
      }
   }

   public static final Rectangle calculateOnScreenBounds(Rectangle componentBounds, Rectangle screenBounds) {
      Rectangle onScreenBounds = new Rectangle(componentBounds);
      if (onScreenBounds.height > screenBounds.height) {
         onScreenBounds.height = screenBounds.height;
      }

      if (onScreenBounds.width > screenBounds.width) {
         onScreenBounds.width = screenBounds.width;
      }

      onScreenBounds.setLocation(calculateScreenFittingLocation(screenBounds, onScreenBounds));
      return onScreenBounds;
   }

   public static void autoScrollOnMouseDrag(final JComponent component) {
      component.addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent evt) {
            component.scrollRectToVisible(new Rectangle(evt.getX(), evt.getY(), 1, 1));
         }
      });
   }

   public static void autoScrollHeaderOnMouseDrag(final JTable table) {
      table.getTableHeader().addMouseMotionListener(new MouseMotionAdapter() {
         @Override
         public void mouseDragged(MouseEvent evt) {
            table.scrollRectToVisible(new Rectangle(evt.getX(), (int)table.getVisibleRect().getY(), 1, 1));
         }
      });
   }

   public static void stopCellEditing(JTable table) {
      TableCellEditor cellEditor = table.getCellEditor();
      if (cellEditor != null) {
         cellEditor.stopCellEditing();
      }
   }

   public static void showFitToScreen(JPopupMenu popup, JComponent invoker, int x, int y) {
      popup.show(invoker, x, y);
      Point point = popup.getLocationOnScreen();
      Dimension size = popup.getSize();
      Rectangle oldRect = new Rectangle(point, size);
      Point newLocation = calculateScreenFittingLocation(invoker, oldRect);
      if (!point.equals(newLocation)) {
         Window window = SwingUtilities.getWindowAncestor(popup);
         if (window != null) {
            window.setLocation(newLocation.x, newLocation.y);
         }
      }
   }

   public static Point calculateScreenFittingLocation(Component screenComponent, Rectangle rectangle) {
      Rectangle screenBounds = calculateScreenBounds(screenComponent);
      return calculateScreenFittingLocation(screenBounds, rectangle);
   }

   public static Point calculateScreenFittingLocation(Rectangle screenBounds, Rectangle rectangle) {
      int x = (int)Math.max(screenBounds.x, Math.min(screenBounds.getMaxX() - (double)rectangle.width, rectangle.x));
      int y = (int)Math.max(screenBounds.y, Math.min(screenBounds.getMaxY() - (double)rectangle.height, rectangle.y));
      return new Point(x, y);
   }

   @Deprecated
   public static Rectangle calculateScreenBounds(Component screenComponent, Toolkit toolkit) {
      return calculateScreenBounds(screenComponent.getGraphicsConfiguration(), toolkit);
   }

   public static Rectangle calculateScreenBounds(Component anyComponentOnScreen) {
      return calculateScreenBounds(anyComponentOnScreen.getGraphicsConfiguration(), anyComponentOnScreen.getToolkit());
   }

   public static Rectangle calculateScreenBounds(GraphicsConfiguration graphicsConfiguration, Toolkit toolkit) {
      Rectangle overallScreenBounds = graphicsConfiguration.getBounds();
      Insets screenInsets = toolkit.getScreenInsets(graphicsConfiguration);
      return new Rectangle(
         overallScreenBounds.x + screenInsets.left,
         overallScreenBounds.y + screenInsets.top,
         overallScreenBounds.width - screenInsets.left - screenInsets.right,
         overallScreenBounds.height - screenInsets.top - screenInsets.bottom
      );
   }

   public static void invokeLaterIfNecessary(Runnable runnable) {
      if (SwingUtilities.isEventDispatchThread()) {
         runnable.run();
      } else {
         SwingUtilities.invokeLater(runnable);
      }
   }

   public static void invokeOnEventDispatchThread(Runnable runnable) {
      if (SwingUtilities.isEventDispatchThread()) {
         runnable.run();
      } else {
         try {
            SwingUtilities.invokeAndWait(runnable);
         } catch (InterruptedException var2) {
            throw new RuntimeException(var2);
         } catch (InvocationTargetException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public static void clearChangeListeners(AbstractSpinnerModel model) {
      ChangeListener[] changeListeners = model.getChangeListeners();

      for (int index = 0; index < changeListeners.length; index++) {
         model.removeChangeListener(changeListeners[index]);
      }
   }

   public static void obeyEnabledModel(Component component, IBooleanModel enabledModel) {
      obeyEnabledModel(new ComponentEnableableAdapter(component), enabledModel);
   }

   public static void obeyEnabledModel(final IEnableable enableable, final IBooleanModel enabledModel) {
      enabledModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            enableable.setEnabled(enabledModel.getValue());
         }
      });
      enableable.setEnabled(enabledModel.getValue());
   }

   public static Set<Container> setContainerEnabled(Container control, boolean enabled) {
      return setContainerEnabled(control, enabled, new HashSet<>());
   }

   public static Set<Container> setContainerEnabled(Container c, boolean enable, Set<Container> components) {
      HashSet<Container> enabledComps = new HashSet<>();
      if (!components.contains(c)) {
         if (c.isEnabled() == enable) {
            enabledComps.add(c);
         } else {
            c.setEnabled(enable);
         }
      }

      return setSubComponentsEnabled(c, enable, components, enabledComps);
   }

   private static Set<Container> setSubComponentsEnabled(Container c, boolean enable, Set<Container> components, Set<Container> enabledComps) {
      Component[] comps = c.getComponents();

      for (int i = 0; i < comps.length; i++) {
         if (comps[i] instanceof Container) {
            Set<Container> enabledContainers = setContainerEnabled((Container)comps[i], enable, components);
            enabledComps.addAll(enabledContainers);
         } else if (!components.contains(comps[i])) {
            if (comps[i].isEnabled() == enable) {
               enabledComps.add(c);
            } else {
               comps[i].setEnabled(enable);
            }
         }
      }

      return enabledComps;
   }

   public static void repack(Window window) {
      if (!window.isVisible()) {
         window.pack();
      } else {
         Rectangle oldBounds = window.getBounds();
         Dimension newSize = window.getPreferredSize();
         Point newLocation = new Point(oldBounds.x + (oldBounds.width - newSize.width) / 2, oldBounds.y + (oldBounds.height - newSize.height) / 2);
         Rectangle newBounds = new Rectangle(newLocation, newSize);
         newBounds = calculateOnScreenBounds(newBounds, calculateScreenBounds(window));
         window.setBounds(newBounds);
         window.validate();
      }
   }

   public static void stopCellEditing(Container container) {
      if (container instanceof JTable) {
         stopCellEditing((JTable)container);
      } else {
         for (Component component : container.getComponents()) {
            if (component instanceof Container) {
               stopCellEditing((Container)component);
            }
         }
      }
   }

   public static JComponent addEmptyBorder(JComponent content) {
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(content, "Center");
      panel.setBorder(LayoutUtilities.getDefaultEmptyBorder());
      return panel;
   }

   public static void addClickActionTo(final Component component, final Action action) {
      component.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            if (component.isEnabled() && action.isEnabled()) {
               action.actionPerformed(new ActionEvent(e, e.getID(), null));
            }
         }
      });
   }

   @Deprecated
   public static void ensureEventDispatchThread() {
      EventDispatchThreadUtilities.ensureIsEventDispatchThread();
   }

   public static boolean isContainedInActiveWindow(Component component) {
      Window window = getWindowFor(component);
      return window == null ? true : window.isActive();
   }

   public static boolean containsFocusOwner(Component component) {
      if (component.isFocusOwner()) {
         return true;
      } else if (!(component instanceof Container)) {
         return false;
      } else {
         Container container = (Container)component;

         for (int i = 0; i < container.getComponentCount(); i++) {
            Component child = container.getComponent(i);
            if (containsFocusOwner(child)) {
               return true;
            }
         }

         return false;
      }
   }
}
