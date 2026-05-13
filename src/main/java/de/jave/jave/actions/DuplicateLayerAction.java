package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class DuplicateLayerAction extends AbstractJaveAction {
   private final JavEApplication application;

   public DuplicateLayerAction(JavEApplication application, JaveMainPanel mainPanel) {
      super(mainPanel, "Duplicate Layer", null);
      this.application = application;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.application.duplicateActiveLayer();
   }
}
