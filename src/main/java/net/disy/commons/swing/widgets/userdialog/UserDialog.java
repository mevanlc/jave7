package net.disy.commons.swing.dialog.userdialog;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.swing.action.ActionConfiguration;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.AbstractDialog;
import net.disy.commons.swing.dialog.core.DialogResult;
import net.disy.commons.swing.dialog.core.IDialogHelpHandler;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.core.IVetoDialogCloseHandler;
import net.disy.commons.swing.dialog.core.internal.DialogButtonBarBuilder;
import net.disy.commons.swing.dialog.core.preferences.IDialogPreferences;
import net.disy.commons.swing.dialog.userdialog.buttons.IDialogButtonConfiguration;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.util.GuiUtilities;
import net.disy.commons.swing.util.RelativePosition;

public class UserDialog extends AbstractDialog implements IUserDialogContainer {
   private final DialogPageControl dialogControl;
   private JButton okButton;
   private JButton cancelButton;
   private boolean neverVisualized = true;
   private final RelativePosition relativePosition;

   public UserDialog(Component parentComponent, IDialogConfiguration<? extends IDialogPage> userDialog) {
      this(parentComponent, userDialog, RelativePosition.CENTER);
   }

   public UserDialog(Component parentComponent, IDialogConfiguration<?> dialogConfiguration, RelativePosition relativePosition) {
      super(parentComponent, dialogConfiguration);
      this.dialogControl = new DialogPageControl(dialogConfiguration.getDialogPage());
      dialogConfiguration.setUserDialogContainer(this);
      this.dialogControl.setDialogControl(this);
      this.initializeContent();
      this.setContent(this.dialogControl.getContent());
      this.updateAll();
      this.dialogControl.requestFocus();
      this.relativePosition = relativePosition;
   }

   public UserDialog(Component parentComponent, IDialogPage dialogPage) {
      this(parentComponent, new DefaultDialogConfiguration<>(dialogPage));
   }

   protected IDialogConfiguration<?> getConfiguration() {
      return (IDialogConfiguration<?>)this.getGenericDialog();
   }

   private void updateAll() {
      this.updateDescription();
      this.updateTitle();
      this.updateMessage();
      this.updateButtons();
   }

   @Override
   public void updateDescription() {
      this.setDescription(this.getDialogControl().getDescription());
   }

   @Override
   public void updateTitle() {
      this.setTitle(this.getDialogControl().getTitle());
   }

   @Override
   public void updateMessage() {
      this.setMessage(this.getDialogControl().getMessage());
   }

   @Override
   public void updateButtons() {
      this.okButton.setEnabled(this.getDialogControl().canFinish());
   }

   @Override
   protected final JComponent createButtonBar() {
      JComponent[] buttons = this.createButtons();
      if (buttons.length > 0 && buttons[0] instanceof JButton) {
         this.setDefaultButton((JButton)buttons[0]);
      }

      DialogButtonBarBuilder buttonBarBuilder = new DialogButtonBarBuilder();
      buttonBarBuilder.addButtons(buttons);
      IDialogHelpHandler helpHandler = this.getDialogControl().getHelpHandler();
      if (helpHandler != null) {
         buttonBarBuilder.setHelpHandler(helpHandler);
      }

      JComponent leftComponent = this.getConfiguration().createOptionalButtonPanelLeftComponent();
      if (leftComponent != null) {
         buttonBarBuilder.addLeftSideComponent(leftComponent);
      }

      return buttonBarBuilder.createButtonBar();
   }

   protected final JComponent[] createButtons() {
      IDialogButtonConfiguration buttonConfiguration = this.getConfiguration().getButtonConfiguration();
      IActionConfiguration okActionConfiguration = buttonConfiguration.getOkActionConfiguration();
      SmartAction okAction = new SmartAction(okActionConfiguration != null ? okActionConfiguration : new ActionConfiguration()) {
         @Override
         protected void execute(Component parentComponent) {
            UserDialog.this.requestFinish();
         }
      };
      this.okButton = new JButton(okAction);
      IActionConfiguration cancelActionConfiguration = buttonConfiguration.getCancelActionConfiguration();
      SmartAction cancelAction = new SmartAction(
              cancelActionConfiguration != null ? cancelActionConfiguration : new ActionConfiguration()
      ) {
         @Override
         protected void execute(Component parentComponent) {
            UserDialog.this.performCancel(parentComponent);
         }
      };
      this.cancelButton = new JButton(cancelAction);
      List<JComponent> buttonList = new ArrayList<>();
      if (okActionConfiguration != null) {
         buttonList.add(this.okButton);
      }

      JComponent[] additionalButtons = this.getConfiguration().createAdditionalButtons();
      buttonList.addAll(Arrays.asList(additionalButtons));
      buttonList.addAll(Arrays.asList(this.createAdditionalButtons()));
      if (cancelActionConfiguration != null) {
         buttonList.add(this.cancelButton);
      }

      return buttonList.toArray(new JComponent[0]);
   }

   protected JComponent[] createAdditionalButtons() {
      return new JComponent[0];
   }

   protected final boolean okPressed() {
      if (!this.getDialogControl().performOk()) {
         return false;
      } else {
         IVetoDialogCloseHandler closeHandler = this.getConfiguration().getVetoCloseHandler();
         return closeHandler.handleDialogAboutToClose(new DialogResult(false), this.getDialog().getWindow());
      }
   }

   public DialogPageControl getDialogControl() {
      return this.dialogControl;
   }

   @Override
   protected final boolean cancelPressed(Component parentComponent) {
      if (!this.getDialogControl().performCancel()) {
         return false;
      } else {
         IVetoDialogCloseHandler closeHandler = this.getConfiguration().getVetoCloseHandler();
         return closeHandler.handleDialogAboutToClose(new DialogResult(true), parentComponent);
      }
   }

   @Override
   public void setVisible(boolean visible) {
      if (visible) {
         if (this.neverVisualized) {
            Dimension customizedPreferedSize = this.getConfiguration().getCustomizedPreferedSize();
            if (customizedPreferedSize != null) {
               this.getDialog().getWindow().setSize(customizedPreferedSize);
            }

            this.placeRelativeToOwner();
            IDialogPage page = this.getConfiguration().getDialogPage();
            page.enter();
            this.getDialog().show();
            this.neverVisualized = false;
         } else {
            IDialogPage page = this.getConfiguration().getDialogPage();
            page.enter();
            this.getDialog().show();
         }
      } else {
         this.closeDialog();
      }
   }

   private void placeRelativeToOwner() {
      IDialogPreferences preference = this.getConfiguration().getPreferences();
      if (preference == null || preference.getBounds() == null) {
         GuiUtilities.placeRelativeToOwner(this.getDialog().getWindow(), this.relativePosition);
      }
   }

   @Override
   public IDialogResult show() {
      this.setVisible(true);
      return new DialogResult(this.isCanceled());
   }

   @Override
   public final void requestFinish() {
      GuiUtilities.stopCellEditing(this.getDialog().getContentPane());
      if (this.getDialogControl().canFinish()) {
         if (this.okPressed()) {
            this.closeDialog();
            this.getCloseHandler().handleDialogClose(new DialogResult(false));
         }
      }
   }

   @Override
   protected void closeDialog() {
      super.closeDialog();
      IDialogPage page = this.getConfiguration().getDialogPage();
      page.leave();
      page.dispose();
   }

   @Override
   public void showNonModal() {
      this.showNonModal(IDialogCloseHandler.NULL_HANDLER);
   }

   @Override
   public void showNonModal(IDialogCloseHandler dialogCloseHandler) {
      this.setCloseHandler(dialogCloseHandler);
      this.getDialog().setModal(false);
      this.setVisible(true);
   }

   public Dimension getSize() {
      return this.getDialog().getWindow().getSize();
   }
}
