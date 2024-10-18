package de.jave.jave;

import de.jave.image2ascii.ConversionException;
import de.jave.image2ascii.commandline.CommandLineImage2Ascii;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandLineJave {
   private final String toolName;
   private final List<String> options;

   public CommandLineJave(String[] args) {
      this.toolName = args[0].toLowerCase().trim();
      this.options = new ArrayList<>(args.length);

       this.options.addAll(Arrays.asList(args).subList(1, args.length));
   }

   public boolean doIt() {
      if (this.toolName.equals("i2a") || this.toolName.equals("image2ascii")) {
         CommandLineImage2Ascii image2Ascii = CommandLineImage2Ascii.initialize();

         try {
            image2Ascii.performJob(this.options);
         } catch (ConversionException var4) {
            var4.printStackTrace();
         }

         return true;
      } else if (this.toolName.equals("debug")) {
         String debugTool = this.options.get(0);
         if (debugTool.equals("cursor")) {
            if (this.options.get(1).equals("off")) {
               JaveGlobalRessources.cursorBlink = false;
            } else {
               try {
                  JaveGlobalRessources.cursorBlinkPause = Integer.parseInt(this.options.get(1));
               } catch (Exception var3) {
                  System.err.println("ERROR - wrong command line options format! " + var3);
               }
            }
         } else {
            System.err.println("ERROR - wrong command line options format! " + debugTool);
         }

         return false;
      } else {
         System.err.println("Unknown options for JavE commadline interface.");
         System.err.println("Supported syntax: ");
         System.err.println("  jave (image2ascii|i2a) INPUTFILE [width=WW] [algorithm=AA]");
         System.err.println("    INPUTFILE is the name (and path) to the image file)");
         System.err.println("    WW        is the width in characters of the result.");
         System.err.println("              The default value is 72.");
         System.err.println("    AA        is the name of the conversion algorithm.");
         System.err.println("              Supported algorithms are:");
         System.err.println("                 1_Pixel_per_Character");
         System.err.println("                 4_Pixels_per_Character (default)");
         return true;
      }
   }
}
