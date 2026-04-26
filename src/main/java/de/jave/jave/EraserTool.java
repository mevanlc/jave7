package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;

public class EraserTool extends AbstractPencilTool {
   private JComboBox chStyle;
   private static final int DEFAULT_STYLE = 0;
   private static final String[] STYLE_STR = new String[]{"Round", "Square", "Right Slash", "Left Slash"};
   private static int DEFAULT_SIZE = 3;
   private static final int MIN_SIZE = 1;
   private static final int MAX_SIZE = 6;
   private static final char[][][][] BRUSHES = new char[][][][]{
      {
            {{'X'}},
            {{'X', 'X'}, {'X', 'X'}},
            {{'\u0000', 'X', 'X', 'X', '\u0000'}, {'X', 'X', 'X', 'X', 'X'}, {'\u0000', 'X', 'X', 'X', '\u0000'}},
            {{'\u0000', 'X', 'X', 'X', '\u0000'}, {'X', 'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X', 'X'}, {'\u0000', 'X', 'X', 'X', '\u0000'}},
            {
                  {'\u0000', '\u0000', 'X', 'X', 'X', '\u0000', '\u0000'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'\u0000', '\u0000', 'X', 'X', 'X', '\u0000', '\u0000'}
            },
            {
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '\u0000'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'\u0000', 'X', 'X', 'X', 'X', 'X', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', 'X', '\u0000', '\u0000', '\u0000'}
            }
      },
      {
            {{'X'}},
            {{'X', 'X'}, {'X', 'X'}},
            {{'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X'}},
            {{'X', 'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X', 'X'}, {'X', 'X', 'X', 'X', 'X'}},
            {
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X'}
            },
            {
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'},
                  {'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X', 'X'}
            }
      },
      {
            {{'X'}},
            {{'\u0000', 'X'}, {'X', '\u0000'}},
            {{'\u0000', '\u0000', 'X', 'X'}, {'\u0000', 'X', 'X', '\u0000'}, {'X', 'X', '\u0000', '\u0000'}},
            {
                  {'\u0000', '\u0000', '\u0000', 'X', 'X'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'X', 'X', '\u0000', '\u0000', '\u0000'}
            },
            {
                  {'\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000'}
            },
            {
                  {'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X'},
                  {'\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000'},
                  {'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'}
            }
      },
      {
            {{'X'}},
            {{'X', '\u0000'}, {'\u0000', 'X'}},
            {{'X', 'X', '\u0000', '\u0000'}, {'\u0000', 'X', 'X', '\u0000'}, {'\u0000', '\u0000', 'X', 'X'}},
            {
                  {'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X'}
            },
            {
                  {'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X'}
            },
            {
                  {'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X', '\u0000'},
                  {'\u0000', '\u0000', '\u0000', '\u0000', '\u0000', 'X', 'X'}
            }
      }
   };
   private SpinnerNumberModel sizeModel;
   private IInlineToolOptions inlineOptions;

   public EraserTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getUndoRedoActionName() {
      return "erase";
   }

   @Override
   public String getName() {
      return "Eraser";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_ERASER_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = new EraserOptionsPanel(this.buildEraserOptionsContent());
      }
      return this.inlineOptions;
   }

   protected JComponent buildEraserOptionsContent() {
      this.chStyle = new JComboBox<>(STYLE_STR);
      this.chStyle.setSelectedIndex(0);
      this.sizeModel = new SpinnerNumberModel(DEFAULT_SIZE, 1, 6, 1);
      this.sizeModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            EraserTool.this.updateBrush();
         }
      });
      this.chStyle.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            EraserTool.this.updateBrush();
         }
      });
      JPanel optionsPanel = new JPanel(new GridDialogLayout(2, false));
      optionsPanel.add(new JLabel("Style:"), new GridDialogLayoutData().setHorizontalSpan(2));
      optionsPanel.add(this.chStyle, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL).setHorizontalSpan(2));
      optionsPanel.add(new JLabel("Size:"), GridDialogLayoutData.RIGHT);
      optionsPanel.add(new JSpinner(this.sizeModel));
      return optionsPanel;
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      this.updateBrush();
   }

   private void updateBrush() {
      this.repaintCursor();
   }

   @Override
   protected void paint(int x0, int y0) {
      char ch = ' ';
      if (this.isMouseRightButton()) {
         ch = this.getMouseCharacterModel().getCharacter1();
      }

      if (ch == 160) {
         ch = ' ';
      }

      char[][] brush = this.getBrush();
      int h = brush.length;
      int w = brush[0].length;
      int cx = (w - 1) / 2;
      int cy = (h - 1) / 2;

      for (int x = 0; x < w; x++) {
         for (int y = 0; y < h; y++) {
            if (brush[y][x] > 0) {
               this.getPlate().setCharForce(x0 - cx + x, y0 - cy + y, ch);
            }
         }
      }

      this.getPlate().repaint(50L);
   }

   @Override
   protected char[][] getBrush() {
      int style = this.chStyle.getSelectedIndex();
      int size = this.sizeModel.getNumber().intValue() - 1;
      return BRUSHES[style][size];
   }
}
