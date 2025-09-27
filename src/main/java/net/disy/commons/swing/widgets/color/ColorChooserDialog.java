package net.disy.commons.swing.dialog.color;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.color.widgets.ColorModel;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.IDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.resources.DisyCommonsSwingMessages;

public class ColorChooserDialog {
   public static Color showDialog(Component parent, final IColorChooserConfiguration configuration, Color color) {
      ColorModel colorModel = new ColorModel(color);
      final ColorChooserPanel colorChooserPanel = new ColorChooserPanel(colorModel, configuration.isTransparencyEnabled());
      AbstractDialogPage page = new AbstractDialogPage("") {
         @Override
         public String getTitle() {
            return configuration.getColorChooserDialogTitle();
         }

         @Override
         public JComponent createContent() {
            return colorChooserPanel.getContent();
         }

         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }
      };
      IDialogConfiguration<AbstractDialogPage> dialogConfiguration = new DefaultDialogConfiguration<AbstractDialogPage>(page) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }

         @Override
         public JComponent[] createAdditionalButtons() {
            Action resetAction = new SmartAction(DisyCommonsSwingMessages.getString("ColorChooserDialog.Reset")) {
               @Override
               protected void execute(Component parentComponent) {
                  colorChooserPanel.resetColor();
               }
            };
            return new JComponent[]{new JButton(resetAction)};
         }
      };
      UserDialog userDialog = new UserDialog(parent, dialogConfiguration);
      IDialogResult result = userDialog.show();
      return result.isCanceled() ? color : colorModel.getColor();
   }
}
