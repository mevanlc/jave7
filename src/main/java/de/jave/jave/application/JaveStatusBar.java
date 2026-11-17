package de.jave.jave.application;

import de.jave.gui.GStatusLabel;
import de.jave.gui.IMouseClickHandler;
import de.jave.gui.StatusBar;
import de.jave.jave.JaveApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.Selection;
import de.jave.jave.ZoomableFontModel;
import de.jave.jave.actions.ResizeDocumentAction;
import de.jave.jave.plate.ActiveEditorModel;
import de.jave.jave.plate.IDocumentEditor;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class JaveStatusBar {
   private final GStatusLabel sizeLabel;
   private final GStatusLabel selectionSizeLabel;
   private Selection currentSelection;
   private final IChangeListener selectionChangeListener = this::updateSelectionSizeLabel;
   private final GStatusLabel lInsert;
   private final JPanel content;
   private final ActiveEditorModel activeEditorModel;

   public JaveStatusBar(
      final JaveApplication jave,
      FontModel fontModel,
      final ActiveEditorModel activeEditorModel,
      StatusBar statusBar,
      final ResizeDocumentAction resizeAction
   ) {
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(fontModel);
      Ensure.ensureArgumentNotNull(activeEditorModel);
      Ensure.ensureArgumentNotNull(resizeAction);
      this.activeEditorModel = activeEditorModel;
      this.sizeLabel = new GStatusLabel("", new IMouseClickHandler() {
         @Override
         public void handleMouseClicked() {
            if (resizeAction.isEnabled()) {
               resizeAction.execute(JaveStatusBar.this.sizeLabel);
            }
         }
      });
      this.sizeLabel.setToolTipText(JaveMessages.Control_SizeLabel_Tooltip);
      this.selectionSizeLabel = new GStatusLabel("", null);
      this.selectionSizeLabel.setToolTipText("Selection bounding width and height in character cells");
      activeEditorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveStatusBar.this.updateSizeLabelToDocumentSize();
            JaveStatusBar.this.updateSelectionModel();
         }
      });
      this.updateSizeLabelToDocumentSize();
      this.updateSelectionModel();
      this.lInsert = new GStatusLabel(JaveMessages.Control_InsertOverwriteLabel_OverwriteAbbreviation, new IMouseClickHandler() {
         @Override
         public void handleMouseClicked() {
            jave.toggleInsert();
            IDocumentEditor activeEditor = activeEditorModel.getActiveEditor();
            if (activeEditor != null) {
               activeEditor.getPlate().requestFocus();
            }
         }
      });
      this.lInsert.setToolTipText(JaveMessages.Control_InsertOverwriteLabel_Tooltip);
      final ObjectModel<ZoomableFontModel> zoomableFontModelModel = new ObjectModel<>();
      activeEditorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveStatusBar.this.updateFontZoomModel(zoomableFontModelModel);
         }
      });
      this.updateFontZoomModel(zoomableFontModelModel);
      JPanel panel = new JPanel(new BorderLayout(0, 0));
      JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
      detailsPanel.add(new FontLabel(fontModel).getContent());
      detailsPanel.add(new ZoomLabel(zoomableFontModelModel).getContent());
      detailsPanel.add(this.selectionSizeLabel);
      detailsPanel.add(this.sizeLabel);
      detailsPanel.add(this.lInsert);
      panel.add(statusBar, BorderLayout.CENTER);
      panel.add(detailsPanel, BorderLayout.EAST);
      this.content = panel;
   }

   private void updateFontZoomModel(ObjectModel<ZoomableFontModel> zoomableFontModelModel) {
      IDocumentEditor editor = this.activeEditorModel.getActiveEditor();
      if (editor == null) {
         zoomableFontModelModel.setValue(null);
      } else {
         ZoomableFontModel model = editor.getPlate().getZoomableFontModel();
         zoomableFontModelModel.setValue(model);
      }
   }

   private void updateSelectionModel() {
      if (this.currentSelection != null) {
         this.currentSelection.removeChangeListener(this.selectionChangeListener);
      }
      IDocumentEditor editor = this.activeEditorModel.getActiveEditor();
      this.currentSelection = editor == null ? null : editor.getPlate().getSelection();
      if (this.currentSelection != null) {
         this.currentSelection.addChangeListener(this.selectionChangeListener);
      }
      this.updateSelectionSizeLabel();
   }

   private void updateSelectionSizeLabel() {
      boolean hasSelection = this.currentSelection != null && this.currentSelection.hasSelection();
      this.selectionSizeLabel.setText(hasSelection
         ? "Selection: " + this.currentSelection.getWidth() + " x " + this.currentSelection.getHeight()
         : "");
      this.selectionSizeLabel.setVisible(hasSelection);
   }

   public JComponent getContent() {
      return this.content;
   }

   public void updateSizeLabelToDocumentSize() {
      IDocumentEditor editor = this.activeEditorModel.getActiveEditor();
      Dimension size = editor == null ? null : editor.getPlate().getDocumentSize();
      String text;
      if (size == null) {
         text = "      ";
      } else {
         text = size.width + " x " + size.height;
      }

      this.sizeLabel.setText(text);
   }

   public void setInsert(boolean insert) {
      this.lInsert
         .setText(insert ? JaveMessages.Control_InsertOverwriteLabel_InsertAbbreviation : JaveMessages.Control_InsertOverwriteLabel_OverwriteAbbreviation);
   }
}
