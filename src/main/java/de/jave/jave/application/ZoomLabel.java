package de.jave.jave.application;

import de.jave.gui.GStatusLabel;
import de.jave.gui.NullMouseClickHandler;
import de.jave.jave.JaveMessages;
import de.jave.jave.ZoomableFontModel;
import javax.swing.JComponent;
import net.dizzy.commons.core.model.ObjectModel;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.component.IComponentContainer;

public class ZoomLabel implements IComponentContainer {
   private static final String NO_ZOOM_STRING = "   ";
   private final GStatusLabel label;
   private ZoomableFontModel currentFontModel;
   private final IChangeListener changeListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         ZoomLabel.this.updateLabel();
      }
   };

   public ZoomLabel(final ObjectModel<ZoomableFontModel> zoomableFontModelModel) {
      Ensure.ensureArgumentNotNull(zoomableFontModelModel);
      this.label = new GStatusLabel("   ", new NullMouseClickHandler());
      this.label.setToolTipText(JaveMessages.Control_ZoomLabel_Tooltip);
      zoomableFontModelModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            ZoomLabel.this.updateToModelModel(zoomableFontModelModel);
         }
      });
      this.updateToModelModel(zoomableFontModelModel);
   }

   private void updateLabel() {
      if (this.currentFontModel == null) {
         this.label.setEnabled(false);
      } else {
         this.label.setEnabled(true);
         int sizeDelta = this.currentFontModel.getSizeDelta();
         if (sizeDelta < 0) {
            this.label.setText(String.valueOf(sizeDelta));
         } else if (sizeDelta > 0) {
            this.label.setText("+" + sizeDelta);
         } else {
            this.label.setText("   ");
         }
      }
   }

   @Override
   public JComponent getContent() {
      return this.label;
   }

   private void updateToModelModel(ObjectModel<ZoomableFontModel> zoomableFontModelModel) {
      if (this.currentFontModel != null) {
         this.currentFontModel.removeChangeListener(this.changeListener);
      }

      this.currentFontModel = zoomableFontModelModel.getValue();
      if (this.currentFontModel != null) {
         this.currentFontModel.addChangeListener(this.changeListener);
      }

      this.updateLabel();
   }
}
