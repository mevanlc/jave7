package de.jave.jave;

import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.ImageIOUtilities;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.tool.dialog.IInlineToolOptions;
import de.jave.jave.watermark.IWatermarkPainter;
import de.jave.jave.watermark.WatermarkData;
import de.jave.jave.watermark.WatermarkImageFile;
import de.jave.lib.Toolbox;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.widgets.HorizontalLine;

public class WatermarkTool extends Tool implements IWatermarkPainter {
   private WatermarkData data;
   private JButton bFit;
   private JButton bOpen;
   private JButton bClose;
   private JTextField tfImageName;
   private JTextField tfPosition;
   private JTextField tfSize;
   private JSpinner spBrightness;
   private JSlider slBrightness;
   private SpinnerNumberModel brightnessModel;
   private JCheckBox cbNegative;
   private double xPos;
   private double yPos;
   private double wPos;
   private double hPos;
   private int mode;
   private MoveResizeRectangle imageRegion;
   private boolean enabled;
   private static final String LABEL = "Watermark";
   private final FileModel imageFileModel = new FileModel();
   private IFileChooserConfiguration fileChooserConfiguration;
   private IInlineToolOptions inlineOptions;
   private final JaveApplicationPreferences applicationPreferences;
   private Point point1;

   public WatermarkTool(JaveMainPanel mainPanel, JavEApplication application, Filter filter) {
      super(mainPanel, application, filter);
      this.applicationPreferences = application.getApplicationPreferences();
      mainPanel.addWatermarkPainter(this);
   }

   @Override
   public String getName() {
      return "Watermark";
   }

   @Override
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean hasData() {
      return this.data != null;
   }

   @Override
   public Icon getIcon() {
      return JaveIcons.TOOL_WATERMARK_ICON;
   }

   @Override
   public IInlineToolOptions getInlineOptionsPanel() {
      if (this.inlineOptions == null) {
         this.inlineOptions = new WatermarkOptionsPanel(this.buildWatermarkOptionsContent());
      }
      return this.inlineOptions;
   }

   protected JComponent buildWatermarkOptionsContent() {
      this.fileChooserConfiguration = ImageIOUtilities.createImageOpenFileChooserConfiguration(
         this.application.getDocumentManager().getCurrentDirectoryModel()
      );

      this.tfImageName = new JTextField(10);
      this.tfImageName.setEditable(false);
      this.imageFileModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            File file = WatermarkTool.this.imageFileModel.getValue();
            WatermarkTool.this.tfImageName.setText(file == null ? "" : file.getName());
            WatermarkTool.this.tfImageName.setToolTipText(file == null ? null : file.getAbsolutePath());
            if (file != null) {
               WatermarkTool.this.fileChooserConfiguration.getCurrentDirectoryModel().setValue(file.getParentFile());
            }
            WatermarkTool.this.bClose.setEnabled(file != null);
         }
      });

      SmartAction openAction = new SmartAction(JaveIcons.OPEN_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            WatermarkTool.this.openImageChooser(parentComponent);
         }
      };
      openAction.setToolTipText("Open Image File as Watermark");
      SmartAction closeAction = new SmartAction(JaveIcons.CLOSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            if (WatermarkTool.this.performCloseImage()) {
               WatermarkTool.this.imageFileModel.setValue(null);
            }
         }
      };
      closeAction.setToolTipText("Close Current Watermark Image");
      this.bOpen = new JButton("Open", JaveIcons.OPEN_ICON);
      this.bOpen.addActionListener(openAction);
      this.bOpen.setMargin(new java.awt.Insets(2, 4, 2, 4));
      this.bClose = new JButton("Close", JaveIcons.CLOSE_ICON);
      this.bClose.addActionListener(closeAction);
      this.bClose.setMargin(new java.awt.Insets(2, 4, 2, 4));
      this.bClose.setEnabled(false);

      this.bFit = new JButton(new SmartAction("Fit Canvas") {
         @Override
         protected void execute(Component parentComponent) {
            WatermarkTool.this.fit();
         }
      });

      this.tfPosition = readonlyField();
      this.tfSize = readonlyField();

      this.brightnessModel = new SpinnerNumberModel(0.0d, -1.0d, 1.0d, 0.05d);
      this.spBrightness = new JSpinner(this.brightnessModel);
      ((JSpinner.DefaultEditor) this.spBrightness.getEditor()).getTextField().setColumns(4);
      this.slBrightness = new JSlider(-100, 100, 0);
      this.slBrightness.setPreferredSize(new java.awt.Dimension(140, this.slBrightness.getPreferredSize().height));
      this.brightnessModel.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int sliderVal = (int) Math.round(WatermarkTool.this.brightnessModel.getNumber().doubleValue() * 100.0);
            if (WatermarkTool.this.slBrightness.getValue() != sliderVal) {
               WatermarkTool.this.slBrightness.setValue(sliderVal);
            }
            WatermarkTool.this.applyBrightness();
         }
      });
      this.slBrightness.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            double dv = WatermarkTool.this.slBrightness.getValue() / 100.0;
            if (Math.abs(WatermarkTool.this.brightnessModel.getNumber().doubleValue() - dv) > 0.0001) {
               WatermarkTool.this.brightnessModel.setValue(dv);
            }
         }
      });

      this.cbNegative = new JCheckBox("Negative", false);
      this.cbNegative.addItemListener(this);

      this.bFit.setEnabled(false);
      this.cbNegative.setEnabled(false);
      this.spBrightness.setEnabled(false);
      this.slBrightness.setEnabled(false);
      this.bOpen.setEnabled(this.mainPanel.getActiveEditorModel().getActiveEditor() != null);

      this.mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (WatermarkTool.this.bOpen != null) {
               WatermarkTool.this.bOpen.setEnabled(WatermarkTool.this.mainPanel.getActiveEditorModel().getActiveEditor() != null);
            }
         }
      });

      JPanel imageRow = new JPanel(new GridDialogLayout(2, false));
      imageRow.add(this.bOpen);
      imageRow.add(this.bClose);

      JPanel brightnessRow = new JPanel(new GridDialogLayout(2, false));
      brightnessRow.add(new JLabel("Brightness:"));
      brightnessRow.add(this.spBrightness);

      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(new JLabel("Image:"));
      panel.add(this.tfImageName, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(imageRow);
      panel.add(new HorizontalLine(), new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(brightnessRow);
      panel.add(this.slBrightness, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(new HorizontalLine(), new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(this.cbNegative);
      panel.add(new HorizontalLine(), new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(new JLabel("Position:"));
      panel.add(this.tfPosition, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(new JLabel("Size:"));
      panel.add(this.tfSize, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      panel.add(this.bFit, new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL));
      return panel;
   }

   private static JTextField readonlyField() {
      JTextField tf = new JTextField(10);
      tf.setEditable(false);
      return tf;
   }

   private void openImageChooser(Component parentComponent) {
      if (this.fileChooserConfiguration == null) {
         this.fileChooserConfiguration = ImageIOUtilities.createImageOpenFileChooserConfiguration(
            this.application.getDocumentManager().getCurrentDirectoryModel()
         );
      }
      FileSelection sel = FileChooserUtilities.performOpenFileChooser(parentComponent, this.fileChooserConfiguration);
      if (!sel.isEmpty() && this.performLoadImage(parentComponent, sel.getFile())) {
         this.imageFileModel.setValue(sel.getFile());
      }
   }

   private void applyBrightness() {
      if (this.data == null) {
         return;
      }
      double brightness = this.brightnessModel.getNumber().doubleValue();
      boolean negative = this.cbNegative.isSelected();
      Image image2 = lighten(this.data.getOriginalImage(), brightness, negative);
      if (image2 != null) {
         this.data.setVisibleImage(image2);
         this.repaintAll();
      }
   }

   @Override
   public void takeToHand() {
      this.setCursor(Cursor.getPredefinedCursor(0));
      this.application.getToolBar().setWatermarkVisible(true);
   }

   @Override
   public void putAside(boolean nextToolIsSelectionTool) {
   }

   @Override
   public boolean containsScreenPoint(Point point) {
      return this.imageRegion != null && this.imageRegion.contains(point);
   }

   @Override
   public void paint(Graphics g, Point plateOrigin, ColorScheme colorScheme, int charWidth, int charHeight) {
      if (this.data != null) {
         int ww = (int)(this.wPos * (double)charWidth);
         int hh = (int)(this.hPos * (double)charHeight);
         Point p0 = this.getPlate().getScreenPointFor(this.xPos, this.yPos);
         int x0 = p0.x;
         int y0 = p0.y;
         this.imageRegion = new MoveResizeRectangle(x0, y0, ww, hh);
         if (this.enabled) {
            g.drawImage(this.data.getVisibleImage(), x0, y0, ww, hh, this.getPlate());
         }
      }
   }

   private boolean performCloseImage() {
      this.data = null;
      this.imageRegion = null;
      this.setCursor(Cursor.getPredefinedCursor(0));
      this.repaintAll();
      this.bFit.setEnabled(false);
      this.cbNegative.setEnabled(false);
      this.spBrightness.setEnabled(false);
      this.slBrightness.setEnabled(false);
      return true;
   }

   public boolean performLoadImage(Component parentComponent, File file) {
      BufferedImage loadedImage;
      try {
         loadedImage = ImageIO.read(file);
      } catch (IOException var5) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message(JaveMessages.JavE, "Error loading image.", var5));
         return false;
      }

      if (loadedImage == null) {
         MessageDialogFactory.showMessageDialog(
            parentComponent, new Message(JaveMessages.JavE, "Error loading image. The file format is not supported.", MessageType.ERROR)
         );
         return false;
      } else if (!this.setImage(new WatermarkImageFile(loadedImage, file))) {
         MessageDialogFactory.showMessageDialog(
            parentComponent,
            new Message(JaveMessages.JavE, "Error loading image: Wrong file format?\nSupported formats: GIF, JPG, BMP (experimental).", MessageType.ERROR)
         );
         return false;
      } else {
         return true;
      }
   }

   public boolean setImage(WatermarkImageFile imageFile) {
      int imageWidth = imageFile.getImage().getWidth();
      int imageHeight = imageFile.getImage().getHeight();
      if (imageWidth != -1 && imageHeight != -1) {
         this.bFit.setEnabled(true);
         this.cbNegative.setEnabled(true);
         this.spBrightness.setEnabled(true);
         this.slBrightness.setEnabled(true);
         Dimension documentSize = this.getCurrentDocumentSize();
         this.wPos = documentSize.getWidth();
         if (this.wPos < 5.0) {
            this.wPos = 5.0;
         }

         this.hPos = (double)imageHeight * this.wPos / (double)imageWidth / 1.98;
         this.application.getToolBar().setWatermarkVisible(true);
         this.updateLabels();
         double brightness = this.brightnessModel.getNumber().doubleValue();
         boolean negative = this.cbNegative.isSelected();
         Image image2 = lighten(imageFile.getImage(), brightness, negative);
         this.data = new WatermarkData(imageFile.getImage(), image2);
         this.repaintAll();
         this.imageFileModel.setValue(imageFile.getFile());
         return true;
      } else {
         return false;
      }
   }

   private Dimension getCurrentDocumentSize() {
      Plate plate = this.getPlate();
      return plate == null ? this.applicationPreferences.getDefaultDocumentSize() : plate.getDocumentSize();
   }

   protected void fit() {
      if (this.data != null) {
         Dimension documentSize = this.getCurrentDocumentSize();
         this.xPos = 0.0;
         this.yPos = 0.0;
         this.wPos = documentSize.getWidth();
         this.hPos = documentSize.getHeight();
         this.updateLabels();
         this.repaintAll();
      }
   }

   @Override
   public void paintCursorFeature(Graphics2D g, Point plateOrigin, ColorScheme colorScheme) {
      if (this.data != null && this.imageRegion != null) {
         g.setColor(colorScheme.getColorToolRegion());
         this.imageRegion.paint(g);
         g.setColor(colorScheme.getColorToolRegion());
         int x0 = this.imageRegion.x;
         int y0 = this.imageRegion.y;
         int ww = this.imageRegion.width;
         int hh = this.imageRegion.height;
         g.setFont(JaveGlobalRessources.FONT_SMALL);
         FontMetrics fm = this.application.getFrame().getFontMetrics(JaveGlobalRessources.FONT_SMALL);
         int labelWidth = fm.stringWidth("Watermark") + 10;
         int[] xPoints = new int[]{x0 + ww - labelWidth - 5, x0 + ww - labelWidth + 5, x0 + ww - 10, x0 + ww};
         int[] yPoints = new int[]{y0 + hh, y0 + hh + 20, y0 + hh + 20, y0 + hh};
         g.setColor(colorScheme.getColorPlateBackground());
         g.fillPolygon(xPoints, yPoints, 4);
         g.setColor(colorScheme.getColorToolRegion());
         g.drawPolygon(xPoints, yPoints, 4);
         g.drawString("Watermark", x0 + ww - labelWidth + 3, y0 + hh + 13);
      }
   }

   @Override
   public void mouseMoved(Point point, Point location, MouseEvent evt) {
      super.mouseMoved(point, location, evt);
      if (this.data != null && this.imageRegion != null) {
         this.setCursor(this.imageRegion.getCursorFor(point));
      }
   }

   @Override
   public void mousePressed(Point point, Point location, MouseEvent evt) {
      if (this.data != null && this.imageRegion != null) {
         this.mode = this.imageRegion.getPlace(point);
         this.point1 = point;
      }
   }

   @Override
   public void mouseDragged(Point point, Point location, MouseEvent evt) {
      if (this.data != null && this.imageRegion != null && this.point1 != null) {
         if (this.mode != 0) {
            int dx = point.x - this.point1.x;
            int dy = point.y - this.point1.y;
            int charWidth = this.getPlate().getCharWidth();
            int charHeight = this.getPlate().getCharHeight();
            double ddx = (double)dx / (double)charWidth;
            double ddy = (double)dy / (double)charHeight;
            this.point1 = point;
            if (this.mode == 1) {
               this.xPos += ddx;
               this.yPos += ddy;
            } else if (this.mode == 2) {
               this.yPos += ddy;
               this.hPos -= ddy;
               if (shiftDown) {
                  this.wPos = this.hPos / (double)this.data.getImageSize().height * (double)this.data.getImageSize().width * 1.98;
               }
            } else if (this.mode == 4) {
               this.hPos += ddy;
               if (shiftDown) {
                  this.wPos = this.hPos / (double)this.data.getImageSize().height * (double)this.data.getImageSize().width * 1.98;
               }
            } else if (this.mode == 5) {
               this.wPos += ddx;
               if (shiftDown) {
                  this.hPos = (double)this.data.getImageSize().height * this.wPos / (double)this.data.getImageSize().width / 1.98;
               }
            } else if (this.mode == 3) {
               this.xPos += ddx;
               this.wPos -= ddx;
               if (shiftDown) {
                  this.hPos = (double)this.data.getImageSize().height * this.wPos / (double)this.data.getImageSize().width / 1.98;
               }
            } else if (this.mode == 7) {
               this.yPos += ddy;
               this.hPos -= ddy;
               this.xPos += ddx;
               this.wPos -= ddx;
               if (shiftDown) {
                  double dw = this.wPos - this.hPos / (double)this.data.getImageSize().height * (double)this.data.getImageSize().width * 1.98;
                  this.wPos -= dw;
                  this.xPos += dw;
               }
            } else if (this.mode == 6) {
               this.yPos += ddy;
               this.hPos -= ddy;
               this.wPos += ddx;
               if (shiftDown) {
                  this.wPos = this.hPos / (double)this.data.getImageSize().height * (double)this.data.getImageSize().width * 1.98;
               }
            } else if (this.mode == 8) {
               this.hPos += ddy;
               this.wPos += ddx;
               if (shiftDown) {
                  this.hPos = (double)this.data.getImageSize().height * this.wPos / (double)this.data.getImageSize().width / 1.98;
               }
            } else if (this.mode == 9) {
               this.hPos += ddy;
               this.xPos += ddx;
               this.wPos -= ddx;
               if (shiftDown) {
                  this.hPos = (double)this.data.getImageSize().height * this.wPos / (double)this.data.getImageSize().width / 1.98;
               }
            }

            if (this.wPos < 2.0) {
               this.wPos = 2.0;
            }

            if (this.hPos < 2.0) {
               this.hPos = 2.0;
            }

            this.updateLabels();
            this.repaintAll();
         }
      }
   }

   public static Image lighten(BufferedImage sourceImage, double value, boolean negative) {
      int width = sourceImage.getWidth();
      int height = sourceImage.getHeight();
      int[] pixels = new int[width * height];
      PixelGrabber pg = new PixelGrabber(sourceImage, 0, 0, width, height, pixels, 0, width);

      try {
         pg.grabPixels();
      } catch (InterruptedException var13) {
         throw new RuntimeException("Internal Error: " + var13.toString());
      }

      if ((pg.getStatus() & 128) != 0) {
         throw new RuntimeException("Internal Error: Image fetch aborted or errored.");
      } else {
         double v1 = 1.0 - value;
         double v255 = value * 255.0;
         if (value < 0.0) {
            v1 = value + 1.0;
            v255 = 0.0;
         }

         if (!negative) {
            for (int i = 0; i < pixels.length; i++) {
               pixels[i] = pixels[i] & 0xFF000000
                  | (int)((double)(pixels[i] >> 16 & 0xFF) * v1 + v255) << 16
                  | (int)((double)(pixels[i] >> 8 & 0xFF) * v1 + v255) << 8
                  | (int)((double)(pixels[i] & 0xFF) * v1 + v255);
            }
         } else {
            for (int i = 0; i < pixels.length; i++) {
               pixels[i] = pixels[i] & 0xFF000000
                  | (int)((double)(255 - (pixels[i] >> 16 & 0xFF)) * v1 + v255) << 16
                  | (int)((double)(255 - (pixels[i] >> 8 & 0xFF)) * v1 + v255) << 8
                  | (int)((double)(255 - (pixels[i] & 0xFF)) * v1 + v255);
            }
         }

         BufferedImage image = new BufferedImage(width, height, 2);
         image.getRaster().setDataElements(0, 0, width, height, pixels);
         return image;
      }
   }

   @Override
   public void itemStateChanged(ItemEvent evt) {
      if (this.data != null) {
         if (evt.getSource() == this.cbNegative) {
            double brightness = this.brightnessModel.getNumber().doubleValue();
            boolean negative = this.cbNegative.isSelected();
            this.data.setVisibleImage(lighten(this.data.getOriginalImage(), brightness, negative));
         }

         this.repaintAll();
      }
   }

   protected void updateLabels() {
      double dx = Toolbox.round(this.xPos, 2);
      double dy = Toolbox.round(this.yPos, 2);
      double dw = Toolbox.round(this.wPos, 2);
      double dh = Toolbox.round(this.hPos, 2);
      this.tfPosition.setText(dx + "; " + dy);
      this.tfSize.setText(dw + "; " + dh);
   }

   public void performLoadImage(Component parent) {
      this.openImageChooser(parent);
   }
}
