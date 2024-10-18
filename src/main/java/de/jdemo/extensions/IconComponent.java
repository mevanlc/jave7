package de.jdemo.extensions;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 * Instances of this class wrap an {@link javax.swing.Icon} as component and by implementing 
 * {@link de.jdemo.extensions.IDirectImageCapturable} provide direct access to the painted image.
 */
public class IconComponent extends Component implements IDirectImageCapturable {
  private Icon icon;

  public IconComponent(Icon icon) {
    this.icon = icon;
  }

  public Dimension getPreferredSize() {
    return getIconSize();
  }

  private Dimension getIconSize() {
    return new Dimension(icon.getIconWidth(), icon.getIconHeight());
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  public Dimension getMaximumSize() {
    return getPreferredSize();
  }

  public void paint(Graphics g) {
    g.setColor(Color.white);
    g.fillRect(0, 0, getIconSize().width, getIconSize().height);
    icon.paintIcon(this, g, 0, 0);
  }

  public BufferedImage getBufferedImage() {
    Dimension size = getIconSize();
    //We do not support transparency, since it does not work consistently with output formats
    BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
    //    BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = (Graphics2D) image.getGraphics();
    //    graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1.0f));
    //    graphics.fillRect(0, 0, size.width, size.height);
    //    graphics.setPaintMode();
    paint(graphics);
    graphics.dispose();
    return image;
  }
}