package de.jave.jave;

import de.jave.gfx.GfxTools;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.selection.SelectionAlgorithms;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.text.TextTools;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class SelectionTool extends Tool {
   private final Timer timer = new Timer(600, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
         SelectionTool.this.updateSelectionBorder();
      }
   });
   protected Point location1;
   private Point point1;
   protected Point location2;
   protected int mode;
   private boolean movedByCursor = false;
   private boolean resizedByCursor = false;
   private int keySelectionAnchorH = 5;
   private int keySelectionAnchorV = 3;
   private boolean selectionMoved = false;
   private boolean selectionResized = false;
   private boolean cloneSelectionOnDrag = false;
   private boolean selectionClonedOnDrag = false;
   protected static final int NONE = 1;
   private static final int MOVE = 0;
   private static final int N_RESIZE = 2;
   private static final int S_RESIZE = 3;
   private static final int W_RESIZE = 4;
   private static final int E_RESIZE = 5;
   private static final int NE_RESIZE = 6;
   private static final int NW_RESIZE = 7;
   private static final int SE_RESIZE = 8;
   private static final int SW_RESIZE = 9;
   protected static final int SELECT = 10;
   protected static final int SELECT_PLUS = 11;
   protected static final int SELECT_MINUS = 12;
   private JCheckBox cb3d;
   private static final boolean DEFAULT_3D = true;
   private JComboBox chSelectionLayer;
   private JCheckBox cbCollision;
   final BooleanModel mergeCharactersModel = new BooleanModel(false);
   private int dx;
   private int dy;
   private int gdx;
   private int gdy;
   private IInlineToolOptions inlineOptions;
   private final SelectionClickSequence selectionClickSequence;
   private final boolean selectionClickSequenceEnabled;

   public SelectionTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      this(mainPanel, application, new SelectionClickSequence(), filter, false);
   }

   public SelectionTool(
      JaveMainPanel mainPanel, JavEApplication application, SelectionClickSequence selectionClickSequence, Filter filter
   ) {
      this(mainPanel, application, selectionClickSequence, filter, true);
   }

   private SelectionTool(
      JaveMainPanel mainPanel,
      JavEApplication application,
      SelectionClickSequence selectionClickSequence,
      Filter filter,
      boolean selectionClickSequenceEnabled
   ) {
      super(mainPanel, application, filter);
      this.selectionClickSequence = selectionClickSequence;
      this.selectionClickSequenceEnabled = selectionClickSequenceEnabled;
      this.mode = 1;
   }

   @Override
   public boolean containsLocation(Point location) {
      return this.hasSelection() && this.getPlate().getSelection().contains(location);
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.cb3d = new JCheckBox("3D View", true);
         this.cb3d.addItemListener(this);
         final MergeCharactersPanel mergeCharactersPanel = new MergeCharactersPanel(this.mergeCharactersModel);
         this.mergeCharactersModel.addChangeListener(new IChangeListener() {
            @Override
            public void stateChanged() {
               SelectionTool.this.setMixMode(SelectionTool.this.mergeCharactersModel.getValue());
            }
         });
         this.chSelectionLayer = new JComboBox<>(Selection.STR_LAYER);
         this.chSelectionLayer.addItemListener(this);
         this.chSelectionLayer.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               mergeCharactersPanel.setEnabled(SelectionTool.this.chSelectionLayer.getSelectedIndex() == 2);
            }
         });
         this.chSelectionLayer.setSelectedIndex(1);
         this.cbCollision = new JCheckBox("Collision", false);
         mergeCharactersPanel.setEnabled(this.chSelectionLayer.getSelectedIndex() == 2);
         final JPanel optionsPanel = new JPanel(new GridDialogLayout(1, false));
         optionsPanel.add(new JLabel("Paste mode:"));
         optionsPanel.add(this.chSelectionLayer, GridDialogLayoutData.FILL_HORIZONTAL);
         optionsPanel.add(this.cbCollision);
         optionsPanel.add(this.cb3d);
         optionsPanel.add(mergeCharactersPanel.getContent(), GridDialogLayoutData.FILL_HORIZONTAL);
         this.inlineOptions = () -> optionsPanel;
      }
      return this.inlineOptions;
   }

   @Override
   public String getName() {
      return "Selection";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_SELECTION_ICON;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.setMixMode(this.mergeCharactersModel.getValue());
      this.timer.start();
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
      this.timer.stop();
      if (!nextToolIsSelectionTool && this.hasSelection()) {
         SelectionAlgorithms.dropSelection(this.getEditor());
         this.getPlate().saveCurrentState("drop selection");
      }

      this.point1 = null;
      this.location1 = null;
      this.location2 = null;
   }

   @Override
   public void prepareForSave() {
      if (this.hasSelection()) {
         this.dropSelection();
      }
   }

   private void updateSelectionBorder() {
      if (this.hasSelection()) {
         this.repaintCursor();
      }
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      this.getPlate().requestFocus();
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.location1 != null && this.location2 != null) {
         Rectangle r = getRectangleFor(this.location1, this.location2);
         this.showStatus("(" + r.x + "," + r.y + ") -> (" + (r.x + r.width) + "," + r.y + r.height + ") = (" + r.width + "," + r.height + ")");
         g.setColor(colorScheme.getColorTool());
         Point p = this.getScreenPointFor(r.x, r.y);
         g.drawRect(p.x, p.y, r.width * this.getPlate().getCharWidth(), r.height * this.getPlate().getCharHeight());
      }
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      if (this.hasSelection()) {
         this.synchronizeToSelection();
         this.getPlate().requestFocus();
      }
   }

   private boolean hasSelection() {
      Plate plate = this.getPlate();
      return plate == null ? false : plate.hasSelection();
   }

   public void synchronizeToSelection() {
      Plate plate = this.getPlate();
      Selection sel = plate == null ? null : plate.getSelection();
      if (sel == null) {
         this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      } else {
         sel.setLayer(this.chSelectionLayer.getSelectedIndex());
         sel.set3dView(this.cb3d.isSelected());
         this.repaintAll();
      }
   }

   protected void selectionCanceled() {
      this.mode = 1;
      this.location1 = null;
      this.repaintCursor();
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.hasSelection()) {
         if (shiftDown) {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_PLUS));
         } else if (controlDown) {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_MINUS));
         }
      }

      if (code != 27 || this.hasSelection() && this.mode != 11 && this.mode != 12) {
         if (this.hasSelection()) {
            if (altDown && code == 38) {
               this.chSelectionLayer.setSelectedIndex((this.chSelectionLayer.getSelectedIndex() + 1) % 3);
               this.itemStateChanged(null);
            } else if (altDown && code == 40) {
               int sel = this.chSelectionLayer.getSelectedIndex() - 1;
               if (sel < 0) {
                  sel = 2;
               }

               this.chSelectionLayer.setSelectedIndex(sel);
               this.itemStateChanged(null);
            } else if (code == 127) {
               this.application.doSelectionDelete();
               this.application.switchToTextTool();
            } else if (controlDown || shiftDown || code != 38 && code != 40 && code != 37 && code != 39) {
               if (code == 10 || code == 27) {
                  Rectangle r = this.getPlate().getSelectionRegion();
                  this.dropSelection();
                  this.application.switchToTextTool(r.x, r.y);
               } else if (!shiftDown || code != 38 && code != 40 && code != 37 && code != 39) {
                  switch (code) {
                     case 37:
                        this.moveSelectionByCursor(-1, 0);
                        this.ensureSelectionLeftVisible();
                        evt.consume();
                        break;
                     case 38:
                        this.moveSelectionByCursor(0, -1);
                        this.ensureSelectionTopVisible();
                        evt.consume();
                        break;
                     case 39:
                        this.moveSelectionByCursor(1, 0);
                        this.ensureSelectionRightVisible();
                        evt.consume();
                        break;
                     case 40:
                        this.moveSelectionByCursor(0, 1);
                        this.ensureSelectionBottomVisible();
                        evt.consume();
                  }
               } else {
                  Rectangle region = this.getPlate().getSelectionRegion();
                  switch (code) {
                     case 37:
                        if (region.width == 1) {
                           this.getPlate().getSelection().resizeByCursorW_LEFT();
                           this.keySelectionAnchorH = 4;
                           this.ensureSelectionLeftVisible();
                        } else if (this.keySelectionAnchorH == 4) {
                           this.getPlate().getSelection().resizeByCursorW_LEFT();
                           this.ensureSelectionLeftVisible();
                        } else {
                           this.getPlate().getSelection().resizeByCursorE_LEFT();
                           this.ensureSelectionRightVisible();
                        }
                        break;
                     case 38:
                        if (region.height == 1) {
                           this.getPlate().getSelection().resizeByCursorN_UP();
                           this.ensureSelectionTopVisible();
                           this.keySelectionAnchorV = 2;
                        } else if (this.keySelectionAnchorV == 2) {
                           this.getPlate().getSelection().resizeByCursorN_UP();
                           this.ensureSelectionTopVisible();
                        } else {
                           this.getPlate().getSelection().resizeByCursorS_UP();
                           this.ensureSelectionBottomVisible();
                        }
                        break;
                     case 39:
                        if (region.width == 1) {
                           this.getPlate().getSelection().resizeByCursorE_RIGHT();
                           this.ensureSelectionRightVisible();
                           this.keySelectionAnchorH = 5;
                        } else if (this.keySelectionAnchorH == 4) {
                           this.getPlate().getSelection().resizeByCursorW_RIGHT();
                           this.ensureSelectionLeftVisible();
                        } else {
                           this.ensureSelectionRightVisible();
                           this.getPlate().getSelection().resizeByCursorE_RIGHT();
                        }
                        break;
                     case 40:
                        if (region.height == 1) {
                           this.getPlate().getSelection().resizeByCursorS_DOWN();
                           this.ensureSelectionBottomVisible();
                           this.keySelectionAnchorV = 3;
                        } else if (this.keySelectionAnchorV == 2) {
                           this.getPlate().getSelection().resizeByCursorN_DOWN();
                           this.ensureSelectionTopVisible();
                        } else {
                           this.getPlate().getSelection().resizeByCursorS_DOWN();
                           this.ensureSelectionBottomVisible();
                           this.keySelectionAnchorV = 3;
                        }
                  }

                  this.getPlate().repaint();
                  this.resizedByCursor = true;
               }
            } else {
               Rectangle r = this.getPlate().getSelectionRegion();
               this.dropSelection();
               int x = 0;
               int y = 0;
               if (code != 38 && code != 40) {
                  if (this.keySelectionAnchorV == 2) {
                     y = r.y;
                  } else {
                     y = r.y + r.height - 1;
                  }

                  if (code == 37) {
                     x = r.x;
                  } else {
                     x = r.x + r.width;
                  }
               } else {
                  if (this.keySelectionAnchorH == 4) {
                     x = r.x;
                  } else {
                     x = r.x + r.width - 1;
                  }

                  if (code == 38) {
                     y = r.y;
                  } else {
                     y = r.y + r.height;
                  }
               }

               boolean ok = true;
               if (x >= this.getPlate().getDocumentSize().width) {
                  ok = false;
                  x = this.getPlate().getDocumentSize().width - 1;
               }

               if (y >= this.getPlate().getDocumentSize().height) {
                  ok = false;
                  y = this.getPlate().getDocumentSize().height - 1;
               }

               if (!ok) {
                  this.beep();
               }

               this.application.switchToTextTool(x, y);
            }
         }
      } else {
         this.selectionCanceled();
      }
   }

   private void ensureSelectionTopVisible() {
      this.getPlate().ensureVisible(this.getPlate().getSelection().getLocation());
   }

   private void ensureSelectionBottomVisible() {
      this.getPlate()
         .ensureVisible(
            new Point(
               this.getPlate().getSelection().getLocation().x, this.getPlate().getSelection().getLocation().y + this.getPlate().getSelection().getHeight() - 1
            )
         );
   }

   private void ensureSelectionLeftVisible() {
      this.getPlate().ensureVisible(this.getPlate().getSelection().getLocation());
   }

   private void ensureSelectionRightVisible() {
      this.getPlate()
         .ensureVisible(
            new Point(
               this.getPlate().getSelection().getLocation().x + this.getPlate().getSelection().getWidth() - 1, this.getPlate().getSelection().getLocation().y
            )
         );
   }

   private void moveSelectionByCursor(int dx, int dy) {
      this.getPlate().moveSelection(dx, dy, this.cbCollision.isSelected());
      Rectangle region = this.getPlate().getSelectionRegion();
      Point location = new Point(region.x + region.width / 2, region.y + region.height / 2);
      this.getPlate().ensureVisible(location);
      this.movedByCursor = true;
   }

   @Override
   public void keyReleased(KeyEvent evt) {
      if (this.movedByCursor) {
         this.movedByCursor = false;
         this.saveCurrentState("move selection");
      }

      if (this.resizedByCursor) {
         this.resizedByCursor = false;
         this.saveCurrentState("resize selection");
      }

      if (this.mode == 11 || this.mode == 12) {
         this.mode = 1;
         this.location1 = null;
         this.location2 = null;
         this.repaintCursor();
      }

      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void keyTyped(char ch, KeyEvent evt) {
      if (!controlDown || ch == '@' || ch == '~' || ch == '|' || ch == '\\') {
         if (this.hasSelection()) {
            Rectangle r = this.getPlate().getSelectionRegion();
            this.application.doSelectionDelete();
            this.application.switchToTextTool(ch, r.x, r.y);
         }
      }
   }

   protected final void dropSelection() {
      SelectionAlgorithms.dropSelection(this.getEditor());
      this.mode = 1;
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.saveCurrentState("drop selection");
      this.repaintAll();
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (this.selectionClickSequenceEnabled && evt.getClickCount() == 1) {
         if (
            evt.getButton() == MouseEvent.BUTTON1
               && !evt.isShiftDown()
               && !evt.isControlDown()
               && !evt.isAltDown()
               && !evt.isMetaDown()
         ) {
            this.selectionClickSequence.start(location, evt.getWhen());
         } else {
            this.selectionClickSequence.cancel();
         }
      }

      this.location2 = null;
      this.location1 = location;
      this.point1 = point;
      this.cloneSelectionOnDrag = false;
      this.selectionClonedOnDrag = false;
      if (evt.isMetaDown()) {
         this.location1 = null;
         if (this.location2 != null) {
            this.location2 = null;
            this.repaintCursor();
         }
      }

      if (this.hasSelection() && shiftDown) {
         this.mode = 11;
         this.selectionMousePressedStarted(location);
      } else if (this.hasSelection() && controlDown) {
         this.mode = 12;
         this.selectionMousePressedStarted(location);
      } else if (!this.hasSelection() || !evt.isMetaDown() && this.getPlate().selectionContains(location)) {
         if (location != null) {
            if (this.hasSelection()) {
               Selection sel = this.getPlate().getSelection();
               int place = sel.getPlace(point);
               if (place == 1) {
                  this.mode = 0;
                  this.cloneSelectionOnDrag = evt.isAltDown();
               } else if (place == 0) {
                  this.mode = 10;
                  this.selectionMousePressedStarted(location);
               } else if (place == 2) {
                  this.mode = 2;
               } else if (place == 4) {
                  this.mode = 3;
               } else if (place == 3) {
                  this.mode = 4;
               } else if (place == 5) {
                  this.mode = 5;
               } else if (place == 7) {
                  this.mode = 7;
               } else if (place == 6) {
                  this.mode = 6;
               } else if (place == 9) {
                  this.mode = 9;
               } else if (place == 8) {
                  this.mode = 8;
               }
            } else {
               this.mode = 10;
               this.selectionMousePressedStarted(location);
            }
         }
      } else {
         this.dropSelection();
         this.mode = 10;
         this.selectionMousePressedStarted(location);
      }
   }

   protected void selectionMousePressedStarted(Point location) {
      this.location1 = location;
   }

   protected void selectionMouseDragged(Point location) {
      if (!location.equals(this.location2)) {
         this.location2 = location;
         this.repaintCursor();
      }
   }

   protected void selectionMouseReleasedFinished(Point location) {
      Rectangle region = getRectangleFor(this.location1, location);
      this.dx = 0;
      this.dy = 0;
      this.location1 = null;
      if (this.mode == 10) {
         JaveSelection content = this.getPlate().cutSelection(region);
         this.getPlate().setSelection(region, content);
         this.synchronizeToSelection();
         this.repaintAll();
         this.getPlate().saveCurrentState("select");
      } else if (this.mode == 11) {
         boolean success = this.getPlate().getSelection().add(region);
         if (!success) {
            this.repaintCursor();
         } else {
            this.repaintAll();
            this.getPlate().saveCurrentState("modify selection");
         }
      } else if (this.mode == 12) {
         boolean success = this.getPlate().getSelection().remove(region);
         if (!success) {
            this.repaintCursor();
         } else {
            this.repaintAll();
            if (this.hasSelection()) {
               this.getPlate().saveCurrentState("modify selection");
            } else {
               this.getPlate().saveCurrentState("drop selection");
               this.application.switchToTextTool();
            }
         }
      }

      this.mode = 1;
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.showStatus("(" + location.x + "," + location.y + ")");
      }

      if (this.mode == 11) {
         if (this.location1 != null && location != null && !evt.isMetaDown()) {
            this.selectionMouseReleasedFinished(location);
         } else {
            this.mode = 1;
            this.location1 = null;
         }
      } else if (this.mode == 12) {
         if (this.location1 != null && location != null && !evt.isMetaDown()) {
            this.selectionMouseReleasedFinished(location);
         } else {
            this.mode = 1;
            this.location1 = null;
         }
      } else if (this.hasSelection()) {
         boolean selectionChanged = this.selectionResized || this.selectionMoved;
         this.dx = 0;
         this.dy = 0;
         this.gdx = 0;
         this.gdy = 0;
         this.location1 = null;
         this.point1 = null;
         this.cloneSelectionOnDrag = false;
         if (this.selectionResized) {
            this.getPlate().saveCurrentState("resize selection");
            this.selectionResized = false;
         }

         if (this.selectionMoved) {
            this.getPlate().saveCurrentState(this.selectionClonedOnDrag ? "clone selection" : "move selection");
            this.selectionMoved = false;
            this.selectionClonedOnDrag = false;
         }

         if (
            !selectionChanged
               && evt.getButton() == MouseEvent.BUTTON1
               && evt.getClickCount() == 1
               && this.selectionClickSequence.isActive()
               && location != null
               && this.getPlate().selectionContains(location)
         ) {
            this.dropSelection();
            this.application.switchToTextTool(location.x, location.y);
         }
      } else if (evt.isMetaDown() && this.location2 != null) {
         this.location2 = null;
         this.location1 = null;
         this.repaintCursor();
      } else if (this.location1 != null && location != null && !evt.isMetaDown()) {
         if (GfxTools.distance(point, this.point1) >= 3.0) {
            this.selectionMouseReleasedFinished(location);
         } else {
            int x = location.x;
            int y = location.y;
            this.location1 = null;
            this.location2 = null;
            this.application.switchToTextTool(x, y);
         }
      }
   }

   @Override
   public void mouseClicked(Point point, Point location, MouseEvent evt) {
      if (evt.getClickCount() == 2 && this.hasSelection()) {
         Selection sel = this.getPlate().getSelection();
         if (sel.contains(location) && sel.isTextbox()) {
            char[][] ch = sel.getContent().getContent();
            char[][] content = new char[ch.length - 2][ch[0].length - 2];

            for (int x = 0; x < content.length; x++) {
                System.arraycopy(ch[x + 1], 1, content[x], 0, content[0].length);
            }

            this.application.editTextBox(TextTools.toString(content), sel.getLocation(), sel.getTextboxStyle());
         }
      } else if (evt.isMetaDown() || !this.getPlate().selectionContains(location)) {
         if (this.hasSelection()) {
            this.dropSelection();
         }

         if (location != null) {
            this.application.switchToTextTool(location.x, location.y);
         }
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      this.selectionClickSequence.cancel();
      if (this.mode != 1 && !evt.isMetaDown()) {
         if (this.mode == 12 || this.mode == 11) {
            this.selectionMouseDragged(location);
         } else if (!this.hasSelection()) {
            if (location == null) {
               if (this.location2 != null) {
                  this.location2 = null;
                  this.repaintAll();
               }
            } else {
               this.selectionMouseDragged(location);
            }
         } else if (location != null && this.point1 != null) {
            int ddx = (point.x - this.point1.x) / this.getPlate().getCharWidth();
            int ddy = (point.y - this.point1.y) / this.getPlate().getCharHeight();
            if (ddx != this.dx || ddy != this.dy) {
               if (this.mode != 0) {
                  this.resize(ddx - this.dx, ddy - this.dy);
                  this.selectionResized = true;
               } else {
                  if (this.cloneSelectionOnDrag && !this.selectionClonedOnDrag) {
                     this.getPlate().getSelection().paste();
                     this.selectionClonedOnDrag = true;
                  }

                  int xx = ddx - this.dx;

                  int yy;
                  for (yy = ddy - this.dy; xx < 0 && this.gdx > 0; this.gdx--) {
                     xx++;
                  }

                  while (xx > 0 && this.gdx < 0) {
                     xx--;
                     this.gdx++;
                  }

                  while (yy < 0 && this.gdy > 0) {
                     yy++;
                     this.gdy--;
                  }

                  while (yy > 0 && this.gdy < 0) {
                     yy--;
                     this.gdy++;
                  }

                  Point tat = this.getPlate().moveSelection(xx, yy, this.cbCollision.isSelected());
                  this.getPlate().ensureVisible(location);
                  this.gdx = this.gdx + (xx - tat.x);
                  this.gdy = this.gdy + (yy - tat.y);
                  this.selectionMoved = true;
               }

               this.dx = ddx;
               this.dy = ddy;
               StringBuffer s = new StringBuffer("[");
               if (this.dx > 0) {
                  s.append('+');
               }

               s.append(this.dx);
               s.append(',');
               if (this.dy > 0) {
                  s.append('+');
               }

               s.append(this.dy);
               s.append(']');
               this.showStatus(s.toString());
            }
         }
      }
   }

   private void resize(int dx, int dy) {
      Selection sel = this.getPlate().getSelection();
      switch (this.mode) {
         case 2:
            sel.resizeTextboxN(dy);
            break;
         case 3:
            sel.resizeTextboxS(dy);
            break;
         case 4:
            sel.resizeTextboxW(dx);
            break;
         case 5:
            sel.resizeTextboxE(dx);
            break;
         case 6:
            sel.resizeTextboxN(dy);
            sel.resizeTextboxE(dx);
            break;
         case 7:
            sel.resizeTextboxN(dy);
            sel.resizeTextboxW(dx);
            break;
         case 8:
            sel.resizeTextboxS(dy);
            sel.resizeTextboxE(dx);
            break;
         case 9:
            sel.resizeTextboxS(dy);
            sel.resizeTextboxW(dx);
      }
   }

   @Override
   public void mouseMoved(Point point, Point location, MouseEvent evt) {
      super.mouseMoved(point, location, evt);
      if (this.hasSelection() && location != null) {
         Selection sel = this.getPlate().getSelection();
         int place = sel.getPlace(point);
         if (place == 1) {
            if (shiftDown) {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_PLUS));
            } else if (controlDown) {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_MINUS));
            } else if (sel.contains(location)) {
               this.setCursor(Cursor.getPredefinedCursor(13));
            } else {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
            }
         } else if (place == 0) {
            if (shiftDown) {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_PLUS));
            } else if (controlDown) {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_MINUS));
            } else {
               this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
            }
         } else if (place == 2) {
            this.setCursor(Cursor.getPredefinedCursor(8));
         } else if (place == 4) {
            this.setCursor(Cursor.getPredefinedCursor(9));
         } else if (place == 3) {
            this.setCursor(Cursor.getPredefinedCursor(10));
         } else if (place == 5) {
            this.setCursor(Cursor.getPredefinedCursor(11));
         } else if (place == 7) {
            this.setCursor(Cursor.getPredefinedCursor(6));
         } else if (place == 6) {
            this.setCursor(Cursor.getPredefinedCursor(7));
         } else if (place == 9) {
            this.setCursor(Cursor.getPredefinedCursor(4));
         } else if (place == 8) {
            this.setCursor(Cursor.getPredefinedCursor(5));
         }
      } else if (this.hasSelection()) {
         if (shiftDown) {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_PLUS));
         } else if (controlDown) {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION_MINUS));
         } else {
            this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
         }
      } else {
         this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      }
   }
}
