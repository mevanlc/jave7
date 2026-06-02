package de.jave.jave.layers.ui;

import de.jave.jave.DocumentListener;
import de.jave.jave.JavEApplication;
import de.jave.jave.PlateDocument;
import de.jave.jave.layers.Layer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;

public final class LayersPanel {
   private static final int BASE_ICON_SIZE = 16;
   private static final int PANEL_WIDTH = 260;
   private static final Dimension BASE_BOTTOM_BUTTON_SIZE = new Dimension(24, 22);
   private static final String ADD_LAYER_GLYPH = "＋";
   private static final String DELETE_LAYER_GLYPH = "−";
   private static final String DUPLICATE_LAYER_GLYPH = "⧉";
   private static final String LAYER_VISIBLE_GLYPH = "◉";
   private static final String LAYER_HIDDEN_GLYPH = "○";
   private static final String LAYER_OPAQUE_GLYPH = "■";
   private static final String LAYER_TRANSPARENT_GLYPH = "□";
   private final JavEApplication application;
   private final PlateDocument document;
   private final JPanel content;
   private final JPanel stackPanel;
   private final JPanel stackHostPanel;
   private final JButton addButton;
   private final JButton deleteButton;
   private final JButton duplicateButton;
   private final JButton visibilityButton;
   private final JToggleButton opaqueButton;
   private final Font baseButtonFont;
   private int appliedIconSize = -1;
   private final DocumentListener documentListener = new DocumentListener() {
      @Override
      public void documentClosing() {
      }

      @Override
      public void documentHiding() {
      }

      @Override
      public void documentShowing() {
         LayersPanel.this.refresh();
      }

      @Override
      public void documentChanged() {
         LayersPanel.this.refresh();
      }
   };

   public LayersPanel(JavEApplication application, PlateDocument document) {
      this.application = application;
      this.document = document;
      this.content = new JPanel(new BorderLayout());
      this.content.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
      this.content.setPreferredSize(new Dimension(PANEL_WIDTH, 120));
      this.content.setMinimumSize(new Dimension(PANEL_WIDTH, 60));

      this.stackPanel = new JPanel();
      this.stackPanel.setLayout(new BoxLayout(this.stackPanel, BoxLayout.Y_AXIS));
      this.stackHostPanel = new JPanel(new BorderLayout());
      this.stackHostPanel.add(this.stackPanel, BorderLayout.SOUTH);
      JScrollPane stackScrollPane = new JScrollPane(this.stackHostPanel);
      stackScrollPane.setBorder(BorderFactory.createEmptyBorder());
      this.content.add(stackScrollPane, BorderLayout.CENTER);

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
      this.addButton = new JButton(ADD_LAYER_GLYPH);
      this.baseButtonFont = this.addButton.getFont();
      this.makeCompact(this.addButton);
      this.addButton.setToolTipText("New Layer");
      this.addButton.addActionListener(event -> this.application.addSecondaryLayerAndActivate());
      this.deleteButton = new JButton(DELETE_LAYER_GLYPH);
      this.makeCompact(this.deleteButton);
      this.deleteButton.setToolTipText("Delete Layer");
      this.deleteButton.addActionListener(event -> this.application.deleteActiveLayer());
      this.duplicateButton = new JButton(DUPLICATE_LAYER_GLYPH);
      this.makeCompact(this.duplicateButton);
      this.duplicateButton.setToolTipText("Duplicate Layer");
      this.duplicateButton.addActionListener(event -> this.application.duplicateActiveLayer());
      this.visibilityButton = new JButton(LAYER_VISIBLE_GLYPH);
      this.makeCompact(this.visibilityButton);
      this.visibilityButton.setToolTipText("Show or Hide Layer");
      this.visibilityButton.addActionListener(event -> this.application.toggleActiveLayerVisibility());
      this.opaqueButton = new JToggleButton(LAYER_OPAQUE_GLYPH);
      this.makeCompact(this.opaqueButton);
      this.opaqueButton.setToolTipText("Toggle Layer Opacity");
      this.opaqueButton.addActionListener(event -> this.application.setActiveLayerOpaque(this.opaqueButton.isSelected()));
      this.updateButtonGlyphSize();
      buttonPanel.add(this.addButton);
      buttonPanel.add(Box.createHorizontalStrut(1));
      buttonPanel.add(this.deleteButton);
      buttonPanel.add(Box.createHorizontalStrut(3));
      buttonPanel.add(this.duplicateButton);
      buttonPanel.add(Box.createHorizontalStrut(3));
      buttonPanel.add(this.visibilityButton);
      buttonPanel.add(Box.createHorizontalStrut(1));
      buttonPanel.add(this.opaqueButton);
      this.content.add(buttonPanel, BorderLayout.SOUTH);

      this.document.addDocumentListener(this.documentListener);
      this.refresh();
   }

   public JComponent getContent() {
      return this.content;
   }

   public void dispose() {
      this.document.removeDocumentListener(this.documentListener);
   }

   public void refresh() {
      this.updateButtonGlyphSize();
      this.rebuildLayerStack();
      this.updateButtons();
      this.content.revalidate();
      this.content.repaint();
   }

   private void makeCompact(AbstractButton button) {
      button.setMargin(new Insets(0, 0, 0, 0));
      button.setFocusable(false);
   }

   private void updateButtonGlyphSize() {
      int iconSize = this.application.getGeneralIconSize();
      if (iconSize == this.appliedIconSize) {
         return;
      }
      this.appliedIconSize = iconSize;
      float scale = iconSize / (float)BASE_ICON_SIZE;
      Font font = scale == 1.0f ? this.baseButtonFont : this.baseButtonFont.deriveFont(this.baseButtonFont.getSize2D() * scale);
      int sizeIncrease = Math.max(0, iconSize - BASE_ICON_SIZE) / 2;
      Dimension size = new Dimension(BASE_BOTTOM_BUTTON_SIZE.width + sizeIncrease, BASE_BOTTOM_BUTTON_SIZE.height + sizeIncrease);
      this.applyButtonPresentation(this.addButton, font, size);
      this.applyButtonPresentation(this.deleteButton, font, size);
      this.applyButtonPresentation(this.duplicateButton, font, size);
      this.applyButtonPresentation(this.visibilityButton, font, size);
      this.applyButtonPresentation(this.opaqueButton, font, size);
   }

   private void applyButtonPresentation(AbstractButton button, Font font, Dimension size) {
      button.setFont(font);
      button.setPreferredSize(size);
      button.setMinimumSize(size);
      button.setMaximumSize(size);
   }

   private void rebuildLayerStack() {
      this.stackPanel.removeAll();
      List<Layer> layers = this.document.getLayeredDocument().getLayersBottomToTop();
      String activeLayerId = this.document.getLayeredDocument().getActiveLayerId();
      for (int index = layers.size() - 1; index >= 0; index--) {
         if (index < layers.size() - 1) {
            this.stackPanel.add(Box.createVerticalStrut(3));
         }
         Layer layer = layers.get(index);
         int layerNumber = index + 1;
         LayerCard card = new LayerCard(this.application, this.document, layer, layerNumber, layer.getId().equals(activeLayerId));
         card.setAlignmentX(0.0f);
         this.stackPanel.add(card);
      }
      this.stackHostPanel.revalidate();
      this.stackHostPanel.repaint();
   }

   private void updateButtons() {
      this.deleteButton.setEnabled(this.document.canDeleteActiveLayer());
      this.duplicateButton.setEnabled(true);
      this.visibilityButton.setEnabled(this.document.canToggleActiveLayerVisibility());
      this.visibilityButton.setText(this.document.isActiveLayerVisible() ? LAYER_VISIBLE_GLYPH : LAYER_HIDDEN_GLYPH);
      this.visibilityButton.setToolTipText(this.document.isActiveLayerVisible() ? "Hide Layer" : "Show Layer");
      this.opaqueButton.setEnabled(this.document.canToggleActiveLayerOpacity());
      this.opaqueButton.setSelected(this.document.isActiveLayerOpaque());
      this.opaqueButton.setText(this.document.isActiveLayerOpaque() ? LAYER_OPAQUE_GLYPH : LAYER_TRANSPARENT_GLYPH);
      this.opaqueButton.setToolTipText(this.document.isActiveLayerOpaque() ? "Make Layer Non-Opaque" : "Make Layer Opaque");
   }
}
