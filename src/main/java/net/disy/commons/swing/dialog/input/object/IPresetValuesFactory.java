package net.disy.commons.swing.dialog.input.object;

import java.util.List;
import net.disy.commons.core.exception.ConfigurationException;
import net.disy.commons.swing.dialog.input.text.IAttributeContext;

public interface IPresetValuesFactory<T> {
   List<T> createList(IAttributeContext var1) throws ConfigurationException;
}
