package net.disy.commons.core.adaptable;

import java.util.HashMap;
import java.util.Map;
import net.disy.commons.core.provider.IProvider;
import net.disy.commons.core.provider.StaticProvider;

public class Adaptable<S> implements IAdaptable<S> {
   private final Map<Class<?>, IProvider<?>> objectProvidersByClass = new HashMap<>();

   @Override
   public <T extends S> T get(Class<T> adapter) {
      if (this.objectProvidersByClass.containsKey(adapter)) {
         return (T)this.objectProvidersByClass.get(adapter).getObject();
      } else {
         for (Class<?> clazz : this.objectProvidersByClass.keySet()) {
            if (clazz.isAssignableFrom(adapter)) {
               Object object = this.objectProvidersByClass.get(clazz).getObject();
               if (adapter.isInstance(object)) {
                  return (T)object;
               }
            }
         }

         return (T)(adapter.isInstance(this) ? this : null);
      }
   }

   public <T extends S> void add(Class<T> clazz, IProvider<T> provider) {
      this.objectProvidersByClass.put(clazz, provider);
   }

   public <T extends S> void add(Class<T> clazz, T object) {
      this.add(clazz, new StaticProvider<>(object));
   }
}
