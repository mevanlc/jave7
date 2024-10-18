package de.jave.lib.gui;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MenuComponent;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Window;
import java.util.EventObject;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("removal")
public class GuiUtilities {
   public static final Dimension TOOLBAR_BUTTON_SIZE = new Dimension(22, 21);
   public static final Border DEFAULT_EMPTY_BORDER = new EmptyBorder(4, 4, 4, 4);

   public static void setSystemLookAndFeel() {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception var1) {
         System.out.println("Error setting native LAF: " + var1);
      }
   }

   public static void setJavaLookAndFeel() {
      try {
         UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      } catch (Exception var1) {
         System.out.println("Error setting Java LAF: " + var1);
      }
   }

   public static void setMotifLookAndFeel() {
      try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
      } catch (Exception var1) {
         System.out.println("Error setting Motif LAF: " + var1);
      }
   }

   public static void setWindowsLookAndFeel() {
      try {
         UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
      } catch (Exception var1) {
         System.out.println("Error setting native LAF: " + var1);
      }
   }

   public static final Frame getParentFrame(Component comp) {
      while (comp != null && !(comp instanceof Frame)) {
         comp = comp.getParent();
      }

      return (Frame)comp;
   }

   public static Window getWindowForComponent(EventObject event) {
      return getWindowFor(event.getSource());
   }

   private static Window getWindowFor(Object object) {
      if (object instanceof Component) {
         return getWindowForComponent((Component)object);
      } else {
         return object instanceof MenuComponent ? getWindowFor(((MenuComponent)object).getParent()) : JOptionPane.getRootFrame();
      }
   }

   public static final Window getWindowForComponent(Component component) {
      if (component == null) {
         return JOptionPane.getRootFrame();
      } else if (component instanceof JPopupMenu) {
         return getWindowForComponent(((JPopupMenu)component).getInvoker());
      } else {
         return component instanceof Window ? (Window)component : getWindowForComponent(component.getParent());
      }
   }

   public static void centerToParent(Window window) {
      centerToComponent(window, window.getParent());
   }

   public static void centerToPoint(Window window, Point center) {
      Dimension size = window.getSize();
      int x = center.x - size.width / 2;
      int y = center.y - size.height / 2;
      x = x < 0 ? 0 : x;
      y = y < 0 ? 0 : y;
      window.setLocation(x, y);
   }

   public static void centerToComponent(Window window, Component component) {
      Window parentWindow = getWindowForComponent(component);
      if (parentWindow != null && parentWindow.isVisible()) {
         Dimension parentSize = parentWindow.getSize();
         Point parentLocation = parentWindow.getLocationOnScreen();
         Point parentCenter = new Point(parentLocation.x + parentSize.width / 2, parentLocation.y + parentSize.height / 2);
         centerToPoint(window, parentCenter);
      } else {
         centerOnScreen(window);
      }
   }

   public static final void centerOnScreen(Window window) {
      Dimension screenSize = window.getToolkit().getScreenSize();
      Point center = new Point(screenSize.width / 2, screenSize.height / 2);
      centerToPoint(window, center);
   }

   public static final Applet getParentApplet(Component comp) {
      while (comp != null && !(comp instanceof Applet)) {
         comp = comp.getParent();
      }

      return (Applet)comp;
   }

   public static JDialog createDialog(Component parentComponent, String title) {
      Window parentWindow = getWindowForComponent(parentComponent);
      JDialog dialog = null;
      if (parentWindow instanceof Dialog) {
         dialog = new JDialog((Dialog)parentWindow);
      } else if (parentWindow instanceof Frame) {
         dialog = new JDialog((Frame)parentWindow);
      } else {
         dialog = new JDialog();
      }

      dialog.setTitle(title);
      return dialog;
   }

   public static void setEnabled(TitledBorder border, boolean enabled) {
      if (enabled) {
         border.setTitleColor(SystemColor.textText);
      } else {
         border.setTitleColor(SystemColor.textInactiveText);
      }
   }
}
