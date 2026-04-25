package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.Plate;
import de.jave.jave.Selection;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;
import net.disy.commons.swing.resources.DisyCommonsSwingIconResources;

public class CutAction extends AbstractJaveAction {
   public CutAction(JaveMainPanel mainPanel) {
      super(mainPanel, "Cut", DisyCommonsSwingIconResources.CUT);
      this.setAcceleratorKey(JaveKeyBindings.CUT);
      this.setToolTipText("Cut");
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      Plate plate = editor.getPlate();
      JaveClipboardSelection s;
      if (!plate.hasSelection()) {
         s = new JaveClipboardSelection(plate.getDocument().getContent());
         plate.clear();
         this.getToolManager().getCurrentTool().reset();
      } else {
         Selection selection = plate.getSelection();
         s = new JaveClipboardSelection(selection.getContent(), selection.getMask());
         plate.unselect();
         this.getToolManager().getCurrentTool().setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      }

      ClipboardTransferer.setClipboardContent(s);
      plate.saveCurrentState("cut");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }
}
