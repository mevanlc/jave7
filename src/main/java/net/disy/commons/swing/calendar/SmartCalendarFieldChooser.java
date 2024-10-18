package net.disy.commons.swing.calendar;

import java.util.Calendar;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class SmartCalendarFieldChooser {
   private final JSpinner spinner;
   private final ObjectModel<Calendar> model;
   private final int calendarField;
   private final ChangeListener modelListener = new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent changeEvent) {
         int selectedValue = SmartCalendarFieldChooser.this.getSpinnerModel().getNumber().intValue();
         Calendar newCalendar = (Calendar)SmartCalendarFieldChooser.this.model.getValue().clone();
         newCalendar.set(SmartCalendarFieldChooser.this.calendarField, selectedValue);
         SmartCalendarFieldChooser.this.model.setValue(newCalendar);
      }
   };
   private final IChangeListener spinnerListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         SmartCalendarFieldChooser.this.updateSpinner();
      }
   };

   public static SmartCalendarFieldChooser createSecondChooser(ObjectModel<Calendar> model) {
      return new SmartCalendarFieldChooser(model, 13, new SpinnerNumberModel(0, 0, 59, 1));
   }

   public static SmartCalendarFieldChooser createMinuteChooser(ObjectModel<Calendar> model) {
      return new SmartCalendarFieldChooser(model, 12, new SpinnerNumberModel(0, 0, 59, 1));
   }

   public static SmartCalendarFieldChooser createHourChooser(ObjectModel<Calendar> model) {
      return new SmartCalendarFieldChooser(model, 10, new SpinnerNumberModel(0, 0, 23, 1));
   }

   public static SmartCalendarFieldChooser createYearChooser(ObjectModel<Calendar> model) {
      return new SmartCalendarFieldChooser(model, 1);
   }

   private SmartCalendarFieldChooser(ObjectModel<Calendar> model, int calendarField) {
      this(model, calendarField, new SpinnerNumberModel());
   }

   private SmartCalendarFieldChooser(ObjectModel<Calendar> model, int calendarField, SpinnerNumberModel spinnerModel) {
      this.spinner = new JSpinner(spinnerModel);
      this.model = model;
      this.calendarField = calendarField;
      model.addChangeListener(this.spinnerListener);
      this.getSpinnerModel().addChangeListener(this.modelListener);
      this.spinner.setEditor(new NumberEditor(this.spinner, "#####"));
      this.updateSpinner();
   }

   public JComponent getContent() {
      return this.spinner;
   }

   private void updateSpinner() {
      SpinnerNumberModel numberModel = this.getSpinnerModel();
      numberModel.setValue(this.getYear());
      numberModel.setMinimum(this.model.getValue().getMinimum(this.calendarField));
      numberModel.setMaximum(this.model.getValue().getMaximum(this.calendarField));
   }

   private int getYear() {
      return this.model.getValue().get(this.calendarField);
   }

   private SpinnerNumberModel getSpinnerModel() {
      return (SpinnerNumberModel)this.spinner.getModel();
   }
}
