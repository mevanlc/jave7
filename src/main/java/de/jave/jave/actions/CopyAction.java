package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.Selection;
import de.jave.jave.actions.enablestrategy.AnyEditorEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.BooleanPreferenceModel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Point;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class CopyAction extends AbstractJaveAction {
   private final BooleanPreferenceModel selectionlessCutCopyOnCellModel;

   public CopyAction(JaveMainPanel mainPanel, BooleanPreferenceModel selectionlessCutCopyOnCellModel) {
      super(mainPanel, "Copy", DisyCommonsSwingIconResources.COPY);
      this.setAcceleratorKey(JaveKeyBindings.COPY);
      this.setToolTipText("Copy");
      this.selectionlessCutCopyOnCellModel = selectionlessCutCopyOnCellModel;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveClipboardSelection sel;
      if (editor.getPlate().hasSelection()) {
         Selection selection = editor.getPlate().getSelection();
         sel = new JaveClipboardSelection(selection.getContent(), selection.getMask());
      } else if (this.selectionlessCutCopyOnCellModel.getValue()) {
         Point cursor = editor.getPlate().getDocument().getCursorLocation();
         CharacterPlate singleChar = new CharacterPlate(1, 1);
         singleChar.set(0, 0, editor.getPlate().getChar(cursor.x, cursor.y));
         sel = new JaveClipboardSelection(singleChar);
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
