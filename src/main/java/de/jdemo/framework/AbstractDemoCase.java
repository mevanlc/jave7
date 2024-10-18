package de.jdemo.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.jdemo.framework.exceptions.DemoExecutionFailedError;
import de.jdemo.framework.exceptions.DemoMethodNotFoundException;

import org.junit.After;
import org.junit.Before;

/**
 * @author Markus Gebhard
 */
public abstract class AbstractDemoCase implements IDemoCase, Cloneable {
	private IDemoCaseRunnable runnable;
	/** the name of the demo method */
	private String demoMethodName;

	public AbstractDemoCase() {
		this(""); //$NON-NLS-1$
	}

	public AbstractDemoCase(final String name) {
		setName(name);
	}

	public void accept(final IDemoVisitor visitor) {
		visitor.visitDemoCase(this);
	}

	public final void setRunnable(final IDemoCaseRunnable runnable) {
		this.runnable = runnable;
	}

	protected final IDemoCaseRunnable getRunnable() {
		return runnable;
	}

	protected static void fail(final String message) {
		throw new DemoExecutionFailedError(message);
	}

	protected static void assertTrue(final String message,
			final boolean condition) {
		if (!condition) {
			fail(message);
		}
	}

	protected static void assertNotNull(final String message,
			final Object object) {
		assertTrue(message, object != null);
	}

	protected static void assertNull(final String message, final Object object) {
		assertTrue(message, object == null);
	}

	/**
	 * Sets up the fixture, for example, open a network connection. This method
	 * is called before a demo is executed.
	 */
	protected void setUp() throws Exception {
		// nothing to do
	}

	/**
	 * Tears down the fixture, for example, close a network connection. This
	 * method is called after a demo is executed.
	 */
	protected void tearDown() throws Exception {
		// nothing to do
	}

	/**
	 * Returns a string representation of the demo case id
	 */
	@Override
	public final String toString() {
		return new DemoIdentifier(getClass().getName(), getName()).toString();
	}

	/**
	 * Gets the demo method name of this DemoCase
	 * 
	 * @return the name of this demo case
	 */
	public final String getName() {
		return demoMethodName;
	}

	/**
	 * Sets the name of this DemoCase
	 * 
	 * @param demoMethodName
	 *            The name to set
	 */
	public final void setName(final String demoMethodName) {
		this.demoMethodName = demoMethodName;
	}

	/**
	 * Can be called from within the demonstration code to indicate that this
	 * demonstration has been finished.
	 * 
	 * @see #cancel()
	 */
	protected void exit() {
		if (runnable != null) {
			runnable.exit();
		} else {
			throw new IllegalStateException("exit without runner"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns a clone of this democase, that is a new instance being the same
	 * demonstration case, but not having any listeners or other temporary
	 * fields. This is intended for starting each demo case from scratch.
	 */
	@Override
	protected Object clone() {
		AbstractDemoCase clone;
		try {
			clone = (AbstractDemoCase) super.clone();
		} catch (final CloneNotSupportedException e) {
			// Should not happen since we are clonable
			throw new RuntimeException(e.toString());
		}
		clone.runnable = null;
		return clone;
	}

	public IDemoCase getClone() {
		return (IDemoCase) clone();
	}

	public final DemoIdentifier getIdentifier() {
		return new DemoIdentifier(getClass().getName(), getName());
	}

	/**
	 * Give this DemoCase the chance to exit. Can be overriden by subclasses
	 * e.g. to dispose windows or dispose other resources. Usually invoked from
	 * an {@link IDemoCaseRunnable} when the user requests termination of the
	 * Demo.
	 * <p>
	 * Should not be called from within a demo.
	 */
	public void cancel() {
		// nothing to do
	}

	@Before
	public final void before() throws Exception {
		executeSetUp();
	}

	@After
	public final void after() throws Exception {
		executeTearDown();
	}

	/**
	 * Called by the DemoCaseRunner to invoke the setUp method.
	 * <p>
	 * Should not be called from within a demo.
	 */
	public void executeSetUp() throws Exception {
		setUp();
	}

	/**
	 * Called by the DemoCaseRunner to invoke the tearDown method.
	 * <p>
	 * Should not be called from within a demo.
	 */
	public void executeTearDown() throws Exception {
		tearDown();
	}

	protected ThreadGroup createThreadGroup() {
		assertNotNull("The demo case does not have a runner.", runnable);
		return new DemoCaseThreadGroup(runnable);
	}

	/**
	 * Override to run the demo and assert its state.
	 * 
	 * @exception Throwable
	 *                if any exception is thrown
	 */
	public void runDemo() throws Throwable {
		assertNotNull("The demo case does not have a runner.", getRunnable());
		assertNotNull("No demo method name specified.", getName());
		Method runMethod = null;
		try {
			// use getMethod to get all public inherited
			// methods. getDeclaredMethods returns all
			// methods of this class but excludes the
			// inherited ones.
			runMethod = getClass().getMethod(getName());
			runMethod.setAccessible(true); // Bugfix: Tests will not run if
											// removed
		} catch (final NoSuchMethodException e) {
			throw new DemoMethodNotFoundException(getClass(), getName());
		}
		if (!Modifier.isPublic(runMethod.getModifiers())) {
			fail("Method '" + getName() + "' must be public in " + getClass()); //$NON-NLS-1$//$NON-NLS-2$
		}

		try {
			runMethod.invoke(this);
		} catch (final InvocationTargetException e) {
			e.fillInStackTrace();
			throw e.getTargetException();
		} catch (final IllegalAccessException e) {
			e.fillInStackTrace();
			throw e;
		}
	}
}