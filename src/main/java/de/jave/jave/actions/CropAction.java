package de.jave.jave.actions;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.IToolManager;
import de.jave.jave.JaveMessages;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.browser.IJaveDocumentTypeVisitor;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.selection.SelectionAlgorithms;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;

public class CropAction extends AbstractJaveAction {
   private final IToolManager toolManager;

   public CropAction(IToolManager toolManager, JaveMainPanel mainPanel) {
      super(mainPanel, "Crop", null);
      this.toolManager = toolManager;
      this.setAcceleratorKey(JaveKeyBindings.CROP);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(final Component parentComponent, final IDocumentEditor editor) {
      editor.getType().accept(new IJaveDocumentTypeVisitor<Void>() {
         public Void visitText(JaveDocumentType type) {
            CropAction.this.cropTextDocument(parentComponent, editor);
            return null;
         }

         public Void visitGame(JaveDocumentType type) {
            throw new UnsupportedOperationException();
         }

         public Void visitAnimation(JaveDocumentType type) {
            CropAction.this.cropAnimation(parentComponent, editor);
            return null;
         }
      });
   }

   private void cropAnimation(Component parentComponent, IDocumentEditor editor) {
      AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
      AnimationEditorModel model = animationEditor.getModel();
      if (editor.getPlate().hasSelection()) {
         Rectangle cropBounds = editor.getPlate().getSelectionRegion();
         SelectionAlgorithms.dropSelection(editor);
         model.cropAllFrames(cropBounds);
         this.getMainPanel().saveCurrentState("crop");
      } else {
         MinInsetsBuilder builder = new MinInsetsBuilder();

         for (int i = 0; i < model.getAnimationFile().getFrameCount(); i++) {
            JaveAnimationFrame frame = model.getAnimationFile().getFrame(i);
            CharacterPlate characterPlate = new CharacterPlate(frame.getContent());
            if (!characterPlate.isEmpty()) {
               Insets emptyInsets = characterPlate.getEmptyInsets();
               builder.add(emptyInsets);
            }
         }

         Insets insets = builder.getInsets();
         if (insets == null) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message(JaveMessages.JavE, "The animation is empty.\nCropping can not be applied.", MessageType.INFORMATION)
            );
            return;
         }

         Dimension size = model.getAnimationFile().getFrame(0).getSize();
         Rectangle cropBounds = new Rectangle(insets.left, insets.top, size.width - insets.left - insets.right, size.height - insets.top - insets.bottom);
         model.cropAllFrames(cropBounds);
         this.getMainPanel().saveCurrentState("crop");
      }
   }

   private void cropTextDocument(Component parentComponent, IDocumentEditor editor) {
      JaveMainPanel mainPanel = this.getMainPanel();
      if (editor.getPlate().hasSelection()) {
         editor.getPlate().crop();
         mainPanel.getCurrentTool().checkSize();
         mainPanel.saveCurrentState("crop");
         this.toolManager.switchToTextTool(0, 0);
      } else {
         if (editor.getPlate().isEmpty()) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message(JaveMessages.JavE, "The current document is empty.\nCropping can not be applied.", MessageType.INFORMATION)
            );
            return;
         }

         editor.getPlate().crop();
         mainPanel.getCurrentTool().checkSize();
         mainPanel.saveCurrentState("crop");
      }
   }
}
