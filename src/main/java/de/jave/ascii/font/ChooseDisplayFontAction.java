package de.jave.ascii.font;

import de.jave.jave.swing.JaveSwingMessages;
import java.awt.Component;
import java.awt.Font;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.message.MessageDialogUtilities;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserIcons;
import net.disy.commons.swing.fontchooser.util.FontUtilities;
import net.disy.commons.swing.fontchooser.view.FontChooserDialog;
import net.disy.commons.swing.fontchooser.view.fixedwidth.FixedWidthFontChooserDialogFactory;

public class ChooseDisplayFontAction extends SmartAction {
   private final FontModel displayFontModel;

   public ChooseDisplayFontAction(FontModel displayFontModel) {
      super(JaveSwingMessages.Action_ChooseDisplayFont_Name, DisyCommonsSwingFontChooserIcons.FONT_ICON);
      Ensure.ensureArgumentNotNull(displayFontModel);
      this.displayFontModel = displayFontModel;
   }

   @Override
   public void execute(Component parentComponent) {
      FontChooserDialog dialog = FixedWidthFontChooserDialogFactory.getInstance()
         .createFontChooserDialog(parentComponent, new FontModel(this.displayFontModel.getFontDescription()));
      IDialogResult result = dialog.show(JaveSwingMessages.Action_ChooseDisplayFont_DialogTitle);
      if (!result.isCanceled()) {
         Font selectedFont = dialog.getFont();
         if (!FontUtilities.isFixedWidth(selectedFont)) {
            Message message = new Message(
               JaveSwingMessages.Action_ChooseDisplayFont_DialogTitle,
               JaveSwingMessages.Action_ChooseDisplayFont_NonFixedWidthFontChosen_UseAnyway_Question,
               MessageType.QUESTION
            );
            boolean confirm = MessageDialogUtilities.showYesNoDialog(parentComponent, message);
            if (!confirm) {
               return;
            }
         }

         this.displayFontModel.setFont(selectedFont);
      }
   }
}
