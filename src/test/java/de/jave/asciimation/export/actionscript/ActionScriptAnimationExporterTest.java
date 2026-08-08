package de.jave.asciimation.export.actionscript;

import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import java.io.BufferedWriter;
import java.io.StringWriter;
import org.junit.Assert;
import org.junit.Test;

public class ActionScriptAnimationExporterTest {
   @Test
   public void emittedPlayerIncludesAlgorithmCDecoder() throws Exception {
      JaveAnimationFrame frame = new JaveAnimationFrame();
      frame.setContent(new CharacterPlate("Ae\u20DD\uD83D\uDE00"));
      JaveAnimationFile animation = new JaveAnimationFile();
      animation.add(frame);
      StringWriter output = new StringWriter();
      BufferedWriter writer = new BufferedWriter(output);

      ActionScriptAnimationExporter.writeToActionScript(writer, animation, false);
      writer.flush();

      Assert.assertTrue(output.toString().contains("static function decodeC(code)"));
      Assert.assertTrue(output.toString().contains("case 'C':"));
      Assert.assertTrue(output.toString().contains("\"C3 1 A%{2:e\u20DD}\uD83D\uDE00\""));
   }
}
