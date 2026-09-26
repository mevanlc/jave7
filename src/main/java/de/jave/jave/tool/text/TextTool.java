package de.jave.jave.tool.text;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.MergeCharactersPanel;
import de.jave.jave.SelectionClickSequence;
import de.jave.jave.SelectionTool;
import de.jave.jave.Tool;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.BooleanPreferenceModel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.CharacterPlate;
import de.jave.lib.cell.GraphemeSplitter;
import de.jave.lib.Toolbox;
import de.jave.text.TextTools;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;
import net.dizzy.commons.swing.ui.ObjectUiListCellRenderer;

public class TextTool extends Tool {
   private final BooleanModel mergeCharactersModel = new BooleanModel(false);
   private final MergeCharactersPanel mergeCharactersPanel = new MergeCharactersPanel(this.mergeCharactersModel);
   private final CursorBlinker blinkThread;
   private final SelectionClickSequence selectionClickSequence;
   private Point point1;
   private Rectangle selectionRegion;
   private JComboBox chMovement;
   private Direction lastDirection = Direction.RIGHT;
   private IInlineToolOptions inlineOptions;

   public TextTool(JaveMainPanel mainPanel, JavEApplication application, BooleanPreferenceModel cursorBlockStyle, Filter filter) {
      this(mainPanel, application, cursorBlockStyle, new SelectionClickSequence(), filter);
   }

   public TextTool(
      JaveMainPanel mainPanel,
      JavEApplication application,
      BooleanPreferenceModel cursorBlockStyle,
      SelectionClickSequence selectionClickSequence,
      Filter filter
   ) {
      super(mainPanel, application, filter);
      Ensure.ensureArgumentNotNull(cursorBlockStyle);
      Ensure.ensureArgumentNotNull(selectionClickSequence);
      this.selectionClickSequence = selectionClickSequence;
      this.blinkThread = new CursorBlinker(mainPanel.getActiveEditorModel(), cursorBlockStyle);
      this.blinkThread.setInsert(isInsert());
   }

   @Override
   public void cursorUp(int lines) {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.y -= lines;
      if (cursorLocation.y < 0) {
         cursorLocation.y = 0;
      }

      this.blinkThread.updateCursor();
   }

   @Override
   public void cursorDown(int lines) {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.y += lines;
      int documentHeight = this.getPlate().getDocumentSize().height;
      if (cursorLocation.y >= documentHeight) {
         cursorLocation.y = documentHeight - 1;
      }

      this.blinkThread.updateCursor();
   }

   private static boolean isOppositeDirection(Direction dir1, Direction dir2) {
      return dir1.isOpposite(dir2);
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.chMovement = new JComboBox<>(CursorMovement.values());
         this.chMovement.setRenderer(new ObjectUiListCellRenderer(new CursorMovementUi()));
         this.chMovement.setSelectedIndex(0);
         this.chMovement.addItemListener(this);
         this.mergeCharactersPanel.setEnabled(!isInsert());
         this.mergeCharactersModel.addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               TextTool.this.mainPanel.requestFocus();
            }
         });
         final JPanel panel = new JPanel(new GridDialogLayout(1, false));
         panel.add(new JLabel(JaveMessages.Tool_Text_CursorMovement));
         panel.add(this.chMovement, GridDialogLayoutData.FILL_HORIZONTAL);
         panel.add(this.mergeCharactersPanel.getContent());
         this.inlineOptions = () -> panel;
      }
      return this.inlineOptions;
   }

   @Override
   public String getName() {
      return JaveMessages.Tool_Text_Name;
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_TEXT_ICON;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.TEXT));
      this.blinkThread.setActive(true);
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.blinkThread.setActive(false);
   }

   @Override
   public void checkSize() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.x >= this.getPlate().getDocumentWidth()) {
         cursorLocation.x = this.getPlate().getDocumentWidth() - 1;
      } else if (cursorLocation.x < 0) {
         cursorLocation.x = 0;
      }

      if (cursorLocation.y >= this.getPlate().getDocumentHeight()) {
         cursorLocation.y = this.getPlate().getDocumentHeight() - 1;
      } else if (cursorLocation.y < 0) {
         cursorLocation.y = 0;
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.selectionRegion != null) {
         this.showStatus(
            "("
               + this.selectionRegion.x
               + ","
               + this.selectionRegion.y
               + ") -> ("
               + (this.selectionRegion.x + this.selectionRegion.width)
               + ","
               + this.selectionRegion.y
               + this.selectionRegion.height
               + ") = ("
               + this.selectionRegion.width
               + ","
               + this.selectionRegion.height
               + ")"
         );
         g.setColor(colorScheme.getColorTool());
         Point p = this.getScreenPointFor(this.selectionRegion.x, this.selectionRegion.y);
         g.drawRect(p.x, p.y, this.selectionRegion.width * this.getPlate().getCharWidth(), this.selectionRegion.height * this.getPlate().getCharHeight());
      }
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (
         evt.getButton() == MouseEvent.BUTTON1
            && !evt.isShiftDown()
            && !evt.isControlDown()
            && !evt.isAltDown()
            && !evt.isMetaDown()
      ) {
         this.selectionClickSequence.prepareSecondClick(location, evt.getWhen());
      } else {
         this.selectionClickSequence.cancel();
      }

      if (location != null) {
         this.blinkThread.setHasSelection(false);
         this.selectionRegion = null;
         this.point1 = point;
         Point cursorLocation = this.getCursorLocation();
         cursorLocation.x = location.x;
         cursorLocation.y = location.y;
         this.getPlate().rememberCursorForUndo();
         this.blinkThread.updateCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      this.selectionClickSequence.cancel();
      Point cursorLocation = this.getCursorLocation();
      if (location != null && this.point1 != null && cursorLocation != null && !evt.isMetaDown()) {
         if (Toolbox.abs(this.point1.x - point.x) > 2 || Toolbox.abs(this.point1.y - point.y) > 2) {
            Rectangle r = getRectangleFor(cursorLocation, location);
            if (r.equals(this.selectionRegion)) {
               return;
            }

            this.blinkThread.setHasSelection(true);
            this.selectionRegion = r;
            this.blinkThread.updateCursor();
            this.repaintCursor();
         } else if (this.selectionRegion != null) {
            this.selectionRegion = null;
            this.blinkThread.setHasSelection(false);
            this.repaintCursor();
         }
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      boolean selectSingleCell = this.selectionClickSequence.completeSecondClick(location);
      if (this.selectionRegion != null) {
         this.selectRegion(this.selectionRegion);
      } else if (
         evt.getButton() == MouseEvent.BUTTON1
            && location != null
            && this.getPlate().isInside(location)
            && selectSingleCell
      ) {
         this.selectRegion(new Rectangle(location.x, location.y, 1, 1));
      } else {
         this.point1 = null;
         this.blinkThread.setHasSelection(false);
         this.selectionRegion = null;
      }
   }

   private void selectRegion(Rectangle region) {
      this.application.switchToSelectonTool();
      this.getPlate().setSelection(region);
      this.point1 = null;
      this.blinkThread.setHasSelection(false);
      this.selectionRegion = null;
      SelectionTool selectionTool = this.application.getSelectionTool();
      selectionTool.synchronizeToSelection();
      this.getPlate().saveCurrentState("select");
   }

   private void moveToTrack() {
      Point cursorLocation = this.getCursorLocation();
      int x = cursorLocation.x;
      int y = cursorLocation.y;
      if (x + 1 < this.getPlate().getDocumentWidth()) {
         int c = this.getPlate().getChar(x + 1, y);
         if (c != ' ' && !this.lastDirection.isOpposite(Direction.RIGHT)) {
            this.moveCursorRight();
            return;
         }
      }

      if (y + 1 < this.getPlate().getDocumentHeight()) {
         int c = this.getPlate().getChar(x, y + 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.DOWN)) {
            this.moveCursorDown();
            return;
         }
      }

      if (x > 0) {
         int c = this.getPlate().getChar(x - 1, y);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.LEFT)) {
            this.moveCursorLeft();
            return;
         }
      }

      if (y > 0) {
         int c = this.getPlate().getChar(x, y - 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.UP)) {
            this.moveCursorUp();
            return;
         }
      }

      if (x + 1 < this.getPlate().getDocumentWidth() && y > 0) {
         int c = this.getPlate().getChar(x + 1, y - 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.RIGHT_UP)) {
            this.moveCursorRightUp();
            return;
         }
      }

      if (x + 1 < this.getPlate().getDocumentWidth() && y + 1 < this.getPlate().getDocumentHeight()) {
         int c = this.getPlate().getChar(x + 1, y + 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.RIGHT_DOWN)) {
            this.moveCursorRightDown();
            return;
         }
      }

      if (x > 0 && y > 0) {
         int c = this.getPlate().getChar(x - 1, y - 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.LEFT_UP)) {
            this.moveCursorLeftUp();
            return;
         }
      }

      if (x > 0 && y + 1 < this.getPlate().getDocumentHeight()) {
         int c = this.getPlate().getChar(x - 1, y + 1);
         if (c != ' ' && !isOppositeDirection(this.lastDirection, Direction.LEFT_DOWN)) {
            this.moveCursorLeftDown();
            return;
         }
      }

      this.moveCursorRight();
   }

   public void charEntered(char ch) {
      this.textEntered(String.valueOf(ch));
   }

   public void textEntered(String text) {
      Point cursorLocation = this.getCursorLocation();
      Dimension previousSize = this.getPlate().getDocumentSize();
      CharacterPlate content = this.getPlate().getContent();
      boolean changed = false;
      this.setMixMode(insert ? false : this.mergeCharactersModel.getValue());

      for (String grapheme : GraphemeSplitter.split(text)) {
         int firstCodePoint = grapheme.codePointAt(0);
         if (Character.isISOControl(firstCodePoint)) {
            continue;
         }
         if (TextCellEditing.enterGrapheme(content, cursorLocation, grapheme, insert)) {
            this.moveAfterEnteredCell();
         }
         changed = true;
      }

      if (changed) {
         if (!previousSize.equals(this.getPlate().getDocumentSize())) {
            this.getPlate().handleDocumentSizeChanged();
         }
         this.getPlate().ensureVisible(cursorLocation);
         this.blinkThread.updateCursor();
         this.repaintAll();
         this.getPlate().saveCurrentState(JaveMessages.Tool_Text_UndoName);
      }
   }

   private void moveAfterEnteredCell() {
      CursorMovement movement = (CursorMovement)this.chMovement.getSelectedItem();
      movement.accept(new ICursorMovementVisitor() {
         @Override
         public void visitNormal(CursorMovement cursorMovement) {
            TextTool.this.moveCursorRight();
         }

         @Override
         public void visitDirected(CursorMovement cursorMovement) {
            if (TextTool.this.lastDirection == Direction.UP) {
               TextTool.this.moveCursorUp();
            } else if (TextTool.this.lastDirection == Direction.DOWN) {
               TextTool.this.moveCursorDown();
            } else if (TextTool.this.lastDirection == Direction.RIGHT) {
               TextTool.this.moveCursorRight();
            } else if (TextTool.this.lastDirection == Direction.LEFT) {
               TextTool.this.moveCursorLeft();
            } else if (TextTool.this.lastDirection == null) {
               TextTool.this.moveCursorRight();
            }
         }

         @Override
         public void visitTrackFollowing(CursorMovement cursorMovement) {
            TextTool.this.moveToTrack();
         }

         @Override
         public void visitNone(CursorMovement cursorMovement) {
         }
      });
   }

   @Override
   public void setInsert(boolean isInsert) {
      super.setInsert(isInsert);
      this.blinkThread.setInsert(isInsert);
      this.blinkThread.updateCursor();
      this.mergeCharactersPanel.setEnabled(!isInsert);
   }

   public void setCursorLocation(int x, int y) {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.x = x;
      cursorLocation.y = y;
      this.getPlate().rememberCursorForUndo();
      this.getPlate().ensureVisible(cursorLocation);
      this.blinkThread.updateCursor();
   }

   private void backSpace() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.x == 0 && cursorLocation.y == 0) {
         this.beep();
         this.blinkThread.updateCursor();
      } else {
         if (!insert) {
            this.moveCursorLeft();
            this.getPlate().setCharForce(cursorLocation, ' ');
         } else {
            CharacterPlate cp = this.getPlate().getContent();
            if (cursorLocation.x == 0) {
               int previousEnd = TextRowEditing.lastNonSpaceColumn(cp, cursorLocation.y - 1) + 1;
               int currentEnd = TextRowEditing.lastNonSpaceColumn(cp, cursorLocation.y) + 1;
               if (previousEnd + currentEnd > cp.getWidth()) {
                  this.getPlate().addColumnsRight(previousEnd + currentEnd - cp.getWidth());
               }
               for (int x = 0; x < currentEnd; x++) {
                  cp.setForce(previousEnd + x, cursorLocation.y - 1, cp.glyphAt(x, cursorLocation.y));
               }
               this.getPlate().removeLine(cursorLocation.y);
               cursorLocation.y--;
               cursorLocation.x = previousEnd;
            } else {
               this.moveCursorLeft();
               TextRowEditing.deleteRightward(cp, cursorLocation.x, cursorLocation.y);
            }
         }

         this.getPlate().ensureVisible(cursorLocation);
         this.repaintAll();
         this.saveCurrentState(JaveMessages.Tool_Text_UndoName);
         this.blinkThread.updateCursor();
      }
   }

   private void delete() {
      Point cursorLocation = this.getCursorLocation();
      if (!insert) {
         this.getPlate().setCharForce(cursorLocation, ' ');
      } else {
         CharacterPlate cp = this.getPlate().getContent();
         TextRowEditing.deleteRightward(cp, cursorLocation.x, cursorLocation.y);
      }

      this.getPlate().ensureVisible(cursorLocation);
      this.repaintAll();
      this.saveCurrentState(JaveMessages.Tool_Text_UndoName);
      this.blinkThread.updateCursor();
   }

   private void enter() {
      Point cursorLocation = this.getCursorLocation();
      if (insert && !shiftDown) {
         CharacterPlate cp = this.getPlate().getContent();
         int sourceRow = cursorLocation.y;
         int splitColumn = cursorLocation.x;
         this.getPlate().insertLine(sourceRow + 1);
         for (int x = splitColumn; x < cp.getWidth(); x++) {
            cp.setForce(x - splitColumn, sourceRow + 1, cp.glyphAt(x, sourceRow));
            cp.setForce(x, sourceRow, ' ');
         }
         this.moveCursorNewline();
         this.getPlate().ensureVisible(cursorLocation);
         this.getPlate().saveCurrentState(JaveMessages.Tool_Text_UndoName);
         this.blinkThread.updateCursor();
      } else {
         this.moveCursorNewline();
         this.getPlate().ensureVisible(cursorLocation);
         this.blinkThread.updateCursor();
      }
   }

   @Override
   public void keyTyped(char ch, KeyEvent evt) {
      this.textTyped(String.valueOf(ch), evt);
   }

   @Override
   public void textTyped(String text, KeyEvent evt) {
      if (!" ".equals(text) || evt == null || !evt.isControlDown()) {
         this.textEntered(text);
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      Point cursorLocation = this.getCursorLocation();
      Dimension previousSize = this.getPlate().getDocumentSize();
      if (TextRowEditing.handleShortcut(this.getPlate().getContent(), cursorLocation, code, evt.getModifiersEx())) {
         this.getPlate().ensureVisible(cursorLocation);
         if (!previousSize.equals(this.getPlate().getDocumentSize())) {
            this.getPlate().handleDocumentSizeChanged();
         } else {
            this.repaintAll();
         }
         this.saveCurrentState(JaveMessages.Tool_Text_UndoName);
         this.blinkThread.updateCursor();
         evt.consume();
         return;
      }

      if (code != 9 || !controlDown) {
         if (code == 27) {
            this.point1 = null;
            this.blinkThread.setHasSelection(false);
            this.selectionRegion = null;
            this.repaintCursor();
         } else {
            if (!shiftDown || code != 38 && code != 40 && code != 37 && code != 39) {
               switch (code) {
                  case 8:
                     this.backSpace();
                     return;
                  case 9:
                     for (int i = 0; i < 8; i++) {
                        this.moveCursorRight();
                     }

                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     return;
                  case 10:
                     this.enter();
                     return;
                  case 35:
                     if (evt.isControlDown()) {
                        this.moveCursorCtrlEnd();
                     } else {
                        this.moveCursorEnd();
                     }

                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     return;
                  case 36:
                     if (evt.isControlDown()) {
                        this.moveCursorCtrlPos1();
                     } else {
                        this.moveCursorPos1();
                     }

                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     return;
                  case 37:
                     this.moveCursorLeft();
                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     evt.consume();
                     return;
                  case 38:
                     this.moveCursorUp();
                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     evt.consume();
                     return;
                  case 39:
                     this.moveCursorRight();
                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     evt.consume();
                     return;
                  case 40:
                     this.moveCursorDown();
                     this.getPlate().ensureVisible(cursorLocation);
                     this.blinkThread.updateCursor();
                     evt.consume();
                     return;
                  case 127:
                     this.delete();
                     return;
               }
            } else {
               int dx = 0;
               int dy = 0;
               switch (code) {
                  case 37:
                     if (cursorLocation.x == 0) {
                        this.beep();
                        return;
                     }

                     dx = 1;
                     break;
                  case 38:
                     if (cursorLocation.y == 0) {
                        this.beep();
                        return;
                     }
                  case 39:
                  case 40:
               }

               this.application.switchToSelectonTool();
               this.getPlate().setSelection(new Rectangle(cursorLocation.x - dx, cursorLocation.y - 0, 1, 1));
               SelectionTool st = this.application.getSelectionTool();
               st.synchronizeToSelection();
               evt.consume();
            }
         }
      }
   }

   private void moveCursorRight() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.x + 1 == this.getPlate().getDocumentWidth()) {
         this.beep();
      } else {
         cursorLocation.x++;
      }

      this.lastDirection = Direction.RIGHT;
   }

   private void moveCursorLeft() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.x == 0) {
         this.beep();
      } else {
         cursorLocation.x--;
      }

      this.lastDirection = Direction.LEFT;
   }

   private void moveCursorNewline() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y + 1 == this.getPlate().getDocumentHeight()) {
         this.beep();
      } else {
         cursorLocation.x = 0;
         cursorLocation.y++;
      }
   }

   private void moveCursorRightDown() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y + 1 != this.getPlate().getDocumentHeight() && cursorLocation.x + 1 != this.getPlate().getDocumentWidth()) {
         cursorLocation.y++;
         cursorLocation.x++;
      } else {
         this.beep();
      }

      this.lastDirection = Direction.RIGHT_DOWN;
   }

   private void moveCursorRightUp() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y != 0 && cursorLocation.x + 1 != this.getPlate().getDocumentWidth()) {
         cursorLocation.y--;
         cursorLocation.x++;
      } else {
         this.beep();
      }

      this.lastDirection = Direction.RIGHT_UP;
   }

   private void moveCursorLeftDown() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y + 1 != this.getPlate().getDocumentHeight() && cursorLocation.x != 0) {
         cursorLocation.y++;
         cursorLocation.x--;
      } else {
         this.beep();
      }

      this.lastDirection = Direction.LEFT_DOWN;
   }

   private void moveCursorLeftUp() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y != 0 && cursorLocation.x != 0) {
         cursorLocation.y--;
         cursorLocation.x--;
      } else {
         this.beep();
      }

      this.lastDirection = Direction.LEFT_UP;
   }

   private void moveCursorDown() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y + 1 == this.getPlate().getDocumentHeight()) {
         this.beep();
      } else {
         cursorLocation.y++;
      }

      this.lastDirection = Direction.DOWN;
   }

   private void moveCursorUp() {
      Point cursorLocation = this.getCursorLocation();
      if (cursorLocation.y == 0) {
         this.beep();
      } else {
         cursorLocation.y--;
      }

      this.lastDirection = Direction.UP;
   }

   private void moveCursorEnd() {
      Point cursorLocation = this.getCursorLocation();
      if (insert) {
         CharacterPlate cp = this.getPlate().getContent();
         int newX = TextRowEditing.lastNonSpaceColumn(cp, cursorLocation.y) + 1;
         if (newX > this.getPlate().getDocumentWidth() - 1) {
            this.getPlate().addColumnsRight(1);
         }

         if (newX == cursorLocation.x) {
            cursorLocation.x = this.getPlate().getDocumentWidth() - 1;
         } else {
            cursorLocation.x = newX;
         }
      } else {
         cursorLocation.x = this.getPlate().getDocumentWidth() - 1;
      }
   }

   private void moveCursorPos1() {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.x = 0;
   }

   private void moveCursorCtrlEnd() {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.x = this.getPlate().getDocumentWidth() - 1;
      cursorLocation.y = this.getPlate().getDocumentHeight() - 1;
   }

   private void moveCursorCtrlPos1() {
      Point cursorLocation = this.getCursorLocation();
      cursorLocation.x = 0;
      cursorLocation.y = 0;
   }
}
