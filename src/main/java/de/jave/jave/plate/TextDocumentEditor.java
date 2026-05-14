package de.jave.jave.plate;

import de.jave.jave.CharacterSets;
import de.jave.jave.JavEApplication;
import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.layers.ui.LayersPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.PlatePreferences;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class TextDocumentEditor extends AbstractDocumentEditor {
   private static final int DEFAULT_LAYERS_PANEL_WIDTH = 260;
   private static final int DEFAULT_SPLIT_DIVIDER_SIZE = 6;
   private final Plate plate;
   private final LayersPanel layersPanel;
   private final JSplitPane splitPane;
   private final String stopGapName;
   private boolean layersPanelVisible;
   private int layersPanelDividerLocation = -1;

   public TextDocumentEditor(
      String stopGapName,
      PlateDocument doc,
      JavEApplication jave,
      PlatePreferences platePreferences,
      ToolManager toolManager,
      FontModel displayFontModel,
      ColorScheme colorScheme,
      CharacterSets characterSets
   ) {
      Ensure.ensureArgumentNotNull(stopGapName);
      Ensure.ensureArgumentNotNull(doc);
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(toolManager);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(colorScheme);
      Ensure.ensureArgumentNotNull(characterSets);
      this.stopGapName = stopGapName;
      this.plate = new Plate(doc, jave, platePreferences, toolManager, displayFontModel, new ObjectModel<>(colorScheme), characterSets);
      this.layersPanel = new LayersPanel(jave, doc);
      this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.plate.getComponent(), null);
      this.splitPane.setResizeWeight(1.0);
      this.splitPane.setDividerSize(0);
      this.layersPanelVisible = false;
      if (jave.isLayersPanelShownByDefault()) {
         this.layersPanelVisible = true;
         this.splitPane.setRightComponent(this.layersPanel.getContent());
         this.splitPane.setDividerSize(DEFAULT_SPLIT_DIVIDER_SIZE);
      }
   }

   @Override
   public void dispose() {
      this.layersPanel.dispose();
      this.plate.dispose();
   }

   @Override
   public Plate getPlate() {
      return this.plate;
   }

   @Override
   public JComponent getContent() {
      return this.splitPane;
   }

   public boolean isLayersPanelVisible() {
      return this.layersPanelVisible;
   }

   public void setLayersPanelVisible(boolean visible) {
      if (this.layersPanelVisible == visible) {
         if (visible) {
            this.layersPanel.refresh();
         }
         return;
      }
      this.layersPanelVisible = visible;
      if (visible) {
         this.layersPanel.refresh();
         this.splitPane.setRightComponent(this.layersPanel.getContent());
         this.splitPane.setDividerSize(DEFAULT_SPLIT_DIVIDER_SIZE);
         this.splitPane.setResizeWeight(1.0);
         this.splitPane.setDividerLocation(this.getVisibleDividerLocation());
         SwingUtilities.invokeLater(() -> this.splitPane.setDividerLocation(this.getVisibleDividerLocation()));
      } else {
         if (this.splitPane.getRightComponent() != null) {
            this.layersPanelDividerLocation = this.splitPane.getDividerLocation();
         }
         this.splitPane.setRightComponent(null);
         this.splitPane.setDividerSize(0);
      }
      this.splitPane.revalidate();
      this.splitPane.repaint();
   }

   private int getVisibleDividerLocation() {
      int width = this.splitPane.getWidth();
      if (this.layersPanelDividerLocation >= 0) {
         return width > 0 ? Math.min(this.layersPanelDividerLocation, Math.max(0, width - 120)) : this.layersPanelDividerLocation;
      }
      if (width <= 0) {
         return DEFAULT_LAYERS_PANEL_WIDTH;
      }
      return Math.max(0, width - DEFAULT_LAYERS_PANEL_WIDTH);
   }

   @Override
   public JaveDocumentType getType() {
      return JaveDocumentType.TEXT;
   }

   @Override
   public boolean isModified() {
      return this.plate.getDocument().isModified();
   }

   @Override
   public File getFile() {
      return this.plate.getDocument().getFile();
   }

   @Override
   public String getStopGapName() {
      return this.stopGapName;
   }
}
