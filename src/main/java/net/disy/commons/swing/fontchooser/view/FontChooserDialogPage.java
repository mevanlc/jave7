package net.disy.commons.swing.fontchooser.view;

import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;

public class FontChooserDialogPage extends AbstractDialogPage {
   private final FontChooserPanel fontChooserPanel;
   private final String title;

   public FontChooserDialogPage(FontChooserPanel fontChooserPanel, String title) {
      super("");
      this.fontChooserPanel = fontChooserPanel;
      this.title = title;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getTitle() {
      return this.title;
   }

   @Override
   public JComponent createContent() {
      return this.fontChooserPanel.getContent();
   }
}
