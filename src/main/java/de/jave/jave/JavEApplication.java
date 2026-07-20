package de.jave.jave;

import de.jave.asciimation.action.OpenAnimationAction;
import de.jave.core.NLS;
import de.jave.gui.StatusBar;
import de.jave.gui.io.AcceptAllFileFilter;
import de.jave.gui.io.CompositeExtensionFileFilter;
import de.jave.gui.io.ExtensionFileFilters;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.gui.io.FileSelection;
import de.jave.gui.io.IFileChooserConfiguration;
import de.jave.gui.io.SmartFileFilter;
import de.jave.gui.splash.IStartupMonitor;
import de.jave.jave.actions.JaveActions;
import de.jave.jave.actions.JaveMenuBar;
import de.jave.jave.actions.JaveTopToolbar;
import de.jave.jave.actions.ResizeDocumentAction;
import de.jave.jave.actions.ShowVtViewerAction;
import de.jave.jave.actions.ToolBar;
import de.jave.jave.actions.UndoRedoModel;
import de.jave.jave.actions.performers.IDocumentSaveListener;
import de.jave.jave.actions.performers.SavePerformer;
import de.jave.jave.actions.preferences.JavePreferencesAction;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.algorithm.rectangle.RectangleStyle;
import de.jave.jave.application.JaveStatusBar;
import de.jave.jave.application.about.JaveAboutDialog;
import de.jave.jave.application.startup.ConfigurationList;
import de.jave.jave.ascii3d.Render3DTool;
import de.jave.jave.browser.AsciiThumbnailBrowser;
import de.jave.jave.browser.JaveFileType;
import de.jave.jave.filter.Filter;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.open.JaveDropFileOpener;
import de.jave.jave.open.OpenImageFilePerformer;
import de.jave.jave.pixelplate.PixelPlateModel;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.DocumentEditorTitleFactory;
import de.jave.jave.plate.GameDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.plate.MouseCharacterModel;
import de.jave.jave.plate.TextDocumentEditor;
import de.jave.jave.preferences.AnimationExportPreferences;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.jave.preferences.PlatePreferences;
import de.jave.gui.layout.InlineOptionsWidthMeasurer;
import de.jave.jave.tool.dialog.FallbackInlineOptionsPanel;
import de.jave.jave.tool.dialog.ToolSelectorBarOptionsHost;
import de.jave.jave.tool.text.TextTool;
import de.jave.jave.version.JaveTitleProvider;
import de.jave.jave.watermark.IWatermarkPainter;
import de.jave.jave.watermark.WatermarkImageFile;
import de.jave.javeplayer.JaveAnimationFile;
import de.jave.lib.CharacterPlate;
import de.jave.lib.gui.GuiUtilities;
import de.jave.lib.gui.IStatusDisplay;
import de.jave.maxosx.IMacOsXApplicationCallbacks;
import de.jave.preferences.JavePreferences;
import de.jave.undo.UndoState;
import de.jave.util.RecentFileOpenListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import net.dizzy.commons.core.io.FileModel;
import net.dizzy.commons.core.message.IMessage;
import net.dizzy.commons.core.message.Message;
import net.dizzy.commons.core.message.MessageType;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.message.MessageDialogFactory;
import net.dizzy.commons.swing.dialog.message.MessageDialogUtilities;
import net.dizzy.commons.swing.dialog.message.YesNoCancel;
import net.dizzy.commons.swing.mousecursor.CursorId;
import net.dizzy.commons.swing.mousecursor.CursorProvider;

public class JavEApplication implements RecentFileOpenListener, IToolManager {
   private final JaveMainPanel mainPanel;
   private final DocumentManager documentManager;
   private final ToolBar toolBar;
   private TextboxDialog textboxDialog;
   private ReplaceCharacterDialog replaceCharacterDialog;
   private AsciiThumbnailBrowser thumbnailBrowser;
   private final JaveActions actions;
   private JaveMenuBar menuBar;
   private final StatusBar status;
   private final JFrame frame;
   private final JavePreferences javePreferences;
   private final JaveApplicationPreferences applicationPreferences;
   private final JaveTopToolbar topToolbar;
   private ToolSelectorBarOptionsHost toolSelectorBarOptionsHost;
   private final BooleanModel watermarkVisibilityModel;
   private final BooleanModel auxLinesVisibilityModel;
   private final PlatePreferences platePreferences;
   private final ConfigurationList configurationList;
   private final CharacterSets characterSets;
   private final UndoRedoModel undoRedoModel;
   private final JaveStatusBar statusBar;
   private final PixelPlateModel pixelPlateModel = new PixelPlateModel();

   public JavEApplication(ConfigurationList configurationList) {
      Ensure.ensureArgumentNotNull(configurationList);
      this.configurationList = configurationList;
      CharSetsConfiguration charSetsConfiguration = configurationList.getRequired(CharSetsConfiguration.class);
      this.characterSets = new CharacterSets(charSetsConfiguration);
      this.frame = new JFrame();
      this.frame.setTitle(JaveTitleProvider.TITLE);
      this.status = new StatusBar();
      this.frame.setIconImages(JaveIcons.JAVE_ICON_IMAGES);
      this.javePreferences = new JavePreferences();
      this.applicationPreferences = new JaveApplicationPreferences(this.javePreferences);
      FileModel currectDirectoryModel = this.applicationPreferences.getCurrectDirectoryModel();
      this.watermarkVisibilityModel = new BooleanModel();
      this.auxLinesVisibilityModel = new BooleanModel();
      this.documentManager = new DocumentManager(currectDirectoryModel, this.applicationPreferences.getDefaultColorSchemeModel());
      this.platePreferences = new PlatePreferences(this.javePreferences);
      MouseCharacterModel mouseCharacterModel = new MouseCharacterModel();
      this.mainPanel = new JaveMainPanel(this, this.status, mouseCharacterModel);
      this.undoRedoModel = new UndoRedoModel(this.mainPanel);
      this.mainPanel.getActiveEditorModel().addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JavEApplication.this.updateFrameTitle();
         }
      });
      this.actions = new JaveActions(
         this,
         configurationList,
         this.javePreferences,
         this.platePreferences,
         this.applicationPreferences.getDisplayFontModel(),
         this.applicationPreferences.getDefaultColorSchemeModel()
      );
      ResizeDocumentAction resizeAction = this.actions.getResizeAction();
      this.statusBar = new JaveStatusBar(
         this,
         this.applicationPreferences.getDisplayFontModel(),
         this.mainPanel.getActiveEditorModel(),
         this.status,
         resizeAction
      );
      this.topToolbar = new JaveTopToolbar(this, this.actions, this.undoRedoModel);
      FallbackInlineOptionsPanel fallback = new FallbackInlineOptionsPanel();
      this.toolSelectorBarOptionsHost = new ToolSelectorBarOptionsHost(fallback);
      this.toolBar = new ToolBar(this, this.applicationPreferences, configurationList, this.platePreferences, this.toolSelectorBarOptionsHost);
      java.util.List<JComponent> measuredPanels = new java.util.ArrayList<>();
      measuredPanels.add(fallback.getContent());
      for (Tool tool : this.mainPanel.getToolManager().getTools()) {
         de.jave.jave.tool.dialog.IInlineToolOptions inline = tool.getInlineOptionsPanel();
         if (inline != null) {
            measuredPanels.add(inline.getContent());
         }
      }
      if (System.getProperty("jave.dump.tool_option_widths") != null) {
         this.dumpToolOptionWidthsAndExit(fallback);
      }
      // Defense-in-depth: even if a future jave.dump.* path doesn't exit
      // here, isDumpModeActive() suppresses interactive prompts downstream
      // (e.g. crash-recovery in startupRecovery).
      this.toolSelectorBarOptionsHost.setMinWidth(InlineOptionsWidthMeasurer.measureMaxWidth(measuredPanels));
      this.watermarkVisibilityModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            boolean v = JavEApplication.this.watermarkVisibilityModel.getValue();
            ((IWatermarkPainter) JavEApplication.this.mainPanel.getToolManager().getTool(ToolBar.WATERMARK_TOOL_INDEX)).setEnabled(v);
            JavEApplication.this.mainPanel.repaint();
         }
      });
      this.auxLinesVisibilityModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            boolean v = JavEApplication.this.auxLinesVisibilityModel.getValue();
            ((IWatermarkPainter) JavEApplication.this.mainPanel.getToolManager().getTool(ToolBar.AUXILIARY_LINES_TOOL_INDEX)).setEnabled(v);
            JavEApplication.this.mainPanel.repaint();
         }
      });
      this.toolSelectorBarOptionsHost.setTool(this.mainPanel.getCurrentTool());
      JaveDropFileOpener.attachTo(this, this.mainPanel.getContent());
      JComponent bottomPanel = this.statusBar.getContent();
      this.frame.getContentPane().setLayout(new BorderLayout());
      this.frame.getContentPane().add(this.topToolbar.getContent(), "North");
      this.frame.getContentPane().add(this.toolBar.getContent(), "West");
      this.frame.getContentPane().add(this.mainPanel.getContent(), "Center");
      this.frame.getContentPane().add(bottomPanel, "South");
      this.installToolShortcuts();
      this.frame.pack();
   }

   private void installToolShortcuts() {
      javax.swing.JComponent root = this.frame.getRootPane();
      javax.swing.InputMap im = root.getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
      javax.swing.ActionMap am = root.getActionMap();
      im.put(de.jave.jave.actions.JaveKeyBindings.SELECT_TOOL, "jave.tool.selection");
      im.put(de.jave.jave.actions.JaveKeyBindings.FREEHAND_SELECT_TOOL, "jave.tool.freehandSelection");
      im.put(de.jave.jave.actions.JaveKeyBindings.TEXT_TOOL, "jave.tool.text");
      am.put("jave.tool.selection", new javax.swing.AbstractAction() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent e) {
            JavEApplication.this.setTool(ToolBar.SELECTION_TOOL_INDEX);
         }
      });
      am.put("jave.tool.freehandSelection", new javax.swing.AbstractAction() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent e) {
            JavEApplication.this.setTool(ToolBar.FREEHAND_SELECTION_TOOL_INDEX);
         }
      });
      am.put("jave.tool.text", new javax.swing.AbstractAction() {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent e) {
            JavEApplication.this.setTool(ToolBar.TEXT_TOOL_INDEX);
         }
      });
   }

   public JaveActions getActions() {
      return this.actions;
   }

   public void performSetColorScheme(ColorScheme colorScheme) {
      PlateDocument doc = this.mainPanel.getDocument();
      if (doc != null) {
         ColorScheme old = doc.getColorScheme();
         if (old != colorScheme) {
            doc.setColorScheme(colorScheme);
            this.mainPanel.saveCurrentState(JaveMessages.Action_ChangeColor_UndoName);
         }
      }

      this.mainPanel.repaint();
   }

   public boolean startupRecovery(IStartupMonitor startupMonitor) {
      if (isDumpModeActive()) {
         return false;
      }
      if (!JaveStatusFile.exists()) {
         return false;
      } else {
         String[] statusData = JaveStatusFile.load();
         if (statusData != null && !statusData[1].equals("0")) {
            int fileCount = Integer.parseInt(statusData[1]);
            String date = statusData[0];
            String question;
            if (fileCount == 1) {
               question = NLS.bind(JaveMessages.CrashRecovery_OneDocumentOpenMessageText, date);
            } else {
               question = NLS.bind(JaveMessages.CrashRecovery_MultipleDocumentsOpenMessageText, date, fileCount);
            }

            Component parentComponent = null;
            IMessage message = new Message(JaveMessages.CrashRecovery_DialogTitle, question, MessageType.WARNING);
            startupMonitor.dispose();
            YesNoCancel result = MessageDialogUtilities.showYesNoCancelDialog(parentComponent, message);
            if (result == YesNoCancel.CANCEL) {
               System.exit(0);
            }

            if (result == YesNoCancel.NO) {
               return false;
            } else {
               this.switchToSelectonTool();
               PlateDocument doc = null;

               for (int i = 2; i < statusData.length; i += 2) {
                  doc = this.documentManager.createNew(this.applicationPreferences.getDefaultDocumentSize());
                  doc.setModified(true);
                  if (statusData[i] != null && statusData[i].length() > 0) {
                     doc.setFile(new File(statusData[i]));
                  }

                  CompressedDocumentState[] docStates = JaveLogFileParser.load(new File(statusData[i + 1]));
                  ColorScheme colorScheme = docStates.length > 0
                     ? docStates[0].getColorScheme()
                     : this.applicationPreferences.getDefaultColorSchemeModel().getValue();
                  TextDocumentEditor editor = new TextDocumentEditor(
                     DocumentDefaultTitleFactory.createDefaultDocumentTitle(),
                     doc,
                     this,
                     this.platePreferences,
                     this.mainPanel.getToolManager(),
                     this.applicationPreferences.getDisplayFontModel(),
                     colorScheme,
                     this.characterSets
                  );
                  this.mainPanel.addEditor(editor);
                  if (docStates != null && docStates.length > 0) {
                     for (int j = 0; j < docStates.length; j++) {
                        doc.getUndoManager().saveCurrentState(docStates[j]);
                     }

                     doc.setDocumentState(docStates[docStates.length - 1]);
                  }
               }

               this.updateStatusFile();
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void startupMenuBar() {
      this.menuBar = new JaveMenuBar(
         this,
         this.configurationList,
         this.javePreferences,
         this.mainPanel,
         this.actions,
         this.applicationPreferences.getRecentFileList(),
         this.applicationPreferences,
         this.platePreferences,
         this.characterSets,
         this.undoRedoModel
      );
      this.frame.setJMenuBar(this.menuBar);
      this.updateUndoRedo();
      this.updateSelectionMenu();
   }

   public void startupFinish2() {
      this.frame.setDefaultCloseOperation(0);
      this.frame.addWindowListener(new WindowAdapter() {
         @Override
         public void windowClosing(WindowEvent e) {
            JavEApplication.this.performExit(JavEApplication.this.frame);
         }
      });
      this.installSystemQuitHandler();
      this.installPreferencesShutdownHook();
      this.actions.getCamelizerSessionManager().installAppForegroundListener(this.frame);
      this.frame.setBounds(this.applicationPreferences.getApplicationFrameBounds());
      this.frame.setExtendedState(this.applicationPreferences.getApplicationFrameState());
      this.frame.setVisible(true);
   }

   /**
    * Route OS-level quit requests (e.g. macOS Cmd+Q, Dock > Quit, system logout)
    * through {@link #performExit} so unsaved-document prompts run and preferences
    * get flushed. Without this the JVM is killed before performExit fires and
    * any in-memory preference changes are lost.
    */
   private void installSystemQuitHandler() {
      if (!Desktop.isDesktopSupported()) {
         return;
      }
      Desktop desktop = Desktop.getDesktop();
      if (!desktop.isSupported(Desktop.Action.APP_QUIT_HANDLER)) {
         return;
      }
      desktop.setQuitHandler((event, response) -> {
         boolean exited = JavEApplication.this.performExit(JavEApplication.this.frame);
         if (!exited) {
            response.cancelQuit();
         }
         // On success performExit calls System.exit; response.performQuit()
         // would otherwise also terminate the JVM, but we never reach here.
      });
   }

   /**
    * Backstop for unclean exits (force quit, SIGTERM, IDE stop) so preferences
    * get flushed even when {@link #performExit} is bypassed. Best-effort only;
    * unsaved-document prompts are not run here.
    */
   private void installPreferencesShutdownHook() {
      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
         try {
            JavEApplication.this.javePreferences.flush();
            JavEApplication.this.applicationPreferences.flush();
            JavEApplication.this.platePreferences.flush();
            JavEApplication.this.applicationPreferences.getRecentFileList().flush();
         } catch (Throwable ignored) {
         }
      }, "jave-prefs-flush"));
   }

   public void startupFinish3() {
      this.applicationPreferences.getRecentFileList().setRecentFileOpenListener(this);
   }

   public void updateSelectionMenu() {
      if (this.menuBar != null) {
         this.menuBar.updateSelectionMenu(this.mainPanel.hasSelection());
      }
   }

   public void updateFrameTitle() {
      this.mainPanel.updateAllDocumentTitles();
      IDocumentEditor activeEditor = this.mainPanel.getActiveEditorModel().getActiveEditor();
      if (activeEditor == null) {
         this.frame.setTitle(JaveTitleProvider.TITLE);
      } else {
         String title = DocumentEditorTitleFactory.createEditorFrameTitle(activeEditor);
         this.frame.setTitle(JaveTitleProvider.TITLE + " - " + title);
      }
   }

   public void updateSizeLabelToDocumentSize() {
      this.statusBar.updateSizeLabelToDocumentSize();
   }

   public void doSelectionDelete() {
      if (!this.mainPanel.hasSelection()) {
         this.mainPanel.selectAll();
      }

      this.mainPanel.getCurrentTool().setCursor(CursorProvider.getInstance().getCursor(CursorId.CROSSHAIR_SELECTION));
      this.mainPanel.unselect();
      this.mainPanel.saveCurrentState(JaveMessages.Edit_Delete_UndoName);
   }

   public void doSelectionShrink() {
      if (!this.mainPanel.hasSelection()) {
         this.mainPanel.selectAll();
      }

      this.mainPanel.shrinkSelection();
      this.mainPanel.saveCurrentState(JaveMessages.Edit_ShrinkSelection_UndoName);
   }

   public void doSelectionExpand() {
      if (this.mainPanel.hasSelection()) {
         this.mainPanel.expandSelection();
         this.mainPanel.saveCurrentState(JaveMessages.Edit_ExpandSelection_UndoName);
      }
   }

   public void doSelectionToBrush() {
      if (this.mainPanel.hasSelection()) {
         CharacterPlate cp = this.mainPanel.getContentOfInterest().getContent();
         if (cp.getWidth() <= 12 && cp.getHeight() <= 12 && cp.getHeight() * cp.getWidth() <= 100) {
            this.setTool(ToolBar.BRUSH_TOOL_INDEX);
            ((BrushTool)this.mainPanel.getCurrentTool()).setBrush(cp);
         } else {
            MessageDialogFactory.showMessageDialog(
               this.frame, new Message(JaveMessages.JavE, JaveMessages.Tool_Brush_SelectionTooBigText, MessageType.INFORMATION)
            );
         }
      }
   }

   public void doReplaceCharacter() {
      if (this.replaceCharacterDialog == null) {
         this.replaceCharacterDialog = new ReplaceCharacterDialog(this.mainPanel, this.frame);
         GuiUtilities.centerOnScreen(this.replaceCharacterDialog.getDialog());
      }

      this.replaceCharacterDialog.show();
   }

   public boolean performExit(Component parentComponent) {
      boolean success = this.doCloseAll(parentComponent);
      if (!success) {
         return false;
      } else {
         if (!JaveStatusFile.delete()) {
            System.err.println("Unable to delete Status-File!");
         }

         JaveStatusFile.deleteAllLogFiles();
         this.applicationPreferences.setApplicationFrameState(this.frame.getExtendedState(), this.frame.getBounds());
         this.javePreferences.flush();
         this.applicationPreferences.flush();
         this.platePreferences.flush();
         this.applicationPreferences.getRecentFileList().flush();
         this.dispose();
         System.exit(0);
         return true;
      }
   }

   public void doNew() {
      this.doNew(this.applicationPreferences.getDefaultDocumentSize(), null);
   }

   public void doNew(java.awt.Dimension size, Character fillCharacter) {
      PlateDocument doc = this.documentManager.createNew(size);
      if (fillCharacter != null) {
         doc.getContent().fill(0, 0, size.width, size.height, fillCharacter.charValue());
      }
      TextDocumentEditor editor = new TextDocumentEditor(
         DocumentDefaultTitleFactory.createDefaultDocumentTitle(),
         doc,
         this,
         this.platePreferences,
         this.mainPanel.getToolManager(),
         this.applicationPreferences.getDisplayFontModel(),
         this.applicationPreferences.getDefaultColorSchemeModel().getValue(),
         this.characterSets
      );
      this.mainPanel.addEditor(editor);
      this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
      this.updateStatusFile();
   }

   public void updateStatusFile() {
      JaveStatusFile.saveLog(this.documentManager);
   }

   public boolean doClose(Component parentComponent) {
      IDocumentEditor editor = this.mainPanel.getEditor();
      if (!editor.isModified()) {
         this.documentManager.closeCurrentDocument();
         this.mainPanel.closeCurrentEditor();
         this.updateStatusFile();
         return true;
      } else {
         boolean success = SavePerformer.performSaveBeforeClose(
            parentComponent,
            editor,
            this.applicationPreferences.getRecentFileList(),
            this.documentManager.getCurrentDirectoryModel(),
            this.status,
            this.getDocumentSaveListener()
         );
         return success ? this.doClose(parentComponent) : false;
      }
   }

   public boolean doCloseAll(Component parentComponent) {
      boolean success = true;

      while (this.mainPanel.getEditor() != null && success) {
         success = this.doClose(parentComponent);
         if (!success) {
            return false;
         }
      }

      return success;
   }

   public void close(PlateDocument doc) {
      int index = this.documentManager.getIndexOf(doc);
      if (index >= 0) {
         this.documentManager.closeDocument(index);
         this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
         this.updateStatusFile();
      }
   }

   @Override
   public void openRecentFile(Component parentComponent, File file) {
      this.open(parentComponent, file);
   }

   public void editTextBox(String content, Point location, RectangleStyle textboxStyle) {
      if (this.textboxDialog == null) {
         this.textboxDialog = new TextboxDialog(this, content, location);
         GuiUtilities.centerOnScreen(this.textboxDialog.getDialog());
      } else {
         this.textboxDialog.setContent(content);
         this.textboxDialog.setLocation(location);
      }

      this.textboxDialog.setTextboxStyle(textboxStyle);
      this.textboxDialog.show();
   }

   public void showAboutDialog() {
      JaveAboutDialog.showAboutDialog(this.frame);
   }

   public void updateUndoRedo() {
      this.undoRedoModel.fireChangeEvent();
   }

   public void doRedo() {
      this.mainPanel.redo();
      this.updateUndoRedo();
      this.updateSelectionMenu();
      this.mainPanel.requestFocus();
   }

   public void doUndo() {
      this.mainPanel.undo();
      this.updateUndoRedo();
      this.updateSelectionMenu();
      this.mainPanel.requestFocus();
   }

   public void doDoc2Watermark() {
      if (this.mainPanel.hasSelection()) {
         this.mainPanel.dropSelection();
      }

      CharacterPlate cp = this.mainPanel.getContentOfInterest().getContent();
      Font font = this.mainPanel.getPlate().getFont();
      ColorScheme colorScheme = this.mainPanel.getPlate().getDocument().getColorScheme();
      BufferedImage image = AsciiToThumbnailConverter.convert(
         cp, font.getSize(), font, colorScheme.getColorPlateBackground(), colorScheme.getColorText(), this.isConnectedLinesView()
      );
      cp.clear();
      this.mainPanel.setContentOfInterest(cp);
      this.mainPanel.saveCurrentState(JaveMessages.Action_ClearContent_UndoName);
      this.setWatermarkImage(new WatermarkImageFile(image));
   }

   public void setWatermarkImage(WatermarkImageFile imageFile) {
      this.setTool(ToolBar.WATERMARK_TOOL_INDEX);
      WatermarkTool tool = (WatermarkTool)this.mainPanel.getToolManager().getTool(ToolBar.WATERMARK_TOOL_INDEX);
      tool.setImage(imageFile);
      tool.fit();
      this.toolBar.setWatermarkVisible(true);
   }

   public void addSecondaryLayerAndActivate() {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      document.addSecondaryLayerAboveActive();
      document.setModified(true);
      document.documentChanged();
      this.status.showStatus("Added Layer " + document.getActiveLayerNumber());
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void activateLayerNumber(int layerNumber) {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null || document.getActiveLayerNumber() == layerNumber) {
         return;
      }
      document.activateLayerNumber(layerNumber);
      document.documentChanged();
      this.status.showStatus("Editing Layer " + document.getActiveLayerNumber());
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void renameLayer(String layerId, String name) {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      if (!document.renameLayer(layerId, name)) {
         this.mainPanel.requestFocus();
         return;
      }
      this.mainPanel.saveCurrentState("rename layer");
      this.status.showStatus("Renamed layer");
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void flattenLayers(boolean includeHiddenSecondaryLayers) {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      if (!document.hasSecondaryLayers()) {
         this.status.showStatus("Document has one layer");
         this.mainPanel.requestFocus();
         return;
      }
      document.flattenLayers(includeHiddenSecondaryLayers);
      this.mainPanel.saveCurrentState(includeHiddenSecondaryLayers ? "flatten layers" : "flatten visible layers");
      this.status.showStatus(includeHiddenSecondaryLayers ? "Flattened layers" : "Flattened visible layers");
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void duplicateActiveLayer() {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      document.duplicateActiveLayer();
      this.mainPanel.saveCurrentState("duplicate layer");
      this.status.showStatus("Duplicated Layer " + document.getActiveLayerNumber());
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void deleteActiveLayer() {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      if (!document.deleteActiveLayer()) {
         this.status.showStatus("Document layer cannot be deleted");
         this.mainPanel.requestFocus();
         return;
      }
      this.mainPanel.saveCurrentState("delete layer");
      this.status.showStatus("Deleted layer");
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void moveActiveLayerUp() {
      this.moveActiveLayer(true);
   }

   public void moveActiveLayerDown() {
      this.moveActiveLayer(false);
   }

   private void moveActiveLayer(boolean up) {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      boolean moved = up ? document.moveActiveLayerUp() : document.moveActiveLayerDown();
      if (!moved) {
         this.status.showStatus(up ? "Layer cannot move up" : "Layer cannot move down");
         this.mainPanel.requestFocus();
         return;
      }
      this.mainPanel.saveCurrentState(up ? "move layer up" : "move layer down");
      this.status.showStatus("Moved Layer " + document.getActiveLayerNumber());
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void toggleActiveLayerVisibility() {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      if (!document.toggleActiveLayerVisibility()) {
         this.status.showStatus("Document layer visibility cannot be changed");
         this.mainPanel.requestFocus();
         return;
      }
      String actionName = document.isActiveLayerVisible() ? "show layer" : "hide layer";
      this.mainPanel.saveCurrentState(actionName);
      this.status.showStatus(document.isActiveLayerVisible() ? "Layer shown" : "Layer hidden");
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void setActiveLayerOpaque(boolean opaque) {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      if (!document.setActiveLayerOpaque(opaque)) {
         this.status.showStatus("Document layer is always opaque");
         this.mainPanel.requestFocus();
         return;
      }
      this.mainPanel.saveCurrentState(opaque ? "make layer opaque" : "make layer non-opaque");
      this.status.showStatus(opaque ? "Layer opaque" : "Layer non-opaque");
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public void activateNextLayer() {
      PlateDocument document = this.mainPanel.getDocument();
      if (document == null) {
         return;
      }
      document.activateNextLayer();
      document.documentChanged();
      this.status.showStatus("Editing Layer " + document.getActiveLayerNumber());
      this.mainPanel.repaint();
      this.mainPanel.requestFocus();
   }

   public boolean isLayersPanelVisible() {
      TextDocumentEditor editor = this.getActiveTextDocumentEditor();
      return editor != null && editor.isLayersPanelVisible();
   }

   public void setLayersPanelVisible(boolean visible) {
      TextDocumentEditor editor = this.getActiveTextDocumentEditor();
      if (editor == null) {
         return;
      }
      editor.setLayersPanelVisible(visible);
      this.javePreferences.setLayersPanelShownByDefault(visible);
      this.mainPanel.requestFocus();
   }

   private TextDocumentEditor getActiveTextDocumentEditor() {
      IDocumentEditor editor = this.mainPanel.getActiveEditorModel().getActiveEditor();
      return editor instanceof TextDocumentEditor ? (TextDocumentEditor)editor : null;
   }

   public void doLoadWatermark() {
      this.setTool(ToolBar.WATERMARK_TOOL_INDEX);
      this.toolBar.setWatermarkVisible(true);
      WatermarkTool tool = (WatermarkTool)this.mainPanel.getToolManager().getTool(ToolBar.WATERMARK_TOOL_INDEX);
      tool.performLoadImage(this.frame);
   }

   public boolean isConnectedLinesView() {
      return this.platePreferences.getConnectedLinesViewModel().getValue();
   }

   public IDocumentSaveListener getDocumentSaveListener() {
      return new IDocumentSaveListener() {
         @Override
         public void savePerformed() {
            JavEApplication.this.menuBar.setRevertEnabled(JavEApplication.this.mainPanel.getDocument().hasFile());
            JavEApplication.this.updateFrameTitle();
         }
      };
   }

   public void doBrowse() {
      if (this.thumbnailBrowser != null) {
         this.thumbnailBrowser.setVisible(true);
      } else {
         this.thumbnailBrowser = new AsciiThumbnailBrowser(this.frame, this, this.getDocumentManager().getCurrentDirectoryModel());
         this.thumbnailBrowser.show();
      }
   }

   public void doRevert(Component parentComponent) {
      PlateDocument doc = this.documentManager.getCurrentDocument();
      boolean ok = MessageDialogUtilities.showOkCancelDialog(
         parentComponent, new Message(JaveMessages.JavE, JaveMessages.Action_Revert_LoseAllChanges_QuestionText, MessageType.QUESTION)
      );
      if (ok) {
         doc.setModified(false);
         File file = doc.getFile();
         this.doClose(parentComponent);
         this.open(parentComponent, file);
      }
   }

   public void doOpen(Component parentComponent) {
      FileSelection fileSelection = FileChooserUtilities.performOpenFileChooser(
         parentComponent,
         new IFileChooserConfiguration() {
            @Override
            public FileModel getCurrentDirectoryModel() {
               return JavEApplication.this.documentManager.getCurrentDirectoryModel();
            }

            @Override
            public String getSaveDialogTitle() {
               return null;
            }

            @Override
            public String getOpenDialogTitle() {
               return JaveMessages.OpenDialog_Title;
            }

            @Override
            public SmartFileFilter[] getFileFilters() {
               return new SmartFileFilter[]{
                  new CompositeExtensionFileFilter(
                     JaveMessages.FileFormat_All,
                     ExtensionFileFilters.TXT,
                     ExtensionFileFilters.JAVEDOC,
                     ExtensionFileFilters.SUPPORTED_IMAGES,
                     ExtensionFileFilters.JMOV,
                     ExtensionFileFilters.VT
                  ),
                  ExtensionFileFilters.TXT,
                  ExtensionFileFilters.JAVEDOC,
                  ExtensionFileFilters.SUPPORTED_IMAGES,
                  ExtensionFileFilters.JMOV,
                  ExtensionFileFilters.VT,
                  new AcceptAllFileFilter()
               };
            }

            @Override
            public String getFileNameSuggestion() {
               return null;
            }

            @Override
            public boolean isMultipleOpenFileSelectionAllowed() {
               return false;
            }
         }
      );
      if (!fileSelection.isEmpty()) {
         this.open(parentComponent, fileSelection.getFile());
      }
   }

   public void open(Component parentComponent, File file) {
      JaveFileType fileType = JaveFileType.guessType(file);
      if (fileType == JaveFileType.ANIMATION) {
         JaveAnimationFile animationFile = OpenAnimationAction.open(file, parentComponent);
         if (animationFile != null) {
            this.applicationPreferences.getRecentFileList().add(file);
            this.openJaveAnimation(animationFile);
         }
      } else if (fileType == JaveFileType.VT) {
         ShowVtViewerAction action = new ShowVtViewerAction(this.applicationPreferences.getCurrectDirectoryModel());
         action.execute(parentComponent, file);
      } else if (fileType == JaveFileType.RASTER_IMAGE) {
         OpenImageFilePerformer.performOpenImageFile(parentComponent, file, this, this.actions, this.mainPanel.getToolManager());
      } else if (this.documentManager.isAlreadyOpen(file)) {
         MessageDialogFactory.showMessageDialog(
            parentComponent, new Message(JaveMessages.JavE, JaveMessages.Action_Open_SelectedFileAlreadyOpen_MessageText, MessageType.INFORMATION)
         );
      } else {
         try {
            PlateDocument doc = this.documentManager.load(file, this.applicationPreferences.getRecentFileList());
            TextDocumentEditor editor = new TextDocumentEditor(
               DocumentDefaultTitleFactory.createDefaultDocumentTitle(file.getName()),
               doc,
               this,
               this.platePreferences,
               this.mainPanel.getToolManager(),
               this.applicationPreferences.getDisplayFontModel(),
               this.applicationPreferences.getDefaultColorSchemeModel().getValue(),
               this.characterSets
            );
            this.mainPanel.addEditor(editor);
            this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
            doc.getUndoManager().getLogFile().append(this.mainPanel.getDocumentState(null));
            this.updateStatusFile();
            this.status.showStatus(JaveMessages.Action_Open_FileLoaded_StatusMessage);
         } catch (IOException var6) {
            MessageDialogFactory.showMessageDialog(
               parentComponent,
               new Message(JaveMessages.JavE, NLS.bind(JaveMessages.Action_Open_ErrorLoadingFile_MessageText, var6.toString()), MessageType.ERROR, var6)
            );
         } catch (OutOfMemoryError var7) {
            MessageDialogFactory.showMessageDialog(
               parentComponent, new Message(JaveMessages.JavE, JaveMessages.Action_Open_ErrorLoadingFile_TooBig_MessageText, MessageType.ERROR)
            );
         }
      }
   }

   public void openJaveAnimation(JaveAnimationFile animationFile) {
      PlateDocument document = this.documentManager.createNew(this.applicationPreferences.getDefaultAnimationSize());
      String stopGapName = DocumentDefaultTitleFactory.createDefaultDocumentTitle(animationFile.getFile());
      AnimationDocumentEditor editor = new AnimationDocumentEditor(
         stopGapName,
         animationFile,
         this,
         document,
         this.platePreferences,
         this.applicationPreferences,
         this.mainPanel.getToolManager(),
         new AnimationExportPreferences(this.javePreferences),
         this.applicationPreferences.getCurrectDirectoryModel(),
         this.characterSets
      );
      this.mainPanel.addEditor(editor);
      this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
      this.updateStatusFile();
   }

   public void doFractal() {
      Filter filter = this.configurationList.getRequired(Filter.class);
      FractalTool dt = new FractalTool(this, this.applicationPreferences, filter);
      dt.show();
   }

   public void doRender3D() {
      AsciiGradientConfiguration gradientConfiguration = this.configurationList.getRequired(AsciiGradientConfiguration.class);
      Filter filter = this.configurationList.getRequired(Filter.class);
      Render3DTool dt = new Render3DTool(this, this.applicationPreferences, gradientConfiguration, filter);
      dt.show();
   }

   public void doFunctionPlotter() {
      Filter filter = this.configurationList.getRequired(Filter.class);
      FunctionPlotTool dt = new FunctionPlotTool(this, this.applicationPreferences, filter);
      dt.show();
   }

   public void doPreviousDocument() {
      int index = this.documentManager.getCurrentDocumentIndex() - 1;
      if (index < 0) {
         index = this.documentManager.getSize() - 1;
      }

      this.setCurrentDocument(index);
   }

   public void doNextDocument() {
      int index = this.documentManager.getCurrentDocumentIndex() + 1;
      if (index >= this.documentManager.getSize()) {
         index = 0;
      }

      this.setCurrentDocument(index);
   }

   public void setCurrentDocument(int index) {
      PlateDocument d = this.mainPanel.getDocument();
      if (d != null) {
         d.documentHiding();
      }

      if (index >= 0 && index < this.documentManager.getSize()) {
         this.documentManager.setCurrentDocument(index);
         PlateDocument currentDoc = this.documentManager.getCurrentDocument();
         this.mainPanel.setCurrentEditor(index);
         this.updateUndoRedo();
         this.mainPanel.repaint();
         currentDoc.documentShowing();
      } else {
         this.updateUndoRedo();
      }
   }

   public void selectAll() {
      this.mainPanel.selectAll();
      this.switchToSelectonTool();
      this.mainPanel.saveCurrentState(JaveMessages.Edit_SelectAll_UndoName);
   }

   public void switchToSelectonTool() {
      this.setTool(ToolBar.SELECTION_TOOL_INDEX);
      ((SelectionTool)this.mainPanel.getCurrentTool()).synchronizeToSelection();
   }

   @Override
   public void switchToTextTool(int x, int y) {
      this.setTool(ToolBar.TEXT_TOOL_INDEX);
      ((TextTool)this.mainPanel.getCurrentTool()).setCursorLocation(x, y);
   }

   public void switchToTextTool(char ch, int x, int y) {
      this.setTool(ToolBar.TEXT_TOOL_INDEX);
      TextTool textTool = (TextTool)this.mainPanel.getCurrentTool();
      textTool.setCursorLocation(x, y);
      textTool.checkSize();
      textTool.charEntered(ch);
   }

   public void switchToTextTool() {
      this.setTool(ToolBar.TEXT_TOOL_INDEX);
   }

   public SelectionTool getSelectionTool() {
      return (SelectionTool)this.mainPanel.getToolManager().getTool(ToolBar.SELECTION_TOOL_INDEX);
   }

   public void setTool(int toolIndex) {
      if (this.mainPanel.getToolManager().getCurrentToolIndex() != toolIndex) {
         Tool newTool = this.mainPanel.getToolManager().getTool(toolIndex);
         if (this.toolSelectorBarOptionsHost != null) {
            this.toolSelectorBarOptionsHost.setTool(newTool);
         }

         this.toolBar.selectToolButton(toolIndex);
         this.mainPanel.setCurrentTool(newTool);
         this.updateSelectionMenu();
      }
   }

   public Plate pasteAsNewDocument(CharacterPlate content) {
      return this.pasteAsNewDocument(content, null);
   }

   public Plate pasteAsNewDocument(CharacterPlate content, String documentName) {
      PlateDocument doc = this.documentManager.createNew(this.applicationPreferences.getDefaultDocumentSize());
      String stopGapName = DocumentDefaultTitleFactory.createDefaultDocumentTitle(documentName);
      doc.setContent(content);
      TextDocumentEditor editor = new TextDocumentEditor(
         stopGapName,
         doc,
         this,
         this.platePreferences,
         this.mainPanel.getToolManager(),
         this.applicationPreferences.getDisplayFontModel(),
         this.applicationPreferences.getDefaultColorSchemeModel().getValue(),
         this.characterSets
      );
      this.mainPanel.addEditor(editor);
      this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
      this.updateStatusFile();
      return editor.getPlate();
   }

   public Plate pasteAsNewGameDocument(CharacterPlate content, String documentName) {
      PlateDocument doc = this.documentManager.createNew(this.applicationPreferences.getDefaultDocumentSize());
      doc.setContent(content);
      GameDocumentEditor editor = new GameDocumentEditor(
         DocumentDefaultTitleFactory.createDefaultDocumentTitle(documentName),
         doc,
         this,
         this.platePreferences,
         this.mainPanel.getToolManager(),
         this.applicationPreferences.getDisplayFontModel(),
         this.applicationPreferences.getDefaultColorSchemeModel(),
         this.characterSets
      );
      this.mainPanel.addEditor(editor);
      this.setCurrentDocument(this.documentManager.getCurrentDocumentIndex());
      this.updateStatusFile();
      return editor.getPlate();
   }

   public void pasteAsNewSelection(CharacterPlate content, int x, int y) {
      this.pasteAsNewSelection(content, new Point(x, y));
   }

   public void pasteAsNewSelection(CharacterPlate content, Point location) {
      if (this.mainPanel.getDocument() == null) {
         this.pasteAsNewDocument(content);
      } else {
         this.mainPanel.pasteAsNewSelection(content, location);
         this.switchToSelectonTool();
         this.updateSelectionMenu();
         this.mainPanel.saveCurrentState(JaveMessages.Edit_Paste_UndoName);
      }
   }

   public void pasteAsNewSelection(CharacterPlate content) {
      this.pasteAsNewSelection(content, this.mainPanel.getPasteLocation());
   }

   public void toggleInsert() {
      boolean insert = !Tool.isInsert();
      this.statusBar.setInsert(insert);
      this.mainPanel.getCurrentTool().setInsert(insert);
      this.mainPanel.repaintCursor();
   }

   public void showTextboxDialog() {
      if (this.textboxDialog == null) {
         this.textboxDialog = new TextboxDialog(this);
         GuiUtilities.centerOnScreen(this.textboxDialog.getDialog());
      }

      this.textboxDialog.show();
   }

   public void dispose() {
      this.frame.dispose();
   }

   public JFrame getFrame() {
      return this.frame;
   }

   public void toFront() {
      this.frame.toFront();
   }

   public DocumentManager getDocumentManager() {
      return this.documentManager;
   }

   public ToolBar getToolBar() {
      return this.toolBar;
   }

   public BooleanModel getWatermarkVisibilityModel() {
      return this.watermarkVisibilityModel;
   }

   public BooleanModel getAuxLinesVisibilityModel() {
      return this.auxLinesVisibilityModel;
   }

   public PixelPlateModel getPixelPlateModel() {
      return this.pixelPlateModel;
   }

   public JaveMainPanel getMainPanel() {
      return this.mainPanel;
   }

   public JaveApplicationPreferences getApplicationPreferences() {
      return this.applicationPreferences;
   }

   public int getGeneralIconSize() {
      return this.javePreferences.getIconSize();
   }

   public boolean isLayersPanelShownByDefault() {
      return this.javePreferences.isLayersPanelShownByDefault();
   }

   public IStatusDisplay getStatusDisplay() {
      return this.status;
   }

   public IMacOsXApplicationCallbacks getMaxOsXApplicationCallbacks() {
      return new IMacOsXApplicationCallbacks() {
         @Override
         public void performShowPreferencesDialog() {
            JavePreferencesAction.performShowPreferencesDialog(
               JavEApplication.this.frame,
               JavEApplication.this.javePreferences,
               JavEApplication.this.applicationPreferences,
               JavEApplication.this.platePreferences,
               JavEApplication.this.mainPanel.getToolManager()
            );
         }

         @Override
         public boolean performShowExitDialog() {
            return JavEApplication.this.performExit(JavEApplication.this.frame);
         }

         @Override
         public void performShowAboutDialog() {
            JaveAboutDialog.showAboutDialog(JavEApplication.this.frame);
         }
      };
   }

   /**
    * True when the JVM is running in any "dump mode" — any system
    * property whose name starts with {@code jave.dump.} is set.
    * Code paths that would otherwise pop interactive dialogs at startup
    * (crash recovery, quick-start, etc.) should short-circuit when this
    * is true so the dump can run unattended.
    */
   public static boolean isDumpModeActive() {
      for (String name : System.getProperties().stringPropertyNames()) {
         if (name.startsWith("jave.dump.")) {
            return true;
         }
      }
      return false;
   }

   private void dumpToolOptionWidthsAndExit(FallbackInlineOptionsPanel fallback) {
      java.util.List<String> names = new java.util.ArrayList<>();
      java.util.List<Integer> widths = new java.util.ArrayList<>();
      names.add("(fallback)");
      widths.add(InlineOptionsWidthMeasurer.measureWidth(fallback.getContent()));
      for (Tool tool : this.mainPanel.getToolManager().getTools()) {
         de.jave.jave.tool.dialog.IInlineToolOptions inline = tool.getInlineOptionsPanel();
         if (inline == null) {
            names.add(tool.getName() + " (no inline panel)");
            widths.add(-1);
         } else {
            names.add(tool.getName());
            widths.add(InlineOptionsWidthMeasurer.measureWidth(inline.getContent()));
         }
      }
      Integer[] order = new Integer[names.size()];
      for (int i = 0; i < order.length; i++) {
         order[i] = i;
      }
      java.util.Arrays.sort(order, (a, b) -> Integer.compare(widths.get(b), widths.get(a)));
      int max = 0;
      for (int w : widths) {
         if (w > max) {
            max = w;
         }
      }
      int padding = InlineOptionsWidthMeasurer.getPaddingPx();
      System.out.println("Inline tool-options panel widths (sorted desc):");
      for (int idx : order) {
         int w = widths.get(idx);
         String wStr = w < 0 ? "    -" : String.format("%5dpx", w);
         System.out.println("  " + wStr + "  " + names.get(idx));
      }
      System.out.println("---");
      System.out.println("  widest measured  : " + max + "px");
      System.out.println("  padding          : " + padding + "px");
      System.out.println("  resulting bar    : " + (max + padding) + "px");
      System.out.flush();
      System.exit(0);
   }
}
