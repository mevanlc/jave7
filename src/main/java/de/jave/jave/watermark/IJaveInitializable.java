package de.jave.jave.configuration;

public interface IJaveInitializable<T> {
   T initialize(IJavaInitializationContext var1) throws ConfigurationException;
}
