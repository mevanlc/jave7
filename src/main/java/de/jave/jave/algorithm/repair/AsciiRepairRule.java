package de.jave.jave.algorithm.repair;

public class AsciiRepairRule {
   private final int value;
   private final char[] chLine1;
   private final char[] chLine2;

   public AsciiRepairRule(int value, char[] chLine1, char[] chLine2) {
      this.value = value;
      this.chLine1 = chLine1;
      this.chLine2 = chLine2;
   }

   public static AsciiRepairRule getFrom(String l1, String l2, String l3) {
      try {
         int value = Integer.parseInt(l1.trim());

          StringBuilder l2Builder = new StringBuilder(l2);
          while (l2Builder.length() < l3.length()) {
            l2Builder.append(" ");
         }
          l2 = l2Builder.toString();

          StringBuilder l3Builder = new StringBuilder(l3);
          while (l3Builder.length() < l2.length()) {
            l3Builder.append(" ");
         }
          l3 = l3Builder.toString();

          return new AsciiRepairRule(value, l2.toCharArray(), l3.toCharArray());
      } catch (Exception var4) {
         System.err.println(var4);
         return null;
      }
   }

   public int rate(char[] a, char[] b) {
      int result = 0;

      for (int x = 0; x < a.length - this.chLine1.length + 1; x++) {
         boolean applies = true;

         for (int i = 0; i < this.chLine1.length && applies; i++) {
            if (this.chLine1[i] != '?' && this.chLine1[i] != a[x + i] && (this.chLine1[i] != '!' || a[x + i] == ' ')) {
               applies = false;
            }
         }

         for (int ix = 0; ix < this.chLine2.length && applies; ix++) {
            if (this.chLine2[ix] != '?' && this.chLine2[ix] != b[x + ix] && (this.chLine2[ix] != '!' || b[x + ix] == ' ')) {
               applies = false;
            }
         }

         if (applies) {
            result += this.value;
         }
      }

      return result;
   }
}
