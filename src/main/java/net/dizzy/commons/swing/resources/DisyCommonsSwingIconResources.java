package net.dizzy.commons.swing.resources;

import javax.swing.Icon;
import javax.swing.UIManager;

public final class DisyCommonsSwingIconResources implements IIconResources {
   public static final Icon CUT = icon("cut");
   public static final Icon COPY = icon("copy");
   public static final Icon PASTE = icon("paste");
   public static final Icon UNDO_MODERN = icon("undo");
   public static final Icon REDO_MODERN = icon("redo");

   private DisyCommonsSwingIconResources() {
   }

   private static Icon icon(String name) {
      Icon icon = UIManager.getIcon("FileView.fileIcon");
      return icon == null ? new javax.swing.ImageIcon() : icon;
   }
}
