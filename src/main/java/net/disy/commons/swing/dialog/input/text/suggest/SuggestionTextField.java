package net.disy.commons.swing.dialog.input.text.suggest;

import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.disy.commons.core.asynchronous.AsynchronousDroppingJobProcessor;
import net.disy.commons.core.asynchronous.IJobProcessor;
import net.disy.commons.core.exception.IExceptionHandler;
import net.disy.commons.core.model.BooleanModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.progress.ICancelable;
import net.disy.commons.core.progress.ProgressUtilities;
import net.disy.commons.core.text.SuggestionResult;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.IBlock;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.component.IComponentContainer;
import net.disy.commons.swing.dialog.input.text.suggest.internal.SuggestionWindow;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.text.ClearTextFieldButton;
import net.disy.commons.swing.text.TextWidgetFactory;

public class SuggestionTextField<T> implements IComponentContainer {
   private final JTextField textField = new JTextField(14);
   private final SuggestionWindow<T> window;
   private final ISuggestionTextFieldConfiguration<T> configuration;
   private final AbstractDocumentChangeListener documentListener;
   private final AsynchronousDroppingJobProcessor<String> textUpdateProcessor;
   private JComponent textFieldComponent;

   public SuggestionTextField(ISuggestionTextFieldConfiguration<T> configuration) {
      this(configuration, null);
   }

   public SuggestionTextField(final ISuggestionTextFieldConfiguration<T> configuration, JComponent optionalLeftInsideTextFieldComponent) {
      Ensure.ensureArgumentNotNull(configuration);
      this.configuration = configuration;
      ClearTextFieldButton button = new ClearTextFieldButton(this.textField, null);
      this.textFieldComponent = TextWidgetFactory.createInternalComponentWrappedTextFieldComponent(optionalLeftInsideTextFieldComponent, this.textField, button);
      this.textField.setToolTipText(configuration.getToolTipText());
      IBlock doubleClickCallback = new IBlock() {
         @Override
         public void execute() {
            SuggestionTextField.this.confirmSelection();
         }
      };
      this.window = new SuggestionWindow<>(this.textFieldComponent, configuration, doubleClickCallback);
      IExceptionHandler exceptionHandler = new IExceptionHandler() {
         @Override
         public void handle(Throwable exception) {
            if (exception instanceof Error) {
               throw (Error)exception;
            } else if (exception instanceof RuntimeException) {
               throw (RuntimeException)exception;
            } else {
               throw new RuntimeException(exception);
            }
         }
      };
      IJobProcessor<String> textUpdateHandler = new IJobProcessor<String>() {
         public void process(ICancelable cancelable, String text) throws InterruptedException {
            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SuggestionTextField.this.window.showWithQueryStarted();
                  SuggestionTextField.this.textField.requestFocus();
               }
            });

            final SuggestionResult<T> suggestionResult;
            try {
               suggestionResult = configuration.getSuggestionsForStringProvider().querySuggestions(text, cancelable);
            } catch (final IOException var5) {
               ProgressUtilities.checkInterrupted(cancelable);
               SwingUtilities.invokeLater(new Runnable() {
                  @Override
                  public void run() {
                     SuggestionTextField.this.window.dispose();
                     configuration.getErrorHandler().handleError(SuggestionTextField.this.textField, var5);
                  }
               });
               return;
            }

            SwingUtilities.invokeLater(new Runnable() {
               @Override
               public void run() {
                  SuggestionTextField.this.window.showLoadedSuggestions(suggestionResult);
                  SuggestionTextField.this.textField.requestFocus();
               }
            });
         }
      };
      this.textUpdateProcessor = new AsynchronousDroppingJobProcessor<>(textUpdateHandler, exceptionHandler);
      this.connectWaitCursorForBusy(this.textUpdateProcessor.getBusyModel());
      this.documentListener = new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            SuggestionTextField.this.startQueryForCurrentText();
         }
      };
      this.textField.getDocument().addDocumentListener(this.documentListener);
      this.textField.addKeyListener(new KeyAdapter() {
         @Override
         public void keyTyped(KeyEvent e) {
            if (e.getKeyChar() == '\n') {
               SuggestionTextField.this.confirmSelection();
            }
         }

         @Override
         public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == 27) {
               SuggestionTextField.this.window.dispose();
            } else if (e.getKeyCode() == 40) {
               SuggestionTextField.this.window.moveSelectionDown();
               e.consume();
            } else if (e.getKeyCode() == 38) {
               SuggestionTextField.this.window.moveSelectionUp();
               e.consume();
            }
         }
      });
   }

   private void connectWaitCursorForBusy(final BooleanModel busyModel) {
      busyModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            if (busyModel.getValue()) {
               SuggestionTextField.this.textField.setCursor(Cursor.getPredefinedCursor(3));
               SuggestionTextField.this.textFieldComponent.setCursor(Cursor.getPredefinedCursor(3));
            } else {
               SuggestionTextField.this.textField.setCursor(Cursor.getPredefinedCursor(2));
               SuggestionTextField.this.textFieldComponent.setCursor(null);
            }
         }
      });
   }

   @Override
   public JComponent getContent() {
      return this.textFieldComponent;
   }

   private void confirmSelection() {
      if (!this.window.isVisible()) {
         this.startQueryForCurrentText();
      } else {
         T item = this.window.getSelectedItem();
         if (item == null) {
            this.textField.getToolkit().beep();
         } else {
            this.textField.getDocument().removeDocumentListener(this.documentListener);
            String searchText = this.configuration.getRawSearchText(item);
            if (searchText != null) {
               this.textField.setText(searchText);
            }

            selectAllWithCaretAtFirstPosition(this.textField);
            this.textField.getDocument().addDocumentListener(this.documentListener);
            this.window.dispose();
            ISuggestionActionHandler<T> actionHandler = this.configuration.getActionHandler();
            actionHandler.handle(this.textField, item);
         }
      }
   }

   private static void selectAllWithCaretAtFirstPosition(JTextField textField) {
      textField.setCaretPosition(textField.getText().length());
      textField.moveCaretPosition(0);
   }

   private void startQueryForCurrentText() {
      String text = this.textField.getText();
      if (StringUtilities.isNullOrEmpty(text)) {
         this.textUpdateProcessor.cancelCurrentJob();
         this.window.dispose();
      } else {
         this.textUpdateProcessor.startJob(text);
      }
   }
}
