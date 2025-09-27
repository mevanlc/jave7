package de.jave.jave.actions;

import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.algorithm.compress.AsciiPacker;
import de.jave.jave.clipart.Clipart;
import de.jave.jave.clipart.ClipartGroup;
import de.jave.jave.clipart.ClipartManager;
import de.jave.jave.clipart.ClipartNewEntryModel;
import de.jave.jave.clipart.ClipartNewEntryPage;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.UserDialog;

public class SelectionToClipartAction extends AbstractJaveAction {
   private final JaveApplicationPreferences preferences;

   public SelectionToClipartAction(JaveMainPanel mainPanel, JaveApplicationPreferences preferences) {
      super(mainPanel, "Export to Clipart...", JaveIcons.EXPORT_CLIPART_ICON);
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      ClipartManager clipartManager = ClipartManager.performLoad(parentComponent);
      if (clipartManager != null) {
         if (editor.getPlate().hasSelection()) {
            CharacterPlate ch = editor.getPlate().getSelectionContent();
            ClipartNewEntryModel model = new ClipartNewEntryModel(ch);
            String groupName = this.preferences.getClipartGroupName();
            ClipartGroup group = clipartManager.getGroup(groupName);
            if (group == null && clipartManager.getGroupCount() > 0) {
               group = clipartManager.getGroup(0);
            }

            ObjectModel<ClipartGroup> groupSelectionModel = new ObjectModel<>(group);
            UserDialog dialog = new UserDialog(
               parentComponent, new ClipartNewEntryPage(groupSelectionModel, model, clipartManager, this.preferences.getDisplayFontModel())
            );
            IDialogResult result = dialog.show();
            if (!result.isCanceled()) {
               Clipart newClipart = new Clipart(model.getName(), AsciiPacker.encode(model.getCode().getContent()), model.getAuthor());
               ClipartGroup newGroup = groupSelectionModel.getValue();
               newGroup.add(newClipart);
               clipartManager.performSave(parentComponent, group);
               this.preferences.setClipartGroupName(newGroup.getName());
            }
         }
      }
   }
}
