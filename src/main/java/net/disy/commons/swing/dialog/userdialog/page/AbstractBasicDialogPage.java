package net.disy.commons.swing.dialog.userdialog.page;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.model.listener.NotifyChangeListenerClosure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.dialog.core.internal.AbstractPage;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.events.ICheckInputValidListener;

public abstract class AbstractBasicDialogPage extends AbstractPage implements IBasicDialogPage {
   private final ListenerList<IRequestFinishListener> requestFinishListeners = new ListenerList<>();
   private final ListenerList<IChangeListener> changeListeners = new ListenerList<>();
   private ICheckInputValidListener inputValidListener;
   private final ICheckInputValidListener inputValidListenerProxy = (ICheckInputValidListener)Proxy.newProxyInstance(
      this.getClass().getClassLoader(), new Class[]{ICheckInputValidListener.class}, new InvocationHandler() {
         @Override
         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            AbstractBasicDialogPage.this.changeListeners.forAllDo(NotifyChangeListenerClosure.INSTANCE);
            return AbstractBasicDialogPage.this.inputValidListener == null ? null : method.invoke(AbstractBasicDialogPage.this.inputValidListener, args);
         }
      }
   );

   @Override
   public IBasicMessage createCurrentMessage() {
      return null;
   }

   @Override
   public void setInputValidListener(ICheckInputValidListener inputValidListener) {
      this.inputValidListener = inputValidListener;
   }

   @Deprecated
   @Override
   public boolean performOk() {
      return true;
   }

   @Deprecated
   @Override
   public boolean performCancel() {
      return true;
   }

   @Override
   public String getDescription() {
      return this.getTitle();
   }

   @Override
   public void requestFocus() {
   }

   protected final ICheckInputValidListener getCheckInputValidListener() {
      return this.inputValidListenerProxy;
   }

   protected final void checkInputValid() {
      this.inputValidListenerProxy.checkInputValid();
   }

   @Override
   public final void addRequestFinishListener(IRequestFinishListener requestFinishListener) {
      this.requestFinishListeners.add(requestFinishListener);
   }

   @Override
   public final void removeRequestFinishListener(IRequestFinishListener requestFinishListener) {
      this.requestFinishListeners.remove(requestFinishListener);
   }

   protected final void fireRequestFinish() {
      this.requestFinishListeners.forAllDo(new IClosure<IRequestFinishListener>() {
         public void execute(IRequestFinishListener listener) {
            listener.requestFinish();
         }
      });
   }

   @Override
   public void updateInputValid() {
   }

   @Override
   public boolean canFinish() {
      return !this.createCurrentMessage().isErrorMessage();
   }

   @Override
   public void addInputValidChangeListener(IChangeListener listener) {
      this.changeListeners.add(listener);
   }

   public void removeInputValidChangeListener(IChangeListener listener) {
      this.changeListeners.remove(listener);
   }

   @Override
   public void enter() {
   }

   @Override
   public void leave() {
   }
}
