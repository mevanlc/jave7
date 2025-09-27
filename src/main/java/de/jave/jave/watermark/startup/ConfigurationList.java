package de.jave.jave.application.startup;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationList {
   private final List<Object> configurations = new ArrayList<>();

   public void add(Object configuration) {
      this.configurations.add(configuration);
   }

   public <T> T getRequired(Class<T> implementationClass) {
      for (Object object : this.configurations) {
         if (implementationClass.isAssignableFrom(object.getClass())) {
            return (T)object;
         }
      }

      throw new RuntimeException("No configuration object implementing " + implementationClass + " configured.");
   }
}
