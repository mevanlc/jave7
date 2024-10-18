package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import javax.swing.Icon;

public final class WatermarkQuickStartItem implements IQuickStartItem {
   @Override
   public Icon getIcon() {
      return QuickStartIcons.ITEM_WATERMARK_ICON;
   }

   @Override
   public String getName() {
      return JaveMessages.QuickStartItem_Watermark_Name;
   }

   @Override
   public String getDescription() {
      return JaveMessages.QuickStartItem_Watermark_Description;
   }

   @Override
   public void perform(JavEApplication application) {
      application.doNew();
      application.doLoadWatermark();
   }
}
