package de.jave.jave.algorithm.camel;

import de.jave.gui.CharField;
import de.jave.gui.CharacterModel;
import de.jave.gui.GSliderArrangement;
import de.jave.gui.io.AbstractSourceFilePanelConfiguration;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.ISourceFilePanelConfiguration;
import de.jave.gui.io.ImageIOUtilities;
import de.jave.gui.io.SourceFilePanel;
import de.jave.image.greyscale.GGreyscaleImage;
import de.jave.image.swing.ImagePanel;
import de.jave.lib.CharacterPlate;
import de.jave.lib.ICharacterPlateSettable;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.message.BasicMessage;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogPanelBuilder;
import net.disy.commons.swing.layout.grid.IDialogComponent;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.ui.ObjectUiListCellRenderer;
import net.disy.commons.swing.widgets.HorizontalLine;

public class CamelizerDialogPage extends AbstractDialogPage {
   private static final String TITLE = "Camelizer";
   private final ICharacterPlateSettable resultSettable;
   private final ImagePanel icPreview;
   private final ImagePanel sourceImagePanel;
   private final Camelizer camelizer;
   private final JPanel content;
   private final BooleanModel changesAppliedModel;
   private final CamelizeTextOptions textOptions = new CamelizeTextOptions();
   private final CamelizeImageOptions imageOptions = new CamelizeImageOptions();
   private final FileModel fileModel = new FileModel();

   public CamelizerDialogPage(
      CharacterPlate sourcePlate,
      ICharacterPlateSettable resultSettable,
      FileModel currentDirectoryModel,
      BooleanModel changesAppliedModel,
      File optionalImageFile
   ) {
      super("Open an image file in order to apply its shape to the current text.");
      Ensure.ensureArgumentNotNull(sourcePlate);
      Ensure.ensureArgumentNotNull(resultSettable);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(changesAppliedModel);
      this.resultSettable = resultSettable;
      this.changesAppliedModel = changesAppliedModel;
      this.camelizer = new Camelizer(sourcePlate.toString());
      final IFileChooserConfiguration fileChooserConfiguration = ImageIOUtilities.createImageOpenFileChooserConfiguration(currentDirectoryModel);
      ISourceFilePanelConfiguration configuration = new AbstractSourceFilePanelConfiguration() {
         @Override
         public String getOpenButtonToolTipText() {
            return "Open Image File";
         }

         @Override
         public IFileChooserConfiguration getFileChooserConfiguration() {
            return fileChooserConfiguration;
         }

         @Override
         public String getLabel() {
            return "Shape Image:";
         }
      };
      SourceFilePanel sourceFilePanel = new SourceFilePanel(this.fileModel, configuration) {
         @Override
         protected boolean performOpenFile(Component parentComponent, File file) {
            return CamelizerDialogPage.this.performOpen(parentComponent, file);
         }
      };
      this.sourceImagePanel = new ImagePanel();
      final CharacterModel fillCharacterModel = new CharacterModel(this.textOptions.getFillCharacter());
      final CharField cfFillCharField = new CharField(fillCharacterModel);
      fillCharacterModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            CamelizerDialogPage.this.textOptions.setFillCharacter(fillCharacterModel.getCharacter());
            CamelizerDialogPage.this.convert();
         }
      });
      int defaultValue = (int)(this.imageOptions.getBrightness() * 10.0);
      final GSliderArrangement slaBrightness = new GSliderArrangement("Brightness:", -10, 10, defaultValue, 1, 10);
      slaBrightness.getModel().addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            CamelizerDialogPage.this.imageOptions.setBrightness(slaBrightness.getDValue());
            CamelizerDialogPage.this.convert();
         }
      });
      final JCheckBox cbNegative = new JCheckBox("Negative Image", this.imageOptions.isNegative());
      cbNegative.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent e) {
            CamelizerDialogPage.this.imageOptions.setNegative(cbNegative.isSelected());
            CamelizerDialogPage.this.convert();
         }
      });
      GridDialogPanelBuilder sourceOptionsPanel = new GridDialogPanelBuilder();
      sourceOptionsPanel.add(slaBrightness);
      sourceOptionsPanel.add(new IDialogComponent() {
         @Override
         public int getColumnCount() {
            return 1;
         }

         @Override
         public void fillInto(JPanel panel, int columnCount) {
            GridDialogLayoutData data = new GridDialogLayoutData();
            data.setHorizontalSpan(columnCount);
            panel.add(cbNegative, data);
         }
      });
      this.icPreview = new ImagePanel();
      final JLabel fillCharacterLabel = new JLabel("Fill Character:");
      JLabel textFormattingLabel = new JLabel("Text Formatting:");
      final JComboBox textFormattingComboBox = new JComboBox<>(CamelizeTextPreserveMode.values());
      textFormattingComboBox.setSelectedItem(this.textOptions.getPreserveMode());
      textFormattingComboBox.setRenderer(new ObjectUiListCellRenderer(new CamelizeTextPreserveModeObjectUi()));
      textFormattingComboBox.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            CamelizerDialogPage.this.textOptions.setPreserveMode((CamelizeTextPreserveMode)textFormattingComboBox.getSelectedItem());
            CamelizerDialogPage.this.updateFillCharacterComponentEnabled(fillCharacterLabel, cfFillCharField);
            CamelizerDialogPage.this.convert();
         }
      });
      this.updateFillCharacterComponentEnabled(fillCharacterLabel, cfFillCharField);
      JPanel imagePanel = new JPanel(new GridDialogLayout(2, false, LayoutUtilities.getComponentSpacing(), 0));
      imagePanel.add(new JLabel("Original Image:"));
      imagePanel.add(new JLabel("Shape Mask:"));
      imagePanel.add(this.sourceImagePanel.getContent(), GridDialogLayoutData.FILL_BOTH);
      imagePanel.add(this.icPreview.getContent(), GridDialogLayoutData.FILL_BOTH);
      JPanel textOptionsPanel = new JPanel(new GridDialogLayout(2, false));
      textOptionsPanel.add(textFormattingLabel, GridDialogLayoutData.RIGHT);
      textOptionsPanel.add(textFormattingComboBox);
      textOptionsPanel.add(fillCharacterLabel, GridDialogLayoutData.RIGHT);
      textOptionsPanel.add(cfFillCharField);
      JPanel panel = new JPanel(new GridDialogLayout(1, false));
      panel.add(sourceFilePanel.createPanel(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(new HorizontalLine(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(sourceOptionsPanel.createPanel());
      panel.add(imagePanel, GridDialogLayoutData.FILL_BOTH);
      panel.add(new HorizontalLine(), GridDialogLayoutData.FILL_HORIZONTAL);
      panel.add(textOptionsPanel, GridDialogLayoutData.FILL_HORIZONTAL);
      this.content = panel;
      if (optionalImageFile != null) {
         this.performOpen(panel, optionalImageFile);
      }
   }

   private void updateFillCharacterComponentEnabled(JLabel label, CharField field) {
      boolean enabled = this.textOptions.getPreserveMode() == CamelizeTextPreserveMode.PRESERVE_WORDS;
      label.setEnabled(enabled);
      field.setEnabled(enabled);
   }

   boolean performOpen(Component parentComponent, File file) {
      Image loadedImage = null;

      try {
         loadedImage = ImageIO.read(file);
      } catch (RuntimeException var5) {
         MessageDialogFactory.showMessageDialog(
            parentComponent,
            new Message(
               "Camelizer", "Unknown error loading image. Wrong file format?\nSupported formats: GIF, JPG, BMP (experimental).", MessageType.ERROR, var5
            )
         );
         return false;
      } catch (IOException var6) {
         MessageDialogFactory.showMessageDialog(parentComponent, new Message("Camelizer", "Error loading image.", MessageType.ERROR, var6));
         return false;
      }

      this.fileModel.setValue(file);
      GGreyscaleImage image = Camelizer.loadImage(loadedImage, parentComponent);
      this.camelizer.setRawImage(image);
      this.sourceImagePanel.setImage(image);
      this.checkInputValid();
      this.convert();
      return true;
   }

   protected void convert() {
      if (this.camelizer.checkReady()) {
         CharacterPlate result = this.camelizer.camelize(this.imageOptions, this.textOptions);
         this.icPreview.setImage(this.camelizer.getPreviewImage());
         this.resultSettable.setCharacterPlate(result);
         this.changesAppliedModel.setValue(true);
      }
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return !this.camelizer.checkReady() ? new BasicMessage("", MessageType.ERROR) : this.getDefaultMessage();
   }

   @Override
   public JComponent createContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "Camelizer";
   }
}
