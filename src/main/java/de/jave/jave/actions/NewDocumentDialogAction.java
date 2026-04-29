package de.jave.jave.actions;

import de.jave.jave.DocumentSizeLimits;
import de.jave.jave.JavEApplication;
import de.jave.jave.icon.JaveIcons;
import de.jave.jave.preferences.JaveApplicationPreferences;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.dialog.core.DialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogHeaderPanelConfiguration;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.userdialog.DefaultDialogConfiguration;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.dialog.userdialog.page.IDialogPage;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class NewDocumentDialogAction extends SmartAction {
   private final JavEApplication application;
   private final JaveApplicationPreferences preferences;

   public NewDocumentDialogAction(JavEApplication application, JaveApplicationPreferences preferences) {
      super("New...", JaveIcons.NEW_DOCUMENT_ICON);
      this.application = application;
      this.preferences = preferences;
      this.setToolTipText("Create a new Document with options");
   }

   @Override
   protected void execute(Component parentComponent) {
      Dimension size = this.preferences.getNewFileSize();
      final SpinnerNumberModel widthModel = new SpinnerNumberModel(
         DocumentSizeLimits.clampWidth(size.width),
         DocumentSizeLimits.MIN_WIDTH,
         DocumentSizeLimits.MAX_WIDTH,
         1
      );
      final SpinnerNumberModel heightModel = new SpinnerNumberModel(
         DocumentSizeLimits.clampHeight(size.height),
         DocumentSizeLimits.MIN_HEIGHT,
         DocumentSizeLimits.MAX_HEIGHT,
         1
      );
      final JCheckBox fillCheckBox = new JCheckBox("Fill canvas with character:", this.preferences.isNewFileFillEnabled());
      final JTextField fillField = new JTextField(String.valueOf(this.preferences.getNewFileFillCharacter()), 2);
      ((javax.swing.text.AbstractDocument)fillField.getDocument()).setDocumentFilter(new SingleCharDocumentFilter());
      fillField.setEnabled(fillCheckBox.isSelected());
      fillCheckBox.addChangeListener(new ChangeListener() {
         @Override
         public void stateChanged(ChangeEvent e) {
            fillField.setEnabled(fillCheckBox.isSelected());
         }
      });
      final JPanel panel = new JPanel(new GridDialogLayout(2, false));
      panel.add(new JLabel("Dimensions:"), GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      panel.add(new JLabel("Width:"), GridDialogLayoutData.RIGHT);
      panel.add(new JSpinner(widthModel));
      panel.add(new JLabel("Height:"), GridDialogLayoutData.RIGHT);
      panel.add(new JSpinner(heightModel));
      panel.add(fillCheckBox);
      panel.add(fillField);
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
            return "New File";
         }
      };
      UserDialog userDialog = new UserDialog(parentComponent, new DefaultDialogConfiguration<IDialogPage>(page) {
         @Override
         public IDialogHeaderPanelConfiguration getHeaderPanelConfiguration() {
            return DialogHeaderPanelConfiguration.createInvisible();
         }
      });
      IDialogResult result = userDialog.show();
      if (result.isCanceled()) {
         return;
      }
      int width = widthModel.getNumber().intValue();
      int height = heightModel.getNumber().intValue();
      boolean fillEnabled = fillCheckBox.isSelected();
      char fillChar = fillField.getText().isEmpty() ? ' ' : fillField.getText().charAt(0);
      Dimension newSize = new Dimension(width, height);
      this.preferences.setNewFileSize(newSize);
      this.preferences.setNewFileFillEnabled(fillEnabled);
      this.preferences.setNewFileFillCharacter(fillChar);
      this.preferences.flush();
      this.application.doNew(newSize, fillEnabled ? Character.valueOf(fillChar) : null);
   }

   private static final class SingleCharDocumentFilter extends javax.swing.text.DocumentFilter {
      @Override
      public void insertString(FilterBypass fb, int offset, String string, javax.swing.text.AttributeSet attr)
         throws javax.swing.text.BadLocationException {
         this.replace(fb, offset, 0, string, attr);
      }

      @Override
      public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs)
         throws javax.swing.text.BadLocationException {
         if (text == null || text.isEmpty()) {
            fb.replace(offset, length, text, attrs);
            return;
         }
         String trimmed = text.substring(0, 1);
         int currentLength = fb.getDocument().getLength();
         int remaining = 1 - (currentLength - length);
         if (remaining <= 0) {
            fb.replace(0, currentLength, trimmed, attrs);
         } else {
            fb.replace(offset, length, trimmed, attrs);
         }
      }
   }
}
