package de.jdemo.framework.util.test;

import javax.swing.JPanel;

import de.jdemo.annotation.Demo;
import de.jdemo.extensions.SwingDemoCase;

public class DummyDemo extends SwingDemoCase {
	public void demo() {
		show(new JPanel());
	}

	@Demo
	public void demoTwo() {
		show(new JPanel());
	}
}