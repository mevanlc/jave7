package de.jave.jave;

import de.jave.jave.preferences.ColorScheme;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.io.IOUtilities;

public class JaveLogFileParser {
   public static CompressedDocumentState[] load(File file) {
      List<CompressedDocumentState> frames = new ArrayList<>();
      BufferedReader reader = null;

      CompressedDocumentState currentFrame;
      try {
         reader = new BufferedReader(new FileReader(file));
         if (reader == null) {
            throw new IOException("Unable to open file '" + file.getAbsolutePath() + "'");
         }

         int frameCount = 0;
         currentFrame = null;
         ColorScheme colorScheme = ColorScheme.BLACK_ON_WHITE;

         String s;
         while ((s = reader.readLine()) != null) {
            if (s.startsWith("J:")) {
               if (currentFrame != null) {
                  frames.add(currentFrame);
               }

               currentFrame = new CompressedDocumentState();
               currentFrame.setColorScheme(colorScheme);
               frameCount++;

               try {
                  String content = s.substring(2);
                  currentFrame.setContent(content);
               } catch (Exception var28) {
                  System.err.println("Warning: Error in format of JavE jmov-Logfile.");
               }
            } else if (s.startsWith("S:")) {
               try {
                  int i1 = 2;
                  int i2 = s.indexOf(32, 3);
                  int i3 = s.indexOf(32, i2 + 1);
                  String s1 = s.substring(2, i2);
                  String s2 = s.substring(i2 + 1, i3);
                  String content = s.substring(i3 + 1);
                  int selectionX = Integer.parseInt(s1);
                  int selectionY = Integer.parseInt(s2);
                  currentFrame.setSelectionContent(content);
                  currentFrame.setSelectionLocation(selectionX, selectionY);
               } catch (Exception var27) {
                  System.err.println("Warning: Error in format of JavE jmov-Logfile.");
               }
            } else if (s.startsWith("M:")) {
               currentFrame.setSelectionMask(s.substring(2));
            } else if (s.startsWith("T:")) {
               String toolName = s.substring(2);
               currentFrame.setTool(toolName);
            } else if (s.startsWith("A:")) {
               String actionName = s.substring(2);
               currentFrame.setAction(actionName);
            } else if (s.startsWith("|:")) {
               int i1 = s.indexOf(32, 2);

               try {
                  int cursorX = Integer.parseInt(s.substring(2, i1));
                  int cursorY = Integer.parseInt(s.substring(i1 + 1));
                  currentFrame.setCursorX(cursorX);
                  currentFrame.setCursorY(cursorY);
               } catch (Exception var26) {
                  System.err.println("Warning: Error in format of JavE animation: " + s);
               }
            } else if (s.startsWith("^:")) {
               int i1 = s.indexOf(32, 2);

               try {
                  int scrollX = Integer.parseInt(s.substring(2, i1));
                  int scrollY = Integer.parseInt(s.substring(i1 + 1));
                  currentFrame.setScrollX(scrollX);
                  currentFrame.setScrollY(scrollY);
               } catch (Exception var25) {
                  System.err.println("Warning: Error in format of JavE animation: " + s);
               }
            } else if (s.startsWith("+:")) {
               try {
                  int duration = Integer.parseInt(s.substring(2));
                  currentFrame.setDuration(duration);
               } catch (Exception var24) {
                  System.err.println("Warning: Error in format of JavE animation: " + s);
               }
            } else if (s.startsWith("C:")) {
               s = s.substring(2);
               ColorScheme[] schemes = ColorScheme.getAll();

               for (int i = 0; i < schemes.length; i++) {
                  if (s.equals(schemes[i].getColorHex())) {
                     colorScheme = schemes[i];
                     currentFrame.setColorScheme(colorScheme);
                     break;
                  }
               }
            }
         }

         if (currentFrame != null) {
            frames.add(currentFrame);
         }

         return frames.toArray(new CompressedDocumentState[0]);
      } catch (FileNotFoundException var29) {
         return null;
      } catch (IOException var30) {
         currentFrame = null;
      } finally {
         IOUtilities.close(reader);
      }

      return new CompressedDocumentState[] { currentFrame };
   }
}
