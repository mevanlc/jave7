package de.jave.jave.layers;

import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.disy.commons.core.util.Ensure;

public final class LayeredDocument {
   private final DocumentLayer documentLayer;
   private final List<SecondaryLayer> secondaryLayers = new ArrayList<>();
   private String activeLayerId;
   private int nextLayerNumber = 2;

   public LayeredDocument(DocumentLayer documentLayer) {
      Ensure.ensureArgumentNotNull(documentLayer);
      this.documentLayer = documentLayer;
      this.activeLayerId = documentLayer.getId();
   }

   public static LayeredDocument fromContent(CharacterPlate content) {
      Ensure.ensureArgumentNotNull(content);
      return new LayeredDocument(new DocumentLayer("layer-1", "Layer 1", content));
   }

   public DocumentLayer getDocumentLayer() {
      return this.documentLayer;
   }

   public List<SecondaryLayer> getSecondaryLayers() {
      return Collections.unmodifiableList(this.secondaryLayers);
   }

   public List<Layer> getLayersBottomToTop() {
      List<Layer> layers = new ArrayList<>();
      layers.add(this.documentLayer);
      layers.addAll(this.secondaryLayers);
      return layers;
   }

   public List<Layer> getLayersTopToBottom() {
      List<Layer> layers = this.getLayersBottomToTop();
      Collections.reverse(layers);
      return layers;
   }

   public int getLayerCount() {
      return 1 + this.secondaryLayers.size();
   }

   public Dimension getSize() {
      return this.documentLayer.getSize();
   }

   public void resizeDocument(int width, int height) {
      this.documentLayer.getContent().setSize(width, height);
      Dimension documentSize = this.getSize();
      for (SecondaryLayer layer : this.secondaryLayers) {
         layer.clipToDocument(documentSize);
      }
   }

   public String getActiveLayerId() {
      return this.activeLayerId;
   }

   public Layer getActiveLayer() {
      Layer layer = this.findLayer(this.activeLayerId);
      return layer == null ? this.documentLayer : layer;
   }

   public SecondaryLayer getActiveSecondaryLayer() {
      Layer layer = this.getActiveLayer();
      return layer instanceof SecondaryLayer ? (SecondaryLayer)layer : null;
   }

   public int getActiveLayerNumber() {
      if (this.isDocumentLayerActive()) {
         return 1;
      }
      SecondaryLayer layer = this.findSecondaryLayer(this.activeLayerId);
      return layer == null ? 1 : this.secondaryLayers.indexOf(layer) + 2;
   }

   public void setActiveLayerId(String activeLayerId) {
      if (this.findLayer(activeLayerId) == null) {
         throw new IllegalArgumentException("Unknown layer id: " + activeLayerId);
      }
      this.activeLayerId = activeLayerId;
   }

   public void activateDocumentLayer() {
      this.activeLayerId = this.documentLayer.getId();
   }

   public SecondaryLayer activateFirstSecondaryLayerOrCreate() {
      if (this.secondaryLayers.isEmpty()) {
         return this.addSecondaryLayerAboveActive();
      }
      SecondaryLayer layer = this.secondaryLayers.get(0);
      this.activeLayerId = layer.getId();
      return layer;
   }

   public boolean isDocumentLayerActive() {
      return this.documentLayer.getId().equals(this.activeLayerId);
   }

   public void activateNextLayer() {
      int activeLayerNumber = this.getActiveLayerNumber();
      if (activeLayerNumber >= this.getLayerCount()) {
         this.activateDocumentLayer();
      } else {
         this.activateLayerNumber(activeLayerNumber + 1);
      }
   }

   public void activateLayerNumber(int layerNumber) {
      if (layerNumber < 1 || layerNumber > this.getLayerCount()) {
         throw new IllegalArgumentException("Unknown layer number: " + layerNumber);
      }
      if (layerNumber == 1) {
         this.activateDocumentLayer();
      } else {
         this.activeLayerId = this.secondaryLayers.get(layerNumber - 2).getId();
      }
   }

   public SecondaryLayer addSecondaryLayerAboveActive() {
      SecondaryLayer layer = new SecondaryLayer(this.createLayerId(), "Layer " + this.nextLayerNumber);
      this.nextLayerNumber++;
      int insertIndex = this.getInsertIndexAbove(this.activeLayerId);
      this.secondaryLayers.add(insertIndex, layer);
      this.activeLayerId = layer.getId();
      return layer;
   }

   public void addSecondaryLayer(SecondaryLayer layer) {
      Ensure.ensureArgumentNotNull(layer);
      this.secondaryLayers.add(layer);
      this.ensureNextLayerNumberAfter(layer.getId());
   }

   public SecondaryLayer duplicateLayer(String layerId) {
      Layer layer = this.findLayer(layerId);
      if (layer == null) {
         throw new IllegalArgumentException("Unknown layer id: " + layerId);
      }

      SecondaryLayer duplicate;
      if (layer instanceof DocumentLayer) {
         duplicate = new SecondaryLayer(this.createLayerId(), layer.getName() + " copy", new Point(0, 0), layer.getContent().getClone());
      } else {
         duplicate = ((SecondaryLayer)layer).duplicate(this.createLayerId(), layer.getName() + " copy");
      }
      this.nextLayerNumber++;
      this.secondaryLayers.add(this.getInsertIndexAbove(layerId), duplicate);
      this.activeLayerId = duplicate.getId();
      return duplicate;
   }

   public SecondaryLayer duplicateActiveLayer() {
      return this.duplicateLayer(this.getActiveLayer().getId());
   }

   public boolean canDeleteActiveLayer() {
      return this.getActiveLayer() instanceof SecondaryLayer && this.getLayerCount() > 1;
   }

   public boolean deleteActiveLayer() {
      if (!this.canDeleteActiveLayer()) {
         return false;
      }
      return this.deleteSecondaryLayer(this.activeLayerId);
   }

   public boolean deleteSecondaryLayer(String layerId) {
      SecondaryLayer layer = this.findSecondaryLayer(layerId);
      if (layer == null) {
         return false;
      }
      boolean removed = this.secondaryLayers.remove(layer);
      if (removed && layerId.equals(this.activeLayerId)) {
         this.activeLayerId = this.documentLayer.getId();
      }
      return removed;
   }

   public boolean moveSecondaryLayer(String layerId, int delta) {
      SecondaryLayer layer = this.findSecondaryLayer(layerId);
      if (layer == null || delta == 0) {
         return false;
      }
      int oldIndex = this.secondaryLayers.indexOf(layer);
      int newIndex = oldIndex + delta;
      if (newIndex < 0 || newIndex >= this.secondaryLayers.size()) {
         return false;
      }
      this.secondaryLayers.remove(oldIndex);
      this.secondaryLayers.add(newIndex, layer);
      return true;
   }

   public void setActiveChar(int x, int y, char ch) {
      if (!this.isInsideDocument(x, y)) {
         return;
      }
      Layer active = this.getActiveLayer();
      if (active instanceof DocumentLayer) {
         this.documentLayer.getContent().set(x, y, ch);
      } else {
         ((SecondaryLayer)active).setCharAtDocument(x, y, ch);
      }
   }

   public void setActiveCharForce(int x, int y, char ch) {
      if (!this.isInsideDocument(x, y)) {
         return;
      }
      Layer active = this.getActiveLayer();
      if (active instanceof DocumentLayer) {
         this.documentLayer.getContent().setForce(x, y, ch);
      } else {
         ((SecondaryLayer)active).setCharAtDocument(x, y, ch);
      }
   }

   public char getActiveChar(int x, int y) {
      if (!this.isInsideDocument(x, y)) {
         return ' ';
      }
      Layer active = this.getActiveLayer();
      if (active instanceof DocumentLayer) {
         return this.documentLayer.getContent().get(x, y);
      }
      return ((SecondaryLayer)active).getCharAtDocument(x, y);
   }

   public CharacterPlate getActiveContentProjection() {
      CharacterPlate projection = new CharacterPlate(this.getSize());
      Layer active = this.getActiveLayer();
      if (active instanceof DocumentLayer) {
         return this.documentLayer.getContent();
      }
      SecondaryLayer layer = (SecondaryLayer)active;
      CharacterPlate content = layer.getContent();
      Point position = layer.getPosition();
      projection.paste(content, position.x, position.y);
      return projection;
   }

   public void replaceActiveContentProjection(CharacterPlate projection) {
      Ensure.ensureArgumentNotNull(projection);
      Layer active = this.getActiveLayer();
      if (active instanceof DocumentLayer) {
         this.documentLayer.getContent().setContent(projection.getContentClone());
         return;
      }
      ((SecondaryLayer)active).replaceContent(new Point(0, 0), projection);
   }

   public CharacterPlate getComposite(boolean includeHiddenSecondaryLayers) {
      CharacterPlate composite = this.documentLayer.getContent().getClone();
      for (SecondaryLayer layer : this.secondaryLayers) {
         if (includeHiddenSecondaryLayers || layer.isVisible()) {
            this.compositeSecondaryLayer(composite, layer);
         }
      }
      return composite;
   }

   public void flatten(boolean includeHiddenSecondaryLayers) {
      this.documentLayer.setContent(this.getComposite(includeHiddenSecondaryLayers));
      this.secondaryLayers.clear();
      this.activeLayerId = this.documentLayer.getId();
   }

   private void compositeSecondaryLayer(CharacterPlate composite, SecondaryLayer layer) {
      CharacterPlate content = layer.getContent();
      Point position = layer.getPosition();
      for (int y = 0; y < content.getHeight(); y++) {
         int documentY = position.y + y;
         if (documentY < 0 || documentY >= composite.getHeight()) {
            continue;
         }
         for (int x = 0; x < content.getWidth(); x++) {
            int documentX = position.x + x;
            if (documentX < 0 || documentX >= composite.getWidth()) {
               continue;
            }
            char ch = content.get(x, y);
            if (ch != ' ' || layer.isOpaque()) {
               composite.setForce(documentX, documentY, ch);
            }
         }
      }
   }

   private boolean isInsideDocument(int x, int y) {
      Dimension size = this.getSize();
      return x >= 0 && y >= 0 && x < size.width && y < size.height;
   }

   private int getInsertIndexAbove(String layerId) {
      SecondaryLayer secondaryLayer = this.findSecondaryLayer(layerId);
      return secondaryLayer == null ? 0 : this.secondaryLayers.indexOf(secondaryLayer) + 1;
   }

   private Layer findLayer(String layerId) {
      if (this.documentLayer.getId().equals(layerId)) {
         return this.documentLayer;
      }
      return this.findSecondaryLayer(layerId);
   }

   private SecondaryLayer findSecondaryLayer(String layerId) {
      for (SecondaryLayer layer : this.secondaryLayers) {
         if (layer.getId().equals(layerId)) {
            return layer;
         }
      }
      return null;
   }

   private String createLayerId() {
      return "layer-" + this.nextLayerNumber;
   }

   private void ensureNextLayerNumberAfter(String layerId) {
      String prefix = "layer-";
      if (!layerId.startsWith(prefix)) {
         return;
      }
      try {
         int layerNumber = Integer.parseInt(layerId.substring(prefix.length()));
         if (layerNumber >= this.nextLayerNumber) {
            this.nextLayerNumber = layerNumber + 1;
         }
      } catch (NumberFormatException exception) {
         // Non-standard ids are valid; they just do not affect generated ids.
      }
   }
}
