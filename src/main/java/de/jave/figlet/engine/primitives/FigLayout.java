package de.jave.figlet.engine.primitives;

import de.jave.figlet.engine.layout.PrintDirection;
import de.jave.lib.IPublicCloneable;
import de.jave.text.QuickString;
import net.disy.commons.core.util.Ensure;

public class FigLayout implements IPublicCloneable {
   private static final String HIERARCHICAL_CHARACTERS = "|/\\[]{}()<>";
   private HorizontalLayoutMode horizontalMode;
   private VerticalLayoutMode verticalMode;
   private HorizontalSmushingRules horizontalRules;
   private VerticalSmushingRules verticalRules;
   private int horizontalSupersmushingDepth = 2;
   private int verticalSupersmushingDepth = 2;
   private PrintDirection printDirection = PrintDirection.LEFT_TO_RIGHT;

   public PrintDirection getPrintDirection() {
      return this.printDirection;
   }

   public FigLayout(
      HorizontalLayoutMode horizontalLayoutMode,
      HorizontalSmushingRules horizontalRules,
      VerticalLayoutMode verticalLayoutMode,
      VerticalSmushingRules verticalRules
   ) {
      this.horizontalMode = horizontalLayoutMode;
      this.verticalMode = verticalLayoutMode;
      this.verticalRules = verticalRules;
      this.horizontalRules = horizontalRules;
   }

   public FigLayout() {
      this(HorizontalLayoutMode.KERNING, new HorizontalSmushingRules(), VerticalLayoutMode.VERTICAL_FITTING, new VerticalSmushingRules());
   }

   public FigLayout intersect(FigLayout other) {
      FigLayout layout = new FigLayout(
         this.getHorizontalLayoutMode().intersect(other.getHorizontalLayoutMode()),
         this.getHorizontalSmushingRules().intersect(other.getHorizontalSmushingRules()),
         this.getVerticalLayoutMode().intersect(other.getVerticalLayoutMode()),
         this.getVerticalSmushingRules().intersect(other.getVerticalSmushingRules())
      );
      layout.setHorizontalSupersmushingDepth(Math.min(this.getHorizontalSupersmushingDepth(), other.getHorizontalSupersmushingDepth()));
      layout.setVerticalSupersmushingDepth(Math.min(this.getVerticalSupersmushingDepth(), other.getVerticalSupersmushingDepth()));
      return layout;
   }

   public int getMaximalVerticalSmushingDepth(QuickString c, QuickString d) {
      if (this.verticalMode == VerticalLayoutMode.FULL_HEIGHT) {
         return 0;
      } else {
         int count = 0;

         while (c.length() > 0 && c.lastChar() == ' ') {
            count++;
            c = c.substring(0, c.length() - 1);
         }

         while (d.length() > 0 && d.firstChar() == ' ') {
            count++;
            d = d.substring(1);
         }

         if (c.length() == 0 || d.length() == 0) {
            return count;
         } else if (this.verticalMode == VerticalLayoutMode.VERTICAL_FITTING) {
            return count;
         } else if (this.verticalMode == VerticalLayoutMode.SPACED_VERTICAL_FITTING) {
            return count - 1;
         } else if (this.verticalMode == VerticalLayoutMode.SUPERSMUSHING) {
            for (int i = 0; i < this.verticalSupersmushingDepth && c.length() > i && c.charAt(c.length() - 1 - i) != 127; i++) {
               count++;
            }

            return count;
         } else if (this.verticalMode == VerticalLayoutMode.REVERSE_SUPERSMUSHING) {
            for (int i = 0; i < this.verticalSupersmushingDepth && d.length() > i && d.charAt(i) != 127; i++) {
               count++;
            }

            return count;
         } else {
            char chA = c.lastChar();
            char chB = d.firstChar();
            if (this.verticalRules.isNoRulesSpecified()) {
               return count + 1;
            } else if (this.verticalRules.isEqualCharacter() && chA == chB && chA != 127) {
               return count + 1;
            } else {
               if (this.verticalRules.isUnderscore()) {
                  if (chA == '_' && "|/\\[]{}()<>".indexOf(chB) != -1) {
                     return count + 1;
                  }

                  if (chB == '_' && "|/\\[]{}()<>".indexOf(chA) != -1) {
                     return count + 1;
                  }
               }

               if (this.verticalRules.isHierarchy()) {
                  int cA = this.getHierarchyClass(chA);
                  int cB = this.getHierarchyClass(chB);
                  if (cA != cB && cA != -1 && cB != -1) {
                     return count + 1;
                  }
               }

               if (this.verticalRules.isHorizontalLine()) {
                  if (chA == '_' && chB == '-') {
                     return count + 1;
                  }

                  if (chA == '-' && chB == '_') {
                     return count + 1;
                  }
               }

               if (this.verticalRules.isVerticalLine() && chA == '|' && chB != 127) {
                  count++;

                  while (c.length() > 0 && c.lastChar() == '|') {
                     count++;
                     c = c.substring(0, c.length() - 1);
                  }

                  return count;
               } else {
                  return count;
               }
            }
         }
      }
   }

   public int getMaximalHorizontalSmushingDepth(QuickString a, QuickString b) {
      if (this.horizontalMode != HorizontalLayoutMode.FULL_WIDTH && this.horizontalMode != HorizontalLayoutMode.FIXED_WIDTH) {
         QuickString c = new QuickString(a);
         QuickString d = new QuickString(b);

         int count;
         for (count = 0; c.length() > 0 && c.lastChar() == ' '; c = c.substring(0, c.length() - 1)) {
            count++;
         }

         while (d.length() > 0 && d.firstChar() == ' ') {
            count++;
            d = d.substring(1);
         }

         if (this.horizontalMode == HorizontalLayoutMode.KERNING) {
            return count;
         } else if (this.horizontalMode == HorizontalLayoutMode.SPACED_KERNING) {
            return count - 1;
         } else if (c.length() == 0 || d.length() == 0) {
            return count;
         } else if (this.horizontalMode == HorizontalLayoutMode.SUPERSMUSHING) {
            for (int i = 0; i < this.horizontalSupersmushingDepth && c.length() > i && c.charAt(c.length() - 1 - i) != 127; i++) {
               count++;
            }

            return count;
         } else if (this.horizontalMode == HorizontalLayoutMode.REVERSE_SUPERSMUSHING) {
            for (int i = 0; i < this.horizontalSupersmushingDepth && d.length() > i && d.charAt(i) != 127; i++) {
               count++;
            }

            return count;
         } else if (this.horizontalRules.isNoRulesSpecified()) {
            return count + 1;
         } else {
            char chA = c.lastChar();
            char chB = d.firstChar();
            c = c.substring(0, c.length() - 1);
            d = d.substring(1);
            if (this.horizontalRules.isEqualCharacter() && chA == chB && chA != 127) {
               return count + 1;
            } else {
               if (this.horizontalRules.isUnderscore()) {
                  if (chA == '_' && "|/\\[]{}()<>".indexOf(chB) != -1) {
                     return count + 1;
                  }

                  if (chB == '_' && "|/\\[]{}()<>".indexOf(chA) != -1) {
                     return count + 1;
                  }
               }

               if (this.horizontalRules.isHierarchy()) {
                  int cA = this.getHierarchyClass(chA);
                  int cB = this.getHierarchyClass(chB);
                  if (cA != cB && cA != -1 && cB != -1) {
                     return count + 1;
                  }
               }

               if (this.horizontalRules.isOppositePair()) {
                  if (chA == ']' && chB == '[') {
                     return count + 1;
                  }

                  if (chA == '[' && chB == ']') {
                     return count + 1;
                  }

                  if (chA == '(' && chB == ')') {
                     return count + 1;
                  }

                  if (chA == ')' && chB == '(') {
                     return count + 1;
                  }

                  if (chA == '{' && chB == '}') {
                     return count + 1;
                  }

                  if (chA == '}' && chB == '{') {
                     return count + 1;
                  }
               }

               if (this.horizontalRules.isBigX()) {
                  if (chA == '/' && chB == '\\') {
                     return count + 1;
                  }

                  if (chA == '\\' && chB == '/') {
                     return count + 1;
                  }

                  if (chA == '>' && chB == '<') {
                     return count + 1;
                  }
               }

               return this.horizontalRules.isHardblank() && chA == 127 && chB == 127 && c.length() > 1 && d.length() > 1 ? count + 1 : count;
            }
         }
      } else {
         return 0;
      }
   }

   private int getHierarchyClass(char ch) {
      int i = "|/\\[]{}()<>".indexOf(ch);
      return i == -1 ? i : i / 2;
   }

   public QuickString smushHorizontal(QuickString a, QuickString b, int depth) {
      QuickString c = new QuickString(a);
      QuickString d = new QuickString(b);
      if (depth < 0) {
         for (int i = 0; i < -depth; i++) {
            c.append(' ');
         }

         return c.add(d);
      } else if (c.length() + d.length() == depth) {
         return c.add(d);
      } else {
         while (depth > 0 && c.length() > 0 && c.lastChar() == ' ') {
            depth--;
            c.cropLastChar();
         }

         while (depth > 0 && d.length() > 0 && d.firstChar() == ' ') {
            depth--;
            d.crop(1);
         }

         if (depth == 0) {
            return c.add(d);
         } else if (this.horizontalMode == HorizontalLayoutMode.SUPERSMUSHING) {
            while (depth > 0 && d.length() > 0 && (d.firstChar() == ' ' || d.firstChar() == 127)) {
               depth--;
               d.cropFirstChar();
            }

            c.crop(0, c.length() - depth);
            return c.add(d);
         } else if (this.horizontalMode == HorizontalLayoutMode.REVERSE_SUPERSMUSHING) {
            while (depth > 0 && c.length() > 0 && (c.lastChar() == ' ' || c.lastChar() == 127)) {
               depth--;
               c.cropLastChar();
            }

            d.crop(depth);
            return c.add(d);
         } else if (depth > 1) {
            throw new RuntimeException();
         } else {
            char chA = c.lastChar();
            char chB = d.firstChar();
            c.cropLastChar();
            d.crop(1);
            if (this.horizontalRules.isNoRulesSpecified()) {
               return c.add(chB, d);
            } else if (this.horizontalRules.isEqualCharacter() && chA == chB && chA != 127) {
               return c.add(chA, d);
            } else {
               if (this.horizontalRules.isUnderscore()) {
                  if (chA == '_' && "|/\\[]{}()<>".indexOf(chB) != -1) {
                     return c.add(chB, d);
                  }

                  if (chB == '_' && "|/\\[]{}()<>".indexOf(chA) != -1) {
                     return c.add(chA, d);
                  }
               }

               if (this.horizontalRules.isHierarchy()) {
                  int cA = this.getHierarchyClass(chA);
                  int cB = this.getHierarchyClass(chB);
                  if (cA != -1 && cB != -1 && cA != cB) {
                     if (cA > cB) {
                        return c.add(chA, d);
                     }

                     return c.add(chB, d);
                  }
               }

               if (!this.horizontalRules.isOppositePair()
                  || (chA != ']' || chB != '[')
                     && (chA != '[' || chB != ']')
                     && (chA != '(' || chB != ')')
                     && (chA != ')' || chB != '(')
                     && (chA != '{' || chB != '}')
                     && (chA != '}' || chB != '{')) {
                  if (this.horizontalRules.isBigX()) {
                     if (chA == '/' && chB == '\\') {
                        return c.add('|', d);
                     }

                     if (chA == '\\' && chB == '/') {
                        return c.add('Y', d);
                     }

                     if (chA == '>' && chB == '<') {
                        return c.add('X', d);
                     }
                  }

                  if (this.horizontalRules.isHardblank() && chA == 127 && chB == 127 && c.length() > 1 && d.length() > 1) {
                     return c.add('\u007f', d);
                  } else {
                     throw new RuntimeException();
                  }
               } else {
                  return c.add('|', d);
               }
            }
         }
      }
   }

   public QuickString smushVertical(QuickString a, QuickString b, int depth) {
      if (a.length() + b.length() == depth) {
         return a.add(b);
      } else {
         QuickString c = new QuickString(a);
         QuickString d = new QuickString(b);

         while (depth > 0 && c.length() > 0 && c.lastChar() == ' ') {
            depth--;
            c.cropLastChar();
         }

         while (depth > 0 && d.length() > 0 && d.firstChar() == ' ') {
            depth--;
            d.crop(1);
         }

         if (depth == 0) {
            return c.add(d);
         } else if (this.verticalMode == VerticalLayoutMode.SUPERSMUSHING) {
            c.crop(0, c.length() - depth);
            return c.add(d);
         } else if (this.verticalMode == VerticalLayoutMode.REVERSE_SUPERSMUSHING) {
            d.crop(depth);
            return c.add(d);
         } else if (depth > 1) {
            throw new RuntimeException();
         } else {
            char chA = c.lastChar();
            char chB = d.firstChar();
            c.cropLastChar();
            d.crop(1);
            if (this.verticalRules.isNoRulesSpecified()) {
               return c.add(chB, d);
            } else if (this.verticalRules.isEqualCharacter() && chA == chB && chA != 127) {
               return c.add(chA, d);
            } else {
               if (this.verticalRules.isUnderscore()) {
                  if (chA == '_' && "|/\\[]{}()<>".indexOf(chB) != -1) {
                     return c.add(chB, d);
                  }

                  if (chB == '_' && "|/\\[]{}()<>".indexOf(chA) != -1) {
                     return c.add(chA, d);
                  }
               }

               if (this.verticalRules.isHierarchy()) {
                  int cA = this.getHierarchyClass(chA);
                  int cB = this.getHierarchyClass(chB);
                  if (cA != -1 && cB != -1 && cA != cB) {
                     if (cA > cB) {
                        return c.add(chA, d);
                     }

                     return c.add(chB, d);
                  }
               }

               if (this.verticalRules.isHorizontalLine()) {
                  if (chA == '_' && chB == '-') {
                     return c.add('=', d);
                  }

                  if (chA == '-' && chB == '_') {
                     return c.add('=', d);
                  }
               }

               if (this.verticalRules.isVerticalLine() && chA == '|' && chB != 127) {
                  depth--;

                  while (c.length() > 0 && c.lastChar() == '|') {
                     depth--;
                     c.cropLastChar();
                  }

                  return c.add(d);
               } else {
                  throw new RuntimeException();
               }
            }
         }
      }
   }

   @Override
   public String toString() {
      return "FigLayout{horizontalMode="
         + this.getHorizontalLayoutMode()
         + "; horizontalSmushingRules="
         + this.getHorizontalSmushingRules()
         + "; supersmushingDepth="
         + this.getHorizontalSupersmushingDepth()
         + "; verticalMode="
         + this.getVerticalLayoutMode()
         + "; verticalSmushingRules="
         + this.getHorizontalSmushingRules()
         + "}";
   }

   public HorizontalLayoutMode getHorizontalLayoutMode() {
      return this.horizontalMode;
   }

   public VerticalLayoutMode getVerticalLayoutMode() {
      return this.verticalMode;
   }

   public VerticalSmushingRules getVerticalSmushingRules() {
      return this.verticalRules;
   }

   public HorizontalSmushingRules getHorizontalSmushingRules() {
      return this.horizontalRules;
   }

   public void setHorizontalMode(HorizontalLayoutMode mode) {
      this.horizontalMode = mode;
   }

   public void setVerticalMode(VerticalLayoutMode mode) {
      this.verticalMode = mode;
   }

   public int getHorizontalSupersmushingDepth() {
      return this.horizontalSupersmushingDepth;
   }

   public void setHorizontalSupersmushingDepth(int supersmushingDepth) {
      this.horizontalSupersmushingDepth = supersmushingDepth;
   }

   @Override
   public Object clone() {
      FigLayout clone = new FigLayout(
         this.getHorizontalLayoutMode(),
         (HorizontalSmushingRules)this.getHorizontalSmushingRules().clone(),
         this.getVerticalLayoutMode(),
         (VerticalSmushingRules)this.getVerticalSmushingRules().clone()
      );
      clone.setHorizontalSupersmushingDepth(this.getHorizontalSupersmushingDepth());
      return clone;
   }

   public void setHorizontalSmushingRules(HorizontalSmushingRules rules) {
      this.horizontalRules = rules;
   }

   public void setVerticalSmushingRules(VerticalSmushingRules rules) {
      this.verticalRules = rules;
   }

   public int getVerticalSupersmushingDepth() {
      return this.verticalSupersmushingDepth;
   }

   public void setVerticalSupersmushingDepth(int supersmushingDepth) {
      this.verticalSupersmushingDepth = supersmushingDepth;
   }

   public void setPrintDirection(PrintDirection printDirection) {
      Ensure.ensureArgumentNotNull(printDirection);
      this.printDirection = printDirection;
   }
}
