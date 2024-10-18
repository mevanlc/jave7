package de.jave.image2ascii;

import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.braille.BrailleDisplay;
import de.jave.image.Rotation;
import de.jave.image.greyscale.algorithm.dithering.IGreyscaleDithering;
import de.jave.image2ascii.dialog.Image2AsciiConversionOptionsPanel;
import de.jave.image2ascii.dialog.Image2AsciiImageProcessingOptionsPanel;
import de.jave.image2ascii.dialog.Image2AsciiOutputOptionsPanel;
import de.jave.image2ascii.dialog.Image2AsciiSourceImagePanel;
import de.jave.image2ascii.model.Image2AsciiImageProcessingOptionsModel;
import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.asynchronous.IJobProcessor;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.progress.ProgressMonitorBar;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.cardlayout.CardPanel;
import net.disy.commons.swing.layout.cardlayout.CardPanelKey;

public class Image2Texter {
   public static final CardPanelKey CARD_PANEL_KEY_BRAILLE = new CardPanelKey("braille");
   public static final CardPanelKey CARD_PANEL_KEY_TEXT = new CardPanelKey("text");
   public static final String TITLE = "Image2Ascii";
   private JPanel panelOutput;
   private final ProgressMonitorBar progressBar = new ProgressMonitorBar();
   private final AsciiTextArea taOutput;
   private final BrailleDisplay brailleDisplay = new BrailleDisplay(" ");
   private final JComponent content;
   private final Image2AsciiSourceImageModel sourceImageModel;
   private final Image2AsciiOptionsModel optionsModel;
   private final CardPanel outputPanel = new CardPanel();
   private final JTabbedPane tabbedPane;
   private CharacterPlate resultPlate;
   private ProxyProgressMonitor currentProgressMonitor;
   private final AsynchronousDroppingJobProcessor<ConversionJob> conversionProcessor = new AsynchronousDroppingJobProcessor<>(
      new IJobProcessor<ConversionJob>() {
         public void process(ICancelable cancelable, ConversionJob job) throws InterruptedException {
            CharacterPlate plate = job.run(cancelable);
            Image2Texter.this.setResult(plate);
         }
      }, new IExceptionHandler() {
         @Override
         public void handle(Throwable exception) {
            MessageDialogFactory.showMessageDialog(Image2Texter.this.content, new Message("Error converting image.", exception));
         }
      }
   );

   public Image2Texter(
      Image2AsciiSourceImageModel sourceImageModel,
      Image2AsciiOptionsModel optionsModel,
      FontModel displayFontModel,
      final ObjectModel<ColorScheme> colorSchemeModel,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      Ensure.ensureArgumentNotNull(sourceImageModel);
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.sourceImageModel = sourceImageModel;
      this.optionsModel = optionsModel;
      sourceImageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2Texter.this.handleChanged();
         }
      });
      final AsciiTextAreaProperties properties = new AsciiTextAreaProperties(displayFontModel);
      properties.setEditable(false);
      this.taOutput = new AsciiTextArea(new Dimension(80, 15), properties);
      colorSchemeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2Texter.updateColorScheme(colorSchemeModel, properties);
         }
      });
      updateColorScheme(colorSchemeModel, properties);
      JComponent sourceImagePanel = new Image2AsciiSourceImagePanel(sourceImageModel).getContent();
      JComponent imageProcessingPanel = new Image2AsciiImageProcessingOptionsPanel(sourceImageModel, optionsModel).getContent();
      JComponent conversionOptionsPanel = new Image2AsciiConversionOptionsPanel(
            optionsModel, displayFontModel, this.outputPanel, this.brailleDisplay, gradientConfiguration, greyscaleTableConfiguration, filter
         )
         .getContent();
      JComponent outputOptionsPanel = new Image2AsciiOutputOptionsPanel(optionsModel.getOutputOptionsModel()).getContent();
      this.tabbedPane = new JTabbedPane();
      this.tabbedPane.add("Source image", addEmptyBorder(sourceImagePanel));
      this.tabbedPane.add("Image processing", addEmptyBorder(imageProcessingPanel));
      this.tabbedPane.add("Conversion", addEmptyBorder(conversionOptionsPanel));
      this.tabbedPane.add("Output", addEmptyBorder(outputOptionsPanel));
      sourceImageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2Texter.this.updateTabbedPaneEnabled();
         }
      });
      this.updateTabbedPaneEnabled();
      JPanel mainPanel = new JPanel(new BorderLayout());
      mainPanel.add(this.tabbedPane, "North");
      mainPanel.add(this.createOutputPanel(), "Center");
      mainPanel.add(this.progressBar, "South");
      this.content = mainPanel;
      optionsModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            Image2Texter.this.handleChanged();
         }
      });
   }

   private static void updateColorScheme(ObjectModel<ColorScheme> colorSchemeModel, AsciiTextAreaProperties properties) {
      ColorScheme scheme = colorSchemeModel.getValue();
      properties.setBackground(scheme.getColorPlateBackground());
      properties.setForeground(scheme.getColorText());
   }

   private void updateTabbedPaneEnabled() {
      boolean enabled = !this.sourceImageModel.isEmpty();

      for (int i = 1; i < this.tabbedPane.getTabCount(); i++) {
         this.tabbedPane.setEnabledAt(i, enabled);
      }
   }

   private static JComponent addEmptyBorder(JComponent panelContent) {
      JPanel panel = new JPanel(new GridLayout(1, 0));
      panel.add(panelContent);
      panel.setBorder(new EmptyBorder(4, 4, 4, 4));
      return panel;
   }

   private JPanel createOutputPanel() {
      this.panelOutput = new JPanel(new BorderLayout());
      this.panelOutput.add(new JLabel("Result:"), "North");
      this.outputPanel.add(this.taOutput.getContent(), CARD_PANEL_KEY_TEXT);
      this.outputPanel.add(this.brailleDisplay.getContent(), CARD_PANEL_KEY_BRAILLE);
      this.outputPanel.setSelectedSubPanel(CARD_PANEL_KEY_TEXT);
      this.panelOutput.add(this.outputPanel.getContent(), "Center");
      return this.panelOutput;
   }

   public CharacterPlate getResult() {
      return this.resultPlate;
   }

   private void setResult(CharacterPlate resultPlate) {
      this.resultPlate = resultPlate;
      if (resultPlate != null) {
         this.taOutput.setText(resultPlate.toString());
      } else {
         this.taOutput.setText("");
      }
   }

   private void convert2Text() {
      if (!this.sourceImageModel.isEmpty()) {
         Image2AsciiImageProcessingOptionsModel imageProcessingOptionsModel = this.optionsModel.getImageProcessingOptionsModel();
         double shapeFactor = this.optionsModel.getOutputOptionsModel().getShapeFactor();
         boolean invert = imageProcessingOptionsModel.isInvert();
         boolean normalize = imageProcessingOptionsModel.isNormalize();
         int highlight = imageProcessingOptionsModel.getHighlightValue();
         int shadow = imageProcessingOptionsModel.getShadowValue();
         double gamma = imageProcessingOptionsModel.getGammaValue();
         double sharpen = imageProcessingOptionsModel.getSharpenValue();
         int newWidth = this.optionsModel.getOutputOptionsModel().getOutputWidth();
         IGreyscaleDithering dithering = imageProcessingOptionsModel.getDithering();
         Rotation rotate = imageProcessingOptionsModel.getRotation();
         if (this.currentProgressMonitor != null) {
            this.currentProgressMonitor.setCanceled(true);
         }

         this.currentProgressMonitor = new ProxyProgressMonitor(this.progressBar);
         ConversionJob conversionThread = new ConversionJob(
            this.currentProgressMonitor,
            this.sourceImageModel.getSourceImage().getRawConversionImage(),
            newWidth,
            shapeFactor,
            normalize,
            invert,
            highlight,
            shadow,
            gamma,
            sharpen,
            dithering,
            rotate,
            this.optionsModel.getAlgorithm()
         );
         this.conversionProcessor.startJob(conversionThread);
      }
   }

   private void handleChanged() {
      this.convert2Text();
      this.content.validate();
   }

   public Component getContent() {
      return this.content;
   }
}
