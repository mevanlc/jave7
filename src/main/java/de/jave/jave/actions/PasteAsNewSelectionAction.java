package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import java.awt.Point;
import javax.swing.KeyStroke;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class PasteAsNewSelectionAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public PasteAsNewSelectionAction(JaveMainPanel mainPanel, JavEApplication jave) {
      super(mainPanel, "Paste As New Selection", DisyCommonsSwingIconResources.PASTE);
      this.setAcceleratorKey(KeyStroke.getKeyStroke(86, 128));
      this.setToolTipText("Paste As New Selection");
      Ensure.ensureArgumentNotNull(jave);
      this.jave = jave;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveClipboardSelection sel = ClipboardTransferer.getClipboardContent();
      if (sel != null) {
         JaveMainPanel mainPanel = this.getMainPanel();
         if (mainPanel.getDocument() == null) {
            this.jave.pasteAsNewDocument(sel.getContent());
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
