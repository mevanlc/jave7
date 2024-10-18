package net.disy.commons.swing.calendar;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;
import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.ArrayUtilities;

public class SmartMonthChooser {
   private final JComboBox comboBox;
   private final ObjectModel<Calendar> model;
   private final ItemListener comboBoxListener = new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent iEvt) {
         int selectedMonth = SmartMonthChooser.this.comboBox.getSelectedIndex();
         Calendar newCalendar = (Calendar)SmartMonthChooser.this.model.getValue().clone();
         newCalendar.set(2, selectedMonth);
         SmartMonthChooser.this.model.setValue(newCalendar);
      }
   };
   private final IChangeListener modelListener = new IChangeListener() {
      @Override
      public void stateChanged() {
         SmartMonthChooser.this.updateComboBox();
      }
   };

   public SmartMonthChooser(ObjectModel<Calendar> model) {
      this.model = model;
      String[] months = new DateFormatSymbols().getMonths();
      months = ArrayUtilities.filter(months, new IPredicate<String>() {
         public boolean evaluate(String value) {
            return value.trim().length() > 0;
         }
      });
      this.comboBox = new JComboBox<>(months);
      this.comboBox.addItemListener(this.comboBoxListener);
      model.addChangeListener(this.modelListener);
      this.updateComboBox();
   }

   private int getMonth() {
      return this.model.getValue().get(2);
   }

   public JComponent getContent() {
      return this.comboBox;
   }

   private void updateComboBox() {
      this.comboBox.setSelectedIndex(this.getMonth());
   }
}
