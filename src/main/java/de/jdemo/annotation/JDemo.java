package de.jdemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Annotations for JDemo demos, that e.g. can be used by demo runners at the
 * time of execution. Note that those annotations are not supported by all demo
 * runners and that you need a JRE 1.5 or greater.
 * 
 * @deprecated (urskr, 13.02.08) Please use the newly added 'Demo' annotation
 *             instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( { ElementType.METHOD })
@Deprecated
public @interface JDemo {
	/**
	 * An optional description for the demo, that might be displayed in a demo
	 * runner application. The format is HTML, as supported by the JTextPane
	 * Swing component.
	 */
	public String description() default ""; //$NON-NLS-1$

	/**
	 * Optional markers for the categories, this demo belongs to. The markers
	 * can be used by demo runners in order to filter all available demos to
	 * certain categories.
	 * <p>
	 * There are no marker names defined by the framework.
	 * <p>
	 * Example: <code>{ &quot;test&quot;, &quot;screenshot&quot; }</code>
	 * might indicate, that the demo is being used for testing and for
	 * automatically generating screenshots.
	 */
	public String[] categories() default {};
}