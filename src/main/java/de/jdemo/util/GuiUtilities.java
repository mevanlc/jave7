package de.jdemo.util;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.util.EventObject;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.JWindow;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * @author Markus Gebhard
 */
public class GuiUtilities {
  public static final Font DEFAULT_FIXED_WIDTH_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 11);
  public static final Font DEFAULT_PROPORTIONAL_FONT = new Font(Font.DIALOG, Font.PLAIN, 11);

  private GuiUtilities() {
    //not instantiable
  }

  public static JDialog createDialog(Component parentComponent) {
    if (parentComponent == null) {
      return new JDialog();
    }
    if (parentComponent instanceof Dialog) {
      return new JDialog((Dialog) parentComponent);

    }
    if (parentComponent instanceof Frame) {
      return new JDialog((Frame) parentComponent);
    }
    return createDialog(parentComponent.getParent());
  }

  public static JWindow createWindow(Component parentComponent) {
    if (parentComponent == null) {
      return new JWindow();
    }
    if (parentComponent instanceof Dialog) {
      return new JWindow((Dialog) parentComponent);

    }
    if (parentComponent instanceof Frame) {
      return new JWindow((Frame) parentComponent);
    }
    return createWindow(parentComponent.getParent());
  }

  public static void setNativeLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      System.out.println("Error setting native LAF: " + e); //$NON-NLS-1$
    }
  }

  public static void expandTreeNode(JTree tree, TreeNode tNode) {
    TreePath tp = new TreePath(((DefaultMutableTreeNode) tNode).getPath());
    tree.expandPath(tp);
    for (int i = 0; i < tNode.getChildCount(); i++) {
      expandTreeNode(tree, tNode.getChildAt(i));
    }
  }

  public static void centerOnScreen(Window window) {
    try {
      Dimension screenSize = window.getToolkit().getScreenSize();
      Dimension windowSize = window.getSize();
      int x = (screenSize.width - windowSize.width) / 2;
      int y = (screenSize.height - windowSize.height) / 2;
      window.setLocation(x, y);
    }
    catch (Exception e) {
      //Unable to get location (IllegalComponentStateException) or unable to set it
      //=> ignore
    }
  }

  public static Window getWindowForComponent(EventObject event) {
    Object object = event.getSource();
    if (object instanceof Component) {
      return getWindowForComponent((Component) object);
    }
    return JOptionPane.getRootFrame();
  }

  public final static Window getWindowForComponent(Component component) {
    if (component == null) {
      return JOptionPane.getRootFrame();
    }
    if (component instanceof JPopupMenu) {
      return getWindowForComponent(((JPopupMenu) component).getInvoker());
    }
    if (component instanceof Window) {
      return (Window) component;
    }
    return getWindowForComponent(component.getParent());
  }
}