package net.disy.commons.swing.dialog.wizard;

import javax.swing.JComponent;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.listener.ListenerList;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IClosure;
import net.disy.commons.swing.dialog.core.IPageContent;
import net.disy.commons.swing.dialog.core.internal.AbstractPage;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.events.CheckInputValidListener;
import net.disy.commons.swing.events.IInputValidCheckable;

public abstract class AbstractWizardPage extends AbstractPage implements IWizardPage, IInputValidCheckable, IPageContent {
   private final ListenerList<IRequestFinishListener> requestFinishListeners = new ListenerList<>();
   private CheckInputValidListener checkInputValidListener;
   private final IBasicMessage defaultMessage;
   private final String title;
   private IBasicMessage message;
   private String description;
   @Deprecated
   private IWizardConfiguration wizard;
   private JComponent content;

   protected AbstractWizardPage(String description, String defaultMessageText) {
      this(description, null, defaultMessageText);
   }

   protected AbstractWizardPage(String description, String title, String defaultMessageText, IWizardConfiguration wizard) {
      this(description, title, defaultMessageText);
      this.setWizard(wizard);
   }

   protected AbstractWizardPage(String description, String title, String defaultMessageText) {
      Ensure.ensureArgumentNotNull("The default message page may not be null (" + this + ")", defaultMessageText);
      this.defaultMessage = new BasicMessage(defaultMessageText);
      this.setMessage(this.defaultMessage);
      this.description = description;
      this.title = title;
   }

   protected final IBasicMessage getDefaultMessage() {
      return this.defaultMessage;
   }

   protected final CheckInputValidListener getCheckInputValidListener() {
      if (this.checkInputValidListener == null) {
         this.checkInputValidListener = new CheckInputValidListener(this);
      }

      return this.checkInputValidListener;
   }

   @Override
   public void checkInputValid() {
      this.setMessage(this.createCurrentMessage());
   }

   protected abstract IBasicMessage createCurrentMessage();

   @Override
   public String getTitle() {
      return this.title == null ? this.getDescription() : this.title;
   }

   @Override
   public final void setWizard(IWizardConfiguration newWizard) {
      this.wizard = newWizard;
   }

   @Deprecated
   @Override
   public final IWizardConfiguration getWizard() {
      return this.wizard;
   }

   private void updateMessage() {
      if (this.getWizard() != null && this.getWizard().getContainer() != null) {
         this.getWizard().getContainer().updateMessage();
         this.getWizard().getContainer().updateButtons();
      }
   }

   @Override
   public final JComponent getContent() {
      if (this.content == null) {
         this.content = this.createContent();
         if (this.getWizard() != null && this.getWizard().shallInitializePagesFromData()) {
            this.initializeFromData();
         }
      }

      return this.content;
   }

   protected abstract JComponent createContent();

   protected final void setDescription(String description) {
      this.description = description;
   }

   @Override
   public final void setMessage(IBasicMessage message) {
      Ensure.ensureArgumentNotNull(message);
      this.message = message;
      this.updateMessage();
   }

   @Override
   public final String getDescription() {
      return this.description;
   }

   @Override
   public final IBasicMessage getMessage() {
      return this.message;
   }

   @Override
   public IWizardPage getNextPage() {
      return this.getWizard().getNextPage(this);
   }

   @Override
   public IWizardPage getPreviousPage() {
      return this.getWizard().getPreviousPage(this);
   }

   @Override
   public boolean canFlipToNextPage() {
      return !this.createCurrentMessage().isErrorMessage() && this.getNextPage() != null;
   }

   protected void setErrorMessage(String text) {
      this.setMessage(new BasicMessage(text, MessageType.ERROR));
   }

   protected void setInformationMessage(String text) {
      this.setMessage(new BasicMessage(text, MessageType.INFORMATION));
   }

   protected void setWarningMessage(String text) {
      this.setMessage(new BasicMessage(text, MessageType.WARNING));
   }

   @Deprecated
   @Override
   public void initializeFromData() {
   }

   protected final boolean isActivePage() {
      return this.getWizard().getContainer().getCurrentPage() == this;
   }

   @Override
   public void enter() {
   }

   @Override
   public void leave() {
   }

   @Override
   public IPageContent getPageContent() {
      return this;
   }

   @Override
   public void addRequestFinishListener(IRequestFinishListener requestFinishListener) {
      this.requestFinishListeners.add(requestFinishListener);
   }

   @Override
   public void removeRequestFinishListener(IRequestFinishListener requestFinishListener) {
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
   public boolean canCancel() {
      return true;
   }

   public final void pageDeactivated() {
   }
}
