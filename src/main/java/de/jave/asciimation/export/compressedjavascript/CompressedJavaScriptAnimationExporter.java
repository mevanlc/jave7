package de.jave.asciimation.export.compressedjavascript;

import de.jave.asciimation.export.AbstractAnimationExporter;
import de.jave.asciimation.export.AnimationExportOptions;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.lib.CharacterPlate;
import de.jave.lib.net.HtmlUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import net.dizzy.commons.core.io.IOUtilities;
import net.dizzy.commons.core.util.Ensure;

public class CompressedJavaScriptAnimationExporter extends AbstractAnimationExporter {
   private final AnimationExportOptions options;
   private final List<Integer> frameDurations = new ArrayList<>();
   private int frameCount = 0;
   private int totalDuration = 0;
   private Dimension maxFrameSize;
   private BufferedWriter bw;
   private CharacterPlate lastContent = null;
   private AnimationProperties animationProperties;
   private AnimationMetaData metaData;

   public CompressedJavaScriptAnimationExporter(AnimationExportOptions options) {
      Ensure.ensureArgumentNotNull(options);
      this.options = options;
   }

   @Override
   public void init(Dimension maxFrameSize, AnimationProperties animationProperties, int frameCount, AnimationMetaData metaData) throws Exception {
      this.maxFrameSize = maxFrameSize;
      this.animationProperties = animationProperties;
      this.metaData = metaData;
      this.bw = new BufferedWriter(new FileWriter(this.options.getFile()));
   }

   @Override
   public void writeFrame(CharacterPlate content) throws Exception {
      if (this.frameCount == 0) {
         writeCompressedJavascriptAnimationHeader(
            this.bw,
            this.maxFrameSize,
            this.metaData.getTitle(),
            this.options.getAdditionalOptions().isLoop(),
            this.options.getAdditionalOptions().isControls(),
            this.animationProperties.getForegroundColor(),
            this.animationProperties.getBackgroundColor()
         );
      }

      this.writeCompressedJavascriptAnimationFrame(this.bw, content);
      this.frameDurations.add(this.animationProperties.getFrameDuration());
      this.frameCount++;
      this.totalDuration = this.totalDuration + this.animationProperties.getFrameDuration();
      this.lastContent = content;
   }

   @Override
   public void finish() throws Exception {
      this.writeCompressedJavascriptAnimationFooter(this.bw);
      IOUtilities.close(this.bw);
   }

   @Override
   public void rollBack() {
      IOUtilities.close(this.bw);
   }

   private static void writeCompressedJavascriptAnimationHeader(
      BufferedWriter bw, Dimension frameSize, String title, boolean loop, boolean controls, Color foregroundColor, Color backgroundColor
   ) throws IOException {
      writeLine(bw, "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
      writeLine(bw, "<html>");
      writeLine(bw, "\t<head>");
      writeLine(bw, "\t\t<title>");
      if (title == null) {
         bw.write("Javascript Animation");
      } else {
         bw.write(HtmlUtilities.encode(title));
      }

      writeLine(bw, "</title>");
      writeLine(bw, "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />");
      writeLine(bw, "\t</head>");
      writeLine(bw, "<body onload=\"Play()\">");
      writeLine(bw, "<form name=\"f\">");
      writeLine(
         bw,
         "<textarea cols=\""
            + frameSize.width
            + "\" rows=\""
            + frameSize.height
            + "\" name=\"ta\" basefont=\"courier\" wrap=\"physical\" style=\""
            + "border-style:none;cursor:hand;overflow:hidden;font-family: monospace;background-color:#"
            + HtmlUtilities.toHexString(backgroundColor)
            + ";color:#"
            + HtmlUtilities.toHexString(foregroundColor)
            + "\" onclick=\"performTogglePlay()\">"
      );
      writeLine(bw, "</textarea>");
      if (controls) {
         writeLine(bw, "<br />");
         writeLine(bw, "<input type=\"button\" name=\"b2\" value=' > ' onclick=\"performPlay()\" />");
         writeLine(bw, "<input type=\"button\" name=\"b1\" value=' | | ' onclick=\"performTogglePlay()\" />");
         writeLine(bw, "<input type=\"text\" size=\"20\" class=\"f\" name=\"progress\" style=\"font-family: monospace;\" />");
         bw.write("<input type=\"checkbox\" name=\"loop\"");
         if (loop) {
            bw.write(" checked=\"checked\"");
         }

         writeLine(bw, " />Loop");
      }

      writeLine(bw, "</form>");
      writeLine(bw, "<script language=\"JavaScript1.2\" type=\"text/javascript\"><!--");
      writeLine(bw, "No=0;");
      writeLine(bw, "go=true;");
      bw.write("var frames = new Array(");
   }

   private static void writeLine(BufferedWriter writer, String text) throws IOException {
      writer.write(text);
      writer.newLine();
   }

   private void writeCompressedJavascriptAnimationFrame(BufferedWriter bw, CharacterPlate content) throws IOException {
      if (this.frameCount > 0) {
         bw.write(",");
         bw.newLine();
      }

      bw.write("\"");
      String difference = getDifferenceCode(this.lastContent, content);
      if (difference != null) {
         this.writeAsJavaScript(bw, difference);
      } else {
         this.writeAsJavaScript(bw, AsciiPacker.encodeOptimized(content.getContent()));
      }

      bw.write("\"");
   }

   private void writeAsJavaScript(BufferedWriter bw, String text) throws IOException {
      StringTokenizer st2 = new StringTokenizer(text, "\\\"<>-", true);

      while (st2.hasMoreTokens()) {
         String token = st2.nextToken();
         if (token.equals("\\")) {
            bw.write("\\\\");
         } else if (token.equals("\"")) {
            bw.write("\\\"");
         } else if (token.equals("-")) {
            bw.write("\\-");
         } else if (token.equals("<")) {
            bw.write("\\x3C");
         } else if (token.equals(">")) {
            bw.write("\\x3E");
         } else {
            bw.write(token);
         }
      }
   }

   private static String getDifferenceCode(CharacterPlate cp1, CharacterPlate cp2) {
      if (cp1 != null && cp2 != null) {
         int w = cp1.getWidth();
         int h = cp1.getHeight();
         if (cp2.getWidth() == w && cp2.getHeight() == h) {
            int diffY = -1;

            for (int y = 0; y < h; y++) {
               if (!cp1.getLine(y).equals(cp2.getLine(y))) {
                  if (diffY != -1) {
                     return null;
                  }

                  diffY = y;
               }
            }

            if (diffY == -1) {
               return "";
            } else {
               String line1 = cp1.getLine(diffY);
               String line2 = cp2.getLine(diffY);
               int diffX = 0;

               while (diffX < w && line1.charAt(diffX) == line2.charAt(diffX)) {
                  diffX++;
               }

               int diffWidth = w - diffX;

               while (diffWidth > 1 && line1.charAt(diffX + diffWidth - 1) == line2.charAt(diffX + diffWidth - 1)) {
                  diffWidth--;
               }

               return diffX == 0
                  ? "D" + diffY + ":" + line2.substring(diffX, diffX + diffWidth)
                  : "D" + diffY + "%" + diffX + ":" + line2.substring(diffX, diffX + diffWidth);
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void writeCompressedJavascriptAnimationFooter(BufferedWriter bw) throws IOException {
      bw.write(");");
      bw.newLine();
      bw.newLine();
      int lastDuration = 0;
      bw.write("var MStotal = " + this.totalDuration + ";");
      bw.newLine();
      bw.write("var Delay = new Array(");

      for (int i = 0; i < this.frameDurations.size(); i++) {
         int dur = this.frameDurations.get(i);
         if (i > 0 && lastDuration == dur) {
            bw.write("0");
         } else if (dur == 0) {
            bw.write("1");
         } else {
            bw.write(String.valueOf(dur));
         }

         lastDuration = dur;
         if (i < this.frameDurations.size() - 1) {
            bw.write(",");
         }

         if ((i + 1) % 16 == 0) {
            bw.newLine();
         }
      }

      bw.write(");");
      bw.newLine();
      bw.newLine();
      bw.write("function decodeA(code){");
      bw.newLine();
      bw.write("  var length=code.length;");
      bw.newLine();
      bw.newLine();
      bw.write("  //1) Algorithm marker");
      bw.newLine();
      bw.write("  var index=1;");
      bw.newLine();
      bw.newLine();
      bw.write("  //2) Width");
      bw.newLine();
      bw.write("  var w=0;");
      bw.newLine();
      bw.write("  var ch=code.charAt(index++);");
      bw.newLine();
      bw.write("  var num=parseInt(ch);");
      bw.newLine();
      bw.write("  while(!isNaN(num)){");
      bw.newLine();
      bw.write("    w *= 10;");
      bw.newLine();
      bw.write("    w += num;");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  //3) Height");
      bw.newLine();
      bw.write("  var h=0;");
      bw.newLine();
      bw.write("  ch=code.charAt(index++);");
      bw.newLine();
      bw.write("  num=parseInt(ch);");
      bw.newLine();
      bw.write("  while(!isNaN(num)){");
      bw.newLine();
      bw.write("    h *= 10;");
      bw.newLine();
      bw.write("    h += num;");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  var chars = new String();");
      bw.newLine();
      bw.newLine();
      bw.write("  while(index<length)");
      bw.newLine();
      bw.write("  {");
      bw.newLine();
      bw.write("    ch = code.charAt(index++);");
      bw.newLine();
      bw.newLine();
      bw.write("    if (ch != '%')");
      bw.newLine();
      bw.write("        chars += ch;");
      bw.newLine();
      bw.write("    else");
      bw.newLine();
      bw.write("    {");
      bw.newLine();
      bw.write("      ch=code.charAt(index++);");
      bw.newLine();
      bw.write("      if (ch=='%')");
      bw.newLine();
      bw.write("        chars += '%';");
      bw.newLine();
      bw.write("      else if (ch=='0')");
      bw.newLine();
      bw.write("        chars += \"\\r\\n\";");
      bw.newLine();
      bw.write("      else");
      bw.newLine();
      bw.write("      {");
      bw.newLine();
      bw.write("        //how many characters?");
      bw.newLine();
      bw.write("        var count=Number(ch);");
      bw.newLine();
      bw.write("        ch=code.charAt(index++);");
      bw.newLine();
      bw.write("        num=parseInt(ch);");
      bw.newLine();
      bw.write("        while(!isNaN(num))");
      bw.newLine();
      bw.write("        {");
      bw.newLine();
      bw.write("          count = count * 10;");
      bw.newLine();
      bw.write("          count = count + num;");
      bw.newLine();
      bw.write("          ch=code.charAt(index++);");
      bw.newLine();
      bw.write("          num=parseInt(ch);");
      bw.newLine();
      bw.write("        }");
      bw.newLine();
      bw.newLine();
      bw.write("        if (ch=='%')");
      bw.newLine();
      bw.write("          ch=code.charAt(index++);");
      bw.newLine();
      bw.newLine();
      bw.write("        //count times ch");
      bw.newLine();
      bw.write("        for (var i=0;i<count;++i)");
      bw.newLine();
      bw.write("          chars += ch;");
      bw.newLine();
      bw.write("      }");
      bw.newLine();
      bw.write("    }");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  return chars;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function decodeB(code){");
      bw.newLine();
      bw.write("  var length=code.length;");
      bw.newLine();
      bw.newLine();
      bw.write("  //1) Algorithm marker");
      bw.newLine();
      bw.write("  var index=1;");
      bw.newLine();
      bw.newLine();
      bw.write("  //2) Width");
      bw.newLine();
      bw.write("  var w=0;");
      bw.newLine();
      bw.write("  var ch=code.charAt(index++);");
      bw.newLine();
      bw.write("  var num=parseInt(ch);");
      bw.newLine();
      bw.write("  while(!isNaN(num)){");
      bw.newLine();
      bw.write("    w*=10;");
      bw.newLine();
      bw.write("    w+=num;");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  //3) Height");
      bw.newLine();
      bw.write("  var h=0;");
      bw.newLine();
      bw.write("  ch=code.charAt(index++);");
      bw.newLine();
      bw.write("  num=parseInt(ch);");
      bw.newLine();
      bw.write("  while(!isNaN(num) && index<length){");
      bw.newLine();
      bw.write("    h*=10;");
      bw.newLine();
      bw.write("    h+=num;");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  var chars = new String();");
      bw.newLine();
      bw.newLine();
      bw.write("  var i=index;");
      bw.newLine();
      bw.write("  for (var y=0;y<h;++y){");
      bw.newLine();
      bw.write("    for (var x=0;x<w;++x){");
      bw.newLine();
      bw.write("      if (i>=length)");
      bw.newLine();
      bw.write("        return chars;");
      bw.newLine();
      bw.write("      else");
      bw.newLine();
      bw.write("        chars += code.charAt(i);");
      bw.newLine();
      bw.write("      ++i;");
      bw.newLine();
      bw.write("    }");
      bw.newLine();
      bw.write("    chars += \"\\r\\n\";");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  return chars;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("preFrame = \"\";");
      bw.newLine();
      bw.newLine();
      bw.write("function decodeD(code){");
      bw.newLine();
      bw.write("  var a = preFrame.split(\"\\r\\n\");");
      bw.newLine();
      bw.write("  var index = 1;");
      bw.newLine();
      bw.newLine();
      bw.write("  var y = 0;");
      bw.newLine();
      bw.write("  ch=code.charAt(index++);");
      bw.newLine();
      bw.write("  num=parseInt(ch);");
      bw.newLine();
      bw.write("  while(!isNaN(num)){");
      bw.newLine();
      bw.write("    y *= 10;");
      bw.newLine();
      bw.write("    y += num;");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  var x = 0;");
      bw.newLine();
      bw.write("  if (ch == '%'){");
      bw.newLine();
      bw.write("    ch=code.charAt(index++);");
      bw.newLine();
      bw.write("    num=parseInt(ch);");
      bw.newLine();
      bw.write("    while(!isNaN(num)){");
      bw.newLine();
      bw.write("      x *= 10;");
      bw.newLine();
      bw.write("      x += num;");
      bw.newLine();
      bw.write("      ch=code.charAt(index++);");
      bw.newLine();
      bw.write("      num=parseInt(ch);");
      bw.newLine();
      bw.write("    }");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  if (y >= a.length)");
      bw.newLine();
      bw.write("    a[y] = \"\";");
      bw.newLine();
      bw.newLine();
      bw.write("  if (a[y].length < x)");
      bw.newLine();
      bw.write("    for (i = a[y].length; i < x; i++)");
      bw.newLine();
      bw.write("      a[y] += \" \";");
      bw.newLine();
      bw.newLine();
      bw.write("  s = code.slice(index);");
      bw.newLine();
      bw.write("  a[y] = a[y].substr(0, x) + s + a[y].slice(x + s.length);");
      bw.newLine();
      bw.newLine();
      bw.write("  return a.join(\"\\r\\n\");");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function decode(code){");
      bw.newLine();
      bw.write("  if (code.length<4)");
      bw.newLine();
      bw.write("    return preFrame;");
      bw.newLine();
      bw.newLine();
      bw.write("  var algorithm = code.charAt(0);");
      bw.newLine();
      bw.write("  switch (algorithm){");
      bw.newLine();
      bw.write("    case 'A':");
      bw.newLine();
      bw.write("      preFrame = decodeA(code);");
      bw.newLine();
      bw.write("      break;");
      bw.newLine();
      bw.write("    case 'B':");
      bw.newLine();
      bw.write("      preFrame = decodeB(code);");
      bw.newLine();
      bw.write("      break;");
      bw.newLine();
      bw.write("    case 'D':");
      bw.newLine();
      bw.write("      preFrame = decodeD(code);");
      bw.newLine();
      bw.write("      break;");
      bw.newLine();
      bw.write("    default:");
      bw.newLine();
      bw.write("      return \"ERROR\";");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  return preFrame;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("lastMS=0;");
      bw.newLine();
      bw.write("MSsum=0;");
      bw.newLine();
      bw.newLine();
      bw.write("function Play(){");
      bw.newLine();
      bw.write("  ms = Delay[No];");
      bw.newLine();
      bw.write("  if (ms==0)");
      bw.newLine();
      bw.write("    ms=lastMS;");
      bw.newLine();
      bw.write("  lastMS=ms;");
      bw.newLine();
      bw.newLine();
      bw.write("  if (go){");
      bw.newLine();
      bw.write("    document.f.ta.value = decode(frames[No]);");
      bw.newLine();
      bw.write("    if (No < frames.length - 1){");
      bw.newLine();
      bw.write("      No++;");
      bw.newLine();
      bw.write("      MSsum += ms;");
      bw.newLine();
      bw.write("    }else{");
      bw.newLine();
      bw.write("      No = 0;");
      bw.newLine();
      bw.write("      MSsum = 0;");
      bw.newLine();
      bw.write("      if (!isLoop()){");
      bw.newLine();
      bw.write("        go=false;");
      bw.newLine();
      bw.write("      }");
      bw.newLine();
      bw.write("    }");
      bw.write("    updateProgress();");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.newLine();
      bw.write("  setTimeout(\"Play()\", ms);");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function isLoop() {");
      bw.newLine();
      bw.write("  if (!document.f.loop){");
      bw.newLine();
      bw.write("    return " + (this.options.getAdditionalOptions().isLoop() ? "true" : "false") + ";");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.write("  return document.f.loop.checked;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function updateProgress(){");
      bw.newLine();
      bw.write("if (!document.f.progress){");
      bw.newLine();
      bw.write("  return;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.write("    p = Math.floor(20 * MSsum / MStotal);");
      bw.newLine();
      bw.write("    s = \"\";");
      bw.newLine();
      bw.write("    for (i = 0; i < 20; i++)");
      bw.newLine();
      bw.write("      s += i == p ? \">\" : \"-\";");
      bw.newLine();
      bw.write("    document.f.progress.value = s;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function performTogglePlay()");
      bw.newLine();
      bw.write("{");
      bw.newLine();
      bw.write("  go=!go;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("function performPlay()");
      bw.newLine();
      bw.write("{");
      bw.newLine();
      bw.write("  if (go){");
      bw.newLine();
      bw.write("    No = 0;");
      bw.newLine();
      bw.write("    MSsum = 0;");
      bw.newLine();
      bw.write("  }");
      bw.newLine();
      bw.write("  go=true;");
      bw.newLine();
      bw.write("}");
      bw.newLine();
      bw.newLine();
      bw.write("  //--></script>");
      bw.newLine();
      bw.newLine();
      bw.write("</body></html>");
      bw.newLine();
   }
}
