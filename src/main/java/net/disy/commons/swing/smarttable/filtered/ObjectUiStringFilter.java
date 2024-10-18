package net.disy.commons.swing.smarttable.filtered;

import net.disy.commons.core.predicate.IPredicate;
import net.disy.commons.core.util.Ensure;
import net.disy.commons.core.util.StringUtilities;
import net.disy.commons.swing.ui.IObjectUi;

public class ObjectUiStringFilter<T> implements IPredicate<T> {
   private final IObjectUi<T>[] objectUis;
   private final String filterText;

   public ObjectUiStringFilter(IObjectUi<T>[] objectUis, String filterText) {
      Ensure.ensureArgumentNotNull(objectUis);
      this.objectUis = objectUis;
      this.filterText = filterText;
   }

   @Override
   public boolean evaluate(T value) {
      if (StringUtilities.isNullOrEmpty(this.filterText)) {
         return true;
      } else {
         for (int i = 0; i < this.objectUis.length; i++) {
            String label = this.objectUis[i].getLabel(value);
            if (!StringUtilities.isNullOrEmpty(label) && label.toLowerCase().indexOf(this.filterText.toLowerCase()) >= 0) {
               return true;
            }
         }

         return false;
      }
   }
}
