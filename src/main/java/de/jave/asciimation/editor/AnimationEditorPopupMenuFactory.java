package de.jave.asciimation.editor;

import de.jave.jave.icon.JaveIcons;
import de.jave.javeplayer.JaveAnimationFile;
import java.awt.Component;
import javax.swing.JPopupMenu;
import net.disy.commons.swing.action.SmartAction;

public class AnimationEditorPopupMenuFactory {
   public static JPopupMenu createFramePopupMenu(final AnimationEditorModel model) {
      final int[] selectedItems = ListSelectionUtilities.getSelectedIndices(model.getFrameSelectionModel());
      JaveAnimationFile animationFile = model.getAnimationFile();
      SmartAction deleteAction = new SmartAction("Delete", JaveIcons.ANIMATION_DELETE_FRAME_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            for (int i = selectedItems.length - 1; i >= 0; i--) {
               model.deleteFrame(selectedItems[i]);
            }
         }
      };
      deleteAction.setEnabled(selectedItems.length > 0 && selectedItems.length < animationFile.getFrameCount());
      SmartAction moveLeftAction = new SmartAction("Move left", JaveIcons.ANIMATION_MOVE_LEFT_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            model.moveFrameLeft(selectedItems[0]);
         }
      };
      moveLeftAction.setEnabled(selectedItems.length == 1 && selectedItems[0] > 0);
      SmartAction moveRightAction = new SmartAction("Move right", JaveIcons.ANIMATION_MOVE_RIGHT_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            model.moveFrameRight(selectedItems[0]);
         }
      };
      moveRightAction.setEnabled(selectedItems.length == 1 && selectedItems[0] < animationFile.getFrameCount() - 1);
      SmartAction selectAllAction = new SmartAction("Select all") {
         @Override
         protected void execute(Component parentComponent) {
            model.getFrameSelectionModel().addSelectionInterval(0, model.getAnimationFile().getFrameCount() - 1);
         }
      };
      selectAllAction.setEnabled(selectedItems.length < animationFile.getFrameCount());
      SmartAction unselectAllAction = new SmartAction("Unselect all") {
         @Override
         protected void execute(Component parentComponent) {
            model.getFrameSelectionModel().clearSelection();
         }
      };
      unselectAllAction.setEnabled(selectedItems.length > 0);
      SmartAction reverseAction = new SmartAction("Reverse", JaveIcons.ANIMATION_REVERSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            if (selectedItems != null && selectedItems.length != 0) {
               int startIndex = selectedItems[0];
               int endIndex = selectedItems[selectedItems.length - 1];
               model.doFramesRevert(startIndex, endIndex);
            }
         }
      };
      reverseAction.setEnabled(selectedItems.length > 1 && isContinuousSelection(selectedItems));
      JPopupMenu popupMenuFrames = new JPopupMenu();
      popupMenuFrames.add(deleteAction);
      popupMenuFrames.add(reverseAction);
      popupMenuFrames.addSeparator();
      popupMenuFrames.add(moveLeftAction);
      popupMenuFrames.add(moveRightAction);
      popupMenuFrames.addSeparator();
      popupMenuFrames.add(selectAllAction);
      popupMenuFrames.add(unselectAllAction);
      return popupMenuFrames;
   }

   private static final boolean isContinuousSelection(int[] selectedItems) {
      return selectedItems.length == selectedItems[selectedItems.length - 1] - selectedItems[0] + 1;
   }
}
