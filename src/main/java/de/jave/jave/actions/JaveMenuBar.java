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
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.lib.gui.GuiUtilities;
import de.jave.lib.gui.IStatusDisplay;
import de.jave.preferences.JavePreferences;
import de.jave.util.RecentFileList;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.menu.HelpImplementedMenuBar;

public class JaveMenuBar extends HelpImplementedMenuBar implements ActionListener {
   private final JavEApplication application;
   private final JMenu menuWindows;
   private final JMenu menuColor;
   private final JMenu menuView;
   private final JMenu menuModify;
   private final JMenu menuSelection;
   private JMenu menuCharacterSets;
   private final JMenuItem miRevert;
   private final JMenuItem miClose;
   private final JMenuItem miCloseAll;
   private final JMenuItem miSelectAll;
   private final JMenuItem miClear;
   private final JMenuItem miReplace;
   private final JMenuItem miRender3D;
   private final JMenuItem miFunctionPlotter;
   private final JMenuItem miDoc2Watermark;
   private JMenuItem miNextWindow;
   private JMenuItem miPreviousWindow;
   private final JCheckBoxMenuItem[] miColorSchemes;
   private JCheckBoxMenuItem[] miCharacters;
   private JCheckBoxMenuItem[] windowMenuItems;
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
      this.miRevert = this.createMenuItem("Revert");
      this.miClose = this.createMenuItem("Close");
      this.miClose.setAccelerator(JaveKeyBindings.CLOSE);
      this.miCloseAll = this.createMenuItem("Close All");
      SmartAction exitAction = new SmartAction("Exit") {
         @Override
         protected void execute(Component parentComponent) {
            application.performExit(parentComponent);
         }
      };
      JMenu menuRecent = new JMenu("Recent Files");
      menuRecent.setIcon(JaveIcons.OPEN_RECENT_ICON);
      recentFileList.setMenu(menuRecent);
      JMenu fileMenu = new SmartMenu(JaveMessages.Menu_File);
      fileMenu.add(actions.getNewDocumentAction());
      fileMenu.addSeparator();
      fileMenu.add(actions.getOpenAction());
      fileMenu.add(this.miRevert);
      fileMenu.add(menuRecent);
      fileMenu.add(actions.getBrowseAction());
      fileMenu.addSeparator();
      fileMenu.add(this.miClose);
      fileMenu.add(this.miCloseAll);
      fileMenu.addSeparator();
      fileMenu.add(actions.getSaveAction());
      fileMenu.add(actions.getSaveAsAction());
      fileMenu.add(actions.getSaveAllAction());
      fileMenu.addSeparator();
      fileMenu.add(new ConvertMakingOfToAnimationAction(preferences, application, mainPanel));
      fileMenu.add(actions.getExportAction());
      fileMenu.addSeparator();
      fileMenu.add(actions.getQuickStartAction());
      fileMenu.addSeparator();
      fileMenu.add(exitAction).setAccelerator(JaveKeyBindings.EXIT);
      JMenu animationMenu = new SmartMenu(JaveMessages.Menu_Animation);
      animationMenu.add(actions.getNewAnimationAction());
      animationMenu.addSeparator();
      animationMenu.add(new OpenAnimationAction(mainPanel, application, application.getDocumentManager().getCurrentDirectoryModel()));
      animationMenu.add(new ShowVtViewerAction(application.getDocumentManager().getCurrentDirectoryModel()));
      animationMenu.addSeparator();
      animationMenu.add(new JaveImportAction(application, application.getDocumentManager().getCurrentDirectoryModel()));
      animationMenu.add(
         new ExportAnimationEditorAction(
            mainPanel,
            application.getDocumentManager().getCurrentDirectoryModel(),
            preferences.getDisplayFontModel(),
            new AnimationExportPreferences(javePreferences)
         )
      );
      animationMenu.addSeparator();
      animationMenu.add(new AddNewFrameAction(mainPanel));
      animationMenu.add(new DuplicateFrameAction(mainPanel));
      animationMenu.add(new DeleteFrameAction(mainPanel));
      animationMenu.addSeparator();
      animationMenu.add(new AnimationEditorPropertiesAction(mainPanel));
      this.miSelectAll = this.createMenuItem("Select All");
      this.miSelectAll.setAccelerator(JaveKeyBindings.SELECT_ALL);
      FontModel displayFontModel = preferences.getDisplayFontModel();
      this.miClear = this.createMenuItem(mainPanel, new Clear(), displayFontModel);
      this.miReplace = this.createMenuItem("Replace...");
      this.miReplace.setAccelerator(JaveKeyBindings.REPLACE);
      JMenu menuEdit = new SmartMenu(JaveMessages.Menu_Edit);
      menuEdit.add(new UndoAction(application, undoRedoModel, false));
      menuEdit.add(new RedoAction(application, undoRedoModel, false));
      menuEdit.addSeparator();
      menuEdit.add(actions.getCutAction());
      menuEdit.add(actions.getCopyAction());
      menuEdit.add(actions.getPasteAsNewSelectionAction());
      menuEdit.add(actions.getPasteAsNewDocumentAction());
      this.pasteIntoSelectionAction = actions.getPasteIntoSelectionAction();
      menuEdit.add(this.pasteIntoSelectionAction);
      menuEdit.addSeparator();
      menuEdit.add(this.miSelectAll);
      menuEdit.addSeparator();
      menuEdit.add(this.miClear);
      menuEdit.add(new CropAction(application, mainPanel));
      menuEdit.add(actions.getResizeAction());
      menuEdit.addSeparator();
      menuEdit.add(this.miReplace);
      JMenuItem miAntiAlias = this.createMenuItem(mainPanel, new AsciiAntialiasing(), displayFontModel);
      JMenuItem miRot13 = this.createMenuItem(mainPanel, new Rot13(), displayFontModel);
      JMenuItem miToUpperCase = this.createMenuItem(mainPanel, new UpperCase(), displayFontModel);
      JMenuItem miToLowerCase = this.createMenuItem(mainPanel, new LowerCase(), displayFontModel);
      JMenuItem miAsciify = this.createMenuItem(mainPanel, new Asciify(), displayFontModel);
      JMenuItem miUnasciify = this.createMenuItem(mainPanel, new Unasciify(), displayFontModel);
      JMenuItem miCenter = this.createMenuItem(mainPanel, new Center(), displayFontModel);
      JMenuItem miCenterByTheLine = this.createMenuItem(mainPanel, CenterByTheLine.getInstance(), displayFontModel);
      JMenuItem miAlignRight = this.createMenuItem(mainPanel, AlignRight.getInstance(), displayFontModel);
      JMenuItem miAlignLeft = this.createMenuItem(mainPanel, AlignLeft.getInstance(), displayFontModel);
      GeneralAlgorithmConfiguration algorithmConfiguration = configurationList.getRequired(GeneralAlgorithmConfiguration.class);
      JMenuItem miFlip = this.createMenuItem(mainPanel, new FlipDynamicAction(algorithmConfiguration), displayFontModel);
      miFlip.setIcon(JaveIcons.FLIP_DYNAMIC_ICON);
      JMenuItem miMirrorDynamic = this.createMenuItem(mainPanel, new MirrorDynamicAction(algorithmConfiguration), displayFontModel);
      miMirrorDynamic.setIcon(JaveIcons.MIRROR_DYNAMIC_ICON);
      JMenu miRotate = new JMenu("Rotate dynamic");
      miRotate.setIcon(JaveIcons.ROTATE_DYNAMIC_ICON);
      miRotate.add(new Rotate180Action(mainPanel, algorithmConfiguration));
      miRotate.add(new Rotate90RightAction(mainPanel, algorithmConfiguration));
      miRotate.add(new Rotate90LeftAction(mainPanel, algorithmConfiguration));
      JMenuItem miFlipStatic = this.createMenuItem(mainPanel, FlipStatic.getInstance(), displayFontModel);
      miFlipStatic.setIcon(JaveIcons.FLIP_STATIC_ICON);
      JMenuItem miMirrorStatic = this.createMenuItem(mainPanel, MirrorStatic.getInstance(), displayFontModel);
      miMirrorStatic.setIcon(JaveIcons.MIRROR_STATIC_ICON);
      JMenu miRotateStatic = new JMenu("Rotate static");
      miRotateStatic.setIcon(JaveIcons.ROTATE_STATIC_ICON);
      miRotateStatic.add(new Rotate180StaticAction(mainPanel));
      miRotateStatic.add(new Rotate90RightStaticAction(mainPanel));
      miRotateStatic.add(new Rotate90LeftStaticAction(mainPanel));
      JMenuItem miShake = this.createMenuItem(mainPanel, new ShakeLines(), displayFontModel);
      JMenuItem miRepairWrapped = new JMenuItem(new RepairWrapped(mainPanel));
      JMenuItem miInvert = this.createMenuItem(mainPanel, new Invert(), displayFontModel);
      AsciiGreyscaleTableConfiguration greyscaleTableConfiguration = configurationList.getRequired(AsciiGreyscaleTableConfiguration.class);
      JMenuItem miBrightness = this.createMenuItem(mainPanel, new Brightness(greyscaleTableConfiguration), displayFontModel);
      JMenuItem miCompress = this.createMenuItem(mainPanel, CompressExpand.getInstance(), displayFontModel);
      JMenu menuRepair = new JMenu("Try to Repair");
      IStatusDisplay status = application.getStatusDisplay();
      AsciiReplaceIllegalConfiguration replaceIllegalConfiguration = configurationList.getRequired(AsciiReplaceIllegalConfiguration.class);
      menuRepair.add(new RepairIllegalReplaceAction(mainPanel, replaceIllegalConfiguration, characterSets, status));
      menuRepair.add(new RepairIllegalRemoveAction(mainPanel, characterSets, status));
      menuRepair.addSeparator();
      menuRepair.add(miRepairWrapped);
      AsciiRepairAlgorithmConfiguration repairAlgorithmConfiguration = configurationList.getRequired(AsciiRepairAlgorithmConfiguration.class);
      menuRepair.add(new JMenuItem(new RepairShakedLinesAction(mainPanel, repairAlgorithmConfiguration)));
      JMenu menuTransform = new JMenu("Transform");
      menuTransform.add(miFlip);
      menuTransform.add(miMirrorDynamic);
      menuTransform.add(miRotate);
      menuTransform.addSeparator();
      menuTransform.add(miFlipStatic);
      menuTransform.add(miMirrorStatic);
      menuTransform.add(miRotateStatic);
      this.menuModify = new SmartMenu(JaveMessages.Menu_Modify);
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
      final CharSetsConfiguration charSetsConfiguration = configurationList.getRequired(CharSetsConfiguration.class);
      String[] s = charSetsConfiguration.getCharsetNames();
      if (s != null && s.length > 1) {
         int defaultCharsetIndex = CharacterSets.getDefaultCharsetIndex();
         this.menuCharacterSets = new JMenu("Character Set");
         this.miCharacters = new JCheckBoxMenuItem[s.length];
         ButtonGroup bg = new ButtonGroup();

         for (int i = 0; i < s.length; i++) {
            this.miCharacters[i] = new JCheckBoxMenuItem(s[i], i == defaultCharsetIndex);
            int finalI = i;
            this.miCharacters[i].addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  JaveMenuBar.this.setCurrentCharsetIndex(finalI, charSetsConfiguration, application);
                  characterSets.setCurrentCharsetIndex(finalI);
                  application.getMainPanel().repaint();
               }
            });
            bg.add(this.miCharacters[i]);
            this.menuCharacterSets.add(this.miCharacters[i]);
            if (i == 0 || i == s.length - 2) {
               this.menuCharacterSets.addSeparator();
            }
         }
      }

      this.menuView = new SmartMenu(JaveMessages.Menu_View);
      this.menuColor = new JMenu("Color");
      ColorScheme[] colorSchemes = ColorScheme.getAll();
      this.miColorSchemes = new JCheckBoxMenuItem[colorSchemes.length];
      ButtonGroup bg = new ButtonGroup();

      for (int ix = 0; ix < colorSchemes.length; ix++) {
         final ColorScheme scheme = colorSchemes[ix];
         this.miColorSchemes[ix] = new JCheckBoxMenuItem(colorSchemes[ix].getName(), colorSchemes[ix].getIcon());
         this.miColorSchemes[ix].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               application.performSetColorScheme(scheme);
            }
         });
         bg.add(this.miColorSchemes[ix]);
         this.menuColor.add(this.miColorSchemes[ix]);
      }

      this.menuView.add(new ZoomInAction(mainPanel));
      this.menuView.add(new ZoomOutAction(mainPanel));
      this.menuView.addSeparator();
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getRulerToggleAction()));
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getGridToggleAction()));
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getMarkIllegalToggleAction()));
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getConnectedLinesViewToggleAction()));
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getAuxLinesVisibilityToggleAction()));
      this.menuView.addSeparator();
      this.menuView.add(ActionWidgetFactory.createToggleMenuItem(actions.getWatermarkVisibilityToggleAction()));

      this.menuView.addSeparator();
      if (this.menuCharacterSets != null) {
         this.menuView.add(this.menuCharacterSets);
      }
      this.menuView.add(this.menuColor);
      this.menuView.add(new ChooseDisplayFontAction(preferences.getDisplayFontModel()));
      this.menuView.add(new JavePreferencesAction(preferences, platePreferences));
      this.menuWindows = new SmartMenu(JaveMessages.Menu_Window);
      this.menuSelection = new SmartMenu(JaveMessages.Menu_Selection);
      this.menuSelection.setEnabled(false);
      this.miSelectionToBrush = this.createMenuItem("Use as Brush");
      this.miSelectionExpand = this.createMenuItem("Expand");
      this.miSelectionShrink = this.createMenuItem("Shrink to fit");
      this.miSelectionDelete = this.createMenuItem("Delete");
      this.menuSelection.add(this.miSelectionToBrush);
      this.menuSelection.add(new SelectionToClipartAction(mainPanel, preferences));
      this.menuSelection.addSeparator();
      this.menuSelection.add(this.miSelectionExpand);
      this.menuSelection.add(this.miSelectionShrink);
      this.menuSelection.addSeparator();
      this.menuSelection.add(new AddBracesToSelectionAction(mainPanel));
      this.menuSelection.addSeparator();
      this.menuSelection.add(this.miSelectionDelete);
      JMenu menuTools = new SmartMenu(JaveMessages.Menu_Tools);
      menuTools.add(actions.getCamelizerAction());
      menuTools.addSeparator();
      menuTools.add(actions.getImage2AsciiAction());
      menuTools.add(actions.getFigletAction());
      menuTools.add(actions.getClipartLibraryAction());
      menuTools.add(actions.getTextBoxAction());
      menuTools.add(actions.getMathematicalExpressionsAction());
      menuTools.addSeparator();
      IFigDriver figDriver = configurationList.getRequired(IFigDriver.class);
      menuTools.add(new FigletExportWizardAction(figDriver, mainPanel, preferences));
      this.miRender3D = this.createMenuItem("Render 3D");
      this.miFunctionPlotter = this.createMenuItem("Function Plotter");
      this.miDoc2Watermark = this.createMenuItem("Set Content as Watermark");
      JMenu menuSpecial = new SmartMenu(JaveMessages.Menu_Special);
      menuSpecial.add(actions.getFractalAction());
      menuSpecial.add(this.miRender3D);
      menuSpecial.add(this.miFunctionPlotter);
      menuSpecial.addSeparator();
      Filter filter = configurationList.getRequired(Filter.class);
      menuSpecial.add(new AsctrisAction(application, filter));
      menuSpecial.add(new LabyrinthAction(application));
      menuSpecial.addSeparator();
      menuSpecial.add(new GameOfLifeAction(filter));
      menuSpecial.addSeparator();
      menuSpecial.add(this.miDoc2Watermark);
      menuSpecial.addSeparator();
      AsciiGradientConfiguration gradientConfiguration = configurationList.getRequired(AsciiGradientConfiguration.class);
      menuSpecial.add(new DecodeSteganogramAction(mainPanel, gradientConfiguration));
      JMenu menuDebug = new JMenu("Debug");
      menuDebug.add(new CrashNowAction());
      JMenu menuHelp = new SmartMenu(JaveMessages.Menu_Help);
      menuHelp.add(new OpenOnlineLocationAction("Online Documentation", "http://www.jave.de/docs/index.html"));
      menuHelp.addSeparator();
      menuHelp.add(menuDebug);
      menuHelp.addSeparator();
      menuHelp.add(new VersionCheckAction());
      menuHelp.add(actions.getAboutAction());
      this.add(fileMenu);
      this.add(animationMenu);
      this.add(menuEdit);
      this.add(this.menuModify);
      this.add(this.menuView);
      this.add(this.menuSelection);
      this.add(menuTools);
      this.add(menuSpecial);
      this.add(this.menuWindows);
      this.setHelpMenu(menuHelp);
      this.docOpenEnabledMenuItems.add(this.miClear);
      this.docOpenEnabledMenuItems.add(this.miSelectAll);
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

   private final JMenuItem createMenuItem(String label) {
      JMenuItem mi = new JMenuItem(label);
      mi.addActionListener(this);
      return mi;
   }

   private final JMenuItem createMenuItem(JaveMainPanel mainPanel, JaveAlgorithm algorithm, FontModel displayFontModel) {
      return new JMenuItem(new JaveAlgorithmAction(mainPanel, displayFontModel, algorithm));
   }

   private void updateColorMenu(ColorScheme colorScheme, IDocumentEditor editor) {
      boolean enabled = colorScheme != null && editor.getType() == JaveDocumentType.TEXT;

      for (JCheckBoxMenuItem item : this.miColorSchemes) {
         item.setEnabled(enabled);
      }

      if (colorScheme != null) {
         ColorScheme[] schemes = ColorScheme.getAll();

         for (int i = 0; i < this.miColorSchemes.length; i++) {
            if (schemes[i] == colorScheme) {
               this.miColorSchemes[i].setSelected(true);
            }
         }
      }
   }

   public void setRevertEnabled(boolean what) {
      this.miRevert.setEnabled(what);
   }

   public void updateSelectionMenu(boolean hasSelection) {
      if (this.menuSelection.isEnabled() != hasSelection) {
         this.menuSelection.setEnabled(hasSelection);
      }
      this.pasteIntoSelectionAction.setEnabled(hasSelection);
   }

   private void updateWindowsMenu() {
      this.menuWindows.removeAll();
      this.miPreviousWindow = new JMenuItem("Previous");
      this.miPreviousWindow.addActionListener(this);
      this.menuWindows.add(this.miPreviousWindow);
      this.miNextWindow = new JMenuItem("Next");
      this.miNextWindow.addActionListener(this);
      this.menuWindows.add(this.miNextWindow);
      this.menuWindows.addSeparator();
      int editorCount = this.application.getMainPanel().getEditorCount();
      boolean docOpen = editorCount > 0;
      this.menuView.setEnabled(docOpen);
      this.menuColor.setEnabled(docOpen);
      this.menuModify.setEnabled(docOpen);
      if (this.docOpenEnabledMenuItems != null) {
         for (int i = 0; i < this.docOpenEnabledMenuItems.size(); i++) {
            this.docOpenEnabledMenuItems.get(i).setEnabled(docOpen);
         }
      }

      this.miPreviousWindow.setEnabled(editorCount > 1);
      this.miNextWindow.setEnabled(editorCount > 1);
      if (!docOpen) {
         this.miRevert.setEnabled(false);
      } else {
         this.miRevert.setEnabled(this.application.getMainPanel().getEditor().getFile() != null);
         String[] menuTitles = this.getMenuTitles();
         this.windowMenuItems = new JCheckBoxMenuItem[menuTitles.length];
         ButtonGroup bg = new ButtonGroup();

         for (int i = 0; i < menuTitles.length; i++) {

            this.windowMenuItems[i] = new JCheckBoxMenuItem(menuTitles[i]);
            this.windowMenuItems[i]
               .setSelected(this.application.getMainPanel().getActiveEditorModel().getActiveEditor() == this.application.getMainPanel().getEditor(i));
            int finalI = i;
            this.windowMenuItems[i].addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  JaveMenuBar.this.application.setCurrentDocument(finalI);
               }
            });
            bg.add(this.windowMenuItems[i]);
            if (i <= 9) {
               this.windowMenuItems[i].setMnemonic(windowShortcuts[i]);
            }

            this.menuWindows.add(this.windowMenuItems[i]);
         }
      }
   }

   private String[] getMenuTitles() {
      JaveMainPanel mainPanel = this.application.getMainPanel();
      String[] result = new String[mainPanel.getEditorCount()];

      for (int i = 0; i < mainPanel.getEditorCount(); i++) {
         result[i] = i + 1 + " " + DocumentEditorTitleFactory.createShortEditorTitle(mainPanel.getEditor(i));
      }

      return result;
   }

   @Override
   public void actionPerformed(ActionEvent evt) {
      Component parentComponent = GuiUtilities.getWindowForComponent(evt);
      this.application.getMainPanel().requestFocus();
      Object source = evt.getSource();
      if (source == this.miSelectionToBrush) {
         this.application.doSelectionToBrush();
      } else if (source == this.miSelectionShrink) {
         this.application.doSelectionShrink();
      } else if (source == this.miSelectionExpand) {
         this.application.doSelectionExpand();
      } else if (source == this.miSelectionDelete) {
         this.application.doSelectionDelete();
      } else if (source == this.miReplace) {
         this.application.doReplaceCharacter();
      } else if (source == this.miDoc2Watermark) {
         this.application.doDoc2Watermark();
      } else if (source == this.miRender3D) {
         this.application.doRender3D();
      } else if (source == this.miFunctionPlotter) {
         this.application.doFunctionPlotter();
      } else if (source == this.miRevert) {
         this.application.doRevert(parentComponent);
      } else if (source == this.miSelectAll) {
         this.application.selectAll();
      } else if (source == this.miNextWindow) {
         this.application.doNextDocument();
      } else if (source == this.miPreviousWindow) {
         this.application.doPreviousDocument();
      } else if (source == this.miClose) {
         this.application.doClose(parentComponent);
      } else if (source == this.miCloseAll) {
         this.application.doCloseAll(parentComponent);
      } else {
         System.err.println("JaveMenuBar: source unknown in actionPerformed!" + source);
      }
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
