package net.disy.commons.swing.dialog.userdialog.page;

import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.dialog.core.IPage;
import net.disy.commons.swing.dispose.IDisposable;
import net.disy.commons.swing.events.ICheckInputValidListener;

public interface IBasicDialogPage extends IPage, IDisposable {
   IBasicMessage createCurrentMessage();

   void requestFocus();

   JComponent createContent();

   void setInputValidListener(ICheckInputValidListener var1);

   @Deprecated
   boolean performCancel();

   @Deprecated
   boolean performOk();

   void updateInputValid();

   void addInputValidChangeListener(IChangeListener var1);
}
