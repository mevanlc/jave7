package de.jave.figlet.file;

public class FigFileName {
   public static final String FIGLET_FONT_FILE_EXTENSION = ".flf";
   public static final String FIGLET_CONTROL_FILE_EXTENSION = ".flc";
   private final String name;

   public FigFileName(String name) {
      if (name.indexOf(47) != -1) {
         name = name.substring(name.lastIndexOf(47) + 1);
      }

      if (!name.endsWith(".flf") && !name.endsWith(".flc")) {
         name = name + ".flf";
      }

      this.name = name;
   }

   public boolean isFont() {
      return this.name.endsWith(".flf");
   }

   public boolean isControlFile() {
      return this.name.endsWith(".flc");
   }

   public String getName() {
      return this.name;
   }

   public String getPrintName() {
      return this.getName().substring(0, this.getName().length() - this.getExtension().length());
   }

   private String getExtension() {
      return this.isControlFile() ? ".flc" : ".flf";
   }

   @Override
   public String toString() {
      return this.getName();
   }
}
