package net.disy.commons.swing.dialog.input;

import javax.swing.JComponent;
import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.page.AbstractBasicDialogPage;

public abstract class BasicSmartDialogPage extends AbstractBasicDialogPage {
   private SmartDialogPanelsBuildResult panelBuildResult;

   @Override
   public final IBasicMessage createCurrentMessage() {
      return this.createHighestPriorityMessage();
   }

   private IBasicMessage createHighestPriorityMessage() {
      HighestPriorityMessageBuilder builder = new HighestPriorityMessageBuilder();
      IOptionalCurrentMessageFactory[] panels = this.getPanels();

      for (int i = 0; i < panels.length; i++) {
         IBasicMessage message = panels[i].createOptionalCurrentMessage();
         if (message != null) {
            builder.addMessage(message);
         }
      }

      this.addAdditionalMessages(builder);
      return builder.getHighestPriorityMessage();
   }

   public boolean isErrorMessage() {
      return this.createHighestPriorityMessage().isErrorMessage();
   }

   protected void addAdditionalMessages(HighestPriorityMessageBuilder builder) {
   }

   @Override
   public final JComponent createContent() {
      SmartDialogPanelsBuildResult panelResult = this.getPanelBuildResult();
      IRequestFinishListener requestFinishListener = new IRequestFinishListener() {
         @Override
         public void requestFinish() {
            BasicSmartDialogPage.this.fireRequestFinish();
         }
      };
      ISmartDialogPanel[] panels = panelResult.getPanels();

      for (int i = 0; i < panels.length; i++) {
         panels[i].addChangeListener(this.getCheckInputValidListener());
         panels[i].addRequestFinishListener(requestFinishListener);
      }

      return panelResult.getCompletePanel();
   }

   private SmartDialogPanelsBuildResult getPanelBuildResult() {
      if (this.panelBuildResult == null) {
         SmartDialogPanelsBuilder builder = new SmartDialogPanelsBuilder();
         this.addPanels(builder);
         this.panelBuildResult = builder.createResult();
      }

      return this.panelBuildResult;
   }

   private ISmartDialogPanel[] getPanels() {
      return this.getPanelBuildResult().getPanels();
   }

   protected abstract void addPanels(ISmartDialogPanelsBuilder var1);

   @Override
   public void requestFocus() {
      ISmartDialogPanel[] panels = this.getPanels();
      if (panels.length > 0) {
         panels[0].requestFocus();
      }
   }
}
