package de.jave.jave.actions;

import de.jave.figlet.engine.IFigDriver;
import de.jave.gui.GCheckbox;
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
import de.jave.jave.tool.linealgorithmic.LineAlgorithmicTool;
import de.jave.jave.tool.rectanglealgorithmic.RectangleAlgorithmicTool;
import de.jave.jave.tool.text.TextTool;
import de.jave.jave.watermark.IWatermarkPainter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.button.RolloverButtonFactory;
import net.disy.commons.swing.button.SmartButtonGroup;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.widgets.HorizontalLine;

public class ToolBar {
   public static final int BRUSH_TOOL_INDEX = 14;
   public static final int DEFAULT_TOOL_INDEX = 0;
   public static final int SELECTION_TOOL_INDEX = 12;
   public static final int TEXT_TOOL_INDEX = 10;
   public static final int AUXILIARY_LINES_TOOL_INDEX = 20;
   public static final int WATERMARK_TOOL_INDEX = 19;
   public static final int TOOL_COUNT = 21;
   private final JavEApplication application;
   private final SmartButtonGroup buttonGroup = new SmartButtonGroup();
   private final JCheckBox cbWatermark;
   private final JCheckBox cbAuxLines;
   private Tool[] tools;
   private int[] buttonIndexToToolIndex;
   private int[] toolIndexToButtonIndex;
   private final JaveApplicationPreferences preferences;
   private final JComponent content;
   private final ConfigurationList configurationList;

   public ToolBar(
      final JavEApplication application, JaveApplicationPreferences preferences, ConfigurationList configurationList, PlatePreferences plateViewOptions
   ) {
      Ensure.ensureArgumentNotNull(configurationList);
      this.configurationList = configurationList;
      this.application = application;
      this.preferences = preferences;
      this.createTools();
      this.cbWatermark = new JCheckBox((String)null, false);
      this.cbWatermark.setToolTipText(JaveMessages.ToolCheckBox_Watermark_Tooltip);
      this.cbWatermark.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            ((IWatermarkPainter)ToolBar.this.tools[19]).setEnabled(ToolBar.this.cbWatermark.isSelected());
            application.getMainPanel().repaint();
            application.getMainPanel().requestFocus();
         }
      });
      this.cbAuxLines = new JCheckBox((String)null, false);
      this.cbAuxLines.setToolTipText(JaveMessages.ToolCheckBox_AuxLines_Tooltip);
      this.cbAuxLines.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            ((IWatermarkPainter)ToolBar.this.tools[20]).setEnabled(ToolBar.this.cbAuxLines.isSelected());
            application.getMainPanel().repaint();
            application.getMainPanel().requestFocus();
         }
      });
      GCheckbox cbGrid = new GCheckbox(plateViewOptions.getGridVisibilityModel(), JaveIcons.GRID_VISIBLE_ICON);
      cbGrid.setToolTipText(JaveMessages.ToolCheckBox_Grid_Tooltip);
      GCheckbox cbPure = new GCheckbox(plateViewOptions.getMarkIllegalModel(), JaveIcons.PURE_ASCII_ICON);
      cbPure.setToolTipText(JaveMessages.ToolCheckBox_MarkIllegal_Tooltip);
      GCheckbox cbPixel = new GCheckbox(plateViewOptions.getConnectedLinesViewModel(), JaveIcons.CONNECTED_LINES_VIEW_ICON);
      cbPixel.setToolTipText(JaveMessages.ToolCheckBox_ConnectedLinesView_Tooltip);
      GridDialogLayoutData twoColumnsData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      twoColumnsData.setHorizontalSpan(2);
      GridDialogLayoutData labelData = new GridDialogLayoutData(GridDialogLayoutData.CENTER);
      labelData.setVerticalAlignment(GridAlignment.END);
      JLabel genericToolsLabel = new JLabel(JaveIcons.TOOLTYPE_GENERIC_ICON);
      genericToolsLabel.setToolTipText(JaveMessages.ToolDescriptionLabel_Generic_ToolTipText);
      JLabel algorithmicToolsLabel = new JLabel(JaveIcons.TOOLTYPE_ALGORITHMIC_ICON);
      algorithmicToolsLabel.setToolTipText(JaveMessages.ToolDescriptionLabel_Algorithmic_ToolTipText);
      JPanel panel = new JPanel(new GridDialogLayout(2, true, 0, 0));
      panel.add(new Gap(1, 6), twoColumnsData);
      panel.add(genericToolsLabel, labelData);
      panel.add(algorithmicToolsLabel, labelData);
      panel.add(new Gap(1, 3), twoColumnsData);

      for (int i = 0; i < 10; i++) {
         if (i == 9) {
            panel.add(new Gap());
         }

         panel.add(this.buttonGroup.getButton(i));
      }

      panel.add(new Gap());
      panel.add(this.createHorizontalLine(6), twoColumnsData);

      for (int i = 10; i < 14; i++) {
         panel.add(this.buttonGroup.getButton(i));
      }

      panel.add(this.createHorizontalLine(6), twoColumnsData);

      for (int i = 14; i < 19; i++) {
         panel.add(this.buttonGroup.getButton(i));
      }

      panel.add(new Gap());
      panel.add(this.createHorizontalLine(6), twoColumnsData);
      panel.add(this.cbWatermark);
      panel.add(this.buttonGroup.getButton(19));
      panel.add(this.cbAuxLines);
      panel.add(this.buttonGroup.getButton(20));
      panel.add(new Gap(3, 3), twoColumnsData);
      panel.add(this.createHorizontalLine(1), twoColumnsData);
      panel.add(this.createHorizontalLine(1), twoColumnsData);
      panel.add(cbGrid.getContent(), twoColumnsData);
      panel.add(cbPure.getContent(), twoColumnsData);
      panel.add(cbPixel.getContent(), twoColumnsData);
      panel.add(this.createHorizontalLine(2), twoColumnsData);
      this.content = panel;
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
      return horizontalLine;
   }

   public void setWatermarkVisible(boolean what) {
      ((IWatermarkPainter)this.tools[19]).setEnabled(what);
      this.cbWatermark.setSelected(what);
   }

   public void setAuxiliaryLinesVisible(boolean what) {
      ((IWatermarkPainter)this.tools[20]).setEnabled(what);
      this.cbAuxLines.setSelected(what);
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
      mainPanel.setCurrentTool(this.tools[0]);

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
         button.setPreferredSize(new Dimension(24, 23));
         button.setSelected(toolIdx == this.application.getMainPanel().getToolManager().getCurrentToolIndex());
         this.buttonGroup.add(button);
      }
   }

   private static int[] createToolIndicesWithButtons() {
      int[] indices = new int[TOOL_COUNT];
      for (int i = 0; i < indices.length; i++) {
         indices[i] = i;
      }
      return indices;
   }

   public JComponent getContent() {
      return this.content;
   }
}
