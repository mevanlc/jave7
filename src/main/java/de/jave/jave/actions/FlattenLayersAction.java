package de.jave.jave.actions;

import de.jave.jave.JaveApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class FlattenLayersAction extends AbstractJaveAction {
   private final JaveApplication application;
   private final boolean includeHiddenSecondaryLayers;

   public FlattenLayersAction(JaveApplication application, JaveMainPanel mainPanel, boolean includeHiddenSecondaryLayers) {
      super(mainPanel, includeHiddenSecondaryLayers ? "Flatten" : "Flatten Visible", null);
      this.application = application;
      this.includeHiddenSecondaryLayers = includeHiddenSecondaryLayers;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.application.flattenLayers(this.includeHiddenSecondaryLayers);
   }
}
