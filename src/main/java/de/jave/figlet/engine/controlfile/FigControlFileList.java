package de.jave.figlet.engine.controlfile;

import de.jave.figlet.engine.IFigConversionContext;
import java.util.ArrayList;
import java.util.List;

public class FigControlFileList {
   private final List files = new ArrayList();

   public void add(FigControlFile c) {
      this.files.add(c);
   }

   public boolean remove(String name) {
      if (!name.endsWith(".flc")) {
         name = name + ".flc";
      }

      for (int i = 0; i < this.files.size(); i++) {
         if (((FigControlFile)this.files.get(i)).getName().equals(name)) {
            this.files.remove(i);
            return true;
         }
      }

      return false;
   }

   public int map(char character, IFigConversionContext context) {
      int result = character;

      for (int i = 0; i < this.files.size(); i++) {
         result = ((FigControlFile)this.files.get(i)).map(result, context);
      }

      return result;
   }

   public void dump() {
      System.out.println(this.toString());
   }

   @Override
   public String toString() {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < this.files.size(); i++) {
         sb.append(this.files.get(i).toString());
      }

      return sb.toString();
   }
}
