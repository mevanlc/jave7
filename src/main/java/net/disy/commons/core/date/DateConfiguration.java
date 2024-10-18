package net.disy.commons.core.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class DateConfiguration implements IDateConfiguration, IDateParser {
   private static final String DATE_FORMAT_COMMENT_DELIMITER = "'";
   private static final String DEFAULT_PATTERN = "dd.MM.yyyy HH:mm:ss";
   private final String pattern;
   private final boolean yearEnabled;
   private final boolean monthEnabled;
   private final boolean dayEnabled;
   private final boolean hourEnabled;
   private final boolean minuteEnabled;
   private final boolean secondEnabled;
   private final SimpleDateFormat format;

   public DateConfiguration() {
      this(null);
   }

   public DateConfiguration(String pattern) {
      this.pattern = pattern == null ? "dd.MM.yyyy HH:mm:ss" : pattern;
      String pureDateFormatPattern = this.getPureDateFormat(this.pattern);
      this.yearEnabled = this.containsAnyChar(pureDateFormatPattern, "y");
      this.monthEnabled = this.containsAnyChar(pureDateFormatPattern, "MDw");
      this.dayEnabled = this.containsAnyChar(pureDateFormatPattern, "dDEFwW");
      this.hourEnabled = this.containsAnyChar(pureDateFormatPattern, "hHkKa");
      this.minuteEnabled = this.containsAnyChar(pureDateFormatPattern, "m");
      this.secondEnabled = this.containsAnyChar(pureDateFormatPattern, "s");
      this.format = new SimpleDateFormat(this.getFormatPattern());
   }

   @Override
   public String getFormatPattern() {
      return this.pattern;
   }

   @Override
   public boolean isYearEnabled() {
      return this.yearEnabled;
   }

   @Override
   public boolean isDayEnabled() {
      return this.dayEnabled;
   }

   @Override
   public boolean isMonthEnabled() {
      return this.monthEnabled;
   }

   @Override
   public boolean isHourEnabled() {
      return this.hourEnabled;
   }

   @Override
   public boolean isMinuteEnabled() {
      return this.minuteEnabled;
   }

   @Override
   public boolean isSecondEnabled() {
      return this.secondEnabled;
   }

   private boolean containsAnyChar(String s, String chars) {
      int len = chars.length();

      for (int i = 0; i < len; i++) {
         if (s.indexOf(chars.charAt(i)) >= 0) {
            return true;
         }
      }

      return false;
   }

   private String getPureDateFormat(String dateFormatPattern) {
      StringTokenizer st = new StringTokenizer(dateFormatPattern, "'", true);
      StringBuffer sb = new StringBuffer();
      boolean outsideComment = true;

      while (st.hasMoreTokens()) {
         String token = st.nextToken();
         if (token.equals("'")) {
            outsideComment = !outsideComment;
         } else if (outsideComment) {
            sb.append(token);
         }
      }

      return sb.toString();
   }

   @Override
   public boolean isDateEnabled() {
      return this.isYearEnabled() || this.isMonthEnabled() || this.isDayEnabled();
   }

   @Override
   public boolean isTimeEnabled() {
      return this.isHourEnabled() || this.isMinuteEnabled() || this.isSecondEnabled();
   }

   @Override
   public String formatDate(Date time) {
      return this.format.format(time);
   }

   @Override
   public Date parseDate(String dateString) throws ParseException {
      return dateString == null ? null : this.format.parse(dateString);
   }
}
