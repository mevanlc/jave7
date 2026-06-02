package de.jave.figlet.swing.action;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.util.StringUtilities;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.label.SmartLabel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public final class FontSampleInputDialogPage extends AbstractDialogPage {
   private AsciiTextArea asciiTextArea;

   public FontSampleInputDialogPage() {
      super("Please provide a text sample to find the FIGlet font, it might have been created from.");
   }

   @Override
   public String getTitle() {
      return "Find Font by Sample";
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(1, false, 0, 0));
      this.asciiTextArea = new AsciiTextArea(new Dimension(30, 12), new AsciiTextAreaProperties());
      this.asciiTextArea.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            FontSampleInputDialogPage.this.checkInputValid();
         }
      });
      panel.add(new SmartLabel("&Sample:", this.asciiTextArea.getContent()));
      panel.add(this.asciiTextArea.getContent(), GridDialogLayoutData.FILL_BOTH);
      return panel;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return StringUtilities.isNullOrTrimmedEmpty(this.asciiTextArea.getText())
         ? new BasicMessage(
            "The sample text is empty. Please provide a text sample to find the FIGlet font, it might have been created from.", MessageType.ERROR
         )
         : this.getDefaultMessage();
   }

   public String getSampleText() {
      return this.asciiTextArea.getText();
   }
}
