package de.jave.asciimation.export.actionscript;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.lib.CharacterPlate;
import de.jave.lib.net.HtmlUtilities;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import net.disy.commons.core.io.IOUtilities;
import net.disy.commons.core.util.Ensure;

public class ActionScriptAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private final JaveAnimationFile animationFile;

   public ActionScriptAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
      this.animationFile = new JaveAnimationFile();
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.animationFile.setProperties(animationProperties);
      this.animationFile.setMetaData(metaData);
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      JaveAnimationFrame frame = new JaveAnimationFrame();
      frame.setContent(content);
      this.animationFile.add(frame);
   }

   @Override
   public void finish() throws Exception {
      BufferedWriter writer = null;

      try {
         writer = new BufferedWriter(new FileWriter(this.options.getFile()));
         writeToActionScript(writer, this.animationFile, this.options.getAdditionalOptions().isLoop());
      } finally {
         IOUtilities.close(writer);
      }
   }

   @Override
   public void rollBack() {
   }

   public static void writeToActionScript(BufferedWriter writer, JaveAnimationFile animationFile, boolean looped) throws IOException {
      String name = "asciimation";
      writer.write("class Asciimation {");
      writer.write("  static var frameIndex = 0;\n");
      writer.write("  static var asciimation = new Array(\n");

      for (int i = 0; i < animationFile.getFrameCount(); i++) {
         JaveAnimationFrame frame = animationFile.getFrame(i);
         char[][] content = frame.getContent();
         CharacterPlate contentPlate = new CharacterPlate(content);
         if (frame.getSelection() != null) {
            CharacterPlate selectionPlate = new CharacterPlate(frame.getSelection());
            selectionPlate.pasteInto(contentPlate, frame.getSelectionX(), frame.getSelectionY());
         }

         writer.write("\"");
         writer.write(encodeFrame(contentPlate));
         writer.write("\"");
         if (i < animationFile.getFrameCount() - 1) {
            writer.write(",");
         }

         writer.write("\n");
      }

      writer.write("  );\n");
      writer.write("\n");
      writer.write("  static function runAsciimation(){\n");
      AnimationProperties properties = animationFile.getProperties();
      writer.write("    var ffamily = \"courier new\";\n");
      writer.write("    var fsize = 12;\n");
      writer.write("    var bgcolor = \"0x" + HtmlUtilities.toHexString(properties.getBackgroundColor()) + "\";\n");
      writer.write("    var fcolor = \"0x" + HtmlUtilities.toHexString(properties.getForegroundColor()) + "\";\n");
      writer.write("    var speed = " + properties.getFrameDuration() + ";\n");
      Dimension frameSize = animationFile.getFrame(0).getSize();
      int width = frameSize.width;
      int height = frameSize.height;
      writer.write("    var looped = " + (looped ? "true" : "false") + ";\n");
      writer.write("    var fwidth = " + width + ";\n");
      writer.write("    var fheight = " + height + ";\n");
      writer.write("    var lh = 0;\n");
      writer.write("\n");
      writer.write("    _root.createTextField(\"asciimation_txt\",1,0,0,fwidth*fsize*0.63,fheight*fsize*1.2);\n");
      writer.write("    _root.asciimation_txt.background=true;\n");
      writer.write("    _root.asciimation_txt.backgroundColor=bgcolor;\n");
      writer.write("    _root.asciimation_txt.selectable=true;\n");
      writer.write("    var asciimation_format = new TextFormat();\n");
      writer.write("    asciimation_format.font=ffamily;\n");
      writer.write("    asciimation_format.size=fsize;\n");
      writer.write("    asciimation_format.color=fcolor;\n");
      writer.write("    asciimation_format.leading=lh;\n");
      writer.write("\n");
      writer.write("    _root.asciimation_txt.setNewTextFormat(asciimation_format);\n");
      writer.write("    frameIndex = 0;\n");
      writer.write("    setInterval(animateAsciimation, speed, frameIndex);\n");
      writer.write("  }\n");
      writer.write("\n");
      writer.write("  static function animateAsciimation(){\n");
      writer.write("    if (frameIndex > asciimation.length-1){\n");
      writer.write("      frameIndex = 0;\n");
      writer.write("    }\n");
      writer.write("    _root.asciimation_txt.text=decode(asciimation[frameIndex]);\n");
      writer.write("    frameIndex++;\n");
      writer.write("  }\n");
      writer.write("\n");
      writer.write("\n");
      writer.write("  static function decodeA(code){\n");
      writer.write("    var length=code.length;\n");
      writer.write("  \n");
      writer.write("    //1) Algorithm marker\n");
      writer.write("    var index=1;\n");
      writer.write("  \n");
      writer.write("    //2) Width\n");
      writer.write("    var w=0;\n");
      writer.write("    var ch=code.charAt(index++);\n");
      writer.write("    var num=parseInt(ch);\n");
      writer.write("    while(!isNaN(num)){\n");
      writer.write("      w *= 10;\n");
      writer.write("      w += num;\n");
      writer.write("      ch=code.charAt(index++);\n");
      writer.write("      num=parseInt(ch);\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    //3) Height\n");
      writer.write("    var h=0;\n");
      writer.write("    ch=code.charAt(index++);\n");
      writer.write("    num=parseInt(ch);\n");
      writer.write("    while(!isNaN(num)){\n");
      writer.write("      h *= 10;\n");
      writer.write("      h += num;\n");
      writer.write("      ch=code.charAt(index++);\n");
      writer.write("      num=parseInt(ch);\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    var chars = new String();\n");
      writer.write("  \n");
      writer.write("    while(index<length)\n");
      writer.write("    {\n");
      writer.write("      ch = code.charAt(index++);\n");
      writer.write("  \n");
      writer.write("      if (ch != '%')\n");
      writer.write("          chars += ch;\n");
      writer.write("      else\n");
      writer.write("      {\n");
      writer.write("        ch=code.charAt(index++);\n");
      writer.write("        if (ch=='%')\n");
      writer.write("          chars += '%';\n");
      writer.write("        else if (ch=='0')\n");
      writer.write("          chars += \"\\n\";\n");
      writer.write("        else\n");
      writer.write("        {\n");
      writer.write("          //how many characters?\n");
      writer.write("          var count=Number(ch);\n");
      writer.write("          ch=code.charAt(index++);\n");
      writer.write("          num=parseInt(ch);\n");
      writer.write("          while(!isNaN(num))\n");
      writer.write("          {\n");
      writer.write("            count = count * 10;\n");
      writer.write("            count = count + num;\n");
      writer.write("            ch=code.charAt(index++);\n");
      writer.write("            num=parseInt(ch);\n");
      writer.write("          }\n");
      writer.write("  \n");
      writer.write("          if (ch=='%')\n");
      writer.write("            ch=code.charAt(index++);\n");
      writer.write("  \n");
      writer.write("          //count times ch\n");
      writer.write("          for (var i=0;i<count;++i)\n");
      writer.write("            chars += ch;\n");
      writer.write("        }\n");
      writer.write("      }\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    return chars;\n");
      writer.write("  }\n");
      writer.write("  \n");
      writer.write("  static function decodeB(code){\n");
      writer.write("    var length=code.length;\n");
      writer.write("  \n");
      writer.write("    //1) Algorithm marker\n");
      writer.write("    var index=1;\n");
      writer.write("  \n");
      writer.write("    //2) Width\n");
      writer.write("    var w=0;\n");
      writer.write("    var ch=code.charAt(index++);\n");
      writer.write("    var num=parseInt(ch);\n");
      writer.write("    while(!isNaN(num)){\n");
      writer.write("      w*=10;\n");
      writer.write("      w+=num;\n");
      writer.write("      ch=code.charAt(index++);\n");
      writer.write("      num=parseInt(ch);\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    //3) Height\n");
      writer.write("    var h=0;\n");
      writer.write("    ch=code.charAt(index++);\n");
      writer.write("    num=parseInt(ch);\n");
      writer.write("    while(!isNaN(num) && index<length){\n");
      writer.write("      h*=10;\n");
      writer.write("      h+=num;\n");
      writer.write("      ch=code.charAt(index++);\n");
      writer.write("      num=parseInt(ch);\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    var chars = new String();\n");
      writer.write("  \n");
      writer.write("    var i=index;\n");
      writer.write("    for (var y=0;y<h;++y){\n");
      writer.write("      for (var x=0;x<w;++x){\n");
      writer.write("        if (i>=length)\n");
      writer.write("          return chars;\n");
      writer.write("        else\n");
      writer.write("          chars += code.charAt(i);\n");
      writer.write("        ++i;\n");
      writer.write("      }\n");
      writer.write("      chars += \"\\n\";\n");
      writer.write("    }\n");
      writer.write("  \n");
      writer.write("    return chars;\n");
      writer.write("  }\n");
      writer.write("   static function decode(code){\n");
      writer.write("     var algorithm = code.charAt(0);\n");
      writer.write("     switch (algorithm){\n");
      writer.write("       case 'A':\n");
      writer.write("         return decodeA(code);\n");
      writer.write("       case 'B':\n");
      writer.write("         return decodeB(code);\n");
      writer.write("       default:\n");
      writer.write("         return \"ERROR\";\n");
      writer.write("     }\n");
      writer.write("   }\n");
      writer.write("\n");
      writer.write("  static function main(mc) {\n");
      writer.write("    runAsciimation();\n");
      writer.write("  }\n");
      writer.write("}");
   }

   public static final String encodeFrame(CharacterPlate content) {
      return encodeLine(AsciiPacker.encodeOptimized(content.getContent()));
   }

   private static final String encodeLine(String line) {
      String trimmed = TextTools.trimRight(line);
      StringBuffer result = new StringBuffer();

      for (int i = 0; i < trimmed.length(); i++) {
         char ch = trimmed.charAt(i);
         if (mustEscape(ch)) {
            result.append('\\');
         }

         result.append(ch);
      }

      return result.toString();
   }

   private static final boolean mustEscape(char ch) {
      return ch == '\\' || ch == '"';
   }
}
