package de.jdemo.framework.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.jdemo.annotation.Demo;
import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemo;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;

/**
 * @author Markus Gebhard
 */
public class DemoUtilities {
	public static final String DEMO_METHOD_NAME_PREFIX = "demo";
	public static final String SUITE_METHOD_NAME = "suite";

	public static IDemoCase createDemoCase(final String demoName,
			final Class demoClass) {
		IDemoCase demo;
		try {
			try {
				demo = createDemoCase(demoName, demoClass, new Class[] { String.class }, new Object[] { demoName });
			} catch (final NoSuchMethodException e) {
				demo = createDemoCase(demoName, demoClass, new Class[0],
						new Object[0]);
			}
			return demo;
		} catch (final InstantiationException e) {
			return createErrorDemo("Could not create demo '" + demoName + "' ("
					+ e + ")", e);
		} catch (final IllegalAccessException e) {
			return createErrorDemo("Could not create demo '" + demoName + "' ("
					+ e + ")", e);
		} catch (final InvocationTargetException e) {
			return createErrorDemo("Could not create demo '" + demoName + "' ("
					+ e + "", e);
		} catch (final NoSuchMethodException e) {
			return createErrorDemo("Could not create demo '" + demoName + "' ("
					+ e + "", e);
		}
	}

	private static IDemoCase createDemoCase(final String demoName,
			final Class demoClass, Class[] classArgs, Object[] objects)
			throws NoSuchMethodException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		IDemoCase demo;
		final Constructor constructor = demoClass
				.getConstructor(classArgs);
		final Object newInstance = constructor
				.newInstance(objects);
		if (!(newInstance instanceof IDemoCase)) {
			demo = createDemoDoesNotImplementDemoInterfaceError(demoName);
		} else {
			demo = (IDemoCase) newInstance;
			demo.setName(demoName);
		}
		return demo;
	}

	private static IDemoCase createDemoDoesNotImplementDemoInterfaceError(
			final String demoName) {
		return createErrorDemo("Could not create demo '" + demoName
				+ "' - class does not implement the " + IDemoCase.class, null);
	}

	public static IDemoCase createErrorDemo(final String message,
			final Throwable exception) {
		return new ErrorDemoCase(new DemoInitializationException(message,
				exception));
	}

	public static String getDisplayName(final IDemo demo) {
		String name = demo.getName();
		if (DEMO_METHOD_NAME_PREFIX.equals(name)) {
			name = getRawClassName(demo.getClass());
		}
		return formatDemoName(name);
	}

	private static String getRawClassName(final Class class1) {
		String name = class1.getName();
		if (name.endsWith("Demo")) {
			name = name.substring(0, name.length() - 4);
		}
		if (name.indexOf('.') != -1) {
			name = name.substring(name.lastIndexOf('.') + 1);
		}
		return name;
	}

	private static String formatDemoName(String name) {
		if (name == null) {
			return null;
		}

		if (isClassName(name)) {
			return name;
		}

		// handle demos w/o name
		if (DEMO_METHOD_NAME_PREFIX.equals(name)) {
			return DEMO_METHOD_NAME_PREFIX + "()";
		}

		if (name.startsWith(DEMO_METHOD_NAME_PREFIX)) {
			name = name.substring(4);
		}

		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < name.length(); ++i) {
			final char ch = name.charAt(i);
			if (i > 0) {
				if (Character.isUpperCase(ch)) {
					sb.append(' ');
				} else if (Character.isDigit(ch)
						&& !Character.isDigit(name.charAt(i - 1))) {
					sb.append(' ');
				}
			}
			sb.append(ch);
		}
		return sb.toString();
	}

	private static boolean isClassName(final String name) {
		return name.indexOf('.') != -1;
	}

	public static IDemoCase createDemo(final DemoIdentifier demoId)
			throws DemoClassNotFoundException {
		Class clazz;
		try {
			clazz = Class.forName(demoId.getClassName());
		} catch (final ClassNotFoundException e) {
			throw new DemoClassNotFoundException(demoId.getClassName());
		}
		return createDemoCase(demoId.getMethodName(), clazz);
	}

	public static boolean isDemoIdentifier(final String demoName) {
		return DemoIdentifier.isValidIdentifier(demoName);
	}

	public static List<Method> getDemoMethods(final Class<?> klass) {
		Class<?> superClass = klass;
		final List<Method> demoMethods = new ArrayList<Method>();
		final List<String> names = new ArrayList<String>();
		while (IDemo.class.isAssignableFrom(superClass)) {
			final Method[] methods = superClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; i++) {
				addDemoMethod(methods[i], names, demoMethods);
			}
			superClass = superClass.getSuperclass();
		}
		return demoMethods;
	}

	private static void addDemoMethod(final Method method,
			final List<String> names, final List<Method> demoMethods) {
		final String name = method.getName();
		if (names.contains(name)) {
			return;
		}
		if (isDemoMethod(method)) {
			names.add(name);
			demoMethods.add(method);
		}
	}

	private static boolean isDemoMethod(final Method method) {
		return method.getParameterTypes().length == 0
				&& method.getReturnType().equals(Void.TYPE)
				&& (methodHasDemoAnnotation(method) || nameStartsWithDemo(method
						.getName()));
	}

	private static boolean methodHasDemoAnnotation(Method method) {
		return method.getAnnotation(Demo.class) != null;
	}

	private static boolean nameStartsWithDemo(final String name) {
		return name.startsWith(DemoUtilities.DEMO_METHOD_NAME_PREFIX);
	}
}