package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.Selection;
import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import javax.swing.KeyStroke;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class CopyAction extends AbstractJaveAction {
   public CopyAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Copy", DisyCommonsSwingIconResources.COPY);
      this.setAcceleratorKey(KeyStroke.getKeyStroke(67, 128));
      this.setToolTipText("Copy");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveClipboardSelection sel;
      if (editor.getPlate().hasSelection()) {
         Selection selection = editor.getPlate().getSelection();
         sel = new JaveClipboardSelection(selection.getContent(), selection.getMask());
      } else {
         sel = new JaveClipboardSelection(editor.getPlate().getDocument().getContent());
      }

      ClipboardTransferer.setClipboardContent(sel);
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AnyEditorEnabledStrategy.getInstance();
   }
}
