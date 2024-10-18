package de.jave.figlet.engine;

public class FigmlTokenizer {
   private String text;

   public FigmlTokenizer(String text) {
      this.text = text;
   }

   public FigmlToken nextToken() {
      if (this.text != null && !this.text.isEmpty()) {
         if (this.text.startsWith("<")) {
            if (this.text.startsWith("<hr")) {
               int i1 = this.text.indexOf(62);
               if (i1 != -1) {
                  String line = this.text.substring(3, i1);
                  if (line.isEmpty()) {
                     line = "-";
                  }

                  this.text = this.text.substring(i1 + 1);
                  return new FigmlToken(11, line);
               }
            } else {
               if (this.text.startsWith("<br>")) {
                  this.text = this.text.substring(4);
                  return new FigmlToken(1, null);
               }

               if (this.text.startsWith("<b>")) {
                  this.text = this.text.substring(3);
                  return new FigmlToken(2, null);
               }

               if (this.text.startsWith("</b>")) {
                  this.text = this.text.substring(4);
                  return new FigmlToken(3, null);
               }

               if (this.text.startsWith("<color=")) {
                  int i1 = this.text.indexOf(62);
                  if (i1 != -1) {
                     String color = this.text.substring(2, i1);
                     this.text = this.text.substring(i1 + 1);
                     i1 = color.indexOf(61);
                     if (i1 != -1) {
                        color = color.substring(i1 + 1);
                        return new FigmlToken(10, color);
                     }
                  }
               } else if (this.text.startsWith("<valign=")) {
                  int i1 = this.text.indexOf(62);
                  if (i1 != -1) {
                     String align = this.text.substring(8, i1);
                     this.text = this.text.substring(i1 + 1);
                     return new FigmlToken(4, align);
                  }
               } else if (this.text.startsWith("<halign=")) {
                  int i1 = this.text.indexOf(62);
                  if (i1 != -1) {
                     String align = this.text.substring(8, i1);
                     this.text = this.text.substring(i1 + 1);
                     return new FigmlToken(5, align);
                  }
               } else {
                  int i1 = this.text.indexOf(62);
                  if (i1 != -1) {
                     String command = this.text.substring(1, i1);
                     FigmlToken token = null;
                     if (command.endsWith(".flf")) {
                        token = new FigmlToken(6, command.substring(0, command.length() - 4));
                     } else if (command.equals("/flc") || command.equals("/*.flc")) {
                        token = new FigmlToken(9, null);
                     } else if (command.endsWith(".flc")) {
                        if (command.startsWith("/")) {
                           token = new FigmlToken(8, command.substring(1, command.length() - 4));
                        } else {
                           token = new FigmlToken(7, command.substring(0, command.length() - 4));
                        }
                     }

                     if (token != null) {
                        this.text = this.text.substring(i1 + 1);
                        return token;
                     }
                  }
               }
            }
         }

         StringBuffer t = new StringBuffer();
         int l = this.text.length();
         int c = 1;
         t.append(this.text.charAt(0));

         while (c < l && this.text.charAt(c) != '<') {
            char ch = this.text.charAt(c);
            t.append(ch);
            c++;
            if (ch == ' ' || ch == '-' || ch == '=') {
               break;
            }
         }

         this.text = this.text.substring(c);
         return new FigmlToken(0, t.toString());
      } else {
         return null;
      }
   }
}
