package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.page.IBasicDialogPage;

public abstract class AbstractCompositeDialogPage extends AbstractAggregationDialogPage {
   public AbstractCompositeDialogPage(String title) {
      super(title);
   }

   @Override
   public final IBasicMessage createCurrentMessage() {
      class CurrentMessageClosure implements AbstractAggregationDialogPage.IPageClosure {
         HighestPriorityMessageBuilder messageBuilder = new HighestPriorityMessageBuilder();

         public CurrentMessageClosure() {
            this.messageBuilder.addMessage(AbstractCompositeDialogPage.this.getDefaultCurrentMessage());
         }

         @Override
         public void execute(IBasicDialogPage page) {
            this.messageBuilder.addMessage(page.createCurrentMessage());
         }
      }

      CurrentMessageClosure closure = new CurrentMessageClosure();
      this.forAllPages(closure);
      return closure.messageBuilder.getHighestPriorityMessage();
   }

   protected abstract IBasicMessage getDefaultCurrentMessage();
}
