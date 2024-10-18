package de.jave.jave.plate;

import de.jave.jave.Tool;

public class ToolManager {
   private Tool currentTool;
   private Tool[] tools;

   public Tool getCurrentTool() {
      return this.currentTool;
   }

   public void setTools(Tool[] tools) {
      this.tools = tools;
   }

   public Tool[] getTools() {
      return this.tools;
   }

   public int getCurrentToolIndex() {
      for (int i = 0; i < this.tools.length; i++) {
         if (this.tools[i] == this.currentTool) {
            return i;
         }
      }

      return -1;
   }

   public Tool getTool(int i) {
      return this.tools[i];
   }

   public void setCurrentTool(Tool tool) {
      this.currentTool = tool;
   }

   public int getToolIndex(String toolName) {
      int i = 0;
      i = 0;

      while (i < this.tools.length && !this.tools[i].getName().equals(toolName)) {
         i++;
      }

      return this.tools[i].getName().equals(toolName) ? i : -1;
   }
}
