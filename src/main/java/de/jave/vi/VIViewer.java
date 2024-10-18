package de.jave.vi;

import java.io.File;

public class VIViewer {
   public static void main(String[] args) {
      File file = new File(args[0]);
      int pause = 10;
      ViPlayMode mode = ViPlayMode.LINE;
      if (args.length > 1) {
         if (args[1].equalsIgnoreCase("line")) {
            mode = ViPlayMode.LINE;
         } else if (args[1].equalsIgnoreCase("char")) {
            mode = ViPlayMode.CHAR;
         } else {
            System.err.println("Syntax error in command line parameter 2 (mode): " + args[1] + " must be 'char' or 'line'");
         }

         if (args.length > 2) {
            try {
               pause = Integer.parseInt(args[2]);
            } catch (NumberFormatException var5) {
               System.err.println("Syntax error in command line parameter 3 (pause in ms): " + args[1]);
            }
         }
      }

      ViViewerApplication.playFile(null, file, mode, pause);
   }
}
