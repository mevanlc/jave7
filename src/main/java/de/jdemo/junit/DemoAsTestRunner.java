package de.jdemo.junit;

import java.lang.reflect.Method;
import java.util.List;

import de.jdemo.framework.IDemoCase;
import de.jdemo.framework.IDemoCaseRunnable;
import de.jdemo.framework.util.DemoUtilities;

import org.junit.internal.runners.InitializationError;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.internal.runners.TestMethod;

// NOT_PUBLISHED
public class DemoAsTestRunner extends JUnit4ClassRunner {

	public DemoAsTestRunner(final Class<? extends IDemoCase> klass)
			throws InitializationError {
		super(klass);
	}

	@Override
	protected List<Method> getTestMethods() {
		return DemoUtilities.getDemoMethods(getTestClass().getJavaClass());
	}

	@Override
	protected TestMethod wrapMethod(Method method) {
		return new DemoMethod(method, getTestClass());
	}

	@Override
	protected Object createTest() throws Exception {
		final IDemoCase demo = (IDemoCase) super.createTest();
		final IDemoCaseRunnable runnable = demo.createRunnable(false);
		return runnable.getDemo();
	}

	@Override
	protected void validate() {
		// nothing to do
	}
}