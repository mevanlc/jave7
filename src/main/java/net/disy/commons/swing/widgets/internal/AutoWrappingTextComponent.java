package net.disy.commons.swing.widgets.internal;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.provider.IProvider;
import net.disy.commons.core.text.TextAlignment;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.color.SwingColors;

public class AutoWrappingTextComponent extends JComponent {
   private final int width;
   private final TextContent content = new TextContent();
   private final ObjectModel<TextSelection> selectionModel = new ObjectModel<>();
   private TextAlignment textAlignment = TextAlignment.LEFT;

   public AutoWrappingTextComponent(String text, int width) {
      Ensure.ensureArgumentNotNull(text);
      this.width = width;
      this.setForeground(SwingColors.getTextAreaForegroundColor());
      this.setBackground(SwingColors.getTextAreaBackgroundColor());
      this.content.setTextBlocks(TextBlockFactory.createTextBlocks(text));
      this.setOpaque(true);
      MouseAdapter mouseListener = new MouseAdapter() {
         private TextPosition startPosition;

         @Override
         public void mouseReleased(MouseEvent e) {
            if (AutoWrappingTextComponent.this.isEnabled()) {
               TextPosition position = this.getTextPositionAt(e.getPoint());
               if (position == null) {
                  this.startPosition = null;
               } else if (this.startPosition != null) {
                  AutoWrappingTextComponent.this.selectionModel.setValue(TextSelection.createSelection(this.startPosition, position));
               }
            }
         }

         @Override
         public void mousePressed(MouseEvent e) {
            if (AutoWrappingTextComponent.this.isEnabled()) {
               AutoWrappingTextComponent.this.clearSelection();
               TextPosition position = this.getTextPositionAt(e.getPoint());
               this.startPosition = position;
               AutoWrappingTextComponent.this.requestFocus();
            }
         }

         @Override
         public void mouseDragged(MouseEvent e) {
            if (AutoWrappingTextComponent.this.isEnabled()) {
               TextPosition position = this.getTextPositionAt(e.getPoint());
               if (position != null) {
                  if (this.startPosition != null) {
                     AutoWrappingTextComponent.this.selectionModel.setValue(TextSelection.createSelection(this.startPosition, position));
                  }
               }
            }
         }

         private TextPosition getTextPositionAt(Point point) {
            FontMetrics metrics = AutoWrappingTextComponent.this.getFontMetrics(AutoWrappingTextComponent.this.getFont());
            TextPositionFindingHandler finder = new TextPositionFindingHandler(metrics, point);
            AutoWrappingTextComponent.this.render(metrics, AutoWrappingTextComponent.this.getWidth(), finder);
            TextPosition textPosition = finder.getTextPosition();
            if (textPosition == null) {
               return point.y < 0 ? new TextPosition(0, 0) : AutoWrappingTextComponent.this.content.getLastTextPosition();
            } else {
               return textPosition;
            }
         }
      };
      this.addMouseListener(mouseListener);
      this.addMouseMotionListener(mouseListener);
      this.selectionModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AutoWrappingTextComponent.this.repaint();
         }
      });
      this.addKeyListener(new TextComponentKeyListener(this.content, this.selectionModel, new IProvider<Toolkit>() {
         public Toolkit getObject() {
            return AutoWrappingTextComponent.this.getToolkit();
         }
      }));
   }

   @Override
   public Dimension getMinimumSize() {
      return this.getPreferredSize();
   }

   public void setTextAlignment(TextAlignment textAlignment) {
      this.textAlignment = textAlignment;
   }

   @Override
   public Dimension getPreferredSize() {
      FontMetrics fontMetrics = this.getFontMetrics(this.getFont());
      int lineCount = this.getLineCount(this.width, fontMetrics);
      return new Dimension(this.width, fontMetrics.getHeight() * lineCount);
   }

   private int getLineCount(int layoutWidth, FontMetrics fontMetrics) {
      LineCountingRenderingHandler lineCountingHandler = new LineCountingRenderingHandler();
      this.render(fontMetrics, layoutWidth, lineCountingHandler);
      return lineCountingHandler.getLineCount();
   }

   @Override
   protected void paintComponent(Graphics g) {
      if (this.isOpaque()) {
         g.setColor(this.getBackground());
         g.fillRect(0, 0, this.getWidth(), this.getHeight());
      }

      g.setFont(this.getFont());
      FontMetrics metrics = g.getFontMetrics();
      this.render(metrics, this.getWidth(), new TextGraphicsRenderingHandler(g, this.getForeground()));
   }

   protected void render(FontMetrics metrics, int layoutWidth, IBlockRenderingHandler blockRenderer) {
      int spaceWidth = metrics.stringWidth(" ");
      int tabWidth = metrics.stringWidth("        ");
      LineBuffer lineBuffer = new LineBuffer(metrics, layoutWidth, blockRenderer, this.textAlignment, this.selectionModel);
      int xOffset = 0;
      int x = 0;

      for (int blockIndex = 0; blockIndex < this.content.getBlockCount(); blockIndex++) {
         TextBlock block = this.content.getBlock(blockIndex);
         int spaceLeft = layoutWidth - x;
         int blockWidth = metrics.stringWidth(block.text);
         boolean fits = spaceLeft >= blockWidth;
         if (!fits && blockIndex > 0) {
            lineBuffer.handleAutoLineBreak();
            x = 0;
         }

         lineBuffer.add(block, blockWidth);
         x += blockWidth;
         TextBlockDelimiter delimiter = block.delimiter;
         switch (delimiter) {
            case END_OF_TEXT:
               lineBuffer.handleNewLine();
               break;
            case NEWLINE:
               lineBuffer.handleNewLine();
               x = 0;
               break;
            case SPACE:
               x += spaceWidth;
               break;
            case TAB:
               x += tabWidth;
         }
      }
   }

   public void setText(String text) {
      this.clearSelection();
      this.content.setTextBlocks(TextBlockFactory.createTextBlocks(text));
      this.invalidate();
      this.repaint();
   }

   private void clearSelection() {
      this.selectionModel.setValue(null);
   }
}
