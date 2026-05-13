package de.jave.jave.application;

import de.jave.gui.GStatusLabel;
import de.jave.gui.IMouseClickHandler;
import de.jave.gui.StatusBar;
import de.jave.jave.DocumentListener;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.PlateDocument;
import de.jave.jave.ZoomableFontModel;
import de.jave.jave.actions.ResizeDocumentAction;
import de.jave.jave.plate.ActiveEditorModel;
import de.jave.jave.plate.IDocumentEditor;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class JaveStatusBar {
   private final GStatusLabel sizeLabel;
   private final GStatusLabel layerCountLabel;
   private final GStatusLabel layerRemoveLabel;
   private final GStatusLabel layerAddLabel;
   private final JLabel layerSeparatorLabel;
   private final GStatusLabel currentLayerLabel;
   private final GStatusLabel lInsert;
   private final JPanel content;
   private final ActiveEditorModel activeEditorModel;
   private PlateDocument layerLabelDocument;
   private final DocumentListener layerDocumentListener = new DocumentListener() {
      @Override
      public void documentClosing() {
         JaveStatusBar.this.updateLayerLabels();
      }

      @Override
      public void documentHiding() {
         JaveStatusBar.this.updateLayerLabels();
      }

      @Override
      public void documentShowing() {
         JaveStatusBar.this.updateLayerLabels();
      }

      @Override
      public void documentChanged() {
         JaveStatusBar.this.updateLayerLabels();
      }
   };

   public JaveStatusBar(
      final JavEApplication jave,
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
      activeEditorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveStatusBar.this.updateSizeLabelToDocumentSize();
            JaveStatusBar.this.updateLayerDocumentListener();
            JaveStatusBar.this.updateLayerLabels();
         }
      });
      this.updateSizeLabelToDocumentSize();
      this.layerCountLabel = new GStatusLabel("", null);
      this.layerRemoveLabel = new GStatusLabel("[-]", null);
      this.layerRemoveLabel.setToolTipText("Layer delete is not implemented yet");
      this.layerAddLabel = new GStatusLabel("[+]", new IMouseClickHandler() {
         @Override
         public void handleMouseClicked() {
            jave.addSecondaryLayerAndActivate();
            JaveStatusBar.this.updateLayerDocumentListener();
            JaveStatusBar.this.updateLayerLabels();
            JaveStatusBar.this.focusActiveEditor();
         }
      });
      this.layerAddLabel.setToolTipText("Add layer");
      this.layerSeparatorLabel = new JLabel("|");
      this.currentLayerLabel = new GStatusLabel("", new IMouseClickHandler() {
         @Override
         public void handleMouseClicked() {
            jave.activateNextLayer();
            JaveStatusBar.this.updateLayerLabels();
            JaveStatusBar.this.focusActiveEditor();
         }
      });
      this.currentLayerLabel.setToolTipText("Click to cycle current layer");
      this.updateLayerDocumentListener();
      this.updateLayerLabels();
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
      JPanel panel = new JPanel(new GridDialogLayout(11, false));
      panel.add(statusBar, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new FontLabel(fontModel).getContent());
      panel.add(new ZoomLabel(zoomableFontModelModel).getContent());
      panel.add(this.sizeLabel);
      panel.add(this.layerCountLabel);
      panel.add(this.layerRemoveLabel);
      panel.add(this.layerAddLabel);
      panel.add(this.layerSeparatorLabel);
      panel.add(this.currentLayerLabel);
      panel.add(this.lInsert);
      panel.add(new Gap(12, 1));
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

   private PlateDocument getActiveDocument() {
      IDocumentEditor editor = this.activeEditorModel.getActiveEditor();
      return editor == null ? null : editor.getPlate().getDocument();
   }

   private void updateLayerDocumentListener() {
      PlateDocument document = this.getActiveDocument();
      if (this.layerLabelDocument == document) {
         return;
      }
      if (this.layerLabelDocument != null) {
         this.layerLabelDocument.removeDocumentListener(this.layerDocumentListener);
      }
      this.layerLabelDocument = document;
      if (this.layerLabelDocument != null) {
         this.layerLabelDocument.addDocumentListener(this.layerDocumentListener);
      }
   }

   private void updateLayerLabels() {
      PlateDocument document = this.getActiveDocument();
      if (document == null) {
         this.layerCountLabel.setText("Layer count:  ");
         this.currentLayerLabel.setText("Current layer:  ");
         return;
      }
      this.layerCountLabel.setText("Layer count: " + document.getLayerCount());
      this.currentLayerLabel.setText("Current layer: " + document.getActiveLayerNumber());
   }

   private void focusActiveEditor() {
      IDocumentEditor activeEditor = this.activeEditorModel.getActiveEditor();
      if (activeEditor != null) {
         activeEditor.getPlate().requestFocus();
      }
   }

   public void setInsert(boolean insert) {
      this.lInsert
         .setText(insert ? JaveMessages.Control_InsertOverwriteLabel_InsertAbbreviation : JaveMessages.Control_InsertOverwriteLabel_OverwriteAbbreviation);
   }
}
