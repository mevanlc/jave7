package de.jave.figlet.swing.application;

import de.jave.ascii.plate.ITextContentListener;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.figlet.IFigLayoutListener;
import de.jave.figlet.IFontSelectionListener;
import de.jave.figlet.engine.IFigDriver;
import de.jave.figlet.engine.layout.HorizontalAlignment;
import de.jave.figlet.engine.primitives.FigFont;
import de.jave.figlet.engine.primitives.FigLayout;
import de.jave.figlet.engine.processing.FigletJobFactory;
import de.jave.figlet.engine.processing.IFigletJob;
import de.jave.figlet.file.IFigFontCategory;
import de.jave.figlet.swing.fontchooser.FontChooserView;
import de.jave.figlet.swing.layout.FigLayoutPanel;
import de.jave.figlet.swing.preferences.JFigletPreferences;
import de.jave.figlet.swing.ui.FigletIcons;
import de.jave.figlet.util.FigException;
import de.jave.figlet.util.FigUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.asynchronous.IJobProcessor;
import net.disy.commons.core.exception.PrintStackTraceExceptionHandler;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class JFigletEditor {
   private final AsciiTextArea taOutput;
   private final AsciiTextArea taInput;
   private final AsynchronousDroppingJobProcessor<IFigletJob> processor;
   private final FontChooserView fontChooser;
   private final FigLayoutPanel layoutPanel;
   private JTextField statusBar;
   private DimensionTextField sizeBar;
   private final IFigDriver figDriver;
   private final JComponent content;
   private final JFigletPreferences preferences;

   public JFigletEditor(final IFigDriver figDriver, FontModel displayFontModel, JFigletPreferences preferences, File tmpFile) {
      Ensure.ensureArgumentNotNull(figDriver);
      Ensure.ensureArgumentNotNull(preferences);
      Ensure.ensureArgumentNotNull(tmpFile);
      this.figDriver = figDriver;
      this.preferences = preferences;
      this.processor = new AsynchronousDroppingJobProcessor<>(new IJobProcessor<IFigletJob>() {
         public void process(ICancelable cancelable, IFigletJob jobObject) throws InterruptedException {
            try {
               String result = figDriver.figletize(jobObject);
               JFigletEditor.this.taOutput.setText(result);
            } catch (FigException var4) {
               var4.printStackTrace();
            }
         }
      }, new PrintStackTraceExceptionHandler());
      this.fontChooser = new FontChooserView(figDriver.getFileLibrary().getFontCategorization(), figDriver, tmpFile);
      this.fontChooser.addFontSelectionListener(new IFontSelectionListener() {
         @Override
         public void fontSelectionChanged() {
            JFigletEditor.this.handleFontSelectionChanged();
         }
      });
      AsciiTextAreaProperties taInputProperties = new AsciiTextAreaProperties(displayFontModel);
      this.taInput = new AsciiTextArea(new Dimension(80, 3), taInputProperties);
      this.taInput.setToolTipText("Text to be converted");
      this.taInput.setText("FIGlet");
      this.taInput.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            JFigletEditor.this.figletize();
         }
      });
      AsciiTextAreaProperties taOutputProperties = new AsciiTextAreaProperties(displayFontModel);
      taOutputProperties.setEditable(false);
      taOutputProperties.getRulerProperties().setHorizontalRulerVisible(true);
      taOutputProperties.getRulerProperties().setPrintMarginColumnVisible(true);
      this.taOutput = new AsciiTextArea(new Dimension(80, 13), taOutputProperties);
      this.taOutput.setToolTipText("Conversion output");
      this.layoutPanel = new FigLayoutPanel();
      this.layoutPanel.addLayoutListener(new IFigLayoutListener() {
         @Override
         public void layoutChanged() {
            JFigletEditor.this.figletize();
         }
      });
      JSplitPane mainSplitPane = new JSplitPane(0, this.taInput.getContent(), this.taOutput.getContent());
      mainSplitPane.setBorder(null);
      mainSplitPane.setContinuousLayout(true);
      JPanel mainContent = new JPanel(new BorderLayout(4, 4));
      mainContent.add(this.fontChooser.getContent(), "North");
      mainContent.add(mainSplitPane, "Center");
      JPanel panel = new JPanel();
      panel.setLayout(new BorderLayout(4, 4));
      panel.add(this.layoutPanel.getContent(), "North");
      panel.add(mainContent, "Center");
      panel.add(this.createStatusBar(), "South");
      this.taInput.selectAll();
      this.taInput.requestFocus();
      this.taOutput.addTextContentListener(new ITextContentListener() {
         @Override
         public void textContentChanged() {
            JFigletEditor.this.updateSizeBar();
         }
      });
      this.setStatus(figDriver.getFileLibrary().getAllFontNames().length + " fonts available.");
      this.updateSizeBar();
      this.handleFontSelectionChanged();
      this.content = panel;
      this.readPreferences();
   }

   public List<? extends Image> getIcon() {
      return FigletIcons.FIGLET_ICON_IMAGES;
   }

   public JComponent getContent() {
      return this.content;
   }

   private void readPreferences() {
      String categoryName = this.preferences.getFigFontCategoryName();
      if (categoryName != null) {
         IFigFontCategory category = FigUtilities.getCategory(this.figDriver.getFileLibrary().getFontCategorization(), categoryName);
         if (category != null) {
            this.fontChooser.setSelectedCategory(category);
         }
      }

      String fontName = this.preferences.getFigFontName();
      if (fontName != null) {
         try {
            FigFont font = this.figDriver.getFont(fontName);
            if (font != null) {
               this.fontChooser.setSelectedFont(font);
            }
         } catch (FigException var4) {
         }
      }
   }

   public void savePreferences() {
      FigFont font = this.getSelectedFont();
      this.preferences.setFigFontName(font == null ? null : font.getName());
      IFigFontCategory category = this.fontChooser.getSelectedFontCategory();
      this.preferences.setFigFontCategoryName(category == null ? null : category.getName());
      this.preferences.flush();
   }

   private void updateSizeBar() {
      Dimension dimension = this.taOutput.getTextSize();
      this.sizeBar.setDimension(dimension);
   }

   private JComponent createStatusBar() {
      this.statusBar = new JTextField();
      this.statusBar.setEditable(false);
      this.sizeBar = new DimensionTextField();
      JPanel panel = new JPanel(new BorderLayout());
      panel.add(this.statusBar, "Center");
      panel.add(this.sizeBar.getContent(), "East");
      return panel;
   }

   private void setStatus(String message) {
      this.statusBar.setText(message);
   }

   private void handleFontSelectionChanged() {
      this.layoutPanel.setLayout(this.getSelectedFont().getLayout());
      this.figletize();
   }

   private FigFont getSelectedFont() {
      return this.fontChooser.getSelectedFont();
   }

   protected void figletize() {
      String text = this.taInput.getText();
      FigFont selectedFont = this.getSelectedFont();
      FigLayout layout = this.layoutPanel.getLayout();
      HorizontalAlignment alignment = this.layoutPanel.getAlignment();
      this.processor.startJob(FigletJobFactory.createJob(text, selectedFont, layout, alignment));
   }

   public String getResultText() {
      return this.taOutput.getText();
   }

   public void requestFocus() {
      this.taInput.requestFocus();
   }
}
