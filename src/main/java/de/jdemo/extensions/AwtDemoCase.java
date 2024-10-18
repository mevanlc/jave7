package de.jdemo.extensions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import de.jdemo.util.GuiUtilities;

/**
 * Abstract superclass for all demos demonstrating AWT components. 
 * @author Markus Gebhard
 */
public abstract class AwtDemoCase extends GuiDemoCase {
  private Image frameIconImage;
  private String frameTitle;

  public AwtDemoCase() {
    super();
  }

  public AwtDemoCase(String name) {
    super(name);
  }

  /** Convenience method for showing any kind of {@link java.awt.Component} object. The frame
   * provided to this object will also be registred as demo window for this GuiDemoCase. */
  protected void show(Component component) {
    assertNotNull("The component to show must not be null.", component);
    Frame f = createFrame();
    f.setLayout(new BorderLayout());
    f.add(component, BorderLayout.CENTER);
    show(f);
  }

  /** Convenience method for showing an array of {@link java.awt.Component} objects.
   * The frame containing the components will also be registered as demo window for this democase.
   * The specified layout manager will be used to lay out the components. */
  protected void show(Component[] components, LayoutManager layoutManager) {
    assertNotNull("The components to show must not be null.", components);
    assertNotNull("The layoutmanager must not be null.", layoutManager);
    Panel panel = new Panel(layoutManager);
    for (int i = 0; i < components.length; ++i) {
      panel.add(components[i]);
    }
    show(panel);
  }

  /** Convenience method for showing any kind of {@link java.awt.Window} object. The given object
   * will also be registred as demo window for this democase (if not being registered already).
   * Note that the window will be packed, centered on the screen and made non-modal if being a dialog.
   * @see #showAsIs(Window) */
  protected void show(final Window window) {
    assertNotNull("The window to show must not be null.", window);
    Runnable runnable = new Runnable() {
      public void run() {
        window.pack();
        GuiUtilities.centerOnScreen(window);
        showAsIs(window);
      }
    };
    runOnEventDispatchThread(runnable);
  }

  protected void runOnEventDispatchThread(Runnable runnable) {
    if (SwingUtilities.isEventDispatchThread()) {
      runnable.run();
    }
    else {
      try {
        SwingUtilities.invokeAndWait(runnable);
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      catch (InvocationTargetException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /** Shows the given {@link java.awt.Window} object without modifying its size or position.
   * The given object will also be registred as demo window for this democase.
   * Note that the window will be made non-modal if being a dialog.
   * @see #show(Window) */
  protected void showAsIs(final Window window) {
    assertNotNull("The window to show must not be null.", window);
    getRunnable().showCalled();
    Runnable runnable = new Runnable() {
      public void run() {
        if (window instanceof Dialog) {
          ((Dialog) window).setModal(false);
        }
        registerDemoWindow(window);
        window.show();
        window.toFront();
      }
    };
    runOnEventDispatchThread(runnable);
  }

  /** Convenience method for creating a new {@link java.awt.Frame} object that can be used for demo
   * implementations. The frame is automatically registered as demo window and decorated
   * with the necessary window listener, icon, title, etc.
   * @see #show(Window)
   * @see #show(Component) */
  protected Frame createFrame() {
    Frame frame = new Frame(getFrameTitle());
    frame.setIconImage(getFrameIconImage());
    registerDemoWindow(frame);
    return frame;
  }

  protected Component createParentComponent() {
    return createFrame();
  }

  protected String getFrameTitle() {
    return frameTitle != null ? frameTitle : getName();
  }

  /** Sets the title for the frame being created by this demo case. Setting it to <code>null</code>  (default)
   * will set the title to the name of the demo.
   * @see #createFrame() */
  protected void setFrameTitle(String frameTitle) {
    this.frameTitle = frameTitle;
  }

  protected Image getFrameIconImage() {
    return frameIconImage;
  }

  /** Sets the image that will be used for the frame icon when showing this demo. */
  protected void setFrameIconImage(Image image) {
    this.frameIconImage = image;
  }

  /** Shows the given text in a TextArea using a fixed width font.
   * @see #showProportional(CharSequence) */
  protected void show(CharSequence text) {
    assertNotNull("The text to show must not be null.", text);
    TextArea textArea = createTextPane(text);
    textArea.setFont(GuiUtilities.DEFAULT_FIXED_WIDTH_FONT);
    show(textArea);
  }

  /** Shows the given text in a TextArea using a proportional font.
   * @see #show(CharSequence) */
  protected void showProportional(CharSequence text) {
    assertNotNull("The text to show must not be null.", text);
    TextArea textArea = createTextPane(text);
    textArea.setFont(GuiUtilities.DEFAULT_PROPORTIONAL_FONT);
    show(textArea);
  }

  private static TextArea createTextPane(CharSequence text) {
    return new TextArea(text.toString(), 10, 60, TextArea.SCROLLBARS_BOTH);
  }
}