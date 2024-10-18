package de.jave.asciimation.export.applet;

import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.asciimation.export.jmov.JmovAnimationExporter;
import de.jave.jave.JaveGlobalRessources;
import de.jave.lib.io.IoUtilities;
import de.jave.lib.net.HtmlUtilities;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.disy.commons.core.io.IOUtilities;

public class AppletAnimationExporter extends JmovAnimationExporter {
   public AppletAnimationExporter(AnimationExportOptions options) {
      super(options);
   }

   @Override
   protected File getJmovOutputFile() {
      return new File(this.getOptions().getFileNameWithoutExtension() + ".jmov");
   }

   @Override
   public void finish() throws Exception {
      super.finish();
      this.createHtmlFile();
      this.copyPlayerFile();
   }

   private void createHtmlFile() throws IOException {
      Dimension plateSize = this.calculateExpectedPlatePixelSize(this.getFrameSize());
      int width = plateSize.width;
      if (this.getOptions().getAdditionalOptions().isControls() && width < 320) {
         width = 320;
      }

      int height = plateSize.height;
      if (this.getOptions().getAdditionalOptions().isControls()) {
         height += 27;
      }

      String widthString = String.valueOf(width);
      String heightString = String.valueOf(height);
      String controlsValueString = this.getOptions().getAdditionalOptions().isControls() ? "1" : "0";
      String autoStartValueString = "0";
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(this.getOptions().getFile()));
         writeLine(writer, "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
         writeLine(writer, "<html>");
         writeLine(writer, "\t<head>");
         writer.write("\t\t<title>");
         if (this.getTitle() == null) {
            writer.write("Ascii Animation");
         } else {
            writer.write(HtmlUtilities.encode(this.getTitle()));
         }

         writeLine(writer, "</title>");
         writeLine(writer, "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
         writeLine(writer, "\t</head>");
         writeLine(writer, "\t<body>");
         writeLine(
            writer,
            "\t\t<applet code=\"de.jave.javeplayer.JavePlayerApplet\" archive=\"javeplayer.jar\" width=\""
               + widthString
               + "\" height=\""
               + heightString
               + "\">"
         );
         writeLine(writer, "\t\t\t<param name=\"file\"       value=\"" + this.getJmovOutputFile().getName() + "\" />");
         writeLine(writer, "\t\t\t<param name=\"controls\"  value=\"" + controlsValueString + "\" />");
         writeLine(writer, "\t\t\t<param name=\"autostart\"       value=\"0\" />");
         writeLine(writer, "\t\t\t<h3>Your browser is not configured or able to run Java Applets</h3>");
         writeLine(writer, "\t\t\t<blockquote>");
         writeLine(writer, "\t\t\t\tViewing this animation requires a Java-enabled browser with Java 1.4 or greater installed.");
         writeLine(writer, "\t\t\t\t<ul>");
         writeLine(writer, "\t\t\t\t\t<li>You need to install Java Plug-in, which happens automatically when");
         writeLine(writer, "\t\t\t\t\tyou <a href=\"http://java.sun.com/j2se/downloads.html\" target=\"_blank\">install the J2SE JRE or SDK</a>.</li>");
         writeLine(writer, "\t\t\t\t\t<li>If your browser still does support Java, perhaps you have Java turned off.</li>");
         writeLine(writer, "\t\t\t\t\t<li>You can find more information in the <a href=\"http://java.sun.com/products/plugin\" target=\"_blank\">Java");
         writeLine(writer, "\t\t\t\t\tPlug-in home page.</a></li>");
         writeLine(writer, "\t\t\t\t</ul>");
         writeLine(writer, "\t\t\t</blockquote>");
         writeLine(writer, "\t\t</applet>");
         writeLine(writer, "\t</body>");
         writer.write("</html>");
      } finally {
         IOUtilities.close(writer);
      }
   }

   private static void writeLine(BufferedWriter writer, String text) throws IOException {
      writer.write(text);
      writer.newLine();
   }

   private void copyPlayerFile() throws Exception, IOException {
      File playerFile = new File(JaveGlobalRessources.codeBase, "javeplayer.jar");
      if (playerFile.exists() && playerFile.canRead()) {
         File playerTargetFile = new File(this.getOptions().getFile().getParentFile(), playerFile.getName());
         IoUtilities.copy(playerFile, playerTargetFile);
      } else {
         throw new Exception("Unable to load player file " + playerFile.getAbsolutePath());
      }
   }
}
