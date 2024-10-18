//Copyright (c) 2008 by disy Informationssysteme GmbH
package de.jdemo.framework.util.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.List;

import javax.swing.JPanel;

import de.jdemo.annotation.Demo;
import de.jdemo.extensions.SwingDemoCase;
import de.jdemo.framework.DemoSuite;
import de.jdemo.framework.util.DemoUtilities;

import org.junit.Test;

// NOT_PUBLISHED
public class DemoUtilities_GetDemoMethods_Test {

	public static class OverridingDemo extends
			DummyPublicVoidSomethingElseAndDemo {
		@Override
		public void demo() {
			show(new JPanel());
		}
	}

	public static class DummyPublicVoidSomethingElseAndDemo extends
			SwingDemoCase {
		public void nothing() {
			show(new JPanel());
		}

		public void demo() {
			show(new JPanel());
		}
	}

	public static class DummyPrivateVoidDemo extends SwingDemoCase {
		public void demo() {
			show(new JPanel());
		}

		@SuppressWarnings("unused")
		private void demo2() {
			show(new JPanel());
		}
	}

	public static class DummyPublicNonVoidDemo extends SwingDemoCase {
		public void demo() {
			show(new JPanel());
		}

		public boolean demo2() {
			show(new JPanel());
			return true;
		}
	}

	public static class AnnotatedDemo extends SwingDemoCase {
		@Demo
		public void hund() {
			show(new JPanel());
		}

		public boolean katze() {
			show(new JPanel());
			return true;
		}
	}

	@Test
	public void returnsMethodForEachDemoMethod() throws Exception {
		final List<Method> demoMethods = DemoUtilities
				.getDemoMethods(DummyDemo.class);
		assertThat(demoMethods.size(), is(2));
		assertThat(demoMethods.get(0).getName(), is(equalTo(DemoSuite
				.createDemo(DummyDemo.class, "demo").getName()))); //$NON-NLS-1$
		assertThat(demoMethods.get(1).getName(), is(equalTo(DemoSuite
				.createDemo(DummyDemo.class, "demoTwo").getName()))); //$NON-NLS-1$
	}

	@Test
	public void returnsOnlyOneMethodForOverriddenDemoMethods() throws Exception {
		final List<Method> demoMethods = DemoUtilities
				.getDemoMethods(OverridingDemo.class);
		assertThat(demoMethods.size(), is(1));
		assertThat(demoMethods.get(0).getName(), is(equalTo(DemoSuite
				.createDemo(OverridingDemo.class, "demo").getName()))); //$NON-NLS-1$
	}

	@Test
	public void returnsVoidMethodsOnly() throws Exception {
		final List<Method> demoMethods = DemoUtilities
				.getDemoMethods(DummyPublicNonVoidDemo.class);
		assertThat(demoMethods.size(), is(1));
		assertThat(demoMethods.get(0).getName(), is(equalTo(DemoSuite
				.createDemo(DummyPublicNonVoidDemo.class, "demo").getName()))); //$NON-NLS-1$
	}

	@Test
	public void returnsMethodsForDemoMethodsOnly() throws Exception {
		final List<Method> demoMethods = DemoUtilities
				.getDemoMethods(DummyPublicVoidSomethingElseAndDemo.class);
		assertThat(demoMethods.size(), is(1));
		assertThat(demoMethods.get(0).getName(),
				is(equalTo(DemoSuite
						.createDemo(DummyPublicVoidSomethingElseAndDemo.class,
								"demo").getName()))); //$NON-NLS-1$
	}

	@Test
	public void recognizesAnnotatedMethodsAsDemos() throws Exception {
		List<Method> demoMethods = DemoUtilities.getDemoMethods(AnnotatedDemo.class);
		assertThat(demoMethods.size(), is(1));
		assertThat(demoMethods.get(0).getName(),
				is(equalTo(DemoSuite
						.createDemo(AnnotatedDemo.class,
								"hund").getName()))); //$NON-NLS-1$
	}

}
