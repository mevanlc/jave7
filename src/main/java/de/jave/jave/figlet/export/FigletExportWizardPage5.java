package de.jave.jave.figlet.export;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.gui.FilenameTextField;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.layout.grid.EndOfLineMarkerComponent;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;

public class FigletExportWizardPage5 extends AbstractFigletExportWizardPage {
   private AsciiTextArea taComments;
   private FilenameTextField tfFilename;

   public FigletExportWizardPage5(FigletExportModel model) {
      super(
         model,
         "Font Name and Comments",
         "Specify a significant name for the new font. Also please add some comments, to let everybody know who created this font."
      );
   }

   @Override
   protected IBasicMessage createCurrentMessage() {
      if (this.getModel().getName().length() == 0) {
         return new BasicMessage("The font name is empty. Please specify a name.", MessageType.ERROR);
      } else if (this.getModel().getName().length() < 4) {
         return new BasicMessage("The font name is discouraged. Font names should be at leat 4 characters long.", MessageType.WARNING);
      } else if (this.getModel().getName().length() > 30) {
         return new BasicMessage("The font name is discouraged. Font names should not be too long.", MessageType.WARNING);
      } else {
         return containsUpperCaseLetters(this.getModel().getName())
            ? new BasicMessage("The font name is discouraged. By convention font names should not contain uppercase letters.", MessageType.WARNING)
            : this.getDefaultMessage();
      }
   }

   private static boolean containsUpperCaseLetters(String name) {
      for (int i = 0; i < name.length(); i++) {
         if (Character.isUpperCase(name.charAt(i))) {
            return true;
         }
      }

      return false;
   }

   @Override
   protected JComponent createContent() {
      this.taComments = new AsciiTextArea(new Dimension(60, 12), new AsciiTextAreaProperties());
      this.taComments.setText(this.getModel().getComment());
      this.tfFilename = new FilenameTextField(this.getModel().getName(), 12);
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      panel.add(new JLabel("Font Name:"));
      panel.add(this.tfFilename, GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new JLabel(".flf"));
      panel.add(new JLabel("Comments:"));
      panel.add(new EndOfLineMarkerComponent());
      GridDialogLayoutData data = new GridDialogLayoutData(GridDialogLayoutData.FILL_BOTH);
      data.setHorizontalSpan(3);
      panel.add(this.taComments.getContent(), data);
      this.getModel().addChangeListener(this.getCheckInputValidListener());
      this.taComments.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            FigletExportWizardPage5.this.getModel().setComment(FigletExportWizardPage5.this.taComments.getText());
         }
      });
      this.tfFilename.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FigletExportWizardPage5.this.getModel().setName(FigletExportWizardPage5.this.tfFilename.getText());
         }
      });
      return panel;
   }

   @Override
   public boolean canFinish() {
      return this.getModel().canFinish();
   }

   @Override
   public void requestFocus() {
      this.tfFilename.requestFocus();
   }
}
