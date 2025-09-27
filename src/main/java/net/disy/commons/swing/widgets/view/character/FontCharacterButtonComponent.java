package net.disy.commons.swing.fontchooser.view.character;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.color.SwingColors;

public class FontCharacterButtonComponent {
   private final JComponent content;
   private final ListenerList<ActionListener> listeners = new ListenerList<>();

   public FontCharacterButtonComponent(final FontCharacterChooserModel model) {
      final Font font = model.getFontModel().getFont();
      final Character[] characters = getDisplayableCharacters(font);
      int columnCount = 20;
      final int rowCount = 1 + (characters.length - 1) / 20;
      int columnWidth = 20;
      int rowHeight = 25;
      final JComponent component = new JComponent() {
         @Override
         public Dimension getMinimumSize() {
            return this.getPreferredSize();
         }

         @Override
         public Dimension getPreferredSize() {
            return new Dimension(400, rowCount * 25);
         }

         @Override
         protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(SwingColors.getTextAreaBackgroundColor());
            g.fillRect(0, 0, 400, rowCount * 25);
            g.setColor(SwingColors.getTextAreaForegroundColor());

            for (int rowIndex = 1; rowIndex <= rowCount; rowIndex++) {
               g.drawLine(0, rowIndex * 25, 400, rowIndex * 25);
            }

            for (int columnIndex = 1; columnIndex <= 20; columnIndex++) {
               g.drawLine(columnIndex * 20, 0, columnIndex * 20, rowCount * 25);
            }

            int descent = g.getFontMetrics().getDescent();

            for (int i = 0; i < characters.length; i++) {
               int x = i % 20 * 20;
               int y = i / 20 * 25;
               if (g.hitClip(x, y, 20, 25)) {
                  Character character = characters[i];
                  this.paintCharacter(g, x, y, descent, character);
               }
            }
         }

         private void paintCharacter(Graphics g, int x, int y, int descent, Character character) {
            if (character == model.getCharacter()) {
               g.setColor(SwingColors.getTextAreaSelectionBackgroundColor());
               g.fillRect(x, y, 20, 25);
               g.setColor(SwingColors.getTextAreaSelectionForegroundColor());
            } else {
               g.setColor(SwingColors.getTextAreaForegroundColor());
            }

            Rectangle oldClipBounds = g.getClipBounds();
            g.setClip(new Rectangle(x, y, 20, 25));
            String label = character.toString();
            Rectangle2D labelBounds = font.getStringBounds(label, new FontRenderContext(new AffineTransform(), false, false));
            int yPos = (int)((double)y + (25.0 + labelBounds.getHeight()) / 2.0) - descent;
            int xPos = (int)((double)x + (20.0 - labelBounds.getWidth()) / 2.0);
            g.drawString(label, xPos, yPos);
            g.setClip(oldClipBounds);
         }
      };
      component.setFont(font);
      component.addMouseListener(new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent e) {
            int x = e.getX() / 20;
            if (x >= 0 && x < 20) {
               int y = e.getY() / 25;
               if (y >= 0 && y < rowCount) {
                  int characterIndex = x + y * 20;
                  if (characterIndex < characters.length) {
                     model.setCharacter(characters[characterIndex]);
                     if (e.getClickCount() == 2) {
                        FontCharacterButtonComponent.this.fireActionEvent();
                     }
                  }
               }
            }
         }
      });
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            component.repaint();
         }
      });
      JScrollPane scrollPane = new JScrollPane(component);
      scrollPane.getViewport().setPreferredSize(new Dimension(400, 250));
      scrollPane.getVerticalScrollBar().setUnitIncrement(25);
      this.content = scrollPane;
   }

   public synchronized void addActionListener(ActionListener listener) {
      this.listeners.add(listener);
   }

   public synchronized void removeActionListener(ActionListener listener) {
      this.listeners.remove(listener);
   }

   private synchronized void fireActionEvent() {
      final ActionEvent event = new ActionEvent(this, -1, null);
      this.listeners.forAllDo(new IClosure<ActionListener>() {
         public void execute(ActionListener listener) {
            listener.actionPerformed(event);
         }
      });
   }

   public JComponent getContent() {
      return this.content;
   }

   private static Character[] getDisplayableCharacters(Font font) {
      List<Character> set = new ArrayList<>();

      for (int i = 0; i < 65536; i++) {
         if (font.canDisplay((char)i)) {
            set.add((char) i);
         }
      }

      return set.toArray(new Character[0]);
   }

   public void requestFocus() {
   }
}
