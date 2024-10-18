package net.disy.commons.swing.calendar;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.model.listener.IChangeListener;

public class SmartDayChooser {
   private final JPanel content = new JPanel(new GridLayout(7, 7));
   private final JButton[] days;
   private JButton selectedDay;
   private Color oldDayBackground;
   private final String[] dayNames;
   private final ObjectModel<Calendar> model;
   private final Calendar today = new GregorianCalendar();

   private static String[] createDayNames() {
      return new DateFormatSymbols().getShortWeekdays();
   }

   public SmartDayChooser(ObjectModel<Calendar> model) {
      this.model = model;
      this.dayNames = createDayNames();
      this.model.addChangeListener(new IChangeListener() {
         @Override
         public void stateChanged() {
            SmartDayChooser.this.drawDays();
         }
      });
      this.days = new JButton[49];
      this.selectedDay = null;

      for (int rowIndex = 0; rowIndex < 7; rowIndex++) {
         for (int columnIndex = 0; columnIndex < 7; columnIndex++) {
            int buttonIndex = columnIndex + 7 * rowIndex;
            this.days[buttonIndex] = rowIndex == 0 ? this.createHeaderButton() : this.createDayButton();
            this.days[buttonIndex].setMargin(new Insets(0, 0, 0, 0));
            this.days[buttonIndex].setFocusPainted(false);
            this.content.add(this.days[buttonIndex]);
         }
      }

      this.init();
      this.updateCalendar(model.getValue().get(5));
   }

   private JButton createDayButton() {
      JButton button = new JButton("x");
      button.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JButton sourceButton = (JButton)e.getSource();
            int day = Integer.valueOf(sourceButton.getText());
            SmartDayChooser.this.updateCalendar(day);
         }
      });
      return button;
   }

   private JButton createHeaderButton() {
      JButton button = new JButton() {
         @Override
         public void addMouseListener(MouseListener l) {
         }
      };
      button.setBackground(new Color(200, 200, 255));
      return button;
   }

   private void drawDays() {
      Calendar tmpCalendar = (Calendar)this.model.getValue().clone();
      int firstDayOfWeek = tmpCalendar.getFirstDayOfWeek();
      tmpCalendar.set(5, 1);
      int firstDay = tmpCalendar.get(7) - firstDayOfWeek;
      if (firstDay < 0) {
         firstDay += 7;
      }

      int i;
      for (i = 0; i < firstDay; i++) {
         this.days[i + 7].setVisible(false);
         this.days[i + 7].setText("");
      }

      tmpCalendar.add(2, 1);
      Date firstDayInNextMonth = tmpCalendar.getTime();
      tmpCalendar.add(2, -1);
      Date day = tmpCalendar.getTime();
      int n = 0;

      for (Color foreground = this.content.getForeground(); day.before(firstDayInNextMonth); day = tmpCalendar.getTime()) {
         this.days[i + n + 7].setText(Integer.toString(n + 1));
         this.days[i + n + 7].setVisible(true);
         if (tmpCalendar.get(6) == this.today.get(6) && tmpCalendar.get(1) == this.today.get(1)) {
            this.days[i + n + 7].setForeground(Color.red);
         } else {
            this.days[i + n + 7].setForeground(foreground);
         }

         if (n + 1 == this.model.getValue().get(5)) {
            this.days[i + n + 7].setBackground(Color.gray);
            this.selectedDay = this.days[i + n + 7];
         } else {
            this.days[i + n + 7].setBackground(this.oldDayBackground);
         }

         n++;
         tmpCalendar.add(5, 1);
      }

      for (int k = n + i + 7; k < 49; k++) {
         this.days[k].setVisible(false);
         this.days[k].setText("");
      }
   }

   public JComponent getContent() {
      return this.content;
   }

   private void init() {
      int firstDayOfWeek = this.model.getValue().getFirstDayOfWeek();
      int day = firstDayOfWeek;

      for (int dayInWeek = 0; dayInWeek < 7; dayInWeek++) {
         this.days[dayInWeek].setText(this.dayNames[day]);
         if (day == 1) {
            this.days[dayInWeek].setForeground(Color.red);
         } else {
            this.days[dayInWeek].setForeground(Color.blue);
         }

         if (day < 7) {
            day++;
         } else {
            day -= 6;
         }
      }

      this.oldDayBackground = new JButton().getBackground();
      this.drawDays();
   }

   private void updateCalendar(int monthDay) {
      Calendar temporaryCalendar = (Calendar)this.model.getValue().clone();
      temporaryCalendar.set(5, 1);
      temporaryCalendar.add(2, 1);
      temporaryCalendar.add(5, -1);
      int maxDaysInMonth = temporaryCalendar.get(5);
      if (monthDay > maxDaysInMonth) {
         monthDay = maxDaysInMonth;
      }

      if (this.selectedDay != null) {
         this.selectedDay.setBackground(this.oldDayBackground);
         this.selectedDay.repaint();
      }

      for (int i = 7; i < 49; i++) {
         if (this.days[i].getText().equals(Integer.toString(monthDay))) {
            this.selectedDay = this.days[i];
            this.selectedDay.setBackground(Color.gray);
            break;
         }
      }

      Calendar newCalendar = (Calendar)this.model.getValue().clone();
      newCalendar.set(5, monthDay);
      this.model.setValue(newCalendar);
   }
}
