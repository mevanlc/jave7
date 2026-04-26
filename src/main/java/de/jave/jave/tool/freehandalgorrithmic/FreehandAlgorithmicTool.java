package de.jave.jave.tool.freehandalgorrithmic;

import de.jave.jave.JavEApplication;
import de.jave.jave.LineAlgorithm;
import de.jave.jave.Tool;
import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithm;
import de.jave.jave.algorithm.freehandalgorithmic.FreehandAlgorithmicMode;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.rendering.PixelPlateRenderer;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class FreehandAlgorithmicTool extends Tool {
   private Point lastMouseLocation;
   private final FreehandAlgorithmicOptions options = new FreehandAlgorithmicOptions();
   private final Filter filter;
   private IInlineToolOptions inlineOptions;

   public FreehandAlgorithmicTool(JaveMainPanel plate, JavEApplication application, Filter filter) {
      super(plate, application, filter);
      this.filter = filter;
   }

   @Override
   public String getName() {
      return "Freehand Algorithmic";
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_FREEHAND_ALGORITHMIC_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = new FreehandAlgorithmicOptionsPanel(this.options, this.getMouseCharacterModel(), this.getMixCharactersModel());
      }
      return this.inlineOptions;
   }

   @Override
   public void takeToHand() {
      this.setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (location != null) {
         this.markPlate = this.createMarkPlate(location);
         this.markPlate.setMode(PixelPlateMode.CHAR);
         this.markPlate.set(location.x, location.y);
         FreehandAlgorithmicMode mode = this.options.getMode();
         if (mode == FreehandAlgorithmicMode.CHARACTERS) {
            this.markPlate.setCharacter(this.getMouseCharacterModel().getCharacter(evt.isMetaDown()));
         } else {
            this.markPlate.setCharacter('#');
         }

         this.lastMouseLocation = location;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null) {
         this.setMixMode(this.isMix());
         LocatedCharacterPlate result = this.convert();
         result.pasteInto(this.getPlate().getContent());
         this.markPlate = null;
         this.saveCurrentState("freehand");
         this.repaintAll();
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (code == 27) {
         this.markPlate = null;
         this.lastMouseLocation = null;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.markPlate != null && location != null && this.lastMouseLocation != null) {
         if (!location.equals(this.lastMouseLocation)) {
            LineAlgorithm.drawLineBresenham(this.markPlate, location, this.lastMouseLocation, this.markPlate.getCharacter());
            this.lastMouseLocation = location;
            this.repaintCursor();
            this.showCoordinates(location);
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.markPlate != null) {
         LocatedCharacterPlate result = this.convert();
         PixelPlateRenderer.paint(g, this.getPlate(), colorScheme, result, plateOrigin);
      }
   }

   private LocatedCharacterPlate convert() {
      FreehandAlgorithmicMode mode = this.options.getMode();
      return new FreehandAlgorithm(this.filter).convertMarksToFreehandLine(this.markPlate, mode);
   }
}
