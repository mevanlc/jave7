package net.disy.commons.swing.dialog.userdialog;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import net.disy.commons.swing.dialog.core.IGenericDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public interface IDialogConfiguration<P extends IDialogPage> extends IGenericDialogConfiguration {
   P getDialogPage();

   void setUserDialogContainer(IUserDialogContainer var1);

   JComponent[] createAdditionalButtons();

   JComponent createOptionalButtonPanelLeftComponent();

   @Deprecated
   boolean performOk(Component var1);

   @Deprecated
   boolean performCancel(Component var1);

   Dimension getCustomizedPreferedSize();
}
