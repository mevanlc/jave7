package net.disy.commons.swing.objectfield;

public class StringFieldConfigurationBuilder {
   private int columns = 10;
   private IObjectFormater<String> formater = new StringFormater();
   private boolean editable = true;

   public void setFormater(IObjectFormater<String> formater) {
      this.formater = formater;
   }

   public void setColumns(int columns) {
      this.columns = columns;
   }

   public void setEditable(boolean editable) {
      this.editable = editable;
   }

   public IObjectFieldConfiguration<String> build() {
      return new StringFieldConfiguration(this.columns, this.formater, this.editable);
   }
}
