package net.disy.commons.swing.fontchooser.util;

import javax.swing.Icon;
import javax.swing.JToggleButton;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.swing.action.ActionWidgetFactory;
import net.disy.commons.swing.action.SmartToggleAction;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserIcons;
import net.disy.commons.swing.fontchooser.resources.DisyCommonsSwingFontChooserMessages;

public class FontWidgetFactory {
   private FontWidgetFactory() {
      throw new UnreachableCodeReachedException();
   }

   public static JToggleButton createUnterlineButton(BooleanModel model) {
      String toolTip = DisyCommonsSwingFontChooserMessages.getString("FontStyleButtons.Underline.ToolTip");
      Icon underlineIcon = DisyCommonsSwingFontChooserIcons.UNDERLINE_ICON;
      SmartToggleAction toggleAction = new SmartToggleAction(model, toolTip, underlineIcon);
      return ActionWidgetFactory.createToggleButton(toggleAction);
   }
}
