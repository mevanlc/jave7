package de.jave.jave.layers.ui;

import de.jave.jave.JavEApplication;
import de.jave.jave.PlateDocument;
import de.jave.jave.layers.DocumentLayer;
import de.jave.jave.layers.Layer;
import de.jave.jave.layers.SecondaryLayer;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class LayerCard extends JPanel {
   private static final int LAYER_CARD_HEIGHT = 72;
   private static final int PREVIEW_COLUMNS = 18;
   private static final int PREVIEW_ROWS = 6;
   private final JavEApplication application;
   private final PlateDocument document;
   private final Layer layer;
   private final int layerNumber;
   private final JPanel namePanel;
   private final JLabel nameLabel;
   private JTextField renameEditor;

   public LayerCard(JavEApplication application, PlateDocument document, Layer layer, int layerNumber, boolean active) {
      super(new BorderLayout(8, 0));
      this.application = application;
      this.document = document;
      this.layer = layer;
      this.layerNumber = layerNumber;
      this.setFixedHeight();
      Color background = active ? getUiColor("List.selectionBackground", Color.DARK_GRAY) : getUiColor("Panel.background", Color.WHITE);
      Color foreground = active ? getUiColor("List.selectionForeground", Color.WHITE) : getUiColor("Label.foreground", Color.BLACK);
      this.setOpaque(true);
      this.setBackground(background);
      this.setBorder(
         BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
               active ? getUiColor("List.selectionBackground", Color.DARK_GRAY) : getUiColor("Separator.foreground", Color.GRAY)
            ),
            BorderFactory.createEmptyBorder(6, 6, 6, 6)
         )
      );
      LayerPreview preview = new LayerPreview(layer);
      this.add(preview, BorderLayout.WEST);

      JPanel details = new JPanel(new GridBagLayout());
      details.setOpaque(false);
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.gridx = 0;
      constraints.gridy = 0;
      constraints.anchor = GridBagConstraints.WEST;
      constraints.insets = new Insets(0, 0, 2, 6);
      JLabel numberLabel = new JLabel("L" + layerNumber);
      numberLabel.setForeground(foreground);
      details.add(numberLabel, constraints);

      constraints.gridx = 1;
      constraints.weightx = 1.0;
      constraints.fill = GridBagConstraints.HORIZONTAL;
      this.namePanel = new JPanel(new BorderLayout());
      this.namePanel.setOpaque(false);
      this.nameLabel = new JLabel(layer.getName());
      this.nameLabel.setForeground(foreground);
      this.namePanel.add(this.nameLabel, BorderLayout.CENTER);
      details.add(this.namePanel, constraints);

      constraints.gridx = 0;
      constraints.gridy = 1;
      constraints.gridwidth = 2;
      constraints.weightx = 0.0;
      constraints.fill = GridBagConstraints.NONE;
      JLabel metadataLabel = new JLabel(this.createMetadataText());
      metadataLabel.setForeground(foreground);
      details.add(metadataLabel, constraints);
      this.add(details, BorderLayout.CENTER);

      MouseAdapter mouseAdapter = new MouseAdapter() {
         @Override
         public void mousePressed(MouseEvent event) {
            if (event.isPopupTrigger()) {
               this.maybeShowPopup(event);
            } else if (SwingUtilities.isLeftMouseButton(event)) {
               LayerCard.this.selectLayer();
               if (event.getClickCount() == 2) {
                  LayerCard.this.startRename();
               }
            }
         }

         @Override
         public void mouseReleased(MouseEvent event) {
            this.maybeShowPopup(event);
         }

         private void maybeShowPopup(MouseEvent event) {
            if (event.isPopupTrigger()) {
               LayerCard.this.createPopupMenu().show(event.getComponent(), event.getX(), event.getY());
            }
         }
      };
      this.addMouseListener(mouseAdapter);
      preview.addMouseListener(mouseAdapter);
      details.addMouseListener(mouseAdapter);
      numberLabel.addMouseListener(mouseAdapter);
      this.namePanel.addMouseListener(mouseAdapter);
      this.nameLabel.addMouseListener(mouseAdapter);
      metadataLabel.addMouseListener(mouseAdapter);
   }

   private void setFixedHeight() {
      this.setMinimumSize(new Dimension(0, LAYER_CARD_HEIGHT));
      this.setPreferredSize(new Dimension(220, LAYER_CARD_HEIGHT));
      this.setMaximumSize(new Dimension(Integer.MAX_VALUE, LAYER_CARD_HEIGHT));
   }

   private String createMetadataText() {
      if (this.layer instanceof DocumentLayer) {
         return "Document Layer";
      }
      SecondaryLayer secondaryLayer = (SecondaryLayer)this.layer;
      Dimension size = secondaryLayer.getContent().getSize();
      String visible = secondaryLayer.isVisible() ? "Shown" : "Hidden";
      String opaque = secondaryLayer.isOpaque() ? "Opaque" : "Transparent";
      return visible + " | " + opaque + " | " + size.width + "x" + size.height;
   }

   private JPopupMenu createPopupMenu() {
      boolean secondaryLayer = this.layer instanceof SecondaryLayer;
      JPopupMenu popup = new JPopupMenu();
      JMenuItem rename = new JMenuItem("Rename Layer");
      rename.addActionListener(event -> this.startRename());
      popup.add(rename);
      popup.addSeparator();
      JMenuItem newLayer = new JMenuItem("New Layer");
      newLayer.addActionListener(event -> {
         this.selectLayer();
         this.application.addSecondaryLayerAndActivate();
      });
      popup.add(newLayer);
      JMenuItem duplicate = new JMenuItem("Duplicate Layer");
      duplicate.addActionListener(event -> {
         this.selectLayer();
         this.application.duplicateActiveLayer();
      });
      popup.add(duplicate);
      JMenuItem delete = new JMenuItem("Delete Layer");
      delete.setEnabled(secondaryLayer && this.document.getLayerCount() > 1);
      delete.addActionListener(event -> {
         this.selectLayer();
         this.application.deleteActiveLayer();
      });
      popup.add(delete);
      popup.addSeparator();
      JMenuItem moveUp = new JMenuItem("Move Up");
      moveUp.setEnabled(this.canMoveUp());
      moveUp.addActionListener(event -> {
         this.selectLayer();
         this.application.moveActiveLayerUp();
      });
      popup.add(moveUp);
      JMenuItem moveDown = new JMenuItem("Move Down");
      moveDown.setEnabled(this.canMoveDown());
      moveDown.addActionListener(event -> {
         this.selectLayer();
         this.application.moveActiveLayerDown();
      });
      popup.add(moveDown);
      popup.addSeparator();
      JMenuItem visibility = new JMenuItem(this.isLayerVisible() ? "Hide Layer" : "Show Layer");
      visibility.setEnabled(secondaryLayer);
      visibility.addActionListener(event -> {
         this.selectLayer();
         this.application.toggleActiveLayerVisibility();
      });
      popup.add(visibility);
      JCheckBoxMenuItem opaque = new JCheckBoxMenuItem("Opaque", this.isLayerOpaque());
      opaque.setEnabled(secondaryLayer);
      opaque.addActionListener(event -> {
         this.selectLayer();
         this.application.setActiveLayerOpaque(opaque.isSelected());
      });
      popup.add(opaque);
      popup.addSeparator();
      JMenuItem flatten = new JMenuItem("Flatten");
      flatten.addActionListener(event -> this.application.flattenLayers(true));
      popup.add(flatten);
      JMenuItem flattenVisible = new JMenuItem("Flatten Visible");
      flattenVisible.addActionListener(event -> this.application.flattenLayers(false));
      popup.add(flattenVisible);
      return popup;
   }

   private void selectLayer() {
      this.application.activateLayerNumber(this.layerNumber);
   }

   private boolean isLayerVisible() {
      return !(this.layer instanceof SecondaryLayer) || ((SecondaryLayer)this.layer).isVisible();
   }

   private boolean isLayerOpaque() {
      return !(this.layer instanceof SecondaryLayer) || ((SecondaryLayer)this.layer).isOpaque();
   }

   private boolean canMoveUp() {
      return this.layer instanceof SecondaryLayer && this.layerNumber < this.document.getLayerCount();
   }

   private boolean canMoveDown() {
      return this.layer instanceof SecondaryLayer && this.layerNumber > 2;
   }

   private static Color getUiColor(String key, Color fallback) {
      Color color = UIManager.getColor(key);
      return color == null ? fallback : color;
   }

   private void startRename() {
      if (this.renameEditor != null) {
         this.renameEditor.requestFocusInWindow();
         this.renameEditor.selectAll();
         return;
      }
      JTextField editor = new JTextField(this.layer.getName());
      this.renameEditor = editor;
      editor.selectAll();
      this.namePanel.removeAll();
      this.namePanel.add(editor, BorderLayout.CENTER);
      this.namePanel.revalidate();
      this.namePanel.repaint();
      SwingUtilities.invokeLater(() -> editor.requestFocusInWindow());
      editor.addActionListener(event -> this.finishRename());
      editor.addFocusListener(new FocusAdapter() {
         @Override
         public void focusLost(FocusEvent event) {
            LayerCard.this.finishRename();
         }
      });
      editor.addKeyListener(new KeyAdapter() {
         @Override
         public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
               LayerCard.this.cancelRename();
            }
         }
      });
   }

   private void finishRename() {
      JTextField editor = this.renameEditor;
      if (editor == null) {
         return;
      }
      String name = editor.getText().trim();
      this.restoreNameLabel();
      if (name.length() == 0 || name.equals(this.layer.getName())) {
         this.application.getMainPanel().requestFocus();
         return;
      }
      this.nameLabel.setText(name);
      this.application.renameLayer(this.layer.getId(), name);
   }

   private void cancelRename() {
      if (this.renameEditor == null) {
         return;
      }
      this.restoreNameLabel();
      this.application.getMainPanel().requestFocus();
   }

   private void restoreNameLabel() {
      this.renameEditor = null;
      this.namePanel.removeAll();
      this.namePanel.add(this.nameLabel, BorderLayout.CENTER);
      this.namePanel.revalidate();
      this.namePanel.repaint();
   }

   private static final class LayerPreview extends JComponent {
      private final Layer layer;

      private LayerPreview(Layer layer) {
         this.layer = layer;
         this.setPreferredSize(new Dimension(72, 44));
         this.setMinimumSize(new Dimension(72, 44));
      }

      @Override
      protected void paintComponent(Graphics graphics) {
         super.paintComponent(graphics);
         Graphics2D g = (Graphics2D)graphics.create();
         try {
            g.setColor(getUiColor("TextArea.background", Color.WHITE));
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.setColor(getUiColor("Separator.foreground", Color.GRAY));
            g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
            CharacterPlate content = this.layer.getContent();
            g.setColor(getUiColor("TextArea.foreground", Color.BLACK));
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 6));
            int columns = Math.min(PREVIEW_COLUMNS, content.getWidth());
            int rows = Math.min(PREVIEW_ROWS, content.getHeight());
            for (int y = 0; y < rows; y++) {
               for (int x = 0; x < columns; x++) {
                  char ch = content.get(x, y);
                  if (ch != ' ') {
                     g.drawString(String.valueOf(ch), 4 + x * 4, 9 + y * 6);
                  }
               }
            }
         } finally {
            g.dispose();
         }
      }
   }
}
