package de.jave.javeplayer;

import net.disy.commons.core.util.ObjectUtilities;

public class AnimationMetaData {
   private String authorName;
   private String authorEmail;
   private String software;
   private String date;
   private String title;

   public String getAuthorEmail() {
      return this.authorEmail;
   }

   public String getAuthorName() {
      return this.authorName;
   }

   public String getDate() {
      return this.date;
   }

   public String getSoftware() {
      return this.software;
   }

   public String getTitle() {
      return this.title;
   }

   public void setAuthorEmail(String authorEmail) {
      this.authorEmail = authorEmail;
   }

   public void setAuthorName(String authorName) {
      this.authorName = authorName;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public void setSoftware(String software) {
      this.software = software;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof AnimationMetaData)) {
         return false;
      } else {
         AnimationMetaData other = (AnimationMetaData)obj;
         return ObjectUtilities.equals(this.authorEmail, other.authorEmail)
            && ObjectUtilities.equals(this.authorName, other.authorName)
            && ObjectUtilities.equals(this.date, other.date)
            && ObjectUtilities.equals(this.software, other.software)
            && ObjectUtilities.equals(this.title, other.title);
      }
   }
}
