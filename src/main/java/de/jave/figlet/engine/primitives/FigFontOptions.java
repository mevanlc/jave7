package de.jave.figlet.engine.primitives;

public class FigFontOptions {
   private String comments;
   private int height;
   private char hardblank;
   private int baseline;
   private int maxLength;
   private int codetagCount;
   private FigLayout layout;
   private int commentLineCount;
   private int actualMaxLineWidth;

   public void setHardblank(char hardblank) {
      this.hardblank = hardblank;
   }

   public void setHeight(int height) {
      this.height = height;
   }

   public int getBaseline() {
      return this.baseline;
   }

   public char getHardblank() {
      return this.hardblank;
   }

   public int getHeight() {
      return this.height;
   }

   public void setBaseline(int baseline) {
      this.baseline = baseline;
   }

   public int getMaxLength() {
      return this.maxLength;
   }

   public void setMaxLength(int i) {
      this.maxLength = i;
   }

   public int getActualMaxLineWidth() {
      return this.actualMaxLineWidth;
   }

   public void setActualMaxLineWidth(int lineLength) {
      this.actualMaxLineWidth = lineLength;
   }

   public int getCodetagCount() {
      return this.codetagCount;
   }

   public void setCodetagCount(int i) {
      this.codetagCount = i;
   }

   public FigLayout getLayout() {
      return this.layout;
   }

   public void setLayout(FigLayout layout) {
      this.layout = layout;
   }

   public int getCommentLineCount() {
      return this.commentLineCount;
   }

   public void setCommentLineCount(int i) {
      this.commentLineCount = i;
   }

   public void setComments(String comments) {
      this.comments = comments;
   }

   public String getComments() {
      return this.comments;
   }
}
