package de.jave.jave;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.formula.parser.Formula;
import de.jave.formula.parser.TokenMgrError;
import de.jave.jave.help.JaveOnlineHelpHandler;
import de.jave.jave.preferences.ColorScheme;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dizzy.commons.core.message.BasicMessage;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.swing.dialog.core.IDialogHelpHandler;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public final class FormulaEditorDialogPage extends AbstractDialogPage {
   private static final String EXAMPLE_FORMULA = "sqrt(pi)/5*x_i+2^{y+z}";
   private final FontModel displayFontModel;
   private final ColorScheme colorScheme;
   private BasicMessage statusMessage;
   private AsciiTextArea resultTextArea;

   public FormulaEditorDialogPage(FontModel displayFontModel, ColorScheme colorScheme) {
      super("Please enter a mathematical expression.");
      this.displayFontModel = displayFontModel;
      this.colorScheme = colorScheme;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.statusMessage == null ? this.getDefaultMessage() : this.statusMessage;
   }

   @Override
   public JComponent createContent() {
      final JTextField inputTextField = new JTextField("sqrt(pi)/5*x_i+2^{y+z}");
      inputTextField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FormulaEditorDialogPage.this.convert(inputTextField.getText());
         }
      });
      inputTextField.selectAll();
      AsciiTextAreaProperties properties = new AsciiTextAreaProperties(this.displayFontModel);
      properties.setEditable(false);
      properties.setForeground(this.colorScheme.getColorText());
      properties.setBackground(this.colorScheme.getColorPlateBackground());
      this.resultTextArea = new AsciiTextArea(new Dimension(40, 7), properties);
      inputTextField.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(inputTextField, "North");
      mainPanel.add(this.resultTextArea.getContent(), "Center");
      this.convert(inputTextField.getText());
      return mainPanel;
   }

   @Override
   public String getTitle() {
      return "Mathematical Expression Editor";
   }

   @Override
   public IDialogHelpHandler getHelpHandler() {
      return new JaveOnlineHelpHandler("formula2/formula2.html");
   }

   private void convert(String code) {
      String result = null;
      boolean syntaxOk = true;
      if (code.trim().length() == 0) {
         result = "";
         syntaxOk = true;
      } else {
         while (result == null) {
            try {
               result = Formula.toAscii(code);
            } catch (TokenMgrError var5) {
               syntaxOk = false;
               result = null;
               if (code.length() == 0) {
                  result = "";
               } else {
                  code = code.substring(0, code.length() - 1);
               }
            } catch (Exception var6) {
               syntaxOk = false;
               result = null;
               if (code.length() == 0) {
                  result = "";
               } else {
                  code = code.substring(0, code.length() - 1);
               }
            }
         }
      }

      this.statusMessage = syntaxOk ? null : new BasicMessage("Syntax error in mathematical expression. Please enter a valid expression.", MessageType.ERROR);
      this.resultTextArea.setText(result);
      this.checkInputValid();
   }

   public String getResult() {
      return this.resultTextArea.getText();
   }
}
