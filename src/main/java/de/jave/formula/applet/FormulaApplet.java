package de.jave.formula.applet;

import de.jave.formula.parser.Formula;
import de.jave.formula.parser.TokenMgrError;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

@SuppressWarnings("removal")
public class FormulaApplet extends Applet implements TextListener {
   private static final Font FONT = new Font("Monospaced", 0, 13);
   private TextField tfInput;
   private TextField tfStatus;
   private TextArea taResult;
   private boolean initialized = false;
   private static final String TITLE = "Jave formula2 - Mathematical expressions in ASCII";
   private static final String DEMO_FORMULA = "sqrt(PI)/5*x_i+2^y";

   @Override
   public String getAppletInfo() {
      return "Jave formula2 - Mathematical expressions in ASCII";
   }

   @Override
   public void init() {
      if (!this.initialized) {
         this.tfInput = new TextField("sqrt(PI)/5*x_i+2^y");
         this.tfInput.addTextListener(this);
         this.tfInput.selectAll();
         this.taResult = new TextArea();
         this.taResult.setEditable(false);
         this.tfStatus = new TextField("Initializing...");
         this.tfStatus.setEditable(false);
         this.tfInput.setFont(FONT);
         this.taResult.setFont(FONT);
         this.setLayout(new BorderLayout());
         this.add(this.tfInput, "North");
         this.add(this.taResult, "Center");
         this.add(this.tfStatus, "South");
         this.initialized = true;
      }
   }

   @Override
   public void start() {
      this.convert();
   }

   @Override
   public void stop() {
   }

   @Override
   public void textValueChanged(TextEvent evt) {
      this.convert();
   }

   public void convert() {
      String code = this.tfInput.getText();
      String result = null;
      boolean syntaxOk = true;
      StringBuilder cutOff = new StringBuilder();
      if (code.trim().length() == 0) {
         result = "";
         syntaxOk = true;
      } else {
         while (result == null) {
            try {
               result = Formula.toAscii(code);
            } catch (TokenMgrError var6) {
               syntaxOk = false;
               result = null;
               if (code.length() == 0) {
                  result = "";
               } else {
                  cutOff.insert(0, code.charAt(code.length() - 1));
                  code = code.substring(0, code.length() - 1);
               }
            } catch (Exception var7) {
               syntaxOk = false;
               result = null;
               if (code.length() == 0) {
                  result = "";
               } else {
                  cutOff.insert(0, code.charAt(code.length() - 1));
                  code = code.substring(0, code.length() - 1);
               }
            }
         }
      }

      this.taResult.setText(result + cutOff);
      String statusMessage = syntaxOk ? "Ok." : "Syntax error.";
      this.tfStatus.setText(statusMessage);
   }
}
