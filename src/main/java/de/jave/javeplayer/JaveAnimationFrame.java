package de.jave.javeplayer;

import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.lib.CharacterPlate;
import java.awt.Dimension;
import java.awt.Point;

public class JaveAnimationFrame {
   private String content;
   private String selection;
   private String action;
   private String tool;
   private int selectionX;
   private int selectionY;
   private int cursorX;
   private int cursorY;
   private int scrollX;
   private int scrollY;
   private boolean isSoundTrigger = false;
   public static final int DEFAULT_DURATION = 66;

   public JaveAnimationFrame duplicate() {
      JaveAnimationFrame f = new JaveAnimationFrame();
      f.content = this.content;
      f.selection = this.selection;
      f.action = this.action;
      f.tool = this.tool;
      f.selectionX = this.selectionX;
      f.selectionY = this.selectionY;
      f.cursorX = this.cursorX;
      f.cursorY = this.cursorY;
      f.scrollX = this.scrollX;
      f.scrollY = this.scrollY;
      f.isSoundTrigger = this.isSoundTrigger;
      return f;
   }

   public boolean isSoundTrigger() {
      return this.isSoundTrigger;
   }

   public void setSoundTrigger(boolean what) {
      this.isSoundTrigger = what;
   }

   public void print() {
      System.err.println("Content: " + this.content);
      System.err.println("Selection: " + this.selection);
      System.err.println("Action: " + this.action);
      System.err.println("Tool: " + this.tool);
   }

   public int[][] getContent() {
      return AsciiPacker.decode(this.content);
   }

   public int[][] getSelection() {
      return this.selection == null ? null : AsciiPacker.decode(this.selection);
   }

   public boolean isEmpty() {
      int[][] ch = this.getContent();
      if (ch.length != 0 && ch[0].length != 0) {
         int w = ch[0].length;
         int h = ch.length;

         for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
               if (ch[y][x] != ' ') {
                  return false;
               }
            }
         }

         return true;
      } else {
         return true;
      }
   }

   public String getAction() {
      return this.action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public int getCursorX() {
      return this.cursorX;
   }

   public void setCursorX(int cursorX) {
      this.cursorX = cursorX;
   }

   public int getCursorY() {
      return this.cursorY;
   }

   public void setCursorY(int cursorY) {
      this.cursorY = cursorY;
   }

   public int getScrollX() {
      return this.scrollX;
   }

   public void setScrollX(int scrollX) {
      this.scrollX = scrollX;
   }

   public int getScrollY() {
      return this.scrollY;
   }

   public void setScrollY(int scrollY) {
      this.scrollY = scrollY;
   }

   public void setSelection(String selection) {
      this.selection = selection;
   }

   public int getSelectionX() {
      return this.selectionX;
   }

   public void setSelectionX(int selectionX) {
      this.selectionX = selectionX;
   }

   public int getSelectionY() {
      return this.selectionY;
   }

   public void setSelectionY(int selectionY) {
      this.selectionY = selectionY;
   }

   public String getTool() {
      return this.tool;
   }

   public void setTool(String tool) {
      this.tool = tool;
   }

   public Point getSelectionLocation() {
      return new Point(this.selectionX, this.selectionY);
   }

   public void setContent(CharacterPlate plate) {
      this.setContent(AsciiPacker.encode(plate.glyphPlane()));
   }

   public Dimension getSize() {
      return new CharacterPlate(this.getContent()).getSize();
   }
}
