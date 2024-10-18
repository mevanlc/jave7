package net.disy.commons.swing.dialog.input.optional;

import java.util.Date;
import net.disy.commons.core.model.ObjectModel;
import net.disy.commons.swing.dialog.input.object.IPresetValuesFactory;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;

public interface IOptionalSmartDialogPanelFactory {
   IOptionalSmartDialogPanel createBytePanel(ObjectModel<Byte> var1, boolean var2);

   IOptionalSmartDialogPanel createDatePanel(ObjectModel<Date> var1, boolean var2, String var3);

   IOptionalSmartDialogPanel createDoublePanel(ObjectModel<Double> var1, boolean var2);

   IOptionalSmartDialogPanel createIntegerPanel(ObjectModel<Integer> var1, boolean var2);

   IOptionalSmartDialogPanel createLongPanel(ObjectModel<Long> var1, boolean var2);

   IOptionalSmartDialogPanel createShortPanel(ObjectModel<Short> var1, boolean var2);

   IOptionalSmartDialogPanel createStringPanel(ObjectModel<String> var1, boolean var2, IPresetValuesFactory<String> var3, IAttributeContext var4);

   IOptionalSmartDialogPanel createBooleanPanel(ObjectModel<Boolean> var1, boolean var2);
}
