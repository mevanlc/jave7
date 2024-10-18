package de.jave.gui.dialog;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.message.LargeIconMessageTypeUi;
import net.disy.commons.swing.message.MessageTypeUi;

public class HtmlMessagePage extends AbstractDialogPage {
   private final HtmlMessage message;

   public HtmlMessagePage(HtmlMessage message) {
      super("");
      Ensure.ensureArgumentNotNull(message);
      this.message = message;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getTitle() {
      return this.message.getTitle() == null ? MessageTypeUi.getInstance().getLabel(this.message.getType()) : this.message.getTitle();
   }

   @Override
   public JComponent createContent() {
      if (this.message.getType() == MessageType.NORMAL) {
         return this.createHtmlTextComponent();
      } else {
         GridDialogLayoutData iconData = new GridDialogLayoutData();
         iconData.setVerticalAlignment(GridAlignment.BEGINNING);
         Icon icon = new LargeIconMessageTypeUi().getIcon(this.message.getType());
         JPanel panel = new JPanel(new GridDialogLayout(2, false, 13, 0));
         panel.add(new JLabel(icon), iconData);
         panel.add(this.createHtmlTextComponent(), GridDialogLayoutData.FILL_BOTH);
         return panel;
      }
   }

   private JComponent createHtmlTextComponent() {
      JTextPane textPane = new JTextPane();
      textPane.setContentType("text/html");
      textPane.setEditable(false);
      textPane.setText(this.message.getHtmlText());
      JScrollPane scrollPane = new JScrollPane(textPane);
      scrollPane.setPreferredSize(new Dimension(360, 300));
      textPane.setCaretPosition(0);
      return scrollPane;
   }
}
