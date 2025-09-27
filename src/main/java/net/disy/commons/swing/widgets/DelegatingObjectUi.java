package net.disy.commons.swing.ui;

import javax.swing.Icon;
import net.disy.commons.core.util.Ensure;

public abstract class DelegatingObjectUi<T, U> implements IObjectUi<T> {
   private final IObjectUi<U> delegate;

   public DelegatingObjectUi(IObjectUi<U> delegate) {
      Ensure.ensureArgumentNotNull(delegate);
      this.delegate = delegate;
   }

   @Override
   public Icon getIcon(T value) {
      return value == null ? null : this.delegate.getIcon(this.getDelegatingValue(value));
   }

   protected abstract U getDelegatingValue(T var1);

   @Override
   public String getLabel(T value) {
      return value == null ? null : this.delegate.getLabel(this.getDelegatingValue(value));
   }

   @Override
   public String getToolTipText(T value) {
      return value == null ? null : this.delegate.getToolTipText(this.getDelegatingValue(value));
   }
}
