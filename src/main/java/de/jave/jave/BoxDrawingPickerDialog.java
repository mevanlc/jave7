package de.jave.jave;

import de.jave.gui.dialog.JDialogFactory;
import de.jave.jave.actions.JaveKeyBindings;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
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
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
   private static final int INSERT_ALL_WIDTH = 24;
   private static final int[] PRIMARY_DIAGRAM_INDEXES = {0, 1, 2};
   private static final int[] SECONDARY_DIAGRAM_INDEXES = {6, 4, 5, 3};
   private static final int WINDOW_MIN_WIDTH = 206;
   private static final int WINDOW_MIN_HEIGHT = 350;
   private static final String PREF_KEY_FONT_SIZE = "boxDrawingPickerCompactFontSize";
   private static final String PREF_KEY_WINDOW_X = "boxDrawingPickerWindowX";
   private static final String PREF_KEY_WINDOW_Y = "boxDrawingPickerWindowY";

   private final JaveMainPanel mainPanel;
   private final JDialog dialog;
   private final JLabel previewLabel;
   private final JLabel infoLabel;
   private final List<DiagramComponent> diagramComponents = new ArrayList<>();
   private JPopupMenu activePopupMenu;
   private char selectedCharacter = '┌';
   private DiagramComponent selectedDiagramComponent;
   private int selectedRow = -1;
   private int selectedColumn = -1;
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
      this.infoLabel.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH.deriveFont(9.0f));
      this.infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

      this.dialog.getContentPane().setLayout(new BorderLayout());
      this.dialog.getContentPane().add(buildContentPanel(), BorderLayout.CENTER);
      this.dialog.getContentPane().add(buildButtonPanel(), BorderLayout.SOUTH);
      bindKeys();
      updateSelectionDisplay();

      this.dialog.pack();
      this.dialog.setMinimumSize(new Dimension(WINDOW_MIN_WIDTH, WINDOW_MIN_HEIGHT));
      Point savedLocation = readSavedWindowLocation();
      if (savedLocation == null) {
         this.dialog.setLocationRelativeTo(parent);
      } else {
         this.dialog.setLocation(savedLocation);
      }
   }

   private JPanel buildContentPanel() {
      JPanel panel = new JPanel(new BorderLayout(0, 3));
      panel.setBorder(new EmptyBorder(2, 6, 3, 6));
      panel.add(wrapLeading(buildDiagramPalette(), 0), BorderLayout.CENTER);

      JPanel selection = new JPanel(new BorderLayout(0, 0));
      selection.setBorder(BorderFactory.createCompoundBorder(
         BorderFactory.createLoweredBevelBorder(),
         new EmptyBorder(1, 3, 3, 3)
      ));
      selection.add(this.previewLabel, BorderLayout.CENTER);
      selection.add(this.infoLabel, BorderLayout.SOUTH);
      panel.add(wrapLeading(selection, 0), BorderLayout.SOUTH);
      return panel;
   }

   private JPanel wrapLeading(Component component, int horizontalGap) {
      JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEADING, horizontalGap, 0));
      wrapper.add(component);
      return wrapper;
   }

   private JPanel buildDiagramPalette() {
      List<BoxDrawingPalette.Diagram> diagrams = BoxDrawingPalette.getDiagrams();
      JPanel primaryRow = new JPanel(new GridLayout(1, PRIMARY_DIAGRAM_INDEXES.length));
      for (int diagramIndex : PRIMARY_DIAGRAM_INDEXES) {
         addDiagram(primaryRow, diagrams.get(diagramIndex), 0);
      }

      JPanel secondaryRow = new JPanel(new GridLayout(1, SECONDARY_DIAGRAM_INDEXES.length));
      for (int diagramIndex : SECONDARY_DIAGRAM_INDEXES) {
         int fontSizeOffset = diagramIndex == 4 || diagramIndex == 5
            ? DASH_FONT_SIZE_OFFSET
            : SECONDARY_FONT_SIZE_OFFSET;
         addDiagram(secondaryRow, diagrams.get(diagramIndex), fontSizeOffset);
      }

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
      JButton insertAllButton = new JButton(new AbstractAction("Insert All") {
         @Override
         public void actionPerformed(ActionEvent event) {
            insertAll();
         }
      });
      JButton helpButton = new JButton(new AbstractAction("?") {
         @Override
         public void actionPerformed(ActionEvent event) {
            showHelp();
         }
      });
      Insets compactMargin = new Insets(1, 2, 1, 2);
      for (JButton button : new JButton[] {insertButton, insertAllButton, helpButton}) {
         button.putClientProperty("JButton.buttonType", "square");
         button.setMargin(compactMargin);
      }
      helpButton.setToolTipText("Show picker instructions");
      JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
      panel.add(insertButton);
      panel.add(insertAllButton);
      panel.add(helpButton);
      return panel;
   }

   private void showHelp() {
      JOptionPane.showMessageDialog(
         this.dialog,
         "Click a piece to select it.\nDouble-click to insert.\nRight-click for alternates.",
         "Box Drawing Picker Help",
         JOptionPane.INFORMATION_MESSAGE
      );
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
         KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.ALT_DOWN_MASK), "boxDrawing.insertAll"
      );
      root.getActionMap().put("boxDrawing.insertAll", new AbstractAction() {
         @Override
         public void actionPerformed(ActionEvent event) {
            insertAll();
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
      selectCharacter(ch, null, -1, -1);
   }

   private void selectCharacter(char ch, DiagramComponent primaryComponent, int row, int column) {
      this.selectedCharacter = ch;
      this.selectedDiagramComponent = primaryComponent;
      this.selectedRow = row;
      this.selectedColumn = column;
      updateSelectionDisplay();
      for (DiagramComponent component : this.diagramComponents) {
         component.repaint();
      }
   }

   static Color createSelectionHintColor(Color selectionColor) {
      return new Color(
         lightenColorComponent(selectionColor.getRed()),
         lightenColorComponent(selectionColor.getGreen()),
         lightenColorComponent(selectionColor.getBlue()),
         selectionColor.getAlpha()
      );
   }

   private static int lightenColorComponent(int component) {
      return component + (255 - component) * 2 / 3;
   }

   static boolean isReleaseInPressedCell(
      int pressedRow, int pressedColumn, int releasedRow, int releasedColumn
   ) {
      return pressedRow >= 0
         && pressedColumn >= 0
         && pressedRow == releasedRow
         && pressedColumn == releasedColumn;
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
      Font menuFont = new Font(Font.MONOSPACED, Font.PLAIN, variantMenuFontSize(this.diagramFontSize));
      for (int i = 0; i < variants.length(); i++) {
         char variant = variants.charAt(i);
         JMenuItem item = new JMenuItem(String.format(
            "%c   U+%04X   %s", variant, (int)variant, BoxDrawingPalette.getDisplayName(variant)
         ));
         item.setFont(menuFont);
         item.setBorder(BorderFactory.createCompoundBorder(
            item.getBorder(), new EmptyBorder(4, 8, 4, 8)
         ));
         item.setEnabled(variant != this.selectedCharacter);
         item.addActionListener(event -> selectCharacter(variant));
         menu.add(item);
      }
      this.activePopupMenu = menu;
      menu.show(invoker, x, y);
   }

   static int variantMenuFontSize(int diagramFontSize) {
      return Math.round(Math.max(14, diagramFontSize - 8) * 1.5f);
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

   private void insertAll() {
      Plate plate = this.mainPanel.getPlate();
      if (plate == null) {
         return;
      }
      Point location = plate.getPasteLocation();
      if (location == null) {
         return;
      }
      insertPaletteRows(plate.getContent(), location, createPaletteLayoutRows());
      plate.saveCurrentState("insert box drawing palette");
      plate.repaint();
   }

   static String[] createPaletteLayoutRows() {
      List<BoxDrawingPalette.Diagram> diagrams = BoxDrawingPalette.getDiagrams();
      List<String> rows = new ArrayList<>();
      appendDiagramRow(rows, diagrams, PRIMARY_DIAGRAM_INDEXES);
      appendDiagramRow(rows, diagrams, SECONDARY_DIAGRAM_INDEXES);
      return rows.toArray(new String[0]);
   }

   private static void appendDiagramRow(
      List<String> output, List<BoxDrawingPalette.Diagram> diagrams, int[] diagramIndexes
   ) {
      int rowHeight = 0;
      List<String[]> diagramRows = new ArrayList<>();
      for (int diagramIndex : diagramIndexes) {
         String[] rows = diagrams.get(diagramIndex).getRows();
         diagramRows.add(rows);
         rowHeight = Math.max(rowHeight, rows.length);
      }

      int slotWidth = INSERT_ALL_WIDTH / diagramIndexes.length;
      for (int row = 0; row < rowHeight; row++) {
         StringBuilder line = new StringBuilder(INSERT_ALL_WIDTH);
         for (String[] rows : diagramRows) {
            int diagramRow = row - (rowHeight - rows.length) / 2;
            String text = diagramRow >= 0 && diagramRow < rows.length ? rows[diagramRow] : "";
            int leftPadding = (slotWidth - text.length()) / 2;
            line.append(" ".repeat(leftPadding));
            line.append(text);
            line.append(" ".repeat(slotWidth - leftPadding - text.length()));
         }
         output.add(line.toString());
      }
   }

   static void insertPaletteRows(CharacterPlate target, Point location, String[] rows) {
      for (int y = 0; y < rows.length; y++) {
         String row = rows[y];
         for (int x = 0; x < row.length(); x++) {
            char ch = row.charAt(x);
            if (ch != ' ' && target.contains(location.x + x, location.y + y)) {
               target.setForce(location.x + x, location.y + y, ch);
            }
         }
      }
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

   private static Preferences preferences() {
      return Preferences.userRoot().node("JavE");
   }

   private Point readSavedWindowLocation() {
      int x = preferences().getInt(PREF_KEY_WINDOW_X, Integer.MIN_VALUE);
      int y = preferences().getInt(PREF_KEY_WINDOW_Y, Integer.MIN_VALUE);
      if (x == Integer.MIN_VALUE || y == Integer.MIN_VALUE) {
         return null;
      }

      Point location = new Point(x, y);
      Rectangle windowBounds = new Rectangle(location, this.dialog.getSize());
      for (GraphicsDevice device : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
         Rectangle visibleBounds = windowBounds.intersection(device.getDefaultConfiguration().getBounds());
         if (visibleBounds.width >= 50 && visibleBounds.height >= 30) {
            return location;
         }
      }
      return null;
   }

   private void saveWindowLocation() {
      Point location = this.dialog.getLocation();
      preferences().putInt(PREF_KEY_WINDOW_X, location.x);
      preferences().putInt(PREF_KEY_WINDOW_Y, location.y);
      flushPreferences();
   }

   private static void flushPreferences() {
      try {
         preferences().flush();
      } catch (BackingStoreException exception) {
         // Display preferences are non-critical.
      }
   }

   private void closeDialog() {
      saveWindowLocation();
      this.dialog.setVisible(false);
   }

   public void show() {
      this.dialog.pack();
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
      private static final int HORIZONTAL_PADDING = 2;
      private static final int VERTICAL_PADDING = 0;
      private final String[] rows;
      private final int fontSizeOffset;
      private int hoverRow = -1;
      private int hoverColumn = -1;
      private int pressedRow = -1;
      private int pressedColumn = -1;

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
               clearPressedCell();
               if (!maybeShowPopup(event) && event.getButton() == MouseEvent.BUTTON1) {
                  capturePressedCell(event.getPoint());
               }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
               if (maybeShowPopup(event)) {
                  clearPressedCell();
               } else if (event.getButton() == MouseEvent.BUTTON1) {
                  Character ch = selectPressedCharacterAt(event.getPoint());
                  if (ch != null) {
                     requestFocusInWindow();
                  }
               } else {
                  clearPressedCell();
               }
            }

            @Override
            public void mouseClicked(MouseEvent event) {
               if (event.getButton() == MouseEvent.BUTTON1
                  && event.getClickCount() == 2
                  && isPrimarySelectionAt(event.getPoint())) {
                  insertSelected();
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
         return new Dimension(
            columns * cellWidth(metrics) + HORIZONTAL_PADDING * 2,
            this.rows.length * metrics.getHeight() + VERTICAL_PADDING * 2
         );
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
                  boolean primarySelection = isPrimarySelection(row, column);
                  Color selectionBackground = UIManager.getColor("List.selectionBackground");
                  if (selectionBackground == null) {
                     selectionBackground = Color.BLUE;
                  }
                  if (!primarySelection) {
                     selectionBackground = createSelectionHintColor(selectionBackground);
                  }
                  graphics.setColor(selectionBackground);
                  graphics.fillRect(x, y, cellWidth, cellHeight);
                  if (primarySelection) {
                     Color selectionForeground = UIManager.getColor("List.selectionForeground");
                     graphics.setColor(selectionForeground == null ? UIManager.getColor("Label.foreground") : selectionForeground);
                  } else {
                     graphics.setColor(UIManager.getColor("Label.foreground"));
                  }
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

      private boolean maybeShowPopup(MouseEvent event) {
         if (!event.isPopupTrigger()) {
            return false;
         }
         Character ch = selectCharacterAt(event.getPoint());
         if (ch != null) {
            showVariants(ch, this, event.getX(), event.getY());
         }
         event.consume();
         return true;
      }

      private Character selectCharacterAt(Point point) {
         int[] cell = getCellAt(point);
         if (cell == null) {
            return null;
         }
         char ch = this.rows[cell[0]].charAt(cell[1]);
         if (ch == ' ') {
            return null;
         }
         selectCharacter(ch, this, cell[0], cell[1]);
         return ch;
      }

      private void capturePressedCell(Point point) {
         int[] cell = getCellAt(point);
         if (cell != null && this.rows[cell[0]].charAt(cell[1]) != ' ') {
            this.pressedRow = cell[0];
            this.pressedColumn = cell[1];
         }
      }

      private Character selectPressedCharacterAt(Point point) {
         int pressedCellRow = this.pressedRow;
         int pressedCellColumn = this.pressedColumn;
         clearPressedCell();

         int[] releasedCell = getCellAt(point);
         if (releasedCell == null || !isReleaseInPressedCell(
            pressedCellRow, pressedCellColumn, releasedCell[0], releasedCell[1]
         )) {
            return null;
         }
         char ch = this.rows[releasedCell[0]].charAt(releasedCell[1]);
         if (ch == ' ') {
            return null;
         }
         selectCharacter(ch, this, releasedCell[0], releasedCell[1]);
         return ch;
      }

      private void clearPressedCell() {
         this.pressedRow = -1;
         this.pressedColumn = -1;
      }

      private boolean isPrimarySelectionAt(Point point) {
         int[] cell = getCellAt(point);
         return cell != null
            && this.rows[cell[0]].charAt(cell[1]) != ' '
            && isPrimarySelection(cell[0], cell[1]);
      }

      private boolean isPrimarySelection(int row, int column) {
         return BoxDrawingPickerDialog.this.selectedDiagramComponent == null
            || (BoxDrawingPickerDialog.this.selectedDiagramComponent == this
               && BoxDrawingPickerDialog.this.selectedRow == row
               && BoxDrawingPickerDialog.this.selectedColumn == column);
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
