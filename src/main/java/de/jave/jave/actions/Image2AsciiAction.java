package de.jave.jave.actions;

import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.IImage2AsciiResultTaker;
import de.jave.image2ascii.Image2AsciiIcons;
import de.jave.jave.Image2AsciiDialog;
import de.jave.jave.actions.enablestrategy.AlwaysEnabledStrategy;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.filter.Filter;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import java.awt.Component;
import java.io.File;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class Image2AsciiAction extends AbstractJaveAction {
   private Image2AsciiDialog image2AsciiDialog;
   private final IImage2AsciiResultTaker resultTaker;
   private final FontModel displayFontModel;
   private final FileModel currentDirectoryModel;
   private final AnimationExportPreferences animationExportPreferences;
   private final ObjectModel<ColorScheme> defaultColorSchemeModel;
   private final ObjectModel<ColorScheme> colorSchemeModel;
   private final AsciiGradientConfiguration gradientConfiguration;
   private final AsciiGreyscaleTableConfiguration greyscaleTableConfiguration;
   private final Filter filter;

   public Image2AsciiAction(
      IImage2AsciiResultTaker resultTaker,
      JaveMainPanel mainPanel,
      FileModel currentDirectoryModel,
      FontModel displayFontModel,
      ObjectModel<ColorScheme> colorSchemeModel,
      AnimationExportPreferences animationExportPreferences,
      AsciiGradientConfiguration gradientConfiguration,
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration,
      Filter filter
   ) {
      super(mainPanel, "Image2Ascii Converter", Image2AsciiIcons.IMAGE2ASCII_ICON);
      Ensure.ensureArgumentNotNull(resultTaker);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(colorSchemeModel);
      Ensure.ensureArgumentNotNull(animationExportPreferences);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      Ensure.ensureArgumentNotNull(greyscaleTableConfiguration);
      Ensure.ensureArgumentNotNull(filter);
      this.defaultColorSchemeModel = colorSchemeModel;
      this.animationExportPreferences = animationExportPreferences;
      this.currentDirectoryModel = currentDirectoryModel;
      this.displayFontModel = displayFontModel;
      this.resultTaker = resultTaker;
      this.colorSchemeModel = new ObjectModel<>(this.defaultColorSchemeModel.getValue());
      this.gradientConfiguration = gradientConfiguration;
      this.greyscaleTableConfiguration = greyscaleTableConfiguration;
      this.filter = filter;
      this.setToolTipText("Show Image to Ascii Conversion Dialog");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return AlwaysEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      ColorScheme colorScheme = editor == null ? null : editor.getPlate().getDocument().getColorScheme();
      this.execute(parentComponent, null, colorScheme);
   }

   public void execute(Component parentComponent, File optionalFile, ColorScheme optionalColorScheme) {
      this.colorSchemeModel.setValue(optionalColorScheme != null ? optionalColorScheme : this.defaultColorSchemeModel.getValue());
      if (this.image2AsciiDialog == null) {
         this.image2AsciiDialog = new Image2AsciiDialog(
            parentComponent,
            this.resultTaker,
            this.currentDirectoryModel,
            this.displayFontModel,
            this.animationExportPreferences,
            this.colorSchemeModel,
            this.gradientConfiguration,
            this.greyscaleTableConfiguration,
            this.filter
         );
      }

      if (optionalFile == null) {
         this.image2AsciiDialog.show();
      } else {
         boolean success = this.image2AsciiDialog.open(optionalFile);
         if (success) {
            this.image2AsciiDialog.show();
         }
      }
   }
}
