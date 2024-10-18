package de.jave.jave.application;

import de.jave.ascii.font.ChooseDisplayFontAction;
import de.jave.gui.GStatusLabel;
import de.jave.gui.IMouseClickHandler;
import de.jave.jave.JaveMessages;
import javax.swing.JComponent;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class FontLabel implements IComponentContainer {
   private final GStatusLabel statusLabel;

   public FontLabel(final FontModel fontModel) {
      Ensure.ensureArgumentNotNull(fontModel);
      this.statusLabel = new GStatusLabel("", new IMouseClickHandler() {
         @Override
         public void handleMouseClicked() {
            new ChooseDisplayFontAction(fontModel).execute(FontLabel.this.statusLabel);
         }
      });
      this.statusLabel.setToolTipText(JaveMessages.Control_FontLabel_Tooltip);
      fontModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            FontLabel.this.updateLabel(fontModel);
         }
      });
      this.updateLabel(fontModel);
   }

   private void updateLabel(FontModel fontModel) {
      String fontText = fontModel.getFontFamilyName() + " " + fontModel.getFontSize();
      this.statusLabel.setText(fontText);
   }

   @Override
   public JComponent getContent() {
      return this.statusLabel;
   }
}
