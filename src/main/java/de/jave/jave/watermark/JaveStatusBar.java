package de.jave.jave.application;

import de.jave.gui.GStatusLabel;
import de.jave.gui.IMouseClickHandler;
import de.jave.gui.StatusBar;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.ZoomableFontModel;
import de.jave.jave.actions.ResizeDocumentAction;
import de.jave.jave.plate.ActiveEditorModel;
import de.jave.jave.plate.IDocumentEditor;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.button.RolloverButtonFactory;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class JaveStatusBar {
   private final GStatusLabel sizeLabel;
   private final GStatusLabel lInsert;
   private final JToggleButton toggleOptionsDialogButton;
   private final JPanel content;
   private final ActiveEditorModel activeEditorModel;

   public JaveStatusBar(
      final JavEApplication jave,
      FontModel fontModel,
      final ActiveEditorModel activeEditorModel,
      StatusBar statusBar,
      final ResizeDocumentAction resizeAction,
      SmartToggleAction toolOptionsDialogToggleAction
   ) {
      Ensure.ensureArgumentNotNull(jave);
      Ensure.ensureArgumentNotNull(fontModel);
      Ensure.ensureArgumentNotNull(activeEditorModel);
      Ensure.ensureArgumentNotNull(resizeAction);
      Ensure.ensureArgumentNotNull(toolOptionsDialogToggleAction);
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
      this.toggleOptionsDialogButton = RolloverButtonFactory.createToggleButton(toolOptionsDialogToggleAction);
      this.toggleOptionsDialogButton.setPreferredSize(new Dimension(24, 19));
      this.toggleOptionsDialogButton.setText(null);
      activeEditorModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveStatusBar.this.updateSizeLabelToDocumentSize();
         }
      });
      this.updateSizeLabelToDocumentSize();
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
      JPanel panel = new JPanel(new GridDialogLayout(7, false));
      panel.add(statusBar, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new FontLabel(fontModel).getContent());
      panel.add(new ZoomLabel(zoomableFontModelModel).getContent());
      panel.add(this.sizeLabel);
      panel.add(this.lInsert);
      panel.add(this.toggleOptionsDialogButton);
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

   public void setInsert(boolean insert) {
      this.lInsert
         .setText(insert ? JaveMessages.Control_InsertOverwriteLabel_InsertAbbreviation : JaveMessages.Control_InsertOverwriteLabel_OverwriteAbbreviation);
   }

   public JComponent getToggleOptionsDialogButton() {
      return this.toggleOptionsDialogButton;
   }
}
