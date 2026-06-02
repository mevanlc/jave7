package de.jave.figlet.io;

import de.jave.figlet.file.IFigFontCategorization;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.file.IFigFontCategoryContainer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class FigCategoriesFileWriter {
   public void write(IFigFontCategorization categorization, File outputFile) throws IOException {
      BufferedWriter writer = null;

      try {
         FileWriter fileWriter = new FileWriter(outputFile);
         writer = new BufferedWriter(fileWriter);
         this.writeHeader(writer);
         this.writeCategory(writer, "", categorization);
      } finally {
         IOUtilities.close(writer);
      }
   }

   private void writeHeader(BufferedWriter writer) throws IOException {
      writer.write("#################################################################");
      writer.newLine();
      writer.write("# FIGlet font categorization file");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("# This file contains a list of all font categories with a list");
      writer.newLine();
      writer.write("# of what fonts belong to each of them.");
      writer.newLine();
      writer.write("# Categories can be ordered in a hierarchy. Format:");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("#   [root]           - defines a category named 'root'.");
      writer.newLine();
      writer.write("#   [root|category1] - defines a subcategory 'category1' to a top");
      writer.newLine();
      writer.write("#                      level category named 'root'");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("# Font categories can have a description:");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("#   [root]{This is the description}");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("# Fonts in the font folder that are not listed in this file");
      writer.newLine();
      writer.write("# will be displayed in the category \"New Fonts\". So you");
      writer.newLine();
      writer.write("# can add new Fonts easily by copying them into the font folder.");
      writer.newLine();
      writer.write("# Also there is an automatically generated font category \"All Fonts\"");
      writer.newLine();
      writer.write("# That will be created by the FIGdriver when loading this file.");
      writer.newLine();
      writer.write("#");
      writer.newLine();
      writer.write("# If you have a suggestion for improvements to this file");
      writer.newLine();
      writer.write("# write to:");
      writer.newLine();
      writer.write("#                markus@jave.de");
      writer.newLine();
      writer.write("#################################################################");
      writer.newLine();
   }

   private void writeCategory(BufferedWriter writer, String prefix, IFigFontCategoryContainer category) throws IOException {
      if (prefix.length() > 0) {
         prefix = prefix + "|";
      }

      if (category instanceof IFigFontCategory) {
         IFigFontCategory fontCategory = (IFigFontCategory)category;
         if (fontCategory.isDynamicallyGenerated()) {
            return;
         }

         prefix = prefix + fontCategory.getName();
         if (fontCategory.getFontCount() > 0 || fontCategory.getDescription() != null) {
            writer.write("[" + prefix + "]");
            String description = fontCategory.getDescription();
            if (description != null) {
               writer.write("{" + description + "}");
            }

            writer.newLine();
            String[] fontNames = fontCategory.getFontNames();

            for (int i = 0; i < fontNames.length; i++) {
               writer.write(fontNames[i]);
               writer.newLine();
            }

            writer.newLine();
         }
      }

      IFigFontCategory[] categories = category.getChildCategories();

      for (int i = 0; i < categories.length; i++) {
         this.writeCategory(writer, prefix, categories[i]);
      }
   }
}
