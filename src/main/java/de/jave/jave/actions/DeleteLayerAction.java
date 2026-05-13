package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.PlateDocument;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;

public class DeleteLayerAction extends AbstractJaveAction {
   private final JavEApplication application;

   public DeleteLayerAction(JavEApplication application, JaveMainPanel mainPanel) {
      super(mainPanel, "Delete Layer", null);
      this.application = application;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return activeEditor -> {
         if (activeEditor == null || activeEditor.getType() != JaveDocumentType.TEXT) {
            return false;
         }
         PlateDocument document = activeEditor.getPlate().getDocument();
         return document != null && document.canDeleteActiveLayer();
      };
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.application.deleteActiveLayer();
   }
}
