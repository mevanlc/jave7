package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.dialog.userdialog.page.IBasicDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;

public class StackedAggregationDialogPage extends AbstractCompositeDialogPage implements IDialogPage {
   private final IDialogPage upperPage;
   private final IBasicDialogPage lowerPage;
   private final JComponent seperator;

   public StackedAggregationDialogPage(String title, IDialogPage upperPage, IBasicDialogPage lowerPage) {
      this(title, upperPage, lowerPage, new Gap(0, 0));
   }

   public StackedAggregationDialogPage(String title, IDialogPage upperPage, IBasicDialogPage lowerPage, JComponent seperator) {
      super(title);
      this.upperPage = upperPage;
      this.lowerPage = lowerPage;
      this.seperator = seperator;
   }

   @Override
   public final IBasicDialogPage[] getPages() {
      return new IBasicDialogPage[]{this.upperPage, this.lowerPage};
   }

   @Override
   public IBasicMessage getDefaultCurrentMessage() {
      return this.upperPage.createCurrentMessage();
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new BorderLayout());
      JPanel upperPanel = new JPanel(new BorderLayout());
      upperPanel.add(this.upperPage.createContent(), "Center");
      upperPanel.add(this.seperator, "South");
      panel.add(upperPanel, "North");
      panel.add(this.lowerPage.createContent(), "Center");
      return panel;
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return this.upperPage.getDefaultMessage();
   }

   @Override
   public void requestFocus() {
      this.upperPage.requestFocus();
   }
}
