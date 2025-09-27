package de.jave.jave.plate;

import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.CompressedDocumentState;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveSelection;
import de.jave.jave.Plate;
import de.jave.jave.PlateDocument;
import de.jave.jave.SelectionTool;
import de.jave.jave.Tool;
import de.jave.jave.browser.JaveDocumentTypeUi;
import de.jave.jave.plate.selection.SelectionAlgorithms;
import de.jave.jave.watermark.IWatermarkPainter;
import de.jave.lib.CharacterPlate;
import de.jave.lib.gui.IStatusDisplay;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.tabbed.ISmartTabbedPaneCloseHandler;
import net.disy.commons.swing.dialog.tabbed.SmartTabbedPane;

public class JaveMainPanel {
   private final ToolManager toolManager = new ToolManager();
   private final List<IDocumentEditor> editors = new LinkedList<>();
   private final SmartTabbedPane tabbedPane;
   private final IStatusDisplay status;
   private final MouseCharacterModel mouseCharacterModel;
   private final List<IWatermarkPainter> watermarkPainters = new ArrayList<>();
   private final ActiveEditorModel activeEditorModel = new ActiveEditorModel();
   private final BooleanModel mixCharactersModel = new BooleanModel(true);

   public JaveMainPanel(final JavEApplication jave, IStatusDisplay status, MouseCharacterModel mouseCharacterModel) {
      Ensure.ensureArgumentNotNull(mouseCharacterModel);
      this.status = status;
      this.mouseCharacterModel = mouseCharacterModel;
      this.tabbedPane = new SmartTabbedPane(new ISmartTabbedPaneCloseHandler() {
         @Override
         public void handleTabClosing(SmartTabbedPane tabbedPane, int tabIndex) {
            jave.setCurrentDocument(tabIndex);
            jave.doClose(tabbedPane.getContent());
         }
      });
      this.tabbedPane.addTabSelectionChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveMainPanel.this.activeEditorModel.setActiveEditor(JaveMainPanel.this.getEditor());
         }
      });
      this.activeEditorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveMainPanel.this.requestFocus();
         }
      });
   }

   public synchronized void addEditor(IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      plate.setStatusDisplay(this.status);

      for (IWatermarkPainter painter : this.watermarkPainters) {
         plate.addWatermarkPainter(painter);
      }

      Icon icon = new JaveDocumentTypeUi().getIcon(editor.getType());
      this.tabbedPane.addTab("", icon, editor.getContent());
      this.editors.add(editor);
      this.updateAllDocumentTitles();
      this.activeEditorModel.setActiveEditor(editor);
   }

   public void updateAllDocumentTitles() {
      for (int i = 0; i < this.getEditorCount(); i++) {
         IDocumentEditor editor = this.getEditor(i);
         String title = DocumentEditorTitleFactory.createEditorTabTitle(editor);
         this.tabbedPane.setTitleAt(i, title);
      }
   }

   public Tool getCurrentTool() {
      return this.toolManager.getCurrentTool();
   }

   public void setCurrentTool(Tool tool) {
      if (this.getCurrentTool() != tool) {
         if (this.getCurrentTool() != null) {
            this.getCurrentTool().putAside(tool instanceof SelectionTool);
         }

         this.toolManager.setCurrentTool(tool);
         tool.takeToHand();
         this.requestFocus();
         Plate plate = this.getPlate();
         if (plate != null) {
            plate.setCursor(tool.getCursor());
            if (plate.getSize().height != 0) {
               plate.repaint();
            }
         }
      }
   }

   public JComponent getContent() {
      return this.tabbedPane.getContent();
   }

   public synchronized PlateDocument getDocument() {
      Plate plate = this.getPlate();
      return plate == null ? null : plate.getDocument();
   }

   public void saveCurrentState(String string) {
      this.getPlate().saveCurrentState(string);
   }

   public void repaint() {
      Plate plate = this.getPlate();
      if (plate != null) {
         plate.repaint();
      }
   }

   public void requestFocus() {
      Plate plate = this.getPlate();
      if (plate != null) {
         plate.requestFocus();
      }
   }

   public boolean hasSelection() {
      Plate plate = this.getPlate();
      return plate == null ? false : plate.hasSelection();
   }

   public synchronized void setCurrentEditor(int index) {
      this.tabbedPane.setSelectedTabIndex(index);
   }

   public void selectAll() {
      SelectionAlgorithms.selectAll(this.getEditor());
   }

   public void unselect() {
      this.getPlate().unselect();
   }

   public void shrinkSelection() {
      SelectionAlgorithms.shrinkSelection(this.getEditor());
   }

   public void expandSelection() {
      SelectionAlgorithms.expandSelection(this.getEditor());
   }

   public CharacterPlate getSelectionContent() {
      return this.getPlate().getSelectionContent();
   }

   public void setContentOfInterest(JaveSelection sel) {
      this.getPlate().setContentOfInterest(sel);
      this.getCurrentTool().checkSize();
   }

   public void revalidate() {
      this.getPlate().revalidate();
   }

   public Rectangle getSelectionRegion() {
      return this.getPlate().getSelectionRegion();
   }

   public void dropSelection() {
      SelectionAlgorithms.dropSelection(this.getEditor());
   }

   public void setSelection(Rectangle braceRegion, CharacterPlate cp) {
      this.getPlate().setSelection(braceRegion, cp);
   }

   public boolean isEmpty() {
      return this.getPlate().isEmpty();
   }

   public synchronized Plate getPlate() {
      IDocumentEditor editor = this.getEditor();
      return editor == null ? null : editor.getPlate();
   }

   public synchronized IDocumentEditor getEditor() {
      if (this.editors.size() == 0) {
         return null;
      } else {
         int index = this.tabbedPane.getSelectedTabIndex();
         return index == -1 ? null : this.editors.get(index);
      }
   }

   public boolean canUndo() {
      Plate plate = this.getPlate();
      return plate == null ? false : plate.canUndo();
   }

   public boolean canRedo() {
      Plate plate = this.getPlate();
      return plate == null ? false : plate.canRedo();
   }

   public String getUndoActionName() {
      return this.getPlate().getUndoActionName();
   }

   public String getRedoActionName() {
      return this.getPlate().getRedoActionName();
   }

   public void redo() {
      this.getPlate().redo();
   }

   public void undo() {
      this.getPlate().undo();
   }

   public boolean hasDocument() {
      return this.getPlate().hasDocument();
   }

   public void setPlateSize(int newWidth, int newHeight) {
      this.getPlate().setPlateSize(newWidth, newHeight);
   }

   public CompressedDocumentState getDocumentState(String actionName) {
      return this.getPlate().getDocumentState(actionName);
   }

   public JaveSelection getContentOfInterest() {
      return this.getPlate().getContentOfInterest();
   }

   public void setSelectionContent(CharacterPlate cp) {
      this.getPlate().setSelectionContent(cp);
   }

   public void setContentOfInterest(CharacterPlate cp) {
      if (this.getPlate().hasSelection()) {
         this.getPlate().setSelectionContent(cp);
      } else {
         this.getPlate().setContent(cp);
      }

      this.getCurrentTool().checkSize();
   }

   public void pasteAsNewSelection(CharacterPlate content, Point location) {
      SelectionAlgorithms.pasteAsNewSelection(this.getEditor(), content, location);
   }

   public void pasteAsNewSelection(JaveClipboardSelection selection, Point location) {
      SelectionAlgorithms.pasteAsNewSelection(this.getEditor(), selection, location);
   }

   public Point getPasteLocation() {
      Plate plate = this.getPlate();
      return plate == null ? new Point(0, 0) : plate.getPasteLocation();
   }

   public void repaintCursor() {
      Plate plate = this.getPlate();
      if (plate != null) {
         plate.repaintCursor();
      }
   }

   public synchronized void closeCurrentEditor() {
      int index = this.tabbedPane.getSelectedTabIndex();
      IDocumentEditor editor = this.editors.remove(index);
      editor.dispose();
      this.tabbedPane.removeTab(index);
      this.activeEditorModel.setActiveEditor(this.getEditor());
   }

   public void showStatus(String text) {
      this.status.showStatus(text);
   }

   public ToolManager getToolManager() {
      return this.toolManager;
   }

   public Dimension getDocumentSize() {
      PlateDocument document = this.getDocument();
      return document == null ? null : document.getSize();
   }

   public MouseCharacterModel getMouseCharacterModel() {
      return this.mouseCharacterModel;
   }

   public void addWatermarkPainter(IWatermarkPainter painter) {
      this.watermarkPainters.add(painter);
   }

   public ActiveEditorModel getActiveEditorModel() {
      return this.activeEditorModel;
   }

   public BooleanModel getMixCharactersModel() {
      return this.mixCharactersModel;
   }

   public IDocumentEditor getEditor(int index) {
      return this.editors.get(index);
   }

   public int getEditorCount() {
      return this.editors.size();
   }
}
