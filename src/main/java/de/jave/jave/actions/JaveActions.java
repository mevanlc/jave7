package de.jave.jave.actions;

import de.jave.figlet.engine.IFigDriver;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.image2ascii.IImage2AsciiResultTaker;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.export.ExportTextAction;
import de.jave.jave.actions.export.TextExportPreferences;
import de.jave.jave.actions.quickstart.QuickStartAction;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.jave.watermark.WatermarkImageFile;
import de.jave.lib.CharacterPlate;
import de.jave.preferences.JavePreferences;
import java.awt.Component;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class JaveActions {
   private final SmartAction newDocumentAction;
   private final SmartAction browseAction;
   private final SmartAction openAction;
   private final SmartAction aboutAction;
   private final CamelizerAction camelizerAction;
   private final SmartAction fractalAction;
   private final SmartAction newAnimationAction;
   private final Image2AsciiAction image2AsciiAction;
   private final SmartToggleAction gridToggleAction;
   private final SmartToggleAction markIllegalToggleAction;
   private final SmartToggleAction connectedLinesViewToggleAction;
   private final SmartToggleAction rulerToggleAction;
   private final SmartToggleAction watermarkVisibilityToggleAction;
   private final SmartToggleAction auxLinesVisibilityToggleAction;
   private final ShowFigletEditorAction figletAction;
   private final ResizeDocumentAction resizeAction;
   private final ClipartLibraryAction clipartLibraryAction;
   private final SmartAction mathematicalExpressionsAction;
   private final SmartAction textBoxAction;
   private final SmartAction saveAction;
   private final SmartAction saveAllAction;
   private final SmartAction saveAsAction;
   private final SmartAction quickStartAction;
   private final SmartAction exportAction;
   private final SmartAction copyAction;
   private final SmartAction cutAction;
   private final SmartAction pasteAsNewSelectionAction;
   private final SmartAction pasteAsNewDocumentAction;

   public JaveActions(
      @Deprecated final JavEApplication jave,
      ConfigurationList configurationList,
      JavePreferences preferences,
      PlatePreferences plateViewOptions,
      FontModel displayFontModel,
      ObjectModel<ColorScheme> defaultColorSchemeModel
   ) {
      Ensure.ensureArgumentNotNull(configurationList);
      Ensure.ensureArgumentNotNull(displayFontModel);
      Ensure.ensureArgumentNotNull(defaultColorSchemeModel);
      Ensure.ensureArgumentNotNull(preferences);
      FileModel currentDirectoryModel = jave.getApplicationPreferences().getCurrectDirectoryModel();
      JaveMainPanel mainPanel = jave.getMainPanel();
      this.copyAction = new CopyAction(mainPanel);
      this.cutAction = new CutAction(mainPanel);
      this.pasteAsNewSelectionAction = new PasteAsNewSelectionAction(mainPanel, jave);
      this.pasteAsNewDocumentAction = new PasteAsNewDocumentAction(mainPanel, jave);
      JaveApplicationPreferences applicationPreferences = jave.getApplicationPreferences();
      TextExportPreferences textExportPreferences = new TextExportPreferences(applicationPreferences);
      this.exportAction = new ExportTextAction(mainPanel, textExportPreferences, currentDirectoryModel, displayFontModel);
      this.quickStartAction = new QuickStartAction(jave);
      this.saveAction = new SaveAction(jave, mainPanel);
      this.resizeAction = new ResizeDocumentAction(jave);
      this.newDocumentAction = new SmartAction("New Document", JaveIcons.NEW_DOCUMENT_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            jave.doNew();
         }
      };
      this.newDocumentAction.setToolTipText("Create a new Document");
      this.newDocumentAction.setAcceleratorKey(JaveKeyBindings.NEW_DOCUMENT);
      AsciiGradientConfiguration gradientConfiguration = configurationList.getRequired(AsciiGradientConfiguration.class);
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration = configurationList.getRequired(AsciiGreyscaleTableConfiguration.class);
      IImage2AsciiResultTaker resultTaker = new IImage2AsciiResultTaker() {
         @Override
         public void takeImage2AsciiResult(CharacterPlate characterPlate, WatermarkImageFile imageFile) {
            jave.pasteAsNewDocument(characterPlate);
            if (imageFile != null) {
               jave.setWatermarkImage(imageFile);
            }
         }
      };
      Filter filter = configurationList.getRequired(Filter.class);
      this.image2AsciiAction = new Image2AsciiAction(
         resultTaker,
         mainPanel,
         currentDirectoryModel,
         displayFontModel,
         defaultColorSchemeModel,
         new AnimationExportPreferences(preferences),
         gradientConfiguration,
         greyscaleTableConfiguration,
         filter
      );
      IFigDriver figDriver = configurationList.getRequired(IFigDriver.class);
      this.figletAction = new ShowFigletEditorAction(figDriver, jave, mainPanel, preferences);
      this.browseAction = new SmartAction("Browse...", JaveIcons.BROWSE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            jave.doBrowse();
         }
      };
      this.browseAction.setToolTipText("Browse ASCII Files");
      this.browseAction.setAcceleratorKey(JaveKeyBindings.BROWSE);
      this.openAction = new SmartAction("Open...", JaveIcons.OPEN_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            jave.doOpen(parentComponent);
         }
      };
      this.openAction.setToolTipText("Open an ASCII File");
      this.openAction.setAcceleratorKey(JaveKeyBindings.OPEN);
      this.aboutAction = new SmartAction("About JavE", JaveIcons.JAVE_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            jave.showAboutDialog();
         }
      };
      this.aboutAction.setToolTipText("About JavE");
      this.camelizerAction = new CamelizerAction(mainPanel, currentDirectoryModel);
      this.fractalAction = new SmartAction("Fractals", JaveIcons.FRACTAL_ICON) {
         @Override
         protected void execute(Component parentComponent) {
            jave.doFractal();
         }
      };
      this.newAnimationAction = new NewAnimationAction(jave);
      this.gridToggleAction = new SmartToggleAction(plateViewOptions.getGridVisibilityModel(), "Grid", JaveIcons.GRID_VISIBLE_ICON);
      this.gridToggleAction.setAcceleratorKey(JaveKeyBindings.TOGGLE_GRID);
      this.markIllegalToggleAction = new SmartToggleAction(plateViewOptions.getMarkIllegalModel(), "Mark illegal characters", JaveIcons.PURE_ASCII_ICON);
      this.connectedLinesViewToggleAction = new SmartToggleAction(
         plateViewOptions.getConnectedLinesViewModel(), "Connected Lines View", JaveIcons.CONNECTED_LINES_VIEW_ICON
      );
      this.rulerToggleAction = new SmartToggleAction(plateViewOptions.getRulerModel(), "Rulers", JaveIcons.RULER_ICON);
      this.watermarkVisibilityToggleAction = new SmartToggleAction(
         jave.getWatermarkVisibilityModel(), "Show Watermark", JaveIcons.TOOL_WATERMARK_ICON
      );
      this.auxLinesVisibilityToggleAction = new SmartToggleAction(
         jave.getAuxLinesVisibilityModel(), "Show Auxiliary Lines", JaveIcons.TOOL_AUXILIARY_LINES_ICON
      );
      this.clipartLibraryAction = new ClipartLibraryAction(jave, mainPanel);
      this.mathematicalExpressionsAction = new MathematicalExpressionsAction(jave, mainPanel);
      this.textBoxAction = new TextBoxAction(jave, mainPanel);
      this.saveAllAction = new SaveAllAction(jave, mainPanel);
      this.saveAsAction = new SaveAsAction(jave, mainPanel);
   }

   public SmartAction getExportAction() {
      return this.exportAction;
   }

   public SmartAction getNewDocumentAction() {
      return this.newDocumentAction;
   }

   public SmartAction getBrowseAction() {
      return this.browseAction;
   }

   public SmartAction getOpenAction() {
      return this.openAction;
   }

   public SmartAction getAboutAction() {
      return this.aboutAction;
   }

   public CamelizerAction getCamelizerAction() {
      return this.camelizerAction;
   }

   public SmartAction getNewAnimationAction() {
      return this.newAnimationAction;
   }

   public SmartAction getFractalAction() {
      return this.fractalAction;
   }

   public SmartToggleAction getGridToggleAction() {
      return this.gridToggleAction;
   }

   public SmartToggleAction getMarkIllegalToggleAction() {
      return this.markIllegalToggleAction;
   }

   public SmartToggleAction getConnectedLinesViewToggleAction() {
      return this.connectedLinesViewToggleAction;
   }

   public SmartToggleAction getWatermarkVisibilityToggleAction() {
      return this.watermarkVisibilityToggleAction;
   }

   public SmartToggleAction getAuxLinesVisibilityToggleAction() {
      return this.auxLinesVisibilityToggleAction;
   }

   public SmartToggleAction getRulerToggleAction() {
      return this.rulerToggleAction;
   }

   public Image2AsciiAction getImage2AsciiAction() {
      return this.image2AsciiAction;
   }

   public ShowFigletEditorAction getFigletAction() {
      return this.figletAction;
   }

   public ResizeDocumentAction getResizeAction() {
      return this.resizeAction;
   }

   public ClipartLibraryAction getClipartLibraryAction() {
      return this.clipartLibraryAction;
   }

   public SmartAction getMathematicalExpressionsAction() {
      return this.mathematicalExpressionsAction;
   }

   public SmartAction getTextBoxAction() {
      return this.textBoxAction;
   }

   public SmartAction getSaveAction() {
      return this.saveAction;
   }

   public SmartAction getSaveAllAction() {
      return this.saveAllAction;
   }

   public SmartAction getSaveAsAction() {
      return this.saveAsAction;
   }

   public SmartAction getQuickStartAction() {
      return this.quickStartAction;
   }

   public SmartAction getCopyAction() {
      return this.copyAction;
   }

   public SmartAction getCutAction() {
      return this.cutAction;
   }

   public SmartAction getPasteAsNewSelectionAction() {
      return this.pasteAsNewSelectionAction;
   }

   public SmartAction getPasteAsNewDocumentAction() {
      return this.pasteAsNewDocumentAction;
   }
}
