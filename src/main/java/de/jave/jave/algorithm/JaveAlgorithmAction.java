package de.jave.jave.algorithm;

import de.jave.jave.JaveOptionsAlgorithmDialog;
import de.jave.jave.JaveSelection;
import de.jave.jave.actions.AbstractJaveAction;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.fontchooser.model.FontModel;

public class JaveAlgorithmAction extends AbstractJaveAction {
   private final JaveAlgorithm algorithm;
   private final FontModel displayFontModel;

   public JaveAlgorithmAction(JaveMainPanel mainPanel, FontModel displayFontModel, JaveAlgorithm algorithm) {
      super(mainPanel, algorithm.getMenuItemLabel(), algorithm.getIcon());
      Ensure.ensureArgumentNotNull(algorithm);
      Ensure.ensureArgumentNotNull(displayFontModel);
      this.algorithm = algorithm;
      this.displayFontModel = displayFontModel;
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      if (this.algorithm instanceof JaveOptionsAlgorithm) {
         this.applyAlgorithm(parentComponent, (JaveOptionsAlgorithm)this.algorithm);
      } else {
         JaveMainPanel mainPanel = this.getMainPanel();
         JaveSelection sel = this.algorithm.apply(mainPanel.getContentOfInterest());
         mainPanel.setContentOfInterest(sel);
         mainPanel.repaint();
         mainPanel.saveCurrentState(this.algorithm.getUndoRedoName());
      }
   }

   public void applyAlgorithm(Component parentComponent, JaveOptionsAlgorithm algo) {
      JaveOptionsAlgorithmDialog d = new JaveOptionsAlgorithmDialog(parentComponent, this.getMainPanel(), algo, this.displayFontModel);
      d.show();
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }
}
