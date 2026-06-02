package de.jave.javeplayer.persistence;

import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.javeplayer.AnimationMetaData;
import de.jave.javeplayer.AnimationProperties;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.javeplayer.JaveAnimationFrame;
import de.jave.javeplayer.JavePlayerUtilities;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.dizzy.commons.core.io.IOUtilities;

public class JaveAnimationFileWriter {
   public void write(JaveAnimationFile animation, File file) throws Exception {
      BufferedWriter writer = null;

      try {
         FileWriter fileWriter = new FileWriter(file);
         writer = new BufferedWriter(fileWriter);
         int lastCursorX = -1;
         int lastCursorY = -1;
         int lastScrollX = -1;
         int lastScrollY = -1;
         writeMetaDataTags(writer, animation.getMetaData());
         writeAnimationPropertiesTags(writer, animation.getProperties());

         for (int i = 0; i < animation.getFrameCount(); i++) {
            JaveAnimationFrame currentFrame = animation.getFrame(i);
            char[][] content = currentFrame.getContent();
            writer.write("J:");
            writer.write(AsciiPacker.encodeOptimized(content));
            writer.newLine();
            char[][] selection = currentFrame.getSelection();
            if (selection != null) {
               writer.write("S:");
               writer.write(String.valueOf(currentFrame.getSelectionX()));
               writer.write(" ");
               writer.write(String.valueOf(currentFrame.getSelectionY()));
               writer.write(" ");
               writer.write(AsciiPacker.encodeOptimized(selection));
               writer.newLine();
            }

            int cursorX = currentFrame.getCursorX();
            int cursorY = currentFrame.getCursorY();
            if (cursorX != lastCursorX || cursorY != lastCursorY) {
               writer.write("|:");
               writer.write(String.valueOf(cursorX));
               writer.write(" ");
               writer.write(String.valueOf(cursorY));
               writer.newLine();
               lastCursorX = cursorX;
               lastCursorY = cursorY;
            }

            int scrollX = currentFrame.getScrollX();
            int scrollY = currentFrame.getScrollY();
            if (scrollX != lastScrollX || scrollY != lastScrollY) {
               writer.write("^:");
               writer.write(String.valueOf(scrollX));
               writer.write(" ");
               writer.write(String.valueOf(scrollY));
               writer.newLine();
               lastScrollX = scrollX;
               lastScrollY = scrollY;
            }

            String tool = currentFrame.getTool();
            if (tool != null) {
               writer.write("T:");
               writer.write(tool.trim());
               writer.newLine();
            }

            String action = currentFrame.getAction();
            if (action != null) {
               writer.write("A:");
               writer.write(action.trim());
               writer.newLine();
            }
         }
      } catch (IOException var22) {
         throw new Exception("Error saving Jave animation: " + var22);
      } finally {
         IOUtilities.close(writer);
      }
   }

   public static void writeAnimationPropertiesTags(BufferedWriter writer, AnimationProperties properties) throws IOException {
      int duration = properties.getFrameDuration();
      writer.write("+:");
      writer.write(String.valueOf(duration));
      writer.newLine();
      Color fg = properties.getForegroundColor();
      Color bg = properties.getBackgroundColor();
      writer.write("C:");
      writer.write(JavePlayerUtilities.colorToHex(bg));
      writer.write(" ");
      writer.write(JavePlayerUtilities.colorToHex(fg));
      writer.newLine();
   }

   public static void writeMetaDataTags(BufferedWriter writer, AnimationMetaData metaData) throws IOException {
      if (metaData.getTitle() != null) {
         writer.write("!:");
         writer.write(metaData.getTitle().trim());
         writer.newLine();
      }

      if (metaData.getSoftware() != null) {
         writer.write("*:");
         writer.write(metaData.getSoftware().trim());
         writer.newLine();
      }

      if (metaData.getDate() != null) {
         writer.write("D:");
         writer.write(metaData.getDate().trim());
         writer.newLine();
      }

      if (metaData.getAuthorName() != null) {
         writer.write("N:");
         writer.write(metaData.getAuthorName().trim());
         writer.newLine();
      }

      if (metaData.getAuthorEmail() != null) {
         writer.write("@:");
         writer.write(metaData.getAuthorEmail().trim());
         writer.newLine();
      }
   }
}
