package de.jave.jave.actions;

import de.jave.jave.JaveMessages;
import de.jave.jave.brace.BraceLocationDialog;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Rectangle;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public class AddBracesToSelectionAction extends SmartAction {
   private final JaveMainPanel mainPanel;

   public AddBracesToSelectionAction(JaveMainPanel mainPanel) {
      super(JaveMessages.Action_AddBrace_Name);
      Ensure.ensureArgumentNotNull(mainPanel);
      this.mainPanel = mainPanel;
   }

   @Override
   protected void execute(Component parentComponent) {
      if (this.mainPanel.hasSelection()) {
         BraceLocationDialog g = new BraceLocationDialog(parentComponent);
         int loc = g.getBraceLocation();
         if (loc != -1) {
            Rectangle region = this.mainPanel.getSelectionRegion();
            this.mainPanel.dropSelection();
            switch (loc) {
               case 0:
                  this.bracesTop(region);
                  break;
               case 1:
                  this.bracesBottom(region);
                  break;
               case 2:
                  this.bracesLeft(region);
                  break;
               case 3:
                  this.bracesRight(region);
            }
         }
      }
   }

   private void bracesRight(Rectangle region) {
      int x = region.x + region.width;
      int y = region.y;
      int w = 2;
      int h = region.height;
      CharacterPlate cp = new CharacterPlate(2, h);
      if (h == 1) {
         cp.setForce(0, 0, '}');
      } else {
         cp.setForce(0, 0, '\\');
         cp.setForce(0, h - 1, '/');
         if (h >= 5) {
            for (int i = 1; i < h - 1; i++) {
               cp.setForce(0, i, '|');
            }
         }

         if (h == 2) {
            cp.setForce(1, 0, '_');
         } else if (h == 3) {
            cp.setForce(1, 1, '>');
         } else if (h == 4) {
            cp.setForce(1, 1, '_');
            cp.setForce(0, 1, '|');
            cp.setForce(0, 2, '|');
         } else {
            cp.setForce(1, (h - 1) / 2, '>');
            cp.setForce(0, (h - 1) / 2, ' ');
         }
      }

      Rectangle braceRegion = new Rectangle(x, y, 2, h);
      this.mainPanel.setSelection(braceRegion, cp);
      this.mainPanel.saveCurrentState(JaveMessages.Action_AddBrace_RightBraceUndoName);
   }

   private void bracesLeft(Rectangle region) {
      int x = region.x - 2;
      int y = region.y;
      int w = 2;
      int h = region.height;
      CharacterPlate cp = new CharacterPlate(2, h);
      if (h == 1) {
         cp.setForce(1, 0, '{');
      } else {
         cp.setForce(1, 0, '/');
         cp.setForce(1, h - 1, '\\');
         if (h >= 5) {
            for (int i = 1; i < h - 1; i++) {
               cp.setForce(1, i, '|');
            }
         }

         if (h == 2) {
            cp.setForce(0, 0, '_');
         } else if (h == 3) {
            cp.setForce(0, 1, '<');
         } else if (h == 4) {
            cp.setForce(0, 1, '_');
            cp.setForce(1, 1, '|');
            cp.setForce(1, 2, '|');
         } else {
            cp.setForce(0, (h - 1) / 2, '<');
            cp.setForce(1, (h - 1) / 2, ' ');
         }
      }

      Rectangle braceRegion = new Rectangle(x, y, 2, h);
      this.mainPanel.setSelection(braceRegion, cp);
      this.mainPanel.saveCurrentState(JaveMessages.Action_AddBrace_LeftBraceUndoName);
   }

   private void bracesTop(Rectangle region) {
      int x = region.x;
      int y = region.y - 2;
      int w = region.width;
      int h = 2;
      CharacterPlate cp = new CharacterPlate(w, 2);
      if (w == 1) {
         cp.setForce(0, 1, '^');
      } else {
         cp.setForce(0, 1, '/');
         cp.setForce(w - 1, 1, '\\');
         if (w > 5) {
            for (int i = 1; i < w - 1; i++) {
               cp.setForce(i, 0, '_');
            }

            cp.setForce(w / 2 - 1, 0, '/');
            cp.setForce(w / 2, 0, '\\');
         } else if (w == 3) {
            cp.setForce(1, 0, '|');
         } else if (w == 4) {
            cp.setForce(1, 0, '|');
            cp.setForce(2, 0, '_');
         } else if (w == 5) {
            cp.setForce(1, 0, '_');
            cp.setForce(2, 0, '|');
            cp.setForce(3, 0, '_');
         }
      }

      Rectangle braceRegion = new Rectangle(x, y, w, 2);
      this.mainPanel.setSelection(braceRegion, cp);
      this.mainPanel.saveCurrentState(JaveMessages.Action_AddBrace_TopBraceUndoName);
   }

   private void bracesBottom(Rectangle region) {
      int x = region.x;
      int y = region.y + region.height;
      int w = region.width;
      int h = 2;
      CharacterPlate cp = new CharacterPlate(w, 2);
      if (w == 1) {
         cp.setForce(0, 0, 'V');
      } else {
         cp.setForce(0, 0, '\\');
         cp.setForce(w - 1, 0, '/');
         if (w > 5) {
            for (int i = 1; i < w - 1; i++) {
               cp.setForce(i, 0, '_');
            }

            cp.setForce(w / 2 - 1, 1, '\\');
            cp.setForce(w / 2, 1, '/');
            cp.setForce(w / 2 - 1, 0, ' ');
            cp.setForce(w / 2, 0, ' ');
         } else if (w == 3) {
            cp.setForce(1, 0, '_');
            cp.setForce(1, 1, '|');
         } else if (w == 4) {
            cp.setForce(2, 0, '_');
            cp.setForce(1, 0, '_');
            cp.setForce(1, 1, '|');
         } else if (w == 5) {
            cp.setForce(1, 0, '_');
            cp.setForce(2, 0, '_');
            cp.setForce(3, 0, '_');
            cp.setForce(2, 1, '|');
         }
      }

      Rectangle braceRegion = new Rectangle(x, y, w, 2);
      this.mainPanel.setSelection(braceRegion, cp);
      this.mainPanel.saveCurrentState(JaveMessages.Action_AddBrace_BottomBraceUndoName);
   }
}
