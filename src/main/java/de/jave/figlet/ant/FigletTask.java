package de.jave.figlet.ant;

import de.jave.figlet.Figlet;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.util.FigException;
import de.jave.lib.net.HtmlUtilities;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Echo;

public class FigletTask extends Echo {
   private static final String FORMAT_HTML = "html";
   private static final String FORMAT_PLAIN = "plain";
   private String fontName = "standard";
   private File fontsDir;
   private boolean printFontComments = false;
   private String outputFormat = null;

   public String getFontName() {
      return this.fontName;
   }

   public void setFontName(String fontName) {
      this.fontName = fontName;
   }

   public void execute() throws BuildException {
      if (this.getOutputFormat() != null && !"html".equals(this.getOutputFormat()) && !"plain".equals(this.getOutputFormat())) {
         throw new BuildException(
            "Unsupported format '" + this.getOutputFormat() + "' - only '" + "html" + "' and '" + "plain" + "' are supported at the moment."
         );
      } else {
         try {
            Figlet figlet = this.createFiglet();
            String rawText = this.message;
            this.message = "";
            if (this.isPrintFontComments()) {
               this.message = this.message + this.getFontHeader(figlet) + "\n";
            }

            this.message = this.message + this.figletize(figlet, rawText);
            this.message = this.convertToOutputFormat(this.message);
            if (this.file == null && this.getOutputFormat() == null) {
               this.message = this.convertWhiteSpaceToHardBlanks(this.message);
            }
         } catch (FigException var3) {
            throw new BuildException(var3);
         }

         super.execute();
      }
   }

   protected String figletize(Figlet figlet, String rawText) throws FigException {
      return figlet.figletize(rawText, this.getFontName());
   }

   private String convertToOutputFormat(String text) {
      if (this.getOutputFormat() == null) {
         return text;
      } else if (this.getOutputFormat().equals("html")) {
         return "<pre>" + HtmlUtilities.encode(text, "\n") + "</pre>";
      } else if (this.getOutputFormat().equals("plain")) {
         return text;
      } else {
         throw new RuntimeException();
      }
   }

   private String getFontHeader(Figlet figlet) throws FigException {
      FigFont font = figlet.getFigDriver().getFont(this.getFontName());
      return font.getOptions().getComments();
   }

   private String convertWhiteSpaceToHardBlanks(String figString) {
      return figString.replace(' ', '\u007f');
   }

   protected Figlet createFiglet() throws FigException {
      return this.getFontsDir() != null ? new Figlet(this.getFontsDir()) : new Figlet();
   }

   private File getFontsDir() {
      return this.fontsDir;
   }

   public void setFontsDir(File fontsDir) {
      this.fontsDir = fontsDir;
   }

   private boolean isPrintFontComments() {
      return this.printFontComments;
   }

   public void setPrintFontComments(boolean printFontComments) {
      this.printFontComments = printFontComments;
   }

   private String getOutputFormat() {
      return this.outputFormat;
   }

   public void setOutputFormat(String outputFormat) {
      this.outputFormat = outputFormat;
   }
}
