package net.disy.commons.swing.dialog.wizard;

import java.awt.Component;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.AbstractDialog;
import net.disy.commons.swing.dialog.core.DialogResult;
import net.disy.commons.swing.dialog.core.IDialogConstants;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.ISwingFrameOrDialog;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.core.internal.DialogButtonBarBuilder;
import net.disy.commons.swing.dialog.input.IRequestFinishListener;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.util.GuiUtilities;
import net.disy.commons.swing.util.IEnableable;

public class WizardDialog extends AbstractDialog implements IWizardContainer, IDialogConstants {
   public static final String FINISH_BUTTON_NAME = "WizardDialog.FinishButton.ComponentName";
   private JButton finishButton;
   private JButton backButton;
   private JButton nextButton;
   private IWizardPage currentPage;
   private final IWizardConfiguration configuration;
   private final Collection<IWizardPage> pages = new HashSet<>();
   private final IRequestFinishListener requestFinishListener = new IRequestFinishListener() {
      @Override
      public void requestFinish() {
         if (WizardDialog.this.nextButton != null && WizardDialog.this.nextButton.isEnabled()) {
            WizardDialog.this.nextButton.doClick();
         } else {
            if (WizardDialog.this.finishButton != null && WizardDialog.this.finishButton.isEnabled()) {
               WizardDialog.this.finishButton.doClick();
            }
         }
      }
   };
   private JButton cancelButton;
   private IEnableable helpEnableable;

   public WizardDialog(Component parent, IWizardConfiguration configuration) {
      super(parent, configuration);
      this.configuration = configuration;
      configuration.setContainer(this);
      configuration.addPages();
      this.initializeContent();
   }

   @Override
   protected JComponent createButtonBar() {
      SmartAction backAction = new SmartAction(DisyCommonsSwingDialogMessages.WIZARD_BACK) {
         @Override
         protected void execute(Component parentComponent) {
            WizardDialog.this.backPressed();
         }
      };
      this.backButton = new JButton(backAction);
      SmartAction nextAction = new SmartAction(DisyCommonsSwingDialogMessages.WIZARD_NEXT) {
         @Override
         protected void execute(Component parentComponent) {
            WizardDialog.this.nextPressed();
         }
      };
      this.nextButton = new JButton(nextAction);
      IDialogButtonConfiguration buttonConfiguration = this.getWizard().getButtonConfiguration();
      IActionConfiguration okActionConfiguration = buttonConfiguration.getOkActionConfiguration();
      SmartAction finishAction = new SmartAction(okActionConfiguration != null ? okActionConfiguration : new ActionConfiguration()) {
         @Override
         protected void execute(Component parentComponent) {
            WizardDialog.this.performFinish(parentComponent);
         }
      };
      this.finishButton = new JButton(finishAction);
      this.finishButton.setName("WizardDialog.FinishButton.ComponentName");
      IActionConfiguration cancelActionConfiguration = buttonConfiguration.getCancelActionConfiguration();
      SmartAction cancelAction = new SmartAction(
              cancelActionConfiguration != null ? cancelActionConfiguration : new ActionConfiguration()
      ) {
         @Override
         protected void execute(Component parentComponent) {
            WizardDialog.this.performCancel(parentComponent);
         }
      };
      this.cancelButton = new JButton(cancelAction);
      DialogButtonBarBuilder buttonBarBuilder = new DialogButtonBarBuilder();
      buttonBarBuilder.addButtonsCompacted(this.backButton, this.nextButton);
      JButton[] additionalButtons = this.createAdditionalButtons();
      buttonBarBuilder.addButtons(additionalButtons);
      if (okActionConfiguration != null) {
         buttonBarBuilder.addButtons(this.finishButton);
      }

      if (cancelActionConfiguration != null) {
         buttonBarBuilder.addButtons(this.cancelButton);
      }

      if (this.getWizard().isHelpAvailable()) {
         IDialogHelpHandler helpHandler = new IDialogHelpHandler() {
            @Override
            public void execute(Component parentComponent) {
               IDialogHelpHandler pageHelpHandler = WizardDialog.this.getCurrentPage().getHelpHandler();
               pageHelpHandler.execute(parentComponent);
            }
         };
         this.helpEnableable = buttonBarBuilder.setHelpHandler(helpHandler);
      }

      return buttonBarBuilder.createButtonBar();
   }

   private JButton[] createAdditionalButtons() {
      return new JButton[0];
   }

   protected final void backPressed() {
      this.showPage(this.getCurrentPage().getPreviousPage());
   }

   @Override
   protected final boolean cancelPressed(Component parentComponent) {
      IVetoDialogCloseHandler vetoCloseHandler = this.configuration.getVetoCloseHandler();
      return vetoCloseHandler.handleDialogAboutToClose(new DialogResult(true), parentComponent);
   }

   protected void nextPressed() {
      this.showPage(this.getCurrentPage().getNextPage());
   }

   @Override
   public void showPage(IWizardPage page) {
      Ensure.ensureArgumentNotNull(page);
      if (this.currentPage != null) {
         this.currentPage.leave();
         this.currentPage.removeRequestFinishListener(this.requestFinishListener);
      }

      page.addRequestFinishListener(this.requestFinishListener);
      this.currentPage = page;
      this.pages.add(page);
      this.updateContent();
      this.updateMessage();
      this.updateDescription();
      this.updateButtons();
      this.updateTitle();
      this.updateSize();
      this.currentPage.enter();
      this.currentPage.getPageContent().requestFocus();
   }

   protected void updateContent() {
      this.setContent(this.getCurrentPage().getPageContent().getContent());
   }

   @Override
   public IWizardPage getCurrentPage() {
      return this.currentPage;
   }

   protected void setCurrentPage(IWizardPage page) {
      this.currentPage = page;
   }

   @Override
   public void updateButtons() {
      IWizardPage page = this.getCurrentPage();
      this.nextButton.setEnabled(page.canFlipToNextPage());
      this.backButton.setEnabled(page.getPreviousPage() != null);
      this.finishButton.setEnabled(this.getWizard().canFinish());
      this.cancelButton.setEnabled(this.getWizard().canCancel());
      if (this.helpEnableable != null) {
         this.helpEnableable.setEnabled(page.getHelpHandler() != null);
      }

      if (this.finishButton.isEnabled()) {
         this.setDefaultButton(this.finishButton);
      } else {
         this.setDefaultButton(this.nextButton);
      }
   }

   @Override
   public void updateMessage() {
      this.setMessage(this.getCurrentPage().getMessage());
   }

   @Override
   public void updateDescription() {
      this.setDescription(this.getCurrentPage().getDescription());
   }

   @Override
   public void updateTitle() {
      this.setTitle(this.getCurrentPage().getTitle());
   }

   @Override
   public final IDialogResult show() {
      ISwingFrameOrDialog configuredDialog = this.getConfiguredDialog();
      GuiUtilities.centerToParent(configuredDialog.getWindow());
      configuredDialog.show();
      return new DialogResult(this.isCanceled());
   }

   public ISwingFrameOrDialog getConfiguredDialog() {
      IWizardPage startingPage = this.getWizard().getStartingPage();
      if (startingPage == null) {
         throw new RuntimeException("Starting page may not be null in IWizard.getStartingPage()");
      } else {
         this.showPage(startingPage);
         ISwingFrameOrDialog configuredDialog = this.getDialog();
         if (configuredDialog == null) {
            throw new IllegalStateException("WizardDialog is already disposed and may not be shown more often than once");
         } else if (configuredDialog.isVisible()) {
            throw new IllegalStateException("WizardDialog is already visible");
         } else {
            return configuredDialog;
         }
      }
   }

   protected final IWizardConfiguration getWizard() {
      return this.configuration;
   }

   private final void performFinish(Component parentComponent) {
      this.currentPage.getNextPage();
      this.currentPage.leave();
      IVetoDialogCloseHandler vetoCloseHandler = this.configuration.getVetoCloseHandler();
      boolean success = vetoCloseHandler.handleDialogAboutToClose(new DialogResult(false), parentComponent);
      if (success) {
         this.closeDialog();
      }
   }

   @Override
   public final void requestFinish() {
      this.performFinish(this.getDialog().getWindow());
   }

   @Override
   public final void requestNext() {
      if (this.getCurrentPage().canFlipToNextPage()) {
         this.nextPressed();
      }
   }

   @Override
   protected final void closeDialog() {
      super.closeDialog();
      this.currentPage.leave();

      for (IWizardPage page : this.pages) {
         page.getPageContent().dispose();
      }
   }
}
