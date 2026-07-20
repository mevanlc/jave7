package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Rectangle;
import net.dizzy.commons.swing.resources.DizzyCommonsSwingIconResources;

public class PasteIntoSelectionAction extends AbstractJaveAction {

   public PasteIntoSelectionAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Paste Into Selection", DizzyCommonsSwingIconResources.PASTE);
      this.setAcceleratorKey(JaveKeyBindings.PASTE_INTO_SELECTION);
      this.setToolTipText("Paste Into Selection");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (FocusedTextClipboardDelegate.tryHandle(FocusedTextClipboardDelegate.Op.PASTE)) {
         return;
      }
      if (!editor.getPlate().hasSelection()) {
         return;
      }
      JaveClipboardSelection sel = ClipboardTransferer.getClipboardContent();
      if (sel == null) {
         return;
      }
      CharacterPlate clipContent = sel.getContent();
      int clipW = clipContent.getWidth();
      int clipH = clipContent.getHeight();
      Rectangle region = editor.getPlate().getSelectionRegion();
      CharacterPlate newContent = new CharacterPlate(region.width, region.height);
      for (int dy = 0; dy < region.height; dy++) {
         for (int dx = 0; dx < region.width; dx++) {
            newContent.set(dx, dy, clipContent.get(dx % clipW, dy % clipH));
         }
      }
      editor.getPlate().getSelection().set(new Rectangle(region.x, region.y, region.width, region.height), newContent);
      editor.getPlate().repaint();
      this.getMainPanel().saveCurrentState("paste into selection");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return activeEditor -> TextAndAnimationEditorEnabledStrategy.getInstance().isEnabledFor(activeEditor)
                             && getMainPanel().hasSelection();
   }
}
