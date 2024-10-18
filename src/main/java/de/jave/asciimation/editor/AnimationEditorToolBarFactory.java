package de.jave.asciimation.editor;

import de.jave.asciimation.action.AddFrameAction;
import de.jave.asciimation.action.AnimationPropertiesAction;
import de.jave.asciimation.action.DeleteCurrentFrameAction;
import de.jave.asciimation.action.DuplicateCurrentFrameAction;
import de.jave.asciimation.action.ExportAnimationAction;
import de.jave.asciimation.action.NavigateFirstAction;
import de.jave.asciimation.action.NavigateLastAction;
import de.jave.asciimation.action.NavigateNextAction;
import de.jave.asciimation.action.NavigatePreviousAction;
import de.jave.asciimation.action.PlayAnimationAction;
import de.jave.jave.actions.ButtonToolbarBuilder;
import de.jave.jave.preferences.AnimationExportPreferences;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.showhide.ShowHideButton;

public class AnimationEditorToolBarFactory {
   public static JPanel createAnimationEditorToolBar(
      AnimationEditorModel model,
      BooleanModel showAdditionalControlsModel,
      FileModel currentDirectoryModel,
      FontModel displayFontModel,
      AnimationExportPreferences animationExportPreferences
   ) {
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      panel.add(createPlayToolsToolBar(model, currentDirectoryModel, displayFontModel, animationExportPreferences));
      panel.add(Box.createHorizontalGlue(), GridDialogLayoutData.FILL_HORIZONTAL);
      ShowHideButton showHideButton = new ShowHideButton(
         showAdditionalControlsModel, "Hide additional Animation Controls", "Show additional Animation Controls"
      );
      panel.add(showHideButton.getContent());
      return panel;
   }

   private static JComponent createPlayToolsToolBar(
      AnimationEditorModel model, FileModel currentDirectoryModel, FontModel displayFontModel, AnimationExportPreferences preferences
   ) {
      Ensure.ensureArgumentNotNull(model);
      ButtonToolbarBuilder builder = new ButtonToolbarBuilder();
      builder.add(new PlayAnimationAction(model));
      builder.addSeparator();
      builder.add(new AddFrameAction(model));
      builder.add(new DuplicateCurrentFrameAction(model));
      builder.add(new DeleteCurrentFrameAction(model));
      builder.addSeparator();
      builder.add(new NavigateFirstAction(model));
      builder.add(new NavigatePreviousAction(model));
      builder.addSeparator();
      builder.add(createFrameIndexLabel(model));
      builder.addSeparator();
      builder.add(new NavigateNextAction(model));
      builder.add(new NavigateLastAction(model));
      builder.addSeparator();
      builder.add(new AnimationPropertiesAction(model));
      builder.addSeparator();
      builder.add(new ExportAnimationAction(model, currentDirectoryModel, displayFontModel, preferences));
      return builder.createPanel();
   }

   private static JLabel createFrameIndexLabel(final AnimationEditorModel model) {
      final JLabel label = new JLabel();
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationEditorToolBarFactory.updateFrameIndexLabel(label, model);
         }
      });
      model.getCurrentFrameIndexModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AnimationEditorToolBarFactory.updateFrameIndexLabel(label, model);
         }
      });
      updateFrameIndexLabel(label, model);
      return label;
   }

   private static void updateFrameIndexLabel(JLabel label, AnimationEditorModel model) {
      int currentFrameNumber = model.getCurrentFrameIndexModel().getCurrentFrameIndex() + 1;
      int frameCount = model.getAnimationFile().getFrameCount();
      label.setText(currentFrameNumber + " / " + frameCount);
      label.setToolTipText("Frame " + currentFrameNumber + " of " + frameCount);
   }
}
