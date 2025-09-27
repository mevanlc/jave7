package de.jave.jave.preferences;

import de.jave.preferences.JavePreferences;
import de.jave.preferences.SmartPreferences;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class PlatePreferences extends SmartPreferences {
   private static final String KEY_AUTO_RESIZE_ON_DROP_FOR_ANIMATION_EDITOR = "autoResizeOnDropForAnimationEditor";
   private static final String KEY_AUTO_RESIZE_ON_DROP_FOR_TEXT_EDITOR = "autoResizeOnDropForTextEditor";
   private static final String KEY_GRID_VISIBLE = "gridVisible";
   private static final String KEY_MARK_ILLEGAL = "markIllegalCharacters";
   private static final String KEY_CONNECTED_LINES_VIEW = "pixelView";
   private static final String KEY_RULER = "ruler";
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

   public PlatePreferences(JavePreferences javePreferences) {
      super(javePreferences.getSubPreferences("plate"));
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
