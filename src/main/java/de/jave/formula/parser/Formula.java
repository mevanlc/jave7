package de.jave.formula.parser;

import de.jave.formula.algorithm.Formula2Algo;
import de.jave.formula.algorithm.Formula2Symbols;
import de.jave.formula.algorithm.FormulaCharField;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Formula implements FormulaConstants {
   public FormulaTokenManager token_source;
   SimpleCharStream jj_input_stream;
   public Token token;
   public Token jj_nt;
   private int jj_ntk;
   private Token jj_scanpos;
   private Token jj_lastpos;
   private int jj_la;
   private int jj_gen;
   private final int[] jj_la1 = new int[0];
   private static int[] jj_la1_0;
   private static int[] jj_la1_1;
   private final Formula.JJCalls[] jj_2_rtns = new Formula.JJCalls[79];
   private boolean jj_rescan = false;
   private int jj_gc = 0;
   private final Formula.LookaheadSuccess jj_ls = new Formula.LookaheadSuccess();
   private final List<int[]> jj_expentries = new ArrayList<>();
   private int[] jj_expentry;
   private int jj_kind = -1;
   private int[] jj_lasttokens = new int[100];
   private int jj_endpos;

   public static String toAscii(String code) throws ParseException {
      InputStream stream = new ByteArrayInputStream(code.getBytes());
      Formula myParserObj = new Formula(stream);

      try {
         return myParserObj.toAscii();
      } catch (TokenMgrError var4) {
         throw new ParseException();
      }
   }

   public final String toAscii() throws ParseException {
      FormulaCharField result = this.Start();
      return result.toString();
   }

   public final FormulaCharField Start() throws ParseException {
      return this.ExpressionSequence();
   }

   public final FormulaCharField ExpressionSequence() throws ParseException {
      Token x = null;
      Token y = null;

      FormulaCharField f1;
      for (f1 = this.Expression(); this.jj_2_1(4); x = null) {
         if (this.jj_2_6(4)) {
            if (this.jj_2_2(4)) {
               x = this.jj_consume_token(48);
            } else if (this.jj_2_3(4)) {
               x = this.jj_consume_token(49);
            } else if (this.jj_2_4(4)) {
               x = this.jj_consume_token(50);
            } else {
               if (!this.jj_2_5(4)) {
                  this.jj_consume_token(-1);
                  throw new ParseException();
               }

               x = this.jj_consume_token(6);
            }
         }

         FormulaCharField f2 = this.Expression();
         if (x == null) {
            f1 = Formula2Algo.seq(f1, f2, ' ', 0);
         } else if (x.toString().equals("..")) {
            f1 = Formula2Algo.seq(f1, f2, "..", 0);
         } else if (x.toString().equals(",")) {
            f1 = Formula2Algo.seq(f1, f2, ',', 0);
         } else if (x.toString().equals("|")) {
            f1 = Formula2Algo.seq(f1, f2, '|', 0);
         } else if (x.toString().equals(";")) {
            f1 = Formula2Algo.seq(f1, f2, ';', 0);
         }
      }

      if (this.jj_2_7(4)) {
         y = this.jj_consume_token(50);
      }

      if (y != null) {
         f1 = Formula2Algo.append(f1, ';');
      }

      return f1;
   }

   public final FormulaCharField Expression() throws ParseException {
      return this.ComparativeExpression();
   }

   public final FormulaCharField ComparativeExpression() throws ParseException {
      Token x = null;
      FormulaCharField f1 = this.AdditiveExpression();

      while (this.jj_2_8(4)) {
         if (this.jj_2_9(4)) {
            x = this.jj_consume_token(11);
         } else if (this.jj_2_10(4)) {
            x = this.jj_consume_token(51);
         } else if (this.jj_2_11(4)) {
            x = this.jj_consume_token(52);
         } else {
            if (!this.jj_2_12(4)) {
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            x = this.jj_consume_token(53);
         }

         FormulaCharField f2 = this.AdditiveExpression();
         if (x.toString().equals("->")) {
            f1 = Formula2Algo.seq(f1, f2, x.toString(), 0);
         } else {
            f1 = Formula2Algo.seq(f1, f2, x.toString(), 1);
         }
      }

      return f1;
   }

   public final FormulaCharField AdditiveExpression() throws ParseException {
      Token x = null;
      FormulaCharField f1 = this.MultiplicativeExpression();

      while (this.jj_2_13(4)) {
         if (this.jj_2_14(4)) {
            x = this.jj_consume_token(4);
         } else {
            if (!this.jj_2_15(4)) {
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            x = this.jj_consume_token(5);
         }

         FormulaCharField f2 = this.MultiplicativeExpression();
         if (x.kind == 4) {
            f1 = Formula2Algo.seq(f1, f2, '+', 1);
         } else {
            f1 = Formula2Algo.seq(f1, f2, '-', 1);
         }
      }

      return f1;
   }

   public final FormulaCharField MultiplicativeExpression() throws ParseException {
      Token x = null;
      FormulaCharField f1 = this.ExpExpression();

      while (this.jj_2_16(4)) {
         if (this.jj_2_17(4)) {
            x = this.jj_consume_token(7);
         } else {
            if (!this.jj_2_18(4)) {
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            x = this.jj_consume_token(8);
         }

         FormulaCharField f2 = this.ExpExpression();
         if (x.kind == 7) {
            f1 = Formula2Algo.seq(f1, f2, '*', 1);
         } else {
            f1 = Formula2Algo.fraction(f1, f2);
         }
      }

      return f1;
   }

   public final FormulaCharField ExpExpression() throws ParseException {
      Token x = null;
      FormulaCharField f1 = this.UnaryExpression();

      while (this.jj_2_19(4)) {
         if (this.jj_2_20(4)) {
            x = this.jj_consume_token(9);
         } else {
            if (!this.jj_2_21(4)) {
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            x = this.jj_consume_token(10);
         }

         FormulaCharField f2 = this.UnaryExpression();
         if (x.kind == 9) {
            f1 = Formula2Algo.sub(f1, f2);
         } else {
            f1 = Formula2Algo.sup(f1, f2);
         }
      }

      return f1;
   }

   public final FormulaCharField UnaryExpression() throws ParseException {
      if (this.jj_2_22(4)) {
         this.jj_consume_token(5);
         FormulaCharField f = this.Element();
         return Formula2Algo.negative(f);
      } else if (this.jj_2_23(4)) {
         this.jj_consume_token(22);
         FormulaCharField f = this.Element();
         return Formula2Algo.not(f);
      } else if (this.jj_2_24(4)) {
         return this.Element();
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField Element() throws ParseException {
      Token t = null;

      while (this.jj_2_25(4)) {
         this.jj_consume_token(24);
      }

      FormulaCharField f;
      if (this.jj_2_26(4)) {
         f = this.Function();
      } else if (this.jj_2_27(4)) {
         f = this.Bracket();
      } else {
         if (!this.jj_2_28(4)) {
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         f = this.Identifier();
      }

      if (this.jj_2_29(4)) {
         t = this.jj_consume_token(54);
      }

      while (this.jj_2_30(4)) {
         this.jj_consume_token(24);
      }

      return t != null ? Formula2Algo.faculty(f) : f;
   }

   public final FormulaCharField Identifier() throws ParseException {
      if (this.jj_2_31(4)) {
         Token t = this.jj_consume_token(40);
         FormulaCharField symbol = Formula2Symbols.getInstance().getSymbol(t.toString());
         return symbol != null ? symbol : new FormulaCharField(t.toString());
      } else if (this.jj_2_32(4)) {
         Token t = this.jj_consume_token(38);
         String s = t.toString();
         return new FormulaCharField(s.substring(1, s.length() - 1));
      } else if (this.jj_2_33(4)) {
         Token t = this.jj_consume_token(39);
         return new FormulaCharField(t.toString());
      } else if (this.jj_2_34(4)) {
         Token t = this.jj_consume_token(43);
         return new FormulaCharField(t.toString());
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField Function() throws ParseException {
      if (this.jj_2_35(4)) {
         return this.SqrtFunction();
      } else if (this.jj_2_36(4)) {
         return this.VecFunction();
      } else if (this.jj_2_37(4)) {
         return this.SumFunction();
      } else if (this.jj_2_38(4)) {
         return this.IntegralFunction();
      } else if (this.jj_2_39(4)) {
         return this.ProdFunction();
      } else if (this.jj_2_40(4)) {
         return this.LimFunction();
      } else if (this.jj_2_41(4)) {
         return this.LogFunction();
      } else if (this.jj_2_42(4)) {
         return this.FloorFunction();
      } else if (this.jj_2_43(4)) {
         return this.CeilFunction();
      } else if (this.jj_2_44(4)) {
         return this.BinomialFunction();
      } else if (this.jj_2_45(4)) {
         return this.MatrixFunction();
      } else if (this.jj_2_46(4)) {
         return this.UnknownFunction();
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField UnknownFunction() throws ParseException {
      Token t = null;
      t = this.jj_consume_token(40);
      this.jj_consume_token(55);
      FormulaCharField f = this.ExpressionSequence();
      this.jj_consume_token(56);
      return Formula2Algo.function(t.toString(), f);
   }

   public final FormulaCharField SqrtFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      this.jj_consume_token(25);
      this.jj_consume_token(55);
      f1 = this.Expression();
      if (this.jj_2_49(4)) {
         if (this.jj_2_47(4)) {
            this.jj_consume_token(50);
         } else {
            if (!this.jj_2_48(4)) {
               this.jj_consume_token(-1);
               throw new ParseException();
            }

            this.jj_consume_token(48);
         }

         f2 = this.Expression();
      }

      this.jj_consume_token(56);
      return f2 == null ? Formula2Algo.sqrt(f1) : Formula2Algo.sqrt(f2, f1);
   }

   public final FormulaCharField SumFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      FormulaCharField f3 = null;
      this.jj_consume_token(26);
      this.jj_consume_token(55);
      f1 = this.Expression();
      if (this.jj_2_52(4)) {
         this.jj_consume_token(56);
         return Formula2Algo.sum(f1);
      } else if (this.jj_2_53(4)) {
         this.jj_consume_token(6);
         f2 = this.Expression();
         this.jj_consume_token(50);
         f3 = this.Expression();
         this.jj_consume_token(56);
         return Formula2Algo.sum(f3, f1, f2);
      } else if (this.jj_2_54(4)) {
         this.jj_consume_token(50);
         f2 = this.Expression();
         if (this.jj_2_50(4)) {
            this.jj_consume_token(56);
            return Formula2Algo.sum(f2, f1);
         } else if (this.jj_2_51(4)) {
            this.jj_consume_token(50);
            f3 = this.Expression();
            this.jj_consume_token(56);
            return Formula2Algo.sum(f3, f1, f2);
         } else {
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField IntegralFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      FormulaCharField f3 = null;
      FormulaCharField f4 = null;
      this.jj_consume_token(28);
      this.jj_consume_token(55);
      f1 = this.Expression();
      if (this.jj_2_59(4)) {
         this.jj_consume_token(56);
         return Formula2Algo.integral(f1);
      } else if (this.jj_2_60(4)) {
         this.jj_consume_token(50);
         f2 = this.AdditiveExpression();
         if (this.jj_2_57(4)) {
            this.jj_consume_token(56);
            return Formula2Algo.integral(f1, f2);
         } else if (this.jj_2_58(4)) {
            this.jj_consume_token(51);
            f3 = this.Expression();
            if (this.jj_2_55(4)) {
               this.jj_consume_token(56);
               return Formula2Algo.integral(f1, f2, f3);
            } else if (this.jj_2_56(4)) {
               this.jj_consume_token(6);
               f4 = this.Expression();
               this.jj_consume_token(56);
               return Formula2Algo.integral(f1, f2, f3, f4);
            } else {
               this.jj_consume_token(-1);
               throw new ParseException();
            }
         } else {
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField ProdFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      FormulaCharField f3 = null;
      this.jj_consume_token(27);
      this.jj_consume_token(55);
      f1 = this.Expression();
      if (this.jj_2_63(4)) {
         this.jj_consume_token(56);
         return Formula2Algo.prod(f1);
      } else if (this.jj_2_64(4)) {
         this.jj_consume_token(6);
         f2 = this.Expression();
         this.jj_consume_token(50);
         f3 = this.Expression();
         this.jj_consume_token(56);
         return Formula2Algo.prod(f3, f1, f2);
      } else if (this.jj_2_65(4)) {
         this.jj_consume_token(50);
         f2 = this.Expression();
         if (this.jj_2_61(4)) {
            this.jj_consume_token(56);
            return Formula2Algo.prod(f2, f1);
         } else if (this.jj_2_62(4)) {
            this.jj_consume_token(50);
            f3 = this.Expression();
            this.jj_consume_token(56);
            return Formula2Algo.prod(f3, f1, f2);
         } else {
            this.jj_consume_token(-1);
            throw new ParseException();
         }
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField MatrixFunction() throws ParseException {
      FormulaCharField f = null;
      List<FormulaCharField> v = new ArrayList<>();
      Token t1 = null;
      Token t2 = null;
      this.jj_consume_token(32);
      this.jj_consume_token(55);
      t1 = this.jj_consume_token(39);
      if (this.jj_2_66(4)) {
         this.jj_consume_token(48);
      } else {
         if (!this.jj_2_67(4)) {
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         this.jj_consume_token(50);
      }

      t2 = this.jj_consume_token(39);
      if (this.jj_2_68(4)) {
         this.jj_consume_token(48);
      } else {
         if (!this.jj_2_69(4)) {
            this.jj_consume_token(-1);
            throw new ParseException();
         }

         this.jj_consume_token(50);
      }

      f = this.Expression();
      v.add(f);

      while (this.jj_2_70(4)) {
         this.jj_consume_token(50);
         f = this.Expression();
         v.add(f);
      }

      this.jj_consume_token(56);
      int rows = Integer.parseInt(t1.toString());
      int cols = Integer.parseInt(t2.toString());
      return Formula2Algo.matrix(rows, cols, v);
   }

   public final FormulaCharField LimFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      this.jj_consume_token(29);
      this.jj_consume_token(55);
      f1 = this.Expression();
      this.jj_consume_token(50);
      f2 = this.Expression();
      this.jj_consume_token(56);
      return Formula2Algo.lim(f1, f2);
   }

   public final FormulaCharField BinomialFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      this.jj_consume_token(33);
      this.jj_consume_token(55);
      f1 = this.Expression();
      this.jj_consume_token(50);
      f2 = this.Expression();
      this.jj_consume_token(56);
      return Formula2Algo.binomial(f1, f2);
   }

   public final FormulaCharField FloorFunction() throws ParseException {
      FormulaCharField f = null;
      this.jj_consume_token(34);
      this.jj_consume_token(55);
      f = this.ExpressionSequence();
      this.jj_consume_token(56);
      return Formula2Algo.floor(f);
   }

   public final FormulaCharField CeilFunction() throws ParseException {
      FormulaCharField f = null;
      this.jj_consume_token(35);
      this.jj_consume_token(55);
      f = this.ExpressionSequence();
      this.jj_consume_token(56);
      return Formula2Algo.ceil(f);
   }

   public final FormulaCharField LogFunction() throws ParseException {
      FormulaCharField f1 = null;
      FormulaCharField f2 = null;
      this.jj_consume_token(31);
      this.jj_consume_token(55);
      f1 = this.Expression();
      this.jj_consume_token(50);
      f2 = this.Expression();
      this.jj_consume_token(56);
      return Formula2Algo.log(f1, f2);
   }

   public final FormulaCharField VecFunction() throws ParseException {
      FormulaCharField f = null;
      if (this.jj_2_71(4)) {
         this.jj_consume_token(30);
         this.jj_consume_token(55);
         f = this.ExpressionSequence();
         this.jj_consume_token(56);
         return Formula2Algo.vector(f);
      } else if (this.jj_2_72(4)) {
         this.jj_consume_token(37);
         this.jj_consume_token(55);
         f = this.ExpressionSequence();
         this.jj_consume_token(56);
         return Formula2Algo.underline(f);
      } else if (this.jj_2_73(4)) {
         this.jj_consume_token(36);
         this.jj_consume_token(55);
         f = this.ExpressionSequence();
         this.jj_consume_token(56);
         return Formula2Algo.overline(f);
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   public final FormulaCharField Bracket() throws ParseException {
      if (this.jj_2_74(4)) {
         this.jj_consume_token(55);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(56);
         return Formula2Algo.roundBrackets(f);
      } else if (this.jj_2_75(4)) {
         this.jj_consume_token(52);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(53);
         return Formula2Algo.spitzBrackets(f);
      } else if (this.jj_2_76(4)) {
         this.jj_consume_token(57);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(58);
         return Formula2Algo.eckigBrackets(f);
      } else if (this.jj_2_77(4)) {
         this.jj_consume_token(59);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(59);
         return Formula2Algo.straightDoubleBrackets(f);
      } else if (this.jj_2_78(4)) {
         this.jj_consume_token(49);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(49);
         return Formula2Algo.straightBrackets(f);
      } else if (this.jj_2_79(4)) {
         this.jj_consume_token(60);
         FormulaCharField f = this.ExpressionSequence();
         this.jj_consume_token(61);
         return f;
      } else {
         this.jj_consume_token(-1);
         throw new ParseException();
      }
   }

   private boolean jj_2_1(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_1();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(0, xla);
      }

      return var3;
   }

   private boolean jj_2_2(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_2();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(1, xla);
      }

      return var3;
   }

   private boolean jj_2_3(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_3();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(2, xla);
      }

      return var3;
   }

   private boolean jj_2_4(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_4();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(3, xla);
      }

      return var3;
   }

   private boolean jj_2_5(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_5();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(4, xla);
      }

      return var3;
   }

   private boolean jj_2_6(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_6();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(5, xla);
      }

      return var3;
   }

   private boolean jj_2_7(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_7();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(6, xla);
      }

      return var3;
   }

   private boolean jj_2_8(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_8();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(7, xla);
      }

      return var3;
   }

   private boolean jj_2_9(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_9();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(8, xla);
      }

      return var3;
   }

   private boolean jj_2_10(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_10();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(9, xla);
      }

      return var3;
   }

   private boolean jj_2_11(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_11();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(10, xla);
      }

      return var3;
   }

   private boolean jj_2_12(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_12();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(11, xla);
      }

      return var3;
   }

   private boolean jj_2_13(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_13();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(12, xla);
      }

      return var3;
   }

   private boolean jj_2_14(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_14();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(13, xla);
      }

      return var3;
   }

   private boolean jj_2_15(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_15();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(14, xla);
      }

      return var3;
   }

   private boolean jj_2_16(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_16();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(15, xla);
      }

      return var3;
   }

   private boolean jj_2_17(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_17();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(16, xla);
      }

      return var3;
   }

   private boolean jj_2_18(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_18();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(17, xla);
      }

      return var3;
   }

   private boolean jj_2_19(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_19();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(18, xla);
      }

      return var3;
   }

   private boolean jj_2_20(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_20();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(19, xla);
      }

      return var3;
   }

   private boolean jj_2_21(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_21();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(20, xla);
      }

      return var3;
   }

   private boolean jj_2_22(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_22();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(21, xla);
      }

      return var3;
   }

   private boolean jj_2_23(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_23();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(22, xla);
      }

      return var3;
   }

   private boolean jj_2_24(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_24();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(23, xla);
      }

      return var3;
   }

   private boolean jj_2_25(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_25();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(24, xla);
      }

      return var3;
   }

   private boolean jj_2_26(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_26();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(25, xla);
      }

      return var3;
   }

   private boolean jj_2_27(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_27();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(26, xla);
      }

      return var3;
   }

   private boolean jj_2_28(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_28();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(27, xla);
      }

      return var3;
   }

   private boolean jj_2_29(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_29();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(28, xla);
      }

      return var3;
   }

   private boolean jj_2_30(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_30();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(29, xla);
      }

      return var3;
   }

   private boolean jj_2_31(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_31();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(30, xla);
      }

      return var3;
   }

   private boolean jj_2_32(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_32();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(31, xla);
      }

      return var3;
   }

   private boolean jj_2_33(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_33();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(32, xla);
      }

      return var3;
   }

   private boolean jj_2_34(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_34();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(33, xla);
      }

      return var3;
   }

   private boolean jj_2_35(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_35();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(34, xla);
      }

      return var3;
   }

   private boolean jj_2_36(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_36();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(35, xla);
      }

      return var3;
   }

   private boolean jj_2_37(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_37();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(36, xla);
      }

      return var3;
   }

   private boolean jj_2_38(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_38();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(37, xla);
      }

      return var3;
   }

   private boolean jj_2_39(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_39();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(38, xla);
      }

      return var3;
   }

   private boolean jj_2_40(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_40();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(39, xla);
      }

      return var3;
   }

   private boolean jj_2_41(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_41();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(40, xla);
      }

      return var3;
   }

   private boolean jj_2_42(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_42();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(41, xla);
      }

      return var3;
   }

   private boolean jj_2_43(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_43();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(42, xla);
      }

      return var3;
   }

   private boolean jj_2_44(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_44();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(43, xla);
      }

      return var3;
   }

   private boolean jj_2_45(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_45();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(44, xla);
      }

      return var3;
   }

   private boolean jj_2_46(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_46();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(45, xla);
      }

      return var3;
   }

   private boolean jj_2_47(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_47();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(46, xla);
      }

      return var3;
   }

   private boolean jj_2_48(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_48();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(47, xla);
      }

      return var3;
   }

   private boolean jj_2_49(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_49();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(48, xla);
      }

      return var3;
   }

   private boolean jj_2_50(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_50();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(49, xla);
      }

      return var3;
   }

   private boolean jj_2_51(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_51();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(50, xla);
      }

      return var3;
   }

   private boolean jj_2_52(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_52();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(51, xla);
      }

      return var3;
   }

   private boolean jj_2_53(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_53();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(52, xla);
      }

      return var3;
   }

   private boolean jj_2_54(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_54();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(53, xla);
      }

      return var3;
   }

   private boolean jj_2_55(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_55();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(54, xla);
      }

      return var3;
   }

   private boolean jj_2_56(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_56();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(55, xla);
      }

      return var3;
   }

   private boolean jj_2_57(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_57();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(56, xla);
      }

      return var3;
   }

   private boolean jj_2_58(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_58();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(57, xla);
      }

      return var3;
   }

   private boolean jj_2_59(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_59();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(58, xla);
      }

      return var3;
   }

   private boolean jj_2_60(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_60();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(59, xla);
      }

      return var3;
   }

   private boolean jj_2_61(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_61();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(60, xla);
      }

      return var3;
   }

   private boolean jj_2_62(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_62();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(61, xla);
      }

      return var3;
   }

   private boolean jj_2_63(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_63();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(62, xla);
      }

      return var3;
   }

   private boolean jj_2_64(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_64();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(63, xla);
      }

      return var3;
   }

   private boolean jj_2_65(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_65();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(64, xla);
      }

      return var3;
   }

   private boolean jj_2_66(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_66();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(65, xla);
      }

      return var3;
   }

   private boolean jj_2_67(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_67();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(66, xla);
      }

      return var3;
   }

   private boolean jj_2_68(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_68();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(67, xla);
      }

      return var3;
   }

   private boolean jj_2_69(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_69();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(68, xla);
      }

      return var3;
   }

   private boolean jj_2_70(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_70();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(69, xla);
      }

      return var3;
   }

   private boolean jj_2_71(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_71();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(70, xla);
      }

      return var3;
   }

   private boolean jj_2_72(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_72();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(71, xla);
      }

      return var3;
   }

   private boolean jj_2_73(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_73();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(72, xla);
      }

      return var3;
   }

   private boolean jj_2_74(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_74();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(73, xla);
      }

      return var3;
   }

   private boolean jj_2_75(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_75();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(74, xla);
      }

      return var3;
   }

   private boolean jj_2_76(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_76();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(75, xla);
      }

      return var3;
   }

   private boolean jj_2_77(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_77();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(76, xla);
      }

      return var3;
   }

   private boolean jj_2_78(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_78();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(77, xla);
      }

      return var3;
   }

   private boolean jj_2_79(int xla) {
      this.jj_la = xla;
      this.jj_lastpos = this.jj_scanpos = this.token;

      boolean var3;
      try {
         return !this.jj_3_79();
      } catch (Formula.LookaheadSuccess var7) {
         var3 = true;
      } finally {
         this.jj_save(78, xla);
      }

      return var3;
   }

   private boolean jj_3_79() {
      if (this.jj_scan_token(60)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(61);
      }
   }

   private boolean jj_3_78() {
      if (this.jj_scan_token(49)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(49);
      }
   }

   private boolean jj_3_77() {
      if (this.jj_scan_token(59)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(59);
      }
   }

   private boolean jj_3_76() {
      if (this.jj_scan_token(57)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(58);
      }
   }

   private boolean jj_3_75() {
      if (this.jj_scan_token(52)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(53);
      }
   }

   private boolean jj_3R_16() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_74()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_75()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_76()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_77()) {
                  this.jj_scanpos = xsp;
                  if (this.jj_3_78()) {
                     this.jj_scanpos = xsp;
                     if (this.jj_3_79()) {
                        return true;
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3_74() {
      if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_6() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_2()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_3()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_4()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_5()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3_2() {
      return this.jj_scan_token(48);
   }

   private boolean jj_3_1() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_6()) {
         this.jj_scanpos = xsp;
      }

      return this.jj_3R_9();
   }

   private boolean jj_3_34() {
      return this.jj_scan_token(43);
   }

   private boolean jj_3_27() {
      return this.jj_3R_16();
   }

   private boolean jj_3_33() {
      return this.jj_scan_token(39);
   }

   private boolean jj_3R_30() {
      if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while (!this.jj_3_1());

         this.jj_scanpos = xsp;
         xsp = this.jj_scanpos;
         if (this.jj_3_7()) {
            this.jj_scanpos = xsp;
         }

         return false;
      }
   }

   private boolean jj_3_56() {
      if (this.jj_scan_token(6)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_32() {
      return this.jj_scan_token(38);
   }

   private boolean jj_3_73() {
      if (this.jj_scan_token(36)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_72() {
      if (this.jj_scan_token(37)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_55() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3R_19() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_71()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_72()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_73()) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean jj_3R_17() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_31()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_32()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_33()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_34()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3_31() {
      return this.jj_scan_token(40);
   }

   private boolean jj_3_26() {
      return this.jj_3R_15();
   }

   private boolean jj_3_71() {
      if (this.jj_scan_token(30)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_58() {
      if (this.jj_scan_token(51)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_55()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_56()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3_57() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3_60() {
      if (this.jj_scan_token(50)) {
         return true;
      } else if (this.jj_3R_10()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_57()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_58()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3R_24() {
      if (this.jj_scan_token(31)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(50);
      }
   }

   private boolean jj_3_25() {
      return this.jj_scan_token(24);
   }

   private boolean jj_3R_14() {
      Token xsp;
      do {
         xsp = this.jj_scanpos;
      } while (!this.jj_3_25());

      this.jj_scanpos = xsp;
      xsp = this.jj_scanpos;
      if (this.jj_3_26()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_27()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_28()) {
               return true;
            }
         }
      }

      xsp = this.jj_scanpos;
      if (this.jj_3_29()) {
         this.jj_scanpos = xsp;
      }

      do {
         xsp = this.jj_scanpos;
      } while (!this.jj_3_30());

      this.jj_scanpos = xsp;
      return false;
   }

   private boolean jj_3_59() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3R_21() {
      if (this.jj_scan_token(28)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_59()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_60()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3_24() {
      return this.jj_3R_14();
   }

   private boolean jj_3_23() {
      return this.jj_scan_token(22) ? true : this.jj_3R_14();
   }

   private boolean jj_3R_13() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_22()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_23()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_24()) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean jj_3_22() {
      return this.jj_scan_token(5) ? true : this.jj_3R_14();
   }

   private boolean jj_3_21() {
      return this.jj_scan_token(10);
   }

   private boolean jj_3_69() {
      return this.jj_scan_token(50);
   }

   private boolean jj_3R_26() {
      if (this.jj_scan_token(35)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_51() {
      if (this.jj_scan_token(50)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_68() {
      return this.jj_scan_token(48);
   }

   private boolean jj_3_50() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3_20() {
      return this.jj_scan_token(9);
   }

   private boolean jj_3_54() {
      if (this.jj_scan_token(50)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_50()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_51()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3_19() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_20()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_21()) {
            return true;
         }
      }

      return this.jj_3R_13();
   }

   private boolean jj_3R_25() {
      if (this.jj_scan_token(34)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3R_12() {
      if (this.jj_3R_13()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while (!this.jj_3_19());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private boolean jj_3_53() {
      if (this.jj_scan_token(6)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         return this.jj_scan_token(50) ? true : this.jj_3R_9();
      }
   }

   private boolean jj_3_18() {
      return this.jj_scan_token(8);
   }

   private boolean jj_3_52() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3R_20() {
      if (this.jj_scan_token(26)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_52()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_53()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_54()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean jj_3R_27() {
      if (this.jj_scan_token(33)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(50);
      }
   }

   private boolean jj_3_67() {
      return this.jj_scan_token(50);
   }

   private boolean jj_3_17() {
      return this.jj_scan_token(7);
   }

   private boolean jj_3_48() {
      return this.jj_scan_token(48);
   }

   private boolean jj_3_16() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_17()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_18()) {
            return true;
         }
      }

      return this.jj_3R_12();
   }

   private boolean jj_3_66() {
      return this.jj_scan_token(48);
   }

   private boolean jj_3R_11() {
      if (this.jj_3R_12()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while (!this.jj_3_16());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private boolean jj_3_47() {
      return this.jj_scan_token(50);
   }

   private boolean jj_3_49() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_47()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_48()) {
            return true;
         }
      }

      return this.jj_3R_9();
   }

   private boolean jj_3_12() {
      return this.jj_scan_token(53);
   }

   private boolean jj_3R_23() {
      if (this.jj_scan_token(29)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(50);
      }
   }

   private boolean jj_3_15() {
      return this.jj_scan_token(5);
   }

   private boolean jj_3_11() {
      return this.jj_scan_token(52);
   }

   private boolean jj_3_14() {
      return this.jj_scan_token(4);
   }

   private boolean jj_3_13() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_14()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_15()) {
            return true;
         }
      }

      return this.jj_3R_11();
   }

   private boolean jj_3R_10() {
      if (this.jj_3R_11()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while (!this.jj_3_13());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private boolean jj_3_10() {
      return this.jj_scan_token(51);
   }

   private boolean jj_3_70() {
      return this.jj_scan_token(50) ? true : this.jj_3R_9();
   }

   private boolean jj_3R_18() {
      if (this.jj_scan_token(25)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_49()) {
            this.jj_scanpos = xsp;
         }

         return this.jj_scan_token(56);
      }
   }

   private boolean jj_3R_28() {
      if (this.jj_scan_token(32)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else if (this.jj_scan_token(39)) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_66()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_67()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3_9() {
      return this.jj_scan_token(11);
   }

   private boolean jj_3_8() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_9()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_10()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_11()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_12()) {
                  return true;
               }
            }
         }
      }

      return this.jj_3R_10();
   }

   private boolean jj_3_30() {
      return this.jj_scan_token(24);
   }

   private boolean jj_3R_31() {
      if (this.jj_3R_10()) {
         return true;
      } else {
         Token xsp;
         do {
            xsp = this.jj_scanpos;
         } while (!this.jj_3_8());

         this.jj_scanpos = xsp;
         return false;
      }
   }

   private boolean jj_3R_29() {
      if (this.jj_scan_token(40)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else {
         return this.jj_3R_30() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_29() {
      return this.jj_scan_token(54);
   }

   private boolean jj_3_62() {
      if (this.jj_scan_token(50)) {
         return true;
      } else {
         return this.jj_3R_9() ? true : this.jj_scan_token(56);
      }
   }

   private boolean jj_3_46() {
      return this.jj_3R_29();
   }

   private boolean jj_3_61() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3R_9() {
      return this.jj_3R_31();
   }

   private boolean jj_3_45() {
      return this.jj_3R_28();
   }

   private boolean jj_3_44() {
      return this.jj_3R_27();
   }

   private boolean jj_3_43() {
      return this.jj_3R_26();
   }

   private boolean jj_3_42() {
      return this.jj_3R_25();
   }

   private boolean jj_3_65() {
      if (this.jj_scan_token(50)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_61()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_62()) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean jj_3_28() {
      return this.jj_3R_17();
   }

   private boolean jj_3_41() {
      return this.jj_3R_24();
   }

   private boolean jj_3_40() {
      return this.jj_3R_23();
   }

   private boolean jj_3_39() {
      return this.jj_3R_22();
   }

   private boolean jj_3_38() {
      return this.jj_3R_21();
   }

   private boolean jj_3_37() {
      return this.jj_3R_20();
   }

   private boolean jj_3_5() {
      return this.jj_scan_token(6);
   }

   private boolean jj_3_36() {
      return this.jj_3R_19();
   }

   private boolean jj_3R_15() {
      Token xsp = this.jj_scanpos;
      if (this.jj_3_35()) {
         this.jj_scanpos = xsp;
         if (this.jj_3_36()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_37()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_38()) {
                  this.jj_scanpos = xsp;
                  if (this.jj_3_39()) {
                     this.jj_scanpos = xsp;
                     if (this.jj_3_40()) {
                        this.jj_scanpos = xsp;
                        if (this.jj_3_41()) {
                           this.jj_scanpos = xsp;
                           if (this.jj_3_42()) {
                              this.jj_scanpos = xsp;
                              if (this.jj_3_43()) {
                                 this.jj_scanpos = xsp;
                                 if (this.jj_3_44()) {
                                    this.jj_scanpos = xsp;
                                    if (this.jj_3_45()) {
                                       this.jj_scanpos = xsp;
                                       if (this.jj_3_46()) {
                                          return true;
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }

      return false;
   }

   private boolean jj_3_35() {
      return this.jj_3R_18();
   }

   private boolean jj_3_64() {
      if (this.jj_scan_token(6)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         return this.jj_scan_token(50) ? true : this.jj_3R_9();
      }
   }

   private boolean jj_3_7() {
      return this.jj_scan_token(50);
   }

   private boolean jj_3_4() {
      return this.jj_scan_token(50);
   }

   private boolean jj_3_63() {
      return this.jj_scan_token(56);
   }

   private boolean jj_3R_22() {
      if (this.jj_scan_token(27)) {
         return true;
      } else if (this.jj_scan_token(55)) {
         return true;
      } else if (this.jj_3R_9()) {
         return true;
      } else {
         Token xsp = this.jj_scanpos;
         if (this.jj_3_63()) {
            this.jj_scanpos = xsp;
            if (this.jj_3_64()) {
               this.jj_scanpos = xsp;
               if (this.jj_3_65()) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   private boolean jj_3_3() {
      return this.jj_scan_token(49);
   }

   private static void jj_la1_init_0() {
      jj_la1_0 = new int[0];
   }

   private static void jj_la1_init_1() {
      jj_la1_1 = new int[0];
   }

   public Formula(InputStream stream) {
      this(stream, null);
   }

   public Formula(InputStream stream, String encoding) {
      try {
         this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
      } catch (UnsupportedEncodingException var4) {
         throw new RuntimeException(var4);
      }

      this.token_source = new FormulaTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
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
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
      }
   }

   public Formula(Reader stream) {
      this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
      this.token_source = new FormulaTokenManager(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
      }
   }

   public void ReInit(Reader stream) {
      this.jj_input_stream.ReInit(stream, 1, 1);
      this.token_source.ReInit(this.jj_input_stream);
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
      }
   }

   public Formula(FormulaTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
      }
   }

   public void ReInit(FormulaTokenManager tm) {
      this.token_source = tm;
      this.token = new Token();
      this.jj_ntk = -1;
      this.jj_gen = 0;

      for (int i = 0; i < 0; i++) {
         this.jj_la1[i] = -1;
      }

      for (int i = 0; i < this.jj_2_rtns.length; i++) {
         this.jj_2_rtns[i] = new Formula.JJCalls();
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
               for (Formula.JJCalls c = this.jj_2_rtns[i]; c != null; c = c.next) {
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
      boolean[] la1tokens = new boolean[62];
      if (this.jj_kind >= 0) {
         la1tokens[this.jj_kind] = true;
         this.jj_kind = -1;
      }

      for (int i = 0; i < 0; i++) {
         if (this.jj_la1[i] == this.jj_gen) {
            for (int j = 0; j < 32; j++) {
               if ((jj_la1_0[i] & 1 << j) != 0) {
                  la1tokens[j] = true;
               }

               if ((jj_la1_1[i] & 1 << j) != 0) {
                  la1tokens[32 + j] = true;
               }
            }
         }
      }

      for (int ix = 0; ix < 62; ix++) {
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

      for (int i = 0; i < 79; i++) {
         try {
            Formula.JJCalls p = this.jj_2_rtns[i];

            while (true) {
               if (p.gen > this.jj_gen) {
                  this.jj_la = p.arg;
                  this.jj_lastpos = this.jj_scanpos = p.first;
                  switch (i) {
                     case 0:
                        this.jj_3_1();
                        break;
                     case 1:
                        this.jj_3_2();
                        break;
                     case 2:
                        this.jj_3_3();
                        break;
                     case 3:
                        this.jj_3_4();
                        break;
                     case 4:
                        this.jj_3_5();
                        break;
                     case 5:
                        this.jj_3_6();
                        break;
                     case 6:
                        this.jj_3_7();
                        break;
                     case 7:
                        this.jj_3_8();
                        break;
                     case 8:
                        this.jj_3_9();
                        break;
                     case 9:
                        this.jj_3_10();
                        break;
                     case 10:
                        this.jj_3_11();
                        break;
                     case 11:
                        this.jj_3_12();
                        break;
                     case 12:
                        this.jj_3_13();
                        break;
                     case 13:
                        this.jj_3_14();
                        break;
                     case 14:
                        this.jj_3_15();
                        break;
                     case 15:
                        this.jj_3_16();
                        break;
                     case 16:
                        this.jj_3_17();
                        break;
                     case 17:
                        this.jj_3_18();
                        break;
                     case 18:
                        this.jj_3_19();
                        break;
                     case 19:
                        this.jj_3_20();
                        break;
                     case 20:
                        this.jj_3_21();
                        break;
                     case 21:
                        this.jj_3_22();
                        break;
                     case 22:
                        this.jj_3_23();
                        break;
                     case 23:
                        this.jj_3_24();
                        break;
                     case 24:
                        this.jj_3_25();
                        break;
                     case 25:
                        this.jj_3_26();
                        break;
                     case 26:
                        this.jj_3_27();
                        break;
                     case 27:
                        this.jj_3_28();
                        break;
                     case 28:
                        this.jj_3_29();
                        break;
                     case 29:
                        this.jj_3_30();
                        break;
                     case 30:
                        this.jj_3_31();
                        break;
                     case 31:
                        this.jj_3_32();
                        break;
                     case 32:
                        this.jj_3_33();
                        break;
                     case 33:
                        this.jj_3_34();
                        break;
                     case 34:
                        this.jj_3_35();
                        break;
                     case 35:
                        this.jj_3_36();
                        break;
                     case 36:
                        this.jj_3_37();
                        break;
                     case 37:
                        this.jj_3_38();
                        break;
                     case 38:
                        this.jj_3_39();
                        break;
                     case 39:
                        this.jj_3_40();
                        break;
                     case 40:
                        this.jj_3_41();
                        break;
                     case 41:
                        this.jj_3_42();
                        break;
                     case 42:
                        this.jj_3_43();
                        break;
                     case 43:
                        this.jj_3_44();
                        break;
                     case 44:
                        this.jj_3_45();
                        break;
                     case 45:
                        this.jj_3_46();
                        break;
                     case 46:
                        this.jj_3_47();
                        break;
                     case 47:
                        this.jj_3_48();
                        break;
                     case 48:
                        this.jj_3_49();
                        break;
                     case 49:
                        this.jj_3_50();
                        break;
                     case 50:
                        this.jj_3_51();
                        break;
                     case 51:
                        this.jj_3_52();
                        break;
                     case 52:
                        this.jj_3_53();
                        break;
                     case 53:
                        this.jj_3_54();
                        break;
                     case 54:
                        this.jj_3_55();
                        break;
                     case 55:
                        this.jj_3_56();
                        break;
                     case 56:
                        this.jj_3_57();
                        break;
                     case 57:
                        this.jj_3_58();
                        break;
                     case 58:
                        this.jj_3_59();
                        break;
                     case 59:
                        this.jj_3_60();
                        break;
                     case 60:
                        this.jj_3_61();
                        break;
                     case 61:
                        this.jj_3_62();
                        break;
                     case 62:
                        this.jj_3_63();
                        break;
                     case 63:
                        this.jj_3_64();
                        break;
                     case 64:
                        this.jj_3_65();
                        break;
                     case 65:
                        this.jj_3_66();
                        break;
                     case 66:
                        this.jj_3_67();
                        break;
                     case 67:
                        this.jj_3_68();
                        break;
                     case 68:
                        this.jj_3_69();
                        break;
                     case 69:
                        this.jj_3_70();
                        break;
                     case 70:
                        this.jj_3_71();
                        break;
                     case 71:
                        this.jj_3_72();
                        break;
                     case 72:
                        this.jj_3_73();
                        break;
                     case 73:
                        this.jj_3_74();
                        break;
                     case 74:
                        this.jj_3_75();
                        break;
                     case 75:
                        this.jj_3_76();
                        break;
                     case 76:
                        this.jj_3_77();
                        break;
                     case 77:
                        this.jj_3_78();
                        break;
                     case 78:
                        this.jj_3_79();
                  }
               }

               p = p.next;
               if (p == null) {
                  break;
               }
            }
         } catch (Formula.LookaheadSuccess var3) {
         }
      }

      this.jj_rescan = false;
   }

   private void jj_save(int index, int xla) {
      Formula.JJCalls p;
      for (p = this.jj_2_rtns[index]; p.gen > this.jj_gen; p = p.next) {
         if (p.next == null) {
            p = p.next = new Formula.JJCalls();
            break;
         }
      }

      p.gen = this.jj_gen + xla - this.jj_la;
      p.first = this.token;
      p.arg = xla;
   }

   static {
      jj_la1_init_0();
      jj_la1_init_1();
   }

   static final class JJCalls {
      int gen;
      Token first;
      int arg;
      Formula.JJCalls next;
   }

   private static final class LookaheadSuccess extends Error {
      private LookaheadSuccess() {
      }
   }
}
