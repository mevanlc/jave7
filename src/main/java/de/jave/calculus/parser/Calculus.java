package de.jave.calculus.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Calculus implements CalculusTreeConstants, CalculusConstants {
   protected JJTCalculusState jjtree = new JJTCalculusState();
   public CalculusTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   private int jj_gen;
   private final int[] jj_la1 = new int[10];
   private static int[] jj_la1_0;
   private final Calculus.JJCalls[] jj_2_rtns = new Calculus.JJCalls[1];
   private boolean jj_rescan = false;
   private int jj_gc = 0;
   private final Calculus.LookaheadSuccess jj_ls = new Calculus.LookaheadSuccess();
   private List<int[]> jj_expentries = new ArrayList<>();
   private int[] jj_expentry;
   private int jj_kind = -1;
   private int[] jj_lasttokens = new int[100];
   private int jj_endpos;

   public static Node getParseTree(String expression) throws ParseException {
      InputStream stream = new ByteArrayInputStream(expression.getBytes());
      Calculus parser = new Calculus(stream);
      return parser.Start();
   }

   public final ASTStart Start() throws ParseException {
      ASTStart jjtn000 = new ASTStart(0);
      boolean jjtc000 = true;
      this.jjtree.openNodeScope(jjtn000);

      ASTStart jjte000;
      try {
         this.AdditiveExpression();
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 25:
               this.jj_consume_token(25);
               break;
            default:
               this.jj_la1[0] = this.jj_gen;
         }

         this.jjtree.closeNodeScope(jjtn000, true);
         jjtc000 = false;
         jjte000 = jjtn000;
      } catch (Throwable var7) {
         if (jjtc000) {
            this.jjtree.clearNodeScope(jjtn000);
            jjtc000 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         }

         if (var7 instanceof ParseException) {
            throw (ParseException)var7;
         }

         throw (Error)var7;
      } finally {
         if (jjtc000) {
            this.jjtree.closeNodeScope(jjtn000, true);
         }
      }

      return jjte000;
   }

   public final void AdditiveExpression() throws ParseException {
      this.MultiplicativeExpression();

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 7:
            case 8:
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 7:
                     this.jj_consume_token(7);
                     ASTAddNode jjtn001 = new ASTAddNode(2);
                     boolean jjtc001 = true;
                     this.jjtree.openNodeScope(jjtn001);

                     try {
                        this.MultiplicativeExpression();
                        continue;
                     } catch (Throwable var17) {
                        if (jjtc001) {
                           this.jjtree.clearNodeScope(jjtn001);
                           jjtc001 = false;
                        } else {
                           this.jjtree.popNode();
                        }

                        if (var17 instanceof RuntimeException) {
                           throw (RuntimeException)var17;
                        }

                        if (var17 instanceof ParseException) {
                           throw (ParseException)var17;
                        }

                        throw (Error)var17;
                     } finally {
                        if (jjtc001) {
                           this.jjtree.closeNodeScope(jjtn001, 2);
                        }
                     }
                  case 8:
                     this.jj_consume_token(8);
                     ASTSubtractNode jjtn002 = new ASTSubtractNode(3);
                     boolean jjtc002 = true;
                     this.jjtree.openNodeScope(jjtn002);

                     try {
                        this.MultiplicativeExpression();
                        continue;
                     } catch (Throwable var15) {
                        if (jjtc002) {
                           this.jjtree.clearNodeScope(jjtn002);
                           jjtc002 = false;
                        } else {
                           this.jjtree.popNode();
                        }

                        if (var15 instanceof RuntimeException) {
                           throw (RuntimeException)var15;
                        }

                        if (var15 instanceof ParseException) {
                           throw (ParseException)var15;
                        }

                        throw (Error)var15;
                     } finally {
                        if (jjtc002) {
                           this.jjtree.closeNodeScope(jjtn002, 2);
                        }
                     }
                  default:
                     this.jj_la1[2] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }
            default:
               this.jj_la1[1] = this.jj_gen;
               return;
         }
      }
   }

   public final void MultiplicativeExpression() throws ParseException {
      this.ExpExpression();

      while (true) {
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 9:
            case 10:
            case 26:
               switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                  case 9:
                     this.jj_consume_token(9);
                     ASTMultNode jjtn001 = new ASTMultNode(4);
                     boolean jjtc001 = true;
                     this.jjtree.openNodeScope(jjtn001);

                     try {
                        this.ExpExpression();
                        continue;
                     } catch (Throwable var31) {
                        if (jjtc001) {
                           this.jjtree.clearNodeScope(jjtn001);
                           jjtc001 = false;
                        } else {
                           this.jjtree.popNode();
                        }

                        if (var31 instanceof RuntimeException) {
                           throw (RuntimeException)var31;
                        }

                        if (var31 instanceof ParseException) {
                           throw (ParseException)var31;
                        }

                        throw (Error)var31;
                     } finally {
                        if (jjtc001) {
                           this.jjtree.closeNodeScope(jjtn001, 2);
                        }
                     }
                  case 10:
                     this.jj_consume_token(10);
                     ASTDivNode jjtn002 = new ASTDivNode(5);
                     boolean jjtc002 = true;
                     this.jjtree.openNodeScope(jjtn002);

                     try {
                        this.ExpExpression();
                        continue;
                     } catch (Throwable var29) {
                        if (jjtc002) {
                           this.jjtree.clearNodeScope(jjtn002);
                           jjtc002 = false;
                        } else {
                           this.jjtree.popNode();
                        }

                        if (var29 instanceof RuntimeException) {
                           throw (RuntimeException)var29;
                        }

                        if (var29 instanceof ParseException) {
                           throw (ParseException)var29;
                        }

                        throw (Error)var29;
                     } finally {
                        if (jjtc002) {
                           this.jjtree.closeNodeScope(jjtn002, 2);
                        }
                     }
                  case 26:
                     this.jj_consume_token(26);
                     ASTModNode jjtn003 = new ASTModNode(6);
                     boolean jjtc003 = true;
                     this.jjtree.openNodeScope(jjtn003);

                     try {
                        this.ExpExpression();
                        continue;
                     } catch (Throwable var27) {
                        if (jjtc003) {
                           this.jjtree.clearNodeScope(jjtn003);
                           jjtc003 = false;
                        } else {
                           this.jjtree.popNode();
                        }

                        if (var27 instanceof RuntimeException) {
                           throw (RuntimeException)var27;
                        }

                        if (var27 instanceof ParseException) {
                           throw (ParseException)var27;
                        }

                        throw (Error)var27;
                     } finally {
                        if (jjtc003) {
                           this.jjtree.closeNodeScope(jjtn003, 2);
                        }
                     }
                  default:
                     this.jj_la1[4] = this.jj_gen;
                     this.jj_consume_token(-1);
                     throw new ParseException();
               }
            default:
               this.jj_la1[3] = this.jj_gen;
               return;
         }
      }
   }

   public final void ExpExpression() throws ParseException {
      this.UnaryExpression();

      while (this.jj_2_1(Integer.MAX_VALUE)) {
         this.jj_consume_token(11);
         ASTExpNode jjtn001 = new ASTExpNode(7);
         boolean jjtc001 = true;
         this.jjtree.openNodeScope(jjtn001);

         try {
            this.ExpExpression();
         } catch (Throwable var7) {
            if (jjtc001) {
               this.jjtree.clearNodeScope(jjtn001);
               jjtc001 = false;
            } else {
               this.jjtree.popNode();
            }

            if (var7 instanceof RuntimeException) {
               throw (RuntimeException)var7;
            }

            if (var7 instanceof ParseException) {
               throw (ParseException)var7;
            }

            throw (Error)var7;
         } finally {
            if (jjtc001) {
               this.jjtree.closeNodeScope(jjtn001, 2);
            }
         }
      }
   }

   public final void UnaryExpression() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 8:
            this.jj_consume_token(8);
            ASTNegativeNode jjtn001 = new ASTNegativeNode(8);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);

            try {
               this.Element();
               break;
            } catch (Throwable var7) {
               if (jjtc001) {
                  this.jjtree.clearNodeScope(jjtn001);
                  jjtc001 = false;
               } else {
                  this.jjtree.popNode();
               }

               if (var7 instanceof RuntimeException) {
                  throw (RuntimeException)var7;
               }

               if (var7 instanceof ParseException) {
                  throw (ParseException)var7;
               }

               throw (Error)var7;
            } finally {
               if (jjtc001) {
                  this.jjtree.closeNodeScope(jjtn001, true);
               }
            }
         case 9:
         case 10:
         case 11:
         case 16:
         case 17:
         case 18:
         case 20:
         case 21:
         case 22:
         case 24:
         case 25:
         case 26:
         default:
            this.jj_la1[5] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 12:
         case 13:
         case 14:
         case 15:
         case 19:
         case 23:
         case 27:
            this.Element();
      }
   }

   public final void Element() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 12:
            ASTXNode jjtn001 = new ASTXNode(9);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);

            try {
               this.jj_consume_token(12);
               break;
            } finally {
               if (jjtc001) {
                  this.jjtree.closeNodeScope(jjtn001, true);
               }
            }
         case 13:
            this.jj_consume_token(13);
            ASTConstNode jjtn002 = new ASTConstNode(10);
            boolean jjtc002 = true;
            this.jjtree.openNodeScope(jjtn002);

            try {
               this.jjtree.closeNodeScope(jjtn002, true);
               jjtc002 = false;
               jjtn002.setValue(Math.PI);
               break;
            } finally {
               if (jjtc002) {
                  this.jjtree.closeNodeScope(jjtn002, true);
               }
            }
         case 14:
            this.jj_consume_token(14);
            ASTConstNode jjtn003 = new ASTConstNode(10);
            boolean jjtc003 = true;
            this.jjtree.openNodeScope(jjtn003);

            try {
               this.jjtree.closeNodeScope(jjtn003, true);
               jjtc003 = false;
               jjtn003.setValue(Math.E);
               break;
            } finally {
               if (jjtc003) {
                  this.jjtree.closeNodeScope(jjtn003, true);
               }
            }
         case 15:
         case 19:
            this.Constant();
            break;
         case 16:
         case 17:
         case 18:
         case 20:
         case 21:
         case 22:
         case 24:
         case 25:
         case 26:
         default:
            this.jj_la1[6] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         case 23:
            this.Function();
            break;
         case 27:
            this.jj_consume_token(27);
            this.AdditiveExpression();
            this.jj_consume_token(28);
      }
   }

   public final void Function() throws ParseException {
      int argumentCount = 0;
      ASTFunctionNode jjtn001 = new ASTFunctionNode(11);
      boolean jjtc001 = true;
      this.jjtree.openNodeScope(jjtn001);

      try {
         String funcname;
         this.jj_consume_token(23);
         funcname = this.token.image;
         this.jj_consume_token(27);
         label96:
         switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
            case 8:
            case 12:
            case 13:
            case 14:
            case 15:
            case 19:
            case 23:
            case 27:
               this.AdditiveExpression();
               argumentCount++;

               while (true) {
                  switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
                     case 29:
                        this.jj_consume_token(29);
                        this.AdditiveExpression();
                        argumentCount++;
                        break;
                     default:
                        this.jj_la1[7] = this.jj_gen;
                        break label96;
                  }
               }
            case 9:
            case 10:
            case 11:
            case 16:
            case 17:
            case 18:
            case 20:
            case 21:
            case 22:
            case 24:
            case 25:
            case 26:
            default:
               this.jj_la1[8] = this.jj_gen;
         }

         this.jj_consume_token(28);
         this.jjtree.closeNodeScope(jjtn001, true);
         jjtc001 = false;
         jjtn001.setFunctionName(funcname);
         if (jjtn001.getFunction().getArgumentCount() != argumentCount) {
            throw new ParseException(
               "Wrong number of arguments (" + argumentCount + "/" + jjtn001.getFunction().getArgumentCount() + ")for function: '" + funcname + "'"
            );
         }
      } catch (Throwable var9) {
         if (jjtc001) {
            this.jjtree.clearNodeScope(jjtn001);
            jjtc001 = false;
         } else {
            this.jjtree.popNode();
         }

         if (var9 instanceof RuntimeException) {
            throw (RuntimeException)var9;
         }

         if (var9 instanceof ParseException) {
            throw (ParseException)var9;
         }

         throw (Error)var9;
      } finally {
         if (jjtc001) {
            this.jjtree.closeNodeScope(jjtn001, true);
         }
      }
   }

   public final void Constant() throws ParseException {
      switch (this.jj_ntk == -1 ? this.jj_ntk() : this.jj_ntk) {
         case 15: {
            Token t = this.jj_consume_token(15);
            ASTInteger jjtn001 = new ASTInteger(12);
            boolean jjtc001 = true;
            this.jjtree.openNodeScope(jjtn001);

            try {
               this.jjtree.closeNodeScope(jjtn001, true);
               jjtc001 = false;
               int value = Integer.parseInt(t.image);
               jjtn001.setValue(value);
               break;
            } finally {
               if (jjtc001) {
                  this.jjtree.closeNodeScope(jjtn001, true);
               }
            }
         }
         case 19: {
            Token t = this.jj_consume_token(19);
            ASTFloat jjtn002 = new ASTFloat(13);
            boolean jjtc002 = true;
            this.jjtree.openNodeScope(jjtn002);

            try {
               this.jjtree.closeNodeScope(jjtn002, true);
               jjtc002 = false;
               double value = Double.parseDouble(this.token.image);
               jjtn002.setValue(value);
               break;
            } finally {
               if (jjtc002) {
                  this.jjtree.closeNodeScope(jjtn002, true);
               }
            }
         }
         default: {
            this.jj_la1[9] = this.jj_gen;
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      }
   }

   private boolean jj_2_1(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_1();
      } catch (Calculus.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, xla);
      }

      return var3;
   }

   private boolean jj_3_1() {
      return this.jj_scan_token(11);
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[]{33554432, 384, 384, 67110400, 67110400, 143192320, 143192064, 536870912, 143192320, 557056};
   }

   public Calculus(InputStream stream) {
      this(stream, null);
   }

   public Calculus(InputStream stream, String encoding) {
      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new CalculusTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   public void ReInit(InputStream stream) {
      this.ReInit(stream, null);
   }

   public void ReInit(InputStream stream, String encoding) {
      try {
         this.jj_input_stream.ReInit(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   public Calculus(Reader stream) {
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new CalculusTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit(stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   public Calculus(CalculusTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   public void ReInit(CalculusTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jjtree.reset();
      this.jj_gen = 0;

      for (int i = 0; i < 10; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Calculus.JJCalls();
      }
   }

   private Token jj_consume_token(int kind) throws ParseException {
      Token oldToken = this.token;
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      if (this.token.kind != kind) {
         this.token = oldToken;
         this.jj_kind = kind;
         throw this.generateParseException();
      } else {
         this.jj_gen++;
         if (++this.jj_gc > 100) {
            this.jj_gc = 0;

            for (int i = 0; i < this.jj_2_rtns.length; i++) {
               for (Calculus.JJCalls c = this.jj_2_rtns[i]; c != null; c = c.next) {
                  if (c.gen < this.jj_gen) {
                     c.first = null;
                  }
               }
            }
         }

         return this.token;
      }
   }

   private boolean jj_scan_token(int kind) {
      if (this.jj_scanpos == this.jj_lastpos) {
         this.jj_la--;
         if (this.jj_scanpos.next == null) {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next = this.token_source.getNextToken();
         } else {
            this.jj_lastpos = this.jj_scanpos = this.jj_scanpos.next;
         }
      } else {
         this.jj_scanpos = this.jj_scanpos.next;
      }

      if (this.jj_rescan) {
         int i = 0;

         Token tok;
         for (tok = this.token; tok != null && tok != this.jj_scanpos; tok = tok.next) {
            i++;
         }

         if (tok != null) {
            this.jj_add_error_token(kind, i);
         }
      }

      if (this.jj_scanpos.kind != kind) {
         return true;
      } else if (this.jj_la == 0 && this.jj_scanpos == this.jj_lastpos) {
         throw this.jj_ls;
      } else {
         return false;
      }
   }

   public final Token getNextToken() {
      if (this.token.next != null) {
         this.token = this.token.next;
      } else {
         this.token = this.token.next = this.token_source.getNextToken();
      }

      this.jj_ntk = -1;
      this.jj_gen++;
      return this.token;
   }

   public final Token getToken(int index) {
      Token t = this.token;

      for (int i = 0; i < index; i++) {
         if (t.next != null) {
            t = t.next;
         } else {
            t = t.next = this.token_source.getNextToken();
         }
      }

      return t;
   }

   private int jj_ntk() {
      return (this.jj_nt = this.token.next) == null
         ? (this.jj_ntk = (this.token.next = this.token_source.getNextToken()).kind)
         : (this.jj_ntk = this.jj_nt.kind);
   }

   private void jj_add_error_token(int kind, int pos) {
      if (pos < 100) {
         if (pos == this.jj_endpos + 1) {
            this.jj_lasttokens[this.jj_endpos++] = kind;
         } else if (this.jj_endpos != 0) {
            this.jj_expentry = new int[this.jj_endpos];

             System.arraycopy(this.jj_lasttokens, 0, this.jj_expentry, 0, this.jj_endpos);

            label41:
            for (int[] oldentry : this.jj_expentries) {
               if (oldentry.length == this.jj_expentry.length) {
                  for (int i = 0; i < this.jj_expentry.length; i++) {
                     if (oldentry[i] != this.jj_expentry[i]) {
                        continue label41;
                     }
                  }

                  this.jj_expentries.add(this.jj_expentry);
                  break;
               }
            }

            if (pos != 0) {
               this.jj_lasttokens[(this.jj_endpos = pos) - 1] = kind;
            }
         }
      }
   }

   public ParseException generateParseException() {
      this.jj_expentries.clear();
      boolean[] la1tokens = new boolean[30];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      for (int i = 0; i < 10; i++) {
         if (this.jj_la1[i] == this.jj_gen) {
            for (int j = 0; j < 32; j++) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }
            }
         }
      }

      for (int ix = 0; ix < 30; ix++) {
         if (la1tokens[ix]) {
            this.jj_expentry = new int[1];
            this.jj_expentry[0] = ix;
            this.jj_expentries.add(this.jj_expentry);
         }
      }

      this.jj_endpos = 0;
      this.jj_rescan_token();
      this.jj_add_error_token(0, 0);
      int[][] exptokseq = new int[this.jj_expentries.size()][];

      for (int ixx = 0; ixx < this.jj_expentries.size(); ixx++) {
         exptokseq[ixx] = this.jj_expentries.get(ixx);
      }

      return new ParseException(this.token, exptokseq, tokenImage);
   }

   public final void enable_tracing() {
   }

   public final void disable_tracing() {
   }

   private void jj_rescan_token() {
      this.jj_rescan = true;

      for (int i = 0; i < 1; i++) {
         try {
            Calculus.JJCalls p = this.jj_2_rtns[i];

            while (true) {
               if (p.gen > this.jj_gen) {
                  this.jj_la = p.arg;
                  this.jj_lastpos = this.jj_scanpos = p.first;
                  switch (i) {
                     case 0:
                        this.jj_3_1();
                  }
               }

               p = p.next;
               if (p == null) {
                  break;
               }
            }
         } catch (Calculus.LookaheadSuccess var3) {
         }
      }

      this.jj_rescan = false;
   }

   private void jj_save(int index, int xla) {
      Calculus.JJCalls p;
      for (p = this.jj_2_rtns[index]; p.gen > this.jj_gen; p = p.next) {
         if (p.next == null) {
            p = p.next = new Calculus.JJCalls();
            break;
         }
      }

      p.gen = this.jj_gen + xla - this.jj_la;
      p.first = this.token;
      p.arg = xla;
   }

   static {
      jj_la1_init_0();
   }

   static final class JJCalls {
      int gen;
      Token first;
      int arg;
      Calculus.JJCalls next;
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }
   }
}
