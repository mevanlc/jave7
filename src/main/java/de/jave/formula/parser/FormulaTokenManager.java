package de.jave.formula.parser;

import java.io.IOException;
import java.io.PrintStream;

public class FormulaTokenManager implements FormulaConstants {
   public PrintStream debugStream = System.out;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final long[] jjbitVec1 = new long[]{0L, 0L, 0L, 1170935974255919120L};
   static final int[] jjnextStates = new int[]{
      48, 49, 41, 52, 53, 56, 57, 104, 105, 45, 43, 46, 45, 37, 38, 41, 48, 49, 41, 58, 59, 41, 92, 102, 68, 74, 79, 84, 32, 33, 39, 40, 50, 51, 54, 55, 60, 61
   };
   public static final String[] jjstrLiteralImages = new String[]{
      "",
      null,
      null,
      "\n",
      "+",
      "-",
      "..",
      "*",
      "/",
      "_",
      "^",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      "AND",
      "OR",
      "NOT",
      "EXISTS",
      null,
      null,
      "sum",
      null,
      null,
      null,
      null,
      null,
      "matrix",
      "binomial",
      "floor",
      "ceil",
      "overline",
      "underline",
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      ",",
      "|",
      ";",
      "=",
      "<",
      ">",
      "!",
      "(",
      ")",
      "[",
      "]",
      "||",
      "{",
      "}"
   };
   public static final String[] lexStateNames = new String[]{"DEFAULT"};
   static final long[] jjtoToken = new long[]{4611415538566434809L};
   static final long[] jjtoSkip = new long[]{6L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds = new int[106];
   private final int[] jjstateSet = new int[212];
   protected char curChar;
   int curLexState = 0;
   int defaultLexState = 0;
   int jjnewStateCnt;
   int jjround;
   int jjmatchedPos;
   int jjmatchedKind;

   public void setDebugStream(PrintStream ds) {
      this.debugStream = ds;
   }

   private final int jjStopStringLiteralDfa_0(int pos, long active0) {
      switch (pos) {
         case 0:
            if ((active0 & 270598668288L) != 0L) {
               this.jjmatchedKind = 40;
               return 34;
            } else if ((active0 & 2251799813685280L) != 0L) {
               return 0;
            } else if ((active0 & 18014398509481984L) != 0L) {
               return 2;
            } else if ((active0 & 67108864L) != 0L) {
               this.jjmatchedKind = 40;
               return 8;
            } else if ((active0 & 64L) != 0L) {
               return 37;
            } else if ((active0 & 4503599627370496L) != 0L) {
               return 106;
            } else {
               if ((active0 & 9007199254740992L) != 0L) {
                  return 87;
               }

               return -1;
            }
         case 1:
            if ((active0 & 2097152L) != 0L) {
               return 34;
            } else {
               if ((active0 & 270663680000L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 1;
                  return 34;
               }

               return -1;
            }
         case 2:
            if ((active0 & 72351744L) != 0L) {
               return 34;
            } else {
               if ((active0 & 270591328256L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 2;
                  return 34;
               }

               return -1;
            }
         case 3:
            if ((active0 & 34359738368L) != 0L) {
               return 34;
            } else {
               if ((active0 & 236231589888L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 3;
                  return 34;
               }

               return -1;
            }
         case 4:
            if ((active0 & 17179869184L) != 0L) {
               return 34;
            } else {
               if ((active0 & 219051720704L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 4;
                  return 34;
               }

               return -1;
            }
         case 5:
            if ((active0 & 4303355904L) != 0L) {
               return 34;
            } else {
               if ((active0 & 214748364800L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 5;
                  return 34;
               }

               return -1;
            }
         case 6:
            if ((active0 & 214748364800L) != 0L) {
               this.jjmatchedKind = 40;
               this.jjmatchedPos = 6;
               return 34;
            }

            return -1;
         case 7:
            if ((active0 & 77309411328L) != 0L) {
               return 34;
            } else {
               if ((active0 & 137438953472L) != 0L) {
                  this.jjmatchedKind = 40;
                  this.jjmatchedPos = 7;
                  return 34;
               }

               return -1;
            }
         default:
            return -1;
      }
   }

   private final int jjStartNfa_0(int pos, long active0) {
      return this.jjMoveNfa_0(this.jjStopStringLiteralDfa_0(pos, active0), pos + 1);
   }

   private int jjStopAtPos(int pos, int kind) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;
      return pos + 1;
   }

   private int jjMoveStringLiteralDfa0_0() {
      switch (this.curChar) {
         case '\n':
            return this.jjStopAtPos(0, 3);
         case '\u000b':
         case '\f':
         case '\r':
         case '\u000e':
         case '\u000f':
         case '\u0010':
         case '\u0011':
         case '\u0012':
         case '\u0013':
         case '\u0014':
         case '\u0015':
         case '\u0016':
         case '\u0017':
         case '\u0018':
         case '\u0019':
         case '\u001a':
         case '\u001b':
         case '\u001c':
         case '\u001d':
         case '\u001e':
         case '\u001f':
         case ' ':
         case '"':
         case '#':
         case '$':
         case '%':
         case '&':
         case '\'':
         case '0':
         case '1':
         case '2':
         case '3':
         case '4':
         case '5':
         case '6':
         case '7':
         case '8':
         case '9':
         case ':':
         case '?':
         case '@':
         case 'B':
         case 'C':
         case 'D':
         case 'F':
         case 'G':
         case 'H':
         case 'I':
         case 'J':
         case 'K':
         case 'L':
         case 'M':
         case 'P':
         case 'Q':
         case 'R':
         case 'S':
         case 'T':
         case 'U':
         case 'V':
         case 'W':
         case 'X':
         case 'Y':
         case 'Z':
         case '\\':
         case '`':
         case 'a':
         case 'd':
         case 'e':
         case 'g':
         case 'h':
         case 'i':
         case 'j':
         case 'k':
         case 'l':
         case 'n':
         case 'p':
         case 'q':
         case 'r':
         case 't':
         case 'v':
         case 'w':
         case 'x':
         case 'y':
         case 'z':
         default:
            return this.jjMoveNfa_0(1, 0);
         case '!':
            return this.jjStartNfaWithStates_0(0, 54, 2);
         case '(':
            return this.jjStopAtPos(0, 55);
         case ')':
            return this.jjStopAtPos(0, 56);
         case '*':
            return this.jjStopAtPos(0, 7);
         case '+':
            return this.jjStopAtPos(0, 4);
         case ',':
            return this.jjStopAtPos(0, 48);
         case '-':
            return this.jjStartNfaWithStates_0(0, 5, 0);
         case '.':
            return this.jjMoveStringLiteralDfa1_0(64L);
         case '/':
            return this.jjStopAtPos(0, 8);
         case ';':
            return this.jjStopAtPos(0, 50);
         case '<':
            return this.jjStartNfaWithStates_0(0, 52, 106);
         case '=':
            return this.jjStartNfaWithStates_0(0, 51, 0);
         case '>':
            return this.jjStartNfaWithStates_0(0, 53, 87);
         case 'A':
            return this.jjMoveStringLiteralDfa1_0(1048576L);
         case 'E':
            return this.jjMoveStringLiteralDfa1_0(8388608L);
         case 'N':
            return this.jjMoveStringLiteralDfa1_0(4194304L);
         case 'O':
            return this.jjMoveStringLiteralDfa1_0(2097152L);
         case '[':
            return this.jjStopAtPos(0, 57);
         case ']':
            return this.jjStopAtPos(0, 58);
         case '^':
            return this.jjStopAtPos(0, 10);
         case '_':
            return this.jjStopAtPos(0, 9);
         case 'b':
            return this.jjMoveStringLiteralDfa1_0(8589934592L);
         case 'c':
            return this.jjMoveStringLiteralDfa1_0(34359738368L);
         case 'f':
            return this.jjMoveStringLiteralDfa1_0(17179869184L);
         case 'm':
            return this.jjMoveStringLiteralDfa1_0(4294967296L);
         case 'o':
            return this.jjMoveStringLiteralDfa1_0(68719476736L);
         case 's':
            return this.jjMoveStringLiteralDfa1_0(67108864L);
         case 'u':
            return this.jjMoveStringLiteralDfa1_0(137438953472L);
         case '{':
            return this.jjStopAtPos(0, 60);
         case '|':
            this.jjmatchedKind = 49;
            return this.jjMoveStringLiteralDfa1_0(576460752303423488L);
         case '}':
            return this.jjStopAtPos(0, 61);
      }
   }

   private int jjMoveStringLiteralDfa1_0(long active0) {
      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var4) {
         this.jjStopStringLiteralDfa_0(0, active0);
         return 1;
      }

      switch (this.curChar) {
         case '.':
            if ((active0 & 64L) != 0L) {
               return this.jjStopAtPos(1, 6);
            }
            break;
         case 'N':
            return this.jjMoveStringLiteralDfa2_0(active0, 1048576L);
         case 'O':
            return this.jjMoveStringLiteralDfa2_0(active0, 4194304L);
         case 'R':
            if ((active0 & 2097152L) != 0L) {
               return this.jjStartNfaWithStates_0(1, 21, 34);
            }
            break;
         case 'X':
            return this.jjMoveStringLiteralDfa2_0(active0, 8388608L);
         case 'a':
            return this.jjMoveStringLiteralDfa2_0(active0, 4294967296L);
         case 'e':
            return this.jjMoveStringLiteralDfa2_0(active0, 34359738368L);
         case 'i':
            return this.jjMoveStringLiteralDfa2_0(active0, 8589934592L);
         case 'l':
            return this.jjMoveStringLiteralDfa2_0(active0, 17179869184L);
         case 'n':
            return this.jjMoveStringLiteralDfa2_0(active0, 137438953472L);
         case 'u':
            return this.jjMoveStringLiteralDfa2_0(active0, 67108864L);
         case 'v':
            return this.jjMoveStringLiteralDfa2_0(active0, 68719476736L);
         case '|':
            if ((active0 & 576460752303423488L) != 0L) {
               return this.jjStopAtPos(1, 59);
            }
      }

      return this.jjStartNfa_0(0, active0);
   }

   private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(0, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(1, active0);
            return 2;
         }

         switch (this.curChar) {
            case 'D':
               if ((active0 & 1048576L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 20, 34);
               }
               break;
            case 'I':
               return this.jjMoveStringLiteralDfa3_0(active0, 8388608L);
            case 'T':
               if ((active0 & 4194304L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 22, 34);
               }
               break;
            case 'd':
               return this.jjMoveStringLiteralDfa3_0(active0, 137438953472L);
            case 'e':
               return this.jjMoveStringLiteralDfa3_0(active0, 68719476736L);
            case 'i':
               return this.jjMoveStringLiteralDfa3_0(active0, 34359738368L);
            case 'm':
               if ((active0 & 67108864L) != 0L) {
                  return this.jjStartNfaWithStates_0(2, 26, 34);
               }
               break;
            case 'n':
               return this.jjMoveStringLiteralDfa3_0(active0, 8589934592L);
            case 'o':
               return this.jjMoveStringLiteralDfa3_0(active0, 17179869184L);
            case 't':
               return this.jjMoveStringLiteralDfa3_0(active0, 4294967296L);
         }

         return this.jjStartNfa_0(1, active0);
      }
   }

   private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(1, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(2, active0);
            return 3;
         }

         switch (this.curChar) {
            case 'S':
               return this.jjMoveStringLiteralDfa4_0(active0, 8388608L);
            case 'e':
               return this.jjMoveStringLiteralDfa4_0(active0, 137438953472L);
            case 'l':
               if ((active0 & 34359738368L) != 0L) {
                  return this.jjStartNfaWithStates_0(3, 35, 34);
               }
            default:
               return this.jjStartNfa_0(2, active0);
            case 'o':
               return this.jjMoveStringLiteralDfa4_0(active0, 25769803776L);
            case 'r':
               return this.jjMoveStringLiteralDfa4_0(active0, 73014444032L);
         }
      }
   }

   private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(2, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(3, active0);
            return 4;
         }

         switch (this.curChar) {
            case 'T':
               return this.jjMoveStringLiteralDfa5_0(active0, 8388608L);
            case 'i':
               return this.jjMoveStringLiteralDfa5_0(active0, 4294967296L);
            case 'l':
               return this.jjMoveStringLiteralDfa5_0(active0, 68719476736L);
            case 'm':
               return this.jjMoveStringLiteralDfa5_0(active0, 8589934592L);
            case 'r':
               if ((active0 & 17179869184L) != 0L) {
                  return this.jjStartNfaWithStates_0(4, 34, 34);
               }

               return this.jjMoveStringLiteralDfa5_0(active0, 137438953472L);
            default:
               return this.jjStartNfa_0(3, active0);
         }
      }
   }

   private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(3, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(4, active0);
            return 5;
         }

         switch (this.curChar) {
            case 'S':
               if ((active0 & 8388608L) != 0L) {
                  return this.jjStartNfaWithStates_0(5, 23, 34);
               }
               break;
            case 'i':
               return this.jjMoveStringLiteralDfa6_0(active0, 77309411328L);
            case 'l':
               return this.jjMoveStringLiteralDfa6_0(active0, 137438953472L);
            case 'x':
               if ((active0 & 4294967296L) != 0L) {
                  return this.jjStartNfaWithStates_0(5, 32, 34);
               }
         }

         return this.jjStartNfa_0(4, active0);
      }
   }

   private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(4, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(5, active0);
            return 6;
         }

         switch (this.curChar) {
            case 'a':
               return this.jjMoveStringLiteralDfa7_0(active0, 8589934592L);
            case 'i':
               return this.jjMoveStringLiteralDfa7_0(active0, 137438953472L);
            case 'n':
               return this.jjMoveStringLiteralDfa7_0(active0, 68719476736L);
            default:
               return this.jjStartNfa_0(5, active0);
         }
      }
   }

   private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(5, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(6, active0);
            return 7;
         }

         switch (this.curChar) {
            case 'e':
               if ((active0 & 68719476736L) != 0L) {
                  return this.jjStartNfaWithStates_0(7, 36, 34);
               }
               break;
            case 'l':
               if ((active0 & 8589934592L) != 0L) {
                  return this.jjStartNfaWithStates_0(7, 33, 34);
               }
               break;
            case 'n':
               return this.jjMoveStringLiteralDfa8_0(active0, 137438953472L);
         }

         return this.jjStartNfa_0(6, active0);
      }
   }

   private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
      if ((active0 = active0 & old0) == 0L) {
         return this.jjStartNfa_0(6, old0);
      } else {
         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var6) {
            this.jjStopStringLiteralDfa_0(7, active0);
            return 8;
         }

         switch (this.curChar) {
            case 'e':
               if ((active0 & 137438953472L) != 0L) {
                  return this.jjStartNfaWithStates_0(8, 37, 34);
               }
            default:
               return this.jjStartNfa_0(7, active0);
         }
      }
   }

   private int jjStartNfaWithStates_0(int pos, int kind, int state) {
      this.jjmatchedKind = kind;
      this.jjmatchedPos = pos;

      try {
         this.curChar = this.input_stream.readChar();
      } catch (IOException var5) {
         return pos + 1;
      }

      return this.jjMoveNfa_0(state, pos + 1);
   }

   private int jjMoveNfa_0(int startState, int curPos) {
      int startsAt = 0;
      this.jjnewStateCnt = 106;
      int i = 1;
      this.jjstateSet[0] = startState;
      int kind = Integer.MAX_VALUE;

      while (true) {
         if (++this.jjround == Integer.MAX_VALUE) {
            this.ReInitRounds();
         }

         if (this.curChar < '@') {
            long l = 1L << this.curChar;

            do {
               i--;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (this.curChar == '>' && kind > 11) {
                        kind = 11;
                     }
                     break;
                  case 1:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddStates(0, 6);
                     } else if (this.curChar == '>') {
                        this.jjCheckNAddTwoStates(2, 87);
                     } else if (this.curChar == '<') {
                        this.jjCheckNAddTwoStates(2, 0);
                     } else if (this.curChar == '.') {
                        this.jjCheckNAdd(37);
                     } else if (this.curChar == '"') {
                        this.jjCheckNAddTwoStates(32, 33);
                     } else if (this.curChar == ' ') {
                        if (kind > 24) {
                           kind = 24;
                        }

                        this.jjCheckNAdd(5);
                     } else if (this.curChar == '-') {
                        this.jjCheckNAdd(0);
                     } else if (this.curChar == '!') {
                        this.jjCheckNAdd(2);
                     } else if (this.curChar == '=') {
                        this.jjCheckNAdd(0);
                     } else if (this.curChar == '?' && kind > 40) {
                        kind = 40;
                     }

                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }

                     if ((287667426198290432L & l) != 0L) {
                        if (kind > 39) {
                           kind = 39;
                        }

                        this.jjCheckNAddStates(7, 9);
                     } else if (this.curChar == '0') {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddStates(10, 12);
                     }
                     break;
                  case 2:
                     if (this.curChar == '=' && kind > 11) {
                        kind = 11;
                     }
                     break;
                  case 3:
                     if (this.curChar == '!') {
                        this.jjCheckNAdd(2);
                     }
                     break;
                  case 4:
                     if (this.curChar == '-') {
                        this.jjCheckNAdd(0);
                     }
                     break;
                  case 5:
                     if (this.curChar == ' ') {
                        if (kind > 24) {
                           kind = 24;
                        }

                        this.jjCheckNAdd(5);
                     }
                  case 6:
                  case 7:
                  case 9:
                  case 10:
                  case 11:
                  case 12:
                  case 13:
                  case 14:
                  case 15:
                  case 16:
                  case 17:
                  case 18:
                  case 19:
                  case 20:
                  case 21:
                  case 22:
                  case 23:
                  case 24:
                  case 25:
                  case 26:
                  case 27:
                  case 28:
                  case 29:
                  case 30:
                  case 38:
                  case 41:
                  case 43:
                  case 45:
                  case 49:
                  case 53:
                  case 59:
                  case 62:
                  case 63:
                  case 64:
                  case 65:
                  case 66:
                  case 67:
                  case 68:
                  case 69:
                  case 70:
                  case 71:
                  case 72:
                  case 73:
                  case 74:
                  case 75:
                  case 76:
                  case 77:
                  case 78:
                  case 79:
                  case 80:
                  case 81:
                  case 82:
                  case 83:
                  case 84:
                  case 88:
                  case 89:
                  case 90:
                  case 91:
                  case 92:
                  case 93:
                  case 94:
                  case 95:
                  case 96:
                  case 97:
                  case 98:
                  case 99:
                  case 100:
                  case 101:
                  case 102:
                  default:
                     break;
                  case 8:
                  case 34:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 31:
                     if (this.curChar == '"') {
                        this.jjCheckNAddTwoStates(32, 33);
                     }
                     break;
                  case 32:
                     if ((-17179869185L & l) != 0L) {
                        this.jjCheckNAddTwoStates(32, 33);
                     }
                     break;
                  case 33:
                     if (this.curChar == '"' && kind > 38) {
                        kind = 38;
                     }
                     break;
                  case 35:
                     if (this.curChar == '?' && kind > 40) {
                        kind = 40;
                     }
                     break;
                  case 36:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(37);
                     }
                     break;
                  case 37:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddStates(13, 15);
                     }
                     break;
                  case 39:
                     if ((43980465111040L & l) != 0L) {
                        this.jjCheckNAdd(40);
                     }
                     break;
                  case 40:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(40, 41);
                     }
                     break;
                  case 42:
                     if (this.curChar == '0') {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddStates(10, 12);
                     }
                     break;
                  case 44:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(44, 45);
                     }
                     break;
                  case 46:
                     if ((71776119061217280L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(46, 45);
                     }
                     break;
                  case 47:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddStates(0, 6);
                     }
                     break;
                  case 48:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddStates(16, 18);
                     }
                     break;
                  case 50:
                     if ((43980465111040L & l) != 0L) {
                        this.jjCheckNAdd(51);
                     }
                     break;
                  case 51:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddTwoStates(51, 41);
                     }
                     break;
                  case 52:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddTwoStates(52, 53);
                     }
                     break;
                  case 54:
                     if ((43980465111040L & l) != 0L) {
                        this.jjCheckNAdd(55);
                     }
                     break;
                  case 55:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(55, 41);
                     }
                     break;
                  case 56:
                     if ((287948901175001088L & l) != 0L) {
                        this.jjCheckNAddTwoStates(56, 57);
                     }
                     break;
                  case 57:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(58);
                     }
                     break;
                  case 58:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddStates(19, 21);
                     }
                     break;
                  case 60:
                     if ((43980465111040L & l) != 0L) {
                        this.jjCheckNAdd(61);
                     }
                     break;
                  case 61:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(61, 41);
                     }
                     break;
                  case 85:
                     if (this.curChar == '<') {
                        this.jjCheckNAddTwoStates(2, 0);
                     }
                     break;
                  case 86:
                     if (this.curChar == '>') {
                        this.jjCheckNAddTwoStates(2, 87);
                     }
                     break;
                  case 87:
                     if (this.curChar == '<') {
                        if (kind > 11) {
                           kind = 11;
                        }
                     } else if (this.curChar == '=' && kind > 11) {
                        kind = 11;
                     }
                     break;
                  case 103:
                     if ((287667426198290432L & l) != 0L) {
                        if (kind > 39) {
                           kind = 39;
                        }

                        this.jjCheckNAddStates(7, 9);
                     }
                     break;
                  case 104:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 39) {
                           kind = 39;
                        }

                        this.jjCheckNAdd(104);
                     }
                     break;
                  case 105:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(105, 45);
                     }
                     break;
                  case 106:
                     if (this.curChar == '>') {
                        if (kind > 11) {
                           kind = 11;
                        }
                     } else if (this.curChar == '=' && kind > 11) {
                        kind = 11;
                     }
               }
            } while (i != startsAt);
         } else if (this.curChar < 128) {
            long l = 1L << (this.curChar & '?');

            do {
               i--;
               switch (this.jjstateSet[i]) {
                  case 1:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }

                     if (this.curChar == 'l') {
                        this.jjAddStates(22, 23);
                     } else if (this.curChar == 'F') {
                        this.jjAddStates(24, 27);
                     } else if (this.curChar == 'v') {
                        this.jjstateSet[this.jjnewStateCnt++] = 29;
                     } else if (this.curChar == 'i') {
                        this.jjstateSet[this.jjnewStateCnt++] = 23;
                     } else if (this.curChar == 'p') {
                        this.jjstateSet[this.jjnewStateCnt++] = 15;
                     } else if (this.curChar == 's') {
                        this.jjstateSet[this.jjnewStateCnt++] = 8;
                     }
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 31:
                  case 33:
                  case 35:
                  case 36:
                  case 37:
                  case 39:
                  case 40:
                  case 42:
                  case 46:
                  case 47:
                  case 48:
                  case 50:
                  case 51:
                  case 52:
                  case 54:
                  case 55:
                  case 56:
                  case 57:
                  case 58:
                  case 60:
                  case 61:
                  case 85:
                  case 86:
                  case 87:
                  default:
                     break;
                  case 6:
                     if (this.curChar == 'r') {
                        if (kind > 25) {
                           kind = 25;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 7;
                     }
                     break;
                  case 7:
                     if (this.curChar == 't' && kind > 25) {
                        kind = 25;
                     }
                     break;
                  case 8:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }

                     if (this.curChar == 'q') {
                        this.jjstateSet[this.jjnewStateCnt++] = 6;
                     }
                     break;
                  case 9:
                     if (this.curChar == 's') {
                        this.jjstateSet[this.jjnewStateCnt++] = 8;
                     }
                     break;
                  case 10:
                     if (this.curChar == 'd') {
                        if (kind > 27) {
                           kind = 27;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 13;
                     }
                     break;
                  case 11:
                     if (this.curChar == 't' && kind > 27) {
                        kind = 27;
                     }
                     break;
                  case 12:
                     if (this.curChar == 'c') {
                        this.jjstateSet[this.jjnewStateCnt++] = 11;
                     }
                     break;
                  case 13:
                     if (this.curChar == 'u') {
                        this.jjstateSet[this.jjnewStateCnt++] = 12;
                     }
                     break;
                  case 14:
                     if (this.curChar == 'o') {
                        this.jjstateSet[this.jjnewStateCnt++] = 10;
                     }
                     break;
                  case 15:
                     if (this.curChar == 'r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 14;
                     }
                     break;
                  case 16:
                     if (this.curChar == 'p') {
                        this.jjstateSet[this.jjnewStateCnt++] = 15;
                     }
                     break;
                  case 17:
                     if (this.curChar == 't') {
                        if (kind > 28) {
                           kind = 28;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 22;
                     }
                     break;
                  case 18:
                     if (this.curChar == 'l' && kind > 28) {
                        kind = 28;
                     }
                     break;
                  case 19:
                     if (this.curChar == 'a') {
                        this.jjstateSet[this.jjnewStateCnt++] = 18;
                     }
                     break;
                  case 20:
                     if (this.curChar == 'r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 19;
                     }
                     break;
                  case 21:
                     if (this.curChar == 'g') {
                        this.jjstateSet[this.jjnewStateCnt++] = 20;
                     }
                     break;
                  case 22:
                     if (this.curChar == 'e') {
                        this.jjstateSet[this.jjnewStateCnt++] = 21;
                     }
                     break;
                  case 23:
                     if (this.curChar == 'n') {
                        this.jjstateSet[this.jjnewStateCnt++] = 17;
                     }
                     break;
                  case 24:
                     if (this.curChar == 'i') {
                        this.jjstateSet[this.jjnewStateCnt++] = 23;
                     }
                     break;
                  case 25:
                     if (this.curChar == 'c') {
                        if (kind > 30) {
                           kind = 30;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 28;
                     }
                     break;
                  case 26:
                     if (this.curChar == 'r' && kind > 30) {
                        kind = 30;
                     }
                     break;
                  case 27:
                     if (this.curChar == 'o') {
                        this.jjstateSet[this.jjnewStateCnt++] = 26;
                     }
                     break;
                  case 28:
                     if (this.curChar == 't') {
                        this.jjstateSet[this.jjnewStateCnt++] = 27;
                     }
                     break;
                  case 29:
                     if (this.curChar == 'e') {
                        this.jjstateSet[this.jjnewStateCnt++] = 25;
                     }
                     break;
                  case 30:
                     if (this.curChar == 'v') {
                        this.jjstateSet[this.jjnewStateCnt++] = 29;
                     }
                     break;
                  case 32:
                     this.jjAddStates(28, 29);
                     break;
                  case 34:
                     if ((576460743847706622L & l) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 38:
                     if ((137438953504L & l) != 0L) {
                        this.jjAddStates(30, 31);
                     }
                     break;
                  case 41:
                     if ((343597383760L & l) != 0L && kind > 43) {
                        kind = 43;
                     }
                     break;
                  case 43:
                     if ((72057594054705152L & l) != 0L) {
                        this.jjCheckNAdd(44);
                     }
                     break;
                  case 44:
                     if ((541165879422L & l) != 0L) {
                        if (kind > 43) {
                           kind = 43;
                        }

                        this.jjCheckNAddTwoStates(44, 45);
                     }
                     break;
                  case 45:
                     if ((17592186048512L & l) != 0L && kind > 43) {
                        kind = 43;
                     }
                     break;
                  case 49:
                     if ((137438953504L & l) != 0L) {
                        this.jjAddStates(32, 33);
                     }
                     break;
                  case 53:
                     if ((137438953504L & l) != 0L) {
                        this.jjAddStates(34, 35);
                     }
                     break;
                  case 59:
                     if ((137438953504L & l) != 0L) {
                        this.jjAddStates(36, 37);
                     }
                     break;
                  case 62:
                     if (this.curChar == 'F') {
                        this.jjAddStates(24, 27);
                     }
                     break;
                  case 63:
                     if (this.curChar == 'H' && kind > 19) {
                        kind = 19;
                     }
                     break;
                  case 64:
                  case 69:
                     if (this.curChar == 'C') {
                        this.jjCheckNAdd(63);
                     }
                     break;
                  case 65:
                     if (this.curChar == 'A') {
                        this.jjstateSet[this.jjnewStateCnt++] = 64;
                     }
                     break;
                  case 66:
                     if (this.curChar == 'E') {
                        this.jjstateSet[this.jjnewStateCnt++] = 65;
                     }
                     break;
                  case 67:
                     if (this.curChar == 'R') {
                        this.jjstateSet[this.jjnewStateCnt++] = 66;
                     }
                     break;
                  case 68:
                     if (this.curChar == 'O') {
                        this.jjstateSet[this.jjnewStateCnt++] = 67;
                     }
                     break;
                  case 70:
                     if (this.curChar == 'A') {
                        this.jjstateSet[this.jjnewStateCnt++] = 69;
                     }
                     break;
                  case 71:
                     if (this.curChar == 'E') {
                        this.jjstateSet[this.jjnewStateCnt++] = 70;
                     }
                     break;
                  case 72:
                     if (this.curChar == '_') {
                        this.jjstateSet[this.jjnewStateCnt++] = 71;
                     }
                     break;
                  case 73:
                     if (this.curChar == 'R') {
                        this.jjstateSet[this.jjnewStateCnt++] = 72;
                     }
                     break;
                  case 74:
                     if (this.curChar == 'O') {
                        this.jjstateSet[this.jjnewStateCnt++] = 73;
                     }
                     break;
                  case 75:
                     if (this.curChar == 'L' && kind > 19) {
                        kind = 19;
                     }
                     break;
                  case 76:
                  case 80:
                     if (this.curChar == 'L') {
                        this.jjCheckNAdd(75);
                     }
                     break;
                  case 77:
                     if (this.curChar == 'A') {
                        this.jjstateSet[this.jjnewStateCnt++] = 76;
                     }
                     break;
                  case 78:
                     if (this.curChar == 'R') {
                        this.jjstateSet[this.jjnewStateCnt++] = 77;
                     }
                     break;
                  case 79:
                     if (this.curChar == 'O') {
                        this.jjstateSet[this.jjnewStateCnt++] = 78;
                     }
                     break;
                  case 81:
                     if (this.curChar == 'A') {
                        this.jjstateSet[this.jjnewStateCnt++] = 80;
                     }
                     break;
                  case 82:
                     if (this.curChar == '_') {
                        this.jjstateSet[this.jjnewStateCnt++] = 81;
                     }
                     break;
                  case 83:
                     if (this.curChar == 'R') {
                        this.jjstateSet[this.jjnewStateCnt++] = 82;
                     }
                     break;
                  case 84:
                     if (this.curChar == 'O') {
                        this.jjstateSet[this.jjnewStateCnt++] = 83;
                     }
                     break;
                  case 88:
                     if (this.curChar == 'l') {
                        this.jjAddStates(22, 23);
                     }
                     break;
                  case 89:
                     if (this.curChar == 'm') {
                        if (kind > 29) {
                           kind = 29;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 91;
                     }
                     break;
                  case 90:
                     if (this.curChar == 's' && kind > 29) {
                        kind = 29;
                     }
                     break;
                  case 91:
                     if (this.curChar == 'e') {
                        this.jjstateSet[this.jjnewStateCnt++] = 90;
                     }
                     break;
                  case 92:
                     if (this.curChar == 'i') {
                        this.jjstateSet[this.jjnewStateCnt++] = 89;
                     }
                     break;
                  case 93:
                     if (this.curChar == 'g') {
                        if (kind > 31) {
                           kind = 31;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 101;
                     }
                     break;
                  case 94:
                     if (this.curChar == 'm') {
                        if (kind > 31) {
                           kind = 31;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 96;
                     }
                     break;
                  case 95:
                     if (this.curChar == 's' && kind > 31) {
                        kind = 31;
                     }
                     break;
                  case 96:
                     if (this.curChar == 'u') {
                        this.jjstateSet[this.jjnewStateCnt++] = 95;
                     }
                     break;
                  case 97:
                     if (this.curChar == 'h') {
                        this.jjstateSet[this.jjnewStateCnt++] = 94;
                     }
                     break;
                  case 98:
                     if (this.curChar == 't') {
                        this.jjstateSet[this.jjnewStateCnt++] = 97;
                     }
                     break;
                  case 99:
                     if (this.curChar == 'i') {
                        this.jjstateSet[this.jjnewStateCnt++] = 98;
                     }
                     break;
                  case 100:
                     if (this.curChar == 'r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 99;
                     }
                     break;
                  case 101:
                     if (this.curChar == 'a') {
                        this.jjstateSet[this.jjnewStateCnt++] = 100;
                     }
                     break;
                  case 102:
                     if (this.curChar == 'o') {
                        this.jjstateSet[this.jjnewStateCnt++] = 93;
                     }
               }
            } while (i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & '?');

            do {
               i--;
               switch (this.jjstateSet[i]) {
                  case 1:
                  case 34:
                     if ((jjbitVec1[i2] & l2) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 8:
                     if ((jjbitVec1[i2] & l2) != 0L) {
                        if (kind > 40) {
                           kind = 40;
                        }

                        this.jjCheckNAdd(34);
                     }
                     break;
                  case 32:
                     if ((jjbitVec0[i2] & l2) != 0L) {
                        this.jjAddStates(28, 29);
                     }
               }
            } while (i != startsAt);
         }

         if (kind != Integer.MAX_VALUE) {
            this.jjmatchedKind = kind;
            this.jjmatchedPos = curPos;
            kind = Integer.MAX_VALUE;
         }

         curPos++;
         if ((i = this.jjnewStateCnt) == (startsAt = 106 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   public FormulaTokenManager(SimpleCharStream stream) {
      this.input_stream = stream;
   }

   public FormulaTokenManager(SimpleCharStream stream, int lexState) {
      this(stream);
      this.SwitchTo(lexState);
   }

   public void ReInit(SimpleCharStream stream) {
      this.jjmatchedPos = this.jjnewStateCnt = 0;
      this.curLexState = this.defaultLexState;
      this.input_stream = stream;
      this.ReInitRounds();
   }

   private void ReInitRounds() {
      this.jjround = -2147483647;
      int i = 106;

      while (i-- > 0) {
         this.jjrounds[i] = Integer.MIN_VALUE;
      }
   }

   public void ReInit(SimpleCharStream stream, int lexState) {
      this.ReInit(stream);
      this.SwitchTo(lexState);
   }

   public void SwitchTo(int lexState) {
      if (lexState < 1 && lexState >= 0) {
         this.curLexState = lexState;
      } else {
         throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
      }
   }

   protected Token jjFillToken() {
      String im = jjstrLiteralImages[this.jjmatchedKind];
      String curTokenImage = im == null ? this.input_stream.GetImage() : im;
      int beginLine = this.input_stream.getBeginLine();
      int beginColumn = this.input_stream.getBeginColumn();
      int endLine = this.input_stream.getEndLine();
      int endColumn = this.input_stream.getEndColumn();
      Token t = Token.newToken(this.jjmatchedKind, curTokenImage);
      t.beginLine = beginLine;
      t.endLine = endLine;
      t.beginColumn = beginColumn;
      t.endColumn = endColumn;
      return t;
   }

   public Token getNextToken() {
      int curPos = 0;

      while (true) {
         try {
            this.curChar = this.input_stream.BeginToken();
         } catch (IOException var8) {
            this.jjmatchedKind = 0;
            return this.jjFillToken();
         }

         try {
            this.input_stream.backup(0);

            while (this.curChar <= '\r' && (8704L & 1L << this.curChar) != 0L) {
               this.curChar = this.input_stream.BeginToken();
            }
         } catch (IOException var10) {
            continue;
         }

         this.jjmatchedKind = Integer.MAX_VALUE;
         this.jjmatchedPos = 0;
         curPos = this.jjMoveStringLiteralDfa0_0();
         if (this.jjmatchedKind == Integer.MAX_VALUE) {
            int error_line = this.input_stream.getEndLine();
            int error_column = this.input_stream.getEndColumn();
            String error_after = null;
            boolean EOFSeen = false;

            try {
               this.input_stream.readChar();
               this.input_stream.backup(1);
            } catch (IOException var9) {
               EOFSeen = true;
               error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
               if (this.curChar != '\n' && this.curChar != '\r') {
                  error_column++;
               } else {
                  error_line++;
                  error_column = 0;
               }
            }

            if (!EOFSeen) {
               this.input_stream.backup(1);
               error_after = curPos <= 1 ? "" : this.input_stream.GetImage();
            }

            throw new TokenMgrError(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
         }

         if (this.jjmatchedPos + 1 < curPos) {
            this.input_stream.backup(curPos - this.jjmatchedPos - 1);
         }

         if ((jjtoToken[this.jjmatchedKind >> 6] & 1L << (this.jjmatchedKind & 63)) != 0L) {
            return this.jjFillToken();
         }
      }
   }

   private void jjCheckNAdd(int state) {
      if (this.jjrounds[state] != this.jjround) {
         this.jjstateSet[this.jjnewStateCnt++] = state;
         this.jjrounds[state] = this.jjround;
      }
   }

   private void jjAddStates(int start, int end) {
      do {
         this.jjstateSet[this.jjnewStateCnt++] = jjnextStates[start];
      } while (start++ != end);
   }

   private void jjCheckNAddTwoStates(int state1, int state2) {
      this.jjCheckNAdd(state1);
      this.jjCheckNAdd(state2);
   }

   private void jjCheckNAddStates(int start, int end) {
      do {
         this.jjCheckNAdd(jjnextStates[start]);
      } while (start++ != end);
   }
}
