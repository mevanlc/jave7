package de.jave.gui.io;

import de.jave.jave.swing.JaveSwingMessages;
import java.io.File;

public class AcceptAllFileFilter extends SmartFileFilter {
   @Override
   public String getDescription() {
      return JaveSwingMessages.FileFormat_AllFiles;
   }

   @Override
   public boolean accept(File f) {
      return true;
   }

   @Override
   public File makeComplete(File file) {
      return file;
   }
}
