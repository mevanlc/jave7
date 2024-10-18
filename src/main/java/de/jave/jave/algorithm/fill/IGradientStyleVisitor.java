package de.jave.jave.algorithm.fill;

public interface IGradientStyleVisitor {
   void visitLinear(GradientStyle var1);

   void visitSunburst(GradientStyle var1);

   void visitRadial(GradientStyle var1);
}
