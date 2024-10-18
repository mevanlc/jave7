package net.disy.commons.swing.dialog.input;

public interface ISmartDialogPanelsBuilder {
   void add(ISmartDialogPanel... var1);

   @Deprecated
   void add(Iterable<ISmartDialogPanel> var1);

   void addVerticalComponentSpacing();
}
