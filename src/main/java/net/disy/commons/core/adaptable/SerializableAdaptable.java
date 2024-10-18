package net.disy.commons.core.adaptable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SerializableAdaptable implements IAdaptable<Serializable>, Serializable {
   private final Map<Class<?>, Object> objectsByClass = new HashMap<>();

   public <T extends Serializable> T get(Class<T> adapter) {
      if (this.objectsByClass.containsKey(adapter)) {
         return (T)this.objectsByClass.get(adapter);
      } else {
         for (Class<?> clazz : this.objectsByClass.keySet()) {
            if (clazz.isAssignableFrom(adapter)) {
               Object object = this.objectsByClass.get(clazz);
               if (adapter.isInstance(object)) {
                  return (T)object;
               }
            }
         }

         return (T)(adapter.isInstance(this) ? this : null);
      }
   }

   public <T extends Serializable> void add(Class<T> clazz, T object) {
      this.objectsByClass.put(clazz, object);
   }
}
