package net.disy.commons.swing.dialog.input.text;

import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.input.ISmartDialogPanelsBuilder;
import net.disy.commons.swing.dialog.input.SmartDialogPage;

public class TextInputDialogPage extends SmartDialogPage {
   private final ITextInputDialogConfiguration configuration;
   private final DefaultTextSmartDialogPanel textPanel;
   private final ObjectModel<String> stringModel;

   public TextInputDialogPage(final ITextInputDialogConfiguration configuration, String initialText) {
      super(configuration.getDefaultMessageText());
      this.configuration = configuration;
      this.stringModel = new ObjectModel<>(initialText);
      this.textPanel = new DefaultTextSmartDialogPanel(configuration.getLabelText(), this.stringModel, new IMessageProducingValidator() {
         @Override
         public IBasicMessage createOptionalCurrentMessage() {
            return configuration.createCurrentMessage(TextInputDialogPage.this.stringModel.getValue());
         }
      });
      this.textPanel.selectAll();
   }

   @Override
   public String getTitle() {
      return this.configuration.getTitle();
   }

   @Override
   protected void addPanels(ISmartDialogPanelsBuilder builder) {
      builder.add(this.textPanel);
   }

   @Override
   public void requestFocus() {
      this.textPanel.requestFocus();
   }

   public String getSelectedText() {
      return this.stringModel.getValue();
   }
}
