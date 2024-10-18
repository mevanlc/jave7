package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.actions.ShowFigletEditorAction;
import javax.swing.Icon;

public final class FigletEditorQuickStartItem implements IQuickStartItem {
   @Override
   public Icon getIcon() {
      return QuickStartIcons.ITEM_FIGLET_ICON;
   }

   @Override
   public String getName() {
      return JaveMessages.QuickStartItem_FIGletEditor_Name;
   }

   @Override
   public String getDescription() {
      return JaveMessages.QuickStartItem_FIGletEditor_Description;
   }

   @Override
   public void perform(JavEApplication application) {
      application.doNew();
      ShowFigletEditorAction figletAction = application.getActions().getFigletAction();
      figletAction.showFigletEditor(application.getFrame());
   }
}
