package net.disy.commons.core.errorhandling;

import java.io.PrintStream;

public class ErrorHandling implements IErrorHandling {
   private final ProblemHandler warningHandler = new ProblemHandler("{0} warnings occured!");
   private final ProblemHandler errorHandler = new ProblemHandler("{0} errors occured!");
   private final PrintStream stream;

   public static ErrorHandling ForFileOutput() {
      return new ErrorHandling(System.out);
   }

   public static ErrorHandling ForConsoleOutput() {
      return new ErrorHandling(System.err);
   }

   private ErrorHandling(PrintStream stream) {
      this.stream = stream;
   }

   @Override
   public void logError(Error error) {
      this.errorHandler.addProblem(error);
   }

   @Override
   public void logWarning(String message) {
      this.warningHandler.addProblem(new Warning(message));
   }

   @Override
   public void finish() {
      this.warningHandler.propagate(this.stream);
      this.errorHandler.propagate(this.stream);
      this.stream.flush();
   }
}
