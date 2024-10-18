package de.jave.formula.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Formula2Symbols {
   private Map symbolMap = new LinkedHashMap();
   private static Formula2Symbols instance = new Formula2Symbols();

   public static Formula2Symbols getInstance() {
      return instance;
   }

   private Formula2Symbols() {
      this.symbolMap.put("alpha", createSymbolCharField(0, new String[]{" _ _", "(_/_"}));
      this.symbolMap.put("beta", createSymbolCharField(1, new String[]{" _", "|_)", "|_)", "|"}));
      this.symbolMap.put("gamma", createSymbolCharField(0, new String[]{"\\/", "()"}));
      this.symbolMap.put("delta", createSymbolCharField(0, new String[]{" (", "(_)"}));
      this.symbolMap.put("epsilon", createSymbolCharField(0, new String[]{" _", "(_", "(_"}));
      this.symbolMap.put("zeta", createSymbolCharField(1, new String[]{"._", "/-'", "\\_", " -'"}));
      this.symbolMap.put("eta", createSymbolCharField(1, new String[]{" ._", " | |", "   |"}));
      this.symbolMap.put("theta", createSymbolCharField(0, new String[]{" _", "/_\\", "\\_/"}));
      this.symbolMap.put("vartheta", createSymbolCharField(1, new String[]{"  ,.", "  `+.", "') |", " `-'"}));
      this.symbolMap.put("iota", createSymbolCharField(0, new String[]{"|", "|,"}));
      this.symbolMap.put("kappa", createSymbolCharField(0, new String[]{"|/", "|\\"}));
      this.symbolMap.put("lambda", createSymbolCharField(0, new String[]{"\\", "/\\"}));
      this.symbolMap.put("mu", createSymbolCharField(1, new String[]{"|_|", "|"}));
      this.symbolMap.put("nu", createSymbolCharField(0, new String[]{"| )", "|/"}));
      this.symbolMap.put("xi", createSymbolCharField(1, new String[]{"._", "(.", "(_", " -'"}));
      this.symbolMap.put("omicron", createSymbolCharField(0, new String[]{" _", "(_)"}));
      this.symbolMap.put("omikron", createSymbolCharField(0, new String[]{" _", "(_)"}));
      this.symbolMap.put("pi", createSymbolCharField(0, new String[]{"__", "||"}));
      this.symbolMap.put("rho", createSymbolCharField(1, new String[]{" _", "|_)", "|"}));
      this.symbolMap.put("varrho", createSymbolCharField(1, new String[]{" _", "(_)", " `-"}));
      this.symbolMap.put("sigma", createSymbolCharField(0, new String[]{" __,", "(_)"}));
      this.symbolMap.put("varsigma", createSymbolCharField(1, new String[]{" _,", "(_", "--'"}));
      this.symbolMap.put("tau", createSymbolCharField(0, new String[]{" _,", "'|"}));
      this.symbolMap.put("upsilon", createSymbolCharField(0, new String[]{") )", "`-'"}));
      this.symbolMap.put("phi", createSymbolCharField(1, new String[]{" |", "(|)", " |"}));
      this.symbolMap.put("varphi", createSymbolCharField(1, new String[]{"   _", "(_|_)", "  |"}));
      this.symbolMap.put("chi", createSymbolCharField(0, new String[]{"'\\/", " /\\,"}));
      this.symbolMap.put("psi", createSymbolCharField(1, new String[]{"(_|_)", "  |"}));
      this.symbolMap.put("omega", createSymbolCharField(0, new String[]{"(_|_)"}));
      this.symbolMap.put("Alpha", createSymbolCharField(0, new String[]{"  /\\", " /--\\", "/    \\"}));
      this.symbolMap.put("Beta", createSymbolCharField(0, new String[]{" __", "|  )", "|--", "|__)"}));
      this.symbolMap.put("Gamma", createSymbolCharField(0, new String[]{" __", "|  '", "|", "|"}));
      this.symbolMap.put("Delta", createSymbolCharField(0, new String[]{"  /\\", " /  \\", "/____\\"}));
      this.symbolMap.put("Epsilon", createSymbolCharField(0, new String[]{" __", "|", "|--", "|__"}));
      this.symbolMap.put("Zeta", createSymbolCharField(0, new String[]{"___", "  /", " /", "/__"}));
      this.symbolMap.put("Eta", createSymbolCharField(0, new String[]{"|  |", "|--|", "|  |"}));
      this.symbolMap.put("Theta", createSymbolCharField(0, new String[]{"  _", " / \\", "| - |", " \\_/"}));
      this.symbolMap.put("Iota", createSymbolCharField(0, new String[]{"`|'", " |", ".|,"}));
      this.symbolMap.put("Kappa", createSymbolCharField(0, new String[]{"| /", "|/", "|\\", "| \\"}));
      this.symbolMap.put("Lambda", createSymbolCharField(0, new String[]{"  /\\", " /  \\", "/    \\"}));
      this.symbolMap.put("Mu", createSymbolCharField(0, new String[]{"|\\ /|", "| V |", "|   |"}));
      this.symbolMap.put("Nu", createSymbolCharField(0, new String[]{"|\\  |", "| \\ |", "|  \\|"}));
      this.symbolMap.put("Xi", createSymbolCharField(0, new String[]{" __", "'  `", " --", ".__,"}));
      this.symbolMap.put("Omicron", createSymbolCharField(0, new String[]{"  __", " /  \\", "(    )", " \\__/"}));
      this.symbolMap.put("Omikron", createSymbolCharField(0, new String[]{"  __", " /  \\", "(    )", " \\__/"}));
      this.symbolMap.put("Pi", createSymbolCharField(0, new String[]{"-----", " | |", " | |"}));
      this.symbolMap.put("PI", createSymbolCharField(0, new String[]{"-----", " | |", " | |"}));
      this.symbolMap.put("Rho", createSymbolCharField(0, new String[]{" __", "|  |", "|--'", "|"}));
      this.symbolMap.put("Sigma", createSymbolCharField(0, new String[]{"___", "\\", " >", "/__"}));
      this.symbolMap.put("Tau", createSymbolCharField(0, new String[]{"_____", "  |", "  |", "  |"}));
      this.symbolMap.put("Upsilon", createSymbolCharField(0, new String[]{"\\   /", " \\ /", "  |", "  |"}));
      this.symbolMap.put("Phi", createSymbolCharField(0, new String[]{" _T_", "/ | \\", "\\_|_/", " .i."}));
      this.symbolMap.put("Chi", createSymbolCharField(0, new String[]{"\\  /", " \\/", " /\\", "/  \\"}));
      this.symbolMap.put("Psi", createSymbolCharField(0, new String[]{"   _", "|  |  |", " \\_|_/", "  .i."}));
      this.symbolMap.put("Omega", createSymbolCharField(0, new String[]{"  __", " /  \\", "(    )", "_\\  /_"}));
      this.symbolMap.put("INFINITY", createSymbolCharField(0, new String[]{"oo"}));
   }

   public FormulaCharField getSymbol(String name) {
      return (FormulaCharField)this.symbolMap.get(name);
   }

   private static FormulaCharField createSymbolCharField(int descent, String[] lines) {
      int height = lines.length;
      int ascent = height - descent - 1;
      return new FormulaCharField(lines, ascent);
   }

   public String[] getNames() {
      Set<String> set = this.symbolMap.keySet();
      return set.toArray(new String[0]);
   }
}
