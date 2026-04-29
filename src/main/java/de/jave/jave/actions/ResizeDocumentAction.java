package de.jave.jave.actions;

import de.jave.jave.JavEApplication;
import de.jave.jave.DocumentSizeLimits;
import de.jave.jave.actions.enablestrategy.IJaveDocumentEditorActionEnabledStrategy;
import de.jave.jave.actions.enablestrategy.TextAndAnimationEditorEnabledStrategy;
import de.jave.jave.browser.JaveDocumentType;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.plate.AnimationDocumentEditor;
import de.jave.jave.plate.IDocumentEditor;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class ResizeDocumentAction extends AbstractJaveAction {
   public ResizeDocumentAction(JavEApplication jave) {
      super(jave.getMainPanel(), "Resize Document", JaveIcons.RESIZE_ICON);
      this.setToolTipText("Resize Document");
   }

   @Override
   protected IJaveDocumentEditorActionEnabledStrategy getEnabledStrategy() {
      return TextAndAnimationEditorEnabledStrategy.getInstance();
   }

   @Override
   protected void ececute(Component parentComponent, IDocumentEditor editor) {
      Dimension documentSize = editor.getPlate().getDocument().getSize();
      final JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("New size:"), GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      panel.add(new JLabel("Width:"));
      SpinnerNumberModel itfWidth = new SpinnerNumberModel(
         DocumentSizeLimits.clampWidth(documentSize.width),
         DocumentSizeLimits.MIN_WIDTH,
         DocumentSizeLimits.MAX_WIDTH,
         1
      );
      panel.add(new JSpinner(itfWidth));
      panel.add(new JLabel("Height:"));
      SpinnerNumberModel itfHeight = new SpinnerNumberModel(
         DocumentSizeLimits.clampHeight(documentSize.height),
         DocumentSizeLimits.MIN_HEIGHT,
         DocumentSizeLimits.MAX_HEIGHT,
         1
      );
      panel.add(new JSpinner(itfHeight));
      IDialogPage page = new AbstractDialogPage("") {
         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }

         @Override
         public JComponent createContent() {
            return panel;
         }

         @Override
         public String getTitle() {
            return "Resize plate";
         }
      };
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(page) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      });
      IDialogResult result = userDialog.show();
      if (!result.isCanceled()) {
         int newWidth = itfWidth.getNumber().intValue();
         int newHeight = itfHeight.getNumber().intValue();
         if (newWidth != documentSize.width || newHeight != documentSize.height) {
            if (editor.getType() == JaveDocumentType.TEXT) {
               this.getMainPanel().setPlateSize(newWidth, newHeight);
            } else {
               if (editor.getType() != JaveDocumentType.ANIMATION) {
                  throw new UnsupportedOperationException();
               }

               AnimationDocumentEditor animationEditor = (AnimationDocumentEditor)editor;
               animationEditor.getModel().setNewFrameSize(new Dimension(newWidth, newHeight));
            }

            this.getMainPanel().saveCurrentState("resize plate");
         }
      }
   }
}
