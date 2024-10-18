package de.jdemo.annotation;

import java.lang.reflect.Method;

import de.jdemo.framework.DemoIdentifier;
import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.exceptions.DemoClassNotFoundException;
import de.jdemo.framework.util.DemoUtilities;

public class DemoAnnotationReader implements IDemoAnnotationReader {

	public DemoAnnotation getAnnotation(final DemoIdentifier identifier)
			throws DemoClassNotFoundException {
		final IDemoCase demoCase = DemoUtilities.createDemo(identifier);
		Method method;
		try {
			method = demoCase.getClass().getMethod(identifier.getMethodName()
            );
		} catch (final NoSuchMethodException e) {
			throw new DemoClassNotFoundException(
					"Demo class does not contain expected demo method ("
							+ identifier + ")");
		}
		DemoAnnotation demoAnnotation = getDemoAnnotation(method);
		if (demoAnnotation != null) {
			return demoAnnotation;
		}
		return getJDemoAnnotation(method);
	}

	private DemoAnnotation getJDemoAnnotation(Method method) {
		final JDemo annotation = method.getAnnotation(JDemo.class);
		if (annotation == null) {
			return null;
		}
		return new DemoAnnotation(annotation.description(), annotation
				.categories());
	}

	private DemoAnnotation getDemoAnnotation(Method method) {
		final Demo annotation = method.getAnnotation(Demo.class);
		if (annotation == null) {
			return null;
		}
		return new DemoAnnotation(annotation.description(), annotation
				.categories());
	}
}