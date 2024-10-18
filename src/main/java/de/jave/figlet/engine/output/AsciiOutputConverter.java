package de.jave.figlet.engine.output;

import de.jave.figlet.engine.primitives.FigFragment;
import de.jave.text.QuickString;

public class AsciiOutputConverter {
   public static String getAs(FigFragment fragment, int type) {
      switch (type) {
         case -1:
            return getAsRAW(fragment.getLines(), fragment.getMaxLineLength());
         case 0:
            return getAsASCII(fragment.getLines());
         case 1:
            return getAsHTML(fragment.getLines());
         case 2:
            return getAsTEX(fragment.getLines());
         case 3:
            return getAsCPP(fragment.getLines());
         case 4:
            return getAsC(fragment.getLines());
         case 5:
            return getAsJAVA(fragment.getLines());
         case 6:
            return getAsModula2B(fragment.getLines());
         case 7:
            return getAsModula2(fragment.getLines());
         case 8:
            return getAsShell1(fragment.getLines());
         case 9:
            return getAsShell2(fragment.getLines());
         case 10:
            return getAsShell3(fragment.getLines());
         default:
            return getAsASCII(fragment.getLines());
      }
   }

   private static String getAsASCII(QuickString[] lines) {
      StringBuffer result = new StringBuffer();

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         lines[i].trimRight();
         result.append(lines[i]);
         result.append('\n');
      }

      return result.toString();
   }

   private static String getAsRAW(QuickString[] lines, int maxLineLength) {
      QuickString result = new QuickString(lines.length * (maxLineLength + 1));

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         result.append(lines[i]);
         result.append('\n');
      }

      return result.toString();
   }

   private static String getAsTEX(QuickString[] lines) {
      String header = "% Vielen Dank f\"ur die Bereitstellung dieses TeX-Headers an\n% Torsten Ullrich\n% \\documentstyle[german, a4]{article}\n\\documentclass{article}\n\\usepackage{a4, german}\n\n\\begin{document}\n\n%\"Andern der Schriftgr\"o\"se\n\n% \\tiny\n\\scriptsize\n% \\footnotesize\n% \\small\n% \\normalsize\n% \\large\n% \\Large\n% \\LARGE\n% \\huge\n% \\Huge\n\n%In der folgenden Umgebung\n%wird der ASCII-Text genau so dargestellt, wie er in der \n%Umgebung steht. Einzige Ausnahme: Der Befehl zum Beenden der Umgebung\n%kann in einer solchen Umgebung nicht ausgegeben werden. Dies stellt\n%jedoch keine ernsthafte Einschr\"ankung\n\n\n\\begin{verbatim}\n";
      String footer = "\\end{verbatim}\n\\end{document}";
      return getBlockCommented(
         lines,
         "% Vielen Dank f\"ur die Bereitstellung dieses TeX-Headers an\n% Torsten Ullrich\n% \\documentstyle[german, a4]{article}\n\\documentclass{article}\n\\usepackage{a4, german}\n\n\\begin{document}\n\n%\"Andern der Schriftgr\"o\"se\n\n% \\tiny\n\\scriptsize\n% \\footnotesize\n% \\small\n% \\normalsize\n% \\large\n% \\Large\n% \\LARGE\n% \\huge\n% \\Huge\n\n%In der folgenden Umgebung\n%wird der ASCII-Text genau so dargestellt, wie er in der \n%Umgebung steht. Einzige Ausnahme: Der Befehl zum Beenden der Umgebung\n%kann in einer solchen Umgebung nicht ausgegeben werden. Dies stellt\n%jedoch keine ernsthafte Einschr\"ankung\n\n\n\\begin{verbatim}\n",
         "\\end{verbatim}\n\\end{document}"
      );
   }

   private static String getAsHTML(QuickString[] lines) {
      StringBuffer result = new StringBuffer();
      result.append("<!-- this code fragment was automatically generate using FigletFIGML 0.1 -->\n");
      result.append("<table border=0>\n");
      result.append("<tr><td>\n");
      result.append("<code><pre>\n");

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         lines[i].trimRight();
         String s = lines[i].toString();

         for (int i1 = s.indexOf(60); i1 != -1; i1 = s.indexOf(60, i1 - 1)) {
            s = s.substring(0, i1) + "&lt;" + s.substring(i1 + 1);
         }

         for (int var5 = s.indexOf(62); var5 != -1; var5 = s.indexOf(62, var5 - 1)) {
            s = s.substring(0, var5) + "&gt;" + s.substring(var5 + 1);
         }

         result.append(s);
         result.append('\n');
      }

      result.append("</pre></code>\n");
      result.append("</td></tr>\n");
      result.append("</table>\n");
      result.append("<!-- end of figlet code -->");
      return result.toString();
   }

   private static String getAsJAVA(QuickString[] lines) {
      StringBuffer result = new StringBuffer();
      result.append("/**\n");

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         result.append("* ");
         result.append(lines[i]);
         result.append('\n');
      }

      result.append("*/");
      return result.toString();
   }

   private static String getAsCPP(QuickString[] lines) {
      return getLineCommented(lines, "// ", "");
   }

   private static String getAsC(QuickString[] lines) {
      return getBlockCommented(lines, "/*", "*/");
   }

   private static String getAsModula2(QuickString[] lines) {
      return getBlockCommented(lines, "(*", "*)");
   }

   private static String getAsModula2B(QuickString[] lines) {
      return getLineCommented(lines, "(* ", "*)");
   }

   private static String getAsShell1(QuickString[] lines) {
      return getLineCommented(lines, "# ", "");
   }

   private static String getAsShell2(QuickString[] lines) {
      return getLineCommented(lines, "; ", "");
   }

   private static String getAsShell3(QuickString[] lines) {
      return getLineCommented(lines, "% ", "");
   }

   private static String getLineCommented(QuickString[] lines, String start, String end) {
      StringBuffer result = new StringBuffer();

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         result.append(start);
         result.append(lines[i]);
         result.append(end);
         result.append('\n');
      }

      return result.toString();
   }

   private static String getBlockCommented(QuickString[] lines, String start, String end) {
      StringBuffer result = new StringBuffer();
      result.append(start);
      result.append('\n');

      for (int i = 0; i < lines.length; i++) {
         lines[i].replace('\u007f', ' ');
         lines[i].trimRight();
         result.append(lines[i]);
         result.append('\n');
      }

      result.append(end);
      return result.toString();
   }
}
