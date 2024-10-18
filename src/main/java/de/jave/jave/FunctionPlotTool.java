package de.jave.jave;

import de.jave.calculus.CalculusTool;
import de.jave.calculus.parser.ParseException;
import de.jave.formula.parser.Formula;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlateFeltPenMode;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterPlate;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class FunctionPlotTool extends AbstractDialogTool implements ItemListener {
   private static final String[] STR_STYLE = new String[]{"Lines", "Dot", "2by2 (JFL...)", "3by2", "Felt pen 8"};
   private static final PixelPlateMode[] MODE = new PixelPlateMode[]{
      PixelPlateMode.PIXEL, PixelPlateMode.DOT, PixelPlateMode.TWO_BY_TWO, PixelPlateMode.THREE_BY_TWO, PixelPlateFeltPenMode.FELTPEN_8
   };
   private final Filter filter;
   private JTextField tfFormula;
   private JComboBox chStyle;
   private JCheckBox cbLegend;
   private JTextField dfXMin;
   private JTextField dfXMax;
   private JTextField dfYMin;
   private JTextField dfYMax;
   private JTextField dfAxisX;
   private JTextField dfAxisY;

   public FunctionPlotTool(JavEApplication asciiPainter, JaveApplicationPreferences preferences, Filter filter) {
      super(asciiPainter, preferences);
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
   }

   @Override
   protected ColorScheme getPreferredColorScheme() {
      return ColorScheme.BLACK_ON_WHITE;
   }

   @Override
   public JComponent getOptionsComponent() {
      javax.swing.event.DocumentListener listener = new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FunctionPlotTool.this.convert();
         }
      };
      this.dfXMin = new JTextField("-4.7", 10);
      this.dfXMin.getDocument().addDocumentListener(listener);
      this.dfXMax = new JTextField("4.7", 10);
      this.dfXMax.getDocument().addDocumentListener(listener);
      this.dfYMin = new JTextField("-9.0", 10);
      this.dfYMin.getDocument().addDocumentListener(listener);
      this.dfYMax = new JTextField("9.0", 10);
      this.dfYMax.getDocument().addDocumentListener(listener);
      this.dfAxisX = new JTextField("0.0", 10);
      this.dfAxisX.getDocument().addDocumentListener(listener);
      this.dfAxisY = new JTextField("0.0", 10);
      this.dfAxisY.getDocument().addDocumentListener(listener);
      this.tfFormula = new JTextField("sin(x)+x/2");
      this.tfFormula.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.tfFormula.getDocument().addDocumentListener(listener);
      JPanel pOptions = new JPanel();
      this.chStyle = new JComboBox<>(STR_STYLE);
      this.chStyle.addItemListener(this);
      this.cbLegend = new JCheckBox("Show Legend", false);
      this.cbLegend.addItemListener(this);
      pOptions.add(new JLabel("Style:"));
      pOptions.add(this.chStyle);
      pOptions.add(this.cbLegend);
      JPanel panel = new JPanel(new GridDialogLayout(4, false));
      panel.add(new JLabel("f(x) ="), GridDialogLayoutData.RIGHT);
      panel.add(this.tfFormula, GridDialogLayoutDataFactory.createHorizontalSpanData(3, GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(new JLabel("X min:"), GridDialogLayoutData.RIGHT);
      panel.add(this.dfXMin);
      panel.add(new JLabel("Y min:"), GridDialogLayoutData.RIGHT);
      panel.add(this.dfYMin);
      panel.add(new JLabel("X max:"), GridDialogLayoutData.RIGHT);
      panel.add(this.dfXMax);
      panel.add(new JLabel("Y max:"), GridDialogLayoutData.RIGHT);
      panel.add(this.dfYMax);
      panel.add(new JLabel("Axis X="), GridDialogLayoutData.RIGHT);
      panel.add(this.dfAxisX);
      panel.add(new JLabel("Axis Y="), GridDialogLayoutData.RIGHT);
      panel.add(this.dfAxisY);
      JPanel p = new JPanel(new GridDialogLayout(1, false));
      p.add(panel);
      p.add(pOptions);
      return p;
   }

   @Override
   protected String getToolTitle() {
      return "Function Plotter";
   }

   @Override
   public String getToolActionName() {
      return "plot";
   }

   @Override
   protected void toolStarted() {
      this.convert();
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      this.convert();
   }

   private void convert() {
      int plateWidth = 70;
      int plateHeight = 30;
      double minX = -4.7;
      double maxX = 4.7;
      double minY = -9.0;
      double maxY = 9.0;

      try {
         minX = Double.parseDouble(this.dfXMin.getText());
         this.dfXMin.setForeground(Color.black);
      } catch (NumberFormatException var28) {
         this.dfXMin.setForeground(Color.red);
         return;
      }

      try {
         maxX = Double.parseDouble(this.dfXMax.getText());
         this.dfXMax.setForeground(Color.black);
      } catch (NumberFormatException var27) {
         this.dfXMax.setForeground(Color.red);
         return;
      }

      try {
         minY = Double.parseDouble(this.dfYMin.getText());
         this.dfYMin.setForeground(Color.black);
      } catch (NumberFormatException var26) {
         this.dfYMin.setForeground(Color.red);
         return;
      }

      try {
         maxY = Double.parseDouble(this.dfYMax.getText());
         this.dfYMax.setForeground(Color.black);
      } catch (NumberFormatException var25) {
         this.dfYMax.setForeground(Color.red);
         return;
      }

      if (maxX == minX) {
         this.dfXMin.setForeground(Color.magenta);
         this.dfXMax.setForeground(Color.magenta);
      } else if (maxY == minY) {
         this.dfYMin.setForeground(Color.magenta);
         this.dfYMax.setForeground(Color.magenta);
      } else {
         if (minX > maxX) {
            double t = minX;
            minX = maxX;
            maxX = t;
         }

         if (minY > maxY) {
            double t = minY;
            minY = maxY;
            maxY = t;
         }

         double axisX = 0.0;
         double axisY = 0.0;

         try {
            axisX = Double.parseDouble(this.dfAxisX.getText());
            this.dfAxisX.setForeground(Color.black);
         } catch (NumberFormatException var24) {
            this.dfAxisX.setForeground(Color.red);
            return;
         }

         try {
            axisY = Double.parseDouble(this.dfAxisY.getText());
            this.dfAxisY.setForeground(Color.black);
         } catch (NumberFormatException var23) {
            this.dfAxisY.setForeground(Color.red);
            return;
         }

         if (axisX < minX) {
            axisX = minX;
            this.dfAxisX.setForeground(Color.magenta);
         } else if (axisX > maxX) {
            axisX = maxX;
            this.dfAxisX.setForeground(Color.magenta);
         }

         if (axisY < minY) {
            axisY = minY;
            this.dfAxisY.setForeground(Color.magenta);
         } else if (axisY > maxY) {
            axisY = maxY;
            this.dfAxisY.setForeground(Color.magenta);
         }

         PixelPlateMode axisMode = PixelPlateMode.PIXEL;
         PixelPlateMode plotMode = MODE[this.chStyle.getSelectedIndex()];
         String formula = this.tfFormula.getText();
         CharacterPlate cp = CalculusTool.createAxis(axisMode, this.filter, axisX, axisY, 70, 30, minX, maxX, minY, maxY);
         cp.setMix(false);

         try {
            CalculusTool.plotFunction(formula, plotMode, this.filter, 70, 30, minX, maxX, minY, maxY, cp);
         } catch (ParseException var22) {
            cp.paste("No valid expression!", 1, 1);
         }

         this.characterPlate.clear();
         cp.pasteIntoForce(this.characterPlate, 0, 0);
         if (this.cbLegend.isSelected()) {
            String s = null;

            try {
               s = Formula.toAscii(formula);
            } catch (de.jave.formula.parser.ParseException var21) {
               s = "No valid expression!";
            }

            CharacterPlate lp = new CharacterPlate(s);
            lp.pasteInto(this.characterPlate, 1, 1);
         }

         this.repaintPlate();
      }
   }
}
