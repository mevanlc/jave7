package net.dizzy.commons.swing.dialog.userdialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.core.DialogDefaults;
import net.dizzy.commons.swing.dialog.core.DialogResult;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory.DialogButtonConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.dizzy.commons.swing.util.GuiUtilities;
import net.dizzy.commons.swing.util.RelativePosition;

public class UserDialog {
   private final JDialog dialog;
   private final IDialogConfiguration<? extends IDialogPage> configuration;
   private IDialogResult result = DialogResult.CANCELED;
   private JButton okButton;

   public UserDialog(Component parent, IDialogConfiguration<? extends IDialogPage> configuration) {
      this(parent, configuration, null);
   }

   public UserDialog(Component parent, IDialogConfiguration<? extends IDialogPage> configuration, RelativePosition position) {
      this.configuration = configuration;
      Window owner = GuiUtilities.getWindowFor(parent);
      dialog = new JDialog(owner, configuration.getPage().getTitle(), Dialog.ModalityType.APPLICATION_MODAL);
      dialog.setIconImages(DialogDefaults.getInstance().getFrameIconImages());
      build();
      dialog.pack();
      dialog.setLocationRelativeTo(parent);
   }

   public UserDialog(Component parent, IDialogPage page) {
      this(parent, new DefaultDialogConfiguration<>(page));
   }

   public IDialogResult show() {
      dialog.setModal(true);
      dialog.setVisible(true);
      return result;
   }

   public void showNonModal(IDialogCloseHandler closeHandler) {
      dialog.setModal(false);
      dialog.setVisible(true);
      if (closeHandler != null) {
         closeHandler.handleDialogClose(result);
      }
   }

   public void setVisible(boolean visible) {
      dialog.setVisible(visible);
   }

   public DialogWindow getDialog() {
      return new DialogWindow(dialog);
   }

   private void build() {
      IDialogPage page = configuration.getPage();
      dialog.getContentPane().add(((AbstractDialogPage) page).createDialogContent(), BorderLayout.CENTER);
      JPanel buttons = new JPanel();
      DialogButtonConfiguration buttonConfiguration = configuration.getButtonConfiguration();
      if (buttonConfiguration.isOkVisible()) {
         okButton = new JButton(buttonConfiguration.getOkLabel());
         okButton.addActionListener(event -> closeOk());
         buttons.add(okButton);
      }
      if (buttonConfiguration.isCancelVisible()) {
         JButton cancelButton = new JButton(buttonConfiguration.getCancelLabel());
         cancelButton.addActionListener(event -> closeCancel());
         buttons.add(cancelButton);
      }
      for (javax.swing.JComponent component : configuration.createAdditionalButtons()) {
         buttons.add(component);
      }
      dialog.getContentPane().add(buttons, BorderLayout.SOUTH);
   }

   private void closeOk() {
      if (((AbstractDialogPage) configuration.getPage()).createDialogCurrentMessage().getType() == MessageType.ERROR) {
         if (okButton != null) {
            okButton.setEnabled(false);
         }
         return;
      }
      if (configuration.performOk(dialog)) {
         result = DialogResult.OK;
         dialog.dispose();
      }
   }

   private void closeCancel() {
      if (configuration.performCancel(dialog)) {
         result = DialogResult.CANCELED;
         dialog.dispose();
      }
   }

   public static class DialogWindow {
      private final JDialog dialog;

      DialogWindow(JDialog dialog) {
         this.dialog = dialog;
      }

      public Window getWindow() {
         return dialog;
      }
   }
}
