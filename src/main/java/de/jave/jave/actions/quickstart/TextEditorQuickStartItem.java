package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import javax.swing.Icon;

public final class TextEditorQuickStartItem implements IQuickStartItem {
   @Override
   public Icon getIcon() {
      return QuickStartIcons.ITEM_TEXT_ICON;
   }

   @Override
   public String getName() {
      return JaveMessages.QuickStartItem_TextEditor_Name;
   }

   @Override
   public String getDescription() {
      return JaveMessages.QuickStartItem_TextEditor_Description;
   }

   @Override
   public void perform(JavEApplication application) {
      application.doNew();
   }
}
