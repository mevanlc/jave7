package de.jave.image.gui;

import java.awt.Dimension;
import java.awt.Graphics;

public interface IDisplayableImage {
   Dimension getSize();

   void paint(Graphics var1, int var2, int var3, int var4, int var5);
}
