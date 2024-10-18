package net.disy.commons.core.date;

import java.text.ParseException;
import java.util.Date;

public interface IDateConfiguration {
   boolean isDateEnabled();

   boolean isTimeEnabled();

   boolean isDayEnabled();

   boolean isMonthEnabled();

   boolean isYearEnabled();

   boolean isHourEnabled();

   boolean isMinuteEnabled();

   boolean isSecondEnabled();

   String getFormatPattern();

   String formatDate(Date var1);

   Date parseDate(String var1) throws ParseException;
}
