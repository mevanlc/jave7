package net.disy.commons.swing.dialog.core;

import net.disy.commons.swing.dialog.input.IRequestFinishListener;

public interface IPage {
   void addRequestFinishListener(IRequestFinishListener var1);

   void removeRequestFinishListener(IRequestFinishListener var1);

   String getDescription();

   boolean canFinish();

   String getTitle();

   IDialogHelpHandler getHelpHandler();

   void enter();

   void leave();
}
