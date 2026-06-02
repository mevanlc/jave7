package de.jave.jave.preferences;

import de.jave.ascii.plate.CellScalingMode;
import de.jave.ascii.plate.CharacterMetrics;
import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import net.dizzy.commons.core.model.BooleanModel;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;

public class PlatePreferences extends SmartPreferences {
   private static final String KEY_AUTO_RESIZE_ON_DROP_FOR_ANIMATION_EDITOR = "autoResizeOnDropForAnimationEditor";
   private static final String KEY_AUTO_RESIZE_ON_DROP_FOR_TEXT_EDITOR = "autoResizeOnDropForTextEditor";
   private static final String KEY_GRID_VISIBLE = "gridVisible";
   private static final String KEY_MARK_ILLEGAL = "markIllegalCharacters";
   private static final String KEY_CONNECTED_LINES_VIEW = "pixelView";
   private static final String KEY_RULER = "ruler";
   private static final String KEY_CELL_SCALING_MODE = "cellScalingMode";
   private static final String KEY_CELL_SCALING_WIDTH = "cellScalingWidthScale";
   private static final String KEY_CELL_SCALING_HEIGHT = "cellScalingHeightScale";
   private static final String KEY_DEFAULT_ZOOM_DELTA = "defaultZoomDelta";
   private static final CellScalingMode DEFAULT_CELL_SCALING_MODE = CellScalingMode.LINE;
   private static final float DEFAULT_CELL_SCALING_FACTOR = 1.0F;
   private static final int DEFAULT_ZOOM_DELTA = 0;
   private static final boolean DEFAULT_AUTO_RESIZE_ON_DROP_FOR_ANIMATION_EDITOR = false;
   private static final boolean DEFAULT_AUTO_RESIZE_ON_DROP_FOR_TEXT_EDITOR = true;
   private static final boolean DEFAULT_GRID = true;
   private static final boolean DEFAULT_PURE_ASCII = true;
   private static final boolean DEFAULT_PIXEL_VIEW = false;
   private static final boolean DEFAULT_RULER = true;
   private final BooleanModel gridVisibilityModel = this.initializeSelectionModel("gridVisible", true);
   private final BooleanModel markIllegalModel = this.initializeSelectionModel("markIllegalCharacters", true);
   private final BooleanModel connectedLinesViewModel = this.initializeSelectionModel("pixelView", false);
   private final BooleanModel rulerModel = this.initializeSelectionModel("ruler", true);
   private final ObjectModel<CellScalingMode> cellScalingModeModel;
   private final ObjectModel<Float> cellScalingWidthModel;
   private final ObjectModel<Float> cellScalingHeightModel;

   public PlatePreferences(JavePreferences javePreferences) {
      super(javePreferences.getSubPreferences("plate"));
      CellScalingMode initialMode = CellScalingMode.fromName(this.get(KEY_CELL_SCALING_MODE, DEFAULT_CELL_SCALING_MODE.name()), DEFAULT_CELL_SCALING_MODE);
      float initialWidth = this.getFloat(KEY_CELL_SCALING_WIDTH, DEFAULT_CELL_SCALING_FACTOR);
      float initialHeight = this.getFloat(KEY_CELL_SCALING_HEIGHT, DEFAULT_CELL_SCALING_FACTOR);
      this.cellScalingModeModel = new ObjectModel<>(initialMode);
      this.cellScalingWidthModel = new ObjectModel<>(initialWidth);
      this.cellScalingHeightModel = new ObjectModel<>(initialHeight);
      CharacterMetrics.setMode(initialMode);
      CharacterMetrics.setScale(initialWidth, initialHeight);
      IChangeListener pushModeAndScale = new IChangeListener() {
         @Override
         public void stateChanged() {
            CharacterMetrics.setMode(PlatePreferences.this.cellScalingModeModel.getValue());
            CharacterMetrics.setScale(PlatePreferences.this.cellScalingWidthModel.getValue(), PlatePreferences.this.cellScalingHeightModel.getValue());
         }
      };
      this.cellScalingModeModel.addChangeListener(pushModeAndScale);
      this.cellScalingWidthModel.addChangeListener(pushModeAndScale);
      this.cellScalingHeightModel.addChangeListener(pushModeAndScale);
   }

   private float getFloat(String key, float defaultValue) {
      String raw = this.get(key, null);
      if (raw == null) {
         return defaultValue;
      }
      try {
         return Float.parseFloat(raw);
      } catch (NumberFormatException e) {
         return defaultValue;
      }
   }

   public ObjectModel<CellScalingMode> getCellScalingModeModel() {
      return this.cellScalingModeModel;
   }

   public ObjectModel<Float> getCellScalingWidthModel() {
      return this.cellScalingWidthModel;
   }

   public ObjectModel<Float> getCellScalingHeightModel() {
      return this.cellScalingHeightModel;
   }

   public CellScalingMode getCellScalingMode() {
      return this.cellScalingModeModel.getValue();
   }

   public void setCellScalingMode(CellScalingMode mode) {
      Ensure.ensureArgumentNotNull(mode);
      this.put(KEY_CELL_SCALING_MODE, mode.name());
      this.cellScalingModeModel.setValue(mode);
   }

   public float getCellScalingWidth() {
      return this.cellScalingWidthModel.getValue();
   }

   public void setCellScalingWidth(float scale) {
      this.put(KEY_CELL_SCALING_WIDTH, Float.toString(scale));
      this.cellScalingWidthModel.setValue(scale);
   }

   public float getCellScalingHeight() {
      return this.cellScalingHeightModel.getValue();
   }

   public void setCellScalingHeight(float scale) {
      this.put(KEY_CELL_SCALING_HEIGHT, Float.toString(scale));
      this.cellScalingHeightModel.setValue(scale);
   }

   private BooleanModel initializeSelectionModel(final String key, boolean defaultValue) {
      final BooleanModel model = new BooleanModel();
      model.setValue(this.getBoolean(key, defaultValue));
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            PlatePreferences.this.put(key, model.getValue());
         }
      });
      return model;
   }

   public BooleanModel getGridVisibilityModel() {
      return this.gridVisibilityModel;
   }

   public BooleanModel getMarkIllegalModel() {
      return this.markIllegalModel;
   }

   public BooleanModel getConnectedLinesViewModel() {
      return this.connectedLinesViewModel;
   }

   public BooleanModel getRulerModel() {
      return this.rulerModel;
   }

   public int getDefaultZoomDelta() {
      return this.getInt(KEY_DEFAULT_ZOOM_DELTA, DEFAULT_ZOOM_DELTA);
   }

   public void setDefaultZoomDelta(int delta) {
      this.put(KEY_DEFAULT_ZOOM_DELTA, delta);
   }

   public boolean isAutoResizeOnDropForTextEditor() {
      return this.getBoolean("autoResizeOnDropForTextEditor", true);
   }

   public void setAutoResizeOnDropForTextEditor(boolean value) {
      this.put("autoResizeOnDropForTextEditor", value);
   }

   public boolean isAutoResizeOnDropForAnimationEditor() {
      return this.getBoolean("autoResizeOnDropForAnimationEditor", false);
   }

   public void setAutoResizeOnDropForAnimationEditor(boolean value) {
      this.put("autoResizeOnDropForAnimationEditor", value);
   }
}
