package de.jave.jave.actions.preferences;

import de.jave.jave.preferences.JaveApplicationPreferences;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.dizzy.commons.core.util.Ensure;
import net.dizzy.commons.swing.layout.grid.GridDialogLayout;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutData;
import net.dizzy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.dizzy.commons.swing.widgets.AutoWrappingLabel;

public class AuthorPreferencesPanel implements IJavePreferencesPanel {
   private final JPanel content;
   private final JaveApplicationPreferences preferences;
   private final JTextField authorTextField;
   private final JTextField emailTextField;

   public AuthorPreferencesPanel(JaveApplicationPreferences preferences) {
      Ensure.ensureArgumentNotNull(preferences);
      this.preferences = preferences;
      JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(
         new AutoWrappingLabel("User data is optional. If specified, it will be saved to the meta data section in new animations and FIGlet fonts.")
            .getContent(),
         GridDialogLayoutDataFactory.createHorizontalSpanData(2, GridDialogLayoutData.FILL_HORIZONTAL)
      );
      panel.add(new JLabel("Name:"), GridDialogLayoutData.RIGHT);
      this.authorTextField = new JTextField(22);
      panel.add(this.authorTextField);
      panel.add(new JLabel("E-mail:"), GridDialogLayoutData.RIGHT);
      this.emailTextField = new JTextField(22);
      panel.add(this.emailTextField);
      this.authorTextField.setText(preferences.getAuthorName());
      this.emailTextField.setText(preferences.getAuthorMail());
      this.content = panel;
   }

   @Override
   public void savePreferences() {
      this.preferences.setAuthorName(this.authorTextField.getText());
      this.preferences.setAuthorMail(this.emailTextField.getText());
   }

   @Override
   public JComponent getContent() {
      return this.content;
   }

   @Override
   public String getTitle() {
      return "User Data";
   }
}
