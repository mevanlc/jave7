package de.jave.jave;

import de.jave.jave.pixelplate.PixelPlate;
import de.jave.jave.preferences.ColorScheme;
import de.jave.jave.preferences.JaveApplicationPreferences;
import de.jave.lib.CharacterPlate;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.disy.commons.core.io.FileModel;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.util.RelativePosition;

public abstract class AbstractDialogTool {
   private final JavEApplication jave;
   private final Plate plate;
   protected final CharacterPlate characterPlate;
   protected final int plateWidth;
   protected final int plateHeight;
   protected PixelPlate markPlate;
   private final PlateDocument document;
   private final JaveApplicationPreferences preferences;
   private UserDialog dialog;

   public AbstractDialogTool(JavEApplication asciiPainter, JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      this.jave = asciiPainter;
      this.characterPlate = new CharacterPlate(this.getPreferredPlateSize());
      this.plateWidth = this.characterPlate.getWidth();
      this.plateHeight = this.characterPlate.getHeight();
      this.plate = this.jave.pasteAsNewDocument(this.characterPlate, this.getToolTitle());
      this.document = this.plate.getDocument();
      this.document.setModified(false);
      this.jave.performSetColorScheme(this.getPreferredColorScheme());
   }

   protected final FileModel getCurrentDirectoryModel() {
      return this.jave.getDocumentManager().getCurrentDirectoryModel();
   }

   protected final UserDialog getDialog() {
      if (this.dialog == null) {
         this.dialog = this.createDialog(this.jave.getFrame());
      }

      return this.dialog;
   }

   private UserDialog createDialog(Component parent) {
      DefaultDialogConfiguration<IDialogPage> configuration = new DefaultDialogConfiguration<IDialogPage>(new AbstractDialogPage("") {
         @Override
         public String getTitle() {
            return AbstractDialogTool.this.getToolTitle();
         }

         @Override
         public JComponent createContent() {
            return AbstractDialogTool.this.getOptionsComponent();
         }

         @Override
         public IBasicMessage createCurrentMessage() {
            return this.getDefaultMessage();
         }
      }) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }

         @Override
         public JComponent[] createAdditionalButtons() {
            Action[] actions = AbstractDialogTool.this.getAdditionalActions();
            JComponent[] buttons = new JComponent[actions.length];

            for (int i = 0; i < actions.length; i++) {
               buttons[i] = new JButton(actions[i]);
            }

            return buttons;
         }

         @Override
         public boolean performOk(Component parentComponent) {
            AbstractDialogTool.this.saveCurrentState(AbstractDialogTool.this.getToolActionName());
            AbstractDialogTool.this.dialog.setVisible(false);
            return true;
         }

         @Override
         public boolean performCancel(Component parentComponent) {
            AbstractDialogTool.this.document.setModified(false);
            AbstractDialogTool.this.jave.doClose(parentComponent);
            AbstractDialogTool.this.getDialog().setVisible(false);
            return true;
         }
      };
      return new UserDialog(parent, configuration, RelativePosition.RIGHT);
   }

   protected void toolStarted() {
   }

   public final void show() {
      UserDialog myDialog = this.getDialog();
      this.toolStarted();
      myDialog.setVisible(true);
   }

   protected void repaintPlate() {
      this.plate.repaint();
   }

   protected void saveCurrentState(String shortDescription) {
      this.plate.saveCurrentState(shortDescription);
   }

   public final Dimension getPreferredPlateSize() {
      return this.preferences.getDefaultDocumentSize();
   }

   protected abstract String getToolTitle();

   protected abstract ColorScheme getPreferredColorScheme();

   protected abstract String getToolActionName();

   protected abstract JComponent getOptionsComponent();

   protected Action[] getAdditionalActions() {
      return new Action[0];
   }
}
