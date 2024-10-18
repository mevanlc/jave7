package de.jave.jave;

import de.jave.jave.algorithm.JaveAlgorithmOptionsListener;
import de.jave.jave.algorithm.JaveAlgorithmOptionsPanel;
import de.jave.jave.algorithm.JaveOptionsAlgorithm;
import de.jave.jave.plate.JaveMainPanel;
import java.awt.Component;
import javax.swing.JComponent;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.fontchooser.model.FontModel;
import net.disy.commons.swing.util.RelativePosition;

public class JaveOptionsAlgorithmDialog implements JaveAlgorithmOptionsListener {
   private final JaveSelection source;
   private boolean applied = false;
   private final JaveOptionsAlgorithm algorithm;
   private final UserDialog dialog;
   private final JaveMainPanel mainPanel;

   public JaveOptionsAlgorithmDialog(Component parentComponent, JaveMainPanel mainPanel, final JaveOptionsAlgorithm algorithm, final FontModel fontModel) {
      Ensure.ensureArgumentNotNull(mainPanel);
      Ensure.ensureArgumentNotNull(algorithm);
      Ensure.ensureArgumentNotNull(fontModel);
      this.mainPanel = mainPanel;
      this.algorithm = algorithm;
      this.source = mainPanel.getContentOfInterest();
      algorithm.getOptions().adjustTo(this.source);
      IDialogPage dialogPage = new AbstractDialogPage("") {
         @Override
         public String getTitle() {
            return algorithm.getMenuItemLabel();
         }

         @Override
         public JComponent createContent() {
            JaveAlgorithmOptionsPanel optionsPanel = algorithm.getOptions().getPanel(fontModel);
            return optionsPanel.getContent();
         }

         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }
      };
      this.dialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(dialogPage) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      }, RelativePosition.RIGHT);
      algorithm.getOptions().addAlgorithmOptionsListener(this);
   }

   @Override
   public void algorithmOptionsChanged() {
      JaveSelection sel = this.algorithm.apply(this.source.getClone());
      this.mainPanel.setContentOfInterest(sel);
      this.applied = true;
   }

   private void performOk() {
      this.algorithm.getOptions().removeAlgorithmOptionsListener(this);
      if (this.applied) {
         this.mainPanel.saveCurrentState(this.algorithm.getUndoRedoName());
      }

      this.applied = false;
      this.dialog.setVisible(false);
   }

   private void performCancel() {
      this.algorithm.getOptions().removeAlgorithmOptionsListener(this);
      if (this.applied) {
         this.mainPanel.setContentOfInterest(this.source);
         this.applied = false;
      }

      this.dialog.setVisible(false);
   }

   public void show() {
      IDialogResult result = this.dialog.show();
      if (result.isCanceled()) {
         this.performCancel();
      } else {
         this.performOk();
      }
   }
}
