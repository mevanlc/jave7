package net.disy.commons.swing.mousecursor;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import net.disy.commons.swing.image.IImageProvider;
import net.disy.commons.swing.image.ImageProvider;

public class ExtendedCursor {
   private static final IImageProvider imageProvider = new ImageProvider("net/disy/commons/swing/mousecursor");
   private final CursorId cursorId;
   private Cursor fallBackCursor;
   private Cursor cursor;
   private Image cursorImage;
   private boolean initialized = false;
   private final List<CursorDescription> cursorDescriptions;

   public ExtendedCursor(CursorId name) {
      this.cursorId = name;
      this.cursorDescriptions = new ArrayList<>();
   }

   public Cursor getCursor() {
      return this.cursor;
   }

   public void addCursorDescription(CursorDescription description) {
      this.cursorDescriptions.add(description);
   }

   public void setFallBackCursor(Cursor fallBackCursor) {
      this.fallBackCursor = fallBackCursor;
   }

   public boolean isInitialized() {
      return this.initialized;
   }

   public void initialize(CursorCapabilities capabilities) {
      this.initialized = true;

      for (int i = 0; i < this.cursorDescriptions.size(); i++) {
         CursorDescription description = this.cursorDescriptions.get(i);
         if (description.getCapabilities().equals(capabilities)) {
            this.cursor = this.loadCursor(description);
            return;
         }
      }

      this.cursor = this.getFallBackCursor();
   }

   private Cursor loadCursor(CursorDescription description) {
      this.cursorImage = imageProvider.getImage(description.getFileName());
      return this.cursorImage == null
         ? this.getFallBackCursor()
         : Toolkit.getDefaultToolkit().createCustomCursor(this.cursorImage, description.getHotSpot(), this.getCursorId().name());
   }

   private Cursor getFallBackCursor() {
      return this.fallBackCursor != null ? this.fallBackCursor : Cursor.getDefaultCursor();
   }

   public CursorId getCursorId() {
      return this.cursorId;
   }

   public Image getCursorImage() {
      return this.cursorImage;
   }
}
