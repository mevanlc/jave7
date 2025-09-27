package net.disy.commons.swing.dialog.input.date;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JFormattedTextField.AbstractFormatter;
import net.disy.commons.core.date.DateConfiguration;
import net.disy.commons.core.date.IDateConfiguration;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.util.DateUtilities;
import net.disy.commons.swing.action.SmartAction;
import net.disy.commons.swing.calendar.CalendarIcons;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.core.IDialogResult;
import net.disy.commons.swing.dialog.input.AbstractLabeledSmartDialogPanel;
import net.disy.commons.swing.dialog.input.IMessageProducingValidator;
import net.disy.commons.swing.dialog.userdialog.UserDialog;
import net.disy.commons.swing.icon.CommonIcons;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;
import net.disy.commons.swing.toolbar.ToolBarUtilities;

public class DateSmartDialogPanel extends AbstractLabeledSmartDialogPanel {
   private final ObjectModel<Date> dateModel;
   private final SimpleDateFormat dateFormat;
   private final IDateConfiguration dateConfiguration;
   private AbstractButton editButton;
   private final boolean enableDelete;
   private JFormattedTextField textField;

   public DateSmartDialogPanel(String label, ObjectModel<Date> dateModel, IMessageProducingValidator validator, String pattern) {
      this(label, dateModel, validator, pattern, true);
   }

   public DateSmartDialogPanel(String label, ObjectModel<Date> dateModel, IMessageProducingValidator validator, String pattern, boolean enableDelete) {
      super(label, validator);
      this.enableDelete = enableDelete;
      this.dateConfiguration = new DateConfiguration(pattern);
      this.dateFormat = new SimpleDateFormat(this.dateConfiguration.getFormatPattern());
      this.dateModel = dateModel;
   }

   @Override
   protected JComponent fillMainComponentInto(JPanel panel, int columnCount) {
      JPanel editPanel = new JPanel(new GridDialogLayout(this.enableDelete ? 3 : 2, false));
      editPanel.add(this.createDateTextField(), GridDialogLayoutData.FILL_HORIZONTAL);
      this.editButton = this.createEditButton();
      editPanel.add(this.editButton, GridDialogLayoutDataFactory.createFillNoGrab());
      if (this.enableDelete) {
         editPanel.add(this.createDeleteButton());
      }

      panel.add(editPanel, GridDialogLayoutDataFactory.createHorizontalSpanData(columnCount, GridDialogLayoutData.FILL_HORIZONTAL));
      return this.editButton;
   }

   private AbstractButton createDeleteButton() {
      final AbstractButton button = ToolBarUtilities.createToolBarButton(
         new SmartAction(DisyCommonsSwingDialogMessages.getString("DateSmartDialogPanel.Tooltip.Delete"), CommonIcons.DELETE) {
            @Override
            protected void execute(Component parentComponent) {
               DateSmartDialogPanel.this.dateModel.setValue(null);
            }
         }
      );
      this.dateModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            button.setEnabled(DateSmartDialogPanel.this.dateModel.getValue() != null);
         }
      });
      button.setEnabled(this.dateModel.getValue() != null);
      return button;
   }

   private AbstractButton createEditButton() {
      return ToolBarUtilities.createToolBarButton(
         new SmartAction(DisyCommonsSwingDialogMessages.getString("DateSmartDialogPanel.Tooltip.Edit"), CalendarIcons.DATE_ICON) {
            @Override
            protected void execute(Component parentComponent) {
               Date date = DateSmartDialogPanel.this.dateModel.getValue();
               ObjectModel<Calendar> calendarModel = new ObjectModel<>();
               calendarModel.setValue(date == null ? new GregorianCalendar() : DateUtilities.toCalendar(date));
               UserDialog userDialog = new UserDialog(parentComponent, new CalendarDialogPage(calendarModel, DateSmartDialogPanel.this.dateConfiguration));
               IDialogResult result = userDialog.show();
               if (!result.isCanceled()) {
                  DateSmartDialogPanel.this.dateModel.setValue(calendarModel.getValue().getTime());
               }
            }
         }
      );
   }

   private JFormattedTextField createDateTextField() {
      this.textField = new JFormattedTextField(new AbstractFormatter() {
         @Override
         public Object stringToValue(String text) {
            throw new UnsupportedOperationException("Text field not editable.");
         }

         @Override
         public String valueToString(Object value) {
            return value == null ? "" : DateSmartDialogPanel.this.dateFormat.format(DateSmartDialogPanel.this.dateModel.getValue());
         }
      });
      this.dateModel.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            DateSmartDialogPanel.this.textField.setValue(DateSmartDialogPanel.this.dateModel.getValue());
         }
      });
      this.textField.setValue(this.dateModel.getValue());
      this.textField.setEditable(false);
      return this.textField;
   }

   @Override
   protected int getMainComponentColumnCount() {
      return 1;
   }

   @Override
   public void addChangeListener(IChangeListener listener) {
      this.dateModel.addChangeListener(listener);
   }

   @Override
   public void requestFocus() {
      if (this.editButton != null) {
         this.editButton.requestFocus();
      }
   }

   @Override
   protected void setMainComponentEnabled(boolean enabled) {
      this.textField.setEnabled(enabled);
      this.editButton.setEnabled(enabled);
   }

   @Override
   protected JComponent[] getOtherComponents() {
      return new JComponent[]{this.textField, this.editButton};
   }
}
