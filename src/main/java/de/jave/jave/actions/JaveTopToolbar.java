package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import javax.swing.JComponent;
import net.disy.commons.core.util.Ensure;

public class JaveTopToolbar {
   private final JComponent content;

   public JaveTopToolbar(JavEApplication application, JaveActions actions, UndoRedoModel undeRedoModel) {
      Ensure.ensureArgumentNotNull(application);
      ButtonToolbarBuilder builder = new ButtonToolbarBuilder();
      builder.add(actions.getQuickStartAction());
      builder.add(actions.getNewDocumentAction());
      builder.add(actions.getNewAnimationAction());
      builder.addSeparator();
      builder.add(actions.getBrowseAction());
      builder.add(actions.getOpenAction());
      builder.add(actions.getSaveAction());
      builder.addSeparator();
      builder.add(actions.getExportAction());
      builder.addSeparator();
      builder.add(actions.getCutAction());
      builder.add(actions.getCopyAction());
      builder.add(actions.getPasteAsNewSelectionAction());
      builder.addSeparator();
      builder.add(new UndoAction(application, undeRedoModel, true));
      builder.add(new RedoAction(application, undeRedoModel, true));
      builder.addSeparator();
      builder.add(actions.getResizeAction());
      builder.addSeparator();
      builder.add(actions.getFigletAction());
      builder.add(actions.getImage2AsciiAction());
      builder.add(actions.getCamelizerAction());
      builder.add(actions.getClipartLibraryAction());
      builder.add(actions.getTextBoxAction());
      builder.add(actions.getMathematicalExpressionsAction());
      builder.addSeparator();
      builder.add(new ZoomOutAction(application.getMainPanel()));
      builder.add(new ZoomInAction(application.getMainPanel()));
      builder.addSeparator();
      builder.add(actions.getAboutAction());
      this.content = builder.createPanel();
   }

   public JComponent getContent() {
      return this.content;
   }
}
