package de.jave.calculus.parser;

import java.io.IOException;
import java.io.PrintStream;

public class CalculusTokenManager implements CalculusConstants {
   public PrintStream debugStream = System.out;
   static final long[] jjbitVec0 = new long[]{0L, 0L, -1L, -1L};
   static final int[] jjnextStates = new int[]{31, 32, 34, 17, 18, 19, 12, 21, 22, 24, 30, 35, 26, 28, 7, 18, 19, 12, 38, 40, 13, 14};
   public static final String[] jjstrLiteralImages = new String[]{
      "",
      null,
      null,
      null,
      null,
      null,
      null,
      "+",
      "-",
      "*",
      "/",
      "^",
      "x",
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
      null,
      null,
      ";",
      "%",
      "(",
      ")",
      ","
   };
   public static final String[] lexStateNames = new String[]{"DEFAULT"};
   static final long[] jjtoToken = new long[]{1049165697L};
   static final long[] jjtoSkip = new long[]{126L};
   protected SimpleCharStream input_stream;
   private final int[] jjrounds = new int[41];
   private final int[] jjstateSet = new int[82];
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
         case '%':
            return this.jjStopAtPos(0, 26);
         case '(':
            return this.jjStopAtPos(0, 27);
         case ')':
            return this.jjStopAtPos(0, 28);
         case '*':
            return this.jjStopAtPos(0, 9);
         case '+':
            return this.jjStopAtPos(0, 7);
         case ',':
            return this.jjStopAtPos(0, 29);
         case '-':
            return this.jjStopAtPos(0, 8);
         case '/':
            return this.jjStartNfaWithStates_0(0, 10, 30);
         case ';':
            return this.jjStopAtPos(0, 25);
         case '^':
            return this.jjStopAtPos(0, 11);
         case 'x':
            return this.jjStartNfaWithStates_0(0, 12, 9);
         default:
            return this.jjMoveNfa_0(1, 0);
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
      this.jjnewStateCnt = 41;
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
                  case 1:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddStates(3, 9);
                     } else if (this.curChar == '/') {
                        this.jjAddStates(10, 11);
                     } else if (this.curChar == '.') {
                        this.jjCheckNAddTwoStates(11, 15);
                     }

                     if ((287667426198290432L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(6, 7);
                     } else if (this.curChar == '0') {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddStates(12, 14);
                     }
                  case 2:
                  case 3:
                  case 4:
                  case 7:
                  case 8:
                  case 12:
                  case 26:
                  default:
                     break;
                  case 5:
                     if ((287667426198290432L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(6, 7);
                     }
                     break;
                  case 6:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(6, 7);
                     }
                     break;
                  case 9:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 23) {
                           kind = 23;
                        }

                        this.jjstateSet[this.jjnewStateCnt++] = 9;
                     }
                     break;
                  case 10:
                     if (this.curChar == '.') {
                        this.jjCheckNAddTwoStates(11, 15);
                     }
                     break;
                  case 11:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddTwoStates(11, 12);
                     }
                     break;
                  case 13:
                     if ((43980465111040L & l) != 0L) {
                        this.jjCheckNAdd(14);
                     }
                     break;
                  case 14:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(14);
                     }
                     break;
                  case 15:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(15);
                     }
                     break;
                  case 16:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddStates(3, 9);
                     }
                     break;
                  case 17:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddTwoStates(17, 12);
                     }
                     break;
                  case 18:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddStates(15, 17);
                     }
                     break;
                  case 19:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(20);
                     }
                     break;
                  case 20:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddTwoStates(20, 12);
                     }
                     break;
                  case 21:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAddTwoStates(21, 22);
                     }
                     break;
                  case 22:
                     if (this.curChar == '.') {
                        this.jjCheckNAdd(23);
                     }
                     break;
                  case 23:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(23);
                     }
                     break;
                  case 24:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 19) {
                           kind = 19;
                        }

                        this.jjCheckNAdd(24);
                     }
                     break;
                  case 25:
                     if (this.curChar == '0') {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddStates(12, 14);
                     }
                     break;
                  case 27:
                     if ((287948901175001088L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(27, 7);
                     }
                     break;
                  case 28:
                     if ((71776119061217280L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(28, 7);
                     }
                     break;
                  case 29:
                     if (this.curChar == '/') {
                        this.jjAddStates(10, 11);
                     }
                     break;
                  case 30:
                     if (this.curChar == '*') {
                        this.jjCheckNAddTwoStates(36, 37);
                     } else if (this.curChar == '/') {
                        this.jjCheckNAddStates(0, 2);
                     }
                     break;
                  case 31:
                     if ((-9217L & l) != 0L) {
                        this.jjCheckNAddStates(0, 2);
                     }
                     break;
                  case 32:
                     if ((9216L & l) != 0L && kind > 5) {
                        kind = 5;
                     }
                     break;
                  case 33:
                     if (this.curChar == '\n' && kind > 5) {
                        kind = 5;
                     }
                     break;
                  case 34:
                     if (this.curChar == '\r') {
                        this.jjstateSet[this.jjnewStateCnt++] = 33;
                     }
                     break;
                  case 35:
                     if (this.curChar == '*') {
                        this.jjCheckNAddTwoStates(36, 37);
                     }
                     break;
                  case 36:
                     if ((-4398046511105L & l) != 0L) {
                        this.jjCheckNAddTwoStates(36, 37);
                     }
                     break;
                  case 37:
                     if (this.curChar == '*') {
                        this.jjAddStates(18, 19);
                     }
                     break;
                  case 38:
                     if ((-140737488355329L & l) != 0L) {
                        this.jjCheckNAddTwoStates(39, 37);
                     }
                     break;
                  case 39:
                     if ((-4398046511105L & l) != 0L) {
                        this.jjCheckNAddTwoStates(39, 37);
                     }
                     break;
                  case 40:
                     if (this.curChar == '/' && kind > 6) {
                        kind = 6;
                     }
               }
            } while (i != startsAt);
         } else if (this.curChar < 128) {
            long l = 1L << (this.curChar & '?');

            do {
               i--;
               switch (this.jjstateSet[i]) {
                  case 0:
                     if (this.curChar == 'i' && kind > 13) {
                        kind = 13;
                     }
                     break;
                  case 1:
                     if ((576460745995190270L & l) != 0L) {
                        if (kind > 23) {
                           kind = 23;
                        }

                        this.jjCheckNAdd(9);
                     }

                     if ((137438953504L & l) != 0L) {
                        if (kind > 14) {
                           kind = 14;
                        }
                     } else if (this.curChar == 'P') {
                        this.jjstateSet[this.jjnewStateCnt++] = 2;
                     } else if (this.curChar == 'p') {
                        this.jjstateSet[this.jjnewStateCnt++] = 0;
                     }
                     break;
                  case 2:
                     if (this.curChar == 'I' && kind > 13) {
                        kind = 13;
                     }
                     break;
                  case 3:
                     if (this.curChar == 'P') {
                        this.jjstateSet[this.jjnewStateCnt++] = 2;
                     }
                     break;
                  case 4:
                     if ((137438953504L & l) != 0L && kind > 14) {
                        kind = 14;
                     }
                  case 5:
                  case 6:
                  case 10:
                  case 11:
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
                  case 28:
                  case 29:
                  case 30:
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 37:
                  default:
                     break;
                  case 7:
                     if ((17592186048512L & l) != 0L && kind > 15) {
                        kind = 15;
                     }
                     break;
                  case 8:
                  case 9:
                     if ((576460745995190270L & l) != 0L) {
                        if (kind > 23) {
                           kind = 23;
                        }

                        this.jjCheckNAdd(9);
                     }
                     break;
                  case 12:
                     if ((137438953504L & l) != 0L) {
                        this.jjAddStates(20, 21);
                     }
                     break;
                  case 26:
                     if ((72057594054705152L & l) != 0L) {
                        this.jjCheckNAdd(27);
                     }
                     break;
                  case 27:
                     if ((541165879422L & l) != 0L) {
                        if (kind > 15) {
                           kind = 15;
                        }

                        this.jjCheckNAddTwoStates(27, 7);
                     }
                     break;
                  case 31:
                     this.jjAddStates(0, 2);
                     break;
                  case 36:
                     this.jjCheckNAddTwoStates(36, 37);
                     break;
                  case 38:
                  case 39:
                     this.jjCheckNAddTwoStates(39, 37);
               }
            } while (i != startsAt);
         } else {
            int i2 = (this.curChar & 255) >> 6;
            long l2 = 1L << (this.curChar & '?');

            do {
               i--;
               switch (this.jjstateSet[i]) {
                  case 31:
                     if ((jjbitVec0[i2] & l2) != 0L) {
                        this.jjAddStates(0, 2);
                     }
                  case 32:
                  case 33:
                  case 34:
                  case 35:
                  case 37:
                  default:
                     break;
                  case 36:
                     if ((jjbitVec0[i2] & l2) != 0L) {
                        this.jjCheckNAddTwoStates(36, 37);
                     }
                     break;
                  case 38:
                  case 39:
                     if ((jjbitVec0[i2] & l2) != 0L) {
                        this.jjCheckNAddTwoStates(39, 37);
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
         if ((i = this.jjnewStateCnt) == (startsAt = 41 - (this.jjnewStateCnt = startsAt))) {
            return curPos;
         }

         try {
            this.curChar = this.input_stream.readChar();
         } catch (IOException var9) {
            return curPos;
         }
      }
   }

   public CalculusTokenManager(SimpleCharStream stream) {
      this.input_stream = stream;
   }

   public CalculusTokenManager(SimpleCharStream stream, int lexState) {
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
      int i = 41;

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

            while (this.curChar <= ' ' && (4294977024L & 1L << this.curChar) != 0L) {
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
