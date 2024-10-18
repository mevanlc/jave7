package de.jave.image2ascii.dialog;

import de.jave.gui.GSliderArrangement;
import de.jave.image.GImage;
import de.jave.image.Rotation;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.greyscale.algorithm.dithering.DitheringAlgorithms;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.image.swing.ImagePanel;
import de.jave.image2ascii.DitheringListCellRenderer;
import de.jave.image2ascii.IImage2AsciiAlgorithm;
import de.jave.image2ascii.RotationUi;
import de.jave.image2ascii.model.Image2AsciiImageProcessingOptionsModel;
import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.layout.grid.GridAlignment;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;

public class Image2AsciiImageProcessingOptionsPanel {
   public static final int HIGHLIGHT_DEFAULT = 100;
   private static final int HIGHLIGHT_MIN = 0;
   private static final int HIGHLIGHT_MAX = 100;
   private static final int HIGHLIGHT_STEP = 1;
   private static final int HIGHLIGHT_DIVIDE = 1;
   public static final int SHADOW_DEFAULT = 0;
   private static final int SHADOW_MIN = 0;
   private static final int SHADOW_MAX = 100;
   private static final int SHADOW_STEP = 1;
   private static final int SHADOW_DIVIDE = 1;
   public static final int GAMMA_DEFAULT = 100;
   private static final int GAMMA_MIN = 1;
   private static final int GAMMA_MAX = 500;
   private static final int GAMMA_STEP = 1;
   public static final int GAMMA_DIVIDE = 100;
   public static final int SHARPEN_DEFAULT = 0;
   private static final int SHARPEN_MIN = 0;
   private static final int SHARPEN_MAX = 100;
   private static final int SHARPEN_STEP = 1;
   public static final int SHARPEN_DIVIDE = 100;
   private final ImagePanel cPreviewImage = new ImagePanel();
   private final JComponent content;

   public Image2AsciiImageProcessingOptionsPanel(final Image2AsciiSourceImageModel sourceImageModel, final Image2AsciiOptionsModel optionsModel) {
      Ensure.ensureArgumentNotNull(sourceImageModel);
      Ensure.ensureArgumentNotNull(optionsModel);
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      GridDialogLayoutData top = new GridDialogLayoutData();
      top.setVerticalAlignment(GridAlignment.BEGINNING);
      panel.add(this.createLeftPanel(optionsModel), top);
      panel.add(this.createRightPanel(optionsModel), GridDialogLayoutData.FILL_BOTH);
      this.content = panel;
      IChangeListener updatePreviewChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2AsciiImageProcessingOptionsPanel.this.convertPreview(sourceImageModel, optionsModel);
         }
      };
      sourceImageModel.addChangeListener(updatePreviewChangeListener);
      optionsModel.addChangeListener(updatePreviewChangeListener);
   }

   private JComponent createLeftPanel(final Image2AsciiOptionsModel optionsModel) {
      GridDialogPanelBuilder panel = new GridDialogPanelBuilder();
      final Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = optionsModel.getImageProcessingOptionsModel();
      final GSliderArrangement slaHighlight = new GSliderArrangement("Highlight:", 0, 100, imageProcessingOptionsModel.getHighlightValue(), 1, 1);
      slaHighlight.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int value = slaHighlight.getValue();
            imageProcessingOptionsModel.setHighlightValue(value);
         }
      });
      panel.add(slaHighlight);
      final GSliderArrangement slaShadow = new GSliderArrangement("Shadow:", 0, 100, imageProcessingOptionsModel.getShadowValue(), 1, 1);
      slaShadow.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            int value = slaShadow.getValue();
            imageProcessingOptionsModel.setShadowValue(value);
         }
      });
      panel.add(slaShadow);
      final GSliderArrangement slaGamma = new GSliderArrangement("Gamma:", 1, 500, (int)(imageProcessingOptionsModel.getGammaValue() * 100.0), 1, 100);
      slaGamma.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            double value = slaGamma.getDValue();
            imageProcessingOptionsModel.setGammaValue(value);
         }
      });
      panel.add(slaGamma);
      final GSliderArrangement slaSharpen = new GSliderArrangement("Sharpen:", 0, 100, (int)(imageProcessingOptionsModel.getSharpenValue() * 100.0), 1, 100);
      slaSharpen.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            double value = slaSharpen.getDValue();
            imageProcessingOptionsModel.setSharpenValue(value);
         }
      });
      panel.add(slaSharpen);
      final JComboBox chRotate = new JComboBox<>(Rotation.values());
      chRotate.setRenderer(new ObjectUiListCellRenderer(new RotationUi()));
      chRotate.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            Rotation selectedItem = (Rotation)chRotate.getSelectedItem();
            optionsModel.getImageProcessingOptionsModel().setRotation(selectedItem);
         }
      });
      panel.add(new IDialogComponent() {
         @Override
         public int getColumnCount() {
            return 2;
         }

         @Override
         public void fillInto(JPanel container, int columnCount) {
            container.add(new JLabel("Rotate:"), GridDialogLayoutData.RIGHT);
            GridDialogLayoutData data = new GridDialogLayoutData();
            data.setHorizontalSpan(columnCount - 1);
            container.add(chRotate, data);
         }
      });
      imageProcessingOptionsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            slaHighlight.setValue(imageProcessingOptionsModel.getHighlightValue());
            slaGamma.setValue((int)(imageProcessingOptionsModel.getGammaValue() * 100.0));
            slaShadow.setValue(imageProcessingOptionsModel.getShadowValue());
            slaSharpen.setValue((int)(imageProcessingOptionsModel.getSharpenValue() * 100.0));
            chRotate.setSelectedItem(imageProcessingOptionsModel.getRotation());
         }
      });
      return panel.createPanel();
   }

   private JPanel createRightPanel(final Image2AsciiOptionsModel optionsModel) {
      final Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = optionsModel.getImageProcessingOptionsModel();
      IGreyscaleDithering[] ditheringAlgorithms = DitheringAlgorithms.getAllGreyscaleDitheringAlgorithms();
      final JComboBox chDithering = new JComboBox<>(ditheringAlgorithms);
      optionsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2AsciiImageProcessingOptionsPanel.this.updateDitheringComboBoxEnabled(chDithering, optionsModel.getAlgorithm());
         }
      });
      this.updateDitheringComboBoxEnabled(chDithering, optionsModel.getAlgorithm());
      chDithering.setRenderer(new DitheringListCellRenderer());
      chDithering.setSelectedItem(imageProcessingOptionsModel.getDithering());
      chDithering.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            IGreyscaleDithering selectedItem = (IGreyscaleDithering)chDithering.getSelectedItem();
            imageProcessingOptionsModel.setDithering(selectedItem);
         }
      });
      JPanel p3 = new JPanel(new BorderLayout());
      p3.add(new JLabel("Dithering:"), "West");
      p3.add(chDithering, "Center");
      final JCheckBox cbInvert = new JCheckBox("Negative Image", imageProcessingOptionsModel.isInvert());
      cbInvert.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            imageProcessingOptionsModel.setInvert(cbInvert.isSelected());
         }
      });
      final JCheckBox cbNormalize = new JCheckBox("Normalize Histogram", imageProcessingOptionsModel.isNormalize());
      cbNormalize.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            imageProcessingOptionsModel.setNormalized(cbNormalize.isSelected());
         }
      });
      imageProcessingOptionsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            cbNormalize.setSelected(imageProcessingOptionsModel.isNormalize());
            cbInvert.setSelected(imageProcessingOptionsModel.isInvert());
         }
      });
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(this.cPreviewImage.getContent(), GridDialogLayoutData.FILL_BOTH);
      panel.add(p3);
      panel.add(cbInvert);
      panel.add(cbNormalize);
      return panel;
   }

   private void updateDitheringComboBoxEnabled(JComboBox chDithering, IImage2AsciiAlgorithm algorithm) {
      chDithering.setEnabled(algorithm != null && algorithm.isMonochromeImageRequired());
   }

   private void convertPreview(Image2AsciiSourceImageModel sourceImageModel, Image2AsciiOptionsModel optionsModel) {
      if (!sourceImageModel.isEmpty()) {
         Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = optionsModel.getImageProcessingOptionsModel();
         boolean invert = imageProcessingOptionsModel.isInvert();
         boolean normalize = imageProcessingOptionsModel.isNormalize();
         int highlight = imageProcessingOptionsModel.getHighlightValue();
         int shadow = imageProcessingOptionsModel.getShadowValue();
         double gamma = imageProcessingOptionsModel.getGammaValue();
         double sharpen = imageProcessingOptionsModel.getSharpenValue();
         IGreyscaleDithering dithering = imageProcessingOptionsModel.getDithering();
         Rotation rotate = imageProcessingOptionsModel.getRotation();
         GGreyscaleImage rawPreviewImage = sourceImageModel.getSourceImage().getRawPreviewImage();
         GGreyscaleImage gi = rawPreviewImage.convert(normalize, invert, gamma, highlight, shadow);
         if (sharpen > 0.0) {
            gi = gi.sharpen(sharpen);
         }

         IImage2AsciiAlgorithm algorithm = optionsModel.getAlgorithm();
         GImage g2 = gi;
         if (algorithm.isMonochromeImageRequired() && dithering != null) {
            g2 = dithering.dither(gi);
         }

         g2 = g2.rotate(rotate);
         this.cPreviewImage.setImage(g2);
      }
   }

   public JComponent getContent() {
      return this.content;
   }
}
