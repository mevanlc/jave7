package net.disy.commons.swing.dialog.userdialog.builder;

import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.IDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class DialogConfigurationBuilder implements IDialogConfigurationBuilder {
   private boolean headerPanelVisible = true;

   @Override
   public <P extends IDialogPage> IDialogConfiguration<P> create(P page) {
      return this.configure(
         new DefaultDialogConfiguration<P>(page) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogConfigurationBuilder.this.headerPanelVisible
                  ? DialogHeaderPanelConfiguration.createVisibleWithoutIcon()
                  : DialogHeaderPanelConfiguration.createInvisible();
            }
         }
      );
   }

   private <P extends IDialogPage> DefaultDialogConfiguration<P> configure(DefaultDialogConfiguration<P> dialogConfiguration) {
      return dialogConfiguration;
   }

   @Override
   public <P extends IDialogPage> IDialogConfiguration<P> create(P page, IDialogButtonConfiguration buttonConfiguration) {
      return this.configure(
         new DefaultDialogConfiguration<P>(page, buttonConfiguration) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogConfigurationBuilder.this.headerPanelVisible
                  ? DialogHeaderPanelConfiguration.createVisibleWithoutIcon()
                  : DialogHeaderPanelConfiguration.createInvisible();
            }
         }
      );
   }

   @Override
   public IDialogConfigurationBuilder invisibleHeaderPanel() {
      this.headerPanelVisible = false;
      return this;
   }
}
