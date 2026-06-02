package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.BooleanPreferenceModel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.resources.DisyCommonsSwingIconResources;

public class PasteAsNewSelectionAction extends AbstractJaveAction {
   private final JavEApplication jave;
   private final BooleanPreferenceModel pasteVFillsSelectionModel;

   public PasteAsNewSelectionAction(JaveMainPanel mainPanel, JavEApplication jave, BooleanPreferenceModel pasteVFillsSelectionModel) {
      super(mainPanel, "Paste As New Selection", DisyCommonsSwingIconResources.PASTE);
      this.setAcceleratorKey(JaveKeyBindings.PASTE_AS_NEW_SELECTION);
      this.setToolTipText("Paste As New Selection");
      Ensure.ensureArgumentNotNull(jave);
      this.jave = jave;
      this.pasteVFillsSelectionModel = pasteVFillsSelectionModel;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (FocusedTextClipboardDelegate.tryHandle(FocusedTextClipboardDelegate.Op.PASTE)) {
         return;
      }
      JaveClipboardSelection sel = ClipboardTransferer.getClipboardContent();
      if (sel != null) {
         JaveMainPanel mainPanel = this.getMainPanel();
         if (mainPanel.getDocument() == null) {
            this.jave.pasteAsNewDocument(sel.getContent());
         } else if (this.pasteVFillsSelectionModel.getValue() && mainPanel.hasSelection()) {
            CharacterPlate clipContent = sel.getContent();
            int clipW = clipContent.getWidth();
            int clipH = clipContent.getHeight();
            Rectangle region = mainPanel.getSelectionRegion();
            CharacterPlate newContent = new CharacterPlate(region.width, region.height);
            for (int dy = 0; dy < region.height; dy++) {
               for (int dx = 0; dx < region.width; dx++) {
                  newContent.set(dx, dy, clipContent.get(dx % clipW, dy % clipH));
               }
            }
            editor.getPlate().getSelection().set(new Rectangle(region.x, region.y, region.width, region.height), newContent);
            editor.getPlate().repaint();
            mainPanel.saveCurrentState("paste into selection");
         } else {
            Point location = mainPanel.getPasteLocation();
            mainPanel.pasteAsNewSelection(sel, location);
            this.jave.switchToSelectonTool();
            this.jave.updateSelectionMenu();
            mainPanel.saveCurrentState("paste");
         }
      }
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return new IJaveDocumentEditorActionEnabledStrategy() {
         @Override
         public boolean isEnabledFor(IDocumentEditor activeEditor) {
            return activeEditor == null || TextAndAnimationEditorEnabledStrategy.getInstance().isEnabledFor(activeEditor);
         }
      };
   }
}
