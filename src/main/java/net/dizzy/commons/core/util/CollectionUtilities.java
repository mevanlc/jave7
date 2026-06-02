package net.dizzy.commons.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class CollectionUtilities {
   private CollectionUtilities() {
   }

   public static <S, T> List<T> transform(Collection<S> source, ITransformer<S, T> transformer) {
      List<T> result = new ArrayList<>(source.size());
      for (S value : source) {
         result.add(transformer.transform(value));
      }
      return result;
   }
}
