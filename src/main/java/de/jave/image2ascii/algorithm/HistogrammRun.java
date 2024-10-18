package de.jave.image2ascii.algorithm;

public class HistogrammRun {
   private final int[] histogramm;
   private int size;
   private final int start;
   private int end;

   public HistogrammRun(int[] histogramm, int start, int end) {
      this.histogramm = histogramm;
      this.start = start;
      this.end = end;

      for (int i = start; i <= end; i++) {
         this.size = this.size + histogramm[i];
      }
   }

   public boolean isSplitable() {
      return this.end > this.start + 1;
   }

   public HistogrammRun split() {
      int index = this.start;

      for (int sum = 0; sum < this.size / 2 && index < this.end - 1; index++) {
         sum += this.histogramm[index];
      }

      HistogrammRun h = new HistogrammRun(this.histogramm, index, this.end);
      this.end = index - 1;
      this.size = 0;

      for (int i = this.start; i <= this.end; i++) {
         this.size = this.size + this.histogramm[i];
      }

      return h;
   }

   public int getSize() {
      return this.size;
   }

   @Override
   public String toString() {
      return "[" + this.start + ".." + this.end + ";" + this.size + "]";
   }

   public int getStart() {
      return this.start;
   }

   public int getEnd() {
      return this.end;
   }
}
