package de.jave.jave;

import de.jave.jave.algorithm.GeneralAlgorithm;
import de.jave.jave.configuration.MessageDialogConfigurationFileErrorHandler;
import de.jave.jave.filter.Filter;
import de.jave.jave.filter.FilterInitializable;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateConfiguration;
import de.jave.jave.pixelplate.PixelPlateConfigurationInitializable;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.rendering.GlyphRenderer;
import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import net.dizzy.commons.core.util.Ensure;

public class ColorScreenSaver extends Window implements MouseListener, MouseMotionListener, KeyListener, Runnable {
   private final PixelPlateMode mode = PixelPlateMode.DOT;
   private final int charWidth;
   private final int charHeight;
   private final int plateWidth;
   private final int plateHeight;
   private boolean shallStop;
   private Dimension offDimension;
   private Image offImage;
   private Graphics offGraphics;
   private PixelPlate pixelPlate1;
   private PixelPlate pixelPlate2;
   private boolean active;
   private int[][] colors;
   protected static final Color[] COLORS = new Color[]{
      Color.white, Color.green, Color.red, Color.blue, Color.yellow, Color.magenta, Color.orange, Color.cyan, Color.pink
   };

   public ColorScreenSaver(Frame frame, Filter filter) {
      super(frame);
      Ensure.ensureArgumentNotNull(filter);
      this.active = false;
      this.setBackground(Color.black);
      this.setForeground(Color.white);
      this.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.charWidth = this.getFontMetrics(this.getFont()).stringWidth("#");
      this.charHeight = (int)Math.round(13.919999999999998);
      Dimension d = this.getPreferredSize();
      this.plateWidth = d.width / this.charWidth;
      this.plateHeight = d.height / this.charHeight;
      this.init(filter);
      this.pack();
      this.show();
      this.addKeyListener(this);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.shallStop = false;
      Thread thread = new Thread(this);
      thread.start();
      this.requestFocus();
   }

   @Override
   public void run() {
      while (!this.shallStop) {
         this.step();
         this.repaint();

         try {
            Thread.sleep(50L);
         } catch (InterruptedException var2) {
         }

         this.active = true;
      }
   }

   public void init(Filter filter) {
      this.colors = new int[this.plateHeight][this.plateWidth];
      Rectangle bounds = new Rectangle(this.plateWidth, this.plateHeight);
      this.pixelPlate1 = new PixelPlate(bounds, filter);
      this.pixelPlate2 = new PixelPlate(bounds, filter);
      this.pixelPlate1.setMode(this.mode);
      this.pixelPlate2.setMode(this.mode);
      int w = this.pixelPlate1.getVirtualWidth();
      int h = this.pixelPlate1.getVirtualHeight();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (Math.random() > 0.6) {
               this.pixelPlate1.set(x, y);
            }
         }
      }

      for (int y = 0; y < this.plateHeight; y++) {
         for (int xx = 0; xx < this.plateWidth; xx++) {
            this.colors[y][xx] = (int)(Math.random() * 100.0);
         }
      }
   }

   public void step() {
      int w = this.pixelPlate1.getVirtualWidth();
      int h = this.pixelPlate1.getVirtualHeight();
      int xScale = w / this.plateWidth;
      int yScale = h / this.plateHeight;
      this.pixelPlate2.clear();

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            int n = this.countNeighBours(x, y);
            double ran = Math.random();
            if (ran < 0.05) {
               n++;
            }

            if (this.pixelPlate1.isSet(x, y)) {
               if (n != 0 && n != 1 && n < 4) {
                  this.pixelPlate2.set(x, y);
               }
            } else if (n == 3) {
               this.pixelPlate2.set(x, y);
               int colorIndex = 0;
               if (this.pixelPlate1.isSet(x - 1, y)) {
                  colorIndex = this.colors[y / yScale][(x - 1) / xScale];
               } else if (this.pixelPlate1.isSet(x + 1, y)) {
                  colorIndex = this.colors[y / yScale][(x + 1) / xScale];
               } else if (this.pixelPlate1.isSet(x, y + 1)) {
                  colorIndex = this.colors[(y + 1) / yScale][x / xScale];
               } else if (this.pixelPlate1.isSet(x, y - 1)) {
                  colorIndex = this.colors[(y - 1) / yScale][x / xScale];
               } else if (this.pixelPlate1.isSet(x - 1, y - 1)) {
                  colorIndex = this.colors[(y - 1) / yScale][(x - 1) / xScale];
               } else if (this.pixelPlate1.isSet(x + 1, y - 1)) {
                  colorIndex = this.colors[(y - 1) / yScale][(x + 1) / xScale];
               } else if (this.pixelPlate1.isSet(x - 1, y + 1)) {
                  colorIndex = this.colors[(y + 1) / yScale][(x - 1) / xScale];
               } else if (this.pixelPlate1.isSet(x + 1, y + 1)) {
                  colorIndex = this.colors[(y + 1) / yScale][(x + 1) / xScale];
               }

               this.colors[y / yScale][x / xScale] = colorIndex;
            }

            if (n > 1 && ran < 0.001) {
               this.pixelPlate2.set(x, y);
               this.colors[y / yScale][x / xScale] = (int)(Math.random() * 100.0);
            }
         }
      }

      this.pixelPlate2.convert();
      PixelPlate u = this.pixelPlate1;
      this.pixelPlate1 = this.pixelPlate2;
      this.pixelPlate2 = u;
      this.repaint();
   }

   public int countNeighBours(int x, int y) {
      int result = 0;
      if (this.pixelPlate1.isSet(x - 1, y)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x + 1, y)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x, y + 1)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x, y - 1)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x - 1, y - 1)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x + 1, y - 1)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x - 1, y + 1)) {
         result++;
      }

      if (this.pixelPlate1.isSet(x + 1, y + 1)) {
         result++;
      }

      return result;
   }

   @Override
   public void paint(Graphics g) {
      this.update(g);
   }

   @Override
   public void update(Graphics g) {
      Dimension d = this.getSize();
      if (this.offGraphics == null || this.offDimension.width != d.width || this.offDimension.height != d.height) {
         this.offImage = this.createImage(d.width, d.height);
         this.offGraphics = this.offImage.getGraphics();
         this.offDimension = new Dimension(d.width, d.height);
      }

      this.offGraphics.setColor(this.getBackground());
      this.offGraphics.fillRect(0, 0, d.width, d.height);
      this.offGraphics.setColor(this.getForeground());
      CharacterPlate pt = this.pixelPlate1.convert();
      GeneralAlgorithm.replace(pt, '\u0000', ' ');
      int[][] ch = pt.glyphPlane();

      for (int y = 0; y < this.plateHeight; y++) {
         for (int x = 0; x < this.plateWidth; x++) {
            if (ch[y][x] != ' ') {
               this.offGraphics.setColor(COLORS[this.colors[y][x] % COLORS.length]);
               GlyphRenderer.drawCell(this.offGraphics, ch[y][x], 2, x, 2 + y * this.charHeight + this.charHeight * 3 / 4, this.charWidth);
            }
         }
      }

      g.drawImage(this.offImage, 0, 0, this);
   }

   public void quit() {
      if (this.active) {
         this.shallStop = true;
         this.dispose();
      }
   }

   @Override
   public void keyReleased(KeyEvent evt) {
      this.quit();
   }

   @Override
   public void keyPressed(KeyEvent evt) {
      this.quit();
   }

   @Override
   public void keyTyped(KeyEvent evt) {
      this.quit();
   }

   @Override
   public void mouseClicked(MouseEvent evt) {
      this.quit();
   }

   @Override
   public void mousePressed(MouseEvent evt) {
      this.quit();
   }

   @Override
   public void mouseReleased(MouseEvent evt) {
      this.quit();
   }

   @Override
   public void mouseEntered(MouseEvent evt) {
   }

   @Override
   public void mouseExited(MouseEvent evt) {
   }

   @Override
   public void mouseMoved(MouseEvent evt) {
   }

   @Override
   public void mouseDragged(MouseEvent evt) {
      this.quit();
   }

   @Override
   public Dimension getPreferredSize() {
      return this.getToolkit().getScreenSize();
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   @Override
   public Dimension getMaximumSize() {
      return this.getPreferredSize();
   }

   public static void main(String[] args) {
      Frame f = new Frame();
      f.pack();
      f.setVisible(true);
      MessageDialogConfigurationFileErrorHandler errorHandler = new MessageDialogConfigurationFileErrorHandler(f);
      JaveConfigurationFileLoader configurationFileLoader = new JaveConfigurationFileLoader(errorHandler);
      PixelPlateConfiguration pixelPlateConfiguration = configurationFileLoader.initConfigFile(new PixelPlateConfigurationInitializable());
      Filter filter = configurationFileLoader.initConfigFile(new FilterInitializable());
      new ColorScreenSaver(f, filter);
   }
}
