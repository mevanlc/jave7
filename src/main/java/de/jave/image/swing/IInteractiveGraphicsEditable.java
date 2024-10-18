package de.jave.image.swing;

import java.awt.Cursor;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public interface IInteractiveGraphicsEditable {
   void addMouseListener(MouseListener var1);

   void addMouseMotionListener(MouseMotionListener var1);

   void addKeyListener(KeyListener var1);

   void setXorPainter(IXorPainter var1);

   void setCursor(Cursor var1);
}
