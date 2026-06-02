package net.dizzy.commons.swing.dialog.userdialog;

import java.awt.Component;

import javax.swing.JComponent;

import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory.DialogButtonConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;

public class DefaultDialogConfiguration<P extends IDialogPage> implements IDialogConfiguration<P> {
   private final P page;
   private final DialogButtonConfiguration buttonConfiguration;

   public DefaultDialogConfiguration(P page) {
      this(page, DialogButtonConfigurationFactory.createOkCancel());
   }

   public DefaultDialogConfiguration(P page, DialogButtonConfiguration buttonConfiguration) {
      this.page = page;
      this.buttonConfiguration = buttonConfiguration;
   }

   @Override public P getPage() { return page; }
   @Override public DialogButtonConfiguration getButtonConfiguration() { return buttonConfiguration; }
   @Override public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() { return DialogHeaderPanelConfiguration.createInvisible(); }
   @Override public boolean performOk(Component parent) { return true; }
   @Override public boolean performCancel(Component parent) { return true; }
   @Override public JComponent[] createAdditionalButtons() { return new JComponent[0]; }

   public JComponent createOptionalButtonPanelLeftComponent() {
      return null;
   }
}
