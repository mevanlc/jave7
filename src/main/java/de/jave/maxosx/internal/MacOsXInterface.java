package de.jave.maxosx.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MacOsXInterface implements InvocationHandler {
   private static Object macOSXApplication;
   private final Object target;
   private final Method handler;
   private final String proxySignature;

   public static void setQuitHandler(Object target, Method quitHandler) {
      setHandler(new MacOsXInterface("handleQuit", target, quitHandler));
   }

   public static void setAboutHandler(Object target, Method aboutHandler) {
      boolean isAboutMenuEnabled = target != null && aboutHandler != null;
      if (isAboutMenuEnabled) {
         setHandler(new MacOsXInterface("handleAbout", target, aboutHandler));
      }

      if (macOSXApplication != null) {
         try {
            Method enableAboutMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledAboutMenu", boolean.class);
            enableAboutMethod.invoke(macOSXApplication, isAboutMenuEnabled);
         } catch (IllegalAccessException var4) {
         } catch (InvocationTargetException var5) {
         } catch (NoSuchMethodException var6) {
         }
      }
   }

   public static void setPreferencesHandler(Object target, Method prefsHandler) {
      boolean enablePrefsMenu = target != null && prefsHandler != null;
      if (enablePrefsMenu) {
         setHandler(new MacOsXInterface("handlePreferences", target, prefsHandler));
      }

      if (macOSXApplication != null) {
         try {
            Method enablePrefsMethod = macOSXApplication.getClass().getDeclaredMethod("setEnabledPreferencesMenu", boolean.class);
            enablePrefsMethod.invoke(macOSXApplication, enablePrefsMenu);
         } catch (IllegalAccessException var4) {
         } catch (InvocationTargetException var5) {
         } catch (NoSuchMethodException var6) {
         }
      }
   }

   public static void setFileHandler(final Object target, final Method fileHandler) {
      setHandler(new MacOsXInterface("handleOpenFile", target, fileHandler) {
         @Override
         public boolean callTarget(Object appleEvent) {
            if (appleEvent != null) {
               try {
                  Method getFilenameMethod = appleEvent.getClass().getDeclaredMethod("getFilename", (Class<?>[])null);
                  String name = (String)getFilenameMethod.invoke(appleEvent, (Object[])null);
                  fileHandler.invoke(target, name);
               } catch (IllegalAccessException var4) {
               } catch (InvocationTargetException var5) {
               } catch (NoSuchMethodException var6) {
               }
            }

            return true;
         }
      });
   }

   public static void setHandler(MacOsXInterface adapter) {
      try {
         Class<?> applicationClass = Class.forName("com.apple.eawt.Application");
         if (macOSXApplication == null) {
            macOSXApplication = applicationClass.getConstructor((Class<?>[])null).newInstance((Object[])null);
         }

         Class<?> applicationListenerClass = Class.forName("com.apple.eawt.ApplicationListener");
         Method addListenerMethod = applicationClass.getDeclaredMethod("addApplicationListener", applicationListenerClass);
         Object osxAdapterProxy = Proxy.newProxyInstance(MacOsXInterface.class.getClassLoader(), new Class[]{applicationListenerClass}, adapter);
         addListenerMethod.invoke(macOSXApplication, osxAdapterProxy);
      } catch (ClassNotFoundException var5) {
      } catch (IllegalAccessException var6) {
      } catch (InstantiationException var7) {
      } catch (NoSuchMethodException var8) {
      } catch (InvocationTargetException var9) {
      }
   }

   protected MacOsXInterface(String signature, Object target, Method handler) {
      this.proxySignature = signature;
      this.target = target;
      this.handler = handler;
   }

   public boolean callTarget(Object appleEvent) throws InvocationTargetException, IllegalAccessException {
      Object result = this.handler.invoke(this.target, (Object[])null);
      return result == null ? true : Boolean.parseBoolean(result.toString());
   }

   @Override
   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      if (this.isCorrectMethod(method, args)) {
         boolean handled = this.callTarget(args[0]);
         this.setApplicationEventHandled(args[0], handled);
      }

      return null;
   }

   private boolean isCorrectMethod(Method method, Object[] args) {
      return this.handler != null && this.proxySignature.equals(method.getName()) && args.length == 1;
   }

   private void setApplicationEventHandled(Object event, boolean handled) {
      if (event != null) {
         try {
            Method setHandledMethod = event.getClass().getDeclaredMethod("setHandled", boolean.class);
            setHandledMethod.invoke(event, handled);
         } catch (IllegalAccessException var4) {
         } catch (NoSuchMethodException var5) {
         } catch (InvocationTargetException var6) {
         }
      }
   }
}
