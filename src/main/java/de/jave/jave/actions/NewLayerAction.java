package de.jave.jave.actions;

import de.jave.jave.JaveApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class NewLayerAction extends AbstractJaveAction {
   private final JaveApplication application;

   public NewLayerAction(JaveApplication application, JaveMainPanel mainPanel) {
      super(mainPanel, "New Layer", null);
      this.application = application;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.application.addSecondaryLayerAndActivate();
   }
}
