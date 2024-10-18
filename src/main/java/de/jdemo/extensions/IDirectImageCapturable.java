package de.jdemo.extensions;

import java.awt.image.BufferedImage;

/**
 * Interface for objects that can be captured to a {@link java.awt.image.BufferedImage} directly.
 * <p>
 * This interface is being used for icon objects or images.
 * @see de.jdemo.extensions.IconComponent
 * 
 * @author Markus Gebhard
 */
public interface IDirectImageCapturable {

  public BufferedImage getBufferedImage();

}