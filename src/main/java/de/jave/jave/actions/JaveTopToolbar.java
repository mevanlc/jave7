package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.JaveMessages;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.action.ActionWidgetFactory;

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
      JToggleButton gridToggle = ActionWidgetFactory.createToggleButton(actions.getGridToggleAction());
      gridToggle.setToolTipText(JaveMessages.ToolCheckBox_Grid_Tooltip);
      builder.add(gridToggle);
      JToggleButton markIllegalToggle = ActionWidgetFactory.createToggleButton(actions.getMarkIllegalToggleAction());
      markIllegalToggle.setToolTipText(JaveMessages.ToolCheckBox_MarkIllegal_Tooltip);
      builder.add(markIllegalToggle);
      JToggleButton connectedLinesToggle = ActionWidgetFactory.createToggleButton(actions.getConnectedLinesViewToggleAction());
      connectedLinesToggle.setToolTipText(JaveMessages.ToolCheckBox_ConnectedLinesView_Tooltip);
      builder.add(connectedLinesToggle);
      JToggleButton watermarkToggle = ActionWidgetFactory.createToggleButton(actions.getWatermarkVisibilityToggleAction());
      watermarkToggle.setToolTipText(JaveMessages.ToolCheckBox_Watermark_Tooltip);
      builder.add(watermarkToggle);
      JToggleButton auxLinesToggle = ActionWidgetFactory.createToggleButton(actions.getAuxLinesVisibilityToggleAction());
      auxLinesToggle.setToolTipText(JaveMessages.ToolCheckBox_AuxLines_Tooltip);
      builder.add(auxLinesToggle);
      builder.addSeparator();
      builder.add(actions.getAboutAction());
      this.content = builder.createPanel();
   }

   public JComponent getContent() {
      return this.content;
   }
}
