package de.jave.jave;

import de.jave.gui.CharField;
import de.jave.gui.CharacterModel;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import net.disy.commons.core.model.listener.IChangeListener;

public class BrushTool extends AbstractPencilTool {
   private JPanel brushPanel;
   private CharacterModel[] brushCharacterModels;
   private int brushHeight = 4;
   private int brushWidth = 4;
   private IInlineToolOptions inlineOptions;
   private static final char[][] DEFAULT_BRUSH = new char[][]{{' ', '_', '_', ' '}, {'d', '8', '8', 'b'}, {'Y', '8', '8', 'P'}, {' ', ' ', ' ', ' '}};

   public BrushTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
   }

   @Override
   public String getName() {
      return "Brush";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_BRUSH_ICON;
   }

   @Override
   protected String getUndoRedoActionName() {
      return "brush";
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         final JPanel optionsPanel = new JPanel(new BorderLayout());
         this.brushPanel = new JPanel();
         this.setBrush(DEFAULT_BRUSH);
         optionsPanel.add(this.brushPanel, "Center");
         optionsPanel.add(new MergeCharactersPanel(this.getMixCharactersModel()).getContent(), "South");
         this.inlineOptions = () -> optionsPanel;
      }
      return this.inlineOptions;
   }

   @Override
   protected void paint(int x0, int y0) {
      char[][] brush = this.getBrush();
      int h = brush.length;
      int w = brush[0].length;
      int cx = (w - 1) / 2;
      int cy = (h - 1) / 2;
      if (this.isMouseRightButton()) {
         for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
               char pixel = brush[y][x];
               if (pixel != ' ') {
                  this.getPlate().setCharForce(x0 - cx + x, y0 - cy + y, ' ');
               }
            }
         }
      } else {
         this.setMixMode(this.isMix());

         for (int x = 0; x < w; x++) {
            for (int yx = 0; yx < h; yx++) {
               char pixel = brush[yx][x];
               if (pixel == 160) {
                  this.getPlate().setCharForce(x0 - cx + x, y0 - cy + yx, ' ');
               } else if (pixel != ' ') {
                  this.getPlate().setChar(x0 - cx + x, y0 - cy + yx, pixel);
               }
            }
         }
      }

      this.getPlate().repaint(50L);
   }

   public void setBrush(CharacterPlate cp) {
      this.setBrush(cp.getContent());
   }

   public void setBrush(char[][] brush) {
      this.brushHeight = brush.length;
      this.brushWidth = brush[0].length;
      this.brushPanel.removeAll();
      this.brushPanel.setLayout(new GridLayout(0, this.brushWidth, 0, 0));
      this.brushCharacterModels = new CharacterModel[this.brushWidth * this.brushHeight];
      IChangeListener changeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            BrushTool.this.repaintCursor();
         }
      };
      CharField[] brushCharFields = new CharField[this.brushWidth * this.brushHeight];
      int index = 0;

      for (int y = 0; y < this.brushHeight; y++) {
         for (int x = 0; x < this.brushWidth; x++) {
            this.brushCharacterModels[index] = new CharacterModel(brush[y][x]);
            brushCharFields[index] = new CharField(this.brushCharacterModels[index]);
            this.brushCharacterModels[index].addChangeListener(changeListener);
            brushCharFields[index].setFont(JaveGlobalRessources.FONT_DEFAULT);
            this.brushPanel.add(brushCharFields[index++]);
         }
      }

      this.brushPanel.revalidate();
      this.brushPanel.repaint();
      this.repaintCursor();
   }

   @Override
   public char[][] getBrush() {
      char[][] result = new char[this.brushHeight][this.brushWidth];
      int index = 0;

      for (int y = 0; y < this.brushHeight; y++) {
         for (int x = 0; x < this.brushWidth; x++) {
            result[y][x] = this.brushCharacterModels[index++].getCharacter();
         }
      }

      return result;
   }
}
