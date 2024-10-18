package de.jave.figlet.engine;

public class FigmlToken {
   public static final int STRING = 0;
   public static final int NEWLINE = 1;
   public static final int BOLD = 2;
   public static final int ENDBOLD = 3;
   public static final int VALIGN = 4;
   public static final int HALIGN = 5;
   public static final int FONT = 6;
   public static final int CONTROL = 7;
   public static final int CONTROL_END = 8;
   public static final int CONTROL_END_ALL = 9;
   public static final int COLOR = 10;
   public static final int HR = 11;
   public static final String[] TOKEN_STR = new String[]{
      "STRING", "NEWLINE", "BOLD", "ENDBOLD", "VALIGN", "HALIGN", "FONT", "CONTROL", "CONTROL_END", "CONTROL_END_ALL", "COLOR", "HR"
   };
   private final int type;
   private final String text;

   public FigmlToken(int type, String text) {
      this.type = type;
      this.text = text;
   }

   @Override
   public String toString() {
      return this.text == null ? "TOKEN<" + TOKEN_STR[this.type] + ">" : "TOKEN<" + TOKEN_STR[this.type] + "><" + this.text + ">";
   }

   public void print() {
      System.out.print(this.toString());
   }

   public void println() {
      System.out.println(this.toString());
   }

   public String getText() {
      return this.text;
   }

   public int getType() {
      return this.type;
   }
}
