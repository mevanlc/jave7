package net.disy.commons.core.util;

import java.util.Date;

public class DateRange {
   private final Date firstDay;
   private final Date lastDay;

   public static DateRange year(int year) {
      Date firstDay = DateUtilities.createDate(year, 1, 1);
      Date lastDay = DateUtilities.createDate(year, 12, 31);
      return new DateRange(firstDay, lastDay);
   }

   public DateRange(Date firstDay, Date lastDay) {
      this.firstDay = firstDay;
      this.lastDay = lastDay;
   }

   public Date getFirstDay() {
      return this.firstDay;
   }

   public Date getLastDay() {
      return this.lastDay;
   }
}
