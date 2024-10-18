package de.jave.gui;

import de.jave.internet.SwingBrowserLauncher;
import de.jave.lib.gui.GuiUtilities;
import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JComponent;

@SuppressWarnings("removal")
public class GHyperlink extends JComponent implements MouseListener {
   private URL url;
   private String label;
   private boolean pressed;
   public static final Color COLOR_LINK = Color.blue;
   public static final Color COLOR_LINK_PRESSED = new Color(0, 0, 160);
   public static final int LEFT = 0;
   public static final int CENTER = 1;
   public static final int RIGHT = 2;
   private int alignment = 0;

   public GHyperlink() {
      this((URL)null, "", 0);
   }

   public GHyperlink(int alignment) {
      this((URL)null, "", alignment);
   }

   public GHyperlink(String url) {
      this(url, 0);
   }

   public GHyperlink(String url, int alignment) {
      this.alignment = alignment;
      this.setURL(url);
      this.addMouseListener(this);
      this.pressed = false;
   }

   public void setURL(String url) {
      if (url != null && url.length() != 0) {
         try {
            this.url = new URL(url);
            this.label = this.url.toExternalForm();
         } catch (MalformedURLException var3) {
            this.label = var3.toString();
         }
      } else {
         this.url = null;
         this.label = "";
      }

      this.invalidate();
      Component parent = this.getParent();
      if (parent != null) {
         parent.validate();
      }

      this.repaint();
   }

   public void setURL(String url, String label) {
      this.label = label;
      if (url != null && url.length() != 0) {
         try {
            this.url = new URL(url);
         } catch (MalformedURLException var4) {
            this.label = var4.toString();
         }
      } else {
         this.url = null;
      }

      this.invalidate();
      Component parent = this.getParent();
      if (parent != null) {
         parent.validate();
      }

      this.repaint();
   }

   public GHyperlink(String url, String label) {
      this(url, label, 0);
   }

   public GHyperlink(String url, String label, int alignment) {
      this.alignment = alignment;
      this.label = label;
      this.setURL(url, label);
      this.addMouseListener(this);
      this.pressed = false;
   }

   public GHyperlink(URL url) {
      this(url, url.toExternalForm());
   }

   public GHyperlink(URL url, int alignment) {
      this(url, url.toExternalForm(), alignment);
   }

   public GHyperlink(URL url, String label) {
      this(url, label, 0);
   }

   public GHyperlink(URL url, String label, int alignment) {
      this.alignment = alignment;
      this.label = label;
      this.url = url;
      this.addMouseListener(this);
      this.pressed = false;
   }

   @Override
   public Dimension getPreferredSize() {
      FontMetrics fm = this.getFontMetrics(this.getFont());
      int sw = fm.stringWidth(this.label);
      return new Dimension(sw + 3, 15);
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   @Override
   protected void paintComponent(Graphics g) {
      if (this.label != null && this.label.length() != 0) {
         if (this.pressed) {
            g.setColor(COLOR_LINK_PRESSED);
         } else {
            g.setColor(COLOR_LINK);
         }

         FontMetrics fm = this.getFontMetrics(this.getFont());
         int sw = fm.stringWidth(this.label);
         int x = 0;
         if (this.alignment == 0) {
            x = 1;
         } else if (this.alignment == 2) {
            Dimension d = this.getSize();
            x = d.width - sw;
         } else if (this.alignment == 1) {
            Dimension d = this.getSize();
            x = (d.width - sw) / 2;
         }

         int y = 11;
         Dimension d = this.getSize();
         y += (d.height - 15 + 1) / 2;
         g.drawString(this.label, x, y);
         g.drawLine(x, y + 1, x + sw - 1, y + 1);
      }
   }

   @Override
   public void mousePressed(MouseEvent evt) {
      if (this.url != null) {
         this.pressed = true;
         this.repaint();
      }
   }

   @Override
   public void mouseReleased(MouseEvent evt) {
      if (this.pressed) {
         this.pressed = false;
         this.repaint();
         Applet applet = GuiUtilities.getParentApplet(this);
         if (applet != null) {
            applet.getAppletContext().showDocument(this.url, "_blank");
         } else {
            try {
               SwingBrowserLauncher.performLaunchBrowser(GuiUtilities.getWindowForComponent(evt), this.url);
            } catch (SecurityException var4) {
               System.err.println(var4);
               return;
            }
         }
      }
   }

   @Override
   public void mouseClicked(MouseEvent evt) {
   }

   @Override
   public void mouseEntered(MouseEvent evt) {
      this.setCursor(Cursor.getPredefinedCursor(12));
   }

   @Override
   public void mouseExited(MouseEvent evt) {
      this.setCursor(Cursor.getDefaultCursor());
      if (this.pressed) {
         this.pressed = false;
         this.repaint();
      }
   }
}
