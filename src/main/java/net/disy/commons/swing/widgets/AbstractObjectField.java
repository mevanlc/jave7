package net.disy.commons.swing.objectfield;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.IBooleanModel;
import net.disy.commons.core.model.IObjectModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.Ensure;

public abstract class AbstractObjectField<T> implements IObjectField<T> {
   private final ObjectModel<T> model;
   private final BooleanModel validStateModel = new BooleanModel(true);
   private final JTextField textField;
   boolean isDocumentListenerEnabled = true;
   private final IObjectFormater<T> formater;

   public AbstractObjectField(IObjectFieldConfiguration<T> configuration) {
      Ensure.ensureNotNull(configuration);
      this.model = configuration.getModelFactory().createInstance();
      this.formater = configuration.getObjectFormater(this.model);
      this.textField = new JTextField(configuration.getColumns());
      this.textField.setEditable(configuration.isEditable());
      this.textField.setHorizontalAlignment(configuration.getHorizontalAlignment());
      this.textField.addActionListener(null);
      final Document document = this.textField.getDocument();
      if (configuration.isEditable()) {
         document.addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
               if (AbstractObjectField.this.isDocumentListenerEnabled) {
                  AbstractObjectField.this.updateModel(document);
               }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
               if (AbstractObjectField.this.isDocumentListenerEnabled) {
                  AbstractObjectField.this.updateModel(document);
               }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               if (AbstractObjectField.this.isDocumentListenerEnabled) {
                  AbstractObjectField.this.updateModel(document);
               }
            }
         });
      }

      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            AbstractObjectField.this.updateFieldText(document);
         }
      });
      this.textField.addKeyListener(new KeyListener() {
         @Override
         public void keyTyped(KeyEvent event) {
            if (event.getKeyChar() == 27) {
               AbstractObjectField.this.updateFieldText(document);
            }
         }

         @Override
         public void keyReleased(KeyEvent event) {
         }

         @Override
         public void keyPressed(KeyEvent event) {
         }
      });
      this.updateFieldText(document);
   }

   protected IObjectFormater<T> getObjectFormater() {
      return this.formater;
   }

   @Override
   public IObjectModel<T> getModel() {
      return this.model;
   }

   public void setHorizontalAlignment(int alignment) {
      this.textField.setHorizontalAlignment(alignment);
   }

   @Override
   public JComponent getContent() {
      return this.textField;
   }

   private String getText(Document document) {
      try {
         return document.getText(0, document.getLength());
      } catch (BadLocationException var3) {
         return "";
      }
   }

   @Override
   public IBooleanModel getValidStateModel() {
      return this.validStateModel;
   }

   synchronized void updateModel(Document document) {
      String text = this.getText(document);
      IObjectFormater<T> objectFormater = this.getObjectFormater();
      if (objectFormater.isValid(text)) {
         this.model.setValue(objectFormater.parse(text));
         this.validStateModel.setValue(true);
      } else {
         this.validStateModel.setValue(false);
      }
   }

   protected synchronized void updateFieldText(Document document) {
      String text = this.getText(document);
      T value = this.model.getValue();
      if (value != null || text != null && text.length() != 0) {
         IObjectFormater<T> objectFormater = this.getObjectFormater();
         if (text != null && text.length() != 0 && objectFormater.isValid(text) && objectFormater.parse(text).equals(value)) {
            this.validStateModel.setValue(true);
         } else {
            String textValue = objectFormater.format(value);

            try {
               if (document instanceof AbstractDocument) {
                  ((AbstractDocument)document).replace(0, document.getLength(), textValue, null);
               } else {
                  this.isDocumentListenerEnabled = false;
                  document.remove(0, document.getLength());
                  this.isDocumentListenerEnabled = true;
                  document.insertString(0, textValue, null);
               }

               this.validStateModel.setValue(objectFormater.isValid(textValue));
            } catch (BadLocationException var7) {
               throw new UnreachableCodeReachedException(var7);
            }
         }
      }
   }

   public void requestFocus() {
      this.textField.requestFocus();
   }

   public synchronized void addActionListener(ActionListener listener) {
      this.textField.addActionListener(listener);
   }

   public synchronized void removeActionListener(ActionListener listener) {
      this.textField.removeActionListener(listener);
   }
}
