package de.jave.jave;

import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.tool.fractal.FractalCutoutModel;
import de.jave.jave.tool.fractal.FractalNavigationButtonPanelFactory;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.util.LayoutUtilities;
import net.dizzy.commons.swing.textfield.DoubleField;

public class FractalTool extends AbstractDialogTool implements ItemListener {
   private static final String[] RESOLUTION_STR = new String[]{"Lowest", "Low (Dots)", "Normal 2by2 (J7L_\"'`()8)", "Higher 3by2", "High"};
   private static final int DEFAULT_RESOLUTION = 4;
   private final FractalCutoutModel cutoutModel = new FractalCutoutModel();
   private JCheckBox cbNegative;
   private JComboBox chResolution;
   private JComboBox chDespecle;
   private SpinnerNumberModel depthModel;
   private final Filter filter;

   public FractalTool(JavEApplication asciiPainter, JaveApplicationPreferences preferences, Filter filter) {
      super(asciiPainter, preferences);
      Ensure.ensureArgumentNotNull(filter);
      this.filter = filter;
      this.getDialog();
      this.convert();
   }

   @Override
   protected ColorScheme getPreferredColorScheme() {
      return ColorScheme.BLACK_ON_WHITE;
   }

   @Override
   protected String getToolTitle() {
      return "Fractal Tool";
   }

   @Override
   public String getToolActionName() {
      return "fractal";
   }

   @Override
   public JComponent getOptionsComponent() {
      JPanel pMandel = new JPanel(new GridDialogLayout(2, false));
      pMandel.add(new JLabel("X:"), GridDialogLayoutData.RIGHT);
      final DoubleField dfX = new DoubleField(10);
      dfX.setValue(this.cutoutModel.getX());
      dfX.addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FractalTool.this.cutoutModel.setX(dfX.getValue());
         }
      });
      pMandel.add(dfX.getContent());
      pMandel.add(new JLabel("Y:"), GridDialogLayoutData.RIGHT);
      final DoubleField dfY = new DoubleField(10);
      dfY.setValue(this.cutoutModel.getY());
      dfY.addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FractalTool.this.cutoutModel.setY(dfY.getValue());
         }
      });
      pMandel.add(dfY.getContent());
      pMandel.add(new JLabel("X span:"), GridDialogLayoutData.RIGHT);
      final DoubleField dfXSpan = new DoubleField(10);
      dfXSpan.setValue(this.cutoutModel.getXSpan());
      dfXSpan.addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            FractalTool.this.cutoutModel.setXSpan(dfXSpan.getValue());
         }
      });
      pMandel.add(dfXSpan.getContent());
      this.cutoutModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            dfX.setValue(FractalTool.this.cutoutModel.getX());
            dfY.setValue(FractalTool.this.cutoutModel.getY());
            dfXSpan.setValue(FractalTool.this.cutoutModel.getXSpan());
            FractalTool.this.convert();
         }
      });
      this.depthModel = new SpinnerNumberModel(1000, 2, 50000, 1);
      this.depthModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            FractalTool.this.convert();
         }
      });
      JSpinner ifDepth = new JSpinner(this.depthModel);
      JPanel p1 = new JPanel(new FlowLayout());
      p1.add(new JLabel("Depth:"));
      p1.add(ifDepth);
      this.chResolution = new JComboBox<>(RESOLUTION_STR);
      this.chResolution.setSelectedIndex(4);
      this.chResolution.addItemListener(this);
      p1.add(new JLabel("Resolution:"));
      p1.add(this.chResolution);
      JPanel p2 = new JPanel(new FlowLayout());
      this.cbNegative = new JCheckBox("negative", false);
      this.cbNegative.addItemListener(this);
      p2.add(this.cbNegative);
      this.chDespecle = new JComboBox<>(new String[]{"none", "0", "1", "2", "3", "4", "5", "6", "7"});
      this.chDespecle.addItemListener(this);
      p2.add(new JLabel("Despecle:"));
      p2.add(this.chDespecle);
      JPanel pRender = new JPanel();
      pRender.setBorder(new TitledBorder("Rendering"));
      pRender.setLayout(new GridDialogLayout(1, false));
      pRender.add(p1);
      pRender.add(p2);
      JPanel pTop = new JPanel(new GridDialogLayout(2, false, LayoutUtilities.getComponentGroupsSpacing(), 0));
      pTop.setBorder(new TitledBorder("Mandelbrot Set"));
      pTop.add(pMandel);
      pTop.add(FractalNavigationButtonPanelFactory.createButtonPanel(this.cutoutModel, new Dimension(this.plateWidth, this.plateHeight)));
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(pTop);
      panel.add(pRender);
      return panel;
   }

   private void convert() {
      if (this.depthModel != null && this.chResolution != null) {
         int iter = this.depthModel.getNumber().intValue();
         boolean negative = this.cbNegative.isSelected();
         int resolution = this.chResolution.getSelectedIndex();
         int despecle = this.chDespecle.getSelectedIndex();
         if (this.markPlate == null) {
            this.markPlate = new PixelPlate(new Rectangle(this.plateWidth, this.plateHeight), this.filter);
         }

         this.markPlate.clear();
         double xCenter = this.cutoutModel.getX();
         double yCenter = this.cutoutModel.getY();
         double xSpan = this.cutoutModel.getXSpan();
         drawMandelbrot(this.markPlate, this.plateWidth, this.plateHeight, xCenter, yCenter, xSpan, iter, negative, resolution, despecle);
         this.characterPlate.clear();
         LocatedCharacterPlate result = this.markPlate.convert();
         result.pasteInto(this.characterPlate);
         this.repaintPlate();
      }
   }

   private static void drawMandelbrot(
      PixelPlate plate, int width, int height, double xCenter, double yCenter, double xSpan, int iter, boolean negative, int resolution, int despecle
   ) {
      double ySpan = xSpan / (double)width * (double)height * 2.0;
      double xMin = xCenter - xSpan / 2.0;
      double yMin = yCenter - ySpan / 2.0;
      int w = width;
      int h = height;
      switch (resolution) {
         case 0:
            plate.setMode(PixelPlateMode.CHAR);
            plate.setCharacter('8');
            break;
         case 1:
            plate.setMode(PixelPlateMode.DOT);
            w = width * 1;
            h = height * 2;
            break;
         case 2:
            plate.setMode(PixelPlateMode.TWO_BY_TWO);
            w = width * 2;
            h = height * 2;
            break;
         case 3:
            plate.setMode(PixelPlateMode.THREE_BY_TWO);
            w = width * 2;
            h = height * 3;
            break;
         case 4:
            plate.setMode(PixelPlateMode.THICK_THIN);
            w = width * 3;
            h = height * 4;
      }

      iter += 3 - iter % 3;
      double dx = xSpan / (double)w;
      double dy = ySpan / (double)h;

      for (int y = 0; y < h; y++) {
         for (int x = 0; x < w; x++) {
            if (getMandelbrot(2.0 * (xMin + (double)x * dx), 2.0 * (yMin + (double)y * dy), iter) == 1 != negative) {
               plate.set(x, y);
            }
         }
      }

      plate.removeNoise(despecle - 1);
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      this.convert();
   }

   private static int getMandelbrot(double real, double imag, int iterations) {
      double r0 = real;
      double i0 = imag;

      int iters;
      for (iters = 1; iters < iterations && real * real + imag * imag < 4.0; iters++) {
         double tmp = 2.0 * real * imag + i0;
         real = real * real - imag * imag + r0;
         imag = tmp;
      }

      return iters % 3 == 0 ? 1 : 0;
   }
}
