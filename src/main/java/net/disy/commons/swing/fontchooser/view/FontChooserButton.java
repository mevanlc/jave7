package net.disy.commons.swing.fontchooser.view;

import java.awt.Component;
import javax.swing.AbstractButton;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.IActionConfiguration;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.component.AbstractActionComponent;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.view.button.LabelFontChooserButtonConfiguration;

public class FontChooserButton extends AbstractActionComponent {
   private final AbstractButton content;
   private final SmartAction action;
   private final FontModel fontModel;

   public FontChooserButton(FontModel fontModel, IFontChooserDialogFactory dialogFactory) {
      this(fontModel, new LabelFontChooserButtonConfiguration(), dialogFactory);
   }

   public FontChooserButton(FontModel fontModel, IActionConfiguration configuration) {
      this(fontModel, configuration, DefaultFontChooserDialogFactory.getInstance());
   }

   public FontChooserButton(FontModel fontModel) {
      this(fontModel, new LabelFontChooserButtonConfiguration(), DefaultFontChooserDialogFactory.getInstance());
   }

   public FontChooserButton(final FontModel fontModel, IActionConfiguration configuration, final IFontChooserDialogFactory dialogFactory) {
      Ensure.ensureArgumentNotNull(configuration);
      Ensure.ensureArgumentNotNull(fontModel);
      this.fontModel = fontModel;
      this.action = new SmartAction(configuration) {
         @Override
         protected void execute(Component parentComponent) {
            FontModel dialogFontModel = new FontModel(fontModel.getFontDescription());
            FontChooserDialog fontChooserDialog = dialogFactory.createFontChooserDialog(parentComponent, dialogFontModel);
            IDialogResult result = fontChooserDialog.show();
            if (!result.isCanceled()) {
               fontModel.setFont(dialogFontModel.getFontDescription());
               FontChooserButton.this.fireActionEvent();
            }
         }
      };
      this.content = ActionWidgetFactory.createButton(this.action);
   }

   public FontModel getFontModel() {
      return this.fontModel;
   }

   public void setEnabled(boolean enabled) {
      this.action.setEnabled(enabled);
   }

   public AbstractButton getContent() {
      return this.content;
   }
}
