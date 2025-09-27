package net.disy.commons.swing.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.Format;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import net.disy.commons.core.exception.UnreachableCodeReachedException;
import net.disy.commons.core.model.IObjectModel;
import net.disy.commons.core.model.IntModel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.ObjectUtilities;
import net.disy.commons.swing.color.SwingColors;
import net.disy.commons.swing.events.AbstractDocumentChangeListener;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.util.LayoutUtilities;
import net.disy.commons.swing.textfield.IntegerField;

public class TextWidgetFactory {
   private static final int DEFAULT_COLUMN_COUNT = 25;

   private TextWidgetFactory() {
      throw new UnreachableCodeReachedException();
   }

   public static JTextField createTextField(IObjectModel<String> stringModel) {
      return createTextField(stringModel, 25);
   }

   public static JTextField createTextField(IObjectModel<String> stringModel, int columnCount) {
      JTextField widget = new JTextField(columnCount);
      return connect(stringModel, widget);
   }

   private static JTextField connect(final IObjectModel<String> stringModel, final JTextField widget) {
      stringModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            String value = stringModel.getValue();
            if (!ObjectUtilities.equals(value, widget.getText())) {
               widget.setText(value);
            }
         }
      });
      widget.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            stringModel.setValue(widget.getText());
         }
      });
      widget.setText(stringModel.getValue());
      return widget;
   }

   public static JPasswordField createPasswordField(ObjectModel<String> model) {
      JPasswordField passwordField = new JPasswordField(25);
      connect(model, passwordField);
      return passwordField;
   }

   public static JComboBox createComboBox(final ObjectModel<String> stringModel, String[] values) {
      final JComboBox widget = new JComboBox<>(values);
      stringModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            String value = stringModel.getValue();
            if (!ObjectUtilities.equals(value, widget.getSelectedItem())) {
               widget.setSelectedItem(value);
            }
         }
      });
      widget.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            stringModel.setValue((String)widget.getSelectedItem());
         }
      });
      widget.setSelectedItem(stringModel.getValue());
      return widget;
   }

   public static JTextField createTextField(final IntModel intModel) {
      final IntegerField widget = new IntegerField();
      intModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            int value = intModel.getValue();
            if (value != widget.getInt()) {
               widget.setInt(value);
            }
         }
      });
      widget.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
         @Override
         protected void documentChanged() {
            intModel.setValue(widget.getInt());
         }
      });
      widget.setInt(intModel.getValue());
      return widget;
   }

   public static JFormattedTextField createUneditableDateWidget(final ObjectModel<Calendar> model, DateFormat format) {
      final JFormattedTextField widget = new JFormattedTextField(format);
      widget.setValue(model.getValue().getTime());
      model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            widget.setValue(model.getValue().getTime());
         }
      });
      widget.setEditable(false);
      return widget;
   }

   public static JComponent createInternalComponentWrappedTextFieldComponent(
      JComponent optionalWestComponent, final JTextField textField, JComponent optionalEastComponent
   ) {
      int spacing = LayoutUtilities.getDpiAdjusted(3);
      textField.setBorder(new EmptyBorder(0, spacing, 0, spacing));
      JPanel panel = new JPanel(new GridDialogLayout((optionalWestComponent == null ? 0 : 1) + 1 + (optionalEastComponent == null ? 0 : 1), false)) {
         @Override
         public void requestFocus() {
            textField.requestFocus();
         }
      };
      panel.setBackground(SwingColors.getTextAreaBackgroundColor());
      if (optionalWestComponent != null) {
         panel.add(optionalWestComponent);
      }

      panel.add(textField, GridDialogLayoutData.FILL_HORIZONTAL);
      if (optionalEastComponent != null) {
         panel.add(optionalEastComponent, GridDialogLayoutData.RIGHT);
      }

      panel.setBorder(BasicBorders.getTextFieldBorder());
      return panel;
   }
}
