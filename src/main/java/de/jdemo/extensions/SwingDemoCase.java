package de.jdemo.extensions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;

import de.jdemo.util.GuiUtilities;

/**
 * Abstract superclass for all demos demonstrating Swing (JFC) components. 
 * @author Markus Gebhard
 */
public abstract class SwingDemoCase extends AwtDemoCase {
  private String preferredLookAndFeelClassName;
  private String configuredLookAndFeelClassName;

  public SwingDemoCase() {
    super();
  }

  public SwingDemoCase(String name) {
    super(name);
  }

  public void executeSetUp() throws Exception {
    initializeLookAndFeel();
    super.executeSetUp();
  }

  /** Convenience method for showing any kind of {@link javax.swing.JComponent} object. The frame
   * provided to this object will also be registred as demo window for this democase. */
  protected void show(final JComponent component) {
    assertNotNull("The component to show must not be null.", component);
    getRunnable().showCalled();
    Runnable runnable = new Runnable() {
      public void run() {
        JFrame frame = createJFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(component, BorderLayout.CENTER);
        frame.pack();
        GuiUtilities.centerOnScreen(frame);
        frame.setVisible(true);
      }
    };
    runOnEventDispatchThread(runnable);
  }

  protected void show(final JPopupMenu popup) {
    assertNotNull("The popup menu to show must not be null.", popup);
    getRunnable().showCalled();

    Runnable runnable = new Runnable() {
      public void run() {
        JFrame frame = createJFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(popup, BorderLayout.CENTER);
        frame.pack();
        GuiUtilities.centerOnScreen(frame);
        frame.setVisible(true);
        popup.show(frame.getContentPane(), 0, 0);
      }
    };
    runOnEventDispatchThread(runnable);
  }

  /** Convenience method for showing a {@link java.awt.Image} object.
   * The frame containing the image will also be registered as demo window for this democase. */
  protected void show(Image image) {
    assertNotNull("The image to show must not be null.", image);
    show(createIcon(image));
  }

  /** Convenience method for showing an array of {@link java.awt.Image} objects.
   * The frame containing the images will also be registered as demo window for this democase.
   * The specified layout manager will be used to lay out the images. */
  protected void show(Image[] images, LayoutManager layoutManager) {
    assertNotNull("The images to show must not be null.", images);
    assertNotNull("The layout manager must not be null.", layoutManager);
    Component[] components = new Component[images.length];
    for (int i = 0; i < images.length; ++i) {
      components[i] = createComponent(images[i]);
    }
    show(components, layoutManager);
  }

  /** Convenience method for showing a {@link javax.swing.Icon} object.
   * The frame containing the icon will also be registered as demo window for this democase. */
  protected void show(Icon icon) {
    assertNotNull("The icon to show must not be null.", icon);
    show(createComponent(icon));
  }

  /** Convenience method for showing an array of {@link javax.swing.Icon} objects.
   * The frame containing the images will also be registered as demo window for this democase.
   * The specified layout manager will be used to lay out the icons. */
  protected void show(Icon[] icons, LayoutManager layoutManager) {
    assertNotNull("The icons to show must not be null.", icons);
    assertNotNull("The layout manager must not be null.", layoutManager);
    Component[] components = new Component[icons.length];
    for (int i = 0; i < icons.length; ++i) {
      components[i] = createComponent(icons[i]);
    }
    show(components, layoutManager);
  }

  /** Shows the given text in a JTextPane using a fixed width font.
   * @see #showProportional(CharSequence) */
  protected void show(CharSequence text) {
    assertNotNull("The text to show must not be null.", text);
    show(new SwingTextPanel(text, GuiUtilities.DEFAULT_FIXED_WIDTH_FONT).getContent());
  }

  /** Shows the given text in a TextArea using a proportional font.
   * @see #show(CharSequence) */
  protected void showProportional(CharSequence text) {
    assertNotNull("The text to show must not be null.", text);
    show(new SwingTextPanel(text, GuiUtilities.DEFAULT_PROPORTIONAL_FONT).getContent());
  }

  private static Component createComponent(Image image) {
    return createComponent(createIcon(image));
  }

  private static Component createComponent(final Icon icon) {
    return new IconComponent(icon);
  }

  private static Icon createIcon(Image image) {
    return new ImageIcon(image);
  }

  protected Component createParentComponent() {
    return createJFrame();
  }

  /** Convenience method for creating a new {@link javax.swing.JFrame} object that can be used for demo
   * implementations.
   * @see #show(Image)
   * @see #show(Component) */
  protected JFrame createJFrame() {
    JFrame frame = new JFrame(getFrameTitle());
    frame.setIconImage(getFrameIconImage());
    frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    registerDemoWindow(frame);
    return frame;
  }

  private void initializeLookAndFeel() {
    //Defaults to the cross platform LaF 
    String lookAndFeelClassName = UIManager.getCrossPlatformLookAndFeelClassName();
    if (configuredLookAndFeelClassName != null) {
      lookAndFeelClassName = configuredLookAndFeelClassName;
    }
    else if (preferredLookAndFeelClassName != null) {
      lookAndFeelClassName = preferredLookAndFeelClassName;
    }

    try {
      UIManager.setLookAndFeel(lookAndFeelClassName);
    }
    catch (Exception e) {
      throw new RuntimeException("Error setting look&feel '" + lookAndFeelClassName + "'", e);
    }
  }

  /** Sets the icon that will be used for the frame when showing this democase. */
  public void setFrameIcon(Icon icon) {
    if (icon == null) {
      setFrameIconImage(null);
      return;
    }
    BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    icon.paintIcon(JOptionPane.getRootFrame(), image.getGraphics(), 0, 0);
    setFrameIconImage(image);
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelSystem()}*/
  @Deprecated
  public void setSystemLookAndFeel() {
    setPreferredLookAndFeelSystem();
  }

  /** Sets the preferred LookAndFeel for this demo to the system's native LookAndFeel.
   * Note that the LookAndFeel might be overridden by the demo runner. */
  protected void setPreferredLookAndFeelSystem() {
    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelCrossPlatform()}*/
  @Deprecated
  public void setCrossPlatformLookAndFeel() {
    setPreferredLookAndFeelCrossPlatform();
  }

  /** Sets the preferred LookAndFeel for this demo to the Java Cross Platform LookAndFeel.
   * Note that the LookAndFeel might be overridden by the demo runner.
   */
  protected void setPreferredLookAndFeelCrossPlatform() {
    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelMotif()}*/
  @Deprecated
  public void setMotifLookAndFeel() {
    setPreferredLookAndFeelMotif();
  }

  /** Sets the preferred LookAndFeel for this demo to the Motif LookAndFeel
   * (<code>com.sun.java.swing.plaf.motif.MotifLookAndFeel</code>). 
   * Note that this LookAndFeel might not be supported on some systems. 
   * Also note that the LookAndFeel might be overridden by the demo runner.
   */
  protected void setPreferredLookAndFeelMotif() {
    setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelWindows()}*/
  @Deprecated
  public void setWindowsLookAndFeel() {
    setPreferredLookAndFeelWindows();
  }

  /** Sets the preferred LookAndFeel for this demo to the Windows LookAndFeel
   * (<code>com.sun.java.swing.plaf.windows.WindowsLookAndFeel</code>).
   * Note that this LookAndFeel might not be supported on some systems. 
   * Also note that the LookAndFeel might be overridden by the demo runner.
   */
  protected void setPreferredLookAndFeelWindows() {
    setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelMac()}*/
  @Deprecated
  public void setMacLookAndFeel() {
    setPreferredLookAndFeelMac();
  }

  /** Sets the preferred LookAndFeel for this demo to the Mac LookAndFeel
   * (<code>com.sun.java.swing.plaf.mac.MacLookAndFeel</code>).
   * Note that this LookAndFeel might not be supported on some systems. 
   * Also note that the LookAndFeel might be overridden by the demo runner.
   */
  protected void setPreferredLookAndFeelMac() {
    setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeelGtk()}*/
  @Deprecated
  public void setGtkLookAndFeel() {
    setPreferredLookAndFeelGtk();
  }

  /** Sets the preferred LookAndFeel for this demo to the Gtk LookAndFeel
   * (<code>com.sun.java.swing.plaf.gtk.GTKLookAndFeel</code>). 
   * Note that this LookAndFeel might not be supported on some systems. 
   */
  protected void setPreferredLookAndFeelGtk() {
    setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
  }

  /** @deprecated As of Jul 21, 2004 (Markus Gebhard), replaced by
   * {@link #setPreferredLookAndFeel(String)}*/
  @Deprecated
  public void setLookAndFeel(String lookAndFeelClassName) {
    setPreferredLookAndFeel(lookAndFeelClassName);
  }

  /** Sets the preferred LookAndFeel for this demo to the one represented by the given
   * LookAndFeel class name.
   * Note that the LookAndFeel might be overridden by the demo runner.
   */
  protected void setPreferredLookAndFeel(String lookAndFeelClassName) {
    this.preferredLookAndFeelClassName = lookAndFeelClassName;
    initializeLookAndFeel();
  }

  /** Installs the given LookAndFeel if it is not yet installed. Does nothing otherwise. */
  public static void installLookAndFeel(String name, String lookAndFeelClassName) {
    if (isInstalledLookAndFeel(name)) {
      return;
    }
    UIManager.installLookAndFeel(name, lookAndFeelClassName);
  }

  public static boolean isInstalledLookAndFeel(String name) {
    LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
    for (int i = 0; i < infos.length; ++i) {
      if (infos[i].getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /** Sets the configured LookAndFeel class name, that shall be used when showing Swing components.
   * This method should not be called from within a demo implementation.
   * Instead it might be called by a demo runner (e.g. a screen capture) to superceede the preferred LookAndFeel
   * for this demo. */
  public void setConfiguredLookAndFeelClassName(String configuredLookAndFeelClassName) {
    this.configuredLookAndFeelClassName = configuredLookAndFeelClassName;
  }

  public String getConfiguredLookAndFeelClassName() {
    return configuredLookAndFeelClassName;
  }
}