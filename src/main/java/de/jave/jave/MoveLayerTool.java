package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.layers.LayeredDocument;
import de.jave.jave.layers.SecondaryLayer;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.selection.SelectionAlgorithms;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.undo.UndoState;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public final class MoveLayerTool extends Tool {
   private Plate dragPlate;
   private SecondaryLayer dragLayer;
   private UndoState dragState;
   private Point2d dragStart;
   private Point originalPosition;
   private LayeredDocument preview;
   private IInlineToolOptions inlineOptions;

   public MoveLayerTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
      mainPanel.getActiveEditorModel().addChangeListener(this::cancelDrag);
   }

   @Override
   public String getName() {
      return "Move Layer";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_MOVE_LAYER_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         JPanel panel = new JPanel();
         panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
         panel.add(new JLabel("<html><b>Move Layer</b><br><br>Drag on the canvas to move<br>the active secondary layer.<br><br>"
            + "Arrow keys: 1 cell<br>Shift + arrows: 10 cells<br>Esc: cancel drag</html>"));
         this.inlineOptions = () -> panel;
      }
      return this.inlineOptions;
   }

   @Override
   public void takeToHand() {
      this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
      this.dropSelection();
      this.showStatus("Drag to move the active secondary layer; arrow keys nudge, Esc cancels.");
   }

   private void dropSelection() {
      Plate plate = this.getPlate();
      if (plate != null && plate.hasSelection()) {
         SelectionAlgorithms.dropSelection(this.getEditor());
         plate.saveCurrentState("drop selection");
      }
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.cancelDrag();
   }

   @Override
   public void focusLost() {
      this.cancelDrag();
   }

   @Override
   public void prepareForSave() {
      this.cancelDrag();
   }

   @Override
   public LayeredDocument getDocumentPreview(PlateDocument document) {
      return this.isValidDrag() && this.dragPlate.getDocument() == document ? this.preview : null;
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent event) {
      if (!SwingUtilities.isLeftMouseButton(event) || event.isPopupTrigger() || location == null) {
         return;
      }
      this.cancelDrag();
      SecondaryLayer layer = this.getMovableLayer();
      if (layer == null) {
         return;
      }
      this.dragPlate = this.getPlate();
      this.dragLayer = layer;
      this.dragState = this.getDocument().getUndoManager().getCurrentState();
      this.dragStart = this.dragPlate.getRealLocationForScreenPoint(point);
      this.originalPosition = layer.getPosition();
      this.preview = this.getDocument().getLayeredDocument().snapshot().restore();
      this.showPosition(this.originalPosition);
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent event) {
      if (this.isValidDrag()) {
         this.updatePreview(point);
      } else {
         this.cancelDrag();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent event) {
      if (!SwingUtilities.isLeftMouseButton(event)) {
         return;
      }
      if (!this.isValidDrag()) {
         this.cancelDrag();
         return;
      }
      this.updatePreview(point);
      Point position = this.preview.getActiveSecondaryLayer().getPosition();
      boolean moved = !position.equals(this.originalPosition);
      Plate plate = this.dragPlate;
      SecondaryLayer layer = this.dragLayer;
      this.cancelDrag();
      if (moved) {
         layer.setPosition(position);
         plate.saveCurrentState("move layer");
         plate.repaint();
         this.showPosition(position);
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent event) {
      if (code == KeyEvent.VK_ESCAPE) {
         this.cancelDrag();
         event.consume();
         return;
      }
      if (event.isControlDown() || event.isAltDown() || event.isMetaDown()) {
         return;
      }
      int step = event.isShiftDown() ? 10 : 1;
      Point delta = switch (code) {
         case KeyEvent.VK_LEFT -> new Point(-step, 0);
         case KeyEvent.VK_RIGHT -> new Point(step, 0);
         case KeyEvent.VK_UP -> new Point(0, -step);
         case KeyEvent.VK_DOWN -> new Point(0, step);
         default -> null;
      };
      if (delta == null) {
         return;
      }
      event.consume();
      if (this.preview != null) {
         return;
      }
      SecondaryLayer layer = this.getMovableLayer();
      if (layer != null) {
         Point position = layer.getPosition();
         position.translate(delta.x, delta.y);
         layer.setPosition(position);
         this.saveCurrentState("move layer");
         this.repaintAll();
         this.showPosition(position);
      }
   }

   private SecondaryLayer getMovableLayer() {
      this.dropSelection();
      PlateDocument document = this.getDocument();
      SecondaryLayer layer = document == null ? null : document.getActiveSecondaryLayer();
      if (layer == null) {
         this.showStatus("Select a secondary layer to move; the document layer stays fixed.");
         return null;
      }
      if (layer.getContent().getWidth() == 0 || layer.getContent().getHeight() == 0) {
         this.showStatus("The active layer is empty.");
         return null;
      }
      return layer;
   }

   private boolean isValidDrag() {
      return this.preview != null
         && this.dragPlate == this.getPlate()
         && this.dragLayer == this.getDocument().getActiveSecondaryLayer()
         && this.dragState == this.getDocument().getUndoManager().getCurrentState()
         && this.originalPosition.equals(this.dragLayer.getPosition());
   }

   private void updatePreview(Point point) {
      Point2d location = this.dragPlate.getRealLocationForScreenPoint(point);
      Point position = new Point(
         this.originalPosition.x + (int)Math.round(location.getX() - this.dragStart.getX()),
         this.originalPosition.y + (int)Math.round(location.getY() - this.dragStart.getY())
      );
      this.preview.getActiveSecondaryLayer().setPosition(position);
      this.showPosition(position);
      this.dragPlate.repaint();
   }

   private void showPosition(Point position) {
      this.showStatus("Layer position: (" + position.x + ", " + position.y + ")");
   }

   private void cancelDrag() {
      Plate plate = this.dragPlate;
      this.dragPlate = null;
      this.dragLayer = null;
      this.dragState = null;
      this.dragStart = null;
      this.originalPosition = null;
      this.preview = null;
      if (plate != null) {
         plate.repaint();
         this.showStatus("");
      }
   }
}
