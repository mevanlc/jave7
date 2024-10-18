package de.jave.jave;

import de.jave.lib.CharacterPlate;
import de.jave.lib.area.BooleanArea;
import java.awt.Dimension;

public class JaveSelection {
   private final CharacterPlate content;
   private BooleanArea mask;

   public JaveSelection(CharacterPlate content) {
      this(content, null);
   }

   public JaveSelection(CharacterPlate content, BooleanArea mask) {
      this.content = content;
      this.mask = mask;
   }

   public JaveSelection getClone() {
      return new JaveSelection(this.content.getClone(), this.mask == null ? null : this.mask.getClone());
   }

   public CharacterPlate getContent() {
      return this.content;
   }

   public BooleanArea getMask() {
      return this.mask;
   }

   public void setMask(BooleanArea mask) {
      if (this.mask == null) {
         this.mask = mask;
      } else if (this.mask != mask) {
         this.mask.setSize(mask.getWidth(), mask.getHeight());
         mask.pasteInto(this.mask, 0, 0);
      }
   }

   @Deprecated
   public int getWidth() {
      return this.content.getWidth();
   }

   @Deprecated
   public int getHeight() {
      return this.content.getHeight();
   }

   public boolean isActive(int x, int y) {
      if (x < 0 || y < 0 || x >= this.content.getWidth() || y >= this.content.getHeight()) {
         return false;
      } else {
         return this.mask == null ? true : this.mask.isSet(x, y);
      }
   }

   public void setContent(CharacterPlate content) {
      if (this.content != content) {
         this.content.setSize(content.getWidth(), content.getHeight());
         content.pasteIntoForce(this.content, 0, 0);
      }
   }

   public Dimension getSize() {
      return this.content.getSize();
   }
}
