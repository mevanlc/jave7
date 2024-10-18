package de.jdemo.framework.test;

import junit.framework.TestCase;
import de.jdemo.framework.DemoIdentifier;

import static org.junit.Assert.assertNotEquals;

/**
 * @author Markus Gebhard
 */
public class DemoIdentifierTest extends TestCase {

	public void testCreationByNames() {
		DemoIdentifier id = new DemoIdentifier(
				"mypackage.myclass123", "demoMethod1"); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals("mypackage.myclass123", id.getClassName()); //$NON-NLS-1$
		assertEquals("demoMethod1", id.getMethodName()); //$NON-NLS-1$
		assertEquals("mypackage.myclass123:demoMethod1", id.getIdentifierName()); //$NON-NLS-1$
	}

	public void testCreationByIdString() {
		DemoIdentifier id = new DemoIdentifier(
				"mypackage.myclass123:demoMethod1"); //$NON-NLS-1$
		assertEquals("mypackage.myclass123", id.getClassName()); //$NON-NLS-1$
		assertEquals("demoMethod1", id.getMethodName()); //$NON-NLS-1$
		assertEquals("mypackage.myclass123:demoMethod1", id.getIdentifierName()); //$NON-NLS-1$
	}

	public void testLegalIdentifiers() {
		new DemoIdentifier("Class$1:demo"); //$NON-NLS-1$
		new DemoIdentifier("Class_1:demo"); //$NON-NLS-1$
		new DemoIdentifier("Class:demo_1"); //$NON-NLS-1$
	}

	public void testIllegalIdentifier1() {
		try {
			new DemoIdentifier("mypackage.myclass123", ""); //$NON-NLS-1$ //$NON-NLS-2$
			fail();
		} catch (Exception expected) {
			// expected
		}
	}

	public void testIllegalIdentifier2() {
		try {
			new DemoIdentifier("", "demoMethod"); //$NON-NLS-1$ //$NON-NLS-2$
			fail();
		} catch (Exception expected) {
			// expected
		}
	}

	public void testAcceptsIdentifierStartingWithAnythingElse() {
		new DemoIdentifier("Class", "myMethod"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	// Any ideas on how to improve this standard equals/hashcode testing stuff?
	// Write to markus@jave.de Thanks.
	public void testSameEquals() {
		DemoIdentifier id = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		assertEquals(id, id);
	}

	public void testEqualEquals() {
		DemoIdentifier id1 = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		DemoIdentifier id2 = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		assertEquals(id1, id2);
	}

	public void testDifferentClassNameNotEquals() {
		DemoIdentifier id1 = new DemoIdentifier("mypackage.myclass1:demoMethod"); //$NON-NLS-1$
		DemoIdentifier id2 = new DemoIdentifier("mypackage.myclass2:demoMethod"); //$NON-NLS-1$
        assertNotEquals(id1, id2);
	}

	public void testDifferentMethodNameNotEquals() {
		DemoIdentifier id1 = new DemoIdentifier("mypackage.myclass:demoMethod1"); //$NON-NLS-1$
		DemoIdentifier id2 = new DemoIdentifier("mypackage.myclass:demoMethod2"); //$NON-NLS-1$
        assertNotEquals(id1, id2);
	}

	public void testSameHasEqualHashcode() {
		DemoIdentifier id = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		assertEquals(id.hashCode(), id.hashCode());
	}

	public void testEqualsHaveEqualHashcode() {
		DemoIdentifier id1 = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		DemoIdentifier id2 = new DemoIdentifier("mypackage.myclass:demoMethod"); //$NON-NLS-1$
		assertEquals(id1.hashCode(), id2.hashCode());
	}

	public void testWarningAsSpecialDemoName() {
		new DemoIdentifier("de.jdemo.framework.DemoSuite$1:warning"); //$NON-NLS-1$
	}
}