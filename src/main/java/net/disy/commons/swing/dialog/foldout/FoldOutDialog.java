package net.disy.commons.swing.dialog.foldout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.label.internal.MnemonicLabel;
import net.disy.commons.swing.label.internal.MnemonicLabelParser;

public class FoldOutDialog extends UserDialog {
   private JButton foldOutButton;
   private boolean isFoldedOut = false;
   private JPanel foldOutPanel;

   public FoldOutDialog(Component parent, IFoldOutDialogConfiguration userDialog) {
      super(parent, userDialog);
      this.isFoldedOut = userDialog.isInitiallyFoldedOut();
      this.updateResizeable();
   }

   @Override
   protected boolean isMainContentGrabVerticalSpace() {
      return false;
   }

   private IFoldOutDialogConfiguration getFoldOutUserDialog() {
      return (IFoldOutDialogConfiguration)this.getConfiguration();
   }

   @Override
   protected JComponent[] createAdditionalButtons() {
      this.foldOutButton = new JButton();
      this.foldOutButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            FoldOutDialog.this.toggleFoldOut();
         }
      });
      this.updateButtonText();
      return new JComponent[]{this.foldOutButton};
   }

   private void toggleFoldOut() {
      this.isFoldedOut = !this.isFoldedOut;
      this.foldOutPanel.setVisible(this.isFoldedOut);
      this.updateButtonText();
      this.updateResizeable();
      if (this.isFoldedOut) {
         this.getFoldOutUserDialog().getFoldOutPage().requestFocus();
      } else {
         this.getConfiguration().getDialogPage().requestFocus();
      }

      this.getDialog().pack();
   }

   private void updateResizeable() {
      this.getDialog().setResizable(this.isFoldedOut);
   }

   private void updateButtonText() {
      if (this.isFoldedOut) {
         configure(this.foldOutButton, this.getFoldOutUserDialog().getFoldInButtonConfiguration());
      } else {
         configure(this.foldOutButton, this.getFoldOutUserDialog().getFoldOutButtonConfiguration());
      }
   }

   private static void configure(JButton button, IActionConfiguration actionConfiguration) {
      MnemonicLabel label = MnemonicLabelParser.parse(actionConfiguration.getName());
      button.setText(label.getPlainText());
      if (label.getMnemonicCharacter() != null) {
         button.setMnemonic(label.getMnemonicCharacter());
      }

      button.setToolTipText(actionConfiguration.getToolTipText());
      button.setIcon(actionConfiguration.getIcon());
   }

   @Override
   protected JComponent createOptionalBelowButtonsPanel() {
      JComponent foldOutContent = this.getFoldOutUserDialog().getFoldOutPage().getContent();
      this.foldOutPanel = new JPanel(new BorderLayout());
      this.foldOutPanel.setVisible(false);
      this.foldOutPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 0, 8));
      this.foldOutPanel.add(foldOutContent, "Center");
      return this.foldOutPanel;
   }
}
