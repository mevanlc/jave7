package net.disy.commons.swing.dialog.userdialog;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.core.internal.AbstractGenericDialogConfiguration;
import net.disy.commons.swing.dialog.core.preferences.IDialogPreferences;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class DefaultDialogConfiguration<P extends IDialogPage> extends AbstractGenericDialogConfiguration implements IDialogConfiguration<P> {
   private IUserDialogContainer dialogContainer;
   private final P dialogPage;
   private final Dimension customizedPreferedSize;

   public DefaultDialogConfiguration(P dialogPage) {
      this(dialogPage, DialogButtonConfigurationFactory.createOkCancel());
   }

   public DefaultDialogConfiguration(P dialogPage, IDialogButtonConfiguration buttonConfiguration) {
      this(dialogPage, buttonConfiguration, null);
   }

   public DefaultDialogConfiguration(P dialogPage, IDialogButtonConfiguration buttonConfiguration, Dimension customizedPreferedSize) {
      this(dialogPage, buttonConfiguration, customizedPreferedSize, null);
   }

   public DefaultDialogConfiguration(
      P dialogPage, IDialogButtonConfiguration buttonConfiguration, Dimension customizedPreferedSize, IDialogPreferences preferences
   ) {
      super(buttonConfiguration, preferences);
      this.dialogPage = dialogPage;
      this.customizedPreferedSize = customizedPreferedSize;
   }

   public DefaultDialogConfiguration(P dialogPage, IDialogPreferences preferences) {
      this(dialogPage, DialogButtonConfigurationFactory.createOkCancel(), null, preferences);
   }

   @Override
   public P getDialogPage() {
      return this.dialogPage;
   }

   @Override
   public void setUserDialogContainer(final IUserDialogContainer dialogContainer) {
      this.dialogContainer = dialogContainer;
      this.dialogPage.addRequestFinishListener(new IRequestFinishListener() {
         @Override
         public void requestFinish() {
            dialogContainer.requestFinish();
         }
      });
   }

   protected final IUserDialogContainer getDialogContainer() {
      return this.dialogContainer;
   }

   @Override
   public JComponent[] createAdditionalButtons() {
      return new JComponent[0];
   }

   @Override
   public JComponent createOptionalButtonPanelLeftComponent() {
      return null;
   }

   @Override
   public IVetoDialogCloseHandler getVetoCloseHandler() {
      return new IVetoDialogCloseHandler() {
         @Override
         public boolean handleDialogAboutToClose(IDialogResult result, Component parentComponent) {
            return result.isCanceled()
               ? DefaultDialogConfiguration.this.performCancel(parentComponent)
               : DefaultDialogConfiguration.this.performOk(parentComponent);
         }
      };
   }

   @Deprecated
   @Override
   public boolean performOk(Component parentComponent) {
      return true;
   }

   @Deprecated
   @Override
   public boolean performCancel(Component parentComponent) {
      return true;
   }

   @Override
   public Dimension getCustomizedPreferedSize() {
      return this.customizedPreferedSize;
   }
}
