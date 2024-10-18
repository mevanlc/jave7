package net.disy.commons.swing.dialog.core.preferences;

import java.awt.Rectangle;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.preferences.IPreferences;

public abstract class AbstractFramePreferences {
   private static final String KEY_X = "X";
   private static final String KEY_Y = "Y";
   private static final String KEY_WIDTH = "WIDTH";
   private static final String KEY_HEIGHT = "HEIGHT";
   private static final String KEY_STATE = "STATE";
   private final IPreferences preferences;
   private final FrameBoundsState defaultState;

   public AbstractFramePreferences(IPreferences preferences, FrameBoundsState defaultState) {
      Ensure.ensureArgumentNotNull(preferences);
      Ensure.ensureArgumentNotNull(defaultState);
      this.preferences = preferences;
      this.defaultState = defaultState;
   }

   public FrameBoundsState loadFrameBoundsState() {
      int x = this.preferences.getInt("X", this.defaultState.getBounds().x);
      int y = this.preferences.getInt("Y", this.defaultState.getBounds().y);
      int width = this.preferences.getInt("WIDTH", this.defaultState.getBounds().width);
      int height = this.preferences.getInt("HEIGHT", this.defaultState.getBounds().height);
      int extendedState = this.preferences.getInt("STATE", this.defaultState.getExtendedFrameState());
      return new FrameBoundsState(new Rectangle(x, y, width, height), extendedState);
   }

   public void saveFrameBoundsState(FrameBoundsState state) {
      this.preferences.putInt("STATE", state.getExtendedFrameState(), this.defaultState.getExtendedFrameState());
      if (state.getExtendedFrameState() != 6) {
         this.preferences.putInt("X", state.getBounds().x, this.defaultState.getBounds().x);
         this.preferences.putInt("Y", state.getBounds().y, this.defaultState.getBounds().y);
         this.preferences.putInt("WIDTH", state.getBounds().width, this.defaultState.getBounds().width);
         this.preferences.putInt("HEIGHT", state.getBounds().height, this.defaultState.getBounds().height);
      }

      this.preferences.flush();
   }
}
