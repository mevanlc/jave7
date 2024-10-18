package de.jave.jave;

import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.area.BooleanArea;
import de.jave.undo.UndoState;
import java.awt.Point;

public class CompressedDocumentState implements UndoState {
   private String selectionContent;
   private String selectionMask;
   private Point selectionLocation;
   private String content;
   private int scrollX;
   private int scrollY;
   private int cursorX;
   private int cursorY;
   private String toolName;
   private String actionName;
   private ColorScheme colorScheme;
   private int duration;

   CompressedDocumentState() {
   }

   public CompressedDocumentState(
      char[][] content,
      Point scrollOrigin,
      char[][] selectionContent,
      Point selectionLocation,
      BooleanArea selectionMask,
      Point cursorLocation,
      String toolName,
      String actionName,
      ColorScheme colorScheme
   ) {
      this.actionName = actionName;
      this.toolName = toolName;
      this.colorScheme = colorScheme;
      this.content = AsciiPacker.encode(content);
      if (selectionContent == null) {
         this.selectionContent = null;
      } else {
         this.selectionContent = AsciiPacker.encode(selectionContent);
      }

      if (selectionMask == null) {
         this.selectionMask = null;
      } else {
         this.selectionMask = selectionMask.toString();
      }

      if (scrollOrigin != null) {
         this.scrollX = scrollOrigin.x;
         this.scrollY = scrollOrigin.y;
      } else {
         this.scrollX = 0;
         this.scrollY = 0;
      }

      if (selectionLocation == null) {
         this.selectionLocation = null;
      } else {
         this.selectionLocation = new Point(selectionLocation.x, selectionLocation.y);
      }

      if (cursorLocation != null) {
         this.cursorX = cursorLocation.x;
         this.cursorY = cursorLocation.y;
      } else {
         this.cursorX = 0;
         this.cursorY = 0;
      }
   }

   public void setTool(String tool) {
      this.toolName = tool;
   }

   public void setAction(String action) {
      this.actionName = action;
   }

   public String getAction() {
      return this.actionName;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public void setSelectionContent(String selectionContent) {
      this.selectionContent = selectionContent;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setSelectionMask(String selectionMask) {
      this.selectionMask = selectionMask;
   }

   public void setColorScheme(ColorScheme colorScheme) {
      this.colorScheme = colorScheme;
   }

   public ColorScheme getColorScheme() {
      return this.colorScheme;
   }

   public void setScrollX(int scrollX) {
      this.scrollX = scrollX;
   }

   public void setScrollY(int scrollY) {
      this.scrollY = scrollY;
   }

   public void setCursorX(int cursorX) {
      this.cursorX = cursorX;
   }

   public void setCursorY(int cursorY) {
      this.cursorY = cursorY;
   }

   public int getCursorX() {
      return this.cursorX;
   }

   public int getCursorY() {
      return this.cursorY;
   }

   public void setSelectionLocation(int selectionX, int selectionY) {
      this.selectionLocation = new Point(selectionX, selectionY);
   }

   @Override
   public int getEstimatedMemorySize() {
      int result = 0;
      if (this.content != null) {
         result += this.content.length();
      }

      if (this.selectionContent != null) {
         result += this.selectionContent.length();
      }

      if (this.toolName != null) {
         result += this.toolName.length();
      }

      if (this.actionName != null) {
         result += this.toolName.length();
      }

      return result + 50;
   }

   @Override
   public String getUndoActionName() {
      return this.actionName;
   }

   public char[][] getContent() {
      return this.content == null ? null : AsciiPacker.decode(this.content);
   }

   public Point getScrollOrigin() {
      return new Point(this.scrollX, this.scrollY);
   }

   public int getScrollX() {
      return this.scrollX;
   }

   public int getScrollY() {
      return this.scrollX;
   }

   public String getPackedSelectionContent() {
      return this.selectionContent;
   }

   public String getPackedContent() {
      return this.content;
   }

   public boolean hasSelection() {
      return this.selectionContent != null;
   }

   public char[][] getSelectionContent() {
      return this.selectionContent == null ? null : AsciiPacker.decode(this.selectionContent);
   }

   public Point getSelectionLocation() {
      return this.selectionLocation;
   }

   public boolean hasSelectionMask() {
      return this.selectionMask != null;
   }

   public BooleanArea getSelectionMask() {
      return this.selectionMask == null ? null : new BooleanArea(this.selectionMask);
   }

   public String getToolName() {
      return this.toolName;
   }

   public Point getCursorLocation() {
      return new Point(this.cursorX, this.cursorY);
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("J:");
      sb.append(this.content);
      sb.append('\n');
      sb.append("^:");
      sb.append(this.scrollX);
      sb.append(' ');
      sb.append(this.scrollY);
      sb.append('\n');
      sb.append("|:");
      sb.append(this.cursorX);
      sb.append(' ');
      sb.append(this.cursorY);
      sb.append('\n');
      sb.append("C:");
      sb.append(this.colorScheme.getColorHex());
      sb.append('\n');
      if (this.selectionContent != null) {
         sb.append("S:");
         sb.append(this.selectionLocation.x);
         sb.append(' ');
         sb.append(this.selectionLocation.y);
         sb.append(' ');
         sb.append(this.selectionContent);
         sb.append('\n');
         if (this.selectionMask != null) {
            sb.append("M:");
            sb.append(this.selectionMask);
            sb.append('\n');
         }
      }

      if (this.toolName != null) {
         sb.append("T:");
         sb.append(this.toolName);
         sb.append('\n');
      }

      if (this.actionName != null) {
         sb.append("A:");
         sb.append(this.actionName);
         sb.append('\n');
      }

      sb.append("+:");
      sb.append(this.duration);
      sb.append('\n');
      return sb.toString();
   }
}
