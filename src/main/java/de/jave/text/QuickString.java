package de.jave.text;

public class QuickString {
   private char[] chars;
   private int offset;
   private int length;

   public QuickString() {
      this.chars = new char[200];
      this.length = 0;
      this.offset = 0;
   }

   public QuickString(int maxLength) {
      this.chars = new char[maxLength];
      this.length = 0;
      this.offset = 0;
   }

   public QuickString(char c) {
      this.chars = new char[20];
      this.chars[0] = c;
      this.offset = 0;
      this.length = 1;
   }

   public QuickString(String s) {
      this.length = s.length();
      this.chars = new char[this.length * 2];
      this.offset = 0;
      s.getChars(0, s.length(), this.chars, this.offset);
   }

   public QuickString(String s, int maxLength) {
      if (s.length() > maxLength) {
         maxLength = s.length();
      }

      this.length = s.length();
      this.chars = new char[maxLength];
      this.offset = 0;
      s.getChars(0, s.length(), this.chars, this.offset);
   }

   public QuickString(char[] ch) {
      this.offset = 0;
      this.length = ch.length;
      this.chars = new char[this.length];
      System.arraycopy(ch, 0, this.chars, 0, this.length);
   }

   public QuickString(QuickString other) {
      this.offset = 0;
      this.length = other.length;
      this.chars = new char[this.length * 2];
      System.arraycopy(other.chars, other.offset, this.chars, 0, this.length);
   }

   public QuickString add(QuickString other) {
      QuickString result = new QuickString((this.length + other.length) * 2);
      result.offset = 0;
      result.length = this.length + other.length;
      System.arraycopy(this.chars, this.offset, result.chars, 0, this.length);
      System.arraycopy(other.chars, other.offset, result.chars, this.length, other.length);
      return result;
   }

   public QuickString add(char ch, QuickString other) {
      QuickString result = new QuickString((this.length + other.length + 1) * 2);
      result.offset = 0;
      result.length = this.length + other.length + 1;
      System.arraycopy(this.chars, this.offset, result.chars, 0, this.length);
      result.chars[this.length] = ch;
      System.arraycopy(other.chars, other.offset, result.chars, this.length + 1, other.length);
      return result;
   }

   @Override
   public int hashCode() {
      int hash = 0;
      int len = this.length < 10 ? this.length : 10;

      for (int i = 0; i < len; i++) {
         hash = 31 * hash + this.chars[this.offset + i];
      }

      return hash;
   }

   public char firstChar() {
      if (this.length == 0) {
         throw new QuickStringIndexOutOfBoundsException("unable to get character from empty string");
      } else {
         return this.chars[this.offset];
      }
   }

   public char lastChar() {
      if (this.length == 0) {
         throw new QuickStringIndexOutOfBoundsException("unable to get character from empty string");
      } else {
         return this.chars[this.offset + this.length - 1];
      }
   }

   public char charAt(int index) {
      if (index >= 0 && index <= this.length - 1) {
         return this.chars[index + this.offset];
      } else {
         throw new QuickStringIndexOutOfBoundsException(index);
      }
   }

   public void setCharAt(int index, char ch) {
      if (index >= 0 && index <= this.length - 1) {
         this.chars[this.offset + index] = ch;
      } else {
         throw new QuickStringIndexOutOfBoundsException(index);
      }
   }

   public void replace(char oldCh, char newCh) {
      for (int i = 0; i < this.length; i++) {
         if (this.chars[this.offset + i] == oldCh) {
            this.chars[this.offset + i] = newCh;
         }
      }
   }

   public void trimRight() {
      while (this.length > 0 && this.chars[this.offset + this.length - 1] <= ' ') {
         this.length--;
      }
   }

   public void trimLeft() {
      while (this.length > 0 && this.chars[this.offset] <= ' ') {
         this.offset++;
         this.length--;
      }
   }

   public void trim() {
      this.trimLeft();
      this.trimRight();
   }

   public boolean startsWith(QuickString other) {
      if (other.length > this.length) {
         return false;
      } else {
         for (int i = 0; i < other.length; i++) {
            if (this.chars[this.offset + i] != other.chars[other.offset + i]) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean startsWith(String s) {
      if (s.length() > this.length) {
         return false;
      } else {
         for (int i = 0; i < s.length(); i++) {
            if (this.chars[this.offset + i] != s.charAt(i)) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean endsWith(QuickString other) {
      if (other.length > this.length) {
         return false;
      } else {
         for (int i = 0; i < other.length; i++) {
            if (this.chars[this.offset + this.length - i] != other.chars[other.offset + other.length - i]) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean endsWith(String s) {
      int l = s.length();
      if (l > this.length) {
         return false;
      } else {
         for (int i = 0; i < l; i++) {
            if (this.chars[this.offset + this.length - i] != s.charAt(l - i)) {
               return false;
            }
         }

         return true;
      }
   }

   public int length() {
      return this.length;
   }

   public void append(QuickString fs) {
      if (this.chars.length < this.offset + this.length + fs.length) {
         char[] ch = new char[this.chars.length + fs.chars.length];
         System.arraycopy(this.chars, this.offset, ch, this.offset, this.length);
         this.chars = ch;
      }

      System.arraycopy(fs.chars, fs.offset, this.chars, this.offset + this.length, fs.length);
      this.length = this.length + fs.length;
   }

   public void append(String s) {
      if (this.chars.length < this.offset + this.length + s.length()) {
         char[] ch = new char[this.chars.length + s.length()];
         System.arraycopy(this.chars, this.offset, ch, this.offset, this.length);
         this.chars = ch;
      }

      s.getChars(0, s.length(), this.chars, this.offset + this.length);
      this.length = this.length + s.length();
   }

   public void append(char ch) {
      if (this.chars.length < this.offset + this.length + 1) {
         char[] ch1 = new char[this.chars.length + 10];
         System.arraycopy(this.chars, this.offset, ch1, this.offset, this.length);
         this.chars = ch1;
      }

      this.chars[this.offset + this.length] = ch;
      this.length++;
   }

   public void prepend(QuickString fs) {
      if (this.offset < fs.length) {
         char[] ch = new char[this.chars.length + fs.chars.length];
         System.arraycopy(this.chars, this.offset, ch, this.offset + fs.length + fs.offset, this.length);
         this.chars = ch;
         this.offset = this.offset + fs.length + fs.offset;
      }

      System.arraycopy(fs.chars, fs.offset, this.chars, this.offset - fs.length, fs.length);
      this.length = this.length + fs.length;
      this.offset = this.offset - fs.length;
   }

   public void cropLastChar() {
      if (this.length == 0) {
         throw new QuickStringIndexOutOfBoundsException("unable to remove character from empty string");
      } else {
         this.length--;
      }
   }

   public void cropFirstChar() {
      if (this.length == 0) {
         throw new QuickStringIndexOutOfBoundsException("unable to remove character from empty string");
      } else {
         this.offset++;
         this.length--;
      }
   }

   public void crop(int start) {
      if (start <= this.length && start >= 0) {
         this.offset += start;
         this.length -= start;
      } else {
         throw new QuickStringIndexOutOfBoundsException(start);
      }
   }

   public void crop(int start, int end) {
      if (start > this.length || start < 0) {
         throw new QuickStringIndexOutOfBoundsException(start);
      } else if (end <= this.length && end >= start) {
         this.offset += start;
         this.length = end - start;
      } else {
         throw new QuickStringIndexOutOfBoundsException(end);
      }
   }

   public QuickString substring(int start, int end) {
      if (start > this.length || start < 0) {
         throw new QuickStringIndexOutOfBoundsException(start);
      } else if (end <= this.length && end >= start) {
         QuickString result = new QuickString(this);
         result.crop(start, end);
         return result;
      } else {
         throw new QuickStringIndexOutOfBoundsException(end);
      }
   }

   public QuickString substring(int start) {
      if (start <= this.length && start >= 0) {
         QuickString result = new QuickString(this);
         result.crop(start);
         return result;
      } else {
         throw new QuickStringIndexOutOfBoundsException(start);
      }
   }

   @Override
   public String toString() {
      return new String(this.chars, this.offset, this.length);
   }

   public void print() {
      System.out.print(this.toString());
   }

   public void println() {
      System.out.println(this.toString());
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof QuickString)) {
         return false;
      } else {
         QuickString other = (QuickString)obj;
         if (other.length != this.length) {
            return false;
         } else {
            for (int i = 0; i < this.length; i++) {
               if (other.chars[other.offset + i] != this.chars[this.offset + i]) {
                  return false;
               }
            }

            return true;
         }
      }
   }
}
