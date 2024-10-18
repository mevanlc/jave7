package de.jave.jave.tool.fill;

import de.jave.ascii.plate.CharacterMetrics;
import de.jave.gfx.GfxTools;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.Tool;
import de.jave.jave.algorithm.fill.FillAlgorithm;
import de.jave.jave.algorithm.fill.FillMatchMode;
import de.jave.jave.algorithm.fill.FillMode;
import de.jave.jave.algorithm.fill.GradientStyle;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pattern.Pattern;
import de.jave.jave.pattern.PatternList;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.mousecursor.CursorId;
import net.disy.commons.swing.mousecursor.CursorProvider;

public class FillTool extends Tool {
   private Point location1;
   private Point point1;
   private Point location2;
   private Point point2;
   private final FillOptions options;
   private final JaveApplicationPreferences applicationPreferences;
   private final PatternList patternList;
   private final AsciiGradientConfiguration gradientConfiguration;

   public FillTool(JaveMainPanel plate, JavEApplication application, PatternList patternList, AsciiGradientConfiguration gradientConfiguration, Filter filter) {
      super(plate, application, filter);
      Ensure.ensureArgumentNotNull(application);
      Ensure.ensureArgumentNotNull(patternList);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.applicationPreferences = application.getApplicationPreferences();
      this.patternList = patternList;
      this.gradientConfiguration = gradientConfiguration;
      String fillPatternName = this.applicationPreferences.getFillPatternName();
      Pattern pattern;
      if (fillPatternName != null && patternList.getPattern(fillPatternName) != null) {
         pattern = patternList.getPattern(fillPatternName);
      } else {
         pattern = patternList.getPattern(0);
      }

      this.options = new FillOptions(pattern);
   }

   @Override
   protected JComponent createOptionsComponent() {
      return new FillOptionsPanel(this.options, this.getMouseCharacterModel(), this.applicationPreferences, this.patternList, this.gradientConfiguration)
         .getContent();
   }

   private FillMode getCurrentMode() {
      return this.options.getFillMode();
   }

   private char[] getGradient() {
      return this.options.getGradient(metaDown);
   }

   @Override
   public String getName() {
      return JaveMessages.Tool_Fill_Name;
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_FILL_ICON;
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
         FillMode mode = this.getCurrentMode();
         if (mode == FillMode.SOLID) {
            FillMatchMode matchMode = this.options.getMatchMode();
            this.setMixMode(false);
            FillAlgorithm.fillSolid(this.getPlate().getContent(), location.x, location.y, this.getMouseChar(), matchMode);
            this.getPlate().saveCurrentState(JaveMessages.Tool_Fill_UndoName_Solid);
            this.repaintAll();
         } else if (mode == FillMode.PATTERN) {
            FillMatchMode matchMode = this.options.getMatchMode();
            Pattern pattern = this.options.getPattern();
            this.setMixMode(false);
            FillAlgorithm.fillPattern(this.getPlate().getContent(), location.x, location.y, pattern, matchMode);
            this.getPlate().saveCurrentState(JaveMessages.Tool_Fill_UndoName_Pattern);
            this.repaintAll();
         } else if (mode == FillMode.GRADIENT && this.options.getGradientStyle() == GradientStyle.SUNBURST) {
            FillMatchMode matchMode = this.options.getMatchMode();
            char[] ch = this.getGradient();
            this.setMixMode(false);
            FillAlgorithm.fillGradient(
               this.getPlate().getContent(), location.x, location.y, location.x, location.y, ch, GradientStyle.SUNBURST, matchMode, this.options.isDither()
            );
            this.getPlate().saveCurrentState(JaveMessages.Tool_Fill_UndoName_Gradient);
            this.repaintAll();
         } else {
            if (mode == FillMode.GRADIENT) {
               this.location1 = location;
               this.point1 = point;
               this.point2 = point;
               this.repaintCursor();
            }
         }
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.getCurrentMode() == FillMode.GRADIENT) {
         this.location2 = location;
         this.point2 = point;
         this.repaintCursor();
      }
   }

   @Override
   public void mouseReleased(Point point, Point location, MouseEvent evt) {
      if (this.getCurrentMode() == FillMode.GRADIENT || this.options.getGradientStyle() != GradientStyle.SUNBURST) {
         this.location2 = location;
         if (location == null || this.location1 == null || this.location1.equals(this.location2)) {
            this.location1 = null;
            this.repaintCursor();
            return;
         }

         GradientStyle style = this.options.getGradientStyle();
         FillMatchMode matchMode = this.options.getMatchMode();
         char[] ch = this.getGradient();
         this.setMixMode(false);
         FillAlgorithm.fillGradient(
            this.getPlate().getContent(), this.location1.x, this.location1.y, this.location2.x, this.location2.y, ch, style, matchMode, this.options.isDither()
         );
         this.location1 = null;
         this.location2 = null;
         this.point1 = null;
         this.point2 = null;
         this.getPlate().saveCurrentState(JaveMessages.Tool_Fill_UndoName_Gradient);
         this.repaintAll();
      }
   }

   @Override
   public void keyPressed(int code, KeyEvent evt) {
      if (this.location1 != null && this.location2 != null) {
         if (code == 27) {
            this.point1 = null;
            this.point2 = null;
            this.location1 = null;
            this.location2 = null;
            this.repaintCursor();
         }
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.location1 != null && this.location2 != null && this.point1 != null && this.point2 != null) {
         g.setColor(colorScheme.getColorToolHelping());
         Point p1 = this.getScreenPointFor(this.location1.x, this.location1.y);
         CharacterMetrics characterMetrics = this.getPlate().getCharacterMetrics();
         int width = characterMetrics.getWidth();
         int height = characterMetrics.getHeight();
         g.drawLine(p1.x + width / 2, p1.y + height / 2, this.point2.x, this.point2.y);
         GfxTools.paintArrow(g, p1.x + width / 2, p1.y + height / 2, this.point2.x, this.point2.y);
      }
   }
}
