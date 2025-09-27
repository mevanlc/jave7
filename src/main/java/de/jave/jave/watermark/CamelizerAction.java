package de.jave.jave.actions;

import de.jave.jave.JaveMessages;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.algorithm.camel.CamelizerDialogPage;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.lib.CharacterPlate;
import de.jave.lib.ICharacterPlateSettable;
import java.awt.Component;
import java.io.File;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.message.Message;
import net.disy.commons.core.message.MessageType;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.message.MessageDialogFactory;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.util.RelativePosition;

public final class CamelizerAction extends AbstractJaveAction {
   private final JaveMainPanel mainPanel;
   private final FileModel currentDirectoryModel;

   public CamelizerAction(JaveMainPanel mainPanel, FileModel currentDirectoryModel) {
      super(mainPanel, "Camelize (Shape to image)", JaveIcons.CAMEL_ICON);
      Ensure.ensureArgumentNotNull(mainPanel);
      Ensure.ensureArgumentNotNull(currentDirectoryModel);
      this.mainPanel = mainPanel;
      this.currentDirectoryModel = currentDirectoryModel;
      this.setToolTipText("Camelize (apply the shape from an image to the current text)");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      File optionalImageFile = null;
      this.perform(parentComponent, editor, optionalImageFile);
   }

   public void perform(Component parentComponent, IDocumentEditor editor, File optionalImageFile) {
      CharacterPlate sourcePlate = editor.getPlate().getContentOfInterest().getContent();
      if (sourcePlate.getNonEmptyCharCount() < 12) {
         MessageDialogFactory.showMessageDialog(
            parentComponent,
            new Message(JaveMessages.JavE, "The current document does not contain enough text.\nThe Camelizer can not be applied.", MessageType.INFORMATION)
         );
      } else {
         BooleanModel changesAppliedModel = new BooleanModel(false);
         CamelizerDialogPage camelDialog = new CamelizerDialogPage(sourcePlate, new ICharacterPlateSettable() {
            @Override
            public void setCharacterPlate(CharacterPlate plate) {
               CamelizerAction.this.mainPanel.setContentOfInterest(plate);
            }
         }, this.currentDirectoryModel, changesAppliedModel, optionalImageFile);
         UserDialog dialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<CamelizerDialogPage>(camelDialog) {
            @Override
            public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
               return DialogHeaderPanelConfiguration.createVisibleWithIcon(JaveIcons.CAMELIZER_DIALOGICON);
            }
         }, RelativePosition.RIGHT);
         IDialogResult result = dialog.show();
         if (changesAppliedModel.getValue()) {
            if (result.isCanceled()) {
               this.mainPanel.setContentOfInterest(sourcePlate);
            } else {
               this.mainPanel.saveCurrentState("camelize");
            }
         }
      }
   }
}
