package de.jave.jave.tool.dialog;

import javax.swing.JComponent;

/**
 * Inline options panel for a tool, rendered in the
 * {@link ToolSelectorBarOptionsHost} at the bottom of the tool selector bar.
 *
 * <p>Implementations may share a single {@link JComponent} across multiple
 * tools — the host removes the component from its previous parent and adds
 * it to itself on each tool change. Swing supports this re-parenting.
 */
public interface IInlineToolOptions {
   JComponent getContent();
}
