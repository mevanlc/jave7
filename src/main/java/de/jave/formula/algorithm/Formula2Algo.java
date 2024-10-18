package de.jave.formula.algorithm;

import java.util.List;

public class Formula2Algo {
   private Formula2Algo() {
   }

   public static FormulaCharField matrix(int rows, int cols, List<FormulaCharField> v) {
      while (v.size() < rows * cols) {
         v.add(new FormulaCharField("0"));
      }

      int[] rowHeights = new int[rows];
      int[] rowAscents = new int[rows];
      int[] colWidths = new int[cols];

      for (int y = 0; y < rows; y++) {
         for (int x = 0; x < cols; x++) {
            FormulaCharField f = v.get(y * cols + x);
            int fAscent = f.getAscent();
            int fHeight = f.getHeight();
            if (y > 0 && f.isTopWeak()) {
               fAscent--;
               fHeight--;
            }

            if (f.getWidth() > colWidths[x]) {
               colWidths[x] = f.getWidth();
            }

            if (fAscent > rowAscents[y]) {
               rowAscents[y] = fAscent;
            }

            if (rowAscents[y] - fAscent + fHeight > rowHeights[y]) {
               rowHeights[y] = rowAscents[y] - fAscent + fHeight;
            }
         }
      }

      int height = 0;

      for (int y = 0; y < rows; y++) {
         height += rowHeights[y];
      }

      height += rows - 1;
      int width = 0;

      for (int x = 0; x < cols; x++) {
         width += colWidths[x];
      }

      width += cols - 1;
      FormulaCharField g = new FormulaCharField(width, height, height / 2);
      int yy = 0;

      for (int y = 0; y < rows; y++) {
         int xx = 0;

         for (int x = 0; x < cols; x++) {
            FormulaCharField fx = v.get(y * cols + x);
            fx.pasteInto(g, xx + (colWidths[x] - fx.getWidth()) / 2, yy + rowAscents[y] - fx.getAscent());
            xx += colWidths[x] + 1;
         }

         yy += rowHeights[y] + 1;
      }

      return g;
   }

   public static FormulaCharField binomial(FormulaCharField f1, FormulaCharField f2) {
      int height = f1.getHeight() + f2.getHeight() + 1;
      int width = max(f1.getWidth(), f2.getWidth());
      FormulaCharField g = new FormulaCharField(width, height, height / 2);
      f1.pasteInto(g, (width - f1.getWidth()) / 2, 0);
      f2.pasteInto(g, (width - f2.getWidth()) / 2, f1.getHeight() + 1);
      return roundBrackets(g);
   }

   public static FormulaCharField roundBrackets(FormulaCharField f) {
      int h = f.getHeight();
      if (h == 2) {
         h++;
      }

      if (h == 1) {
         FormulaCharField g = new FormulaCharField(f.getWidth() + 2, 1);
         g.set(0, 0, '(');
         g.set(g.getWidth() - 1, 0, ')');
         f.pasteInto(g, 1, 0);
         return g;
      } else {
         FormulaCharField g = new FormulaCharField(f.getWidth() + 6, h, f.getAscent());
         if (h == 3 && f.getHeight() == 2 && f.getAscent() == 0) {
            f.pasteInto(g, 3, 1);
            g.setAscent(g.getAscent() + 1);
         } else {
            f.pasteInto(g, 3, 0);
         }

         g.set(1, 0, '/');
         g.set(g.getWidth() - 2, 0, '\\');
         g.set(1, g.getHeight() - 1, '\\');
         g.set(g.getWidth() - 2, g.getHeight() - 1, '/');

         for (int y = 1; y < g.getHeight() - 1; y++) {
            g.set(0, y, '|');
            g.set(g.getWidth() - 1, y, '|');
         }

         return g;
      }
   }

   public static FormulaCharField spitzBrackets(FormulaCharField f) {
      int height = f.getHeight();
      if (height == 2) {
         height++;
      }

      int klammerBreite = (height + 1) / 2;
      int schraegStrichAnzahl = height / 2;
      FormulaCharField g = new FormulaCharField(f.getWidth() + 2 * klammerBreite, height, f.getAscent());
      f.pasteInto(g, klammerBreite, 0);
      if (height / 2 * 2 != height) {
         g.set(0, height / 2, '<');
         g.set(g.getWidth() - 1, height / 2, '>');
      }

      for (int i = 0; i < schraegStrichAnzahl; i++) {
         g.set(klammerBreite - i - 1, i, '/');
         g.set(g.getWidth() - klammerBreite + i, i, '\\');
         g.set(klammerBreite - i - 1, height - i - 1, '\\');
         g.set(g.getWidth() - klammerBreite + i, height - i - 1, '/');
      }

      return g;
   }

   public static FormulaCharField eckigBrackets(FormulaCharField f) {
      int h = f.getHeight();
      if (h == 1) {
         FormulaCharField g = new FormulaCharField(f.getWidth() + 2, 1);
         g.set(0, 0, '[');
         g.set(g.getWidth() - 1, 0, ']');
         f.pasteInto(g, 1, 0);
         return g;
      } else {
         int delta = 1;
         if (f.isTopWeak()) {
            delta = 0;
         }

         FormulaCharField g = new FormulaCharField(f.getWidth() + 6, h + delta, f.getAscent() + delta);
         f.pasteInto(g, 3, delta);
         g.set(1, 0, '_');
         g.set(g.getWidth() - 2, 0, '_');
         g.set(1, g.getHeight() - 1, '_');
         g.set(g.getWidth() - 2, g.getHeight() - 1, '_');

         for (int y = 1; y < g.getHeight(); y++) {
            g.set(0, y, '|');
            g.set(g.getWidth() - 1, y, '|');
         }

         return g;
      }
   }

   public static FormulaCharField floor(FormulaCharField f) {
      FormulaCharField g = new FormulaCharField(f.getWidth() + 6, f.getHeight(), f.getAscent());
      f.pasteInto(g, 3, 0);
      g.set(1, g.getHeight() - 1, '_');
      g.set(g.getWidth() - 2, g.getHeight() - 1, '_');

      for (int y = 0; y < g.getHeight(); y++) {
         g.set(0, y, '|');
         g.set(g.getWidth() - 1, y, '|');
      }

      return g;
   }

   public static FormulaCharField ceil(FormulaCharField f) {
      int delta = 1;
      if (f.getHeight() > 2 && f.isTopWeak()) {
         delta = 0;
      }

      FormulaCharField g = new FormulaCharField(f.getWidth() + 6, f.getHeight() + delta, f.getAscent() + delta);
      f.pasteInto(g, 3, delta);
      g.set(1, 0, '_');
      g.set(g.getWidth() - 2, 0, '_');

      for (int y = 1; y < g.getHeight(); y++) {
         g.set(0, y, '|');
         g.set(g.getWidth() - 1, y, '|');
      }

      return g;
   }

   public static FormulaCharField straightBrackets(FormulaCharField f) {
      FormulaCharField g = new FormulaCharField(f.getWidth() + 4, f.getHeight(), f.getAscent());
      f.pasteInto(g, 2, 0);

      for (int y = 0; y < g.getHeight(); y++) {
         g.set(0, y, '|');
         g.set(g.getWidth() - 1, y, '|');
      }

      return g;
   }

   public static FormulaCharField straightDoubleBrackets(FormulaCharField f) {
      FormulaCharField g = new FormulaCharField(f.getWidth() + 6, f.getHeight(), f.getAscent());
      f.pasteInto(g, 3, 0);

      for (int y = 0; y < g.getHeight(); y++) {
         g.set(0, y, '|');
         g.set(1, y, '|');
         g.set(g.getWidth() - 1, y, '|');
         g.set(g.getWidth() - 2, y, '|');
      }

      return g;
   }

   public static FormulaCharField sqrt(FormulaCharField f) {
      return sqrt(f, null);
   }

   public static FormulaCharField sqrt(FormulaCharField f, FormulaCharField base) {
      int d = min(4, f.getHeight() + 1);
      int top = 0;
      int left = 0;
      int height = f.getHeight() + 1;
      int width = f.getWidth() + d + 2;
      int ascent = (height + 1) / 2;
      if (base != null) {
         if (ascent < base.getHeight()) {
            height += base.getHeight() - ascent;
            top = base.getHeight() - ascent;
            ascent = base.getHeight();
         }

         if (base.getWidth() > 2) {
            left = base.getWidth() - 2;
            if (f.getHeight() > 2) {
               left--;
            }

            width += left;
         }
      }

      FormulaCharField g = new FormulaCharField(width, height, ascent);
      f.pasteInto(g, d + 1 + left, 1 + top);

      for (int i = 0; i < f.getWidth() + 2; i++) {
         g.set(d + i + left, top, '_');
      }

      for (int i = 0; i < f.getHeight(); i++) {
         g.set(d - 1 + left, i + 1 + top, '|');
      }

      if (f.getHeight() > 1) {
         g.set(left, ascent - 1, '_');
      }

      if (base != null) {
         int xx = left + 2 - base.getWidth();
         if (f.getHeight() == 1) {
            xx--;
         }

         int yy = ascent - base.getHeight();
         if (base.getWidth() > 1 && f.getHeight() != 2) {
            xx++;
         }

         base.pasteInto(g, xx, yy);
      }

      int xd0 = 1;
      int yd0 = ascent;
      if (f.getHeight() == 1) {
         xd0 = 0;
      }

      while (xd0 < d - 1) {
         g.set(xd0 + left, yd0, '\\');
         xd0++;
         yd0++;
      }

      return g;
   }

   protected static final void drawIntegralSymbol(FormulaCharField g, int x0, int y0, int height) {
      g.set(x0 + 2, y0, '/');
      g.set(x0, y0 + height - 1, '/');

      for (int y = 1; y < height - 1; y++) {
         g.set(x0 + 1, y0 + y, '|');
      }
   }

   public static FormulaCharField integral(FormulaCharField f) {
      int ascent = max(1, f.getAscent());
      int height = max(3, f.getHeight() - f.getAscent() + ascent);
      int width = 4 + f.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      f.pasteInto(g, 4, ascent - f.getAscent());
      drawIntegralSymbol(g, 0, 0, height);
      return g;
   }

   public static FormulaCharField integral(FormulaCharField f1, FormulaCharField f2) {
      int ascent = max(1, f1.getAscent(), f2.getAscent());
      int height = max(3, f1.getHeight() - f1.getAscent() + ascent, f2.getHeight() - f2.getAscent() + ascent);
      int width = 4 + f1.getWidth() + 1 + 1 + f2.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      f1.pasteInto(g, 4, ascent - f1.getAscent());
      f2.pasteInto(g, 4 + f1.getWidth() + 2, ascent - f2.getAscent());
      g.set(4 + f1.getWidth() + 1, ascent, 'd');
      drawIntegralSymbol(g, 0, 0, height);
      return g;
   }

   public static FormulaCharField integral(FormulaCharField f1, FormulaCharField f2, FormulaCharField f3) {
      int d = max(3, f3.getWidth());
      int ascent = max(1, f1.getAscent(), f2.getAscent());
      int height = f3.getHeight() + max(3, f1.getHeight() - f1.getAscent() + ascent, f2.getHeight() - f2.getAscent() + ascent);
      int width = d + 1 + f1.getWidth() + 1 + 1 + f2.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      f1.pasteInto(g, d + 1, ascent - f1.getAscent());
      f2.pasteInto(g, d + 1 + f1.getWidth() + 2, ascent - f2.getAscent());
      g.set(d + 1 + f1.getWidth() + 1, ascent, 'd');
      drawIntegralSymbol(g, (d - 3) / 2, 0, height - f3.getHeight());
      f3.pasteInto(g, (d - f3.getWidth()) / 2, height - f3.getHeight());
      return g;
   }

   public static FormulaCharField integral(FormulaCharField f1, FormulaCharField f2, FormulaCharField f3, FormulaCharField f4) {
      return new FormulaCharField("INTEGRAL(x;y;u;v)");
   }

   public static FormulaCharField sum(FormulaCharField fs, FormulaCharField fstart, FormulaCharField fend) {
      int ascent = max(fend.getHeight() + 2, fs.getAscent());
      int height = 1 + ascent + max(2 + fstart.getHeight(), fs.getHeight() - fs.getAscent());
      int width = fs.getWidth() + max(fstart.getWidth(), fend.getWidth(), 3) + 1;
      int d = width - fs.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      fend.pasteInto(g, (d - fend.getWidth()) / 2, ascent - 3 - fend.getHeight() + 1);
      fstart.pasteInto(g, (d - fstart.getWidth()) / 2, ascent + 3);
      int e = (d - 3) / 2;
      drawSumSymbol(g, e, ascent);
      fs.pasteInto(g, d, ascent - fs.getAscent());
      return g;
   }

   public static FormulaCharField sum(FormulaCharField f, FormulaCharField fover) {
      int ascent = max(2, f.getAscent());
      int height = 1 + ascent + max(2 + fover.getHeight(), f.getHeight() - f.getAscent());
      int width = f.getWidth() + max(fover.getWidth(), 3) + 1;
      int d = width - f.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      fover.pasteInto(g, (d - fover.getWidth()) / 2, ascent + 3);
      int e = (d - 3) / 2;
      drawSumSymbol(g, e, ascent);
      f.pasteInto(g, d, ascent - f.getAscent());
      return g;
   }

   public static FormulaCharField sum(FormulaCharField f) {
      int ascent = max(2, f.getAscent());
      int height = 1 + ascent + max(2, f.getHeight() - f.getAscent());
      int width = f.getWidth() + 4;
      int d = 4;
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      drawSumSymbol(g, 0, ascent);
      f.pasteInto(g, 4, ascent - f.getAscent());
      return g;
   }

   protected static final void drawSumSymbol(FormulaCharField g, int x, int y) {
      g.set(x + 1, y, '>');
      g.set(x, y - 1, '\\');
      g.set(x, y + 1, '/');
      g.insert("---", x, y - 2);
      g.insert("---", x, y + 2);
   }

   protected static final void drawProdSymbol(FormulaCharField g, int x, int y) {
      g.insert("_____", x, y - 2);
      g.set(x + 1, y - 1, '|');
      g.set(x + 1, y, '|');
      g.set(x + 1, y + 1, '|');
      g.set(x + 3, y - 1, '|');
      g.set(x + 3, y, '|');
      g.set(x + 3, y + 1, '|');
   }

   public static FormulaCharField prod(FormulaCharField fs, FormulaCharField fstart, FormulaCharField fend) {
      int ascent = max(fend.getHeight() + 2, fs.getAscent());
      int height = 1 + ascent + max(1 + fstart.getHeight(), fs.getHeight() - fs.getAscent());
      int width = fs.getWidth() + max(fstart.getWidth(), fend.getWidth(), 5) + 1;
      int d = width - fs.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      fend.pasteInto(g, (d - fend.getWidth()) / 2, ascent - 3 - fend.getHeight() + 1);
      fstart.pasteInto(g, (d - fstart.getWidth()) / 2, ascent + 2);
      int e = (d - 5) / 2;
      drawProdSymbol(g, e, ascent);
      fs.pasteInto(g, d, ascent - fs.getAscent());
      return g;
   }

   public static FormulaCharField prod(FormulaCharField f, FormulaCharField fover) {
      int ascent = max(2, f.getAscent());
      int height = 1 + ascent + max(1 + fover.getHeight(), f.getHeight() - f.getAscent() - 1);
      int width = f.getWidth() + max(fover.getWidth(), 5) + 1;
      int d = width - f.getWidth();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      fover.pasteInto(g, (d - fover.getWidth()) / 2, ascent + 2);
      int e = (d - 5) / 2;
      drawProdSymbol(g, e, ascent);
      f.pasteInto(g, d, ascent - f.getAscent());
      return g;
   }

   public static FormulaCharField prod(FormulaCharField f) {
      int ascent = max(2, f.getAscent());
      int height = 1 + ascent + max(1, f.getHeight() - f.getAscent());
      int width = f.getWidth() + 6;
      int d = 6;
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      drawProdSymbol(g, 0, ascent);
      f.pasteInto(g, 6, ascent - f.getAscent());
      return g;
   }

   static FormulaCharField or(FormulaCharField f1, FormulaCharField f2) {
      return seq(f1, f2, "\\/", 1);
   }

   static FormulaCharField and(FormulaCharField f1, FormulaCharField f2) {
      return seq(f1, f2, "/\\", 1);
   }

   public static FormulaCharField seq(FormulaCharField f1, FormulaCharField f2, String symbol, int distance) {
      int ascent = max(f1.getAscent(), f2.getAscent());
      int height = ascent + max(f1.getHeight() - f1.getAscent(), f2.getHeight() - f2.getAscent());
      int width = f1.getWidth() + 2 * distance + symbol.length() + f2.getWidth();
      FormulaCharField ft = new FormulaCharField(width, height);
      ft.setAscent(ascent);
      f1.pasteInto(ft, 0, ascent - f1.getAscent());
      f2.pasteInto(ft, f1.getWidth() + 2 * distance + symbol.length(), ascent - f2.getAscent());
      ft.insert(symbol, f1.getWidth() + distance, ascent);
      return ft;
   }

   public static FormulaCharField seq(FormulaCharField f1, FormulaCharField f2, char symbol, int distance) {
      if (symbol != '|') {
         return seq(f1, f2, String.valueOf(symbol), distance);
      } else {
         FormulaCharField g = seq(f1, f2, " | ", distance);

         for (int y = 0; y < g.getHeight(); y++) {
            g.set(f1.getWidth() + 1, y, '|');
         }

         return g;
      }
   }

   public static FormulaCharField faculty(FormulaCharField f) {
      return append(f, '!');
   }

   public static FormulaCharField append(FormulaCharField f, char ch) {
      FormulaCharField ft = new FormulaCharField(f.getWidth() + 1, f.getHeight());
      f.pasteInto(ft, 0, 0);
      ft.setAscent(f.getAscent());
      ft.set(ft.getWidth() - 1, ft.getAscent(), ch);
      return ft;
   }

   public static FormulaCharField fraction(FormulaCharField f1, FormulaCharField f2) {
      int ascent = f1.getHeight();
      int height = f1.getHeight() + f2.getHeight() + 1;
      int width = max(f1.getWidth(), f2.getWidth());
      if (f1.getHeight() > 2 || f2.getHeight() > 2) {
         width += 2;
      }

      FormulaCharField ft = new FormulaCharField(width, height);
      ft.setAscent(f1.getHeight());
      f1.pasteInto(ft, (width - f1.getWidth()) / 2, 0);
      f2.pasteInto(ft, (width - f2.getWidth()) / 2, f1.getHeight() + 1);

      for (int i = 0; i < width; i++) {
         ft.set(i, f1.getHeight(), '-');
      }

      return ft;
   }

   public static FormulaCharField sub(FormulaCharField f1, FormulaCharField f2) {
      int delta = 0;

      while (delta + 1 < f1.getWidth() && f1.get(f1.getWidth() - delta - 1, f1.getHeight() - 1) == ' ') {
         delta++;
      }

      int height = f1.getHeight() + f2.getHeight();
      int width = f1.getWidth() + f2.getWidth() - delta;
      if (width < f1.getWidth()) {
         width = f1.getWidth();
      }

      int ascent = f1.getAscent();
      FormulaCharField g = new FormulaCharField(width, height, ascent);
      f1.pasteInto(g, 0, 0);
      f2.pasteInto(g, f1.getWidth() - delta, f1.getHeight());
      return g;
   }

   public static FormulaCharField sup(FormulaCharField f1, FormulaCharField f2) {
      int delta = 0;
      if (f1.isTopWeak()) {
         delta = 1;
      }

      int height = f1.getHeight() + f2.getHeight() - delta;
      int width = f1.getWidth() + f2.getWidth();
      int ascent = f2.getHeight() + f1.getAscent() - delta;
      FormulaCharField ft = new FormulaCharField(width, height, ascent);
      f1.pasteInto(ft, 0, f2.getHeight() - delta);
      f2.pasteInto(ft, f1.getWidth(), 0);
      return ft;
   }

   public static FormulaCharField negative(FormulaCharField f) {
      int delta = 0;
      if (f.getHeight() > 2) {
         delta = 1;
      }

      FormulaCharField g = new FormulaCharField(f.getWidth() + 1 + delta, f.getHeight(), f.getAscent());
      f.pasteInto(g, 1 + delta, 0);
      g.set(0, g.getAscent(), '-');
      return g;
   }

   public static FormulaCharField not(FormulaCharField f) {
      int height = f.getHeight();
      int ascent = f.getAscent();
      if (height == 1) {
         height++;
         ascent++;
      } else if (ascent == 0) {
         height++;
         ascent++;
      }

      FormulaCharField g = new FormulaCharField(f.getWidth() + 3, height, ascent);
      f.pasteInto(g, 3, ascent - f.getAscent());
      g.set(0, g.getAscent() - 1, '_');
      g.set(1, g.getAscent(), '|');
      return g;
   }

   public static FormulaCharField function(String name, FormulaCharField f) {
      int offSet = name.length();
      FormulaCharField g = roundBrackets(f);
      FormulaCharField h = new FormulaCharField(g.getWidth() + offSet, g.getHeight(), g.getAscent());
      g.pasteInto(h, offSet, 0);
      h.insert(name, 0, g.getAscent());
      return h;
   }

   public static FormulaCharField vector(FormulaCharField f) {
      int width = max(f.getWidth(), 2);
      FormulaCharField g = new FormulaCharField(width, f.getHeight() + 1, f.getAscent() + 1);
      f.pasteInto(g, 0, 1);
      g.set(width - 1, 0, '>');

      for (int i = 0; i < width - 1; i++) {
         g.set(i, 0, '-');
      }

      return g;
   }

   public static FormulaCharField overline(FormulaCharField f) {
      FormulaCharField g = new FormulaCharField(f.getWidth(), f.getHeight() + 1, f.getAscent() + 1);
      f.pasteInto(g, 0, 1);

      for (int i = 0; i < f.getWidth(); i++) {
         g.set(i, 0, '_');
      }

      return g;
   }

   public static FormulaCharField underline(FormulaCharField f) {
      FormulaCharField g = new FormulaCharField(f.getWidth(), f.getHeight() + 1, f.getAscent());
      f.pasteInto(g, 0, 0);

      for (int i = 0; i < f.getWidth(); i++) {
         g.set(i, f.getHeight(), '-');
      }

      return g;
   }

   public static FormulaCharField lim(FormulaCharField f1, FormulaCharField f2) {
      int ascent = f2.getAscent();
      int height = ascent + 1 + max(f2.getHeight() - f2.getAscent() - 1, f1.getHeight());
      int width = 1 + max(3, f1.getWidth()) + f2.getWidth();
      FormulaCharField ft = new FormulaCharField(width, height);
      ft.setAscent(ascent);
      f1.pasteInto(ft, 0, height - f1.getHeight());
      f2.pasteInto(ft, width - f2.getWidth(), 0);
      ft.insert("lim", (width - f2.getWidth() - 4) / 2, ascent);
      return ft;
   }

   public static FormulaCharField log(FormulaCharField f1, FormulaCharField f2) {
      int ascent = f2.getAscent();
      int height = ascent + 1 + max(f1.getHeight(), f2.getHeight() - f2.getAscent() - 1);
      int width = 3 + f1.getWidth() + f2.getWidth();
      FormulaCharField ft = new FormulaCharField(width, height);
      ft.setAscent(ascent);
      f1.pasteInto(ft, 3, ascent + 1);
      f2.pasteInto(ft, 3 + f1.getWidth(), 0);
      ft.insert("log", 0, ascent);
      return ft;
   }

   static FormulaCharField PI() {
      FormulaCharField f = new FormulaCharField(2, 2);
      f.insert("__", 0, 0);
      f.insert("||", 0, 1);
      f.setAscent(1);
      return f;
   }

   static FormulaCharField INFINITY() {
      return new FormulaCharField("oo");
   }

   static FormulaCharField FORALL() {
      FormulaCharField f = new FormulaCharField(4, 2);
      f.insert("\\__/", 0, 0);
      f.insert("\\/", 1, 1);
      f.setAscent(1);
      return f;
   }

   static FormulaCharField EXISTS() {
      FormulaCharField f = new FormulaCharField(2, 3);
      f.insert("_ ", 0, 0);
      f.insert("_|", 0, 1);
      f.insert("_|", 0, 2);
      f.setAscent(2);
      return f;
   }

   public static final int max(int a, int b) {
      return a > b ? a : b;
   }

   public static final int max(int a, int b, int c) {
      return a > b ? (a > c ? a : c) : (b > c ? b : c);
   }

   public static final int min(int a, int b) {
      return a < b ? a : b;
   }
}
