package net.disy.commons.swing.fontchooser.view;

import java.awt.Toolkit;
import javax.swing.JComponent;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public abstract class AbstractFontAdjustComponent implements IFontModelView {
   private JComponent content;
   private final FontModel fontModel;
   private final IFontDialogProperties properties;

   public AbstractFontAdjustComponent(FontModel fontModel, IFontDialogProperties properties) {
      Ensure.ensureArgumentNotNull(properties);
      Ensure.ensureArgumentNotNull(fontModel);
      this.fontModel = fontModel;
      this.properties = properties;
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (AbstractFontAdjustComponent.this.content != null) {
               AbstractFontAdjustComponent.this.updateFontModelView();
            }
         }
      });
   }

   protected final IFontDialogProperties getFontDialogProperties() {
      return this.properties;
   }

   protected abstract void updateFontModelView();

   protected abstract JComponent createContent();

   @Override
   public final JComponent getContent() {
      if (this.content == null) {
         this.content = this.createContent();
         this.updateFontModelView();
      }

      return this.content;
   }

   @Override
   public final FontModel getFontModel() {
      return this.fontModel;
   }

   @Override
   public abstract void setEnabled(boolean var1);

   @Override
   public abstract boolean isEnabled();

   protected void rejectUserInput() {
      Toolkit.getDefaultToolkit().beep();
      this.updateFontModelView();
   }
}
