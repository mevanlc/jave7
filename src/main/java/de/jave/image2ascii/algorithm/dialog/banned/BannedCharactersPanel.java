package de.jave.image2ascii.algorithm.dialog.banned;

import de.jave.jave.JaveGlobalRessources;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dizzy.commons.core.model.listener.IChangeListener;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.action.SmartAction;
import net.dizzy.commons.swing.dialog.core.IDialogResult;
import net.dizzy.commons.swing.dialog.userdialog.UserDialog;
import net.dizzy.commons.swing.events.AbstractDocumentChangeListener;
import net.dizzy.commons.swing.fontchooser.model.FontModel;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.IDialogComponent;

public class BannedCharactersPanel implements IDialogComponent {
   private final JTextField bannedCharactersTextField;
   private final FontModel fontModel;
   private final BannedCharactersModel model;
   private final IChangeListener modelChangeListener;

   public BannedCharactersPanel(final BannedCharactersModel model, FontModel fontModel) {
      Ensure.ensureArgumentNotNull(model);
      Ensure.ensureArgumentNotNull(fontModel);
      this.model = model;
      this.fontModel = fontModel;
      this.bannedCharactersTextField = new JTextField(20);
      this.bannedCharactersTextField.setEditable(true);
      this.bannedCharactersTextField.setFont(JaveGlobalRessources.FONT_SMALL_FIXEDWIDTH);
      this.bannedCharactersTextField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            if (BannedCharactersPanel.this.bannedCharactersTextField.isVisible()) {
               model.setBannedCharacters(BannedCharactersPanel.this.bannedCharactersTextField.getText());
            }
         }
      });
      this.modelChangeListener = new IChangeListener() {
         @Override
         public void stateChanged() {
            BannedCharactersPanel.this.updateView();
         }
      };
      model.addChangeListener(this.modelChangeListener);
      this.updateView();
   }

   private void updateView() {
      if (!new AsciiCharacterSetModel(this.bannedCharactersTextField.getText()).equals(new AsciiCharacterSetModel(this.model.getBannedCharacters()))) {
         this.bannedCharactersTextField.setText(this.model.getBannedCharacters());
      }
   }

   @Override
   public void fillInto(JPanel panel, int columnCount) {
      panel.add(new JLabel("Banned characters:"), GridDialogLayoutData.RIGHT);
      GridDialogLayoutData layoutData = new GridDialogLayoutData(GridDialogLayoutData.FILL_HORIZONTAL);
      layoutData.setHorizontalSpan(columnCount - 2);
      panel.add(this.bannedCharactersTextField, layoutData);
      panel.add(
         new JButton(
            new SmartAction("Choose...") {
               @Override
               protected void execute(Component parentComponent) {
                  BannedCharactersDialogPage page = new BannedCharactersDialogPage(
                     BannedCharactersPanel.this.model.getBannedCharacters(), BannedCharactersPanel.this.fontModel.getFont()
                  );
                  UserDialog dialog = new UserDialog(parentComponent, page);
                  IDialogResult result = dialog.show();
                  if (!result.isCanceled()) {
                     BannedCharactersPanel.this.model.setBannedCharacters(page.getBannedCharcaters());
                  }
               }
            }
         )
      );
   }

   @Override
   public int getColumnCount() {
      return 3;
   }

   public void dispose() {
      this.model.removeChangeListener(this.modelChangeListener);
   }
}
