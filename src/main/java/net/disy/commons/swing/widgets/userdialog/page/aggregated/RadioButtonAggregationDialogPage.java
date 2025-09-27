package net.disy.commons.swing.dialog.userdialog.page.aggregated;

import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.HighestPriorityMessageBuilder;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.ArrayUtilities;
import net.disy.commons.core.util.ITransformer;
import net.disy.commons.swing.component.Gap;
import net.disy.commons.swing.dialog.userdialog.page.IBasicDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.util.GuiUtilities;
import net.disy.commons.swing.util.ToggleComponentEnabler;

public class RadioButtonAggregationDialogPage<M> extends AbstractAggregationDialogPage implements IDialogPage {
   private final IDialogPageWithModel<M>[] dialogPages;
   private final JRadioButton[] radioButtons;
   private JComponent[] contents;
   private final String defaultMessage;

   public RadioButtonAggregationDialogPage(String title, String defaultMessage, IDialogPageWithModel<M>... dialogPages) {
      super(title);
      this.defaultMessage = defaultMessage;
      this.dialogPages = dialogPages;
      this.radioButtons = new JRadioButton[dialogPages.length];
      ButtonGroup buttonGroup = new ButtonGroup();

      for (int i = 0; i < this.radioButtons.length; i++) {
         this.radioButtons[i] = new JRadioButton();
         this.radioButtons[i].addActionListener(this.getCheckInputValidListener());
         buttonGroup.add(this.radioButtons[i]);
      }

      this.radioButtons[0].setSelected(true);
   }

   protected void updateComponents() {
      for (int i = 0; i < this.dialogPages.length; i++) {
         GuiUtilities.setContainerEnabled(this.contents[i], this.radioButtons[i].isSelected());
      }
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      HighestPriorityMessageBuilder messageBuilder = new HighestPriorityMessageBuilder();
      messageBuilder.addMessage(this.getDefaultMessage());
      messageBuilder.addMessage(this.getCurrentPage().getDialogPage().createCurrentMessage());
      return messageBuilder.getHighestPriorityMessage();
   }

   @Override
   protected IBasicDialogPage[] getPages() {
      return ArrayUtilities.transform(this.dialogPages, IBasicDialogPage.class, new ITransformer<IDialogPageWithModel<M>, IBasicDialogPage>() {
         public IBasicDialogPage transform(IDialogPageWithModel<M> input) {
            return input.getDialogPage();
         }
      });
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      this.contents = new JComponent[this.dialogPages.length];

      for (int i = 0; i < this.dialogPages.length; i++) {
         panel.add(this.radioButtons[i]);
         JLabel label = new JLabel(this.dialogPages[i].getLabel());
         panel.add(label);
         this.contents[i] = this.dialogPages[i].getDialogPage().createContent();
         panel.add(new Gap());
         panel.add(this.contents[i], new GridDialogLayoutData().setGrabExcessHorizontalSpace(true).setHorizontalAlignment(GridAlignment.FILL));
         ToggleComponentEnabler.connectWithDecoration(this.radioButtons[i], this.contents[i], label);
      }

      this.updateComponents();
      return panel;
   }

   @Override
   public IBasicMessage getDefaultMessage() {
      return new BasicMessage(this.defaultMessage);
   }

   public IDialogPageWithModel<M> getCurrentPage() {
      for (int i = 0; i < this.radioButtons.length; i++) {
         if (this.radioButtons[i].isSelected()) {
            return this.dialogPages[i];
         }
      }

      throw new UnreachableCodeReachedException("ip");
   }
}
