package de.jave.jave;

import de.jave.ascii.IAsciiGuiConstants;
import de.jave.lib.CodeBaseTool;
import java.awt.Color;
import java.awt.Font;
import java.io.File;

public class JaveGlobalRessources {
   public static boolean cursorBlink = true;
   public static final File codeBase;
   public static final Font FONT_BOLD;
   public static final Font FONT_ITALIC;
   public static final Font FONT_SMALL;
   public static final Font FONT_DEFAULT;
   public static final Font FONT_SMALL_FIXEDWIDTH;
   public static int cursorBlinkPause;
   public static final int UNDO_BUFFER_SIZE = 524288;
   public static final Color COLOR_CHARFIELD_BACK;
   public static final File TMP_FOLDER;

   static {
      File base = CodeBaseTool.getCodeBase(JaveGlobalRessources.class);
      File configurationFolder = new File(base, "config");
      if (!configurationFolder.exists()) {
         base = new File(".");
      }

      codeBase = base;
      FONT_BOLD = new Font("Monospaced", 1, 13);
      FONT_ITALIC = new Font("Dialog", 2, 12);
      FONT_SMALL = new Font("Dialog", 0, 10);
      FONT_DEFAULT = IAsciiGuiConstants.DEFAULT_FIXED_WIDTH_FONT;
      FONT_SMALL_FIXEDWIDTH = IAsciiGuiConstants.DEFAULT_ASCII_FONT;
      cursorBlinkPause = 450;
      COLOR_CHARFIELD_BACK = new Color(131, 187, 186);
      TMP_FOLDER = new File(codeBase, "tmp");
   }
}
