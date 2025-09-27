package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.actions.Image2AsciiAction;
import javax.swing.Icon;

public final class Image2AsciiQuickStartItem implements IQuickStartItem {
   @Override
   public Icon getIcon() {
      return QuickStartIcons.ITEM_IMAGE2ASCII_ICON;
   }

   @Override
   public String getName() {
      return JaveMessages.QuickStartItem_Image2Ascii_Name;
   }

   @Override
   public String getDescription() {
      return JaveMessages.QuickStartItem_Image2Ascii_Description;
   }

   @Override
   public void perform(JavEApplication application) {
      Image2AsciiAction action = application.getActions().getImage2AsciiAction();
      action.execute(application.getFrame(), null, null);
   }
}
