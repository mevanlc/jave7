package net.disy.commons.swing.dialog.input.date;

import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.disy.commons.core.date.IDateConfiguration;
import net.disy.commons.core.message.IBasicMessage;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.calendar.SmartCalendarFieldChooser;
import net.disy.commons.swing.calendar.SmartDayChooser;
import net.disy.commons.swing.calendar.SmartMonthChooser;
import net.disy.commons.swing.dialog.DisyCommonsSwingDialogMessages;
import net.disy.commons.swing.dialog.userdialog.page.AbstractDialogPage;
import net.disy.commons.swing.label.SmartLabel;
import net.disy.commons.swing.layout.grid.EndOfLineMarkerComponent;
import net.disy.commons.swing.layout.grid.GridDialogLayout;
import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.disy.commons.swing.layout.grid.GridDialogLayoutDataFactory;

public class CalendarDialogPage extends AbstractDialogPage {
   private final ObjectModel<Calendar> calendarModel;
   private final IDateConfiguration dateConfiguration;

   public CalendarDialogPage(ObjectModel<Calendar> calendar, IDateConfiguration dateConfiguration) {
      super(DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.DefaultMessage"));
      this.calendarModel = calendar;
      this.dateConfiguration = dateConfiguration;
   }

   @Override
   public JComponent createContent() {
      JPanel panel = new JPanel(new GridDialogLayout(3, false));
      if (this.dateConfiguration.isDayEnabled()) {
         JComponent chooser = new SmartDayChooser(this.calendarModel).getContent();
         panel.add(
            new SmartLabel(DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.DayLabel"), chooser), GridDialogLayoutDataFactory.createTopData()
         );
         panel.add(chooser, GridDialogLayoutDataFactory.createHorizontalSpanData(2));
      }

      if (this.dateConfiguration.isMonthEnabled()) {
         JComponent chooser = new SmartMonthChooser(this.calendarModel).getContent();
         panel.add(new SmartLabel(DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.MonthLabel"), chooser));
         panel.add(chooser, GridDialogLayoutDataFactory.createFillNoGrab());
         panel.add(new EndOfLineMarkerComponent());
      }

      if (this.dateConfiguration.isYearEnabled()) {
         JComponent chooser = SmartCalendarFieldChooser.createYearChooser(this.calendarModel).getContent();
         panel.add(new SmartLabel(DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.YearLabel"), chooser));
         panel.add(chooser, GridDialogLayoutDataFactory.createFillNoGrab());
         panel.add(new EndOfLineMarkerComponent());
      }

      if (this.dateConfiguration.isTimeEnabled()) {
         this.addTimePanel(panel);
      }

      return new HorizontalCenteredPanel(panel);
   }

   private void addTimePanel(JPanel parentPanel) {
      parentPanel.add(new SmartLabel(DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.TimeLabel")));
      String labelString = "";
      JPanel timePanel = new JPanel(new GridDialogLayout(this.getTimeColumnCount(), false));
      timePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      if (this.dateConfiguration.isHourEnabled()) {
         JComponent chooser = SmartCalendarFieldChooser.createHourChooser(this.calendarModel).getContent();
         timePanel.add(chooser, GridDialogLayoutData.FILL_HORIZONTAL);
         labelString = labelString + "dd";
      }

      if (!StringUtilities.isNullOrEmpty(labelString)) {
         timePanel.add(new SmartLabel(":"));
         labelString = labelString + ":";
      }

      if (this.dateConfiguration.isMinuteEnabled()) {
         JComponent chooser = SmartCalendarFieldChooser.createMinuteChooser(this.calendarModel).getContent();
         timePanel.add(chooser, GridDialogLayoutData.FILL_HORIZONTAL);
         labelString = labelString + "mm";
      }

      if (!StringUtilities.isNullOrEmpty(labelString)) {
         timePanel.add(new SmartLabel(":"));
         labelString = labelString + ":";
      }

      if (this.dateConfiguration.isSecondEnabled()) {
         JComponent chooser = SmartCalendarFieldChooser.createSecondChooser(this.calendarModel).getContent();
         timePanel.add(chooser, GridDialogLayoutData.FILL_HORIZONTAL);
         labelString = labelString + "ss";
      }

      parentPanel.add(timePanel, GridDialogLayoutDataFactory.createFillNoGrab());
      parentPanel.add(new SmartLabel("(" + labelString + ")"));
   }

   private int getTimeColumnCount() {
      int fieldCount = 0;
      if (this.dateConfiguration.isHourEnabled()) {
         fieldCount++;
      }

      if (this.dateConfiguration.isMinuteEnabled()) {
         fieldCount++;
      }

      if (this.dateConfiguration.isSecondEnabled()) {
         fieldCount++;
      }

      return 2 * fieldCount;
   }

   @Override
   public IBasicMessage createCurrentMessage() {
      return this.getDefaultMessage();
   }

   @Override
   public String getTitle() {
      return DisyCommonsSwingDialogMessages.getString("CalendarDialogPage.Title");
   }
}
