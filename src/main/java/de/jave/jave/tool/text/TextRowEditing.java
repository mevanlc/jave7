package de.jave.jave.tool.text;

import de.jave.lib.CharacterPlate;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

final class TextRowEditing {
   private TextRowEditing() {
   }

   static boolean handleShortcut(CharacterPlate content, Point cursor, int keyCode, int modifiers) {
      boolean controlDown = (modifiers & InputEvent.CTRL_DOWN_MASK) != 0;
      boolean altDown = (modifiers & InputEvent.ALT_DOWN_MASK) != 0;
      boolean shiftDown = (modifiers & InputEvent.SHIFT_DOWN_MASK) != 0;

      if (keyCode == KeyEvent.VK_SPACE && controlDown) {
         if (shiftDown) {
            deleteRightward(content, cursor.x, cursor.y);
         } else {
            insertRightward(content, cursor.x, cursor.y);
         }
         return true;
      }

      if (
         !altDown
            || keyCode != KeyEvent.VK_LEFT
               && keyCode != KeyEvent.VK_RIGHT
               && keyCode != KeyEvent.VK_UP
               && keyCode != KeyEvent.VK_DOWN
      ) {
         return false;
      }

      switch (keyCode) {
         case KeyEvent.VK_LEFT:
            if (shiftDown) {
               insertLeftward(content, cursor);
            } else {
               deleteRightward(content, cursor.x, cursor.y);
            }
            break;
         case KeyEvent.VK_RIGHT:
            if (shiftDown) {
               deleteLeftward(content, cursor.x, cursor.y);
            } else {
               insertRightward(content, cursor.x, cursor.y);
            }
            break;
         case KeyEvent.VK_UP:
            if (shiftDown) {
               deleteDownward(content, cursor.x, cursor.y);
            } else {
               insertUpward(content, cursor);
            }
            break;
         case KeyEvent.VK_DOWN:
            if (shiftDown) {
               insertDownward(content, cursor.x, cursor.y);
            } else {
               deleteUpward(content, cursor.x, cursor.y);
            }
            break;
         default:
            throw new IllegalStateException("Unexpected arrow key: " + keyCode);
      }
      return true;
   }

   private static void insertRightward(CharacterPlate content, int cursorX, int cursorY) {
      int width = content.getWidth();
      if (content.get(width - 1, cursorY) != ' ') {
         content.addColumnsRight(1);
      }

      for (int x = content.getWidth() - 1; x > cursorX; x--) {
         content.setForce(x, cursorY, content.get(x - 1, cursorY));
      }
      content.setForce(cursorX, cursorY, ' ');
   }

   private static void deleteRightward(CharacterPlate content, int cursorX, int cursorY) {
      for (int x = cursorX; x < content.getWidth() - 1; x++) {
         content.setForce(x, cursorY, content.get(x + 1, cursorY));
      }
      content.setForce(content.getWidth() - 1, cursorY, ' ');
   }

   private static void insertLeftward(CharacterPlate content, Point cursor) {
      if (content.get(0, cursor.y) != ' ') {
         content.addColumnsLeft(1);
         cursor.x++;
      }

      for (int x = 0; x < cursor.x; x++) {
         content.setForce(x, cursor.y, content.get(x + 1, cursor.y));
      }
      content.setForce(cursor.x, cursor.y, ' ');
   }

   private static void deleteLeftward(CharacterPlate content, int cursorX, int cursorY) {
      for (int x = cursorX; x > 0; x--) {
         content.setForce(x, cursorY, content.get(x - 1, cursorY));
      }
      content.setForce(0, cursorY, ' ');
   }

   private static void insertUpward(CharacterPlate content, Point cursor) {
      if (content.get(cursor.x, 0) != ' ') {
         content.addLinesTop(1);
         cursor.y++;
      }

      for (int y = 0; y < cursor.y; y++) {
         content.setForce(cursor.x, y, content.get(cursor.x, y + 1));
      }
      content.setForce(cursor.x, cursor.y, ' ');
   }

   private static void deleteUpward(CharacterPlate content, int cursorX, int cursorY) {
      for (int y = cursorY; y > 0; y--) {
         content.setForce(cursorX, y, content.get(cursorX, y - 1));
      }
      content.setForce(cursorX, 0, ' ');
   }

   private static void insertDownward(CharacterPlate content, int cursorX, int cursorY) {
      int height = content.getHeight();
      if (content.get(cursorX, height - 1) != ' ') {
         content.addLinesBottom(1);
      }

      for (int y = content.getHeight() - 1; y > cursorY; y--) {
         content.setForce(cursorX, y, content.get(cursorX, y - 1));
      }
      content.setForce(cursorX, cursorY, ' ');
   }

   private static void deleteDownward(CharacterPlate content, int cursorX, int cursorY) {
      for (int y = cursorY; y < content.getHeight() - 1; y++) {
         content.setForce(cursorX, y, content.get(cursorX, y + 1));
      }
      content.setForce(cursorX, content.getHeight() - 1, ' ');
   }
}
