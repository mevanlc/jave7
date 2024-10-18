package net.disy.commons.core.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtilities {
   public static Date createDate(int year, int month, int day) {
      Ensure.ensureArgumentTrue("Illegal value for month: " + month, month >= 1);
      Ensure.ensureArgumentTrue("Illegal value for month: " + month, month <= 12);
      return new GregorianCalendar(year, month - 1, day).getTime();
   }

   public static boolean isValidDateFormatOrNull(String dateFormat) {
      if (dateFormat == null) {
         return true;
      } else {
         try {
            new SimpleDateFormat(dateFormat);
            return true;
         } catch (Exception var2) {
            return false;
         }
      }
   }

   public static Calendar toCalendar(Date date) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      return calendar;
   }

   public static Date add(Date date, int field, int value) {
      if (date instanceof java.sql.Date) {
         return add((java.sql.Date)date, field, value);
      } else {
         Calendar calendar = toCalendar(date);
         calendar.add(field, value);
         return calendar.getTime();
      }
   }

   public static java.sql.Date add(java.sql.Date date, int field, int value) {
      Calendar calendar = toCalendar(date);
      calendar.add(field, value);
      return new java.sql.Date(calendar.getTime().getTime());
   }

   public static int getYear(Date date) {
      return toCalendar(date).get(1);
   }
}
