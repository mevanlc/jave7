package de.jave.asciimation.action;

import de.jave.asciimation.editor.AnimationEditorModel;
import de.jave.javeplayer.JavePlayer;
import de.jave.javeplayer.JavePlayerResources;
import java.awt.Component;
import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class PlayAnimationAction extends SmartAction {
   private final AnimationEditorModel model;

   public PlayAnimationAction(AnimationEditorModel model) {
      super("Play Animation", JavePlayerResources.PLAY_ICON);
      Ensure.ensureArgumentNotNull(model);
      this.model = model;
      this.setToolTipText("Show in Animation Player");
   }

   @Override
   protected void execute(Component parentComponent) {
      final JavePlayer player = new JavePlayer();
      player.setAnimationFile(this.model.getAnimationFile());
      AbstractDialogPage page = new AbstractDialogPage("") {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            return player;
         }

         @Override
         public String getTitle() {
            return "JMOV Animation Player";
         }
      };
      UserDialog dialog = new UserDialog(
         parentComponent, new DefaultDialogConfiguration<IDialogPage>(page, DialogButtonConfigurationFactory.createCloseOnly()) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogHeaderPanelConfiguration.createInvisible();
            }
         }
      );
      dialog.show();
   }
}
