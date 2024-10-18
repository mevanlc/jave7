package net.disy.commons.core.text;

import java.util.List;
import net.disy.commons.core.message.IMessage;
import net.disy.commons.core.util.Ensure;

public class SuggestionResult<T> {
   private final List<T> items;
   private final IMessage optionalStateMessage;

   public SuggestionResult(List<T> items) {
      this(items, null);
   }

   public SuggestionResult(List<T> items, IMessage optionalStateMessage) {
      Ensure.ensureArgumentNotNull(items);
      this.items = items;
      this.optionalStateMessage = optionalStateMessage;
   }

   public List<T> getItems() {
      return this.items;
   }

   public IMessage getOptionalStateMessage() {
      return this.optionalStateMessage;
   }
}
