package de.jave.jave;

import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.actions.JaveKeyBindings;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/** A diagram-first picker for the Unicode Box Drawing block. */
public class BoxDrawingPickerDialog {
   private static final int FONT_SIZE_MIN = 16;
   private static final int FONT_SIZE_MAX = 48;
   private static final int FONT_SIZE_DEFAULT = 20;
   private static final int FONT_SIZE_STEP = 2;
   private static final int SECONDARY_FONT_SIZE_OFFSET = -4;
   private static final int DASH_FONT_SIZE_OFFSET = -7;
   private static final int WINDOW_MIN_WIDTH = 220;
   private static final int WINDOW_MIN_HEIGHT = 380;
   private static final String PREF_KEY_FONT_SIZE = "boxDrawingPickerCompactFontSize";
   private static final String PREF_KEY_WINDOW_WIDTH = "boxDrawingPickerCompactWindowWidth";
   private static final String PREF_KEY_WINDOW_HEIGHT = "boxDrawingPickerCompactWindowHeight";

   private final JaveMainPanel mainPanel;
   private final JDialog dialog;
   private final JLabel previewLabel;
   private final JLabel infoLabel;
   private final List<DiagramComponent> diagramComponents = new ArrayList<>();
   private JPopupMenu activePopupMenu;
   private char selectedCharacter = '┌';
   private int diagramFontSize;

   public BoxDrawingPickerDialog(Component parent, JaveMainPanel mainPanel) {
      this.mainPanel = mainPanel;
      int saved = preferences().getInt(PREF_KEY_FONT_SIZE, FONT_SIZE_DEFAULT);
      this.diagramFontSize = Math.max(FONT_SIZE_MIN, Math.min(FONT_SIZE_MAX, saved));
      this.dialog = JDialogFactory.createJDialog(parent, "Box Drawing Picker", false);
      this.dialog.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent event) {
            closeDialog();
         }
      });

      this.previewLabel = new JLabel(" ", SwingConstants.CENTER);
      this.previewLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 40));
      this.infoLabel = new JLabel(" ");
      this.infoLabel.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH.deriveFont(10.0f));
      this.infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(buildContentPanel(), BorderLayout.CENTER);
      this.dialog.getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);
      bindKeys();
      updateSelectionDisplay();

      this.dialog.pack();
      this.dialog.setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT));
      Dimension savedSize = readSavedWindowSize();
      if (savedSize != null) {
         this.dialog.setSize(savedSize);
      }
      this.dialog.setLocationRelativeTo(parent);
   }

   private JPanel buildContentPanel() {
      JPanel panel = new JPanel(new BorderLayout(0, 3));
      panel.setBorder(new EmptyBorder(5, 7, 3, 7));
      panel.add(buildHint(), BorderLayout.NORTH);
      panel.add(wrapLeading(buildDiagramPalette(), 0), BorderLayout.CENTER);

      JPanel selection = new JPanel(new BorderLayout(0, 0));
      selection.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLoweredBevelBorder(),
         new EmptyBorder(1, 3, 3, 3)
      ));
      selection.add(this.previewLabel, BorderLayout.CENTER);
      selection.add(this.infoLabel, BorderLayout.SOUTH);
      panel.add(wrapLeading(selection, 7), BorderLayout.SOUTH);
      return panel;
   }

   private JPanel wrapLeading(Component component, int horizontalGap) {
      JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, horizontalGap, 0));
      wrapper.add(component);
      return wrapper;
   }

   private Box buildHint() {
      Box hint = Box.createVerticalBox();
      hint.add(createHintLine("Click a piece to select it"));
      hint.add(createHintLine("double-click to insert"));
      hint.add(createHintLine("Right-click for alternates"));
      return hint;
   }

   private JLabel createHintLine(String text) {
      JLabel line = new JLabel(text);
      line.setFont(JaveGlobalRessources.FONT_DEFAULT);
      return line;
   }

   private JPanel buildDiagramPalette() {
      List<BoxDrawingPalette.Diagram> diagrams = BoxDrawingPalette.getDiagrams();
      JPanel primaryRow = new JPanel(new GridLayout(1, 3));
      addDiagram(primaryRow, diagrams.get(0), 0);
      addDiagram(primaryRow, diagrams.get(1), 0);
      addDiagram(primaryRow, diagrams.get(2), 0);

      JPanel secondaryRow = new JPanel(new GridLayout(1, 4));
      addDiagram(secondaryRow, diagrams.get(6), SECONDARY_FONT_SIZE_OFFSET);
      addDiagram(secondaryRow, diagrams.get(4), DASH_FONT_SIZE_OFFSET);
      addDiagram(secondaryRow, diagrams.get(5), DASH_FONT_SIZE_OFFSET);
      addDiagram(secondaryRow, diagrams.get(3), SECONDARY_FONT_SIZE_OFFSET);

      JPanel palette = new JPanel();
      palette.setLayout(new BoxLayout(palette, BoxLayout.Y_AXIS));
      palette.setBorder(BorderFactory.createLineBorder(separatorColor()));
      palette.add(primaryRow);
      palette.add(secondaryRow);
      return palette;
   }

   private void addDiagram(JPanel row, BoxDrawingPalette.Diagram diagram, int fontSizeOffset) {
      DiagramComponent component = new DiagramComponent(diagram, fontSizeOffset);
      this.diagramComponents.add(component);
      row.add(component);
   }

   private Color separatorColor() {
      Color color = UIManager.getColor("Separator.foreground");
      return color == null ? UIManager.getColor("controlShadow") : color;
   }

   private JPanel buildButtonPanel() {
      JButton insertButton = new JButton(new AbstractAction("Insert") {
         @Override
         public void actionPerformed(ActionEvent event) {
            if (handlePopupEnter()) {
               return;
            }
            insertSelected();
         }
      });
      JButton insertCloseButton = new JButton(new AbstractAction("Insert & Close") {
         @Override
         public void actionPerformed(ActionEvent event) {
            insertSelected();
            closeDialog();
         }
      });
      Insets compactMargin = new Insets(1, 7, 1, 7);
      insertButton.setMargin(compactMargin);
      insertCloseButton.setMargin(compactMargin);
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 14, 0));
      panel.add(insertButton);
      panel.add(insertCloseButton);
      return panel;
   }

   private void bindKeys() {
      JComponent root = this.dialog.getRootPane();
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "boxDrawing.insert");
      root.getActionMap().put("boxDrawing.insert", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            insertSelected();
         }
      });
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
         KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK), "boxDrawing.insertClose"
      );
      root.getActionMap().put("boxDrawing.insertClose", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            insertSelected();
            closeDialog();
         }
      });
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "boxDrawing.close");
      root.getActionMap().put("boxDrawing.close", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            closeDialog();
         }
      });
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(JaveKeyBindings.ZOOM_IN, "boxDrawing.zoomIn");
      root.getActionMap().put("boxDrawing.zoomIn", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            zoomIn();
         }
      });
      root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(JaveKeyBindings.ZOOM_OUT, "boxDrawing.zoomOut");
      root.getActionMap().put("boxDrawing.zoomOut", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            zoomOut();
         }
      });
   }

   private void selectCharacter(char ch) {
      this.selectedCharacter = ch;
      updateSelectionDisplay();
      for (DiagramComponent component : this.diagramComponents) {
         component.repaint();
      }
   }

   private void updateSelectionDisplay() {
      this.previewLabel.setText(String.valueOf(this.selectedCharacter));
      this.infoLabel.setText(String.format(
         "U+%04X  %s", (int)this.selectedCharacter, BoxDrawingPalette.getDisplayName(this.selectedCharacter)
      ));
   }

   private void showVariants(char ch, Component invoker, int x, int y) {
      String variants = BoxDrawingPalette.getAlternates(ch);
      if (variants.isEmpty()) {
         return;
      }
      JPopupMenu menu = new JPopupMenu();
      menu.addPopupMenuListener(new PopupKeyHandler(menu));
      Font menuFont = new Font(Font.MONOSPACED, Font.PLAIN, Math.max(14, this.diagramFontSize - 8));
      for (int i = 0; i < variants.length(); i++) {
         char variant = variants.charAt(i);
         JMenuItem item = new JMenuItem(String.format(
            "%c   U+%04X   %s", variant, (int)variant, BoxDrawingPalette.getDisplayName(variant)
         ));
         item.setFont(menuFont);
         item.setEnabled(variant != this.selectedCharacter);
         item.addActionListener(event -> selectCharacter(variant));
         menu.add(item);
      }
      this.activePopupMenu = menu;
      menu.show(invoker, x, y);
   }

   private boolean handlePopupEnter() {
      JPopupMenu menu = this.activePopupMenu;
      if (menu == null || !menu.isVisible()) {
         return false;
      }
      activateArmedMenuItem(menu);
      return true;
   }

   private void activateArmedMenuItem(JPopupMenu menu) {
      for (Component component : menu.getComponents()) {
         if (component instanceof JMenuItem) {
            JMenuItem item = (JMenuItem)component;
            if (item.isEnabled() && item.getModel().isArmed()) {
               menu.setVisible(false);
               item.doClick();
               return;
            }
         }
      }
      menu.setVisible(false);
   }

   private void insertSelected() {
      Plate plate = this.mainPanel.getPlate();
      if (plate == null) {
         return;
      }
      Point location = plate.getPasteLocation();
      if (location == null) {
         return;
      }
      plate.setCharForce(location, this.selectedCharacter);
      plate.saveCurrentState("insert box drawing character");
      plate.repaint();
   }

   private void zoomIn() {
      if (this.diagramFontSize < FONT_SIZE_MAX) {
         this.diagramFontSize = Math.min(FONT_SIZE_MAX, this.diagramFontSize + FONT_SIZE_STEP);
         updateDiagramFonts();
      }
   }

   private void zoomOut() {
      if (this.diagramFontSize > FONT_SIZE_MIN) {
         this.diagramFontSize = Math.max(FONT_SIZE_MIN, this.diagramFontSize - FONT_SIZE_STEP);
         updateDiagramFonts();
      }
   }

   private void updateDiagramFonts() {
      Dimension oldPreferredSize = this.dialog.getPreferredSize();
      preferences().putInt(PREF_KEY_FONT_SIZE, this.diagramFontSize);
      flushPreferences();
      for (DiagramComponent component : this.diagramComponents) {
         component.updateFont();
      }
      this.dialog.getContentPane().revalidate();
      this.dialog.getContentPane().repaint();

      Dimension newPreferredSize = this.dialog.getPreferredSize();
      Dimension currentSize = this.dialog.getSize();
      int width = Math.max(
         WINDOW_MIN_WIDTH, currentSize.width + newPreferredSize.width - oldPreferredSize.width
      );
      int height = Math.max(
         WINDOW_MIN_HEIGHT, currentSize.height + newPreferredSize.height - oldPreferredSize.height
      );
      this.dialog.setSize(width, height);
   }

   private Dimension readSavedWindowSize() {
      int width = preferences().getInt(PREF_KEY_WINDOW_WIDTH, -1);
      int height = preferences().getInt(PREF_KEY_WINDOW_HEIGHT, -1);
      if (width < WINDOW_MIN_WIDTH || height < WINDOW_MIN_HEIGHT) {
         return null;
      }
      return new Dimension(width, height);
   }

   private void saveWindowSize() {
      Dimension size = this.dialog.getSize();
      preferences().putInt(PREF_KEY_WINDOW_WIDTH, size.width);
      preferences().putInt(PREF_KEY_WINDOW_HEIGHT, size.height);
      flushPreferences();
   }

   private static Preferences preferences() {
      return Preferences.userRoot().node("JavE");
   }

   private static void flushPreferences() {
      try {
         preferences().flush();
      } catch (BackingStoreException exception) {
         // Display preferences are non-critical.
      }
   }

   private void closeDialog() {
      saveWindowSize();
      this.dialog.setVisible(false);
   }

   public void show() {
      this.dialog.setVisible(true);
      if (!this.diagramComponents.isEmpty()) {
         this.diagramComponents.get(0).requestFocusInWindow();
      }
   }

   private final class PopupKeyHandler implements KeyEventDispatcher, PopupMenuListener {
      private final JPopupMenu menu;
      private boolean installed;

      PopupKeyHandler(JPopupMenu menu) {
         this.menu = menu;
      }

      @Override
      public boolean dispatchKeyEvent(KeyEvent event) {
         if (event.getID() != KeyEvent.KEY_PRESSED || !this.menu.isVisible()) {
            return false;
         }
         if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            activateArmedMenuItem(this.menu);
            return true;
         }
         if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.menu.setVisible(false);
            return true;
         }
         return false;
      }

      @Override
      public void popupMenuWillBecomeVisible(PopupMenuEvent event) {
         if (!this.installed) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
            this.installed = true;
         }
      }

      @Override
      public void popupMenuWillBecomeInvisible(PopupMenuEvent event) {
         uninstall();
      }

      @Override
      public void popupMenuCanceled(PopupMenuEvent event) {
         uninstall();
      }

      private void uninstall() {
         if (this.installed) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this);
            this.installed = false;
         }
         if (BoxDrawingPickerDialog.this.activePopupMenu == this.menu) {
            BoxDrawingPickerDialog.this.activePopupMenu = null;
         }
      }
   }

   private final class DiagramComponent extends JComponent {
      private static final int PADDING = 2;
      private final String[] rows;
      private final int fontSizeOffset;
      private int hoverRow = -1;
      private int hoverColumn = -1;

      DiagramComponent(BoxDrawingPalette.Diagram diagram, int fontSizeOffset) {
         this.rows = diagram.getRows();
         this.fontSizeOffset = fontSizeOffset;
         this.setOpaque(true);
         this.setBackground(UIManager.getColor("Panel.background"));
         this.setFocusable(true);
         this.setToolTipText("");
         updateFont();
         this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent event) {
               updateHover(event.getPoint());
            }
         });
         this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent event) {
               clearHover();
            }

            @Override
            public void mousePressed(MouseEvent event) {
               maybeShowPopup(event);
            }

            @Override
            public void mouseReleased(MouseEvent event) {
               maybeShowPopup(event);
            }

            @Override
            public void mouseClicked(MouseEvent event) {
               if (event.getButton() != MouseEvent.BUTTON1) {
                  return;
               }
               Character ch = getCharacterAt(event.getPoint());
               if (ch != null) {
                  requestFocusInWindow();
                  selectCharacter(ch);
                  if (event.getClickCount() == 2) {
                     insertSelected();
                  }
               }
            }
         });
      }

      void updateFont() {
         int fontSize = BoxDrawingPickerDialog.this.diagramFontSize + this.fontSizeOffset;
         this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, fontSize));
         this.revalidate();
         this.repaint();
      }

      @Override
      public Dimension getPreferredSize() {
         FontMetrics metrics = this.getFontMetrics(this.getFont());
         int columns = 0;
         for (String row : this.rows) {
            columns = Math.max(columns, row.length());
         }
         return new Dimension(columns * cellWidth(metrics) + PADDING * 2, this.rows.length * metrics.getHeight() + PADDING * 2);
      }

      @Override
      protected void paintComponent(Graphics graphics) {
         super.paintComponent(graphics);
         FontMetrics metrics = graphics.getFontMetrics(this.getFont());
         int cellWidth = cellWidth(metrics);
         int cellHeight = metrics.getHeight();
         int originX = (this.getWidth() - maxColumns() * cellWidth) / 2;
         int originY = (this.getHeight() - this.rows.length * cellHeight) / 2;

         for (int row = 0; row < this.rows.length; row++) {
            String text = this.rows[row];
            for (int column = 0; column < text.length(); column++) {
               char ch = text.charAt(column);
               if (ch == ' ') {
                  continue;
               }
               int x = originX + column * cellWidth;
               int y = originY + row * cellHeight;
               if (ch == BoxDrawingPickerDialog.this.selectedCharacter) {
                  graphics.setColor(UIManager.getColor("List.selectionBackground"));
                  graphics.fillRect(x, y, cellWidth, cellHeight);
                  Color selectionForeground = UIManager.getColor("List.selectionForeground");
                  graphics.setColor(selectionForeground == null ? UIManager.getColor("Label.foreground") : selectionForeground);
               } else if (row == this.hoverRow && column == this.hoverColumn) {
                  Color hover = UIManager.getColor("List.dropCellBackground");
                  graphics.setColor(hover == null ? UIManager.getColor("controlHighlight") : hover);
                  graphics.fillRect(x, y, cellWidth, cellHeight);
                  graphics.setColor(UIManager.getColor("Label.foreground"));
               } else {
                  graphics.setColor(UIManager.getColor("Label.foreground"));
               }
               graphics.setFont(this.getFont());
               graphics.drawString(String.valueOf(ch), x, originY + row * cellHeight + metrics.getAscent());
            }
         }
      }

      @Override
      public String getToolTipText(MouseEvent event) {
         Character ch = getCharacterAt(event.getPoint());
         if (ch == null) {
            return null;
         }
         return String.format("%c  U+%04X  %s", ch, (int)ch, BoxDrawingPalette.getDisplayName(ch));
      }

      private void maybeShowPopup(MouseEvent event) {
         if (!event.isPopupTrigger()) {
            return;
         }
         Character ch = getCharacterAt(event.getPoint());
         if (ch != null) {
            selectCharacter(ch);
            showVariants(ch, this, event.getX(), event.getY());
         }
         event.consume();
      }

      private void updateHover(Point point) {
         int oldRow = this.hoverRow;
         int oldColumn = this.hoverColumn;
         int[] cell = getCellAt(point);
         if (cell == null || this.rows[cell[0]].charAt(cell[1]) == ' ') {
            this.hoverRow = -1;
            this.hoverColumn = -1;
         } else {
            this.hoverRow = cell[0];
            this.hoverColumn = cell[1];
         }
         if (oldRow != this.hoverRow || oldColumn != this.hoverColumn) {
            repaint();
         }
      }

      private void clearHover() {
         if (this.hoverRow >= 0 || this.hoverColumn >= 0) {
            this.hoverRow = -1;
            this.hoverColumn = -1;
            repaint();
         }
      }

      private Character getCharacterAt(Point point) {
         int[] cell = getCellAt(point);
         if (cell == null) {
            return null;
         }
         char ch = this.rows[cell[0]].charAt(cell[1]);
         return ch == ' ' ? null : ch;
      }

      private int[] getCellAt(Point point) {
         FontMetrics metrics = this.getFontMetrics(this.getFont());
         int width = cellWidth(metrics);
         int height = metrics.getHeight();
         int originX = (this.getWidth() - maxColumns() * width) / 2;
         int originY = (this.getHeight() - this.rows.length * height) / 2;
         int column = (point.x - originX) / width;
         int row = (point.y - originY) / height;
         if (point.x < originX || point.y < originY || row < 0 || row >= this.rows.length || column < 0 || column >= this.rows[row].length()) {
            return null;
         }
         return new int[]{row, column};
      }

      private int maxColumns() {
         int columns = 0;
         for (String row : this.rows) {
            columns = Math.max(columns, row.length());
         }
         return columns;
      }

      private int cellWidth(FontMetrics metrics) {
         return metrics.charWidth('M');
      }
   }
}
