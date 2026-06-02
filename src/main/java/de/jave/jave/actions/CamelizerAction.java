package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.algorithm.camel.CamelizerSessionManager;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import java.io.File;
import net.dizzy.commons.core.util.Ensure;

public final class CamelizerAction extends AbstractJaveAction {
   private final CamelizerSessionManager sessionManager;

   public CamelizerAction(JaveMainPanel mainPanel, CamelizerSessionManager sessionManager) {
      super(mainPanel, "Camelize (Shape to image)", JaveIcons.CAMEL_ICON);
      Ensure.ensureArgumentNotNull(mainPanel);
      Ensure.ensureArgumentNotNull(sessionManager);
      this.sessionManager = sessionManager;
      this.setToolTipText("Camelize (apply the shape from an image to the current text)");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      this.perform(parentComponent, editor, null);
   }

   public void perform(Component parentComponent, IDocumentEditor editor, File optionalImageFile) {
      this.sessionManager.openOrFocus(parentComponent, editor, optionalImageFile);
   }
}
