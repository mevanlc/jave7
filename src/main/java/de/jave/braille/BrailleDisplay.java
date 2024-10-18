package de.jave.braille;

import de.jave.braille.table.BrailleTables;
import de.jave.braille.table.IBrailleTable;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

public class BrailleDisplay extends JComponent {
   private String[] lines;
   private int maxLineLength;
   private static final int[] CHAR_WIDTH = new int[]{10, 11, 12, 14, 17};
   private static final int[] CHAR_HEIGHT = new int[]{20, 23, 26, 29, 35};
   private static final int[] DY = new int[]{4, 5, 5, 6, 7};
   private static final int[] DX = new int[]{3, 3, 4, 6, 6};
   private static final int[] X0 = new int[]{2, 2, 2, 2, 3};
   private static final int[] Y0 = new int[]{2, 2, 3, 3, 4};
   private static final int[] DOT_HEIGHT = new int[]{2, 2, 3, 3, 4};
   private static final int[] DOT_WIDTH = new int[]{2, 2, 3, 3, 4};
   private int charWidth;
   private int charHeight;
   private int dx;
   private int dy;
   private int x0;
   private int y0;
   private int dotHeight;
   private int dotWidth;
   private int viewSize;
   private IBrailleTable brailleTable = BrailleTables.getDefaultTable();
   private final JComponent content;

   public BrailleDisplay() {
      this("");
   }

   public BrailleDisplay(String text) {
      this.viewSize = 0;
      this.charWidth = CHAR_WIDTH[0];
      this.charHeight = CHAR_HEIGHT[0];
      this.dx = DX[0];
      this.dy = DY[0];
      this.x0 = X0[0];
      this.y0 = Y0[0];
      this.dotWidth = DOT_WIDTH[0];
      this.dotHeight = DOT_HEIGHT[0];
      this.setText(text);
      this.addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent evt) {
            char ch = evt.getKeyChar();
            if (ch == '+' || ch == ')' || ch == '9') {
               BrailleDisplay.this.incrementViewSize();
            } else if (ch == '-' || ch == '(' || ch == '8') {
               BrailleDisplay.this.decrementViewSize();
            }
         }
      });
      this.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent evt) {
            if (evt.isMetaDown()) {
               BrailleDisplay.this.incrementViewSize();
            } else {
               BrailleDisplay.this.decrementViewSize();
            }
         }
      });
      this.content = new JScrollPane(this);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void setMode(IBrailleTable brailleTable) {
      if (this.brailleTable != brailleTable) {
         this.brailleTable = brailleTable;
         this.repaint();
      }
   }

   public void setViewSize(int viewSize) {
      if (this.viewSize != viewSize) {
         this.viewSize = viewSize;
         this.charWidth = CHAR_WIDTH[this.viewSize];
         this.charHeight = CHAR_HEIGHT[this.viewSize];
         this.dx = DX[this.viewSize];
         this.dy = DY[this.viewSize];
         this.x0 = X0[this.viewSize];
         this.y0 = Y0[this.viewSize];
         this.dotWidth = DOT_WIDTH[this.viewSize];
         this.dotHeight = DOT_HEIGHT[this.viewSize];
         this.invalidate();
         Container parent = this.getParent();
         if (parent != null) {
            parent.doLayout();
         }

         this.repaint();
      }
   }

   public void setText(String text) {
      StringTokenizer st = new StringTokenizer(text, "\n");
      int lineCount = st.countTokens();
      this.lines = new String[lineCount];
      int y = 0;

      for (this.maxLineLength = 0; st.hasMoreTokens(); y++) {
         this.lines[y] = st.nextToken();
         if (this.lines[y].length() > this.maxLineLength) {
            this.maxLineLength = this.lines[y].length();
         }
      }

      this.invalidate();
      Container parent = this.getParent();
      if (parent != null) {
         parent.doLayout();
      }

      this.repaint();
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(this.maxLineLength * this.charWidth, this.lines.length * this.charHeight);
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
      g.setColor(Color.gray);

      for (int y = 0; y < this.lines.length; y++) {
         for (int x = 0; x < this.lines[y].length(); x++) {
            g.drawRect(x * this.charWidth, y * this.charHeight, this.charWidth - 2, this.charHeight - 3);
         }
      }

      g.setColor(Color.black);

      for (int y = 0; y < this.lines.length; y++) {
         for (int x = 0; x < this.lines[y].length(); x++) {
            this.paintBraille(g, x * this.charWidth, y * this.charHeight, this.lines[y].charAt(x));
         }
      }
   }

   private final void paintBraille(Graphics g, int x, int y, char character) {
      int code = this.brailleTable.getBraillePattern(character);
      int x1 = x + this.x0;
      int x2 = x1 + this.dx;
      int y1 = y + this.y0;
      int y2 = y1 + this.dy;
      int y3 = y2 + this.dy;
      int y4 = y3 + this.dy;
      if ((code & 1) > 0) {
         this.paintBrailleDot(g, x1, y1);
      }

      if ((code & 2) > 0) {
         this.paintBrailleDot(g, x1, y2);
      }

      if ((code & 4) > 0) {
         this.paintBrailleDot(g, x1, y3);
      }

      if ((code & 8) > 0) {
         this.paintBrailleDot(g, x2, y1);
      }

      if ((code & 16) > 0) {
         this.paintBrailleDot(g, x2, y2);
      }

      if ((code & 32) > 0) {
         this.paintBrailleDot(g, x2, y3);
      }

      if ((code & 64) > 0) {
         this.paintBrailleDot(g, x1, y4);
      }

      if ((code & 128) > 0) {
         this.paintBrailleDot(g, x2, y4);
      }
   }

   private final void paintBrailleDot(Graphics g, int x, int y) {
      g.fillRect(x, y, this.dotWidth, this.dotHeight);
   }

   private void incrementViewSize() {
      if (this.viewSize < CHAR_WIDTH.length - 1) {
         this.setViewSize(this.viewSize + 1);
      }
   }

   private void decrementViewSize() {
      if (this.viewSize > 0) {
         this.setViewSize(this.viewSize - 1);
      }
   }
}
