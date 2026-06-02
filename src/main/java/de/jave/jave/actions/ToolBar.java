package de.jave.jave.actions;

import de.jave.figlet.engine.IFigDriver;
import de.jave.gui.layout.Gap;
import de.jave.jave.ArcToolGeneric;
import de.jave.jave.BezierToolGeneric;
import de.jave.jave.BrushTool;
import de.jave.jave.CloneTool;
import de.jave.jave.EllipseToolGeneric;
import de.jave.jave.EraserTool;
import de.jave.jave.FreehandSelectionTool;
import de.jave.jave.FreehandToolGeneric;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.LineToolGeneric;
import de.jave.jave.PanTool;
import de.jave.jave.RectangleToolGeneric;
import de.jave.jave.SelectionTool;
import de.jave.jave.Tool;
import de.jave.jave.WatermarkTool;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.figlet.FIGletTool;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.pattern.PatternList;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.jave.tool.auxiliarylines.AuxiliaryLinesTool;
import de.jave.jave.tool.ellipsealgorithmic.EllipseAlgorithmicTool;
import de.jave.jave.tool.fill.FillTool;
import de.jave.jave.tool.freehandalgorrithmic.FreehandAlgorithmicTool;
import de.jave.jave.tool.dialog.ToolSelectorBarOptionsHost;
import de.jave.jave.tool.linealgorithmic.LineAlgorithmicTool;
import de.jave.jave.tool.rectanglealgorithmic.RectangleAlgorithmicTool;
import de.jave.jave.tool.text.TextTool;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.button.RolloverButtonFactory;
import net.dizzy.commons.swing.button.SmartButtonGroup;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;
import net.dizzy.commons.swing.widgets.HorizontalLine;

public class ToolBar {
   public static final int FREEHAND_TOOL_INDEX = 0;
   public static final int FREEHAND_ALGORITHMIC_TOOL_INDEX = 1;
   public static final int LINE_TOOL_INDEX = 2;
   public static final int LINE_ALGORITHMIC_TOOL_INDEX = 3;
   public static final int RECTANGLE_TOOL_INDEX = 4;
   public static final int RECTANGLE_ALGORITHMIC_TOOL_INDEX = 5;
   public static final int ELLIPSE_TOOL_INDEX = 6;
   public static final int ELLIPSE_ALGORITHMIC_TOOL_INDEX = 7;
   public static final int BEZIER_TOOL_INDEX = 8;
   public static final int ARC_TOOL_INDEX = 9;
   public static final int TEXT_TOOL_INDEX = 10;
   public static final int FIGLET_TOOL_INDEX = 11;
   public static final int SELECTION_TOOL_INDEX = 12;
   public static final int FREEHAND_SELECTION_TOOL_INDEX = 13;
   public static final int BRUSH_TOOL_INDEX = 14;
   public static final int ERASER_TOOL_INDEX = 15;
   public static final int FILL_TOOL_INDEX = 16;
   public static final int CLONE_TOOL_INDEX = 17;
   public static final int PAN_TOOL_INDEX = 18;
   public static final int WATERMARK_TOOL_INDEX = 19;
   public static final int AUXILIARY_LINES_TOOL_INDEX = 20;
   public static final int DEFAULT_TOOL_INDEX = FREEHAND_TOOL_INDEX;
   public static final int TOOL_COUNT = 21;
   private final JavEApplication application;
   private final SmartButtonGroup buttonGroup = new SmartButtonGroup();
   private Tool[] tools;
   private int[] buttonIndexToToolIndex;
   private int[] toolIndexToButtonIndex;
   private final JaveApplicationPreferences preferences;
   private final JComponent content;
   private final ConfigurationList configurationList;

   public ToolBar(
      final JavEApplication application,
      JaveApplicationPreferences preferences,
      ConfigurationList configurationList,
      PlatePreferences plateViewOptions,
      ToolSelectorBarOptionsHost optionsHost
   ) {
      Ensure.ensureArgumentNotNull(configurationList);
      Ensure.ensureArgumentNotNull(optionsHost);
      this.configurationList = configurationList;
      this.application = application;
      this.preferences = preferences;
      this.createTools();

      GridDialogLayoutData fourColumnsData = new GridDialogLayoutData();
      fourColumnsData.setHorizontalSpan(4);
      JPanel panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
      JPanel toolsPanel = new JPanel(new GridDialogLayout(4, false, 0, 0));
      toolsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
      // Top padding aligns the first tool button with the bottom of the
      // document tab bar in the main panel.
      toolsPanel.add(new Gap(1, 30), fourColumnsData);

      // Generic + Algorithmic groups joined: 10 tools paired (gen,alg)
      // for each shape that has both — Bezier and Arc trail (gen only).
      // row(freehand, fh-alg, line, line-alg)
      // row(rect, rect-alg, ellipse, e-alg)
      // row(bezier, arc)
      toolsPanel.add(this.btn(0));
      toolsPanel.add(this.btn(1));
      toolsPanel.add(this.btn(2));
      toolsPanel.add(this.btn(3));
      toolsPanel.add(this.btn(4));
      toolsPanel.add(this.btn(5));
      toolsPanel.add(this.btn(6));
      toolsPanel.add(this.btn(7));
      toolsPanel.add(this.btn(8));
      toolsPanel.add(this.btn(9));
      toolsPanel.add(new Gap());
      toolsPanel.add(new Gap());
      toolsPanel.add(this.createHorizontalLine(6), fourColumnsData);

      // Selection + Brush + view-overlay groups: 11 tools in 3 rows.
      // row 1: text, figlet, sel, fh-sel
      // row 2: brush, eraser, fill, clone
      // row 3: pan, auxlines, watermark
      toolsPanel.add(this.btn(10));
      toolsPanel.add(this.btn(11));
      toolsPanel.add(this.btn(12));
      toolsPanel.add(this.btn(13));
      toolsPanel.add(this.btn(14));
      toolsPanel.add(this.btn(15));
      toolsPanel.add(this.btn(16));
      toolsPanel.add(this.btn(17));
      toolsPanel.add(this.btn(18));
      toolsPanel.add(this.btn(20));
      toolsPanel.add(this.btn(19));
      toolsPanel.add(new Gap());

      toolsPanel.setMaximumSize(toolsPanel.getPreferredSize());
      panel.add(toolsPanel);

      panel.add(new Gap(1, 4));
      panel.add(this.createHorizontalLine(2));
      JComponent optionsContent = optionsHost.getContent();
      optionsContent.setAlignmentX(Component.LEFT_ALIGNMENT);
      panel.add(optionsContent);
      this.content = panel;
   }

   private JComponent btn(int toolIndex) {
      return this.buttonGroup.getButton(this.toolIndexToButtonIndex[toolIndex]);
   }

   private HorizontalLine createHorizontalLine(int verticalSpace) {
      HorizontalLine horizontalLine = new HorizontalLine(20);
      horizontalLine.setMargin(
         new Insets(
            LayoutUtilities.getDpiAdjusted(verticalSpace),
            LayoutUtilities.getDpiAdjusted(3),
            LayoutUtilities.getDpiAdjusted(verticalSpace),
            LayoutUtilities.getDpiAdjusted(3)
         )
      );
      horizontalLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, horizontalLine.getPreferredSize().height));
      horizontalLine.setAlignmentX(Component.LEFT_ALIGNMENT);
      return horizontalLine;
   }

   public void setWatermarkVisible(boolean what) {
      this.application.getWatermarkVisibilityModel().setValue(what);
   }

   public void setAuxiliaryLinesVisible(boolean what) {
      this.application.getAuxLinesVisibilityModel().setValue(what);
   }

   public void selectToolButton(int toolIndex) {
      if (toolIndex < 0 || toolIndex >= this.toolIndexToButtonIndex.length) {
         return;
      }
      int buttonIndex = this.toolIndexToButtonIndex[toolIndex];
      if (buttonIndex >= 0) {
         this.buttonGroup.setSelectedIndex(buttonIndex);
      }
   }

   protected void createTools() {
      JaveMainPanel mainPanel = this.application.getMainPanel();
      this.tools = new Tool[21];
      Filter filter = this.configurationList.getRequired(Filter.class);
      this.tools[0] = new FreehandToolGeneric(mainPanel, this.application, filter);
      this.tools[2] = new LineToolGeneric(mainPanel, this.application, filter);
      this.tools[4] = new RectangleToolGeneric(mainPanel, this.application, filter);
      this.tools[6] = new EllipseToolGeneric(mainPanel, this.application, filter);
      this.tools[8] = new BezierToolGeneric(mainPanel, this.application, filter);
      this.tools[9] = new ArcToolGeneric(mainPanel, this.application, filter);
      this.tools[1] = new FreehandAlgorithmicTool(mainPanel, this.application, filter);
      this.tools[3] = new LineAlgorithmicTool(mainPanel, this.application, filter);
      this.tools[5] = new RectangleAlgorithmicTool(mainPanel, this.application, filter);
      this.tools[7] = new EllipseAlgorithmicTool(mainPanel, this.application, filter);
      this.tools[10] = new TextTool(mainPanel, this.application, this.preferences.getCursorBlockStyleModel(), filter);
      IFigDriver figDriver = this.configurationList.getRequired(IFigDriver.class);
      this.tools[11] = new FIGletTool(mainPanel, this.application, figDriver, filter);
      this.tools[12] = new SelectionTool(mainPanel, this.application, filter);
      this.tools[13] = new FreehandSelectionTool(mainPanel, this.application, filter);
      this.tools[14] = new BrushTool(mainPanel, this.application, filter);
      this.tools[15] = new EraserTool(mainPanel, this.application, filter);
      PatternList patternList = this.configurationList.getRequired(PatternList.class);
      AsciiGradientConfiguration gradientConfiguration = this.configurationList.getRequired(AsciiGradientConfiguration.class);
      this.tools[16] = new FillTool(mainPanel, this.application, patternList, gradientConfiguration, filter);
      this.tools[17] = new CloneTool(mainPanel, this.application, filter);
      this.tools[18] = new PanTool(mainPanel, this.application, filter);
      this.tools[19] = new WatermarkTool(mainPanel, this.application, filter);
      this.tools[20] = new AuxiliaryLinesTool(mainPanel, this.application, filter);
      mainPanel.getToolManager().setTools(this.tools);
      mainPanel.setCurrentTool(this.tools[DEFAULT_TOOL_INDEX]);

      this.buttonIndexToToolIndex = createToolIndicesWithButtons();
      this.toolIndexToButtonIndex = new int[this.tools.length];
      java.util.Arrays.fill(this.toolIndexToButtonIndex, -1);
      for (int b = 0; b < this.buttonIndexToToolIndex.length; b++) {
         this.toolIndexToButtonIndex[this.buttonIndexToToolIndex[b]] = b;
      }

      for (int b = 0; b < this.buttonIndexToToolIndex.length; b++) {
         final int toolIdx = this.buttonIndexToToolIndex[b];
         SmartAction action = new SmartAction(this.tools[toolIdx].getIcon()) {
            @Override
            protected void execute(Component parentComponent) {
               int sel = ToolBar.this.buttonGroup.getSelectedIndex();
               ToolBar.this.application.setTool(ToolBar.this.buttonIndexToToolIndex[sel]);
               ToolBar.this.application.getMainPanel().requestFocus();
            }
         };
         action.setToolTipText(this.tools[toolIdx].getName());
         JToggleButton button = RolloverButtonFactory.createToggleButton(action);
         button.setSelected(toolIdx == this.application.getMainPanel().getToolManager().getCurrentToolIndex());
         this.buttonGroup.add(button);
      }
   }

   private static int[] createToolIndicesWithButtons() {
      // All tools get a button. Tool indices in tools[] match button-tool
      // mapping 1:1 today; the indirection layer remains in place so we
      // can re-introduce skips (e.g. for tools moved menu-only) cheaply.
      int[] indices = new int[TOOL_COUNT];
      for (int t = 0; t < TOOL_COUNT; t++) {
         indices[t] = t;
      }
      return indices;
   }

   public JComponent getContent() {
      return this.content;
   }
}
