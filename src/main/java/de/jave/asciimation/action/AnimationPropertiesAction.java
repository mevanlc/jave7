package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.jave.icon.JaveIcons;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.IDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class AnimationPropertiesAction extends SmartAction {
   private final AnimationEditorModel model;

   public AnimationPropertiesAction(AnimationEditorModel model) {
      super(JaveIcons.PROPERTIES_ICON);
      this.setToolTipText("Animation Properties");
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
   }

   @Override
   protected void execute(Component parentComponent) {
      showPropertiesDialog(parentComponent, this.model);
   }

   public static void showPropertiesDialog(Component parentComponent, AnimationEditorModel model) {
      final AnimationPropertiesDialogPage dialogPage = new AnimationPropertiesDialogPage(model);
      IDialogConfiguration<AnimationPropertiesDialogPage> userDialog = new DefaultDialogConfiguration<AnimationPropertiesDialogPage>(dialogPage) {
         @Override
         public JComponent[] createAdditionalButtons() {
            return new JComponent[]{new JButton(new SmartAction(DisyCommonsSwingDialogMessages.APPLY) {
               @Override
               protected void execute(Component parent) {
                  dialogPage.applySettings();
               }
            })};
         }
      };
      UserDialog dialog = new UserDialog(parentComponent, userDialog);
      IDialogResult result = dialog.show();
      if (!result.isCanceled()) {
         dialogPage.applySettings();
      }
   }
}
