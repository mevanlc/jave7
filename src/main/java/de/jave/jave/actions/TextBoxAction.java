package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class TextBoxAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public TextBoxAction(JavEApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Textbox Editor", JaveIcons.TEXTBOX_ICON);
      this.jave = jave;
      this.setToolTipText("Show Textbox Editor");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.jave.showTextboxDialog();
   }
}
