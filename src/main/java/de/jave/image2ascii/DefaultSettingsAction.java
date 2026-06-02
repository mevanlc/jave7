package de.jave.image2ascii;

import de.jave.image2ascii.model.Image2AsciiOptionsModel;
import de.jave.image2ascii.model.Image2AsciiSourceImageModel;
import java.awt.Component;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;

public final class DefaultSettingsAction extends SmartAction {
   private final Image2AsciiOptionsModel optionsModel;

   public DefaultSettingsAction(Image2AsciiOptionsModel optionsModel, final Image2AsciiSourceImageModel sourceImageModel) {
      super("Default Settings");
      Ensure.ensureArgumentNotNull(optionsModel);
      Ensure.ensureArgumentNotNull(sourceImageModel);
      this.optionsModel = optionsModel;
      sourceImageModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DefaultSettingsAction.this.updateEnabled(sourceImageModel);
         }
      });
      this.updateEnabled(sourceImageModel);
   }

   private void updateEnabled(Image2AsciiSourceImageModel sourceImageModel) {
      this.setEnabled(!sourceImageModel.isEmpty());
   }

   @Override
   protected void execute(Component parentComponent) {
      this.optionsModel.reset();
   }
}
