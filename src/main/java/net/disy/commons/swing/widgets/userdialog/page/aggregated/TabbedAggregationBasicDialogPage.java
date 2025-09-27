package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.swing.dialog.userdialog.page.IBasicDialogPage;
import net.disy.commons.swing.message.MessageTypeUi;

public class TabbedAggregationBasicDialogPage extends AbstractCompositeDialogPage {
   private final IBasicDialogPage[] pages;
   private JTabbedPane tabbedPane;

   public TabbedAggregationBasicDialogPage(String title, IBasicDialogPage... pages) {
      super(title);
      this.pages = pages;
      this.createTabbedPane();
   }

   @Override
   public final IBasicDialogPage[] getPages() {
      return this.pages;
   }

   @Override
   public void updateInputValid() {
      super.updateInputValid();

      for (int i = 0; i < this.pages.length; i++) {
         IBasicMessage currentMessage = this.pages[i].createCurrentMessage();
         MessageType messageType = currentMessage == null ? MessageType.NORMAL : currentMessage.getType();
         this.tabbedPane.setIconAt(i, messageType == MessageType.NORMAL ? null : MessageTypeUi.getInstance().getIcon(messageType));
      }
   }

   @Override
   protected IBasicMessage getDefaultCurrentMessage() {
      return this.currentPage().createCurrentMessage();
   }

   @Override
   public void requestFocus() {
      this.currentPage().requestFocus();
   }

   private IBasicDialogPage currentPage() {
      return this.pages[this.tabbedPane.getSelectedIndex()];
   }

   @Override
   public JComponent createContent() {
      return this.tabbedPane;
   }

   private void createTabbedPane() {
      this.tabbedPane = new JTabbedPane();
      this.forAllPages(new AbstractAggregationDialogPage.IPageClosure() {
         @Override
         public void execute(IBasicDialogPage dialogPage) {
            TabbedAggregationBasicDialogPage.this.tabbedPane.addTab(dialogPage.getTitle(), dialogPage.createContent());
         }
      });
      this.tabbedPane.addChangeListener(this.getCheckInputValidListener());
   }

   public void setEnabledAt(int index, boolean enabled) {
      this.tabbedPane.setEnabledAt(index, enabled);
   }
}
