package de.jave.jave.clipart;

public class ClipartDialogResult {
   private final Clipart clipart;
   private final ClipartGroup group;

   public ClipartDialogResult(ClipartGroup group, Clipart clipart) {
      this.group = group;
      this.clipart = clipart;
   }

   public ClipartGroup getGroup() {
      return this.group;
   }

   public Clipart getClipart() {
      return this.clipart;
   }
}
