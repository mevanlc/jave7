package net.disy.commons.core.errorhandling;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ProblemHandler {
   private final List<IProblem> problemList = new ArrayList<>();
   private final String summaryPattern;

   public ProblemHandler(String summaryPattern) {
      this.summaryPattern = summaryPattern;
   }

   public void addProblem(IProblem problem) {
      this.problemList.add(problem);
   }

   public void propagate(PrintStream stream) {
      if (!this.problemList.isEmpty()) {
         stream.println(MessageFormat.format(this.summaryPattern, this.problemList.size()));

         for (IProblem error : this.problemList) {
            error.print(stream);
         }
      }
   }

   public boolean hasProblems() {
      return !this.problemList.isEmpty();
   }
}
