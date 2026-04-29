package de.jave.jave.preferences;

import de.jave.ascii.IAsciiGuiConstants;
import de.jave.gui.io.FileChooserUtilities;
import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import de.jave.util.RecentFileList;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.File;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class JaveApplicationPreferences extends SmartPreferences {
   private static final String KEY_CLIPART_GROUP_NAME = "clipartGroupName";
   private static final String KEY_FILL_PATTERN_NAME = "fillPatternName";
   private static final String KEY_USE_AWT_FILECHOOSER = "useAwtFileChooser";
   private static final String KEY_FONT = "font";
   private static final String KEY_COLOR_SCHEME = "colorscheme";
   private static final String KEY_FRAME_HEIGHT = "frameHeight";
   private static final String KEY_FRAME_WIDTH = "frameWidth";
   private static final String KEY_FRAME_Y = "frameY";
   private static final String KEY_FRAME_X = "frameX";
   private static final String KEY_FRAME_STATE = "frameState";
   private static final String KEY_ANIMATION_WIDTH = "animationWidth";
   private static final String KEY_ANIMATION_HEIGHT = "animationHeight";
   private static final String KEY_DOCUMENT_WIDTH = "documentWidth";
   private static final String KEY_DOCUMENT_HEIGHT = "documentHeight";
   private static final String KEY_CURRENT_DIRECTORY = "currentDirectory";
   private static final String KEY_AUTHOR_MAIL = "authorMail";
   private static final String KEY_AUTHOR_NAME = "authorName";
   private static final String KEY_CURSOR_BLOCK_STYLE = "cursorBlockStyle";
   private static final String KEY_SELECTIONLESS_CUT_COPY_ON_CELL = "selectionlessCutCopyOnCell";
   private static final String KEY_PASTE_V_FILLS_SELECTION = "pasteVFillsSelection";
   private static final String KEY_SHOW_QUICK_START_ON_STARTUP = "showQuickStartOnStartup";
   private static final boolean DEFAULT_USE_AWT_FILECHOOSER = false;
   private static final int DEFAULT_FRAME_X = 0;
   private static final int DEFAULT_FRAME_Y = 10;
   private static final int DEFAULT_FRAME_WIDTH = 787;
   private static final int DEFAULT_FRAME_HEIGHT = 537;
   private static final int DEFAULT_OPTIONS_LOCATION_X = 600;
   private static final int DEFAULT_OPTIONS_LOCATION_Y = 90;
   private static final int DEFAULT_DOCUMENT_HEIGHT = 30;
   private static final int DEFAULT_DOCUMENT_WIDTH = 71;
   private static final int DEFAULT_ANIMATION_HEIGHT = 30;
   private static final int DEFAULT_ANIMATION_WIDTH = 71;
   private static final int DEFAULT_DISPLAY_FONT_STYLE = 0;
   private static final int DEFAULT_DISPLAY_FONT_SIZE = 13;
   private static final boolean DEFAULT_SHOW_QUICK_START_ON_STARTUP = true;
   private static final String DEFAULT_DISPLAY_FONT_NAME = IAsciiGuiConstants.DEFAULT_ASCII_FONT.getName();
   private static final int DEFAULT_FRAME_STATE = 0;
   private static final boolean DEFAULT_SMALL_FRAME = false;
   private final RecentFileList recentFileList = new RecentFileList(new SmartPreferences(this.getSubPreferences("recentFiles")));
   private final FileModel currentDirectoryModel;
   private final BooleanPreferenceModel cursorBlockStyleModel;
   private final BooleanPreferenceModel selectionlessCutCopyOnCellModel;
   private final BooleanPreferenceModel pasteVFillsSelectionModel;
   private final FontModel displayFontModel;
   private final ObjectModel<ColorScheme> defaultColorSchemeModel;
   private final BooleanModel useAwtFileChooserModel = new BooleanModel(this.getBoolean("useAwtFileChooser", false));

   public JaveApplicationPreferences(JavePreferences javePreferences) {
      super(javePreferences.getSubPreferences("application"));
      this.useAwtFileChooserModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            boolean value = JaveApplicationPreferences.this.useAwtFileChooserModel.getValue();
            JaveApplicationPreferences.this.put("useAwtFileChooser", value);
            JaveApplicationPreferences.this.flush();
            FileChooserUtilities.useAwtFileChooser = value;
         }
      });
      FileChooserUtilities.useAwtFileChooser = this.useAwtFileChooserModel.getValue();
      this.currentDirectoryModel = new FileModel();
      String directory = this.get("currentDirectory", null);
      if (directory != null) {
         this.currentDirectoryModel.setValue(new File(directory));
      }

      this.currentDirectoryModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            File currentDirectory = JaveApplicationPreferences.this.currentDirectoryModel.getValue();
            JaveApplicationPreferences.this.put("currentDirectory", currentDirectory);
            JaveApplicationPreferences.this.flush();
         }
      });
      this.cursorBlockStyleModel = new BooleanPreferenceModel(this, "cursorBlockStyle", false);
      this.selectionlessCutCopyOnCellModel = new BooleanPreferenceModel(this, KEY_SELECTIONLESS_CUT_COPY_ON_CELL, false);
      this.pasteVFillsSelectionModel = new BooleanPreferenceModel(this, KEY_PASTE_V_FILLS_SELECTION, false);
      this.displayFontModel = new FontModel(this.getFont("font"));
      this.displayFontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveApplicationPreferences.this.putFont("font", JaveApplicationPreferences.this.displayFontModel.getFont());
            JaveApplicationPreferences.this.flush();
         }
      });
      ColorScheme colorScheme = ColorScheme.getById(this.get("colorscheme", ColorScheme.BLACK_ON_WHITE.getId()));
      this.defaultColorSchemeModel = new ObjectModel<>(colorScheme);
      this.defaultColorSchemeModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            JaveApplicationPreferences.this.put("colorscheme", JaveApplicationPreferences.this.defaultColorSchemeModel.getValue().getId());
            JaveApplicationPreferences.this.flush();
         }
      });
   }

   public FontModel getDisplayFontModel() {
      return this.displayFontModel;
   }

   public ObjectModel<ColorScheme> getDefaultColorSchemeModel() {
      return this.defaultColorSchemeModel;
   }

   public BooleanModel getUseAwtFileChooserModel() {
      return this.useAwtFileChooserModel;
   }

   private void putFont(String keyPrefix, Font font) {
      this.put(keyPrefix + "Name", font.getName());
      this.put(keyPrefix + "Size", font.getSize());
      this.put(keyPrefix + "Style", font.getStyle());
   }

   private Font getFont(String keyPrefix) {
      String fontName = this.get(keyPrefix + "Name", DEFAULT_DISPLAY_FONT_NAME);
      int fontSize = this.getInt(keyPrefix + "Size", 13);
      int fontStyle = this.getInt(keyPrefix + "Style", 0);
      return new Font(fontName, fontStyle, fontSize);
   }

   public Rectangle getApplicationFrameBounds() {
      int x = this.getInt("frameX", 0);
      int y = this.getInt("frameY", 10);
      int w = this.getInt("frameWidth", 787);
      int h = this.getInt("frameHeight", 537);
      return new Rectangle(x, y, w, h);
   }

   public RecentFileList getRecentFileList() {
      return this.recentFileList;
   }

   public Dimension getDefaultDocumentSize() {
      return new Dimension(this.getInt("documentWidth", 71), this.getInt("documentHeight", 30));
   }

   public void setDefaultDocumentSize(Dimension size) {
      this.put("documentWidth", size.width);
      this.put("documentHeight", size.height);
   }

   public Dimension getDefaultAnimationSize() {
      return new Dimension(this.getInt("animationWidth", 71), this.getInt("animationHeight", 30));
   }

   public void setDefaultAnimationSize(Dimension size) {
      this.put("animationWidth", size.width);
      this.put("animationHeight", size.height);
   }

   public FileModel getCurrectDirectoryModel() {
      return this.currentDirectoryModel;
   }

   public String getAuthorName() {
      return this.get("authorName", "");
   }

   public void setAuthorName(String authorName) {
      this.put("authorName", authorName);
   }

   public String getAuthorMail() {
      return this.get("authorMail", "");
   }

   public void setAuthorMail(String authorMail) {
      this.put("authorMail", authorMail);
   }

   public BooleanPreferenceModel getCursorBlockStyleModel() {
      return this.cursorBlockStyleModel;
   }

   public BooleanPreferenceModel getSelectionlessCutCopyOnCellModel() {
      return this.selectionlessCutCopyOnCellModel;
   }

   public BooleanPreferenceModel getPasteVFillsSelectionModel() {
      return this.pasteVFillsSelectionModel;
   }

   public int getApplicationFrameState() {
      return this.getInt("frameState", 0);
   }

   public void setApplicationFrameState(int frameState, Rectangle bounds) {
      this.put("frameState", frameState);
      if (frameState != 6) {
         this.put("frameX", bounds.x);
         this.put("frameY", bounds.y);
         this.put("frameWidth", bounds.width);
         this.put("frameHeight", bounds.height);
      }
   }

   public boolean isShowQuickStartOnStartup() {
      return this.getBoolean("showQuickStartOnStartup", true);
   }

   public void setShowQuickStartOnStartup(boolean value) {
      this.put("showQuickStartOnStartup", value);
   }

   public String getClipartGroupName() {
      return this.get("clipartGroupName", null);
   }

   public void setClipartGroupName(String groupName) {
      this.put("clipartGroupName", groupName);
      this.flush();
   }

   public String getFillPatternName() {
      return this.get("fillPatternName", null);
   }

   public void setFillPatternName(String name) {
      this.put("fillPatternName", name);
      this.flush();
   }

   public int getStartupToolIndex() {
      return this.getInt("startupToolIndex", 0);
   }

   public void setStartupToolIndex(int index) {
      this.put("startupToolIndex", index);
   }

   public Dimension getNewFileSize() {
      return new Dimension(
         this.getInt("newFileWidth", DEFAULT_DOCUMENT_WIDTH),
         this.getInt("newFileHeight", DEFAULT_DOCUMENT_HEIGHT)
      );
   }

   public void setNewFileSize(Dimension size) {
      this.put("newFileWidth", size.width);
      this.put("newFileHeight", size.height);
   }

   public boolean isNewFileFillEnabled() {
      return this.getBoolean("newFileFillEnabled", false);
   }

   public void setNewFileFillEnabled(boolean enabled) {
      this.put("newFileFillEnabled", enabled);
   }

   public char getNewFileFillCharacter() {
      return (char)this.getInt("newFileFillCharacter", ' ');
   }

   public void setNewFileFillCharacter(char ch) {
      this.put("newFileFillCharacter", ch);
   }

}
