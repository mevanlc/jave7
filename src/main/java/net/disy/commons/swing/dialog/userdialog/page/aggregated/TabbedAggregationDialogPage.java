package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class TabbedAggregationDialogPage extends TabbedAggregationBasicDialogPage implements IDialogPage {
   private final IBasicMessage defaultMessage;

   public TabbedAggregationDialogPage(String title, IDialogPage... pages) {
      super(title, pages);
      this.defaultMessage = pages[0].getDefaultMessage();
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return this.defaultMessage;
   }
}
