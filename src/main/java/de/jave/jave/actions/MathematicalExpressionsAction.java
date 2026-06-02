package de.jave.jave.actions;

import de.jave.jave.FormulaEditorDialogPage;
import de.jave.jave.JavEApplication;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import de.jave.jave.preferences.ColorScheme;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.fontchooser.model.FontModel;

public class MathematicalExpressionsAction extends AbstractJaveAction {
   private final JavEApplication jave;

   public MathematicalExpressionsAction(JavEApplication jave, JaveMainPanel mainPanel) {
      super(mainPanel, "Mathematical Expressions", JaveIcons.FORMULA_ICON);
      this.jave = jave;
      this.setToolTipText("Show Mathematical Expressions Editor");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      ColorScheme colorScheme = editor.getPlate().getColorSchemeModel().getValue();
      FontModel displayFontModel = this.jave.getApplicationPreferences().getDisplayFontModel();
      FormulaEditorDialogPage page = new FormulaEditorDialogPage(displayFontModel, colorScheme);
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<>(page));
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         this.jave.pasteAsNewSelection(new CharacterPlate(page.getResult()));
      }
   }
}
