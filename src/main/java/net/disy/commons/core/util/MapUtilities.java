package net.disy.commons.core.util;

import java.util.HashMap;
import java.util.Map;
import net.disy.commons.core.predicate.IPredicate;

public class MapUtilities {
   public static <V, K> Map<K, V> filter(Map<K, V> map, IPredicate<K> predicate) {
      Map<K, V> result = new HashMap<>();

      for (K key : map.keySet()) {
         if (predicate.evaluate(key)) {
            result.put(key, map.get(key));
         }
      }

      return result;
   }
}
