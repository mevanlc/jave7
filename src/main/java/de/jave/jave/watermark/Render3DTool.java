package de.jave.jave.ascii3d;

import de.jave.jave.AbstractDialogTool;
import de.jave.jave.AsciiGradientComboBoxFactory;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.ascii3d.example.Cube3dExample;
import de.jave.jave.ascii3d.example.I3dExample;
import de.jave.jave.ascii3d.example.Jave3dExample;
import de.jave.jave.ascii3d.example.Key3dExample;
import de.jave.jave.ascii3d.example.Pyramid3dExample;
import de.jave.jave.filter.Filter;
import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.pixelplate.PixelPlateMode;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.LocatedCharacterPlate;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.Action;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.toolbar.ToolBarBuilder;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class Render3DTool extends AbstractDialogTool implements ItemListener, KeyListener {
   static final String TITLE = "3D Rendering Tool";
   static final double D = 3.0;
   private JComboBox chDemo;
   JComboBox chStyle;
   JComboBox tfGradient;
   private static final String[] STYLES = new String[]{"Solid", "Wireframe", "High Resolution", "Shaded"};
   private static final I3dExample[] EXAMPLES = new I3dExample[]{new Key3dExample(), new Cube3dExample(), new Pyramid3dExample(), new Jave3dExample()};
   private static final int WIREFRAME_VISIBLE = 0;
   private static final int WIREFRAME = 1;
   private static final int HIRES = 2;
   private static final int SHADED = 3;
   private static final int SOLID = 4;
   private static final int SOLID_DEPTH = 5;
   JTextArea taScript;
   private final Navigate3dModel model = new Navigate3dModel();
   private final JavEApplication application;
   private JLabel gradientLabel;
   private final AsciiGradientConfiguration gradientConfiguration;
   private final Filter filter;

   public Render3DTool(JavEApplication application, JaveApplicationPreferences preferences, AsciiGradientConfiguration gradientConfiguration, Filter filter) {
      super(application, preferences);
      Ensure.ensureArgumentNotNull(application);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.application = application;
      this.gradientConfiguration = gradientConfiguration;
      this.filter = filter;
      this.getDialog().getDialog().getWindow().addKeyListener(this);
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Render3DTool.this.render();
         }
      });
      this.render();
   }

   @Override
   protected ColorScheme getPreferredColorScheme() {
      return ColorScheme.WHITE_ON_BLACK;
   }

   @Override
   protected String getToolTitle() {
      return "3D Rendering Tool";
   }

   @Override
   public String getToolActionName() {
      return "render 3D";
   }

   @Override
   public JComponent getOptionsComponent() {
      this.taScript = new JTextArea(EXAMPLES[0].getCode(), 9, 40);
      this.taScript.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            Render3DTool.this.render();
         }
      });
      this.taScript.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      final DefaultBoundedRangeModel sliderModel = new DefaultBoundedRangeModel(this.model.getAlpha(), 1, 0, 360);
      sliderModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            Render3DTool.this.model.setAlpha(sliderModel.getValue());
         }
      });
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            sliderModel.setValue(Render3DTool.this.model.getAlpha());
         }
      });
      JSlider rotateSlider = new JSlider(sliderModel);
      rotateSlider.setToolTipText("Rotate");
      ToolBarBuilder toolBarBuilder = new ToolBarBuilder();
      toolBarBuilder.add(new RotateLeftAction(this.model));
      toolBarBuilder.add(new RotateRightAction(this.model));
      toolBarBuilder.add(rotateSlider);
      toolBarBuilder.add(new ZoomInAction(this.model));
      toolBarBuilder.add(new ZoomOutAction(this.model));
      toolBarBuilder.add(new ResetAction(this.model));
      JToolBar toolBar = toolBarBuilder.createToolBar();
      this.chDemo = new JComboBox<>(EXAMPLES);
      this.chDemo.setRenderer(new ObjectUiListCellRenderer(new Ascii3dExampleObjectUi()));
      this.chDemo.addItemListener(this);
      this.chStyle = new JComboBox<>(STYLES);
      this.chStyle.addItemListener(this);
      this.tfGradient = AsciiGradientComboBoxFactory.createComponent(this.gradientConfiguration);
      this.tfGradient.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Render3DTool.this.render();
         }
      });
      JPanel pControls = new JPanel(new GridDialogLayout(6, false));
      pControls.add(new JLabel("Demos:"));
      pControls.add(this.chDemo);
      pControls.add(new JLabel("Style:"));
      pControls.add(this.chStyle);
      this.gradientLabel = new JLabel("Gradient:");
      pControls.add(this.gradientLabel);
      pControls.add(this.tfGradient);
      JPanel p = new JPanel(new BorderLayout(2, 2));
      p.add(toolBar, "North");
      p.add(pControls, "South");
      p.add(new JScrollPane(this.taScript), "Center");
      p.addKeyListener(this);
      pControls.addKeyListener(this);
      toolBar.addKeyListener(this);
      this.updateGradientEnabled();
      return p;
   }

   private void updateGradientEnabled() {
      boolean isShaded = this.chStyle.getSelectedIndex() == 3;
      this.tfGradient.setEnabled(isShaded);
      this.gradientLabel.setEnabled(isShaded);
   }

   @Override
   protected Action[] getAdditionalActions() {
      return new Action[]{new Animate3dAction(this, this.application, this.model, this.filter)};
   }

   @Override
   public void keyReleased(KeyEvent evt) {
   }

   @Override
   public void keyTyped(KeyEvent evt) {
   }

   @Override
   public void keyPressed(KeyEvent evt) {
      int code = evt.getKeyCode();
      if (code == 37) {
         this.model.doRotateLeft();
         evt.consume();
      } else if (code == 39) {
         this.model.doRotateRight();
         evt.consume();
      } else if (code == 38) {
         this.model.doZoomIn();
         evt.consume();
      } else if (code == 40) {
         this.model.doZoomOut();
         evt.consume();
      }
   }

   private final void render() {
      if (this.markPlate == null) {
         this.markPlate = new PixelPlate(new Rectangle(this.plateWidth, this.plateHeight), this.filter);
      }

      double dx = this.plateWidth / 2;
      double dy = this.plateHeight / 2;
      int style = this.chStyle.getSelectedIndex();
      String gradient = (String)this.tfGradient.getSelectedItem();
      if (gradient.length() == 0) {
         gradient = " ";
      }

      render(this.markPlate, this.taScript.getText(), style, gradient, this.model.getAlpha(), 3.0, this.model.getZoom(), dx, dy);
      this.characterPlate.clear();
      LocatedCharacterPlate result = this.markPlate.convert();
      result.pasteInto(this.characterPlate);
      this.repaintPlate();
   }

   public static final void render(PixelPlate markPlate, String script, int style, String gradient, double alpha, double d, double scale, double dx, double dy) {
      switch (style) {
         case 0:
         case 1:
            markPlate.setMode(PixelPlateMode.DOT);
            break;
         case 2:
            markPlate.setMode(PixelPlateMode.PIXEL);
            break;
         case 3:
         case 4:
         case 5:
            markPlate.setMode(PixelPlateMode.CHAR);
      }

      markPlate.clear();
      double scaleX = scale * 1.98;
      double scaleY = scale;
      int vWidth = markPlate.getVirtualWidth();
      int vHeight = markPlate.getVirtualHeight();
      double factorX = vWidth / markPlate.getWidth();
      double factorY = vHeight / markPlate.getHeight();
      double[][] zBuffer = new double[vHeight][vWidth];

      for (int x = 0; x < vWidth; x++) {
         zBuffer[0][x] = Double.MAX_VALUE;
      }

      for (int y = 1; y < vHeight; y++) {
         System.arraycopy(zBuffer[0], 0, zBuffer[y], 0, vWidth);
      }

      int[][] markBuffer = new int[vHeight][vWidth];
      int mode = 0;
      List<double[]> points = new ArrayList<>();
      List<Integer> shades = new ArrayList<>();
      StringTokenizer st = new StringTokenizer(script, "\n", false);
      int polCounter = 0;

      while (st.hasMoreTokens()) {
         String line = st.nextToken().trim();
         if (line.length() != 0 && line.charAt(0) != '#' && !line.startsWith("AlWuzEre")) {
            if (mode == 0 && line.indexOf(46) == -1 && line.indexOf(45) == -1) {
               mode = 1;
            }

            if (mode == 0) {
               try {
                  StringTokenizer sl = new StringTokenizer(line, " \t", false);
                  double x1 = Double.parseDouble(sl.nextToken());
                  double y1 = Double.parseDouble(sl.nextToken());
                  double z1 = Double.parseDouble(sl.nextToken());
                  double[] p = transform(x1, y1, z1, alpha, d, scaleX, scaleY, dx, dy);
                  points.add(p);
               } catch (Exception var96) {
                  System.err.println("Warning: Line '" + line + "' seems to be invalid. - ignored");
               }
            } else {
               try {
                  polCounter++;
                  StringTokenizer sl = new StringTokenizer(line, " \t", false);
                  int count = sl.countTokens();
                  int[] polygon = new int[count];

                  for (int i = 0; i < count; i++) {
                     polygon[i] = Integer.parseInt(sl.nextToken());
                  }

                  if (style == 1) {
                     for (int i = 0; i < count; i++) {
                        double[] p0 = points.get(polygon[i]);
                        double[] p1 = points.get(polygon[(i + 1) % count]);
                        markPlate.drawLine(p0[0] + dx, p0[1] + dy, p1[0] + dx, p1[1] + dy);
                     }
                  } else {
                     double[] polX = new double[count];
                     double[] polY = new double[count];

                     for (int i = 0; i < count; i++) {
                        double[] p = points.get(polygon[i]);
                        polX[i] = p[0] * factorX;
                        polY[i] = p[1] * factorY;
                     }

                     Polygon2d pol2 = new Polygon2d(polX, polY);
                     double[] p0 = points.get(polygon[0]);
                     double[] p1 = points.get(polygon[1]);
                     double[] p2 = points.get(polygon[2]);
                     double ax = p0[3] - p1[3];
                     double ay = p0[4] - p1[4];
                     double az = p0[2] - p1[2];
                     double bx = p2[3] - p1[3];
                     double by = p2[4] - p1[4];
                     double bz = p2[2] - p1[2];
                     double nx = ay * bz - az * by;
                     double ny = az * bx - ax * bz;
                     double nz = ax * by - ay * bx;
                     double e = Math.sqrt(nx * nx + ny * ny + nz * nz);
                     nx /= e;
                     ny /= e;
                     nz /= e;
                     ax = p0[5] - p1[5];
                     ay = p0[6] - p1[6];
                     az = p0[7] - p1[7];
                     bx = p2[5] - p1[5];
                     by = p2[6] - p1[6];
                     bz = p2[7] - p1[7];
                     double lnx = ay * bz - az * by;
                     double lny = az * bx - ax * bz;
                     double lnz = ax * by - ay * bx;
                     e = Math.sqrt(lnx * lnx + lny * lny + lnz * lnz);
                     lnx /= e;
                     lny /= e;
                     lnz /= e;
                     double lx = 1.0;
                     double ly = -1.0;
                     double lz = 2.0;
                     e = Math.sqrt(lx * lx + ly * ly + lz * lz);
                     lx /= e;
                     ly /= e;
                     lz /= e;
                     double cosAlpha = lx * lnx + ly * lny + lz * lnz;
                     double angle = (180.0 / Math.PI) * Math.acos(cosAlpha);
                     if (angle > 90.0) {
                        angle = 180.0 - angle;
                     }

                     int iAngle = (int)(angle / 90.0 * (double)gradient.length());
                     if (iAngle >= gradient.length()) {
                        iAngle = gradient.length() - 1;
                     }

                     shades.add(iAngle);
                     double px = p0[3] - 0.0;
                     double py = p0[4] - 0.0;
                     double pz = p0[2] + d;
                     int minY = (int)(pol2.getMinY() + dy * factorY) - 1;
                     int maxY = (int)(pol2.getMaxY() + dy * factorY) + 1;
                     int minX = (int)(pol2.getMinX() + dx * factorX) - 1;
                     int maxX = (int)(pol2.getMaxX() + dx * factorX) + 1;
                     if (minY < 0) {
                        minY = 0;
                     }

                     if (minX < 0) {
                        minX = 0;
                     }

                     if (maxX >= vWidth) {
                        maxX = vWidth - 1;
                     }

                     if (maxY >= vHeight) {
                        maxY = vHeight - 1;
                     }

                     for (int y = minY; y <= maxY; y++) {
                        for (int x = minX; x <= maxX; x++) {
                           if (pol2.contains((double)x - dx * factorX, (double)y - dy * factorY)) {
                              double xx = (double)x / factorX - dx;
                              double yy = (double)y / factorY - dy;
                              double z = getDepth(xx, yy, d, nx, ny, nz, px, py, pz);
                              if (z < zBuffer[y][x]) {
                                 zBuffer[y][x] = z;
                                 markBuffer[y][x] = polCounter;
                              }
                           }
                        }
                     }
                  }
               } catch (Exception var97) {
                  System.err.println("Warning: Line '" + line + "' seems to be invalid. - ignored");
               }
            }
         }
      }

      if (style != 0 && style != 2) {
         if (style == 4) {
            for (int y = 0; y < vHeight; y++) {
               for (int xx = 0; xx < vWidth; xx++) {
                  markPlate.set(xx, y, " 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ#*+-:?!()[]{}@".charAt(markBuffer[y][xx]));
               }
            }
         } else if (style == 3) {
            for (int y = 0; y < vHeight; y++) {
               for (int xx = 0; xx < vWidth; xx++) {
                  if (markBuffer[y][xx] > 0) {
                     int shade = shades.get(markBuffer[y][xx] - 1);
                     markPlate.set(xx, y, gradient.charAt(gradient.length() - shade - 1));
                  }
               }
            }
         } else {
            double min = 0.0;
            double max = 0.0;

            for (int y = 0; y < vHeight; y++) {
               for (int xxx = 0; xxx < vWidth; xxx++) {
                  if (zBuffer[y][xxx] < min) {
                     min = zBuffer[y][xxx];
                  }

                  if (zBuffer[y][xxx] > max && zBuffer[y][xxx] < Double.MAX_VALUE) {
                     max = zBuffer[y][xxx];
                  }
               }
            }

            for (int y = 0; y < vHeight; y++) {
               for (int xxx = 0; xxx < vWidth; xxx++) {
                  if (!(zBuffer[y][xxx] > max)) {
                     int index = (int)((zBuffer[y][xxx] - min) / (max - min) * (double)gradient.length());
                     if (index < 0) {
                        index = 0;
                     } else if (index >= gradient.length()) {
                        index = gradient.length() - 1;
                     }

                     markPlate.set(xxx, y, gradient.charAt(index));
                  }
               }
            }
         }
      } else {
         for (int y = 0; y < vHeight; y++) {
            for (int xxxx = 0; xxxx < vWidth; xxxx++) {
               if (markBuffer[y][xxxx] > 0) {
                  if (xxxx + 1 < vWidth && markBuffer[y][xxxx + 1] != markBuffer[y][xxxx]) {
                     markPlate.set(xxxx, y);
                  } else if (y + 1 < vHeight && markBuffer[y + 1][xxxx] != markBuffer[y][xxxx]) {
                     markPlate.set(xxxx, y);
                  } else if (xxxx + 1 < vWidth && markBuffer[y][xxxx + 1] == 0) {
                     markPlate.set(xxxx, y);
                  } else if (y + 1 < vHeight && markBuffer[y + 1][xxxx] == 0) {
                     markPlate.set(xxxx, y);
                  } else if (xxxx > 0 && markBuffer[y][xxxx - 1] == 0) {
                     markPlate.set(xxxx, y);
                  } else if (y > 0 && markBuffer[y - 1][xxxx] == 0) {
                     markPlate.set(xxxx, y);
                  }
               }
            }
         }
      }
   }

   public static final double getDepth(double x, double y, double d, double nx, double ny, double nz, double px, double py, double pz) {
      double sx = x - 0.0;
      double sy = y - 0.0;
      return (px * nx + py * ny + pz * nz) / (sx * nx + sy * ny + d * nz);
   }

   public static final double[] transform(double x, double y, double z, double alpha, double d, double scaleX, double scaleY, double dx, double dy) {
      double a = alpha / 180.0 * Math.PI;
      double x1 = x * Math.cos(a) + z * Math.sin(a);
      double z1 = z * Math.cos(a) - x * Math.sin(a);
      double var36 = x1 * scaleX;
      double y1 = y * -scaleY;
      double xx = var36 / (z1 / d + 1.0);
      double yy = y1 / (z1 / d + 1.0);
      return new double[]{xx, yy, z1, var36, y1, x1, y, z1};
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      Object source = evt.getSource();
      if (source == this.chDemo) {
         this.taScript.setText(((I3dExample)this.chDemo.getSelectedItem()).getCode());
         this.render();
      } else if (source == this.chStyle) {
         this.updateGradientEnabled();
         this.render();
      }
   }

   public void dispose() {
      this.getDialog().setVisible(false);
   }
}
