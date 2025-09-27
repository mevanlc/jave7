package de.jave.jave.actions.quickstart;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import de.jave.jave.actions.NewAnimationAction;
import javax.swing.Icon;

public final class AnimationEditorQuickStartItem implements IQuickStartItem {
   @Override
   public Icon getIcon() {
      return QuickStartIcons.ITEM_ANIMATION_ICON;
   }

   @Override
   public String getName() {
      return JaveMessages.QuickStartItem_AnimationEditor_Name;
   }

   @Override
   public String getDescription() {
      return JaveMessages.QuickStartItem_AnimationEditor_Description;
   }

   @Override
   public void perform(JavEApplication application) {
      new NewAnimationAction(application).doCreateNewAnimation();
   }
}
