package de.jave.javeplayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.dizzy.commons.core.util.Ensure;

public class JaveAnimationFile {
   private File file;
   private AnimationMetaData metaData = new AnimationMetaData();
   private AnimationProperties properties = new AnimationProperties();
   private final List frames = new ArrayList();
   private int defaultDuration = 66;

   public JaveAnimationFile(File file) {
      this.file = file;
   }

   public JaveAnimationFile() {
      this(null);
   }

   public void setDefaultDuration(int ms) {
      this.defaultDuration = ms;
   }

   public int getDefaultDuration() {
      return this.defaultDuration;
   }

   public AnimationMetaData getMetaData() {
      return this.metaData;
   }

   public void setMetaData(AnimationMetaData metaData) {
      Ensure.ensureArgumentNotNull(metaData);
      this.metaData = metaData;
   }

   public AnimationProperties getProperties() {
      return this.properties;
   }

   public void setProperties(AnimationProperties properties) {
      Ensure.ensureArgumentNotNull(properties);
      this.properties = properties;
   }

   public void load(URL url) throws Exception {
      this.frames.clear();
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new InputStreamReader(ZIPTool.openPossiblyZipped(url.openStream())));
         int frameCount = 0;
         this.setProperties(new AnimationProperties());
         JaveAnimationFrame currentFrame = null;
         String lastContent = null;

         String s;
         while ((s = reader.readLine()) != null) {
            if (s.startsWith("J:")) {
               if (currentFrame != null) {
                  this.frames.add(currentFrame);
               }

               currentFrame = new JaveAnimationFrame();
               frameCount++;
               if (s.length() == 2) {
                  currentFrame.setContent(lastContent);
               } else {
                  char ch = s.charAt(2);
                  if (ch >= '0' && ch <= '9') {
                     try {
                        int i1 = 2;
                        int i2 = s.indexOf(32, 3);
                        int i3 = s.indexOf(32, i2 + 1);
                        int i4 = s.indexOf(32, i3 + 1);
                        int i5 = s.indexOf(32, i4 + 1);
                        String s1 = s.substring(2, i2);
                        String s2 = s.substring(i2 + 1, i3);
                        String s3 = s.substring(i3 + 1, i4);
                        String s4 = null;
                        if (i5 != -1) {
                           s4 = s.substring(i4 + 1, i5);
                        } else {
                           s4 = s.substring(i4 + 1);
                        }

                        int scrollX = Integer.parseInt(s1);
                        int scrollY = Integer.parseInt(s2);
                        int cursorX = Integer.parseInt(s3);
                        int cursorY = Integer.parseInt(s4);
                        String content = null;
                        if (i5 == -1) {
                           content = lastContent;
                        } else {
                           content = s.substring(i5 + 1);
                        }

                        lastContent = content;
                        currentFrame.setContent(content);
                        currentFrame.setScrollX(scrollX);
                        currentFrame.setScrollY(scrollY);
                        currentFrame.setCursorX(cursorX);
                        currentFrame.setCursorY(cursorY);
                     } catch (Exception var41) {
                        System.err.println("Warning: Error in format of JavE Animation." + var41);
                     }
                  } else {
                     String content = s.substring(2);
                     lastContent = content;
                     currentFrame.setContent(content);
                  }
               }
            } else if (s.startsWith("S:")) {
               if (currentFrame != null) {
                  try {
                     int i1x = 2;
                     int i2x = s.indexOf(32, 3);
                     int i3x = s.indexOf(32, i2x + 1);
                     String s1x = s.substring(2, i2x);
                     String s2x = s.substring(i2x + 1, i3x);
                     String content = s.substring(i3x + 1);
                     int selectionX = Integer.parseInt(s1x);
                     int selectionY = Integer.parseInt(s2x);
                     currentFrame.setSelection(content);
                     currentFrame.setSelectionX(selectionX);
                     currentFrame.setSelectionY(selectionY);
                  } catch (Exception var40) {
                     System.err.println("Warning: Error in format of JavE Animation.");
                  }
               }
            } else if (s.startsWith("T:")) {
               if (currentFrame != null) {
                  String toolName = s.substring(2);
                  currentFrame.setTool(toolName);
               }
            } else if (s.startsWith("A:")) {
               if (currentFrame != null) {
                  String actionName = s.substring(2);
                  currentFrame.setAction(actionName);
               }
            } else if (s.startsWith("+:")) {
               try {
                  int duration = Integer.parseInt(s.substring(2));
                  this.properties.setFrameDuration(duration);
               } catch (NumberFormatException var39) {
                  System.err.println("Warning: Error in format of JavE Animation:  " + s);
               }
            } else if (s.startsWith("|:")) {
               if (currentFrame != null) {
                  int i1x = s.indexOf(32, 2);

                  try {
                     int cursorX = Integer.parseInt(s.substring(2, i1x));
                     int cursorY = Integer.parseInt(s.substring(i1x + 1));
                     currentFrame.setCursorX(cursorX);
                     currentFrame.setCursorY(cursorY);
                  } catch (Exception var38) {
                     System.err.println("Warning: Error in format of JavE Animation: " + s);
                  }
               }
            } else if (s.startsWith("^:")) {
               if (currentFrame != null) {
                  int i1x = s.indexOf(32, 2);

                  try {
                     int scrollX = Integer.parseInt(s.substring(2, i1x));
                     int scrollY = Integer.parseInt(s.substring(i1x + 1));
                     currentFrame.setScrollX(scrollX);
                     currentFrame.setScrollY(scrollY);
                  } catch (Exception var37) {
                     System.err.println("Warning: Error in format of JavE Animation: " + s);
                  }
               }
            } else if (s.startsWith("@:")) {
               if (this.metaData.getAuthorEmail() == null && s.length() > 2) {
                  this.metaData.setAuthorEmail(s.substring(2));
               }
            } else if (s.startsWith("N:")) {
               if (this.metaData.getAuthorName() == null && s.length() > 2) {
                  this.metaData.setAuthorName(s.substring(2));
               }
            } else if (s.startsWith("!:")) {
               if (this.metaData.getTitle() == null && s.length() > 2) {
                  this.metaData.setTitle(s.substring(2));
               }
            } else if (s.startsWith("D:")) {
               if (this.metaData.getDate() == null && s.length() > 2) {
                  this.metaData.setDate(s.substring(2));
               }
            } else if (s.startsWith("C:")) {
               try {
                  int i1x = s.indexOf(32, 2);
                  this.properties.setBackgroundColor(JavePlayerUtilities.hexToColor(s.substring(2, i1x)));
                  this.properties.setForegroundColor(JavePlayerUtilities.hexToColor(s.substring(i1x + 1)));
               } catch (Exception var36) {
                  System.err.println("Warning: Error in format of JavE Animation: " + s);
               }
            } else if (s.startsWith("*:")) {
               if (this.metaData.getSoftware() == null && s.length() > 2) {
                  this.metaData.setSoftware(s.substring(2));
               }
            } else if (s.startsWith("#") && currentFrame != null) {
               currentFrame.setSoundTrigger(true);
            }
         }

         if (currentFrame != null) {
            this.frames.add(currentFrame);
         }
      } catch (FileNotFoundException var42) {
         throw new Exception("Error loading JavE Animation: File not found: '" + url + "'", var42);
      } catch (IOException var43) {
         throw new Exception("Error loading JavE Animation: Wrong file format.", var43);
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (Exception var35) {
            }
         }
      }
   }

   public void add(JaveAnimationFrame newFrame) {
      this.frames.add(newFrame);
   }

   public void insertFrame(JaveAnimationFrame newFrame, int index) {
      this.frames.add(index, newFrame);
   }

   public void setFrameAt(JaveAnimationFrame newFrame, int index) {
      this.frames.set(index, newFrame);
   }

   public void deleteFrame(int index) {
      this.frames.remove(index);
   }

   public void moveFrameRight(int index) {
      Object frame = this.frames.get(index);
      this.frames.remove(index);
      this.frames.add(index + 1, frame);
   }

   public void moveFrameLeft(int index) {
      this.moveFrameRight(index - 1);
   }

   public void setFile(File file) {
      this.file = file;
   }

   public File getFile() {
      return this.file;
   }

   public JaveAnimationFrame getFrame(int index) {
      return (JaveAnimationFrame)this.frames.get(index);
   }

   public int getFrameCount() {
      return this.frames.size();
   }
}
