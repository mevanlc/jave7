package de.jave.jave.actions;

import de.jave.jave.JaveSelection;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextEditorOnlyEnabledStrategy;
import de.jave.jave.algorithm.gradient.AsciiGradientConfiguration;
import de.jave.jave.algorithm.steganogram.GradientSteganogramPage;
import de.jave.jave.plate.IDocumentEditor;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.dialog.userdialog.buttons.DialogButtonConfigurationFactory;

public class DecodeSteganogramAction extends AbstractJaveAction {
   private final AsciiGradientConfiguration gradientConfiguration;

   public DecodeSteganogramAction(JaveMainPanel mainPanel, AsciiGradientConfiguration gradientConfiguration) {
      super(mainPanel, "Decode Steganogram", null);
      Ensure.ensureArgumentNotNull(gradientConfiguration);
      this.gradientConfiguration = gradientConfiguration;
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextEditorOnlyEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      JaveSelection content = editor.getPlate().getContentOfInterest();
      UserDialog dialog = new UserDialog(
         parentComponent,
         new DefaultDialogConfiguration<GradientSteganogramPage>(
            new GradientSteganogramPage(content.getContent(), this.gradientConfiguration), DialogButtonConfigurationFactory.createCloseOnly()
         ) {
         }
      );
      dialog.show();
   }
}
