package de.jave.jave.actions;

import de.jave.ascii.font.ChooseDisplayFontAction;
import de.jave.ascii.plate.textareabased.AsciiTextArea;
import de.jave.ascii.plate.textareabased.AsciiTextAreaProperties;
import de.jave.asciimation.action.AddNewFrameAction;
import de.jave.asciimation.action.AnimationEditorPropertiesAction;
import de.jave.asciimation.action.DeleteFrameAction;
import de.jave.asciimation.action.DuplicateFrameAction;
import de.jave.asciimation.action.ExportAnimationEditorAction;
import de.jave.asciimation.action.OpenAnimationAction;
import de.jave.figlet.engine.IFigDriver;
import de.jave.image2ascii.AsciiGreyscaleTableConfiguration;
import de.jave.jave.CharSetsConfiguration;
import de.jave.jave.CharacterSets;
import de.jave.jave.JavEApplication;
import de.jave.jave.JaveGlobalRessources;
import de.jave.jave.JaveMessages;
import de.jave.jave.PlateDocument;
import de.jave.jave.actions.fileimport.JaveImportAction;
import de.jave.jave.actions.preferences.JavePreferencesAction;
import de.jave.jave.algorithm.AlignLeft;
import de.jave.jave.algorithm.AlignRight;
import de.jave.jave.algorithm.AsciiAntialiasing;
import de.jave.jave.algorithm.Asciify;
import de.jave.jave.algorithm.Brightness;
import de.jave.jave.algorithm.Center;
import de.jave.jave.algorithm.CenterByTheLine;
import de.jave.jave.algorithm.Clear;
import de.jave.jave.algorithm.CompressExpand;
import de.jave.jave.algorithm.FlipDynamicAction;
import de.jave.jave.algorithm.FlipStatic;
import de.jave.jave.algorithm.GeneralAlgorithmConfiguration;
import de.jave.jave.algorithm.Invert;
import de.jave.jave.algorithm.JaveAlgorithm;
import de.jave.jave.algorithm.JaveAlgorithmAction;
import de.jave.jave.algorithm.LowerCase;
import de.jave.jave.algorithm.MirrorDynamicAction;
import de.jave.jave.algorithm.MirrorStatic;
import de.jave.jave.algorithm.RepairWrapped;
import de.jave.jave.algorithm.Rot13;
import de.jave.jave.algorithm.Rotate180Action;
import de.jave.jave.algorithm.Rotate180StaticAction;
import de.jave.jave.algorithm.Rotate90LeftAction;
import de.jave.jave.algorithm.Rotate90LeftStaticAction;
import de.jave.jave.algorithm.Rotate90RightAction;
import de.jave.jave.algorithm.Rotate90RightStaticAction;
import de.jave.jave.algorithm.ShakeLines;
import de.jave.jave.algorithm.Unasciify;
import de.jave.jave.algorithm.UpperCase;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.algorithm.repair.AsciiRepairAlgorithmConfiguration;
import de.jave.jave.algorithm.replaceillegal.AsciiReplaceIllegalConfiguration;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.menu.MenuHub;
import de.jave.jave.menu.MenuSearchController;
import de.jave.jave.menu.MenuSearchHistory;
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.TextDocumentEditor;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.lib.gui.GuiUtilities;
import de.jave.lib.gui.IStatusDisplay;
import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import de.jave.util.RecentFileList;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Consumer;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import net.dizzy.commons.core.io.FileDisplayNameUtilities;
import net.dizzy.commons.core.message.IBasicMessage;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.AbstractDizzyAction;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.dizzy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.dizzy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.icon.IBaseIconProvider;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.menu.HelpImplementedMenuBar;

public class JaveMenuBar extends HelpImplementedMenuBar {
   private final JavEApplication application;
   private final MenuHub hub = new MenuHub(this);
   private final RecentFileList recentFileList;
   private final JMenu menuRecent;
   private final Map<IDocumentEditor, String> unsavedWindowIds = new WeakHashMap<>();
   private final JaveApplicationPreferences preferences;
   private final JMenu menuWindows;
   private final JMenu menuColor;
   private final JMenu menuView;
   private final JMenu menuModify;
   private final JMenu menuLayer;
   private final DeleteLayerAction deleteLayerAction;
   private final DuplicateLayerAction duplicateLayerAction;
   private final JMenuItem miToggleLayerVisibility;
   private final JCheckBoxMenuItem miLayerOpaque;
   private final JCheckBoxMenuItem miLayersPanel;
   private final JMenu menuSelection;
   private JMenu menuCharacterSets;
   private final JMenuItem miRevert;
   private final JMenuItem miClose;
   private final JMenuItem miCloseAll;
   private final JMenuItem miSelectAll;
   private final JMenuItem miSelectConnected;
   private final JMenuItem miClear;
   private final JMenuItem miReplace;
   private final JMenuItem miRender3D;
   private final JMenuItem miFunctionPlotter;
   private final JMenuItem miDoc2Watermark;
   private JMenuItem miNextWindow;
   private JMenuItem miPreviousWindow;
   private final JCheckBoxMenuItem[] miColorSchemes;
   private JCheckBoxMenuItem[] miCharacters;
   private List<WindowTarget> windowTargets;
   private record WindowTarget(IDocumentEditor editor, String label, String id, boolean persistent) {}
   private final JMenuItem miSelectionToBrush;
   private final JMenuItem miSelectionShrink;
   private final JMenuItem miSelectionExpand;
   private final JMenuItem miSelectionDelete;
   private final List<JMenuItem> docOpenEnabledMenuItems = new ArrayList<>();
   private SmartAction pasteIntoSelectionAction;
   private static final int[] windowShortcuts = new int[]{49, 50, 51, 52, 53, 54, 55, 56, 57, 48};

   public JaveMenuBar(
      final JavEApplication application,
      ConfigurationList configurationList,
      JavePreferences javePreferences,
      final JaveMainPanel mainPanel,
      JaveActions actions,
      RecentFileList recentFileList,
      JaveApplicationPreferences preferences,
      PlatePreferences platePreferences,
      final CharacterSets characterSets,
      UndoRedoModel undoRedoModel
   ) {
      Ensure.ensureArgumentNotNull(platePreferences);
      Ensure.ensureArgumentNotNull(configurationList);
      Ensure.ensureArgumentNotNull(preferences);
      Ensure.ensureArgumentNotNull(characterSets);
      final CurrentColorSchemeModel currentColorSchemeModel = new CurrentColorSchemeModel(mainPanel.getActiveEditorModel());
      currentColorSchemeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveMenuBar.this.updateColorMenu(currentColorSchemeModel.getColorScheme(), mainPanel.getActiveEditorModel().getActiveEditor());
         }
      });
      this.application = application;
      this.preferences = preferences;
      this.recentFileList = recentFileList;
      this.hub.setDecorator(this::installTransparentIconIfMissing);
      this.miRevert = this.createMenuItem("file.revert", "Revert", parent -> application.doRevert(parent));
      this.miClose = this.createMenuItem("file.close", "Close", parent -> application.doClose(parent));
      this.miClose.getAction().putValue(Action.ACCELERATOR_KEY, JaveKeyBindings.CLOSE);
      this.miCloseAll = this.createMenuItem("file.closeAll", "Close All", parent -> application.doCloseAll(parent));
      SmartAction exitAction = new SmartAction("Exit") {
         @Override
         protected void execute(Component parentComponent) {
            application.performExit(parentComponent);
         }
      };
      this.menuRecent = this.hub.menu("file.recent", "Recent Files");
      this.menuRecent.setIcon(JaveIcons.OPEN_RECENT_ICON);
      recentFileList.addChangeListener(this::updateRecentFilesMenu);
      this.updateRecentFilesMenu();
      JMenu fileMenu = this.hub.menu("file", JaveMessages.Menu_File);
      fileMenu.add(this.hub.item("file.newDocument", actions.getNewDocumentAction()));
      fileMenu.add(this.hub.item("file.newDocumentDialog", new NewDocumentDialogAction(application, preferences)));
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.open", actions.getOpenAction()));
      fileMenu.add(this.miRevert);
      fileMenu.add(this.menuRecent);
      fileMenu.add(this.hub.item("file.browse", actions.getBrowseAction()));
      fileMenu.addSeparator();
      fileMenu.add(this.miClose);
      fileMenu.add(this.miCloseAll);
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.save", actions.getSaveAction()));
      fileMenu.add(this.hub.item("file.saveAs", actions.getSaveAsAction()));
      fileMenu.add(this.hub.item("file.saveAll", actions.getSaveAllAction()));
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.convertMakingOfToAnimation", new ConvertMakingOfToAnimationAction(preferences, application, mainPanel)));
      fileMenu.add(this.hub.item("file.export", actions.getExportAction()));
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.quickStart", actions.getQuickStartAction()));
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.javePreferences", new JavePreferencesAction(javePreferences, preferences, platePreferences, mainPanel.getToolManager())));
      fileMenu.addSeparator();
      fileMenu.add(this.hub.item("file.exit", exitAction)).getAction().putValue(Action.ACCELERATOR_KEY, JaveKeyBindings.EXIT);
      JMenu animationMenu = this.hub.menu("animation", JaveMessages.Menu_Animation);
      animationMenu.add(this.hub.item("animation.newAnimation", actions.getNewAnimationAction()));
      animationMenu.addSeparator();
      animationMenu.add(this.hub.item("animation.openAnimation", new OpenAnimationAction(mainPanel, application, application.getDocumentManager().getCurrentDirectoryModel())));
      animationMenu.add(this.hub.item("animation.showVtViewer", new ShowVtViewerAction(application.getDocumentManager().getCurrentDirectoryModel())));
      animationMenu.addSeparator();
      animationMenu.add(this.hub.item("animation.javeImport", new JaveImportAction(application, application.getDocumentManager().getCurrentDirectoryModel())));
      animationMenu.add(this.hub.item("animation.exportAnimationEditor", new ExportAnimationEditorAction(
            mainPanel,
            application.getDocumentManager().getCurrentDirectoryModel(),
            preferences.getDisplayFontModel(),
            new AnimationExportPreferences(javePreferences)
         )));
      animationMenu.addSeparator();
      animationMenu.add(this.hub.item("animation.addNewFrame", new AddNewFrameAction(mainPanel)));
      animationMenu.add(this.hub.item("animation.duplicateFrame", new DuplicateFrameAction(mainPanel)));
      animationMenu.add(this.hub.item("animation.deleteFrame", new DeleteFrameAction(mainPanel)));
      animationMenu.addSeparator();
      animationMenu.add(this.hub.item("animation.animationEditorProperties", new AnimationEditorPropertiesAction(mainPanel)));
      this.miSelectAll = this.createMenuItem("edit.selectAll", "Select All", parent -> application.selectAll());
      this.miSelectAll.getAction().putValue(Action.ACCELERATOR_KEY, JaveKeyBindings.SELECT_ALL);
      this.miSelectConnected = this.createMenuItem("edit.selectConnected", "Select Connected", parent -> application.selectConnected());
      this.miSelectConnected.getAction().putValue(Action.ACCELERATOR_KEY, JaveKeyBindings.SELECT_CONNECTED);
      FontModel displayFontModel = preferences.getDisplayFontModel();
      this.miClear = this.createMenuItem("edit.clear", mainPanel, new Clear(), displayFontModel);
      this.miReplace = this.createMenuItem("edit.replace", "Replace...", parent -> application.doReplaceCharacter());
      this.miReplace.getAction().putValue(Action.ACCELERATOR_KEY, JaveKeyBindings.REPLACE);
      JMenu menuEdit = this.hub.menu("edit", JaveMessages.Menu_Edit);
      menuEdit.add(this.hub.item("edit.undo", new UndoAction(application, undoRedoModel, false)));
      menuEdit.add(this.hub.item("edit.redo", new RedoAction(application, undoRedoModel, false)));
      menuEdit.addSeparator();
      menuEdit.add(this.hub.item("edit.cut", actions.getCutAction()));
      menuEdit.add(this.hub.item("edit.copy", actions.getCopyAction()));
      menuEdit.add(this.hub.item("edit.pasteAsNewSelection", actions.getPasteAsNewSelectionAction()));
      menuEdit.add(this.hub.item("edit.pasteAsNewDocument", actions.getPasteAsNewDocumentAction()));
      this.pasteIntoSelectionAction = actions.getPasteIntoSelectionAction();
      menuEdit.add(this.hub.item("edit.pasteIntoSelection", this.pasteIntoSelectionAction));
      menuEdit.addSeparator();
      menuEdit.add(this.miSelectAll);
      menuEdit.add(this.miSelectConnected);
      menuEdit.addSeparator();
      menuEdit.add(this.miClear);
      menuEdit.add(this.hub.item("edit.crop", new CropAction(application, mainPanel)));
      menuEdit.add(this.hub.item("edit.resize", actions.getResizeAction()));
      menuEdit.addSeparator();
      menuEdit.add(this.miReplace);
      JMenuItem miAntiAlias = this.createMenuItem("modify.antiAlias", mainPanel, new AsciiAntialiasing(), displayFontModel);
      JMenuItem miRot13 = this.createMenuItem("modify.rot13", mainPanel, new Rot13(), displayFontModel);
      JMenuItem miToUpperCase = this.createMenuItem("modify.toUpperCase", mainPanel, new UpperCase(), displayFontModel);
      JMenuItem miToLowerCase = this.createMenuItem("modify.toLowerCase", mainPanel, new LowerCase(), displayFontModel);
      JMenuItem miAsciify = this.createMenuItem("modify.asciify", mainPanel, new Asciify(), displayFontModel);
      JMenuItem miUnasciify = this.createMenuItem("modify.unasciify", mainPanel, new Unasciify(), displayFontModel);
      JMenuItem miCenter = this.createMenuItem("modify.center", mainPanel, new Center(), displayFontModel);
      JMenuItem miCenterByTheLine = this.createMenuItem("modify.centerByTheLine", mainPanel, CenterByTheLine.getInstance(), displayFontModel);
      JMenuItem miAlignRight = this.createMenuItem("modify.alignRight", mainPanel, AlignRight.getInstance(), displayFontModel);
      JMenuItem miAlignLeft = this.createMenuItem("modify.alignLeft", mainPanel, AlignLeft.getInstance(), displayFontModel);
      GeneralAlgorithmConfiguration algorithmConfiguration = configurationList.getRequired(GeneralAlgorithmConfiguration.class);
      JMenuItem miFlip = this.createMenuItem("modify.flip", mainPanel, new FlipDynamicAction(algorithmConfiguration), displayFontModel);
      miFlip.setIcon(JaveIcons.FLIP_DYNAMIC_ICON);
      JMenuItem miMirrorDynamic = this.createMenuItem("modify.mirrorDynamic", mainPanel, new MirrorDynamicAction(algorithmConfiguration), displayFontModel);
      miMirrorDynamic.setIcon(JaveIcons.MIRROR_DYNAMIC_ICON);
      JMenu miRotate = this.hub.menu("modify.transform.rotateDynamic", "Rotate dynamic");
      miRotate.setIcon(JaveIcons.ROTATE_DYNAMIC_ICON);
      miRotate.add(this.hub.item("modify.transform.rotateDynamic.rotate180", new Rotate180Action(mainPanel, algorithmConfiguration)));
      miRotate.add(this.hub.item("modify.transform.rotateDynamic.rotate90Right", new Rotate90RightAction(mainPanel, algorithmConfiguration)));
      miRotate.add(this.hub.item("modify.transform.rotateDynamic.rotate90Left", new Rotate90LeftAction(mainPanel, algorithmConfiguration)));
      JMenuItem miFlipStatic = this.createMenuItem("modify.flipStatic", mainPanel, FlipStatic.getInstance(), displayFontModel);
      miFlipStatic.setIcon(JaveIcons.FLIP_STATIC_ICON);
      JMenuItem miMirrorStatic = this.createMenuItem("modify.mirrorStatic", mainPanel, MirrorStatic.getInstance(), displayFontModel);
      miMirrorStatic.setIcon(JaveIcons.MIRROR_STATIC_ICON);
      JMenu miRotateStatic = this.hub.menu("modify.transform.rotateStatic", "Rotate static");
      miRotateStatic.setIcon(JaveIcons.ROTATE_STATIC_ICON);
      miRotateStatic.add(this.hub.item("modify.transform.rotateStatic.rotate180Static", new Rotate180StaticAction(mainPanel)));
      miRotateStatic.add(this.hub.item("modify.transform.rotateStatic.rotate90RightStatic", new Rotate90RightStaticAction(mainPanel)));
      miRotateStatic.add(this.hub.item("modify.transform.rotateStatic.rotate90LeftStatic", new Rotate90LeftStaticAction(mainPanel)));
      JMenuItem miShake = this.createMenuItem("modify.shake", mainPanel, new ShakeLines(), displayFontModel);
      JMenuItem miRepairWrapped = this.hub.item("modify.repair.wrapped", new RepairWrapped(mainPanel));
      JMenuItem miInvert = this.createMenuItem("modify.invert", mainPanel, new Invert(), displayFontModel);
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration = configurationList.getRequired(AsciiGreyscaleTableConfiguration.class);
      JMenuItem miBrightness = this.createMenuItem("modify.brightness", mainPanel, new Brightness(greyscaleTableConfiguration), displayFontModel);
      JMenuItem miCompress = this.createMenuItem("modify.compress", mainPanel, CompressExpand.getInstance(), displayFontModel);
      JMenu menuRepair = this.hub.menu("modify.repair", "Try to Repair");
      IStatusDisplay status = application.getStatusDisplay();
      AsciiReplaceIllegalConfiguration replaceIllegalConfiguration = configurationList.getRequired(AsciiReplaceIllegalConfiguration.class);
      menuRepair.add(this.hub.item("modify.repair.repairIllegalReplace", new RepairIllegalReplaceAction(mainPanel, replaceIllegalConfiguration, characterSets, status)));
      menuRepair.add(this.hub.item("modify.repair.repairIllegalRemove", new RepairIllegalRemoveAction(mainPanel, characterSets, status)));
      menuRepair.addSeparator();
      menuRepair.add(miRepairWrapped);
      AsciiRepairAlgorithmConfiguration repairAlgorithmConfiguration = configurationList.getRequired(AsciiRepairAlgorithmConfiguration.class);
      menuRepair.add(this.hub.item("modify.repair.repairShakedLines", new RepairShakedLinesAction(mainPanel, repairAlgorithmConfiguration)));
      JMenu menuTransform = this.hub.menu("modify.transform", "Transform");
      menuTransform.add(miFlip);
      menuTransform.add(miMirrorDynamic);
      menuTransform.add(miRotate);
      menuTransform.addSeparator();
      menuTransform.add(miFlipStatic);
      menuTransform.add(miMirrorStatic);
      menuTransform.add(miRotateStatic);
      this.menuModify = this.hub.menu("modify", JaveMessages.Menu_Modify);
      this.menuModify.add(miRot13);
      this.menuModify.add(miToLowerCase);
      this.menuModify.add(miToUpperCase);
      this.menuModify.add(miAsciify);
      this.menuModify.add(miUnasciify);
      this.menuModify.addSeparator();
      this.menuModify.add(miCenter);
      this.menuModify.add(miCenterByTheLine);
      this.menuModify.add(miAlignRight);
      this.menuModify.add(miAlignLeft);
      this.menuModify.addSeparator();
      this.menuModify.add(menuTransform);
      this.menuModify.add(miInvert);
      this.menuModify.add(miBrightness);
      this.menuModify.addSeparator();
      this.menuModify.add(miShake);
      this.menuModify.add(menuRepair);
      this.menuModify.add(miAntiAlias);
      this.menuModify.addSeparator();
      this.menuModify.add(miCompress);
      this.menuLayer = this.hub.menu("layer", "Layer");
      this.menuLayer.add(this.hub.item("layer.newLayer", new NewLayerAction(application, mainPanel)));
      this.deleteLayerAction = new DeleteLayerAction(application, mainPanel);
      this.menuLayer.add(this.hub.item("layer.delete", this.deleteLayerAction));
      this.duplicateLayerAction = new DuplicateLayerAction(application, mainPanel);
      this.menuLayer.add(this.hub.item("layer.duplicate", this.duplicateLayerAction));
      this.menuLayer.addSeparator();
      this.miToggleLayerVisibility = this.createMenuItem("layer.toggleVisibility", "Hide Layer", parent -> {
         application.toggleActiveLayerVisibility();
         this.updateLayerMenu();
      });
      this.menuLayer.add(this.miToggleLayerVisibility);
      this.miLayerOpaque = this.hub.toggle("layer.opaque", MenuHub.toggleAction("Opaque", selected -> {
         application.setActiveLayerOpaque(selected);
         this.updateLayerMenu();
      }));
      this.menuLayer.add(this.miLayerOpaque);
      this.menuLayer.addSeparator();
      this.menuLayer.add(this.hub.item("layer.layerCrop", new LayerCropAction(mainPanel)));
      this.menuLayer.addSeparator();
      this.menuLayer.add(this.hub.item("layer.flatten", new FlattenLayersAction(application, mainPanel, true)));
      this.menuLayer.add(this.hub.item("layer.flattenVisible", new FlattenLayersAction(application, mainPanel, false)));
      this.menuLayer.addMenuListener(new MenuListener() {
         @Override
         public void menuSelected(MenuEvent event) {
            JaveMenuBar.this.updateLayerMenu();
         }

         @Override
         public void menuDeselected(MenuEvent event) {
         }

         @Override
         public void menuCanceled(MenuEvent event) {
         }
      });
      final CharSetsConfiguration charSetsConfiguration = configurationList.getRequired(CharSetsConfiguration.class);
      String[] s = charSetsConfiguration.getCharsetNames();
      if (s != null && s.length > 1) {
         int defaultCharsetIndex = CharacterSets.getDefaultCharsetIndex();
         this.menuCharacterSets = this.hub.menu("view.characterSet", "Character Set");
         this.miCharacters = new JCheckBoxMenuItem[s.length];
         ButtonGroup bg = new ButtonGroup();

         for (int i = 0; i < s.length; i++) {
            int charsetIndex = i;
            this.miCharacters[i] = this.hub.toggle("view.characterSet." + i, MenuHub.action(s[i], parent -> {
               this.setCurrentCharsetIndex(charsetIndex, charSetsConfiguration, application);
               characterSets.setCurrentCharsetIndex(charsetIndex);
               application.getMainPanel().repaint();
            }));
            this.miCharacters[i].getAction().putValue(Action.SELECTED_KEY, i == defaultCharsetIndex);
            bg.add(this.miCharacters[i]);
            this.menuCharacterSets.add(this.miCharacters[i]);
            if (i == 0 || i == s.length - 2) {
               this.menuCharacterSets.addSeparator();
            }
         }
      }

      this.menuView = this.hub.menu("view", JaveMessages.Menu_View);
      this.menuColor = this.hub.menu("view.color", "Color");
      ColorScheme[] colorSchemes = ColorScheme.getAll();
      this.miColorSchemes = new JCheckBoxMenuItem[colorSchemes.length];
      ButtonGroup bg = new ButtonGroup();

      for (int ix = 0; ix < colorSchemes.length; ix++) {
         final ColorScheme scheme = colorSchemes[ix];
         Action colorAction = MenuHub.action(scheme.getName(), parent -> application.performSetColorScheme(scheme));
         colorAction.putValue(Action.SMALL_ICON, scheme.getIcon());
         this.miColorSchemes[ix] = this.hub.toggle("view.color." + scheme.getId(), colorAction);
         bg.add(this.miColorSchemes[ix]);
         this.menuColor.add(this.miColorSchemes[ix]);
      }

      this.menuView.add(this.hub.item("view.zoomIn", new ZoomInAction(mainPanel)));
      this.menuView.add(this.hub.item("view.zoomOut", new ZoomOutAction(mainPanel)));
      this.menuView.add(this.hub.toggle("view.autoZoom", actions.getAutoZoomToggleAction()));
      this.menuView.addSeparator();
      this.menuView.add(this.hub.toggle("view.rulerToggle", actions.getRulerToggleAction()));
      this.menuView.add(this.hub.toggle("view.gridToggle", actions.getGridToggleAction()));
      this.menuView.add(this.hub.toggle("view.markIllegalToggle", actions.getMarkIllegalToggleAction()));
      this.menuView.add(this.hub.toggle("view.connectedLinesViewToggle", actions.getConnectedLinesViewToggleAction()));
      this.menuView.add(this.hub.toggle("view.auxLinesVisibilityToggle", actions.getAuxLinesVisibilityToggleAction()));
      this.menuView.addSeparator();
      this.menuView.add(this.hub.toggle("view.watermarkVisibilityToggle", actions.getWatermarkVisibilityToggleAction()));
      this.menuView.add(this.hub.toggle("view.toolsPaletteToggle", actions.getToolsPaletteToggleAction()));
      this.miLayersPanel = this.hub.toggle("view.layersPanel", MenuHub.toggleAction("Layers Panel", selected -> {
         application.setLayersPanelVisible(selected);
         this.updateLayersPanelMenu();
      }));
      this.menuView.add(this.miLayersPanel);

      this.menuView.addSeparator();
      if (this.menuCharacterSets != null) {
         this.menuView.add(this.menuCharacterSets);
      }
      this.menuView.add(this.menuColor);
      this.menuView.add(this.hub.item("view.chooseDisplayFont", new ChooseDisplayFontAction(preferences.getDisplayFontModel())));
      this.menuWindows = this.hub.menu("window", JaveMessages.Menu_Window);
      this.menuSelection = this.hub.menu("selection", JaveMessages.Menu_Selection);
      this.menuSelection.setEnabled(false);
      this.miSelectionToBrush = this.createMenuItem("selection.useAsBrush", "Use as Brush", parent -> application.doSelectionToBrush());
      this.miSelectionExpand = this.createMenuItem("selection.expand", "Expand", parent -> application.doSelectionExpand());
      this.miSelectionShrink = this.createMenuItem("selection.shrinkToFit", "Shrink to fit", parent -> application.doSelectionShrink());
      this.miSelectionDelete = this.createMenuItem("selection.delete", "Delete", parent -> application.doSelectionDelete());
      this.menuSelection.add(this.miSelectionToBrush);
      this.menuSelection.add(this.hub.item("selection.selectionToClipart", new SelectionToClipartAction(mainPanel, preferences)));
      this.menuSelection.addSeparator();
      this.menuSelection.add(this.miSelectionExpand);
      this.menuSelection.add(this.miSelectionShrink);
      this.menuSelection.addSeparator();
      this.menuSelection.add(this.hub.item("selection.addBracesToSelection", new AddBracesToSelectionAction(mainPanel)));
      this.menuSelection.addSeparator();
      this.menuSelection.add(this.miSelectionDelete);
      JMenu menuTools = this.hub.menu("tools", JaveMessages.Menu_Tools);
      menuTools.add(this.hub.item("tools.camelizer", actions.getCamelizerAction()));
      menuTools.addSeparator();
      menuTools.add(this.hub.item("tools.image2Ascii", actions.getImage2AsciiAction()));
      menuTools.add(this.hub.item("tools.figlet", actions.getFigletAction()));
      menuTools.add(this.hub.item("tools.clipartLibrary", actions.getClipartLibraryAction()));
      menuTools.add(this.hub.item("tools.textBox", actions.getTextBoxAction()));
      menuTools.add(this.hub.item("tools.mathematicalExpressions", actions.getMathematicalExpressionsAction()));
      menuTools.add(this.hub.item("tools.boxDrawingPicker", actions.getBoxDrawingPickerAction()));
      menuTools.add(this.hub.item("tools.unicodePicker", actions.getUnicodePickerAction()));
      menuTools.addSeparator();
      IFigDriver figDriver = configurationList.getRequired(IFigDriver.class);
      menuTools.add(this.hub.item("tools.figletExportWizard", new FigletExportWizardAction(figDriver, mainPanel, preferences)));
      this.miRender3D = this.createMenuItem("special.render3d", "Render 3D", parent -> application.doRender3D());
      this.miFunctionPlotter = this.createMenuItem("special.functionPlotter", "Function Plotter", parent -> application.doFunctionPlotter());
      this.miDoc2Watermark = this.createMenuItem("special.contentAsWatermark", "Set Content as Watermark", parent -> application.doDoc2Watermark());
      JMenu menuSpecial = this.hub.menu("special", JaveMessages.Menu_Special);
      menuSpecial.add(this.hub.item("special.fractal", actions.getFractalAction()));
      menuSpecial.add(this.miRender3D);
      menuSpecial.add(this.miFunctionPlotter);
      menuSpecial.addSeparator();
      Filter filter = configurationList.getRequired(Filter.class);
      menuSpecial.add(this.hub.item("special.asctris", new AsctrisAction(application, filter)));
      menuSpecial.add(this.hub.item("special.labyrinth", new LabyrinthAction(application)));
      menuSpecial.addSeparator();
      menuSpecial.add(this.hub.item("special.gameOfLife", new GameOfLifeAction(filter)));
      menuSpecial.addSeparator();
      menuSpecial.add(this.miDoc2Watermark);
      menuSpecial.addSeparator();
      AsciiGradientConfiguration gradientConfiguration = configurationList.getRequired(AsciiGradientConfiguration.class);
      menuSpecial.add(this.hub.item("special.decodeSteganogram", new DecodeSteganogramAction(mainPanel, gradientConfiguration)));
      JMenu menuDebug = this.hub.menu("help.debug", "Debug");
      menuDebug.add(this.hub.item("help.debug.crashNow", new CrashNowAction()));
      JMenu menuHelp = this.hub.menu("help", JaveMessages.Menu_Help);
      menuHelp.add(this.hub.item("help.openOnlineLocation", new OpenOnlineLocationAction("Online Documentation", "http://www.jave.de/docs/index.html")));
      menuHelp.addSeparator();
      menuHelp.add(menuDebug);
      menuHelp.addSeparator();
      menuHelp.add(this.hub.item("help.versionCheck", new VersionCheckAction()));
      menuHelp.add(this.hub.item("help.about", actions.getAboutAction()));
      this.hub.addMenu(fileMenu);
      this.hub.addMenu(animationMenu);
      this.hub.addMenu(menuEdit);
      this.hub.addMenu(this.menuLayer);
      this.hub.addMenu(this.menuModify);
      this.hub.addMenu(this.menuView);
      this.hub.addMenu(this.menuSelection);
      this.hub.addMenu(menuTools);
      this.hub.addMenu(menuSpecial);
      this.hub.addMenu(this.menuWindows);
      this.hub.addMenu(menuHelp);
      this.installTransparentIconsOnMenuItems();
      this.docOpenEnabledMenuItems.add(this.miClear);
      this.docOpenEnabledMenuItems.add(this.miSelectAll);
      this.docOpenEnabledMenuItems.add(this.miSelectConnected);
      this.docOpenEnabledMenuItems.add(this.miClose);
      this.docOpenEnabledMenuItems.add(this.miCloseAll);
      this.docOpenEnabledMenuItems.add(this.miReplace);
      this.docOpenEnabledMenuItems.add(this.miDoc2Watermark);
      this.docOpenEnabledMenuItems.add(miInvert);
      this.docOpenEnabledMenuItems.add(miCompress);
      this.docOpenEnabledMenuItems.add(miBrightness);
      this.updateColorMenu(currentColorSchemeModel.getColorScheme(), mainPanel.getActiveEditorModel().getActiveEditor());
      mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveMenuBar.this.updateWindowsMenu();
         }
      });
      this.updateWindowsMenu();
   }

   private JMenuItem createMenuItem(String id, String label, Consumer<Component> execute) {
      return this.hub.item(id, MenuHub.action(label, source -> {
         Component parent = GuiUtilities.getWindowForComponent(source);
         this.application.getMainPanel().requestFocus();
         execute.accept(parent);
      }));
   }

   private JMenuItem createMenuItem(String id, JaveMainPanel mainPanel, JaveAlgorithm algorithm, FontModel displayFontModel) {
      return this.hub.item(id, new JaveAlgorithmAction(mainPanel, displayFontModel, algorithm));
   }

   public MenuSearchController install(JFrame frame) {
      return this.hub.install(frame,
         new MenuSearchHistory(new SmartPreferences(this.preferences.getSubPreferences("menuSearchHistory"))),
         () -> {
            this.updateWindowsMenu();
            this.updateSelectionMenu(this.application.getMainPanel().hasSelection());
         }, () -> {
            JaveMainPanel panel = this.application.getMainPanel();
            return panel.getPlate() == null ? panel.getContent() : panel.getPlate();
         });
   }

   private void updateRecentFilesMenu() {
      this.menuRecent.removeAll();
      File[] files = this.recentFileList.getFiles();
      if (files.length == 0) {
         this.menuRecent.add(this.hub.placeholder("none"));
      }
      for (int i = 0; i < files.length; i++) {
         File file = files[i];
         String label = i + " " + FileDisplayNameUtilities.createShortenedFileName(file.getPath(), 25);
         this.menuRecent.add(this.hub.item("file.recent." + file.toPath().toAbsolutePath().normalize().toUri(),
            MenuHub.action(label, parent -> this.recentFileList.open(file, GuiUtilities.getWindowForComponent(parent)))));
      }
   }

   private void updateColorMenu(ColorScheme colorScheme, IDocumentEditor editor) {
      boolean enabled = colorScheme != null && editor != null && editor.getType() == JaveDocumentType.TEXT;

      for (JCheckBoxMenuItem item : this.miColorSchemes) {
         item.getAction().setEnabled(enabled);
      }

      if (colorScheme != null) {
         ColorScheme[] schemes = ColorScheme.getAll();

         for (int i = 0; i < this.miColorSchemes.length; i++) {
            this.miColorSchemes[i].getAction().putValue(Action.SELECTED_KEY, schemes[i] == colorScheme);
         }
      }
   }

   public void setRevertEnabled(boolean what) {
      this.miRevert.getAction().setEnabled(what);
   }

   public void updateSelectionMenu(boolean hasSelection) {
      if (this.menuSelection.isEnabled() != hasSelection) {
         this.menuSelection.setEnabled(hasSelection);
      }
      this.pasteIntoSelectionAction.setEnabled(hasSelection);
   }

   public void updateWindowsMenu() {
      JaveMainPanel mainPanel = this.application.getMainPanel();
      List<WindowTarget> targets = new ArrayList<>();
      Set<String> fileIds = new HashSet<>();
      for (int i = 0; i < mainPanel.getEditorCount(); i++) {
         IDocumentEditor editor = mainPanel.getEditor(i);
         String fileId = editor.getFile() == null ? null
            : "window.file." + editor.getFile().toPath().toAbsolutePath().normalize().toUri();
         // Multiple animation editors can refer to the same file. Extra instances are session-only.
         boolean persistent = fileId != null && fileIds.add(fileId);
         String id = persistent ? fileId
            : this.unsavedWindowIds.computeIfAbsent(editor, ignored -> "window.unsaved." + UUID.randomUUID());
         targets.add(new WindowTarget(editor, (i + 1) + " " + DocumentEditorTitleFactory.createShortEditorTitle(editor), id, persistent));
      }
      if (!targets.equals(this.windowTargets)) {
         this.windowTargets = List.copyOf(targets);
         this.rebuildWindowsMenu();
      }
      int editorCount = this.application.getMainPanel().getEditorCount();
      boolean docOpen = editorCount > 0;
      this.menuView.setEnabled(docOpen);
      this.menuColor.setEnabled(docOpen);
      this.menuModify.setEnabled(docOpen);
      this.updateLayersPanelMenu();
      this.updateLayerMenu();
      if (this.docOpenEnabledMenuItems != null) {
         for (int i = 0; i < this.docOpenEnabledMenuItems.size(); i++) {
            this.docOpenEnabledMenuItems.get(i).getAction().setEnabled(docOpen);
         }
      }

      this.miPreviousWindow.getAction().setEnabled(editorCount > 1);
      this.miNextWindow.getAction().setEnabled(editorCount > 1);
      if (!docOpen) {
         this.miRevert.getAction().setEnabled(false);
      } else {
         this.miRevert.getAction().setEnabled(this.application.getMainPanel().getEditor().getFile() != null);
      }
      for (int i = 0; i < this.windowTargets.size(); i++) {
         this.menuWindows.getItem(i + 3).getAction().putValue(Action.SELECTED_KEY,
            mainPanel.getActiveEditorModel().getActiveEditor() == this.windowTargets.get(i).editor());
      }
   }

   private void rebuildWindowsMenu() {
      this.menuWindows.removeAll();
      this.miPreviousWindow = this.createMenuItem("window.previous", "Previous", parent -> this.application.doPreviousDocument());
      this.menuWindows.add(this.miPreviousWindow);
      this.miNextWindow = this.createMenuItem("window.next", "Next", parent -> this.application.doNextDocument());
      this.menuWindows.add(this.miNextWindow);
      this.menuWindows.addSeparator();
      ButtonGroup group = new ButtonGroup();
      for (int i = 0; i < this.windowTargets.size(); i++) {
         WindowTarget target = this.windowTargets.get(i);
         JCheckBoxMenuItem item = this.hub.toggle(target.id(), MenuHub.action(target.label(), parent -> {
            JaveMainPanel panel = this.application.getMainPanel();
            for (int index = 0; index < panel.getEditorCount(); index++) {
               if (panel.getEditor(index) == target.editor()) {
                  this.application.setCurrentDocument(index);
                  return;
               }
            }
         }), target.persistent());
         group.add(item);
         if (i < windowShortcuts.length) {
            item.setMnemonic(windowShortcuts[i]);
         }
         this.menuWindows.add(item);
      }
   }

   private void updateLayerMenu() {
      IDocumentEditor activeEditor = this.application.getMainPanel().getActiveEditorModel().getActiveEditor();
      boolean textEditorActive = activeEditor != null && activeEditor.getType() == JaveDocumentType.TEXT;
      PlateDocument document = textEditorActive ? activeEditor.getPlate().getDocument() : null;
      this.menuLayer.setEnabled(textEditorActive);
      this.deleteLayerAction.setEnabled(
         textEditorActive && document != null && document.canDeleteActiveLayer()
      );
      this.duplicateLayerAction.setEnabled(textEditorActive);
      boolean secondaryLayerActive = textEditorActive && document != null && !document.isDocumentLayerActive();
      this.miToggleLayerVisibility.getAction().setEnabled(secondaryLayerActive);
      this.miToggleLayerVisibility.getAction().putValue(Action.NAME, document != null && !document.isActiveLayerVisible() ? "Show Layer" : "Hide Layer");
      this.miLayerOpaque.getAction().setEnabled(secondaryLayerActive);
      this.miLayerOpaque.getAction().putValue(Action.SELECTED_KEY, document == null || document.isActiveLayerOpaque());
   }

   private void updateLayersPanelMenu() {
      IDocumentEditor activeEditor = this.application.getMainPanel().getActiveEditorModel().getActiveEditor();
      boolean textEditorActive = activeEditor instanceof TextDocumentEditor;
      this.miLayersPanel.getAction().setEnabled(textEditorActive);
      this.miLayersPanel.getAction().putValue(Action.SELECTED_KEY, textEditorActive && ((TextDocumentEditor)activeEditor).isLayersPanelVisible());
   }

   private void installTransparentIconsOnMenuItems() {
      for (int i = 0; i < this.getMenuCount(); i++) {
         JMenu menu = this.getMenu(i);
         if (menu != null) {
            this.installTransparentIcons(menu);
         }
      }
   }

   private void installTransparentIcons(JMenu menu) {
      for (int i = 0; i < menu.getItemCount(); i++) {
         JMenuItem item = menu.getItem(i);
         if (item != null) {
            this.installTransparentIconIfMissing(item);
            if (item instanceof JMenu) {
               this.installTransparentIcons((JMenu)item);
            }
         }
      }
   }

   private void installTransparentIconIfMissing(JMenuItem item) {
      if (item.getIcon() == null) {
         item.setIcon(JaveIcons.TRANSPARENT_ICON);
      }
      this.installBaseMenuIcon(item);
   }

   private void installBaseMenuIcon(JMenuItem item) {
      Icon baseIcon = this.getBaseMenuIcon(item);
      if (baseIcon != null && baseIcon != item.getIcon()) {
         item.setIcon(baseIcon);
      }
   }

   private Icon getBaseMenuIcon(JMenuItem item) {
      Action action = item.getAction();
      if (action != null) {
         Object baseIcon = action.getValue(AbstractDizzyAction.BASE_ICON);
         if (baseIcon instanceof Icon) {
            return (Icon)baseIcon;
         }
      }

      Icon icon = item.getIcon();
      return icon instanceof IBaseIconProvider ? ((IBaseIconProvider)icon).getBaseIcon() : icon;
   }

   private void setCurrentCharsetIndex(int index, CharSetsConfiguration charSetsConfiguration, JavEApplication jave) {
      if (index == charSetsConfiguration.getUserDefinedIndex()) {
         FontModel fontModel = new FontModel(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
         AsciiTextAreaProperties textAreaProperties = new AsciiTextAreaProperties(fontModel);
         String text = charSetsConfiguration.getCharSetChatacters(index);
         AsciiTextArea taSet = new AsciiTextArea(new Dimension(60, 5), textAreaProperties);
         taSet.setText(text);
         final JPanel panel = new JPanel(new GridDialogLayout(1, false));
         panel.add(new JLabel("Legal Characters:"));
         panel.add(taSet.getContent());
         panel.add(new JLabel("(The Character [space] is always included)"));
         IDialogPage page = new AbstractDialogPage("") {
            @Override
            public IBasicMessage createCurrentMessage() {
               return this.getDefaultMessage();
            }

            @Override
            public JComponent createContent() {
               return panel;
            }

            @Override
            public String getTitle() {
               return "User Defined Character Set";
            }
         };
         UserDialog userDialog = new UserDialog(
            jave.getFrame(), new DefaultDialogConfiguration<IDialogPage>(page, DialogButtonConfigurationFactory.createOkOnly()) {
               @Override
               public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
                  return DialogHeaderPanelConfiguration.createInvisible();
               }
            }
         );
         IDialogResult result = userDialog.show();
         if (!result.isCanceled()) {
            charSetsConfiguration.setCharsetCharacters(index, " " + taSet.getText());
         }
      }
   }

}
