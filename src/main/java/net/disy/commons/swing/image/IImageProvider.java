package net.disy.commons.swing.image;

import java.awt.Image;
import javax.swing.Icon;

public interface IImageProvider {
   Image getImage(String var1);

   Image getAnimatedImage(String var1);

   Icon getImageIcon(String var1);

   Icon getAnimatedImageIcon(String var1);
}
