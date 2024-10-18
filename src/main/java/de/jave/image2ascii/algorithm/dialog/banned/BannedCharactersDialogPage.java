package de.jave.image2ascii.algorithm.dialog.banned;

import java.awt.Font;
import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;

public class BannedCharactersDialogPage extends AbstractDialogPage {
   private final AsciiCharactersSelectDialogPanel content;

   public BannedCharactersDialogPage(String bannedCharacters, Font font) {
      super("Please select the Ascii characters that shall not be used.");
      this.content = new AsciiCharactersSelectDialogPanel(bannedCharacters, font);
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getTitle() {
      return "Banned Characters";
   }

   @Override
   public JComponent createContent() {
      return this.content.getContent();
   }

   public String getBannedCharcaters() {
      return this.content.getSelectedCharacters();
   }
}
