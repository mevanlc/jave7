package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.AlwaysEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class PasteAsNewDocumentAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public PasteAsNewDocumentAction(JaveMainPanel mainPanel, JavEApplication jave) {
      super(mainPanel, "Paste As New Document", DisyCommonsSwingIconResources.PASTE);
      Ensure.ensureArgumentNotNull(jave);
      this.jave = jave;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveClipboardSelection sel = ClipboardTransferer.getClipboardContent();
      if (sel != null) {
         this.jave.pasteAsNewDocument(sel.getContent());
      }
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AlwaysEnabledStrategy.getInstance();
   }
}
