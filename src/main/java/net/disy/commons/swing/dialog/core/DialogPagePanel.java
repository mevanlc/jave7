package net.disy.commons.swing.dialog.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.message.DialogMessageModel;
import net.disy.commons.swing.dialog.userdialog.IMessageSetable;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.events.CheckInputValidListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class DialogPagePanel implements IMessageSetable {
   private final JPanel contentPanel;
   private final IDialogHeaderPanelConfiguration headerPanelConfiguration;
   private final DialogMessageModel messageModel = new DialogMessageModel();
   private final ObjectModel<String> descriptionModel = new ObjectModel<>();
   private JComponent content;

   public DialogPagePanel(IDialogHeaderPanelConfiguration headerPanelConfiguration) {
      Ensure.ensureArgumentNotNull(headerPanelConfiguration);
      this.headerPanelConfiguration = headerPanelConfiguration;
      this.contentPanel = new JPanel(new GridLayout(0, 1));
      this.contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 8, 10, 8));
   }

   public JComponent createPanel() {
      JPanel dialogPagePanel = new JPanel(new BorderLayout());
      if (this.headerPanelConfiguration.isHeaderPanelVisible()) {
         DialogHeaderPanel headerPanel = new DialogHeaderPanel(this.messageModel, this.descriptionModel, this.headerPanelConfiguration.getLargeDialogIcon());
         dialogPagePanel.add(headerPanel.getContent(), "North");
      }

      dialogPagePanel.add(this.createDialogPagePanel(), "Center");
      return dialogPagePanel;
   }

   private Component createDialogPagePanel() {
      JPanel dialogPagePanel = new JPanel(new GridDialogLayout(1, false));
      GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      data.setWidthHint(IDialogConstants.MINIMUM_CONTENT_SIZE.width);
      data.setHeightHint(IDialogConstants.MINIMUM_CONTENT_SIZE.height);
      dialogPagePanel.add(this.contentPanel, data);
      return dialogPagePanel;
   }

   @Override
   public final void setMessage(IBasicMessage message) {
      this.messageModel.setMessage(message);
      this.contentPanel.validate();
      this.contentPanel.repaint();
   }

   public final void setDescription(String description) {
      this.descriptionModel.setValue(description);
   }

   public void setContent(JComponent content) {
      this.content = content;
      this.contentPanel.removeAll();
      this.contentPanel.add(content);
      this.contentPanel.revalidate();
   }

   public JComponent getContent() {
      return this.content;
   }

   public Dimension getPreferredSize() {
      return this.contentPanel.getPreferredSize();
   }

   public Dimension getSize() {
      return this.contentPanel.getSize();
   }

   public static DialogPagePanel getForDialogPage(IDialogPage dialogPage) {
      DialogPagePanel dialogPagePanel = new DialogPagePanel(DialogHeaderPanelConfiguration.createVisibleWithoutIcon());
      dialogPage.setInputValidListener(new CheckInputValidListener(new DialogPageInputValidCheckable(dialogPagePanel, dialogPage)));
      IBasicMessage defaultMessage = dialogPage.getDefaultMessage();
      Ensure.ensureTrue("default message must be of type NORMAL", defaultMessage.getType() == MessageType.NORMAL);
      dialogPagePanel.setMessage(defaultMessage);
      dialogPagePanel.setContent(dialogPage.createContent());
      return dialogPagePanel;
   }
}
