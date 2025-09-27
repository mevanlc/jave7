package net.disy.commons.swing.dialog.io;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.disy.commons.core.io.FileDisplayNameUtilities;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public class NonEditableFileStringTextField {
   private final ObjectModel<String> fileNameModel;
   private final JTextField textField;

   public NonEditableFileStringTextField(ObjectModel<String> fileNameModel) {
      Ensure.ensureArgumentNotNull(fileNameModel);
      this.textField = new JTextField(20);
      this.textField.setEditable(false);
      this.fileNameModel = fileNameModel;
      fileNameModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            NonEditableFileStringTextField.this.updateFileName();
         }
      });
      this.textField.addComponentListener(new ComponentAdapter() {
         @Override
         public void componentResized(ComponentEvent e) {
            NonEditableFileStringTextField.this.updateFileName();
         }
      });
      this.updateFileName();
   }

   private void updateFileName() {
      final String fileName = this.fileNameModel.getValue();
      SwingUtilities.invokeLater(
         new Runnable() {
            @Override
            public void run() {
               if (fileName == null) {
                  NonEditableFileStringTextField.this.textField.setText("");
                  NonEditableFileStringTextField.this.textField.setToolTipText(null);
               } else {
                  NonEditableFileStringTextField.this.textField.setToolTipText(fileName);
                  int length = 10;

                  while (
                     length + 1 <= fileName.length()
                        && NonEditableFileStringTextField.fitsLengthInTextField(
                           NonEditableFileStringTextField.this.textField, FileDisplayNameUtilities.createShortenedFileName(fileName, length + 1)
                        )
                  ) {
                     length++;
                  }

                  String shortenedFileName = FileDisplayNameUtilities.createShortenedFileName(fileName, length);
                  NonEditableFileStringTextField.this.textField.setText(shortenedFileName);
               }
            }
         }
      );
   }

   private static boolean fitsLengthInTextField(JTextField textField, String text) {
      int availableWidth = textField.getBounds().width;
      Insets borderInsets = textField.getBorder().getBorderInsets(textField);
      availableWidth -= borderInsets.left;
      availableWidth -= borderInsets.right;
      Font font = textField.getFont();
      int stringWidth = textField.getFontMetrics(font).stringWidth(text);
      return stringWidth <= availableWidth;
   }

   public JComponent getContent() {
      return this.textField;
   }

   public void setEnabled(boolean enabled) {
      this.textField.setEnabled(enabled);
   }
}
