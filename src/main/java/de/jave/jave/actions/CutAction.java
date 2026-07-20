package de.jave.jave.actions;

import de.jave.awt.clipboard.ClipboardTransferer;
import de.jave.awt.clipboard.JaveClipboardSelection;
import de.jave.jave.Plate;
import de.jave.jave.Selection;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.BooleanPreferenceModel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Point;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;
import net.dizzy.commons.swing.resources.DizzyCommonsSwingIconResources;

public class CutAction extends AbstractJaveAction {
   private final BooleanPreferenceModel selectionlessCutCopyOnCellModel;

   public CutAction(JaveMainPanel mainPanel, BooleanPreferenceModel selectionlessCutCopyOnCellModel) {
      super(mainPanel, "Cut", DizzyCommonsSwingIconResources.CUT);
      this.setAcceleratorKey(JaveKeyBindings.CUT);
      this.setToolTipText("Cut");
      this.selectionlessCutCopyOnCellModel = selectionlessCutCopyOnCellModel;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (FocusedTextClipboardDelegate.tryHandle(FocusedTextClipboardDelegate.Op.CUT)) {
         return;
      }
      Plate plate = editor.getPlate();
      JaveClipboardSelection s;
      if (!plate.hasSelection()) {
         if (this.selectionlessCutCopyOnCellModel.getValue()) {
            Point cursor = plate.getDocument().getCursorLocation();
            CharacterPlate singleChar = new CharacterPlate(1, 1);
            singleChar.set(0, 0, plate.getChar(cursor.x, cursor.y));
            ClipboardTransferer.setClipboardContent(new JaveClipboardSelection(singleChar));
            plate.setCharForce(cursor, ' ');
            plate.saveCurrentState("cut");
            return;
         }
         s = new JaveClipboardSelection(plate.getContent());
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
